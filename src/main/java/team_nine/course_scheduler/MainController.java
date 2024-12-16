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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private Spinner<Integer> courseHourSpinner;
    @FXML
    private ChoiceBox<String> dayChoiceBox;
    @FXML
    private ChoiceBox<String> timeChoiceBox;
    @FXML
    private ListView<Student> addCourseStudentList;
    private ArrayList<Student> selectedStudents = new ArrayList<>();
    @FXML
    private ChoiceBox<Classroom> classroomChoiceBox;
    @FXML
    private ListView<Student> searchStudentListView;
    @FXML
    private Button addStudentButton;
    @FXML
    private Button deleteStudentButton;
    @FXML
    private TextField searchStudentField;
    @FXML
    private Button searchStudentButton;
    @FXML
    private List<Student> selectedStudentsforAddDelete;
    @FXML
    private ChoiceBox<Course> courseChoiceBox;
    private ObservableList<Course> courses;
    private Course selectedCourse;
    private ObservableList<Student> allStudents;
    private ObservableList<Student> filteredStudents;
    @FXML
    private ChoiceBox<String> selectclassroomSwitchChoiceBox;
    @FXML
    private ChoiceBox<String> selectcourseSwitchChoiceBox;
    @FXML
    private Button allocateButton;
    @FXML
    private Button cancelButton;
    private ObservableList<String> courseList = FXCollections.observableArrayList();
    private ObservableList<String> classroomList = FXCollections.observableArrayList();
    @FXML
    private MenuItem optionallyAllocationWindowMenuItem;




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
                    controller.classroomChoiceBox.setItems(FXCollections.observableArrayList(Database.getAllClassrooms()));

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
        String classroom = classroomChoiceBox.getValue().getClassroom();
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
    @FXML
    public void initializeAddDeleteStudent() {
        setupCourseChoiceBox();
        setupStudentListView();
        setupSearchFeature();

    }

    private void setupCourseChoiceBox() {
        ObservableList<Course> courses = FXCollections.observableArrayList(Database.getAllCourses());
        courseChoiceBox.setItems(courses);

        courseChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldCourse, newCourse) -> {
            if (newCourse != null) {
                updateStudentListForCourse(newCourse);
            }
        });

        if (!courses.isEmpty()) {
            courseChoiceBox.getSelectionModel().select(0);
            updateStudentListForCourse(courses.get(0));
        }
    }

    private void setupStudentListView() {
        selectedStudents = new ArrayList<>();
        searchStudentListView.setCellFactory(param -> new CheckBoxListCell<>(this::createCheckBox));
    }

    private void setupSearchFeature() {
        searchStudentButton.setOnAction(event -> filterStudentList());
        searchStudentField.textProperty().addListener((obs, oldText, newText) -> filterStudentList()); // Filter on text change
    }


    private void updateStudentListForCourse(Course course) {
        String[] studentNames = course.getStudents().split(",\\s*");
        allStudents = FXCollections.observableArrayList(Database.getAllStudents());

        for (String name : studentNames) {
            allStudents.add(new Student(name));
        }

        filteredStudents = FXCollections.observableArrayList(allStudents);
        searchStudentListView.setItems(filteredStudents);
    }

    private ObservableValue<Boolean> createCheckBoxforStudent(Student student) {
        SimpleBooleanProperty selected = new SimpleBooleanProperty();
        selected.addListener((obs, wasSelected, isNowSelected) -> handleCheckBoxSelection(student, isNowSelected));
        return selected;
    }

    private void handleCheckBoxSelectionforStudent(Student student, boolean isSelected) {
        if (isSelected) {
            selectedStudents.add(student);
        } else {
            selectedStudents.remove(student);
        }
    }

    private void filterStudentList() {
        String query = searchStudentField.getText().toLowerCase();
        if (query.isEmpty()) {
            filteredStudents.setAll(allStudents);
        } else {
            filteredStudents.setAll(allStudents.filtered(student -> student.getName().toLowerCase().contains(query)));
        }
    }

    @FXML
    private void addStudentsToCourse() {
        Course selectedCourse = courseChoiceBox.getValue();
        if (selectedCourse != null && !selectedStudents.isEmpty()) {
            for (Student student : selectedStudents) {
                System.out.println("Adding student: " + student.getName() + " to course: " + courseChoiceBox.getValue());
            }
        } else {
            System.out.println("No students selected to add.");
        }
    }

    @FXML
    private void deleteStudentsFromCourse() {
        Course selectedCourse = courseChoiceBox.getValue();
        if (selectedCourse != null && !selectedStudents.isEmpty()) {
            for (Student student : selectedStudents) {
                System.out.println("Deleting student: " + student.getName() + " from course: " + courseChoiceBox.getValue());
            }
        } else {
            System.out.println("No students selected to delete.");
        }
    }
    @FXML
    public void onaddDeleteStudentButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/team_nine/course_scheduler/AddDeleteStudents.fxml"));
            Stage stage = new Stage();
           //
            stage.setScene(new Scene(loader.load()));

            MainController controller = loader.getController();
            controller.initializeAddDeleteStudent();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Unable to open the this window.");
        }
    }

    public void initializeAllocateClass() {
        selectcourseSwitchChoiceBox.setItems(courseList);
        selectclassroomSwitchChoiceBox.setItems(classroomList);

        selectcourseSwitchChoiceBox.getSelectionModel().selectFirst();
        selectclassroomSwitchChoiceBox.getSelectionModel().selectFirst();

        allocateButton.setOnAction(event -> allocateCourseAndClassroom());
    }

    private void allocateCourseAndClassroom() {
        String selectedCourse = selectcourseSwitchChoiceBox.getValue();
        String selectedClassroom = selectclassroomSwitchChoiceBox.getValue();

        if (selectedCourse != null && selectedClassroom != null) {
            System.out.println("Allocated " + selectedCourse + " to " + selectedClassroom);
        } else {
            System.out.println("Please select both a course and a classroom.");
        }
    }


    public void initializeOptionally() {
        optionallyAllocationWindowMenuItem.setOnAction(event -> openAllocationWindow());
    }

    @FXML
    private void openAllocationWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/team_nine/course_scheduler/AllocateClassroom.fxml"));
            AnchorPane allocationWindow = loader.load();

            Stage allocationStage = new Stage();
            //allocationStage.setTitle("Allocate Course and Classroom");

            Scene allocationScene = new Scene(allocationWindow);
            allocationStage.setScene(allocationScene);

            allocationStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
