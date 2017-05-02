package sorting;

import java.util.ArrayList;

import graphics.ArrayCanvas;
import utils.CanvasUtils;
import utils.Constants;
import utils.Shuffler;

public class SelectionSort
	{
		public static void main(String[] args)
			{
				ArrayList<Integer> list = Shuffler.random(Shuffler.inOrder(10, true));
				System.out.println(list);
				sort(list);
				System.out.println(list);
			}

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
								ac.getArray().removeHighlight(min);
							}
							min = j;
							ac.getArray().addHighlight(min, Constants.COLOR_POINTER2);
						} else
							ac.getArray().removeHighlight(j);
					}
					CanvasUtils.swap(ac, ac.getArray(), i, min);
					ac.getArray().removeHighlight(i);
					ac.getArray().removeHighlight(min);
					CanvasUtils.sleep(10);
				}
			}

		public static void sort(ArrayList<Integer> list)
			{
				for (int i = 0; i < list.size(); i++) {
					int min = i;
					for (int j = i + 1; j < list.size(); j++) {
						if (list.get(j).compareTo(list.get(min)) < 0)
							min = j;
					}
					Shuffler.swap(list, i, min);
				}
			}
	}
