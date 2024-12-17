package team_nine.course_scheduler;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class SchedulePanelController {
    @FXML
    private Text scheduleTitleText;
    @FXML
    private GridPane gridPane;

    private enum Day {
        monday(1), tuesday(2), wednesday(3), thursday(4), friday(5);

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
        FOUR_FORTY_FIVE(10, "16:45"), FIVE_FORTY(11, "17:40"), SIX_THIRTY_FIVE(12, "18:35"), SEVEN_THIRTY(13, "19:30"), EIGHT_TWENTY_FIVE(14, "20:25"), NINE_TWENTY(15, "21:20"), TEN_FIFTEEN(16, "22:15");

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
            gridPane.add(new Text(day.toString()), day.getValue(), 0);
        }
        for (Hour hour : Hour.values()) {
            gridPane.add(new Text(hour.getTime()), 0, hour.getOrder());
        }
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Text) {
                GridPane.setMargin(node, new Insets(5));
                GridPane.setHalignment(node, HPos.CENTER);
            }
        }
    }

    public void initializeForStudent(String student) {
        initialize("%s Schedule".formatted(student));
        createWeeklySchedule(Database.getCoursesForStudent(student));
    }

    public void initializeForLecturer(String lecturer) {
        initialize("%s Schedule".formatted(lecturer));
        createWeeklySchedule(Database.getCoursesForLecturer(lecturer));
    }

    public void initializeForClassroom(String classroom) {
        initialize("%s Schedule".formatted(classroom));
        createWeeklySchedule(Database.getCoursesForClassroom(classroom));
    }

    private void createWeeklySchedule(Course[] courses) {
        for (Course course : courses) {
            Course courseInfo = Database.getCourse(course.getCourse());
            String[] dateTimeSplit = courseInfo.getTime_to_start().split(" ");
            int day = Day.valueOf(dateTimeSplit[0].toLowerCase()).getValue();
            int time = Hour.fromTime(dateTimeSplit[1]).getOrder();
            String rdColor = getRandomColor();
            for (int i = 0; i < courseInfo.getDuration(); i++) {
                StackPane coursePane = new StackPane(new Text(" " + courseInfo.getCourse()));
                coursePane.setStyle("-fx-background-color: " + rdColor + ";");
                gridPane.add(coursePane, day, time + i);
            }
        }
    }

    private String getRandomColor() {
        String[] colors = {"lightcoral", "lightcyan", "lightgoldenrodyellow", "lightgray", "lightgreen", "lightpink",
            "lightsalmon", "lightseagreen", "lightskyblue", "lightsteelblue", "lightyellow", "aliceblue", "lavender",
            "mintcream", "honeydew", "ivory"};
        return colors[(int) (Math.random() * colors.length)];
    }
}