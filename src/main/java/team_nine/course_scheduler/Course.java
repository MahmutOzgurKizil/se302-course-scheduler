package team_nine.course_scheduler;

import java.util.ArrayList;

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

    public void autoAssign(Course course){
        Classroom[] PotentialClasses = Database.getAllClassrooms();
        for(Classroom c : PotentialClasses){
            if(Database.getAvailability(c,course.time_to_start)){
                Database.changeClassroom(course.course,c.getClassroom());
                System.out.println("Course succesfully added to classroom: " + c.getClassroom() + " at time: "+course.time_to_start);
                return;
            }
            System.out.println("All Classrooms are occupied");
        }
    }

    public void manualAssign(Course course, Classroom classroom){
        String theClassroom = Database.getAllocatedClassroom(classroom.getClassroom());
        String theCourse = course.getCourse();

    try {
        //if available match if else dont do anything
        if(Database.getAvailability(classroom,course.getTime_to_start())){
            Database.matchClassroom(theCourse,theClassroom);
        }else{
            System.out.println("classroom occup'ed");

        }
    }catch(Exception e){
        System.out.println("Error when assigning classroom");
        e.printStackTrace();
    }
    }

    public void createCourse(String time_to_start,String course, String lecturer, int duration){
        Course NewCourse = new Course(time_to_start,course,lecturer,duration);
        Database.addCourse(course,time_to_start,duration,lecturer,null);
    }


    public void switchClassrooms(Course OtherCourse, Classroom DesiredClassroom) {
        try{
            String current = Database.getAllocatedClassroom(this.course);
            String theClassroom = Database.getAllocatedClassroom(OtherCourse.getCourse());

            //if classroom is not available return nothing if available change clasroom
            if (theClassroom != null && !Database.getAvailability(DesiredClassroom, OtherCourse.getTime_to_start())) {
                System.out.println("Desired classroom is in use.");
                return;
            }
            Database.changeClassroom(this.course, DesiredClassroom.getClassroom());
        }catch (Exception e){
            System.out.println("Error while switching classrooms."+ e.getMessage());
            e.printStackTrace();
        }
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
