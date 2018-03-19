package sorting;

import java.util.ArrayList;

import graphics.ArrayCanvas;
import graphics.PaintableArray;
import utils.CanvasUtils;

public class ParallelQuickSort
	{
		private static int partition(ArrayCanvas ac, PaintableArray pa, int low, int high)
			{
				ArrayList<Integer> arr = pa.list;
				int pivotVal = arr.get(low);
				int right = high;
				int left = low + 1;

				while (left <= right) {

					while (left <= high && arr.get(left) <= pivotVal)
						left++;

					while (arr.get(right) > pivotVal) {
						CanvasUtils.compare(ac, pa, low, right);
						right--;
					}
					if (left < right) {
						CanvasUtils.swap(ac, pa, left, right);
						left++;
						right--;
					}
				}
				CanvasUtils.swap(ac, pa, low, right);
				return right;
			}

		private static void sort(ArrayCanvas ac, PaintableArray pa, int low, int high)
			{
				int pivot;
				if (low >= high)
					return;

				pivot = partition(ac, pa, low, high);
				Thread t1 = ac.addThread(new Thread(() ->
					{
						PaintableArray left = pa.subArrayLinked(low, pivot - 1);
						sort(ac, left, low, pivot - 1);
						left.remove(ac.getGraphicsContext2D());
					}));
				t1.start();
				Thread t2 = ac.addThread(new Thread(() ->
					{
						PaintableArray right = pa.subArrayLinked(pivot + 1, high);
						sort(ac, right, pivot + 1, high);
						right.remove(ac.getGraphicsContext2D());
					}));
				t2.start();
				try {
					t1.join();
					t2.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		// Array Canvas needs to be done
		public static void sort(ArrayCanvas ac)
			{
				sort(ac, ac.getArray(), 0, ac.getArray().list.size() - 1);
				Sorter.isSorted(ac.getArray(), true);
			}
	}
