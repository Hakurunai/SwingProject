package fr.cda.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class LoggerHelper
{
    /**
     * static getter allowing the usage of the log4J logger
     */
    public static final Logger log = LogManager.getLogger(LoggerHelper.class);
}
