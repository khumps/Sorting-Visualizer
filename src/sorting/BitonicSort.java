package sorting;

import java.util.ArrayList;

import graphics.ArrayCanvas;
import graphics.PaintableArray;
import javafx.scene.canvas.GraphicsContext;
import utils.CanvasUtils;

public class BitonicSort
	{
		private static void compare(ArrayCanvas ac, PaintableArray pa, int i, int j, boolean dir)
			{
				if (dir == CanvasUtils.compare(ac, pa, i, j) > 0) {
					CanvasUtils.swap(ac, pa, i, j);
				}
			}

		private static void merge(ArrayCanvas ac, PaintableArray list, int lo, int count, boolean direction)
			{
				if (count > 1) {
					int k = count / 2;
					for (int i = lo; i < lo + k; i++) {
						compare(ac, list, i, i + k, direction);
					}
					PaintableArray left = list.subArrayLinked(lo, lo + k);
					merge(ac, list, lo, k, direction);
					left.remove(ac.getGraphicsContext2D());
					PaintableArray right = list.subArrayLinked(lo + k, lo + count);
					merge(ac, list, lo + k, k, direction);
					right.remove(ac.getGraphicsContext2D());
				}
			}

		private static int nextPowTwo(int val)
			{
				int next = 2;
				while (next < val)
					next *= 2;
				return next;
			}

		private static int padLength(ArrayList<Integer> list)
			{
				int finalLength = nextPowTwo(list.size());
				int numToAdd = finalLength - list.size();
				for (int i = 0; i < numToAdd; i++) {
					list.add(0, 0);
				}
				return numToAdd;
			}

		private static void removePad(ArrayList<Integer> list, int numPadded)
			{
				for (int i = 0; i < numPadded; i++) {
					list.remove(0);
				}
			}

		public static <E extends Comparable<E>> void sort(ArrayCanvas ac)
			{
				ArrayList<Integer> list = ac.getArray().list;
				int padding = padLength(list);
				System.out.println(ac.getArray().list.size());
				ac.getArray().updateArray();
				sort(ac, ac.getArray(), 0, list.size(), true, ac.getGraphicsContext2D());
				removePad(list, padding);
				ac.getArray().updateArray();
			}

		public static <E extends Comparable<E>> void sort(ArrayCanvas ac, PaintableArray pa, int lo, int count, boolean direction, GraphicsContext gc)
			{
				if (count > 1) {
					int k = count / 2;
					PaintableArray left = pa.subArrayLinked(lo, lo + k);
					sort(ac, left, lo, k, true, gc);
					left.remove(gc);
					PaintableArray right = pa.subArrayLinked(lo + k, lo + count);
					sort(ac, right, lo + k, k, false, gc);
					right.remove(gc);
					merge(ac, pa, lo, count, direction);
				}
			}
	}
