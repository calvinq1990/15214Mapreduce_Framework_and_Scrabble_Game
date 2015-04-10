package edu.cmu.cs.cs214.hw4.core;

/**
 * In this class, I don't need a field to record the 
 * number of tiles in Bag. What I did is to compare the
 * number of returned tiles with the number of tiles requested
 * when the game call getTile. If it the returned number is not
 * enough, it means the bag is empty
 */

import java.util.Collections;
import java.util.LinkedList;

public class Bag { 
	
	private LinkedList<Tile> bagTiles = new LinkedList<Tile>();
	
	public Bag(){
		for(Character chara: Character.values()){
			for(int i = 0; i < chara.getAmount(); i++){
				bagTiles.add(new Tile(chara.getCharacter(), chara.getValue()));
			}		
		}
		this.shuffle();
	}
	
	public LinkedList<Tile> getTiles(int number){
		LinkedList<Tile> tempTiles = new LinkedList<Tile>();
		for(int i = 0; i < number; i++){
			if(bagTiles.size() == 0){
				
				break;
			}
			tempTiles.add(bagTiles.getFirst());
			bagTiles.removeFirst();
			
		}
		return tempTiles;
	}


	/**
	 * Shuffle the tiles in bag
	 */
	public void shuffle() {
		Collections.shuffle(bagTiles);
	}
}
