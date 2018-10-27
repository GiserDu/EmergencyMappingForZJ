package com.zz.chart.chartstyle;

import java.awt.Color;

import org.dom4j.Element;

/**
 * 
 * @author chen
 *				用来存储符号绘制样式
 */
public class ChartStyle implements Cloneable{
	
	public ChartStyle clone(){ 
		ChartStyle o = null; 
		try{
		o = (ChartStyle)super.clone(); 
		}catch(CloneNotSupportedException e){ 
		e.printStackTrace(); 
		}
		return o; 
	}
	public void setEdges(boolean hasEdge) {
		drawBarOutLine = hasEdge;//bar
		SectionOutlinesVisible = hasEdge;//pie
		DrawOutlines = hasEdge;//line
		OutLines = hasEdge;
		
	}
	public boolean getEdges() {
		return drawBarOutLine;
	}
	public void setLabels(boolean hasLabel) {
		isLable = hasLabel;//bar
		bLabel = hasLabel;//pie
		isLabel = hasLabel;//custom
		lineLable = hasLabel;//line
		isLables = hasLabel;//combine
	}
	public boolean getLabels() {
		return isLabel;
	}
	
	public void setLabelsFontSize(int size) {
		itemLabelFontSize = size;//bar
		LabelFontSize = size;//pie
		lineItemLabelFontSize = size;//line
		customItemLabelFontSize = size;//custom
		itemLabelFontSizes = size;//combine
	}
	
	public  void setLabelFontSizemm(float labelFontSizemm) {
		this.labelFontSizemm = labelFontSizemm;
	}

	
	public float getLabelFontSizemm() {
		return labelFontSizemm;
	}

	
	protected float labelFontSizemm;//浮点
	protected String chartID;//符号ID
	//bar
	
	protected boolean drawBarOutLine;//是否绘制柱子边线
	protected int outLinePaint;//柱子边线颜色
	protected float outLineBasicStroke;//柱子边线粗细
	protected double minimumBarLength;//柱子的最小长度
	protected double itemMargin;//柱子间的距离，以像素为单位
	protected boolean isLable;//是否绘制标签
	protected String itemLabelFontName;//标签字体名称
	protected int itemLabelFontSize;//标签字体大小
	protected int itemLabelPaint;//标签字体颜色
	protected int transparent;//填充色的透明度
	protected boolean IsCrack;
	protected boolean GradientPaint;
	protected double xOffSet;

	
	
	public double getxOffSet() {
		return xOffSet;
	}
	public void setxOffSet(double xOffSet) {
		this.xOffSet = xOffSet;
	}
	public boolean isGradientPaint() {
		return GradientPaint;
	}
	public void setGradientPaint(boolean gradientPaint) {
		GradientPaint = gradientPaint;
	}
	public boolean isIsCrack() {
		return IsCrack;
	}
	public void setIsCrack(boolean isCrack) {
		IsCrack = isCrack;
	}


	//line
	protected String BackgroundPaint;//
	protected boolean OutlineVisible;//
	protected boolean DomainGridlinesVisible;//
	protected boolean RangeGridlinesVisible;//
	protected float DomainGridlineStroke;//
	protected float RangeGridlineStroke;//
	protected boolean BaseShapesVisible;//
	protected boolean DrawOutlines;//
	protected boolean UseFillPaint;//
	protected String BaseFillPaint;//
	protected float OutlineStroke;//
	protected float Stroke;//
	protected float BaseAreaAlpha;//
	protected boolean lineLable;//是否绘制标签
	protected String lineItemLabelFontName;//标签字体名称
	protected int lineItemLabelFontSize;//标签字体大小
	protected int lineItemLabelPaint;//标签字体颜色
	protected boolean rangAxisVisible;//
	protected boolean domainAxisVisible;//
	protected boolean rTickLabelsVisible;//
	protected boolean dTickLabelsVisible;//
	protected int LowerMargin;//
	
	//pie
	
	protected float BackgroundAlpha;
	protected float ForegroundAlpha;
	protected boolean OutlineVisble;
	protected boolean SectionOutlinesVisible;
	protected float BaseSectionOutlineStroke;
	protected int BaseSectionOutlinePaint;
	protected boolean Circular;
	protected double StartAngle;
	protected double LabelLinkMargin;
	protected int LabelFontSize; 
	protected String LabelGenerator;
	protected double LabelGap;
	protected String LabelFontName;
	protected int LabelFontPaint;
	protected boolean bLabel;
	protected double TiltAngle;
	protected double DepthFactor;
	protected double SectionDepth;

	//custom
	protected String path;
	protected int hGap;
	protected int vGap;
	protected int arrange;
	protected int perValue;
	protected int perWidth;
	protected int perHeight;
	protected Color colorOutFill;
	protected Color colorOutDraw;
	protected Color colorCenter;
	protected double ratioOffset;
	protected double ratioInMax;
	protected double ratioCenterOffset;
	protected double ratioUpDownUnitValue;
	protected boolean isLabel;
	
	//combine
	protected int outLinePaints;//边线颜色
	protected float outLineBasicStrokes;//边线粗细
	protected boolean isLables;//是否绘制标签
	protected String itemLabelFontNames;//标签字体名称
	protected int itemLabelFontSizes;//标签字体大小
	protected int itemLabelPaints;//标签字体颜色
	protected float BackgroundAlphas;
	protected float ForegroundAlphas;
	protected boolean OutlinesVisble;
	protected boolean OutLines;//是否绘制边线
	
	public double getSectionDepth() {
		return SectionDepth;
	}
	public void setSectionDepth(double sectionDepth) {
		SectionDepth = sectionDepth;
	}
	public double getDepthFactor() {
		return DepthFactor;
	}
	public void setDepthFactor(double depthFactor) {
		DepthFactor = depthFactor;
	}
	
	public double getTiltAngle() {
		return TiltAngle;
	}
	public void setTiltAngle(double tiltAngle) {
		TiltAngle = tiltAngle;
	}

	public boolean isOutLines() {
		return OutLines;
	}
	public void setOutLines(boolean outLines) {
		OutLines = outLines;
	}
	public int getOutLinePaints() {
		return outLinePaints;
	}
	public void setOutLinePaints(int outLinePaints) {
		this.outLinePaints = outLinePaints;
	}
	public float getOutLineBasicStrokes() {
		return outLineBasicStrokes;
	}
	public void setOutLineBasicStrokes(float outLineBasicStrokes) {
		this.outLineBasicStrokes = outLineBasicStrokes;
	}
	public boolean isLables() {
		return isLables;
	}
	public void setLables(boolean isLables) {
		this.isLables = isLables;
	}
	public String getItemLabelFontNames() {
		return itemLabelFontNames;
	}
	public void setItemLabelFontNames(String itemLabelFontNames) {
		this.itemLabelFontNames = itemLabelFontNames;
	}
	public int getItemLabelFontSizes() {
		return itemLabelFontSizes;
	}
	public void setItemLabelFontSizes(int itemLabelFontSizes) {
		this.itemLabelFontSizes = itemLabelFontSizes;
	}
	public int getItemLabelPaints() {
		return itemLabelPaints;
	}
	public void setItemLabelPaints(int itemLabelPaints) {
		this.itemLabelPaints = itemLabelPaints;
	}
	public float getBackgroundAlphas() {
		return BackgroundAlphas;
	}
	public void setBackgroundAlphas(float backgroundAlphas) {
		BackgroundAlphas = backgroundAlphas;
	}
	public float getForegroundAlphas() {
		return ForegroundAlphas;
	}
	public void setForegroundAlphas(float foregroundAlphas) {
		ForegroundAlphas = foregroundAlphas;
	}
	public boolean isOutlinesVisble() {
		return OutlinesVisble;
	}
	public void setOutlinesVisble(boolean outlinesVisble) {
		OutlinesVisble = outlinesVisble;
	}
	
	
	public boolean isLabel() {
		return isLabel;
	}
	public void setLabel(boolean isLabel) {
		this.isLabel = isLabel;
	}
	public String getCustomItemLabelFontName() {
		return customItemLabelFontName;
	}
	public void setCustomItemLabelFontName(String customItemLabelFontName) {
		this.customItemLabelFontName = customItemLabelFontName;
	}
	public int getCustomItemLabelFontSize() {
		return customItemLabelFontSize;
	}
	public void setCustomItemLabelFontSize(int customItemLabelFontSize) {
		this.customItemLabelFontSize = customItemLabelFontSize;
	}
	public int getCustomItemLabelPaint() {
		return customItemLabelPaint;
	}
	public void setCustomItemLabelPaint(int customItemLabelPaint) {
		this.customItemLabelPaint = customItemLabelPaint;
	}


	protected String customItemLabelFontName;//标签字体名称
	protected int customItemLabelFontSize;//标签字体大小
	protected int customItemLabelPaint;//标签字体颜色
	
	
	

	protected int perLength;

	protected double ratioLengthGap;
	protected double ratioWidthGap;

	/**
	 * 
	 * 获取符号绘制样式
	 * 
	 * @param chartId
	 * 					符号ID
	 * @param chartPath 
	 * 					符号定义文件存放的路径
	 */
	public void Load(String chartPath) {
	
	}
	public void Load(Element chartElement) {
		
	}

	/**
	 * 保存符号绘制样式
	 * 
	 * @return  
	 * 			符号绘制样式
	 */
	public ChartStyle Save() {
		return null;
	}

	public String getChartID() {
		return chartID;
	}

	public void setChartID(String chartID) {
		this.chartID = chartID;
	}

	public boolean isDrawBarOutLine() {
		return drawBarOutLine;
	}

	public void setDrawBarOutLine(boolean drawBarOutLine) {
		this.drawBarOutLine = drawBarOutLine;
	}

	public int getOutLinePaint() {
		return outLinePaint;
	}

	public void setOutLinePaint(int outLinePaint) {
		this.outLinePaint = outLinePaint;
	}

	public float getOutLineBasicStroke() {
		return outLineBasicStroke;
	}

	public void setOutLineBasicStroke(float outLineBasicStroke) {
		this.outLineBasicStroke = outLineBasicStroke;
	}

	public double getMinimumBarLength() {
		return minimumBarLength;
	}

	public void setMinimumBarLength(double minimumBarLength) {
		this.minimumBarLength = minimumBarLength;
	}

	public double getItemMargin() {
		return itemMargin;
	}

	public void setItemMargin(double itemMargin) {
		this.itemMargin = itemMargin;
	}

	public boolean isLable() {
		return isLable;
	}

	public void setLable(boolean isLable) {
		this.isLable = isLable;
	}

	public String getItemLabelFontName() {
		return itemLabelFontName;
	}

	public void setItemLabelFontName(String itemLabelFontName) {
		this.itemLabelFontName = itemLabelFontName;
	}

	public int getItemLabelFontSize() {
		return itemLabelFontSize;
	}

	public void setItemLabelFontSize(int itemLabelFontSize) {
		this.itemLabelFontSize = itemLabelFontSize;
	}

	public int getItemLabelPaint() {
		return itemLabelPaint;
	}

	public void setItemLabelPaint(int itemLabelPaint) {
		this.itemLabelPaint = itemLabelPaint;
	}

	public int getTransparent() {
		return transparent;
	}

	public void setTransparent(int transparent) {
		this.transparent = transparent;
	}

	public String getBackgroundPaint() {
		return BackgroundPaint;
	}

	public void setBackgroundPaint(String backgroundPaint) {
		BackgroundPaint = backgroundPaint;
	}

	public boolean isOutlineVisible() {
		return OutlineVisible;
	}

	public void setOutlineVisible(boolean outlineVisible) {
		OutlineVisible = outlineVisible;
	}

	public boolean isDomainGridlinesVisible() {
		return DomainGridlinesVisible;
	}

	public void setDomainGridlinesVisible(boolean domainGridlinesVisible) {
		DomainGridlinesVisible = domainGridlinesVisible;
	}

	public boolean isRangeGridlinesVisible() {
		return RangeGridlinesVisible;
	}

	public void setRangeGridlinesVisible(boolean rangeGridlinesVisible) {
		RangeGridlinesVisible = rangeGridlinesVisible;
	}

	public float getDomainGridlineStroke() {
		return DomainGridlineStroke;
	}

	public void setDomainGridlineStroke(float domainGridlineStroke) {
		DomainGridlineStroke = domainGridlineStroke;
	}

	public float getRangeGridlineStroke() {
		return RangeGridlineStroke;
	}

	public void setRangeGridlineStroke(float rangeGridlineStroke) {
		RangeGridlineStroke = rangeGridlineStroke;
	}

	public boolean isBaseShapesVisible() {
		return BaseShapesVisible;
	}

	public void setBaseShapesVisible(boolean baseShapesVisible) {
		BaseShapesVisible = baseShapesVisible;
	}

	public boolean isDrawOutlines() {
		return DrawOutlines;
	}

	public void setDrawOutlines(boolean drawOutlines) {
		DrawOutlines = drawOutlines;
	}

	public boolean isUseFillPaint() {
		return UseFillPaint;
	}

	public void setUseFillPaint(boolean useFillPaint) {
		UseFillPaint = useFillPaint;
	}

	public String getBaseFillPaint() {
		return BaseFillPaint;
	}

	public void setBaseFillPaint(String baseFillPaint) {
		BaseFillPaint = baseFillPaint;
	}

	public float getOutlineStroke() {
		return OutlineStroke;
	}

	public void setOutlineStroke(float outlineStroke) {
		OutlineStroke = outlineStroke;
	}

	public float getStroke() {
		return Stroke;
	}

	public void setStroke(float stroke) {
		Stroke = stroke;
	}

	public float getBaseAreaAlpha() {
		return BaseAreaAlpha;
	}

	public void setBaseAreaAlpha(float baseAreaAlpha) {
		BaseAreaAlpha = baseAreaAlpha;
	}
	public boolean isLineLable() {
		return lineLable;
	}
	public void setLineLable(boolean lineLable) {
		this.lineLable = lineLable;
	}
	public String getLineItemLabelFontName() {
		return lineItemLabelFontName;
	}
	public void setLineItemLabelFontName(String lineItemLabelFontName) {
		this.lineItemLabelFontName = lineItemLabelFontName;
	}
	public int getLineItemLabelFontSize() {
		return lineItemLabelFontSize;
	}
	public void setLineItemLabelFontSize(int lineItemLabelFontSize) {
		this.lineItemLabelFontSize = lineItemLabelFontSize;
	}
	public int getLineItemLabelPaint() {
		return lineItemLabelPaint;
	}
	public void setLineItemLabelPaint(int lineItemLabelPaint) {
		this.lineItemLabelPaint = lineItemLabelPaint;
	}

	public boolean isRangAxisVisible() {
		return rangAxisVisible;
	}

	public void setRangAxisVisible(boolean rangAxisVisible) {
		this.rangAxisVisible = rangAxisVisible;
	}

	public boolean isDomainAxisVisible() {
		return domainAxisVisible;
	}

	public void setDomainAxisVisible(boolean domainAxisVisible) {
		this.domainAxisVisible = domainAxisVisible;
	}

	public boolean isrTickLabelsVisible() {
		return rTickLabelsVisible;
	}

	public void setrTickLabelsVisible(boolean rTickLabelsVisible) {
		this.rTickLabelsVisible = rTickLabelsVisible;
	}

	public boolean isdTickLabelsVisible() {
		return dTickLabelsVisible;
	}

	public void setdTickLabelsVisible(boolean dTickLabelsVisible) {
		this.dTickLabelsVisible = dTickLabelsVisible;
	}

	public int getLowerMargin() {
		return LowerMargin;
	}

	public void setLowerMargin(int lowerMargin) {
		LowerMargin = lowerMargin;
	}

	public float getBackgroundAlpha() {
		return BackgroundAlpha;
	}

	public void setBackgroundAlpha(float backgroundAlpha) {
		BackgroundAlpha = backgroundAlpha;
	}

	public float getForegroundAlpha() {
		return ForegroundAlpha;
	}

	public void setForegroundAlpha(float foregroundAlpha) {
		ForegroundAlpha = foregroundAlpha;
	}

	public boolean isOutlineVisble() {
		return OutlineVisble;
	}

	public void setOutlineVisble(boolean outlineVisble) {
		OutlineVisble = outlineVisble;
	}

	public boolean isSectionOutlinesVisible() {
		return SectionOutlinesVisible;
	}

	public void setSectionOutlinesVisible(boolean sectionOutlinesVisible) {
		SectionOutlinesVisible = sectionOutlinesVisible;
	}

	public float getBaseSectionOutlineStroke() {
		return BaseSectionOutlineStroke;
	}

	public void setBaseSectionOutlineStroke(float baseSectionOutlineStroke) {
		BaseSectionOutlineStroke = baseSectionOutlineStroke;
	}

	public int getBaseSectionOutlinePaint() {
		return BaseSectionOutlinePaint;
	}

	public void setBaseSectionOutlinePaint(int baseSectionOutlinePaint) {
		BaseSectionOutlinePaint = baseSectionOutlinePaint;
	}

	public boolean isCircular() {
		return Circular;
	}

	public void setCircular(boolean circular) {
		Circular = circular;
	}

	public double getStartAngle() {
		return StartAngle;
	}

	public void setStartAngle(double startAngle) {
		StartAngle = startAngle;
	}

	public double getLabelLinkMargin() {
		return LabelLinkMargin;
	}

	public void setLabelLinkMargin(double labelLinkMargin) {
		LabelLinkMargin = labelLinkMargin;
	}



	public int getLabelFontSize() {
		return LabelFontSize;
	}
	public void setLabelFontSize(int labelFontSize) {
		LabelFontSize = labelFontSize;
	}
	public String getLabelFontName() {
		return LabelFontName;
	}
	public void setLabelFontName(String labelFontName) {
		LabelFontName = labelFontName;
	}
	public int getLabelFontPaint() {
		return LabelFontPaint;
	}
	public void setLabelFontPaint(int labelFontPaint) {
		LabelFontPaint = labelFontPaint;
	}
	public boolean isbLabel() {
		return bLabel;
	}
	public void setbLabel(boolean bLabel) {
		this.bLabel = bLabel;
	}
	public String getLabelGenerator() {
		return LabelGenerator;
	}

	public void setLabelGenerator(String labelGenerator) {
		LabelGenerator = labelGenerator;
	}

	public double getLabelGap() {
		return LabelGap;
	}

	public void setLabelGap(double labelGap) {
		LabelGap = labelGap;
	}



	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int gethGap() {
		return hGap;
	}

	public void sethGap(int hGap) {
		this.hGap = hGap;
	}

	public int getvGap() {
		return vGap;
	}

	public void setvGap(int vGap) {
		this.vGap = vGap;
	}

	public int getArrange() {
		return arrange;
	}

	public void setArrange(int arrange) {
		this.arrange = arrange;
	}

	public int getPerValue() {
		return perValue;
	}

	public void setPerValue(int perValue) {
		this.perValue = perValue;
	}

	public int getPerWidth() {
		return perWidth;
	}

	public void setPerWidth(int perWidth) {
		this.perWidth = perWidth;
	}

	public int getPerHeight() {
		return perHeight;
	}

	public void setPerHeight(int perHeight) {
		this.perHeight = perHeight;
	}




	

}
