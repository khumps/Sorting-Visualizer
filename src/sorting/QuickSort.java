package sorting;

import java.util.ArrayList;

import graphics.ArrayCanvas;
import graphics.PaintableArray;
import utils.CanvasUtils;

public class QuickSort
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

		private static void quickSort(ArrayCanvas ac, PaintableArray pa, int low, int high)
			{
				int pivot;
				if (low >= high)
					return;

				pivot = partition(ac, pa, low, high);
				PaintableArray left = pa.subArrayLinked(low, pivot - 1);
				quickSort(ac, left, low, pivot - 1);
				left.remove(ac.getGraphicsContext2D());
				PaintableArray right = pa.subArrayLinked(pivot + 1, high);
				quickSort(ac, right, pivot + 1, high);
				right.remove(ac.getGraphicsContext2D());
			}

		// Array Canvas needs to be done
		public static void sort(ArrayCanvas ac)
			{

				quickSort(ac, ac.getArray(), 0, ac.getArray().list.size() - 1);
			}

	}
