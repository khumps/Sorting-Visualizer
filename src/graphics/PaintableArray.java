package graphics;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import utils.CanvasUtils;
import utils.Constants;

/**
 * Visualizes an instance of ArrayList<Integer> and handles painting it onto a canvas. Also can handle painting subsections of the base ArrayList
 * 
 * @author Kevin Humphreys
 *
 */
public class PaintableArray
	{
		private final ArrayList<PaintableArray> children;

		public final int depth;
		public double height;
		public int hi;
		HashMap<Integer, Color> highlights;
		public final ArrayList<Integer> list;
		public int lo;
		public int max;
		public final PaintableArray parent;
		public double width;
		public double x;
		public double y;

		public PaintableArray(ArrayList<Integer> list, double x, double y, double width, double height, int depth, PaintableArray parent)
			{
				this.list = list;
				this.x = x;
				this.y = y;
				this.width = width;
				this.height = height;
				this.depth = depth;
				this.parent = parent;
				lo = 0;
				hi = list.size();
				max = maxVal();
				children = new ArrayList<PaintableArray>();
				highlights = new HashMap<Integer, Color>(list.size());

			}

		public PaintableArray(ArrayList<Integer> list, int lo, int hi, double x, double y, double width, double height, int depth, PaintableArray parent)
			{
				this.list = list;
				this.x = x;
				this.y = y;
				this.width = width;
				this.height = height;
				this.depth = depth;
				this.parent = parent;
				this.lo = lo;
				this.hi = hi;
				max = maxVal();
				children = new ArrayList<PaintableArray>();
				highlights = new HashMap<Integer, Color>(list.size());
			}

		/**
		 * Adds highlights to be painted until removed
		 * 
		 * @param index
		 *            index to be highlighted
		 * @param color
		 *            Color to highlight the index
		 */
		public void addHighlight(int index, Color color)
			{
				CanvasUtils.sleep(Constants.DELAY);
				highlights.put(index, color);
			}

		/**
		 * Adds highlights for two indexes being swapped
		 */
		public void addSwap(int index1, int index2)
			{
				addHighlight(index1, Constants.COLOR_SWAP);
				addHighlight(index2, Constants.COLOR_SWAP);
			}

		/**
		 * Cleans up the canvas of this and all child array
		 * 
		 * @param gc
		 */
		public void cleanup(GraphicsContext gc)
			{
				clear(gc);
			}

		/**
		 * Erases the canvas of the array and all child arrays
		 */
		public void clear(GraphicsContext gc)
			{
				gc.clearRect(x - 1, y - 1, width + 2, height + 2);
				for (int i = 0; i < children.size(); i++) {
					if (children.get(i) != null) {
						children.get(i).clear(gc);
					}
				}
			}

		/**
		 * Returns a copy of the PaintableArray but with a different array
		 */
		public PaintableArray copyDifferentArray(ArrayList<Integer> list)
			{
				return new PaintableArray(list, x, y, width, height, 1, null);
			}

		/**
		 * Returns an individual highlight from the Map
		 */
		public Color getHighlight(int index)
			{
				if (highlights.containsKey(index)) {
					return highlights.get(index);
				}
				return Color.BLACK;
			}

		/**
		 * Returns the maximum value in the list
		 */
		private int maxVal()
			{
				int max = list.get(0);
				for (int i = 1; i < list.size(); i++) {
					if (list.get(i) > max)
						max = list.get(i);
				}
				return max;
			}

		/**
		 * Paints this and all children arrays
		 */
		public void paintAll(ArrayCanvas ac)
			{
				paintArray(ac);
				paintChildren(ac);
			}

		/**
		 * Paints this and all children arrays up until the specified depth
		 */
		public void paintAll(ArrayCanvas ac, int depth)
			{
				paintArray(ac);
				for (int i = 0; i < children.size(); i++) {
					if (children.get(i) != null && children.get(i).depth <= depth)
						children.get(i).paintAll(ac, depth);
				}

			}

		/**
		 * Paints the array
		 */
		public void paintArray(ArrayCanvas ac)
			{
				CanvasUtils.drawArray(this, lo, hi, max, x, y, width, height, ac);
			}

		/**
		 * Paints this but using the coordinates and size of a different array
		 */
		public void paintArray(ArrayCanvas ac, PaintableArray param)
			{
				CanvasUtils.drawArray(this, lo, hi, max, param.x, param.y, width, param.height, ac);
			}

		/**
		 * Paints all children arrays without painting this
		 */
		public void paintChildren(ArrayCanvas ac)
			{
				for (PaintableArray p : children) {
					p.paintArray(ac);
					p.paintChildren(ac);
				}
			}

		/**
		 * Prints string representations of this and all children
		 */
		public void printAll()
			{
				for (PaintableArray p : children)
					p.printAll();
			}

		/**
		 * Removes the PaintableArray and clears it from the screen
		 */
		public void remove(GraphicsContext gc)
			{
				CanvasUtils.clearRect(x, y, width, height, gc);
				for (int i = 0; i < children.size(); i++) {
					if (children.get(i) != null) {
						PaintableArray pa = children.get(i);
						CanvasUtils.clearRect(pa.x, pa.y, pa.width, pa.height, gc);
					}
				}
				removeChildren(gc);
				synchronized (children)

				{
					parent.children.remove(this);
				}
			}

		/**
		 * Removes all children from the PaintableArray
		 * 
		 * @param gc
		 */
		public void removeChildren(GraphicsContext gc)
			{
				children.clear();
			}

		/**
		 * Removes a highlight
		 * 
		 * @param index
		 *            Index to remove highlight from
		 */
		public void removeHighlights(int... indexes)
			{
				CanvasUtils.sleep(Constants.DELAY);
				for (int i : indexes) {
					highlights.remove(i);
				}
			}

		public void clearHighlights()
			{
				highlights.clear();
			}

		/**
		 * Returns a PaintableArray that contains a copied subarray from lo -> hi. They do not share the same array
		 */
		public PaintableArray subArray(int lo, int hi)
			{
				ArrayList<Integer> temp = new ArrayList<Integer>(list.subList(lo, hi));
				PaintableArray p = new PaintableArray(temp, x + width / (this.hi - this.lo) * (lo - this.lo) + 2, y + height + 10, width / (this.hi - this.lo) * (hi - lo) - 4, height / 1.8, depth + 1,
						this);
				synchronized (children) {
					children.add(p);
				}
				return p;
			}

		/**
		 * Creates a child PaintableArray that shares the exact same ArrayList as its parent, only paints from lo -> hi at the given coords.
		 */
		public PaintableArray subArrayLinked(int lo, int hi)
			{
				PaintableArray p = new PaintableArray(list, x + width / (this.hi - this.lo) * (lo - this.lo) + 2, y + height + 10, width / (this.hi - this.lo) * (hi - lo) - 4, height / 1.5, depth + 1,
						this);
				synchronized (children) {
					children.add(p);
				}
				p.lo = lo;
				p.hi = hi;
				return p;
			}

		@Override
		public String toString()
			{
				return "Node: " + lo + " " + hi;
			}

		/**
		 * Re-evaluates the max length of the array as well as the largest element
		 */
		public void updateArray()
			{
				hi = list.size();
				max = maxVal();
			}

		public void updateSize(double width, GraphicsContext gc)
			{
				_updateSize(width / this.width, gc);
			}

		public void _updateSize(double widthScale, GraphicsContext gc)
			{
				clear(gc);
				width *= widthScale;
				x *= widthScale;
				for (int i = 0; i < children.size(); i++) {
					children.get(i)._updateSize(widthScale, gc);
				}
			}

	}
