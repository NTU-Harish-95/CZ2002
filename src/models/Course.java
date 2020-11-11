package models;

import java.util.ArrayList;
import java.util.TreeMap;

public class Course {
    private String name;
    private String courseCode;
    private String school;
    private String courseInfo;
    private ArrayList<Integer> index = new ArrayList<Integer>(); //available indexes in Course
    private TreeMap<Integer, String> classSchedule = new TreeMap<Integer, String>();
    private TreeMap<Integer, String> venue = new TreeMap<Integer, String>();
    private TreeMap<Integer, Integer> vacancy = new TreeMap<Integer, Integer>(); //vacancy per index
    private TreeMap<Integer,ArrayList<String>> students = new TreeMap<Integer,ArrayList<String>>(); // index to student list mapping

    public Course(String name, String courseCode, String school, String courseInfo, ArrayList<Integer> index, TreeMap<Integer, String> classSchedule, TreeMap<Integer, String> venue, TreeMap<Integer, Integer> vacancy) {
        this.name = name;
        this.courseCode = courseCode;
        this.school = school;
        this.courseInfo = courseInfo;
        this.index = index;
        this.classSchedule = classSchedule;
        this.venue = venue;
        this.vacancy = vacancy;
    }
    public Course(String name, String courseCode, String school, String courseInfo, ArrayList<Integer> index, TreeMap<Integer, String> classSchedule, TreeMap<Integer, String> venue, TreeMap<Integer, Integer> vacancy, TreeMap<Integer,ArrayList<String>> students) {
        this.name = name;
        this.courseCode = courseCode;
        this.school = school;
        this.courseInfo = courseInfo;
        this.index = index;
        this.classSchedule = classSchedule;
        this.venue = venue;
        this.vacancy = vacancy;
        this.students = students;
    }

    public void addStudent(String metricNum, int index){
        //get the old list
        if(students.get(index) != null){
            ArrayList<String> updatedStudents = students.get(index);
            updatedStudents.add(metricNum);
            students.put(index, updatedStudents);
        } else {
            ArrayList<String> updatedStudents = new ArrayList<String>();
            updatedStudents.add(metricNum);
            students.put(index, updatedStudents);
        }
        decrementVacancy(index);
    }

    public void removeStudent(String metricNum, int index){
        if(students.get(index) != null) {
            ArrayList<String> s = students.get(index);
            for(int i=0; i< s.size(); i++){
                if(s.get(i).equals(metricNum)){
                    s.remove(i);
                }
            }
            students.put(index, s);
            incrementVacancy(index);
        }
    }

    public boolean findStudent(String metricNum, int index){
        if(students.get(index) != null) {
            ArrayList<String> s = students.get(index);
            for(int i=0; i< s.size(); i++){
                if(s.get(i).equals(metricNum)){
                    return true;
                }
            }
        }
        return false;
    }

    private void incrementVacancy(int index){
        int currVacancy = vacancy.get(index);
        System.out.println("old Vacancy: "+ currVacancy);
        currVacancy++;
        System.out.println("new Vacancy: "+ currVacancy);

        vacancy.put(index, currVacancy);
    }

    private void decrementVacancy(int index){
        int currVacancy = vacancy.get(index);
        if(currVacancy > 0){
            currVacancy--;
        }
        vacancy.put(index, currVacancy);
    }

    public String getVenueFor(int index){
        return classSchedule.get(index);
    }

    public String getClassScheduleFor(int index){
        return classSchedule.get(index);
    }

    public ArrayList<String> getStudentListFor(int index){
        return students.get(index);
    }

    public int getVacancyFor(int index){
        return vacancy.get(index);
    }

    public String getName() {
        return name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public boolean indexExists(int idx){
        for(int i=0; i< index.size(); i++){
            if(index.get(i) == idx) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> getIndex() {
        return index;
    }

    public static Course fromStringToCourse(String line){
        String name = line.split(":")[0];
        String courseCode = line.split(":")[1];
        String school = line.split(":")[2];
        String courseInfo = line.split(":")[3];
        ArrayList<Integer> idx = new ArrayList<Integer>();
        TreeMap<Integer, String> classSchedule = new TreeMap<Integer, String>();
        TreeMap<Integer, String> venue = new TreeMap<Integer, String>();
        TreeMap<Integer, Integer> vacancy = new TreeMap<Integer, Integer>(); //vacancy per index
        TreeMap<Integer,ArrayList<String>> students = new TreeMap<Integer,ArrayList<String>>(); // index to student list mapping
        //extract both index and class schedule
        String scheduleString = line.split(":")[4];
        String[] schedules = scheduleString.split("-");
        for(int i=1; i<schedules.length; i++) {
            String extractedSchedule = schedules[i];
            //split index and timings
            String index = extractedSchedule.split("/")[0];
            idx.add(Integer.parseInt(index));
            String timings = extractedSchedule.split("/")[1];
            classSchedule.put(Integer.parseInt(index), timings);
        }
        //extract venue
        String venueString = line.split(":")[5];
        String[] venues = venueString.split("-");
        for(int i=1; i<venues.length; i++) {
            String extractedVenue = venues[i];
            //split venue
            String v = extractedVenue.split("/")[1];
            String index = extractedVenue.split("/")[0];
            venue.put(Integer.parseInt(index), v);
        }
        //extract vacancy
        String vacancyString = line.split(":")[6];
        String[] vacancies = vacancyString.split("-");
        for(int i=1; i<vacancies.length; i++) {
            String extractedVacancy = vacancies[i];
            String vac = extractedVacancy.split("/")[1];
            String index = extractedVacancy.split("/")[0];
            vacancy.put(Integer.parseInt(index), Integer.parseInt(vac));
        }
        //extract students
        // students -> :students-index/s1/s2/s3-index/s1/s2/s3/s4 ... 
        String studentString = line.split(":")[7];
        String[] stds = studentString.split("-");
        if(stds.length == 0){
            ArrayList<String> studList = new ArrayList<String>();
            for(int i =0; i< idx.size(); i++){
                students.put(idx.get(i), studList);
            }
        }else {
            for(int i=1; i<stds.length; i++) {
                String extractedStudent = stds[i];
                String index = extractedStudent.split("/")[0];
                String[] studs = extractedStudent.split("/");
                //for each student
                ArrayList<String> studList = new ArrayList<String>();
                for(int s=1; s<studs.length; s++){
                    studList.add(studs[s]);
                }
                students.put(Integer.parseInt(index), studList);
            }
        }
        return new Course(name, courseCode, school, courseInfo, idx, classSchedule, venue, vacancy, students);
    }

    //break down the indexes
    // add venue + vacancy (10 each) + students(student metric) for each index
    public static String toCourseString(String courseName, String courseCode, String school, String courseInfo, ArrayList<Integer> idx, TreeMap<Integer,String>classSchedule ,TreeMap<Integer, String> venue, TreeMap<Integer, Integer>vacancy) {
        //courseName:courseCode:school:courseInfo:schedule...:venue
        String fileString = courseName+":"+courseCode+":"+school+":"+courseInfo;
        // schedule -> :schedule-index/MON,1930,2230-index/MON,1930,2230..
        fileString += ":schedule";
        for(int i=0; i< idx.size(); i++){
            fileString += "-"+idx.get(i)+"/"+classSchedule.get(idx.get(i));
        }
        // venue -> :venue-index/venueName-index/venueName ...
        fileString += ":venue";
        for(int i=0; i< idx.size(); i++){
            fileString += "-"+idx.get(i)+"/"+venue.get(idx.get(i));
        }
        // vacancy -> :vacancy-index/vacancy-index/vacancy ... 
        fileString += ":vacancy";
        for(int i=0; i< idx.size(); i++){
            fileString += "-"+idx.get(i)+"/"+vacancy.get(idx.get(i));
        }
        //assume no students yet
        fileString += ":students";

        return fileString;
    }

    public String toCourseString() {
        //courseName:courseCode:school:courseInfo:schedule...:venue
        String fileString = name+":"+courseCode+":"+school+":"+courseInfo;
        // schedule -> :schedule-index/MON,1930,2230-index/MON,1930,2230..
        fileString += ":schedule";
        for(int i=0; i< index.size(); i++){
            fileString += "-"+index.get(i)+"/"+classSchedule.get(index.get(i));
        }
        // venue -> :venue-index/venueName-index/venueName ...
        fileString += ":venue";
        for(int i=0; i< index.size(); i++){
            fileString += "-"+index.get(i)+"/"+venue.get(index.get(i));
        }
        // vacancy -> :vacancy-index/vacancy-index/vacancy ... 
        fileString += ":vacancy";
        for(int i=0; i< index.size(); i++){
            fileString += "-"+index.get(i)+"/"+vacancy.get(index.get(i));
        }
        // students -> :students-index/s1/s2/s3-index/s1/s2/s3/s4 ... 
        fileString += ":students";
        for(int i=0; i< index.size(); i++){
            int id = index.get(i);
            ArrayList<String> stds = students.get(id);
            fileString += "-"+id;
            if(stds == null){
            } else {
                //for each student
                for(int k=0; k< stds.size(); k++){
                    fileString += "/"+stds.get(k);
                }
            }
        }

        return fileString;
    }
}