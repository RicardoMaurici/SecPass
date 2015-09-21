package SecPass.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
* @author Elanne Melilo de Souza 10101180
* @author Ricardo Maurici Ferreira 10201015
* Date 15/09/2015
*/

public class MainWindow extends JFrame implements ActionListener{
	private JPanel panel;
	private JLabel labelOperation;
	
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
		
		switch(opcoes){
		case Login:
			JOptionPane.showMessageDialog(this, "Login information.", "Construction", JOptionPane.INFORMATION_MESSAGE);
			break;
		case newPassword:
			JOptionPane.showMessageDialog(this, "Insert a new password.", "Construction", JOptionPane.INFORMATION_MESSAGE); 
			break;
		case getPassword:
			JOptionPane.showMessageDialog(this, "Get password.", "Construction", JOptionPane.INFORMATION_MESSAGE); 
		}
	}

}
