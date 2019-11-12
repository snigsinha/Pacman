package Pacman;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * This is the SideBar class which handles the representation of scores, 
 * lives and when game is over. It also sets up the quit button and handles
 * the functionality of quitting the game
 */
public class Sidebar {

	/**
	 * private instances of the labels are created as they are called in more than
	 * one method in the class. This is mainly so that when certain conditions are
	 * checked for in the game class these methods can be called to change the text
	 * of these labels appropriately. Note that the instance of the _livesValue is
	 * in this class rather than in the game class like the variable that keeps
	 * track of the score is. This is because the game class does not need to keep
	 * track of the number of lives, but just the condition for when a life is lost.
	 */
	private Label _score;
	private Label _lives;
	private Label _gameOver;
	private Label _gameWon;
	private HBox _hbox;
	private int _livesValue;

	public Sidebar(HBox hbox) {
		/**
		 * The constructor takes in an HBox as a parameter so it can be added to the
		 * instance of the HBox in the PaneOrganiser class. Appropriate methods are
		 * called to set up the appearance of the sidebar and the functionality of the
		 * quitButton
		 */
		_hbox = hbox;
		// _livesValue is set to 3, the number of lives for when the game starts
		_livesValue = Constants.STARTING_LIVES;
		_hbox.setFocusTraversable(false);
		this.setUpLabels();
		this.setUpQuitButton();
		_hbox.setSpacing(Constants.SPACING);

	}

	/**
	 * This method takes care to set up the sidebar labels for when the app starts.
	 * It is private as it is only called in the constructor to set things up just
	 * for when the game starts
	 */
	private void setUpLabels() {
		_score = new Label();
		_score.setTextFill(Color.BLACK);
		_score.setText("Score: " + 0);

		_lives = new Label();
		_lives.setTextFill(Color.BLACK);
		_lives.setText("Lives: " + _livesValue);

		_gameOver = new Label();
		_gameOver.setTextFill(Color.RED);

		_gameWon = new Label();
		_gameWon.setTextFill(Color.GREEN);

		_hbox.getChildren().addAll(_score, _lives, _gameOver, _gameWon);

	}

	/**
	 * This method updates the score label to show the changing score in the game.
	 * This method is called in the game class so it is constantly updated when
	 * collisions take place (See checkCollision() in game class). As a result of
	 * this being called in the game class, this method is public.
	 */
	public void updateScore(int score) {
		_score.setText("Score: " + score);
	}

	public void updateLives() {

		_livesValue--;
		_lives.setText("Lives: " + _livesValue);
	}

	public int getLives() {
		return _livesValue;
	}

	/**
	 * This method updates the _gameOver label to show when the game is over. This
	 * method is called in the game class so game Over is constantly checked for
	 * (See checkGameEnds() in game class). As a result of this being called in the
	 * game class, this method is public.
	 */
	public void showGameOver() {
		_gameOver.setText("Game Over!");
	}

	/**
	 * This method updates the _gameWon label to show when the game is won. This
	 * method is called in the game class so game won is constantly checked for (See
	 * checkGameEnds() in game class). As a result of this being called in the game
	 * class, this method is public.
	 */
	public void showGameWon() {
		_gameWon.setText("You Won!");
	}

	// This method sets up the quitButton functionally and graphically
	private void setUpQuitButton() {
		Button quit = new Button("Quit");
		quit.setOnAction(new QuitHandler());
		_hbox.getChildren().add(quit);
	}

	// This private inner class takes care of the functionality of the quitButton
	private class QuitHandler implements EventHandler<ActionEvent> {
		/**
		 * QuitHandler is a private subclass of EventHandler set up so that when added
		 * to the quit button, the app quits
		 */
		public QuitHandler() {
		}

		public void handle(ActionEvent q) {
			Platform.exit();
		}
	}

}
