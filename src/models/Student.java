package models;

import java.util.ArrayList;

public class Student {
    private String name;
    private String metricNumber; // unique per student
    private String gender;
    private String nationality;

    public Student(String name, String metricNumber, String gender, String nationality){
        this.name = name;
        this.metricNumber = metricNumber;
        this.gender = gender;
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }

    public String getMetricNumber() {
        return metricNumber;
    }

    public void printStudent(){
        System.out.println("Name: "+ getName()+", Metric num: "+ getMetricNumber());
    }

    public static Student fromStringToStudent(String line){
        String name = line.split(":")[0];
        String metric = line.split(":")[1];
        String gender = line.split(":")[2];
        String nationality = line.split(":")[3];
        //String courses = line.split(":")[4];
        return new Student(name,metric,gender,nationality);
/*         if(courses.split("-").length > 1){
            ArrayList<String> crs = new ArrayList<String>();
            for(int i=1; i< courses.split("-").length; i++){
                crs.add(courses.split("-")[i]);
            }
            return new Student(name,metric,gender,nationality,crs);
        } else {
            return new Student(name,metric,gender,nationality);
        } */
    }
}