package team_nine.course_scheduler;
// This static class is responsible for creating and managing the database.
// The database has 5 tables: Courses, Classrooms, Allocated, Students, and Enrollments.
// The database is created in the user's home directory under the Documents folder.
// The usage of the database is "Database.createAndConnectToDatabase()" etc.

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Database {
    private static final String dbPath = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "CourseSchedulerDatabase";
    private static final String URL = "jdbc:sqlite:" + dbPath + File.separator + "lectures.db";
    private static Connection conn = null;

    private static void createAndConnectToDatabase() {

        File dbDir = new File(dbPath);
        if(!dbDir.exists()) {
            dbDir.mkdirs();
        }
        File dbFile = new File(dbPath + File.separator + "lectures.db");
        if(!dbFile.exists()) {
            // SQL commands to create tables
            String createCoursesTable = """
            CREATE TABLE Courses (
                course TEXT PRIMARY KEY,
                time_to_start TEXT,
                duration INTEGER,
                lecturer TEXT
            );
            """;

            String createClassroomsTable = """
            CREATE TABLE Classrooms (
                classroom TEXT PRIMARY KEY,
                capacity INTEGER
            );
            """;

            String createAllocatedTable = """
            CREATE TABLE Allocated (
                course TEXT,
                classroom TEXT,
                FOREIGN KEY (course) REFERENCES Courses(course),
                FOREIGN KEY (classroom) REFERENCES Classrooms(classroom)
            );
            """;

            String createStudentsTable = """
            CREATE TABLE Students (
                name TEXT PRIMARY KEY
            );
            """;

            String createEnrollmentsTable = """
            CREATE TABLE Enrollments (
                course TEXT,
                student_name TEXT,
                FOREIGN KEY (course) REFERENCES Courses(course),
                FOREIGN KEY (student_name) REFERENCES Students(name)
            );
            """;

            try  {
                conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement();
                stmt.execute(createCoursesTable);
                stmt.execute(createClassroomsTable);
                stmt.execute(createAllocatedTable);
                stmt.execute(createStudentsTable);
                stmt.execute(createEnrollmentsTable);
                System.out.println("Database and tables created successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public static void addCourse(String course, String timeToStart, int duration, String lecturer, String[] students) {
        try {
            PreparedStatement insertCourse = conn.prepareStatement("""
                INSERT INTO Courses (course, time_to_start, duration, lecturer)
                VALUES (?, ?, ?, ?);
            """);

            PreparedStatement insertStudent = conn.prepareStatement("""
                INSERT INTO Students (name)
                VALUES (?);
            """);

            PreparedStatement insertEnrollment = conn.prepareStatement("""
                INSERT INTO Enrollments (course, student_name)
                VALUES (?, ?);
            """);

            insertCourse.setString(1, course);
            insertCourse.setString(2, timeToStart);
            insertCourse.setInt(3, duration);
            insertCourse.setString(4, lecturer);
            insertCourse.execute();

            for(String student: students) {
                insertStudent.setString(1, student);
                insertEnrollment.setString(1, course);
                insertEnrollment.setString(2, student);

                insertEnrollment.execute();
                insertStudent.execute();
            }

            System.out.println("Course added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeClassroom(String course, String classroom) {
        try {
            PreparedStatement updateAllocated = conn.prepareStatement("""
                UPDATE Allocated
                SET classroom = ?
                WHERE course = ?;
            """);

            updateAllocated.setString(1, classroom);
            updateAllocated.setString(2, course);
            updateAllocated.execute();
            System.out.println("Classroom change successfully.");
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void matchClassroom(String course, String classroom) {
        try {
            PreparedStatement insertAllocated = conn.prepareStatement("""
                INSERT INTO Allocated (course, classroom)
                VALUES (?, ?);
            """);

            insertAllocated.setString(1, course);
            insertAllocated.setString(2, classroom);
            insertAllocated.execute();
            System.out.println("Classroom matched successfully.");
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    // YAPILACAK
    public static String getClassroomOfCourse(String name) {
        try {
            PreparedStatement getClassroom = conn.prepareStatement("""
                SELECT classroom
                FROM Allocated
                WHERE course = ?;
            """);
            getClassroom.setString(1, name);
            getClassroom.execute();
            return getClassroom.getResultSet().getString("classroom");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    public static void addStudent(String course, String[] students) {
        try {
            PreparedStatement insertStudent = conn.prepareStatement("""
                INSERT INTO Students (name)
                VALUES (?)
                WHERE NOT EXISTS (SELECT 1 FROM Students WHERE name = ?);
            """);

            PreparedStatement insertEnrollment = conn.prepareStatement("""
                INSERT INTO Enrollments (course, student_name)
                VALUES (?, ?);
            """);

            for (String student: students) {
                insertStudent.setString(1, student);
                insertStudent.setString(2, student);

                insertEnrollment.setString(1, course);
                insertEnrollment.setString(2, student);

                insertStudent.execute();
                insertEnrollment.execute();
            }
            System.out.println("Student added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeStudent(String course, String student) {
        try {
            PreparedStatement deleteEnrollment = conn.prepareStatement("""
                DELETE FROM Enrollments
                WHERE course = ? AND student_name = ?;
            """);

            // If there is no course that this person is enrolled, should they be deleted?

            deleteEnrollment.setString(1, course);
            deleteEnrollment.setString(2, student);
            deleteEnrollment.execute();
            System.out.println("Student removed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
