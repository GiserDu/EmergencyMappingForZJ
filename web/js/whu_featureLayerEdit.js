//定义几个全局变量
var rendererNow = null;
var thisFeatureLayer = {};
var edited = false;

var editFeatureLayer = function (featureLayer,type,name){
	if(!featureLayer){
        layui.use('layer', function () {
            var layer = layui.layer;
            layer.open({
                title: '图层尚未展示'
                ,content: "请先载入该图层！"
            });
        })
		return;
	}
	thisFeatureLayer.feature = featureLayer;
    thisFeatureLayer.type = type;
    thisFeatureLayer.name = name;
	//创建要素服务编辑面板
	creatFeatureLayerEditPanel();
	//注册当前要素图层的点击事件
	selectedFeatureLayer(featureLayer,type);
	editFeatureLayerStart();
};

//创建要素服务编辑面板
var creatFeatureLayerEditPanel = function(){
	var html = [
		'<div class="map-marking-panel-head">',
		'<label id="map-featureLayer-panel-title">绘制点标注</label>',
		'<i class="fa fa-close icon-close" onclick="closeFeatureLayerPanel()"></i>',
		'</div>',
		'<div class="map-marking-panel-container">',
		'<div id="map-featureLayer-panel-tip" class="map-marking-panel-tip">地图上待标注位置鼠标左键点击地图添加一个点标注，可填写名称、备注、图标，点击保存生效！</div>',
	
		//标注
		'<div class="map-marking-info" id="map-featureLayer-info">',
		'<div id="map-featureLayer-info-container" class="map-marking-info-container">',
		'<div class="layui-form-item mark-style" id="pointFeatureLayer-style-item">',
		'<label class="layui-form-label">图标</label>',
		'<div class="layui-input-block">',
		'<img id="pointFeatureLayerIcon" src="assets/img/pointIcon/8.png">',
		'<button class="layui-btn layui-btn-mini layui-btn-normal" onclick="changePointFeatureLayerStyle()">更换</button>',
		'</div>',
		'<div class="layui-form-item"><label class="layui-form-label">高度</label><div class="layui-input-inline"><input type="number" id="pointFeatureLayer-height" class="layui-input input-number" onchange="pointFeatureLayerStyleChange()"></div><div class="layui-form-mid layui-word-aux">(单位：像素)</div></div>',
		'<div class="layui-form-item"><label class="layui-form-label">宽度</label><div class="layui-input-inline"><input type="number" id="pointFeatureLayer-width" class="layui-input input-number" onchange="pointFeatureLayerStyleChange()"></div><div class="layui-form-mid layui-word-aux">(单位：像素)</div></div>',
		'<div class="layui-form-item"><label class="layui-form-label">偏移角度</label><div class="layui-input-inline"><input type="number" id="pointFeatureLayer-angle" class="layui-input input-number" onchange="pointFeatureLayerStyleChange()"></div><div class="layui-form-mid layui-word-aux">(单位：度)</div></div>',
		'<div class="layui-form-item"><label class="layui-form-label">Y轴偏移量</label><div class="layui-input-inline"><input type="number" id="pointFeatureLayer-xoffset" class="layui-input input-number" onchange="pointFeatureLayerStyleChange()"></div><div class="layui-form-mid layui-word-aux">(单位：像素)</div></div>',
		'<div class="layui-form-item"><label class="layui-form-label">Y轴偏移量</label><div class="layui-input-inline"><input type="number" id="pointFeatureLayer-yoffset" class="layui-input input-number" onchange="pointFeatureLayerStyleChange()"></div><div class="layui-form-mid layui-word-aux">(单位：像素)</div></div>',
		'</div>',
		'<div class="layui-form-item mark-style" id="lineFeatureLayer-style-item">',
		'<label class="layui-form-label">线样式</label>',
		'<div class="layui-input-block">',
		'<div id="lineFeatureLayer-style" class="line-style"></div>',
		'<button class="layui-btn layui-btn-mini layui-btn-normal" onclick="changeLineFeatureLayerStyle()">更换</button>',
		'</div>',
		'</div>',
		'<div class="layui-form-item mark-style" id="polygonFeatureLayer-style-item">',
		'<label class="layui-form-label">面样式</label>',
		'<div class="layui-input-block">',
		'<div id="polygonFeatureLayer-style" class="polygon-style"></div>',
		'<button class="layui-btn layui-btn-mini layui-btn-normal" onclick="changePolygonFeatureLayerStyle()">更换</button>',
		'</div>',
		'</div>',
		'<div class="layui-form-item">',
		'<div class="layui-input-block">',
		'<button class="layui-btn " onclick="featureLayerInfoSave()">保存</button>',
		'<!--<button class="layui-btn layui-btn-danger" onclick="infoDelete()">删除</button>-->',
		'</div>',
		'</div>',
		'</div>',
		'</div>',
	
		//点图标面板
		'<div class="point-style-edit" id="pointFeatureLayer-style-edit">',
		'<ul>',
		'<li>',
		'<img src="assets/img/pointIcon/1.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/2.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/3.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/4.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/5.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/6.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/7.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/8.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/9.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/10.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/11.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/12.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/13.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/14.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/15.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/16.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/17.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/18.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/19.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/20.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/21.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/22.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/23.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/24.png">',
		'</li>',
		'<li>',
		'<img src="assets/img/pointIcon/25.png">',
		'</li>',
		'</ul>',
		'<button class="layui-btn  margin-10" onclick="changePointFeatureLayerStyleBack()">返回</button>',
		'</div>',
	
		//线样式面板
		'<div class="line-style-edit" id="lineFeatureLayer-style-edit">',
		'<div class="style-preview">',
		'<div id="lineFeatureLayer-style-preview" class="line-style-preview"></div>',
		'</div>',
		'<div class="layui-form-item layui-form-text">',
		'<label class="layui-form-label">线颜色</label>',
		'<div class="layui-input-inline">',
		'<input id="lineFeatureLayer-color" type = "color" class="layui-input input-color" onchange="lineFeatureLayerStyleChange()" value="#ff4500">',
		'</div>',
		'</div>',
		'<div class="layui-form-item layui-form-text">',
		'<label class="layui-form-label">线型</label>',
		'<div class="layui-input-inline">',
		'<select class="form-control" style="width:190px" onchange="lineFeatureLayerStyleChange()" id="lineFeatureLayer-line-style"><option value="esriSLSSolid">实线</option><option value="esriSLSDash">虚线</option><option value="esriSLSDot">点</option><option value="esriSLSDashDot">虚线-点</option><option value="esriSLSDashDotDot">虚线-点-点</option><option value="esriSLSLongDash">长虚线</option><option value="esriSLSLongDashDot">长虚线-点</option><option value="esriSLSShortDash">短虚线</option><option value="esriSLSShortDashDot">短虚线-点</option><option value="esriSLSShortDashDotDot">短虚线-点-点</option><option value="esriSLSLongDashDot">长虚线-点</option><option value="esriSLSShortDash">短虚线</option><option value="esriSLSShortDashDot">短虚线-点</option><option value="esriSLSShortDashDotDot">短虚线-点-点</option><option value="esriSLSShortDot">细点</option></select>',
		'</div>',
		'</div>',
		'<div class="layui-form-item layui-form-text">',
		'<label class="layui-form-label">线样式</label>',
		'<div class="layui-input-inline">',
		'<select class="form-control" style="width:190px"  style="width:190px"  onchange="lineFeatureLayerStyleChange()" id="lineFeatureLayer-marker-style"><option value="">无</option><option value="arrow">箭头</option></select>',
		'</div>',
		'</div>',
		'<div class="layui-form-item">',
		'<label class="layui-form-label">线透明度</label>',
		'<div class="layui-input-inline">',
		'<input type="range" id="lineFeatureLayer-opacity" min="0" max="1" step="0.1" class="input-range" onchange="lineFeatureLayerStyleChange()">',
		'</div>',
		'<div class="layui-form-mid layui-word-aux">0:完全透明,1:不透明</div>',
		'</div>',
		'<div class="layui-form-item">',
		'<label class="layui-form-label">线宽</label>',
		'<div class="layui-input-inline">',
		'<input type="number" id="lineFeatureLayer-width" class="layui-input input-number" onchange="lineFeatureLayerStyleChange()">',
		'</div>',
		'<div class="layui-form-mid layui-word-aux">(单位：像素)</div>',
		'</div>',
		'<div class="layui-form-item">',
		'<div class="layui-input-block">',
		'<button class="layui-btn " onclick="changePolylineFeatureLayerStyleBack()">返回</button>',
		'<button class="layui-btn " onclick="saveLineFeatureLayerStyle()">保存</button>',
		'</div>',
		'</div>',
		'</div>',
	
		//面样式面板
		'<div class="polygon-style-edit" id="polygonFeatureLayer-style-edit">',
		'<div class="style-preview">',
		'<div id="polygonFeatureLayer-style-preview" class="polygon-style-preview"></div>',
		'</div>',
		'<div class="layui-form-item layui-form-text">',
		'<label class="layui-form-label">填充色(仅在填充样式为实心的生效)</label>',
		'<div class="layui-input-inline">',
		'<input id="polygonFeatureLayer-fill-color"  type = "color" class="layui-input input-color" onchange="polygonFeatureLayerStyleChange()" value="#ff4500">',
		'</div>',
		'</div>',
		'<div class="layui-form-item layui-form-text">',
		'<label class="layui-form-label">填充样式</label>',
		'<div class="layui-input-inline">',
		'<select class="form-control" style="width:190px" onchange="polygonFeatureLayerStyleChange()" id="polygonFeatureLayer-fill-style"><option value="esriSFSSolid">实心</option><option value="esriSFSForwardDiagonal">斜线</option><option value="esriSFSBackwardDiagonal">反斜线</option><option value="esriSFSCross">格网</option><option value="esriSFSDiagonalCross">交叉格网</option><option value="esriSFSHorizontal">水平线</option><option value="esriSFSVertical">垂线</option></select>',
		'</div>',
		'</div>',
		'<div class="layui-form-item">',
		'<label class="layui-form-label">填充透明度</label>',
		'<div class="layui-input-inline">',
		'<input type="range" id="polygonFeatureLayer-fill-opacity" min="0" step="0.1" max="1" class="input-range" onchange="polygonFeatureLayerStyleChange()">',
		'</div>',
		'<div class="layui-form-mid layui-word-aux">0:完全透明,1:不透明</div>',
		'</div>',
		'<div class="layui-form-item layui-form-text">',
		'<label class="layui-form-label">边框颜色</label>',
		'<div class="layui-input-inline">',
		'<input id="polygonFeatureLayer-border-color" type="color" class="layui-input input-color" onchange="polygonFeatureLayerStyleChange()" value="#FFD700">',
		'</div>',
		'</div>',
		'<div class="layui-form-item layui-form-text">',
		'<label class="layui-form-label">边线样式</label>',
		'<div class="layui-input-inline">',
		'<select class="form-control" style="width:190px"  onchange="polygonFeatureLayerStyleChange()" id="polygonFeatureLayer-border-style"><option value="esriSLSSolid">实线</option><option value="esriSLSShortDot">细点</option><option value="esriSLSDot">点</option><option value="esriSLSDash">虚线</option><option value="esriSLSDashDot">虚线-点</option><option value="esriSLSDashDot">虚线-点-点</option><option value="esriSLSLongDash">长虚线</option><option value="esriSLSLongDashDot">长虚线-点</option><option value="esriSLSShortDash">短虚线</option><option value="esriSLSShortDashDot">短虚线-点</option><option value="esriSLSShortDashDotDot">短虚线-点-点</option><option value="esriSLSNull">无</option></select>',
		'</div>',
		'</div>',
		'<div class="layui-form-item">',
		'<label class="layui-form-label">边框透明度</label>',
		'<div class="layui-input-inline">',
		'<input type="range" id="polygonFeatureLayer-border-opacity" min="0" step="0.1" max="1" class="input-range" onchange="polygonFeatureLayerStyleChange()">',
		'</div>',
		'<div class="layui-form-mid layui-word-aux">(0-1)0:完全透明,1:不透明</div>',
		'</div>',
		'<div class="layui-form-item">',
		'<label class="layui-form-label">边框宽</label>',
		'<div class="layui-input-inline">',
		'<input type="number" id="polygonFeatureLayer-border-width" class="layui-input input-number" onchange="polygonFeatureLayerStyleChange()">',
		'</div>',
		'<div class="layui-form-mid layui-word-aux">(单位：像素)</div>',
		'</div>',
		'<div class="layui-form-item">',
		'<div class="layui-input-block">',
		'<button class="layui-btn " onclick="changePolygonaFeatureLayerStyleBack()">返回</button>',
		'<button class="layui-btn " onclick="savePolygonFeatureLayerStyle()">保存</button>',
		'</div>',
		'</div>',
		'</div>'
	].join('');
	var link = document.createElement('div');
	link.setAttribute('id', 'map-featureLayer-panel');
	link.setAttribute('class', 'map-marking-panel');
	link.innerHTML = html;
	document.body.appendChild(link);
}

//开始编辑，打开面板
var editFeatureLayerStart = function() {
	var panelHeadTitle, panelTip;
	
	panelHeadTitle = "要素样式编辑";
	panelTip = "点击更改按钮对要素图层进行样式编辑！";

	document.getElementById('map-featureLayer-panel-title').innerHTML = panelHeadTitle;
	document.getElementById('map-featureLayer-panel-tip').innerHTML = panelTip;
};

//对选择的要素进行属性编辑
var selectedFeatureLayer = function (featureLayer,type) {
    var pointStyleItem = document.getElementById('pointFeatureLayer-style-item');
    var lineStyleItem = document.getElementById('lineFeatureLayer-style-item');
    var polygonStyleItem = document.getElementById('polygonFeatureLayer-style-item');
    switch (type) {
    case "point":
        addClass(pointStyleItem, "show");
        removeClass(lineStyleItem, "show");
        removeClass(polygonStyleItem, "show");
        break;
    case "polyline":
        removeClass(pointStyleItem, "show");
        addClass(lineStyleItem, "show");
        removeClass(polygonStyleItem, "show");
        break;
    case 'polygon':
        removeClass(pointStyleItem, "show");
        removeClass(lineStyleItem, "show");
        addClass(polygonStyleItem, "show");
        break
    }
    rendererNow = featureLayer.renderer.symbol;
    thisFeatureLayer.feature = featureLayer;
    thisFeatureLayer.type = type;

    document.getElementById('pointFeatureLayerIcon').src = rendererNow.url;
   	document.getElementById('pointFeatureLayer-height').value=rendererNow.height;
	document.getElementById('pointFeatureLayer-width').value=rendererNow.width;
	document.getElementById('pointFeatureLayer-angle').value=rendererNow.angle;
	document.getElementById('pointFeatureLayer-xoffset').value=rendererNow.xoffset;
	document.getElementById('pointFeatureLayer-yoffset').value=rendererNow.yoffset;
    
    var lineStyle = document.getElementById('lineFeatureLayer-style');
    var lineStyle = document.getElementById('lineFeatureLayer-style');
    lineStyle.style.backgroundColor = rendererNow.color;
    lineStyle.style.height = rendererNow.width + 'px';
    var polygonStyle = document.getElementById('polygonFeatureLayer-style');
    polygonStyle.style.backgroundColor =  rendererNow.color;
    polygonStyle.style.border = rendererNow.width + 'px solid ' + rendererNow.color;
    openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-panel-tip', 'map-featureLayer-info']);
};

//改变点的图标
var changePointFeatureLayerStyle = function () {
    openFeatureLayerPanelContent(['map-featureLayer-panel', 'pointFeatureLayer-style-edit']);
    var obj = document.getElementById('pointFeatureLayer-style-edit');
    for (var i = 0; i < obj.children[0].children.length; i++) {
        obj.children[0].children[i].onclick = function () {
            var img = this.children[0];
            document.getElementById('pointFeatureLayerIcon').src = img.src;
            //在这一步改变点要素图层的图片文件
            rendererNow={};
            rendererNow.url = img.src;
            rendererNow.type = "esriPMS";
            if (drawType == 'edit') {
                openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
            } else {
               // addDrawInteraction('Point');
                openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
            }
        }
    }
};

//监听点图标参数的变化
var pointFeatureLayerStyleChange = function(){
	var height = document.getElementById('pointFeatureLayer-height').value;
	var width = document.getElementById('pointFeatureLayer-width').value;
	var angle = document.getElementById('pointFeatureLayer-angle').value;
	var xoffset = document.getElementById('pointFeatureLayer-xoffset').value;
	var yoffset = document.getElementById('pointFeatureLayer-yoffset').value;
	rendererNow.height = parseInt(height);
	rendererNow.width = parseInt(width);
	rendererNow.angle = parseInt(angle);
	rendererNow.xoffset = parseInt(xoffset);
	rendererNow.yoffset = parseInt(yoffset);
}

//目的是获得点击的图层的样式属性
var changeLineFeatureLayerStyle = function () {
    openFeatureLayerPanelContent(['map-featureLayer-panel', 'lineFeatureLayer-style-edit']);
    var border_rgba = tinycolor(rendererNow.color).toRgb();
    var border_opacity = border_rgba.a;
    var border_colorHex = tinycolor(rendererNow.color).toHexString();
    document.getElementById('lineFeatureLayer-color').value = border_colorHex;
    document.getElementById('lineFeatureLayer-opacity').value = border_opacity;
    document.getElementById('lineFeatureLayer-width').value = rendererNow.width;
    var preview = document.getElementById('lineFeatureLayer-style-preview');
    preview.style.backgroundColor = rendererNow.color;
    preview.style.border = rendererNow.width + 'px solid ' + rendererNow.color
};

//目的是改变“预览框”中的颜色预览
var lineFeatureLayerStyleChange = function () {
    var colorHex = document.getElementById('lineFeatureLayer-color').value;
    var rgb = tinycolor(colorHex).toRgb();
    var opacity = document.getElementById('lineFeatureLayer-opacity').value;
    var rgba = 'rgba(' + rgb.r + ',' + rgb.g + ',' + rgb.b + ',' + opacity + ')';
    var lineWidth = document.getElementById('lineFeatureLayer-width').value;
    var preview = document.getElementById('lineFeatureLayer-style-preview');
    preview.style.backgroundColor = rgba;
    preview.style.height = lineWidth + 'px'
};


//目的是改变多边形图层的整体样式
var saveLineFeatureLayerStyle = function() {
	var colorHex = document.getElementById('lineFeatureLayer-color').value;
    var rgb = tinycolor(colorHex).toRgb();
    var opacity = document.getElementById('lineFeatureLayer-opacity').value;
    var rgba = 'rgba(' + rgb.r + ',' + rgb.g + ',' + rgb.b + ',' + opacity + ')';
    var lineWidth = document.getElementById('lineFeatureLayer-width').value;
    var lineStyle = document.getElementById('lineFeatureLayer-line-style').value;
    var markerStyle = document.getElementById('lineFeatureLayer-marker-style').value;
    var simpleJson = {};
	require(["esri/Color"], function(Color) {rgba= Color.fromString(rgba);});
	//用交互得来的数值重新设置renderrenderer
	if(markerStyle==="arrow"){
        simpleJson = {
            "type": "esriCLS",
            "color": rgba,
            "width": lineWidth,
            "style": lineStyle,//"esriCLSShortDot",//可以改变
            "marker": {
                "style": markerStyle,//"arrow",//可以改变
                "placement": "end"//可以改变
            },
            "cap": "CAP_SQUARE",
            "join": "JOIN_ROUND",
            "miterLimit": 2
        };
	}else{
        simpleJson = {
            "type": "esriSLS",
            "color": rgba,
            "width": lineWidth,
            "style": lineStyle,//"esriCLSShortDot",//可以改变
        };
	}
	rendererNow = simpleJson;
	
	var lineStyle = document.getElementById('lineFeatureLayer-style');
    lineStyle.style.backgroundColor = rgba;
    lineStyle.style.height = lineWidth + 'px';
    if (drawType == 'edit') {
        openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
    } else {
       // addDrawInteraction('LineString');
        openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
    }
};

//目的是获得点击的图层的样式属性
var changePolygonFeatureLayerStyle = function () {
    openFeatureLayerPanelContent(['map-featureLayer-panel', 'polygonFeatureLayer-style-edit']);
    var border_rgba = tinycolor(rendererNow.outline.color).toRgb();
    var border_opacity = border_rgba.a;
    var border_colorHex = tinycolor(rendererNow.outline.color).toHexString();
    document.getElementById('polygonFeatureLayer-border-color').value = border_colorHex;
    document.getElementById('polygonFeatureLayer-border-opacity').value = border_opacity;
    document.getElementById('polygonFeatureLayer-border-width').value = rendererNow.outline.width;
    var fill_rgba = tinycolor(rendererNow.color).toRgb();
    var fill_opacity = fill_rgba.a;
    var fill_colorHex = tinycolor(rendererNow.color).toHexString();
    document.getElementById('polygonFeatureLayer-fill-color').value = fill_colorHex;
    document.getElementById('polygonFeatureLayer-fill-opacity').value = fill_opacity;
    var preview = document.getElementById('polygonFeatureLayer-style-preview');
    preview.style.backgroundColor = rendererNow.color;
    preview.style.border = rendererNow.outline.width + 'px solid ' + rendererNow.outline.color
};

//目的是改变“预览框”中的颜色预览
var polygonFeatureLayerStyleChange = function () {
    var border_colorHex = document.getElementById('polygonFeatureLayer-border-color').value;
    var border_opacity = document.getElementById('polygonFeatureLayer-border-opacity').value;
    var border_width = document.getElementById('polygonFeatureLayer-border-width').value;
    var fill_style = document.getElementById('polygonFeatureLayer-border-style').value
    var border_rgb = tinycolor(border_colorHex).toRgb();
    var border_rgba = 'rgba(' + border_rgb.r + ',' + border_rgb.g + ',' + border_rgb.b + ',' + border_opacity + ')';
    var fill_colorHex = document.getElementById('polygonFeatureLayer-fill-color').value;
    var fill_opacity = document.getElementById('polygonFeatureLayer-fill-opacity').value;
    var fill_rgb = tinycolor(fill_colorHex).toRgb();
    var fill_rgba = 'rgba(' + fill_rgb.r + ',' + fill_rgb.g + ',' + fill_rgb.b + ',' + fill_opacity + ')';
    var preview = document.getElementById('polygonFeatureLayer-style-preview');
    preview.style.backgroundColor = fill_rgba;
    preview.style.border = border_width + 'px solid' + border_rgba
};

//目的是取消面样式的改变
var changePolygonaFeatureLayerStyleBack = function () {
    if (drawType == 'edit') {
        openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
    } else {
        openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
    }
};

//目的是取消线样式的改变
var changePolylineFeatureLayerStyleBack = function () {
    if (drawType == 'edit') {
        openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
    } else {
        openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
    }
};
//目的是取消点样式的改变
var changePointFeatureLayerStyleBack = function () {
    if (drawType == 'edit') {
        openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
    } else {
        openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
    }
};

//目的是改变多边形图层的整体样式
var savePolygonFeatureLayerStyle = function() {
	var border_colorHex = document.getElementById('polygonFeatureLayer-border-color').value;
	var border_opacity = document.getElementById('polygonFeatureLayer-border-opacity').value;
	var border_width = document.getElementById('polygonFeatureLayer-border-width').value;
	var border_style = document.getElementById('polygonFeatureLayer-border-style').value;
	var border_rgb = tinycolor(border_colorHex).toRgb();
	var border_rgba = 'rgba(' + border_rgb.r + ',' + border_rgb.g + ',' + border_rgb.b + ',' + border_opacity + ')';
	var fill_colorHex = document.getElementById('polygonFeatureLayer-fill-color').value;
	var fill_opacity = document.getElementById('polygonFeatureLayer-fill-opacity').value;
	var fill_rgb = tinycolor(fill_colorHex).toRgb();
	var fill_rgba = 'rgba(' + fill_rgb.r + ',' + fill_rgb.g + ',' + fill_rgb.b + ',' + fill_opacity + ')';
	var fill_style = document.getElementById('polygonFeatureLayer-fill-style').value
	var thisFillColor = null;
	var thisborderColor = null;
	require(["esri/Color"], function(Color) {thisFillColor= Color.fromString(fill_rgba);thisBorderColor= Color.fromString(border_rgba);});
	//用交互得来的数值重新设置renderrenderer
/*	markStyle.polygon.borderColor = border_rgba;
	markStyle.polygon.borderWidth = border_width;
	markStyle.polygon.fillColor = fill_rgba;*/
	var simpleJson = {
			"type": "esriSFS", //SimpleFillSymbol(简单填充类型)
			"color": thisFillColor, //填充颜色(仅在style为STYLE_SOLID时生效)
			"outline": {
				"type": "esriSLS",
				"style": border_style,
				"color": thisBorderColor,
				"width": border_width
			}, //填充轮廓线要素"render"对象(见下述线要素)
			"style": fill_style //填充样式	
	};
	rendererNow = simpleJson;
	
	var polygonStyle = document.getElementById('polygonFeatureLayer-style');
	polygonStyle.style.backgroundColor = fill_rgba;
	polygonStyle.style.border = border_width + 'px solid ' + border_rgba;

	if(drawType == 'edit') {
		openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
	} else {
		//addDrawInteraction(drawType);
		openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
	}
};

//应用以改变了的属性
var featureLayerInfoSave = function(){
	require([
		"esri/layers/FeatureLayer",
		"esri/InfoTemplate", "esri/dijit/PopupTemplate", "esri/renderers/SimpleRenderer", "esri/layers/GraphicsLayer"
	], function(FeatureLayer, InfoTemplate, PopupTemplate, SimpleRenderer, GraphicsLayer) {
        if(thisFeatureLayer.type==="point")
		{
            pointFeatureLayerStyleChange();
            if(!(document.getElementById('pointFeatureLayer-height').value&& document.getElementById('pointFeatureLayer-width').value)){
                layui.use('layer', function () {
                    var lay = layui.layer;
                    lay.open({
                        title: '提示'
                        ,content: '高或者宽的像素值不能为0！'
                    });
                })
				return;
		}
		}
		var simpleJson = {
			"type": "simple",
			"label": thisFeatureLayer.name,
			"description": "",
			"symbol": rendererNow
		};
		var rend = new SimpleRenderer(simpleJson);
		thisFeatureLayer.feature.setRenderer(rend);
		thisFeatureLayer.feature.refresh();
		if(!(typeof legend ==="undefined"||null===legend)){
            legend.refresh();
			return;
		}else if(!(typeof iMLegend ==="undefined"||null===iMLegend)){
            iMLegend.refresh();
            return;
		}
	})
	edited=true;
}

//关闭图层编辑信息面板
var closeFeatureLayerPanel = function () {
    clearFeatureInteraction();
    openFeatureLayerPanelContent(null)
};

//解除事件绑定
var clearFeatureInteraction = function () {
    var obj1 = document.getElementById('map-featureLayer-info');
    removeClass(obj1, "show");
};


//打开信息面板
var openFeatureLayerPanelContent = function (idList) {
    var obj0 = document.getElementById('map-featureLayer-panel');
    removeClass(obj0, "show");
    var obj1 = document.getElementById('map-featureLayer-info');
    removeClass(obj1, "show");
    var obj2 = document.getElementById('map-featureLayer-panel-tip');
    removeClass(obj2, "show");
    var obj3 = document.getElementById('pointFeatureLayer-style-edit');
    removeClass(obj3, "show");
    var obj4 = document.getElementById('lineFeatureLayer-style-edit');
    removeClass(obj4, "show");
    var obj5 = document.getElementById('polygonFeatureLayer-style-edit');
    removeClass(obj5, "show");
    if (idList) {
        for (var index = 0; index < idList.length; index++) {
            var id = idList[index];
            var obj = document.getElementById(id);
            addClass(obj, "show")
        }
    }
};


//返回当前的样式信息 会暴露给其他页面
var getRendererNow = function(){
	if (rendererNow){
		return rendererNow;
	}else{
		return false;
	}
	
};


//是否已经编辑过
var isEdited = function(){
return edited;
};

//Edited开关
var closeEdited = function(){
		edited = false;
}
