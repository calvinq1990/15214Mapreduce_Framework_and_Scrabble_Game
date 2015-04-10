package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import edu.cmu.cs.cs214.hw4.core.Board;
import edu.cmu.cs.cs214.hw4.core.Game;
import edu.cmu.cs.cs214.hw4.core.GameChangeListener;
import edu.cmu.cs.cs214.hw4.core.Location;
import edu.cmu.cs.cs214.hw4.core.Player;
import edu.cmu.cs.cs214.hw4.core.SpecialTile;
import edu.cmu.cs.cs214.hw4.core.Tile;


/***********************************************************
 *
 * It is the main part of GUI. It is the concrete observer
 * of the game, which implement GameChangeListener. It is 
 * an important field of core Game.
 * 
 * When user have operation in GUI, the listener here will 
 * call the functions in Game. Then when Game get changed,
 * it will receive information from game, then it will have
 * relative change.
 * 
 ***********************************************************/

public class GameMainPanel extends JPanel implements GameChangeListener{
	private Game game;
	private JButton[][] squareButton;
	private JButton[] tileButton;
	private JLabel[] playerScore;
	private JButton[] specialTileButton;
	private JPanel boardPanel;
	private int selectedTileIndex;
	private int selectedSpecialTileIndex;
	private JPanel scoreBoardPanel;
	private JPanel rackPanel;
	private JFrame frame = (JFrame) SwingUtilities.getRoot(this);
	private final static int SPECIALTILE_PRICE = 5;
	private static final long serialVersionUID = 1L;
	
	public GameMainPanel(Game game){
		this.game = game;
		this.selectedTileIndex = -1;
		this.selectedSpecialTileIndex = -1;
		
		game.addGameChangeListener(this);
		game.startNewGame();
		
		// build the GUI
		boardPanel = createBoardPanel();
		scoreBoardPanel = createScoreBoardPanel();
		
		JPanel controlPanel = createControlButtonPanel();
		rackPanel = createRackPanel();
		
		JPanel emptyPanel = new JPanel();
		
		JPanel specialRackPanel = createSpecialRackPanel();
		
		JPanel twoRackPanel = new JPanel(new GridLayout(3,1));
		twoRackPanel.add(rackPanel);
		twoRackPanel.add(emptyPanel);
		twoRackPanel.add(specialRackPanel);
		
		JPanel functionPanel = new JPanel();
		functionPanel.setLayout(new BorderLayout());
		functionPanel.add(scoreBoardPanel, BorderLayout.EAST);
		functionPanel.add(controlPanel,BorderLayout.CENTER);
		functionPanel.add(twoRackPanel,BorderLayout.WEST);

		setLayout(new BorderLayout());
		add(boardPanel, BorderLayout.NORTH);
		add(functionPanel, BorderLayout.SOUTH);
		
	}
	
	// create the control buttons
	private JPanel createControlButtonPanel(){ 
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,1));
		
		// create pass
		JButton passButton = new JButton();
		passButton.setText("pass");
		panel.add(passButton);
		passButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	game.pass();
            }
        });
		
		// create confirm
		JButton confirmButton = new JButton();
		confirmButton.setText("confirm");
		panel.add(confirmButton);
		confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	game.confirm();
            }
        });
		
		// create purchase
		JButton purchaseButton = new JButton();
		purchaseButton.setText("purchase");
		panel.add(purchaseButton);
		purchaseButton.addActionListener(new ActionListener() {
           
            public void actionPerformed(ActionEvent e) {
            	if (game.getCurrentPlayer().getScore() >= SPECIALTILE_PRICE) {
            		Object[] options = { "Bomb", "Negative", "Reverse", "Steal-move",
        				"No thanks" };
            		int n = -1;
            		while (true) {
            			n = JOptionPane.showOptionDialog(frame,
            				"$5 to buy a special tile ",
        					"Special Tiles Option", JOptionPane.YES_NO_CANCEL_OPTION,
        					JOptionPane.QUESTION_MESSAGE, null, options, options[4]);
        			if (n == -1 || n == 4)
        				break;
        			
        				game.purchase(n);
        				break;
            		}
        		
        		} else {
        			JOptionPane.showMessageDialog(frame, "Error! Not enough money!", 
        					"Exchange Fault",
        					JOptionPane.INFORMATION_MESSAGE);
        			
        		}

        	}
            
        });
		// create exchange
		JButton exchangeButton = new JButton();
		exchangeButton.setText("exchange");
		panel.add(exchangeButton);
		exchangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (!tileButton[selectedTileIndex].getText().equals("")){
            		game.exchange(selectedTileIndex);
            		game.disableSelectedTileIndex();
            	}else{
            		
            		JOptionPane.showMessageDialog(frame, "Error! Select a tile please.", 
        					"Exchange Fault",
        					JOptionPane.INFORMATION_MESSAGE);
            	}
            }
        });
		
		return panel;
	}
	// create special Rack Panel
	private JPanel createSpecialRackPanel(){ 
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,7));
		specialTileButton = new JButton[7];
		for(int i = 0; i< 7; i++){
			specialTileButton[i] = new JButton();
			specialTileButton[i].setText("");
			specialTileButton[i].addActionListener(new specialRackTileListener(i));
			specialTileButton[i].setPreferredSize(new Dimension(90, 30));
			panel.add(specialTileButton[i]);
		}
		panel.setBorder(BorderFactory.createTitledBorder("Player Special Rack"));
		return panel;
	}
	
	// create rack panel
	private JPanel createRackPanel(){ 
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,7));
		tileButton = new JButton[7];
		
		Tile[] tiles = game.getCurrentPlayer().getRack().getTiles();
		for(int i = 0; i< 7; i++){
			tileButton[i] = new JButton();
			tileButton[i].setText(tiles[i].getCharacter());
			tileButton[i].addActionListener(new rackTileListener(i));
			tileButton[i].setSize(10, 5);
			tileButton[i].setPreferredSize(new Dimension(90, 30));
			panel.add(tileButton[i]);
			
		}
		
		panel.setBorder(BorderFactory.createTitledBorder("Player Rack"));
		return panel;
	}
	
	// create socre board
	private JPanel createScoreBoardPanel(){
		JPanel panel = new JPanel();
		int playerNum = game.getPlayers().size();
		panel.setLayout(new GridLayout(playerNum,1));
		playerScore = new JLabel[playerNum];
		
		panel.setLayout(new GridLayout(playerNum,1));
		for(int i =0; i < playerNum; i++){
			playerScore[i] = new JLabel();
			Player player = game.getPlayers().get(i);
			playerScore[i].setText(player.getName() + ":" + Integer.toString(player.getScore()));
			playerScore[i].setPreferredSize(new Dimension(200, 20));
			panel.add(playerScore[i]);
			
		}
		panel.setBorder(BorderFactory.createTitledBorder("ScoreBoard"));
		return panel;
	}
	
	private JPanel createBoardPanel(){
		JPanel panel = new JPanel();
	
		squareButton = new JButton[15][15];
		panel.setLayout(new GridLayout(15,15));

		// Create all of the squares and display them.
		
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				
				squareButton[x][y] = new JButton();
				squareButton[x][y].addActionListener(new squareButtonListener(new Location(x,y)));
				squareButton[x][y].setPreferredSize(new Dimension(70, 30));
				this.setProperty(x,y);
				
				panel.add(squareButton[x][y]);
			}
		}
		
		
		panel.setBorder(BorderFactory.createTitledBorder("Current Player: " + game.getCurrentPlayer().getName()));
		return panel;
		
	}
	
	// detail listener of special rack operations
	private class specialRackTileListener implements ActionListener {
		private int index;
		
		public specialRackTileListener(int i){
			index = i;
		}	
		
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			selectedSpecialTileIndex = index;
			game.setSelectedSpecialTile(index);
			game.disableSelectedTileIndex();

		}
	}
	
	// detail listener of rack operations
	private class rackTileListener implements ActionListener {
		private int index;
		
		public rackTileListener(int i){
			index = i;
		}	
		
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			selectedTileIndex = index;
			game.setSelectedTile(index);
			game.disableSelectedSpecialTileIndex();

		}
	}
	
	// detail listener of square button operations
	// it can handle the tile and special tile at the same time
	private class squareButtonListener implements ActionListener {
	
		private Location location;
		
		
		public squareButtonListener(Location location){
				this.location = location;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			
			if(selectedTileIndex != -1 && !game.getBoard().getSquare
					(location.getX(), location.getY()).isSelected()){
				game.selectTileToBoard(location);
				selectedTileIndex =-1;
				
			}
			
			if(selectedSpecialTileIndex != -1 && !game.getBoard().getSquare
					(location.getX(), location.getY()).isSelected()){
				game.selectTileToBoard(location);
				selectedSpecialTileIndex =-1;
				
			}

		}

	}
	
	// it is an important method. Especially when the square is needed to
	// recover, it should observe its property.
	private void setProperty(int x, int y){
		int letterTile = game.getBoard().getProperty(new Location(x,y))[0];
		int wordTile = game.getBoard().getProperty(new Location(x,y))[1];
		
		switch(letterTile){
		case 2:
			squareButton[x][y].setText("DL");
			squareButton[x][y].setBackground(Color.red);
			squareButton[x][y].setOpaque(true);
			break;
		case 3:
			squareButton[x][y].setText("TL");
			squareButton[x][y].setBackground(Color.blue);
			squareButton[x][y].setOpaque(true);
			break;
		default:
			
			break;
		}
		
		
		switch(wordTile){
		case 2:
			squareButton[x][y].setText("DW");
			squareButton[x][y].setBackground(Color.pink);
			squareButton[x][y].setOpaque(true);
			break;
		case 3:
			squareButton[x][y].setText("TW");
			squareButton[x][y].setBackground(Color.yellow);
			squareButton[x][y].setOpaque(true);
			break;
		default:
			
			break;
		}
		
		if(wordTile == 1 && letterTile == 1){
			squareButton[x][y].setText("");
		}
	}


	@Override
	public void gameEnded() {
		JOptionPane.showMessageDialog(frame, "Game End", 
				"Please start a new Game!",
				JOptionPane.INFORMATION_MESSAGE);
		frame.remove(this);
		frame.add(new NamePanel(frame, game));
		frame.setTitle("Start");
		frame.setResizable(false);
		
		frame.pack();
		
	}


	@Override
	public void invalidWord(LinkedList<Location> selectedLocations) {
		Tile[] tiles = game.getCurrentPlayer().getRack().getTiles();
			for(int i = 0; i< 7; i++){
					
				tileButton[i].setText(tiles[i].getCharacter());
					
			}
			
			for(Location loc : selectedLocations){
				setProperty(loc.getX(), loc.getY());
			}
	}

	


	@Override
	public void currentPlayerChanged(Player player) {
		this.boardPanel.setBorder(BorderFactory.createTitledBorder("Current Player: " + game.getCurrentPlayer().getName()));
		Tile[] tiles = game.getCurrentPlayer().getRack().getTiles();
		for(int i = 0; i< 7; i++){
			tileButton[i].setText(tiles[i].getCharacter());
			
			SpecialTile specialTile =game.getCurrentPlayer().getSpecialRack().getSpecialTiles()[i];
			if(specialTile != null)
				specialTileButton[i].setText(specialTile.getName());
			else
				specialTileButton[i].setText("");
		}
		
		Board board = game.getBoard();
		for(int y = 0; y < 15; y++){
			for(int x = 0; x < 15; x++){
				Tile tile = board.getSquare(x, y).getTile();
				SpecialTile specialTile = board.getSquare(x, y).getSpecailTile();
				if (tile != null){
					squareButton[x][y].setText(tile.getCharacter());
				}else 
					setProperty(x,y);
				if(specialTile != null){
					if (specialTile.getPlayer() == game.getCurrentPlayer().getIndex()){
						squareButton[x][y].setText(specialTile.getCharacter());
					}else
						setProperty(x,y);
				}
				
			}
		}
			
	}


	@Override
		public void rackChanged(int index) {
	
			tileButton[index].setText("");
	
			
		}
	
	
	@Override
	public void specialRackChanged(int index) {

		specialTileButton[index].setText("");

		
	}


	@Override
	public void squareChanged(Location location, Tile tile) {
		if (tile != null)
			squareButton[location.getX()][location.getY()].setText(tile.getCharacter());
		
		
		
	}


	@Override
	public void specialSquareChanged(Location location, SpecialTile specialTile) {
		if (specialTile != null)
			squareButton[location.getX()][location.getY()].setText(specialTile.getCharacter());
		
	}


	@Override
	public void scoreChanged(Player player) {
		this.playerScore[player.getIndex()].setText(player.getName() + ":" + Integer.toString(player.getScore()));
		
	}


	@Override
	public void exchange(int index) {
		tileButton[index].setText(game.getCurrentPlayer().getRack().getTiles()[index].getCharacter());
		
		
	}


	@Override
	public void purchase(int i) {
		// TODO Auto-generated method stub
		SpecialTile[] specialTiles = game.getCurrentPlayer().getSpecialRack().getSpecialTiles();
		specialTileButton[i].setText(specialTiles[i].getName());
		
		
	}











	

}
