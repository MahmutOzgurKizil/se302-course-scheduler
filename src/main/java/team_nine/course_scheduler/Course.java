package team_nine.course_scheduler;

public class Course {
    private String time_to_start;
    private String course;
    private String lecturer;
    private int duration;

    //Course Constructor
    public Course(String time_to_start, String course, String lecturer, int duration) {
        this.course = course;
        this.time_to_start = time_to_start;
        this.duration = duration;
        this.lecturer = lecturer;
    }

    //Setters & Getters
    public String getCourse() {return course;}
    public void setCourse(String course) {this.course = course;}
    public int getDuration() {return duration;}
    public String getLecturer() {return lecturer;}
    public String getTime_to_start() {return time_to_start;}
    public void setDuration(int duration) {this.duration = duration;}
    public void setLecturer(String lecturer) {this.lecturer = lecturer;}
    public void setTime_to_start(String time_to_start) {this.time_to_start = time_to_start;}



}
