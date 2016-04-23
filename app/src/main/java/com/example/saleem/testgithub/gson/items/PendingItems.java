package com.example.saleem.testgithub.gson.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 4/23/2016.
 */
public class PendingItems {

    private List<PendingToDoList> PendingToDoList = new ArrayList<PendingToDoList>();
    private boolean IsSucceeded;

    public List<PendingToDoList> getPendingToDoList() {
        return PendingToDoList;
    }


    public void setPendingToDoList(List<PendingToDoList> PendingToDoList) {
        this.PendingToDoList = PendingToDoList;
    }


    public boolean getIsSucceeded() {
        return IsSucceeded;
    }


    public void setIsSucceeded(boolean IsSucceeded) {
        this.IsSucceeded = IsSucceeded;
    }


    public class PendingToDoList {

        private String Id;
        private String Title;
        private int PriorityId;
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

        public int getPriorityId() {
            return PriorityId;
        }

        public void setPriorityId(int PriorityId) {
            this.PriorityId = PriorityId;
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
