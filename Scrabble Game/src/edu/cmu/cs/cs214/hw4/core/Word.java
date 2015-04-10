package edu.cmu.cs.cs214.hw4.core;


import java.util.LinkedList;


public class Word {
	private int startX;
	private int startY;
	private int endY;
	private int endX;
	private LinkedList<Tile> tiles = new LinkedList<Tile>();
	
	public Word(int startX, int startY, int endX, int endY){
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		
	}
	
	public void setTiles(LinkedList<Tile> tiles){
		this.tiles = tiles;
	}
	
	public LinkedList<Tile> getTiles(){
		return tiles;
	}
	

	public int getStartX(){
		return startX;
	}
	
	public int getStartY(){
		return startY;
	}
	
	public int getEndX(){
		return endX;
	}
	
	public int getEndY(){
		return endY;
	}
	
	public int getLength(){
		return Math.max((endX - startX + 1), (endY - startY + 1));
	}
	
}
