package team_nine.course_scheduler;

import java.util.ArrayList;

public class Course {
    private  String hour;
    private  String classroom;
    private  String students;
    private  String dateTime;
    private String courseName;
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

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getStudents() {
        return students;
    }

    public void setStudents(String students) {
        this.students = students;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Course(String courseName, String dateTime, String lecturer, String students, String classroom, String hour) {
        this.courseName=courseName;
        this.dateTime=dateTime;
        this.lecturer=lecturer;
        this.students=students;
        this.classroom=classroom;
        this.hour=hour;

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

    public void autoAssign(Course course){

    }

    public void manualAssign(Course course, Classroom classroom){

    }

    public void createCourse(String time_to_start,String course, String lecturer, int duration){
        Course NewCourse = new Course(time_to_start,course,lecturer,duration);
        Database.addCourse(course,time_to_start,duration,lecturer,null);
    }


    public void switchClassrooms(Course OtherCourse, Classroom DesiredClassroom){

    }

    public void addStudents(ArrayList<Student> students){
        String[] EnrolledStudents = new  String[students.size()];
        int i =0;
        for(Student s : students){
            i++;
            EnrolledStudents[i]= String.valueOf(students.get(i));
        }
        Database.addStudent(course,EnrolledStudents);
    }

    public void removeStudents(Student student){
        String withdrawal = student.getName();
        Database.removeStudent(course,withdrawal);
    }
}
