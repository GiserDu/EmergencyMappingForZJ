package com.zz.chart.render.bar;


import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
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
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.DataUtilities;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import org.jfree.util.PublicCloneable;

public class StackedBarChart3DRenderer extends BarChartRenderer
        implements Cloneable, PublicCloneable, Serializable {

    /** For serialization. */
    static final long serialVersionUID = 6402943811500067531L;

    /** A flag that controls whether the bars display values or percentages. */
    private boolean renderAsPercentages;
    
    /** A flag that controls whether or not bar outlines are drawn. */
    private boolean drawBarOutline;
    
    /** The size of x-offset for the 3D effect. */
    private double xOffset;

    public double getXOffset() {
		return xOffset;
	}

	public void setXOffset(double xOffset) {
		this.xOffset = xOffset;
	}

	public double getYOffset() {
		return yOffset;
	}

	public void setYOffset(double yOffset) {
		this.yOffset = yOffset;
	}

	/** The size of y-offset for the 3D effect. */
    private double yOffset;

    /**
     * Creates a new renderer.  By default, the renderer has no tool tip
     * generator and no URL generator.  These defaults have been chosen to
     * minimise the processing required to generate a default chart.  If you
     * require tool tips or URLs, then you can easily add the required
     * generators.
     */
    public StackedBarChart3DRenderer() {
        this(false);
    }

    /**
     * Creates a new renderer.
     *
     * @param renderAsPercentages  a flag that controls whether the data values
     *                             are rendered as percentages.
     */
    public StackedBarChart3DRenderer(boolean renderAsPercentages) {
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
    public StackedBarChart3DRenderer(double xOffset, double yOffset) {

        super();
        this.xOffset = xOffset;
        this.yOffset = yOffset;
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
     * Returns a flag that controls whether or not bar outlines are drawn.
     *
     * @return A boolean.
     *
     * @see #setDrawBarOutline(boolean)
     */
    public boolean isDrawBarOutline() {
        return this.drawBarOutline;
    }

    /**
     * Sets the flag that controls whether or not bar outlines are drawn and
     * sends a {@link RendererChangeEvent} to all registered listeners.
     *
     * @param draw  the flag.
     *
     * @see #isDrawBarOutline()
     */
    public void setDrawBarOutline(boolean draw) {
        this.drawBarOutline = draw;
        fireChangeEvent();
    }

    /**
     * Returns <code>true</code> if the renderer displays each item value as
     * a percentage (so that the stacked bars add to 100%), and
     * <code>false</code> otherwise.
     *
     * @return A boolean.
     *
     * @see #setRenderAsPercentages(boolean)
     */
    public boolean getRenderAsPercentages() {
        return this.renderAsPercentages;
    }

    /**
     * Sets the flag that controls whether the renderer displays each item
     * value as a percentage (so that the stacked bars add to 100%), and sends
     * a {@link RendererChangeEvent} to all registered listeners.
     *
     * @param asPercentages  the flag.
     *
     * @see #getRenderAsPercentages()
     */
    public void setRenderAsPercentages(boolean asPercentages) {
        this.renderAsPercentages = asPercentages;
        fireChangeEvent();
    }

    /**
     * Returns the number of passes (<code>3</code>) required by this renderer.
     * The first pass is used to draw the bar shadows, the second pass is used
     * to draw the bars, and the third pass is used to draw the item labels
     * (if visible).
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
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @return The range (or <code>null</code> if the dataset is empty).
     */
    public Range findRangeBounds(CategoryDataset dataset) {
        if (this.renderAsPercentages) {
            return new Range(0.0, 1.0);
        }
        else {
            return DatasetUtilities.findStackedRangeBounds(dataset, getBase());
        }
    }

    /**
     * Calculates the bar width and stores it in the renderer state.
     *
     * @param plot  the plot.
     * @param dataArea  the data area.
     * @param rendererIndex  the renderer index.
     * @param state  the renderer state.
     */
    protected void calculateBarWidth(CategoryPlot plot,
                                     Rectangle2D dataArea,
                                     int rendererIndex,
                                     CategoryItemRendererState state) {

        // calculate the bar width
        CategoryAxis xAxis = plot.getDomainAxisForDataset(rendererIndex);
        CategoryDataset data = plot.getDataset(rendererIndex);
        if (data != null) {
            PlotOrientation orientation = plot.getOrientation();
            double space = 0.0;
            if (orientation == PlotOrientation.HORIZONTAL) {
                space = dataArea.getHeight();
            }
            else if (orientation == PlotOrientation.VERTICAL) {
                space = dataArea.getWidth();
            }
            double maxWidth = space * getMaximumBarWidth();
            int columns = data.getColumnCount();
            double categoryMargin = 0.0;
            if (columns > 1) {
                categoryMargin = xAxis.getCategoryMargin();
            }

            double used = space * (1 - xAxis.getLowerMargin()
                                     - xAxis.getUpperMargin()
                                     - categoryMargin);
            if (columns > 0) {
                state.setBarWidth(Math.min(used / columns, maxWidth));
            }
            else {
                state.setBarWidth(Math.min(used, maxWidth));
            }
        }

    }

    /**
     * Draws a stacked bar for a specific item.
     *
     * @param g2  the graphics device.
     * @param state  the renderer state.
     * @param dataArea  the plot area.
     * @param plot  the plot.
     * @param domainAxis  the domain (category) axis.
     * @param rangeAxis  the range (value) axis.
     * @param dataset  the data.
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     * @param pass  the pass index.
     */
    public void drawItem(Graphics2D g2,
                         CategoryItemRendererState state,
                         Rectangle2D dataArea,
                         CategoryPlot plot,
                         CategoryAxis domainAxis,
                         ValueAxis rangeAxis,
                         CategoryDataset dataset,
                         int row,
                         int column,
                         int pass) {

        // nothing is drawn for null values...
        Number dataValue = dataset.getValue(row, column);
        if (dataValue == null) {
            return;
        }

        double value = dataValue.doubleValue();
        double total = 0.0;  // only needed if calculating percentages
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
                }
                else {
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
            }
            else {
                barBase = RectangleEdge.LEFT;
            }
        }
        else {
            if (positive && !inverted || !positive && inverted) {
                barBase = RectangleEdge.BOTTOM;
            }
            else {
                barBase = RectangleEdge.TOP;
            }
        }

        RectangleEdge location = plot.getRangeAxisEdge();
        if (positive) {
            translatedBase = rangeAxis.valueToJava2D(positiveBase, dataArea,
                    location);
            translatedValue = rangeAxis.valueToJava2D(positiveBase + value,
                    dataArea, location);
        }
        else {
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
            bar = new Rectangle2D.Double(barL0, barW0, barLength,
                    state.getBarWidth());
        }
        else {
            bar = new Rectangle2D.Double(barW0, barL0, state.getBarWidth(),
                    barLength);
        }
        Paint itemPaint = getItemPaint(row, column);
        GradientPaint pBarPainter = new GradientPaint((float) barW0,(float) (barL0 + barLength/2), (Color)itemPaint,(float) (barW0+ state.getBarWidth()),(float) (barL0 + barLength ),new Color(255, 255, 255, 255),true);

        g2.setPaint(pBarPainter);
        
        if (pass == 0) {
            if (getShadowsVisible()) {
                boolean pegToBase = (positive && (positiveBase == getBase()))
                        || (!positive && (negativeBase == getBase()));
                getBarPainter().paintBarShadow(g2, this, row, column, bar,
                        barBase, pegToBase);
            }
        }

       
        else if (pass == 1) {
            //getBarPainter().paintBar(g2, this, row, column, bar, barBase);
        	g2.fill(bar);
        	
//        	 Paint itempaint = getItemPaint(row, column);
//        	 g2.setPaint(itempaint);
//        	 System.out.println(itempaint);
//        	g2.fill(bar);

            // add an item entity, if this information is being collected
            EntityCollection entities = state.getEntityCollection();
            if (entities != null) {
                addItemEntity(entities, dataset, row, column, bar);
            }
        }

        double x0 = bar.getMinX();
        double x1 = x0 + getXOffset();
        double x2 = bar.getMaxX();
        double x3 = x2 + getXOffset();

        double y0 = bar.getMinY() - getYOffset();
        double y1 = bar.getMinY();
        double y2 = bar.getMaxY() - getYOffset();
        double y3 = bar.getMaxY();

        GeneralPath bar3dRight = null;
        GeneralPath bar3dTop = null;
        if (barLength > 0.0) {
            bar3dRight = new GeneralPath();
            bar3dRight.moveTo((float) x2, (float) y3);
            bar3dRight.lineTo((float) x2, (float) y1);
            bar3dRight.lineTo((float) x3, (float) y0);
            bar3dRight.lineTo((float) x3, (float) y2);
            bar3dRight.closePath();

            if (itemPaint instanceof Color) {
                g2.setPaint(((Color) itemPaint).darker());
            }
            g2.fill(bar3dRight);
        }

        if (value>0) {
        	bar3dTop = new GeneralPath();
            bar3dTop.moveTo((float) x0, (float) y1);
            bar3dTop.lineTo((float) x1, (float) y0);
            bar3dTop.lineTo((float) x3, (float) y0);
            bar3dTop.lineTo((float) x2, (float) y1);
            bar3dTop.closePath();
            g2.fill(bar3dTop);
		}

        if (isDrawBarOutline()
                && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
            g2.setStroke(getItemOutlineStroke(row, column));
            g2.setPaint(getItemOutlinePaint(row, column));
            g2.draw(bar);
            if (bar3dRight != null) {
                g2.draw(bar3dRight);
            }
            if (bar3dTop != null) {
                g2.draw(bar3dTop);
            }
        }
        if (drawBarOutline) {
        	
			g2.setPaint(getBaseOutlinePaint());
			g2.setStroke(getBaseOutlineStroke());
			g2.draw(bar);
		}
        else if (pass == 2) {
            CategoryItemLabelGenerator generator = getItemLabelGenerator(row,
                    column);
            if (generator != null && isItemLabelVisible(row, column)) {
                drawItemLabel(g2, dataset, row, column, plot, generator, bar,
                        (value < 0.0));
            }
        }
    }


}
