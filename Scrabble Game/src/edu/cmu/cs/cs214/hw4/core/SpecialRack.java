package edu.cmu.cs.cs214.hw4.core;


/**
 * 
 * store the special tiles for players
 * its functions are similar as rack class
 *
 */
public class SpecialRack {

	private SpecialTile[] specialTiles = new SpecialTile[7];

	public void removeSpecialTile(int index) {
		specialTiles[index] = null;
	}

	public int addSpecialTile(int index) {
		SpecialTile specialTile = null;

		switch (index) {
		case 0:
			specialTile = new BoomTile();
			break;
		case 1:
			specialTile = new NegativeTile();
			break;
		case 2:
			specialTile = new ReverseTile();
			break;
		case 3:
			specialTile = new BoomTile();
			break;

		}
		for (int i = 0; i < 7; i++) {
			if (specialTiles[i] == null) {
				specialTiles[i] = specialTile;
				return i;
			}

		}
		return -2;

	}

	public SpecialTile[] getSpecialTiles() {
		return this.specialTiles;
	}

}
