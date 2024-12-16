package team_nine.course_scheduler;
// This static class is responsible for creating and managing the database.
// The database has 5 tables: Courses, Classrooms, Allocated, Students, and Enrollments.
// The database is created in the user's home directory under the Documents folder.
// The usage of the database is "Database.createAndConnectToDatabase()" etc.

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Database {
    private static final String dbPath = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "CourseSchedulerDatabase";
    private static final String URL = "jdbc:sqlite:" + dbPath + File.separator + "lectures.db";
    private static Connection conn;

    static {
        createAndConnectToDatabase();
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createAndConnectToDatabase() {
        File dbDir = new File(dbPath);
        if (!dbDir.exists()) dbDir.mkdirs();

        File dbFile = new File(dbPath + File.separator + "lectures.db");
        if (!dbFile.exists()) {
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

            try (Connection conn = DriverManager.getConnection(URL);
                 Statement stmt = conn.createStatement()) {
                stmt.execute(createCoursesTable);
                stmt.execute(createClassroomsTable);
                stmt.execute(createAllocatedTable);
                stmt.execute(createStudentsTable);
                stmt.execute(createEnrollmentsTable);
                System.out.println("Database and tables created successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addCourse(String course, String timeToStart, int duration, String lecturer, String[] students) {
        try (PreparedStatement insertCourse = conn.prepareStatement("""
                INSERT INTO Courses (course, time_to_start, duration, lecturer)
                VALUES (?, ?, ?, ?);
            """);
             PreparedStatement insertStudent = conn.prepareStatement("""
                INSERT OR IGNORE INTO Students (name)
                VALUES (?);
            """);
             PreparedStatement insertEnrollment = conn.prepareStatement("""
                INSERT INTO Enrollments (course, student_name)
                VALUES (?, ?);
            """)) {

            insertCourse.setString(1, course);
            insertCourse.setString(2, timeToStart);
            insertCourse.setInt(3, duration);
            insertCourse.setString(4, lecturer);
            insertCourse.execute();

            for (String student : students) {
                insertStudent.setString(1, student);
                insertEnrollment.setString(1, course);
                insertEnrollment.setString(2, student);
                insertStudent.execute();
                insertEnrollment.execute();
            }
            System.out.println("Course added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeClassroom(String course, String classroom) {
        try (PreparedStatement updateAllocated = conn.prepareStatement("""
                UPDATE Allocated
                SET classroom = ?
                WHERE course = ?;
            """)) {
            updateAllocated.setString(1, classroom);
            updateAllocated.setString(2, course);
            updateAllocated.execute();
            System.out.println("Classroom change successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void matchClassroom(String course, String classroom) {
        try (PreparedStatement insertAllocated = conn.prepareStatement("""
                INSERT INTO Allocated (course, classroom)
                VALUES (?, ?);
            """)) {
            insertAllocated.setString(1, course);
            insertAllocated.setString(2, classroom);
            insertAllocated.execute();
            System.out.println("Classroom matched successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addStudent(String course, String[] students) {
        try (PreparedStatement insertStudent = conn.prepareStatement("""
                INSERT OR IGNORE INTO Students (name) VALUES (?);
            """);
             PreparedStatement insertEnrollment = conn.prepareStatement("""
                INSERT INTO Enrollments (course, student_name)
                VALUES (?, ?);
            """)) {
            for (String student : students) {
                insertStudent.setString(1, student);
                insertEnrollment.setString(1, course);
                insertEnrollment.setString(2, student);
                insertStudent.execute();
                insertEnrollment.execute();
            }
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeStudent(String course, String student) {
        try (PreparedStatement deleteEnrollment = conn.prepareStatement("""
                DELETE FROM Enrollments
                WHERE course = ? AND student_name = ?;
            """)) {
            deleteEnrollment.setString(1, course);
            deleteEnrollment.setString(2, student);
            deleteEnrollment.execute();
            System.out.println("Student removed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void populateCourses(File file) {
        try (Scanner sc = new Scanner(file)) {
            sc.nextLine(); // Skip the header
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().split(";");
                String[] students = new String[line.length - 4];
                System.arraycopy(line, 4, students, 0, line.length - 4);
                addCourse(line[0], line[1], Integer.parseInt(line[2]), line[3], students);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void populateClassrooms(File file) {
        try (Scanner sc = new Scanner(file)) {
            sc.nextLine(); // Skip the header
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().split(";");
                addClassroom(line[0], Integer.parseInt(line[1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void addClassroom(String s, int i) {
        try (PreparedStatement insertClassroom = conn.prepareStatement("""
                INSERT INTO Classrooms (classroom, capacity)
                VALUES (?, ?);
            """)) {
            insertClassroom.setString(1, s);
            insertClassroom.setInt(2, i);
            insertClassroom.execute();
            System.out.println("Classroom added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Course[] getAllCourses() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Courses;")) {
            ArrayList<Course> courses = new ArrayList<>();
            while (rs.next()) {
                courses.add(new Course(rs.getString("course"), rs.getString("time_to_start"), rs.getString("lecturer"), rs.getInt("duration")));
            }
            return courses.toArray(new Course[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Course[0];
        }
    }

    public static Student[] getAllStudents() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Students;")) {
            ArrayList<Student> students = new ArrayList<>();
            while (rs.next()) {
                students.add(new Student(rs.getString("name")));
            }
            return students.toArray(new Student[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Student[0];
        }
    }

    public static Classroom[] getAllClassrooms() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Classrooms;")) {
            ArrayList<Classroom> classrooms = new ArrayList<>();
            while (rs.next()) {
                classrooms.add(new Classroom(rs.getString("classroom"), rs.getInt("capacity")));
            }
            return classrooms.toArray(new Classroom[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Classroom[0];
        }
    }

    public static String getAllocatedClassroom(String course) {
        try (PreparedStatement stmt = conn.prepareStatement("""
                SELECT classroom FROM Allocated WHERE course = ?;
            """)) {
            stmt.setString(1, course);
            ResultSet rs = stmt.executeQuery();
            return rs.getString("classroom");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /*
    public static boolean getAvailability(Classroom classroom, String DesiredTime) {
        try (PreparedStatement stmt = conn.prepareStatement("""
                SELECT time_to_start, duration FROM Courses WHERE classroom_name = ? AND ? BETWEEN time_to_start AND DATE_ADD(time_to_start, INTERVAL duration MINUTE)
                """)) {
            stmt.setString(1, classroom.getClassroom());
            stmt.setString(2, DesiredTime);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { return false; }
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return true;
    }
     */

    public static int getCapacity(Classroom classroom){
        try (PreparedStatement stmt = conn.prepareStatement("""
            SELECT capacity FROM Classrooms WHERE classroom_name = ?
            """)){
            stmt.setString(1,classroom.getClassroom());
            ResultSet rs = stmt.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getStudentNumber(Course course){
        try (PreparedStatement stmt = conn.prepareStatement("""
        SELECT COUNT(*) FROM Enrollments WHERE course_name = ?
        """)){
            stmt.setString(1,course.getCourse());
            ResultSet rs = stmt.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTimeOfCourse(Course course){
        try (PreparedStatement stmt = conn.prepareStatement("""
        SELECT time_to_start FROM Courses WHERE course_name = ?
        """)){
            stmt.setString(1,course.getCourse());
            ResultSet rs = stmt.executeQuery();
            return rs.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getDuration(Course course){
        try (PreparedStatement stmt = conn.prepareStatement("""
        SELECT duration FROM Courses WHERE course_name = ?
        """)){
            stmt.setString(1,course.getCourse());
            ResultSet rs = stmt.executeQuery();
            return rs.getString(2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getAllocatedCourse(String classroom) {
        try (PreparedStatement stmt = conn.prepareStatement("""
                SELECT course FROM Allocated WHERE classroom = ?;
            """)) {
            stmt.setString(1, classroom);
            ResultSet rs = stmt.executeQuery();
            return rs.getString("course");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getAllLecturers() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT lecturer FROM Courses;")) {
            ArrayList<String> lecturers = new ArrayList<>();
            while (rs.next()) {
                lecturers.add(rs.getString("lecturer"));
            }
            return lecturers.toArray(new String[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public static String[] getStudentsInCourse(String course) {
        System.out.println("Fetching students for course: " + course); // Debugging line
        try (PreparedStatement stmt = conn.prepareStatement("""
            SELECT student_name
            FROM Enrollments
            WHERE TRIM(course) = ?;
        """)) {
            stmt.setString(1, course);
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> students = new ArrayList<>();
            while (rs.next()) {
                String studentName = rs.getString("student_name");
                System.out.println("Found student: " + studentName); // Debugging line
                students.add(studentName);
            }
            return students.toArray(new String[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public static Course[] getCoursesForStudent(String student) {
        try (PreparedStatement stmt = conn.prepareStatement("""
            SELECT course
            FROM Enrollments
            WHERE TRIM(student_name) = ?;
        """)) {
            stmt.setString(1, student);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Course> courses = new ArrayList<>();
            while (rs.next()) {
                String courseName = rs.getString("course");
                courses.add(new Course(courseName, "", "", 0));
            }
            return courses.toArray(new Course[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Course[0];
        }
    }

    public static Course getCourse(String course) {
        try (PreparedStatement stmt = conn.prepareStatement("""
            SELECT *
            FROM Courses
            WHERE TRIM(course) = ?;
        """)) {
            stmt.setString(1, course);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Course(rs.getString("course"), rs.getString("time_to_start"), rs.getString("lecturer"), rs.getInt("duration"));
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Course[] getCoursesForLecturer(String lecturer) {
        try (PreparedStatement stmt = conn.prepareStatement("""
            SELECT course
            FROM Courses
            WHERE TRIM(lecturer) = ?;
        """)) {
            stmt.setString(1, lecturer);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Course> courses = new ArrayList<>();
            while (rs.next()) {
                String courseName = rs.getString("course");
                courses.add(new Course(courseName, "", "", 0));
            }
            return courses.toArray(new Course[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Course[0];
        }
    }

}