package edu.cmu.cs.cs214.hw4.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;



/**
 * 
 * It is used to access the squares
 * Such as the game or the move should access the 
 * squares through the board
 *
 */
public class Board {
	private Square[][] squares ;

	public Board() {
		squares = new Square[15][15];
		
		// Triple Words
		addProperty(0,0, Property.tripleword);
		addProperty(0,7, Property.tripleword);
		addProperty(0,14, Property.tripleword);
		// double Words
		addProperty(1,1, Property.doubleword);
		addProperty(2,2, Property.doubleword);
		addProperty(3,3, Property.doubleword);
		addProperty(4,4, Property.doubleword);
		// Triple Letter
		addProperty(5,1, Property.tripleletter);
		addProperty(5,5, Property.tripleletter);
		addProperty(1,5, Property.tripleletter);
		// Double Letter
		addProperty(3,0, Property.doubleletter);
		addProperty(0,3, Property.doubleletter);
		addProperty(6,2, Property.doubleletter);
		addProperty(2,6, Property.doubleletter);
		addProperty(7,3, Property.doubleletter);
		addProperty(3,7, Property.doubleletter);
		addProperty(6,6, Property.doubleletter);
		
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (squares[i][j] == null) {
					squares[i][j] = new Square(Property.regular);
				}
			}
		}
	}

	
	private void addProperty(int x, int y, Property property) {
		squares[x][y] = new Square(property);
		squares[14 - x][14 - y] = new Square(property);
		squares[14 - x][y] = new Square(property);
		squares[x][14 - y] = new Square(property);

	}
	
	/***********************************************************
	 * Below are the methods of getting information the board
	 * They are used in Game and many other parts, when getting
	 * important field
	 * 
	 ***********************************************************/
	public Tile getTile(Location loc) {
		return squares[loc.getX()][loc.getY()].getTile();
	}
	
	public LinkedList<SpecialTile> getSpecialTile(HashMap<Location, Tile> map){
		LinkedList<SpecialTile> specialTiles = new LinkedList<SpecialTile>();
		Iterator<Map.Entry<Location, Tile>> entries = map.entrySet().iterator();
		Map.Entry<Location, Tile> entry;
		while(entries.hasNext()){
			entry = entries.next();
			SpecialTile sTile = squares[entry.getKey().getX()][entry.getKey().getY()].getSpecailTile();
			if(sTile != null)
				specialTiles.add(sTile);
			
		}
		return specialTiles;
		
	}

	public int[] getProperty(Location location) {
		int[] num = { 1, 1 };
		if (squares[location.getX()][location.getY()].isPropertyUsed())
			return num;
		else
			return squares[location.getX()][location.getY()].getProperty();
	}


	public Square getSquare(int x, int y) {
		return squares[x][y];
	}
	
	// it is used to split the word into tiles. Then it is helpful to construct
	// words
	public LinkedList<Tile> getWordTiles(Word word, HashMap<Location, Tile> map) {
	
		LinkedList<Tile> wordTile = new LinkedList<Tile>();
		Tile tempTile;
		Location tempLoc;
		int startX, startY, endX, endY;
		startX = word.getStartX();
		startY = word.getStartY();
		endX = word.getEndX();
		endY = word.getEndY();
		// for row word
		if (startX == endX) {
			for (int i = startY; i <= endY; i++) {
				tempTile = squares[startX][i].getTile();
				if (tempTile == null) {
					Iterator<Map.Entry<Location, Tile>> entries = map
							.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<Location, Tile> entry = entries.next();
						tempLoc = entry.getKey();
						if (tempLoc.getX() == startX && tempLoc.getY() == i) {
							tempTile = entry.getValue();
						}
					}
				}
				wordTile.add(tempTile);
			}
	
		}
		// for col word 
		else if (startY == endY) {
			for (int i = startX; i <= endX; i++) {
				tempTile = squares[i][startY].getTile();
				if (tempTile == null) {
					Iterator<Map.Entry<Location, Tile>> entries = map
							.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<Location, Tile> entry = entries.next();
						tempLoc = entry.getKey();
						if (tempLoc.getX() == i && tempLoc.getY() == startY) {
							tempTile = entry.getValue();
						}
					}
				}
				wordTile.add(tempTile);
			}
	
		}
		word.setTiles(wordTile);
		return wordTile;
	
	}


	public String getWordString(Word word, HashMap<Location, Tile> map) {
		LinkedList<Tile> wordTile = new LinkedList<Tile>();
		String tempString = "";
	
		wordTile = getWordTiles(word, map);
		for (int i = 0; i < wordTile.size(); i++) {
			tempString += wordTile.get(i).getCharacter();
		}
		return tempString;
	
	}


	public void addSpecialTile(Location location, SpecialTile specialTile){
		squares[location.getX()][location.getY()].setSpecialTile(specialTile);
		
		
	}
	public void addTile(Location location, Tile tile) {
		squares[location.getX()][location.getY()].setTile(tile);
		squares[location.getX()][location.getY()].setSelected(true);
	}

	
	
	// if the key word is row, we just need to check each selected 
	// character in column
	
	public LinkedList<Word> constructWords(HashMap<Location,Tile> map, 
			String rowOrCol) {
		LinkedList<Word> wordList = new LinkedList<Word>();
		int startX = -1;
		int startY = -1;
		int endX = -1;
		int endY = -1;
		Iterator<Map.Entry<Location, Tile>> entries = map.entrySet().iterator();
		Map.Entry<Location, Tile> entry;
		if (rowOrCol.equals("Single")) {
			entry = entries.next();
			startX = entry.getKey().getX();
			endX = startX;		
			startY = entry.getKey().getY();
			endY = startY;

			// check the row word
			while (startX > 0 && squares[startX - 1][startY].getTile() != null) {
				startX--;
			}
			while (endX < 14 && squares[endX + 1][startY].getTile() != null) {
				endX++;
			}
			if(startX != endX || startY != endY)
				wordList.add(new Word(startX, startY, endX, endY));
			

			// check the column word
			startX = entry.getKey().getX();
			endX = startX;		
			startY = entry.getKey().getY();
			endY = startY;
			
			while (startY > 0 && squares[startX][startY-1].getTile() != null) {
				startY--;
			}
			while (endY < 14 && squares[startX][endY+1].getTile() != null) {
				endY++;
			}
			
			
			if(startX != endX || startY != endY)
				wordList.add(new Word(startX, startY, endX, endY));

		}
		
		if (rowOrCol.equals("Row")) {
			while(entries.hasNext()){
				entry = entries.next();
				startX = entry.getKey().getX();
				endX = startX;
				startY = entry.getKey().getY();
				endY = startY;
				while (startY > 0 && squares[startX][startY - 1].getTile() != null) {
					startY--;
				}
				while (endY < 14 && squares[startX][endY + 1].getTile() != null) {
					endY++;
				}
				
				if(startX != endX ||  startY != endY)
					wordList.add(new Word(startX, startY, endX, endY));
			}
			

		}
		if (rowOrCol.equals("Column")) {
			while(entries.hasNext()) {
				entry = entries.next();
				startY = entry.getKey().getY();
				endY = startY;
				startX = entry.getKey().getX();
				endX = startX;
				while (startX > 0 && squares[startX - 1][startY].getTile() != null) {
					startX--;
				}
				while (endX < 14 && squares[endX + 1][startY].getTile() != null) {
					endX++;
				}
				if(startX != endX || startY != endY)
					wordList.add(new Word(startX, startY, endX, endY));
			}
		}

		return wordList;
	}

	// construct the key word, if it is null, it mean the word is not adjacent
	//
	// if the flag is false, no tile is selected into this square
	// at the same time no existing tile in this square
	// so this square is empty, return null,
	// it means invalid input word
	public Word constructkeyWord(HashMap<Location, Tile> map, String rowOrCol) {
		Iterator<Map.Entry<Location, Tile>> entries = map.entrySet().iterator();
		int startX = -1;
		int startY = -1;
		int endX = -1;
		int endY = -1;
		int y;
		int x;
		boolean flag = false;
		LinkedList<Location> locList = new LinkedList<Location>();
		Location tempLoc;

		if (rowOrCol.equals("Row")) {
			if (entries.hasNext()) {
				Map.Entry<Location, Tile> entry = entries.next();
				startY = entry.getKey().getY();
				endY = startY;
				startX = entry.getKey().getX();
				endX = entry.getKey().getX();
			}
			while (entries.hasNext()) {
				Map.Entry<Location, Tile> entry = entries.next();
				tempLoc = entry.getKey();
				locList.add(tempLoc);
				x = tempLoc.getX();
				if (x < startX)
					startX = x;
				else if(x > endX)
					endX = x;
			}

			// search the most left unnull square new startX;

			while (startX >0 && squares[startX - 1][startY].getTile() != null) {
				startX--;
			}

			// search if the tiles are adjacent
			for (int i = startX + 1; i <= endX; i++) {
				flag = false;

				// no existing tile in this square
				if (squares[i][startY].getTile() == null) {
					for (int k = 0; k < locList.size(); k++) {
						if (locList.get(k).getX() == i)
							flag = true;
					}
					
					if (flag == false && i <= endX) {
						return null;
					}
				}
			}
			// search the real endX
			while (endX < 14 && squares[endX + 1][startY].getTile() != null) {
				endX++;
			}
			return new Word(startX, startY, endX, endY);

		}
		if (rowOrCol.equals("Column")) {
			if (entries.hasNext()) {
				Map.Entry<Location, Tile> entry = entries.next();
				startX = entry.getKey().getX();
				endX = startX;
				startY = entry.getKey().getY();
				endY = entry.getKey().getY();
			}
			while (entries.hasNext()) {
				Map.Entry<Location, Tile> entry = entries.next();
				tempLoc = entry.getKey();
				locList.add(tempLoc);
				

				y = tempLoc.getY();
				if (y < startY)
					startY = y;
				else if(y > endY)
					endY = y;
			}
			
			while (startY > 0 && squares[startX][startY - 1].getTile() != null ) {
				startY--;
			}
			

			// search if the tiles are adjacent
			for (int i = startY + 1; i <= endY; i++) {
				flag = false;

				// no existing tile in this square
				if (squares[startX][i].getTile() == null) {
					for (int k = 0; k < locList.size(); k++) {
						if (locList.get(k).getY() == i)
							flag = true;
					}
			
					if (flag == false && i <= endY) {
						return null;
					}
				}
			}
			// search the real endX
			
			while (endY < 14 && squares[startX][endY + 1].getTile() != null) {
				endY++;
			}
			return new Word(startX, startY, endX, endY);
		}

		return null;

	}
	
	// make the square null
	public int bombSquares(int x, int y){
		if(squares[x][y].getSpecailTile() != null)
			squares[x][y].setSpecialTile(null);
		if(squares[x][y].getTile() != null){
			int value = squares[x][y].getTile().
					getValue()*squares[x][y].getProperty()[0];
			squares[x][y].setTile(null);
			squares[x][y].setSelected(false);
			return value;
		}else
			return 0;
		
		
	}
	
	// when the square is occupied, we can't select tile to that square
	public void setSquareOccupied(HashMap<Location, Tile> map){
		Iterator<Map.Entry<Location, Tile>> entries = map.entrySet().iterator();
		Map.Entry<Location, Tile> entry;
		while(entries.hasNext()){
			entry = entries.next();
			Location loc = entry.getKey();
			squares[loc.getX()][loc.getY()].setOccupied();
		}
	}
}
