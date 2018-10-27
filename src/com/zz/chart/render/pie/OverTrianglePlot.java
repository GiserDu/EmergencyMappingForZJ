package com.zz.chart.render.pie;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlotState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ShapeUtilities;
import org.jfree.util.UnitType;

@SuppressWarnings("serial")
public class OverTrianglePlot extends TPiePlot {

    private double itemSpace;
    private double latestStartX;
    private double itemAngle;

    private boolean isItemSpaceLeft;
    private boolean isItemDirectLeft;

    /**
     * Creates a new plot. The dataset is initially set to <code>null</code>.
     */
    public OverTrianglePlot() {
	this(null);
    }

    /**
     * Creates a plot that will draw a pie chart for the specified dataset.
     * 
     * @param dataset
     *            the dataset (<code>null</code> permitted).
     */
    public OverTrianglePlot(PieDataset dataset) {
	super(dataset);

//	this.itemSpace = 0.0;
	this.latestStartX = 0.0;
	this.itemAngle = 45.0;

	this.isItemSpaceLeft = true;
	this.isItemDirectLeft = true;
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

	if (this.isItemSpaceLeft) {
	    this.itemSpace = pieArea.getHeight() / 10.0;
	} else {
	    this.itemSpace = pieArea.getHeight() / 10.0 * (-1);
	}

	// plot the data (unless the dataset is null)...
	if ((this.dataset != null) && (this.dataset.getKeys().size() > 0)) {

	    List keys = this.dataset.getKeys();
	    double totalValue = DatasetUtilities.calculatePieDatasetTotal(this.dataset);

	    double maxValue = maxValue(this.dataset);
	    int passesRequired = state.getPassesRequired();

	    Shape[] labelArea = new Shape[keys.size()];

	    for (int pass = 0; pass < passesRequired; pass++) {
		for (int section = 0; section < keys.size(); section++) {
		    Number n = this.dataset.getValue(section);
		    if (n != null) {
			double value = n.doubleValue();
			if (value > 0.0) {
			    value = (value * pieArea.getHeight()) / maxValue;
//			    System.out.println(value);
			    labelArea[section] = drawItem(g2, section, value, state, pass);
			}
		    }
		}
	    }
	    if (this.simpleLabels) {
		drawSimpleLabels(g2, keys, totalValue, plotArea, labelArea, state);
	    } else {
		drawLabels(g2, keys, totalValue, plotArea, labelArea, state);
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
	protected Shape drawItem(Graphics2D g2, int section, double value, PiePlotState state, int currentPass) {

	double maxValue = state.getPieArea().getHeight();

	double itemUpLength = value * Math.tan(this.itemAngle * Math.PI / 180.0);

	double startX, startY;
	GeneralPath path = new GeneralPath();
	
	if (this.isItemDirectLeft) {
	    if (0 == section) {
		startX = state.getPieCenterX() - 0.8 * state.getPieWRadius();
	    } else {
		startX = this.getLatestStartX();
	    }
	    startY = state.getPieCenterY() + 0.5 * maxValue;
	    
	    path.moveTo(startX, startY);
	    path.lineTo(startX, startY - value);
	    path.lineTo(startX + itemUpLength, startY - value);
	    
	} else {
	    if (0 == section) {
		startX = state.getPieCenterX() + 0.8 * state.getPieWRadius();
	    } else {
		startX = this.getLatestStartX();
	    }
	    startY = state.getPieCenterY() + 0.5 * maxValue;
	    
	    path.moveTo(startX, startY);
	    path.lineTo(startX, startY - value);
	    path.lineTo(startX - itemUpLength, startY - value);
	}

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
	this.setLatestStartX(startX + this.itemSpace);
	return path;
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

    public void setItemSpace(double distance) {
	if ((distance >= -30.0) && (distance <= 30.0))
	    this.itemSpace = distance;
    }

    public void setLatestStartX(double x) {
	this.latestStartX = x;
    }

    public double getLatestStartX() {
	return this.latestStartX;
    }

    public void setItemAngle(double itemAngle) {
	this.itemAngle = itemAngle;
    }

    /**
     * Draws the pie section labels in the simple form.
     * 
     * @param g2
     *            the graphics device.
     * @param keys
     *            the section keys.
     * @param totalValue
     *            the total value for all sections in the pie.
     * @param plotArea
     *            the plot area.
     * @param pieArea
     *            the area containing the pie.
     * @param state
     *            the plot state.
     * @since 1.0.7
     */
    @SuppressWarnings("unchecked")
	protected void drawSimpleLabels(Graphics2D g2, List keys, double totalValue, Rectangle2D plotArea, Shape[] pieArea,
	    PiePlotState state) {

	Composite originalComposite = g2.getComposite();
	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

	RectangleInsets labelInsets = new RectangleInsets(UnitType.RELATIVE, 0.10, 0.10, 0.10, 0.10);

	@SuppressWarnings("unused")
	double runningTotal = 0.0;
	Iterator iterator = keys.iterator();
	for (int i = 0; i < keys.size(); i++) {
	    Comparable key = (Comparable) iterator.next();

	    Rectangle2D labelsArea = labelInsets.createInsetRectangle(pieArea[i].getBounds2D());

	    boolean include = true;
	    double v = 0.0;
	    Number n = getDataset().getValue(key);
	    if (n == null) {
		include = !getIgnoreNullValues();
	    } else {
		v = n.doubleValue();
		include = getIgnoreZeroValues() ? v > 0.0 : v >= 0.0;
	    }

	    if (include) {
		int x = (int) labelsArea.getMinX();
		int y = (int) labelsArea.getMinY();

		PieSectionLabelGenerator labelGenerator = getLabelGenerator();
		if (labelGenerator == null) {
		    continue;
		}
		String label = labelGenerator.generateSectionLabel(this.dataset, key);
		if (label == null) {
		    continue;
		}
		g2.setFont(this.getLabelFont());
		FontMetrics fm = g2.getFontMetrics();
		Rectangle2D bounds = TextUtilities.getTextBounds(label, g2, fm);
		Rectangle2D out = this.getLabelPadding().createOutsetRectangle(bounds);
		Shape bg = ShapeUtilities.createTranslatedShape(out, x - bounds.getCenterX(), y - bounds.getCenterY());
		if (this.getLabelShadowPaint() != null) {
		    Shape shadow = ShapeUtilities.createTranslatedShape(bg, this.getShadowXOffset(), this
			    .getShadowYOffset());
		    g2.setPaint(this.getLabelShadowPaint());
		    g2.fill(shadow);
		}
		if (this.getLabelBackgroundPaint() != null) {
		    g2.setPaint(this.getLabelBackgroundPaint());
		    g2.fill(bg);
		}
		if (this.getLabelOutlinePaint() != null && this.getLabelOutlineStroke() != null) {
		    g2.setPaint(this.getLabelOutlinePaint());
		    g2.setStroke(this.getLabelOutlineStroke());
		    g2.draw(bg);
		}

		g2.setPaint(this.getLabelPaint());
		g2.setFont(this.getLabelFont());
		TextUtilities.drawAlignedString(getLabelGenerator().generateSectionLabel(getDataset(), key), g2, x, y,
			TextAnchor.CENTER);

	    }
	}
	g2.setComposite(originalComposite);
    }

    /**
     * Draws the labels for the pie sections.
     * 
     * @param g2
     *            the graphics device.
     * @param keys
     *            the keys.
     * @param totalValue
     *            the total value.
     * @param plotArea
     *            the plot area.
     * @param linkArea
     *            the link area.
     * @param state
     *            the state.
     */
    @SuppressWarnings("unchecked")
	protected void drawLabels(Graphics2D g2, List keys, double totalValue, Rectangle2D plotArea, Shape[] linkArea,
	    PiePlotState state) {

	Composite originalComposite = g2.getComposite();
	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

	RectangleInsets labelInsets = new RectangleInsets(UnitType.RELATIVE, 0.10, 0.10, 0.10, 0.10);

	int linkLength = 100;
	Iterator iterator = keys.iterator();
	for (int i = 0; i < keys.size(); i++) {
	    Comparable key = (Comparable) iterator.next();
	    if(null==linkArea[i])
	    {
	    	continue;
	    }
	    Rectangle2D labelsArea = labelInsets.createInsetRectangle(linkArea[i].getBounds2D());

	    boolean include = true;
	    double v = 0.0;
	    Number n = this.dataset.getValue(key);
	    if (n == null) {
		include = !this.ignoreNullValues;
	    } else {
		v = n.doubleValue();
		include = this.getIgnoreZeroValues() ? v > 0.0 : v >= 0.0;
	    }

	    if (include) {
		int x = (int) (labelsArea.getMaxX() + linkLength);
		int y = (int) labelsArea.getMinY();

		PieSectionLabelGenerator labelGenerator = getLabelGenerator();
		if (labelGenerator == null) {
		    continue;
		}
		String label = labelGenerator.generateSectionLabel(this.dataset, key);
		if (label == null) {
		    continue;
		}
		g2.setFont(this.getLabelFont());
		FontMetrics fm = g2.getFontMetrics();
		Rectangle2D bounds = TextUtilities.getTextBounds(label, g2, fm);
		Rectangle2D out = this.getLabelPadding().createOutsetRectangle(bounds);
		Shape bg = ShapeUtilities.createTranslatedShape(out, x - bounds.getCenterX(), y - bounds.getCenterY());

		if (this.getLabelShadowPaint() != null) {
		    Shape shadow = ShapeUtilities.createTranslatedShape(bg, this.getShadowXOffset(), this
			    .getShadowYOffset());
		    g2.setPaint(this.getLabelShadowPaint());
		    g2.fill(shadow);
		}
		if (this.getLabelBackgroundPaint() != null) {
		    g2.setPaint(this.getLabelBackgroundPaint());
		    g2.fill(bg);
		}
		if (this.getLabelOutlinePaint() != null && this.getLabelOutlineStroke() != null) {
		    g2.setPaint(this.getLabelOutlinePaint());
		    g2.setStroke(this.getLabelOutlineStroke());
		    g2.draw(bg);
		    g2.draw(new Line2D.Double(x - linkLength, y, bg.getBounds2D().getMinX(), y));
		}

		g2.setPaint(this.getLabelPaint());
		g2.setFont(this.getLabelFont());
		TextUtilities.drawAlignedString(getLabelGenerator().generateSectionLabel(getDataset(), key), g2, x, y,
			TextAnchor.CENTER);

	    }
	}
	g2.setComposite(originalComposite);
    }
}
