package com.zz.chart.render.pie;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.plot.PiePlotState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;
import org.jfree.util.ShapeUtilities;

@SuppressWarnings("serial")
public class WrinkleRectPlot extends TPiePlot {

    private Point2D.Double point;
    private double shiftAngle;
    private double itemWidth;

    /**
     * Creates a new plot. The dataset is initially set to <code>null</code>.
     */
    public WrinkleRectPlot() {
	this(null);
    }

    /**
     * Creates a plot that will draw a pie chart for the specified dataset.
     * 
     * @param dataset
     *            the dataset (<code>null</code> permitted).
     */
    public WrinkleRectPlot(PieDataset dataset) {
	super(dataset);

	this.point = null;
	this.shiftAngle = 30.0;
	this.itemWidth = 100.0;
    }

    /**
     * Draws the triangle.
     * 
     * @param g2
     *            the graphics device.
     * @param plotArea
     *            the plot area.
     * @param info
     *            chart rendering info.
     */
    @SuppressWarnings("unchecked")
	protected void drawPie(Graphics2D g2, Rectangle2D plotArea, PlotRenderingInfo info) {

	PiePlotState state = initialise(g2, plotArea, this, null, info);

	// adjust the plot area for interior spacing and labels...
	double labelReserve = 0.0;
	if (this.getLabelGenerator() != null && !this.simpleLabels) {
	    labelReserve = this.getLabelGap() + this.getMaximumLabelWidth();
	}
	double gapHorizontal = plotArea.getWidth() * (this.getInteriorGap() + labelReserve) * 2.0;
	double gapVertical = plotArea.getHeight() * this.getInteriorGap() * 2.0;

	if (DEBUG_DRAW_INTERIOR) {
	    double hGap = plotArea.getWidth() * this.getInteriorGap();
	    double vGap = plotArea.getHeight() * this.getInteriorGap();

	    double igx1 = plotArea.getX() + hGap;
	    double igx2 = plotArea.getMaxX() - hGap;
	    double igy1 = plotArea.getY() + vGap;
	    double igy2 = plotArea.getMaxY() - vGap;
	    g2.setPaint(Color.gray);
	    g2.draw(new Rectangle2D.Double(igx1, igy1, igx2 - igx1, igy2 - igy1));
	}

	double linkX = plotArea.getX() + gapHorizontal / 2;
	double linkY = plotArea.getY() + gapVertical / 2;
	double linkW = plotArea.getWidth() - gapHorizontal;
	double linkH = plotArea.getHeight() - gapVertical;

	// make the link area a square if the pie chart is to be circular...
	if (this.isCircular()) {
	    double min = Math.min(linkW, linkH) / 2;
	    linkX = (linkX + linkX + linkW) / 2 - min;
	    linkY = (linkY + linkY + linkH) / 2 - min;
	    linkW = 2 * min;
	    linkH = 2 * min;
	}

	// the link area defines the dog leg points for the linking lines to
	// the labels
	Rectangle2D linkArea = new Rectangle2D.Double(linkX, linkY, linkW, linkH);
	state.setLinkArea(linkArea);

	if (DEBUG_DRAW_LINK_AREA) {
	    g2.setPaint(Color.blue);
	    g2.draw(linkArea);
	    g2.setPaint(Color.yellow);
	    g2.draw(new Ellipse2D.Double(linkArea.getX(), linkArea.getY(), linkArea.getWidth(), linkArea.getHeight()));
	}

	// the explode area defines the max circle/ellipse for the exploded
	// pie sections. it is defined by shrinking the linkArea by the
	// linkMargin factor.
	double lm = 0.0;
	if (!this.simpleLabels) {
	    lm = this.getLabelLinkMargin();
	}
	double hh = linkArea.getWidth() * lm * 2.0;
	double vv = linkArea.getHeight() * lm * 2.0;
	Rectangle2D explodeArea = new Rectangle2D.Double(linkX + hh / 2.0, linkY + vv / 2.0, linkW - hh, linkH - vv);

	state.setExplodedPieArea(explodeArea);

	// the pie area defines the circle/ellipse for regular pie sections.
	// it is defined by shrinking the explodeArea by the explodeMargin
	// factor.
	double maximumExplodePercent = getMaximumExplodePercent();
	double percent = maximumExplodePercent / (1.0 + maximumExplodePercent);

	double h1 = explodeArea.getWidth() * percent;
	double v1 = explodeArea.getHeight() * percent;
	Rectangle2D pieArea = new Rectangle2D.Double(explodeArea.getX() + h1 / 2.0, explodeArea.getY() + v1 / 2.0,
		explodeArea.getWidth() - h1, explodeArea.getHeight() - v1);

	if (DEBUG_DRAW_PIE_AREA) {
	    g2.setPaint(Color.green);
	    g2.draw(pieArea);
	}
	state.setPieArea(pieArea);
	state.setPieCenterX(pieArea.getCenterX());
	state.setPieCenterY(pieArea.getCenterY());
	state.setPieWRadius(pieArea.getWidth() / 2.0);
	state.setPieHRadius(pieArea.getHeight() / 2.0);

	this.itemWidth = pieArea.getWidth() / 7.0;
	// plot the data (unless the dataset is null)...
	if ((this.dataset != null) && (this.dataset.getKeys().size() > 0)) {

	    List keys = this.dataset.getKeys();
	    double totalValue = DatasetUtilities.calculatePieDatasetTotal(this.dataset);

	    double maxValue = maxValue(this.dataset);
	    int passesRequired = state.getPassesRequired();

	    for (int pass = 0; pass < passesRequired; pass++) {
	    	for (int section = 0; section < keys.size(); section++) {
			    Number n = this.dataset.getValue(section);
			    double value = 0.0;
			    if (n != null) {
				value = n.doubleValue();
			    }

			    value = (value * pieArea.getHeight()) / maxValue;
			    drawItem(g2, section, value, state, pass);
			}
	    }
	    if (this.simpleLabels) {
		drawSimpleLabels(g2, keys, totalValue, plotArea, linkArea, state);
	    } else {
		drawLabels(g2, keys, totalValue, plotArea, linkArea, state);
	    }

	} else {
	    drawNoDataMessage(g2, plotArea);
	}
    }

    /**
     * Draws a single data item.
     * 
     * @param g2
     *            the graphics device (<code>null</code> not permitted).
     * @param section
     *            the section index.
     * @param dataArea
     *            the data plot area.
     * @param state
     *            state information for one chart.
     * @param currentPass
     *            the current pass index.
     */
    @SuppressWarnings("unchecked")
	protected void drawItem(Graphics2D g2, int section, double value, PiePlotState state, int currentPass) {

	double startX = 0.0;
	double startY = 0.0;
	int sectionCount = this.dataset.getKeys().size();

	double angle = 0.0;
	if (0 == (section % 2))
	    angle = this.shiftAngle;
	else
	    angle = 180.0 - this.shiftAngle;

	double xStartOffset = sectionCount * this.itemWidth / 2.0;
	double itemXOffset = this.itemWidth;
	double itemYOffset = this.itemWidth * Math.tan(angle * Math.PI / 180.0);

	if (0 == section) {
	    startX = state.getPieCenterX() - xStartOffset;
	    startY = state.getPieCenterY() + 0.5 * state.getPieArea().getHeight();
	} else {
	    startX = this.getLatestPoint().getX();
	    startY = this.getLatestPoint().getY();
	}

	GeneralPath path = new GeneralPath();
	path.moveTo(startX, startY);
	path.lineTo(startX, startY - value);
	path.lineTo(startX + itemXOffset, startY - value - itemYOffset);
	path.lineTo(startX + itemXOffset, startY - itemYOffset);
	path.closePath();

	if (currentPass == 0) {
	    if (this.getShadowPaint() != null) {
		Shape shadowArc = ShapeUtilities.createTranslatedShape(path, (float) this.getShadowXOffset(),
			(float) this.getShadowYOffset());
		g2.setPaint(this.getShadowPaint());
		g2.fill(shadowArc);
	    }
	} else if (currentPass == 1) {
	    Comparable key = getSectionKey(section);
	    Paint paint = lookupSectionPaint(key);
	    g2.setPaint(paint);
	    g2.fill(path);

	    Paint outlinePaint = lookupSectionOutlinePaint(key);
	    Stroke outlineStroke = lookupSectionOutlineStroke(key);
	    if (this.isSectionOutlinesVisible()) {
		g2.setPaint(outlinePaint);
		g2.setStroke(outlineStroke);
		g2.draw(path);
	    }

	    // update the linking line target for later
	    // add an entity for the pie section
	    if (state.getInfo() != null) {
		EntityCollection entities = state.getEntityCollection();
		if (entities != null) {
		    String tip = null;
		    if (this.getToolTipGenerator() != null) {
			tip = this.getToolTipGenerator().generateToolTip(this.dataset, key);
		    }
		    String url = null;
		    if (this.getUrlGenerator() != null) {
			url = this.getUrlGenerator().generateURL(this.dataset, key, this.getPieIndex());
		    }
		    PieSectionEntity entity = new PieSectionEntity(path, this.dataset, this.getPieIndex(), section,
			    key, tip, url);
		    entities.add(entity);
		}
	    }
	}
	Point2D.Double point = new Point2D.Double(startX + itemXOffset, startY - itemYOffset);
	this.setLatestPoint(point);
    }

    /**
     * return the max value of the dataset.
     * 
     * @param data
     * @return
     */
    public double maxValue(PieDataset dataset) {
	int sizeCount = dataset.getKeys().size();
	double maxValue = dataset.getValue(0).doubleValue();

	for (int section = 0; section < sizeCount; section++) {
	    Number n = dataset.getValue(section);
	    if (n != null && (n.doubleValue() > maxValue)) {
		maxValue = n.doubleValue();
	    }
	}
	return maxValue;
    }

    /**
     * set the start point of each item.
     * 
     * @param point
     */
    public void setLatestPoint(Point2D.Double point) {
	this.point = point;
    }

    /**
     * get the start point of the item.
     * 
     * @return
     */
    public Point2D.Double getLatestPoint() {
	return this.point;
    }

    /**
     * set the angle of scope of the item.
     * 
     * @param angle
     */
    public void setShiftAngle(double angle) {
	this.shiftAngle = angle;
    }

    /**
     * set the item width
     * 
     * @param width
     */
    public void setItemWidth(double width) {
	this.itemWidth = width;
    }
}
