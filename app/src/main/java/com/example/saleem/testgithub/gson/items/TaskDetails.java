package com.example.saleem.testgithub.gson.items;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 5/2/2016.
 */
public class TaskDetails {

    private String Title;
    private String Descreption;
    private int Priority;
    private String DLine;
    private String AttachedImgUrl;
    private String UserName;
    private String UserImgUrl;
    private Boolean IsSucceeded;

    /**
     *
     * @return
     * The Title
     */
    public String getTitle() {
        return Title;
    }

    /**
     *
     * @param Title
     * The Title
     */
    public void setTitle(String Title) {
        this.Title = Title;
    }

    /**
     *
     * @return
     * The Descreption
     */
    public String getDescreption() {
        return Descreption;
    }

    /**
     *
     * @param Descreption
     * The Descreption
     */
    public void setDescreption(String Descreption) {
        this.Descreption = Descreption;
    }

    /**
     *
     * @return
     * The Priority
     */
    public int getPriority() {
        return Priority;
    }

    /**
     *
     * @param Priority
     * The Priority
     */
    public void setPriority(int Priority) {
        this.Priority = Priority;
    }

    /**
     *
     * @return
     * The DLine
     */
    public String getDLine() {
        return DLine;
    }

    /**
     *
     * @param DLine
     * The DLine
     */
    public void setDLine(String DLine) {
        this.DLine = DLine;
    }

    /**
     *
     * @return
     * The AttachedImgUrl
     */
    public String getAttachedImgUrl() {
        return AttachedImgUrl;
    }

    /**
     *
     * @param AttachedImgUrl
     * The AttachedImgUrl
     */
    public void setAttachedImgUrl(String AttachedImgUrl) {
        this.AttachedImgUrl = AttachedImgUrl;
    }

    /**
     *
     * @return
     * The UserName
     */
    public String getUserName() {
        return UserName;
    }

    /**
     *
     * @param UserName
     * The UserName
     */
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    /**
     *
     * @return
     * The UserImgUrl
     */
    public String getUserImgUrl() {
        return UserImgUrl;
    }

    /**
     *
     * @param UserImgUrl
     * The UserImgUrl
     */
    public void setUserImgUrl(String UserImgUrl) {
        this.UserImgUrl = UserImgUrl;
    }

    /**
     *
     * @return
     * The IsSucceeded
     */
    public Boolean getIsSucceeded() {
        return IsSucceeded;
    }

    /**
     *
     * @param IsSucceeded
     * The IsSucceeded
     */
    public void setIsSucceeded(Boolean IsSucceeded) {
        this.IsSucceeded = IsSucceeded;
    }

}
