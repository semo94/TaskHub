package com.example.saleem.testgithub.app;


public class Config {
    // server URL configuration
    public static final String URL_REQUEST_SMS = "http://www.taskhub.tk/semo94/TaskHub/APIs/request_sms.php";
    public static final String URL_VERIFY_OTP = "http://www.taskhub.tk/semo94/TaskHub/APIs/verify_otp.php";
    public static final String Get_UserInfo = "http://www.taskhub.tk/semo94/TaskHub/API/Get_UserInfo.php";
    public static final String GetMyContactsList = "http://www.taskhub.tk/semo94/TaskHub/API/GetMyContactsList.php";
    public static final String UploadContacts = "http://www.taskhub.tk/semo94/TaskHub/API/UploadContacts.php";
    public static final String Get_PendingList = "http://www.taskhub.tk/semo94/TaskHub/API/PendingToDoList.php";
    public static final String Get_UnderProgressList = "http://www.taskhub.tk/semo94/TaskHub/API/UnderProgressToDoList.php";
    public static final String Get_MyNeedsList = "http://www.taskhub.tk/semo94/TaskHub/API/myNeedsList.php";


    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "MSG";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";
}