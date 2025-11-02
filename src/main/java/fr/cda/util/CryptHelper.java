package fr.cda.util;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;

public class CryptHelper
{
    public static void main(String[] args) throws Exception {
        // Initialise Tink (nécessaire une seule fois)
        AeadConfig.register();

        // Crée ou charge une clé
        KeysetHandle keysetHandle = KeysetHandle.generateNew(AeadKeyTemplates.AES256_GCM);

        // Récupère un AEAD (Authenticated Encryption with Associated Data)
        Aead aead = keysetHandle.getPrimitive(Aead.class);

        byte[] plaintext = "Secret document".getBytes();
        byte[] associatedData = "metadata".getBytes(); // facultatif

        // Chiffrement
        byte[] ciphertext = aead.encrypt(plaintext, associatedData);

        // Déchiffrement
        byte[] decrypted = aead.decrypt(ciphertext, associatedData);

        System.out.println(new String(decrypted));
    }
}
