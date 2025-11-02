package fr.cda.util;

import fr.cda.model.OperationResult;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * An collection of method able to let us transfer some data to a remote FTP server
 */
public abstract class FTPSender
{
    /**
     * Serve to send an array of data to a server via FTP
     * @param fileToSend The paths of the files you want to send
     * @param serverUrl The url of the server
     * @param serverUsername The username to use to log in
     * @param serverPassword The password to use, in correlation of the username, to log in
     * @param remoteServerPath The path on the remote server where you want to put your data
     * @param port The port use for the connection
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public static OperationResult<Void> SendData(final String[] fileToSend, final String serverUrl, final String serverUsername,
                                           final String serverPassword, final String remoteServerPath, final int port)
    {
        LoggerHelper.log.info("TRY : enabling a connection to FTP server.");
        FTPClient ftpClient = new FTPClient();

        try
        {
            ftpClient.connect(serverUrl, port);
            boolean success = ftpClient.login(serverUsername, serverPassword);

            if (!success)
            {
                final String localErrorMessage = "Connection failed -> check your id";
                LoggerHelper.log.error("ERROR : {}", localErrorMessage);
                return OperationResult.FAILURE(localErrorMessage);
            }

            // binary mode to avoid corruption. Every file format will work with this mode.
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Useful if protected by firewall/NAT
            ftpClient.enterLocalPassiveMode();

            final Path remotePATH = Paths.get(remoteServerPath);
            Path remoteCompletePath;
            for (String path : fileToSend)
            {
                remoteCompletePath = Paths.get(remotePATH.toString(), path);

                CreateDirToDistantRepo(ftpClient, remoteCompletePath.getParent().toString());

                LoggerHelper.log.info("START Sending to FTP server file : {}", remoteCompletePath.getFileName());
                try (InputStream inputStream = new FileInputStream(path))
                {
                    //FTP need a path using / ONLY. But Path.get can generate a path with / or \ depending on the OS
                    final String FTP_PATH = remoteCompletePath.toString().replace("\\", "/");
                    if (!ftpClient.storeFile(FTP_PATH, inputStream))
                    {
                        final String localErrorMessage = "ERROR : while sending file : " + remoteCompletePath.getFileName();
                        LoggerHelper.log.error(localErrorMessage);
                        return OperationResult.FAILURE(localErrorMessage);
                    }
                    else
                    {
                        LoggerHelper.log.info("SUCCESS : file {} was successfully sent", remoteCompletePath.getFileName());
                    }
                }
            }

            // Disconnection
            ftpClient.logout();
            ftpClient.disconnect();

            return OperationResult.SUCCESS("SUCCESS : All file are successfully sent");
        }
        catch (IOException ex)
        {
            final String localErrorMessage = "ERROR : Occurred while sending file to FTP server.";
            LoggerHelper.log.error(localErrorMessage);
            ex.printStackTrace();
            return OperationResult.FAILURE(localErrorMessage);
        }
    }

    /**
     * Use to ensure ourselves that a valid directory is existing on the remote before we send our data to it
     * @param ftpClient The client on which we are connected
     * @param dirPath The path we want to ensure existence on the remote server
     */
    private static void CreateDirToDistantRepo(final FTPClient ftpClient, final String dirPath)
    {
        LoggerHelper.log.info("START : verifying existing dir on FTP server for : " + dirPath);
        String[] dirs = dirPath.split("[/\\\\]+");
        String currentPath = "";

        for (String dir : dirs)
        {
            if (dir == null || dir.trim().isEmpty()) continue;
            //We explicitly use here a /, cause FTP server WON'T WORK with \
            currentPath += "/" + dir;

            try
            {
                if (!ftpClient.changeWorkingDirectory(currentPath))
                {
                    LoggerHelper.log.info("START : Creating new dir on FTP server : " + currentPath);
                    ftpClient.makeDirectory(currentPath);
                    ftpClient.changeWorkingDirectory(currentPath);
                }
            }
            catch (IOException e)
            {
                LoggerHelper.log.error("ERROR : Occurred while trying to create a directory on the FTP server.");
                e.printStackTrace();
            }
        }
    }
}
