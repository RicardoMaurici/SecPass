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
import SecPass.gui.painel.RemovePasswordPanel;
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
	
	public MainWindow(){
		super();
		this.setInterfaceLayout();
		//this.geraChaveDerivada();
		this.decifraDPK();
		tabela = new ArrayList<Tabela>();
		abrirArquivoTabela();
		this.inicializar();
	}

	/*
	 * Metodo: Inicializa os componentes da interface principal
	 */
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
	
	/*
	 * Metodo: Define Layout da Interface
	 */
	private void setInterfaceLayout() {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	/*
	 * Metodo: Define componentes do painel
	 */
	private JPanel getPane(){
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		this.labelOperation = new JLabel();
		this.panel.add(this.labelOperation, BorderLayout.SOUTH);
		this.panel.setBorder(BorderFactory.createLineBorder(getBackground(), 10));
		return this.panel;
	}

	/*
	 * Metodo: Aciona a acao do respectivo item de menu selecionado
	 */
	public void actionPerformed(ActionEvent e) {
		Options opcoes = Options.valueOf(e.getActionCommand());
		AbstractPanel panel = null;
		switch(opcoes){
		case newPassword:
			setContentPane(new NewPasswordPanel(this));
			pack();
			break;
		case removePassword:
			setContentPane(new RemovePasswordPanel(this));
			pack();
			break;
		case removeAllPassword:
			this.removeAll();
			break;
		case about:
			new DialogoSobre(this);
			break;
		case getPassword:
			setContentPane(new GetPasswordPanel(this));
			pack();
		}
	}
	
	/*
	 * Metodo: Decifra a PK derivada que esta no arquivo (conteudoArq[2]), a partir da senha fornecida no acesso (pk)
	 */
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
		  String pk = this.acesso();
		try{
			String senha = ""; //Inserir a senha desejada
			String iv = conteudoArq[1];
			String senhaDerivada = logica.geraChaveDerivada(senha, conteudoArq[0], interacoes);
			//System.out.println(senhaDerivada);
			String senhaFinal = logica.cifraGCM(pk, senhaDerivada, iv);
			this.texto = conteudoArq[0]+"\n"+iv+"\n"+senhaFinal;
			this.salvarArquivo();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}*/

	/*
	 * Metodo: Solicita a senha para utilizar o gerenciador
	 */
	private String acesso() {
		logica = new Logica();
		this.interacoes = 10000;
		String inputSenha, masterKey = "";
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Insira a senha do Gerenciador:");
		JPasswordField pass = new JPasswordField(10);
		panel.add(label);
		panel.add(pass);
		String[] options = new String[]{"Cancela", "OK"};
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
	
	/*
	 * Metodo: Abrir o arquivo com a senha mestre cifrada.
	 */
	private void abrirArquivo() {
        File arquivo = new File("src/SecPass/arquivos/arquivo.txt");
        this.leArquivo(arquivo);
	}
	
	/*
	 * Metodo: Abrir o arquivo que contem a tabela.
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

	/* 
	 * Metodo: Escreve no arquivo, que consta em 'arquivo', o conteudo do texto 
	*/
	private void escreveArquivo(File arquivo) {
		try{
			FileOutputStream fos = new FileOutputStream(arquivo); 
			fos.write(texto.getBytes()); 
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/* 
	 * Metodo: Imprime uma mensagem para o usuario 
	*/
	public void informaMsg(String msg) {
		JOptionPane.showMessageDialog(this, msg, "ERRO", JOptionPane.ERROR_MESSAGE);
	}

	/* 
	 * Metodo: Realiza a cifragem de um item inserido na tabela 
	*/
	public void cifra(String dominio, String valor) {
		String pk = this.acesso();
		Tabela itemTabela = new Tabela();
		try {
			String dpk = logica.decifraGCM(pk, conteudoArq[1], conteudoArq[2], this);
			itemTabela.setChaveCifrada(logica.cifraGCM(dpk, dominio, conteudoArq[1]));
			itemTabela.setHmac(logica.geraHMAC(itemTabela.getChaveCifrada(), logica.decifraGCM(pk, conteudoArq[1], conteudoArq[2], this)));
			itemTabela.setValorCifrado(logica.cifraGCM(logica.geraChaveDerivada(itemTabela.getHmac().substring(0, 16), conteudoArq[0], interacoes), valor, conteudoArq[1]));
			this.tabela.add(itemTabela);
			salvarArquivoTabela();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/* 
	 * Metodo: Realiza a decifragem de um item da tabela 
	*/
	public String decifra(String dominio) {
		String pk = this.acesso();
		Tabela itemTabela = null;
		try {
			String dpk = logica.decifraGCM(pk, conteudoArq[1], conteudoArq[2], this);
			dominio = (logica.cifraGCM(dpk, dominio, conteudoArq[1]));
			String mac = (logica.geraHMAC(dominio, logica.decifraGCM(pk, conteudoArq[1], conteudoArq[2], this)));
			for(Tabela itemTabela1:this.tabela){
				if(itemTabela1.getHmac().equals(mac)){
					itemTabela=itemTabela1;
					break;
				}
			}
			if(itemTabela != null)
				return logica.decifraGCM(logica.geraChaveDerivada(itemTabela.getHmac().substring(0, 16), conteudoArq[0], interacoes), conteudoArq[1], itemTabela.getValorCifrado(), this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/* 
	 * Metodo: Realiza a remocao de um item da tabela 
	*/
	public boolean remove(String dominio, String valor) {
		String pk = this.acesso();
		Tabela itemTabela = new Tabela();
		boolean removido=false;
		try {
			String dpk = logica.decifraGCM(pk, conteudoArq[1], conteudoArq[2], this);
			itemTabela.setChaveCifrada(logica.cifraGCM(dpk, dominio, conteudoArq[1]));
			itemTabela.setHmac(logica.geraHMAC(itemTabela.getChaveCifrada(), logica.decifraGCM(pk, conteudoArq[1], conteudoArq[2], this)));
			itemTabela.setValorCifrado(logica.cifraGCM(logica.geraChaveDerivada(itemTabela.getHmac().substring(0, 16), conteudoArq[0], interacoes), valor, conteudoArq[1]));
			for(int i=0; i<this.tabela.size(); i++){
				if(this.tabela.get(i).getValorCifrado().equals(itemTabela.getValorCifrado())){
					this.tabela.remove(i);
					salvarArquivoTabela();
					removido=true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return removido;
	}
	
	/* 
	 * Metodo: Elimina todos os item da tabela para iniciar uma nova tabela 
	*/
	public void removeAll(){
		int resposta = JOptionPane.showConfirmDialog(this,"Você tem certeza que deseja iniciar uma nova tabela\nObs: Todos os registros serão apagados","INICIAR TABELA", JOptionPane.YES_NO_OPTION);
		if(resposta==0){
			this.acesso();
			this.tabela = new ArrayList<Tabela>();
			salvarArquivoTabela();
		}
	}

}
