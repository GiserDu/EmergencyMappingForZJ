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
var textMarker = 'TEXT'
var isPlotIconCreated=false;

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
	var html = "<ul id='accordion-plot'class='accordion'><li id='plot-point'><div class='link'><i class='fa fa-map'aria-hidden='true'></i>点符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(point)'>点</a></li></ul></li><li id='plot-polyline'><div class='link'><i class='fa fa-bandcamp'aria-hidden='true'></i>线符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(polyline)'>折线</a></li><li><a href='#'onclick='marking(freehand_polyline)'>自由线</a></li></ul></li><li id='plot-polygon'><div class='link'><i class='fa fa-pie-chart'aria-hidden='true'></i>面符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(polygon)'>多边形</a></li><li><a href='#'onclick='marking(freehand_polygon)'>自由面</a></li><li><a href='#'onclick='marking(rectangle)'>矩形</a></li><li><a href='#'onclick='marking(circle)'>圆</a></li><li><a href='#'onclick='marking(ellipse)'>椭圆</a></li><li><a href='#'onclick='marking(arrow)'>箭头</a></li><li><a href='#'onclick='marking(triangle)'>三角形</a></li></ul></li><li id='plot-tools'><div class='link'><i class='fa fa-paint-brush'aria-hidden='true'></i>工具<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(edit)'>编辑</a></li><li><a href='#' onclick='addMeasureInteraction()'>测量</a></li></ul></li></ul>";
	var link = document.createElement('div');
    link.setAttribute('id','nav-plot'); 
    link.innerHTML = html;  
    document.body.appendChild(link); 
    var accordion = new Accordion($('#accordion-plot'), true);//风琴菜单交互
};
creatEditDiv();
function creatEditDiv() {
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
        '<legend style="margin: 0px;padding:0px; ">操作提示</legend>',
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
        '<div class="layui-collapse" lay-accordion="">\n' +
        '    <div class="layui-colla-item plotItem">\n' +
        '        <h2 class="layui-colla-title">默认样式</h2>\n' +
        '        <div class="layui-colla-content layui-show">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item plotItem">\n' +
        '        <h2 class="layui-colla-title">避难场所</h2>\n' +
        '        <div class="layui-colla-content ">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item plotItem">\n' +
        '        <h2 class="layui-colla-title">防护目标</h2>\n' +
        '        <div class="layui-colla-content" >\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item plotItem">\n' +
        '        <h2 class="layui-colla-title">交通工具</h2>\n' +
        '        <div class="layui-colla-content">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item plotItem">\n' +
        '        <h2 class="layui-colla-title">救援队伍</h2>\n' +
        '        <div class="layui-colla-content" >\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item plotItem">\n' +
        '        <h2 class="layui-colla-title">事件类型</h2>\n' +
        '        <div class="layui-colla-content">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item plotItem">\n' +
        '        <h2 class="layui-colla-title">危险源</h2>\n' +
        '        <div class="layui-colla-content">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item plotItem">\n' +
        '        <h2 class="layui-colla-title">物资储备库</h2>\n' +
        '        <div class="layui-colla-content">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item plotItem">\n' +
        '        <h2 class="layui-colla-title">医疗单位</h2>\n' +
        '        <div class="layui-colla-content">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item plotItem">\n' +
        '        <h2 class="layui-colla-title">应急通信资源</h2>\n' +
        '        <div class="layui-colla-content">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '</div>',
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
        '<input type="range" id="line-opacity" min="0" max="1" step="0.1" class="input-range" onchange="lineStyleChange()">',
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
        '<input type="range" id="polygon-fill-opacity" min="0" step="0.1" max="1" class="input-range" onchange="polygonStyleChange()">',
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
        '<input type="range" id="polygon-border-opacity" min="0" step="0.1" max="1" class="input-range" onchange="polygonStyleChange()">',
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
}

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


//动态加载标绘的点图标面板
var loadIconPanel=function(){
    var tbPath="assets/img/";
    var symbolClass=["mrys","bncs","fhmb","jtgj","jydw","sjlx","wxy","wzcbk","yldw","yjtxzy"];

    var symbolName=[
        ["1","10","11","12","13","14","15","16","17","18","19","2","20","21","22","23","24","25","3","4","5","6","7","8","9"],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","人防工事-0_3","公园-0_3","其它人防工事-0_3","其它避护场所-0_3","广场-0_3","应急避护场区-0_3","救助管理站-0_3","绿地-0_3","防空地下室-0_3","防空洞-0_3"],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","中等职业教育学校-0_3","其它学校-0_3","其它重要生态区-0_3","其它防护目标-0_3","初级中学-0_3","变电所-0_3","地下水监测站-0_3","地表水、饮用水水质监测点-0_3","堤防工程-0_3","小学-0_3","居民地注记-0_3","幼儿园-0_3","拦河水闸工程-0_3","普通高中-0_3","森林-0_3","植物园-0_3","气象监测台站-0_3","水库大坝-0_3","水源地-0_3","消防安全重点单位-0_3","消防栓-0_3","湿地-0_3","灌区-0_3","部门级科研机构-0_3","重要生态区-0_3","重要部位-0_3","野生动物保护管理场所-0_3","高等学校-0_3"],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","其它运输保障机构-0_3","汽车运输企业-0_3","航空企业-0_3","航运企业-0_3","运输保障机构-0_3","铁路运输企业-0_3"],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","专业救援队伍-0_3","公共卫生类专家-0_3","公安消防部队-0_3","公安警察-0_3","其它专业救援队伍-0_3","其它武警部队-0_3","其它警察-0_3","医疗卫生资源-0_3","医疗救援队-0_3","危险化学品事故专业救援队-0_3","地震救援队-0_3","地震灾害紧急救援队-0_3","应急人力资源-0_3","应急志愿者-0_3","林业公安-0_3","武警-0_3","矿山事故救援队-0_3","重大动物疫病应急预备队-0_3","陆地搜寻与救护队-0_3","食物中毒事件应急预备队-0_3"],
        ["11000-1_3","11000-2_3","11000-3_3","11000-4_3","11A00-1_3","11A00-2_3","11A00-3_3","11A00-4_3","11A52-1_3","11B00-4_3","11B03-5_3","11C00_3","11D00-1_3","11D03-2_3","11E00-4_3","11G01-2_3","11G05-4_3","12A00-3_3","12C00-1_3","12D00-3_3","12E00_3","12G00-2_3","12H00-3_3","12H01-4_3","12H02-5_3","12Q00-1_3","13000-2_3","13A00_3","14000-1_3","14A00-4_3","14F00-2_3","14G00-3_3","default-0_3","def_3","专用应急物资及储备_3","其它重要部位_3","国内恐怖组织_3","学校_3","武警_3","水运交通基础设施_3","疾病预防控制中心（防疫站）_3","运输站场_3","金融机构_3"],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","事故灾难危险源-0_3","公共卫生危险源-0_3","其他生产安全重点单位-0_3","其它事故灾难安全危险源-0_3","其它危险源和风险隐患区-0_3","其它气象灾害风险区-0_3","其它生产安全危险源-0_3","其它社会安全隐患-0_3","农业生物灾害风险区-0_3","冰雹灾害风险区-0_3","加油站-1_3","医疗机构类危险源-1_3","危险化学品使用单位-1_3","危险化学品生产企业-1_3","危险化学品经营单位-1_3","危险性外来入侵生物风险区-1_3","大风灾害风险区-1_3","库区（库）-1_3","暴雨灾害风险区-1_3","气瓶充装站-1_3","污染源-1_3","沙尘暴灾害风险区-1_3","油气田-1_3","烟花爆竹生产企业-1_3","生产场所-1_3","社会安全隐患-1_3","自然灾害风险隐患区-1_3","贮罐区（贮罐）-1_3","雪灾风险区-1_3","雷电灾害风险区-1_3","霜冻灾害风险区-1_3","高温灾害风险区-1_3"],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","储备物资库-0_3","重要场所-0_3"],
        ["default-0_3","default-10_3","default-11_3","default-12_3","default-1_3","default-2_3","default-3_3","default-4_3","专科疾病防治院（所、站）-10_3","专科疾病防治院（所、站）-5_3","其它医疗机构-11_3","其它医疗机构-6_3","其它卫生监督机构-12_3","其它卫生监督机构-7_3","其它疾病预防控制机构-1_3","其它疾病预防控制机构-8_3","医学科学研究机构-2_3","医学科学研究机构-9_3","医疗卫生资源-3_3","医疗机构-0_3","医疗机构-4_3","医院-10_3","医院-5_3","卫生监督所（局）-11_3","卫生监督所（局）-6_3","卫生院-12_3","卫生院-7_3","县级卫生监督所（局）-1_3","县级卫生监督所（局）-8_3","县级疾病预防控制中心（防疫站）-2_3","县级疾病预防控制中心（防疫站）-9_3","国家级卫生监督机构-3_3","国家级疾病预防控制中心-0_3","国家级疾病预防控制中心-4_3","妇幼保健院（所、站）-10_3","妇幼保健院（所、站）-5_3","市级卫生监督所（局）-11_3","市级卫生监督所（局）-6_3","市级疾病预防控制中心-12_3","市级疾病预防控制中心-7_3","急救中心（站）-1_3","急救中心（站）-8_3","疗养院-2_3","疗养院-9_3","疾病预防控制中心（防疫站）-3_3","省级卫生监督所（局）-0_3","省级卫生监督所（局）-4_3","省级疾病预防控制中心-10_3","省级疾病预防控制中心-5_3","社区卫生服务中心（站）-11_3","社区卫生服务中心（站）-6_3","职业病防治院（所、站、中心）-12_3","职业病防治院（所、站、中心）-7_3","诊所、卫生所（室）、医务室-1_3","诊所、卫生所（室）、医务室-8_3","门诊部-2_3","门诊部-9_3"],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","交通通信中心-0_3","党政专用通信保障机构-0_3","其它通信保障机构-0_3","基础电信运营企业地市分公司-0_3","基础电信运营企业省公司-0_3","基础电信运营企业集团公司-0_3","电信运营企业-0_3","移动运营企业-0_3","联通运营企业-0_3","通信保障机构-0_3","铁通运营企业-0_3"]
    ];


    var  collaItem=document.getElementsByClassName('plotItem');

    for (var i=0;i<collaItem.length;i++){
        //获取到ul节点添加li
        var ulDom=collaItem[i].children[1].children[0];
        ulDom.innerHTML="";
        for (j=0;j<symbolName[i].length;j++){
            // alert(j);
            var liElement=document.createElement('li');
            var imgElement=document.createElement('img');

            liElement.setAttribute('data-toogle','tooltip');
            liElement.setAttribute('title',symbolName[i][j]);

            imgElement.setAttribute('src',tbPath+symbolClass[i]+'/'+symbolName[i][j]+'.png');
            imgElement.setAttribute('class','emergencyPlotIcon');


            ulDom.appendChild(liElement).appendChild(imgElement);
        }
    }
    isPlotIconCreated = true;
}

if(!isPlotIconCreated){
    loadIconPanel();
}

