package Pacman;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class models Pacman in the Pacman game. Instances of this class are 
 * contained in game to have Pacman appear on the Pacman Board. 
 */
public class Pacman {
	
	/**
	 * The instance variable _pacman is created to graphically set up pacman. It is an
	 * instance variable because it is called when setting up its appearance and
	 * when creating accessor and mutator methods for the location of the pacman.
	 */
	private ImageView _pacman;

	public Pacman() {
		
		/**
		 * A instance of an ImageView is created to my personal choice of
		 * graphical representation of pacman
		 */
		_pacman = new ImageView(
				new Image("Pacman/pacman.png", Constants.SQUARE_SIDE, Constants.SQUARE_SIDE, true, true));
		_pacman.setCache(true);
	
	}

	/**
	 * This method sets the x location of pacman using the set location method from
	 * java for ellipses. This is used when setting location of pacman.
	 */
	public void setLocX(double x) { 
		_pacman.setX(x);
	}

	/**
	 * This method sets the y location of pacman using the set location method from
	 * java for ellipses. This is used when setting location of pacman.
	 */
	public void setLocY(double y) {
		_pacman.setY(y);
	}

	/**
	 * This method gets the x location of pacman using the get location method from
	 * java for ImageView.
	 */
	public double getLocX() {
		return _pacman.getX();
	}

	/**
	 * This method gets the y location of pacman using the get location method from
	 * java for ImageView.
	 */
	public double getLocY() {
		return _pacman.getY();
	}
	
	/**
	 * This method returns the node of pacman. This is used when adding pacman to
	 * the pane in the Game class.
	 */
	public Node getNode() {
		return _pacman;
	}

}
