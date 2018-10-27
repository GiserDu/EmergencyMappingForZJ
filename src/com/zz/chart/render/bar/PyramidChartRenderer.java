package com.zz.chart.render.bar;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.DataUtilities;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.Size2D;
import org.jfree.ui.TextAnchor;
import org.jfree.util.PublicCloneable;

/**
 * A stacked bar renderer for use with the
 * {@link org.jfree.chart.plot.CategoryPlot} class.
 */
public class PyramidChartRenderer extends BarRenderer implements Cloneable,
		PublicCloneable, Serializable {

	/** For serialization. */
	static final long serialVersionUID = 6402943811500067531L;

	/** A flag that controls whether the bars display values or percentages. */
	private boolean renderAsPercentages;

	/**
	 * Creates a new renderer. By default, the renderer has no tool tip
	 * generator and no URL generator. These defaults have been chosen to
	 * minimise the processing required to generate a default chart. If you
	 * require tool tips or URLs, then you can easily add the required
	 * generators.
	 */
	public PyramidChartRenderer() {
		this(false);
	}

	/**
	 * Creates a new renderer.
	 * 
	 * @param renderAsPercentages
	 *            a flag that controls whether the data values are rendered as
	 *            percentages.
	 */
	public PyramidChartRenderer(boolean renderAsPercentages) {
		super();
		this.renderAsPercentages = renderAsPercentages;

		// set the default item label positions, which will only be used if
		// the user requests visible item labels...
		ItemLabelPosition p = new ItemLabelPosition(ItemLabelAnchor.CENTER,
				TextAnchor.CENTER);
		setBasePositiveItemLabelPosition(p);
		setBaseNegativeItemLabelPosition(p);
		setPositiveItemLabelPositionFallback(null);
		setNegativeItemLabelPositionFallback(null);
	}

	/**
	 * Returns <code>true</code> if the renderer displays each item value as a
	 * percentage (so that the stacked bars add to 100%), and <code>false</code>
	 * otherwise.
	 * 
	 * @return A boolean.
	 * 
	 * @see #setRenderAsPercentages(boolean)
	 */
	public boolean getRenderAsPercentages() {
		return this.renderAsPercentages;
	}

	/**
	 * Sets the flag that controls whether the renderer displays each item value
	 * as a percentage (so that the stacked bars add to 100%), and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param asPercentages
	 *            the flag.
	 * 
	 * @see #getRenderAsPercentages()
	 */
	public void setRenderAsPercentages(boolean asPercentages) {
		this.renderAsPercentages = asPercentages;
		fireChangeEvent();
	}

	/**
	 * Returns the number of passes (<code>3</code>) required by this
	 * renderer. The first pass is used to draw the bar shadows, the second pass
	 * is used to draw the bars, and the third pass is used to draw the item
	 * labels (if visible).
	 * 
	 * @return The number of passes required by the renderer.
	 */
	public int getPassCount() {
		return 3;
	}

	/**
	 * Returns the range of values the renderer requires to display all the
	 * items from the specified dataset.
	 * 
	 * @param dataset
	 *            the dataset (<code>null</code> permitted).
	 * 
	 * @return The range (or <code>null</code> if the dataset is empty).
	 */
	public Range findRangeBounds(CategoryDataset dataset) {
		if (this.renderAsPercentages) {
			return new Range(0.0, 1.0);
		} else {
			return DatasetUtilities.findStackedRangeBounds(dataset, getBase());
		}
	}

	/**
	 * Calculates the bar width and stores it in the renderer state.
	 * 
	 * @param plot
	 *            the plot.
	 * @param dataArea
	 *            the data area.
	 * @param rendererIndex
	 *            the renderer index.
	 * @param state
	 *            the renderer state.
	 */
	protected void calculateBarWidth(CategoryPlot plot, Rectangle2D dataArea,
			int rendererIndex, CategoryItemRendererState state) {

		// calculate the bar width
		CategoryAxis xAxis = plot.getDomainAxisForDataset(rendererIndex);
		CategoryDataset data = plot.getDataset(rendererIndex);
		if (data != null) {
			PlotOrientation orientation = plot.getOrientation();
			double space = 0.0;
			if (orientation == PlotOrientation.HORIZONTAL) {
				space = dataArea.getHeight();
			} else if (orientation == PlotOrientation.VERTICAL) {
				space = dataArea.getWidth();
			}
			double maxWidth = space * getMaximumBarWidth();
			int columns = data.getColumnCount();
			double categoryMargin = 0.0;
			if (columns > 1) {
				categoryMargin = xAxis.getCategoryMargin();
			}

			double used = space
					* (1 - xAxis.getLowerMargin() - xAxis.getUpperMargin() - categoryMargin);
			if (columns > 0) {
				state.setBarWidth(Math.min(used / columns, maxWidth));
			} else {
				state.setBarWidth(Math.min(used, maxWidth));
			}
		}

	}

	/**
	 * Draws a stacked bar for a specific item.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param state
	 *            the renderer state.
	 * @param dataArea
	 *            the plot area.
	 * @param plot
	 *            the plot.
	 * @param domainAxis
	 *            the domain (category) axis.
	 * @param rangeAxis
	 *            the range (value) axis.
	 * @param dataset
	 *            the data.
	 * @param row
	 *            the row index (zero-based).
	 * @param column
	 *            the column index (zero-based).
	 * @param pass
	 *            the pass index.
	 */
	public void drawItem(Graphics2D g2, CategoryItemRendererState state,
			Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis,
			ValueAxis rangeAxis, CategoryDataset dataset, int row, int column,
			int pass) {

		// nothing is drawn for null values...
		Number dataValue = dataset.getValue(row, column);
		if (dataValue == null) {
			return;
		}

		double value = dataValue.doubleValue();
		double total = 0.0; // only needed if calculating percentages
		if (this.renderAsPercentages) {
			total = DataUtilities.calculateColumnTotal(dataset, column);
			value = value / total;
		}

		PlotOrientation orientation = plot.getOrientation();
		double barW0 = domainAxis.getCategoryMiddle(column, getColumnCount(),
				dataArea, plot.getDomainAxisEdge())
				- state.getBarWidth() / 2.0;

		double positiveBase = getBase();
		double negativeBase = positiveBase;

		for (int i = 0; i < row; i++) {
			Number v = dataset.getValue(i, column);
			if (v != null) {
				double d = v.doubleValue();
				if (this.renderAsPercentages) {
					d = d / total;
				}
				if (d > 0) {
					positiveBase = positiveBase + d;
				} else {
					negativeBase = negativeBase + d;
				}
			}
		}

		double translatedBase;
		double translatedValue;
		boolean positive = (value > 0.0);
		boolean inverted = rangeAxis.isInverted();
		RectangleEdge barBase;
		if (orientation == PlotOrientation.HORIZONTAL) {
			if (positive && inverted || !positive && !inverted) {
				barBase = RectangleEdge.RIGHT;
			} else {
				barBase = RectangleEdge.LEFT;
			}
		} else {
			if (positive && !inverted || !positive && inverted) {
				barBase = RectangleEdge.BOTTOM;
			} else {
				barBase = RectangleEdge.TOP;
			}
		}

		RectangleEdge location = plot.getRangeAxisEdge();
		if (positive) {
			translatedBase = rangeAxis.valueToJava2D(positiveBase, dataArea,
					location);
			translatedValue = rangeAxis.valueToJava2D(positiveBase + value,
					dataArea, location);
		} else {
			translatedBase = rangeAxis.valueToJava2D(negativeBase, dataArea,
					location);
			translatedValue = rangeAxis.valueToJava2D(negativeBase + value,
					dataArea, location);
		}
		double barL0 = Math.min(translatedBase, translatedValue);
		double barLength = Math.max(Math.abs(translatedValue - translatedBase),
				getMinimumBarLength());

		Rectangle2D bar = null;
		if (orientation == PlotOrientation.HORIZONTAL) {
			bar = new Rectangle2D.Double(barL0, barW0 , barLength, state
					.getBarWidth());
		} else {
			bar = new Rectangle2D.Double(barW0, barL0 + barLength - dataArea.getHeight()/2, state.getBarWidth(),
					barLength);
		}
 
		if (pass == 0) {
			if (getShadowsVisible()) {
				boolean pegToBase = (positive && (positiveBase == getBase()))
						|| (!positive && (negativeBase == getBase()));
				getBarPainter().paintBarShadow(g2, this, row, column, bar,
						barBase, pegToBase);
			}
		} else if (pass == 1) {
			getBarPainter().paintBar(g2, this, row, column, bar, barBase);

			// add an item entity, if this information is being collected
			EntityCollection entities = state.getEntityCollection();
			if (entities != null) {
				addItemEntity(entities, dataset, row, column, bar);
			}
		} else if (pass == 2) {
			CategoryItemLabelGenerator generator = getItemLabelGenerator(row,
					column);
			if (generator != null && isItemLabelVisible(row, column)) {
				drawItemLabel(g2, dataset, row, column, plot, generator, bar,
						(value < 0.0));
			}
		}
	}

	protected void drawItemLabel(Graphics2D g2, CategoryDataset data, int row,
			int column, CategoryPlot plot,
			CategoryItemLabelGenerator generator, Rectangle2D bar,
			boolean negative) {

		String label = generator.generateLabel(data, row, column);
		if (label == null) {
			return; // nothing to do
		}

		label = label.replace("-", "");
		Font labelFont = getItemLabelFont(row, column);
		g2.setFont(labelFont);
		Paint paint = getItemLabelPaint(row, column);
		g2.setPaint(paint);

		// find out where to place the label...
		ItemLabelPosition position = null;
		if (!negative) {
			position = getPositiveItemLabelPosition(row, column);
		} else {
			position = getNegativeItemLabelPosition(row, column);
		}

		// work out the label anchor point...
		Point2D anchorPoint = this.calculateLabelAnchorPoint(position
				.getItemLabelAnchor(), bar, plot.getOrientation());

		if (isInternalAnchor(position.getItemLabelAnchor())) {
//			Shape bounds = TextUtilities.calculateRotatedStringBounds(label,
//					g2, (float) anchorPoint.getX(), (float) anchorPoint.getY(),
//					position.getTextAnchor(), position.getAngle(), position
//							.getRotationAnchor());
			Shape bounds = this.getLabelTextBound(label,g2,bar);
			
			if (bounds != null) {
				//if (!bar.contains(bounds.getBounds2D()))
				if (!bar.contains(bounds.getBounds2D()))
				 {
					if (!negative) {
						position = getPositiveItemLabelPositionFallback();
					} else {
						position = getNegativeItemLabelPositionFallback();
					}
					if (position != null) {
						anchorPoint = calculateLabelAnchorPoint(position
								.getItemLabelAnchor(), bar, plot
								.getOrientation());
					}
				}
			}

		}

		if (position != null) {
			TextUtilities.drawRotatedString(label, g2, (float) anchorPoint
					.getX(), (float) anchorPoint.getY(), position
					.getTextAnchor(), position.getAngle(), position
					.getRotationAnchor());
		}
	}

	private Rectangle2D.Double getLabelTextBound(String label,Graphics2D g2,Rectangle2D bar) {
		
		TextTitle textTitle = new TextTitle();
		textTitle.setText(label);
		textTitle.setFont(g2.getFont());
		textTitle.setMargin(1, 1, 1, 1);
		BufferedImage imgTitle = new BufferedImage(1, 1,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2Title = imgTitle.createGraphics();
        Size2D sizeTitle = textTitle.arrange(g2Title);
        g2Title.dispose();
        int wTitle = (int) Math.rint(sizeTitle.width);
        int hTitle = (int) Math.rint(sizeTitle.height);
        
        Rectangle2D.Double boud = new Rectangle2D.Double(bar.getCenterX()-wTitle/2,bar.getCenterY()-hTitle/2,wTitle,hTitle);
        
        return boud;
	}
	
	 /**
     * Calculates the item label anchor point.
     *
     * @param anchor  the anchor.
     * @param bar  the bar.
     * @param orientation  the plot orientation.
     *
     * @return The anchor point.
     */
    private Point2D calculateLabelAnchorPoint(ItemLabelAnchor anchor,
                                              Rectangle2D bar,
                                              PlotOrientation orientation) {

        Point2D result = null;
        double offset = getItemLabelAnchorOffset();
        double x0 = bar.getX() - offset;
        double x1 = bar.getX();
        double x2 = bar.getX() + offset;
        double x3 = bar.getCenterX();
        double x4 = bar.getMaxX() - offset;
        double x5 = bar.getMaxX();
        double x6 = bar.getMaxX() + offset;

        double y0 = bar.getMaxY() + offset;
        double y1 = bar.getMaxY();
        double y2 = bar.getMaxY() - offset;
        double y3 = bar.getCenterY();
        double y4 = bar.getMinY() + offset;
        double y5 = bar.getMinY();
        double y6 = bar.getMinY() - offset;

        if (anchor == ItemLabelAnchor.CENTER) {
            result = new Point2D.Double(x3, y3);
        }
        else if (anchor == ItemLabelAnchor.INSIDE1) {
            result = new Point2D.Double(x4, y4);
        }
        else if (anchor == ItemLabelAnchor.INSIDE2) {
            result = new Point2D.Double(x4, y4);
        }
        else if (anchor == ItemLabelAnchor.INSIDE3) {
            result = new Point2D.Double(x4, y3);
        }
        else if (anchor == ItemLabelAnchor.INSIDE4) {
            result = new Point2D.Double(x4, y2);
        }
        else if (anchor == ItemLabelAnchor.INSIDE5) {
            result = new Point2D.Double(x4, y2);
        }
        else if (anchor == ItemLabelAnchor.INSIDE6) {
            result = new Point2D.Double(x3, y2);
        }
        else if (anchor == ItemLabelAnchor.INSIDE7) {
            result = new Point2D.Double(x2, y2);
        }
        else if (anchor == ItemLabelAnchor.INSIDE8) {
            result = new Point2D.Double(x2, y2);
        }
        else if (anchor == ItemLabelAnchor.INSIDE9) {
            result = new Point2D.Double(x2, y3);
        }
        else if (anchor == ItemLabelAnchor.INSIDE10) {
            result = new Point2D.Double(x2, y4);
        }
        else if (anchor == ItemLabelAnchor.INSIDE11) {
            result = new Point2D.Double(x2, y4);
        }
        else if (anchor == ItemLabelAnchor.INSIDE12) {
            result = new Point2D.Double(x3, y4);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE1) {
            result = new Point2D.Double(x5, y6);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE2) {
            result = new Point2D.Double(x6, y5);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE3) {
            result = new Point2D.Double(x6, y3);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE4) {
            result = new Point2D.Double(x6, y1);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE5) {
            result = new Point2D.Double(x5, y0);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE6) {
            result = new Point2D.Double(x3, y0);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE7) {
            result = new Point2D.Double(x1, y0);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE8) {
            result = new Point2D.Double(x0, y1);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE9) {
            result = new Point2D.Double(x0, y3);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE10) {
            result = new Point2D.Double(x0, y5);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE11) {
            result = new Point2D.Double(x1, y6);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE12) {
            result = new Point2D.Double(x3, y6);
        }

        return result;

    }
    
    /**
     * Returns <code>true</code> if the specified anchor point is inside a bar.
     *
     * @param anchor  the anchor point.
     *
     * @return A boolean.
     */
    private boolean isInternalAnchor(ItemLabelAnchor anchor) {
        return anchor == ItemLabelAnchor.CENTER
               || anchor == ItemLabelAnchor.INSIDE1
               || anchor == ItemLabelAnchor.INSIDE2
               || anchor == ItemLabelAnchor.INSIDE3
               || anchor == ItemLabelAnchor.INSIDE4
               || anchor == ItemLabelAnchor.INSIDE5
               || anchor == ItemLabelAnchor.INSIDE6
               || anchor == ItemLabelAnchor.INSIDE7
               || anchor == ItemLabelAnchor.INSIDE8
               || anchor == ItemLabelAnchor.INSIDE9
               || anchor == ItemLabelAnchor.INSIDE10
               || anchor == ItemLabelAnchor.INSIDE11
               || anchor == ItemLabelAnchor.INSIDE12;
    }
	/**
	 * Tests this renderer for equality with an arbitrary object.
	 * 
	 * @param obj
	 *            the object (<code>null</code> permitted).
	 * 
	 * @return A boolean.
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof PyramidChartRenderer)) {
			return false;
		}
		PyramidChartRenderer that = (PyramidChartRenderer) obj;
		if (this.renderAsPercentages != that.renderAsPercentages) {
			return false;
		}
		return super.equals(obj);
	}

}
