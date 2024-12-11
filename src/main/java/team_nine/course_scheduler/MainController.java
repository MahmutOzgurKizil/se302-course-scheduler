package team_nine.course_scheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {
    @FXML

    private TableView<Student> studentTableView;
    @FXML
    private TableColumn<Student, String> studentNameColumn;
    @FXML
    public void StudentFillTableView(){
        Database.addStudent("ce212", new String[]{"Şeriffe"});
        Student[] students = Database.getAllStudents();
        if (students.length == 0) {
            System.out.println("No students returned from the database!");
        } else {
            for (Student student : students) {
                System.out.println("Student Name: " + student.getName());
            }
        }
        ObservableList<Student> observableStudentList = FXCollections.observableArrayList(students);
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentTableView.setItems(observableStudentList);
    }
}
