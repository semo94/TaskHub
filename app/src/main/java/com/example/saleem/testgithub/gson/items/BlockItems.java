package com.example.saleem.testgithub.gson.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 5/2/2016.
 */
public class BlockItems {

    private List<BlockedList> BlockedList = new ArrayList<BlockedList>();
    private Boolean IsSucceeded;


    /**
     * @return The BlockedList
     */
    public List<BlockedList> getBlockedList() {
        return BlockedList;
    }

    /**
     * @param BlockedList The BlockedList
     */
    public void setBlockedList(List<BlockedList> BlockedList) {
        this.BlockedList = BlockedList;
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


    public class BlockedList {

        private String ID;
        private String UserName;
        private String Image;


        /**
         * @return The ID
         */
        public String getID() {
            return ID;
        }

        /**
         * @param ID The ID
         */
        public void setID(String ID) {
            this.ID = ID;
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
         * @return The Image
         */
        public String getImage() {
            return Image;
        }

        /**
         * @param Image The Image
         */
        public void setImage(String Image) {
            this.Image = Image;
        }

    }

}