package SecPass.gui.painel;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

import java.awt.event.ActionEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import SecPass.gui.MainWindow;

/**
* @author Elanne Melilo de Souza 10101180
* @author Ricardo Maurici Ferreira 10201015
* Date 21/09/2015
*/

public class NewPasswordPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelDominio, labelPassword;
	private JTextField tfDominio, tfPassword;
	private JButton clearButton;
	private MainWindow ui;

	public NewPasswordPanel(MainWindow ui) {
		super("Nova Senha", new JButton("Gravar"));
		this.ui = ui;
		defineComponents();
		adjustComponents();
	}

	@Override
	public void defineComponents() {
		labelDominio = new JLabel("Dominio:");
		labelPassword = new JLabel("Senha:");
		tfDominio = new JTextField();
		tfPassword = new JPasswordField(6);
		clearButton = new JButton("Limpar");
		clearButton.addActionListener(this);
		//submitButton.addActionListener(this);
	}

	@Override
	public void adjustComponents() {
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(LEADING)
						.addComponent(labelDominio)
						.addComponent(labelPassword)
						.addComponent(submitButton))
						.addGroup(
								layout.createParallelGroup(LEADING)
								.addComponent(tfDominio)
								.addComponent(tfPassword)
								.addComponent(clearButton)));

		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(BASELINE)
						.addComponent(labelDominio)
						.addComponent(tfDominio))
						.addGroup(
								layout.createParallelGroup(BASELINE)
								.addComponent(labelPassword)
								.addComponent(tfPassword))
								.addGroup(
										layout.createParallelGroup(BASELINE)
										.addComponent(submitButton)
										.addComponent(clearButton)));
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submitButton && !tfDominio.getText().equals("") && !tfPassword.getText().equals("")) { //acionou salvar
			ui.cifra(tfDominio.getText(), tfPassword.getText());
			tfDominio.setText("");
			tfPassword.setText("");
			JOptionPane.showMessageDialog(null, "Gravado com sucesso!", "SUCESSO", JOptionPane.INFORMATION_MESSAGE);
		}else if(e.getSource() == clearButton){
			tfDominio.setText("");
			tfPassword.setText("");
		}else
			ui.informaMsg("Campos sem valores");
	}

}
