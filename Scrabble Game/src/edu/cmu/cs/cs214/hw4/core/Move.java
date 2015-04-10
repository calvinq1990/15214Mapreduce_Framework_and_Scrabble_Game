package edu.cmu.cs.cs214.hw4.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * 
 * It is mainly used to calculate score and 
 * validate the word
 *
 */
public class Move {
	private String rowOrCol = null;
	private Board board;
	private Word keyWord;
	private Dictionary dic;
	private LinkedList<Word> wordList;
	private boolean isFirstTurn;
	
	HashMap<Location, Tile> map = new HashMap<Location, Tile>();
	private boolean rowValid = false, colValid = false;
	


	public Move(Board board, HashMap<Location, Tile> map, Dictionary dic, boolean isFirstTurn) {
		this.board = board;
		this.map = map;
		this.dic = dic;
		this.isFirstTurn = isFirstTurn;
	
	}

	public int calculateAllWord(){
		int score = 0;
		if(keyWord != null)
			wordList.addFirst(keyWord);
		for(int i = 0; i < wordList.size(); i++){
			int calculatedScore=calculateWord(wordList.get(i));
			score += calculatedScore;
			System.out.println("score:"+calculatedScore);
		}
		// deactivate the property of square
		deactivateSquareProperty(wordList);
		return score;
	}
	
	private void deactivateSquareProperty(LinkedList<Word> wordList){
		for(int j = 0; j < wordList.size(); j++){
			LinkedList<Tile> tiles = wordList.get(j).getTiles();
			for(int i = 0; i< tiles.size(); i++){
				Tile tile = tiles.get(i);
				Location loc = tile.getLocation();
				board.getSquare(loc.getX(), loc.getY()).useProperty();
			}
		
			
		}
	}
	
	private int calculateWord(Word word) {
		
		int wordScore = 0;
		int tileTimes = 1;	
		int wordTimes = 1 ;
		int[] property;
		LinkedList<Tile> tiles = word.getTiles();
		for(int i = 0; i < tiles.size(); i++){
			Tile tile = tiles.get(i);
			Location loc = tile.getLocation();
			
			property = board.getProperty(loc);
			
			tileTimes = property[0];
			if(1 < property[1])
				wordTimes *= property[1];
			System.out.println(tile.getCharacter()+":"+tiles.get(i).getValue()+" "+"tileTimes:"+ tileTimes);
			System.out.println(tile.getCharacter()+":"+tiles.get(i).getValue()+" "+"wordTimes:"+ property[1]);
			wordScore = wordScore + tiles.get(i).getValue() * tileTimes;
			System.out.println("wordScore update: " + wordScore);
			
		}
		System.out.println(" one wordTimes: " + wordTimes );
		
		wordScore *= wordTimes;
		System.out.println(" one word score: " + wordScore );
		
		
		
		
		return wordScore;
	}


	public Board addTiles() {
		Iterator<Map.Entry<Location, Tile>> entries = map.entrySet().iterator();
		Map.Entry<Location, Tile> entry;
		Location tempLoc;
		Tile tempTile;
		while (entries.hasNext()) {
			entry = entries.next();
			tempLoc = entry.getKey();
			tempTile = entry.getValue();
			tempTile.addLocation(tempLoc);
			board.addTile(tempLoc, tempTile);
		}
		return board;

	}

	public boolean isWordsValid() {
	
		if(keyWord != null){
			System.out.println("keyword: " + board.getWordString(keyWord, map));
			if(!dic.lookUp(board.getWordString(keyWord, map)))
				return false;
		}
		for (int i = 0; i < wordList.size(); i++) {
			System.out.println("word: " + board.getWordString(wordList.get(i), map));
			if (!dic.lookUp(board.getWordString(wordList.get(i), map)))
				return false;
		}
		return true;

	}

	public boolean validateLocation() {
		rowValid = false;
		colValid = false;
		if (1 == map.size()) {
			Iterator<Map.Entry<Location, Tile>> iter = map.entrySet().iterator();
			
			Location loc = iter.next().getKey();
			keyWord = new Word(loc.getX(),loc.getY(),loc.getX(),loc.getY());
			
			wordList = board.constructWords(map, "Single");
			
			
			//check if the selected tiles are adjacent to old tiles
			
			if (wordList.size() > 0)
				return true;
			
			
//			else if (wordList.size() == 1){
//				if (wordList.get(0).getTiles().size() > map.size())
//					return true;
//			}

		} else {
			// check Row or Column
			rowValid = checkRow();
			colValid = checkCol();
			if (rowValid || colValid) {

				if (rowValid) {
					rowOrCol = "Row";
				} else {
					rowOrCol = "Column";
				}
				

				// check if the selected tiles are adjacent
				keyWord = board.constructkeyWord(map, rowOrCol);
				if(keyWord == null)
					return false;
				
				
				wordList = board.constructWords(map, rowOrCol);
				//check if the selected tiles are adjacent to old tiles
				if(!this.isFirstTurn){
					if(wordList.size() > 0 || keyWord.getLength() > map.size())
						return true;
				
				}else 
					return true;

			}

		}
		return false;

	}

	public boolean checkCol() {

		Iterator<Map.Entry<Location, Tile>> entries = map.entrySet().iterator();
		int x;

		boolean bool = false;
		if (entries.hasNext()) {
			Map.Entry<Location, Tile> entry = entries.next();
			x = entry.getKey().getX();

			bool = true;
			
			while (entries.hasNext()) {
				//rowOrCol = "Column";
				entry = entries.next();
				if (x != entry.getKey().getX()) {
					bool = false;
					break;
				}

			}
		}

		return bool;

	}

	public boolean checkRow() {
		Iterator<Map.Entry<Location, Tile>> entries = map.entrySet().iterator();
		int y;

		boolean bool = false;
		if (entries.hasNext()) {
			Map.Entry<Location, Tile> entry = entries.next();
			y = entry.getKey().getY();
			bool = true;
			
			while (entries.hasNext()) {
				//rowOrCol = "Row";
				entry = entries.next();
				if (y != entry.getKey().getY()) {
					bool = false;
					break;
				}

			}
		}

		return bool;
	}

}
