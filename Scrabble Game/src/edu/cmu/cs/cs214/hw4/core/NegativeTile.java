package edu.cmu.cs.cs214.hw4.core;

public class NegativeTile implements SpecialTile {
	private Location loc;
	private int playerIndex;
	public String getName() {
		// TODO Auto-generated method stub
		return "Negative";
	}


	public void takeAction(Game game) {
		int turnScore = game.getCurrentTurnScore();
		game.getCurrentPlayer().deductScore(2 * turnScore);
		System.out.println("neg activate!!!!!!!!!!!!!!");
	}


	public void setLocation(Location loc) {
		this.loc = loc;
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getCharacter() {
		// TODO Auto-generated method stub
		return "Ne";
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
