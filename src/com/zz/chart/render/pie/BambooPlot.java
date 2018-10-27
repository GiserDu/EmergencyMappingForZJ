package com.zz.chart.render.pie;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
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
public class BambooPlot extends TPiePlot {

    private Point2D.Double originPoint;
    private Point2D.Double startPoint;

    private double itemGap;
    private double itemBottomLength;

    private double itemShiftAngle;
    private double latestLAngle;

    private boolean isRound;
    private boolean isLengend;

    private Point2D.Double legendLUP;
    private Point2D.Double legendRBP;

    /**
     * Creates a new plot. The dataset is initially set to <code>null</code>.
     */
    public BambooPlot() {
	this(null);
    }

    /**
     * Creates a plot that will draw a pie chart for the specified dataset.
     * 
     * @param dataset
     *            the dataset (<code>null</code> permitted).
     */
    public BambooPlot(PieDataset dataset) {
	super(dataset);

	this.itemGap = 0.0;
	this.itemBottomLength = 0.0;

	this.itemShiftAngle = 15.0;
	this.latestLAngle = 0.0;

	this.originPoint = null;
	this.startPoint = null;

	this.isRound = true;
	this.isLengend = false;

	this.legendLUP = null;
	this.legendRBP = null;
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

	this.itemBottomLength = pieArea.getWidth() / 10.0;
	this.itemGap = this.itemBottomLength / 2;
	
	// the origin point of the plot
	Point2D.Double point = new Point2D.Double(state.getPieCenterX(), state.getPieCenterY() + 0.5
		* pieArea.getHeight());
	this.setOriginPoint(point);

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
			    value = value * (pieArea.getHeight() / maxValue);
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

	double sectionNums = this.dataset.getKeys().size();
	double startX = 0.0;
	double startY = 0.0;

	double startXOff = (sectionNums / 2) * this.itemBottomLength + (sectionNums / 2 - 0.5) * this.itemGap;
	double leftAngle = 0.0;
	double rightAngle = 0.0;

	if (0 == section) {
	    startX = this.originPoint.getX() - startXOff;
	    startY = this.originPoint.getY();

	    leftAngle = (sectionNums / 2) * this.itemShiftAngle;
	    rightAngle = (sectionNums / 2 - 1) * this.itemShiftAngle;
	} else {
	    startX = this.getLatestStartPoint().getX();
	   
	    startY = this.getLatestStartPoint().getY();

	    leftAngle = this.latestLAngle - this.itemShiftAngle;
	    rightAngle = this.latestLAngle - 2 * this.itemShiftAngle;
	}

	double xLOffset = value * Math.tan(leftAngle * Math.PI / 180.0);
	double xROffset = value * Math.tan(rightAngle * Math.PI / 180.0);

	double itemUpLength = this.itemBottomLength + xLOffset - xROffset;
	Rectangle2D arcArea = new Rectangle2D.Double(startX - xLOffset, startY - value - itemUpLength / 4,
		itemUpLength, itemUpLength / 2);

	Rectangle2D arcBounds = getArcBounds(arcArea, arcArea, 0.0, 360.0, 0.0);
	Arc2D.Double arc = new Arc2D.Double(arcBounds, 180.0, 180.0, Arc2D.OPEN);
	Arc2D.Double eclipse = new Arc2D.Double(arcBounds, 0.0, 360.0, Arc2D.PIE);

	GeneralPath path = new GeneralPath();
	if (this.isRound) {
	    path.append(arc.getPathIterator(null), false);
	    path.lineTo(startX + this.itemBottomLength, startY);
	    path.lineTo(startX, startY);
	    path.lineTo(startX - xLOffset, startY - value);
	} else {
	    path.moveTo(startX, startY);
	    path.lineTo(startX - xLOffset, startY - value);
	    path.lineTo(startX + this.itemBottomLength - xROffset, startY - value);
	    path.lineTo(startX + this.itemBottomLength, startY);
	}
	path.closePath();

	if (currentPass == 0) {
	    if (this.getShadowPaint() != null) {
		Shape shadowArc = ShapeUtilities.createTranslatedShape(path, (float) this.getShadowXOffset(),
			(float) this.getShadowYOffset());
		g2.setPaint(this.getShadowPaint());
		g2.fill(shadowArc);
		if (this.isRound) {
		    Shape shadowEclipe = ShapeUtilities.createTranslatedShape(eclipse, (float) this.getShadowXOffset(),
			    (float) this.getShadowYOffset());
		    g2.fill(shadowEclipe);
		}
	    }
	} else if (currentPass == 1) {
	    Comparable key = getSectionKey(section);
	    Paint paint = lookupSectionPaint(key);
	    g2.setPaint(paint);
	    if (this.isRound) {	
		g2.fill(eclipse);
	    }
	    g2.fill(path);
	    
	    Paint outlinePaint = lookupSectionOutlinePaint(key);
	    Stroke outlineStroke = lookupSectionOutlineStroke(key);
	    if (this.isSectionOutlinesVisible()) {
		g2.setPaint(outlinePaint);
		g2.setStroke(outlineStroke);
		g2.draw(path);
		if (this.isRound) {
		    g2.draw(eclipse);
		}
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

	if (this.isLengend && (1 == currentPass)) {
	    double height = state.getPieHRadius() * 2;
	    if ((section == 0))
		this.legendLUP = new Point2D.Double(arc.getStartPoint().getX(), arc.getStartPoint().getY());

	    if (section == (sectionNums - 1)) {
		this.legendRBP = new Point2D.Double(arc.getEndPoint().getX(), this.originPoint.getY());
		double maxValue = maxValue(this.dataset);
//		System.out.println(maxValue);		
		int n = (int) (maxValue / 5);

		g2.setPaint(Color.GRAY);
		g2.drawLine((int) legendRBP.x + 20, (int) legendRBP.y, (int) legendRBP.x + 20, (int) (legendRBP.y
			- height - 20));
		for (int i = 0; i <= 5; i++) {
		    g2.drawLine((int)(legendRBP.x + 20), (int)(legendRBP.y - i * height / 5), (int) (legendRBP.x + 22),
			    (int)(legendRBP.y - i * height / 5));

		    String str = String.valueOf(i * n);
		    g2.setFont(new Font("黑体", Font.PLAIN, 15));
		    g2.drawString(str, (int) (legendRBP.x + 30), (int)(legendRBP.y - i * height / 5 + 4));
		}
		g2.drawLine((int) legendRBP.x + 20, (int) legendRBP.y, (int) legendLUP.x - 20, (int) legendRBP.y);
	    }
	}
	Point2D.Double point = new Point2D.Double(startX + this.itemBottomLength + this.itemGap, startY);

	this.setLatestStartPoint(point);
	this.setLatestLAngle(leftAngle);
    }

    /**
     * Draws the labels for the pie sections.
     *
     * @param g2  the graphics device.
     * @param keys  the keys.
     * @param totalValue  the total value.
     * @param plotArea  the plot area.
     * @param linkArea  the link area.
     * @param state  the state.
     */
    @SuppressWarnings("unchecked")
	protected void drawLabels(Graphics2D g2, List keys, double totalValue,
                              Rectangle2D plotArea, Rectangle2D linkArea,
                              PiePlotState state) {

        
    }

    /**
     * return the max value of the dataset.
     * 
     * @param data
     * @return
     */
    public double maxValue(PieDataset data) {
	int sizeCount = dataset.getKeys().size();
	double maxValue = dataset.getValue(0).doubleValue();

	for (int section = 0; section < sizeCount; section++) {
	    Number n = this.dataset.getValue(section);
	    if (n != null && (n.doubleValue() > maxValue)) {
		maxValue = n.doubleValue();
	    }
	}
	return maxValue;
    }

    /**
     * set the origin point of the plot.
     * 
     * @param point
     */
    private void setOriginPoint(Point2D.Double point) {
	this.originPoint = point;
    }

    /**
     * get the start point of each item.
     * 
     * @return
     */
    private Point2D.Double getLatestStartPoint() {
	return this.startPoint;
    }

    /**
     * set the latest start point.
     * 
     * @param point
     */
    private void setLatestStartPoint(Point2D.Double point) {
	this.startPoint = point;
    }

    /**
     * set the angle between the left side and the right side of each item
     * 
     * @param angle
     */
    public void setItemShiftAngle(double angle) {
	this.itemShiftAngle = angle;
    }

    /**
     * set the left side angle of the latest item;
     * 
     * @param angle
     */
    private void setLatestLAngle(double angle) {
	this.latestLAngle = angle;
    }

    /**
     * set the bottom width of each item
     * 
     * @param length
     */
    public void setItemBottomWidth(double length) {
	this.itemBottomLength = length;
    }

    /**
     * set the horizontal distance between items
     * 
     * @param gap
     */
    public void setItemGap(double gap) {
	this.itemGap = gap;
    }

    public void setIsItemRound(boolean bool) {
	this.isRound = bool;
    }
    
    public void setIsLegend(boolean bool) {
	this.isLengend = bool;
    } 
}
