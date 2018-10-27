/* ---------------------
 * CylinderRenderer.java
 * ---------------------
 * (C) Copyright 2005-2008, by Object Refinery Limited.
 *
 */

package com.zz.chart.render.bar;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.category.CategoryDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;

/**
 * A custom renderer that draws cylinders to represent data from a
 * CategoryDataset in a CategoryPlot.
 */
public class CylinderChartRenderer extends BarChart3DRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public CylinderChartRenderer() {
		super();
	}

	/**
	 * Creates a new renderer.
	 * 
	 * @param xOffset
	 *            the x-offset for the 3D effect.
	 * @param yOffset
	 *            the y-offset for the 3D effect.
	 */
	public CylinderChartRenderer(double xOffset, double yOffset) {
		super(xOffset, yOffset);
	}

	/**
	 * Draws a cylinder to represent one data item.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param state
	 *            the renderer state.
	 * @param dataArea
	 *            the area for plotting the data.
	 * @param plot
	 *            the plot.
	 * @param domainAxis
	 *            the domain axis.
	 * @param rangeAxis
	 *            the range axis.
	 * @param dataset
	 *            the dataset.
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

		// check the value we are plotting...
		Number dataValue = dataset.getValue(row, column);
		if (dataValue == null) {
			return;
		}

		double value = dataValue.doubleValue();

		Rectangle2D adjusted = new Rectangle2D.Double(dataArea.getX(), dataArea
				.getY()
				+ getYOffset(), dataArea.getWidth() - getXOffset(), dataArea
				.getHeight()
				- getYOffset());

		PlotOrientation orientation = plot.getOrientation();

		double barW0 = calculateBarW0(plot, orientation, adjusted, domainAxis,
				state, row, column);
		double[] barL0L1 = calculateBarL0L1(value);
		if (barL0L1 == null) {
			return; // the bar is not visible
		}

		RectangleEdge edge = plot.getRangeAxisEdge();
		float transL0 = (float) rangeAxis.valueToJava2D(barL0L1[0], adjusted,
				edge);
		float transL1 = (float) rangeAxis.valueToJava2D(barL0L1[1], adjusted,
				edge);
		float barL0 = Math.min(transL0, transL1);
		float barLength = Math.abs(transL1 - transL0);

		// draw the bar...
		GeneralPath bar = new GeneralPath();
		Shape top = null;
		if (orientation == PlotOrientation.HORIZONTAL) {
			bar.moveTo((float) (barL0 + getXOffset() / 2), (float) barW0);
			bar.lineTo((float) (barL0 + barLength + getXOffset() / 2),
					(float) barW0);
			Arc2D arc = new Arc2D.Double(barL0 + barLength, barW0,
					getXOffset(), state.getBarWidth(), 90, 180, Arc2D.OPEN);
			bar.append(arc, true);
			bar.lineTo((float) (barL0 + getXOffset() / 2),
					(float) (barW0 + state.getBarWidth()));
			arc = new Arc2D.Double(barL0, barW0, getXOffset(), state
					.getBarWidth(), 270, -180, Arc2D.OPEN);
			bar.append(arc, true);
			bar.closePath();
			if (value > 0) {
				top = new Ellipse2D.Double(barL0 + barLength, barW0,
						getXOffset(), state.getBarWidth());
			}

		} else {
			bar.moveTo((float) barW0, (float) (barL0 - getYOffset() / 2));
			bar.lineTo((float) barW0,
					(float) (barL0 + barLength - getYOffset() / 2));
			Arc2D arc = new Arc2D.Double(barW0,
					(barL0 + barLength - getYOffset()), state.getBarWidth(),
					getYOffset(), 180, 180, Arc2D.OPEN);
			bar.append(arc, true);
			bar.lineTo((float) (barW0 + state.getBarWidth()),
					(float) (barL0 - getYOffset() / 2));
			arc = new Arc2D.Double(barW0, (barL0 - getYOffset()), state
					.getBarWidth(), getYOffset(), 0, -180, Arc2D.OPEN);
			bar.append(arc, true);
			bar.closePath();

			if (value > 0) {
				top = new Ellipse2D.Double(barW0, barL0 - getYOffset(), state
						.getBarWidth(), getYOffset());
			}
		}
		Paint itemPaint = getItemPaint(row, column);
		
//        GradientPaint pBarPainter = new GradientPaint((float) barW0,(float) (barL0 + barLength - getYOffset()), (Color)itemPaint,(float) (barW0+ state.getBarWidth()/ 2),(float) (barL0 + barLength - getYOffset()),new Color(255, 255, 255, 255),true);

        g2.setPaint(itemPaint);
		if (getGradientPaintTransformer() != null
				&& itemPaint instanceof GradientPaint) {
			GradientPaint gp = (GradientPaint) itemPaint;
			itemPaint = getGradientPaintTransformer().transform(gp, bar);
		}
		//g2.setPaint(itemPaint);
		g2.fill(bar);

		if (itemPaint instanceof GradientPaint) {
			g2.setPaint(((GradientPaint) itemPaint).getColor2());
		}
		if (top != null) {
			
			g2.setPaint(((Color) itemPaint).darker());
			//g2.setPaint(PaintAlpha.darker(itemPaint));
			g2.fill(top);
		}

		if (isDrawBarOutline()
				&& state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
			g2.setStroke(getItemOutlineStroke(row, column));
			g2.setPaint(getItemOutlinePaint(row, column));
			g2.draw(bar);
			if (top != null) {
				g2.draw(top);
			}
		}

		CategoryItemLabelGenerator generator = getItemLabelGenerator(row,
				column);
		if (generator != null && isItemLabelVisible(row, column)) {
			drawItemLabel(g2, dataset, row, column, plot, generator, bar
					.getBounds2D(), (value < 0.0));
		}

		// collect entity and tool tip information...
		if (state.getInfo() != null) {
			EntityCollection entities = state.getEntityCollection();
			if (entities != null) {
				String tip = null;
				CategoryToolTipGenerator tipster = getToolTipGenerator(row,
						column);
				if (tipster != null) {
					tip = tipster.generateToolTip(dataset, row, column);
				}
				String url = null;
				if (getItemURLGenerator(row, column) != null) {
					url = getItemURLGenerator(row, column).generateURL(dataset,
							row, column);
				}
				CategoryItemEntity entity = new CategoryItemEntity(bar
						.getBounds2D(), tip, url, dataset, dataset
						.getRowKey(row), dataset.getColumnKey(column));
				entities.add(entity);
			}
		}

	}

	protected void drawItemLabel(Graphics2D g2, CategoryDataset data, int row,
			int column, CategoryPlot plot,
			CategoryItemLabelGenerator generator, Rectangle2D bar,
			boolean negative) {

		String label = generator.generateLabel(data, row, column);
		if (label == null || label.equals("0")) {
			return; // nothing to do
		}

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
		Point2D anchorPoint = calculateLabelAnchorPoint(position
				.getItemLabelAnchor(), bar, plot.getOrientation());

		if (isInternalAnchor(position.getItemLabelAnchor())) {
			Shape bounds = TextUtilities.calculateRotatedStringBounds(label,
					g2, (float) anchorPoint.getX(), (float) anchorPoint.getY(),
					position.getTextAnchor(), position.getAngle(), position
							.getRotationAnchor());

			if (bounds != null) {
				if (!bar.contains(bounds.getBounds2D())) {
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

	/**
	 * Calculates the item label anchor point.
	 * 
	 * @param anchor
	 *            the anchor.
	 * @param bar
	 *            the bar.
	 * @param orientation
	 *            the plot orientation.
	 * 
	 * @return The anchor point.
	 */
	private Point2D calculateLabelAnchorPoint(ItemLabelAnchor anchor,
			Rectangle2D bar, PlotOrientation orientation) {

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
		} else if (anchor == ItemLabelAnchor.INSIDE1) {
			result = new Point2D.Double(x4, y4);
		} else if (anchor == ItemLabelAnchor.INSIDE2) {
			result = new Point2D.Double(x4, y4);
		} else if (anchor == ItemLabelAnchor.INSIDE3) {
			result = new Point2D.Double(x4, y3);
		} else if (anchor == ItemLabelAnchor.INSIDE4) {
			result = new Point2D.Double(x4, y2);
		} else if (anchor == ItemLabelAnchor.INSIDE5) {
			result = new Point2D.Double(x4, y2);
		} else if (anchor == ItemLabelAnchor.INSIDE6) {
			result = new Point2D.Double(x3, y2);
		} else if (anchor == ItemLabelAnchor.INSIDE7) {
			result = new Point2D.Double(x2, y2);
		} else if (anchor == ItemLabelAnchor.INSIDE8) {
			result = new Point2D.Double(x2, y2);
		} else if (anchor == ItemLabelAnchor.INSIDE9) {
			result = new Point2D.Double(x2, y3);
		} else if (anchor == ItemLabelAnchor.INSIDE10) {
			result = new Point2D.Double(x2, y4);
		} else if (anchor == ItemLabelAnchor.INSIDE11) {
			result = new Point2D.Double(x2, y4);
		} else if (anchor == ItemLabelAnchor.INSIDE12) {
			result = new Point2D.Double(x3, y4);
		} else if (anchor == ItemLabelAnchor.OUTSIDE1) {
			result = new Point2D.Double(x5, y6);
		} else if (anchor == ItemLabelAnchor.OUTSIDE2) {
			result = new Point2D.Double(x6, y5);
		} else if (anchor == ItemLabelAnchor.OUTSIDE3) {
			result = new Point2D.Double(x6, y3);
		} else if (anchor == ItemLabelAnchor.OUTSIDE4) {
			result = new Point2D.Double(x6, y1);
		} else if (anchor == ItemLabelAnchor.OUTSIDE5) {
			result = new Point2D.Double(x5, y0);
		} else if (anchor == ItemLabelAnchor.OUTSIDE6) {
			result = new Point2D.Double(x3, y0);
		} else if (anchor == ItemLabelAnchor.OUTSIDE7) {
			result = new Point2D.Double(x1, y0);
		} else if (anchor == ItemLabelAnchor.OUTSIDE8) {
			result = new Point2D.Double(x0, y1);
		} else if (anchor == ItemLabelAnchor.OUTSIDE9) {
			result = new Point2D.Double(x0, y3);
		} else if (anchor == ItemLabelAnchor.OUTSIDE10) {
			result = new Point2D.Double(x0, y5);
		} else if (anchor == ItemLabelAnchor.OUTSIDE11) {
			result = new Point2D.Double(x1, y6);
		} else if (anchor == ItemLabelAnchor.OUTSIDE12) {
			result = new Point2D.Double(x3, y6);
		}

		return result;

	}

}
