package com.example.saleem.testgithub.gson.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 5/2/2016.
 */
public class UnderProgressItems {
    private List<ProgressToDo> ProgressToDo = new ArrayList<ProgressToDo>();
    private Boolean IsSucceeded;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The UnderProgressToDoList
     */
    public List<ProgressToDo> getProgressToDo() {
        return ProgressToDo;
    }

    /**
     * @param ProgressToDo The UnderProgressToDoList
     */
    public void setProgressToDo(List<ProgressToDo> ProgressToDo) {
        this.ProgressToDo = ProgressToDo;
    }

    /**
     * @return The IsSucceeded
     */
    public Boolean getIsSucceeded() {
        return IsSucceeded;
    }

    /**
     * @param IsSucceeded The IsSucceeded
     */
    public void setIsSucceeded(Boolean IsSucceeded) {
        this.IsSucceeded = IsSucceeded;
    }


    public class ProgressToDo {

        private String Id;
        private String sid;
        private String Title;
        private int PriorityId;
        private String UserName;
        private String ImageUrl;


        /**
         * @return The Id
         */
        public String getId() {
            return Id;
        }

        /**
         * @param Id The Id
         */
        public void setId(String Id) {
            this.Id = Id;
        }

        /**
         * @return The sid
         */
        public String getSid() {
            return sid;
        }

        /**
         * @param sid The sid
         */
        public void setSid(String sid) {
            this.sid = sid;
        }

        /**
         * @return The Title
         */
        public String getTitle() {
            return Title;
        }

        /**
         * @param Title The Title
         */
        public void setTitle(String Title) {
            this.Title = Title;
        }

        /**
         * @return The PriorityId
         */
        public int getPriorityId() {
            return PriorityId;
        }

        /**
         * @param PriorityId The PriorityId
         */
        public void setPriorityId(int PriorityId) {
            this.PriorityId = PriorityId;
        }

        /**
         * @return The UserName
         */
        public String getUserName() {
            return UserName;
        }

        /**
         * @param UserName The UserName
         */
        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        /**
         * @return The ImageUrl
         */
        public String getImageUrl() {
            return ImageUrl;
        }

        /**
         * @param ImageUrl The ImageUrl
         */
        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }


    }

}
