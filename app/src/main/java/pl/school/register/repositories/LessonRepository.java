package pl.school.register.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.school.register.model.Lesson;
import pl.school.register.model.LessonBlock;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllBySchoolClassId(@Param("school_class_id") Long school_class_id);

    List<Lesson> findAllBySubjectId(@Param("subject_id") Long subject_id);

    List<Lesson> findAllByTeacherId(Long teacher_id);
}