package pl.school.register.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.school.register.model.Student;
import pl.school.register.model.Student_Class;
import pl.school.register.repositories.StudentRepository;
import pl.school.register.repositories.Student_ClassRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    public final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public void addStudent(Student student){
        studentRepository.save(student);
    }

    public List<Student> getStudent_Classes(){
        return studentRepository.findAll();
    }

    public Optional<Student> getStudent_ClassById(Long id){
        return studentRepository.findById(id);
    }
}