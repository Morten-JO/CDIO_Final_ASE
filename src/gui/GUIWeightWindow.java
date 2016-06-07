package gui;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import input.SocketHandler;

public class GUIWeightWindow extends JFrame{

	public static final String titleName = "ASE - Controller";
	
	public GUIWeightWindow(SocketHandler handler){
		super();
		this.setTitle(GUIWeightWindow.titleName);
		GUIWeightPanel panel = new GUIWeightPanel(handler);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.add(panel);
		this.pack();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
}
