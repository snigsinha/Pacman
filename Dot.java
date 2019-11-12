package Pacman;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 * This class models the dots in the Pacman game. Instances of this class are 
 * contained in game to have dots appear on the Pacman Board. This class
 * implements the Collidable interface as Pacman can collide with dots. 
 * This is so that in the game class, for each smartSquare, the ArrayList
 * uses polymorphism of Collidable objects. 
 */
public class Dot implements Collidable {

	/**
	 * The instance variable _dot is created to graphically set up the dot. It is an
	 * instance variable because it is called when setting up its appearance and
	 * when creating accessor and mutator methods for the location of the dot. There
	 * are instance variables _pacman, _game and _pane as information from these
	 * classes are needed to deal specifically with when a dot collides with pacman.
	 */
	private Ellipse _dot;
	private Pacman _pacman;
	private Game _game;
	private Pane _pane;

	public Dot(Pacman pacman, Game game, Pane pane) {

		/**
		 * In the constructor, pacman, game and pane are taken in as parameters to
		 * finish setting up the association needed for when setting up the dot's
		 * collide method. A instance of an ellipse is created to my personal choice of
		 * graphical representation
		 */
		_game = game;
		_pacman = pacman;
		_pane = pane;
		_dot = new Ellipse(Constants.DOT_RAD, Constants.DOT_RAD);
		_dot.setFill(Color.WHEAT);

	}

	/**
	 * This method sets the x location of the dot using the set location method from
	 * java for ellipses. This is used when setting location of the dot.
	 */
	public void setLocX(double x) {

		_dot.setCenterX(x);
	}

	/**
	 * This method sets the y location of the dot using the set location method from
	 * java for ellipses. This is used when setting location of the dot.
	 */
	public void setLocY(double y) {
		_dot.setCenterY(y);
	}

	/**
	 * This method gets the x location of the dot using the get location method from
	 * java for ellipses.
	 */
	public double getLocX() {
		return _dot.getCenterX();
	}

	/**
	 * This method gets the y location of the dot using the get location method from
	 * java for ellipses.
	 */
	public double getLocY() {
		return _dot.getCenterY();
	}

	/**
	 * This method returns the node of the dot.This is used when adding the dot to
	 * the pane in the Game class.
	 */
	public Node getNode() {
		return _dot;
	}

	/**
	 * This is the collide method from the Collidable interface that is defined
	 * here. This method is called in the game class when Pacman "collides" with
	 * objects to give the appearance that pacman is eating a dot. This method also
	 * helps the game keep track of how many dots and energizers are left on the
	 * pacman board as when all the dots and energizers are eaten, the game is won.
	 * The instance variables of _game, _pacman and _pane are called on here to get
	 * relevant information from those classes
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

	}

	/**
	 * This is the getScore method from the Collidable interface that is defined
	 * here. This method is called in the game class when Pacman "collides" with
	 * objects to return the value of the score that is earned when a dot is eaten
	 * by pacman. This is called in the game class to update the score appropriately
	 * (See game class's checkCollision method to see how this takes place)
	 */
	public int getScore() {

		return Constants.DOT_SCORE;
	}

}