pipeline {
    agent any

    environment {
        IMAGE_NAME = 'mohamedbn01/student-management'
        IMAGE_TAG  = "${BUILD_NUMBER}"
        MAVEN_IMG  = 'maven:3.9-eclipse-temurin-17'
    }

    triggers {
        pollSCM('H/3 * * * *')
    }

    stages {
        stage('Commit') {
            steps {
                sh 'echo "=== Dernier commit ==="; git log -1 --pretty=fuller; git rev-parse HEAD'
            }
        }
        stage('Build') {
            steps {
                sh 'docker run --rm -v jenkins_home:/jh -v maven-cache:/root/.m2 -w /jh/workspace/${JOB_NAME} ${MAVEN_IMG} mvn -B clean package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
        stage('Test unitaire') {
            steps {
                sh 'docker run --rm -v jenkins_home:/jh -v maven-cache:/root/.m2 -w /jh/workspace/${JOB_NAME} ${MAVEN_IMG} mvn -B test'
                junit 'target/surefire-reports/*.xml'
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest .'
            }
        }
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_TOKEN')]) {
                    sh '''
                        set -e
                        echo "$DOCKER_TOKEN" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push "$IMAGE_NAME:$IMAGE_TAG"
                        docker push "$IMAGE_NAME:latest"
                    '''
                }
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
        }
        success {
            echo "Build #${env.BUILD_NUMBER} reussi : image ${env.IMAGE_NAME}:${env.IMAGE_TAG} publiee sur Docker Hub."
        }
        failure {
            echo "Build #${env.BUILD_NUMBER} en echec : aucune image n'a ete publiee."
        }
    }
}
