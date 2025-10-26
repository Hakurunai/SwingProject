package fr.cda.model;

/**
 * Representation of an id
 * Used to specified what is an id in the program, we cannot use any String
 */
public class ID
{
    private final String id;

    public ID(final String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ID other = (ID) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
