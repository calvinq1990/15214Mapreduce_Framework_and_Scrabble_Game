package edu.cmu.cs.cs214.hw4.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.cmu.cs.cs214.hw4.core.Game;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
		
				Game game = new Game();
				JFrame frame = new JFrame("Add participants");
				frame.add(new NamePanel(frame,game));	     				
				frame.pack();
				frame.setResizable(true);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
			}
		});
	}

}

//public class Main {
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//        		SwingUtilities.invokeLater(new Runnable() {
//        			@Override
//        			public void run() {
//        				JFrame frame = new JFrame("Start a Scabble Game");
//        				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        				Game game = new Game();
//        				frame.add(new NamePanel(frame,game));	     				
//        				frame.pack();
//        				frame.setResizable(false);
//        				frame.setVisible(true);
//        				
//
//        			}
//        		});
//            }
//        });
//    }
//
//
//}


