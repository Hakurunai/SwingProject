package fr.cda.model;

import fr.cda.util.LoggerHelper;

/**
 * Simple class able to keep track of a number overtime
 * Use by {@link Database} to generate unique ID
 */
public class DataIdGenerator
{
    private long currentId;

    public DataIdGenerator()
    {
        this.currentId = 0;
    }

    /**
     * Way to generate the new value for an id
     * @return a new id, each call to this method increment the internal count by +1
     */
    public final long GetNextId()
    {
        LoggerHelper.log.info("GENERATE : a new id is generated, long value : {}", ++currentId);
        return currentId;
    }

    /**
     * Way to generate the new value for an id
     * @return a new id in String, each call to this method increment the internal count by +1
     */
    public final String GetNextIdAsString()
    {
        return String.valueOf(GetNextId());
    }

    /**
     * Allow to update the internal currentId ONLY if the value pass as parameter is greater
     * @param value The value we want to at least initialized internal currentId to
     */
    public void TryUpdateCurrentId(final long value)
    {
        if (value <= currentId)
            return;

        currentId = value;
    }
}
