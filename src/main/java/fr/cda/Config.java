package fr.cda;

import io.github.cdimascio.dotenv.Dotenv;

public abstract class Config
{
    private final static Dotenv dotenv;

    public static final String DB_ORDER_FILE_PATH;
    public static final String DB_PRODUCT_FILE_PATH;

    public static final String REVIEW_ORDER_FILE_NAME;
    public static final String REVIEW_ORDER_FILE_PATH;

    public static final String SEND_BLUE_API_KEY;
    public static final String SEND_BLUE_MAIL_TARGET_MAIL;
    public static final String SEND_BLUE_MAIL_TARGET_NAME;
    public static final String SEND_BLUE_MAIL_SENDER_MAIL;
    public static final String SEND_BLUE_MAIL_SENDER_NAME;

    public static final String OUTPUT_PRODUCT_FILE_PATH;
    public static final String OUTPUT_PRODUCT_FILE_NAME;
    public static final String OUTPUT_ORDER_FILE_PATH;
    public static final String OUTPUT_ORDER_FILE_NAME;

    static
    {
        dotenv = Dotenv.load();

        DB_ORDER_FILE_PATH = dotenv.get("DB_ORDER_FILE_PATH");
        DB_PRODUCT_FILE_PATH = dotenv.get("DB_PRODUCT_FILE_PATH");

        REVIEW_ORDER_FILE_PATH = dotenv.get("REVIEW_ORDER_FILE_PATH");
        REVIEW_ORDER_FILE_NAME = dotenv.get("REVIEW_ORDER_FILE_NAME");

        SEND_BLUE_API_KEY = dotenv.get("SEND_BLUE_API_KEY");
        SEND_BLUE_MAIL_TARGET_MAIL = dotenv.get("SEND_BLUE_MAIL_TARGET_MAIL");
        SEND_BLUE_MAIL_TARGET_NAME = dotenv.get("SEND_BLUE_MAIL_TARGET_NAME");
        SEND_BLUE_MAIL_SENDER_MAIL = dotenv.get("SEND_BLUE_MAIL_SENDER_MAIL");
        SEND_BLUE_MAIL_SENDER_NAME = dotenv.get("SEND_BLUE_MAIL_SENDER_NAME");

        OUTPUT_PRODUCT_FILE_PATH  = dotenv.get("OUTPUT_PRODUCT_FILE_PATH");
        OUTPUT_PRODUCT_FILE_NAME = dotenv.get("OUTPUT_PRODUCT_FILE_NAME");
        OUTPUT_ORDER_FILE_PATH = dotenv.get("OUTPUT_ORDER_FILE_PATH");
        OUTPUT_ORDER_FILE_NAME = dotenv.get("OUTPUT_ORDER_FILE_NAME");
    }
}
