package Pacman;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * This class deals with the organization of the panes in the pacman game and 
 * its graphical representation
 */
public class PaneOrganiser {

	/**
	 * The BorderPane _root is an instance variable because it is called more than
	 * once in this class
	 */
	private BorderPane _root;

	public PaneOrganiser() {

		/**
		 * In the constructor, instances of the sidebar and game are created so that
		 * when Pane Organizer is called in the App class, all the methods in their
		 * respective constructors will be called. Instances of pane and hbox are
		 * created so that they can be added to the BorderPane and set to an appropriate
		 * size for the game.
		 */
		_root = new BorderPane();
		HBox hbox = new HBox();
		Pane pane = new Pane();
		Sidebar sidebar = new Sidebar(hbox);
		Game game = new Game(pane, sidebar);
		pane.setPrefSize(Constants.GAMEPANE_WIDTH, Constants.GAMEPANE_HEIGHT);
		hbox.setPrefSize(Constants.HBOX_WIDTH, Constants.HBOX_HEIGHT);
		_root.setCenter(pane);
		_root.setBottom(hbox);

	}

	/**
	 * This method returns the BorderPane which is called when setting the scene in
	 * the App class
	 */
	public Pane getRoot() {
		return _root;
	}
}