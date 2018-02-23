package utils;

import javafx.scene.paint.Color;

public class Constants
	{

		public enum SHUFFLER_TYPE
			{
			NEARLY_SORTED_ALL_UNIQUES, NEARLY_SORTED_FEW_UNIQUES, RANDOM_ALL_UNIQUES, RANDOM_FEW_UNIQUES, REVERSE_ALL_UNIQUES
			}

		// Sorter
		// @formatter:off
		public enum SORTING_TYPE{
				SELECTION,
				INSERTION,
				RADIX,
				BOGO,
				//SHELL,
				BUBBLE,
				HEAP,
				MERGE,
				PMERGE,
				QUICK,
				PQUICK,
				BITONIC,
				PBITONIC
		}
		
		public enum STATE
		{
		STOPPED,
		RUNNING,
		PAUSED;
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
		public static final int MIN_DELAY = 0;
		public static final int MAX_DELAY = 200;
		// PaintableArray
		public static final int maxDrawDepth = 6;
		// @formatter:on
	}
