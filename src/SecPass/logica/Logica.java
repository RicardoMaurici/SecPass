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

	/* 
	 * Metodo: gera o Sal que sera utilizado na derivacao de senha
	*/
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

	/* 
	 * Metodo: gera o HMac usando o parametro chaveDerivada para chave do HMac e a entradaCifrada para entrada do HMac
	*/
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

			// Instancia o HMac, passando o digest
			HMac umHmac = new HMac(digest);

			// Define o tamanho do array de saida
			byte[] saida = new byte[umHmac.getMacSize()];

			//converte para byte
			byte[] entrada = Hex.decodeHex(entradaCifrada.toCharArray());

			// Define a chave do HMac
			KeyParameter chave = new KeyParameter(Hex.decodeHex(chaveDerivada.toCharArray()));
			umHmac.init(chave);

			// Define a entrada para o HMac
			umHmac.update(entrada, 0, entrada.length);

			// Define o buffer para colocar o resultado do HMac, saida � a tag
			umHmac.doFinal(saida, 0);

			//converte tag
			tag = new String(Hex.encodeHex(saida));
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return tag;
	}

	/* 
	 * Metodo: deriva a chave passada como parametro. Passa tambem o sal e o numero de 
	 * iteracoes como parametro para derivar a chava
	*/
	public String geraChaveDerivada(String senha, String sal, Integer iteracoes) {
		PBEKeySpec spec = new PBEKeySpec(senha.toCharArray(), sal.getBytes(), iteracoes, 128);
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

	/* 
	 * Metodo: cifra o texto plano com GCM
	*/
	public String cifraGCM(String pK, String textoPlano, String iv) throws DecoderException {
		//Recebe senha
		byte[] chave = org.apache.commons.codec.binary.Hex.decodeHex(pK.toCharArray());
		//Recebe texto que deseja cifrar
		byte[] plano = textoPlano.getBytes(); // Msg de entrada
		//IV
		byte[] ivB = org.apache.commons.codec.binary.Hex.decodeHex(iv.toCharArray());

		// Instancia um GCM com AES usando o formato da BouncyCastle
		GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());

		// Parametros passados para o GCM: chave, tamanho do mac, iv em bytes
		KeyParameter chave2 = new KeyParameter(chave);
		AEADParameters parametros = new AEADParameters(chave2, MAC_SIZE, ivB);

		// cifrando
		gcm.init(true, parametros);
		int tamanhoSaida = gcm.getOutputSize(plano.length);
		byte[] saida = new byte[tamanhoSaida];
		
		// processa os bytes calculando o offset para cifrar
		int offset = gcm.processBytes(plano, 0, plano.length, saida, 0);

		try {
			// cifra os bytes
			gcm.doFinal(saida, offset);
		} catch (InvalidCipherTextException e) {
			e.printStackTrace();
		}
		
		return Hex.encodeHexString(saida);

	}

	/* 
	 * Metodo: decifra o texto cifrado com GCM
	*/
	public String decifraGCM(String pK, String iv, String textoCifrado, MainWindow ui) throws Exception {
		// IV
		byte[] iv2 = org.apache.commons.codec.binary.Hex.decodeHex(iv.toCharArray());
		
		//texto que e para decifrar
		byte[] txtCifrado = org.apache.commons.codec.binary.Hex.decodeHex(textoCifrado.toCharArray());
		
		GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());

		// Parametros passados para o GCM: chave, tamanho do mac, o IV
		KeyParameter chave2 = new KeyParameter(Hex.decodeHex(pK.toCharArray()));
		AEADParameters parametros = new AEADParameters(chave2, MAC_SIZE, iv2);

		// Aciona o decifrar do gcm - false para decifrar
		gcm.init(false, parametros);

		int tamanhoSaida = gcm.getOutputSize(txtCifrado.length);
		byte[] saida = new byte[tamanhoSaida];
		int temp = gcm.processBytes(txtCifrado, 0, txtCifrado.length,
				saida, 0);
		try {
			gcm.doFinal(saida, temp);

		} catch (InvalidCipherTextException e) {
			ui.informaMsg("Senha inválida");
			System.exit(0);
		}
		return new String(saida);
	}
}
