package com.esprim.student.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.esprim.student.entity.Department;
import com.esprim.student.entity.Student;
import com.esprim.student.repository.DepartmentRepository;
import com.esprim.student.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    public StudentService(StudentRepository studentRepository, DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Etudiant introuvable : " + id));
    }

    public Student create(String firstName, String lastName, String email, Long departmentId) {
        if (email == null || !email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) {
            throw new IllegalArgumentException("Adresse e-mail invalide : " + email);
        }
        Department department = null;
        if (departmentId != null) {
            department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Departement introuvable : " + departmentId));
        }
        Student student = new Student(firstName, lastName, email, department);
        return studentRepository.save(student);
    }

    public List<Department> findAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department createDepartment(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom du departement ne peut pas etre vide");
        }
        return departmentRepository.save(new Department(name));
    }
}
