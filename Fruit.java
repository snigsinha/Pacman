package Pacman;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * This class models the Fruit in the Pacman game. An instance of this class is
 * contained in game to have fruit appear on the Pacman Board. This class
 * implements the Collidable interface as Pacman can eat fruit. 
 * This is so that in the game class, for each smartSquare, the ArrayList
 * uses polymorphism of Collidable objects. 
 */
public class Fruit implements Collidable {
	private Game _game;
	private Pane _pane;
	private Pacman _pacman;
	private ImageView _fruit;

	/**
	 * The instance variable _fruit is created to graphically set up the fruit. It
	 * is an instance variable because it is called when setting up its appearance
	 * and when creating accessor and mutator methods for its location. There are
	 * instance variables _pacman, _game and _pane as information from these classes
	 * are needed to deal specifically with when a fruit collides with pacman.
	 */
	public Fruit(Pacman pacman, Game game, Pane pane) {

		/**
		 * In the constructor, pacman, game and pane are taken in as parameters to
		 * finish setting up the association needed for when setting up the dot's
		 * collide method. A instance of an ellipse is created to my personal choice of
		 * graphical representation
		 */
		_game = game;
		_pacman = pacman;
		_pane = pane;
		_fruit = new ImageView(
				new Image("Pacman/cherry.png", Constants.SQUARE_SIDE, Constants.SQUARE_SIDE, true, true));
		_fruit.setCache(true);
	}

	/**
	 * This method sets the x location of the fruit using the set location method
	 * from java for ImageView. This is used when setting location of the fruit.
	 */
	public void setLocX(double x) {
		_fruit.setX(x);
	}

	/**
	 * This method sets the y location of the fruit using the set location method
	 * from java for ImageView. This is used when setting location of the fruit.
	 */
	public void setLocY(double y) {
		_fruit.setY(y);
	}

	/**
	 * This method gets the x location of the fruit using the get location method
	 * from java for ImageView.
	 */
	public double getLocX() {
		return _fruit.getX();
	}

	/**
	 * This method gets the y location of the fruit using the get location method
	 * from java for ImageView.
	 */
	public double getLocY() {
		return _fruit.getY();
	}

	/*
	 * This method returns the node of the fruit.This is used when adding the fruit
	 * to the pane in the Game class.
	 */
	public Node getNode() {
		return _fruit;
	}

	/*
	 * This is the collide method from the Collidable interface that is defined
	 * here. This method is called in the game class when Pacman "collides" with
	 * objects to give the appearance that pacman is eating a fruit. The instance
	 * variables of _game, _pacman and _pane are called on here to get relevant
	 * information from those classes
	 */
	public void collide() {
		// Removing dot from ArrayList of SmartSquare when colliding with
		// pacman,therefore Pacman's location is referred to
		_game.getBoardArray()[(int) ((_pacman.getLocY() - (Constants.SQUARE_SIDE / 2))
				/ Constants.SQUARE_SIDE)][(int) ((_pacman.getLocX() - (Constants.SQUARE_SIDE / 2))
						/ Constants.SQUARE_SIDE)].getArrayList().remove(this);
		// Graphically removes dot
		_pane.getChildren().remove(this.getNode());

	}

	/*
	 * This is the getScore method from the Collidable interface that is defined
	 * here. This method is called in the game class when Pacman "collides" with
	 * objects to return the value of the score that is earned when a dot is eaten
	 * by pacman. This is called in the game class to update the score appropriately
	 * (See game class's checkCollision method to see how this takes place)
	 */
	public int getScore() {
		return Constants.FRUIT_SCORE;

	}
}