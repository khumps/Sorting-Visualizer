package graphics;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import utils.Constants;

/**
 * Responsible for acting as a clock to keep running repetitive repaint tasks
 * 
 * @author Kevin Humphreys
 *
 */
public class CanvasTimer extends AnimationTimer
	{
		FlowPane pane;
		ArrayList<ArrayPanel> arrays;

		public CanvasTimer(FlowPane pane, ArrayList<ArrayPanel> canvas)
			{
				this.pane = pane;
				this.arrays = canvas;
			}

		/**
		 * Loop for repainting all of the ArrayPanels
		 */
		@Override
		public void handle(long now)
			{
				double panelWidth = arrays.size() < Constants.MAX_SORTERS / 2 ? pane.getWidth() / arrays.size() : pane.getWidth() / (arrays.size() / 2);
				double panelHeight = arrays.size() < Constants.MAX_SORTERS / 2 ? pane.getHeight() : pane.getHeight() / 2;
				for (ArrayPanel ap : arrays) {
					ap.updateSize(panelWidth, panelHeight);
					ap.updateInfo();
					ap.canvas.paintAll(Constants.maxDrawDepth);
				}
			}

	}
