package sorting;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import utils.CanvasUtils;
import graphics.ArrayCanvas;
import graphics.PaintableArray;

public class Sorter
	{
		/**
		 * Sorts the given list with the given algorithm and visualizes it using the give GraphicsContext
		 * 
		 * @param list
		 * @param type
		 * @param gc
		 */
		public static <E extends Comparable<E>> void sort(ArrayCanvas ac)
			{
				switch (ac.getSortingType())
					{
					case BITONIC:
						BitonicSort.sort(ac);
						break;
					case BUBBLE:
						BubbleSort.sort(ac);
						break;
					case HEAP:
						HeapSort.sort(ac);
						break;
					case INSERTION:
						InsertionSort.sort(ac);
						break;
					case MERGE:
						MergeSort.sort(ac);
						break;
					case PBITONIC:
						ParallelBitonicSort.sort(ac);
						break;
					case PMERGE:
						ParallelMergeSort.sort(ac);
						break;
					case PQUICK:
						ParallelQuickSort.sort(ac);
						break;
					case QUICK:
						QuickSort.sort(ac);
						break;
					case SELECTION:
						SelectionSort.sort(ac);
						break;
					/* case SHELL:
						break; */
					case RADIX:
						RadixSort.sort(ac);
						break;
					case BOGO:
						BogoSort.sort(ac);
						break;
					}
			}

		/**
		 * Compares two elements by index in a list
		 */
		public static <E extends Comparable<E>> int compare(ArrayList<E> list, int i, int j)
			{
				return list.get(i).compareTo(list.get(j));
			}

		/**
		 * Checks a PaintableArray and sees if it is sorted, highlights the array as it checks
		 * @param delay add animation delay for the user to see the highlights
		 */
		public static boolean isSorted(PaintableArray pa, boolean delay)
			{
				ArrayList<Integer> list = pa.list;
				if (list.isEmpty()) {
					return true;
				}
				int last = list.get(0);
				pa.addHighlight(0, Color.GREEN);
				for (int i = 1; i < list.size(); i++) {
					if (list.get(i) < last) {
						pa.addHighlight(i, Color.RED);
						if (delay) {
							CanvasUtils.sleep(1000);
						}
						pa.clearHighlights();
						return false;
					}
					last = list.get(i);
					pa.addHighlight(i, Color.GREEN);
					if (delay) {
						CanvasUtils.sleep(500 / pa.list.size());
					}
				}
				if (delay) {
					CanvasUtils.sleep(1000);
				}
				pa.clearHighlights();
				return true;
			}
	}