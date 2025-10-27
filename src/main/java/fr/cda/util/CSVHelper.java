package fr.cda.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class CSVHelper
{
    public static String[] ReadCsvFile(final String filePath) throws IOException
    {
        List<String> lines = Files.readAllLines(Path.of(filePath));
        return lines.toArray(String[]::new);
    }
}
