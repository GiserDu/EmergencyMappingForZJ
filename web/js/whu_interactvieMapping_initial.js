var map;//没有使用var声明的变量，会成为全局对象window的属性,这里仅仅声明map为一个全局变量!
var tb;//toolbar,绘制用
var featureLayerTree;
var baseLayerHB;
var baseLayerURL;//用于进行矢量,影像底图切换时,保证当前行政区划底图的url
var zjBaseLayer;
var winWidth=0;
var winHeight=0;
var template = {};//
var indi = new Array();//建立所选指标的全局变量,初始情况下indi.length==0
var classIndex;//当前所选分级指标
var classGLayer,chartGLayer;//预设分级图层和统计图层
var regionParam = 0;//regionParam为所选的区域代码(1:初始第一级17个地级市;其他:对应地州市的区域代码)
var rgnName;//当前所选区域名称
var geometry = new Object();//当前所选区域中心点对象
var baseMap=new Array();//底图
var doMapIndex=0;//制图目录树表示，0表示未构造，1表示构造
var doMapIndex_Template =0;//模板制图目录树表示
var ARIndex=0;//行政区目录树表示，0表示为构造，1表示构造
var studyAreaLayer;//制图区域
var ServerLayerArr=[];//专题服务数组
var iframeWinIndex //制图模板弹窗索引
var layerNodesObj;
var layerNodesObj_Template;// 模板制图树对象
var selectedNode; //当前选择的要素节点
// var selectedID;
var nodeTheme;
var nodePath; //存储节点的路径
var getThisTheme;
var getThisPath = [];//当前要素节点的所有父节点（包括自己）
var textEditFlag = 0; //标识对服务地址要素图层编辑时的一种特殊情况
//图层目录树结构，底图图层可包含多个服务，用逗号隔开，需要声明服务类型：WebTiledLayer，ArcGISDynamicMapServiceLayer，ArcGISTiledMapServiceLayer
var layerNodes =[
    {id:1, pId:0, name:"地理底图", open:true, "nocheck":true,children:[
            //{id:102, name:"矢量图",url:"http://t0.tianditu.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=${col}&TILEROW=${row}&TILEMATRIX=${level},http://t2.tianditu.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=${col}&TILEROW=${row}&TILEMATRIX=${level}",mapType:"WebTiledLayer",checked:true},
            {id:101, name:"矢量图",url:"http://t0.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=${col}&TILEROW=${row}&TILEMATRIX=${level}&tk=cede75bb109d5e8048ebc21308b91e54,http://t0.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=${col}&TILEROW=${row}&TILEMATRIX=${level}&tk=cede75bb109d5e8048ebc21308b91e54",mapType:"WebTiledLayer",checked:true},
            {id:102, name:"影像图",url:"http://t0.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={level}&TILEROW={row}&TILECOL={col}&tk=cede75bb109d5e8048ebc21308b91e54,http://t0.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={level}&TILEROW={row}&TILECOL={col}&tk=cede75bb109d5e8048ebc21308b91e54",mapType:"WebTiledLayer"},
               /*{id:101,name:"新疆矢量图",url:"http://106.124.138.198:8080/OneMapServer/rest/services/XJ_VECTOR/MapServer,http://106.124.138.198:8080/OneMapServer/rest/services/XJ_POI/MapServer",checked:true}*/
          // {id:101, name:"专项矢量图",url:"http://qk.casm.ac.cn:9090/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=map&y=${row}&x=${col}&z=${level}",mapType:"WebTiledLayer"},
            //{id:102, name:"专项影像图",url:"http://qk.casm.ac.cn:9090/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=sate&y=${row}&x=${col}&z=${level}",mapType:"WebTiledLayer"}
        ]},

    {id:2, pId:0, name:"专题服务图层",isParent:true, open:true,children:[], "nocheck":true},

    {id:3, pId:0, name:"要素图层", isParent:true,open:true,children:[], "nocheck":true},
    {id:4, pId:0, name:"统计图层", isParent:true,open:true,children:[], "nocheck":true}

];
var addressChanged ;//记录要素编辑时，要素地址有无发生变化
var nameChanged ;
var buttonChanged;//记录要素编辑时，有没有点击button改变要素
var alertFlag = 0; //是否第一次弹出模板选择layer
var layerIndex;
var typeFlag; //记录当前打开的是模板还是空白制图 0:模板；1：空白
var layerCloseFlag = 0; //是否选择了模板或新建了空白模板
var templateFlag = 0; //是否打开过模板layer
var blankFlag = 0; //是否打开过空白制图layer
var templateClassNo; //模板layer的id
var blankClassNo; //空白制图layer的id
var thematicData={};
var iMLegend;//iM means interactiveMapping
var iMLegendCreated = false;//图例是否创建？
var tjLayerName = ""; //统计图层名作为layer的id
var zoomFlag = 0; //是否已下钻
var fieldsOrIndi = "";
var field_cn = "";
var treeNodeUrl; //记录统计图层节点的原始图层名（即graphicslayer的id）
var mappingPageType = 0; //记录当前制图页面的类别 0(blank)：普通制图；1(edit)：用户地图编辑；2(readOnly)：用户地图查看
var isSaved = 0; //记录用户的制图操作是否已保存 0：未保存；1：已保存
$(document).ready(function() {
    findDimensions();
    $("#mapContainer").height(winHeight);
    initMap();

    //获取当前窗口尺寸
    function findDimensions() {
        //获取窗口宽度
        if(window.innerWidth)
            winWidth=window.innerWidth;
        else if (document.body&&document.body.clientWidth)
            winWidth=document.body.clientWidth;
        if(window.innerHeight)
            winHeight=window.innerHeight;
        else if (document.body&&document.body.clientHeight)
            winHeight=document.body.clientHeight;
        //通过深入Document内部对body进行检测，获取窗口大小
        if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth)
        {
            winHeight = document.documentElement.clientHeight;
            winWidth = document.documentElement.clientWidth;
        }
    };

    //浏览器尺寸发生变化是动态适应页面
    $(window).resize(function () {
        findDimensions();
        $("#mapContainer").height(winHeight);
    });


    function initMap() {
        require(["esri/layers/TileInfo","esri/map","esri/config","esri/layers/WebTiledLayer","esri/layers/ArcGISDynamicMapServiceLayer","esri/layers/ArcGISTiledMapServiceLayer","esri/layers/GraphicsLayer"],function (TileInfo,Map,config,WebTiledLayer,ArcGISDynamicMapServiceLayer,ArcGISTiledMapServiceLayer,GraphicsLayer) {
            map = new Map("mapContainer", {
                //basemap:"dark-gray-vector",
                logo:false,
                center: [104,35],
                zoom: 7
            });

               /* //配置代理
                config.defaults.io.proxyUrl = "../esriproxy/";
                config.defaults.io.alwaysUseProxy = false;*/
            map.on("load", function (evt) {
                //processResults(testData);
            });
           baseMap=new Array();
           var baseMapUrls=layerNodes[0].children[0].url.split(",");
           var baseMapType=layerNodes[0].children[0].mapType;
            $.each(baseMapUrls, function (i) {
                var url=baseMapUrls[i];
                switch (baseMapType){
                    case "WebTiledLayer":
                        baseMap.push(new WebTiledLayer(url,{"id":url
        }));
        break;
                    case "ArcGISDynamicMapServiceLayer":
                        baseMap.push(new ArcGISDynamicMapServiceLayer(url,{"id":url}));
                        break;
                    case "ArcGISTiledMapServiceLayer":
                        baseMap.push(new ArcGISTiledMapServiceLayer(url,{"id":url}));
                        break;

                }
                map.addLayer(baseMap[i]);

            });

            // 加载浙江行政区划底图
            zjBaseLayer = new ArcGISDynamicMapServiceLayer("http://47.96.162.249:6080/arcgis/rest/services/zjmap/ZJ_regions33/MapServer",{});
            zjBaseLayer.setVisibleLayers([1]);

            map.addLayer(zjBaseLayer);
            require(['esri/geometry/Point'],function(EsriPoint){
                map.centerAndZoom(new EsriPoint({
                    x: 120.33999,
                    y: 29.231858,
                    spatialReference: {
                        wkid: 4326
                    }
                }), 8)
            });

           //baseMap=new ArcGISTiledMapServiceLayer("http://106.124.138.198:8080/OneMapServer/rest/services/XJ_VECTOR/MapServer");
           /*baseMap = new WebTiledLayer(
                //'http://qk.casm.ac.cn:9090/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=map&y=${row}&x=${col}&z=${level}',{id:"baseMap"}
                'https://${subDomain}.tile.thunderforest.com/cycle/${level}/${col}/${row}.png',{"copyright": 'Maps © <a href="http://www.thunderforest.com">Thunderforest</a>, Data © <a href="http://www.openstreetmap.org/copyright">OpenStreetMap contributors</a>',
                    "id": "OpenCycleMap",
                    "subDomains": ["a", "b", "c"]}
            );*/
           // map.addLayer(baseMap);
            studyAreaLayer=new GraphicsLayer('',{id:"studyAreaLayer",name:"studyAreaLayer"});
            studyAreaLayer.name = "studyAreaLayer";
            map.addLayer(studyAreaLayer);
            mapExtentChange = map.on("zoom-end", function zoomed() {
                var zoomLevel = map.getZoom();
                if  (zoomFlag == 0){
                    if (zoomLevel > 8){
                        zjBaseLayer.setVisibleLayers([0]);
                        onZoomInLevelAbove10();
                        zoomFlag = 1;
                    }
                }
                else if (zoomFlag == 1){
                    if (zoomLevel < 9){
                        zjBaseLayer.setVisibleLayers([1]);
                        onZoomInLevelBelow10();
                        zoomFlag = 0;
                    }
                }
        });
        });
    }
    var getUrlParams=function (url) {
        //获取url后缀参数，返回参数对象,
        var urlParamsObj={
            "mappingType":"",
            "mapId":"",
        };
        if(!window.location.search){
            urlParamsObj.mappingType=0;
        }else {
            var paramsArr=url.split("?")[1].split("&");
            urlParamsObj.mappingType =paramsArr[0].split("=")[1];
            urlParamsObj.mapId=paramsArr[1].split("=")[1]
        }
        return urlParamsObj;
    };
    switch (getUrlParams(window.location.href).mappingType){
        case "blank":
            mappingPageType = 0;
            break;
        case "edit":
            mappingPageType = 1;
            break;
        case "readOnly":
            mappingPageType = 2;
            break;
    }
    if (mappingPageType == 0){
        layui.use('layer', function () {
            var layer1 = layui.layer;
            layer1.open({
                title: '制图模板选择',
                skin: "layui-layer-lan",
                type: 2,
                shade: 0,
                resize: false,
                area: ["600px","500px"],
                // btn: ['按钮1','按钮2','按钮3'],
                content: 'indexMini_zj.html',
                success: function (layero, index) {
                    $("#layui-layer-iframe1").css("height",'456px');
                },
                yes: function(index, layero) {//确定后执行回调

                },
                cancel: function(index, layero){
                    blank_btnClick();
                }
            });
        });
    }
    else {
        $("#templateMap").css("display", "none");
        var userMapId = localStorage.getItem("mapId");
        addModelLayUI(userMapId);
    }
    if (mappingPageType == 2) {
        isSaved = 1;
        $("#map-upload").css("display", "none");
    }
    else if (mappingPageType == 1)
        $("#plotButton").css("display", "none");
    window.onbeforeunload=function(e) {
        var unloads = isSaved;
        if (unloads == 0) {
            return "您确定要退出页面吗？";
        }
    }
    window.onunload= function(){
        window;
    }
});
//框选定位
$("#RecNav").click(function () {

    require(["esri/toolbars/draw","esri/graphic","esri/symbols/SimpleFillSymbol","esri/layers/GraphicsLayer"],function (Draw,Graphic,SimpleFillSymbol,GraphicsLayer) {

        tb= new Draw(map);
        tb.on("draw-end", function(evt){

            map.setExtent(evt.geometry.getExtent());
            var symbol_Rec = new SimpleFillSymbol();
            symbol_Rec.color.a=0.01;
            var graphic = new Graphic(evt.geometry,symbol_Rec);
            //判断框选图层是否被移除
            if (!map.getLayer("studyAreaLayer")){
                studyAreaLayer.clear();
                map.addLayer(studyAreaLayer);
                studyAreaLayer.add(graphic);
            }
            else {
                studyAreaLayer.clear();
                studyAreaLayer.add(graphic);
            }

            tb.deactivate();
            map.enableMapNavigation();
            map.showZoomSlider();
        });
        map.disableMapNavigation();
        tb.activate(Draw.EXTENT);    //激活相应的图形
    })
});
//取消定位选择框
$("#cancelSelect").click(function () {
    studyAreaLayer.clear();

})
//行政区定位
$("#adminNav").click(function () {
    if(ARIndex==0){
        creatARpanel("区域","administrativeRegion");
        ARIndex=1
    }

    layui.use('layer', function () {
        var layer1 = layui.layer;
        layer1.open({
            title: '行政区选择',
            skin: "layui-layer-lan",
            type: 1,
            shade: 0,
            content: $('#administrativeRegion'),
            success: function () {

            },
            yes: function(index, layero) {//确定后执行回调

            }
        });
    });
});

//空模板制图
//TODO:增加统计图层配置，图层排序
function doMap() {
    doMapping(layerNodes)
    //制图树实现函数
    function doMapping(layerNodes_InFunc) {
        //树的设置选项，大部分是回调函数
        var setting = {
            check: {
                enable: true
            },
            data: {
                keep:{
                    parent: true
                },
                simpleData: {
                    enable: true
                }
            },
            edit: {
                enable: true,
                drag: {
                    isCopy: false,
                    isMove: false,
                }, //节点不能拖拽
                showRenameBtn: false,
                showRemoveBtn: false
            },
            view:{
                addHoverDom: addHoverDom,
                removeHoverDom: removeHoverDom
            },
            callback: {
                beforeCheck: function (treeId,treeNode) {
                    layerOncheck("doMapTree",treeNode);
                 },//勾选前回调，用于加载图层
                beforeRemove: beforeRemove,//移除前回调
                beforeRename: beforeRename//修改名字之前回调
            }
        };
        function addHoverDom(treeId, treeNode) {
            var aObj = $("#" + treeNode.tId + "_a");

            //首先判断是否是父节点，非父亲节点不能增加
            if(treeNode.isParent){

                //如果是底图，没有增加按钮，直接return
                if(treeNode.id==1){
                    return;
                }

                if ($("#doMapAdd_"+treeNode.id).length>0) return;
                var editStr = "<span id='doMapAdd_"+treeNode.id+"' class='button doMapAdd'  onfocus='this.blur();'></span>";
                aObj.append(editStr);
                var btn = $("#doMapAdd_"+treeNode.id);
                //绑定添加子节点事件
                if (btn) btn.bind("click", function(){
                    if(treeNode.id==2){//针对专题服务图层的添加事件
                        layui.use('layer', function (layui_index) {
                            var layer = layui.layer;
                            layer.open({
                                title: '添加专题服务',
                                skin: "layui-layer-lan",
                                type: 0,
                                shade: 0,
                                content:"<div><p>服务名称：<input id='newSLName'></input></p><br/><p>服务地址：<input id='newSLAds'></input></p></div>",
                                yes: function(index, layero) {//确定后执行回调
                                    if($("#newSLName").val()==""||$("#newSLAds").val()==""){
                                        alert("属性不能为空！");
                                        return;
                                    }
                                    var newnode={name:$("#newSLName").val(),url:$("#newSLAds").val()};
                                    layerNodes[1].children.push(newnode);
                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                                    treeObj.addNodes(treeNode,-1, newnode);

                                    require([
                                        "esri/layers/ArcGISDynamicMapServiceLayer",
                                        "esri/InfoTemplate", "esri/dijit/PopupTemplate"
                                    ], function (ArcGISDynamicMapServiceLayer, InfoTemplate, PopupTemplate)
                                    {
                                        var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                                        //判断专题服务是图层还是地图
                                        var serviceUrl = $("#newSLAds").val();
                                        var serviceUrlstr=serviceUrl;
                                        if(serviceUrl.substr(-9)!="MapServer"){
                                            serviceUrlstr=serviceUrl.substring(0,serviceUrl.lastIndexOf("/"));
                                        }

                                        try{
                                            var layer = new ArcGISDynamicMapServiceLayer(serviceUrlstr,{id:$("#newSLName").val()+"_"+$("#newSLAds").val()});
                                        }catch (e) {
                                            alert("服务地址有误！")
                                            return false;
                                        }
                                        //如果是图层，则只显示这个图层
                                        if(serviceUrl.substr(-9)!="MapServer"){
                                            var showindex=serviceUrl.substring(serviceUrl.lastIndexOf("/")+1,serviceUrl.length);
                                            layer.setVisibleLayers([showindex]);
                                        }


                                        //var layer = new ArcGISDynamicMapServiceLayer(serviceUrl,{id: $("#newSLName").val()+"_"+$("#newSLAds").val()});
                                        ServerLayerArr.push(layer);
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
                                                layui.use('layer', function () {
                                                    var lay = layui.layer;
                                                    lay.confirm('加载成功，缩放到该图层?', {
                                                        icon: 3,
                                                        title: '提示'
                                                    }, function (layui_index) {
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
                                                        }catch (e) {
                                                            lay.open({
                                                                title: '提示'
                                                                ,content: '服务图层与底图图层坐标系统不同，暂时无法缩放！您可手动缩放到该图层！'
                                                            });
                                                            console.log(e);
                                                        }
                                                        lay.close(layui_index);
                                                    })
                                                })
                                            }
                                        },this)
                                    });

                                    layer.close(index);
                                }});
                        });
                    }
                    if(treeNode.id==3){//针对要素图层的添加事件
                        layui.use('layer', function (layui_index) {
                            var layer = layui.layer;
                            layer.open({
                                title: '添加要素图层',
                                skin: "layui-layer-lan",
                                type: 0,
                                shade: 0,
                                // content:"<div><p>要素名称：<input id='newFLName'></input></p><br/><p>要素地址：<input id='newFLAds'></input></p></div>",
                                content: "<div id='zeo'><p style='padding-left: 12px'>要素名称：<input id='newFLName'></input></p><br/><p class='FLS_p'><input id='textLayer' name='layer' value='text' type='radio' onclick='changeSource(this)'/>服务地址：<input id='newFLAds' disabled></input></p>"

                                + "<br/><p class='tree_p' style='display: none'><input id='buttonLayer' name='layer' value='button' type='radio' onclick='changeSource(this)' />专题服务：<button id='selectButton' onclick='openTreeWindow()' class='layui-btn layui-btn-sm layui-btn-disabled' disabled>选择要素</button></p></div>",

                                yes: function(index, layero) {//（第一个按钮，即确认）确定后执行回调

                                    var textLayerChecked = $("#textLayer").is(":checked")?"checked":"unchecked";
                                    var buttonLayerChecked = $("#buttonLayer").is(":checked")?"checked":"unchecked";
                                    var textLayerDisabled = $("#newFLAds").prop("disabled")==true?"disabled":"undisabled";
                                    var buttonLayerDisabled = $("#selectButton").prop("disabled")==true?"disabled":"undisabled";
                                    if (textLayerChecked == "checked" && buttonLayerChecked == "unchecked"){
                                        if($("#newFLName").val()==""||$("#newFLAds").val()=="") {
                                            alert("属性不能为空1！");
                                            return;
                                        }
                                    }
                                    else if (textLayerChecked == "unchecked" && buttonLayerChecked == "checked"){
                                        // document.getElementById("selectButton").innerHTML="New text!";
                                        // alert($("#selectButton").html());
                                        if($("#newFLName").val()==""||$("#selectButton").html()=="选择要素") {
                                            alert("属性不能为空2！");
                                            return;
                                        }
                                    }
                                    else{
                                        alert("属性不能为空3！");
                                        return;
                                    }
                                    if (textLayerChecked == "checked"){
                                        nodePath = [];
                                        nodeTheme = null;
                                    }

                                    var newNode={name:$("#newFLName").val(),url:$("#newFLAds").val(),textLayerChecked:textLayerChecked,buttonLayerChecked:buttonLayerChecked,textLayerDisabled:textLayerDisabled,buttonLayerDisabled:buttonLayerDisabled,lastUrl:"0", nodePath:nodePath, theme:nodeTheme};
                                    layerNodes[2].children.push(newNode);
                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                                    treeObj.addNodes(treeNode,-1, newNode);
                                    console.log(newNode["nodePath"]);
                                    layer.close(index);
                                }
                            });
                        });
                    }
                    if(treeNode.id==4){//针对统计图层的添加事件
                        layui.use('layer', function() {
                            var layer = layui.layer;
                            layerIndex = layer.open({
                                type: 1,
                                title: ['添加统计图层'],
                                shadeClose: false,
                                skin: "layui-layer-lan tjLayerContent",
                                shade: 0,
                                area: ['700px', '480px'],
                                // content: $('#tjPanel'),
                                content:originalTjLayerContent,
                                success: function(layero,index){
                                    //do something
                                    opentjMenuLayer();

                                    // console.log(layero.find(".layui-layer-content").html());
                                    // var newNode={name:$("#newFLName").val(),url:$("#newFLAds").val()};

                                    $(".tjInfoSubmit").bind('click',function () {
                                        var tjLayertest=layero.find(".layui-layer-content").html();
                                        constructTjJson3();
                                        var index=layer.open({
                                            type: 0,
                                            title:"统计图层名称",
                                            skin:"layui-layer-lan",
                                            content:' <div style="margin-left:-24px">\n' +
                                            '             <label class="layui-form-label">图层名</label>\n' +
                                            '             <div class="layui-input-block" style="margin-left: 88px">\n' +
                                            '                  <input type="text" id="newSLName" name="tjLayerName" lay-verify="required" placeholder="请输入统计图层名称" autocomplete="off" class="layui-input">\n' +
                                            '             </div>\n' +
                                            '          </div>',
                                            yes:function (index,layero) {
                                                console.log("OK");

                                                tjLayerName = $("input[ name='tjLayerName' ]").val();

                                                //统计图层所有参数
                                                allTjLayerContent = {
                                                    "name": tjLayerName,
                                                    "spatialdata": tjPanel1,
                                                    "statisticdata": tjPanel2,
                                                    "cartographydata": tjPanel3
                                                }

                                                if (tjLayerName == "") {
                                                    layer.tips('请输入图层名称', '#newSLName')
                                                    // layer.alert('');
                                                }

                                                if (tjLayerName != "") {
                                                    var newNode = {
                                                        name: tjLayerName,
                                                        url: tjLayerName,
                                                        // dom: tjLayertest,
                                                        // cartographydata: tjPanel3,
                                                        allContent: allTjLayerContent,
                                                        checked: true
                                                    };
                                                    layerNodes[3].children.push(newNode);
                                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                                                    treeObj.addNodes(treeNode, -1, newNode);

                                                    switch (allTjLayerContent.cartographydata.type) {
                                                        case "1":
                                                            tjType = "chartLayerData";
                                                            break;
                                                        case "2":
                                                            tjType = "classLayerData";
                                                            break;
                                                    }
                                                    fieldsOrIndi = allTjLayerContent.statisticdata.fieldsName;
                                                    allTjLayerContent = JSON.stringify(allTjLayerContent);
                                                    console.log(allTjLayerContent);
                                                    var zoomLevel = map.getZoom();
                                                    if (zoomLevel < 9)
                                                        initTjLayer(allTjLayerContent, tjType, "1");
                                                    else
                                                        initTjLayer(allTjLayerContent, tjType, "2");
                                                    $("#legend-container .legend").remove();
                                                    layer.close(index);
                                                    layer.close(layerIndex);
                                                }
                                            }
                                        });
                                    });
                                },
                            });
                        });
                    }
                });
            }

            //如果不是父亲节点
            else{
                var editStr = "<span id='doMapEdit_"+treeNode.id+"' class='button doMapEdit'  onfocus='this.blur();'></span>"+
                    "<span id='doMapRemove_"+treeNode.id+"' class='button doMapRemove'  onfocus='this.blur();'></span>";
                //如果是地理底图，不显示删除
                if(treeNode.getParentNode().id==1){
                    editStr = "<span id='doMapEdit_"+treeNode.id+"' class='button doMapEdit'  onfocus='this.blur();'></span>";
                }
                if ($("#doMapEdit_"+treeNode.id).length>0) return;
                aObj.append(editStr);

                var btn = $("#doMapEdit_"+treeNode.id);
                if (btn) btn.bind("click", function(){
                    //编辑，根据父节点不同，功能不同
                    //编辑底图，只能编辑底图地址，不能编辑名字
                    if(treeNode.getParentNode().id==1){//如果是底图服务
                        layui.use('layer', function (layui_index) {
                            var layer = layui.layer;
                            layer.open({
                                title: '编辑底图服务',
                                skin: "layui-layer-lan",
                                type: 0,
                                shade: 0,
                                content:"<div><p>服务名称：<input id='editBLName' disabled value='"+treeNode.name+"'></input></p><br/><p>服务地址：<input id='editBLAds' value='"+treeNode.url+"'></input></p></div>",
                                yes: function(index, layero) {//确定后执行回调
                                    //  var editnode={name:$("#newSLName").val(),url:$("#newSLAds").val()};
                                    if($("#editBLName").val()==""||$("#editBLAds").val()==""){
                                        alert("属性不能为空！");
                                        return;
                                    }
                                    //treeNode.name=$("#editBLAds").val();
                                    $.each(baseMap,function (i) {
                                        if(treeNode.url.indexOf(baseMap[i].url)!=-1){
                                            map.removeLayer(baseMap[i]);
                                        }
                
                                    });
                                    treeNode.url=$("#editBLAds").val();
                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                                    treeObj.updateNode(treeNode);
                                    layerNodes.filter(function (p) {
                                        if(p.name==treeNode.name){
                                            p.url=treeNode.url;
                                        }
                                    });

                                    require(["esri/layers/ArcGISTiledMapServiceLayer"],function (ArcGISTiledMapServiceLayer) {
                                       /*  $.each(baseMap,function (i) {
                                            map.removeLayer(baseMap[i]);
                                        }); */
                                        baseMap=new Array();
                                        var urls= $("#editBLAds").val().split(",");
                                        $.each(urls, function (i) {
                                            var url=urls[i];
                                            baseMap.push(new ArcGISTiledMapServiceLayer(url,{"id":url}));
                                            map.addLayer(baseMap[i]);

                                        });
                                        $.each(baseMap,function (i) {
                                            map.reorderLayer(baseMap[i],i);
                                        });
                                    });
                                    /*baseMap._url.path=$("#editBLAds").val();
                                    $.each(baseMap,function (i) {
                                        map.addLayer(baseMap[i]);
                                    });
                                    if($.inArray("baseMap",map.layerIds)!=-1){
                                        map.getLayer("baseMap").refresh();
                                    }*/
                                    layer.close(index);
                                }});
                        });

                    }
                    //编辑专题服务图层
                    if(treeNode.getParentNode().id==2){//如果是专题服务
                        layui.use('layer', function (layui_index) {
                            var layer = layui.layer;
                            layer.open({
                                title: '编辑专题服务',
                                skin: "layui-layer-lan",
                                type: 0,
                                shade: 0,
                                content:"<div><p>服务名称：<input id='editSLName' value='"+treeNode.name+"'></input></p><br/><p>服务地址：<input id='editSLAds' value='"+treeNode.url+"'></input></p></div>",
                                yes: function(index, layero) {//确定后执行回调
                                    //  var editnode={name:$("#newSLName").val(),url:$("#newSLAds").val()};
                                    if($("#editSLName").val()==""||$("#editSLAds").val()==""){
                                        alert("属性不能为空！");
                                        return;
                                    }
                                    if(map&&(map.getLayer(treeNode.url))){
                                        var thisLayer = map.getLayer(treeNode.url);
                                        map.removeLayer(thisLayer);
                                    }
                                    treeNode.name=$("#editSLName").val();
                                    treeNode.url=$("#editSLAds").val();
                                    treeNode.checked=false;
                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                                    treeObj.updateNode(treeNode);

                                    layer.close(index);
                                }});
                        });

                    }
                    //编辑要素服务图层
                    if(treeNode.getParentNode().id==3){//如果是要素服务
                        addressChanged = false;
                        buttonChanged = false;
                        var layer = layui.layer;
                        getThisPath = treeNode["nodePath"];
                        getThisTheme = treeNode["theme"];
                        var changeSource1; //比较编辑前后数据来源方式是否发生变化
                        var changeSource2;
                        //对服务地址图层编辑时的一种特殊情况
                        if (treeNode.textLayerChecked == "checked" && getThisTheme == undefined){
                            getThisTheme = "选择要素";
                            textEditFlag = 1;
                        }

                        console.log(getThisPath);
                        layui.use('layer', function (layui_index) {

                            layer.open({
                                title: '编辑要素图层',
                                skin: "layui-layer-molv",
                                type: 0,
                                shade: 0,
                                btn: ['确认','修改要素样式'],
                                //content:"<div><p>要素名称：<input id='editFLName' value='"+treeNode.name+"'></input></p><br/><p>要素地址：<input id='editFLAds' value='"+treeNode.url+"'></input></p></div>",
                                content: "<div id='zeo'><p style='padding-left: 12px'>要素名称：<input id='editFLName' value='"+treeNode.name+"'onchange='nameChange()'></input></p><br/><p class='FLS_p'><input id='textLayer' name='layer' value='text' type='radio' onclick='changeSource(this)' "+treeNode.textLayerChecked+"/>服务地址：<input id='editFLAds' "+treeNode.textLayerDisabled+" value='"+treeNode.url+"'onchange='addressChange()'></input></p>"
                                + "<br/><p class='tree_p' style='display: none'><input id='buttonLayer' name='layer' value='button' type='radio' onclick='changeSource(this)' "+treeNode.buttonLayerChecked+"/>专题服务：<button id='selectButton' onclick='openSelectedTree(getThisPath)' class='layui-btn layui-btn-sm' "+treeNode.buttonLayerDisabled+">选择要素</button></p></div>",
                                success: function (layero, index) {
                                    document.getElementById("selectButton").innerHTML = getThisTheme;
                                    if (treeNode.textLayerChecked == "checked")
                                        getThisTheme = undefined;
                                    changeSource1 = (treeNode.textLayerChecked == "checked")?"text":"button";
                                },
                                yes: function(index, layero) {//确定后执行回调
                                    //  var editnode={name:$("#newSLName;").val(),url:$("#newSLAds").val()};
                                    // alert(selectedNode.name);

                                    //存储标签的加载方式
                                    var textLayerChecked = $("#textLayer").is(":checked")?"checked":"unchecked";
                                    var buttonLayerChecked = $("#buttonLayer").is(":checked")?"checked":"unchecked";
                                    var textLayerDisabled = $("#editFLAds").prop("disabled")==true?"disabled":"undisabled";
                                    var buttonLayerDisabled = $("#selectButton").prop("disabled")==true?"disabled":"undisabled";
                                    treeNode.textLayerChecked = textLayerChecked;
                                    treeNode.buttonLayerChecked = buttonLayerChecked;
                                    treeNode.textLayerDisabled = textLayerDisabled;
                                    treeNode.buttonLayerDisabled = buttonLayerDisabled;
                                    if ($("#selectButton").html()!= getThisTheme){  //判断专题图层是否做出了改变
                                        treeNode["nodePath"] = nodePath;
                                        treeNode["theme"] = nodeTheme;
                                    }

                                    console.log(treeNode);
                                    //treeNode.name=$("#editSLName").val();
                                    //treeNode.url=$("#editSLAds").val();
                                    var nodeIndex = treeNode.getIndex();
                                    layerNodes[2].children[nodeIndex].lastUrl =treeNode.url;
                                    treeNode.lastUrl=treeNode.url;
                                    treeNode.name=$("#editFLName").val();
                                    treeNode.url=$("#editFLAds").val();
                                    //记录数据源的开源方式
                                    layerNodes[2].children[nodeIndex].textLayerChecked = textLayerChecked;
                                    layerNodes[2].children[nodeIndex].buttonLayerChecked  = buttonLayerChecked ;
                                    layerNodes[2].children[nodeIndex].textLayerDisabled = textLayerDisabled;
                                    layerNodes[2].children[nodeIndex].buttonLayerDisabled = buttonLayerDisabled;
                                    layerNodes[2].children[nodeIndex].name = $("#editFLName").val();
                                    layerNodes[2].children[nodeIndex].url =$("#editFLAds").val();
                                    if (textLayerChecked == "checked" && buttonLayerChecked == "unchecked"){
                                        if($("#newFLName").val()==""||$("#newFLAds").val()=="") {
                                            alert("属性不能为空1！");
                                            return;
                                        }
                                    }
                                    else if (textLayerChecked == "unchecked" && buttonLayerChecked == "checked"){

                                        if($("#newFLName").val()==""||$("#selectButton").html()=="选择要素") {
                                            alert("属性不能为空2！");
                                            return;
                                        }
                                    }
                                    else{
                                        alert("属性不能为空3！");
                                        return;
                                    }
                                    console.log(treeNode.checked);
                                    changeSource2 = (treeNode.textLayerChecked == "checked")?"text":"button";
                                    //如果数据来源方式发生了改变，则先移除原有图层
                                    //FIXED:当图层地址填写错误时，是remove不掉的fromZeo
                                    if (changeSource2 != changeSource1){
                                        if (changeSource1 == "text"){
                                            if(map.getLayer(treeNode.lastUrl)){
                                                map.removeLayer(map.getLayer(treeNode.lastUrl));
                                            }else{
                                                console.log("图层地址有误");
                                            }
                                        }
                                        else
                                            map.removeLayer(map.getLayer(treeNode.thematicData.id));
                                    }
                                    //如果在勾选状态下被编辑，点击确定后直接在地图上更新图层
                                    if (treeNode.checked == true){
                                        treeNode.checked = false;
                                        layerOncheck("doMapTree", treeNode);
                                        treeNode.checked = true;
                                    }
                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                                    treeObj.updateNode(treeNode);
                                    //layerNodes=treeObj.transformToArray(treeObj.getNodes());
                                    //如果名字发生了变化则更新图例
                                    var layer_ = map.getLayer(treeNode.url)
                                    if(nameChanged&&layer_){
                                        var render_ = layer_.renderer;
                                        render_.label = treeNode.name;
                                        layer_.setRenderer(render_);
                                        layer_.refresh();
                                        if(!(typeof iMLegend ==="undefined"||null===iMLegend)){
                                            iMLegend.refresh();
                                        }

                                    }
                                    nameChanged = false;
                                    layer.close(index);
                                },
                                btn2: function(index, layero)
                                {
                                    //按钮【修改要素样式】的回调
                                    //判断是否勾选了
                                    if(treeNode.textLayerChecked==='unchecked'&&treeNode.buttonLayerChecked==='unchecked'){
                                        alert("未设置数据源，不能编辑！");
                                        return false;
                                    }else if($("#selectButton").html()==="选择要素"&&!($("#editFLAds").val())){//没有选择也没有填地址
                                        alert("未设置数据源，不能编辑！");
                                        return false;
                                    }
                                    //判断图层是否发生了任意改变
                                    if((treeNode.theme&&$("#selectButton").html()!= treeNode.theme)||((changeSource1&&changeSource2)&&changeSource1!=changeSource2)||addressChanged){
                                        alert("图层数据源发生了改变，请先点击确定加载图层！");
                                        return false;
                                    }
                                    var layerID;
                                    if(treeNode.textLayerChecked==="checked"){
                                        layerID = treeNode.url;
                                    }else if (treeNode.buttonLayerChecked==="checked"){
                                        layerID = treeNode.thematicData.id;
                                    }
                                    //判断数据源是否加载到地图上，没有加载也不能编辑
                                    if(treeNode.textLayerChecked==="checked"){//没有选择也没有填地址
                                        if(!(map.getLayer(layerID))){
                                            alert("在当前地图中找不到文本框所代表的图层，请检查地址是否有误！");
                                            return false;
                                        }
                                    }
                                    /*方法说明
                                     *@method editFeatureLayer
                                     *@param{layer,layerType,layerLabel}
                                     *@return {void}
                                    */
                                    var layerInMap = map.getLayer(layerID);
                                    var layerLabel = treeNode.name;
                                    var layerType;
                                    switch (layerInMap.geometryType)  {
                                        case "esriGeometryPoint":
                                            layerType = 'point'
                                            break;
                                        case "esriGeometryPolyline":
                                            layerType = 'polyline'
                                            break;
                                        case "esriGeometryPolygon":
                                            layerType = 'polygon'
                                            break;
                                    }
                                    editFeatureLayer(layerInMap,layerType,layerLabel);
                                    //return false 开启该代码可禁止点击该按钮关闭
                                }
                            });
                        });
                    }
                    //编辑统计服务图层
                    if(treeNode.getParentNode().id==4){//如果是统计服务
                        layui.use('layer', function () {
                            var layer = layui.layer;
                            layerIndex = layer.open({
                                type: 1,
                                title: ['编辑统计服务'],
                                shadeClose: false,
                                skin: "layui-layer-lan tjLayerContent",
                                shade: 0,
                                area: ['700px', '480px'],
                                // content:layerNodes[3].children[0].dom,
                                content:originalTjLayerContent,
                                success: function(layero,index){
                                    // modifytjMenuLayer(treeNode.cartographydata);
                                    modifytjMenuLayer_new(treeNode.allContent);

                                    // var newNode={name:$("#newFLName").val(),url:$("#newFLAds").val()};

                                    $(".tjInfoSubmit").bind('click',function () {
                                        var tjLayertest=layero.find(".layui-layer-content").html();
                                        // console.log(tjLayertest);
                                        constructTjJson3();
                                        // constructTjJson3_modify(treeNode.allContent);

                                        var index=layer.open({
                                            type: 0,
                                            title:"修改图层名称",
                                            skin:"layui-layer-lan",
                                            content:' <div style="margin-left:-24px">\n' +
                                            '             <label class="layui-form-label">图层名</label>\n' +
                                            '             <div class="layui-input-block" style="margin-left: 88px">\n' +
                                            '                  <input type="text" id="newSLName" name="tjLayerName1" lay-verify="required" placeholder="请输入统计图层名称" autocomplete="off" class="layui-input" value="'+treeNode.name+'">\n' +
                                            '             </div>\n' +
                                            '          </div>',
                                            yes:function (index,layero) {
                                                console.log("OK2");
                                                tjLayerName = $("input[ name='tjLayerName1' ]").val();
                                                var oldTjName = treeNode.name;
                                                treeNodeUrl = treeNode.url;

                                                allTjLayerContent = {
                                                    "name": tjLayerName,
                                                    "spatialdata": tjPanel1,
                                                    "statisticdata": tjPanel2,
                                                    "cartographydata": tjPanel3
                                                }

                                                if (tjLayerName == "") {
                                                    layer.tips('请输入图层名称', '#newSLName')
                                                    // layer.alert('');
                                                }

                                                if (tjLayerName != "") {
                                                    treeNode.name = tjLayerName;
                                                    treeNode.checked = true;
                                                    // treeNode.dom = tjLayertest;
                                                    // treeNode.cartographydata = tjPanel3;
                                                   treeNode.allContent=allTjLayerContent;

                                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                                                    treeObj.updateNode(treeNode);

                                                    switch (allTjLayerContent.cartographydata.type) {
                                                        case "1":
                                                            tjType = "chartLayerData";
                                                            break;
                                                        case "2":
                                                            tjType = "classLayerData";
                                                            break;
                                                    }
                                                    fieldsOrIndi = allTjLayerContent.statisticdata.fieldsName;
                                                    allTjLayerContent = JSON.stringify(allTjLayerContent);
                                                    console.log(allTjLayerContent);
                                                    if (tjLayerName != oldTjName){
                                                        tjLayerName = oldTjName;
                                                    }
                                                    var zoomLevel = map.getZoom();
                                                    if (zoomLevel < 9)
                                                        initTjLayer(allTjLayerContent, tjType, "1");
                                                    else
                                                        initTjLayer(allTjLayerContent, tjType, "2");
                                                    $("#legend-container .legend").remove();
                                                    layer.close(index);
                                                    layer.close(layerIndex);
                                                }
                                            }
                                        });
                                    });
                                },
                            });
                        });
                    }
                });
                if(treeNode.getParentNode().id!=1){
                    var btn1 = $("#doMapRemove_"+treeNode.id);
                    if (btn1) btn1.bind("click", function(){
                        //删除，根据父节点不同，功能不同
                        //删除专题服务图层
                        if(!confirm("确认删除 节点 -- " + treeNode.name + " 吗？")){
                            return;
                        }
                        //删除专题服务图层
                        if(treeNode.getParentNode().id==2){//如果是专题服务
                            var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                            treeObj.removeNode(treeNode,true);
                            treeNode.getParentNode().isParent=true;
                            treeObj.refresh();
                            if(map&&(map.getLayer(treeNode.url))){
                                var thisLayer = map.getLayer(treeNode.url);
                                map.removeLayer(thisLayer);
                            }
                        }
                        //删除要素服务图层
                        if(treeNode.getParentNode().id==3){//如果是要素服务
                            var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                            treeObj.removeNode(treeNode,true);
                            treeNode.getParentNode().isParent=true;
                            treeObj.refresh();
                            //删除节点时将地图上的图层也删去
                            if (treeNode.textLayerChecked == "checked"){
                                if(map&&(map.getLayer(treeNode.url))){
                                    var thisLayer = map.getLayer(treeNode.url);
                                    map.removeLayer(thisLayer);
                                }
                            }
                            else if (treeNode.buttonLayerChecked == "checked"){
                                var layerNow2 = map.getLayer(treeNode["nodePath"][treeNode["nodePath"].length-1].id);
                                if(layerNow2){
                                    map.removeLayer(layerNow2);
                                }
                            }

                        }
                        //删除统计服务图层
                        if(treeNode.getParentNode().id==4){//如果是统计服务
                            var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                            treeObj.removeNode(treeNode,true);
                            treeNode.getParentNode().isParent=true;
                            treeObj.refresh();
                            //删除节点时将地图上的图层也删去
                            if(map&&(map.getLayer(treeNode.url))) {
                                var thisLayer = map.getLayer(treeNode.url);
                                var thisTjLayerType = thisLayer.name;
                                map.removeLayer(thisLayer);

                                switch (thisTjLayerType) {
                                    case "chartGLayer":
                                        var chartFlag = 0;
                                        for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                                            if (map.getLayer(map.graphicsLayerIds[i]).name == "chartGLayer") {
                                                chartFlag = 1;
                                                break;
                                            }
                                        }
                                        if (chartFlag == 0)
                                            indi = [];
                                        $("#legend-container .legend").remove();
                                        break;
                                    case "classGLayer":
                                        var classFlag = 0;
                                        for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                                            if (map.getLayer(map.graphicsLayerIds[i]).name == "classGLayer") {
                                                classFlag = 1;
                                                break;
                                            }
                                        }
                                        if (classFlag == 0)
                                            field_cn = "";
                                        $("#legend-container .legend").remove();
                                        break;
                                }
                                //删除节点时，调整组合图例的内容
                                editFlag = 1;
                                switch (thisTjLayerType){
                                    case "chartGLayer":
                                        var cLN = 0;
                                        for (var i=0; i<map.graphicsLayerIds.length; i++) {
                                            if (map.getLayer(map.graphicsLayerIds[i]).name == "chartGLayer") {
                                                var thisLayer = map.getLayer(map.graphicsLayerIds[i]);
                                                cLN++;
                                                var zoomLevel = map.getZoom();
                                                if (zoomLevel < 9)
                                                    changeLayerOnZoom(thisLayer, "chartLayerData", "1", cLN);
                                                else
                                                    changeLayerOnZoom(thisLayer, "chartLayerData", "2", cLN);
                                            }
                                        }
                                        break;
                                    case "classGLayer":
                                        for (var i=0; i<map.graphicsLayerIds.length; i++) {
                                            if (map.getLayer(map.graphicsLayerIds[i]).name == "classGLayer"){
                                                var thisLayer = map.getLayer(map.graphicsLayerIds[i]);
                                                var zoomLevel = map.getZoom();
                                                if (zoomLevel < 9)
                                                    changeLayerOnZoom(thisLayer, "classLayerData", "1", 1);
                                                else
                                                    changeLayerOnZoom(thisLayer, "classLayerData", "2", 1);
                                            }
                                        }
                                        break;
                                }
                                editFlag = 0;
                            }
                        }
                    });
                }
            }
        };

        function removeHoverDom(treeId, treeNode) {
            if(treeNode.isParent){
                $("#doMapAdd_" +treeNode.id).unbind().remove();
            }
            else{
                $("#doMapEdit_" +treeNode.id).unbind().remove();
                $("#doMapRemove_" +treeNode.id).unbind().remove();
            }

        };

        function beforeRename(treeId, treeNode, newName, isCancel) {
            //className = (className === "dark" ? "":"dark");
            //showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
            if (newName.length == 0) {
                setTimeout(function() {
                    var zTree = $.fn.zTree.getZTreeObj("doMapTree");
                    zTree.cancelEditName();
                    alert("节点名称不能为空.");
                }, 0);
                return false;
            }
            return true;
        }

        function beforeRemove(treeId, treeNode) {
            //className = (className === "dark" ? "":"dark");
            //showLog("[ "+getTime()+" beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
            /*var zTree = $.fn.zTree.getZTreeObj("doMapTree");
            zTree.selectNode(treeNode);
            return confirm("确认删除 节点 -- " + treeNode.name + " 吗？");*/
        }

        if(doMapIndex==0){
            layerNodesObj=$.fn.zTree.init($("#doMapTree"), setting, layerNodes_InFunc);

            doMapIndex=1;
        }
        layerNodesObj.refresh();
    }
    layui.use('layer', function (layui_index) {
        var layer = layui.layer;
        layer.open({
            title: '交互制图',
            skin: "layui-layer-lan",
            type: 1,
            maxmin:true,
            closeBtn:0,
            shade: 0,
            resize: true,
            maxmin:true,
            area: ['250', '400px'],
            // btn: ['编辑图层'],
            closeBtn:0,
            content:
                $('#complexLayer'),
            yes: function(index, layero) {//确定后执行回调

            }});
    });
    layui.use('element', function(){
        var element = layui.element;
        //切换Tab到图层编辑时刷新
        element.on('tab(test1)', function(data){
            console.log(data);
            if (data.index == 1){
                layerEdit();
            }
        });
    });
}

//打开专题目录
function openTreeWindow() {
    $.ajaxSetup({async:false});
    $.getJSON("http://qk.casm.ac.cn:9090/ythjzweb/tucengbygl/getleveljson.it?pid=858",function(data) {
        for (var i=0; i<data.length; i++){
            data[i].name = data[i]["title"];
            data[i].isParent = data[i]["isFolder"];
            if (data[i].isFolder){
                data[i]["nocheck"] = true;
            }
        }
        // console.log(data);

        var zNodes = data;
        var setting1 = {
            check:{
                chkStyle: "radio",
                enable: true,
                radioType: "all"
            },
            callback:{
                beforeExpand: buildChildren,
                beforeCheck: function (treeId, treeNode) {
                    nodePath = treeNode.getPath();
                    nodeTheme = treeNode.name;
                    selectedNode = treeNode;
                    // console.log(nodePath);
                }
                // beforeThematicLayerAdd
            }
        };

        featureLayerTree = $.fn.zTree.init($("#treeContent"), setting1, zNodes);
        // $('#treeContent').append("<button>确定</button>");


    });
    layui.use('layer', function () {
        var layer1 = layui.layer;
        layer1.open({
            title: '专题图层',
            skin: "layui-layer-lan",
            type: 1,
            btn: ['确定'],
            content: $('#treeContent'),
            success: function (layero, index) {
                //窗口加载初始状态，避免不改动时出错
                selectedNode = null;
            },
            yes: function(index, layero) {//确定后执行回调
                // console.log(selectedNode);
                // console.log(nodePath);
                if (selectedNode != null)
                    document.getElementById("selectButton").innerHTML = selectedNode.name;
                layer1.close(index);

            }
        });
    });
}

//构造目录树子节点
function buildChildren(treeId, treeNode) {
    $.getJSON("http://qk.casm.ac.cn:9090/ythjzweb/tucengbygl/getleveljson.it?pid=" + treeNode["pid"], function (data2) {
        // console.log(treeNode);

        for (var j=0; j<data2.length; j++){
            data2[j].name = data2[j]["title"];
            data2[j].isParent = data2[j]["isFolder"];
            if (data2[j].isFolder){
                data2[j]["nocheck"] = true;
            }
        }
        // console.log(data2);
        treeNode["children"] = data2;
        var treeObj = $.fn.zTree.getZTreeObj("treeContent");

        treeObj.updateNode(treeNode);
        treeObj.refresh();
    });
}

//打开指定要素的树
function openSelectedTree(thisNodePath){
    $.ajaxSetup({async:false});
    // console.log(featureLayerTree);
    if (thisNodePath == undefined){
        alert('模板要素数据不能另选数据，若要另外添加数据请添加图层！');
        return;
    }
    if (textEditFlag == 1){
        openTreeWindow();
        textEditFlag = 0;
        return;
    }
    $.getJSON("http://qk.casm.ac.cn:9090/ythjzweb/tucengbygl/getleveljson.it?pid=858",function(data) {
        for (var i=0; i<data.length; i++){
            data[i].name = data[i]["title"];
            data[i].isParent = data[i]["isFolder"];
            if (data[i].isFolder){
                data[i]["nocheck"] = true;
            }
            //根据路径展开节点
            if (data[i].name == thisNodePath[0].name){
                buildChildren("treeContent", data[i]);
                data[i]["open"] = true;
                for (var j=0; j<data[i]["children"].length; j++){
                    if (data[i]["children"][j].name == thisNodePath[1].name){
                        if (thisNodePath.length == 2)
                            data[i]["children"][j]["checked"] = true;
                        else {
                            buildChildren("treeContent", data[i]["children"][j]);
                            data[i]["children"][j]["open"] = true;
                            for (var k=0; k<data[i]["children"][j]["children"].length; k++){
                                if (data[i]["children"][j]["children"][k].name == thisNodePath[2].name)
                                    data[i]["children"][j]["children"][k]["checked"] = true;
                            }
                        }
                    }
                }
            }
        }
        // console.log(data);

        var zNodes1 = data;
        // for (var j=0; j<nodePath.length-1; j++){
        //     buildChildren("treeContent", nodePath[j]);
        // }
        // var treeObj = $.fn.zTree.getZTreeObj("treeContent");
        // console.log(treeObj);
        // nodePath[nodePath.length-1]["checked"] = true;
        // console.log(thisNodePath);

        var setting2 = {
            check:{
                chkStyle: "radio",
                enable: true,
                radioType: "all"
            },
            callback:{
                beforeExpand: buildChildren,
                beforeCheck: function (treeId, treeNode) {

                    if (treeNode != thisNodePath[thisNodePath.length-1]){
                        nodeTheme = treeNode.name;
                        nodePath = treeNode.getPath();
                        selectedNode = treeNode;
                        // console.log(nodePath);
                    }   //如果改变了选择
                    else {
                        nodeTheme = thisNodePath[thisNodePath.length-1].name;
                        nodePath = thisNodePath;

                    }
                }
                // beforeThematicLayerAdd
            }
        };

        featureLayerTree = $.fn.zTree.init($("#treeContent"), setting2, zNodes1);

    });
    layui.use('layer', function () {
        var layer1 = layui.layer;
        layer1.open({
            title: '专题图层',
            skin: "layui-layer-lan",
            type: 1,
            btn: ['确定'],
            content: $('#treeContent'),
            success: function (layero, index) {
                //窗口加载初始状态，避免不改动时出错
                selectedNode = thisNodePath[thisNodePath.length-1];
            },
            yes: function(index, layero) {//确定后执行回调
                // console.log(selectedNode);
                // console.log(treeNode);
                // console.log(thisNodePath[thisNodePath.length-1]);
                if (selectedNode == thisNodePath[thisNodePath.length-1]){
                    nodeTheme = thisNodePath[thisNodePath.length-1].name;
                    nodePath = thisNodePath;
                }
                document.getElementById("selectButton").innerHTML = selectedNode.name;
                layer1.close(index);
            }
        });
    });
}

//为每个制图模板添加配置面板的弹出框
//TODO:增加统计图层配置，图层排序
//选择其他模板的提示
function sweetAlert1(mapName) {
    if (alertFlag ==0){
        typeFlag = 0;
        layerCloseFlag = 1;
        templateFlag = 1;
        addModelLayUI(mapName);
        templateClassNo = layer.index;
    }

    else if (alertFlag ==1){
        layer.confirm('该操作会清除之前编辑的内容，是否继续？', {icon: 3, title:'提示'}, function(index){
            layerCloseFlag = 1;
            layer.close(layer.index);
            if (layerIndex){
                if (typeFlag == 1){
                    // for(var i=1; i<layerIndex+1; i++){
                     if (blankClassNo)
                        $("#layui-layer"+ blankClassNo).css("display", "none");
                    // }
                    // layer.close(layerIndex);
                }
            }
            //移除非标绘的图层
            // var otherLayerIDs = (map.graphicsLayerIds).slice();
            // for (var i=0; i<otherLayerIDs.length;i++){
            //     if(otherLayerIDs[i]==='pointPlotLayer'||otherLayerIDs[i]==='linePlotLayer'||otherLayerIDs[i]==='polygonPlotLayer'){
            //         otherLayerIDs.splice(i,1)
            //     }
            // }
            // for (var i=0; i<otherLayerIDs.length;i++){
            //     var layerRemoved = map.getLayer(otherLayerIDs[i])
            // }            //     map.removeLayer(layerRemoved);
           //清除标绘图层graphics
            polygonFeatureLayer.clear();
            polylineFeatureLayer.clear();
            pointFeatureLayer.clear();
            //删除本地和缓存的标绘图形
            deleteAllFeatureFromLocalstorage();
            //移除所有图层
            map.removeAllLayers();
            //将清将清空graphics的图层加上，因为后面还要标绘。

            map.addLayer(polygonFeatureLayer);
            map.addLayer(polylineFeatureLayer);
            map.addLayer(pointFeatureLayer);
           /*  $.each(baseMap,function (i) {
                map.addLayer(baseMap[i]);
            }); */
            require(["esri/layers/WebTiledLayer","esri/layers/ArcGISDynamicMapServiceLayer","esri/layers/ArcGISTiledMapServiceLayer"],function (WebTiledLayer,ArcGISDynamicMapServiceLayer,ArcGISTiledMapServiceLayer) {
              
               baseMap=new Array();
               var baseMapUrls=layerNodes[0].children[0].url.split(",");
               var baseMapType=layerNodes[0].children[0].mapType;
                $.each(baseMapUrls, function (i) {
                    var url=baseMapUrls[i];
                    switch (baseMapType){
                        case "WebTiledLayer":
                            baseMap.push(new WebTiledLayer(url,{"id":url
            }));
            break;
                        case "ArcGISDynamicMapServiceLayer":
                            baseMap.push(new ArcGISDynamicMapServiceLayer(url,{"id":url}));
                            break;
                        case "ArcGISTiledMapServiceLayer":
                            baseMap.push(new ArcGISTiledMapServiceLayer(url,{"id":url}));
                            break;
    
                    }
                    map.addLayer(baseMap[i]);
    
                });
                map.addLayer(zjBaseLayer);
            });
            $("#legend-container .legend").remove();
            indi = [];
            field_cn = "";
            addModelLayUI(mapName);
            if (templateFlag == 0)
                templateClassNo = layer.index;
            templateFlag = 1;
            $("#layui-layer"+ (templateClassNo)).css("display", "block");
            // layerIndex = layer.index;
            typeFlag = 0;
            $("#layui-layer"+ (templateClassNo) +" .layui-layer-title").text(mapName);
        });

    }
}

//为每个制图模板添加弹出框
function addModelLayUI(mapName) {
    var userMapName;
    //根据本地存储获取模板
    (function getTemplate() {
        var disaster_status = localStorage.getItem("disaster_status");
        var disaster_type = localStorage.getItem("disaster_type");
        var template_scale = localStorage.getItem("template_scale");
        var template_theme = localStorage.getItem("template_theme");
        var template_map = localStorage.getItem("template_map");
        // $("#mapNameInfo").html(template_map);
        var url = "";
        if (mappingPageType == 0)
            url = "./servlet/GetTemplateLayer?disasterStatus="+disaster_status+"&disasterType="+disaster_type+"&templateScale="+template_scale+"&templateTheme="+template_theme+"&templateMap="+template_map+"&queryType=queryLayer&mapId=null";
        else
            url = "./servlet/GetTemplateLayer?disasterStatus=null&disasterType=null&templateScale=null&templateTheme=null&templateMap=null&mapId=" + mapName + "&queryType=userMap";
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
                if (mappingPageType == 0){
                    if ((typeof data[0]["SIX_LZJTU_LAYER"])==="string"&&!(data[0]["SIX_LZJTU_LAYER"]==="")&&!(data[0]["SIX_LZJTU_LAYER"].toLowerCase()==="null")) {
                        //要考虑到字符串string为空的情况
                        try{
                            str=eval("(" + data[0]["SIX_LZJTU_LAYER"] + ")");
                            jsonStr=str[0];
                        }
                        catch(e){
                            alert("数据库中模板格式错误");
                            // console.log(e);
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
                            // console.log(jsonStr)
                            alert("数据库中模板为空或格式错误，使用缺省模板")
                        }
                        // console.log(template);
                        return;
                    }catch (e) {
                        alert("数据库中模板格式错误");
                        // console.log(e);
                    }
                }
                else {
                    userMapName = data[0]["userMapName"];
                    if ((typeof data[0]["userMapParam"])==="string"&&!(data[0]["userMapParam"]==="")&&!(data[0]["userMapParam"].toLowerCase()==="null")) {
                        //要考虑到字符串string为空的情况
                        try{
                            str=eval("(" + data[0]["userMapParam"] + ")");
                            jsonStr=str;
                        }
                        catch(e){
                            alert("数据库中模板格式错误");
                            // console.log(e);
                        }
                    }else if ((typeof data[0]["userMapParam"])==="object"&&!(data[0]["userMapParam"]===null)){
                        //要考虑到object为空的情况
                        str=data[0]["userMapParam"];
                        jsonStr=str;
                    }
                    try{
                        //if(jsonStr&&jsonStr.featureLayer&&jsonStr.featureLayer.modules&&jsonStr.featureLayer.modules.length&&jsonStr.featureLayer.modules[0].name)
                        if(jsonStr)
                        {
                            template=jsonStr;
                        }else{
                            // console.log(jsonStr)
                            alert("数据库中模板为空或格式错误，使用缺省模板")
                        }
                        // console.log(template);
                        return;
                    }catch (e) {
                        alert("数据库中模板格式错误");
                        // console.log(e);
                    }
                }
            },
            error: function (xhr, status, errMsg) {
                alert('error');
                // console.log(errMsg);
            }

        });
    })();
    //利用template生成树节点
    //生成树
    //编辑节点
    var baseLayer_Model=template.baseLayer
    var featureLayer_Model=template.featureLayer
    var serviceLayer_Model=template.serviceLayer
    var statisticLayer_Model= template.statisticLayer
    //修改底图节点_暂时不用

    if (mappingPageType == 0){
        var layerNodes_Model=[
            {id:1, pId:0, name:"地理底图", open:true, "nocheck":true,children:[
                // {id:102, name:"矢量图",url:"http://t0.tianditu.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=${col}&TILEROW=${row}&TILEMATRIX=${level},http://t2.tianditu.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=${col}&TILEROW=${row}&TILEMATRIX=${level}",mapType:"WebTiledLayer",checked:true},
                // {id:102, name:"影像图",url:"http://t6.tianditu.gov.cn/DataServer?T=img_w&x=${col}&y=${row}&l=${level},http://t2.tianditu.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=${col}&TILEROW=${row}&TILEMATRIX=${level}",mapType:"WebTiledLayer"},
                {id:101, name:"矢量图",url:"http://t0.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=${col}&TILEROW=${row}&TILEMATRIX=${level}&tk=cede75bb109d5e8048ebc21308b91e54",mapType:"WebTiledLayer",checked:true},
                    {id:102, name:"影像图",url:"http://t0.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={level}&TILEROW={row}&TILECOL={col}&tk=cede75bb109d5e8048ebc21308b91e54",mapType:"WebTiledLayer"},
                    // {id:101, name:"矢量图",url:"http://t0.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=${col}&TILEROW=${row}&TILEMATRIX=${level}&tk=cede75bb109d5e8048ebc21308b91e54,http://t0.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=${col}&TILEROW=${row}&TILEMATRIX=${level}&tk=cede75bb109d5e8048ebc21308b91e54",mapType:"WebTiledLayer",checked:true},
                    //
                    // {id:102, name:"影像图",url:"http://t0.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={level}&TILEROW={row}&TILECOL={col}&tk=cede75bb109d5e8048ebc21308b91e54,http://t0.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={level}&TILEROW={row}&TILECOL={col}&tk=cede75bb109d5e8048ebc21308b91e54",mapType:"WebTiledLayer"},

                    /*{id:101,name:"新疆矢量图",url:"http://106.124.138.198:8080/OneMapServer/rest/services/XJ_VECTOR/MapServer,http://106.124.138.198:8080/OneMapServer/rest/services/XJ_POI/MapServer",checked:true}*/
                // {id:101, name:"专项矢量图",url:"http://qk.casm.ac.cn:9090/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=map&y=${row}&x=${col}&z=${level}",mapType:"WebTiledLayer"},
                //{id:102, name:"专项影像图",url:"http://qk.casm.ac.cn:9090/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=sate&y=${row}&x=${col}&z=${level}",mapType:"WebTiledLayer"}
            ]},

            {id:2, pId:0, name:"专题服务图层",isParent:true, open:true,children:[], "nocheck":true},

            {id:3, pId:0, name:"要素图层", isParent:true,open:true,children:[], "nocheck":true},
            {id:4, pId:0, name:"统计图层", isParent:true,open:true,children:[], "nocheck":true}

        ];

        /*增加专题服务图层节点*/
        layerNodes_Model[1].children=serviceLayer_Model.modules;
        //增加要素图层
        layerNodes_Model[2].children=featureLayer_Model.modules;
        //增加统计图层
        layerNodes_Model[3].children=statisticLayer_Model.modules;

        //添加模板数据标识属性 templateData 或 urlFeatureData
        for (var i=0,l=layerNodes_Model[2].children.length;i<l;i++){
            layerNodes_Model[2].children[i].dataType="templateData"
        }
        //为模板中的统计图层初始化
        for (var i=0,l=layerNodes_Model[3].children.length;i<l;i++){ //记录作为graphicsLayerId的节点原始名称
            layerNodes_Model[3].children[i].url = layerNodes_Model[3].children[i].name;
            layerNodes_Model[3].children[i].checked = true;
        }
        for(var i=0;i<statisticLayer_Model.modules.length;i++){
            if (statisticLayer_Model.modules[i].cartographydata.type == "2") {
                // layerNodes_Model[3].children[i].checked = true;
                tjLayerName=statisticLayer_Model.modules[i]["name"];
                var tjType = "classLayerData";
                fieldsOrIndi = statisticLayer_Model.modules[i].statisticdata.fieldsName;
                var str=JSON.stringify(statisticLayer_Model.modules[i])
                var zoomLevel = map.getZoom();
                if (zoomLevel < 9)
                    initTjLayer(str, tjType, "1");
                else
                    initTjLayer(str, tjType, "2");
                break;
            }
        }
        for(var i=0;i<statisticLayer_Model.modules.length;i++){
            if (statisticLayer_Model.modules[i].cartographydata.type == "1") {
                // layerNodes_Model[3].children[i].checked = true;
                tjLayerName=statisticLayer_Model.modules[i]["name"];
                var tjType = "chartLayerData";
                fieldsOrIndi = statisticLayer_Model.modules[i].statisticdata.fieldsName;
                var str=JSON.stringify(statisticLayer_Model.modules[i])
                var zoomLevel = map.getZoom();
                if (zoomLevel < 9)
                    initTjLayer(str, tjType, "1");
                else
                    initTjLayer(str, tjType, "2");
                break;
            }
        }
    }


    else if (mappingPageType == 1 || mappingPageType == 2)
        layerNodes_Model = template;

    doMapping_Template(layerNodes_Model)
    function doMapping_Template(layerNodes_Model) {
        var setting = {
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            edit: {
                enable: true,
                drag: {
                    isCopy: false,
                    isMove: false,
                }, //节点不能拖拽
                showRenameBtn: false,
                showRemoveBtn: false
            },
            view:{
                addHoverDom: addHoverDom,
                removeHoverDom: removeHoverDom
            },
            callback: {
                beforeCheck: function (treeId, treeNode) {
                    layerOncheck("doMapTree_Template",treeNode);
                },
                beforeRemove: beforeRemove,
                beforeRename: beforeRename
            }
        };
        function addHoverDom(treeId, treeNode) {
            if (mappingPageType == 2)
                return;
            var aObj = $("#" + treeNode.tId + "_a");
            //首先判断是否是父节点
            if(treeNode.isParent){
                //如果是底图，没有增加按钮
                if(treeNode.id==1){
                    return;
                }
                if ($("#doMapAdd_"+treeNode.id).length>0) return;
                var editStr = "<span id='doMapAdd_"+treeNode.id+"' class='button doMapAdd'  onfocus='this.blur();'></span>";
                aObj.append(editStr);
                var btn = $("#doMapAdd_"+treeNode.id);
                //绑定添加子节点事件
                if (btn) btn.bind("click", function(){
                    if(treeNode.id==2){//针对专题服务图层的添加事件
                        layui.use('layer', function (layui_index) {
                            var layer = layui.layer;
                            layer.open({
                                title: '添加专题服务',
                                skin: "layui-layer-lan",
                                type: 0,
                                shade: 0,
                                content:"<div><p>服务名称：<input id='newSLName'></input></p><br/><p>服务地址：<input id='newSLAds'></input></p></div>",
                                yes: function(index, layero) {//确定后执行回调
                                    if($("#newSLName").val()==""||$("#newSLAds").val()==""){
                                        alert("属性不能为空！");
                                        return;
                                    }
                                    var newnode={name:$("#newSLName").val(),url:$("#newSLAds").val()};
                                    layerNodes[1].children.push(newnode);
                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree_Template");
                                    treeObj.addNodes(treeNode,-1, newnode);

                                    require([
                                        "esri/layers/ArcGISDynamicMapServiceLayer",
                                        "esri/InfoTemplate", "esri/dijit/PopupTemplate"
                                    ], function (ArcGISDynamicMapServiceLayer, InfoTemplate, PopupTemplate) {
                                        var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                                        //判断专题服务是图层还是地图
                                        var serviceUrl = $("#newSLAds").val();
                                        var serviceUrlstr=serviceUrl;
                                        if(serviceUrl.substr(-9)!="MapServer"){
                                            serviceUrlstr=serviceUrl.substring(0,serviceUrl.lastIndexOf("/"));
                                        }
                                        try{
                                            var layer = new ArcGISDynamicMapServiceLayer(serviceUrlstr,{id:$("#newSLName").val()+"_"+$("#newSLAds").val()});
                                        }catch (e) {
                                            alert("服务地址有误！")
                                            return false;
                                        }
                                        //如果是图层，则只显示这个图层
                                        if(serviceUrl.substr(-9)!="MapServer"){
                                            var showindex=serviceUrl.substring(serviceUrl.lastIndexOf("/")+1,serviceUrl.length);
                                            layer.setVisibleLayers([showindex]);
                                        }

                                        //var layer = new ArcGISDynamicMapServiceLayer(serviceUrl,{id: $("#newSLName").val()+"_"+$("#newSLAds").val()});
                                        ServerLayerArr.push(layer);
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
                                                layui.use('layer', function () {
                                                    var lay = layui.layer;
                                                    lay.confirm('加载成功，缩放到该图层?', {
                                                        icon: 3,
                                                        title: '提示'
                                                    }, function (layui_index) {
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
                                                                        // console.log(p);
                                                                        map.setExtent(p[0]);
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
                                                            // console.log(e);
                                                        }
                                                        lay.close(layui_index);
                                                    })
                                                })
                                            }
                                        },this)
                                    });

                                    layer.close(index);
                                }});
                        });
                    }
                    if(treeNode.id==3){//针对要素图层的添加事件
                        layui.use('layer', function (layui_index) {
                            var layer = layui.layer;
                            layer.open({
                                title: '添加要素服务',
                                skin: "layui-layer-lan",
                                type: 0,
                                shade: 0,
                                // content:"<div><p>要素名称：<input id='newFLName'></input></p><br/><p>要素地址：<input id='newFLAds'></input></p></div>",
                                content: "<div id='zeo'><p style='padding-left: 12px'>要素名称：<input id='newFLName'></input></p><br/><p class='FLS_p'><input id='textLayer' name='layer' value='text' type='radio' onclick='changeSource(this)'/>服务地址：<input id='newFLAds' disabled></input></p>"

                                + "<br/><p class='tree_p' style='display: none'><input id='buttonLayer' name='layer' value='button' type='radio' onclick='changeSource(this)' />专题服务：<button id='selectButton' onclick='openTreeWindow()' class='layui-btn layui-btn-sm layui-btn-disabled' disabled>选择要素</button></p></div>",

                                yes: function(index, layero) {//确定后执行回调

                                    var textLayerChecked = $("#textLayer").is(":checked")?"checked":"unchecked";
                                    var buttonLayerChecked = $("#buttonLayer").is(":checked")?"checked":"unchecked";
                                    var textLayerDisabled = $("#newFLAds").prop("disabled")==true?"disabled":"undisabled";
                                    var buttonLayerDisabled = $("#selectButton").prop("disabled")==true?"disabled":"undisabled";
                                    if (textLayerChecked == "checked" && buttonLayerChecked == "unchecked"){
                                        if($("#newFLName").val()==""||$("#newFLAds").val()=="") {
                                            alert("属性不能为空1！");
                                            return;
                                        }
                                    }
                                    else if (textLayerChecked == "unchecked" && buttonLayerChecked == "checked"){
                                        // document.getElementById("selectButton").innerHTML="New text!";
                                        // alert($("#selectButton").html());
                                        if($("#newFLName").val()==""||$("#selectButton").html()=="选择要素") {
                                            alert("属性不能为空2！");
                                            return;
                                        }
                                    }
                                    else{
                                        alert("属性不能为空3！");
                                        return;
                                    }
                                    if (textLayerChecked == "checked"){
                                        nodePath = [];
                                        nodeTheme = null;
                                    }

                                    var newNode={
                                        name:$("#newFLName").val(),
                                        url:$("#newFLAds").val(),
                                        dataType:"urlFeatureData",
                                        textLayerChecked:textLayerChecked,
                                        buttonLayerChecked:buttonLayerChecked,
                                        textLayerDisabled:textLayerDisabled,
                                        buttonLayerDisabled:buttonLayerDisabled,
                                        lastUrl:"0",
                                        nodePath:nodePath,
                                        theme:nodeTheme};
                                    layerNodes[2].children.push(newNode);
                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree_Template");
                                    treeObj.addNodes(treeNode,-1, newNode);
                                    // console.log(newNode["nodePath"]);
                                    layer.close(index);
                                }});
                        });
                    }
                    if(treeNode.id==4){//针对统计图层的添加事件
                        layui.use('layer', function() {
                            var layer = layui.layer;
                            layerIndex = layer.open({
                                type: 1,
                                title: ['添加统计图层'],
                                shadeClose: false,
                                skin: "layui-layer-lan tjLayerContent",
                                shade: 0,
                                area: ['700px', '480px'],
                                // content: $('#tjPanel'),
                                content:originalTjLayerContent,
                                success: function(layero,index){
                                    //do something
                                    opentjMenuLayer();

                                    // console.log(layero.find(".layui-layer-content").html());
                                    // var newNode={name:$("#newFLName").val(),url:$("#newFLAds").val()};

                                    $(".tjInfoSubmit").bind('click',function () {
                                        var tjLayertest=layero.find(".layui-layer-content").html();
                                        constructTjJson3();
                                        var index=layer.open({
                                            type: 0,
                                            title:"统计图层名称",
                                            skin:"layui-layer-lan",
                                            content:' <div style="margin-left:-24px">\n' +
                                            '             <label class="layui-form-label">图层名</label>\n' +
                                            '             <div class="layui-input-block" style="margin-left: 88px">\n' +
                                            '                  <input type="text" id="newSLName" name="tjLayerName" lay-verify="required" placeholder="请输入统计图层名称" autocomplete="off" class="layui-input">\n' +
                                            '             </div>\n' +
                                            '          </div>',
                                            yes:function (index,layero) {
                                                console.log("OK");

                                                tjLayerName = $("input[ name='tjLayerName' ]").val();

                                                //统计图层所有参数
                                                allTjLayerContent = {
                                                    "name": tjLayerName,
                                                    "spatialdata": tjPanel1,
                                                    "statisticdata": tjPanel2,
                                                    "cartographydata": tjPanel3
                                                }

                                                if (tjLayerName == "") {
                                                    layer.tips('请输入图层名称', '#newSLName')
                                                    // layer.alert('');
                                                }

                                                if (tjLayerName != "") {
                                                    var newNode = {
                                                        name: tjLayerName,
                                                        url: tjLayerName, //记录作为graphicsLayerId的节点原始名称
                                                        // allContent: allTjLayerContent,
                                                        spatialdata:tjPanel1,
                                                        statisticdata:tjPanel2,
                                                        cartographydata:tjPanel3,
                                                        checked: true
                                                    };
                                                    layerNodes[3].children.push(newNode);
                                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree_Template");
                                                    treeObj.addNodes(treeNode, -1, newNode);

                                                    switch (allTjLayerContent.cartographydata.type) {
                                                        case "1":
                                                            tjType = "chartLayerData";
                                                            break;
                                                        case "2":
                                                            tjType = "classLayerData";
                                                            break;
                                                    }
                                                    fieldsOrIndi = allTjLayerContent.statisticdata.fieldsName;
                                                    allTjLayerContent = JSON.stringify(allTjLayerContent);
                                                    // console.log(allTjLayerContent);
                                                    var zoomLevel = map.getZoom();
                                                    if (zoomLevel < 9)
                                                        initTjLayer(allTjLayerContent, tjType, "1");
                                                    else
                                                        initTjLayer(allTjLayerContent, tjType, "2");
                                                    $("#legend-container .legend").remove();
                                                    layer.close(index);
                                                    layer.close(layerIndex);
                                                }
                                            }
                                        });
                                    });
                                },
                            });
                        });
                    }
                    //alert("添加" + treeNode.name);
                    //var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                    //var newNode = {name:"newNode1"};
                    // treeObj.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
                });

            }
            else{
                var editStr = "<span id='doMapEdit_"+treeNode.id+"' class='button doMapEdit'  onfocus='this.blur();'></span>"+
                    "<span id='doMapRemove_"+treeNode.id+"' class='button doMapRemove'  onfocus='this.blur();'></span>";
                //如果是地理底图，不显示删除
                if(treeNode.getParentNode().id==1){
                    editStr = "<span id='doMapEdit_"+treeNode.id+"' class='button doMapEdit'  onfocus='this.blur();'></span>";
                }
                if ($("#doMapEdit_"+treeNode.id).length>0) return;
                aObj.append(editStr);
                var btn = $("#doMapEdit_"+treeNode.id);
                if (btn) btn.bind("click", function(){
                    //编辑，根据父节点不同，功能不同
                    if(treeNode.getParentNode().id==1){//如果是专题服务
                        layui.use('layer', function (layui_index) {
                            var layer = layui.layer;
                            layer.open({
                                title: '编辑底图服务',
                                skin: "layui-layer-lan",
                                type: 0,
                                shade: 0,

                                content:"<div><p>服务名称：<input id='editBLName' disabled value='"+treeNode.name+"'></input></p><br/><p>服务地址：<input id='editBLAds' value='"+treeNode.url+"'></input></p></div>",
                                yes: function(index, layero) {//确定后执行回调
                                    //  var editnode={name:$("#newSLName").val(),url:$("#newSLAds").val()};
                                    if($("#editBLName").val()==""||$("#editBLAds").val()==""){
                                        alert("属性不能为空！");
                                        return;
                                    }
                                    //treeNode.name=$("#editBLAds").val();
                                    $.each(baseMap,function (i) {
                                        if(treeNode.url.indexOf(baseMap[i].url)!=-1){
                                            map.removeLayer(baseMap[i]);
                                        }
                
                                    });
                                    treeNode.url=$("#editBLAds").val();
                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree_Template");
                                    treeObj.updateNode(treeNode);
                                    layerNodes.filter(function (p) {
                                        if(p.name==treeNode.name){
                                            p.url=treeNode.url;
                                        }
                                    });
                                    /*baseMap.url=$("#editBLAds").val();
                                    baseMap._url.path=$("#editBLAds").val();
                                    if($.inArray("baseMap",map.layerIds)!=-1){
                                        map.getLayer("baseMap").refresh();
                                    }*/
                                    require(["esri/layers/ArcGISTiledMapServiceLayer"],function (ArcGISTiledMapServiceLayer) {
                                       /*  $.each(baseMap,function (i) {
                                            map.removeLayer(baseMap[i]);
                                        }); */
                                        baseMap=new Array();
                                        var urls= $("#editBLAds").val().split(",")
                                        $.each(urls, function (i) {
                                            var url=urls[i];
                                            baseMap.push(new ArcGISTiledMapServiceLayer(url,{"id":url}));
                                            map.addLayer(baseMap[i]);

                                        });
                                        $.each(baseMap,function (i) {
                                            map.reorderLayer(baseMap[i],i);
                                        });
                                    });

                                    layer.close(index);
                                }});
                        });

                    }
                    if(treeNode.getParentNode().id==2){//如果是专题服务
                        layui.use('layer', function (layui_index) {
                            var layer = layui.layer;
                            layer.open({
                                title: '编辑专题服务',
                                skin: "layui-layer-lan",
                                type: 0,
                                shade: 0,
                                content:"<div><p>服务名称：<input id='editSLName' value='"+treeNode.name+"'></input></p><br/><p>服务地址：<input id='editSLAds' value='"+treeNode.url+"'></input></p></div>",
                                yes: function(index, layero) {//确定后执行回调
                                    //  var editnode={name:$("#newSLName").val(),url:$("#newSLAds").val()};
                                    if($("#editSLName").val()==""||$("#editSLAds").val()==""){
                                        alert("属性不能为空！");
                                        return;
                                    }
                                    if(map&&(map.getLayer(treeNode.url))){
                                        var thisLayer = map.getLayer(treeNode.url);
                                        map.removeLayer(thisLayer);
                                    }
                                    treeNode.name=$("#editSLName").val();
                                    treeNode.url=$("#editSLAds").val();
                                    treeNode.checked=false;
                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree_Template");
                                    treeObj.updateNode(treeNode);

                                    layer.close(index);
                                }});
                        });

                    }
                    if(treeNode.getParentNode().id==3)//如果是要素服务
                    {
                        var layer = layui.layer;
                        getThisPath = treeNode["nodePath"];
                        getThisTheme = treeNode["theme"];
                        var changeSource1; //比较编辑前后数据来源方式是否发生变化
                        var changeSource2;
                        //对服务地址图层编辑时的一种特殊情况
                        // console.log(treeNode);
                        // console.log(getThisTheme);
                        if (treeNode.textLayerChecked == "checked" && getThisTheme == undefined){
                            getThisTheme = "选择要素";
                            textEditFlag = 1;
                        }
                        // console.log(getThisPath);
                        //判断是否是示例图数据
                        if (treeNode.data==undefined){

                        }
                        else {
                            treeNode.textLayerChecked = "checked";
                            treeNode.textLayerDisabled="undisabled"
                            treeNode.buttonLayerChecked="unchecked";
                            treeNode.buttonLayerDisabled="disabled";

                        }

                        layui.use('layer', function (layui_index) {
                            layer.open({
                                title: '编辑要素服务',
                                skin: "layui-layer-molv",
                                type: 0,
                                shade: 0,
                                btn: ['确认','修改要素样式'],
                                //content:"<div><p>要素名称：<input id='editFLName' value='"+treeNode.name+"'></input></p><br/><p>要素地址：<input id='editFLAds' value='"+treeNode.url+"'></input></p></div>",
                                content: "<div id='zeo'>" +
                                "<p style='padding-left: 12px'>要素名称：<input id='editFLName' value='"+treeNode.name+"'onchange='nameChange()'></input></p>" +
                                "<br/><p class='FLS_p'><input id='textLayer' name='layer' value='text' type='radio' onclick='changeSource(this)' "+treeNode.textLayerChecked+"/>服务地址：<input id='editFLAds' "+treeNode.textLayerDisabled+" value='"+((treeNode.data)?treeNode.data:treeNode.url)+"'></input></p>"
                                + "<br/><p class='tree_p' style='display: none'><input id='buttonLayer' name='layer' value='button' type='radio' onclick='changeSource(this)' "+treeNode.buttonLayerChecked+"/>专题服务：<button id='selectButton' onclick='openSelectedTree(getThisPath)' class='layui-btn layui-btn-sm' "+treeNode.buttonLayerDisabled+">选择要素</button></p></div>",
                                success: function (layero, index) {
                                    if  (treeNode.buttonLayerDisabled=="undisabled")
                                        document.getElementById("selectButton").innerHTML = getThisTheme;
                                    if (treeNode.textLayerChecked == "checked")
                                        getThisTheme = undefined;
                                    changeSource1 = (treeNode.textLayerChecked == "checked")?"text":"button";
                                },
                                yes: function(index, layero) //确定后执行回调
                                {
                                    //  var editnode={name:$("#newSLName;").val(),url:$("#newSLAds").val()};
                                    // alert(selectedNode.name);

                                    //存储标签的加载方式
                                    var textLayerChecked = $("#textLayer").is(":checked")?"checked":"unchecked";
                                    var buttonLayerChecked = $("#buttonLayer").is(":checked")?"checked":"unchecked";
                                    var textLayerDisabled = $("#editFLAds").prop("disabled")==true?"disabled":"undisabled";
                                    var buttonLayerDisabled = $("#selectButton").prop("disabled")==true?"disabled":"undisabled";
                                    treeNode.textLayerChecked = textLayerChecked;
                                    treeNode.buttonLayerChecked = buttonLayerChecked;
                                    treeNode.textLayerDisabled = textLayerDisabled;
                                    treeNode.buttonLayerDisabled = buttonLayerDisabled;
                                    if ($("#selectButton").html()!= getThisTheme){  //判断专题图层是否做出了改变
                                        treeNode["nodePath"] = nodePath;
                                        treeNode["theme"] = nodeTheme;
                                    }

                                    // console.log(treeNode);

                                    var nodeIndex = treeNode.getIndex();
                                    if (layerNodes_Model[2].children[nodeIndex]) {
                                        layerNodes_Model[2].children[nodeIndex].lastUrl = treeNode.url;
                                        treeNode.lastUrl = treeNode.url;
                                        //记录数据源的开源方式
                                        layerNodes_Model[2].children[nodeIndex].textLayerChecked = textLayerChecked;
                                        layerNodes_Model[2].children[nodeIndex].buttonLayerChecked = buttonLayerChecked;
                                        layerNodes_Model[2].children[nodeIndex].textLayerDisabled = textLayerDisabled;
                                        layerNodes_Model[2].children[nodeIndex].buttonLayerDisabled = buttonLayerDisabled;
                                        layerNodes_Model[2].children[nodeIndex].name = $("#editFLName").val();
                                        layerNodes_Model[2].children[nodeIndex].url = $("#editFLAds").val();
                                    }
                                    treeNode.name = $("#editFLName").val();
                                    treeNode.url = $("#editFLAds").val();
                                    if (textLayerChecked == "checked" && buttonLayerChecked == "unchecked"){
                                        if($("#newFLName").val()==""||$("#newFLAds").val()=="") {
                                            alert("属性不能为空1！");
                                            return;
                                        }
                                    }
                                    else if (textLayerChecked == "unchecked" && buttonLayerChecked == "checked"){
                                        // document.getElementById("selectButton").innerHTML="New text!";
                                        // alert($("#selectButton").html());
                                        if($("#newFLName").val()==""||$("#selectButton").html()=="选择要素") {
                                            alert("属性不能为空2！");
                                            return;
                                        }
                                    }
                                    else{
                                        alert("属性不能为空3！");
                                        return;
                                    }
                                    // console.log(treeNode.checked);
                                    changeSource2 = (treeNode.textLayerChecked == "checked")?"text":"button";
                                    //如果数据来源方式发生了改变，则先移除原有图层(改变了数据源，先移除旧图层，再添加新图层)
                                    if (changeSource2 != changeSource1){
                                        if (changeSource1 == "text"){
                                            if (map.getLayer(treeNode.lastUrl))
                                                map.removeLayer(map.getLayer(treeNode.lastUrl));
                                        }
                                        else{
                                            if (treeNode.thematicData)
                                                map.removeLayer(map.getLayer(treeNode.thematicData.id));
                                        }

                                    }
                                    //如果在勾选状态下被编辑，点击确定后直接在地图上更新图层
                                    if ( treeNode.checked == true){
                                        treeNode.checked = false;
                                        layerOncheck("doMapTree_Template", treeNode);
                                        treeNode.checked = true;
                                    }
                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree_Template");
                                    treeObj.updateNode(treeNode);
                                    //layerNodes=treeObj.transformToArray(treeObj.getNodes());
                                    //如果名字发生了变化则更新图例
                                    var layer_ = map.getLayer(treeNode.url)
                                    if(nameChanged&&layer_){
                                        var render_ = layer_.renderer;
                                        render_.label = treeNode.name;
                                        layer_.setRenderer(render_);
                                        layer_.refresh();
                                        if(!(typeof iMLegend ==="undefined"||null===iMLegend)){
                                            iMLegend.refresh();
                                        }

                                    }
                                    nameChanged = false;
                                    layer.close(index);
                                },
                                btn2: function(index, layero)
                                {
                                    //按钮【修改要素样式】的回调
                                    //判断是否勾选了
                                    if(treeNode.textLayerChecked==='unchecked'&&treeNode.buttonLayerChecked==='unchecked'){
                                        alert("未设置数据源，不能编辑！");
                                        return false;
                                    }else if($("#selectButton").html()==="选择要素"&&!($("#editFLAds").val())){//没有选择也没有填地址
                                        alert("未设置数据源，不能编辑！");
                                        return false;
                                    }
                                    //判断图层是否发生了任意改变
                                    if((treeNode.theme&&$("#selectButton").html()!= treeNode.theme)||((changeSource1&&changeSource2)&&changeSource1!=changeSource2)||addressChanged){
                                        alert("图层数据源发生了改变，请先点击确定加载图层！");
                                        return false;
                                    }
                                    var layerID;
                                    if(treeNode.textLayerChecked==="checked"){
                                        if (mappingPageType == 0)
                                            layerID = treeNode.data;
                                        else
                                            layerID = treeNode.url;
                                    }else if (treeNode.buttonLayerChecked==="checked"){
                                        layerID = treeNode.thematicData.id;
                                    }
                                    //判断数据源是否加载到地图上，没有加载也不能编辑
                                    if(treeNode.textLayerChecked==="checked"){//没有选择也没有填地址
                                        if(!(map.getLayer(layerID))){
                                            alert("在当前地图中找不到文本框所代表的图层，请检查地址是否有误！");
                                            return false;
                                        }
                                    }
                                    /*方法说明
                                     *@method editFeatureLayer
                                     *@param{layer,layerType,layerLabel}
                                     *@return {void}
                                    */
                                    var layerInMap = map.getLayer(layerID);
                                    var layerLabel = treeNode.name;
                                    var layerType;
                                    switch (layerInMap.geometryType)  {
                                        case "esriGeometryPoint":
                                            layerType = 'point'
                                            break;
                                        case "esriGeometryPolyline":
                                            layerType = 'polyline'
                                            break;
                                        case "esriGeometryPolygon":
                                            layerType = 'polygon'
                                            break;
                                    }
                                    editFeatureLayer(layerInMap,layerType,layerLabel);
                                    //return false 开启该代码可禁止点击该按钮关闭
                                },
                            });
                        });


                    }
                    //编辑统计服务图层
                    if(treeNode.getParentNode().id==4){//如果是统计服务
                        layui.use('layer', function () {
                            var layer = layui.layer;
                            layerIndex = layer.open({
                                type: 1,
                                title: ['编辑统计服务'],
                                shadeClose: false,
                                skin: "layui-layer-lan tjLayerContent",
                                shade: 0,
                                area: ['700px', '480px'],
                                // content:layerNodes[3].children[0].dom,
                                content:originalTjLayerContent,
                                success: function(layero,index){
                                    // modifytjMenuLayer(treeNode.cartographydata);
                                    if (!treeNode.allContent)
                                        modifytjMenuLayer_new(treeNode);
                                    else
                                        modifytjMenuLayer_new(treeNode.allContent);

                                    // var newNode={name:$("#newFLName").val(),url:$("#newFLAds").val()};

                                    $(".tjInfoSubmit").bind('click',function () {
                                        var tjLayertest=layero.find(".layui-layer-content").html();
                                        // console.log(tjLayertest);
                                        constructTjJson3();
                                        // constructTjJson3_modify(treeNode.allContent);

                                        var index=layer.open({
                                            type: 0,
                                            title:"修改图层名称",
                                            skin:"layui-layer-lan",
                                            content:' <div style="margin-left:-24px">\n' +
                                            '             <label class="layui-form-label">图层名</label>\n' +
                                            '             <div class="layui-input-block" style="margin-left: 88px">\n' +
                                            '                  <input type="text" id="newSLName" name="tjLayerName1" lay-verify="required" placeholder="请输入统计图层名称" autocomplete="off" class="layui-input" value="'+treeNode.name+'">\n' +
                                            '             </div>\n' +
                                            '          </div>',
                                            yes:function (index,layero) {
                                                console.log("OK2");
                                                tjLayerName = $("input[ name='tjLayerName1' ]").val();
                                                var oldTjName = treeNode.name;
                                                treeNodeUrl = treeNode.url;

                                                allTjLayerContent = {
                                                    "name": tjLayerName,
                                                    "spatialdata": tjPanel1,
                                                    "statisticdata": tjPanel2,
                                                    "cartographydata": tjPanel3
                                                }

                                                if (tjLayerName == "") {
                                                    layer.tips('请输入图层名称', '#newSLName')
                                                    // layer.alert('');
                                                }

                                                if (tjLayerName != "") {
                                                    treeNode.name = tjLayerName;
                                                    treeNode.checked = true;
                                                    // treeNode.dom = tjLayertest;
                                                    // treeNode.cartographydata = tjPanel3;
                                                    treeNode.statisticdata=tjPanel2;
                                                    treeNode.cartographydata=tjPanel3;

                                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree_Template");
                                                    treeObj.updateNode(treeNode);

                                                    switch (allTjLayerContent.cartographydata.type) {
                                                        case "1":
                                                            tjType = "chartLayerData";
                                                            break;
                                                        case "2":
                                                            tjType = "classLayerData";
                                                            break;
                                                    }
                                                    fieldsOrIndi = allTjLayerContent.statisticdata.fieldsName;
                                                    allTjLayerContent = JSON.stringify(allTjLayerContent);
                                                    // console.log(allTjLayerContent);
                                                    if (tjLayerName != oldTjName)
                                                        tjLayerName = oldTjName;
                                                    var zoomLevel = map.getZoom();
                                                    if (zoomLevel < 9)
                                                        initTjLayer(allTjLayerContent, tjType, "1");
                                                    else
                                                        initTjLayer(allTjLayerContent, tjType, "2");
                                                    $("#legend-container .legend").remove();
                                                    layer.close(index);
                                                    layer.close(layerIndex);
                                                }
                                            }
                                        });
                                    });
                                },
                            });
                        });
                    }
                    /*var zTree = $.fn.zTree.getZTreeObj("doMapTree");
                    zTree.selectNode(treeNode);
                    zTree.editName(treeNode);*/
                });
                if(treeNode.getParentNode().id!=1){
                    var btn1 = $("#doMapRemove_"+treeNode.id);
                    if (btn1) btn1.bind("click", function(){
                        if(!confirm("确认删除 图层 -- " + treeNode.name + " 吗？")){
                            return;
                        }
                        //编辑，根据父节点不同，功能不同
                        if(treeNode.getParentNode().id==2){//如果是专题服务
                            var treeObj = $.fn.zTree.getZTreeObj("doMapTree_Template");
                            treeObj.removeNode(treeNode,true);
                            treeNode.getParentNode().isParent=true;
                            treeObj.refresh();
                            if(map&&(map.getLayer(treeNode.url))){
                                var thisLayer = map.getLayer(treeNode.url);
                                map.removeLayer(thisLayer);
                            }
                        }
                        if(treeNode.getParentNode().id==3){//如果是要素
                            // alert("删除要素数据");
                            var treeObj = $.fn.zTree.getZTreeObj("doMapTree_Template");
                            treeObj.removeNode(treeNode,true);
                            treeNode.getParentNode().isParent=true;
                            treeObj.refresh();
                            //删除节点时将地图上的图层也删去
                            //判断是否是模板数据
                            if (treeNode.dataType==="templateData"){
                                if(map&&(map.getLayer(treeNode.data))){
                                    var thisLayer = map.getLayer(treeNode.data);
                                    map.removeLayer(thisLayer);
                                }
                            }
                            else if (treeNode.textLayerChecked == "checked"){
                                if(map&&(map.getLayer(treeNode.data))){
                                    var thisLayer = map.getLayer(treeNode.data);
                                    map.removeLayer(thisLayer);
                                }
                            }
                            else if (treeNode.buttonLayerChecked == "checked"){
                                var layerNow2 = map.getLayer(treeNode["nodePath"][treeNode["nodePath"].length-1].id);
                                if(layerNow2){
                                    map.removeLayer(layerNow2);
                                }
                            }

                        }
                        //删除统计服务图层
                        if(treeNode.getParentNode().id==4){//如果是统计服务
                            var treeObj = $.fn.zTree.getZTreeObj("doMapTree_Template");
                            treeObj.removeNode(treeNode,true);
                            treeNode.getParentNode().isParent=true;
                            treeObj.refresh();
                            //删除节点时将地图上的图层也删去
                            if(map&&(map.getLayer(treeNode.url))) {
                                var thisLayer = map.getLayer(treeNode.url);
                                var thisTjLayerType = thisLayer.name;
                                map.removeLayer(thisLayer);

                                switch (thisTjLayerType) {
                                    case "chartGLayer":
                                        var chartFlag = 0;
                                        for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                                            if (map.getLayer(map.graphicsLayerIds[i]).name == "chartGLayer") {
                                                chartFlag = 1;
                                                break;
                                            }
                                        }
                                        if (chartFlag == 0)
                                            indi = [];
                                        $("#legend-container .legend").remove();
                                        break;
                                    case "classGLayer":
                                        var classFlag = 0;
                                        for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                                            if (map.getLayer(map.graphicsLayerIds[i]).name == "classGLayer") {
                                                classFlag = 1;
                                                break;
                                            }
                                        }
                                        if (classFlag == 0)
                                            field_cn = "";
                                        $("#legend-container .legend").remove();
                                        break;
                                }
                                //删除节点时，调整组合图例的内容
                                editFlag = 1;
                                switch (thisTjLayerType){
                                    case "chartGLayer":
                                        var cLN = 0;
                                        for (var i=0; i<map.graphicsLayerIds.length; i++) {
                                            if (map.getLayer(map.graphicsLayerIds[i]).name == "chartGLayer") {
                                                var thisLayer = map.getLayer(map.graphicsLayerIds[i]);
                                                cLN++;
                                                var zoomLevel = map.getZoom();
                                                if (zoomLevel < 9)
                                                    changeLayerOnZoom(thisLayer, "chartLayerData", "1", cLN);
                                                else
                                                    changeLayerOnZoom(thisLayer, "chartLayerData", "2", cLN);
                                            }
                                        }
                                        break;
                                    case "classGLayer":
                                        for (var i=0; i<map.graphicsLayerIds.length; i++) {
                                            if (map.getLayer(map.graphicsLayerIds[i]).name == "classGLayer"){
                                                var thisLayer = map.getLayer(map.graphicsLayerIds[i]);
                                                var zoomLevel = map.getZoom();
                                                if (zoomLevel < 9)
                                                    changeLayerOnZoom(thisLayer, "classLayerData", "1", 1);
                                                else
                                                    changeLayerOnZoom(thisLayer, "classLayerData", "2", 1);
                                            }
                                        }
                                        break;
                                }
                                editFlag = 0;
                            }


                        }
                    });
                }

            }

        };
        function removeHoverDom(treeId, treeNode) {
            if(treeNode.isParent){
                $("#doMapAdd_" +treeNode.id).unbind().remove();
            }
            else{
                $("#doMapEdit_" +treeNode.id).unbind().remove();
                $("#doMapRemove_" +treeNode.id).unbind().remove();
            }

        };

        function beforeRename(treeId, treeNode, newName, isCancel) {
            //className = (className === "dark" ? "":"dark");
            //showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
            if (newName.length == 0) {
                setTimeout(function() {
                    var zTree = $.fn.zTree.getZTreeObj("doMapTree_Template");
                    zTree.cancelEditName();
                    alert("节点名称不能为空.");
                }, 0);
                return false;
            }
            return true;
        }

        function beforeRemove(treeId, treeNode) {
            /*  var zTree = $.fn.zTree.getZTreeObj("doMapTree_Template");
              zTree.selectNode(treeNode);
              return confirm("确认删除 图层 -- " + treeNode.name + " 吗？");*/
        }
        // if(doMapIndex_Template==0){
        layerNodesObj_Template=$.fn.zTree.init($("#doMapTree_Template"), setting, layerNodes_Model);

        // doMapIndex_Template=1;
        // }

    }

    function layerTitle() {
        var title;
        if (mappingPageType == 0)
            title = mapName;
        else if (mappingPageType == 1)
            title = "编辑：" + userMapName;
        else
            title = "查看：" + userMapName;
        return title;
    }
    layui.use('layer', function () {
        var tTreeLayer = layui.layer;
        console.log(mapName);
        tTreeLayer.open({
            title: layerTitle(),
            skin: "layui-layer-lan",
            type: 1,
            shade: 0,
            resize: true,
            maxmin:true,
            closeBtn:0,
            area: ['250', '400px'],
            // btn: ['编辑图层'],
            content: $('#complexLayer_Template'),
            success: function (index, layero) {

                if (mappingPageType == 1 || mappingPageType == 2){
                    if (map.getLayer("studyAreaLayer"))
                        map.removeLayer(map.getLayer("studyAreaLayer"));
                    if (mappingPageType == 1){
                        if (map.getLayer("pointPlotLayer"))
                            map.removeLayer(map.getLayer("pointPlotLayer"));
                        if (map.getLayer("linePlotLayer"))
                            map.removeLayer(map.getLayer("linePlotLayer"));
                        if (map.getLayer("polygonPlotLayer"))
                            map.removeLayer(map.getLayer("polygonPlotLayer"));
                    }
                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree_Template");
                    var nodes = treeObj.getNodesByParam("isParent", true, null);
                    var checkedNodes1 = nodes[1].children;
                    var checkedNodes2 = nodes[2].children;
                    var checkedNodes3 = nodes[3].children;
                    for(var i=0;i<checkedNodes3.length;i++){
                        var content;
                        if (checkedNodes3[i].allContent)
                            content = checkedNodes3[i].allContent;
                        else
                            content = checkedNodes3[i];
                        if (content.cartographydata.type == "2") {
                            // layerNodes_Model[3].children[i].checked = true;
                            tjLayerName=checkedNodes3[i]["name"];
                            var tjType = "classLayerData";
                            fieldsOrIndi = content.statisticdata.fieldsName;
                            var tjLayerContent = {};
                            tjLayerContent.name = tjLayerName;
                            tjLayerContent.spatialdata = content.spatialdata;
                            tjLayerContent.statisticdata = content.statisticdata;
                            tjLayerContent.cartographydata = content.cartographydata;
                            var str=JSON.stringify(tjLayerContent);
                            var zoomLevel = map.getZoom();
                            if (zoomLevel < 9)
                                initTjLayer(str, tjType, "1");
                            else
                                initTjLayer(str, tjType, "2");
                            break;
                        }
                    }
                    if (checkedNodes1.length){
                        for (var i=0; i<checkedNodes1.length; i++){
                            checkedNodes1[i].checked = false;
                            treeObj.checkNode(checkedNodes1[i], true, false, true);
                        }
                    }
                    for (var j=0; j<checkedNodes2.length; j++){
                        checkedNodes2[j].checked = false;
                        treeObj.checkNode(checkedNodes2[j], true, false, true);
                    }

                    for (var k=0; k<checkedNodes3.length; k++){
                        checkedNodes3[k].checked = false;
                        treeObj.checkNode(checkedNodes3[k], true, false, true);
                    }

                    // treeObj.setEditable(false);
                    for(var i=0;i<checkedNodes3.length;i++){
                        var content;
                        if (checkedNodes3[i].allContent)
                            content = checkedNodes3[i].allContent;
                        else
                            content = checkedNodes3[i];
                        if (content.cartographydata.type == "1") {
                            // layerNodes_Model[3].children[i].checked = true;
                            tjLayerName=checkedNodes3[i]["name"];
                            var tjType = "chartLayerData";
                            fieldsOrIndi = content.statisticdata.fieldsName;
                            var tjLayerContent = {};
                            tjLayerContent.name = tjLayerName;
                            tjLayerContent.spatialdata = content.spatialdata;
                            tjLayerContent.statisticdata = content.statisticdata;
                            tjLayerContent.cartographydata = content.cartographydata;
                            var str=JSON.stringify(tjLayerContent);
                            var zoomLevel = map.getZoom();
                            if (zoomLevel < 9)
                                initTjLayer(str, tjType, "1");
                            else
                                initTjLayer(str, tjType, "2");
                            break;
                        }
                    }
                }


            },
            yes: function(index, layero) {//确定后执行回调

            },
            cancel: function(index, layero){
                if(confirm('确定要关闭么,将清除所有操作图层')){ //只有当点击confirm框的确定时，该层才会关闭

                    layer.close(index)
                }
                return false;
            }
        });
    });
    layui.use('element', function(){
        var element = layui.element;
        //同空白模板
        element.on('tab(test2)', function(data){
            // console.log(data);
            if (data.index == 1){
                layerEdit();
            }
        });
    });
}

//空白模板制图按钮
function blank_btnClick() {
    // var index=parent.layer.getFrameIndex(window.name);
    // parent.layer.close(index);
    // parent.location.reload();
    // var parentId=parent.$("#id").val();
    if (alertFlag ==0){
        typeFlag = 1;
        layerCloseFlag = 1;
        blankFlag = 1;
        doMap();
        blankClassNo = layer.index;
    }
    else if (alertFlag ==1){
        layer.confirm('该操作会清除之前编辑的内容，是否继续？', {icon: 3, title:'提示'}, function(index){
            layerCloseFlag = 1;
            layer.close(layer.index);
            if (layerIndex){
                if (typeFlag == 0){
                    // for(var i=1; i<layerIndex+1; i++){
                    if (templateClassNo)
                        $("#layui-layer"+ templateClassNo).css("display", "none");
                    // layer.close(layerIndex);
                }
            }
            //移除非标绘的图层
            // var otherLayerIDs = (map.graphicsLayerIds).slice();
            // for (var i=0; i<otherLayerIDs.length;i++){
            //     if(otherLayerIDs[i]==='pointPlotLayer'||otherLayerIDs[i]==='linePlotLayer'||otherLayerIDs[i]==='polygonPlotLayer'){
            //         otherLayerIDs.splice(i,1)
            //     }
            // }
            // for (var i=0; i<otherLayerIDs.length;i++){
            //     var layerRemoved = map.getLayer(otherLayerIDs[i])
            //     map.removeLayer(layerRemoved);
            // }
            polygonFeatureLayer.clear();
            polylineFeatureLayer.clear();
            pointFeatureLayer.clear();
            //删除本地和缓存的标绘图形
            deleteAllFeatureFromLocalstorage();
            //移除所有图层
            map.removeAllLayers();
            //将清将清空graphics的图层加上，因为后面还要标绘。
            map.addLayer(polygonFeatureLayer);
            map.addLayer(polylineFeatureLayer);
            map.addLayer(pointFeatureLayer);
            /* $.each(baseMap,function (i) {
                map.addLayer(baseMap[i]);
            }); */
            require(["esri/layers/WebTiledLayer","esri/layers/ArcGISDynamicMapServiceLayer","esri/layers/ArcGISTiledMapServiceLayer"],function (WebTiledLayer,ArcGISDynamicMapServiceLayer,ArcGISTiledMapServiceLayer) {
              
                baseMap=new Array();
                var baseMapUrls=layerNodes[0].children[0].url.split(",");
                var baseMapType=layerNodes[0].children[0].mapType;
                 $.each(baseMapUrls, function (i) {
                     var url=baseMapUrls[i];
                     switch (baseMapType){
                         case "WebTiledLayer":
                             baseMap.push(new WebTiledLayer(url,{"id":url
             }));
             break;
                         case "ArcGISDynamicMapServiceLayer":
                             baseMap.push(new ArcGISDynamicMapServiceLayer(url,{"id":url}));
                             break;
                         case "ArcGISTiledMapServiceLayer":
                             baseMap.push(new ArcGISTiledMapServiceLayer(url,{"id":url}));
                             break;
     
                     }
                     map.addLayer(baseMap[i]);
     
                 });
                map.addLayer(zjBaseLayer);
            });
            //清空之前交互制图中添加的图层节点
            var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
            if (treeObj){
                var nodes = treeObj.getNodesByParam("isParent", true, null);
                // console.log(nodes);
                treeObj.removeChildNodes(nodes[1]);
                treeObj.removeChildNodes(nodes[2]);
                treeObj.removeChildNodes(nodes[3]);
                treeObj.refresh();
            }
            //清空之前模板制图中添加的图层节点
            layerNodes[1].children = [];
            layerNodes[2].children = [];
            layerNodes[3].children = [];
            //清空图例
            $("#legend-container .legend").remove();
            indi = [];
            field_cn = "";

            doMap();
            if (blankFlag == 0)
                blankClassNo = layer.index;
            blankFlag = 1;
            $("#layui-layer"+ (blankClassNo)).css("display", "block");
            typeFlag = 1;
            $("#layui-layer"+ (blankClassNo) +" .layui-layer-title").text('交互制图');
            // layerIndex = layer.index;
            // $(".layui-layer").css("display", "block");
        });
    }

//选择制图模板,弹出制图模板图层
}

function chooseTemplate(mapName) {
    var index=parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
    // parent.layer.closeAll();
    parent.sweetAlert1(mapName);
}

//图层顺序改变和编辑
function layerEdit() {
    //构造当前图层树
    var layerObj;
    var setting = {
        check:{
            enable: false,
        },
        view:{
            showIcon: true,
        },
        edit:{
            enable: true,
            drag:{
                isCopy: false,
                isMove: true,
                inner: false,
                // prev: canPrev,
                // next: canPrev,
            },
            showRenameBtn: false,
            showRemoveBtn: false,
        },
        data:{
            keep:{
                leaf: true,
                parent: true,
            }
        },
        callback:{
            beforeDrag: zTreeBeforeDrag,
            beforeDrop: zTreeBeforeDrop,
            onDrop: zTreeOnDrop,
            onClick: zTreeOnClick,
        }
    };
    // function canPrev(treeId, nodes, targetNode) {
    //     return !targetNode.isParent;
    // }
    //父节点不能移动
    function zTreeBeforeDrag(treeId, treeNodes) {
        if(treeNodes[0]["isParent"] == true || treeNodes[0].name == "地理底图") //暂时设定地理底图节点不能移动
            return false;     
        return true;
    }
    var targetNodeIndex;
    var targetLayerIndex1;
    var baseMapNode1;
    var baseMapNode1Index;
    //节点只能在同一图层组中移动
    function zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType) {
        var oldPid=treeNodes[0].getParentNode();
        var targetPid=targetNode.getParentNode();
        targetNodeIndex = targetNode.getIndex();

        for (var ii=0; ii<map.layerIds.length; ii++){
            if (map.layerIds[ii] == targetNode.mapId){
                targetLayerIndex1 = ii;
                break;
            }
        }
        if(oldPid!=targetPid){
            alert("只能在同一图层组中移动位置！");
            return false;
        }
    }
    //节点移动后改变图层顺序
    function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) {
        if (targetNode){
            if (treeNodes[0].getParentNode().name == "图层"){
                // console.log(targetNode.name);
                var nodeIndex = treeNodes[0].getIndex();
                var thisLayer = map.getLayer(treeNodes[0].mapId);
                var newLayerIndex = treeNodes[0].getParentNode().children.length - nodeIndex - 1;
                map.reorderLayer(thisLayer, newLayerIndex);
                $("#legend-container .legend").remove();
                var classDropFlag = 0;
                if (treeNodes[0].cartographydata){
                    if (treeNodes[0].cartographydata.type == "2")
                        classDropFlag = 1;
                }
                else if (treeNodes[0].allContent.cartographydata){
                    if (treeNodes[0].allContent.cartographydata.type == "2")
                        classDropFlag = 1;
                }
                if (classDropFlag == 1){
                    editFlag = 1;
                    for (var i=map.graphicsLayerIds.length-1; i>=0; i--){
                        if (map.getLayer(map.graphicsLayerIds[i]).name == "classGLayer"){
                            var thisLayer = map.getLayer(map.graphicsLayerIds[i])
                            var zoom = map.getZoom();
                            if (zoom < 9)
                                changeLayerOnZoom(thisLayer, "classLayerData", "1", 1);
                            else
                                changeLayerOnZoom(thisLayer, "classLayerData", "2", 1);
                            break;
                        }
                    }
                    editFlag = 0;
                    classDropFlag = 0;
                }
            }
            else if (baseMap.length){
                if (treeNodes[0].name == "地理底图"){ //暂未实现底图节点的移动
                    var nodeIndex = treeNodes[0].getIndex();
                    var theseLayers = new Array();
                    // var newLayerIndexs;
                    // theseLayers.push(map.getLayer(treeNodes[0].mapId));
                    var firstLayerIndex;
                    for (var j=0; j<map.layerIds.length; j++){
                        if (map.layerIds[j] == treeNodes[0].mapId){
                            firstLayerIndex = j;
                            break;
                        }
                    }
                    for (var i=0; i<baseMap.length; i++){
                        theseLayers.push(map.getLayer(map.layerIds[firstLayerIndex+i]));
                        // newLayerIndexs = treeNodes[0].getParentNode().children.length + baseMap.length - 1 - (nodeIndex+i) - 1;
                        // newLayerIndexs.push(nodeIndex + i + 1);
                        // map.reorderLayer(theseLayers, newLayerIndexs);
                    }
                    // console.log(targetNode.getIndex());
                    var newLayerIndex;

                    if (targetNode.getIndex() > targetNodeIndex){
                        //移到了目标节点上面
                        if (targetLayerIndex1 == (map.layerIds.length - 1)){
                            newLayerIndex = targetLayerIndex1;
                            for (var k=0; k<theseLayers.length; k++){
                                map.reorderLayer(theseLayers[k], (newLayerIndex));
                            }
                        }

                        else{
                            newLayerIndex = targetLayerIndex1;
                            for (var k=0; k<theseLayers.length; k++){
                                map.reorderLayer(theseLayers[k], (newLayerIndex+1));
                            }
                        }

                    }
                    else if (targetNode.getIndex() < targetNodeIndex){
                        //移到了目标节点下面
                        if (targetLayerIndex1 == 0){
                            newLayerIndex = targetLayerIndex1;
                            for (var k=0; k<theseLayers.length; k++){
                                map.reorderLayer(theseLayers[k], (newLayerIndex+k));
                            }
                        }

                        else{
                            newLayerIndex = targetLayerIndex1;
                            for (var k=0; k<theseLayers.length; k++){
                                map.reorderLayer(theseLayers[k], (newLayerIndex+k));
                            }
                        }
                    }
                }
                else {
                    var zTreeObj;
                    if (typeFlag == 1)
                        zTreeObj = $.fn.zTree.getZTreeObj("layerTree");
                    else
                        zTreeObj = $.fn.zTree.getZTreeObj("layerTree_Template");
                    var baseMapNode2 = zTreeObj.getNodeByParam("name", "地理底图", null);
                    // console.log(baseMapNode1Index);
                    // console.log(baseMapNode2.getIndex());
                    if (baseMapNode2.getIndex() == baseMapNode1Index){
                        var nodeIndex = treeNodes[0].getIndex();
                        var thisLayer = map.getLayer(treeNodes[0].mapId);
                        var newLayerIndex = treeNodes[0].getParentNode().children.length + baseMap.length - 1 - nodeIndex - 1;
                        map.reorderLayer(thisLayer, newLayerIndex);
                    }
                    else if (baseMapNode2.getIndex() < baseMapNode1Index){
                        //某个非底图图层移动到了底图图层下面
                        var nodeIndex = treeNodes[0].getIndex();
                        var thisLayer = map.getLayer(treeNodes[0].mapId);
                        var newLayerIndex = treeNodes[0].getParentNode().children.length - nodeIndex - 1;
                        map.reorderLayer(thisLayer, newLayerIndex);
                    }
                    else{ //某个非底图图层移动到了底图图层上面
                        var nodeIndex = treeNodes[0].getIndex();
                        var thisLayer = map.getLayer(treeNodes[0].mapId);
                        var newLayerIndex = treeNodes[0].getParentNode().children.length + baseMap.length - 1 - nodeIndex - 1;
                        map.reorderLayer(thisLayer, newLayerIndex);
                    }
                    baseMapNode1Index = baseMapNode2.getIndex();
                }
            }
            else{
                // console.log(targetNode.name);
                var nodeIndex = treeNodes[0].getIndex();
                var thisLayer = map.getLayer(treeNodes[0].mapId);
                var newLayerIndex = treeNodes[0].getParentNode().children.length - nodeIndex - 1;
                map.reorderLayer(thisLayer, newLayerIndex);
            }
        }

    }
    //点击节点打开透明度设置窗口
    function zTreeOnClick(event, treeId, treeNode, clickFlag) {
        var nowOpacity = map.getLayer(treeNode.mapId).opacity * 100;
        layui.use('layer', function (layui_index) {
            var layer = layui.layer;
            layer.open({
                title: '图层透明度设置',
                skin: "layui-layer-lan",
                type: 0,
                shade: 0,
                content: "<div id='opacity'><p style='padding-left: 12px'><div cata='layerOpacity' id='layerOpacity' class='data_slider' style='padding-top: 25px'></div></p></div>",

                yes: function (index, layero) {//确定后执行回调
                    layer.close(index);
                }
            });
        });
        layui.use('slider', function(){
            var slider = layui.slider;

            //渲染
            slider.render({
                elem: '#layerOpacity',  //绑定元素
                min: 0,
                max: 100,
                value: nowOpacity,
                change: function (value) {
                    var changeOpacity = value / 100;
                    map.getLayer(treeNode.mapId).setOpacity(changeOpacity);
                },
            });
        });
    }
    var treeObj;
    var parentNodes;
    var allServiceNodes; //当前模板的所有专题图层
    var allFeatureNodes; //当前模板的所有要素图层
    var allTjNodes;
    var serviceLayerNodes = new Array(); //当前在map中的地图图层
    var featureLayerNodes = new Array(); //当前在map中的graphic图层
    if (typeFlag == 0 || mappingPageType != 0){
        treeObj = $.fn.zTree.getZTreeObj("doMapTree_Template");
    }
    else if (typeFlag == 1)
        treeObj = $.fn.zTree.getZTreeObj("doMapTree");
    if (treeObj){
        parentNodes = treeObj.getNodesByParam("isParent", true, null);
        allFeatureNodes = parentNodes[2].children;
        allServiceNodes = parentNodes[1].children;
        allTjNodes = parentNodes[3].children;
    }
    //得到所有地图节点
    for (var i=map.layerIds.length-1; i>=0; i--){
        if (allServiceNodes){
            for (var j=0; j<allServiceNodes.length; j++){
                if (map.layerIds[i] == allServiceNodes[j].url){
                    allServiceNodes[j].mapId = map.layerIds[i];
                    serviceLayerNodes.push(allServiceNodes[j]);
                }

            }
        }
    }
    var baseMapNode = {name: "地理底图", mapId: map.layerIds[0]};
    serviceLayerNodes.push(baseMapNode);
    //得到所有图层节点
    for (var i=map.graphicsLayerIds.length-1; i>=0; i--){
        switch (map.graphicsLayerIds[i]){
            case "studyAreaLayer":
                featureLayerNodes.push({name:"制图区域框", mapId:map.graphicsLayerIds[i]});
                break;
            case "pointPlotLayer":
                featureLayerNodes.push({name:"标绘点", mapId:map.graphicsLayerIds[i]});
                break;
            case "linePlotLayer":
                featureLayerNodes.push({name:"标绘线", mapId:map.graphicsLayerIds[i]});
                break;
            case "polygonPlotLayer":
                featureLayerNodes.push({name:"标绘面", mapId:map.graphicsLayerIds[i]});
                break;
        }
        if (allFeatureNodes){
            for (var j=0; j<allFeatureNodes.length; j++){
                if (allFeatureNodes[j].dataType){
                    if (allFeatureNodes[j].dataType == "templateData"){
                        if (map.graphicsLayerIds[i] == allFeatureNodes[j].data){
                            allFeatureNodes[j].mapId = map.graphicsLayerIds[i];
                            featureLayerNodes.push(allFeatureNodes[j]);
                        }
                    }
                    else if (allFeatureNodes[j].dataType == "urlFeatureData"){
                        if (map.graphicsLayerIds[i] == allFeatureNodes[j].url){
                            allFeatureNodes[j].mapId = map.graphicsLayerIds[i];
                            featureLayerNodes.push(allFeatureNodes[j]);
                        }
                        else if (allFeatureNodes[j]["thematicData"])
                            if (map.graphicsLayerIds[i] == allFeatureNodes[j]["thematicData"].id){
                                allFeatureNodes[j].mapId = map.graphicsLayerIds[i];
                                featureLayerNodes.push(allFeatureNodes[j]);
                            }
                    }
                }
                else{
                    if (map.graphicsLayerIds[i] == allFeatureNodes[j].url){
                        allFeatureNodes[j].mapId = map.graphicsLayerIds[i];
                        featureLayerNodes.push(allFeatureNodes[j]);
                    }

                    else if (allFeatureNodes[j]["thematicData"])
                        if (map.graphicsLayerIds[i] == allFeatureNodes[j]["thematicData"].id){
                            allFeatureNodes[j].mapId = map.graphicsLayerIds[i];
                            featureLayerNodes.push(allFeatureNodes[j]);
                        }
                }
                // else if (allFeatureNodes[j].textLayerChecked == "checked")
            }
        }
        if (allTjNodes){
            for (var k=0; k<allTjNodes.length; k++){
                if (map.graphicsLayerIds[i] == allTjNodes[k].url){
                    allTjNodes[k].mapId = allTjNodes[k].url;
                    featureLayerNodes.push(allTjNodes[k]);
                }
            }
        }
    }
    var layerTreeData = [
        {name: "地图", pid: 1, isParent: true, open: true, children: serviceLayerNodes},
        {name: "图层", pid: 2, isParent: true, open: true, children: featureLayerNodes}
    ]

    // layui.use('layer', function () {
    //     var layer1 = layui.layer;
    //     layer1.open({
    //         title: '图层编辑',
    //         skin: "layui-layer-lan",
    //         type: 1,
    //         shade: 0,
    //         resize: false,
    //         area: ["200px","400px"],
    //         btn: ['刷新'],
    //         content: "<div id='layerEdit'>" +
    //         "<p style='padding: 6px'>在下方拖动图层以改变叠放顺序，点击图层以设置其透明度:</p>" +
    //         "<p><div id='layerTree' class='ztree' style='padding: 6px;'></div></p></div>",
    //         // content: $('#layerTree'),
    //         yes: function(index, layero) {
    //             layerEdit();
    //             layer.close(layer.index);
    //         }
    //     });
    // });
    if (typeFlag == 1)
        layerObj = $.fn.zTree.init($("#layerTree"), setting, layerTreeData);
    else
        layerObj = $.fn.zTree.init($("#layerTree_Template"), setting, layerTreeData);
    baseMapNode1 = layerObj.getNodeByParam("name", "地理底图", null);
    baseMapNode1Index = baseMapNode1.getIndex();
}

//选择制图模板
$("#templateMap").click(function () {
    /**
     * @Description: 制图模板函数，点击出现详细灾害标签
     * @Param:
     * @return:
     */
    //
    if (layerCloseFlag == 1)
        layerIndex = layer.index;
    layerCloseFlag = 0;
    // console.log(layerIndex);
    for(var i=0; i<$(".layui-layer-title").length; i++){
        var title = $($(".layui-layer-title")[i]).text();
        if(title == "制图模板选择"){
            return;
        }
    }
    alertFlag = 1;
    layui.use('layer', function () {
        var layer1 = layui.layer;
        layer1.open({
            title: '制图模板选择',
            skin: "layui-layer-lan",
            type: 2,
            shade: 0,
            resize: false,
            area: ["600px","500px"],
            // btn: ['按钮1','按钮2','按钮3'],
            content: 'indexMini_zj.html',
            yes: function(index, layero) {//确定后执行回调

            }
        });
    });
})

//图层check事件
function layerOncheck(treeId, treeNode) {
    //如果没有创建图例，先创建图例
    if (mappingPageType == 0){
        if(!iMLegendCreated){
            require([
                "esri/dijit/Legend",
                "dojo/domReady!"
            ], function(
                Legend
            ) {
                iMLegend = new Legend({
                    autoUpdate:true,
                    map: map
                }, "iMLegendDiv");
                iMLegend.startup();
                iMLegendCreated = true;
            })
        }
    }
    if(treeNode.isParent){
        return;
    }
    else {
        if(treeNode.getParentNode().id==1){
            //地理底图
            if(treeNode.checked){
                $.each(baseMap,function (i) {
                    if(treeNode.url.indexOf(baseMap[i].url)!=-1){
                        map.removeLayer(baseMap[i]);
                    }

                });

            }else {
               //控制底图只能显示一个
                var treeObj = $.fn.zTree.getZTreeObj(treeId);
                var mateNote=treeNode.getParentNode().children;
                for(var i=0;i<mateNote.length;i++){
                    if(mateNote[i].checked){

                        $.each(baseMap,function (j) {
                            if(mateNote[i].url.indexOf(baseMap[j].url)!=-1){
                                map.removeLayer(baseMap[j]);
                            }

                        });
                        treeObj.checkNode(mateNote[i], false, true);
                    }
                }
                require(["esri/layers/WebTiledLayer","esri/layers/ArcGISDynamicMapServiceLayer","esri/layers/ArcGISTiledMapServiceLayer"],function (WebTiledLayer,ArcGISDynamicMapServiceLayer,ArcGISTiledMapServiceLayer) {
                    var mate=treeNode.getParentNode().children;
                    for (var  i = 0; i < mate.length; i++) {
                        if(mate[i].id==treeNode.id)
                            continue;
                        var treeObj = $.fn.zTree.getZTreeObj(treeId);
                        treeObj.checkNode(mate[i], false, true);
                    }
                    $.each(baseMap,function (i) {
                        if(treeNode.url.indexOf(baseMap[i].url)!=-1){
                            map.removeLayer(baseMap[i]);
                        }

                    });
                    baseMap=new Array();
                    var urls=treeNode.url.split(",");
                var baseMapType=treeNode.mapType;
                    $.each( urls, function (i) {
                        var url=urls[i];
                        switch (baseMapType){
                            case "WebTiledLayer":
                                baseMap.push(new WebTiledLayer(url,{"id":url
                                }));
                                break;
                            case "ArcGISDynamicMapServiceLayer":
                                baseMap.push(new ArcGISDynamicMapServiceLayer(url,{"id":url}));
                                break;
                            case "ArcGISTiledMapServiceLayer":
                                baseMap.push(new ArcGISTiledMapServiceLayer(url,{"id":url}));
                                break;

                        }
                        map.addLayer(baseMap[i]);

                    });

                    $.each(baseMap,function (i) {
                        map.reorderLayer(baseMap[i],i);
                    });
                    //map.removeLayer(baseMap);
                   /* baseMap = new WebTiledLayer(
                        treeNode.url
                    );
                    map.addLayer(baseMap)
                    map.reorderLayer(baseMap,1);*/
                });

            }
        }
        if(treeNode.getParentNode().id==2){
            //专题服务
            if(!treeNode.checked){
                /*   ServerLayerArr.filter(function (p) {
                       var id=treeNode.name+"_"+(treeNode.url);
                       if(p.id==id){
                           map.removeLayer(map.getLayer(id));
                       }
                   });*/
                require([
                    "esri/layers/ArcGISDynamicMapServiceLayer",
                    "esri/InfoTemplate", "esri/dijit/PopupTemplate"
                ], function (ArcGISDynamicMapServiceLayer, InfoTemplate, PopupTemplate)
                {
                    if (map && (map.getLayer(treeNode.url))) {//如果已经加载，只是做了隐藏，显示就好了，下面的步骤跳过
                        var thisLayer = map.getLayer(treeNode.url);
                        thisLayer.show();
                        return;
                    }

                    var serviceUrl = treeNode.url;
                    //判断专题服务是图层还是地图

                    var serviceUrlstr=serviceUrl;
                    if(serviceUrl.substr(-9)!="MapServer"){
                        serviceUrlstr=serviceUrl.substring(0,serviceUrl.lastIndexOf("/"));
                    }
                    try{
                        var layer = new ArcGISDynamicMapServiceLayer(serviceUrlstr,{id:serviceUrl});
                    }catch (e) {
                        alert("服务地址有误！")
                        return false;
                    }
                    //如果是图层，则只显示这个图层
                    if(serviceUrl.substr(-9)!="MapServer"){
                        var showindex=serviceUrl.substring(serviceUrl.lastIndexOf("/")+1,serviceUrl.length);
                        layer.setVisibleLayers([showindex]);
                    }
                    map.addLayer(layer);
                    /*//var layer = new ArcGISDynamicMapServiceLayer(serviceUrl,{id: $("#newSLName").val()+"_"+$("#newSLAds").val()});
                    ServerLayerArr.push(layer);*/

                });
            }
            else {
                /*ServerLayerArr.filter(function (p) {
                    var id=treeNode.name+"_"+(treeNode.url);
                    if(p.id==id){
                        map.addLayer(p);
                    }
                });*/
                var thisLayer = map.getLayer( treeNode.url);
                thisLayer.hide();
            }
        }
        if (treeNode.getParentNode().id===3) {//如果操作的是要素图层
            var dataUrl_template = treeNode.data;
            var lastDataUrl_template = treeNode.lastUrl;//
            var isChecked = !treeNode.checked;
            var dataUrl = treeNode.url;
            var lastDataUrl = treeNode.lastUrl;//上一次存储的url。加载时，应先将上一次存储的url代表的要素删去
            var lastThematic = treeNode.thematicData; //上一次存储的专题数据。加载时，应先将上一次存储的专题要素删去
            if(isChecked){//如果被勾选

                if (treeNode.dataType=="templateData")
                {
                    //如果是模板数据
                    //如果服务地址不为空
                    //先判断上次存储的url代表的图层是否加载，如果加载了，则删去
                    if (map && (map.getLayer(dataUrl_template))) {//如果已经加载，只是做了隐藏，显示就好了，下面的步骤跳过
                        var thisLayer = map.getLayer(dataUrl_template);
                        thisLayer.show();
                        return;
                    }
                    if (map && (map.getLayer(dataUrl_template))) {
                        map.removeLayer(map.getLayer(dataUrl_template));
                    }
                    require([
                        "esri/layers/FeatureLayer",
                        "esri/InfoTemplate", "esri/dijit/PopupTemplate", "esri/renderers/SimpleRenderer"
                    ], function (FeatureLayer, InfoTemplate, PopupTemplate, SimpleRenderer) {

                        var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                        var layer = new FeatureLayer(dataUrl_template, {
                            mode: FeatureLayer.MODE_SNAPSHOT,
                            outFields: ["*"],
                            opacity: "1",
                            infoTemplate: infoTemplate,
                            id: dataUrl_template
                        });
                        layer.on("load", function () {
                            var simpleJson_line = {
                                "type": "simple",
                                "label": treeNode.name,
                                "description": "",
                                "symbol": {
                                    "type": "esriSLS", //SimpleLineSymbol(简单线类型)
                                    "color": [115, 76, 0, 255], //颜色
                                    "width": 2, //线宽
                                    "style": "esriSLSDash" //线形
                                }
                            };
                            var simpleJson_polygon = {
                                "type": "simple",
                                "label": treeNode.name,
                                "description": "",
                                "symbol": {
                                    "type": "esriSFS",
                                    "style": "esriSFSSolid",
                                    "color": [115, 76, 0, 255],
                                    "outline": {
                                        "type": "esriSLS",
                                        "style": "esriSLSSolid",
                                        "color": [110, 110, 110, 255],
                                        "width": 1
                                    }
                                }
                            };
                            var simpleJson_point = {
                                "type": "simple",
                                "label": treeNode.name,
                                "description": "",
                                "symbol": {
                                    "type": "esriPMS",
                                    "url": "./assets/img/pointIcon/6.png",
                                    "contentType": "image/png",
                                    "width": 15,
                                    "height": 15
                                }
                            };
                            var rend;
                            require(["esri/renderers/SimpleRenderer"
                            ], function (SimpleRenderer) {
                                switch (layer.geometryType) {
                                    case "esriGeometryPoint":
                                        if(treeNode.style){
                                            simpleJson_point.symbol = treeNode.style.render;
                                        }
                                        rend = new SimpleRenderer(simpleJson_point)
                                        break;
                                    case "esriGeometryPolyline":
                                        if(treeNode.style){
                                            simpleJson_line.symbol = treeNode.style.render;
                                        }
                                        rend = new SimpleRenderer(simpleJson_line)
                                        break;
                                    case "esriGeometryPolygon":
                                        if(treeNode.style){
                                            simpleJson_polygon.symbol = treeNode.style.render;
                                        }
                                        rend = new SimpleRenderer(simpleJson_polygon)
                                        break;
                                }
                                layer.setRenderer(rend);
                            });
                        });
                        layer.on("update", function(evt) {
                            if(evt.target.geometryType!="esriGeometryPoint"){

                                return;
                            }
                            if(evt.target.renderer.symbol.url.indexOf(".")!=0){
                                return;
                            }
                            var base64= getBase64Image( evt.target.renderer.symbol.url);
                            evt.target.renderer.symbol.setUrl(base64);
                            evt.target.refresh();
                        });
                        map.addLayer(layer);
                    })
                }
                else {
                    // console.log(treeNode.textLayerChecked);
                    if (treeNode.textLayerChecked == "checked") {
                        if (dataUrl == "") {//服务地址为空，则返回
                            layui.use('layer', function () {
                                var lay = layui.layer;
                                lay.open({
                                    title: '提示'
                                    , content: '请先设置要素图层地址！'
                                });
                            })
                            return;
                        } else {
                            //如果服务地址不为空
                            //先判断上次存储的url代表的图层是否加载，如果加载了，则删去
                            //如果两次URL并没有发生改变,则不做操作
                            if (dataUrl===lastDataUrl && mappingPageType == 0) {
                                return;
                            }
                            if (map && (map.getLayer(lastDataUrl))) {
                                map.removeLayer(map.getLayer(lastDataUrl));
                            }
                            if (map && (map.getLayer(dataUrl))) {//如果已经加载，只是做了隐藏，显示就好了，下面的步骤跳过
                                var thisLayer = map.getLayer(dataUrl);
                                thisLayer.show();
                                return;
                            }
                            require([
                                "esri/layers/FeatureLayer",
                                "esri/InfoTemplate", "esri/dijit/PopupTemplate", "esri/renderers/SimpleRenderer"
                            ], function (FeatureLayer, InfoTemplate, PopupTemplate, SimpleRenderer) {

                                var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                                var layer = new FeatureLayer(dataUrl, {
                                    mode: FeatureLayer.MODE_SNAPSHOT,
                                    outFields: ["*"],
                                    opacity: "1",
                                    infoTemplate: infoTemplate,
                                    id: dataUrl
                                });
                                layer.on("load", function () {
                                    var simpleJson_line = {
                                        "type": "simple",
                                        "label": treeNode.name,
                                        "description": "",
                                        "symbol": {
                                            "type": "esriSLS", //SimpleLineSymbol(简单线类型)
                                            "color": [115, 76, 0, 255], //颜色
                                            "width": 2, //线宽
                                            "style": "esriSLSDash" //线形
                                        }
                                    };
                                    var simpleJson_polygon = {
                                        "type": "simple",
                                        "label": treeNode.name,
                                        "description": "",
                                        "symbol": {
                                            "type": "esriSFS",
                                            "style": "esriSFSSolid",
                                            "color": [115, 76, 0, 255],
                                            "outline": {
                                                "type": "esriSLS",
                                                "style": "esriSLSSolid",
                                                "color": [110, 110, 110, 255],
                                                "width": 1
                                            }
                                        }
                                    };
                                    var simpleJson_point = {
                                        "type": "simple",
                                        "label": treeNode.name,
                                        "description": "",
                                        "symbol": {
                                            "type": "esriPMS",
                                            "url": "./assets/img/pointIcon/6.png",
                                            "contentType": "image/png",
                                            "width": 15,
                                            "height": 15
                                        }
                                    };
                                    var rend;
                                    require(["esri/renderers/SimpleRenderer"
                                    ], function (SimpleRenderer) {
                                        switch (layer.geometryType) {
                                            case "esriGeometryPoint":
                                                // if (treeNode.style)
                                                //     rend = treeNode.style;
                                                // else
                                                    // simpleJson_point.symbol = treeNode.style;
                                                // simpleJson_point.symbol = {"url":"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAL4ElEQVR4XuWbC3BU5RXHf/fe3U2ygQQICQkGQniDI/IQAbEGqy0dlXdBSgq1VrRjUUStOmIVEVSEgZFKfaCotVIfoFiGh6ZTmMrDqECUgAYQeQYCCQl5Zzf33s7Zb1cRgexNdjEzPZnMZnO/xzn/73zn9X1XI/rUEZiuQQ8dI8HCqgGqbOwiYC+wB/gCOBx9Vn48gxblSbvqaButoQmXcF0SdImDYh+U1cOhWiioRi+owSg1T1uwy8TcBmwANgEno8xbYPhoA/CUB9dDvtosiPnhVBoaOjpmpQ/yK2FHBWw4hfFhKVq5VWRibrFhNbAGOBEtMKIKgIa2wh6dPI5F3aBTXIMy6GhYpX7YXAY5p+CDE7gO+o9bmB9Z8E9gfYODOGwQVQB0eNW6u+MtzOkCbxXB8DbQMTYsFkVDbL8F60vg/SJiXi2mHuszE/MfwDKgMqyBGmgUVQCAh+nqnUvBEPi8HDaWQoc4+E07R7wHwDhYA+8UwfOH0b+t22dhCwh/A047GuysxtEGYJgbY4N/Q18Y1hq+roJ1JSAr27sF3NTWOe9+G5YchqVH0XdXCxCzgDedD6R6RBsAmWCjPSQxi40DwA/4TfiqGjaVQbUJqTEwOgXauR3JELAXT+4n5tHD1Jk+MZZTAXGtjijqAABpOtp26+aUVF7oCRUWaDYkumF7BfznlNKItm7olwDXtnYkAIfr4I7d6OtOHbSwxwLbnQxwMQAQfnob6GvMSSmdmNcV6m3QNbAFHg8cqIWPSuCbGkg0oLMXesXDwISwZDEwMKd8gfbG8RIb+gJHwup4MbZAiBENPrdf6DWAsSmQU6K8Qbd4qDOh1oK0GLBk/cpVXFBYB/EGdIiFdh7o7oXO53elLgzq+2yGnZXPAvc0NwAGG+hbzbIstcIi4LYKiNWVcN28SiMqTRBT0NYDtUFbIdpxtFbZi3iX2iqiNV4D4gxwaVBRr4Casgu2lD0H3NXcAHiUQQmP658Mwqq3VDhcZ8P+GrXStg3JMcoQpsWCtBGtEFBaGAqoEp8Kn6V9tQWWrQAzgasS4b9luB7aTz3m1cDmZgWABpvsBd2Hcl9HKKmHkyK0BgkGGJIS1SgBqyz1PckDLQ1oF6MA8FnKbshqe3QFiPwdJ75Awy6ogpvyYF/1i8AfwxX+orhBINNA32t+NcigZzwcqf3eE2hiCG2lyqLSkiQdqIE6C6pM5R1EYPEY8boCIT0GWnuUOz3mh9cKcS8+ih/zNeD3ToS/WADcTkbsi8aBLEzRV1F7UW/jLAckQAggAoQILcbxmE9phpBsh2SP7HFYWQTFfty7akXld9rwFCpXcExRd4MarLT/lD6W53pCeT0c96l9qzfAqwDiDqq7cCl9OsTAyDxYXbwVWBIMfD4G6hxLHuwQbQASdbQDVk6/VlzfRhmw06YKhGS1nVCSCwp9cNWnYivGAKucdD9fW4dcOJ5ytO4x3rcqspRaf1sLNeaP1b+hYSU+6OWFuQcwHtlfZGJlAlJZajJFG4AljEy+U/ugL7b4dXFj4aj/2WKJZ0iPhWu3wSennwfubLLkF2EL6AZ6gflGr678Ng2O10FpI9RfbIEIv70cV1aeGL1rANn3EaFoasBAA/1T89Q10NoFB2uVazvb+jckhgAg7vOuAvTnjuy2sC9tqIuT59EEYCYDWs7RPh+MbQajPvHrTgGI0yHGgCtyJYZ4FHjCiYANtY0aAIE6wKzOWTzWWYW+xX4V/TXk/s7kWFZfEqb3TmBMyMfE6gkUNCSUk+eRBEBStcuA7sClHlzTfXsGxdEtjkDOLgmL09UXSSQLnLATbUXRv234hRPhwmkbCQCkgnGHC/1WA6NbXW8P9E9QjD+cqXy+RH8+2zkArdxQ4YcBn0oQJWGuhLsRpaYCcL+Bfo/Zx3sJD3SC61rjSvViyY9lqYxPQlqJ/hqj/mL8lhzBmLan3MTqBJRGVPomFkTeJtUzgQXdcWen46f+e95O+VXWJ+ovyU+5qfa+k+hPYv+MGLh+O3xc9jpwS6SFl/EaqwGr6OEdxZaB0OYcxUyJ+IQyY1WZS7I7p/tfiqV7qnBduU18/6+AD5sLAI/pccYs69BQVbk5F1WYqs4nKyjW/1zZ34WkCfn+h/ehP3Uwz8LuFw3hG6MBWS6MjfVb+sOQxAvzJJHfrupg+UpXIIS7BQLlLg1vci7V1P0VuLtZAKDBDntGx77awh7YgZJuAyT7/4sqaOWC9jFgWioXaGj1pb0UOz4qwXjom1ITazrwRkPTNea5ExswuAVxWysrh6jqTLgkNQA5EZKMTspYCS5lDyQ6PBeGkvhIyiye48YkWHUCY0wgCLoCkOPziJITAG7nshYver68Gt+ZFj9cdk764aRP1ffEPYrtlBBXQBGhQyQaIjU+iSN6xqua3yN70eYeOGyrQKtJZ4Fns+sEgDmMTZmprbw8PPU/HzA11vcFUDF2AobU+qTKK65PAqcUj8oAgxQ4Buv/idwhiLg9cALATAYnznFtHSxuKdx1b7idGEfRCiExklINPhdtKEX7+bZKGy4ByhseOLwWTgDIjMG9p+7rgS56eJUxOw+v4U3trFVACzI3SdV4SiQNohMAJGr6lz0uZYS2IrgNRHVDri3KYARswfQCtMWHVtsw0hl852/tCADJ9jTYZs/u4uYvmeq4So68hcQxuMWgBWv9gf8F098QSE5nE7sgfaWQIgckLx9Fn/r1EQu7N1ARCRCcsiRzjnFhvFe/tDvabenYZcFbX4auDFjAkAWNWQiAH3wGnwtgIaMno0qyJEZRXGPAKAY/pZYoobQcpq4txhi1s8TEuhw4+lMBIPP+2YPrGd997WFuN8XHvmplE0IAiDCygiHjJlCfmRDJdwFK/hdoGxRHusjf0jcASBDMTG/gQMTI3n3MxOoDFP+UAMjcU3W0l6xrWsHLvVXmJyCIWxPfHljVoAAhTuV7iL7bFsE253sWOj6T1Pjx/Wiz9m+3YWAQpiZj0JgtcOakQzR4zW7n6c7zvWB0sorwpAASbvk7JPiF8oRQcnTbbnilcCXw6yZLHhygqQDIMK2AZ90YU/w3tYZpHdGGJ2HLEbfkAjViF5rArggfq6N18mIPzoXccimKSnE0IhQJAEKM/EyD+924RvomtoXfpUHvePVMQl7xGFIdCjcjlH6iTXJLJCM2UBlyTdsrQZhcgZG7xRGhSAIQYmikBtM8uPrW/SElOeHlKyjPOQJ9WqoLEGLc5B7A6WCdQIxcyF7Ip2iLHKPJkXlysP3bJ3BP2i1H4HL1Ra7ARIyiAUCIuVXPdr1v1N0fPsHutdu49NgUkFJZv5bQxauMpsT8IV8fSorEiAo4cldgRyW8ehR3zmkRfi7wSMQkj6ANOBdPcr3rwLvvvts665fDOLW9kKHXXl1VQsVmN65MDbr5esaoS09yiVq2SOgqjJwfHla/7mKryo8p5/4vAZ9FWngZL1oaMCUjI+P1devWkZSUxJIlS5g9e3YeIKUtSfP6o1xZhig76HE2VgvQvDb2seA7BIeC+b+8TxA1ihYA702ePHnMokWLKCsrIzs7m9zc3PnAA1GTpJEDRwOAVOCb5cuXe2+44QY2b97MiBEj5JwgKhWdRsr9XbdoAHBrhw4dXlm7di1paWksWLCAp59++ktA4vdmR9EAYEV2dva4xYsXU1xczMSJE9mxY0dEg5dIohhpANoA+5ctW5Y4YcIEcnJyGDNGrvMwwOkl5kgKeaGxIg3ApIyMjDfXrFlDamoq8+bNY/78+RK1SfTWLCnSALyVnZ19s7i9wsJCRAvy8/MleJEgpllSJAGQY/Jvly5dmjhp0iTWr1/PuHHjRGip3nzVLKWPcCA0Pj09/R0Jftq3by+WX9Rforcrm6vwkY4E3546deoEcXui/uPHjxf1nwk8+f8CQO6MGTOuXLhwIatXr2bkyEDhtkcwrG22GETSBqwaPnz4qHvvvZcHH3yQvLy8tcCNzVbyIGORBODvffr0mSxXY/Lz8w8Gk52L8v5vU0COJADPSLU4WK+XwEfeDG/2FEkAugZfVloObGn2kgcZ/B8pIEB9C545uQAAAABJRU5ErkJggg==","type":"esriPMS","height":26.666666,"width":26.666666,"size":16,"xoffset":0,"yoffset":0}
                                                rend = new SimpleRenderer(simpleJson_point);
                                                if (treeNode.style)
                                                    rend.symbol = treeNode.style;
                                                break;
                                            case "esriGeometryPolyline":
                                                rend = new SimpleRenderer(simpleJson_line);
                                                if (treeNode.style)
                                                    rend.symbol = treeNode.style;
                                                break;
                                            case "esriGeometryPolygon":
                                                rend = new SimpleRenderer(simpleJson_polygon);
                                                if (treeNode.style)
                                                    rend.symbol = treeNode.style;
                                                break;
                                        }
                                        layer.setRenderer(rend);
                                    });
                                });
                                layer.on("update", function(evt) {
                                    if(evt.target.geometryType!="esriGeometryPoint"){

                                        return;
                                    }
                                    if(evt.target.renderer.symbol.url.indexOf(".")!=0){
                                        return;
                                    }
                                    var base64= getBase64Image( evt.target.renderer.symbol.url);
                                    evt.target.renderer.symbol.setUrl(base64);
                                    evt.target.refresh();
                                });
                                map.addLayer(layer);
                            })
                        }
                    }
                    else if (treeNode.buttonLayerChecked == "checked") {
                        var renderer = null;
                        if (lastThematic) {
                            if (map && (map.getLayer(lastThematic.id))) {
                                renderer = (map.getLayer(lastThematic.id)).renderer;
                                map.removeLayer(map.getLayer(lastThematic.id));
                            }
                        }
                        treeNode.featureRenderer = renderer;
                        beforeThematicLayerAdd("doMapTree_Template", treeNode);
                    }
                }
            }else{//如果取消勾选
                //hide当前url的图层
                if (treeNode.dataType=="templateData"){
                    if (map && (map.getLayer(dataUrl_template))) {
                        var thisLayer = map.getLayer(dataUrl_template);
                        thisLayer.hide();

                    }
                }
                else
                {
                    if (treeNode.textLayerChecked == "checked") {
                        if (map && (map.getLayer(dataUrl))) {
                            var thisLayer = map.getLayer(dataUrl);
                            thisLayer.hide()
                        }
                    }
                    //hide当前的专题要素图层
                    else if (treeNode.buttonLayerChecked == "checked") {
                        var layerNow1 = map.getLayer(treeNode["nodePath"][treeNode["nodePath"].length - 1].id);
                        // console.log(treeNode["nodePath"][treeNode["nodePath"].length - 1]);
                        if (layerNow1)
                            layerNow1.hide();
                    }
                }
                return;
            }
        }
        else {
            var isChecked = !treeNode.checked;
            var dataUrl = treeNode.name;
            // console.log(dataUrl);
            if (isChecked){
                if (map && (map.getLayer(dataUrl))) {
                    var thisLayer = map.getLayer(dataUrl);
                    thisLayer.show();
                }
            }
            else {
                if (map && (map.getLayer(dataUrl))) {
                    var thisLayer = map.getLayer(dataUrl);
                    thisLayer.hide();
                }
            }
        }
    }
}

//模板制图check事件
function layerOncheck_Template(treeId, treeNode) {
    //如果没有创建图例，先创建图例
    if(!iMLegendCreated){
        require([
            "esri/dijit/Legend",
            "dojo/domReady!"
        ], function(
            Legend
        ) {
            iMLegend = new Legend({
                autoUpdate:true,
                map: map
            }, "iMLegendDiv");
            iMLegend.startup();
            iMLegendCreated = true;
        })
    }
    if(treeNode.isParent){
        return;
    }
    else {
        if(treeNode.getParentNode().id==1){
            //地理底图
            if(treeNode.checked){
                $.each(baseMap,function (i) {
                    if(treeNode.url.indexOf(baseMap[i].url)!=-1){
                        map.removeLayer(baseMap[i]);
                    }

                });
            }else {
                require(["esri/layers/WebTiledLayer","esri/layers/ArcGISDynamicMapServiceLayer","esri/layers/ArcGISTiledMapServiceLayer"],function (WebTiledLayer,ArcGISDynamicMapServiceLayer,ArcGISTiledMapServiceLayer) {
                    var mate=treeNode.getParentNode().children;
                    for (var  i = 0; i < mate.length; i++) {
                        if(mate[i].id==treeNode.id)
                            continue;
                        var node = layerNodesObj.getNodeByTId(mate[i].tId);
                        layerNodesObj.checkNode(node, false, true);
                    }
                   /* map.removeLayer(baseMap);
                    baseMap = new WebTiledLayer(
                        treeNode.url
                    );
                    map.addLayer(baseMap)
                    map.reorderLayer(baseMap,1);*/
                    $.each(baseMap,function (i) {
                        if(treeNode.url.indexOf(baseMap[i].url)!=-1){
                            map.removeLayer(baseMap[i]);
                        }

                    });
                    baseMap=new Array();
                    var urls=treeNode.url.split(",");
                    $.each( urls, function (i) {
                        var url=urls[i];
                        baseMap.push(new ArcGISTiledMapServiceLayer(url,{"id":url}));
                        map.addLayer(baseMap[i]);

                    });
                    $.each(baseMap,function (i) {
                        map.reorderLayer(baseMap[i],i);
                    });
                });

            }
        }
        if(treeNode.getParentNode().id==2){
            //专题服务
            //改为show/hide的显示机制
            if(treeNode.checked){
                if (map && (map.getLayer(treeNode.url))) {//如果已经加载，只是做了隐藏，显示就好了，下面的步骤跳过
                    var thisLayer = map.getLayer(treeNode.url);
                    thisLayer.show();
                    return;
                }
                ServerLayerArr.filter(function (p) {
                    var id=treeNode.name+"_"+(treeNode.url);
                    if(p.id==id){
                        map.getLayer(id).hide();
                    }
                });

            }
            else {
                ServerLayerArr.filter(function (p) {
                    var id=treeNode.name+"_"+(treeNode.url);
                    if(p.id==id){
                        map.addLayer(p);
                    }
                });
            }
        }
        if (treeNode.getParentNode().id===3) {//如果操作的是要素图层
            var isChecked = !treeNode.checked;
            var dataUrl = treeNode.data;
            var lastDataUrl = treeNode.lastUrl;//上一次存储的url。加载时，应先将上一次存储的url代表的要素删去
            var lastThematic = treeNode.thematicData; //上一次存储的专题数据。加载时，应先将上一次存储的专题要素删去
            if(isChecked){//如果被勾选
                // console.log(treeNode.textLayerChecked);
                if (treeNode.textLayerChecked == "checked"){
                    if (dataUrl == "") {//服务地址为空，则返回
                        layui.use('layer', function () {
                            var lay = layui.layer;
                            lay.open({
                                title: '提示'
                                ,content: '请先设置要素图层地址！'
                            });
                        })
                        return;
                    }else{//如果服务地址不为空
                        //先判断上次存储的url代表的图层是否加载，如果加载了，则删去
                        if(map&&(map.getLayer(lastDataUrl))){
                            map.removeLayer(map.getLayer(lastDataUrl));
                        }
                        if(map&&(map.getLayer(dataUrl))){//如果已经加载，只是做了隐藏，显示就好了，下面的步骤跳过
                            var thisLayer = map.getLayer(dataUrl);
                            thisLayer.show();
                            return;
                        }
                        require([
                            "esri/layers/FeatureLayer",
                            "esri/InfoTemplate", "esri/dijit/PopupTemplate", "esri/renderers/SimpleRenderer"
                        ], function (FeatureLayer, InfoTemplate, PopupTemplate, SimpleRenderer) {

                            var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                            var layer = new FeatureLayer(dataUrl, {
                                mode: FeatureLayer.MODE_SNAPSHOT,
                                outFields: ["*"],
                                opacity: "1",
                                infoTemplate: infoTemplate,
                                id: dataUrl
                            });
                            layer.on("load", function(){
                                var simpleJson_line = {
                                    "type": "simple",
                                    "label": treeNode.name,
                                    "description": "",
                                    "symbol":  {
                                        "type": "esriSLS", //SimpleLineSymbol(简单线类型)
                                        "color": [115, 76, 0, 255], //颜色
                                        "width": 2, //线宽
                                        "style": "esriSLSDash" //线形
                                    }
                                };
                                var simpleJson_polygon = {
                                    "type": "simple",
                                    "label": treeNode.name,
                                    "description": "",
                                    "symbol":  {
                                        "type": "esriSFS",
                                        "style": "esriSFSSolid",
                                        "color": [115,76,0,255],
                                        "outline": {
                                            "type": "esriSLS",
                                            "style": "esriSLSSolid",
                                            "color": [110,110,110,255],
                                            "width": 1
                                        }}
                                };
                                var simpleJson_point = {
                                    "type": "simple",
                                    "label": treeNode.name,
                                    "description": "",
                                    "symbol":  {
                                        "type": "esriSMS",
                                        "style": "esriSMSSquare",
                                        "color": [76,115,0,255],
                                        "size": 8,
                                        "angle": 0,
                                        "xoffset": 0,
                                        "yoffset": 0,
                                        "outline":
                                            {
                                                "color": [152,230,0,255],
                                                "width": 1
                                            }}
                                };
                                var rend;
                                require(["esri/renderers/SimpleRenderer"
                                ], function ( SimpleRenderer) {
                                    switch (layer.geometryType)  {
                                        case "esriGeometryPoint":
                                            rend = new SimpleRenderer(simpleJson_point)
                                            break;
                                        case "esriGeometryPolyline":
                                            rend = new SimpleRenderer(simpleJson_line)
                                            break;
                                        case "esriGeometryPolygon":
                                            rend = new SimpleRenderer(simpleJson_polygon)
                                            break;
                                    }
                                    layer.setRenderer(rend);
                                });
                            });
                            layer.on("update", function(evt) {
                                if(evt.target.geometryType!="esriGeometryPoint"){

                                    return;
                                }
                                if(evt.target.renderer.symbol.url.indexOf(".")!=0){
                                    return;
                                }
                                var base64= getBase64Image( evt.target.renderer.symbol.url);
                                evt.target.renderer.symbol.setUrl(base64);
                                evt.target.refresh();
                            });
                            map.addLayer(layer);
                        })
                    }
                }
                else if (treeNode.buttonLayerChecked == "checked"){
                    if (lastThematic){
                        if(map&&(map.getLayer(lastThematic.id))){
                            map.removeLayer(map.getLayer(lastThematic.id));
                        }
                    }
                    beforeThematicLayerAdd("doMapTree_Template", treeNode);
                }
            }else{//如果取消勾选
                //hide当前url的图层
                if (treeNode.textLayerChecked == "checked"){
                    if(map&&(map.getLayer(dataUrl))){
                        var thisLayer = map.getLayer(dataUrl);
                        thisLayer.hide()
                    }
                }
                //hide当前的专题要素图层
                else if (treeNode.buttonLayerChecked == "checked"){
                    var layerNow1 = map.getLayer(treeNode["nodePath"][treeNode["nodePath"].length-1].id);
                    console.log(treeNode["nodePath"][treeNode["nodePath"].length-1]);
                    if(layerNow1)
                        layerNow1.hide();
                }
                return;
            }
        }
        else {
            if (map && (map.getLayer(dataUrl))) {
                var thisLayer = map.getLayer(dataUrl);
                thisLayer.hide();
            }
        }
    }
}

//专题数据check之前回调，用于请求数据
function beforeThematicLayerAdd(treeId, treeNode) {
    if(treeNode.checked){//如果已经加载，则移除图层
        var layerNow = map.getLayer(treeNode["nodePath"][treeNode["nodePath"].length-1].id);
        if(layerNow){
            map.removeLayer(layerNow);
        }
    }else{//如果没有加载，则先移除其他图层，再加载.
        if (treeNode["nodePath"][treeNode["nodePath"].length-1])
            if(map&&(map.getLayer(treeNode["nodePath"][treeNode["nodePath"].length-1].id))){//如果已经加载，只是做了隐藏，显示就好了，下面的步骤跳过
                var thisLayer = map.getLayer(treeNode["nodePath"][treeNode["nodePath"].length-1].id);
                thisLayer.show();
                return;
            }
        // var layerNow = map.getLayer(thematicData.id);
        // console.log(layerNow);
        // if(layerNow){
        //     map.removeLayer(layerNow);
        // }
        var keyId = treeNode["nodePath"][treeNode["nodePath"].length-1].key;
        // console.log(keyId);
        var poiData;
        $.ajax({
            type : "get",
            async: true,
            //url : "http://qk.casm.ac.cn:9090/geowinmap/ds?r=0.41608809004258696&serviceproviderid=map.mapservice&serviceid=circlequery&cl=83.16650390625001,32.30570601389429&ol=84.04541015625001,33.04550781490999&z=7&layerids="+id+"&__conditiontokenid=&requesttype=jsonp&jsonpcallback=?",
            url:"http://qk.casm.ac.cn:9090/geowinmap/ds?r=0.2376504259402532&serviceproviderid=map.mapservice&serviceid=rectquery&cl=100,26&ol=77,36&z=4&layerids="+keyId+"&__conditiontokenid=&requesttype=jsonp&jsonpcallback=?",
            dataType: "jsonp",
            jsonp:"callback", //请求php的参数名
            jsonpCallback: "jsonhandle",//要执行的回调函数
            success : function(data) {
                // console.log(data);
                //requestSucceeded(data);
                poiData = JSON.parse(data);
                // console.log(poiData);
                thematicData.data = poiData;
                thematicData.id = treeNode["nodePath"][treeNode["nodePath"].length-1].id;
                thematicData.name = treeNode["nodePath"][treeNode["nodePath"].length-1].name;
                //将thematicData赋给节点，并将其重置
                treeNode["thematicData"] = thematicData;
                thematicData = [];
                // console.log(treeNode);
                addThematicLayer(treeNode);
            }
        });
    }
}

//专题数据加载，用于加载数据
function addThematicLayer(treeNode) {
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
                "label":treeNode.name,
                "type": "simple",
                "symbol": {
                    "type": "esriPMS",
                    "url": "./assets/img/pointIcon/6.png",
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
    require([
        "esri/layers/FeatureLayer",
        "esri/dijit/PopupTemplate"
    ], function(FeatureLayer,PopupTemplate) {
        map.on("mouse-drag", function(evt) {
            if (map.infoWindow.isShowing) {
                var loc = map.infoWindow.getSelectedFeature().geometry;
                if (!map.extent.contains(loc)) {
                    map.infoWindow.hide();
                }
            }
        });
        var popupTemplate = new PopupTemplate({
            title: "{title}",
            description: "{description}"
        });
        featureLayer = new FeatureLayer(featureCollection, {
            id: treeNode["thematicData"].id,
            infoTemplate: popupTemplate,
            showLabels: true
        });
        featureLayer.on("click", function(evt) {
            map.infoWindow.setFeatures([evt.graphic]);
        });
        featureLayer.on("update", function(evt) {
            if(evt.target.geometryType!="esriGeometryPoint"){

              return;
          }
          if(evt.target.renderer.symbol.url.indexOf(".")!=0){
                return;
            }
           var base64= getBase64Image( evt.target.renderer.symbol.url);
            evt.target.renderer.symbol.setUrl(base64);
            evt.target.refresh();
        });
    });
    map.on("layers-add-result", function(results) {
        var features = [];
        require([
            "esri/graphic", "esri/tasks/FeatureSet","esri/geometry/Point"
        ], function(Graphic,FeatureSet,Point) {
            for(i in treeNode["thematicData"].data){
                var geometry = new Point(treeNode["thematicData"].data[i].point);
                var graphic = new Graphic(geometry);
                var attr = {};
                attr["title"] = treeNode["thematicData"].data[i].name ? treeNode["thematicData"].data[i].name : "无名称";
                var info=""
                for ( j in treeNode["thematicData"].data[i]){
                    info +=j+":"+treeNode["thematicData"].data[i][j]+'<br>'
                    //alert(info)
                }
                attr["description"] = info;

                //attr["description"] = JSON.stringify(poi);
                graphic.setAttributes(attr);
                graphic.attributes.Name=treeNode["thematicData"].data[i].name;
                features.push(graphic);
            }
        });
        featureLayer.applyEdits(features, null, null);
        if(treeNode.featureRenderer){
            var renderer = treeNode.featureRenderer;
            renderer.label = treeNode.name;
            featureLayer.setRenderer(renderer);
            featureLayer.refresh();
        }
    });
    map.addLayers([featureLayer]);
}

//要素图层数据源发生改变的监听
function changeSource(node){
    if(node.value=='text'){
        $("#newFLAds").attr("disabled",false);
        $("#editFLAds").attr("disabled",false);
        $("#selectButton ").attr("disabled",true);
        $("#selectButton ").addClass("layui-btn-disabled");
        return;
    }else if(node.value=='button'){
        $("#selectButton ").attr("disabled",false);
        $("#selectButton ").removeClass("layui-btn-disabled");
        $("#newFLAds").attr("disabled",true);
        $("#editFLAds").attr("disabled",true);
        return;
    }
}

//监听要素服务地址栏有没有发生变化
function addressChange(){
    addressChanged = true;
}

//如果名字发生了变化，则设置为true。每次点击确认之后，从新设置为false
function nameChange(treeNode) {
    nameChanged = true;
}
//图片转为base64
function getBase64Image(imgurl) {
    var img = new Image();
    img.src = imgurl;
    var canvas = document.createElement("canvas");
    canvas.width = img.width;
    canvas.height = img.height;
    var ctx = canvas.getContext("2d");
    ctx.drawImage(img, 0, 0, img.width, img.height);
    var ext = img.src.substring(img.src.lastIndexOf(".")+1).toLowerCase();
    var dataURL = canvas.toDataURL("image/"+ext);
    return dataURL;
}
