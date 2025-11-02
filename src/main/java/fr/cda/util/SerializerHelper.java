package fr.cda.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Utility class for general serialisation necessity
 */
public abstract class SerializerHelper
{
    public static String[] ReadFile(final String filePath) throws IOException
    {
        LoggerHelper.log.info("TRY : to read file at path {}", filePath);
        Path path = Path.of(filePath);

        if (!Files.exists(path))
        {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        List<String> lines = Files.readAllLines(Path.of(filePath));
        return lines.toArray(String[]::new);
    }

    public static boolean SerializeToFile(final String content, final String path, final String fileName)
    {
        LoggerHelper.log.info("TRY : to serialize file {} at path {}", fileName, path);

        if (!EnsureOrCreatePath(path))
        {
            LoggerHelper.log.error("FAIL : to ensure/create path : " + path);
            return false;
        }

        Path filePath = Path.of(path, fileName);

        try
        {
            Files.writeString
                    (
                    filePath,
                    content,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
                    );

            LoggerHelper.log.info("SUCCESS : File written to : " + filePath.toAbsolutePath());
        }
        catch (IOException e)
        {
            LoggerHelper.log.error("FAIL : while writing file : " + filePath + " | " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Util method used to ensure that a path exist. If not, this method will generate it
     * @param filePath The path you want to ensure his existence
     * @return True in case of success, false if an issue occurred during the process
     */
    public static boolean EnsureOrCreatePath(final String filePath)
    {
        LoggerHelper.log.info("TRY to ensure/create path : " + filePath);
        Path path = Paths.get(filePath);

        try
        {
            Files.createDirectories(path);
            LoggerHelper.log.info("Creation of path SUCCEED to path : " + filePath);
            return true;

        } catch (IOException e)
        {
            LoggerHelper.log.error("Creation of path FAIL to path : " + filePath);
            return false;
        }
    }
}
