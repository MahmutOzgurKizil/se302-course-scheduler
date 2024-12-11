package team_nine.course_scheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private TableView<Student> studentTableView;
    @FXML
    private TableColumn<Student, String> studentNameColumn;
    @FXML
    private Button studentsListButton;
    @FXML
    private Button lecturersListButton;
    @FXML
    private Button helpButton;
    @FXML
    private TableView courseTableView;
    @FXML
    private TableColumn courseName;
    @FXML
    private TableColumn courseStartTime;
    @FXML
    private TableColumn courseDuration;
    @FXML
    private TableColumn courseLecturer;
    @FXML
    private TableView classroomTableView;
    @FXML
    private TableColumn classroomNameColumn;
    @FXML
    private TableColumn classroomCapacityColumn;
    @FXML
    private MenuButton actionsMenuButton;
    @FXML
    private MenuItem assignClassroomMenuItem;
    @FXML
    private MenuItem importCsvMenuItem;
    @FXML
    private MenuItem addDeleteStudentMenuItem;
    @FXML
    private MenuItem addNewCourseMenuItem;
    @FXML
    private MenuItem automatically;
    @FXML
    private MenuItem optionally;



    @FXML
    public void initialize() {
        System.out.println("Initializing TableView...");

        Database.addStudent("ce212", new String[]{"Şerife", "Bahar"});

        Student[] students = Database.getAllStudents();

        if (students.length == 0) {
            System.out.println("No students returned from the database!");
        } else {
            for (Student student : students) {
                System.out.println("Student Name: " + student.getName());
            }
        }

        ObservableList<Student> observableStudentList = FXCollections.observableArrayList(students);

        //studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        if (studentNameColumn == null) {
            System.out.println("studentNameColumn is null!");
            return;
        }
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        studentTableView.setItems(observableStudentList);
    }

    @FXML
    public void StudentFillTableView() {
        System.out.println("Refreshing TableView...");
        initialize();
    }
    @FXML
    public void onStudentsListButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/team_nine/course_scheduler/StudentList.fxml"));
            Stage studentListStage = new Stage();
            studentListStage.setTitle("Student List");
            studentListStage.setScene(new Scene(loader.load()));
            studentListStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
