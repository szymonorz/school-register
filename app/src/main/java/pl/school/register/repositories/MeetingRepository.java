package pl.school.register.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.school.register.model.Meeting;
import pl.school.register.model.projections.MeetingInWeek;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query(value = "SELECT m.id , account.first_name AS teacherFirstName," +
                    " account.last_name AS teacherLastName," +
                    " m.time AS lessonStartTime,  " +
                    " m.room AS room, " +
                    " subject.subject_name AS subjectName, " +
                    " m.start_time AS startTime, " +
                    " m.week_day AS weekday FROM meeting m " +
                    "JOIN lesson ON m.lesson_id = lesson.id " +
                    "JOIN subject ON subject.id = lesson.subject_id " +
                    "JOIN account ON account.id = lesson.teacher_id " +
                    "WHERE m.teacher_id = :teacher_id AND lesson.school_class_id = :school_class_id AND lesson.subject_id = :subject_id " +
                    "AND m.time BETWEEN :start AND :end ORDER BY m.time", nativeQuery = true)
    List<MeetingInWeek> findAllTeacherMeetingsInWeek(@Param("teacher_id") Long teacher_id,
                                                 @Param("school_class_id") Long school_class_id,
                                                 @Param("subject_id") Long subject_id,
                                                 @Param("start") LocalDate start,
                                                 @Param("end") LocalDate end);

    @Query(value = "SELECT m.id, account.first_name AS teacherFirstName," +
            " account.last_name AS teacherLastName," +
            " m.time AS lessonStartTime,  " +
            " m.room AS room, " +
            " subject.subject_name AS subjectName, " +
            " m.start_time AS startTime, " +
            " m.week_day AS weekday " +
            "FROM meeting m " +
            "JOIN lesson ON m.lesson_id = lesson.id " +
            "JOIN subject ON subject.id = lesson.subject_id " +
            "JOIN account ON account.id = lesson.teacher_id " +
            "LEFT JOIN attendance ON m.id = attendance.meeting_id " +
            "WHERE lesson.school_class_id = :school_class_id " +
            "AND m.time BETWEEN :start AND :end ORDER BY m.time", nativeQuery = true)
    List<MeetingInWeek> findAllSchoolClassMeetingsInWeek(@Param("school_class_id") Long school_class_id,
                                                     @Param("start") LocalDate start,
                                                     @Param("end") LocalDate end);

}
