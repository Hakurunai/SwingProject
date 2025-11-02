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

public abstract class MailMessenger
{
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

//            TransactionalEmailsApi api = new TransactionalEmailsApi();
//            SendSmtpEmailSender sender = new SendSmtpEmailSender();
//            sender.setEmail("johngreta904@gmail.com");
//            sender.setName("John Doe");
//            List<SendSmtpEmailTo> toList = new ArrayList<SendSmtpEmailTo>();
//            SendSmtpEmailTo to = new SendSmtpEmailTo();
//            to.setEmail("laubert.yoann@gmail.com");
//            to.setName("John Doe");
//            toList.add(to);
//            List<SendSmtpEmailCc> ccList = new ArrayList<SendSmtpEmailCc>();
//            SendSmtpEmailCc cc = new SendSmtpEmailCc();
//            cc.setEmail("example1@example.com");
//            cc.setName("Janice Doe");
//            ccList.add(cc);
//            List<SendSmtpEmailBcc> bccList = new ArrayList<SendSmtpEmailBcc>();
//            SendSmtpEmailBcc bcc = new SendSmtpEmailBcc();
//            bcc.setEmail("example2@example.com");
//            bcc.setName("John Doe");
//            bccList.add(bcc);
//            SendSmtpEmailReplyTo replyTo = new SendSmtpEmailReplyTo();
//            replyTo.setEmail("replyto@domain.com");
//            replyTo.setName("John Doe");
////            SendSmtpEmailAttachment attachment = new SendSmtpEmailAttachment();
////            attachment.setName("test.jpg");
////            byte[] encode = Files.readAllBytes(Paths.get("local_filepath\\test.jpg"));
////            attachment.setContent(encode);
////            List<SendSmtpEmailAttachment> attachmentList = new ArrayList<SendSmtpEmailAttachment>();
////            attachmentList.add(attachment);
//            Properties headers = new Properties();
//            headers.setProperty("Some-Custom-Name", "unique-id-1234");
//            Properties params = new Properties();
//            params.setProperty("parameter", "My param value");
//            params.setProperty("subject", "New Subject");
//            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
//            sendSmtpEmail.setSender(sender);
//            sendSmtpEmail.setTo(toList);
//            sendSmtpEmail.setCc(ccList);
//            sendSmtpEmail.setBcc(bccList);
//            sendSmtpEmail.setHtmlContent("<html><body><h1>This is my first transactional email {{params.parameter}}</h1></body></html>");
//            sendSmtpEmail.setSubject("My {{params.subject}}");
//            sendSmtpEmail.setReplyTo(replyTo);
////            sendSmtpEmail.setAttachment(attachmentList);
//            sendSmtpEmail.setHeaders(headers);
//            sendSmtpEmail.setParams(params);
//            List<SendSmtpEmailTo1> toList1 = new ArrayList<SendSmtpEmailTo1>();
//            SendSmtpEmailTo1 to1 = new SendSmtpEmailTo1();
//            to1.setEmail("example1@example.com");
//            to1.setName("John Doe");
//            toList1.add(to1);
//            List<SendSmtpEmailMessageVersions> messageVersions = new ArrayList<>();
//            SendSmtpEmailMessageVersions versions1 = new SendSmtpEmailMessageVersions();
//            SendSmtpEmailMessageVersions versions2 = new SendSmtpEmailMessageVersions();
//            versions1.to(toList1);
//            versions2.to(toList1);
//            messageVersions.add(versions1);
//            messageVersions.add(versions2);
//            sendSmtpEmail.setMessageVersions(messageVersions);
//            sendSmtpEmail.setTemplateId(1L);
//            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
//            System.out.println(response.toString());

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
