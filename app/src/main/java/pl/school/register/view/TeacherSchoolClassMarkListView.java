package pl.school.register.view;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import lombok.NoArgsConstructor;
import pl.school.register.model.*;
import pl.school.register.service.LessonService;
import pl.school.register.service.MarkService;
import pl.school.register.service.StudentService;
import pl.school.register.service.TeacherService;
import pl.school.register.view.components.RowSegment;
import pl.school.register.view.components.dialog.NewMarkDialog;
import pl.school.register.view.components.dialog.UpdateMarkDialog;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Route(value = "teacher/class/:classId/lesson/:lessonId/mark", layout = TeacherLayout.class)
public class TeacherSchoolClassMarkListView extends VerticalLayout implements BeforeEnterObserver {
    private StudentService studentService;
    private TeacherService teacherService;
    private MarkService markService;
    private LessonService lessonService;
    private Teacher teacher;
    private Lesson currentLesson;
    private Long classId, lessonId;
    public TeacherSchoolClassMarkListView(StudentService studentService,
                                          TeacherService teacherService,
                                          MarkService markService,
                                          LessonService lessonService){
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.lessonService = lessonService;
        this.markService = markService;
        teacher = teacherService.getById(1L).get();
    }
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        RouteParameters parameters = beforeEnterEvent.getRouteParameters();
        Optional<String> classIdOptional = parameters.get("classId");
        Optional<String> lessonIdOptional = parameters.get("lessonId");

        if (classIdOptional.isPresent() && lessonIdOptional.isPresent()) {
            classId = Long.parseLong(classIdOptional.get());
            lessonId = Long.parseLong(lessonIdOptional.get());
            currentLesson = lessonService.getById(lessonId).get();
            drawLayout();
        }
    }

    private void drawLayout(){
        removeAll();
        List<Mark> teacherMarks = markService.getAllByTeacherIdAndLessonId(1L, lessonId);
        List<String> labels = teacherMarks
                .stream()
                .filter( distinctByKey(Mark::getLabel) )
                .map(Mark::getLabel)
                .collect(Collectors.toList());
        List<Student> students = studentService.getAllBySchoolClassId(classId);
        add(new StudentRow(labels));
        students.forEach(s ->{
            List<Mark> marks = markService.getAllByStudentIdAndLessonId(s.getId(), lessonId);
            add(new StudentRow(s, marks, labels));
        });
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor){
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Tag("tr")
    private class StudentRow extends Div {
        public StudentRow(Student student, List<Mark> marks, List<String> columns){
            Deque<Mark> marksDeque = new ArrayDeque(marks);
            String fullName = String.format("%s %s", student.getFirstName(), student.getLastName());
            add(new StudentListSegment(student.getIndex()));
            add(new StudentListSegment(fullName));
            for (String column : columns) {
                if (!marksDeque.isEmpty()) {
                    Mark mark = marksDeque.peek();
                    if (column.equals(mark.getLabel())) {
                        add(new StudentListMarkSegment(mark));
                        marksDeque.pop();
                        continue;
                    }
                }
                add(new StudentListMarkSegment());
            }
            add(new StudentListMarkSegment(student));

        }

        public StudentRow(List<String> labels){
            add(new StudentListSegment("Index"));
            add(new StudentListSegment("Student"));
            labels.forEach(label -> add(new StudentListSegment(label)));
        }
    }

    @Tag("td")
    @NoArgsConstructor
    private class StudentListSegment extends RowSegment{
        public StudentListSegment(String  studentNameString){
            Label name = new Label(studentNameString);
            add(name);
        }

        public StudentListSegment(Long index){
            Label idx = new Label(index.toString());
            add(idx);
        }
    }

    private class StudentListMarkSegment extends RowSegment {
        public StudentListMarkSegment(Mark mark){
            Label markLabel = new Label(mark.getValue().toString());
            add(markLabel);
            Button edit = new Button(new Icon(VaadinIcon.EDIT));
            edit.addClickListener(buttonClickEvent -> {
                UpdateMarkDialog umd = new UpdateMarkDialog();
                umd.setData(mark);
                umd.setOnConfirmAction(listener -> {
                    markService.update(mark);
                    drawLayout();
                    umd.close();
                });
                umd.build();
                umd.open();
            });
            Button delete = new Button(new Icon(VaadinIcon.DEL));
            delete.addClickListener(buttonClickEvent -> {
                markService.deleteById(mark.getId());
                drawLayout();
            });
            add(edit, delete);
        }

        public StudentListMarkSegment(){
            Button addNewExisting = new Button(new Icon(VaadinIcon.PLUS));
            add(addNewExisting);
        }

        public StudentListMarkSegment(Student student){
            Button addNewMarkButton = new Button(new Icon(VaadinIcon.PLUS));
            addNewMarkButton.addClickListener(listener -> {
                NewMarkDialog newMarkDialog = new NewMarkDialog();
                Mark mark = new Mark();
                mark.setStudent(student);
                mark.setTeacher(teacher);
                mark.setLesson(currentLesson);
                newMarkDialog.setData(mark);
                newMarkDialog.setOnConfirmAction(nlistener -> {
                    markService.update(mark);
                    drawLayout();
                    newMarkDialog.close();
                });
                newMarkDialog.build();
                newMarkDialog.open();
            });
            add(addNewMarkButton);
        }
    }

}
