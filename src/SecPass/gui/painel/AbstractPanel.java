package SecPass.gui.painel;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class AbstractPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	protected String title;
	protected JButton submitButton;

	public AbstractPanel(String title, JButton processButton) {
		this.title = title;
		this.submitButton = processButton;
		defineDesign();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	private void defineDesign() {
		setBackground(Color.LIGHT_GRAY);
		setBorder(BorderFactory.createTitledBorder(title));
		submitButton.addActionListener(this);
	}

	public abstract void defineComponents();

	public abstract void adjustComponents();

	public abstract void actionPerformed(ActionEvent e);

	protected void clearPanel() {
		getRootPane().setBackground(Color.LIGHT_GRAY);
		setVisible(false);
	}
}
