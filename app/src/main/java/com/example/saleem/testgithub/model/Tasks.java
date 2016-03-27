package com.example.saleem.testgithub.model;


public class Tasks {

    private String userName, thumbnailUrl;
    private String taskTitle, status;
    private int priorityID;


    public Tasks() {
    }

    public Tasks(String taskTitle, String status, int priorityID, String userName, String thumbnailUrl) {
        this.taskTitle = taskTitle;
        this.status = status;
        this.priorityID = priorityID;
        this.userName = userName;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriorityID() {
        return priorityID;
    }

    public void setPriorityID(int priorityID) {
        this.priorityID = priorityID;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

}
