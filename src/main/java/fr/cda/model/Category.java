package fr.cda.model;

public class Category
{
    private final String categoryName;

    public Category(final String name)
    {
        this.categoryName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Category other = (Category) o;
        return categoryName.equals(other.categoryName);
    }

    @Override
    public int hashCode() {
        return categoryName.hashCode();
    }

    public String getCategoryName()
    {
        return categoryName;
    }
}
