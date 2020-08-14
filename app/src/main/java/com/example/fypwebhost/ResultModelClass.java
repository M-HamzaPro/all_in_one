package com.example.fypwebhost;

public class ResultModelClass {

    String similarity, studentName, studentEmail;

    public ResultModelClass(String similarity, String studentName, String studentEmail) {
        this.similarity = similarity;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }
}
