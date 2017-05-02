package graphics;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import utils.Constants;
import utils.Shuffler;

/**
 * Launch point of the application. Controls everything else
 * 
 * @author Kevin Humphreys
 *
 */
public class VisualizingPanel extends Application
	{
		public static void main(String[] args)
			{
				launch(args);
			}

		private Button addSorter;
		protected ArrayList<ArrayPanel> arrays;
		private ArrayList<Integer> baseArray;
		private FlowPane canvasPanel;
		// private ComboBox<>
		private Slider delay;
		private HBox gui;
		protected boolean isRunning;
		protected boolean isSorting;
		public final ImageView PAUSE_IMAGE = new ImageView(new Image(getClass().getResourceAsStream("../pause.png")));
		private Button shuffle;
		private ComboBox<Integer> sizes;

		private Button sort;
		public final ImageView SORT_IMAGE = new ImageView(new Image(getClass().getResourceAsStream("../sort.png")));
		public final ImageView stopImage = new ImageView(new Image(getClass().getResourceAsStream("../stop.png")));

		protected CanvasTimer timer;

		/**
		 * Add another array visualization
		 */
		public void addArray()
			{
				if (arrays.size() < Constants.MAX_SORTERS && !isSorting()) {
					ArrayPanel ap = new ArrayPanel(this, baseArray, 500, 500);
					canvasPanel.getChildren().add(ap);
					arrays.add(ap);
					double panelWidth = arrays.size() < Constants.MAX_SORTERS / 2 ? canvasPanel.getWidth() / arrays.size() : canvasPanel.getWidth() / (arrays.size() / 2);
					double panelHeight = arrays.size() < Constants.MAX_SORTERS / 2 ? canvasPanel.getHeight() : canvasPanel.getHeight() / 2;
					for (ArrayPanel a : arrays) {
						a.updateSize(panelWidth, panelHeight);
					}
				}

			}

		/**
		 * Called after all panels have finished sorting
		 */
		public void finishSort()
			{
				isSorting = false;
				isRunning = false;
				Platform.runLater(new Runnable()
					{
						@Override
						public void run()
							{
								sort.setText("Sort");
								sort.setGraphic(SORT_IMAGE);
							}

					});
			}

		/**
		 * Initialize elements of the UI
		 */
		public void initUI()
			{
				// Inits sort/start/stop Button
				sort = new Button("Sort", SORT_IMAGE);
				sort.setPrefSize(100, 20);
				sort.setOnAction(e ->
					{
						if (!isSorting()) {
							sort.setText("Pause");
							sort();
							sort.setGraphic(PAUSE_IMAGE);
						}

						else {
							if (isRunning) {
								pause();
								sort.setText("Resume");
								sort.setGraphic(SORT_IMAGE);
							} else {
								resume();
								sort.setText("Pause");
								sort.setGraphic(PAUSE_IMAGE);
							}
						}

					});
				gui.getChildren().add(sort);

				// Init shuffle Button
				Image shuffleImage = new Image(getClass().getResourceAsStream("../shuffle.png"));
				shuffle = new Button("Shuffle", new ImageView(shuffleImage));
				shuffle.setPrefSize(100, 20);
				shuffle.setOnAction(e ->
					{
						if (random()) {
							sort.setGraphic(SORT_IMAGE);
							sort.setText("Sort");
						}
					});
				gui.getChildren().add(shuffle);

				// Init size ComboBox
				sizes = new ComboBox<>();
				sizes.getItems().add(8);
				sizes.getItems().add(16);
				sizes.getItems().add(32);
				sizes.getItems().add(64);
				sizes.getItems().add(128);
				sizes.getItems().add(256);
				sizes.getItems().add(512);
				sizes.getItems().add(1024);
				sizes.getItems().add(2048);
				sizes.getSelectionModel().selectFirst();

				sizes.setOnAction(e ->
					{
						ArrayCanvas.setSize(sizes.getSelectionModel().getSelectedItem());
					});
				gui.getChildren().add(sizes);

				// Init shuffler ComboBox

				// Init delay Slider
				delay = new Slider(1, 50, 10);
				delay.setPrefSize(300, 20);
				delay.setTooltip(new Tooltip("Delay"));
				delay.setShowTickLabels(true);
				delay.setShowTickMarks(true);
				delay.valueProperty().addListener(e ->
					{
						Constants.DELAY = (long) delay.getValue();
					});
				gui.getChildren().add(delay);

				// Init add sorter button
				addSorter = new Button("Add Sorter");
				addSorter.setPrefSize(100, 20);
				addSorter.setOnAction(e ->
					{
						addArray();
					});
				gui.getChildren().add(addSorter);
			}

		public boolean isSorting()
			{
				for (ArrayPanel ap : arrays) {
					if (ap.isSorting()) {
						return true;
					}
				}
				return false;
			}

		/**
		 * Pauses sorting
		 */
		public void pause()
			{
				if (isRunning && isSorting) {
					isRunning = false;
					for (ArrayPanel ap : arrays) {
						ap.suspend();
						ap.canvas.suspendThreads();
					}
				}
			}

		/**
		 * Run after gui has been init
		 */
		private void postInit()
			{
				ArrayCanvas.setSize(sizes.getSelectionModel().getSelectedItem());
				baseArray = Shuffler.generateArray(null, ArrayCanvas.ARRAY_SIZE);
				addArray();
			}

		/**
		 * 
		 * @return
		 */
		public boolean random()
			{

				if (!isRunning) {
					if (isSorting)
						stop();
					baseArray = Shuffler.random(Shuffler.inOrder(sizes.getSelectionModel().getSelectedItem(), true));
					for (ArrayPanel ap : arrays) {
						ap.init(baseArray);
					}
					return true;
				}
				return false;
			}

		public void removePanel(ArrayPanel ap)
			{
				canvasPanel.getChildren().remove(ap);
				arrays.remove(ap);
			}

		/**
		 * Resumes sorting if it has been paused
		 */
		public void resume()
			{
				if (!isRunning && isSorting) {
					isRunning = true;
					for (ArrayPanel ap : arrays) {
						ap.resume();
						ap.canvas.resumeThreads();
					}
				}
			}

		/**
		 * Sorts all panels
		 */
		public synchronized void sort()
			{
				for (ArrayPanel ap : arrays) {
					ap.sort();
				}
				isSorting = true;
				isRunning = true;

				Thread t = new Thread(() ->
					{
						for (ArrayPanel ap : arrays) {
							try {
								ap.canvas.mainThread.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						finishSort();
					});
				t.start();

			}

		@Override
		public void start(Stage primaryStage)
			{
				primaryStage.setTitle("Sorting Visualizer");
				BorderPane bp = new BorderPane();
				gui = new HBox();
				canvasPanel = new FlowPane();
				canvasPanel.setHgap(20);
				canvasPanel.setVgap(10);
				bp.prefWidthProperty().bind(primaryStage.widthProperty());
				bp.prefHeightProperty().bind(primaryStage.heightProperty());
				canvasPanel.prefWidthProperty().bind(bp.widthProperty());
				canvasPanel.prefHeightProperty().bind(bp.heightProperty());
				arrays = new ArrayList<ArrayPanel>();
				timer = new CanvasTimer(canvasPanel, arrays);
				initUI();
				postInit();
				bp.setTop(gui);
				bp.setCenter(canvasPanel);
				primaryStage.setMaximized(true);
				primaryStage.setResizable(false);
				primaryStage.setScene(new Scene(bp));
				timer.start();
				primaryStage.show();
			}

		/**
		 * Stops all running sorters
		 */
		@Override
		public void stop()
			{
				if (isSorting) {
					for (ArrayPanel ap : arrays) {
						ap.stop();
						ap.canvas.cleanThreads();
					}
					isSorting = false;
					isRunning = false;
				}
			}
	}