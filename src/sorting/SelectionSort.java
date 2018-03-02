package sorting;

import java.util.ArrayList;

import graphics.ArrayCanvas;
import utils.CanvasUtils;
import utils.Constants;

public class SelectionSort
	{
		public static void sort(ArrayCanvas ac)
			{
				ArrayList<Integer> list = ac.getArray().list;
				for (int i = 0; i < list.size(); i++) {
					ac.getArray().addHighlight(i, Constants.COLOR_POINTER1);
					int min = i;
					for (int j = i + 1; j < list.size(); j++) {
						ac.getArray().addHighlight(j, Constants.COLOR_POINTER1);
						if (CanvasUtils.compare(ac, ac.getArray(), min, j) > 0) {
							if (min != i) {
								ac.getArray().removeHighlights(min);
							}
							min = j;
							ac.getArray().addHighlight(min, Constants.COLOR_POINTER2);
						} else
							ac.getArray().removeHighlights(j);
					}
					CanvasUtils.swap(ac, ac.getArray(), i, min);
					ac.getArray().removeHighlights(i);
					ac.getArray().removeHighlights(min);
					CanvasUtils.sleep(10);

				}
				Sorter.isSorted(ac.getArray(), true);
			}
	}
