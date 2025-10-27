package fr.cda.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataIdGeneratorTest
{
    DataIdGenerator idGenerator;

    @BeforeEach
    void setUp()
    {
        idGenerator = new DataIdGenerator();
    }

    @Test
    void getNextId()
    {
        final int NB_ID = 10;

        long[] multipleId = new long[NB_ID];
        for (int i = 0; i < NB_ID; i++)
        {
            multipleId[i] = idGenerator.GetNextId();
            assertTrue(multipleId[i] > 0, "Each generated ID must be greater than zero");
            if (i > 0)
            {
                assertTrue(multipleId[i-1] < multipleId[i], "Each generated ID must be greater than the previous one");
            }
        }
    }
}