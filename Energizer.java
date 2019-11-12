package Pacman;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 * This class models the energizers in the Pacman game. Instances of this class are 
 * contained in game to have energizers appear on the Pacman Board. This class
 * implements the Collidable interface as Pacman can collide with dots. 
 * This is so that in the game class, for each smartSquare, the ArrayList
 * uses polymorphism of Collidable objects. 
 */
public class Energizer implements Collidable {

	/**
	 * The instance variable _energizer is created to graphically set up the
	 * energizer. It is an instance variable because it is called when setting up
	 * its appearance and when creating accessor and mutator methods for the
	 * location of the energizer. There are instance variables _pacman, _game and
	 * _pane as information from these classes are needed to deal specifically with
	 * when a energizer collides with pacman.
	 */
	private Ellipse _energizer;
	private Pacman _pacman;
	private Game _game;
	private Pane _pane;

	public Energizer(Pacman pacman, Game game, Pane pane) {

		/**
		 * In the constructor, pacman, game and pane are taken in as parameters to
		 * finish setting up the association needed for when setting up the energizer's
		 * collide method. A instance of an ellipse is created to my personal choice of
		 * graphical representation
		 */
		_energizer = new Ellipse(Constants.ENERGIZER_RAD, Constants.ENERGIZER_RAD);
		_energizer.setFill(Color.WHEAT);
		_game = game;
		_pacman = pacman;
		_pane = pane;

	}

	/**
	 * This method sets the x location of the energizer using the set location
	 * method from java for ellipses. This is used when setting location of the
	 * energizer.
	 */
	public void setLocX(double x) {
		_energizer.setCenterX(x);
	}

	/**
	 * This method sets the y location of the energizer using the set location
	 * method from java for ellipses. This is used when setting location of the
	 * energizer.
	 */
	public void setLocY(double y) {
		_energizer.setCenterY(y);
	}

	/**
	 * This method gets the x location of the energizer using the get location
	 * method from java for ellipses.
	 */
	public double getLocX() {
		return _energizer.getCenterX();
	}

	/**
	 * This method gets the y location of the energizer using the get location
	 * method from java for ellipses.
	 */
	public double getLocY() {
		return _energizer.getCenterY();
	}

	/**
	 * This method returns the node of the energizer.This is used when adding the
	 * energizer to the pane in the Game class.
	 */
	public Node getNode() {
		return _energizer;
	}

	/**
	 * This is the collide method from the Collidable interface that is defined
	 * here. This method is called in the game class when Pacman "collides" with
	 * objects to give the appearance that pacman is eating a energizer. This method
	 * also helps the game keep track of how many dots and energizers are left on
	 * the pacman board as when all the dots and energizers are eaten, the game is
	 * won. This method also sets the GhostMode of the game to Frightened and
	 * changes the color of the Ghosts appropriately to indicate this to the user.The
	 * instance variables of _game, _pacman and _pane are called on here to get
	 * relevant information from those classes.
	 */
	public void collide() {
		// Keeping track of dots on board
		_game.updateDotCounter();
		// Removing dot from ArrayList of SmartSquare when colliding with
		// pacman,therefore Pacman's location is referred to
		_game.getBoardArray()[(int) (_pacman.getLocY() / Constants.SQUARE_SIDE)][(int) (_pacman.getLocX()
				/ Constants.SQUARE_SIDE)].getArrayList().remove(this);
		// Graphically removes dot
		_pane.getChildren().remove(this.getNode());
		//Setting mode to frightened logically and graphically 
		_game.setCurrentGhostMode(GhostMode.FRIGHTENED);
		_game.changeGhostColorFrightened();

	}
	
	/**
	 * This is the getScore method from the Collidable interface that is defined
	 * here. This method is called in the game class when Pacman "collides" with
	 * objects to return the value of the score that is earned when a dot is eaten
	 * by pacman. This is called in the game class to update the score appropriately
	 * (See game class's checkCollision method to see how this takes place)
	 */
	public int getScore() {
		return Constants.ENERGIZER_SCORE;

	}

}