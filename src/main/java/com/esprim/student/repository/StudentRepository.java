package com.esprim.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esprim.student.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByDepartmentId(Long departmentId);
}
