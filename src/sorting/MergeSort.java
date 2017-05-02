package sorting;

import java.util.ArrayList;

import graphics.ArrayCanvas;
import graphics.PaintableArray;
import utils.Constants;

public class MergeSort
	{
		public static void merge(ArrayCanvas ac, PaintableArray left, PaintableArray right)
			{
				PaintableArray merge = left.parent;
				int indexL = 0;
				int indexR = 0;
				int indexRes = 0;

				ArrayList<Integer> leftA = left.list;
				ArrayList<Integer> rightA = right.list;
				ArrayList<Integer> mergeA = merge.list;
				left.addHighlight(0, Constants.COLOR_POINTER2);
				while (indexL < leftA.size() || indexR < rightA.size()) {
					left.addHighlight(indexL, Constants.COLOR_POINTER1);
					right.addHighlight(indexR, Constants.COLOR_POINTER1);
					ac.compare();
					if (indexL < leftA.size() && indexR < rightA.size()) {
						if (leftA.get(indexL) <= rightA.get(indexR)) {
							left.addHighlight(indexL, Constants.COLOR_SWAP);
							merge.addHighlight(indexRes, Constants.COLOR_SWAP);
							mergeA.set(indexRes, leftA.get(indexL));
							ac.swap();
							left.removeHighlight(indexL);
							merge.removeHighlight(indexRes);
							left.removeHighlight(indexR);
							indexL++;
							indexRes++;
						} else {
							right.addHighlight(indexR, Constants.COLOR_SWAP);
							merge.addHighlight(indexRes, Constants.COLOR_SWAP);
							mergeA.set(indexRes, rightA.get(indexR));
							ac.swap();
							right.removeHighlight(indexR);
							merge.removeHighlight(indexRes);
							right.removeHighlight(indexR);
							indexR++;
							indexRes++;
						}
					} else {
						ac.compare();
						if (indexL < leftA.size()) {
							mergeA.set(indexRes, leftA.get(indexL));
							ac.swap();
							indexL++;
							indexRes++;
						} else if (indexR < rightA.size()) {
							mergeA.set(indexRes, rightA.get(indexR));
							ac.swap();
							indexR++;
							indexRes++;
						}
					}
				}
			}

		public static <E extends Comparable<E>> void sort(ArrayCanvas ac)
			{
				ArrayList<Integer> B = ac.getArray().list;
				sort(ac, ac.getArray());

			}

		public static void sort(ArrayCanvas ac, PaintableArray pa)
			{
				if (pa.list.size() < 2) {
					return;
				}
				int midpoint = (pa.hi - pa.lo) / 2;
				PaintableArray left = pa.subArray(0, midpoint);

				sort(ac, left);
				PaintableArray right = pa.subArray(midpoint, pa.hi);
				sort(ac, right);

				merge(ac, left, right);
				left.remove(ac.getGraphicsContext2D());
				right.remove(ac.getGraphicsContext2D());
			}

	}
