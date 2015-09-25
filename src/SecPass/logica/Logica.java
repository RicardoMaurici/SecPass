package SecPass.logica;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import SecPass.gui.MainWindow;

/**
* @author Elanne Melilo de Souza 10101180
* @author Ricardo Maurici Ferreira 10201015
* Date 21/09/2015
*/

public class Logica {
	private static final int MAC_SIZE = 128;

	public String geraSalt() {
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

	public String geraHMAC(String entradaCifrada, String chaveDerivada) {
		String tag = "";
		try {
			int addProvider = Security.addProvider(new BouncyCastleProvider());

			if (Security.getProvider("BC") == null) {
				System.out.println("Bouncy Castle provider NAO disponivel");
			} else {
				System.out.println("Bouncy Castle provider esta disponivel");
			}

			SHA512Digest digest = new SHA512Digest();

			// Instancia o HMac e passa o digest como parâmetro
			HMac umHmac = new HMac(digest);

			byte[] resBuf1 = new byte[umHmac.getMacSize()];

			byte[] input;

			input = Hex.decodeHex(entradaCifrada.toCharArray());

			// Define a chave do HMac
			KeyParameter key1 = new KeyParameter(Hex.decodeHex(chaveDerivada.toCharArray()));
			umHmac.init(key1);

			// Define a entrada do HMac
			umHmac.update(input, 0, input.length);

			// Define o buffer para colocar o resultado do HMac, resBuf1 é a tag
			umHmac.doFinal(resBuf1, 0);

			System.out.println("Mensagem 1 = " + Hex.encodeHexString(input));
			tag = new String(Hex.encodeHex(resBuf1));
			System.out.println("HMAC da Mensagem 1 = "+ new String(Hex.encodeHex(resBuf1)));
		} catch (DecoderException e) {
			e.printStackTrace();
		}

		return tag;

	}

	public static String geraChaveDerivada(String key, String salt,
			Integer iterations) {
		PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(),
				iterations, 128);
		SecretKeyFactory pbkdf2 = null;
		String senhaDerivada = null;
		try {
			pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			SecretKey sk = pbkdf2.generateSecret(spec);
			senhaDerivada = Hex.encodeHexString(sk.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return senhaDerivada;
	}

	public String cifraGCM(String pK, String textoPlano, String iv) throws DecoderException {
		//Recebe senha
		byte[] K = pK.getBytes();
		//Recebe texto que deseja cifrar
		byte[] P = textoPlano.getBytes(); // Msg de entrada
		//IV
		byte[] N = org.apache.commons.codec.binary.Hex.decodeHex(iv.toCharArray());

		// Instancia um GCM com AES usando o formato da BouncyCastle
		GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());

		// Parametros a passados para o GCM: chave, tamanho do mac, iv em bytes
		KeyParameter key2 = new KeyParameter(K);
		AEADParameters params = new AEADParameters(key2, MAC_SIZE, N);

		// cifrando
		gcm.init(true, params);
		int outsize = gcm.getOutputSize(P.length);
		byte[] outc = new byte[outsize];
		
		// processa os bytes calculando o offset para cifrar
		int lengthOutc = gcm.processBytes(P, 0, P.length, outc, 0);

		try {
			// cifra os bytes
			gcm.doFinal(outc, lengthOutc);
		} catch (InvalidCipherTextException e) {
			e.printStackTrace();
		}

		System.out.println("Senha cifrada = \t\t\t" + Hex.encodeHexString(outc));

		// Recupera tag do GCM
		byte[] encT1 = gcm.getMac();
		System.out.println("Tag msg cifrada = \t\t" + Hex.encodeHexString(encT1));
		
		return Hex.encodeHexString(outc);

	}

	public String decifraGCM(String pK, String iv, String textoCifrado, MainWindow ui) throws Exception {
		// IV
		byte[] N = org.apache.commons.codec.binary.Hex.decodeHex(iv.toCharArray());
		
		byte[] txtCifrado = org.apache.commons.codec.binary.Hex.decodeHex(textoCifrado.toCharArray());
		GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());

		// Parametros passados para o GCM: chave, tamanho do mac, o IV
		KeyParameter key2 = new KeyParameter(Hex.decodeHex(pK.toCharArray()));
		AEADParameters params = new AEADParameters(key2, MAC_SIZE, N);

		// DECIFRAR usando GCMBlockCipher - false para decifrar
		gcm.init(false, params);

		int outsize2 = gcm.getOutputSize(txtCifrado.length);
		byte[] out2 = new byte[outsize2];
		int offOut2 = gcm.processBytes(txtCifrado, 0, txtCifrado.length,
				out2, 0);
		try {
			gcm.doFinal(out2, offOut2);

		} catch (InvalidCipherTextException e) {
			ui.informaMsg("Senha inválida");
		}

		System.out.println("Msg decifrada = \t\t" + (Hex.encodeHexString(out2)));
		return Hex.encodeHexString(out2);
	}
}
