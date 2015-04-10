
1. domain.pdf: it is a domain model, a UML class diagram. It provides a structural view of vocabulary and key concepts of the Scrabble game.  

2. system_sequence.pdf: it is the sequence diagram between the game and player(user) for the Scrabble game.

3. interaction_validate.pdf: sequence diagram of validating a move. Mainly judge if the tiles in a row or column, if it is adjacent to exist tiles and if it is a word in dictionary.

4. interaction_move.pdf: sequence diagram of executing a move.It mainly includes adding move into square of board, getting the special tile, calculating scores(get property and get value) and other methods.

5. behavioral contract.pdf: The behavioral contract for a user placing tiles on theboard including preconditions and postconditions.

6. object.pdf: object model of scrabble game. It provides the detail attributes and methods to implement this game.

7.rationale.pdf: to explain my design goals, principles and patterns I used when designing. And how I make a design decision.

8.Additional special tile(skip tile): skip next round.If a player activate a Skip tile, the player will stop playing in next round.

9. An abstract class of square and specialTile. It is convenient to extend them when adding new specialTile or even new property square.

