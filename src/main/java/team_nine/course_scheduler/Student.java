package team_nine.course_scheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Student {
    private String name;

    //Student Constructor
    public Student(String name){this.name=name;}

    //Setter & Getter
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
