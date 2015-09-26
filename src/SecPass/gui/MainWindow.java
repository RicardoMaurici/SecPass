package SecPass.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.*;
import SecPass.gui.painel.AbstractPanel;
import SecPass.gui.painel.GetPasswordPanel;
import SecPass.gui.painel.NewPasswordPanel;
import SecPass.logica.Logica;
import SecPass.logica.Tabela;

/**
* @author Elanne Melilo de Souza 10101180
* @author Ricardo Maurici Ferreira 10201015
* Date 15/09/2015
*/

public class MainWindow extends JFrame implements ActionListener{
	private JPanel panel;
	private JLabel labelOperation;
	private String texto;
	private String[] conteudoArq;
	private Integer interacoes;
	private Logica logica;
	private ArrayList<Tabela> tabela;
	//private String pk;
	public MainWindow(){
		super();
		this.setInterfaceLayout();
		//pk = this.acesso();
		//this.geraChaveDerivada();
		this.decifraDPK();
		tabela = new ArrayList<Tabela>();
		abrirArquivoTabela();
		//this.geraChaveDerivada();	
		this.inicializar();
	}


	private void decifraDPK() {
		String pk = this.acesso();
		try {
			logica.decifraGCM(pk, conteudoArq[1], conteudoArq[2], this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*Esse metodo so e usado na primeira vez, para cifrar a chave que da acesso a tabela 
	  private void geraChaveDerivada() {
		  //System.out.println("pK:"+pk);
		try{
			String senha = "INE56SecPac80";
			String iv = "3cf39a538794b8364e09f131c16c9b1b";
			String senhaDerivada = logica.geraChaveDerivada(senha, conteudoArq[0], interacoes);
			//System.out.println(senhaDerivada);
			String senhaFinal = logica.cifraGCM(pk, senhaDerivada, iv);
			this.texto = conteudoArq[0]+"\n"+iv+"\n"+senhaFinal;
			this.salvarArquivo();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
*/
	private String acesso() {
		//123Seguranca456
		logica = new Logica();
		this.interacoes = 10000;
		String inputSenha, masterKey = "";
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Insira a senha do Gerenciador:");
		JPasswordField pass = new JPasswordField(10);
		panel.add(label);
		panel.add(pass);
		String[] options = new String[]{"Cancel", "OK"};
		int option = JOptionPane.showOptionDialog(null, panel, "SENHA GERENCIADOR",
		                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
		                         null, options, options[1]);
		if(option == 0){
			System.exit(0);
		    
		}else{
			inputSenha = new String(pass.getPassword());
		    this.abrirArquivo();
			masterKey = logica.geraChaveDerivada(inputSenha, conteudoArq[0], interacoes);
		}
		
		return masterKey;
	}

	public void inicializar(){
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setJMenuBar(new MenuBar(this));
		this.setContentPane(this.getPane());
		this.setSize(400,200);
		this.setMinimumSize(new Dimension(400,200));
		this.setTitle("Secure Password");
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private void setInterfaceLayout() {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	private JPanel getPane(){
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		this.labelOperation = new JLabel();
		this.panel.add(this.labelOperation, BorderLayout.SOUTH);
		this.panel.setBorder(BorderFactory.createLineBorder(getBackground(), 10));
		return this.panel;
	}

	public void actionPerformed(ActionEvent e) {
		Options opcoes = Options.valueOf(e.getActionCommand());
		AbstractPanel panel = null;
		switch(opcoes){
		case newPassword:
			panel = new NewPasswordPanel(this);
			break;
		case getPassword:
			panel = new GetPasswordPanel(this);
		}
		setContentPane(panel);
		pack();
	}
	
	/*
	 * Metodo: Abrir o arquivo com a senha mestre cifrada.
	 */
	private void abrirArquivo() {
        File arquivo = new File("src/SecPass/arquivos/arquivo.txt");
        this.leArquivo(arquivo);
	}
	
	/*
	 * Metodo: Abrir o arquivo com a tabela.
	 */
	private void abrirArquivoTabela() {
        File arquivo = new File("src/SecPass/arquivos/arquivoTabela.txt");
        try{
			FileReader reader = new FileReader(arquivo);
			BufferedReader leitor = new BufferedReader(reader);
			this.texto = "";
			String linha;
			while((linha = leitor.readLine()) != null){
				this.texto+=linha + ":";
			}
			leitor.close();
			reader.close();
			String[] conteudo = this.texto.split(":");
			if(conteudo.length>=3){
				for(int i=0; i<conteudo.length; i+=3){
					Tabela itemTabela = new Tabela();
					itemTabela.setHmac(conteudo[i]);
					itemTabela.setChaveCifrada(conteudo[i+1]);
					itemTabela.setValorCifrado(conteudo[i+2]);
					this.tabela.add(itemTabela);
					}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/*
	 * Metodo: Le o arquivo que e passado como parametro e guarda na variavel texto.
	 * @param File arquivo
	 */
	private void leArquivo(File arquivo) {
		try{
			FileReader reader = new FileReader(arquivo);
			BufferedReader leitor = new BufferedReader(reader);
			this.texto = "";
			String linha;
			while((linha = leitor.readLine()) != null){
				this.texto+=linha + ":";
			}
			leitor.close();
			reader.close();
			conteudoArq = this.texto.split(":");
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Metodo: Salvar um arquivo.
	 */
	private void salvarArquivo() {
       	File arquivo = new File("src/SecPass/arquivos/arquivo.txt");
        this.escreveArquivo(arquivo);
	}
	
	/*
	 * Metodo: Salvar o arquivo com a Tabela.
	 */
	private void salvarArquivoTabela() {
       	File arquivo = new File("src/SecPass/arquivos/arquivoTabela.txt");
       	texto = "";
       	for(Tabela itemTabela: this.tabela){
       		texto+=itemTabela.getHmac()+"\n"+itemTabela.getChaveCifrada()+"\n"+itemTabela.getValorCifrado()+"\n";
       	}
        this.escreveArquivo(arquivo);
	}

	/* Metodo: Escreve no arquivo, que consta em 'arquivo', o conteudo do texto */
	private void escreveArquivo(File arquivo) {
		try{
			FileOutputStream fos = new FileOutputStream(arquivo); 
			fos.write(texto.getBytes()); 
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void informaMsg(String msg) {
		JOptionPane.showMessageDialog(this, msg, "ERRO", JOptionPane.ERROR_MESSAGE);
	}


	public String getTexto() {
		return texto;
	}


	public void setTexto(String texto) {
		this.texto = texto;
	}


	public void cifra(String dominio, String valor) {
		String pk = this.acesso();
		Tabela itemTabela = new Tabela();
		try {
			String dpk = logica.decifraGCM(pk, conteudoArq[1], conteudoArq[2], this);
			itemTabela.setChaveCifrada(logica.cifraGCM(dpk, dominio, conteudoArq[1]));
			itemTabela.setHmac(logica.geraHMAC(itemTabela.getChaveCifrada(), logica.decifraGCM(pk, conteudoArq[1], conteudoArq[2], this)));
			//System.out.println(itemTabela.getHmac().substring(0, 64).getBytes().length);	
			itemTabela.setValorCifrado(logica.cifraGCM(logica.geraChaveDerivada(itemTabela.getHmac().substring(0, 16), conteudoArq[0], interacoes), valor, conteudoArq[1]));
			this.tabela.add(itemTabela);
			salvarArquivoTabela();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String decifra(String dominio) {
		String pk = this.acesso();
		Tabela itemTabela = null;
		try {
			String dpk = logica.decifraGCM(pk, conteudoArq[1], conteudoArq[2], this);
			dominio = (logica.cifraGCM(dpk, dominio, conteudoArq[1]));
			String mac = (logica.geraHMAC(dominio, logica.decifraGCM(pk, conteudoArq[1], conteudoArq[2], this)));
			for(Tabela itemTabela1:this.tabela){
				if(itemTabela1.getHmac().equals(mac))
					itemTabela=itemTabela1;
			}
			if(itemTabela != null)
				return logica.decifraGCM(logica.geraChaveDerivada(itemTabela.getHmac().substring(0, 16), conteudoArq[0], interacoes), conteudoArq[1], itemTabela.getValorCifrado(), this);
			//System.out.println(itemTabela.getHmac().substring(0, 64).getBytes().length);
			//itemTabela.setValorCifrado(logica.cifraGCM(itemTabela.getHmac().substring(0, 16), valor, conteudoArq[1]));
			//this.tabela.add(itemTabela);
			//salvarArquivoTabela();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
