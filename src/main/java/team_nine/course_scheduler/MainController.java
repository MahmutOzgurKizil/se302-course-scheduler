package team_nine.course_scheduler;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

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
    private TableView <String>lecturerTableView;
    @FXML
    private TableColumn <String, String> lecturerNameColumn;
    @FXML
    private Button addCourseButton;
    @FXML
    private TextField courseNameTextField;
    @FXML
    private TextField courseLecturerTextField;
    @FXML
    private TextField courseStudentsTextField;
    @FXML
    private TextField courseClassroomTextField;
    @FXML
    private Spinner<Integer> courseHourSpinner;
    @FXML
    private ChoiceBox<String> dayChoiceBox;
    @FXML
    private ChoiceBox<String> timeChoiceBox;
    @FXML
    private ListView<Student> addCourseStudentList;
    private ArrayList<Student> selectedStudents = new ArrayList<>();

    @FXML
    public void initialize() {
        try {
            courseName.setCellValueFactory(new PropertyValueFactory<>("course"));
            courseStartTime.setCellValueFactory(new PropertyValueFactory<>("time_to_start"));
            courseDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
            courseLecturer.setCellValueFactory(new PropertyValueFactory<>("lecturer"));

            ObservableList<Course> courses = FXCollections.observableArrayList(Database.getAllCourses());
            courseTableView.setItems(courses);

            ObservableList<Classroom> classrooms = FXCollections.observableArrayList(Database.getAllClassrooms());
            classroomNameColumn.setCellValueFactory(new PropertyValueFactory<>("classroom"));
            classroomCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
            classroomTableView.setItems(classrooms);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    public void openCourseInfo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseInfoPanel.fxml"));
            Stage courseInfoStage = new Stage();
            courseInfoStage.setTitle("Course Info");
            courseInfoStage.setScene(new Scene(loader.load()));


            CourseInfoPanelController controller = loader.getController();
            controller.initialize((Course) courseTableView.getSelectionModel().getSelectedItem());

            courseInfoStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initializeStudentList() {
        System.out.println("Initializing TableView...");

        Student[] students = Database.getAllStudents();

        if (students.length == 0) {
            System.out.println("No students returned from the database!");
        } else {
            for (Student student : students) {
                System.out.println("Student Name: " + student.getName());
            }
        }

        ObservableList<Student> observableStudentList = FXCollections.observableArrayList(students);

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
        initializeStudentList();
    }
    @FXML
    public void onStudentsListButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/team_nine/course_scheduler/StudentList.fxml"));
            Stage studentListStage = new Stage();
            studentListStage.setTitle("Student List");
            studentListStage.setScene(new Scene(loader.load()));

            studentListStage.showingProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    // Call the method to initialize the table
                    MainController controller = loader.getController();
                    controller.initializeStudentList();
                }
            });

            studentListStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleStudentSchedule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SchedulePanel.fxml"));
            Stage studentScheduleStage = new Stage();
            studentScheduleStage.setTitle("Student Schedule");
            studentScheduleStage.setScene(new Scene(loader.load()));
            SchedulePanelController controller = loader.getController();
            controller.initializeForStudent(studentTableView.getSelectionModel().getSelectedItem().getName());
            studentScheduleStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeLecturerList() {
        String[] lecturerNames = Database.getAllLecturers();
        ObservableList<String> observableLecturerList = FXCollections.observableArrayList(lecturerNames);

        /*if (lecturerNames.length == 0) {
            System.out.println("No lecturers returned from the database!");
        } else {
            for (String name : lecturerNames) {
                System.out.println("Lecturer Name: " + name);
            }
        }*/

        if (lecturerNameColumn == null) {
            System.out.println("lecturerNameColumn is null!");
            return;
        }
        lecturerNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        lecturerTableView.setItems(observableLecturerList);
    }
    @FXML
    public void lecturerFillTableView() {
        initializeLecturerList();
    }

    @FXML
    public void handleLecturerSchedule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SchedulePanel.fxml"));
            Stage lecturerScheduleStage = new Stage();
            lecturerScheduleStage.setTitle("Lecturer Schedule");
            lecturerScheduleStage.setScene(new Scene(loader.load()));
            SchedulePanelController controller = loader.getController();
            controller.initializeForLecturer(lecturerTableView.getSelectionModel().getSelectedItem());
            lecturerScheduleStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLecturersListButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/team_nine/course_scheduler/LecturersList.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Lecturer List");
            stage.setScene(new Scene(loader.load()));

            stage.showingProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    // Call the method to initialize the table
                    MainController controller = loader.getController();
                    controller.initializeLecturerList();
                }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void onHelpButtonClick(ActionEvent event) {
        Stage helpStage = new Stage();
        helpStage.setTitle("Help - Instructions");

        TextFlow textFlow = new TextFlow();

        Text titleText = new Text("Help Instructions\n");
        titleText.setStyle("-fx-font-size: 16px;-fx-font-weight:bold;");
        Text instructionsText = new Text("EMPTY\n");
        textFlow.getChildren().addAll(titleText, instructionsText);

        ScrollPane scrollPane = new ScrollPane(textFlow);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(400, 300);

        Scene helpScene = new Scene(scrollPane);
        helpStage.setScene(helpScene);
        helpStage.show();
    }

    @FXML
    public void onAddNewCourseClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/team_nine/course_scheduler/Create Course.fxml"));
            Stage addCourseStage = new Stage();
            addCourseStage.setTitle("Add New Course");
            addCourseStage.setScene(new Scene(loader.load()));
            addCourseStage.showingProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    // Call the method to initialize the table
                    MainController controller = loader.getController();
                    controller.courseHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8,1));
                    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
                    String[] times = {"8:30", "9:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55", "15:50", "16:45", "17:40", "18:35", "19:30"};
                    controller.dayChoiceBox.setItems(FXCollections.observableArrayList(days));
                    controller.timeChoiceBox.setItems(FXCollections.observableArrayList(times));
                    controller.addCourseStudentList.setCellFactory(param -> new CheckBoxListCell<>(controller::createCheckBox));
                    controller.addCourseStudentList.setItems(FXCollections.observableArrayList(Database.getAllStudents()));

                }
            });
            addCourseStage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load Add New Course window");
            alert.setContentText("There was an error opening the Add New Course window.");
            alert.showAndWait();
        }
    }


    private ObservableValue<Boolean> createCheckBox(Student student) {
        SimpleBooleanProperty selected = new SimpleBooleanProperty();
        selected.addListener((obs, wasSelected, isNowSelected) -> handleCheckBoxSelection(student, isNowSelected));
        CheckBox checkBox = new CheckBox(student.getName());
        checkBox.selectedProperty().bindBidirectional(selected);
        return selected;
    }

    private void handleCheckBoxSelection(Student student, boolean isSelected) {
        if (isSelected) {
            selectedStudents.add(student); // Add student to selected list
            System.out.println(selectedStudents);
        } else {
            selectedStudents.remove(student); // Remove student from selected list
            System.out.println(selectedStudents);
        }
    }

    @FXML
    public void onAddCourseClick() {
        Student[] students = selectedStudents.toArray(new Student[0]);

        String courseName = courseNameTextField.getText();
        String day = dayChoiceBox.getValue();
        String time = timeChoiceBox.getValue();
        String lecturer = courseLecturerTextField.getText();
        String classroom = courseClassroomTextField.getText();
        int hour = courseHourSpinner.getValue();

        if (courseName.isEmpty()||day.isEmpty()||time.isEmpty()||lecturer.isEmpty()||students.length == 0||classroom.isEmpty()) {
            showErrorMessage("All fields must be filled in.");

        } else {
            //Course newCourse = new Course(courseName, dateTime, lecturer, students, classroom, hour);
            showSuccessMessage("Course added successfully!");
            // save it in a database
            // addCourseToList(newCourse);
        }
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Course Added");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
