package edu.cmu.cs.cs214.hw4.core;

public class Player {
	private String name;
	private int score;
	private Rack rack;
	private SpecialRack specialRack;
	private int index;
	

	
	public Player(String name, int index){
		this.score = 0;
		rack = new Rack();
		specialRack = new SpecialRack();
		this.name = name;
		this.rack = new Rack();
		this.index = index;
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public void addScore(int score){
		this.score += score;
	}
	
//	public void setScore(int score){
//		this.score = score;
//	}
	
	public int getScore(){
		return this.score;
	}
	
	public SpecialRack getSpecialRack(){
		return this.specialRack;
	}
	public Rack getRack(){
		return this.rack;
	}
	
	public void deductScore(int score){
		this.score = this.score - score;
	}
	
	public String getName(){
		return this.name;
	}
	
//	public boolean hasSpecialTiles(){
//		SpecialTile[] specialTiles = this.specialRack.getSpecialTiles();
//		for(int i = 0; i< 7; i++){
//			if(specialTiles[i] != null)
//				return true;
//		}
//		return false;
//	}
	
}
