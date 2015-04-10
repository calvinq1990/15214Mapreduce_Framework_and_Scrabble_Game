package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import edu.cmu.cs.cs214.hw4.core.Game;

public class NamePanel extends JPanel {

	/**
	 * It is the name panel for inputting player's names
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private Game game;
	private LinkedList<String> names = new LinkedList<String>();
	private JLabel[] playerLabels = new JLabel[4];
	private JTextField[] playerFields = new JTextField[4];
	
	public NamePanel(JFrame frame, Game game){
		this.frame = frame;
		this.game = game;
		
	
		playerLabels[0] = new JLabel("Player1:");
		playerFields[0] = new JTextField(12); 
		playerLabels[1] = new JLabel("Player2:");
		playerFields[1] = new JTextField(12);
		playerLabels[2] = new JLabel("Player3:");
		playerFields[2] = new JTextField(12); 
		playerLabels[3] = new JLabel("Player4:");
		playerFields[3] = new JTextField(12);
		
		JPanel panel1 = new JPanel(); 
		panel1.add(playerLabels[0]);
		panel1.add(playerFields[0]);
		panel1.add(playerLabels[1]);
		panel1.add(playerFields[1]);
		
		JPanel panel2 = new JPanel();
		panel2.add(playerLabels[2]);
		panel2.add(playerFields[2]);
		panel2.add(playerLabels[3]);
		panel2.add(playerFields[3]);
		
		JButton startButton = new JButton("start");
		startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	for(JTextField field: playerFields){
            		String name = field.getText();
            		
            		if (!name.equals("")){
            			names.add(name);
            		}
            	}
            	
            	if (2 > names.size()){
        			JOptionPane.showMessageDialog(frame, "Error!", 
        					"the number of input players is not enough",
        					JOptionPane.INFORMATION_MESSAGE);
        			return;
        		}
            	
            	// add player to game
            	for(int i = 0; i < names.size(); i++){
            		game.addPlayer(names.get(i), i);
            	}
            	
                startGame();
               
            }
        }); 
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(panel1, BorderLayout.NORTH);
		panel.add(panel2, BorderLayout.CENTER);
		panel.add(startButton, BorderLayout.SOUTH);
		add(panel);
	}
	
	private void startGame(){
		frame.remove(this);
		frame.add(new GameMainPanel(game));
		frame.setTitle("Scabble");
		frame.setResizable(false);
		frame.pack();
	}


	
}
