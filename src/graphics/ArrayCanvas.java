package graphics;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import sorting.Sorter;
import utils.Constants.SORTING_TYPE;
import utils.Constants.STATE;

/**
 * Responsible for visualizing one individual instance of sorting (one algorithm and one array)
 * 
 * @author Kevin Humphreys
 *
 */
public class ArrayCanvas extends Canvas
	{
		public static double ARRAY_HEIGHT;
		public static int ARRAY_SIZE;
		PaintableArray array;
		private int comparisons = 0;
		Thread mainThread;
		private VisualizingPanel panel;
		ArrayList<Thread> threads;
		private SORTING_TYPE sortingType;
		private int swaps = 0;

		public ArrayCanvas(VisualizingPanel panel, ArrayList<Integer> list, double x, double y, double width, double height)
			{
				super(width, height);
				// setManaged(true);
				this.panel = panel;
				ARRAY_HEIGHT = height / 3;
				array = new PaintableArray(list, x, y, width, ARRAY_HEIGHT, 1, null);
				ARRAY_SIZE = list.size();
				threads = new ArrayList<Thread>();
			}

		/**
		 * Cleans up the canvas and removes any leftover PaintableArrays
		 */
		public void cleanup()
			{
				reset();
				array.cleanup(getGraphicsContext2D());
			}

		/**
		 * Increments the compare count
		 */
		public void compare()
			{
				comparisons++;
			}

		public PaintableArray getArray()
			{
				return array;
			}

		public int getCompare()
			{
				return comparisons;
			}

		/**
		 * Returns the size of the sorting array
		 */
		public int getSize()
			{
				return ARRAY_SIZE;
			}

		/**
		 * Returns the SORTING_TYPE for the canvas
		 * 
		 * @return
		 */
		public SORTING_TYPE getSortingType()
			{
				return sortingType;
			}

		public int getSwap()
			{
				return swaps;
			}

		/**
		 * Sets the array to a different array
		 * 
		 * @param list
		 */
		public void init(ArrayList<Integer> list)
			{
				array = array.copyDifferentArray(list);
			}

		/**
		 * Returns is the array is in the process of being sorted
		 * 
		 * @return Is the sorting thread still alive
		 */
		public boolean isSorting()
			{
				return mainThread.isAlive();
			}

		/**
		 * Paints the array and all sub-arrays
		 */
		public void paintAll()
			{
				array.paintAll(this);
			}

		/**
		 * Paints the array and all sub-arrays up until the specified depth
		 */
		public void paintAll(int depth)
			{
				array.paintAll(this, depth);
			}

		/**
		 * Resets the compare and swap count
		 */
		private void reset()
			{
				comparisons = 0;
				swaps = 0;
			}

		/**
		 * Sets the SORTING_TYPE for the canvas
		 */
		public void setSortingType(SORTING_TYPE type)
			{
				sortingType = type;
			}

		/**
		 * Sets the size of arrays for any future reinitializations
		 */
		public static void setArraySize(int size)
			{
				ARRAY_SIZE = size;
			}

		/**
		 * Sorts the array in a new thread
		 */
		public void sort()
			{
				if (panel.state == STATE.PAUSED || panel.state == STATE.STOPPED && sortingType != null) {
					Runnable sort = () ->
						{
							Sorter.sort(this);
						};
					mainThread = new Thread(sort);
					mainThread.start();
				}
			}

		/**
		 * Increments the swap count
		 */
		public void swap()
			{
				swaps++;
			}

		/**
		 * Adds a thead to the list of threads
		 */
		public Thread addThread(Thread t)
			{
				threads.add(t);
				return t;
			}

		/**
		 * Stops and removes all threads on this canvas
		 */
		public void cleanThreads()
			{
				for (int i = 0; i < threads.size(); i++) {
					if (threads.get(i) != null) {
						threads.get(i).stop();
					}
				}
				threads.clear();
			}

		/**
		 * Suspends all threads running on this canvas
		 */
		public void suspendThreads()
			{
				for (int i = 0; i < threads.size(); i++) {
					if (threads.get(i) != null)
						threads.get(i).suspend();
				}
			}

		/**
		 * Resumes all threads running on this canvas
		 */
		public void resumeThreads()
			{
				for (int i = 0; i < threads.size(); i++) {
					if (threads.get(i) != null)
						threads.get(i).resume();
				}
			}

		public void updateSize(double width, GraphicsContext gc)
			{
				array.updateSize(width, gc);
			}

	}
