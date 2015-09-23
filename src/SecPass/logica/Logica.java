package SecPass.logica;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;

public class Logica {
	private static final int MAC_SIZE = 128;
	 
	public String geraSalt(){
		SecureRandom sr;
		byte[] salt = null;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
	        salt = new byte[16];
	        sr.nextBytes(salt);
	        
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return Hex.encodeHexString(salt);
	}
	
	public static String geraChaveDerivada(String key, String salt, Integer iterations) {
        PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), iterations, 128);
        SecretKeyFactory pbkdf2 = null;
        String senhaDerivada = null;
        try {
            pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            SecretKey sk = pbkdf2.generateSecret(spec);
            senhaDerivada = Hex.encodeHexString(sk.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return senhaDerivada;
    }
	public void cifraGCM(String pK, String textoPlano, String textoCifrado, String iv, String Tag) throws DecoderException{
        
        //  chave (K)
        //String pK = "feffe9928665731c6d6a8f9467308308feffe9928665731c6d6a8f9467308308";
        byte[] K = org.apache.commons.codec.binary.Hex.decodeHex(pK.toCharArray());
    
        //  texto plano (P)
        byte[] P = Hex.decodeHex(textoPlano.toCharArray()); // Mensagem de entrada
                
        /**
        String pP;
        pP = "d9313225f88406e5a55909c5aff5269a"
            + "86a7a9531534f7da2e4c303d8a318a72"
            + "1c3c0c95956809532fcf0e2449a6b525"
            + "b16aedf5aa0de657ba637b391aafd255";
        */
        //= org.apache.commons.codec.binary.Hex.decodeHex(pP.toCharArray());
        
        //  nonce (IV)
        //String pN;
        //pN = "cafebabefacedbaddecaf888";
        byte[] N = org.apache.commons.codec.binary.Hex.decodeHex(iv.toCharArray());
        
        //  tag (T)
        String tag;
        //= "b094dac5d93471bdec1a502270e3cc6c";
        
        //  texto cifrado (C)
        byte[] C = Hex.decodeHex(textoCifrado.toCharArray());
        
        /**
        String pC;
        pC = "522dc1f099567d07f47f37a32a84427d"
            + "643a8cdcbfe5c0c97598a2bd2555d1aa"
            + "8cb08e48590dbb3da7b08b1056828838"
            + "c5f61e6393ba7a0abcc9f662898015ad"
            + T;
            */
        //= org.apache.commons.codec.binary.Hex.decodeHex(pC.toCharArray());
               

                
 
        
        // CIFRAR criando GCMBlockCipher
        
        // Instancia um GCM com AES usando o formato da BouncyCastle
        GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());
        
        
        // Parametros a serem passados para o GCM: chave, tamanho do mac, o nonce
        KeyParameter key2 = new KeyParameter(K);        
        AEADParameters params = new AEADParameters(key2, MAC_SIZE, N);
        
        // true para cifrar
        gcm.init(true, params);
        int outsize = gcm.getOutputSize(P.length);
        byte[] outc = new byte[outsize];
        //processa os bytes calculando o offset para cifrar
        int lengthOutc = gcm.processBytes(P, 0, P.length, outc, 0);

        try {
            //cifra os bytes
            gcm.doFinal(outc, lengthOutc);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
        }
        
        System.out.println("Msg cifrada = \t\t\t"+Hex.encodeHexString(outc));
        
        // Recupera tag do GCM
        byte[] encT1 = gcm.getMac();
        System.out.println("Tag msg cifrada = \t\t"+Hex.encodeHexString(encT1));
        
           
        // tampering step - mudando o texto cifrado para ver se eh detectado!
        outc[11] ^= '0' ^ '9';
        System.out.println("A msg cifrada FOI MODIFICADA!!");
	}
    
	public void decifraGCM(String iv, String pK, byte[] textoCifrado) throws Exception
    {       
        
        //  nonce (IV)
        //String pN;
        //pN = "cafebabefacedbaddecaf888";
        byte[] N = org.apache.commons.codec.binary.Hex.decodeHex(iv.toCharArray());
        
        
        // Instancia um GCM com AES usando o formato da BouncyCastle
        GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());
        
        
        // Parametros a serem passados para o GCM: chave, tamanho do mac, o nonce
        KeyParameter key2 = new KeyParameter(Hex.decodeHex(pK.toCharArray()));        
        AEADParameters params = new AEADParameters(key2, MAC_SIZE, N);

        
        // DECIFRAR usando GCMBlockCipher
        // false para decifrar
        gcm.init(false, params);
        
        int outsize2 = gcm.getOutputSize(textoCifrado.length);
        byte[] out2 = new byte[outsize2];
        int offOut2 = gcm.processBytes(textoCifrado, 0, textoCifrado.length, out2, 0);
        try {
            gcm.doFinal(out2, offOut2);
            
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
        }
        
        byte[] encT2 = gcm.getMac();
        System.out.println("Tag msg cifrada modificada = \t"+Hex.encodeHexString(encT2));
        
        String decifrado = new String(out2);
        System.out.println("Msg decifrada = \t\t"+decifrado);
    }
}
