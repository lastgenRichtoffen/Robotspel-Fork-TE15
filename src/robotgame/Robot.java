package robotgame;
import java.util.ArrayList;
import blocks.*;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Robot extends Group {

	private final double SQUARE_SIZE;
	private AnimationTimer at;
	private static int NUMBER_OF_ROBOTS = 0;

	private Color color;
	private State state = State.FREE;
	private ArrayList<Operation> stack = new ArrayList<Operation>();

	private Node moveObject = null;

	/**
	 * Creates a new robot with the given size.
	 * 
	 * @param size
	 *            the length of the square
	 */
	public Robot(double size) {

		NUMBER_OF_ROBOTS++;

		choseColor();

		this.setTranslateX(1);
		this.setTranslateY(1);

		this.SQUARE_SIZE = size;
		final Color GRAY = Color.GRAY;

		final double SIZE = SQUARE_SIZE - 2;
		final double MIDDLE = SIZE / 2;
		final double BODY_RADIUS = (SIZE - SIZE / 4) / 2;
		final double EYE_ANGLE = 20;
		final double BACK_ANGLE = 60;
		final double EYE_RADIUS = SIZE / 15;
		final double WHEEL_SIZE = 0.9 * SIZE;

		Rectangle bg = new Rectangle();
		bg.setWidth(SIZE);
		bg.setHeight(SIZE);
		bg.setFill(Color.TRANSPARENT);

		Arc body = new Arc();
		body.setRadiusX(BODY_RADIUS);
		body.setRadiusY(BODY_RADIUS);
		body.setCenterX(MIDDLE);
		body.setCenterY(MIDDLE);
		body.setStartAngle(-BACK_ANGLE);
		body.setLength(180 + 2 * BACK_ANGLE);
		body.setFill(GRAY);

		Eye leftEye = new Eye();
		leftEye.setRadius(EYE_RADIUS);
		leftEye.setCenterX(MIDDLE - BODY_RADIUS * Math.cos(Math.toRadians(90 - EYE_ANGLE)));
		leftEye.setCenterY(MIDDLE - BODY_RADIUS * Math.sin(Math.toRadians(90 - EYE_ANGLE)));
		leftEye.setFill(this.color);

		Eye rightEye = new Eye();
		rightEye.setRadius(EYE_RADIUS);
		rightEye.setCenterX(MIDDLE + BODY_RADIUS * Math.cos(Math.toRadians(90 - EYE_ANGLE)));
		rightEye.setCenterY(MIDDLE - BODY_RADIUS * Math.sin(Math.toRadians(90 - EYE_ANGLE)));
		rightEye.setFill(this.color);

		Rectangle wheels = new Rectangle();
		wheels.setWidth(WHEEL_SIZE);
		wheels.setHeight(BODY_RADIUS * Math.sin(Math.toRadians(BACK_ANGLE)) * 1.5);
		wheels.setTranslateX((SIZE - WHEEL_SIZE) / 2);
		wheels.setTranslateY(MIDDLE - wheels.getHeight() / 2);
		wheels.setFill(this.color);

		Polygon triangle = new Polygon(MIDDLE + BODY_RADIUS / 2, MIDDLE, MIDDLE - BODY_RADIUS / 2, MIDDLE, MIDDLE,
				MIDDLE - Math.sqrt(3) * (BODY_RADIUS / 2));
		triangle.setTranslateY(Math.sqrt(3) * (BODY_RADIUS / 6));
		triangle.setFill(this.color);

		this.getChildren().addAll(bg, rightEye, leftEye, wheels, body, triangle);

		this.setOnMouseClicked(event -> {

			if (event.getButton() == MouseButton.PRIMARY) {
				this.rotateLeft();
			} else {
				this.moveForward();
			}

		});
	}

	/**
	 * @return the current stack of the Robot
	 */
	public ArrayList<Operation> getStack() {
		return this.stack;
	}

	/**
	 * Move the Robot forward n tiles.
	 * 
	 * @param n
	 *            number of tiles to move the Robot forward.
	 */
	public void moveForward(int n) {
		for (int i = 0; i < n; i++) {
			moveForward();
		}
	}

	/**
	 * Move the Robot forward one tile.
	 */
	public void moveForward() {
		if (isBusy()) {
			addToStack(Operation.MOVE_FORWARD);
			return;
		}

		if (checkCollision(this.getScene().getRoot())) {
			setState(State.FREE);
			return;
		}

		setState(State.BUSY);
		moveForwardAnimation();
	}

	public void rotateRight() {
		if (isBusy()) {
			addToStack(Operation.ROTATE_RIGHT);
			return;
		}
		setState(State.BUSY);
		rotateRightAnimation();
	}

	public void rotateLeft() {
		if (isBusy()) {
			addToStack(Operation.ROTATE_LEFT);
			return;
		}
		setState(State.BUSY);
		rotateLeftAnimation();
	}
	
	private void choseColor() {
		switch (NUMBER_OF_ROBOTS) {
		case 1:
			color = Color.RED;
			break;
		case 2:
			color = Color.BLUE;
			break;
		case 3:
			color = Color.LIMEGREEN;
			break;
		case 4:
			color = Color.PINK;
			break;
		case 5:
			color = Color.TURQUOISE;
			break;
		default:
			color = Color.BLACK;
			break;
		}

	}
	
	private void rotateLeftAnimation() {
		at = new AnimationTimer() {
			long rotate = 0;

			@Override
			public void handle(long now) {
				setRotate(getRotate() + 2);
				rotate += 2;
				if (rotate == 90) {
					setState(State.FREE);
					this.stop();
				}
			}
		};
		at.start();
	}

	private void rotateRightAnimation() {
		at = new AnimationTimer() {
			long rotate = 0;

			@Override
			public void handle(long now) {
				setRotate(getRotate() - 2);
				rotate -= 2;
				if (rotate == -90) {
					setState(State.FREE);
					this.stop();
				}
			}
		};
		at.start();
	}

	private void moveForwardAnimation() {
		at = new AnimationTimer() {
			long move = 0;

			@Override
			public void handle(long now) {
				setTranslateX(Math.cos(Math.toRadians(270 + getRotate())) + getTranslateX());
				setTranslateY(Math.sin(Math.toRadians(270 + getRotate())) + getTranslateY());
				if (moveObject != null) {
					moveObject.setTranslateX(Math.cos(Math.toRadians(270 + getRotate())) + moveObject.getTranslateX());
					moveObject.setTranslateY(Math.sin(Math.toRadians(270 + getRotate())) + moveObject.getTranslateY());
				}
				move++;
				if (move == SQUARE_SIZE) {
					moveObject = null;
					setState(State.FREE);
					this.stop();
				}
			}
		};
		at.start();
	}

	private boolean isBusy() {
		if (this.state == State.BUSY) {
			return true;
		} else {
			return false;
		}
	}


	private void checkStack() {

		if (this.state == State.BUSY) {
			return;
		}

		if (stack.size() != 0) {

			switch (stack.get(0)) {

			case ROTATE_LEFT:
				stack.remove(0);
				rotateLeft();
				break;
			case ROTATE_RIGHT:
				stack.remove(0);
				rotateRight();
				break;
			case MOVE_FORWARD:
				stack.remove(0);
				moveForward();
				break;
			default:
				stack.remove(0);
				break;

			}

		}

	}

	private void setState(State s) {
		this.state = s;
		if (s == State.FREE) {
			checkStack();
		}
	}

	private void addToStack(Operation o) {
		stack.add(o);
	}

	private boolean checkBlockCollision(Block b) {

		double minX = b.getXPos();
		double minY = b.getYPos();

		double height = b.getSize();
		double width = b.getSize();

		boolean collision = collides(minX, minY, width, height);

		if (collision) {
			if (b instanceof Collectible) {
				collect(b);
				return false;
			}
			else if (b instanceof Deadly) {								//ends the game if robot interacts with deadly block
				die;
				return false;
			}
			else if (b instanceof Movable && moveObject == null) {
				moveObject = b;
				System.out.println("MOVEABLE");
				
				this.setTranslateX(
						Math.cos(Math.toRadians(270 + this.getRotate())) * SQUARE_SIZE + this.getTranslateX());
				this.setTranslateY(
						Math.sin(Math.toRadians(270 + this.getRotate())) * SQUARE_SIZE + this.getTranslateY());

				if (checkCollision(this.getScene().getRoot())) {
					moveObject = null;
				} 
				
				this.setTranslateX(
						-Math.cos(Math.toRadians(270 + this.getRotate())) * SQUARE_SIZE + this.getTranslateX());
				this.setTranslateY(
						-Math.sin(Math.toRadians(270 + this.getRotate())) * SQUARE_SIZE + this.getTranslateY());
				if(moveObject == null){
					return true;
				}
				
				return false;
				
				
			}

			return true;

		} 
		else {
			return false;
		}
	}

	private void collect(Block b) {
		b.setVisible(false);
		b = null;
	}

	private void die{
		this.setVisible(false);
		
	}
	
	private boolean checkCollision(Node n) {

		if (n == null || n.equals(this) || n instanceof NotCollidable) {
			return false; // Ignore n
		}

		if (n instanceof Block) {
			if(checkBlockCollision((Block) n)){
				return true;
			}
		}  else if (n instanceof Parent) {
			for (Node childNode : ((Parent) n).getChildrenUnmodifiable()) {
				if (checkCollision(childNode)) {
					return true;
				}
			}
		} else {
			
			double minX = n.getTranslateX();
			double minY = n.getTranslateY();
			Parent parent = n.getParent();
			
			while ((parent != null)) {
				minX += parent.getTranslateX();
				minY += parent.getTranslateY();
				parent = parent.getParent();
			}

			double nodeWidth = n.getBoundsInLocal().getWidth();
			double nodeHeight = n.getBoundsInLocal().getHeight();
			minX += n.getBoundsInLocal().getMinX();
			minY += n.getBoundsInLocal().getMinY();

			if (n instanceof Circle) {
				minX -= ((Circle) n).getRadius();
				minY -= ((Circle) n).getRadius();
			}
			if (nodeHeight == 0 || nodeWidth == 0) {
				return false;
			}
			if (this.collides(minX, minY, nodeWidth, nodeHeight)) {
				// ((Rectangle) n).setFill(this.color);
				return true;
			}
			return false;
		}
		return false;
	}

	private boolean collides(double minX, double minY, double nodeWidth, double nodeHeight) {

		double xPos = this.getTranslateX();
		double yPos = this.getTranslateY();
		Parent parent = this.getParent();
		while (!parent.getStyleClass().toString().equals("root")) {
			xPos += parent.getTranslateX();
			yPos += parent.getTranslateY();
			parent = parent.getParent();
		}

		xPos += Math.cos(Math.toRadians(270 + getRotate())) * SQUARE_SIZE;
		yPos += Math.sin(Math.toRadians(270 + getRotate())) * SQUARE_SIZE;

		double width = this.getBoundsInLocal().getWidth();
		double height = this.getBoundsInLocal().getHeight();

		// System.out.println(xPos + " " + yPos + " " + width + " " + height);
		// System.out.println(minX + " " + minY + " " + nodeWidth + " " +
		// nodeHeight);

		Rectangle r1 = new Rectangle(xPos, yPos, width, height);

		return r1.intersects(minX, minY, nodeWidth, nodeHeight);

	}

}

enum State {
	BUSY, FREE
};

enum Operation {
	ROTATE_LEFT, ROTATE_RIGHT, MOVE_FORWARD
};

class Eye extends Circle {
}