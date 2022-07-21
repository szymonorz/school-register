package pl.school.register.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Teacher teacher;

    @ManyToOne
    @NotNull
    private SchoolClass schoolClass;

    @ManyToOne
    @NotNull
    private Subject subject;

    @OneToMany(mappedBy = "lesson")
    private Set<LessonBlock> lessonBlocks = new HashSet<>();

    @OneToMany(mappedBy = "lesson")
    private Set<Meeting> meetings = new HashSet<>();

    @OneToMany(mappedBy = "lesson")
    private Set<Mark> marks = new HashSet<>();
}
