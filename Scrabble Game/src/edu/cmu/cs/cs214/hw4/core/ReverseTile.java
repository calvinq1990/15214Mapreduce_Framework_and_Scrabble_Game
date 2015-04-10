package edu.cmu.cs.cs214.hw4.core;

import java.util.LinkedList;

/**
 * 
 * change the players' linked list to reverse
 *
 */

public class ReverseTile implements SpecialTile {
	private Location loc;
	private int playerIndex;
	public String getName() {
	
		return "Reverse";
	}
	
	

	public void takeAction(Game game) {
		Player tempPlayer;
		LinkedList<Player> tempPlayers = new LinkedList<Player>();
		LinkedList<Player> players = game.getPlayers();		
	//	System.out.println("players size: " + players.size() );
		tempPlayer = players.removeFirst();
		//System.out.println("size: " +players.size() );
		//System.out.println("3: " + players.get(2));
	//	System.out.println("2: " + players.get(1).getName());
	//	System.out.println("1: " + players.get(0).getName());
		for(int i = 0; i < players.size(); i++){
			tempPlayers.add( players.get(players.size()-1 - i));
			//System.out.println(" add i:" + (players.size() -1- i));
			
		}
		
		
		tempPlayers.addFirst(tempPlayer);
		game.setPlayers(tempPlayers);
		

		
		
		
	}


	public void setLocation(Location loc) {
		// TODO Auto-generated method stub
		this.loc = loc;
		
	}



	@Override
	public String getCharacter() {
		// TODO Auto-generated method stub
		return "Re";
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
