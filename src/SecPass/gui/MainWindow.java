package SecPass.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import SecPass.gui.painel.AbstractPanel;
import SecPass.gui.painel.GetPasswordPanel;
import SecPass.gui.painel.NewPasswordPanel;

/**
* @author Elanne Melilo de Souza 10101180
* @author Ricardo Maurici Ferreira 10201015
* Date 15/09/2015
*/

public class MainWindow extends JFrame implements ActionListener{
	private JPanel panel;
	private JLabel labelOperation;
	private String texto;
	
	public MainWindow(){
		super();	
		this.inicializar();
	}

	private void inicializar(){
		this.setInterfaceLayout();
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
			panel = new NewPasswordPanel();
			break;
		case getPassword:
			panel = new GetPasswordPanel();
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
				this.texto+=linha + "\n";
			}
			leitor.close();
			reader.close();
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

	/* Metodo: Escreve no arquivo que consta em 'arquivo' o conteudo do texto */
	private void escreveArquivo(File arquivo) {
		try{
			FileOutputStream fos = new FileOutputStream(arquivo); 
			fos.write(texto.getBytes()); 
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
