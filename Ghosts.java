package Pacman;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class models a Ghost in the Pacman game.This class implements the
 * Collidable interface as Pacman can collide with Ghosts. This is so that in
 * the game class, for each smartSquare, the ArrayList uses polymorphism of
 * Collidable objects. Instances of this class are contained in game to have
 * Ghosts appear on the Pacman Board.
 */
public class Ghosts implements Collidable {

	/**
	 * The instance variable _ghost is created to graphically set up the Ghost. It
	 * is an instance variable because it is called when setting up its appearance
	 * and when creating accessor and mutator methods for the location of the dot.
	 * There are instance variables _game, _sidebar as information from these
	 * classes are needed to deal specifically in the methods bfs(),
	 * checkValidNeighbours(), moveGhost(), getFrightenedDirection(), collide() and
	 * getScore(). The 2D array of directions is an instance variable as it is
	 * called on in both bfs() and checkValidNeighbours() and _currentDirection is
	 * an instance variable as it's used in both bfs() and getFrightenedDirection().
	 */
	private Rectangle _ghost;
	private Direction[][] _direction;
	private Direction _currentDirection;
	private Game _game;
	private Sidebar _sidebar;

	public Ghosts(Color color, Game game, Sidebar sidebar) {

		/**
		 * In the constructor, associations for game and side bar are set up. The
		 * constructor takes in Color as a parameter as in the game, each instance of
		 * ghost is a different color (when not in frightened mode). The Ghost is set up
		 * graphically to my aesthetic.
		 * 
		 */
		_game = game;
		_sidebar = sidebar;
		_ghost = new Rectangle(Constants.SQUARE_SIDE, Constants.SQUARE_SIDE);
		_ghost.setFill(color);
		// _currentDireciton set to null as when bfs() is called it will be updated
		_currentDirection = null;

	}

	private boolean checkValidNeighbours(int y, int x) {
		boolean flag = false;
		if (_game.moveValidity(x * Constants.SQUARE_SIDE, y * Constants.SQUARE_SIDE) && _direction[y][x] == null) {
			flag = true;
		}
		return flag;
	}

	/**
	 * This method uses breath first search algorithm to return the direction the
	 * ghost should take to take the shortest path to its target. It is called in
	 * the game class when the GhostMode is SCATTER or FRIGHTENED. (See
	 * moveGhostsAndChangeColor() method in Game Class). Everything executed in this
	 * method is done according to the help slides and hand out. This method takes
	 * in a target as a parameter as different ghosts have different targets in
	 * different GhostModes (See moveGhostsAndChangeColor() method in Game class)
	 */
	public Direction bfs(BoardCoordinate target) {
		/**
		 * Local Variable created to keep track of Directions in a Queue of
		 * BoardCoordinates as prescribes by handout and helpslides so each square is
		 * checked
		 */
		Queue<BoardCoordinate> squareQueue = new LinkedList<BoardCoordinate>();

		// New Direction 2D array instantiated each time bfs is called to reset the
		// array
		_direction = new Direction[Constants.BOARD_SIDE][Constants.BOARD_SIDE];

		/**
		 * Local Variables created and set up to keep track of distances and cells
		 * giving the closest distance closestDistance set to inifinity as any distance
		 * calculuated after that will be set as closest distance appropriately
		 */
		Double closestDistance = Double.POSITIVE_INFINITY;
		BoardCoordinate closestDistanceCell = new BoardCoordinate((int) this.getLocY() / Constants.SQUARE_SIDE,
				(int) this.getLocX() / Constants.SQUARE_SIDE, false);
		Double currentCellDistance = 0.0;

		// Takes care of edge cases where Ghost movement is wrapped
		if ((this.getLocY() / Constants.SQUARE_SIDE) == Constants.WRAP_YLOC
				&& (this.getLocX() / Constants.SQUARE_SIDE) == Constants.WRAP_LEFT_XLOC) {

			if (_currentDirection == Direction.LEFT) {
				// Appropriate neighbour added to the queue
				squareQueue.add(new BoardCoordinate(Constants.WRAP_YLOC, Constants.WRAP_RIGHT_XLOC, false));
				// Appropriate direction added to 2D board array
				_direction[Constants.WRAP_YLOC][Constants.WRAP_RIGHT_XLOC] = Direction.LEFT;

				/**
				 * adding neighbour to Queue and direction to 2D array is repeated when
				 * conditions for valid neighbours are satisfied
				 */
			}

			if (_currentDirection == Direction.RIGHT) {
				squareQueue.add(new BoardCoordinate(Constants.WRAP_YLOC, Constants.WRAP_LEFT_XLOC + 1, false));
				_direction[Constants.WRAP_YLOC][Constants.WRAP_LEFT_XLOC + 1] = Direction.RIGHT;
			}

		} else if ((this.getLocY() / Constants.SQUARE_SIDE) == Constants.WRAP_YLOC
				&& (this.getLocX() / Constants.SQUARE_SIDE) == Constants.WRAP_RIGHT_XLOC) {

			if (_currentDirection == Direction.RIGHT) {
				squareQueue.add(new BoardCoordinate(Constants.WRAP_YLOC, Constants.WRAP_LEFT_XLOC, false));
				_direction[Constants.WRAP_YLOC][Constants.WRAP_LEFT_XLOC] = Direction.RIGHT;
			}

			if (_currentDirection == Direction.LEFT) {
				squareQueue.add(new BoardCoordinate(Constants.WRAP_YLOC, Constants.WRAP_RIGHT_XLOC - 1, false));
				_direction[Constants.WRAP_YLOC][Constants.WRAP_RIGHT_XLOC - 1] = Direction.LEFT;
			}

		} else {
			/**
			 * if ghost movement is not wrapped, ghost's INITIAL directionsfor each possible
			 * direction (left, right, up, down) are checked for normally. moveValidity
			 * method from game checks for walls while currentDirection is checked for
			 * opposite direction to ensure that Ghost doesn't turn 180 degrees
			 */

			if (_game.moveValidity(this.getLocX() + Constants.SQUARE_SIDE, this.getLocY())
					&& !(_currentDirection == Direction.LEFT)) {
				squareQueue.add(new BoardCoordinate((int) ((this.getLocY()) / Constants.SQUARE_SIDE),
						(int) (this.getLocX() / Constants.SQUARE_SIDE) + 1, false));
				_direction[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
						/ Constants.SQUARE_SIDE) + 1] = Direction.RIGHT;
			}

			if (_game.moveValidity(this.getLocX() - Constants.SQUARE_SIDE, this.getLocY())
					&& !(_currentDirection == Direction.RIGHT)) {
				squareQueue.add(new BoardCoordinate((int) (this.getLocY() / Constants.SQUARE_SIDE),
						(int) (this.getLocX() / Constants.SQUARE_SIDE) - 1, false));
				_direction[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
						/ Constants.SQUARE_SIDE) - 1] = Direction.LEFT;
			}

			if (_game.moveValidity(this.getLocX(), this.getLocY() + Constants.SQUARE_SIDE)
					&& !(_currentDirection == Direction.UP)) {
				squareQueue.add(new BoardCoordinate((int) ((this.getLocY() / Constants.SQUARE_SIDE) + 1),
						(int) (this.getLocX() / Constants.SQUARE_SIDE), false));
				_direction[(int) (this.getLocY() / Constants.SQUARE_SIDE) + 1][(int) this.getLocX()
						/ Constants.SQUARE_SIDE] = Direction.DOWN;
			}

			if (_game.moveValidity(this.getLocX(), this.getLocY() - Constants.SQUARE_SIDE)
					&& !(_currentDirection == Direction.DOWN)) {
				squareQueue.add(new BoardCoordinate((int) ((this.getLocY() / Constants.SQUARE_SIDE) - 1),
						(int) this.getLocX() / Constants.SQUARE_SIDE, false));
				_direction[(int) ((this.getLocY() / Constants.SQUARE_SIDE) - 1)][(int) this.getLocX()
						/ Constants.SQUARE_SIDE] = Direction.UP;
			}

		}

		/**
		 * This part of the algorithm checks for the neighbours for the ghosts initial
		 * neighbours. Here closest distance is calculated for each neighbour and the
		 * variables keeping track of closestDistance cell, currentCell,
		 * currentCellDistance and clostestCellDistance is updated accordingly
		 */

		while (!squareQueue.isEmpty())

		{
			BoardCoordinate currentCell = squareQueue.remove();
			// Calculation for distance between currentCell and target
			currentCellDistance = Math.sqrt(Math.pow(currentCell.getColumn() - target.getColumn(), Constants.POWER)
					+ Math.pow(currentCell.getRow() - target.getRow(), Constants.POWER));

			// ClosestDistance and closestDistanceCell is updated accordingly
			if (currentCellDistance < closestDistance) {
				closestDistance = currentCellDistance;
				closestDistanceCell = currentCell;
			}

			// Takes care of edge cases when wrapping ghost movement
			if (currentCell.getRow() == Constants.WRAP_YLOC && currentCell.getColumn() == Constants.WRAP_LEFT_XLOC) {
				if (_direction[Constants.WRAP_YLOC][Constants.WRAP_RIGHT_XLOC] == null) {
					squareQueue.add(new BoardCoordinate(Constants.WRAP_YLOC, Constants.WRAP_LEFT_XLOC, false));
					_direction[Constants.WRAP_YLOC][Constants.WRAP_RIGHT_XLOC] = _direction[currentCell
							.getRow()][currentCell.getColumn()];
				}
			} else if (currentCell.getRow() == Constants.WRAP_YLOC
					&& currentCell.getColumn() == Constants.WRAP_RIGHT_XLOC) {
				if (_direction[Constants.WRAP_YLOC][Constants.WRAP_LEFT_XLOC] == null) {
					squareQueue.add(new BoardCoordinate(Constants.WRAP_YLOC, Constants.WRAP_LEFT_XLOC, false));
					_direction[Constants.WRAP_YLOC][Constants.WRAP_LEFT_XLOC] = _direction[currentCell
							.getRow()][currentCell.getColumn()];
				}

			} else {
				/**
				 * Checks for Neighbours normally by checking for walls and whether a cell has
				 * already been checked for each possible direction (left, right, up, down) (See
				 * checkValidNeighbours() method in Ghosts.class (this class)). Valid neighbour
				 * is added to the Queue and Direction of currentCell is added for the location
				 * of the valid neighbour to the 2D Direction Array
				 */
				if (this.checkValidNeighbours(currentCell.getRow(), currentCell.getColumn() + 1)) {

					squareQueue.add(new BoardCoordinate(currentCell.getRow(), currentCell.getColumn() + 1, false));
					_direction[currentCell.getRow()][currentCell.getColumn()
							+ 1] = _direction[currentCell.getRow()][currentCell.getColumn()];

				}

				if (this.checkValidNeighbours(currentCell.getRow(), currentCell.getColumn() - 1)) {

					squareQueue.add(new BoardCoordinate(currentCell.getRow(), currentCell.getColumn() - 1, false));
					_direction[currentCell.getRow()][currentCell.getColumn()
							- 1] = _direction[currentCell.getRow()][currentCell.getColumn()];

				}

				if (this.checkValidNeighbours(currentCell.getRow() + 1, currentCell.getColumn())) {

					squareQueue.add(new BoardCoordinate(currentCell.getRow() + 1, currentCell.getColumn(), false));
					_direction[currentCell.getRow() + 1][currentCell
							.getColumn()] = _direction[currentCell.getRow()][currentCell.getColumn()];

				}

				if (this.checkValidNeighbours(currentCell.getRow() - 1, currentCell.getColumn())) {

					squareQueue.add(new BoardCoordinate(currentCell.getRow() - 1, currentCell.getColumn(), false));
					_direction[currentCell.getRow() - 1][currentCell
							.getColumn()] = _direction[currentCell.getRow()][currentCell.getColumn()];

				}

			}

		}
		// _currentDirection set to direction that will take ghost to the target with
		// the shortest path
		_currentDirection = _direction[closestDistanceCell.getRow()][closestDistanceCell.getColumn()];
		return _currentDirection;

	}

	/**
	 * This method moves the ghost based on the direction passed in the parameter.
	 * This parameter is taken in as different ghosts will have different directions
	 * for different modes. (See moveGhostsAndChangeColor() method in the Game
	 * class)It checks for each possible direction (left, right, up, down) then
	 * removes it from respective ArrayList of collidables for the smartSquare it is
	 * in, moves ghost graphically, then adds it to the respective ArrayList for the
	 * SmartSquare it is in.
	 */
	public void moveGhost(Direction d) {

		if (d == Direction.RIGHT) {
			// Taking care of edge case where ghost movement is wrapped while moving right
			if (this.getLocY() == (Constants.WRAP_YLOC * Constants.SQUARE_SIDE)
					&& this.getLocX() == (Constants.WRAP_RIGHT_XLOC * Constants.SQUARE_SIDE)) {

				_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
						/ Constants.SQUARE_SIDE)].getArrayList().remove(this);
				this.setLocX(Constants.WRAP_LEFT_XLOC * Constants.SQUARE_SIDE);
				_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
						/ Constants.SQUARE_SIDE)].getArrayList().add(this);
			} else {
				// Moving ghost normally when moving right
				_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
						/ Constants.SQUARE_SIDE)].getArrayList().remove(this);
				this.setLocX(this.getLocX() + Constants.SQUARE_SIDE);
				_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
						/ Constants.SQUARE_SIDE)].getArrayList().add(this);
			}
		}

		if (d == Direction.LEFT) {
			// Taking care of edge case where ghost movement is wrapped while moving right
			if (this.getLocY() == (Constants.WRAP_YLOC * Constants.SQUARE_SIDE)
					&& this.getLocX() == (Constants.WRAP_LEFT_XLOC * Constants.SQUARE_SIDE)) {

				_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
						/ Constants.SQUARE_SIDE)].getArrayList().remove(this);
				this.setLocX(Constants.WRAP_RIGHT_XLOC * Constants.SQUARE_SIDE);
				_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
						/ Constants.SQUARE_SIDE)].getArrayList().add(this);

			} else {
				// Moving ghost normally when moving left
				_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
						/ Constants.SQUARE_SIDE)].getArrayList().remove(this);
				this.setLocX(this.getLocX() - Constants.SQUARE_SIDE);
				_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
						/ Constants.SQUARE_SIDE)].getArrayList().add(this);
			}
		}

		if (d == Direction.UP) {

			_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
					/ Constants.SQUARE_SIDE)].getArrayList().remove(this);
			this.setLocY(this.getLocY() - Constants.SQUARE_SIDE);
			_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
					/ Constants.SQUARE_SIDE)].getArrayList().add(this);
		}

		if (d == Direction.DOWN) {

			_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
					/ Constants.SQUARE_SIDE)].getArrayList().remove(this);
			this.setLocY(this.getLocY() + Constants.SQUARE_SIDE);
			_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) (this.getLocX()
					/ Constants.SQUARE_SIDE)].getArrayList().add(this);
		}

	}

	/**
	 * This method generates a random valid direction at each intersection the ghost
	 * encounters for when the ghost is in frightened mode. An ArrayList is used to
	 * keep track of the varying number of valid directions a ghost can take at
	 * every intersection. For each possible direction (left, right, up down), walls
	 * are checked for and _currentDirection is checked so that a direction that
	 * would cause the ghost to turn 180 degrees won't be added
	 */
	public Direction getFrightenedGhostDirection() {
		ArrayList<Direction> randomValidDirection = new ArrayList<Direction>();
		// Taking care of edge cases for when ghost movement is wrapped
		if ((this.getLocY() / Constants.SQUARE_SIDE) == Constants.WRAP_YLOC
				&& (this.getLocX() / Constants.SQUARE_SIDE) == Constants.WRAP_LEFT_XLOC) {

			if (_currentDirection == Direction.LEFT) {
				randomValidDirection.add(Direction.LEFT);

			}

			if (_currentDirection == Direction.RIGHT) {
				randomValidDirection.add(Direction.RIGHT);

			}

		} else if ((this.getLocY() / Constants.SQUARE_SIDE) == Constants.WRAP_YLOC
				&& (this.getLocX() / Constants.SQUARE_SIDE) == Constants.WRAP_RIGHT_XLOC) {

			if (_currentDirection == Direction.RIGHT) {
				randomValidDirection.add(Direction.RIGHT);

			}
			if (_currentDirection == Direction.LEFT) {
				randomValidDirection.add(Direction.LEFT);
			}
		}

		else {
			// Dealing with normal cases for ghost movement
			if (_game.moveValidity(this.getLocX() + Constants.SQUARE_SIDE, this.getLocY())
					&& !(_currentDirection == Direction.LEFT)) {
				randomValidDirection.add(Direction.RIGHT);
			}

			if (_game.moveValidity(this.getLocX() - Constants.SQUARE_SIDE, this.getLocY())
					&& !(_currentDirection == Direction.RIGHT)) {
				randomValidDirection.add(Direction.LEFT);
			}

			if (_game.moveValidity(this.getLocX(), this.getLocY() - Constants.SQUARE_SIDE)
					&& !(_currentDirection == Direction.DOWN)) {
				randomValidDirection.add(Direction.UP);
			}

			if (_game.moveValidity(this.getLocX(), this.getLocY() + Constants.SQUARE_SIDE)
					&& !(_currentDirection == Direction.UP)) {
				randomValidDirection.add(Direction.DOWN);
			}

			/**
			 * Math.random is used to assign a random direction from the list of valid
			 * direction the ghost can take at each intersection
			 */
			_currentDirection = randomValidDirection.get((int) Math.random() * randomValidDirection.size());

		}
		return _currentDirection;
	}

	/**
	 * This method sets the color of the ghost as each ghost has a different color
	 * and in different modes, ghosts must change color (e.g. all ghosts are
	 * LightBlue in Frightened mode). (See moveGhostsAndChangeColor() method in the
	 * Game class)
	 */
	public void setColor(Color color) {
		_ghost.setFill(color);
	}

	/**
	 * This method sets the x location of the Ghost using the set location method
	 * from java for Rectangles. This is used when setting location of the Ghost.
	 */
	public void setLocX(double x) {
		_ghost.setX(x);
	}

	/**
	 * This method sets the y location of the Ghost using the set location method
	 * from java for Rectangles. This is used when setting location of the Ghost.
	 */
	public void setLocY(double y) {
		_ghost.setY(y);
	}

	/**
	 * This method gets the x location of the ghost using the get location method
	 * from java for Rectangles.
	 */
	public double getLocX() {
		return _ghost.getX();
	}

	/**
	 * This method gets the y location of the ghost using the get location method
	 * from java for Rectangles.
	 */
	public double getLocY() {
		return _ghost.getY();
	}

	/**
	 * This method returns the node of the ghost.This is used when adding the dot to
	 * the pane in the Game class.
	 */
	public Node getNode() {
		return _ghost;
	}

	/**
	 * This is the collide method from the Collidable interface that is defined
	 * here. This method is called in the game class when Pacman "collides" with
	 * objects to give the appearance that pacman is eating or getting eaten by a
	 * ghost. T The instance variables of _game, _sidebar and _pane are called on
	 * here to get relevant information from those classes
	 */
	public void collide() {
		/**
		 * In frightened mode, pacman eats the ghost during collision so ghosts are
		 * added and removed from the appropriate arraylists and added to the ghostpen
		 * queue appropriately
		 */
		if (_game.getCurrentGhostMode() == GhostMode.FRIGHTENED) {
			_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) ((this.getLocX())
					/ Constants.SQUARE_SIDE)].getArrayList().remove(this);
			_game.getGhostQueue().add(this);
			this.setLocX(Constants.GHOST_START_COL * Constants.SQUARE_SIDE);
			this.setLocY((Constants.GHOST_START_ROW + Constants.LEAVING_GHOST_LOCY_OFFSET) * Constants.SQUARE_SIDE);
			_game.getBoardArray()[(int) (this.getLocY() / Constants.SQUARE_SIDE)][(int) ((this.getLocX())
					/ Constants.SQUARE_SIDE)].getArrayList().add(this);

		} else {

			/**
			 * sidebar is the only class that updates lives and lives are only lost when
			 * pacman collides with a ghost in CHASE and SCATTER. The reset() method from
			 * game sets the location of pacman and ghosts to their initial positions 
			 */
			_sidebar.updateLives();
			_game.reset();

		}

	}

	/**
	 * This is the getScore method from the Collidable interface that is defined
	 * here. This method is called in the game class when Pacman "collides" with
	 * objects to return the value of the score that is earned when ghost is eaten
	 * by pacman. This is called in the game class to update the score appropriately and only in frightened mode
	 * (See game class's checkCollision method to see how this takes place)
	 */
	public int getScore() {
		int score = 0;
		if (_game.getCurrentGhostMode() == GhostMode.FRIGHTENED) {
			score = Constants.GHOST_SCORE;
		}

		return score;

	}

}