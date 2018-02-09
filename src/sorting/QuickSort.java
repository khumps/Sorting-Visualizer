package sorting;

import java.util.ArrayList;

import com.sun.prism.paint.Paint;

import graphics.ArrayCanvas;
import graphics.PaintableArray;
import utils.CanvasUtils;
import utils.Constants;
import utils.Shuffler;

public class QuickSort
	{
		private static int partition(ArrayCanvas ac, PaintableArray pa, int p, int q)
			{
				ArrayList<Integer> list = pa.list;
				int pivotVal = list.get(p);
				int i = p;
				for (int j = p + 1; j < q; j++) {
					CanvasUtils.compare(ac, pa, j, p);
					if (list.get(j) <= pivotVal) {
						CanvasUtils.swap(ac, pa, ++i, j);
					}
				}
				CanvasUtils.swap(ac, pa, p, i);
				return i;
			}

		public static void randQuickSort(ArrayCanvas ac, PaintableArray pa, int p, int r)
			{
				if (p < r) {
					int randomPivotIndex = (int) (Math.random() * (r - p)) + p;
					CanvasUtils.swap(ac, pa, p, randomPivotIndex);
					int pivot = partition(ac, pa, p, r);
					pa.addHighlight(pivot, Constants.COLOR_POINTER1);
					PaintableArray left = pa.subArrayLinked(p, pivot);
					randQuickSort(ac, left, p, pivot);
					System.out.println(p + " " + pivot + " " + r);
					if (pivot + 1 < r - 1) {
						PaintableArray right = pa.subArrayLinked(pivot + 1, r - 1);
						randQuickSort(ac, right, pivot + 1, r);
						right.remove(ac.getGraphicsContext2D());
					}
					left.remove(ac.getGraphicsContext2D());
					pa.removeHighlights(pivot);
				}
			}

		public static void sort(ArrayCanvas ac)
			{
				randQuickSort(ac, ac.getArray(), 0, ac.getArray().list.size());
				//quickSort(ac, ac.getArray(), 0, ac.getArray().list.size() - 1);
			}

	}
