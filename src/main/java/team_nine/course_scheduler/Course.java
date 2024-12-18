package team_nine.course_scheduler;

import java.time.LocalTime;
import java.util.ArrayList;

public class Course {
    private String time_to_start;
    private String course;
    private String lecturer;
    private int duration;

    //Course Constructor
    public Course(String course, String time_to_start, String lecturer, int duration) {
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

    public void autoAssign(Course course) {
        Classroom[] potentialClasses = Database.getAllClassrooms();
        if (potentialClasses == null || potentialClasses.length == 0) {
            System.out.println("No classrooms available in the database.");
            return;
        }

        try {
            for (Classroom classroom : potentialClasses) {
                if (isAvailable(classroom, course) && Database.getCapacity(classroom) >= Database.getStudentNumber(course)) {
                    Database.matchClassroom(course.course, classroom.getClassroom());
                    System.out.println("Course successfully added to classroom: " +
                            classroom.getClassroom() + " at time: " + course.time_to_start);
                    return;
                }
            }
            System.out.println("All classrooms are occupied or do not meet capacity requirements.");

        } catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException: Ensure database and array logic are correct.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during autoAssign.");
            e.printStackTrace();
        }
    }


    public void manualAssign(Course course, Classroom classroom){
        String theClassroom = Database.getAllocatedClassroom(classroom.getClassroom());
        String theCourse = course.getCourse();

    try {
        //if available match if else dont do anything
        if(isAvailable(classroom,course)){
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
            if (theClassroom != null && isAvailable(DesiredClassroom, OtherCourse)){
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

    public static Course iterateDate(Course course){
        String date = Database.getTimeOfCourse(course);
        int duration = Integer.parseInt(Database.getDuration(course));
        String[] SplitDate = date.split(" ");
        String[] PossibleTimes ={"08:30","09:25","10:20","11:15",
                                "12:10","13:05", "14:00","14:55",
                                "15:50","16:45","17:40","18:35"
                                ,"19:30","20:25","21:20","22:15"};
        int i = 0;
        for (String s : PossibleTimes){
            if(SplitDate[1]==s){
                String temp = s;
                break;
            }
            i++;
        }
        Course temp = new Course(PossibleTimes[i+duration], "","" , 0);
        return temp;
    }
    public static boolean isAvailable(Classroom classroom,Course course){
        for (Course classCourse : Database.getCoursesForClassroomAllInfo(classroom.getClassroom())){
            LocalTime start1 = LocalTime.parse(zeroPad(course.getTime_to_start().split(" ")[1]));
            LocalTime start2 = LocalTime.parse(zeroPad(classCourse.time_to_start.split(" ")[1]));
            int durationMultiplier1 = course.getDuration();
            int durationMultiplier2 = classCourse.getDuration();
            int slotDuration = 55;
            LocalTime end1 = start1.plusMinutes(slotDuration * durationMultiplier1);
            LocalTime end2 = start2.plusMinutes(slotDuration * durationMultiplier2);
            if ((start1.isBefore(end2) && start2.isBefore(end1))){
                return false;
            }
        }
        return true;
    }

    private static String zeroPad(String time) {
        String[] parts = time.split(":");
        String hour = parts[0].length() == 1 ? "0" + parts[0] : parts[0]; // Add leading zero if needed
        return hour + ":" + parts[1];
    }
    @Override
    public String toString() {
        return course;
    }
}
