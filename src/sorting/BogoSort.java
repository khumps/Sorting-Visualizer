package sorting;

import java.util.ArrayList;

import graphics.ArrayCanvas;
import utils.CanvasUtils;

public class BogoSort
	{
		public static <E extends Comparable<E>> void sort(ArrayCanvas ac)
			{
				ArrayList<Integer> array = ac.getArray().list;
				while (!isSorted(array)) {
					CanvasUtils.swap(ac, ac.getArray(), (int) (Math.random() * array.size()), (int) (Math.random() * array.size()));
				}
			}

		private static boolean isSorted(ArrayList<Integer> list)
			{
				if (list.isEmpty()) {
					return true;
				}
				int last = list.get(0);
				for (int i = 1; i < list.size(); i++) {
					if (list.get(i) < last) {
						return false;
					}
					last = list.get(i);
				}
				return true;
			}
	}
