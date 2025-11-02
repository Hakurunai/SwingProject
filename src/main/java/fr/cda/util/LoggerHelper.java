package fr.cda.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Util class giving access to the log4j logger
 */
public abstract class LoggerHelper
{
    /**
     * static getter of the log4J logger
     */
    public static final Logger log = LogManager.getLogger(LoggerHelper.class);
}
