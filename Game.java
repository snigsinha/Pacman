
package Pacman;

import java.util.LinkedList;
import java.util.Queue;
import cs015.fnl.PacmanSupport.BoardLocation;
import cs015.fnl.PacmanSupport.SupportMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * This class deals with all the Game logic in pacman, Including, pacman's
 * movement, changing modes, the ghost pen, setting up the map, checking for
 * collisions and when the game is over
 */
public class Game {

	private Pane _gamePane;
	private SmartSquare[][] _board;
	private Boolean[][] _wall;
	private Timeline _timeline;
	private Timeline _timelineGhostPen;
	private Pacman _pacman;
	private Ghosts _red;
	private Ghosts _orange;
	private Ghosts _blue;
	private Ghosts _pink;
	private Direction _pacmanDirection;
	private Direction _keyDirection;
	private int _score;
	private int _dotCounter;
	private Sidebar _sidebar;
	private Queue<Ghosts> _ghostQueue;
	private GhostMode _currentGhostMode;
	private int _counter;
	private int _frightenedModeCounter;
	private KeyHandler _keyHandler;

	public Game(Pane _pane, Sidebar sidebar) {

		/**
		 * Private instances of the ghost, pacman, timeline, 2D arrays, ints, Direction
		 * and keyHandlder are setup as instance variables as they are called on in
		 * methods throughout the game class. The constructor takes in a pane and a
		 * sidebar as a paramter as methods from those classes are called in the game
		 * class. Each ghost is an instance variable as in some methods, ghosts are
		 * dealt with differently. (See moveGhostsAndChangeColor() method and reset()
		 * method in the game class)
		 */
		_gamePane = new Pane();
		_board = new SmartSquare[Constants.BOARD_SIDE][Constants.BOARD_SIDE];
		_ghostQueue = new LinkedList<Ghosts>();
		// reference to sidebar and pane set up
		_gamePane = _pane;
		_sidebar = sidebar;
		// keyHandler set up
		_keyHandler = new KeyHandler();
		_gamePane.setOnKeyPressed(_keyHandler);
		_gamePane.setFocusTraversable(true);
		// Direction and modes are set to null or relevant modes for when the game
		// starts
		_pacmanDirection = null;
		_keyDirection = null;
		_currentGhostMode = GhostMode.CHASE;
		// counters to switch between modes and to keep track of score and dots are set
		// to appropriate initial values
		_frightenedModeCounter = 0;
		_counter = 0;
		_score = 0;
		_dotCounter = Constants.NUM_DOTS;
		// Methods are called to set the game graphically and logically for when it
		// starts
		this.createBoard();
		this.setUpTimeline();
		this.initialGhostPen();
		this.setUpGhostPenTimeline();

	}

	// Timeline is set up in its standard form for smooth animation
	private void setUpTimeline() {
		KeyFrame kf = new KeyFrame(Duration.millis(Constants.DURATION), new MoveHandler());
		_timeline = new Timeline(kf);
		_timeline.setCycleCount(Animation.INDEFINITE);
		_timeline.play();

	}

	// Timeline is set up so ghosts are released from ghostpen periodically
	private void setUpGhostPenTimeline() {
		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.GHOST_PEN_DURATION), new GhostPenHandler());
		_timelineGhostPen = new Timeline(kf);
		_timelineGhostPen.setCycleCount(Animation.INDEFINITE);
		_timelineGhostPen.play();
	}

	/**
	 * This method sets up the pacman board based on the BoardLocation map
	 */
	private void createBoard() {
		BoardLocation[][] boardLocation = SupportMap.getMap();
		_wall = new Boolean[Constants.BOARD_SIDE][Constants.BOARD_SIDE];

		for (int r = 0; r < Constants.BOARD_SIDE; r++) {
			for (int c = 0; c < Constants.BOARD_SIDE; c++) {
				if (boardLocation[r][c] == BoardLocation.PACMAN_START_LOCATION) {
					SmartSquare smartSquare = new SmartSquare(Color.BLACK);
					_board[r][c] = smartSquare;
					_board[r][c].setLocX(c * Constants.SQUARE_SIDE);
					_board[r][c].setLocY(r * Constants.SQUARE_SIDE);
					_gamePane.getChildren().add(_board[r][c].getRectangles());
					_pacman = new Pacman();
					_pacman.setLocX(smartSquare.getLocX());
					_pacman.setLocY(smartSquare.getLocY());
					_gamePane.getChildren().add(_pacman.getNode());
					// 2D array of boolean is used when checking for walls in checking validity
					// method
					_wall[r][c] = false;

				}
			}

		}

		for (int r = 0; r < Constants.BOARD_SIDE; r++) {
			for (int c = 0; c < Constants.BOARD_SIDE; c++) {
				if (boardLocation[r][c] == BoardLocation.WALL) {
					SmartSquare smartSquare = new SmartSquare(Color.BLUE);
					_board[r][c] = smartSquare;
					_board[r][c].setLocX(c * Constants.SQUARE_SIDE);
					_board[r][c].setLocY(r * Constants.SQUARE_SIDE);
					_gamePane.getChildren().add(_board[r][c].getRectangles());
					_wall[r][c] = true;

				}

			}
		}
		for (int r = 0; r < Constants.BOARD_SIDE; r++) {
			for (int c = 0; c < Constants.BOARD_SIDE; c++) {
				if (boardLocation[r][c] == BoardLocation.FREE) {
					SmartSquare smartSquare = new SmartSquare(Color.BLACK);
					_board[r][c] = smartSquare;
					_board[r][c].setLocX(c * Constants.SQUARE_SIDE);
					_board[r][c].setLocY(r * Constants.SQUARE_SIDE);
					_gamePane.getChildren().add(_board[r][c].getRectangles());
					_wall[r][c] = false;

				}
			}
		}

		for (int r = 0; r < Constants.BOARD_SIDE; r++) {
			for (int c = 0; c < Constants.BOARD_SIDE; c++) {
				if (boardLocation[r][c] == BoardLocation.DOT) {
					SmartSquare smartSquare = new SmartSquare(Color.BLACK);
					_board[r][c] = smartSquare;
					_board[r][c].setLocX(c * Constants.SQUARE_SIDE);
					_board[r][c].setLocY(r * Constants.SQUARE_SIDE);
					_gamePane.getChildren().add(_board[r][c].getRectangles());
					Dot dot = new Dot(_pacman, this, _gamePane);
					dot.setLocX(smartSquare.getLocX() + Constants.CONVERT_ELLIP_TO_RECT);
					dot.setLocY(smartSquare.getLocY() + Constants.CONVERT_ELLIP_TO_RECT);
					smartSquare.getArrayList().add(dot);
					_gamePane.getChildren().add(dot.getNode());
					_wall[r][c] = false;

				}
			}

		}
		for (int r = 0; r < Constants.BOARD_SIDE; r++) {
			for (int c = 0; c < Constants.BOARD_SIDE; c++) {

				if (boardLocation[r][c] == BoardLocation.ENERGIZER) {
					SmartSquare smartSquare = new SmartSquare(Color.BLACK);
					_board[r][c] = smartSquare;

					_board[r][c].setLocX(c * Constants.SQUARE_SIDE);
					_board[r][c].setLocY(r * Constants.SQUARE_SIDE);
					_gamePane.getChildren().add(_board[r][c].getRectangles());
					Energizer energizer = new Energizer(_pacman, this, _gamePane);
					energizer.setLocX(smartSquare.getLocX() + Constants.CONVERT_ELLIP_TO_RECT);
					energizer.setLocY(smartSquare.getLocY() + Constants.CONVERT_ELLIP_TO_RECT);
					smartSquare.getArrayList().add(energizer);
					_gamePane.getChildren().add(energizer.getNode());
					_wall[r][c] = false;

				}

			}
		}

		/**
		 * Fruit setup was hardcoded as it is extracredit so it's not part of the map
		 * and it is not a set BoardLocation
		 */
		SmartSquare smartSquareFruit = _board[Constants.FRUIT_ROW][Constants.FRUIT_COL];
		Fruit fruit = new Fruit(_pacman, this, _gamePane);
		fruit.setLocX(smartSquareFruit.getLocX());
		fruit.setLocY(smartSquareFruit.getLocY());
		smartSquareFruit.getArrayList().add(fruit);
		_gamePane.getChildren().add(fruit.getNode());

		for (int r = 0; r < Constants.BOARD_SIDE; r++) {
			for (int c = 0; c < Constants.BOARD_SIDE; c++) {
				if (boardLocation[r][c] == BoardLocation.GHOST_START_LOCATION) {
					SmartSquare smartSquare = new SmartSquare(Color.BLACK);
					_board[r][c] = smartSquare;
					_board[r][c].setLocX(c * Constants.SQUARE_SIDE);
					_board[r][c].setLocY(r * Constants.SQUARE_SIDE);
					_gamePane.getChildren().add(_board[r][c].getRectangles());

					_red = new Ghosts(Color.RED, this, _sidebar);
					_blue = new Ghosts(Color.CYAN, this, _sidebar);
					_pink = new Ghosts(Color.PINK, this, _sidebar);
					_orange = new Ghosts(Color.ORANGE, this, _sidebar);

					_red.setLocX(smartSquare.getLocX());
					_red.setLocY(smartSquare.getLocY() - (Constants.SQUARE_SIDE * Constants.LEAVING_GHOST_LOCY_OFFSET));

					_pink.setLocX(smartSquare.getLocX() + (Constants.SQUARE_SIDE * -1));
					_pink.setLocY(smartSquare.getLocY());

					_blue.setLocX(smartSquare.getLocX());
					_blue.setLocY(smartSquare.getLocY());

					_orange.setLocX(smartSquare.getLocX() + (Constants.SQUARE_SIDE * +1));
					_orange.setLocY(smartSquare.getLocY());

					smartSquare.getArrayList().add(_blue);
					smartSquare.getArrayList().add(_red);
					smartSquare.getArrayList().add(_pink);
					smartSquare.getArrayList().add(_orange);
					_board[r][c - 1].getArrayList().add(_pink);
					_board[r][c + 1].getArrayList().add(_orange);
					_board[r - Constants.LEAVING_GHOST_LOCY_OFFSET][c].getArrayList().add(_red);

					_gamePane.getChildren().addAll(_red.getNode(), _blue.getNode(), _pink.getNode(), _orange.getNode());
					_wall[r][c] = false;

				}
			}
		}
		// To move pacman to the front graphically
		_pacman.getNode().toFront();

	}

	/**
	 * This method sets up the ghost pen queue initially and is called in the
	 * constructor of game and in the rest method
	 */
	private void initialGhostPen() {
		_ghostQueue.add(_pink);
		_ghostQueue.add(_blue);
		_ghostQueue.add(_orange);

	}

	/**
	 * This method checks for collisions between pacman and collidable objects to
	 * update score and remove objects appropriately. This method loops through the
	 * arraylist of the smartsquare pacman is inin order to collide with every
	 * collidable object
	 */
	private void checkCollision() {
		// converting location to location on 2D array
		double xS = (_pacman.getLocX() / Constants.SQUARE_SIDE);
		double yS = (_pacman.getLocY() / Constants.SQUARE_SIDE);

		for (int i = 0; i < _board[(int) yS][(int) xS].getArrayList().size(); i++) {
			_score = _score + _board[(int) yS][(int) xS].getArrayList().get(i).getScore();
			_sidebar.updateScore(_score);
			_board[(int) yS][(int) xS].getArrayList().get(i).collide();

		}

	}

	/**
	 * This method checks moveValidity taking in x location and y location as
	 * parameters to check to see whether there is a wall. This returns a boolean
	 * for whether there is a wall or not at the location
	 */
	public boolean moveValidity(double x, double y) {
		boolean validMove = true;

		double xS = (x / Constants.SQUARE_SIDE);
		double yS = (y / Constants.SQUARE_SIDE);

		if (_wall[(int) yS][(int) xS]) {

			validMove = false;

		}
		return validMove;
	}

	/**
	 * This method deals with the movement of pacman. To ensure pacman moves
	 * correctly it takes the direction into account (controlled by key input, see
	 * keyHandler) and checks for walls with the moveValidity method. It also
	 * rotates the image of pacman so it's mouth is opening appropriately to have
	 * the illusion of pacman eating collidables objects
	 */
	private void movePacman() {

		if (_pacmanDirection == Direction.LEFT
				&& this.moveValidity(_pacman.getLocX() - Constants.SQUARE_SIDE, _pacman.getLocY())) {
			_pacman.setLocX(_pacman.getLocX() - Constants.SQUARE_SIDE);
			_pacman.setLocY(_pacman.getLocY());
			_pacman.getNode().setRotate(Constants.PACMAN_FACE_LEFT);

		}

		if (_pacmanDirection == Direction.RIGHT
				&& this.moveValidity(_pacman.getLocX() + Constants.SQUARE_SIDE, _pacman.getLocY())) {
			_pacman.setLocX(_pacman.getLocX() + Constants.SQUARE_SIDE);
			_pacman.setLocY(_pacman.getLocY());
			_pacman.getNode().setRotate(Constants.PACMAN_FACE_RIGHT);

		}

		if (_pacmanDirection == Direction.DOWN
				&& this.moveValidity(_pacman.getLocX(), _pacman.getLocY() + Constants.SQUARE_SIDE)) {

			_pacman.setLocX(_pacman.getLocX());
			_pacman.setLocY(_pacman.getLocY() + Constants.SQUARE_SIDE);
			_pacman.getNode().setRotate(Constants.PACMAN_FACE_DOWN);
		}

		if (_pacmanDirection == Direction.UP
				&& this.moveValidity(_pacman.getLocX(), _pacman.getLocY() - Constants.SQUARE_SIDE)) {

			_pacman.setLocX(_pacman.getLocX());
			_pacman.setLocY(_pacman.getLocY() - Constants.SQUARE_SIDE);
			_pacman.getNode().setRotate(Constants.PACMAN_FACE_UP);
		}

		this.wrapPacmanMovement();
		// Extra credit, pacman smooth movement
		this.moveSmoothly();
	}

	/**
	 * This method allows for pacman to move smoothly. If a key is pressed but
	 * pacman can't move because there is a wall, the keyDirection stores this and
	 * gets pacman to move in that direction as soon as there isn't a wall there
	 */
	private void moveSmoothly() {

		if (_keyDirection == Direction.RIGHT
				&& this.moveValidity(_pacman.getLocX() + Constants.SQUARE_SIDE, _pacman.getLocY())) {
			_pacmanDirection = _keyDirection;
		}

		if (_keyDirection == Direction.LEFT
				&& this.moveValidity(_pacman.getLocX() - Constants.SQUARE_SIDE, _pacman.getLocY())) {
			_pacmanDirection = _keyDirection;
		}

		if (_keyDirection == Direction.DOWN
				&& this.moveValidity(_pacman.getLocX(), _pacman.getLocY() + Constants.SQUARE_SIDE)) {
			_pacmanDirection = _keyDirection;
		}

		if (_keyDirection == Direction.UP
				&& this.moveValidity(_pacman.getLocX(), _pacman.getLocY() - Constants.SQUARE_SIDE)) {
			_pacmanDirection = _keyDirection;
		}

	}

	/**
	 * This method deals with the movement of pacman when the movement is wrapped
	 * taking direction into account
	 */
	private void wrapPacmanMovement() {

		if (_pacman.getLocY() == (Constants.WRAP_YLOC * Constants.SQUARE_SIDE)
				&& _pacman.getLocX() == ((Constants.WRAP_LEFT_XLOC) * Constants.SQUARE_SIDE)
				&& _pacmanDirection == Direction.LEFT) {
			_pacman.setLocX(Constants.WRAP_RIGHT_XLOC * Constants.SQUARE_SIDE);
		}

		if (_pacman.getLocY() == (Constants.WRAP_YLOC * Constants.SQUARE_SIDE)
				&& _pacman.getLocX() == (Constants.WRAP_RIGHT_XLOC * Constants.SQUARE_SIDE)
				&& _pacmanDirection == Direction.RIGHT) {
			_pacman.setLocX(Constants.WRAP_LEFT_XLOC * Constants.SQUARE_SIDE);
		}

	}

	/**
	 * This method changes the colors of the ghost apporpriately for when in
	 * frightened mode. This is triggered by pacman's collision with an energizer
	 * (See collide() method in energizer class)
	 */
	public void changeGhostColorFrightened() {
		_red.setColor(Color.LIGHTBLUE);
		_pink.setColor(Color.LIGHTBLUE);
		_blue.setColor(Color.LIGHTBLUE);
		_orange.setColor(Color.LIGHTBLUE);
	}

	/**
	 * This method deals with the movement of ghosts and its change in color based
	 * on the current ghost mode of the game. It also increments the counter whose
	 * conditions will be checked to swap between CHASE and SCATTER ghost modes and
	 * the counter for frightened mode.
	 */
	private void moveGhostsAndChangeColor() {

		if (_currentGhostMode == GhostMode.FRIGHTENED) {
			// ghosts move randomly hence the getFrightenedGhostDirection() is passed in the
			// moveGhost method for each ghost
			_frightenedModeCounter++;
			_red.moveGhost(_red.getFrightenedGhostDirection());
			_pink.moveGhost(_pink.getFrightenedGhostDirection());
			_blue.moveGhost(_blue.getFrightenedGhostDirection());
			_orange.moveGhost(_orange.getFrightenedGhostDirection());

		} else {
			// So during CHASE and SCATTER

			_red.setColor(Color.RED);
			_blue.setColor(Color.CYAN);
			_pink.setColor(Color.PINK);
			_orange.setColor(Color.ORANGE);
			_counter++;
			if (_currentGhostMode == GhostMode.CHASE) {
				// Ghost moves according to suggested targets in the help slides
				_red.moveGhost(_red.bfs(new BoardCoordinate((int) _pacman.getLocY() / Constants.SQUARE_SIDE,
						(int) _pacman.getLocX() / Constants.SQUARE_SIDE, true)));
				_pink.moveGhost(_pink.bfs(new BoardCoordinate((int) _pacman.getLocY() / Constants.SQUARE_SIDE + 1,
						(int) _pacman.getLocX() / Constants.SQUARE_SIDE + Constants.PINK_TARGET_OFFSET_X, true)));
				_blue.moveGhost(_blue.bfs(new BoardCoordinate(
						(int) _pacman.getLocY() / Constants.SQUARE_SIDE + Constants.BLUE_TARGET_OFFSET_Y,
						(int) _pacman.getLocX() / Constants.SQUARE_SIDE, true)));
				_orange.moveGhost(_orange.bfs(new BoardCoordinate((int) _pacman.getLocY() / Constants.SQUARE_SIDE,
						(int) _pacman.getLocX() / Constants.SQUARE_SIDE + Constants.ORANGE_TARGET_OFFSET_X, true)));
			}

			if (_currentGhostMode == GhostMode.SCATTER) {
				// Ghost moves according to suggested targets in the help slides in their
				// corners
				_red.moveGhost(
						_red.bfs(new BoardCoordinate(Constants.UPPER_RIGHT_ROW, Constants.UPPER_RIGHT_COL, true)));
				_pink.moveGhost(
						_pink.bfs(new BoardCoordinate(Constants.UPPER_LEFT_ROW, Constants.UPPER_RIGHT_COL, true)));
				_blue.moveGhost(
						_blue.bfs(new BoardCoordinate(Constants.LOWER_RIGHT_ROW, Constants.LOWER_RIGHT_COL, true)));
				_orange.moveGhost(
						_orange.bfs(new BoardCoordinate(Constants.LOWER_LEFT_ROW, Constants.LOWER_RIGHT_COL, true)));

			}

		}

	}

	/**
	 * This method changes the ghost mode according to counters set so that Ghost
	 * modes last for an appropriate amount of time
	 */
	private void controlModes() {
		if (_frightenedModeCounter > Constants.FRIGHTENED_MODE_RESET) {
			_frightenedModeCounter = 0;
			_currentGhostMode = null;

		}

		if (_currentGhostMode == GhostMode.FRIGHTENED) {

			/**
			 * set to 0 during frightened mode so that when frightened mode ends it starts
			 * again normally
			 */
			_counter = 0;
		}

		if (_counter > Constants.CHASE_SCATER_MODE_RESET) {
			// set to 0 to start again
			_counter = 0;
		}

		if (_counter > 0 && _counter < Constants.CHASE_TO_SCATTER) {
			_currentGhostMode = GhostMode.CHASE;
		}

		if (_counter > Constants.CHASE_TO_SCATTER) {
			_currentGhostMode = GhostMode.SCATTER;
		}
	}

	/**
	 * This method updates the dot counter and is called in dot and energizer
	 * classes (See checkGameEnds() method to see when this counter is used to check
	 * for gameWon
	 */
	public void updateDotCounter() {
		_dotCounter--;
	}

	/**
	 * This method deals with reseting pacman and ghosts to their intial positions
	 * graphically and logically once pacman is eaten by a ghost and a life is lost.
	 * Logically refers to removing and adding ghosts to the arraylist of
	 * smartSquares and queues appropriately
	 */
	public void reset() {
		// Set to whatever vairables were set to when the game started (see constructor
		// of the game class)
		_pacmanDirection = null;
		_keyDirection = null;
		_counter = 0;
		_currentGhostMode = GhostMode.CHASE;

		// ghostpen queue is reset
		_ghostQueue.clear();
		_ghostQueue.add(_red);

		// dealing with pacman
		_pacman.setLocX(Constants.PACMAN_START_COL * Constants.SQUARE_SIDE);
		_pacman.setLocY(Constants.PACMAN_START_ROW * Constants.SQUARE_SIDE);

		// dealing with red ghost
		_board[(int) (_red.getLocY() / Constants.SQUARE_SIDE)][(int) ((_red.getLocX()) / Constants.SQUARE_SIDE)]
				.getArrayList().remove(_red);
		_red.setLocX(Constants.GHOST_START_COL * Constants.SQUARE_SIDE);
		_red.setLocY((Constants.GHOST_START_ROW - Constants.LEAVING_GHOST_LOCY_OFFSET) * Constants.SQUARE_SIDE);
		_board[(int) (_red.getLocY() / Constants.SQUARE_SIDE)][(int) ((_red.getLocX()) / Constants.SQUARE_SIDE)]
				.getArrayList().add(_red);
		if (_ghostQueue.contains(_red)) {
			_ghostQueue.remove(_red);
		}

		// dealing with blue ghost
		_board[(int) (_blue.getLocY() / Constants.SQUARE_SIDE)][(int) ((_blue.getLocX()) / Constants.SQUARE_SIDE)]
				.getArrayList().remove(_blue);
		_blue.setLocX(Constants.GHOST_START_COL * Constants.SQUARE_SIDE);
		_blue.setLocY(Constants.GHOST_START_ROW * Constants.SQUARE_SIDE);
		_board[(int) (_blue.getLocY() / Constants.SQUARE_SIDE)][(int) ((_blue.getLocX()) / Constants.SQUARE_SIDE)]
				.getArrayList().add(_blue);

		// dealing with pink ghost
		_board[(int) (_pink.getLocY() / Constants.SQUARE_SIDE)][(int) ((_pink.getLocX()) / Constants.SQUARE_SIDE)]
				.getArrayList().remove(_pink);
		_pink.setLocX((Constants.GHOST_START_COL + 1) * Constants.SQUARE_SIDE);
		_pink.setLocY(Constants.GHOST_START_ROW * Constants.SQUARE_SIDE);
		_board[(int) (_pink.getLocY() / Constants.SQUARE_SIDE)][(int) ((_pink.getLocX()) / Constants.SQUARE_SIDE)]
				.getArrayList().add(_pink);

		// dealing with orange ghost
		_board[(int) (_orange.getLocY() / Constants.SQUARE_SIDE)][(int) ((_orange.getLocX()) / Constants.SQUARE_SIDE)]
				.getArrayList().remove(_orange);
		_orange.setLocX((Constants.GHOST_START_COL - 1) * Constants.SQUARE_SIDE);
		_orange.setLocY(Constants.GHOST_START_ROW * Constants.SQUARE_SIDE);
		_board[(int) (_orange.getLocY() / Constants.SQUARE_SIDE)][(int) ((_orange.getLocX()) / Constants.SQUARE_SIDE)]
				.getArrayList().add(_orange);

		// adding the blue, pink and orange ghost to the queue
		this.initialGhostPen();

	}

	/**
	 * This method checks to see if the game ends and calls appropriately methods on
	 * the reference to sidebar to change the label appropriately and stop timeline
	 * and disable keyhandler appropriately
	 */
	private void checkGameEnds() {

		// game is lost when all the lives are lost
		if (_sidebar.getLives() == 0) {
			_timeline.stop();
			_timelineGhostPen.stop();
			_gamePane.setOnKeyPressed(_keyHandler);
			_sidebar.showGameOver();
		}

		// game is won when all the dots and energizers are eaten
		if (_dotCounter == 0) {
			_timeline.stop();
			_timelineGhostPen.stop();
			_gamePane.setOnKeyPressed(_keyHandler);
			_sidebar.showGameWon();
		}
	}

	/**
	 * This method changes ghost mode and is called in the Energizer class as then
	 * the energizer collides with pacman, the ghostmode must be changed to
	 * frightened. (See collide method in energizer class)
	 */
	public void setCurrentGhostMode(GhostMode ghostMode) {
		_currentGhostMode = ghostMode;
	}

	/**
	 * This method returns the ghost mode which is called in the ghost class as
	 * collisions with ghost vary based on the mode frightened. (See collide method
	 * in energizer class)
	 */
	public GhostMode getCurrentGhostMode() {
		return _currentGhostMode;
	}

	/**
	 * This method returns the ghost queue as when pacman eats ghosts during
	 * frighetened mode, they must be added back to the ghost pen queue
	 */
	public Queue<Ghosts> getGhostQueue() {
		return _ghostQueue;
	}

	/**
	 * This method returns the BoardArray of smartSquares as the smartSquares
	 * arraylists must be accessed by all the classes that implement collidable to
	 * add and remove collidable objects appropriately
	 */
	public SmartSquare[][] getBoardArray() {
		return _board;
	}

	/**
	 * This private inner class deals with the keyInput that controls pacman's
	 * direction. It stores the keyDirection pressed for smooth movement and moves
	 * pacman only when there are no walls for normal movement
	 */
	private class KeyHandler implements EventHandler<KeyEvent> {

		public KeyHandler() {

		}

		public void handle(KeyEvent e) {
			KeyCode keyPressed = e.getCode();

			switch (keyPressed) {
			case RIGHT:

				_keyDirection = Direction.RIGHT;

				if (Game.this.moveValidity((_pacman.getLocX()) + Constants.SQUARE_SIDE, _pacman.getLocY())) {
					_pacmanDirection = Direction.RIGHT;
				}

				break;
			case LEFT:

				_keyDirection = Direction.LEFT;

				if (Game.this.moveValidity(_pacman.getLocX() - Constants.SQUARE_SIDE, _pacman.getLocY())) {
					_pacmanDirection = Direction.LEFT;
				}

				break;
			case DOWN:

				_keyDirection = Direction.DOWN;

				if (Game.this.moveValidity(_pacman.getLocX(), _pacman.getLocY() + Constants.SQUARE_SIDE)) {
					_pacmanDirection = Direction.DOWN;
				}

				break;
			case UP:

				_keyDirection = Direction.UP;

				if (Game.this.moveValidity(_pacman.getLocX(), _pacman.getLocY() - Constants.SQUARE_SIDE)) {
					_pacmanDirection = Direction.UP;
				}

				break;
			default:
				break;

			}
			e.consume();
		}
	}

	/**
	 * This private inner class deals with the timeline of the game which is
	 * constantly moving pacman and the ghosts, checking for collisions, controling
	 * ghost modes and checking for when the game ends
	 */
	private class MoveHandler implements EventHandler<ActionEvent> {
		/**
		 * This private inner class is a subclass of the abstract superclass
		 * EventHandler so the handle method is defined to call all the methods
		 * 
		 */
		public void handle(ActionEvent event) {

			Game.this.movePacman();
			Game.this.checkCollision();
			Game.this.moveGhostsAndChangeColor();
			Game.this.checkCollision();
			Game.this.controlModes();
			Game.this.checkGameEnds();

		}
	}

	/**
	 * This class deals with the timeline of the ghost pen so that periodically
	 * ghosts are released graphically and logically
	 */
	private class GhostPenHandler implements EventHandler<ActionEvent> {
		/**
		 * This private inner class is a subclass of the abstract superclass
		 * EventHandler so the handle method is defined to call all the methods
		 * 
		 */
		public void handle(ActionEvent event) {
			if (!_ghostQueue.isEmpty()) {
				Ghosts ghost = _ghostQueue.remove();
				ghost.setLocX(Constants.GHOST_START_COL * Constants.SQUARE_SIDE);
				ghost.setLocY(
						(Constants.GHOST_START_ROW - Constants.LEAVING_GHOST_LOCY_OFFSET) * Constants.SQUARE_SIDE);
				_board[Constants.GHOST_START_ROW - Constants.LEAVING_GHOST_LOCY_OFFSET][Constants.GHOST_START_COL]
						.getArrayList().add(ghost);
			}

		}
	}

}