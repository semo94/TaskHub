package com.example.saleem.testgithub.gson.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 5/2/2016.
 */
public class NotificationsItems {
    private List<AllNotification> AllNotifications = new ArrayList<AllNotification>();
    private Boolean IsSucceeded;

    /**
     *
     * @return
     * The AllNotifications
     */
    public List<AllNotification> getAllNotifications() {
        return AllNotifications;
    }

    /**
     *
     * @param AllNotifications
     * The AllNotifications
     */
    public void setAllNotifications(List<AllNotification> AllNotifications) {
        this.AllNotifications = AllNotifications;
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

    public class AllNotification {

        private String nid;
        private String receiverId;
        private String senderId;
        private String body;
        private String isRead;
        private String ImageUrl;

        /**
         *
         * @return
         * The nid
         */
        public String getNid() {
            return nid;
        }

        /**
         *
         * @param nid
         * The nid
         */
        public void setNid(String nid) {
            this.nid = nid;
        }

        /**
         *
         * @return
         * The receiverId
         */
        public String getReceiverId() {
            return receiverId;
        }

        /**
         *
         * @param receiverId
         * The receiver-id
         */
        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        /**
         *
         * @return
         * The senderId
         */
        public String getSenderId() {
            return senderId;
        }

        /**
         *
         * @param senderId
         * The sender-id
         */
        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        /**
         *
         * @return
         * The body
         */
        public String getBody() {
            return body;
        }

        /**
         *
         * @param body
         * The body
         */
        public void setBody(String body) {
            this.body = body;
        }

        /**
         *
         * @return
         * The isRead
         */
        public String getIsRead() {
            return isRead;
        }

        /**
         *
         * @param isRead
         * The is_read
         */
        public void setIsRead(String isRead) {
            this.isRead = isRead;
        }

        /**
         *
         * @return
         * The ImageUrl
         */
        public String getImageUrl() {
            return ImageUrl;
        }

        /**
         *
         * @param ImageUrl
         * The ImageUrl
         */
        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }


    }

}
