package Pacman;

/**
 * The Interface Collidable is made so the ArrayList in each smartSquare can keep track of
 * Dots, Energizers, Fruit and Ghosts
 */
public interface Collidable {

	/**
	 * This method is declared here as Ghosts, Energizers, Fruit and Dots collide
	 * with Pacman differently. (See respective classes for definition of these
	 * methods for each class).
	 */
	public void collide();

	/**
	 * This method is declared here as Ghosts, Energizers, Fruit and Dots add
	 * different scores to the game.(See respective classes for definition of these
	 * methods for each class and checkCollision() method in the Game class) 
	 */
	public int getScore();

}
