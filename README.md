# student-management

Application Spring Boot de gestion d'étudiants (départements + étudiants), conteneurisée et livrée en continu par Jenkins.

## Stack

- Java 17 — Spring Boot 3.2.5 — MySQL 8
- Contexte API : `/api` (départements, étudiants)
- Port dans le conteneur : `8080` (publié sur `8089` côté hôte dans la démo)

## Conteneurisation

- `Dockerfile` : image `eclipse-temurin:17-jre`, jar copié en `/app/app.jar`, `ENTRYPOINT ["java", "-jar", "app.jar"]`
- `.dockerignore` : tout exclure (`*`) puis réinclure uniquement `!target/*.jar`
- Réseau `sm-net`, conteneurs `mysql-db` et `student-app`, volume `sm-mysql-data`

## Pipeline Jenkins (5 stages)

`Commit` → `Build` → `Test unitaire` → `Docker Build` → `Docker Push`

- Déclenchement : `pollSCM('H/3 * * * *')` — chaque `git push` suffit
- Tag publié : `mohamedbn01/student-management:${BUILD_NUMBER}` + `latest`
- Credential Jenkins : `dockerhub-credentials` (login via `--password-stdin`, token jamais journalisé)

## Démonstration

1. `git push origin main`
2. Jenkins se déclenche seul et passe les 5 stages au vert
3. Un nouveau tag numéroté apparaît sur Docker Hub
4. `docker run -d --name student-app --network sm-net -p 8089:8080 mohamedbn01/student-management:<tag>` répond sur `http://localhost:8089/api/students`
