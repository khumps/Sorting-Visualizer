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
import utils.Constants.STATE;
import utils.Constants.SHUFFLER_TYPE;
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

		Stage primaryStage;
		private Button addSorter;
		ArrayList<ArrayPanel> sorters;
		private ArrayList<Integer> baseArray;
		FlowPane sorterPanel;
		private Slider delay;
		private HBox gui;
		STATE state;
		final ImageView PAUSE_IMAGE = new ImageView(new Image(getClass().getResourceAsStream("../pause.png")));
		final ImageView SORT_IMAGE = new ImageView(new Image(getClass().getResourceAsStream("../sort.png")));
		final ImageView STOP_IMAGE = new ImageView(new Image(getClass().getResourceAsStream("../stop.png")));
		private Button shuffle;
		private ComboBox<Integer> sizes;
		private ComboBox<Constants.SHUFFLER_TYPE> shuffleType;
		private Button sort;

		protected CanvasTimer timer;

		/**
		 * Called after all panels have finished sorting
		 */
		public void finishSort()
			{
				setState(STATE.STOPPED);
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
							if (state == STATE.RUNNING) {
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
						if (shuffleArray()) {
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
						ArrayCanvas.setArraySize(sizes.getSelectionModel().getSelectedItem());
					});
				gui.getChildren().add(sizes);

				// Init shuffler ComboBox
				shuffleType = new ComboBox<>();
				shuffleType.getItems().addAll(SHUFFLER_TYPE.values());
				shuffleType.getSelectionModel().select(SHUFFLER_TYPE.RANDOM_ALL_UNIQUES);
				gui.getChildren().add(shuffleType);
				// Init delay Slider
				delay = new Slider(Constants.MIN_DELAY, Constants.MAX_DELAY, (Constants.MAX_DELAY + Constants.MIN_DELAY) / 2);
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
						addSorter();
					});
				gui.getChildren().add(addSorter);
			}

		public boolean isSorting()
			{
				for (ArrayPanel ap : sorters) {
					if (ap.isSorting()) {
						return true;
					}
				}
				return false;
			}

		/**
		 * Pauses sorting
		 */
		private void pause()
			{
				setState(STATE.PAUSED);
				for (ArrayPanel ap : sorters) {
					ap.suspend();
					ap.canvas.suspendThreads();
				}
			}

		/**
		 * Run after gui has been init
		 */
		private void postInit()
			{
				setState(STATE.STOPPED);
				ArrayCanvas.setArraySize(sizes.getSelectionModel().getSelectedItem());
				baseArray = Shuffler.generateArray(null, ArrayCanvas.ARRAY_SIZE);
				// addSorter();
			}

		/**
		 * Randomizes all arrays if the sorter isnt running
		 * 
		 * @return true if the sorter was stopped or paused, false otherwise
		 */
		public boolean shuffleArray()
			{
				if (state == STATE.PAUSED || state == STATE.STOPPED) {
					if (state == STATE.PAUSED)
						stop();
					baseArray = Shuffler.generateArray(shuffleType.getSelectionModel().getSelectedItem(), sizes.getSelectionModel().getSelectedItem());
					for (ArrayPanel ap : sorters) {
						ap.init(baseArray);
					}
					return true;
				}
				return false;
			}

		/**
		 * Add another array visualization
		 */
		public void addSorter()
			{
				if (sorters.size() < Constants.MAX_SORTERS && !isSorting()) {
					ArrayPanel ap = new ArrayPanel(this, baseArray, 500, 1000);
					sorterPanel.getChildren().add(ap);
					sorters.add(ap);
					double panelWidth = sorterPanel.getWidth() / sorters.size();
					for (ArrayPanel a : sorters) {
						a.updateSize(panelWidth);
					}
				}
				if (sorters.size() < 2)
					primaryStage.setMinWidth(Constants.MIN_SORTER_WIDTH * 2);
				else
					primaryStage.setMinWidth(sorters.size() * Constants.MIN_SORTER_WIDTH);
			}

		public void removeSorter(ArrayPanel ap)
			{
				ap.stop();
				sorters.remove(ap);
				sorterPanel.getChildren().remove(ap);
				if (sorters.size() < 2)
					primaryStage.setMinWidth(Constants.MIN_SORTER_WIDTH * 2);
				else
					primaryStage.setMinWidth(sorters.size() * Constants.MIN_SORTER_WIDTH);
			}

		@Override
		public void start(Stage primaryStage)
			{
				this.primaryStage = primaryStage;
				primaryStage.setTitle("Sorting Visualizer");
				primaryStage.setMinWidth(Constants.MIN_SORTER_WIDTH * 2);
				primaryStage.setMinHeight(500);
				BorderPane bp = new BorderPane();
				gui = new HBox();
				sorterPanel = new FlowPane();
				sorterPanel.setManaged(true);
				sorterPanel.setHgap(Constants.HGAP);
				sorters = new ArrayList<ArrayPanel>();
				timer = new CanvasTimer(this);
				initUI();
				postInit();
				bp.setTop(gui);
				bp.setCenter(sorterPanel);
				primaryStage.setResizable(true);
				primaryStage.setScene(new Scene(bp));
				timer.start();
				primaryStage.show();
			}

		/**
		 * Sorts all panels
		 */
		public synchronized void sort()
			{
				if (state == STATE.PAUSED || state == STATE.STOPPED) {
					for (ArrayPanel ap : sorters) {
						ap.sort();
					}
					setState(STATE.RUNNING);
					Thread t = new Thread(() ->
						{
							for (ArrayPanel ap : sorters) {
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
			}

		/**
		 * Resumes sorting if it has been paused
		 */
		public void resume()
			{
				if (state == STATE.PAUSED) {
					setState(STATE.RUNNING);
					for (ArrayPanel ap : sorters) {
						ap.resume();
						ap.canvas.resumeThreads();
					}
				}
			}

		/**
		 * Stops all running sorters
		 */
		@Override
		public void stop()
			{
				if (state == STATE.PAUSED || state == STATE.RUNNING) {
					for (ArrayPanel ap : sorters) {
						ap.stop();
						ap.canvas.cleanThreads();
					}
					setState(STATE.STOPPED);
				}
			}

		protected void setState(STATE state)
			{
				this.state = state;
			}
	}