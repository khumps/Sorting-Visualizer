package graphics;

import java.util.ArrayList;

import graphics.VisualizingPanel.STATE;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import utils.Constants;
import utils.Constants.SORTING_TYPE;

/**
 * Contains and ArrayCanvas for drawing the array as well as HUD elements for selecting the sorting algorithm as well as showing number of comparisons and swaps
 * 
 * @author humphreysk
 *
 */
public class ArrayPanel extends Pane
	{
		ArrayCanvas canvas;
		private Label comparisons;
		HBox gui;
		VisualizingPanel panel;
		private Label swaps;

		public ArrayPanel(VisualizingPanel panel, ArrayList<Integer> list, double width, double height)
			{
				this.panel = panel;
				canvas = new ArrayCanvas(panel, new ArrayList<Integer>(list), 0, 30, width, height);
				gui = new HBox();
				initGui();
				getChildren().add(canvas);
				getChildren().add(gui);

			}

		/**
		 * Initializes/reinitializes the enclosed canvas for use(new algorithm, new array, etc)
		 */
		public void init(ArrayList<Integer> list)
			{
				canvas.cleanup();
				canvas.init(new ArrayList<Integer>(list));
			}

		/**
		 * Initializes elements of the gui for the panel
		 */
		public void initGui()
			{
				// Init sorting types ComboBox
				ComboBox<Constants.SORTING_TYPE> types = new ComboBox<>();
				types.getItems().addAll(SORTING_TYPE.values());
				types.getSelectionModel().selectFirst();
				canvas.setSortingType(types.getSelectionModel().getSelectedItem());
				types.setOnAction(e ->
					{
						canvas.setSortingType(types.getSelectionModel().getSelectedItem());
					});
				gui.getChildren().add(types);

				// Init comparisons
				comparisons = new Label();
				comparisons.setText(" Comparisons: " + canvas.getCompare() + " ");
				gui.getChildren().add(comparisons);

				// Init swaps
				swaps = new Label();
				swaps.setText(" Swaps: " + canvas.getSwap() + " ");
				gui.getChildren().add(swaps);

				// Init remove Button
				ImageView removeImage = new ImageView(new Image(getClass().getResourceAsStream("../remove.png")));
				Button remove = new Button("", removeImage);
				remove.setTooltip(new Tooltip("Remove Panel"));
				remove.setOnAction(e ->
					{
						remove();
					});
				gui.getChildren().add(remove);
			}

		/**
		 * Checks if the panel is sorting
		 */
		public boolean isSorting()
			{
				if (canvas.mainThread != null) {
					return canvas.mainThread.isAlive();
				}
				return false;
			}

		/**
		 * Discards the panel from from the containing VisualizingPanel
		 */
		public void remove()
			{
				if (panel.state == STATE.PAUSED || panel.state == STATE.STOPPED)
					panel.removeSorter(this);
			}

		/**
		 * Resumes all threads on the ArrayCanvas. Used for resuming the sorting and the animation
		 */
		public void resume()
			{
				if (canvas.mainThread != null) {
					canvas.mainThread.resume();
				}
			}

		/**
		 * Initiates the sorting
		 */
		public void sort()
			{
				canvas.sort();
			}

		/**
		 * Terminates all threads of the ArrayCanvas
		 */
		public void stop()
			{
				if (canvas.mainThread != null) {
					canvas.mainThread.stop();
				}
			}

		/**
		 * Temporarily suspends all threads on the ArrayCanvas. Used for pausing the sorting and the animation
		 */
		public void suspend()
			{
				if (canvas.mainThread != null) {
					canvas.mainThread.suspend();
				}

			}

		/**
		 * Updates the comparison and swap labels
		 */
		public void updateInfo()
			{
				comparisons.setText(" Comparisons: " + canvas.getCompare() + " ");
				swaps.setText(" Swaps: " + canvas.getSwap() + " ");
			}

		public void updateSize(double width)
			{
				if (canvas != null) {
					if (canvas.getWidth() != width) {
						setMaxWidth(width);
						canvas.setWidth(width);
						canvas.updateSize(width, canvas.getGraphicsContext2D());
					}
				}
			}
	}