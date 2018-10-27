var point = 'POINT';
var polyline = 'POLYLINE';
var freehand_polyline = 'FREEHAND_POLYLINE';
var polygon = 'POLYGON';
var freehand_polygon = 'FREEHAND_POLYGON';
var rectangle = 'RECTANGLE';
var circle = 'CIRCLE';
var ellipse = 'ELLIPSE';
var arrow = 'ARROW';
var triangle ='TRIANGLE';
var edit = 'EDIT';

//滑动图名图例
function slideInfos(d,px){
	if(d==1){
        $("#mapNameInfo").animate({right:px+"px"},500);
        $("#info").animate({right:px+"px"},500);
	}else if(d==-1){
        $("#mapNameInfo").animate({left:px+"px"},500);
        $("#info").animate({left:px+"px"},500);
	}else{
		return;
	}
}
//弹出标绘菜单
function showPlotDiv(){
    hideSliderDiv();
    // $("#mapNameInfo").animate({right:"482px"},500);
    // $("#info").animate({right:"482px"},500);
    slideInfos(1,482);
    if(!($("#nav-plot").length >0)){//判断是否已经生成
		creatPlotDiv(); //动态生成标绘面板
	}
	if($("#nav-plot").width()==239){//判断是否已经弹出
		return;
	}
	$("#nav-plot").animate({width:"185px"},50);
	$("#nav-plot").animate({width:"240px"},500);

};

//创建div
function creatPlotDiv(){
	var html = "<ul id='accordion-plot'class='accordion'><li id='plot-point'><div class='link'><i class='fa fa-map'aria-hidden='true'></i>点符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(point)'>点</a></li><li><a href='#'>待完善1</a></li><li><a href='#'>待完善2</a></li></ul></li><li id='plot-polyline'><div class='link'><i class='fa fa-bandcamp'aria-hidden='true'></i>线符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(polyline)'>折线</a></li><li><a href='#'onclick='marking(freehand_polyline)'>自由线</a></li><li><a href='#'>待完善1</a></li><li><a href='#'>待完善2</a></li></ul></li><li id='plot-polygon'><div class='link'><i class='fa fa-pie-chart'aria-hidden='true'></i>面符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(polygon)'>多边形</a></li><li><a href='#'onclick='marking(freehand_polygon)'>自由面</a></li><li><a href='#'onclick='marking(rectangle)'>矩形</a></li><li><a href='#'onclick='marking(circle)'>圆</a></li><li><a href='#'onclick='marking(ellipse)'>椭圆</a></li><li><a href='#'onclick='marking(arrow)'>箭头</a></li><li><a href='#'onclick='marking(triangle)'>三角形</a></li><li><a href='#'>待完善1</a></li></ul></li><li id='plot-tools'><div class='link'><i class='fa fa-paint-brush'aria-hidden='true'></i>工具<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(edit)'>编辑</a></li><li><a href='#' onclick='addMeasureInteraction()'>测量</a></li></ul></li></ul>";
	var link = document.createElement('div');
    link.setAttribute('id','nav-plot'); 
    link.innerHTML = html;  
    document.body.appendChild(link); 
    var accordion = new Accordion($('#accordion-plot'), true);//风琴菜单交互
    //动态添加标绘样式修改面板
    var html2 = [
    		'<div class="map-marking-panel-head">',
    			'<label id="map-marking-panel-title">绘制点标注</label>',
    			'<i class="fa fa-close icon-close" onclick="closeMarkingPanel()"></i>',
    		'</div>',
    		'<div class="map-marking-panel-container">',
    			'<div id="map-marking-panel-tip" class="map-marking-panel-tip">地图上待标注位置鼠标左键点击地图添加一个点标注，可填写名称、备注、图标，点击保存生效！</div>',
    
    			//编辑
    			'<div id="map-mark-edit" class="map-mark-edit">',
    				'<button class="layui-btn layui-btn-small " onclick="addSelectInteraction()" title="选择">',
    					'<i class="fa fa-mouse-pointer"></i>',
    				'</button>',
    				'<button class="layui-btn layui-btn-small " onclick="addModifyInteraction()" title="编辑">',
    					'<i class="fa fa-pencil"></i>',
    				'</button>',
    				'<button class="layui-btn layui-btn-small layui-btn-warm" onclick="addDeleteInteraction()" title="删除">',
    					'<i class="fa fa-trash"></i>',
    				'</button>',
    				'<button class="layui-btn layui-btn-small layui-btn-danger" onclick="addAllDeleteInteraction()" title="删除所有">',
    					'<i class="fa fa-eraser"></i>',
    				'</button>',
    				'<fieldset class="layui-elem-field layui-field-title">',
    					'<legend>操作提示</legend>',
    					'<div id="map-mark-edit-tip" class="layui-field-box">',
    
    					'</div>',
    				'</fieldset>',
    			'</div>',
    
    			//标注
    			'<div class="map-marking-info" id="map-marking-info">',
    				'<div id="map-marking-info-container" class="layui-form map-marking-info-container">',
    					'<div class="layui-form-item">',
    						'<label class="layui-form-label">名称</label>',
    						'<div class="layui-input-block">',
    							'<input type="text" id="mark-name" placeholder="请输入标注名称" autocomplete="off" class="layui-input">',
    						'</div>',
    					'</div>',
    					'<div class="layui-form-item layui-form-text">',
    						'<label class="layui-form-label">备注</label>',
    						'<div class="layui-input-block">',
    							'<textarea id="mark-remark" placeholder="请输入标注备注" class="layui-textarea"></textarea>',
    						'</div>',
    					'</div>',
    					'<div class="layui-form-item mark-style" id="point-style-item">',
    						'<label class="layui-form-label">图标</label>',
    						'<div class="layui-input-block">',
    							'<img id="pointIcon" src="assets/img/pointIcon/8.png">',
    							'<button class="layui-btn layui-btn-mini layui-btn-normal" onclick="changeIcon()">更换</button>',
    						'</div>',
    
    					'</div>',
    					'<div class="layui-form-item mark-style" id="line-style-item">',
    						'<label class="layui-form-label">线样式</label>',
    						'<div class="layui-input-block">',
    							'<div id="line-style" class="line-style"></div>',
    							'<button class="layui-btn layui-btn-mini layui-btn-normal" onclick="changeLineStyle()">更换</button>',
    						'</div>',
    					'</div>',
    					'<div class="layui-form-item mark-style" id="polygon-style-item">',
    						'<label class="layui-form-label">面样式</label>',
    						'<div class="layui-input-block">',
    							'<div id="polygon-style" class="polygon-style"></div>',
    							'<button class="layui-btn layui-btn-mini layui-btn-normal" onclick="changePolygonStyle()">更换</button>',
    						'</div>',
    					'</div>',
    					'<div class="layui-form-item">',
    						'<div class="layui-input-block">',
    							'<button class="layui-btn " onclick="infoSave()">保存</button>',
    							'<!--<button class="layui-btn layui-btn-danger" onclick="infoDelete()">删除</button>-->',
    						'</div>',
    					'</div>',
    				'</div>',
    			'</div>',
    
    			//点图标面板
    			'<div class="point-style-edit" id="point-style-edit">',
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
    				'<button class="layui-btn  margin-10" onclick="changeIconBack()">返回</button>',
    			'</div>',
    
    			//线样式面板
    			'<div class="line-style-edit" id="line-style-edit">',
    				'<div class="style-preview">',
    					'<div id="line-style-preview" class="line-style-preview"></div>',
    				'</div>',
    				'<div class="layui-form-item layui-form-text">',
    					'<label class="layui-form-label">线颜色</label>',
    					'<div class="layui-input-inline">',
    						'<input id="line-color" type = "color" class="layui-input input-color" onchange="lineStyleChange()" value="#ff4500">',
    					'</div>',
    				'</div>',
    				'<div class="layui-form-item">',
    					'<label class="layui-form-label">线透明度</label>',
    					'<div class="layui-input-inline">',
    						'<input type="range" id="line-opacity" min="0" max="1" step="0.1" class="layui-input input-range" onchange="lineStyleChange()">',
    					'</div>',
    					'<div class="layui-form-mid layui-word-aux">0:完全透明,1:不透明</div>',
    				'</div>',
    				'<div class="layui-form-item">',
    					'<label class="layui-form-label">线宽</label>',
    					'<div class="layui-input-inline">',
    						'<input type="number" id="line-width" class="layui-input input-number" onchange="lineStyleChange()">',
    					'</div>',
    					'<div class="layui-form-mid layui-word-aux">(单位：像素)</div>',
    				'</div>',
    				'<div class="layui-form-item">',
    					'<div class="layui-input-block">',
    						'<button class="layui-btn " onclick="changeLineStyleBack()">返回</button>',
    						'<button class="layui-btn " onclick="saveLineStyle()">保存</button>',
    					'</div>',
    				'</div>',
    			'</div>',
    
    			//面样式面板
    			'<div class="polygon-style-edit" id="polygon-style-edit">',
    				'<div class="style-preview">',
    					'<div id="polygon-style-preview" class="polygon-style-preview"></div>',
    				'</div>',
    				'<div class="layui-form-item layui-form-text">',
    					'<label class="layui-form-label">填充色</label>',
    					'<div class="layui-input-inline">',
    						'<input id="polygon-fill-color"  type = "color" class="layui-input input-color" onchange="polygonStyleChange()" value="#ff4500">',
    					'</div>',
    				'</div>',
    				'<div class="layui-form-item">',
    					'<label class="layui-form-label">填充透明度</label>',
    					'<div class="layui-input-inline">',
    						'<input type="range" id="polygon-fill-opacity" min="0" step="0.1" max="1" class="layui-input input-range" onchange="polygonStyleChange()">',
    					'</div>',
    					'<div class="layui-form-mid layui-word-aux">0:完全透明,1:不透明</div>',
    				'</div>',
    				'<div class="layui-form-item layui-form-text">',
    					'<label class="layui-form-label">边框颜色</label>',
    					'<div class="layui-input-inline">',
    						'<input id="polygon-border-color" type="color" class="layui-input input-color" onchange="polygonStyleChange()" value="FFD700">',
    					'</div>',
    				'</div>',
    				'<div class="layui-form-item">',
    					'<label class="layui-form-label">边框透明度</label>',
    					'<div class="layui-input-inline">',
    						'<input type="range" id="polygon-border-opacity" min="0" step="0.1" max="1" class="layui-input input-range" onchange="polygonStyleChange()">',
    					'</div>',
    					'<div class="layui-form-mid layui-word-aux">(0-1)0:完全透明,1:不透明</div>',
    				'</div>',
    				'<div class="layui-form-item">',
    					'<label class="layui-form-label">边框宽</label>',
    					'<div class="layui-input-inline">',
    						'<input type="number" id="polygon-border-width" class="layui-input input-number" onchange="polygonStyleChange()">',
    					'</div>',
    					'<div class="layui-form-mid layui-word-aux">(单位：像素)</div>',
    				'</div>',
    				'<div class="layui-form-item">',
    					'<div class="layui-input-block">',
    						'<button class="layui-btn " onclick="changePolygonStyleBack()">返回</button>',
    						'<button class="layui-btn " onclick="savePolygonStyle()">保存</button>',
    					'</div>',
    				'</div>',
    			'</div>',
    			//测量信息面板
    			'<div class="measure-info" id="measure-info">',
    				'<div id="measurementDiv"></div>',
    			'</div>',
    		'</div>'
    ].join('');
    var link2 = document.createElement('div');
    link2.setAttribute('id','map-marking-panel'); 
    link2.setAttribute('class','map-marking-panel'); 
    link2.innerHTML = html2;  
    document.body.appendChild(link2); 
};

//风琴菜单
var Accordion = function (el, multiple) {
    this.el = el || {};
    this.multiple = multiple || false;
    var links = this.el.find('.link');
    links.on('click', {
        el: this.el,
        multiple: this.multiple
    }, this.dropdown);
};

//添加方法
Accordion.prototype.dropdown = function (e) {
	var opened = $("#accordion-plot .open"); //其他的二级菜单关闭
	if(opened.length > 0) {
		var checked = opened[0];
	};
	var $el = e.data.el;
	$this = $(this), $next = $this.next();
	if(checked&&$this.parent().attr('id') == checked.id) {
		$next.slideToggle();
		$this.parent().toggleClass('open');
		if(!e.data.multiple) {
			$el.find('.submenu').not($next).slideUp().parent().removeClass('open');
		} 
	}else {
			$("#accordion-plot .submenu").hide(300); //其他的二级菜单关闭
			$("#accordion-plot .open").removeClass('open');
			$next.slideToggle();
			$this.parent().toggleClass('open');
			if(!e.data.multiple) {
			$el.find('.submenu').not($next).slideUp().parent().removeClass('open');
			}
		};
		closeMarkingPanel();
};

//隐藏标绘菜单
function hidePlotDiv(){
	try {
        closeMarkingPanel();
    }catch (e) {
		return;
    }
	if($("#nav-plot").width()==0){//判断是否已经隐藏
		return;
	}
	$("#nav-plot").animate({width:"185px"},500);
	$("#nav-plot").animate({width:"0px"},50);
    // $("#mapNameInfo").animate({right:"242px"},500);
    // $("#info").animate({right:"242px"},500);
    slideInfos(1,242);
};
