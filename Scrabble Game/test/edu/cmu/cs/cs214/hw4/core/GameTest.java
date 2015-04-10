package edu.cmu.cs.cs214.hw4.core;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GameTest {
	private Game game;
	private Player player1, player2, player3;
	private Tile a,a2,n,i,m,l,b,e,t,s,g;

	@Before
	public void setUp() {
		game = new Game();
		player1 = game.addPlayer("Chang",1);
		player2 = game.addPlayer("Nan",2);
		player3 = game.addPlayer("Lin",3);	
	
		a = new Tile("a",1);
		n = new Tile("n",1);
		i = new Tile("i",1);
		m = new Tile("m",3);
		a2 = new Tile("a",1);
		l = new Tile("l",1);
		b = new Tile("b",3);
		e = new Tile("e",1);
		t = new Tile("t",1);
		
		s = new Tile("s",1);
		g = new Tile("g",2);
		
	}



	@Test
	public void testPlayer() {
		
		assertEquals(player1.getName(), "Chang");
		player1.addScore(5);
		assertEquals(player1.getScore(), 5);
	}
	
	
	
	
	@Test
	public void testAdjacentCol(){
		game.startNewGame();
		
		game.getMap().put(new Location(7,4),a);
		game.getMap().put(new Location(7,5),n);
		game.getMap().put(new Location(7,6),i);
		game.getMap().put(new Location(7,7),m);
		game.getMap().put(new Location(7,8),a2);
		game.getMap().put(new Location(7,9),l);
	
		
		game.getBoard().getSquare(7, 4).setTile(a);
		game.getBoard().getSquare(7, 5).setTile(n);
		game.getBoard().getSquare(7, 6).setTile(i);
		game.getBoard().getSquare(7, 7).setTile(m);
		game.getBoard().getSquare(7, 8).setTile(a2);
		game.getBoard().getSquare(7, 9).setTile(l);
	
		
		
		game.confirm();
		// player adds addjacent 'animal' in column in all in normal squares 
		assertEquals(player1.getScore(),8);
		
		
	}
	@Test
	public void testAdjacentRow(){
		game.startNewGame();
		
		game.getMap().put(new Location(4,7),a);
		game.getMap().put(new Location(5,7),n);
		game.getMap().put(new Location(6,7),i);
		game.getMap().put(new Location(7,7),m);
		game.getMap().put(new Location(8,7),a2);
		game.getMap().put(new Location(9,7),l);
	
		
		game.getBoard().getSquare(4, 7).setTile(a);
		game.getBoard().getSquare(5, 7).setTile(n);
		game.getBoard().getSquare(6, 7).setTile(i);
		game.getBoard().getSquare(7, 7).setTile(m);
		game.getBoard().getSquare(8, 7).setTile(a2);
		game.getBoard().getSquare(9, 7).setTile(l);
		
		
		game.confirm();
		// player adds addjacent 'animal' in row all in normal squares 
		assertEquals(player1.getScore(),8);	
	}
	@Test
	public void testNotARow(){
		game.startNewGame();
	
		game.getMap().put(new Location(4,7),a);
		game.getMap().put(new Location(5,7),n);
		game.getMap().put(new Location(6,7),i);
		game.getMap().put(new Location(7,7),m);
		game.getMap().put(new Location(8,7),a2);
		game.getMap().put(new Location(10,7),l);
	
		
		game.getBoard().getSquare(4, 7).setTile(a);
		game.getBoard().getSquare(5, 7).setTile(n);
		game.getBoard().getSquare(6, 7).setTile(i);
		game.getBoard().getSquare(7, 7).setTile(m);
		game.getBoard().getSquare(8, 7).setTile(a2);
		game.getBoard().getSquare(10, 7).setTile(l);
		
		game.confirm();
		// player adds addjacent 'animal' in column in all in normal squares 
		assertEquals(player1.getScore(),0);	
	}
	
	@Test
	public void testAdjacent(){
		game.startNewGame();
		
		game.getMap().put(new Location(4,7),a);
		game.getMap().put(new Location(5,7),n);
		game.getMap().put(new Location(6,7),i);
		game.getMap().put(new Location(7,7),m);
		game.getMap().put(new Location(8,7),a2);
		game.getMap().put(new Location(9,7),l);
	
		
		game.getBoard().getSquare(4, 7).setTile(a);
		game.getBoard().getSquare(5, 7).setTile(n);
		game.getBoard().getSquare(6, 7).setTile(i);
		game.getBoard().getSquare(7, 7).setTile(m);
		game.getBoard().getSquare(8, 7).setTile(a2);
		game.getBoard().getSquare(9, 7).setTile(l);
		
		
		game.confirm();
		
		game.getMap().put(new Location(4,6),a);
		game.getMap().put(new Location(5,6),n);
		game.getMap().put(new Location(6,6),i);
		game.getMap().put(new Location(7,6),m);
		game.getMap().put(new Location(8,6),a2);
		game.getMap().put(new Location(9,6),l);
	
		
		game.getBoard().getSquare(4, 6).setTile(a);
		game.getBoard().getSquare(5, 6).setTile(n);
		game.getBoard().getSquare(6, 6).setTile(i);
		game.getBoard().getSquare(7, 6).setTile(m);
		game.getBoard().getSquare(8, 6).setTile(a2);
		game.getBoard().getSquare(9, 6).setTile(l);
		
	
		game.confirm();
		assertEquals(player1.getScore(),8);
		assertEquals(player2.getScore(),0);	
	}
	
	@Test
	public void testNotACol(){
		game.startNewGame();
		
		game.getMap().put(new Location(7,4),a);
		game.getMap().put(new Location(7,5),n);
		game.getMap().put(new Location(7,6),i);
		game.getMap().put(new Location(7,7),m);
		game.getMap().put(new Location(7,8),a2);
		game.getMap().put(new Location(7,10),l);
	
		
		game.getBoard().getSquare(7, 4).setTile(a);
		game.getBoard().getSquare(7, 5).setTile(n);
		game.getBoard().getSquare(7, 6).setTile(i);
		game.getBoard().getSquare(7, 7).setTile(m);
		game.getBoard().getSquare(7, 8).setTile(a2);
		game.getBoard().getSquare(7, 9).setTile(l);
	
		
		game.confirm();
		// player adds addjacent 'animal' in column in all in normal squares 
		assertEquals(player1.getScore(),0);	
	}
	
	@Test
	public void testNotInMiddle(){
		game.startNewGame();
		
		
		game.getMap().put(new Location(6,1),a);
		game.getMap().put(new Location(6,2),n);
		game.getMap().put(new Location(6,3),i);
		game.getMap().put(new Location(6,4),m);
		game.getMap().put(new Location(6,5),a2);
		game.getMap().put(new Location(6,6),l);
	
		
		game.getBoard().getSquare(6, 1).setTile(a);
		game.getBoard().getSquare(6, 2).setTile(n);
		game.getBoard().getSquare(6, 3).setTile(i);
		game.getBoard().getSquare(6, 4).setTile(m);
		game.getBoard().getSquare(6, 5).setTile(a2);
		game.getBoard().getSquare(6, 6).setTile(l);
		

		
		game.confirm();
		// player adds addjacent 'animal' in column in all in normal squares 
		assertEquals(player1.getScore(),0);	
	}
	
	@Test
	public void testServalWords(){
		game.startNewGame();
		
		game.getMap().put(new Location(4,7),a);
		game.getMap().put(new Location(5,7),n);
		game.getMap().put(new Location(6,7),i);
		game.getMap().put(new Location(7,7),m);
		game.getMap().put(new Location(8,7),a2);
		game.getMap().put(new Location(9,7),l);
	
		
		game.getBoard().getSquare(4, 7).setTile(a);
		game.getBoard().getSquare(5, 7).setTile(n);
		game.getBoard().getSquare(6, 7).setTile(i);
		game.getBoard().getSquare(7, 7).setTile(m);
		game.getBoard().getSquare(8, 7).setTile(a2);
		game.getBoard().getSquare(9, 7).setTile(l);
		
		
		game.confirm();
		game.getMap().put(new Location(8,6),e);
		game.getMap().put(new Location(8,8),t);
		
	
		
		game.getBoard().getSquare(8, 6).setTile(e);
		game.getBoard().getSquare(8, 8).setTile(t);


		game.confirm();
		
		assertEquals(player1.getScore(),8);
		assertEquals(player2.getScore(),5);	
	}
	
	@Test
	// invalid word, still the same turn
	public void testNotInDicWord(){
		game.startNewGame();
		
		game.getMap().put(new Location(6,7),a2);
		game.getMap().put(new Location(7,7),a);
		game.getMap().put(new Location(8,7),m);
	
		game.getBoard().getSquare(6, 7).setTile(a2);
		game.getBoard().getSquare(7, 7).setTile(a);
		game.getBoard().getSquare(8, 7).setTile(m);

		game.confirm();
		assertEquals(player1.getScore(),0);
		assertEquals(game.getCurrentPlayer(),player1);
		
	}
	
	@Test
	public void testSingleCharWord(){
		game.startNewGame();
		
		game.getMap().put(new Location(7,7),a2);
		game.getBoard().getSquare(7, 7).setTile(a2);
	
		
		game.confirm();
		assertEquals(player1.getScore(),0);
		
	}

	@Test
	public void testSpecialTile(){
		
		
		game.startNewGame();
		player1.addScore(10);
		game.purchase(4);
		
		game.getMap().put(new Location(4,7),a);
		game.getMap().put(new Location(5,7),n);
		game.getMap().put(new Location(6,7),i);
		game.getMap().put(new Location(7,7),m);
		game.getMap().put(new Location(8,7),a2);
		game.getMap().put(new Location(9,7),l);
	
		
		game.getBoard().getSquare(4, 7).setTile(a);
		game.getBoard().getSquare(5, 7).setTile(n);
		game.getBoard().getSquare(6, 7).setTile(i);
		game.getBoard().getSquare(7, 7).setTile(m);
		game.getBoard().getSquare(8, 7).setTile(a2);
		game.getBoard().getSquare(9, 7).setTile(l);
		
		Location loc = new Location(6,8);
		game.putSpecialTile(loc, new BoomTile());	
		
		SpecialTile specialTile = new BoomTile();
		game.getSpecialMap().put(loc, specialTile);
		specialTile.setLocation(loc);
		
		Location loc2 = new Location(6,9);
		SpecialTile specialTile2 = new ReverseTile();
		game.putSpecialTile(loc2, specialTile2);	
		
		game.getSpecialMap().put(loc2, specialTile2);
		specialTile.setLocation(loc2);
		game.confirm();
		assertEquals(game.getCurrentPlayer().getIndex(),3);

	
	}


}

