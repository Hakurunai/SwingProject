package fr.cda;

import io.github.cdimascio.dotenv.Dotenv;

public abstract class Config
{
    private final static Dotenv dotenv;

    public static final String DB_ORDER_FILE_PATH;
    public static final String DB_PRODUCT_FILE_PATH;

    public static final String REVIEW_ORDER_FILE_NAME;
    public static final String REVIEW_ORDER_FILE_PATH;

    static
    {
        dotenv = Dotenv.load();

        DB_ORDER_FILE_PATH = dotenv.get("DB_ORDER_FILE_PATH");
        DB_PRODUCT_FILE_PATH = dotenv.get("DB_PRODUCT_FILE_PATH");

        REVIEW_ORDER_FILE_PATH = dotenv.get("REVIEW_ORDER_FILE_PATH");
        REVIEW_ORDER_FILE_NAME = dotenv.get("REVIEW_ORDER_FILE_NAME");
    }
}
