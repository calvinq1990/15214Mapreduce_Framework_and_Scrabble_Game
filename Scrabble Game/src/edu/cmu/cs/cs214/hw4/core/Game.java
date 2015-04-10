package edu.cmu.cs.cs214.hw4.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


/**
 * Game is the controller of this game. It connects the GUI and the 
 * core parts of the game.
 * 
 * It contains a observer "GameChangeListener". When the game got changed
 * it will notify the GUI to change the player, score and other information.
 *
 *
 */

public class Game {
	private LinkedList<Player> players = new LinkedList<Player>();
	private Player currentPlayer;
	private boolean isFirstTurn;
	private Bag bag;
	private Dictionary dic;
	private Board board;
	private HashMap<Location, Tile> map;
	private HashMap<Location, SpecialTile> specialMap;
	private boolean isGameOver;
	private final static int SPECIALTILE_PRICE = 5;
	private int currentTurnScore;
	private GameChangeListener listener;
	private int selectedTileIndex;
	private int selectedSpecialTileIndex;
	private LinkedList<Location> selectedLocations;

	public Game() {
		this.isGameOver = false;
		map = new HashMap<Location, Tile>();
		specialMap = new HashMap<Location, SpecialTile>();
		this.selectedLocations = new LinkedList<Location>();
		this.selectedTileIndex = -1;
		this.selectedSpecialTileIndex = -1;
	}

	/**
	 * Each time for a new Game, the bag,dictionary and board will be
	 * initialized
	 * 
	 */

	public void startNewGame() {
		isFirstTurn = true;
		dic = new Dictionary();
		board = new Board();
		bag = new Bag();
		this.currentPlayer = players.getFirst();

		for (int i = 0; i < players.size(); i++) {
			LinkedList<Tile> list = bag.getTiles(7);
			players.get(i).getRack().initialRack(list);
			for (int k = 0; k < 7; k++) {
			}
		}
	}

	/***********************************************************
	 * Below are the methods of getting information by game Such as get players,
	 * board and many other elements, which may be used in GUI or other core
	 * parts
	 * 
	 ***********************************************************/
	
	public LinkedList<Player> getPlayers() {
		return this.players;
	}

	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	public void getNextPlayer() {
		players.addLast(players.removeFirst());
		this.currentPlayer = players.getFirst();
		this.notifyCurrentPlayerChanged();
	}

	/**
	 * this method is used to return the score of this turn to Negative Speical
	 * Tile.
	 */
	public int getCurrentTurnScore() {
		return this.currentTurnScore;
	}

	/**
	 * this method is used to return the board to the boom Special Tile action.
	 */
	public Board getBoard() {
		return this.board;
	}

	public int[] getScore() {
		int[] scores = new int[players.size()];
		for (int i = 0; i < players.size(); i++) {
			scores[i] = players.get(i).getScore();
		}
		return scores;

	}

	public Rack getRack(Player player) {
		return player.getRack();
	}

	public HashMap<Location, Tile> getMap() {
		return this.map;
	}

	public HashMap<Location, SpecialTile> getSpecialMap() {
		return this.specialMap;
	}

	public boolean getGameStatus() {
		return this.isGameOver;
	}

	/***********************************************************
	 * 	Belows are the methods of setting , adding parameters 
	 *  or changing some status of some variable. Such as setPlayer
	 *  add listener and other operations
	 * 
	 ***********************************************************/
	public void setPlayers(LinkedList<Player> players) {
		this.players = players;
	}

	public void setSelectedTile(int i) {
		this.selectedTileIndex = i;
	}

	public void setSelectedSpecialTile(int i) {
		this.selectedSpecialTileIndex = i;
	}

	public void disableSelectedSpecialTileIndex() {
		this.selectedSpecialTileIndex = -1;
	}

	public void disableSelectedTileIndex() {
		this.selectedTileIndex = -1;
	}

	public Player addPlayer(String name, int index) {
		Player player = new Player(name, index);
		players.add(player);
		return player;

	}

	public void addGameChangeListener(GameChangeListener listener) {
		this.listener = listener;
	}

	public void putSpecialTile(Location loc, SpecialTile specialTile) {
		if (specialTile.getName().equals("BoomTile")) {
			specialTile.setLocation(loc);
		}
		this.specialMap.put(loc, specialTile);
	}
	
	/***********************************************************
	 * Below are the methods operations in GUI such as pass, 
	 * confirm and so on.
	 ***********************************************************/

	public void pass() {

		// recover the rack
		LinkedList<Tile> tiles = new LinkedList<Tile>();
		if (map.size() != 0) {
			for (Tile tile : map.values()) {
				tiles.add(tile);
			}
			this.currentPlayer.getRack().refill(tiles);
		}

		// recover the board
		Iterator<Map.Entry<Location, Tile>> entries = map.entrySet().iterator();
		Map.Entry<Location, Tile> entry;
		while (entries.hasNext()) {
			entry = entries.next();
			Location loc = entry.getKey();
			board.getSquare(loc.getX(), loc.getY()).setTile(null);
			board.getSquare(loc.getX(), loc.getY()).setSelected(false);
		}
		this.getNextPlayer();
		map = new HashMap<Location, Tile>();
		specialMap = new HashMap<Location, SpecialTile>();

		this.currentTurnScore = 0;
		this.selectedLocations = new LinkedList<Location>();
	}

	public void exchange(int exchangeIndex) {
		Tile tile = bag.getTiles(1).get(0);

		this.currentPlayer.getRack().exchange(tile, exchangeIndex);

		notifyExchange(exchangeIndex);

	}

	public void confirm() {
		Rack rack;
		Move move = new Move(board, map, dic, isFirstTurn);
		LinkedList<Tile> list = new LinkedList<Tile>();

		if (0 == map.size())
			return;

		if (inMiddleSquare() && move.validateLocation() && move.isWordsValid()) {

			// add tiles and special tiles to board actually
			this.board = move.addTiles();

			// set score of player
			this.currentTurnScore = move.calculateAllWord();
			this.currentPlayer.addScore(this.currentTurnScore);

			// remove the tiles of rack of currentPlayer actually
			rack = this.currentPlayer.getRack();

			// rack.removeTile(map);

			// refill tiles to the currentPlayer's rack
			list = bag.getTiles(map.size());
			if (list.size() < map.size()) {
				this.isGameOver = true;
				this.notifyGameEnd();
			}
			rack.refill(list);

			// special tile takes action if special tile is activated
			if (specialMap.size() != 0) {
				Iterator<Map.Entry<Location, SpecialTile>> entries = specialMap
						.entrySet().iterator();
				Map.Entry<Location, SpecialTile> entry;
				while (entries.hasNext()) {
					entry = entries.next();
					entry.getValue().takeAction(this);
					board.getSquare(entry.getKey().getX(),
							entry.getKey().getY()).setSpecialTile(null);

				}
			}
			isFirstTurn = false;
			this.notifyScoreChanged();
			this.getNextPlayer();

		}

		else {
			// recover the rack
			LinkedList<Tile> tiles = new LinkedList<Tile>();
			if (map.size() != 0) {
				for (Tile tile : map.values()) {
					tiles.add(tile);
				}
			}
			this.currentPlayer.getRack().refill(tiles);

			// recover the board
			Iterator<Map.Entry<Location, Tile>> entries = map.entrySet()
					.iterator();
			Map.Entry<Location, Tile> entry;
			while (entries.hasNext()) {
				entry = entries.next();
				Location loc = entry.getKey();
				board.getSquare(loc.getX(), loc.getY()).setTile(null);
				board.getSquare(loc.getX(), loc.getY()).setSelected(false);
			}

			this.notifyInvalidWord();
		}
		map = new HashMap<Location, Tile>();
		specialMap = new HashMap<Location, SpecialTile>();
		this.currentTurnScore = 0;
		this.selectedLocations = new LinkedList<Location>();

	}

	public void purchase(int index) {
		int i = -2;
		if (currentPlayer.getScore() > SPECIALTILE_PRICE) {
	
			i = this.currentPlayer.getSpecialRack().addSpecialTile(index);
			getCurrentPlayer().deductScore(SPECIALTILE_PRICE);
	
		} else
			return;
		notifyPurchase(i);
		notifyScoreChanged();
	}

	public boolean inMiddleSquare() {
		if (!this.isFirstTurn)
			return true;

		Iterator<Map.Entry<Location, Tile>> entries = map.entrySet().iterator();
		Map.Entry<Location, Tile> entry;
		while (entries.hasNext()) {
			entry = entries.next();
			if (entry.getKey().getX() == 7 && entry.getKey().getY() == 7)
				return true;

		} 
		return false;
	}

	// it is an important method to set the location
	// when the GUI has operation. Also the tiles, special tiles
	// are actually added here.
	public void selectTileToBoard(Location location) {
		this.selectedLocations.add(location);
		if (selectedTileIndex > -1) {
			Tile tile = this.currentPlayer.getRack().getTiles()[selectedTileIndex];
			board.addTile(location, tile);

			map.put(location, tile);
			this.currentPlayer.getRack().removeTile(tile);
			// check activate special tile or not if do, add to specialMap
			SpecialTile special = board.getSquare(location.getX(),
					location.getY()).getSpecailTile();
			if (special != null)
				specialMap.put(location, special);
			this.notifyRackChanged();
			this.notifySquareChanged(location, tile);
		} else if (selectedSpecialTileIndex > -1) {
			SpecialTile specialTile = this.currentPlayer.getSpecialRack()
					.getSpecialTiles()[selectedSpecialTileIndex];
			
			specialTile.setPlayer(this.currentPlayer.getIndex());
			specialTile.setLocation(location);
			board.addSpecialTile(location, specialTile);
			this.currentPlayer.getSpecialRack().removeSpecialTile(
					selectedSpecialTileIndex);
			this.notifySpecialRackChanged();
			this.notifySpecialSquareChanged(location, specialTile);

		}
	}

	/***********************************************************
	 * Below are the methods to notify the GUI that something has
	 * been change. The Gui should do some change after calling
	 * that.
	 * 
	 ***********************************************************/
	public void notifyGameEnd() {
		if (this.listener != null)
			this.listener.gameEnded();
	}

	private void notifyRackChanged() {
		if (this.listener != null)
			this.listener.rackChanged(this.selectedTileIndex);
	}

	private void notifySpecialRackChanged() {
		if (this.listener != null)
			this.listener.specialRackChanged(this.selectedSpecialTileIndex);
	}

	private void notifySquareChanged(Location loc, Tile tile) {
		if (this.listener != null)
			this.listener.squareChanged(loc, tile);
	}

	private void notifySpecialSquareChanged(Location loc,
			SpecialTile specialTile) {
		if (this.listener != null)
			this.listener.specialSquareChanged(loc, specialTile);
	}

	private void notifyScoreChanged() {
		if (this.listener != null)
			this.listener.scoreChanged(this.currentPlayer);
	}

	private void notifyCurrentPlayerChanged() {
		if (this.listener != null)
			this.listener.currentPlayerChanged(this.currentPlayer);
	}

	private void notifyInvalidWord() {
		if (this.listener != null)
			this.listener.invalidWord(this.selectedLocations);
	}

	private void notifyExchange(int index) {
		if (this.listener != null)
			this.listener.exchange(index);
	}

	private void notifyPurchase(int i) {
		if (this.listener != null)
			this.listener.purchase(i);
	}

}
