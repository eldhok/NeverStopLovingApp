package com.stoplovingnever.nest.app;

/**
 * Created by Lincoln on 06/01/16.
 */
public class EndPoints {

    // localhost url
    // public static final String BASE_URL = "http://192.168.0.101/gcm_chat/v1";
   // ifconfig

    //new  IP Address 31.170.165.167 hostinger

   public static final String BASE_URL_PAYTM = "http://52.26.199.108/Paytm_App_Checksum_Kit_PHP-master";
  //  public static final String BASE_URL_PAYTM = "http://neverstoploving.esy.es/Paytm_App_Checksum_Kit_PHP-master";
  // public static final String BASE_URL_PAYTM = "http://192.168.1.100/Paytm_App_Checksum_Kit_PHP-master";
    public static final String V_C = BASE_URL_PAYTM + "/verifyChecksum.php";
    public static final String G_C = BASE_URL_PAYTM + "/generateChecksum.php";



   public static final String BASE_URL = "http://52.26.199.108/gcm_chat/v1";
    //public static final String BASE_URL = "http://neverstoploving.esy.es/gcm_chat/v1";
   // public static final String BASE_URL = "http://192.168.1.100/gcm_chat/v1";
    public static final String LOGIN = BASE_URL + "/user/login";
    public static final String USER = BASE_URL + "/user/_ID_";
    public static final String CHAT_ROOMS = BASE_URL + "/chat_rooms";
    public static final String CHAT_THREAD = BASE_URL + "/chat_rooms/_ID_";
    public static final String CHAT_ROOM_MESSAGE = BASE_URL + "/chat_rooms/_ID_/message";

    public static final String OTP = BASE_URL + "/user/otp";
    public static final String MSG = BASE_URL + "/msg_conf";

    public static final String CHAT_FRIENDS = BASE_URL + "/friends";
    public static final String LIKE = BASE_URL + "/like";
    public static final String BLOCK = BASE_URL + "/block";
    public static final String USER_VALID = BASE_URL + "/valid_user";
    public static final String COMPLIANT = BASE_URL + "/compliant";


    public static final String CHAT_FRIENDS_LIST = BASE_URL + "/friends_list";
    public static final String CHAT_PAYMENT = BASE_URL + "/payment";
    public static final String CHAT_MSG_LEFT = BASE_URL + "/msg_left";


    public static final String CRUSH_REQ = BASE_URL + "/user/crush_req";
    public static final String TEST = BASE_URL + "/user/test";


    //http://romannurik.github.io/AndroidAssetStudio/icons-launcher.html

/*
    paytm


Used ID is                  :    MakingInnovations

Password is                :    nb0QQqj

https://pguat.paytm.com/PayTMSecured/app/auth/login

 Merchant ID             :   Making42857475470515

 Merchant Panel Link    :   https://pguat.paytm.com/PayTMSecured/app/auth/login


  */
}
