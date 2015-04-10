package edu.cmu.cs.cs214.hw4.core;

/**
 * 
 * unify all the specialTile
 *
 */
public interface SpecialTile {
	public void setPlayer(int index);
	public int getPlayer();
	public abstract String getName();

	// for boom tile to set the location
	public void setLocation(Location loc);
	

	
	public void takeAction(Game game);
	
	public String getCharacter(); 
	
	public Location getLocation();
}
