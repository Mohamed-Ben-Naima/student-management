package com.esprim.student.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.esprim.student.entity.Department;
import com.esprim.student.entity.Student;
import com.esprim.student.repository.DepartmentRepository;
import com.esprim.student.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void createSavesStudentWhenEmailIsValid() {
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student saved = studentService.create("Amina", "Benali", "amina.benali@esprim.ma", null);

        assertThat(saved.getFirstName()).isEqualTo("Amina");
        assertThat(saved.getFullName()).isEqualTo("Amine Benali FAUX");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void createRejectsInvalidEmail() {
        assertThatThrownBy(() -> studentService.create("Amina", "Benali", "pas-un-email", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("invalide");

        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void createLinksDepartmentWhenProvided() {
        Department informatique = new Department("Informatique");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(informatique));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student saved = studentService.create("Youssef", "Alaoui", "youssef.alaoui@esprim.ma", 1L);

        assertThat(saved.getDepartment()).isNotNull();
        assertThat(saved.getDepartment().getName()).isEqualTo("Informatique");
    }

    @Test
    void createDepartmentRejectsBlankName() {
        assertThatThrownBy(() -> studentService.createDepartment("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("vide");
    }
}
