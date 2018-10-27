package com.zz.chart.render.pie;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.chart.plot.PiePlotState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.urls.PieURLGenerator;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintUtilities;
import org.jfree.util.Rotation;
import org.jfree.util.ShapeUtilities;
import org.jfree.util.UnitType;


/**
 * A customised pie plot that leaves a hole in the middle.
 */
public class HalfRingPlot extends TRingPlot {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3288265580602203371L;

	/**
     * A flag that controls whether or not separators are drawn between the
     * sections of the chart.
     */
    private boolean separatorsVisible;

    /** The stroke used to draw separators. */
    private transient Stroke separatorStroke;

    /** The paint used to draw separators. */
    private transient Paint separatorPaint;

    /**
     * The length of the inner separator extension (as a percentage of the
     * depth of the sections).
     */
    private double innerSeparatorExtension;

    /**
     * The length of the outer separator extension (as a percentage of the
     * depth of the sections).
     */
    private double outerSeparatorExtension;

    /**
     * The depth of the section as a percentage of the diameter.
     */
    private double sectionDepth;

    private Paint totalPaint ;
    
    
    /**
     * 是否绘制环下的整个半圆环
     */
    private boolean bDrawTotalHRing = false;
    
    private boolean bDrawTotalLabel = false;
    
    private boolean bDrawLabel = false;
    
    /** A number formatter for the value. */
    private NumberFormat numberFormat;

    /** A number formatter for the percentage. */
    private NumberFormat percentFormat;
    
    /**
     * Creates a new plot with a <code>null</code> dataset.
     */
    public HalfRingPlot() {
        this(null);
    }

    /**
     * Creates a new plot for the specified dataset.
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     */
    public HalfRingPlot(PieDataset dataset) {
        super(dataset);
        
		//hw-2013-3-4
//        this.separatorsVisible = true;
        this.separatorsVisible = false;
        
        this.separatorStroke = new BasicStroke(0.5f);
        this.separatorPaint = Color.yellow;
        this.innerSeparatorExtension = 0.20;  // twenty percent
        this.outerSeparatorExtension = 0.20;  // twenty percent
        this.sectionDepth = 0.20; // 20%
        this.totalPaint = new Color(239,251,242);
        this.setStartAngle(0);
        
        NumberFormat numberFormat =  NumberFormat.getNumberInstance();
		numberFormat.setGroupingUsed(false);
		this.numberFormat = numberFormat;
    }

    /**
     * Returns a flag that indicates whether or not separators are drawn between
     * the sections in the chart.
     *
     * @return A boolean.
     *
     * @see #setSeparatorsVisible(boolean)
     */
    public boolean getSeparatorsVisible() {
        return this.separatorsVisible;
    }

    /**
     * Sets the flag that controls whether or not separators are drawn between
     * the sections in the chart, and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param visible  the flag.
     *
     * @see #getSeparatorsVisible()
     */
    public void setSeparatorsVisible(boolean visible) {
        this.separatorsVisible = visible;
        fireChangeEvent();
    }

    /**
     * Returns the separator stroke.
     *
     * @return The stroke (never <code>null</code>).
     *
     * @see #setSeparatorStroke(Stroke)
     */
    public Stroke getSeparatorStroke() {
        return this.separatorStroke;
    }

    /**
     * Sets the stroke used to draw the separator between sections and sends
     * a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param stroke  the stroke (<code>null</code> not permitted).
     *
     * @see #getSeparatorStroke()
     */
    public void setSeparatorStroke(Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.separatorStroke = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the separator paint.
     *
     * @return The paint (never <code>null</code>).
     *
     * @see #setSeparatorPaint(Paint)
     */
    public Paint getSeparatorPaint() {
        return this.separatorPaint;
    }

    /**
     * Sets the paint used to draw the separator between sections and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param paint  the paint (<code>null</code> not permitted).
     *
     * @see #getSeparatorPaint()
     */
    public void setSeparatorPaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.separatorPaint = paint;
        fireChangeEvent();
    }

    /**
     * Returns the length of the inner extension of the separator line that
     * is drawn between sections, expressed as a percentage of the depth of
     * the section.
     *
     * @return The inner separator extension (as a percentage).
     *
     * @see #setInnerSeparatorExtension(double)
     */
    public double getInnerSeparatorExtension() {
        return this.innerSeparatorExtension;
    }

    /**
     * Sets the length of the inner extension of the separator line that is
     * drawn between sections, as a percentage of the depth of the
     * sections, and sends a {@link PlotChangeEvent} to all registered
     * listeners.
     *
     * @param percent  the percentage.
     *
     * @see #getInnerSeparatorExtension()
     * @see #setOuterSeparatorExtension(double)
     */
    public void setInnerSeparatorExtension(double percent) {
        this.innerSeparatorExtension = percent;
        fireChangeEvent();
    }

    /**
     * Returns the length of the outer extension of the separator line that
     * is drawn between sections, expressed as a percentage of the depth of
     * the section.
     *
     * @return The outer separator extension (as a percentage).
     *
     * @see #setOuterSeparatorExtension(double)
     */
    public double getOuterSeparatorExtension() {
        return this.outerSeparatorExtension;
    }

    /**
     * Sets the length of the outer extension of the separator line that is
     * drawn between sections, as a percentage of the depth of the
     * sections, and sends a {@link PlotChangeEvent} to all registered
     * listeners.
     *
     * @param percent  the percentage.
     *
     * @see #getOuterSeparatorExtension()
     */
    public void setOuterSeparatorExtension(double percent) {
        this.outerSeparatorExtension = percent;
        fireChangeEvent();
    }

    /**
     * Returns the depth of each section, expressed as a percentage of the
     * plot radius.
     *
     * @return The depth of each section.
     *
     * @see #setSectionDepth(double)
     * @since 1.0.3
     */
    public double getSectionDepth() {
        return this.sectionDepth;
    }

    /**
     * The section depth is given as percentage of the plot radius.
     * Specifying 1.0 results in a straightforward pie chart.
     *
     * @param sectionDepth  the section depth.
     *
     * @see #getSectionDepth()
     * @since 1.0.3
     */
    public void setSectionDepth(double sectionDepth) {
        this.sectionDepth = sectionDepth;
        fireChangeEvent();
    }

    /**
     * Initialises the plot state (which will store the total of all dataset
     * values, among other things).  This method is called once at the
     * beginning of each drawing.
     *
     * @param g2  the graphics device.
     * @param plotArea  the plot area (<code>null</code> not permitted).
     * @param plot  the plot.
     * @param index  the secondary index (<code>null</code> for primary
     *               renderer).
     * @param info  collects chart rendering information for return to caller.
     *
     * @return A state object (maintains state information relevant to one
     *         chart drawing).
     */
    public PiePlotState initialise(Graphics2D g2, Rectangle2D plotArea,
            TPiePlot plot, Integer index, PlotRenderingInfo info) {

        PiePlotState state = super.initialise(g2, plotArea, plot, index, info);
        state.setPassesRequired(3);
        return state;

    }

    /**
     * Draws the plot on a Java 2D graphics device (such as the screen or a
     * printer).
     *
     * @param g2  the graphics device.
     * @param area  the area within which the plot should be drawn.
     * @param anchor  the anchor point (<code>null</code> permitted).
     * @param parentState  the state from the parent plot, if there is one.
     * @param info  collects info about the drawing
     *              (<code>null</code> permitted).
     */
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor,
                     PlotState parentState, PlotRenderingInfo info) {

        // adjust for insets...
        RectangleInsets insets = getInsets();
        insets.trim(area);

        if (info != null) {
            info.setPlotArea(area);
            info.setDataArea(area);
        }

        drawBackground(g2, area);
        drawOutline(g2, area);

        Shape savedClip = g2.getClip();
        g2.clip(area);

        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                getForegroundAlpha()));

        if (!DatasetUtilities.isEmptyOrNull(this.dataset)) {
            drawPie(g2, area, info);
        }
        else {
            drawNoDataMessage(g2, area);
        }

        g2.setClip(savedClip);
        g2.setComposite(originalComposite);

        drawOutline(g2, area);

    }

    /**
     * Draws the pie.
     *
     * @param g2  the graphics device.
     * @param plotArea  the plot area.
     * @param info  chart rendering info.
     */
    @SuppressWarnings("unchecked")
	protected void drawPie(Graphics2D g2, Rectangle2D plotArea,
                           PlotRenderingInfo info) {

        PiePlotState state = initialise(g2, plotArea, this, null, info);

        // adjust the plot area for interior spacing and labels...
        double labelReserve = 0.0;
        if (this.getLabelGenerator() != null && !this.simpleLabels) {
            labelReserve = this.getLabelGap() + this.getMaximumLabelWidth();
        }
        double gapHorizontal = plotArea.getWidth() * (this.getInteriorGap()
                + labelReserve) * 2.0;
        double gapVertical = plotArea.getHeight() * this.getInteriorGap() * 2.0;


        if (DEBUG_DRAW_INTERIOR) {
            double hGap = plotArea.getWidth() * this.getInteriorGap();
            double vGap = plotArea.getHeight() * this.getInteriorGap();

            double igx1 = plotArea.getX() + hGap;
            double igx2 = plotArea.getMaxX() - hGap;
            double igy1 = plotArea.getY() + vGap;
            double igy2 = plotArea.getMaxY() - vGap;
            g2.setPaint(Color.gray);
            g2.draw(new Rectangle2D.Double(igx1, igy1, igx2 - igx1,
                    igy2 - igy1));
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
        Rectangle2D linkArea = new Rectangle2D.Double(linkX, linkY, linkW,
                linkH);
        state.setLinkArea(linkArea);

        if (DEBUG_DRAW_LINK_AREA) {
            g2.setPaint(Color.blue);
            g2.draw(linkArea);
            g2.setPaint(Color.yellow);
            g2.draw(new Ellipse2D.Double(linkArea.getX(), linkArea.getY(),
                    linkArea.getWidth(), linkArea.getHeight()));
        }

        // the explode area defines the max circle/ellipse for the exploded
        // pie sections.  it is defined by shrinking the linkArea by the
        // linkMargin factor.
        double lm = 0.0;
        if (!this.simpleLabels) {
            lm = this.getLabelLinkMargin();
        }
        double hh = linkArea.getWidth() * lm * 2.0;
        double vv = linkArea.getHeight() * lm * 2.0;
        Rectangle2D explodeArea = new Rectangle2D.Double(linkX + hh / 2.0,
                linkY + vv / 2.0, linkW - hh, linkH - vv);

        state.setExplodedPieArea(explodeArea);

        // the pie area defines the circle/ellipse for regular pie sections.
        // it is defined by shrinking the explodeArea by the explodeMargin
        // factor.
        double maximumExplodePercent = getMaximumExplodePercent();
        double percent = maximumExplodePercent / (1.0 + maximumExplodePercent);

        double h1 = explodeArea.getWidth() * percent;
        double v1 = explodeArea.getHeight() * percent;
        Rectangle2D pieArea = new Rectangle2D.Double(explodeArea.getX()
                + h1 / 2.0, explodeArea.getY() + v1 / 2.0,
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

        // plot the data (unless the dataset is null)...
        if ((this.dataset != null) && (this.dataset.getKeys().size() > 0)) {

            List keys = this.dataset.getKeys();
            double totalValue = DatasetUtilities.calculatePieDatasetTotal(
                    this.dataset);

            int passesRequired = state.getPassesRequired();
            for (int pass = 0; pass < passesRequired; pass++) {
                double runningTotal = 0.0;
                state.setLatestAngle(this.getStartAngle());
                for (int section = 0; section < keys.size(); section++) {
                    Number n = this.dataset.getValue(section);
                    if (n != null) {
                        double value = n.doubleValue();
                        if (value > 0.0) {
                            runningTotal += value;
                            drawItem(g2, section, explodeArea, state, pass);
                        }
                    }
                }
            }
            if (this.bDrawLabel) {
            	if (this.getSimpleLabels()) {
                    drawSimpleLabels(g2, keys, totalValue, plotArea, linkArea,
                            state);
                }
                else {
                    drawLabels(g2, keys, totalValue, plotArea, linkArea, state);
                }
			}
            
            
            if (this.isBDrawTotalHRing()) {
				this.drawTotalHRing(g2,state);
			}
        }
        else {
            drawNoDataMessage(g2, plotArea);
        }
    }
    
    private void drawTotalHRing(Graphics2D g2,PiePlotState state) {
    	// create the bounds for the inner arc
    	 Rotation direction = getDirection();
    	 double angleStart = this.getStartAngle(),angle = 0;
         if (direction == Rotation.CLOCKWISE)
         {
        	 angle = -180;
         }
         if (direction == Rotation.ANTICLOCKWISE) {
			angle= 180;
		}
        
        Rectangle2D arcBounds = getArcBounds(state.getPieArea(),state.getExplodedPieArea(), angleStart, angle, 0);
             
        double depth = this.sectionDepth / 2.0;
        RectangleInsets s = new RectangleInsets(UnitType.RELATIVE, depth, depth, depth, depth);
        Rectangle2D innerArcBounds = new Rectangle2D.Double();
        innerArcBounds.setRect(arcBounds);
        s.trim(innerArcBounds);
             
        Arc2D.Double arc = new Arc2D.Double(innerArcBounds,angleStart, angle, Arc2D.PIE);
        g2.setPaint(this.getTotalPaint());
        g2.fill(arc);
        g2.setPaint(this.getOutlinePaint());
        g2.setStroke(this.getBaseSectionOutlineStroke());
        g2.draw(arc);
        
        if (this.isBDrawTotalLabel()) {
        	Rectangle2D totalArea = arc.getBounds2D();
        	this.drawTotalLabel(g2,totalArea);
		}
	}
    
    private void drawTotalLabel(Graphics2D g2,Rectangle2D totalArea) {
    	Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));

        RectangleInsets labelInsets = new RectangleInsets(UnitType.RELATIVE,0.18, 0.18, 0.18, 0.18);
        Rectangle2D labelsArea = labelInsets.createInsetRectangle(totalArea);

        PieSectionLabelGenerator labelGenerator = getLabelGenerator();
        if (labelGenerator == null) {return;}
        
        double total = calculatePieDatasetTotal(dataset);

        String label = this.numberFormat.format(total);
        
        if (label == null) {return;}
        
        double x = labelsArea.getCenterX();
        double y = labelsArea.getCenterY();
        g2.setPaint(this.getLabelPaint());
        
        Font font = this.getLabelFont();
        FontRenderContext frc = g2.getFontRenderContext();
        Rectangle2D rectangle2D = font.getStringBounds(label, frc);
        //这里是关键，因为仿射变换之后这里的font也会有影响，必须把x平移该字符宽度的一半
		AffineTransform ATYInvert = new AffineTransform(-1.0d, 0.0d, 0.0d, 1.0d,rectangle2D.getWidth()/2, 0.0d);
		font = font.deriveFont(ATYInvert);
        
        g2.setFont(font);
        
        
		TextUtilities.drawAlignedString(label, g2,(float)x, (float)y,TextAnchor.CENTER_LEFT);
        
        g2.setComposite(originalComposite);

	}
    
    /**
     * Calculates the total of all the values in a {@link PieDataset}.  If
     * the dataset contains negative or <code>null</code> values, they are
     * ignored.
     *
     * @param dataset  the dataset (<code>null</code> not permitted).
     *
     * @return The total.
     */
    @SuppressWarnings("unchecked")
	public  double calculatePieDatasetTotal(PieDataset dataset) {
        if (dataset == null) {
            throw new IllegalArgumentException("Null 'dataset' argument.");
        }
        List keys = dataset.getKeys();
        double totalValue = 0;
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            Comparable current = (Comparable) iterator.next();
            if (current != null) {
                Number value = dataset.getValue(current);
                double v = 0.0;
                if (value != null) {
                    v = Double.parseDouble(this.numberFormat.format(value));
                }
                if (v > 0) {
                    totalValue = totalValue + v;
                }
            }
        }
        return totalValue;
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

        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                1.0f));

        // classify the keys according to which side the label will appear...
        DefaultKeyedValues leftKeys = new DefaultKeyedValues();
        DefaultKeyedValues rightKeys = new DefaultKeyedValues();

        double runningTotal = 0.0;
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            Comparable key = (Comparable) iterator.next();
            boolean include = true;
            double v = 0.0;
            Number n = this.dataset.getValue(key);
            if (n == null) {
                include = !this.ignoreNullValues;
            }
            else {
                v = n.doubleValue();
                include = this.getIgnoreZeroValues() ? v > 0.0 : v >= 0.0;
            }

            if (include) {
                runningTotal = runningTotal + v;
                // work out the mid angle (0 - 90 and 270 - 360) = right,
                // otherwise left
                double mid = this.getStartAngle() + (this.getDirection().getFactor()
                        * ((runningTotal - v / 2.0) * 180) / totalValue);
                if (Math.cos(Math.toRadians(mid)) < 0.0) {
                    leftKeys.addValue(key, new Double(mid));
                }
                else {
                    rightKeys.addValue(key, new Double(mid));
                }
            }
        }

        g2.setFont(getLabelFont());

        // calculate the max label width from the plot dimensions, because
        // a circular pie can leave a lot more room for labels...
        double marginX = plotArea.getX() + this.getInteriorGap()
                * plotArea.getWidth();
        double gap = plotArea.getWidth() * this.getLabelGap();
        double ww = linkArea.getX() - gap - marginX;
        float labelWidth = (float) this.getLabelPadding().trimWidth(ww);

        // draw the labels...
        if (this.getLabelGenerator() != null) {
            drawLeftLabels(leftKeys, g2, plotArea, linkArea, labelWidth,
                    state);
            drawRightLabels(rightKeys, g2, plotArea, linkArea, labelWidth,
                    state);
        }
        g2.setComposite(originalComposite);

    }
    
    /**
     * Draws the pie section labels in the simple form.
     *
     * @param g2  the graphics device.
     * @param keys  the section keys.
     * @param totalValue  the total value for all sections in the pie.
     * @param plotArea  the plot area.
     * @param pieArea  the area containing the pie.
     * @param state  the plot state.
     *
     * @since 1.0.7
     */
    @SuppressWarnings("unchecked")
	protected void drawSimpleLabels(Graphics2D g2, List keys,
            double totalValue, Rectangle2D plotArea, Rectangle2D pieArea,
            PiePlotState state) {

        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                1.0f));

        RectangleInsets labelInsets = new RectangleInsets(UnitType.RELATIVE,
                0.18, 0.18, 0.18, 0.18);
        Rectangle2D labelsArea = labelInsets.createInsetRectangle(pieArea);
        double runningTotal = 0.0;
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            Comparable key = (Comparable) iterator.next();
            boolean include = true;
            double v = 0.0;
            Number n = getDataset().getValue(key);
            if (n == null) {
                include = !getIgnoreNullValues();
            }
            else {
                v = n.doubleValue();
                include = getIgnoreZeroValues() ? v > 0.0 : v >= 0.0;
            }

            if (include) {
                runningTotal = runningTotal + v;
                // work out the mid angle (0 - 90 and 270 - 360) = right,
                // otherwise left
                double mid = getStartAngle() + (getDirection().getFactor()
                        * ((runningTotal - v / 2.0) * 180) / totalValue);

                Arc2D arc = new Arc2D.Double(labelsArea, getStartAngle(),
                        mid - getStartAngle(), Arc2D.OPEN);
                int x = (int) arc.getEndPoint().getX();
                int y = (int) arc.getEndPoint().getY();

                PieSectionLabelGenerator labelGenerator = getLabelGenerator();
                if (labelGenerator == null) {
                    continue;
                }
                String label = labelGenerator.generateSectionLabel(
                        this.dataset, key);
                if (label == null) {
                    continue;
                }
                g2.setFont(this.getLabelFont());
                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D bounds = TextUtilities.getTextBounds(label, g2, fm);
                Rectangle2D out = this.getLabelPadding().createOutsetRectangle(
                        bounds);
                Shape bg = ShapeUtilities.createTranslatedShape(out,
                        x - bounds.getCenterX(), y - bounds.getCenterY());
                if (this.getLabelShadowPaint() != null) {
                    Shape shadow = ShapeUtilities.createTranslatedShape(bg,
                            this.getShadowXOffset(), this.getShadowYOffset());
                    g2.setPaint(this.getLabelShadowPaint());
                    g2.fill(shadow);
                }
                if (this.getLabelBackgroundPaint() != null) {
                    g2.setPaint(this.getLabelBackgroundPaint());
                    g2.fill(bg);
                }
                if (this.getLabelOutlinePaint() != null
                        && this.getLabelOutlineStroke() != null) {
                    g2.setPaint(this.getLabelOutlinePaint());
                    g2.setStroke(this.getLabelOutlineStroke());
                    g2.draw(bg);
                }

                g2.setPaint(this.getLabelPaint());
                g2.setFont(this.getLabelFont());
                TextUtilities.drawAlignedString(getLabelGenerator()
                        .generateSectionLabel(getDataset(), key), g2, x, y,
                        TextAnchor.CENTER);

            }
        }

        g2.setComposite(originalComposite);

    }
    
    /**
     * Draws a single data item.
     *
     * @param g2  the graphics device (<code>null</code> not permitted).
     * @param section  the section index.
     * @param dataArea  the data plot area.
     * @param state  state information for one chart.
     * @param currentPass  the current pass index.
     */
    @SuppressWarnings("unchecked")
	protected void drawItem(Graphics2D g2,
                            int section,
                            Rectangle2D dataArea,
                            PiePlotState state,
                            int currentPass) {

        PieDataset dataset = getDataset();
        Number n = dataset.getValue(section);
        if (n == null) {
            return;
        }
        double value = n.doubleValue();
        double angle1 = 0.0;
        double angle2 = 0.0;

        Rotation direction = getDirection();
        if (direction == Rotation.CLOCKWISE) {
            angle1 = state.getLatestAngle();
            angle2 = angle1 - value / state.getTotal() * 180.0;
        }
        else if (direction == Rotation.ANTICLOCKWISE) {
            angle1 = state.getLatestAngle();
            angle2 = angle1 + value / state.getTotal() * 180.0;
        }
        else {
            throw new IllegalStateException("Rotation type not recognised.");
        }

        double angle = (angle2 - angle1);
        if (Math.abs(angle) > getMinimumArcAngleToDraw()) {
            Comparable key = getSectionKey(section);
            double ep = 0.0;
            double mep = getMaximumExplodePercent();
            if (mep > 0.0) {
                ep = getExplodePercent(key) / mep;
            }
            Rectangle2D arcBounds = getArcBounds(state.getPieArea(),
                    state.getExplodedPieArea(), angle1, angle, ep);
            Arc2D.Double arc = new Arc2D.Double(arcBounds, angle1, angle,
                    Arc2D.OPEN);

            // create the bounds for the inner arc
            double depth = this.sectionDepth / 2.0;
            RectangleInsets s = new RectangleInsets(UnitType.RELATIVE,
                depth, depth, depth, depth);
            Rectangle2D innerArcBounds = new Rectangle2D.Double();
            innerArcBounds.setRect(arcBounds);
            s.trim(innerArcBounds);
            // calculate inner arc in reverse direction, for later
            // GeneralPath construction
            Arc2D.Double arc2 = new Arc2D.Double(innerArcBounds, angle1
                    + angle, -angle, Arc2D.OPEN);
            GeneralPath path = new GeneralPath();
            path.moveTo((float) arc.getStartPoint().getX(),
                    (float) arc.getStartPoint().getY());
            path.append(arc.getPathIterator(null), false);
            path.append(arc2.getPathIterator(null), true);
            path.closePath();

            Line2D separator = new Line2D.Double(arc2.getEndPoint(),
                    arc.getStartPoint());

            if (currentPass == 0) {
                Paint shadowPaint = getShadowPaint();
                double shadowXOffset = getShadowXOffset();
                double shadowYOffset = getShadowYOffset();
                if (shadowPaint != null) {
                    Shape shadowArc = ShapeUtilities.createTranslatedShape(
                            path, (float) shadowXOffset, (float) shadowYOffset);
                    g2.setPaint(shadowPaint);
                    g2.fill(shadowArc);
                }
            }
            else if (currentPass == 1) {
                Paint paint = lookupSectionPaint(key);
                g2.setPaint(paint);
                g2.fill(path);
                Paint outlinePaint = lookupSectionOutlinePaint(key);
                Stroke outlineStroke = lookupSectionOutlineStroke(key);
                if (this.getSectionOutlinesVisible()) {
                    g2.setPaint(outlinePaint);
                    g2.setStroke(outlineStroke);
                    g2.draw(path);
                }

                // add an entity for the pie section
                if (state.getInfo() != null) {
                    EntityCollection entities = state.getEntityCollection();
                    if (entities != null) {
                        String tip = null;
                        PieToolTipGenerator toolTipGenerator
                                = getToolTipGenerator();
                        if (toolTipGenerator != null) {
                            tip = toolTipGenerator.generateToolTip(dataset,
                                    key);
                        }
                        String url = null;
                        PieURLGenerator urlGenerator = getURLGenerator();
                        if (urlGenerator != null) {
                            url = urlGenerator.generateURL(dataset, key,
                                    getPieIndex());
                        }
                        PieSectionEntity entity = new PieSectionEntity(path,
                                dataset, getPieIndex(), section, key, tip,
                                url);
                        entities.add(entity);
                    }
                }
            }
            else if (currentPass == 2) {
                if (this.separatorsVisible) {
                    Line2D extendedSeparator = extendLine(separator,
                        this.innerSeparatorExtension,
                        this.outerSeparatorExtension);
                    g2.setStroke(this.separatorStroke);
                    g2.setPaint(this.separatorPaint);
                    g2.draw(extendedSeparator);
                }
            }
        }
        state.setLatestAngle(angle2);
    }

    /**
     * Tests this plot for equality with an arbitrary object.
     *
     * @param obj  the object to test against (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HalfRingPlot)) {
            return false;
        }
        HalfRingPlot that = (HalfRingPlot) obj;
        if (this.separatorsVisible != that.separatorsVisible) {
            return false;
        }
        if (!ObjectUtilities.equal(this.separatorStroke,
                that.separatorStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.separatorPaint, that.separatorPaint)) {
            return false;
        }
        if (this.innerSeparatorExtension != that.innerSeparatorExtension) {
            return false;
        }
        if (this.outerSeparatorExtension != that.outerSeparatorExtension) {
            return false;
        }
        if (this.sectionDepth != that.sectionDepth) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Creates a new line by extending an existing line.
     *
     * @param line  the line (<code>null</code> not permitted).
     * @param startPercent  the amount to extend the line at the start point
     *                      end.
     * @param endPercent  the amount to extend the line at the end point end.
     *
     * @return A new line.
     */
    private Line2D extendLine(Line2D line, double startPercent,
                              double endPercent) {
        if (line == null) {
            throw new IllegalArgumentException("Null 'line' argument.");
        }
        double x1 = line.getX1();
        double x2 = line.getX2();
        double deltaX = x2 - x1;
        double y1 = line.getY1();
        double y2 = line.getY2();
        double deltaY = y2 - y1;
        x1 = x1 - (startPercent * deltaX);
        y1 = y1 - (startPercent * deltaY);
        x2 = x2 + (endPercent * deltaX);
        y2 = y2 + (endPercent * deltaY);
        return new Line2D.Double(x1, y1, x2, y2);
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException  if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writeStroke(this.separatorStroke, stream);
        SerialUtilities.writePaint(this.separatorPaint, stream);
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream)
        throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.separatorStroke = SerialUtilities.readStroke(stream);
        this.separatorPaint = SerialUtilities.readPaint(stream);
    }

	public Paint getTotalPaint() {
		return totalPaint;
	}

	public void setTotalPaint(Paint totalPaint) {
		this.totalPaint = totalPaint;
	}

	public boolean isBDrawTotalHRing() {
		return bDrawTotalHRing;
	}

	public void setBDrawTotalHRing(boolean drawTotalHRing) {
		bDrawTotalHRing = drawTotalHRing;
	}

	public boolean isBDrawTotalLabel() {
		return bDrawTotalLabel;
	}

	public void setBDrawTotalLabel(boolean drawTotalLabel) {
		if (this.isBDrawTotalHRing()) {
			bDrawTotalLabel = drawTotalLabel;
		}
		else return;
	}

	public NumberFormat getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}

	public NumberFormat getPercentFormat() {
		return percentFormat;
	}

	public void setPercentFormat(NumberFormat percentFormat) {
		this.percentFormat = percentFormat;
	}

	public boolean isBDrawLabel() {
		return bDrawLabel;
	}

	public void setBDrawLabel(boolean drawLabel) {
		bDrawLabel = drawLabel;
	}

}
