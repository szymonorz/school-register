package pl.school.register.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Length(max = 255)
    private String className;

    @NotNull
    @NotBlank
    @Length(max = 255)
    private String profile;

    @OneToOne
    @NotNull
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Teacher homeroomTeacher;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "schoolClass")
    @EqualsAndHashCode.Exclude
    private Set<Lesson> lessons = new HashSet<>();

    @OneToMany(mappedBy = "schoolClass")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Student> students = new HashSet<>();
}
