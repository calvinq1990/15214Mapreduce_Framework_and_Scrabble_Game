package edu.cmu.cs.cs214.hw4.core;


import java.util.LinkedList;

/**
 * 
 * store the tiles for players.It has many functions
 * such as fill, remove, initial and so on.
 *
 */
public class Rack {

	private Tile[] tiles = new Tile[7];

	public void removeTile(Tile tile){
		for (int i = 0; i< 7; i++){
			if (tiles[i] == tile)
				tiles[i] = null;
		}	
	}
	
	// argument tiles are based on the empty number
	public void refill(LinkedList<Tile> tiles){
		for(int i = 0; i < 7; i++){
			if (this.tiles[i] == null){
				this.tiles[i] = tiles.getFirst();
				tiles.removeFirst();
			}
			
		}
	}
	
	public void exchange(Tile tile, int index){
		tiles[index] = tile;
		
	}
	
	public Tile[] getTiles(){
		return this.tiles;
	}
	public void initialRack(LinkedList<Tile> list){
		for(int i = 0; i < 7; i++){
			this.tiles[i] = list.get(i);
		}
	}
	
}
