package utils;

import java.util.ArrayList;

import graphics.ArrayCanvas;
import graphics.PaintableArray;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sorting.Sorter;

public class CanvasUtils
	{
		/**
		 * Clears the specified area of a GraphicsContext
		 */
		public static void clearRect(double x, double y, double width, double height, GraphicsContext gc)
			{
				Platform.runLater(() ->
					{

						gc.clearRect(x - 1, y - 1, width + 2, height + 2);

					});
			}

		/**
		 * Compares two indexes in a list and halts the thread to show it
		 */
		public static <E extends Comparable<E>> int compare(ArrayCanvas ac, PaintableArray list, int i, int j)
			{
				CanvasUtils.sleep(Constants.DELAY);
				ac.compare();
				return Sorter.compare(list.list, i, j);
			}

		/**
		 * Draws the given array on the given canvas
		 * 
		 * @param list
		 *            The list to draw
		 * @param lo
		 *            Index to start drawing at
		 * @param hi
		 *            Index to end at
		 * @param max
		 *            The largest value in the list (used for scaling)
		 * @param x
		 *            The top left corner x-value of the rectangle to draw in
		 * @param y
		 *            The top left corner y-value of the rectangle to draw in
		 * @param width
		 *            The width of the rectangle to draw in
		 * @param height
		 *            The height of the rectangle to draw in
		 * @param ac
		 *            The Canvas to draw on
		 */
		public static void drawArray(PaintableArray pa, int lo, int hi, int max, double x, double y, double width, double height, ArrayCanvas ac)
			{
				ArrayList<Integer> list = pa.list;
				GraphicsContext gc = ac.getGraphicsContext2D();
				gc.clearRect(x - 1, y - 1, width + 2, height + 2);
				double heightMultiplier = height / max; // Calculates the scale factor for the height of the bars
				double barWidth = (width - 1) / (hi - lo); // Calculates the width of each bar
				gc.setStroke(Color.BLACK); // Sets the color of the bounding box
				gc.setLineWidth(2); // Sets the width of the stroke for the bounding box
				gc.strokeRect(x, y, width, height); // Draws the bounding box of the array
				gc.setLineWidth(barWidth / 10); // Sets the width to stroke the outlines of the bars
				if (gc.getLineWidth() > 1)
					gc.setLineWidth(1);
				for (int i = lo; i < hi; i++) {
					double barHeight = list.get(i) * heightMultiplier; // Calculates how tall to draw the bar
					gc.setFill(pa.getHighlight(i)); // Sets the color for the fill of the bar
					gc.fillRect(x + (i - lo) * barWidth, y + height - barHeight, barWidth, barHeight); // Draws the bars
					gc.setStroke(Color.GRAY); // Sets the color for the outline of the bar
					gc.strokeRect(x + (i - lo) * barWidth, y + height - barHeight, barWidth, barHeight); // Draws the outline of the bars
				}
			}

		/**
		 * Stops the current thread
		 * 
		 * @param milliseconds
		 *            Number of milliseconds to stop the thread
		 */
		public static void sleep(long milliseconds)
			{
				try {
					Thread.sleep(milliseconds);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		/**
		 * Swaps two indexes in a list, halts the thread, and highlights the indexes being swapped
		 */
		public static <E> void swap(ArrayCanvas ac, PaintableArray pa, int i, int j)
			{
				pa.addSwap(i, j); // Highlights the indexes being swapped
				Shuffler.swap(pa.list, i, j); // Swaps the indexes
				ac.swap();
				CanvasUtils.sleep(Constants.DELAY * 5); // Halts the thread
				pa.removeHighlights(i, j); // Removes the highlights
			}
	}
