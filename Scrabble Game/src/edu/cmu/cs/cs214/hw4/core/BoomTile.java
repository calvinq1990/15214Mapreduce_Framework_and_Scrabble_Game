package edu.cmu.cs.cs214.hw4.core;

/**
 * 
 * when boom tile is activated, the player will
 * deduct points in radius of 3.
 * And the tile there will removed
 *
 */

public class BoomTile implements SpecialTile {
	private Location loc;
	private int playerIndex;
	public String getName() {
		// TODO Auto-generated method stub
		return "Boom";
	}
	

	public void setLocation(Location loc){
		this.loc = loc;
	}
	

	public void takeAction(Game game) {
		Board board = game.getBoard();

		
		int x = loc.getX();
		int y = loc.getY();
		int score = 0;
		
		for(int i = -3; i< 4; i++){
			if((x+i) >= 0)
				score += board.bombSquares( x+i, y);
		}
		
		for(int i = -3; i< 4; i++){
			if((y+i) >= 0)
				score += board.bombSquares(x, y + i);
		}
		
		game.getCurrentPlayer().deductScore(score);
		System.out.println("bomb : " + score);
		
		
	
		
		
	}


	@Override
	public String getCharacter() {
		// TODO Auto-generated method stub
		return "bo";
	}


	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return this.loc;
	}


	@Override
	public void setPlayer(int index) {
		this.playerIndex = index;
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getPlayer() {
		// TODO Auto-generated method stub
		return this.playerIndex;
	}
	

}
