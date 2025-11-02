package fr.cda.util;

import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

public abstract class CryptHelper
{
    static
    {
        try
        {
            AeadConfig.register();
        }
        catch (GeneralSecurityException e)
        {
            LoggerHelper.log.error("Issue while initializing AEAD for Tink crypting operation");
            e.printStackTrace();
        }
    }

    /**
     * Use to decipher a file crypted with Tink : IE crypted via {@link #EncryptFile(String, String, String, String)}
     * @param fileToDecrypt The file you want to decipher
     * @param keyToUse The key to decipher it. MAKE SURE this is the same as the one used to cipher the file
     * @return A String containing the data in clear from the cipher file
     * @throws GeneralSecurityException Could be thrown while using Tink
     * @throws IOException Could be thrown during operation on file
     */
    public static String DecryptFile(final String fileToDecrypt, final String keyToUse) throws GeneralSecurityException, IOException
    {
        final byte[] keyBytes = Base64.getDecoder().decode(keyToUse);

        KeysetHandle keysetHandle = CleartextKeysetHandle
                                            .read(BinaryKeysetReader
                                                          .withInputStream(new ByteArrayInputStream(keyBytes)));

        Aead aead = keysetHandle.getPrimitive(Aead.class);

        byte[] readCryptedFile = SerializerHelper.ReadFileAsByte(fileToDecrypt);

        byte[] clearFile = aead.decrypt(readCryptedFile, new byte[0]);
        String decryptedString = new String(clearFile, StandardCharsets.UTF_8);

        return decryptedString;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    /**
     * Use to crypt a file using Tink library in AES
     * @param fileToCrypt The file you want to crypt
     * @param cryptedPathLocation The desired location where you want to put the crypted file
     * @param cryptedFileName The name of the generated crypted file
     * @param keyToUse The key to use when doing cypher operation
     * @return True in case of success, false otherwise
     */
    public static boolean EncryptFile(final String fileToCrypt, final String cryptedPathLocation,
                                      final String cryptedFileName, final String keyToUse)
    {
        try
        {
            final byte[] keyBytes = Base64.getDecoder().decode(keyToUse);

            KeysetHandle keysetHandle = CleartextKeysetHandle
                                                .read(BinaryKeysetReader
                                                              .withInputStream(new ByteArrayInputStream(keyBytes)));

            Aead aead = keysetHandle.getPrimitive(Aead.class);

            byte[] readFile = SerializerHelper.ReadFileAsByte(fileToCrypt);
            byte[] cryptedFile = aead.encrypt(readFile, new byte[0]);
            SerializerHelper.SerializeToFile(cryptedFile, cryptedPathLocation, cryptedFileName);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Use to generate a local key in AES256 for cipher operation
     */
    public static void GenerateKey()
    {
        try
        {
            // Init
            AeadConfig.register();

            // Generate a key
            KeysetHandle keysetHandle = KeysetHandle.generateNew(AeadKeyTemplates.AES256_GCM);

            // Extract in binary
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            CleartextKeysetHandle.write(keysetHandle, BinaryKeysetWriter.withOutputStream(byteArrayOutputStream));

            // Encode in base64
            String keyBase64 = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

            // Copy-paste it
            System.out.println("GENERATED KEY : " + keyBase64);
        }
        catch (Exception p_e)
        {
            p_e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception
    {
        GenerateKey();
    }
}
