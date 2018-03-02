package sorting;

import java.util.ArrayList;

import graphics.ArrayCanvas;
import graphics.PaintableArray;
import javafx.scene.canvas.GraphicsContext;
import utils.CanvasUtils;

public class ParallelBitonicSort
	{
		private static void compare(ArrayCanvas ac, PaintableArray pa, int i, int j, boolean dir)
			{
				if (dir == (CanvasUtils.compare(ac, pa, i, j) > 0)) {
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
					Runnable mLeft = () ->
						{
							merge(ac, list, lo, k, direction);
						};
					Thread t1 = ac.addThread(new Thread(mLeft));
					t1.start();
					Runnable mRight = () ->
						{
							merge(ac, list, lo + k, k, direction);
						};
					Thread t2 = ac.addThread(new Thread(mRight));
					t2.start();
					try {
						t1.join();
						t2.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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
				sort(ac, ac.getArray(), 0, list.size(), true, ac.getGraphicsContext2D());
				removePad(list, padding);
				Sorter.isSorted(ac.getArray(), true);
			}

		public static <E extends Comparable<E>> void sort(ArrayCanvas ac, PaintableArray pa, int lo, int count, boolean direction, GraphicsContext gc)
			{
				if (count > 1) {
					int k = count / 2;
					Runnable left = () ->
						{
							PaintableArray leftArray = pa.subArrayLinked(lo, lo + k);
							sort(ac, leftArray, lo, k, true, gc);
							leftArray.remove(gc);
						};
					Thread t1 = ac.addThread(new Thread(left));
					t1.start();
					Runnable right = () ->
						{
							PaintableArray rightArray = pa.subArrayLinked(lo + k, lo + count);
							sort(ac, rightArray, lo + k, k, false, gc);
							rightArray.remove(gc);
						};
					Thread t2 = ac.addThread(new Thread(right));
					t2.start();
					try {
						t1.join();
						t2.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					merge(ac, pa, lo, count, direction);
				}
			}
	}
