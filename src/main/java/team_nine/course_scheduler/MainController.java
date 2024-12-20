package team_nine.course_scheduler;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalTime;
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
    private TableView<Classroom> classroomTableView;
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
    private ObservableList<Course> courses = FXCollections.observableArrayList();
    private Course selectedCourse;
    private ObservableList<Student> allStudents;
    private ObservableList<Student> filteredStudents;
    @FXML
    private ChoiceBox<Classroom> selectclassroomSwitchChoiceBox;
    @FXML
    private ChoiceBox<Course> selectcourseSwitchChoiceBox;
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

            courses = FXCollections.observableArrayList(Database.getAllCourses());
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

        if (lecturerNameColumn == null) {
            System.out.println("lecturerNameColumn is null!");
            return;
        }
        lecturerNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        lecturerTableView.setItems(observableLecturerList);
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
    public void handleClassroomSchedule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SchedulePanel.fxml"));
            Stage lecturerScheduleStage = new Stage();
            lecturerScheduleStage.setTitle("Classroom Schedule");
            lecturerScheduleStage.setScene(new Scene(loader.load()));
            SchedulePanelController controller = loader.getController();
            controller.initializeForClassroom(classroomTableView.getSelectionModel().getSelectedItem().getClassroom());
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

        Text titleText = new Text("Help Instructions\n\n");
        titleText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Main instructions text with bolded subtitles
        Text instructionsText = new Text("Course Scheduler\n\n");
        instructionsText.setStyle("-fx-font-weight: bold;");

        Text descriptionText = new Text("""
            This guide will help you navigate the key features of the program, including creating courses, importing sets of classes and courses, assigning courses to classrooms, and managing course-classroom relations.

            """);

        Text operationsTitle = new Text("Operations\n\n");
        operationsTitle.setStyle("-fx-font-weight: bold;");

        Text operationsText = new Text("""
            - Create a course: Click “Add New Course” under the “Actions” menu. Enter details such as date/time, name, lecturer, classroom, duration, and students for the course.

            - Import CSV file: Select “Import Courses” or “Import Classrooms” to load data from an existing CSV file. Navigate to the desired file path and choose the CSV file to import classrooms or courses.

            - Add/Delete Students: Use “Add/Delete Student” to modify the list of students for a course. Select the course, choose the relevant students, and click “Add” or “Delete.”

            - Assign classrooms automatically: Click “Assign Classroom” to allocate free courses to available classrooms automatically.

            - Assign classrooms manually: Click “Assign Classroom,” select a course and a classroom, and click “Allocate.” The program will allocate the course if the classroom is available and has enough capacity.
            
            """);

        Text searchTitle = new Text("Searching and Filtering\n\n");
        searchTitle.setStyle("-fx-font-weight: bold;");

        Text searchText = new Text("""
            - Students List: Click “Students” to view the list of students. Select a student to see their weekly schedule.

            - Lecturers List: Click “Lecturers” to view the list of lecturers. Select a lecturer to see their weekly schedule.

            - Courses List: The main menu displays the list of courses. Click a course to view details such as Lecturer, Start Time, Duration, Classroom, and the student list.

            - Classrooms List: The main menu displays the list of classrooms, including their names and capacities. Click a classroom to view its schedule.
            
            """);
        Text linkText = new Text("For more information, visit ");
        Hyperlink wikiLink = new Hyperlink("here.");
        wikiLink.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        wikiLink.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/MahmutOzgurKizil/se302-course-scheduler/wiki"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        // Add all text elements to the TextFlow
        textFlow.getChildren().addAll(titleText, instructionsText, descriptionText, operationsTitle, operationsText, searchTitle, searchText, linkText, wikiLink);

        textFlow.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(textFlow);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(400, 300);
        scrollPane.setVvalue(1.0);

        Scene helpScene = new Scene(scrollPane);
        helpStage.setScene(helpScene);

        helpStage.setOnShown(e -> {
            Platform.runLater(() -> {
                scrollPane.setVvalue(0);
                textFlow.requestFocus();
            });
        });

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
                    String[] times = {"8:30", "9:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55", "15:50", "16:45", "17:40", "18:35", "19:30", "20:25", "21:20", "22:15"};
                    controller.dayChoiceBox.setItems(FXCollections.observableArrayList(days));
                    controller.timeChoiceBox.setItems(FXCollections.observableArrayList(times));
                    controller.addCourseStudentList.setCellFactory(param -> new CheckBoxListCell<>(controller::createCheckBox));
                    controller.addCourseStudentList.setItems(FXCollections.observableArrayList(Database.getAllStudents()));
                    controller.classroomChoiceBox.setItems(FXCollections.observableArrayList(Database.getAllClassrooms()));

                }
            });
            addCourseStage.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load Add New Course window");
            alert.setContentText("There was an error opening the Add New Course window.");
            alert.showAndWait();
        }
        courses = FXCollections.observableArrayList(Database.getAllCourses());
        courseTableView.setItems(courses);
    }

    private ObservableValue<Boolean> createCheckBox(Student student) {
        SimpleBooleanProperty selected = new SimpleBooleanProperty(selectedStudents.contains(student));
        selected.addListener((obs, wasSelected, isNowSelected) -> handleCheckBoxSelection(student, isNowSelected));
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
    public void onAddCourseClick(ActionEvent event) {
        Student[] students = selectedStudents.toArray(new Student[0]);
        String courseName = "";
        String day = "";
        String time = "";
        String lecturer = "";
        String classroom = "";
        int hour = courseHourSpinner.getValue();

        if (classroomChoiceBox.getValue() != null) {
            courseName = courseNameTextField.getText();
            day = dayChoiceBox.getValue();
            time = timeChoiceBox.getValue();
            lecturer = courseLecturerTextField.getText();
            classroom = classroomChoiceBox.getValue().getClassroom();

        }

        if (courseName.isEmpty()||day.isEmpty()||time.isEmpty()||lecturer.isEmpty()||students.length == 0||classroom.isEmpty()) {
            showErrorMessage("All fields must be filled in.");
            return;
        }else if (Database.getCourse(courseName) != null) {
            showErrorMessage("Course with the same name already exists.");
            return;
        }
        else {
            Course newCourse = new Course(courseName,day.concat(" "+time),lecturer,hour);

            for (Student student : students) {
                Course[] studentCourses = Database.getCoursesForStudentAllInfo(student.getName());
                if(doSchedulesConflict(student.getName(), newCourse, studentCourses)) return;
            }
            {
                Course[] lecturerCourses = Database.getCoursesForLecturerAllInfo(lecturer);
                if (doSchedulesConflict(lecturer, newCourse, lecturerCourses)) return;

                Course[] classroomCourses = Database.getCoursesForClassroomAllInfo(classroom);
                if (doSchedulesConflict(classroom, newCourse, classroomCourses)) return;
            }

            LocalTime latestEndTime = LocalTime.parse("23:11");
            LocalTime morningStartTime = LocalTime.parse("08:30");
            LocalTime courseStartTime = LocalTime.parse(zeroPad(newCourse.getTime_to_start().split(" ")[1]));
            LocalTime courseEndTime = courseStartTime.plusMinutes(55 * newCourse.getDuration());
                if ( courseEndTime.isAfter(latestEndTime)||
                        courseEndTime.isBefore(morningStartTime)) {
                    showErrorMessage("Course ends after 23:00.");
                    return;
                }

        }

        // ADD COURSE TO DATABASE
        String[] studentNames = new String[students.length];
        for (int i = 0; i < students.length; i++) {
            studentNames[i] = students[i].getName();
        }
        Database.addCourse(courseName, day.concat(" "+time), hour,lecturer, studentNames);
        Database.matchClassroom(courseName,classroom);
        showSuccessMessage("Course added successfully!");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    private boolean doSchedulesConflict(String classroom, Course newCourse, Course[] classroomCourses) {
        for (Course classroomCourse : classroomCourses) {
            String newCourseDay = newCourse.getTime_to_start().split(" ")[0];
            String classroomCourseDay = classroomCourse.getTime_to_start().split(" ")[0];

            if (newCourseDay.equals(classroomCourseDay)) {
                LocalTime courseStartTime = LocalTime.parse(zeroPad(newCourse.getTime_to_start().split(" ")[1]));
                LocalTime classroomCourseStartTime = LocalTime.parse(zeroPad(classroomCourse.getTime_to_start().split(" ")[1]));
                if (doCoursesConflict(courseStartTime, newCourse.getDuration(), classroomCourseStartTime, classroomCourse.getDuration())) {
                    showErrorMessage("Course conflicts with %s's schedule.".formatted(classroom));
                    return true;
                }
            }
        }
        return false;
    }

    private static String zeroPad(String time) {
        String[] parts = time.split(":");
        String hour = parts[0].length() == 1 ? "0" + parts[0] : parts[0]; // Add leading zero if needed
        return hour + ":" + parts[1];
    }

    private static boolean doCoursesConflict(LocalTime start1, int durationMultiplier1, LocalTime start2, int durationMultiplier2) {
        int slotDuration = 55;
        LocalTime end1 = start1.plusMinutes(slotDuration * durationMultiplier1);
        LocalTime end2 = start2.plusMinutes(slotDuration * durationMultiplier2);
        return start1.isBefore(end2) && start2.isBefore(end1);
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
        selectedStudents.clear();
        String[] studentNames = Database.getStudentsInCourse(course.getCourse());
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
                Course[] studentCourses = Database.getCoursesForStudentAllInfo(student.getName());
                if (doSchedulesConflict(student.getName(), selectedCourse, studentCourses)){
                    showErrorMessage("Course conflicts with %s's schedule.".formatted(student.getName()));
                    return;
                }
            }
            selectedCourse.addStudents(selectedStudents);
            updateStudentListForCourse(selectedCourse);
        } else {
            System.out.println("No students selected to add.");
        }
    }

    @FXML
    private void deleteStudentsFromCourse(ActionEvent event) {
        int i = 0;
        Course selectedCourse = courseChoiceBox.getValue();
        if (selectedCourse != null && !selectedStudents.isEmpty()) {
            try {
                for (Student student : selectedStudents) {
                    System.out.println("Deleting student: " + student.getName() + " from course: " + selectedCourse.getCourse());
                    selectedCourse.removeStudents(selectedStudents.get(i)); // Remove the student
                    i++;
                }
                // Update the UI to reflect changes
                updateStudentListForCourse(selectedCourse);
                showSuccessMessage("Selected students have been removed from the course successfully!");
                selectedStudents.clear();

            } catch (Exception e) {
                // Handle any exceptions that might occur
                showErrorMessage("An error occurred while deleting students from the course.");
                e.printStackTrace();
            }
        } else {
            // Handle case when no students or no course is selected
            showErrorMessage("No course or students selected for deletion.");
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
    @FXML
    private void allocateCourseAndClassroom() {
      Course selectedCourse = selectcourseSwitchChoiceBox.getValue();
      Classroom selectedClassroom = selectclassroomSwitchChoiceBox.getValue();
        if(selectedClassroom == null){
            System.out.println("Please select a classroom");

            return;
        }
        if(selectedCourse == null){
            System.out.println("please select a course");
            return;
        }

        if (!selectedCourse.getCourse().isEmpty() && !selectedClassroom.getClassroom().isEmpty()) {
            try{
                selectedCourse.manualAssign(selectedCourse, selectedClassroom);
                showSuccessMessage("The course was successfully assigned to the classroom");
            }
            catch (Exception e) {
                showErrorMessage("Error while matching the course to the classroom(Classroom might be busy at that time or the capacity might not be enough.");
            }
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
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));


            stage.showingProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    MainController controller = loader.getController();
                    controller.selectclassroomSwitchChoiceBox.setItems(FXCollections.observableArrayList(Database.getAllClassrooms()));
                    controller.selectcourseSwitchChoiceBox.setItems(FXCollections.observableArrayList(Database.getAllCourses()));

                }
            });


            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void importCoursesFromCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Select Courses CSV File");
        File file = fileChooser.showOpenDialog(null);
        try {
            Database.populateCourses(file);
        }catch (Exception e){
            showErrorMessage("Selected file is not a valid CSV file.");
        }
        initialize();
    }

    @FXML
    private void importClassroomsFromCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Select Classrooms CSV File");
        File file = fileChooser.showOpenDialog(null);
        try {
            Database.populateClassrooms(file);
        }catch (Exception e){
            showErrorMessage("Selected file is not a valid CSV file.");
        }

        initialize();
    }

    @FXML
    private void handleAutoAssign(){
        Course[] courses = Database.getAllCourses();
        for (Course course : courses) {
            course.autoAssign(course);
        }
    }

    @FXML
    private void allocationWindowCancel(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelButtonClick() {
        selectcourseSwitchChoiceBox.getSelectionModel().clearSelection();
        selectclassroomSwitchChoiceBox.getSelectionModel().clearSelection();

    }



}
