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
		VisualizingPanel panel;
		FlowPane pane;
		ArrayList<ArrayPanel> arrays;

		public CanvasTimer(VisualizingPanel panel)
			{
				this.panel = panel;
				this.pane = panel.sorterPanel;
				this.arrays = panel.sorters;
			}

		/**
		 * Loop for repainting all of the ArrayPanels
		 */
		@Override
		public void handle(long now)
			{
				double panelWidth = 1;
				if (arrays.size() > 0)
					panelWidth = pane.getWidth() / arrays.size() - (((arrays.size()) * Constants.HGAP) / arrays.size());
				for (ArrayPanel ap : arrays) {
					if (arrays.size() > 0)
						ap.updateSize(panelWidth);
					ap.updateInfo();
					ap.canvas.paintAll(Constants.maxDrawDepth);
				}
			}

	}
