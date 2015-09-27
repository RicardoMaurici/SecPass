package SecPass.gui;

import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JDialog;

/**
* @author Elanne Melilo de Souza 10101180
* @author Ricardo Maurici Ferreira 10201015
* Data 27/09/2015
*/

/* 
 * Essa classe apresenta informacoes sobre o trabalho.
*/
public class DialogoSobre extends JDialog {
	private JLabel logo, titulo, disciplina, professor, equipe, equipe2, data, versao;
	private JSeparator separador;
	
	/*
	 * Construtor
	*/
	public DialogoSobre(MainWindow iu){
		super(iu, "Sobre", true);
		this.inicializar();
		this.posicionar();
		this.pack();
		this.setLocationRelativeTo(iu);
		this.setVisible(true);
	}
	
	/*
	 * Metodo: Inicializa e configura os componentes que serao utilizado no JDialog
	 */
	private void inicializar(){
		this.titulo = new JLabel("Tarefa Prática (Dupla) - Gerenciador de senhas");
		this.titulo.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
		this.professor = new JLabel("Professora: Carla Merkle Westphall");
		this.disciplina = new JLabel("Disciplina: INE5680 - Segurança da Informação e de Redes");
		this.equipe = new JLabel("Alunos/Autores: ");
		this.equipe2 = new JLabel("Elanne Melilo de Souza - 10101180 e Ricardo Maurici Ferreira - 10201015");
		this.data = new JLabel("Florianópolis, 27 de Setembro de 2015");
		this.versao = new JLabel("Versão: 1.0");
		this.separador = new JSeparator();
	}
	
	/*
	 * Metodo: Define o layout para posicionamento dos componentes no JDialog
	 */
	private void posicionar(){
		
		GroupLayout layout = new GroupLayout(getContentPane());
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		setResizable(false);
		{
			ParallelGroup p1;
			SequentialGroup sg;
			int ps = GroupLayout.PREFERRED_SIZE;
		
			p1 = layout.createParallelGroup(Alignment.CENTER);
			p1.addComponent(titulo, ps, ps, ps).addComponent(disciplina, ps, ps, ps).addComponent(professor, ps, ps, ps).addComponent(equipe, ps, ps, ps).addComponent(equipe2, ps, ps, ps).addComponent(separador, ps, 200, ps).addComponent(versao, ps, ps, ps).addComponent(data, ps, ps, ps);
			sg = layout.createSequentialGroup();
			sg.addGroup(p1);
			layout.setHorizontalGroup(sg);
		}
		
		{
			ParallelGroup p1;
			ParallelGroup p2;
			ParallelGroup p3;
			ParallelGroup p4;
			ParallelGroup p5;
			ParallelGroup p6;
			ParallelGroup p7;
			ParallelGroup p8;
			ParallelGroup p9;
			SequentialGroup sg;
			
			p1 = layout.createParallelGroup(Alignment.CENTER);
			
			p2 = layout.createParallelGroup(Alignment.CENTER);
			p2.addComponent(titulo);
			
			p3 = layout.createParallelGroup(Alignment.CENTER);
			p3.addComponent(disciplina);
			
			p4 = layout.createParallelGroup(Alignment.CENTER);
			p4.addComponent(professor);
			
			p5 = layout.createParallelGroup(Alignment.CENTER);
			p5.addComponent(equipe);
			
			p6 = layout.createParallelGroup(Alignment.CENTER);
			p6.addComponent(equipe2);
			
			p7 = layout.createParallelGroup(Alignment.CENTER);
			p7.addComponent(separador);
			
			p8 = layout.createParallelGroup(Alignment.CENTER);
			p8.addComponent(versao);
			
			p9 = layout.createParallelGroup(Alignment.CENTER);
			p9.addComponent(data);
			
			sg = layout.createSequentialGroup();
			sg.addGroup(p1).addGroup(p2).addGroup(p3).addGroup(p4).addGroup(p5).addGroup(p6).addGroup(p7).addGroup(p8).addGroup(p9);
			layout.setVerticalGroup(sg);
			
		}
	}
}