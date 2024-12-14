package team_nine.course_scheduler;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class SchedulePanelController {
    @FXML
    private Text scheduleTitleText;
    @FXML
    private GridPane gridPane;

    private enum Day {
        MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5);

        private final int value;

        Day(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    private enum Hour {
        EIGHT_THIRTY(1, "08:30"), NINE_TWENTY_FIVE(2, "09:25"), TEN_TWENTY(3, "10:20"), ELEVEN_FIFTEEN(4, "11:15"),
        TWELVE_TEN(5, "12:10"), ONE_FIVE(6, "13:05"), TWO(7, "14:00"), TWO_FIFTY_FIVE(8, "14:55"), THREE_FIFTY(9, "15:50"),
        FOUR_FORTY_FIVE(10, "16:45"), FIVE_FORTY(11, "17:40"), SIX_THIRTY_FIVE(12, "18:35"), SEVEN_THIRTY(13, "19:30");

        private final int order;
        private final String time;

        Hour(int order, String time) {
            this.order = order;
            this.time = time;
        }

        public int getOrder() {
            return order;
        }

        public String getTime() {
            return time;
        }
        public static Hour fromTime(String time) {
            String normalizedTime = time.length() == 4 ? "0" + time : time;
            for (Hour hour : Hour.values()) {
                if (hour.getTime().equals(normalizedTime)) {
                    return hour;
                }
            }
            throw new IllegalArgumentException("No enum constant for time: " + time);
        }
    }

    private void initialize(String title) {
        scheduleTitleText.setText(title);
        for (Day day : Day.values()) {
            Text dayText = new Text(day.toString());
            gridPane.add(dayText, day.getValue(), 0);
        }
        for (Hour hour : Hour.values()) {
            Text hourText = new Text(hour.getTime());
            gridPane.add(hourText, 0, hour.getOrder());
        }
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Text) {
                GridPane.setMargin(node, new Insets(5));
                GridPane.setMargin(node, new Insets(5));
                GridPane.setHalignment(node, HPos.CENTER);            }
        }
    }

    public void initializeForStudent(String student) {
        initialize("%s Schedule".formatted(student));
        Course[] courses = Database.getCoursesForStudent(student);
        for (Course course : courses) {
            Course courseInfo = Database.getCourse(course.getCourse());
            String date_time = courseInfo.getTime_to_start();
            String[] date_time_split = date_time.split(" ");
            int day = Day.valueOf(date_time_split[0].toUpperCase()).getValue();
            int time = Hour.fromTime(date_time_split[1]).getOrder();
            for (int i = 0; i < courseInfo.getDuration(); i++) {
                Text courseText = new Text(" "+courseInfo.getCourse());
                gridPane.add(courseText, day, time + i);
            }
        }
    }
}
