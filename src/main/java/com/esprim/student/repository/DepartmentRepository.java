package com.esprim.student.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esprim.student.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);
}
