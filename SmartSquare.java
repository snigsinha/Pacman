package Pacman;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/*
 * This class models smartSquares in the Pacman game. Instances of this class are 
 * contained in game to have the Squares appear on the Pacman Board. This class also 
 * contains an ArrayList of Collidable objects so that in the Game class, whenever pacman 
 * enters a square, collisions are always checked for (See checkCollisions() method in 
 * the Game class). 
 */
public class SmartSquare {

	/*
	 * Private instance variables of the Rectangle and ArrayList are created as they
	 * are called upon more than once in this class
	 */
	private Rectangle _smartSquare;
	private ArrayList<Collidable> _container;;

	public SmartSquare(Color color) {

		/*
		 * New Instances of a Rectangle and ArraList is created so that every time a
		 * SmartSquare is instantiated, these are also instantiated.
		 */
		_smartSquare = new Rectangle(Constants.SQUARE_SIDE, Constants.SQUARE_SIDE, color);
		_container = new ArrayList<Collidable>();
	}

	/*
	 * This method sets the x location of the smartSquare using the set location
	 * method from java for Rectangles. This is used when setting location of the
	 * SmartSquare.
	 */
	public void setLocX(double x) {
		_smartSquare.setX(x);
	}

	/*
	 * This method sets the y location of the smartSquare using the set location
	 * method from java for Rectangles. This is used when setting location of the
	 * SmartSquare.
	 */
	public void setLocY(double y) {
		_smartSquare.setY(y);
	}

	/*
	 * This method gets the x location of the smartSquare using the get location
	 * method from java for Rectangles.
	 */
	public double getLocX() {
		return _smartSquare.getX();
	}

	/*
	 * This method gets the y location of the smartSquare using the get location
	 * method from java for Rectangles.
	 */
	public double getLocY() {
		return _smartSquare.getY();
	}

	/*
	 * This method return the rectangles in the array which is called when adding
	 * the SmartSquares graphically to the pane
	 */
	public Rectangle getRectangles() {
		return _smartSquare;
	}

	/*
	 * This method returns the ArrayList of the SmartSquare. This is called whenever
	 * a Collidable object is being added or removed in the Game, Dot, Energizer,
	 * Fruit or Ghosts classes
	 */
	public ArrayList<Collidable> getArrayList() {
		return _container;
	}

}