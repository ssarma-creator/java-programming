
/** 
 * Animation of a racing car
 */

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This method overrides application start method
 * 
 * @param primaryStage specifies the stage in which the image is displayed
 * @throws Exception occurs while loading images
 */
public class AnimationRacingCar extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		CarPane cPane = new CarPane(650, 200);
		// create an animation
		Timeline timeLine = new Timeline(new KeyFrame(Duration.millis(10), e -> cPane.moveCar(1)));
		timeLine.setCycleCount(cPane.getOneCycleDuration());
		timeLine.play();
		timeLine.setOnFinished(e -> {
			cPane.resetPane();
			timeLine.play();
		});

		/**
		 * Pause, Resume, Increase and Decrease Buttons are created Actions are set for
		 * the buttons
		 */
		Button pause = new Button("Pause");
		Button resume = new Button("Resume");
		Button increase = new Button("Increase");
		Button decrease = new Button("Decrease");
		pause.setOnAction(e -> timeLine.pause());
		resume.setOnAction(e -> timeLine.play());
		increase.setOnAction(e -> {
			timeLine.setRate(timeLine.getCurrentRate() + 1);
		});
		decrease.setOnAction(e -> {
			if (timeLine.getCurrentRate() <= 0)
				return;
			timeLine.setRate(timeLine.getCurrentRate() - 1);
		});
		/**
		 * Location of the buttons
		 */
		HBox hButtons = new HBox(pause, resume, increase, decrease);
		hButtons.setSpacing(10);
		hButtons.setAlignment(Pos.CENTER);
		hButtons.setPadding(new Insets(5, 5, 5, 5));

		BorderPane bPane = new BorderPane(cPane, null, null, hButtons, null);
		bPane.setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case UP:
				increase.fire();
				break;
			case DOWN:
				decrease.fire();
				break;
			}
		});

		primaryStage.setScene(new Scene(bPane));
		primaryStage.setTitle("Animation");
		primaryStage.show();

	}

	/**
	 * This is the main function that contains application launch function
	 * 
	 * @param args command line argument
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

	/**
	 * Method used to create car pane
	 * 
	 * @param w width of the car pane
	 * @param h height of the car pane
	 */

	private class CarPane extends Pane {

		private double w;
		private double h;

		/**
		 * Car's drawing start point x, y and radius of left Tire
		 */
		private double leftTireX;
		private double leftTireY;
		private double tireRadius;

		/**
		 *
		 * Index 0 = left tire Index 1 = right tire
		 */
		Circle[] tires = new Circle[2];
		Polyline cover = new Polyline();
		ObservableList<Double> points;
		Rectangle base;

		/**
		 * 
		 * @param width
		 * @param height
		 */
		private CarPane(double width, double height) {

			/**
			 *
			 * Car's drawing start point Get width, height and measurements
			 */
			w = width;
			h = height;
			leftTireX = w * 0.2;
			leftTireY = h * 0.9;
			tireRadius = h * 0.1;

			/**
			 * set MIN Width, Height
			 */
			setMinWidth(w);
			setMinHeight(h);

			/**
			 * set MAX Width, Height
			 */
			setMaxWidth(w);
			setMaxHeight(h);

			resetPane();
		}

		public void resetPane() {
			if (points != null)
				points.clear();
			getChildren().clear();
			drawCar();
			moveCar(tireRadius * 13 * -1);
		}

		public void drawCar() {
			for (int i = 0; i < tires.length; i++) {
				tires[i] = new Circle(leftTireX + (i * 4 * tireRadius), leftTireY, tireRadius);
				tires[i].setStroke(Color.BLACK);
				tires[i].setFill(Color.BLACK);
			}

			/**
			 * To draw the car's base
			 */
			double baseX = tires[0].getCenterX() - tires[0].getRadius() * 3;
			double baseY = tires[0].getCenterY() - tires[0].getRadius() * 3;
			base = new Rectangle(baseX, baseY, tireRadius * 10, tireRadius * 2);
			base.setFill(Color.GRAY);
			base.setStroke(Color.BLACK);

			/**
			 * To draw the car's top
			 */
			double startX = base.getX() + tireRadius * 2;
			double startY = base.getY();
			double currentX = startX;
			double currentY = startY;

			points = cover.getPoints();

			// start point
			double distance = tireRadius * 2;
			points.addAll(currentX, currentY);

			// top right
			currentX += distance;
			currentY -= distance;
			points.addAll(currentX, currentY);

			// right
			currentX += distance;
			points.addAll(currentX, currentY);

			// bottom right
			currentX += distance;
			currentY += distance;
			points.addAll(currentX, currentY);

			// to connect starting point
			points.addAll(startX, startY);
			cover.setFill(Color.GRAY);

			getChildren().addAll(tires);
			getChildren().add(base);
			getChildren().add(cover);

		}

		private void moveCar(double distance) {

			/**
			 * tires
			 */
			for (Circle c : tires) {
				c.setCenterX(c.getCenterX() + distance);
			}

			base.setX(base.getX() + distance);

			for (int i = 0; i < points.size(); i += 2) {
				points.set(i, points.get(i) + distance);
			}

		}

		public int getOneCycleDuration() {
			return (int) w;
		}

	}
}