package pl.school.register.model;

import lombok.Data;
import pl.school.register.model.enumerations.WeekDay;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;


//I don't remember why it exists but apparently it is used in 30 places so I couldn't be bothered to fix all those places
@Entity
@Data
public class LessonBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "weekDay")
    @NotNull
    private WeekDay weekDay;
    @NotNull
    private String startTime;
    @NotNull
    @Min(0)
    private Integer duration;
    @ManyToOne
    @NotNull
    private Lesson lesson;


}
