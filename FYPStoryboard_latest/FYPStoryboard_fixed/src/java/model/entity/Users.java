package model.entity;

public class Users {
    private int userID;
    private String user_name;
    private String user_email;
    private String user_role;

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getUser_name() { return user_name; }
    public void setUser_name(String user_name) { this.user_name = user_name; }

    public String getUser_email() { return user_email; }
    public void setUser_email(String user_email) { this.user_email = user_email; }

    public String getUser_role() { return user_role; }
    public void setUser_role(String user_role) { this.user_role = user_role; }
}
