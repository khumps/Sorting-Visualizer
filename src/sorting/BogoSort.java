package sorting;

import java.util.ArrayList;
import graphics.ArrayCanvas;
import utils.CanvasUtils;

public class BogoSort
	{
		public static <E extends Comparable<E>> void sort(ArrayCanvas ac)
			{
				ArrayList<Integer> array = ac.getArray().list;
				while (!Sorter.isSorted(ac.getArray())) {
					CanvasUtils.swap(ac, ac.getArray(), (int) (Math.random() * array.size()), (int) (Math.random() * array.size()));
				}
			}
	}
