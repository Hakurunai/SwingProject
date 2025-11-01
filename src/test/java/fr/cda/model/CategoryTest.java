package fr.cda.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CategoryTest
{
    Category catDefault;
    Category equalToDefault;
    Category differentFromDefault;

    @BeforeEach
    void setUp()
    {
        catDefault = new Category("Livre");
        equalToDefault = new Category("Livre");
        differentFromDefault = new Category("DVD");
    }

    @Test
    void testEquals()
    {
        assertEquals(catDefault, equalToDefault, "Different instance with the same internal String content must be equal");
        assertNotEquals(catDefault, differentFromDefault, "Any difference between the internal String content must lead to a non equality");
    }

    @Test
    void testHashCode()
    {
        assertEquals(catDefault.hashCode(), equalToDefault.hashCode(), "Different instance with the same internal String content must be equal");
        assertNotEquals(catDefault.hashCode(), differentFromDefault.hashCode(), "Any difference between the internal String content must lead to a non equality");
    }
}