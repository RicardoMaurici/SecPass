package SecPass.gui.painel;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import SecPass.gui.MainWindow;

/**
* @author Elanne Melilo de Souza 10101180
* @author Ricardo Maurici Ferreira 10201015
* Date 20/09/2015
*/

public class GetPasswordPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelDominio, labelPassword;
	private JTextField tfDominio, tfPassword;
	private JButton clearButton;
	private MainWindow ui;
	
	public GetPasswordPanel(MainWindow ui) {
		super("Retorna Senha", new JButton("Buscar"));
		defineComponents();
		adjustComponents();
		this.ui = ui;
	}

	@Override
	public void defineComponents() {
		labelDominio = new JLabel("Dominio:");
		labelPassword = new JLabel("Senha:");
		tfDominio = new JTextField();
		tfPassword = new JTextField();
		clearButton = new JButton("Limpar");
		tfPassword.setEnabled(false);
		clearButton.addActionListener(this);
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
		if(e.getSource()==submitButton){
			String valor = ui.decifra(tfDominio.getText());
			if(valor!=null)
				tfPassword.setText(valor);
			else
				ui.informaMsg("Dominio não encontrado");
		}else if(e.getSource()==clearButton){
			tfDominio.setText("");
			tfPassword.setText("");
		}
		
	}

}
