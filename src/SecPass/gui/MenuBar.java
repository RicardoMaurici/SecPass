package SecPass.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
* @author Elanne Melilo de Souza 
* @author Ricardo Maurici Ferreira 
* Date 15/09/2015
*/

public class MenuBar extends JMenuBar {
	

	public MenuBar(MainWindow mw){
		JMenu menu;
		JMenuItem item;;
		
		menu=new JMenu("Password");
		item=new JMenuItem("New Password", KeyEvent.VK_T);
		item.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_2, ActionEvent.ALT_MASK));
		item.setActionCommand(Options.newPassword.name());
		item.addActionListener(mw);
		menu.add(item);
		
		item=new JMenuItem("Get Password", KeyEvent.VK_T);
		item.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_3, ActionEvent.ALT_MASK));
		item.setActionCommand(Options.getPassword.name());
		item.addActionListener(mw);
		menu.add(item);
		add(menu);
		
		item=new JMenuItem("Remove Password", KeyEvent.VK_T);
		item.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_4, ActionEvent.ALT_MASK));
		item.setActionCommand(Options.removePassword.name());
		item.addActionListener(mw);
		menu.add(item);
		add(menu);
	}
}
