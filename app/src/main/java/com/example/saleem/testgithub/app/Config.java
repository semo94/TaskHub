package com.example.saleem.testgithub.app;


public class Config {
    // server URL configuration
    public static final String URL_REQUEST_SMS = "http://www.taskhub.tk/semo94/TaskHub/APIs/request_sms.php";
    public static final String URL_VERIFY_OTP = "http://www.taskhub.tk/semo94/TaskHub/APIs/verify_otp.php";
    public static final String Get_UserInfo = "http://www.taskhub.tk/semo94/TaskHub/API/Get_UserInfo.php";
    public static final String UserInfo = "http://www.taskhub.tk/semo94/TaskHub/API/UserInfo.php";
    public static final String GetMyContactsList = "http://www.taskhub.tk/semo94/TaskHub/API/GetMyContactsList.php";
    public static final String UploadContacts = "http://www.taskhub.tk/semo94/TaskHub/API/UploadContacts.php";
    public static final String Get_PendingList = "http://www.taskhub.tk/semo94/TaskHub/API/PendingToDoList.php";
    public static final String Get_UnderProgressList = "http://www.taskhub.tk/semo94/TaskHub/API/ProgToDoList.php";
    public static final String Get_MyNeedsList = "http://www.taskhub.tk/semo94/TaskHub/API/myNeedsList.php";
    public static final String Get_DeactivateAccount = "http://www.taskhub.tk/semo94/TaskHub/API/DeactivateAccount.php";
    public static final String Get_TaskDetails = "http://www.taskhub.tk/semo94/TaskHub/API/taskDetails.php?Taskid=";
    public static final String Get_BlockList = "http://www.taskhub.tk/semo94/TaskHub/API/GetBlockedList.php";
    public static final String Post_UnBlockUser = "http://www.taskhub.tk/semo94/TaskHub/API/UnBlockUser.php";
    public static final String Post_BlockUser = "http://www.taskhub.tk/semo94/TaskHub/API/BlockUser.php";

    public static final String Post_feedback = "http://www.taskhub.tk/semo94/TaskHub/API/feedback.php";


    public static final String Get_Notifications = "http://www.taskhub.tk/semo94/TaskHub/API/Allnotf.php";

    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "MSG";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";
}