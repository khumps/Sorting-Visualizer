package utils;

import java.util.ArrayList;

import utils.Constants.SHUFFLER_TYPE;

public class Shuffler
	{
		/**
		 * Generates a list of Integers of length numEntries with numEntries/10 of each value
		 */
		public static ArrayList<Integer> fewUniques(int numEntries, boolean forwards)
			{
				ArrayList<Integer> list = new ArrayList<Integer>();
				int val = 1;
				for (int i = 0; i < numEntries; i++) {
					if (i % (numEntries / 10) == 0)
						val++;
					list.add(val);
				}
				return list;
			}

		/**
		 * Generates an array with the given SHUFFLER_TYPE and given size
		 */
		public static ArrayList<Integer> generateArray(SHUFFLER_TYPE type, int size)
			{
				if (type == null) {
					return inOrder(size, true);
				}
				switch (type)
					{
					case NEARLY_SORTED_ALL_UNIQUES:
						return nearlySorted(inOrder(size, true));
					case NEARLY_SORTED_FEW_UNIQUES:
						return nearlySorted(fewUniques(size, true));
					case RANDOM_ALL_UNIQUES:
						return random(inOrder(size, true));
					case RANDOM_FEW_UNIQUES:
						return random(fewUniques(size, true));
					case REVERSE_ALL_UNIQUES:
						return inOrder(size, false);
					default:
						return inOrder(size, true);

					}
			}

		/**
		 * Generates a list of integers from 1-numEntries if forwards or numEntries-1 if !forwards
		 */
		public static ArrayList<Integer> inOrder(int numEntries, boolean forwards)
			{
				ArrayList<Integer> temp = new ArrayList<Integer>();
				if (forwards) {
					for (int i = 1; i <= numEntries; i++)
						temp.add(i);
				} else {
					for (int i = numEntries; i > 0; i--) {
						temp.add(i);
					}
				}
				return temp;
			}

		/**
		 * Takes the parameter array and swaps a few of the elements
		 */
		public static <E> ArrayList<E> nearlySorted(ArrayList<E> list)
			{
				for (int i = 0; i < list.size(); i += list.size() / (list.size() / 5)) {
					swap(list, i, (int) (Math.random() * list.size()));
				}
				return list;
			}

		/**
		 * Takes the parameter array and randomizes it
		 */
		public static <E> ArrayList<E> random(ArrayList<E> list)
			{
				for (int i = 0; i < list.size(); i++) {
					swap(list, i, (int) (Math.random() * list.size()));
				}
				return list;
			}

		/**
		 * Swaps two indexes in a list
		 */
		public static <E> void swap(ArrayList<E> list, int i, int j)
			{
				list.set(i, list.set(j, list.get(i)));
			}

	}