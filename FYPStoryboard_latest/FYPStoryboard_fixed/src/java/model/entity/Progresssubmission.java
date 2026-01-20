package model.entity;

import java.sql.Date;

public class Progresssubmission {
    private int progressID;
    private Date submitted_date;
    private String progress_status;
    private int projectID;

    // Attachments
    private String studentFileName;
    private String studentFilePath;
    private String supervisorFileName;
    private String supervisorFilePath;

    public Progresssubmission() {}

    public int getProgressID() { return progressID; }
    public void setProgressID(int progressID) { this.progressID = progressID; }

    public Date getSubmitted_date() { return submitted_date; }
    public void setSubmitted_date(Date submitted_date) { this.submitted_date = submitted_date; }

    public String getProgress_status() { return progress_status; }
    public void setProgress_status(String progress_status) { this.progress_status = progress_status; }

    public int getProjectID() { return projectID; }
    public void setProjectID(int projectID) { this.projectID = projectID; }

    public String getStudentFileName() { return studentFileName; }
    public void setStudentFileName(String studentFileName) { this.studentFileName = studentFileName; }

    public String getStudentFilePath() { return studentFilePath; }
    public void setStudentFilePath(String studentFilePath) { this.studentFilePath = studentFilePath; }

    public String getSupervisorFileName() { return supervisorFileName; }
    public void setSupervisorFileName(String supervisorFileName) { this.supervisorFileName = supervisorFileName; }

    public String getSupervisorFilePath() { return supervisorFilePath; }
    public void setSupervisorFilePath(String supervisorFilePath) { this.supervisorFilePath = supervisorFilePath; }
}
