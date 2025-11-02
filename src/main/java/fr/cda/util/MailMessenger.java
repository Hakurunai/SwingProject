package fr.cda.util;

import fr.cda.Config;
import fr.cda.model.OperationResult;
import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class used to manipulate Send Blue dependency
 */
public abstract class MailMessenger
{
    /**
     * Method to send a mail who can contain some attachments
     * @param targetMail The mail of the receiver
     * @param targetName The name of the receiver
     * @param senderMail The mail of the sender
     * @param senderName The name of the sender
     * @param subject The subject of the mail
     * @param content The content of the mail
     * @param attachmentPath An array of Path allowing us to attach some file to the mail
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public static OperationResult<Void> SendMail(final String targetMail, final String targetName,
                                                 final String senderMail, final String senderName,
                                                 final String subject, final String content,
                                                 final Path[] attachmentPath)
    {
        LoggerHelper.log.info("TRY : send a mail from {} to {}", senderMail, targetMail);

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        // Configure API key authorization : api-key
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(Config.SEND_BLUE_API_KEY);
        try
        {
            TransactionalEmailsApi api = new TransactionalEmailsApi();

            // Sender
            SendSmtpEmailSender smtpSender = new SendSmtpEmailSender();
            smtpSender.setEmail(senderMail);
            smtpSender.setName(senderName);

            // Target
            List<SendSmtpEmailTo> toList = new ArrayList<>();
            SendSmtpEmailTo smtpTarget = new SendSmtpEmailTo();
            smtpTarget.setEmail(targetMail);
            smtpTarget.setName(targetName);
            toList.add(smtpTarget);

            // Content
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(smtpSender);
            sendSmtpEmail.setTo(toList);
            sendSmtpEmail.setSubject(subject);
            sendSmtpEmail.setTextContent(content);

            // Attachment
            List<SendSmtpEmailAttachment> attachmentList = new ArrayList<>();
            if (attachmentPath != null)
            {
                for (Path path : attachmentPath)
                {
                    SendSmtpEmailAttachment attachment = new SendSmtpEmailAttachment();

                    attachment.setName(path.getFileName().toString());
                    byte[] encode = Files.readAllBytes(path);
                    attachment.setContent(encode);
                    attachmentList.add(attachment);
                }
            }
            sendSmtpEmail.setAttachment(attachmentList);


            // Sending
            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);

            LoggerHelper.log.info("SUCCESS : send a mail from {} to {}", senderMail, targetMail);
            return OperationResult.SUCCESS("SUCCESS : Mail sent successfully");
        }
        catch (Exception e)
        {
            LoggerHelper.log.error("FAIL : Sending mail : " + e.getMessage());
            e.printStackTrace();
            return OperationResult.FAILURE("FAIL : Sending mail : " + e.getMessage());
        }
    }
}
