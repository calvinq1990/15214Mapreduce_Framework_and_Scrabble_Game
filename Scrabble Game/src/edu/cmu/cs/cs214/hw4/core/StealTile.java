package edu.cmu.cs.cs214.hw4.core;

/*
 * this special tile is not implemented
 */

public class StealTile implements SpecialTile {
	private Location loc;
	private int playerIndex;
	public String getName() {
	
		return "Steal";
	}

	public void takeAction(Game game) {
		
	}


	public void setLocation(Location loc) {
		// TODO Auto-generated method stub
		this.loc = loc;
	}

	@Override
	public String getCharacter() {
		// TODO Auto-generated method stub
		return "St";
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