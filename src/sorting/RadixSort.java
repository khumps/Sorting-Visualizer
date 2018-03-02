package sorting;

import java.util.ArrayList;

import graphics.ArrayCanvas;
import graphics.PaintableArray;
import utils.CanvasUtils;
import utils.Constants;

public class RadixSort
    {
        public static void sort(ArrayCanvas ac)
            {
                radixSort(ac);
            }

        /**
        	 * Finds the largest element, and calls counting sort for all the places up until that so that the array becomes properly sorted
        	 */
        public static void radixSort(ArrayCanvas ac)
            {
                ArrayList<Integer> list = ac.getArray().list;
                int maxDecimalPlaces = (maxVal(list) + "").length();
                for (int i = 1; i <= maxDecimalPlaces; i++) {
                    auxSort(ac, i);
                }
                Sorter.isSorted(ac.getArray(), true);
            }

        /**
         * Sorts an array so that all elements are in accending order according to the value of that decimal place, 1 being the 1's place, etc.
         */
        private static void auxSort(ArrayCanvas ac, int place)
            {
                PaintableArray pa = ac.getArray();
                ArrayList<Integer> list = pa.list;
                int[] counts = new int[10];
                for (int val : list) {
                    counts[getPlace(val, place)]++;
                }
                for (int i = 1; i < counts.length; i++) {
                    counts[i] += counts[i - 1];
                }
                PaintableArray ret = pa.subArray(0, pa.list.size());
                for (int i = 0; i < pa.list.size(); i++) {
                    ret.list.set(i, 0);
                }
                for (int i = list.size() - 1; i >= 0; i--) {
                    int j = list.get(i);
                    int k = counts[getPlace(j, place)];
                    ret.addHighlight(k - 1, Constants.COLOR_POINTER1);
                    ret.list.set(k - 1, list.get(i));
                    counts[getPlace(j, place)]--;
                    ret.removeHighlights(k - 1);
                }
                for (int i = 0; i < list.size(); i++) {
                    CanvasUtils.sleep(1);
                    list.set(i, ret.list.get(i));
                }
                ret.remove(ac.getGraphicsContext2D());
            }

        /**
         * Returns the number at the given place in a value. ex getPlace(345,1) would return 5
         */
        public static int getPlace(int a, int place)
            {
                for (int i = 0; i < place - 1; i++) {
                    a /= 10;
                }
                return a % 10;
            }

        /**
         * Returns the maximum value in an array, used to get the number of times that radix sort should run counting sort
         */
        public static int maxVal(ArrayList<Integer> a)
            {
                int max = Integer.MIN_VALUE;
                for (int val : a) {
                    if (val > max) {
                        max = val;
                    }
                }
                return max;
            }
    }