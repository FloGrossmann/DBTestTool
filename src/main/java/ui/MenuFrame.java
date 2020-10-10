package ui;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MenuFrame extends JFrame {

	private static final long serialVersionUID = 5735221567303441881L;
	
	Container c;
	MenuPanel menuPanel;
	
	public MenuFrame() {
		this.c = getContentPane();
		setSize(600, 600);
		setTitle("DB Test Tool");
		menuPanel = new MenuPanel();
		add(menuPanel);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
}
