package sorting;

import java.util.ArrayList;

import graphics.ArrayCanvas;
import utils.CanvasUtils;

public class HeapSort
	{
		public static void heapify(ArrayCanvas ac, int count)
			{
				int start = count / 2 - 1;
				while (start >= 0) {
					siftDown(ac, start, count - 1);
					start -= 1;
				}
			}

		public static void siftDown(ArrayCanvas ac, int start, int end)
			{
				ArrayList<Integer> list = ac.getArray().list;
				int root = start;
				while (root * 2 + 1 <= end) {
					int child = root * 2 + 1;
					int swap = root;
					ac.compare();
					if (list.get(swap) < list.get(child)) {
						swap = child;
					}
					ac.compare();
					if (child + 1 <= end && list.get(swap) < list.get(child + 1)) {
						swap = child + 1;
					}
					ac.compare();
					if (swap != root) {
						CanvasUtils.swap(ac, ac.getArray(), root, swap);
						root = swap;
					} else {
						return;
					}
				}
			}

		public static ArrayList<Integer> sort(ArrayCanvas ac)
			{
				ArrayList<Integer> list = ac.getArray().list;
				int count = list.size();
				heapify(ac, count);
				int end = count - 1;
				while (end > 0) {
					CanvasUtils.swap(ac, ac.getArray(), end, 0);
					end = end - 1;
					siftDown(ac, 0, end);
				}
				return list;
				Sorter.isSorted(ac.getArray(), true);
			}

	}
