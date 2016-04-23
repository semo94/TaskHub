package com.example.saleem.testgithub.gson.items;

import java.util.ArrayList;
import java.util.List;


public class MyNeedsItems {

    private List<MyNeddsList> MyNeddsList = new ArrayList<MyNeddsList>();
    private boolean IsSucceeded;


    public List<MyNeddsList> getMyNeddsList() {
        return MyNeddsList;
    }

    public void setMyNeddsList(List<MyNeddsList> MyNeddsList) {
        this.MyNeddsList = MyNeddsList;
    }


    public boolean getIsSucceeded() {
        return IsSucceeded;
    }


    public void setIsSucceeded(boolean IsSucceeded) {
        this.IsSucceeded = IsSucceeded;
    }


   public class MyNeddsList {

        private String Id;
        private String Title;
        private String status;
        private String UserName;
        private String ImageUrl;


        public String getId() {
            return Id;
        }


        public void setId(String Id) {
            this.Id = Id;
        }


        public String getTitle() {
            return Title;
        }


        public void setTitle(String Title) {
            this.Title = Title;
        }


        public String getStatus() {
            return status;
        }


        public void setStatus(String status) {
            this.status = status;
        }


        public String getUserName() {
            return UserName;
        }


        public void setUserName(String UserName) {
            this.UserName = UserName;
        }


        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

    }
}