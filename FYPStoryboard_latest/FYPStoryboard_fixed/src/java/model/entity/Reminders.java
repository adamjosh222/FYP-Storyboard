package model.entity;

import java.sql.Date;

public class Reminders {
    private int reminderID;
    private String reminder_name;
    private Date due_date;
    private String submission_status;
    private String recurrence;
    private int projectID;

    public int getReminderID() { return reminderID; }
    public void setReminderID(int reminderID) { this.reminderID = reminderID; }

    public String getReminder_name() { return reminder_name; }
    public void setReminder_name(String reminder_name) { this.reminder_name = reminder_name; }

    public Date getDue_date() { return due_date; }
    public void setDue_date(Date due_date) { this.due_date = due_date; }

    public String getSubmission_status() { return submission_status; }
    public void setSubmission_status(String submission_status) { this.submission_status = submission_status; }

    public String getRecurrence() { return recurrence; }
    public void setRecurrence(String recurrence) { this.recurrence = recurrence; }

    public int getProjectID() { return projectID; }
    public void setProjectID(int projectID) { this.projectID = projectID; }
}
