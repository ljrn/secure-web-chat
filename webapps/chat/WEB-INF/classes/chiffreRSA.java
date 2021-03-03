

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class chiffreRSA {
    
    public static byte[] chiffre(byte[] message) throws Exception {

	// Lecture des octets de la clé publique depuis le fichier
	byte[] bytesPub = Files.readAllBytes(Paths.get("chatSecurise.pub"));
 
	// regenere la clé publique en mémoire
	X509EncodedKeySpec ksPub = new X509EncodedKeySpec(bytesPub);
	KeyFactory kfPub = KeyFactory.getInstance("RSA");
	PublicKey pubKey = kfPub.generatePublic(ksPub);
        	     
	// chiffre le message avec la clé publique   
	byte [] encrypted = encrypt(pubKey, message);             
	return encrypted;
    }

public static byte[] encrypt(PublicKey pubKey, byte[] message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);  

        return cipher.doFinal(message);  
    } 
}
