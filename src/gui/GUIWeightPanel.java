package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import input.SocketHandler;

public class GUIWeightPanel extends JPanel{

	private JButton refreshBtn;
	private JTextPane textPane;
	
	public GUIWeightPanel(SocketHandler handler){
		super();
		refreshBtn = new JButton("Refresh weights");
		refreshBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.forcePassiveRefresh();
			}
		});
		textPane = new JTextPane();
		refreshListModel(handler);
		this.add(refreshBtn);
		this.add(textPane);
	}
	
	public void refreshListModel(SocketHandler handler){
		if(textPane != null){
			textPane.removeAll();
			StyledDocument document = textPane.getStyledDocument();
			textPane.setEditable(false);
			Style redStyle = textPane.addStyle("red", null);
			StyleConstants.setForeground(redStyle, Color.red);
			Style greenStyle = textPane.addStyle("green", null);
			StyleConstants.setForeground(greenStyle, Color.green);
			Style blackStyle = textPane.addStyle("black", null);
			StyleConstants.setForeground(blackStyle, Color.black);
			for(int i = 0; i < handler.getWeights().length; i++){
				if(handler.getWeights()[i] != null){
					try {
						document.insertString(document.getLength(), "Weight #"+i+" with ip: "+handler.getIps()[i]+" is ", blackStyle);
						document.insertString(document.getLength(), "online", greenStyle);
						if(i != handler.getWeights().length - 1){
							document.insertString(document.getLength(), "\r\n", redStyle);
						}
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				} else{
					try {
						document.insertString(document.getLength(), "Weight #"+i+" with ip: "+handler.getIps()[i]+" is ", blackStyle);
						document.insertString(document.getLength(), "offline", redStyle);
						if(i != handler.getWeights().length - 1){
							document.insertString(document.getLength(), "\r\n", redStyle);
						}
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
}
