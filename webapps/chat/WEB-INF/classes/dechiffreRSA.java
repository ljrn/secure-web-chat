

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

public class dechiffreRSA {
    
    public static String dechiffre(byte[] encrypted) throws Exception {


	// Lecture des octets de la clé privée depuis le fichier
	byte[] bytesPriv = Files.readAllBytes(Paths.get("chatSecurise.priv"));

	// regenere la clé privée en mémoire
	PKCS8EncodedKeySpec ksPriv = new PKCS8EncodedKeySpec(bytesPriv);
	KeyFactory kfPriv = KeyFactory.getInstance("RSA");
	PrivateKey privateKey = kfPriv.generatePrivate(ksPriv);

        	     
	// dechiffre le message avec la clé privée   	
        byte[] secret = decrypt(privateKey, encrypted);                                 
        return new String(secret);     // Message secret
    }

public static byte[] decrypt(PrivateKey privateKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        return cipher.doFinal(encrypted);
	}
}
