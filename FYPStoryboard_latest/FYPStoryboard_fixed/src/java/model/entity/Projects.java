package model.entity;

import java.sql.Date;

public class Projects {
    private int projectID;
    private String project_title;
    private String description;
    private Date start_date;
    private Date end_date;
    private String project_status;
    private int studentID;

    public int getProjectID() { return projectID; }
    public void setProjectID(int projectID) { this.projectID = projectID; }

    public String getProject_title() { return project_title; }
    public void setProject_title(String project_title) { this.project_title = project_title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getStart_date() { return start_date; }
    public void setStart_date(Date start_date) { this.start_date = start_date; }

    public Date getEnd_date() { return end_date; }
    public void setEnd_date(Date end_date) { this.end_date = end_date; }

    public String getProject_status() { return project_status; }
    public void setProject_status(String project_status) { this.project_status = project_status; }

    public int getStudentID() { return studentID; }
    public void setStudentID(int studentID) { this.studentID = studentID; }
}
