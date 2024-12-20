module team_nine.course_scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens team_nine.course_scheduler to javafx.fxml;
    exports team_nine.course_scheduler;
}