package edu.cmu.cs.cs214.hw4.core;

public class Square {
	private Tile tile;
	private SpecialTile specialTile;
	private int[] property = {1,1};// property[0]: tileTimes, property[1]: wordTimes
	private boolean isPropertyUsed = false;
	private boolean isOccupied = false;
	private boolean isSelected = false;
	
	public void setSelected(boolean bool){
		this.isSelected = bool;
	}
	
	public boolean isSelected(){
		return this.isSelected;
	}
	
	
	public boolean isPropertyUsed(){
		return isPropertyUsed;
	}
	
	public void useProperty(){
		this.isPropertyUsed = true;
	}
	
	public Square(Property property){
		tile = null;
		switch (property){
			case doubleletter:
				this.property[0] =2;
				break;
			case tripleletter:
				this.property[0] = 3;
				break;
			case doubleword:
				this.property[1] = 2;
				break;
			case tripleword:
				this.property[1] = 3;
				break;
			default:
				break;
		}
	}
	
	public void setTile(Tile tile){
		this.tile = tile;
	}
	
	public Tile getTile(){
		return this.tile;
	}
	
	
	public int[] getProperty(){
		
		return this.property;
	}
	
	public void setSpecialTile(SpecialTile tile){
		this.specialTile = tile;
	}
	
	public SpecialTile getSpecailTile(){
		return this.specialTile;
	}

	public void setOccupied(){
		this.isOccupied = true;
	}
	
	public boolean isOccupied(){
		return this.isOccupied;
	}
	

}
