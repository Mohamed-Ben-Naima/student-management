package com.esprim.student.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esprim.student.entity.Department;
import com.esprim.student.service.StudentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final StudentService studentService;

    public DepartmentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Department> create(@RequestBody Department department) {
        Department created = studentService.createDepartment(department.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Department> list() {
        return studentService.findAllDepartments();
    }

    @GetMapping("/{id}")
    public Department get(@PathVariable Long id) {
        return studentService.findAllDepartments().stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Departement introuvable : " + id));
    }
}
