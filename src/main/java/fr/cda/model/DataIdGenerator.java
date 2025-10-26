package fr.cda.model;

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
    public long GetNextId()
    {
        return ++currentId;
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
