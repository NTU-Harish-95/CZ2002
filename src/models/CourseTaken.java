package models;

public class CourseTaken {

    private String name;
    private String courseCode;
    private int courseIndex;
    private String classSchedule;

    public CourseTaken(String name, String courseCode, int courseIndex, String classSchedule) {
        this.name = name;
        this.courseCode = courseCode;
        this.courseIndex = courseIndex;
        this.classSchedule = classSchedule;
    }   

    public String getName() {
        return name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getCourseIndex() {
        return courseIndex;
    }

    public String getClassSchedule() {
        return classSchedule;
    }
}