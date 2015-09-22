package SecPass.gui.painel;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelLogin, labelPassword;
	private JTextField tfLogin, tfPassword;
	private JButton clearButton;
	
	public LoginPanel() {
		super("Login", new JButton("Login"));
		defineComponents();
		adjustComponents();
	}

	@Override
	public void defineComponents() {
		labelLogin = new JLabel("Login:");
		labelPassword = new JLabel("Password:");
		tfLogin = new JTextField();
		tfPassword = new JPasswordField(6);
		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);
		submitButton.addActionListener(this);
	}

	@Override
	public void adjustComponents() {
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
