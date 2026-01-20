package model.entity;

import java.sql.Date;

public class Notes {
    private int notesID;
    private String notes_title;
    private String notes_content;
    private Date created_date;
    private int userID;
    private int projectID;

    public int getNotesID() { return notesID; }
    public void setNotesID(int notesID) { this.notesID = notesID; }

    public String getNotes_title() { return notes_title; }
    public void setNotes_title(String notes_title) { this.notes_title = notes_title; }

    public String getNotes_content() { return notes_content; }
    public void setNotes_content(String notes_content) { this.notes_content = notes_content; }

    public Date getCreated_date() { return created_date; }
    public void setCreated_date(Date created_date) { this.created_date = created_date; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public int getProjectID() { return projectID; }
    public void setProjectID(int projectID) { this.projectID = projectID; }
}
