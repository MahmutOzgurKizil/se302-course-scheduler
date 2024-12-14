package team_nine.course_scheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class CourseInfoPanelController {
    @FXML
    private Text courseNameText;
    @FXML
    private TextArea courseDetailsTextArea;
    @FXML
    private ListView<String> studentListView;

    public void initialize(Course course){
        courseNameText.setText(course.getCourse());
        courseDetailsTextArea.setText("""
                Lecturer: %s
                Start Time: %s
                Duration: %s
                """.formatted(course.getLecturer(), course.getTime_to_start(), course.getDuration()));

        studentListView.setItems(FXCollections.observableArrayList(Database.getStudentsInCourse(course.getCourse())));
    }

}
