package fr.cda.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IDTest
{
    ID idDefault;
    ID equalToDefault;
    ID differentFromDefault;

    @BeforeEach
    void setUp()
    {
        idDefault = new ID("ABC-2");
        equalToDefault = new ID("ABC-2");
        differentFromDefault = new ID("ABC-3");
    }

    @AfterEach
    void tearDown()
    {}

    @Test
    void testEquals()
    {
        assertEquals(idDefault, equalToDefault, "Different instance with the same internal String content must be equal");
        assertNotEquals(idDefault, differentFromDefault, "Any difference between the internal String content must lead to a non equality");
    }

    @Test
    void testHashCode()
    {
        assertEquals(idDefault.hashCode(), equalToDefault.hashCode(), "Different instance with the same internal String content must be equal");
        assertNotEquals(idDefault.hashCode(), differentFromDefault.hashCode(), "Any difference between the internal String content must lead to a non equality");
    }
}