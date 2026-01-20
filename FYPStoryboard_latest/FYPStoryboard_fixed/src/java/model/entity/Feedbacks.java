package model.entity;

import java.sql.Date;

public class Feedbacks {
    private int feedbackID;
    private String feedback_text;
    private Date feedback_date;
    private int supervisorID;
    private int progressID;

    public int getFeedbackID() { return feedbackID; }
    public void setFeedbackID(int feedbackID) { this.feedbackID = feedbackID; }

    public String getFeedback_text() { return feedback_text; }
    public void setFeedback_text(String feedback_text) { this.feedback_text = feedback_text; }

    public Date getFeedback_date() { return feedback_date; }
    public void setFeedback_date(Date feedback_date) { this.feedback_date = feedback_date; }

    public int getSupervisorID() { return supervisorID; }
    public void setSupervisorID(int supervisorID) { this.supervisorID = supervisorID; }

    public int getProgressID() { return progressID; }
    public void setProgressID(int progressID) { this.progressID = progressID; }
}
