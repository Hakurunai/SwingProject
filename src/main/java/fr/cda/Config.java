package fr.cda;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * His role is to retrieve all the data contained in the .env file and hide the real value from the code
 */
public abstract class Config
{
    private final static Dotenv dotenv;

    public static final String DB_ORDER_FILE_PATH;
    public static final String DB_PRODUCT_FILE_PATH;


    public static final String OUTPUT_FILE_PATH;
    public static final String REVIEW_ORDER_FILE_NAME;
    public static final String OUTPUT_PRODUCT_FILE_NAME;
    public static final String OUTPUT_ORDER_FILE_NAME;


    public static final String TINK_CRYPTED_KEY;
    public static final String OUTPUT_CRYPTED_FILE_PATH;
    public static final String OUTPUT_CRYPTED_PRODUCT_FILE_NAME;
    public static final String OUTPUT_CRYPTED_ORDER_FILE_NAME;


    public static final String SEND_BLUE_API_KEY;
    public static final String SEND_BLUE_MAIL_TARGET_MAIL;
    public static final String SEND_BLUE_MAIL_TARGET_NAME;
    public static final String SEND_BLUE_MAIL_SENDER_MAIL;
    public static final String SEND_BLUE_MAIL_SENDER_NAME;


    public static final String FTP_SERVER_URL;
    public static final String FTP_SERVER_USERNAME;
    public static final String FTP_SERVER_PASSWORD;
    public static final String FTP_SERVER_PATH_TO_SEND;


    static
    {
        dotenv = Dotenv.load();

        DB_ORDER_FILE_PATH = dotenv.get("DB_ORDER_FILE_PATH");
        DB_PRODUCT_FILE_PATH = dotenv.get("DB_PRODUCT_FILE_PATH");


        OUTPUT_FILE_PATH = dotenv.get("OUTPUT_FILE_PATH");
        REVIEW_ORDER_FILE_NAME = dotenv.get("REVIEW_ORDER_FILE_NAME");
        OUTPUT_PRODUCT_FILE_NAME = dotenv.get("OUTPUT_PRODUCT_FILE_NAME");
        OUTPUT_ORDER_FILE_NAME = dotenv.get("OUTPUT_ORDER_FILE_NAME");


        TINK_CRYPTED_KEY = dotenv.get("TINK_CRYPTED_KEY");
        OUTPUT_CRYPTED_FILE_PATH = dotenv.get("OUTPUT_CRYPTED_FILE_PATH");
        OUTPUT_CRYPTED_PRODUCT_FILE_NAME = dotenv.get("OUTPUT_CRYPTED_PRODUCT_FILE_NAME");
        OUTPUT_CRYPTED_ORDER_FILE_NAME = dotenv.get("OUTPUT_CRYPTED_ORDER_FILE_NAME");


        SEND_BLUE_API_KEY = dotenv.get("SEND_BLUE_API_KEY");
        SEND_BLUE_MAIL_TARGET_MAIL = dotenv.get("SEND_BLUE_MAIL_TARGET_MAIL");
        SEND_BLUE_MAIL_TARGET_NAME = dotenv.get("SEND_BLUE_MAIL_TARGET_NAME");
        SEND_BLUE_MAIL_SENDER_MAIL = dotenv.get("SEND_BLUE_MAIL_SENDER_MAIL");
        SEND_BLUE_MAIL_SENDER_NAME = dotenv.get("SEND_BLUE_MAIL_SENDER_NAME");


        FTP_SERVER_URL = dotenv.get("FTP_SERVER_URL");
        FTP_SERVER_USERNAME = dotenv.get("FTP_SERVER_USERNAME");
        FTP_SERVER_PASSWORD = dotenv.get("FTP_SERVER_PASSWORD");
        FTP_SERVER_PATH_TO_SEND = dotenv.get("FTP_SERVER_PATH_TO_SEND");
    }
}
