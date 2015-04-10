package edu.cmu.cs.cs214.hw4.core;

public class Tile{

	
	private int value;
	private String character;
	private Location location;


	
	
	public Tile(String character, int value ){
		this.value = value;
		this.character = character;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public String getCharacter(){
		return this.character;
	}
	public void addLocation(Location loc){
		this.location = loc;
	}
	public Location getLocation(){
		return this.location;
		
	}
	
	
	


	public String getName() {
	
		return "Tile";
	}

}
