package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import input.SocketHandler;

public class GUIWeightPanel extends JPanel implements Observer{

	private JButton refreshBtn;
	private JTextPane textPane;
	private SocketHandler handler;
	
	public GUIWeightPanel(SocketHandler handler){
		super();
		this.handler = handler;
		handler.addObserver(this);
		refreshBtn = new JButton("Refresh weights");
		refreshBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.forcePassiveRefresh();
			}
		});
		textPane = new JTextPane();
		refreshListModel();
		this.add(refreshBtn);
		this.add(textPane);
	}
	
	public void refreshListModel(){
		if(textPane != null){
			textPane.removeAll();
			try {
				textPane.getStyledDocument().remove(0, textPane.getStyledDocument().getLength());
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
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

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1.equals("update")){
			refreshListModel();
		}
	}
}
