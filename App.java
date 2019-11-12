package Pacman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the main class where your Pacman game will start. The main method of
 * this application calls the App constructor.
 */

public class App extends Application {

	@Override
	public void start(Stage stage) {
		// Creates top-level object, set up the scene, and stage is hown.
		PaneOrganiser organiser = new PaneOrganiser();
		stage.setScene(new Scene(organiser.getRoot(), Constants.GAMEPANE_WIDTH,
				Constants.GAMEPANE_HEIGHT + Constants.HBOX_HEIGHT));
		stage.setTitle("Pacman!");
		stage.show();

	}

	/**
	 * Here is the mainline! No need to change this.
	 */
	public static void main(String[] argv) {
		// launch is a method inherited from Application
		launch(argv);
	}
}
