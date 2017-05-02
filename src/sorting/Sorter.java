package sorting;

import java.util.ArrayList;

import graphics.ArrayCanvas;
import utils.Constants;

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
					case SHELL:
						break;
					}
			}
	}