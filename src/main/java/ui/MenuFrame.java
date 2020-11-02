package ui;

import java.awt.Container;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MenuFrame extends JFrame {

	private static final long serialVersionUID = 5735221567303441881L;
	
	Container c;
	JPanel menuPanel;
	
	public MenuFrame() {
		this.c = getContentPane();
		setSize(600, 650);
		setTitle("DB Test Tool");
		try {
			setIconImage(new ImageIcon(ImageIO.read(new File("src/main/resources/speed.png"))).getImage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		menuPanel = new MenuPanel();
		add(menuPanel);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
}
