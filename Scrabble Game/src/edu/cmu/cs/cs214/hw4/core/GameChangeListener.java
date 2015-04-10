package edu.cmu.cs.cs214.hw4.core;

import java.util.LinkedList;


public interface GameChangeListener {

    /**
     * Called when any tile on the board changes. This
     * includes changes to initialize a fresh board.
     *
     * @param x The x coordinate of the updated cell on the board.
     * @param y The y coordinate of the updated cell on the board.
     */
    public void squareChanged(Location loc, Tile tile);

    /**
     * Called when the current player changed
     *
     * @param player The new current player.
     */
    public void currentPlayerChanged(Player currPlayer);

    /**
     * Called when the game ends, announcing the winner (or null on a tie).
     *
     * @param winner The winner of the game, or null on a tie.
     */
    public void gameEnded();
    
    /**
     * Called when the rack is changed
     */
    public void rackChanged(int index);
    
    /**
     * Called when the special rack is changed
     */
    public void specialRackChanged(int index);
    
    /**
     * Called when the score is changed
     */
    public void scoreChanged(Player player);
    
    /**
     * Called when the word is not valid
     */
	public void invalidWord(LinkedList<Location> selectedLocations);

	/**
     * Called when exchange button is pressed
     */
	public void exchange(int index);
	/**
     * Called when purchase button is pressed
     */
	public void purchase(int i);
	
	/**
     * Called when special square is changed
     */
	
	public void specialSquareChanged(Location loc, SpecialTile specialTile);

}
