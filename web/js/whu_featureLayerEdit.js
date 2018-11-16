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
    //点符号面板
    crearFeatureIconList();
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
        '<img id="pointFeatureLayerIcon" src="assets/img/mrys/8.png">',
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
        // '<div class="point-style-edit" id="pointFeatureLayer-style-edit">',
        // '<ul>',
        // '<li>',
        // '<img src="assets/img/pointIcon/1.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/2.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/3.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/4.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/5.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/6.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/7.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/8.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/9.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/10.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/11.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/12.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/13.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/14.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/15.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/16.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/17.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/18.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/19.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/20.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/21.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/22.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/23.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/24.png">',
        // '</li>',
        // '<li>',
        // '<img src="assets/img/pointIcon/25.png">',
        // '</li>',
        // '</ul>',
        //

        '<div class="point-style-edit" id="pointFeatureLayer-style-edit">',
        '<div class="layui-collapse" lay-accordion="">\n' +
        '    <div class="layui-colla-item">\n' +
        '        <h2 class="layui-colla-title">默认样式</h2>\n' +
        '        <div class="layui-colla-content layui-show" id="default-symbol">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item">\n' +
        '        <h2 class="layui-colla-title">避难场所</h2>\n' +
        '        <div class="layui-colla-content " id="shelter-symbol">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item">\n' +
        '        <h2 class="layui-colla-title">防护目标</h2>\n' +
        '        <div class="layui-colla-content" id="defense-object-symbol">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item">\n' +
        '        <h2 class="layui-colla-title">交通工具</h2>\n' +
        '        <div class="layui-colla-content" id="vehicle-symbol">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item">\n' +
        '        <h2 class="layui-colla-title">救援队伍</h2>\n' +
        '        <div class="layui-colla-content" id="rescue-team-symbol">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item">\n' +
        '        <h2 class="layui-colla-title">事件类型</h2>\n' +
        '        <div class="layui-colla-content" id="incident-type-symbol">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item">\n' +
        '        <h2 class="layui-colla-title">危险源</h2>\n' +
        '        <div class="layui-colla-content" id="hazard-source-symbol">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item">\n' +
        '        <h2 class="layui-colla-title">物资储备库</h2>\n' +
        '        <div class="layui-colla-content" id="Reserve-store-symbol">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item">\n' +
        '        <h2 class="layui-colla-title">医疗单位</h2>\n' +
        '        <div class="layui-colla-content" id="medical-symbol">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="layui-colla-item">\n' +
        '        <h2 class="layui-colla-title">应急通信资源</h2>\n' +
        '        <div class="layui-colla-content" id="communication-resources-symbol">\n' +
        '            <ul></ul>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '</div>',
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

    //手风琴面板需要调用element元素
    layui.use(['element', 'layer'], function(){
        var element = layui.element;
        element.init();
        var layer = layui.layer;

        //监听折叠
        element.on('collapse(test)', function(data){
            layer.msg('展开状态：'+ data.show);
            alert("hh");
        });
    });
}

//点符号面板
var crearFeatureIconList=function () {

    var tbPath="assets/img/";
    var symbolClass=["mrys","bncs","fhmb","jtgj","jydw","sjlx","wxy","wzcbk","yldw","yjtxzy"];

    var symbolName=[
        ["1","10","11","12","13","14","15","16","17","18","19","2","20","21","22","23","24","25","3","4","5","6","7","8","9",],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","人防工事-0_3","人防工事-1_3","人防工事-2_3","人防工事-3_3","人防工事-4_3","人防工事-5_3","人防工事-6_3","公园-0_3","公园-1_3","公园-2_3","公园-3_3","公园-4_3","公园-5_3","公园-6_3","其它人防工事-0_3","其它人防工事-1_3","其它人防工事-2_3","其它人防工事-3_3","其它人防工事-4_3","其它人防工事-5_3","其它人防工事-6_3","其它避护场所-0_3","其它避护场所-1_3","其它避护场所-2_3","其它避护场所-3_3","其它避护场所-4_3","其它避护场所-5_3","其它避护场所-6_3","广场-0_3","广场-1_3","广场-2_3","广场-3_3","广场-4_3","广场-5_3","广场-6_3","应急避护场区-0_3","应急避护场区-1_3","应急避护场区-2_3","应急避护场区-3_3","应急避护场区-4_3","应急避护场区-5_3","应急避护场区-6_3","救助管理站-0_3","救助管理站-1_3","救助管理站-2_3","救助管理站-3_3","救助管理站-4_3","救助管理站-5_3","救助管理站-6_3","绿地-0_3","绿地-1_3","绿地-2_3","绿地-3_3","绿地-4_3","绿地-5_3","绿地-6_3","防空地下室-0_3","防空地下室-1_3","防空地下室-2_3","防空地下室-3_3","防空地下室-4_3","防空地下室-5_3","防空地下室-6_3","防空洞-0_3","防空洞-1_3","防空洞-2_3","防空洞-3_3","防空洞-4_3","防空洞-5_3","防空洞-6_3"],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","中等职业教育学校-0_3","中等职业教育学校-1_3","中等职业教育学校-2_3","中等职业教育学校-3_3","中等职业教育学校-4_3","中等职业教育学校-5_3","中等职业教育学校-6_3","其它学校-0_3","其它学校-1_3","其它学校-2_3","其它学校-3_3","其它学校-4_3","其它学校-5_3","其它学校-6_3","其它重要生态区-0_3","其它重要生态区-1_3","其它重要生态区-2_3","其它重要生态区-3_3","其它重要生态区-4_3","其它重要生态区-5_3","其它重要生态区-6_3","其它防护目标-0_3","其它防护目标-1_3","其它防护目标-2_3","其它防护目标-3_3","其它防护目标-4_3","其它防护目标-5_3","其它防护目标-6_3","初级中学-0_3","初级中学-1_3","初级中学-2_3","初级中学-3_3","初级中学-4_3","初级中学-5_3","初级中学-6_3","变电所-0_3","变电所-1_3","变电所-2_3","变电所-3_3","变电所-4_3","变电所-5_3","变电所-6_3","地下水监测站-0_3","地下水监测站-1_3","地下水监测站-2_3","地下水监测站-3_3","地下水监测站-4_3","地下水监测站-5_3","地下水监测站-6_3","地表水、饮用水水质监测点-0_3","地表水、饮用水水质监测点-1_3","地表水、饮用水水质监测点-2_3","地表水、饮用水水质监测点-3_3","地表水、饮用水水质监测点-4_3","地表水、饮用水水质监测点-5_3","地表水、饮用水水质监测点-6_3","堤防工程-0_3","堤防工程-1_3","堤防工程-2_3","堤防工程-3_3","堤防工程-4_3","堤防工程-5_3","堤防工程-6_3","小学-0_3","小学-1_3","小学-2_3","小学-3_3","小学-4_3","小学-5_3","小学-6_3","居民地注记-0_3","居民地注记-1_3","居民地注记-2_3","居民地注记-3_3","居民地注记-4_3","居民地注记-5_3","居民地注记-6_3","幼儿园-0_3","幼儿园-1_3","幼儿园-2_3","幼儿园-3_3","幼儿园-4_3","幼儿园-5_3","幼儿园-6_3","拦河水闸工程-0_3","拦河水闸工程-1_3","拦河水闸工程-2_3","拦河水闸工程-3_3","拦河水闸工程-4_3","拦河水闸工程-5_3","拦河水闸工程-6_3","普通高中-0_3","普通高中-1_3","普通高中-2_3","普通高中-3_3","普通高中-4_3","普通高中-5_3","普通高中-6_3","森林-0_3","森林-1_3","森林-2_3","森林-3_3","森林-4_3","森林-5_3","森林-6_3","植物园-0_3","植物园-1_3","植物园-2_3","植物园-3_3","植物园-4_3","植物园-5_3","植物园-6_3","气象监测台站-0_3","气象监测台站-1_3","气象监测台站-2_3","气象监测台站-3_3","气象监测台站-4_3","气象监测台站-5_3","气象监测台站-6_3","水库大坝-0_3","水库大坝-1_3","水库大坝-2_3","水库大坝-3_3","水库大坝-4_3","水库大坝-5_3","水库大坝-6_3","水源地-0_3","水源地-1_3","水源地-2_3","水源地-3_3","水源地-4_3","水源地-5_3","水源地-6_3","消防安全重点单位-0_3","消防安全重点单位-1_3","消防安全重点单位-2_3","消防安全重点单位-3_3","消防安全重点单位-4_3","消防安全重点单位-5_3","消防安全重点单位-6_3","消防栓-0_3","消防栓-1_3","消防栓-2_3","消防栓-3_3","消防栓-4_3","消防栓-5_3","消防栓-6_3","湿地-0_3","湿地-1_3","湿地-2_3","湿地-3_3","湿地-4_3","湿地-5_3","湿地-6_3","灌区-0_3","灌区-1_3","灌区-2_3","灌区-3_3","灌区-4_3","灌区-5_3","灌区-6_3","部门级科研机构-0_3","部门级科研机构-1_3","部门级科研机构-2_3","部门级科研机构-3_3","部门级科研机构-4_3","部门级科研机构-5_3","部门级科研机构-6_3","重要生态区-0_3","重要生态区-1_3","重要生态区-2_3","重要生态区-3_3","重要生态区-4_3","重要生态区-5_3","重要生态区-6_3","重要部位-0_3","重要部位-1_3","重要部位-2_3","重要部位-3_3","重要部位-4_3","重要部位-5_3","重要部位-6_3","野生动物保护管理场所-0_3","野生动物保护管理场所-1_3","野生动物保护管理场所-2_3","野生动物保护管理场所-3_3","野生动物保护管理场所-4_3","野生动物保护管理场所-5_3","野生动物保护管理场所-6_3","高等学校-0_3","高等学校-1_3","高等学校-2_3","高等学校-3_3","高等学校-4_3","高等学校-5_3","高等学校-6_3"],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","其它运输保障机构-0_3","其它运输保障机构-1_3","其它运输保障机构-2_3","其它运输保障机构-3_3","其它运输保障机构-4_3","其它运输保障机构-5_3","其它运输保障机构-6_3","汽车运输企业-0_3","汽车运输企业-1_3","汽车运输企业-2_3","汽车运输企业-3_3","汽车运输企业-4_3","汽车运输企业-5_3","汽车运输企业-6_3","航空企业-0_3","航空企业-1_3","航空企业-2_3","航空企业-3_3","航空企业-4_3","航空企业-5_3","航空企业-6_3","航运企业-0_3","航运企业-1_3","航运企业-2_3","航运企业-3_3","航运企业-4_3","航运企业-5_3","航运企业-6_3","运输保障机构-0_3","运输保障机构-1_3","运输保障机构-2_3","运输保障机构-3_3","运输保障机构-4_3","运输保障机构-5_3","运输保障机构-6_3","铁路运输企业-0_3","铁路运输企业-1_3","铁路运输企业-2_3","铁路运输企业-3_3","铁路运输企业-4_3","铁路运输企业-5_3","铁路运输企业-6_3"],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","专业救援队伍-0_3","专业救援队伍-1_3","专业救援队伍-2_3","专业救援队伍-3_3","专业救援队伍-4_3","专业救援队伍-5_3","专业救援队伍-6_3","公共卫生类专家-0_3","公共卫生类专家-1_3","公共卫生类专家-2_3","公共卫生类专家-3_3","公共卫生类专家-4_3","公共卫生类专家-5_3","公共卫生类专家-6_3","公安消防部队-0_3","公安消防部队-1_3","公安消防部队-2_3","公安消防部队-3_3","公安消防部队-4_3","公安消防部队-5_3","公安消防部队-6_3","公安警察-0_3","公安警察-1_3","公安警察-2_3","公安警察-3_3","公安警察-4_3","公安警察-5_3","公安警察-6_3","其它专业救援队伍-0_3","其它专业救援队伍-1_3","其它专业救援队伍-2_3","其它专业救援队伍-3_3","其它专业救援队伍-4_3","其它专业救援队伍-5_3","其它专业救援队伍-6_3","其它武警部队-0_3","其它武警部队-1_3","其它武警部队-2_3","其它武警部队-3_3","其它武警部队-4_3","其它武警部队-5_3","其它武警部队-6_3","其它警察-0_3","其它警察-1_3","其它警察-2_3","其它警察-3_3","其它警察-4_3","其它警察-5_3","其它警察-6_3","医疗卫生资源-0_3","医疗卫生资源-1_3","医疗卫生资源-2_3","医疗卫生资源-3_3","医疗卫生资源-4_3","医疗卫生资源-5_3","医疗卫生资源-6_3","医疗救援队-0_3","医疗救援队-1_3","医疗救援队-2_3","医疗救援队-3_3","医疗救援队-4_3","医疗救援队-5_3","医疗救援队-6_3","危险化学品事故专业救援队-0_3","危险化学品事故专业救援队-1_3","危险化学品事故专业救援队-2_3","危险化学品事故专业救援队-3_3","危险化学品事故专业救援队-4_3","危险化学品事故专业救援队-5_3","危险化学品事故专业救援队-6_3","地震救援队-0_3","地震救援队-1_3","地震救援队-2_3","地震救援队-3_3","地震救援队-4_3","地震救援队-5_3","地震救援队-6_3","地震灾害紧急救援队-0_3","地震灾害紧急救援队-1_3","地震灾害紧急救援队-2_3","地震灾害紧急救援队-3_3","地震灾害紧急救援队-4_3","地震灾害紧急救援队-5_3","地震灾害紧急救援队-6_3","应急人力资源-0_3","应急人力资源-1_3","应急人力资源-2_3","应急人力资源-3_3","应急人力资源-4_3","应急人力资源-5_3","应急人力资源-6_3","应急志愿者-0_3","应急志愿者-1_3","应急志愿者-2_3","应急志愿者-3_3","应急志愿者-4_3","应急志愿者-5_3","应急志愿者-6_3","林业公安-0_3","林业公安-1_3","林业公安-2_3","林业公安-3_3","林业公安-4_3","林业公安-5_3","林业公安-6_3","武警-0_3","武警-1_3","武警-2_3","武警-3_3","武警-4_3","武警-5_3","武警-6_3","矿山事故救援队-0_3","矿山事故救援队-1_3","矿山事故救援队-2_3","矿山事故救援队-3_3","矿山事故救援队-4_3","矿山事故救援队-5_3","矿山事故救援队-6_3","重大动物疫病应急预备队-0_3","重大动物疫病应急预备队-1_3","重大动物疫病应急预备队-2_3","重大动物疫病应急预备队-3_3","重大动物疫病应急预备队-4_3","重大动物疫病应急预备队-5_3","重大动物疫病应急预备队-6_3","陆地搜寻与救护队-0_3","陆地搜寻与救护队-1_3","陆地搜寻与救护队-2_3","陆地搜寻与救护队-3_3","陆地搜寻与救护队-4_3","陆地搜寻与救护队-5_3","陆地搜寻与救护队-6_3","食物中毒事件应急预备队-0_3","食物中毒事件应急预备队-1_3","食物中毒事件应急预备队-2_3","食物中毒事件应急预备队-3_3","食物中毒事件应急预备队-4_3","食物中毒事件应急预备队-5_3","食物中毒事件应急预备队-6_3"],
        ["11000-1_3","11000-2_3","11000-3_3","11000-4_3","11A00-1_3","11A00-2_3","11A00-3_3","11A00-4_3","11A00_3","11A01-1_3","11A01-2_3","11A01-3_3","11A01-4_3","11A01_3","11A52-1_3","11A52-2_3","11A52-3_3","11A52-4_3","11B00-1_3","11B00-2_3","11B00-3_3","11B00-4_3","11B00_3","11B03-0_3","11B03-1_3","11B03-2_3","11B03-3_3","11B03-4_3","11B03-5_3","11C00-0_3","11C00-1_3","11C00-2_3","11C00-3_3","11C00-4_3","11C00-5_3","11C00_3","11C02-0_3","11C02-1_3","11C02-2_3","11C02-3_3","11C02-4_3","11C02-5_3","11D00-1_3","11D00-2_3","11D00-3_3","11D00-4_3","11D00_3","11D03-0_3","11D03-1_3","11D03-2_3","11D03-3_3","11D03-4_3","11D03-5_3","11E00-1_3","11E00-2_3","11E00-3_3","11E00-4_3","11G00-1_3","11G00-2_3","11G00-3_3","11G00-4_3","11G00_3","11G01-1_3","11G01-2_3","11G01-3_3","11G01-4_3","11G01_3","11G05-1_3","11G05-2_3","11G05-3_3","11G05-4_3","12000-1_3","12000-2_3","12000-3_3","12000-4_3","12A00-1_3","12A00-2_3","12A00-3_3","12A00-4_3","12A00_3","12B00-1_3","12B00-2_3","12B00-3_3","12B00-4_3","12C00-1_3","12C00-2_3","12C00-3_3","12C00-4_3","12C00_3","12D00-1_3","12D00-2_3","12D00-3_3","12D00-4_3","12D00_3","12E00-1_3","12E00-2_3","12E00-3_3","12E00-4_3","12E00_3","12F00-1_3","12F00-2_3","12F00-3_3","12F00-4_3","12G00-0_3","12G00-1_3","12G00-2_3","12G00-3_3","12G00-4_3","12G00-5_3","12H00-0_3","12H00-1_3","12H00-2_3","12H00-3_3","12H00-4_3","12H00-5_3","12H01-0_3","12H01-1_3","12H01-2_3","12H01-3_3","12H01-4_3","12H01-5_3","12H02-0_3","12H02-1_3","12H02-2_3","12H02-3_3","12H02-4_3","12H02-5_3","12M00-1_3","12M00-2_3","12M00-3_3","12M00-4_3","12M00_3","12Q00-0_3","12Q00-1_3","12Q00-2_3","12Q00-3_3","12Q00-4_3","12Q00-5_3","12Q00_3","13000-1_3","13000-2_3","13000-3_3","13000-4_3","13A00-1_3","13A00-2_3","13A00-3_3","13A00-4_3","13A00_3","13C99-0_3","13C99-1_3","13C99-2_3","13C99-3_3","13C99-4_3","13C99-5_3","14000-1_3","14000-2_3","14000-3_3","14000-4_3","14A00-1_3","14A00-2_3","14A00-3_3","14A00-4_3","14B00-1_3","14B00-2_3","14B00-3_3","14B00-4_3","14F00-0_3","14F00-1_3","14F00-2_3","14F00-3_3","14F00-4_3","14F00-5_3","14F00_3","14G00-1_3","14G00-2_3","14G00-3_3","14G00-4_3","14G00_3","19000_3","41E00_3","41F00_3","41G00_3","default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default_3","def_3","event-1_3","event-2_3","people-1_3","people-2_3","专业救援队伍_3","专家_3","专用应急物资及储备_3","专项应急资金_3","党政军机关_3","公众聚集场所_3","公安警察_3","公路基础设施_3","其它社会安全隐患_3","其它重要部位_3","其它防护目标_3","其它非法组织_3","内涝灾害风险区_3","军队_3","办事机构_3","医疗机构_3","国内恐怖组织_3","国防目标_3","国际恐怖组织_3","地质灾害风险区_3","地震灾害风险区_3","城市基础设施_3","基本生活保障物资_3","学校_3","工作机构_3","指挥和综合协调机构_3","政府部门_3","新闻广播机构_3","森林火灾风险区_3","森林草原火灾风险区_3","武警_3","民航交通设施_3","气象灾害风险区_3","水利设施_3","水旱灾害风险区-1_3","水旱灾害风险区-2_3","水旱灾害风险区_3","水运交通基础设施_3","洪水灾害风险区_3","海洋灾害风险区_3","涉黑团体_3","生产安全危险源_3","生物灾害风险区_3","电力基础设施_3","疾病预防控制中心（防疫站）_3","病原微生物实验室类危险源_3","科研机构_3","草原火灾风险区_3","菌毒株保藏类危险源_3","贩毒组织_3","运输安全危险源_3","运输站场_3","运输设备_3","通信保障机构_3","通信系统_3","通信网_3","通信设备_3","避护场所_3","金融机构_3","铁路基础设施_3","领导机构_3"],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","事故灾难危险源-0_3","事故灾难危险源-1_3","事故灾难危险源-2_3","事故灾难危险源-3_3","事故灾难危险源-4_3","事故灾难危险源-5_3","事故灾难危险源-6_3","公共卫生危险源-0_3","公共卫生危险源-1_3","公共卫生危险源-2_3","公共卫生危险源-3_3","公共卫生危险源-4_3","公共卫生危险源-5_3","公共卫生危险源-6_3","其他生产安全重点单位-0_3","其他生产安全重点单位-1_3","其他生产安全重点单位-2_3","其他生产安全重点单位-3_3","其他生产安全重点单位-4_3","其他生产安全重点单位-5_3","其他生产安全重点单位-6_3","其它事故灾难安全危险源-0_3","其它事故灾难安全危险源-1_3","其它事故灾难安全危险源-2_3","其它事故灾难安全危险源-3_3","其它事故灾难安全危险源-4_3","其它事故灾难安全危险源-5_3","其它事故灾难安全危险源-6_3","其它危险源和风险隐患区-0_3","其它危险源和风险隐患区-1_3","其它危险源和风险隐患区-2_3","其它危险源和风险隐患区-3_3","其它危险源和风险隐患区-4_3","其它危险源和风险隐患区-5_3","其它危险源和风险隐患区-6_3","其它气象灾害风险区-0_3","其它气象灾害风险区-1_3","其它气象灾害风险区-2_3","其它气象灾害风险区-3_3","其它气象灾害风险区-4_3","其它气象灾害风险区-5_3","其它气象灾害风险区-6_3","其它生产安全危险源-0_3","其它生产安全危险源-1_3","其它生产安全危险源-2_3","其它生产安全危险源-3_3","其它生产安全危险源-4_3","其它生产安全危险源-5_3","其它生产安全危险源-6_3","其它社会安全隐患-0_3","其它社会安全隐患-1_3","其它社会安全隐患-2_3","其它社会安全隐患-3_3","其它社会安全隐患-4_3","其它社会安全隐患-5_3","其它社会安全隐患-6_3","农业生物灾害风险区-0_3","农业生物灾害风险区-1_3","农业生物灾害风险区-2_3","农业生物灾害风险区-3_3","农业生物灾害风险区-4_3","农业生物灾害风险区-5_3","农业生物灾害风险区-6_3","冰雹灾害风险区-0_3","冰雹灾害风险区-1_3","冰雹灾害风险区-2_3","冰雹灾害风险区-3_3","冰雹灾害风险区-4_3","冰雹灾害风险区-5_3","加油站-0_3","加油站-1_3","加油站-2_3","加油站-3_3","加油站-4_3","加油站-5_3","加油站-6_3","医疗机构类危险源-0_3","医疗机构类危险源-1_3","医疗机构类危险源-2_3","医疗机构类危险源-3_3","医疗机构类危险源-4_3","医疗机构类危险源-5_3","医疗机构类危险源-6_3","危险化学品使用单位-0_3","危险化学品使用单位-1_3","危险化学品使用单位-2_3","危险化学品使用单位-3_3","危险化学品使用单位-4_3","危险化学品使用单位-5_3","危险化学品使用单位-6_3","危险化学品生产企业-0_3","危险化学品生产企业-1_3","危险化学品生产企业-2_3","危险化学品生产企业-3_3","危险化学品生产企业-4_3","危险化学品生产企业-5_3","危险化学品生产企业-6_3","危险化学品经营单位-0_3","危险化学品经营单位-1_3","危险化学品经营单位-2_3","危险化学品经营单位-3_3","危险化学品经营单位-4_3","危险化学品经营单位-5_3","危险化学品经营单位-6_3","危险性外来入侵生物风险区-0_3","危险性外来入侵生物风险区-1_3","危险性外来入侵生物风险区-2_3","危险性外来入侵生物风险区-3_3","危险性外来入侵生物风险区-4_3","危险性外来入侵生物风险区-5_3","危险性外来入侵生物风险区-6_3","大风灾害风险区-0_3","大风灾害风险区-1_3","大风灾害风险区-2_3","大风灾害风险区-3_3","大风灾害风险区-4_3","大风灾害风险区-5_3","大风灾害风险区-6_3","库区（库）-0_3","库区（库）-1_3","库区（库）-2_3","库区（库）-3_3","库区（库）-4_3","库区（库）-5_3","库区（库）-6_3","暴雨灾害风险区-0_3","暴雨灾害风险区-1_3","暴雨灾害风险区-2_3","暴雨灾害风险区-3_3","暴雨灾害风险区-4_3","暴雨灾害风险区-5_3","暴雨灾害风险区-6_3","气瓶充装站-0_3","气瓶充装站-1_3","气瓶充装站-2_3","气瓶充装站-3_3","气瓶充装站-4_3","气瓶充装站-5_3","气瓶充装站-6_3","污染源-0_3","污染源-1_3","污染源-2_3","污染源-3_3","污染源-4_3","污染源-5_3","污染源-6_3","沙尘暴灾害风险区-0_3","沙尘暴灾害风险区-1_3","沙尘暴灾害风险区-2_3","沙尘暴灾害风险区-3_3","沙尘暴灾害风险区-4_3","沙尘暴灾害风险区-5_3","沙尘暴灾害风险区-6_3","油气田-0_3","油气田-1_3","油气田-2_3","油气田-3_3","油气田-4_3","油气田-5_3","油气田-6_3","烟花爆竹生产企业-0_3","烟花爆竹生产企业-1_3","烟花爆竹生产企业-2_3","烟花爆竹生产企业-3_3","烟花爆竹生产企业-4_3","烟花爆竹生产企业-5_3","烟花爆竹生产企业-6_3","生产场所-0_3","生产场所-1_3","生产场所-2_3","生产场所-3_3","生产场所-4_3","生产场所-5_3","生产场所-6_3","社会安全隐患-0_3","社会安全隐患-1_3","社会安全隐患-2_3","社会安全隐患-3_3","社会安全隐患-4_3","社会安全隐患-5_3","社会安全隐患-6_3","自然灾害风险隐患区-0_3","自然灾害风险隐患区-1_3","自然灾害风险隐患区-2_3","自然灾害风险隐患区-3_3","自然灾害风险隐患区-4_3","自然灾害风险隐患区-5_3","自然灾害风险隐患区-6_3","贮罐区（贮罐）-0_3","贮罐区（贮罐）-1_3","贮罐区（贮罐）-2_3","贮罐区（贮罐）-3_3","贮罐区（贮罐）-4_3","贮罐区（贮罐）-5_3","贮罐区（贮罐）-6_3","雪灾风险区-0_3","雪灾风险区-1_3","雪灾风险区-2_3","雪灾风险区-3_3","雪灾风险区-4_3","雪灾风险区-5_3","雪灾风险区-6_3","雷电灾害风险区-0_3","雷电灾害风险区-1_3","雷电灾害风险区-2_3","雷电灾害风险区-3_3","雷电灾害风险区-4_3","雷电灾害风险区-5_3","雷电灾害风险区-6_3","霜冻灾害风险区-0_3","霜冻灾害风险区-1_3","霜冻灾害风险区-2_3","霜冻灾害风险区-3_3","霜冻灾害风险区-4_3","霜冻灾害风险区-5_3","霜冻灾害风险区-6_3","高温灾害风险区-0_3","高温灾害风险区-1_3","高温灾害风险区-2_3","高温灾害风险区-3_3","高温灾害风险区-4_3","高温灾害风险区-5_3","高温灾害风险区-6_3"],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","储备物资库-0_3","储备物资库-1_3","储备物资库-2_3","储备物资库-3_3","储备物资库-4_3","储备物资库-5_3","储备物资库-6_3","重要场所-0_3","重要场所-1_3","重要场所-2_3","重要场所-3_3","重要场所-4_3","重要场所-5_3","重要场所-6_3"],
        ["default-0_3","default-10_3","default-11_3","default-12_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","default-7_3","default-8_3","default-9_3","专科疾病防治院（所、站）-0_3","专科疾病防治院（所、站）-10_3","专科疾病防治院（所、站）-11_3","专科疾病防治院（所、站）-12_3","专科疾病防治院（所、站）-1_3","专科疾病防治院（所、站）-2_3","专科疾病防治院（所、站）-3_3","专科疾病防治院（所、站）-4_3","专科疾病防治院（所、站）-5_3","专科疾病防治院（所、站）-6_3","专科疾病防治院（所、站）-7_3","专科疾病防治院（所、站）-8_3","专科疾病防治院（所、站）-9_3","其它医疗机构-0_3","其它医疗机构-10_3","其它医疗机构-11_3","其它医疗机构-12_3","其它医疗机构-1_3","其它医疗机构-2_3","其它医疗机构-3_3","其它医疗机构-4_3","其它医疗机构-5_3","其它医疗机构-6_3","其它医疗机构-7_3","其它医疗机构-8_3","其它医疗机构-9_3","其它卫生监督机构-0_3","其它卫生监督机构-10_3","其它卫生监督机构-11_3","其它卫生监督机构-12_3","其它卫生监督机构-1_3","其它卫生监督机构-2_3","其它卫生监督机构-3_3","其它卫生监督机构-4_3","其它卫生监督机构-5_3","其它卫生监督机构-6_3","其它卫生监督机构-7_3","其它卫生监督机构-8_3","其它卫生监督机构-9_3","其它疾病预防控制机构-0_3","其它疾病预防控制机构-10_3","其它疾病预防控制机构-11_3","其它疾病预防控制机构-12_3","其它疾病预防控制机构-1_3","其它疾病预防控制机构-2_3","其它疾病预防控制机构-3_3","其它疾病预防控制机构-4_3","其它疾病预防控制机构-5_3","其它疾病预防控制机构-6_3","其它疾病预防控制机构-7_3","其它疾病预防控制机构-8_3","其它疾病预防控制机构-9_3","医学科学研究机构-0_3","医学科学研究机构-10_3","医学科学研究机构-11_3","医学科学研究机构-12_3","医学科学研究机构-1_3","医学科学研究机构-2_3","医学科学研究机构-3_3","医学科学研究机构-4_3","医学科学研究机构-5_3","医学科学研究机构-6_3","医学科学研究机构-7_3","医学科学研究机构-8_3","医学科学研究机构-9_3","医疗卫生资源-0_3","医疗卫生资源-10_3","医疗卫生资源-11_3","医疗卫生资源-12_3","医疗卫生资源-1_3","医疗卫生资源-2_3","医疗卫生资源-3_3","医疗卫生资源-4_3","医疗卫生资源-5_3","医疗卫生资源-6_3","医疗卫生资源-7_3","医疗卫生资源-8_3","医疗卫生资源-9_3","医疗机构-0_3","医疗机构-10_3","医疗机构-11_3","医疗机构-12_3","医疗机构-1_3","医疗机构-2_3","医疗机构-3_3","医疗机构-4_3","医疗机构-5_3","医疗机构-6_3","医疗机构-7_3","医疗机构-8_3","医疗机构-9_3","医院-0_3","医院-10_3","医院-11_3","医院-12_3","医院-1_3","医院-2_3","医院-3_3","医院-4_3","医院-5_3","医院-6_3","医院-7_3","医院-8_3","医院-9_3","卫生监督所（局）-0_3","卫生监督所（局）-10_3","卫生监督所（局）-11_3","卫生监督所（局）-12_3","卫生监督所（局）-1_3","卫生监督所（局）-2_3","卫生监督所（局）-3_3","卫生监督所（局）-4_3","卫生监督所（局）-5_3","卫生监督所（局）-6_3","卫生监督所（局）-7_3","卫生监督所（局）-8_3","卫生监督所（局）-9_3","卫生院-0_3","卫生院-10_3","卫生院-11_3","卫生院-12_3","卫生院-1_3","卫生院-2_3","卫生院-3_3","卫生院-4_3","卫生院-5_3","卫生院-6_3","卫生院-7_3","卫生院-8_3","卫生院-9_3","县级卫生监督所（局）-0_3","县级卫生监督所（局）-10_3","县级卫生监督所（局）-11_3","县级卫生监督所（局）-12_3","县级卫生监督所（局）-1_3","县级卫生监督所（局）-2_3","县级卫生监督所（局）-3_3","县级卫生监督所（局）-4_3","县级卫生监督所（局）-5_3","县级卫生监督所（局）-6_3","县级卫生监督所（局）-7_3","县级卫生监督所（局）-8_3","县级卫生监督所（局）-9_3","县级疾病预防控制中心（防疫站）-0_3","县级疾病预防控制中心（防疫站）-10_3","县级疾病预防控制中心（防疫站）-11_3","县级疾病预防控制中心（防疫站）-12_3","县级疾病预防控制中心（防疫站）-1_3","县级疾病预防控制中心（防疫站）-2_3","县级疾病预防控制中心（防疫站）-3_3","县级疾病预防控制中心（防疫站）-4_3","县级疾病预防控制中心（防疫站）-5_3","县级疾病预防控制中心（防疫站）-6_3","县级疾病预防控制中心（防疫站）-7_3","县级疾病预防控制中心（防疫站）-8_3","县级疾病预防控制中心（防疫站）-9_3","国家级卫生监督机构-0_3","国家级卫生监督机构-10_3","国家级卫生监督机构-11_3","国家级卫生监督机构-12_3","国家级卫生监督机构-1_3","国家级卫生监督机构-2_3","国家级卫生监督机构-3_3","国家级卫生监督机构-4_3","国家级卫生监督机构-5_3","国家级卫生监督机构-6_3","国家级卫生监督机构-7_3","国家级卫生监督机构-8_3","国家级卫生监督机构-9_3","国家级疾病预防控制中心-0_3","国家级疾病预防控制中心-10_3","国家级疾病预防控制中心-11_3","国家级疾病预防控制中心-12_3","国家级疾病预防控制中心-1_3","国家级疾病预防控制中心-2_3","国家级疾病预防控制中心-3_3","国家级疾病预防控制中心-4_3","国家级疾病预防控制中心-5_3","国家级疾病预防控制中心-6_3","国家级疾病预防控制中心-7_3","国家级疾病预防控制中心-8_3","国家级疾病预防控制中心-9_3","妇幼保健院（所、站）-0_3","妇幼保健院（所、站）-10_3","妇幼保健院（所、站）-11_3","妇幼保健院（所、站）-12_3","妇幼保健院（所、站）-1_3","妇幼保健院（所、站）-2_3","妇幼保健院（所、站）-3_3","妇幼保健院（所、站）-4_3","妇幼保健院（所、站）-5_3","妇幼保健院（所、站）-6_3","妇幼保健院（所、站）-7_3","妇幼保健院（所、站）-8_3","妇幼保健院（所、站）-9_3","市级卫生监督所（局）-0_3","市级卫生监督所（局）-10_3","市级卫生监督所（局）-11_3","市级卫生监督所（局）-12_3","市级卫生监督所（局）-1_3","市级卫生监督所（局）-2_3","市级卫生监督所（局）-3_3","市级卫生监督所（局）-4_3","市级卫生监督所（局）-5_3","市级卫生监督所（局）-6_3","市级卫生监督所（局）-7_3","市级卫生监督所（局）-8_3","市级卫生监督所（局）-9_3","市级疾病预防控制中心-0_3","市级疾病预防控制中心-10_3","市级疾病预防控制中心-11_3","市级疾病预防控制中心-12_3","市级疾病预防控制中心-1_3","市级疾病预防控制中心-2_3","市级疾病预防控制中心-3_3","市级疾病预防控制中心-4_3","市级疾病预防控制中心-5_3","市级疾病预防控制中心-6_3","市级疾病预防控制中心-7_3","市级疾病预防控制中心-8_3","市级疾病预防控制中心-9_3","急救中心（站）-0_3","急救中心（站）-10_3","急救中心（站）-11_3","急救中心（站）-12_3","急救中心（站）-1_3","急救中心（站）-2_3","急救中心（站）-3_3","急救中心（站）-4_3","急救中心（站）-5_3","急救中心（站）-6_3","急救中心（站）-7_3","急救中心（站）-8_3","急救中心（站）-9_3","疗养院-0_3","疗养院-10_3","疗养院-11_3","疗养院-12_3","疗养院-1_3","疗养院-2_3","疗养院-3_3","疗养院-4_3","疗养院-5_3","疗养院-6_3","疗养院-7_3","疗养院-8_3","疗养院-9_3","疾病预防控制中心（防疫站）-0_3","疾病预防控制中心（防疫站）-10_3","疾病预防控制中心（防疫站）-11_3","疾病预防控制中心（防疫站）-12_3","疾病预防控制中心（防疫站）-1_3","疾病预防控制中心（防疫站）-2_3","疾病预防控制中心（防疫站）-3_3","疾病预防控制中心（防疫站）-4_3","疾病预防控制中心（防疫站）-5_3","疾病预防控制中心（防疫站）-6_3","疾病预防控制中心（防疫站）-7_3","疾病预防控制中心（防疫站）-8_3","疾病预防控制中心（防疫站）-9_3","省级卫生监督所（局）-0_3","省级卫生监督所（局）-10_3","省级卫生监督所（局）-11_3","省级卫生监督所（局）-12_3","省级卫生监督所（局）-1_3","省级卫生监督所（局）-2_3","省级卫生监督所（局）-3_3","省级卫生监督所（局）-4_3","省级卫生监督所（局）-5_3","省级卫生监督所（局）-6_3","省级卫生监督所（局）-7_3","省级卫生监督所（局）-8_3","省级卫生监督所（局）-9_3","省级疾病预防控制中心-0_3","省级疾病预防控制中心-10_3","省级疾病预防控制中心-11_3","省级疾病预防控制中心-12_3","省级疾病预防控制中心-1_3","省级疾病预防控制中心-2_3","省级疾病预防控制中心-3_3","省级疾病预防控制中心-4_3","省级疾病预防控制中心-5_3","省级疾病预防控制中心-6_3","省级疾病预防控制中心-7_3","省级疾病预防控制中心-8_3","省级疾病预防控制中心-9_3","社区卫生服务中心（站）-0_3","社区卫生服务中心（站）-10_3","社区卫生服务中心（站）-11_3","社区卫生服务中心（站）-12_3","社区卫生服务中心（站）-1_3","社区卫生服务中心（站）-2_3","社区卫生服务中心（站）-3_3","社区卫生服务中心（站）-4_3","社区卫生服务中心（站）-5_3","社区卫生服务中心（站）-6_3","社区卫生服务中心（站）-7_3","社区卫生服务中心（站）-8_3","社区卫生服务中心（站）-9_3","职业病防治院（所、站、中心）-0_3","职业病防治院（所、站、中心）-10_3","职业病防治院（所、站、中心）-11_3","职业病防治院（所、站、中心）-12_3","职业病防治院（所、站、中心）-1_3","职业病防治院（所、站、中心）-2_3","职业病防治院（所、站、中心）-3_3","职业病防治院（所、站、中心）-4_3","职业病防治院（所、站、中心）-5_3","职业病防治院（所、站、中心）-6_3","职业病防治院（所、站、中心）-7_3","职业病防治院（所、站、中心）-8_3","职业病防治院（所、站、中心）-9_3","诊所、卫生所（室）、医务室-0_3","诊所、卫生所（室）、医务室-10_3","诊所、卫生所（室）、医务室-11_3","诊所、卫生所（室）、医务室-12_3","诊所、卫生所（室）、医务室-1_3","诊所、卫生所（室）、医务室-2_3","诊所、卫生所（室）、医务室-3_3","诊所、卫生所（室）、医务室-4_3","诊所、卫生所（室）、医务室-5_3","诊所、卫生所（室）、医务室-6_3","诊所、卫生所（室）、医务室-7_3","诊所、卫生所（室）、医务室-8_3","诊所、卫生所（室）、医务室-9_3","门诊部-0_3","门诊部-10_3","门诊部-11_3","门诊部-12_3","门诊部-1_3","门诊部-2_3","门诊部-3_3","门诊部-4_3","门诊部-5_3","门诊部-6_3","门诊部-7_3","门诊部-8_3","门诊部-9_3"],
        ["default-0_3","default-1_3","default-2_3","default-3_3","default-4_3","default-5_3","default-6_3","交通通信中心-0_3","交通通信中心-1_3","交通通信中心-2_3","交通通信中心-3_3","交通通信中心-4_3","交通通信中心-5_3","交通通信中心-6_3","党政专用通信保障机构-0_3","党政专用通信保障机构-1_3","党政专用通信保障机构-2_3","党政专用通信保障机构-3_3","党政专用通信保障机构-4_3","党政专用通信保障机构-5_3","党政专用通信保障机构-6_3","其它通信保障机构-0_3","其它通信保障机构-1_3","其它通信保障机构-2_3","其它通信保障机构-3_3","其它通信保障机构-4_3","其它通信保障机构-5_3","其它通信保障机构-6_3","基础电信运营企业地市分公司-0_3","基础电信运营企业地市分公司-1_3","基础电信运营企业地市分公司-2_3","基础电信运营企业地市分公司-3_3","基础电信运营企业地市分公司-4_3","基础电信运营企业地市分公司-5_3","基础电信运营企业地市分公司-6_3","基础电信运营企业省公司-0_3","基础电信运营企业省公司-1_3","基础电信运营企业省公司-2_3","基础电信运营企业省公司-3_3","基础电信运营企业省公司-4_3","基础电信运营企业省公司-5_3","基础电信运营企业省公司-6_3","基础电信运营企业集团公司-0_3","基础电信运营企业集团公司-1_3","基础电信运营企业集团公司-2_3","基础电信运营企业集团公司-3_3","基础电信运营企业集团公司-4_3","基础电信运营企业集团公司-5_3","基础电信运营企业集团公司-6_3","电信运营企业-0_3","电信运营企业-1_3","电信运营企业-2_3","电信运营企业-3_3","电信运营企业-4_3","电信运营企业-5_3","电信运营企业-6_3","移动运营企业-0_3","移动运营企业-1_3","移动运营企业-2_3","移动运营企业-3_3","移动运营企业-4_3","移动运营企业-5_3","移动运营企业-6_3","联通运营企业-0_3","联通运营企业-1_3","联通运营企业-2_3","联通运营企业-3_3","联通运营企业-4_3","联通运营企业-5_3","联通运营企业-6_3","通信保障机构-0_3","通信保障机构-1_3","通信保障机构-2_3","通信保障机构-3_3","通信保障机构-4_3","通信保障机构-5_3","通信保障机构-6_3","铁通运营企业-0_3","铁通运营企业-1_3","铁通运营企业-2_3","铁通运营企业-3_3","铁通运营企业-4_3","铁通运营企业-5_3","铁通运营企业-6_3"]
    ];

    var  collaItem=document.getElementsByClassName('layui-colla-item');

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
            imgElement.setAttribute('class','emergencyIcon');


            ulDom.appendChild(liElement).appendChild(imgElement);
        }

    }
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
    var obj = document.getElementsByClassName('emergencyIcon');
    $(obj).click(function(e){
        var thisDom = e.target;
        document.getElementById('pointFeatureLayerIcon').src = thisDom.src;
        //在这一步改变点要素图层的图片文件
        rendererNow={};
        rendererNow.url = thisDom.src;
        rendererNow.type = "esriPMS";
        if (drawType == 'edit') {
            openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
        } else {
            // addDrawInteraction('Point');
            openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
        }
    });

    // for (var i=0;i<obj.length;i++){
    //     obj[i].onClick=function () {
    //         alert(obj[0].src);
    //         document.getElementById('pointFeatureLayerIcon').src = this.src;
    //         //在这一步改变点要素图层的图片文件
    //         rendererNow={};
    //         rendererNow.url = this.src;
    //         rendererNow.type = "esriPMS";
    //         if (drawType == 'edit') {
    //             openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
    //         } else {
    //             // addDrawInteraction('Point');
    //             openFeatureLayerPanelContent(['map-featureLayer-panel', 'map-featureLayer-info', 'map-featureLayer-panel-tip'])
    //         }
    //     }
    // }
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
