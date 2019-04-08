//预设一个制图模板，只包含featureLayer
var legend = null;
var featureKey;
var dataGot = {};//全局变量存储请求到的原始数据
var isLegended = false;
template = {
    //制图尺度(若仅有一个尺度,则可缺省为空)
    "mapScale": [],
    "serviceLayer": {
        "modules": [
            {  "name": "湖泊1", //要素图层模型名称(如:湖泊)
                "data": "http://47.96.162.249:6080/arcgis/rest/services/project6/earthquake/MapServer"},
            {  "name": "湖泊2", //要素图层模型名称(如:湖泊)
                "data": "http://47.96.162.249:6080/arcgis/rest/services/project6/earthquake/MapServer"}
            ]
    },
    //要素图层(可能有多个点/线/面modules, module<-->layer对应) [若有多个module叠加,按叠加先后顺序组织modules数组元素(面->线->点)]
    "featureLayer": {
        "modules": [{
            "type": "polygon", //要素类型
            "name": "湖泊1", //要素图层模型名称(如:湖泊)
            "data": "https://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Demographics/ESRI_Census_USA/MapServer/3", //要素数据来源(用户前端输入)
            "style": {
                "opacity": "0.5", //图层透明度
                //1.SimpleFillSymbol的符号配置(可直接以json为参数构造对象)
                "render": {
                    "type": "esriSFS", //SimpleFillSymbol(简单填充类型)
                    "color": [0, 76, 0, 255], //填充颜色(仅在style为STYLE_SOLID时生效)
                    "outline": {
                        "type": "esriSLS",
                        "style": "esriSLSDot",
                        "color": [0, 76, 0, 255],
                        "width": 1
                    }, //填充轮廓线要素"render"对象(见下述线要素)
                    "style": "esriSFSSolid" //填充样式
                }
            }
        },
            {
                "type": "polygon", //要素类型
                "name": "湖泊2", //要素图层模型名称(如:湖泊)
                "data": "https://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Demographics/ESRI_Census_USA/MapServer/3", //要素数据来源(用户前端输入)
                "style": {
                    "opacity": "1", //图层透明度
                    //2.PictureFillSymbol的符号配置(可直接以json为参数构造对象)
                    "render": {
                        "type": "esriPFS", //PictureFillSymbol(图片填充类型)
                        "url": "./assets/images/ss.png", //填充图片地址,如"graphics/redArrow2.png",
                        "height": 16, //填充图片高度
                        "width": 16, //填充图片宽度
                        "outline": {
                            "type": "esriSLS",
                            "style": "esriSLSDot",
                            "color": [115, 76, 0, 255],
                            "width": 1
                        }, //填充轮廓线要素"render"对象(见下述线要素)
                        "xoffset": 0, //x方向偏移量
                        "yoffset": 0, //y方向偏移量
                        "xscale": 1, //x方向比例因子(默认值:1)
                        "yscale": 1 //y方向比例因子(默认值:1)
                    }
                }
            },
            {
                "type": "polyline", //要素类型
                "name": "道路1", //要素图层模型名称(如:道路)
                "data": "https://services.arcgis.com/V6ZHFr6zdgNZuVG0/ArcGIS/rest/services/Chicago_L_Lines/FeatureServer/1", //要素数据来源(用户前端输入)
                "style": {
                    "opacity": "1", //图层透明度

                    //1.SimpleLineSymbol的符号配置(可直接以json为参数构造对象)
                    "render": {

                        "type": "esriSLS", //SimpleLineSymbol(简单线类型)
                        "color": [115, 76, 0, 255], //颜色
                        "width": 2, //线宽
                        "style": "esriSLSDash" //线形

                    }
                }
            },
            {
                "type": "polyline", //要素类型
                "name": "道路2", //要素图层模型名称(如:道路)
                "data": "", //要素数据来源(用户前端输入)
                "style": {
                    "opacity": "1", //图层透明度
                    //2.CartographicLineSymbol(可直接以json为参数构造对象)
                    "render": {
                        "type": "esriCLS", //CartographicLineSymbol(制图线类型)
                        "color": [115, 76, 0, 255], //颜色
                        "width": 2, //线宽
                        "style": "esriCLSShortDot", //线形
                        //线端点的符号类型(v3.23加入的属性)
                        "marker": {
                            "style": "arrow", //v3.23仅支持arrow一种
                            "placement": "end" //[ begin/end/begin-end.]
                        },
                        "cap": "CAP_SQUARE", //线端点的风格(无/圆形/方形)
                        "join": "JOIN_ROUND", //线的连接处风格(平/尖锐/圆滑)
                        "miterLimit": 2 //显示线连接的大小阈值
                    }
                }
            },
            {
                "type": "point", //要素类型
                "name": "震源分布点1", //要素图层模型名称(如:震源分布点)
                "data": "https://services.arcgis.com/V6ZHFr6zdgNZuVG0/arcgis/rest/services/nyc_parks_gardens_hist_sites/FeatureServer/0", //要素数据来源(用户前端输入)
                "style": {
                    "type": "SimpleMarkerSymbol", //SimpleMarkerSymbol(圆形,矩形,十字等),PictureMarkerSymbol(以图片为符号)
                    "opacity": 1, //图层透明度
                    //1.SimpleMarkerSymbol的符号配置(可直接以json为参数构造对象)
                    "render": {
                        "type": "esriSMS", //SimpleMarkerSymbol(圆形,矩形,十字等)
                        "angle": 0, //符号角度
                        "color": [115, 76, 0, 255], //符号颜色
                        "outline": {
                            "type": "esriSLS",
                            "style": "esriSLSDot",
                            "color": [115, 76, 0, 255],
                            "width": 1
                        }, //符号轮廓(线要素对象)
                        "size": 10, //符号尺寸
                        "style": "esriSMSDiamond", //符号样式
                        "xoffset": 0, //x方向偏移量
                        "yoffset": 0 //y方向偏移量
                    }
                }

            },
            {
                "type": "point", //要素类型
                "name": "震源分布点2", //要素图层模型名称(如:震源分布点)
                "data": "https://services.arcgis.com/V6ZHFr6zdgNZuVG0/arcgis/rest/services/nyc_parks_gardens_hist_sites/FeatureServer/0", //要素数据来源(用户前端输入)
                "style": {
                    "type": "PictureMarkerSymbol", //SimpleMarkerSymbol(圆形,矩形,十字等),PictureMarkerSymbol(以图片为符号)
                    "opacity": 1, //图层透明度

                    //2.PictureMarkerSymbol的符号配置(可直接以json为参数构造对象)
                    "render": {
                        "type": "esriPMS", //PictureMarkerSymbol(以图片为符号)
                        "url": "./assets/images/ss.png", //如"graphics/redArrow2.png",符号图片地址
                        "height": 8, //图片高度(像素)
                        "width": 8, //图片宽度(像素)
                        "angle": 0, //图片角度
                        "xoffset": 0, //x方向偏移量
                        "yoffset": 0, //y方向偏移量
                    }
                }

            }
        ]
    }
}
var defaultStyle ={
    point: {
        "type": "SimpleMarkerSymbol", //SimpleMarkerSymbol(圆形,矩形,十字等),PictureMarkerSymbol(以图片为符号)
        "opacity": 1, //图层透明度
        //1.SimpleMarkerSymbol的符号配置(可直接以json为参数构造对象)
        "render": {
            "type": "esriSMS", //SimpleMarkerSymbol(圆形,矩形,十字等)
            "angle": 0, //符号角度
            "color": [115, 76, 0, 255], //符号颜色
            "outline": {
                "type": "esriSLS",
                "style": "esriSLSDot",
                "color": [115, 76, 0, 255],
                "width": 1
            }, //符号轮廓(线要素对象)
            "size": 10, //符号尺寸
            "style": "esriSMSDiamond", //符号样式
            "xoffset": 0, //x方向偏移量
            "yoffset": 0, //y方向偏移量
        }
    },
    polyline: {
        "opacity": "1", //图层透明度
        //2.CartographicLineSymbol(可直接以json为参数构造对象)
        "render": {
            "type": "esriCLS", //CartographicLineSymbol(制图线类型)
            "color": [70, 130, 180, 255], //颜色
            "width": 1, //线宽
            "style": "esriCLSShortDot", //线形
            //线端点的符号类型(v3.23加入的属性)
            "cap": "CAP_SQUARE", //线端点的风格(无/圆形/方形)
            "join": "JOIN_ROUND", //线的连接处风格(平/尖锐/圆滑)
            "miterLimit": 2 //显示线连接的大小阈值
        }
    },
    polygon: {
        "opacity": "0.5", //图层透明度
        //1.SimpleFillSymbol的符号配置(可直接以json为参数构造对象)
        "render": {
            "type": "esriSFS", //SimpleFillSymbol(简单填充类型)
            "color": [0, 76, 0, 255], //填充颜色(仅在style为STYLE_SOLID时生效)
            "outline": {
                "type": "esriSLS",
                "style": "esriSLSDot",
                "color": [0, 76, 0, 255],
                "width": 1
            }, //填充轮廓线要素"render"对象(见下述线要素)
            "style": "esriSFSSolid" //填充样式
        }
    }
}
//模拟数据 根据尺度选择不同的模板
var template_scale = sessionStorage.getItem("template_scale");
var error = {};

var template_name="";
switch (template_scale) {
    case "国家":
        template_name="template1";
        break;
    case "省级":
        template_name="template2";
        break;
    case "区域":
        template_name="template3";
        break;
}

//通过后台获取模板（也就是后台每个表里six_lzjtu_layer里的json，如果为空，则使用缺省值）
(function getTemplate() {
    var disaster_status = sessionStorage.getItem("disaster_status");
    var disaster_type = sessionStorage.getItem("disaster_type");
    var template_scale = sessionStorage.getItem("template_scale");
    var template_theme = sessionStorage.getItem("template_theme");
    var template_map = sessionStorage.getItem("template_map");
    $("#mapNameInfo").html(template_map);
    var url = "./servlet/GetTemplateLayer?disasterStatus="+disaster_status+"&disasterType="+disaster_type+"&templateScale="+template_scale+"&templateTheme="+template_theme+"&templateMap="+template_map+"&queryType=queryLayer";
    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        cache:false,
        contentType: "charset=utf-8",
        async:false,//设置为同步操作就可以给全局变量赋值成功
        scriptCharset: 'utf-8',
        success: function (data) {
            //str=data[0]["SIX_LZJTU_LAYER"].slice(1,data[0]["SIX_LZJTU_LAYER"].length-4);
            var jsonStr;
            if ((typeof data[0]["SIX_LZJTU_LAYER"])==="string"&&!(data[0]["SIX_LZJTU_LAYER"]==="")&&!(data[0]["SIX_LZJTU_LAYER"].toLowerCase()==="null")) {
                //要考虑到字符串string为空的情况
               try{
                   str=eval("(" + data[0]["SIX_LZJTU_LAYER"] + ")");
                   jsonStr=str[0];
               }
               catch(e){
                   alert("数据库中模板格式错误");
                   console.log(e);
               }
            }else if ((typeof data[0]["SIX_LZJTU_LAYER"])==="object"&&!(data[0]["SIX_LZJTU_LAYER"]===null)){
                //要考虑到object为空的情况
                str=data[0]["SIX_LZJTU_LAYER"];
                jsonStr=str[0];
            }
            try{
                //if(jsonStr&&jsonStr.featureLayer&&jsonStr.featureLayer.modules&&jsonStr.featureLayer.modules.length&&jsonStr.featureLayer.modules[0].name)
                if(jsonStr)
                {
                    template=jsonStr;
                }else{
                    console.log(jsonStr)
                    alert("数据库中模板为空或格式错误，使用缺省模板")
                }
                console.log(template);
                return;
            }catch (e) {
                alert("数据库中模板格式错误");
                console.log(e);
            }
        },
        error: function (xhr, status, errMsg) {
            alert('error');
            console.log(errMsg);
        }
    });
})();
var flag =0;
//根据制图模板，构造前端样式
function generateFeatureLayer() {
    //首先判断制图模板中featureLayer是否为空，不为空才有下一步
    //@Zeo：感觉这一步写错了
    if (JSON.stringify(template.baseLayer) != '{}') {
        //设置前端样式为可见
        $("#featureLayer").css("display", "inline");
        //构造之前先清空
        $("#featureLayer ul").empty();
        //分析制图模板，为每一个featureLayer构造前端样式
        for (var index = 0; index < template.featureLayer.modules.length; index++) {
            //每一个featureLayer
            featureLayerItem = template.featureLayer.modules[index]
            //判断要素类型，设置样式：点 线 面
            featureType = ""
            switch (featureLayerItem["type"]) {
                case "polygon":
                    featureType = "fa-square"
                    break;
                case "polyline":
                    featureType = "fa-ellipsis-h"
                    break;
                case "point":
                    featureType = "fa-dot-circle-o"
                    break;
            }
            //字体颜色 如果有服务地址，则为亮 否则为暗
            var featureNameColor;
            if (featureLayerItem["data"] == "") {
                featureNameColor = "gray";
            } else if (featureLayerItem["data"] != "") {
                featureNameColor = "yellow";
            }
            var iconColor;
            if(map&&(map.getLayer(featureLayerItem.data))&&((map.getLayer(featureLayerItem.data)).visible)){
                iconColor='rgb(0, 253, 255)';
            }else if(map&&(map.getLayer(featureLayerItem.data.id))&&((map.getLayer(featureLayerItem.data.id)).visible)){
                iconColor='#e2e2e2';
            }else {
                iconColor='#e2e2e2';
            }
            //构造样式
            styleItem = '<li><div class="featureLayerCss" data-type="' + featureLayerItem["type"] + '" data-index="' + index + '"><i class="featureLayerCssI1 fa ' + featureType + '" aria-hidden="true" style="color:'+iconColor+'"></i><a state="0" href="#" style="color:' + featureNameColor + '">' + featureLayerItem["name"] + '</a><i class="featureLayerCssI2 fa fa-cog"  aria-hidden="true"></i></div></li>'
            //增加到页面
            $("#featureLayer ul").append(styleItem)

        }
        //要素图层点击事件
        $("#featureLayer a").click(function () {
            node = this;
            loadFeatureLayer(node);
        });
        //要素图层样式设置点击事件
        $(".featureLayerCssI2").click(function () {
            node = this ;
            featureLayerSet(node);
        });
        flag=1;
    }
}

//符号样式修改器
function styleModifier() {
    element = "<div>"
}

var getTemplate = function(){
    return template;
}


generateFeatureLayer();



function loadAllLayer(){
    var featureLayerNode = $("#featureLayer a");
    if(!featureLayerNode) return;
    for (i=0;i<featureLayerNode.length;i++) {
        var thisNode=featureLayerNode[i];
        state = $(thisNode).attr("state");
        //获取当前要素服务在制图模板中信息
        addFeature(thisNode);
    }
    var serviceLayerNode = $("#serviceLayer a");
    if(!serviceLayerNode) return;
    for (i=0;i<serviceLayerNode.length;i++) {
        var thisNode=serviceLayerNode[i];
        state = $(thisNode).attr("state");
        //获取当前要素服务在制图模板中信息
        addService(thisNode);
    }
    //map.setExtent(featureLayerExtent);
    createLegend();
}



/* var taskBar1 = new TaskBar($('#mapContainer'), {
	position: 'right-top',
	title: '设置',
	width: 350,
	height: 150,
	content: $("<br/><p class='FLS_p'>服务地址：<input id='FLAddress' ></input><i class='FLS_i fa fa-cog'></i></p><br/><p class='FLS_p'><button>确定</button><button>取消</button></p>")
}); */
function createLegend(){
    require([
        "esri/map",
        "esri/Color",
        "esri/layers/FeatureLayer",
        "esri/renderers/UniqueValueRenderer",
        "esri/symbols/SimpleLineSymbol",
        "esri/dijit/PopupTemplate",
        "esri/dijit/Legend",
        "dojo/domReady!"
    ], function(
        Map, Color,
        FeatureLayer,
        UniqueValueRenderer,
        SimpleLineSymbol,
        PopupTemplate,
        Legend
    ) {
         legend = new Legend({
             autoUpdate:true,
             map: map
        }, "legendDiv");

        legend.startup();
        isLegended = true;
    })
}

function loadFeatureLayer(node) {
    //点击要素图层
    //当前要素图层选中状态 0：未选中 1：选中
    state = $(node).attr("state");
    itemClicked = $(node);
    //获取当前要素服务在制图模板中信息
    featureLayerItem = template.featureLayer.modules[$($(node).parent()).attr("data-index")]
    //先判断模板中data的类型，如果是object则说明是从服务器端请求的数据。如果是字符串，说明是从模板中请求的数据。加载方式不一样。
    if (typeof(featureLayerItem.data)==="object"){
        if(state == 0){//说明该节点代表的数据还没有加载到图层上去
            //判断要素服务地址是否存在
            if (featureLayerItem.data == "") {
                layui.use('layer', function () {
                    var lay = layui.layer;
                    lay.open({
                        title: '提示'
                        ,content: '请先设置要素图层地址！'
                    });
                })
            }else if((map.getLayer(featureLayerItem.data.featureObj.id))&&(!((map.getLayer(featureLayerItem.data.featureObj.id)).visible))){//如果地图上已加载该数据图层但是隐藏
                (map.getLayer((template.featureLayer.modules[$($(node).parent()).attr("data-index")]).data)).show();//使隐藏的图层展示出来
                $(node).attr("state", "1");//展示出来就改变state属性为1
                $($(node).parent()).find(".featureLayerCssI1").css("color", ' rgb(0, 253, 255)');//展示出来就改变颜色
            }else if((map.getLayer(featureLayerItem.data.featureObj.id))&&((map.getLayer(featureLayerItem.data.featureObj.id)).visible)){//如果地图上也加载了，也能看见，就改变node节点的颜色
                $(node).attr("state", "1");
                $($(node).parent()).find(".featureLayerCssI1").css("color", ' rgb(0, 253, 255)');
            }else{
                if(!(featureLayerItem.style)){
                    featureLayerItem.style=defaultStyle[featureLayerItem["type"]];
                }
                require([
                    "esri/layers/FeatureLayer",
                    "esri/InfoTemplate", "esri/dijit/PopupTemplate", "esri/renderers/SimpleRenderer", "esri/layers/GraphicsLayer"
                ], function (FeatureLayer, InfoTemplate, PopupTemplate, SimpleRenderer, GraphicsLayer) {
                    var simpleJson = {
                        "type": "simple",
                        "label": featureLayerItem.name,
                        "description": "",
                        // "symbol": isEdited()?renderer:featureLayerItem.style.render
                        "symbol":featureLayerItem.style.render
                    }
                    var rend = new SimpleRenderer(simpleJson);
                    //var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                    var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                    var serviceUrl = featureLayerItem.data.featureObj;
                    /*var layer = new FeatureLayer(serviceUrl, {
                        /!* featureReduction: {
                            type: "cluster"
                          }, *!/
                        mode: FeatureLayer.MODE_SNAPSHOT,
                        outFields: ["*"],
                        opacity: featureLayerItem.style.opacity,
                        //infoTemplate: infoTemplate,
                        //id: featureLayerItem.data.id
                    });
                    layer.setRenderer(rend)*/
                    map.on("layers-add-result", function(results) {
                        var features = [];
                        require([
                            "esri/graphic", "esri/tasks/FeatureSet","esri/geometry/Point"
                        ], function(Graphic,FeatureSet,Point) {
                            for(i in featureLayerItem.data.data.data){
                                var geometry = new Point(featureLayerItem.data.data.data[i].point);
                                var graphic = new Graphic(geometry);
                                features.push(graphic);
                            }
                        });
                        serviceUrl.applyEdits(features, null, null);

                    });
                    map.addLayers([serviceUrl]);
                })
            }
        }else{//说明该节点代表的数据已经加载到了地图上

        }
    }else if (typeof(featureLayerItem.data)==="string"){
        if (state == 0) {
            //判断要素服务地址是否存在
            if (featureLayerItem.data == "") {
                layui.use('layer', function () {
                    var lay = layui.layer;
                    lay.open({
                        title: '提示'
                        ,content: '请先设置要素图层地址！'
                    });
                })
            } else if((map.getLayer(featureLayerItem.data))&&(!((map.getLayer(featureLayerItem.data)).visible))){
                if(!(map.getLayer((template.featureLayer.modules[$($(node).parent()).attr("data-index")]).data)))
                {return};
                (map.getLayer((template.featureLayer.modules[$($(node).parent()).attr("data-index")]).data)).show();
                var layerExtent=(map.getLayer((template.featureLayer.modules[$($(node).parent()).attr("data-index")]).data)).fullExtent;
                layui.use('layer', function () {
                    var lay = layui.layer;
                    lay.confirm('加载成功，缩放到该图层?', {
                        icon: 3,
                        title: '提示'
                    }, function (layui_index) {
                        try {
                            //如果图层与地图坐标系不同，转换一下再设置全局范围
                            if(layerExtent.spatialReference.wkid!=map.spatialReference.wkid ){
                                require(["esri/tasks/GeometryService","esri/config"], function(GeometryService,config) {
                                    //配置代理
                                    config.defaults.io.proxyUrl = "../esriproxy/";
                                    config.defaults.io.alwaysUseProxy = false;
                                    var geometryService = new GeometryService(ESRI_GeometyService);
                                    geometryService.project([layerExtent],map.spatialReference, function (p) {
                                        console.log(p);
                                        map.setExtent(p[0]);
                                    });
                                });
                            }else {
                                map.setExtent(layerExtent);
                            }


                            /*if(map.spatialReference.wkid!=layerExtent.spatialReference.wkid){
                                lay.open({
                                    title: '提示'
                                    ,content: '服务图层与底图图层坐标系统不同，暂时无法缩放！您可手动缩放到该图层！'
                                });
                                return;
                            }else{
                            map.setExtent(layerExtent);
                            }*/
                        }catch (e) {
                            lay.open({
                                title: '提示'
                                ,content: '服务图层与底图图层坐标系统不同，暂时无法缩放！您可手动缩放到该图层！'
                            });
                            console.log(e);
                        }
                        lay.close(layui_index)
                    })
                })
                $(node).attr("state", "1");
                $($(node).parent()).find(".featureLayerCssI1").css("color", ' rgb(0, 253, 255)');
            }else if((map.getLayer(featureLayerItem.data))&&((map.getLayer(featureLayerItem.data)).visible)){
                $(node).attr("state", "1");
                $($(node).parent()).find(".featureLayerCssI1").css("color", ' rgb(0, 253, 255)');
            }else{
                if(!(featureLayerItem.style)){
                    featureLayerItem.style=defaultStyle[featureLayerItem["type"]];
                }
                require([
                    "esri/layers/FeatureLayer",
                    "esri/InfoTemplate", "esri/dijit/PopupTemplate", "esri/renderers/SimpleRenderer", "esri/layers/GraphicsLayer"
                ], function (FeatureLayer, InfoTemplate, PopupTemplate, SimpleRenderer, GraphicsLayer) {
                    //var renderer = getRendererNow();
                    //这一步会影响
                    var simpleJson = {
                        "type": "simple",
                        "label": featureLayerItem.name,
                        "description": "",
                        // "symbol": isEdited()?renderer:featureLayerItem.style.render
                        "symbol":featureLayerItem.style.render
                    }
                    var rend = new SimpleRenderer(simpleJson);
                    //var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                    var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                    var serviceUrl = featureLayerItem.data;
                    var layer = new FeatureLayer(serviceUrl, {
                        /* featureReduction: {
                            type: "cluster"
                          }, */
                        mode: FeatureLayer.MODE_SNAPSHOT,
                        outFields: ["*"],
                        opacity: featureLayerItem.style.opacity,
                        infoTemplate: infoTemplate,
                        id: featureLayerItem.data
                    });
                    layer.setRenderer(rend)
                    map.addLayer(layer);
                    map.on("layer-add-result",function(e){
                        if(e.error){
                            error.errorMessage = e.error;
                            error.id=e.layer.id;
                            layui.use('layer', function () {
                                var layer = layui.layer;
                                layer.open({
                                    title: '服务地址有误'
                                    ,content: "服务地址有误，请确认！地址是"+error.id
                                });
                            })
                        }else{
                            style();
                            if(!isLegended){createLegend()};
                            layui.use('layer', function () {
                                var lay = layui.layer;
                                lay.confirm('加载成功，缩放到该图层?', {
                                    icon: 3,
                                    title: '提示'
                                }, function (layui_index) {
                                    //legend.refresh();
                                    try {
                                        layerExtent=layer.fullExtent;
                                        //如果图层与地图坐标系不同，转换一下再设置全局范围
                                        if(layerExtent.spatialReference.wkid!=map.spatialReference.wkid ){
                                            require(["esri/tasks/GeometryService","esri/config"], function(GeometryService,config) {
                                                //配置代理
                                                config.defaults.io.proxyUrl = "../esriproxy/";
                                                config.defaults.io.alwaysUseProxy = false;
                                                var geometryService = new GeometryService(ESRI_GeometyService);
                                                geometryService.project([layerExtent],map.spatialReference, function (p) {
                                                    console.log(p);
                                                    map.setExtent(p[0]);
                                                });
                                            });
                                        }else {
                                            map.setExtent(layerExtent);
                                        }

                                        /*if(map.spatialReference.wkid!=layer.fullExtent.spatialReference.wkid){
                                            lay.open({
                                                title: '提示'
                                                ,content: '服务图层与底图图层坐标系统不同，暂时无法缩放！您可手动缩放到该图层！'
                                            });
                                            return;
                                        }else{
                                            map.setExtent(layer.fullExtent);
                                        }*/
                                    }catch (e) {
                                        lay.open({
                                            title: '提示'
                                            ,content: '服务图层与底图图层坐标系统不同，暂时无法缩放！您可手动缩放到该图层！'
                                        });
                                        console.log(e);
                                    }
                                    lay.close(layui_index)
                                })
                            })
                        }
                    },this)
                });
            }
        } else {
            //获取当前要素服务在制图模板中信息
            featureLayerItem = template.featureLayer.modules[$($(node).parent()).attr("data-index")]
            //获取当前要取消的图层
            thisFLayer = map.getLayer(featureLayerItem.data)
            thisFLayer.hide()
            //map.removeLayer(thisFLayer)
            $(node).attr("state", "0")
            $($(node).parent()).find(".featureLayerCssI1").css("color", '#e2e2e2');
        }
    }
    //alert($(this).text());
    var style = function(){
        $(node).attr("state", "1");
        $($(node).parent()).find(".featureLayerCssI1").css("color", ' rgb(0, 253, 255)');
    }
}

//@YH添加：
function treeWindow(node,dataIndex) {//第一个参数：被点击的小齿轮dom节点 第二个参数：图层地址在模板中的位置（null表示不是在模板中得到的）
    layui.use('layer', function (layui_index) {
        var layer = layui.layer;
        layer.open({
            title: '专题图层',
            skin: "layui-layer-lan",
            content: "<br/><div id='tree'></div>",
            yes: function(index, layero){//确定后执行回调
                //确定之后 ，判断dataGot是否为空或者null，如不为空①将已在本图层加载的图层remove掉 ②将存储在dataGot中的数据加载在图层中③将dataIndex设为空
                if(dataGot&&dataIndex){
                    $($(node).parent()).find(".featureLayerCssI1").css("color", '#e2e2e2');
                    //--将老图层移除掉
                    var data = template.featureLayer.modules[dataIndex].data;
                    if(data !== null && data !== undefined && data !== ''){
                        var thisFLayer = map.getLayer(data)
                        if(thisFLayer){
                            map.removeLayer(thisFLayer)
                        }
                        $(node).attr("state", "0")
                    }//--
                    //将模板中的数据替换。URL替换成featureCollection对象
                    var newData = createFeatureCollection(dataGot);
                    template.featureLayer.modules[dataIndex].data = newData;
                    generateFeatureLayer();
                    console.log("dataGot不为空，开始加载");
                }else{
                    console.log("dataGot为空，取消加载")
                }
                console.log("yes")
                layer.close(index); //如果设定了yes回调，需进行手工关闭
            },
            end:function () {//取消或者叉掉之后的回调
                dataGot = null;
                console.log("cancel")
            }
        });
    });
    $.ajaxSetup({async:false});
    $.getJSON("http://qk.casm.ac.cn:9090/ythjzweb/tucengbygl/getleveljson.it?pid=858",function(data) {
        console.log("first");
        console.log(data);
        var folder;
        do {


            for (var i = 0; i < data.length; i++) {
                folder = data[i];
                // console.log(folder);
                if (data[i]["isFolder"] == true) {
                    $.getJSON("http://qk.casm.ac.cn:9090/ythjzweb/tucengbygl/getleveljson.it?pid=" + data[i]["pid"], function (data2) {
                        // console.log(data);
                        console.log("second"+i);
                        console.log(data2);
                        data[i]["children"] = data2;

                        for (var j = 0; j < data2.length; j++) {
                            folder = data2[j];
                            if (data[i]["children"][j]["isFolder"] == true) {
                                $.getJSON("http://qk.casm.ac.cn:9090/ythjzweb/tucengbygl/getleveljson.it?pid=" + data[i]["children"][j]["pid"], function (data3) {
                                    data[i]["children"][j]["children"] = data3;
                                });
                            }
                        }
                    });
                }
            }
        }
        while(i==0)
       $("#tree").dynatree({
            checkbox: true,
            // Override class name for checkbox icon, so rasio buttons are displayed:
            classNames: {nodeIcon: ""},
            // Select mode 3: multi-hier
            selectMode: 1,
            children: data,
           onSelect:function(flag,node){
              var target = node.getEventTargetType(event);
               if(target=='checkbox'&&flag){
                   //当checkbox被勾选时，触发的事件。
                   // 当被勾选时，请求服务端数据储存在本地全局变量中（或者替换之前已存储本地的数据）。但是在对话框中点确定才会加载。
                   console.log(node.data);
                   console.log("勾选");
                   doGetFeatureSet(node.data.key,node.data.title);//将请求到的原始数据存储到本地全局变量dataGot中（记得防止全局变量污染）
                   console.log("勾选请求到："+dataGot);
                   //featureKey = node.data.key;
               }else if(target=='checkbox'&&!flag){
                   //当被取消勾选时，触发的事件。
                   //当被取消勾选时，清空存储在本地的服务器请求数据。
                   dataGot = {};
                   console.log("取消勾选,现在的dataGot为："+dataGot);
               }
            }
        });
        console.log("final");
        console.log(data);

    });

}
//----------

//----FJH增加
function doGetFeatureSet(id,name){
    var poiData;
    $.ajax({
        type : "get",
        async: true,
        //url : "http://qk.casm.ac.cn:9090/geowinmap/ds?r=0.41608809004258696&serviceproviderid=map.mapservice&serviceid=circlequery&cl=83.16650390625001,32.30570601389429&ol=84.04541015625001,33.04550781490999&z=7&layerids="+id+"&__conditiontokenid=&requesttype=jsonp&jsonpcallback=?",
        url:"http://qk.casm.ac.cn:9090/geowinmap/ds?r=0.2376504259402532&serviceproviderid=map.mapservice&serviceid=rectquery&cl=100,26&ol=77,36&z=4&layerids="+id+"&__conditiontokenid=&requesttype=jsonp&jsonpcallback=?",
        dataType: "jsonp",
        jsonp:"callback", //请求php的参数名
        jsonpCallback: "jsonhandle",//要执行的回调函数
        success : function(data) {
            console.log(data);
            //requestSucceeded(data);
             poiData = JSON.parse(data);
             dataGot.data = poiData;
            dataGot.id = id;
            dataGot.name = name;
        }
    });
    return poiData;
}
function requestSucceeded(response, io) {
    var features = [];
    require([
        "esri/map",
        "esri/layers/FeatureLayer",
        "esri/dijit/PopupTemplate",
        "esri/request",
        "esri/geometry/Point",
        "esri/graphic",
        "dojo/on",
        "dojo/_base/array",
        "dojo/domReady!"
    ], function(
        Map,
        FeatureLayer,
        PopupTemplate,
        esriRequest,
        Point,
        Graphic,
        on,
        array
    ) {
        //loop through the items and add to the feature layer
        var poiData = JSON.parse(response);
        console.log(poiData);

/*        用请求到的数据新建对象，可以加载最后点击确定的时刻在加载，节省内存。
            for(i in poiData){
            var geometry = new Point(poiData[i].point);
            var graphic = new Graphic(geometry);
            // var attr = {};
            // graphic.setAttributes(attr);
            features.push(graphic);
        }*/
    })
    return poiData;
}
function initFeature(){
    require([
        "esri/map",
        "esri/layers/FeatureLayer",
        "esri/dijit/PopupTemplate",
        "esri/request",
        "esri/geometry/Point",
        "esri/graphic",
        "dojo/on",
        "dojo/_base/array",
        "dojo/domReady!"
    ], function(
        Map,
        FeatureLayer,
        PopupTemplate,
        esriRequest,
        Point,
        Graphic,
        on,
        array
    ) {
        //create a feature collection for the flickr photos
        var featureCollection = {
            "layerDefinition": null,
            "featureSet": {
                "features": [],
                "geometryType": "esriGeometryPoint"
            }
        };
        featureCollection.layerDefinition = {
            "geometryType": "esriGeometryPoint",
            "objectIdField": "ObjectID",
            "drawingInfo": {
                "renderer": {
                    "type": "simple",
                    "symbol": {
                        "type": "esriPMS",
                        "url": "./image/title.png",
                        "contentType": "image/png",
                        "width": 15,
                        "height": 15
                    }
                }
            },
            "fields": [{
                "name": "ObjectID",
                "alias": "ObjectID",
                "type": "esriFieldTypeOID"
            }, {
                "name": "description",
                "alias": "Description",
                "type": "esriFieldTypeString"
            }, {
                "name": "title",
                "alias": "Title",
                "type": "esriFieldTypeString"
            }]
        };

        //define a popup template
        var popupTemplate = new PopupTemplate({
            title: "{title}",
            description: "{description}"
        });

        //create a feature layer based on the feature collection
        featureLayerTree = new FeatureLayer(featureCollection, {
            id: 'flickrLayer',
            infoTemplate: popupTemplate
        });

        //associate the features with the popup on click
        featureLayerTree.on("click", function(evt) {
            map.infoWindow.setFeatures([evt.graphic]);
        });
        //add the feature layer that contains the flickr photos to the map
        map.addLayers([featureLayerTree]);
    })
}
function changeInput(node){
    if(node.value=='text'){
        $("#FLAddress").attr("disabled",false);
        $("#openTreeWindow ").attr("disabled",true);
        $("#openTreeWindow ").addClass("layui-btn-disabled");
        /*//取消另一种方式显示的图层
        thisLayer = map.getLayer(featureLayerItem.data)
        thisLayer.hide();
        thisLayer = map.getLayer($("#FLAddress").val());
        thisLayer.show();*/
        return;
    }else if(node.value=='button'){
        $("#openTreeWindow ").attr("disabled",false);
        $("#openTreeWindow ").removeClass("layui-btn-disabled");
        $("#FLAddress").attr("disabled",true);
        /*//取消另一种方式显示的图层
        anotherLayer = map.getLayer($("#FLAddress").val());
        anotherLayer.hide();
        //thisLayer = map.getLayer();
        //thisLayer.show();*/
        return;
    }
}
//-----------
function featureLayerSet(node) {
    //获取当前要素服务在制图模板中信息
    //要素图层中的小齿轮
    featureLayerItem = template.featureLayer.modules[$($(node).parent()).attr("data-index")]
    templateIndex = $($(node).parent()).attr("data-index");
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            title: '设置',
            skin: "layui-layer-lan",

            yes: function (index, layero) {
                //如果选择的是输入地址
                if($("#textLayer").is(":checked"))
                {
                    $($(node).parent()).find(".featureLayerCssI1").css("color", '#e2e2e2');
                    index = $("#FLAddress").attr("templateIndex");
                    var data = template.featureLayer.modules[index].data;
                    if(data !== null && data !== undefined && data !== ''){
                        thisFLayer = map.getLayer(data)
                        if(thisFLayer){
                            map.removeLayer(thisFLayer)
                        }
                        $(node).attr("state", "0")
                    }
                    template.featureLayer.modules[index].data = $("#FLAddress").val();
                    generateFeatureLayer();
                }
                var index1 = layer.open();
                layer.close(index1);
            },
            content: "<form><br/><p class='FLS_p'><input id='textLayer' name='layer' value='text' type='radio' onclick='changeInput(this)' />服务地址：<input id='FLAddress' templateIndex='" + templateIndex + "'  value='"+featureLayerItem.data+"'></input><i class='FLS_i fa fa-cog'></i></p>"
                // @YH改：
                + "<p class='tree_p'><input id='buttonLayer' name='layer' value='button' type='radio' onclick='changeInput(this)' />专题服务：<button id='openTreeWindow' class='layui-btn layui-btn-sm layui-btn-disabled' disabled>选择要素</button></p><form/>"
                //----------
        });
        // @YH改：
        $("#openTreeWindow").click(function (e) {
            var hasClicked = node;//获取要素图层被点击的小齿轮
            //$($(node).parent()).find(".featureLayerCssI1").css("color", '#e2e2e2');
            var dataIndex = $("#FLAddress").attr("templateIndex");
            /*var data = template.featureLayer.modules[index].data;
            if(data !== null && data !== undefined && data !== ''){
                thisFLayer = map.getLayer(data)
                if(thisFLayer){
                    map.removeLayer(thisFLayer)
                }
                $(node).attr("state", "0")
            }*/
            treeWindow(hasClicked,dataIndex);
        })
        //----------
        //为地址组件绑定事件
        $(".FLS_i").click(function () {
            //changePolygonStyle();
            var index1 = layer.open();
            layer.close(index1);
            editFeatureLayer(map.getLayer(featureLayerItem.data),featureLayerItem.type,featureLayerItem.name);
            //alert(featureLayerItem.type);
        });

    });
    /* alert($($(this).parent()).attr("data-type"))
    alert($($(this).parent()).attr("data-index")) */

}
var featureLayerExtent = null;
//有数据，但没加载图层。用此函数将data代表的图层添加上去
function addFeature(node) {
    featureLayerItem = template.featureLayer.modules[$($(node).parent()).attr("data-index")];
    if(!featureLayerItem||!featureLayerItem.data)return;
    require([
        "esri/layers/FeatureLayer",
        "esri/InfoTemplate", "esri/dijit/PopupTemplate", "esri/renderers/SimpleRenderer", "esri/layers/GraphicsLayer"
    ], function (FeatureLayer, InfoTemplate, PopupTemplate, SimpleRenderer, GraphicsLayer) {
        //这一步会影响
        var simpleJson = {
            "type": "simple",
            "label": featureLayerItem.name,
            "description": "",
            // "symbol": isEdited()?renderer:featureLayerItem.style.render
            "symbol":featureLayerItem.style.render
        }
        var rend = new SimpleRenderer(simpleJson);
        //var infoTemplate = new InfoTemplate("${NAME}", "${*}");
        var infoTemplate = new InfoTemplate("${NAME}", "${*}");
        var serviceUrl = featureLayerItem.data;
        var layer = new FeatureLayer(serviceUrl, {
            mode: FeatureLayer.MODE_SNAPSHOT,
            outFields: ["*"],
            opacity: featureLayerItem.style.opacity,
            infoTemplate: infoTemplate,
            id: featureLayerItem.data
        });
        layer.setRenderer(rend)
        map.addLayer(layer);
        map.on("layer-add-result",function(e){
            if(e.error){
                return;
            }else {
                $(node).attr("state", "1");
                $($(node).parent()).find(".featureLayerCssI1").css("color", ' rgb(0, 253, 255)');
            }
        })
    });
}

function getFeatureLayerExtent(){
    var fullExtent = null;
    var x = 0;
    for(i=0;i<map.graphicsLayerIds.length;i++){
        var id = map.graphicsLayerIds[i];
        var layer = map.getLayer(id);
        layer.on("load", function(service) {
            x++;
            var extent = layer.fullExtent?layer.fullExtent:layer.initialExtent;
            if(!fullExtent){
                fullExtent = extent;
            }else{
                fullExtent.union(extent);
            }
            if(x==map.graphicsLayerIds.length-1){
                if(fullExtent.spatialReference.wkid!=map.spatialReference.wkid ){
                    require(["esri/tasks/GeometryService","esri/config"], function(GeometryService,config) {
                        //配置代理
                        config.defaults.io.proxyUrl = "../esriproxy/";
                        config.defaults.io.alwaysUseProxy = false;
                        var geometryService = new GeometryService(ESRI_GeometyService);
                        geometryService.project([fullExtent],map.spatialReference, function (p) {
                            map.setExtent(p[0]);
                            console.log(p);
                        });
                    });
                }else {
                    map.setExtent(fullExtent);
                }
            }
        });
    }
    return fullExtent;
    try {
        var layerExtent=layer.fullExtent;
        //如果图层与地图坐标系不同，转换一下再设置全局范围
        if(layerExtent.spatialReference.wkid!=map.spatialReference.wkid ){
            require(["esri/tasks/GeometryService","esri/config"], function(GeometryService,config) {
                //配置代理
                config.defaults.io.proxyUrl = "../esriproxy/";
                config.defaults.io.alwaysUseProxy = false;
                var geometryService = new GeometryService(ESRI_GeometyService);
                geometryService.project([layerExtent],map.spatialReference, function (p) {
                    console.log(p);
                    if(!featureLayerExtent){
                        featureLayerExtent= p[0];
                    } else{
                        featureLayerExtent = featureLayerExtent.union(p[0]);
                    }
                });
            });
        }else {
            map.setExtent(layerExtent);
        }
    }catch (e) {
        lay.open({
            title: '提示'
            ,content: '服务图层与底图图层坐标系统不同，暂时无法缩放！您可手动缩放到该图层！'
        });
        console.log(e);
    }
}

function addService(node){
    var serviceLayerItem = template.serviceLayer.modules[$($(node).parent()).attr("data-index")]
    if(!serviceLayerItem||!serviceLayerItem.url)return;
    require([
        "esri/layers/ArcGISDynamicMapServiceLayer",
        "esri/InfoTemplate", "esri/dijit/PopupTemplate"
    ], function (ArcGISDynamicMapServiceLayer, InfoTemplate, PopupTemplate) {
        var infoTemplate = new InfoTemplate("${NAME}", "${*}");
        var serviceUrl = serviceLayerItem.url;
        serviceUrlstr=serviceUrl.substring(0,serviceUrl.lastIndexOf("/"));
        var layer = new ArcGISDynamicMapServiceLayer(serviceUrlstr,{id: serviceLayerItem.url});
        var showindex=serviceUrl.substring(serviceUrl.lastIndexOf("/")+1,serviceUrl.length);
        layer.setVisibleLayers([showindex]);
        map.addLayer(layer);
        map.on("layer-add-result",function(e){
            if(e.error){
                return;
            }else{
                $(node).attr("state", "1");
                $($(node).parent()).find(".serviceLayerCssI1").css("color", ' rgb(0, 253, 255)');
            }
        })
    });
}

function createFeatureCollection(data) {//data:dataGot中获得的数据
    var poiData = data.data;
    var featureLayer;
    var featureCollection = {
        "layerDefinition": null,
        "featureSet": {
            "features": [],
            "geometryType": "esriGeometryPoint"
        }
    };
    featureCollection.layerDefinition = {
        "geometryType": "esriGeometryPoint",
        "objectIdField": "ObjectID",
        "drawingInfo": {
            "renderer": {
                "type": "simple",
                "symbol": {
                    "type": "esriPMS",
                    "url": "./image/title.png",
                    "contentType": "image/png",
                    "width": 15,
                    "height": 15
                }
            }
        },
        "fields": [{
            "name": "ObjectID",
            "alias": "ObjectID",
            "type": "esriFieldTypeOID"
        }, {
            "name": "description",
            "alias": "Description",
            "type": "esriFieldTypeString"
        }, {
            "name": "title",
            "alias": "Title",
            "type": "esriFieldTypeString"
        }]
    };
    var features=[];
    require([
        "esri/layers/FeatureLayer",
    ], function(FeatureLayer) {
        featureLayer = new FeatureLayer(featureCollection, {
            id: '55'
        });
    });
    return {
        featureObj:featureLayer,
        data:data
    }
}