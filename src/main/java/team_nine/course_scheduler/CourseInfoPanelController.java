package team_nine.course_scheduler;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

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
    @FXML
    public void handleStudentSchedule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SchedulePanel.fxml"));
            Stage studentScheduleStage = new Stage();
            studentScheduleStage.setTitle("Student Schedule");
            studentScheduleStage.setScene(new Scene(loader.load()));
            SchedulePanelController controller = loader.getController();
            controller.initializeForStudent(studentListView.getSelectionModel().getSelectedItem());
            studentScheduleStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
