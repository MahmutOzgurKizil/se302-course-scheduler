package team_nine.course_scheduler;

public class Student {
    private String name;

    //Student Constructor
    public Student(String name){this.name=name;}

    //Setter & Getter
    public String getName() {return name;}

    @Override
    public String toString() {
        return name;
    }
}
