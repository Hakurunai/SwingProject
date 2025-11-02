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
    /**
     * Read a file after ensuring his existence and return his content as byte[]
     * @param filePath The path of the file to read (including his name and extension)
     * @return An array of byte returned by the method {@link Files#readAllBytes(Path)}
     * @throws IOException This exception can be thrown during the process
     */
    public static byte[] ReadFileAsByte(final String filePath) throws IOException
    {
        LoggerHelper.log.info("TRY : to read in binary file at path {}", filePath);
        Path path = Path.of(filePath);

        if (!Files.exists(path))
        {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        return Files.readAllBytes(Path.of(filePath));
    }


    /**
     * Read a file after ensuring his existence and return his content
     * @param filePath The path of the file to read (including his name and extension)
     * @return An array of String, each one corresponding to one line of the file
     * @throws IOException This exception can be thrown during the process
     */
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

    /**
     * Serialize an array of byte[] in a file
     * @param content The content of the file to serialize
     * @param path The path where you want to put the file
     * @param fileName The name of the generated file, including his extension
     * @return True in case of success, false otherwise
     */
    public static boolean SerializeToFile(final byte[] content, final String path, final String fileName)
    {
        LoggerHelper.log.info("TRY : to serialize file in binary {} at path {}", fileName, path);

        if (!EnsureOrCreatePath(path))
        {
            LoggerHelper.log.error("FAIL : to ensure/create path : " + path);
            return false;
        }
        Path filePath = Path.of(path, fileName);

        try
        {
            Files.write(filePath, content);
            return true;
        }
        catch (IOException e)
        {
            LoggerHelper.log.error("FAIL : to serialize file : {} at path : {}", fileName, path);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Serialize a String in a file
     * @param content The content of the file
     * @param path The path where you want to put the file
     * @param fileName The name of the generated file, including his extension
     * @return True in case of success, false otherwise
     */
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
            return false;
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
