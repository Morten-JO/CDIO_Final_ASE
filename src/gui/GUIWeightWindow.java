package gui;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import input.SocketHandler;

public class GUIWeightWindow extends JFrame{

	public static final String titleName = "ASE Viewer";
	
	public GUIWeightWindow(SocketHandler handler){
		super();
		this.setTitle(GUIWeightWindow.titleName);
		GUIWeightPanel panel = new GUIWeightPanel(handler);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.add(panel);
		this.pack();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WindowListener exiter = new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e){
				int confirm = JOptionPane.showOptionDialog(null, "Do you want to close the ASE?", "Exit ASE", 
							  JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if(confirm == 0){
					handler.shutdownASE();
					System.exit(0);
				}
			}
		};
		this.addWindowListener(exiter);
		this.setResizable(false);
		this.setVisible(true);
	}
	
}
