package utils;

import javafx.scene.paint.Color;

public class Constants
	{

		public enum SHUFFLER_TYPE
			{
			NEARLY_SORTED_AU, NEARLY_SORTED_FU, RANDOM_AU, RANDOM_FU
			}

		// Sorter
		// @formatter:off
		public enum SORTING_TYPE{
				SELECTION,
				INSERTION,
				SHELL,
				BUBBLE,
				HEAP,
				MERGE,
				PMERGE,
				QUICK,
				PQUICK,
				BITONIC,
				PBITONIC
		}
		public static final Color COLOR_POINTER1 = Color.RED;
		public static final Color COLOR_POINTER2 = Color.GREEN;

		// ArrayCanvas
		public static final Color COLOR_SWAP = Color.BLUE;

		// CanvasTimer
		public static long DELAY = 10;

		// VisualizingPanel
		public static final int MAX_SORTERS = 6;
		public static final int HGAP = 30; // Gap in between sorters
		public static final int MIN_SORTER_WIDTH = 400; // Minimum size of a single sorter
		// PaintableArray
		public static final int maxDrawDepth = 20;
		// @formatter:on
	}
