package sorting;

import java.util.ArrayList;

import graphics.ArrayCanvas;
import utils.CanvasUtils;
import utils.Constants;

public class BubbleSort
	{
		public static void sort(ArrayCanvas ac)
			{
				ArrayList<Integer> list = ac.getArray().list;
				for (int i = 0; i < list.size() - 1; i++) {
					ac.getArray().addHighlight(i, Constants.COLOR_POINTER1);
					for (int j = 1; j < list.size() - i; j++) {
						ac.getArray().addHighlight(j, Constants.COLOR_POINTER1);
						ac.compare();
						if (list.get(j - 1) > list.get(j)) {
							CanvasUtils.swap(ac, ac.getArray(), j, j - 1);
						}
						ac.getArray().removeHighlights(j);
					}
					ac.getArray().removeHighlights(i);
				}
				Sorter.isSorted(ac.getArray(), true);
			}

	}
