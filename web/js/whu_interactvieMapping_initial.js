var map;//没有使用var声明的变量，会成为全局对象window的属性,这里仅仅声明map为一个全局变量!
var tb;//toolbar,绘制用
var featureLayerTree;
var baseLayerHB;
var baseLayerURL;//用于进行矢量,影像底图切换时,保证当前行政区划底图的url
var winWidth=0;
var winHeight=0;
var template = {};//
var indi = new Array();//建立所选指标的全局变量,初始情况下indi.length==0
var classIndex;//当前所选分级指标
var classGLayer,chartGLayer;//预设分级图层和统计图层
var regionParam = 0;//regionParam为所选的区域代码(1:初始第一级17个地级市;其他:对应地州市的区域代码)
var rgnName;//当前所选区域名称
var geometry = new Object();//当前所选区域中心点对象
var baseMap;//底图
var doMapIndex=0;//制图目录树表示，0表示为构造，1表示构造
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
var layerNodes =[
    {id:1, pId:0, name:"地理底图", open:true, "nocheck":true,children:[
            {id:101, name:"矢量图",url:"http://qk.casm.ac.cn:9090/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=map&y=${row}&x=${col}&z=${level}",checked:true},
            {id:102, name:"影像图",url:"http://qk.casm.ac.cn:9090/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=sate&y=${row}&x=${col}&z=${level}"}
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
var typeFlag; //记录当前打开的是模板还是空白制图
var layerCloseFlag = 0; //是否选择了模板或新建了空白模板
var templateFlag = 0; //是否打开过模板layer
var blankFlag = 0; //是否打开过空白制图layer
var templateClassNo; //模板layer的id
var blankClassNo; //空白制图layer的id
var thematicData={};
var iMLegend;//iM means interactiveMapping
var iMLegendCreated = false;//图例是否创建？
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
        require(["esri/map","esri/layers/WebTiledLayer","esri/layers/ArcGISDynamicMapServiceLayer","esri/layers/GraphicsLayer"],function (Map,WebTiledLayer,ArcGISDynamicMapServiceLayer,GraphicsLayer) {
            map = new Map("mapContainer", {
                //basemap:"dark-gray-vector",
                center: [104,35],
                zoom: 5
            });
            baseMap = new WebTiledLayer(
                //'http://qk.casm.ac.cn:9090/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=map&y=${row}&x=${col}&z=${level}',{id:"baseMap"}
                'https://${subDomain}.tile.thunderforest.com/cycle/${level}/${col}/${row}.png',{"copyright": 'Maps © <a href="http://www.thunderforest.com">Thunderforest</a>, Data © <a href="http://www.openstreetmap.org/copyright">OpenStreetMap contributors</a>',
                    "id": "OpenCycleMap",
                    "subDomains": ["a", "b", "c"]}
            );
            map.addLayer(baseMap);
            studyAreaLayer=new GraphicsLayer('',{id:"studyAreaLayer",name:"studyAreaLayer"});
            studyAreaLayer.name = "studyAreaLayer";
            map.addLayer(studyAreaLayer);
            mapExtentChange = map.on("zoom-end", function zoomed() {
                var zoomLevel = map.getZoom();
            });
        });
    }
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
            content: 'indexMini.html',
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
                showRenameBtn: false,
                showRemoveBtn: false
            },
            view:{
                addHoverDom: addHoverDom,
                removeHoverDom: removeHoverDom
            },
            callback: {
                beforeCheck: layerOncheck,//勾选前回调，用于加载图层
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

                                        var serviceUrl = $("#newSLAds").val();
                                        var serviceUrlstr=serviceUrl.substring(0,serviceUrl.lastIndexOf("/"));
                                        try{
                                            var layer = new ArcGISDynamicMapServiceLayer(serviceUrlstr,{id:$("#newSLName").val()+"_"+$("#newSLAds").val()});
                                        }catch (e) {
                                            alert("服务地址有误！")
                                            return false;
                                        }
                                        var showindex=serviceUrl.substring(serviceUrl.lastIndexOf("/")+1,serviceUrl.length);
                                        layer.setVisibleLayers([showindex]);

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

                                + "<br/><p class='tree_p'><input id='buttonLayer' name='layer' value='button' type='radio' onclick='changeSource(this)' />专题服务：<button id='selectButton' onclick='openTreeWindow()' class='layui-btn layui-btn-sm layui-btn-disabled' disabled>选择要素</button></p></div>",

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
                    if(treeNode.id==4){
                        layui.use('layer', function(){
                            var layer = layui.layer;
                            layer.open({
                                title: '添加统计图层',
                                skin: "layui-layer-lan",
                                type: 0,
                                shade: 0,
                                content:$('#test'),
                                yes:function(index, layero){
                                    //do something
                                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                                }
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
                                    treeNode.url=$("#editBLAds").val();
                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                                    treeObj.updateNode(treeNode);
                                    layerNodes.filter(function (p) {
                                        if(p.name==treeNode.name){
                                            p.url=treeNode.url;
                                        }
                                    });
                                    baseMap.url=$("#editBLAds").val();
                                    baseMap._url.path=$("#editBLAds").val();
                                    if($.inArray("baseMap",map.layerIds)!=-1){
                                        map.getLayer("baseMap").refresh();
                                    }
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
                                + "<br/><p class='tree_p'><input id='buttonLayer' name='layer' value='button' type='radio' onclick='changeSource(this)' "+treeNode.buttonLayerChecked+"/>专题服务：<button id='selectButton' onclick='openSelectedTree(getThisPath)' class='layui-btn layui-btn-sm' "+treeNode.buttonLayerDisabled+">选择要素</button></p></div>",
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
                });
                if(treeNode.getParentNode().id!=1){
                    var btn1 = $("#doMapRemove_"+treeNode.id);
                    if (btn1) btn1.bind("click", function(){
                        //删除，根据父节点不同，功能不同
                        //删除专题服务图层
                        if(!confirm("确认删除 节点 -- " + treeNode.name + " 吗？")){
                            return;
                        }
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
            closeBtn:0,
            content:$('#doMapTree'),
            yes: function(index, layero) {//确定后执行回调
            }});
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
        console.log(data);

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
                    console.log(nodePath);
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
                console.log(selectedNode);
                console.log(nodePath);
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
    console.log(featureLayerTree);
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
        console.log(data);

        var zNodes1 = data;
        // for (var j=0; j<nodePath.length-1; j++){
        //     buildChildren("treeContent", nodePath[j]);
        // }
        // var treeObj = $.fn.zTree.getZTreeObj("treeContent");
        // console.log(treeObj);
        // nodePath[nodePath.length-1]["checked"] = true;
        console.log(thisNodePath);

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
                        console.log(nodePath);
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
                console.log(selectedNode);
                // console.log(treeNode);
                console.log(thisNodePath[thisNodePath.length-1]);
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
                    for(var i=1; i<layerIndex+1; i++){
                        $("#layui-layer"+ i).css("display", "none");
                    }
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
            //将清将清空graphics的图层加上，因为后面还要标绘
            map.addLayer(polygonFeatureLayer);
            map.addLayer(polylineFeatureLayer);
            map.addLayer(pointFeatureLayer);
            map.addLayer(baseMap);
            addModelLayUI(mapName);
            if (templateFlag == 0)
                templateClassNo = layer.index;
            templateFlag = 1;
            $("#layui-layer"+ (templateClassNo)).css("display", "block");
            // layerIndex = layer.index;
            typeFlag = 0;
            $(".layui-layer-title").text(mapName);
        });

    }
}

//为每个制图模板添加弹出框
function addModelLayUI(mapName) {
    //根据本地存储获取模板
    (function getTemplate() {
        var disaster_status = localStorage.getItem("disaster_status");
        var disaster_type = localStorage.getItem("disaster_type");
        var template_scale = localStorage.getItem("template_scale");
        var template_theme = localStorage.getItem("template_theme");
        var template_map = localStorage.getItem("template_map");
        // $("#mapNameInfo").html(template_map);
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
    //利用template生成树节点
    //生成树
    //编辑节点
    var baseLayer_Model=template.baseLayer
    var featureLayer_Model=template.featureLayer
    var serviceLayer_Model=template.serviceLayer
    var statisticLayer_Model= template.statisticLayer
    //修改底图节点_暂时不用

    var layerNodes_Model=[
        // {id:0, pId:0, name:mapName, open:true, "nocheck":true},
        {id:1, pId:0, name:"地理底图", open:true, "nocheck":true,children:[
                {id:101, name:"矢量图",url:"http://qk.casm.ac.cn:9090/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=map&y=${row}&x=${col}&z=${level}",checked:true},
                {id:102, name:"影像图",url:"http://qk.casm.ac.cn:9090/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=sate&y=${row}&x=${col}&z=${level}"}
            ]},

        {id:2, pId:0, name:"专题服务图层",isParent:true, open:true,children:[], "nocheck":true},

        {id:3, pId:0, name:"要素图层", isParent:true,open:true,children:[], "nocheck":true},
        // {id:4, pId:0, name:"统计图层", isParent:true,open:false,children:[], "nocheck":true},

    ];

    /*增加专题服务图层节点*/
    layerNodes_Model[1].children=serviceLayer_Model.modules;
    //增加要素图层
    layerNodes_Model[2].children=featureLayer_Model.modules;
    // layerNodes_Model[3].children=statisticLayer_Model.modules;

    //添加模板数据标识属性 templateData 或 urlFeatureData
    for (var i=0,l=layerNodes_Model[2].children.length;i<l;i++){
        layerNodes_Model[2].children[i].dataType="templateData"
    }


    doMapping_Template(layerNodes_Model)
    function doMapping_Template(layerNodes_InFunc) {
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
                showRenameBtn: false,
                showRemoveBtn: false
            },
            view:{
                addHoverDom: addHoverDom,
                removeHoverDom: removeHoverDom
            },
            callback: {
                beforeCheck: layerOncheck,
                beforeRemove: beforeRemove,
                beforeRename: beforeRename
            }
        };
        function addHoverDom(treeId, treeNode) {
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
                                        var serviceUrl = $("#newSLAds").val();
                                        var serviceUrlstr=serviceUrl.substring(0,serviceUrl.lastIndexOf("/"));
                                        try{
                                            var layer = new ArcGISDynamicMapServiceLayer(serviceUrlstr,{id:$("#newSLName").val()+"_"+$("#newSLAds").val()});
                                        }catch (e) {
                                            alert("服务地址有误！")
                                            return false;
                                        }
                                        var showindex=serviceUrl.substring(serviceUrl.lastIndexOf("/")+1,serviceUrl.length);
                                        layer.setVisibleLayers([showindex]);

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
                                title: '添加要素服务',
                                skin: "layui-layer-lan",
                                type: 0,
                                shade: 0,
                                // content:"<div><p>要素名称：<input id='newFLName'></input></p><br/><p>要素地址：<input id='newFLAds'></input></p></div>",
                                content: "<div id='zeo'><p style='padding-left: 12px'>要素名称：<input id='newFLName'></input></p><br/><p class='FLS_p'><input id='textLayer' name='layer' value='text' type='radio' onclick='changeSource(this)'/>服务地址：<input id='newFLAds' disabled></input></p>"

                                + "<br/><p class='tree_p'><input id='buttonLayer' name='layer' value='button' type='radio' onclick='changeSource(this)' />专题服务：<button id='selectButton' onclick='openTreeWindow()' class='layui-btn layui-btn-sm layui-btn-disabled' disabled>选择要素</button></p></div>",

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
                                    console.log(newNode["nodePath"]);
                                    layer.close(index);
                                }});
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
                                    treeNode.url=$("#editBLAds").val();
                                    var treeObj = $.fn.zTree.getZTreeObj("doMapTree_Template");
                                    treeObj.updateNode(treeNode);
                                    layerNodes.filter(function (p) {
                                        if(p.name==treeNode.name){
                                            p.url=treeNode.url;
                                        }
                                    });
                                    baseMap.url=$("#editBLAds").val();
                                    baseMap._url.path=$("#editBLAds").val();
                                    if($.inArray("baseMap",map.layerIds)!=-1){
                                        map.getLayer("baseMap").refresh();
                                    }
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
                        console.log(treeNode);
                        console.log(getThisTheme);
                        if (treeNode.textLayerChecked == "checked" && getThisTheme == undefined){
                            getThisTheme = "选择要素";
                            textEditFlag = 1;
                        }
                        console.log(getThisPath);
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
                                + "<br/><p class='tree_p'><input id='buttonLayer' name='layer' value='button' type='radio' onclick='changeSource(this)' "+treeNode.buttonLayerChecked+"/>专题服务：<button id='selectButton' onclick='openSelectedTree(getThisPath)' class='layui-btn layui-btn-sm' "+treeNode.buttonLayerDisabled+">选择要素</button></p></div>",
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

                                    console.log(treeNode);

                                    var nodeIndex = treeNode.getIndex();
                                    if (layerNodes_InFunc[2].children[nodeIndex]) {
                                        layerNodes_InFunc[2].children[nodeIndex].lastUrl = treeNode.url;
                                        treeNode.lastUrl = treeNode.url;
                                        //记录数据源的开源方式
                                        layerNodes_InFunc[2].children[nodeIndex].textLayerChecked = textLayerChecked;
                                        layerNodes_InFunc[2].children[nodeIndex].buttonLayerChecked = buttonLayerChecked;
                                        layerNodes_InFunc[2].children[nodeIndex].textLayerDisabled = textLayerDisabled;
                                        layerNodes_InFunc[2].children[nodeIndex].buttonLayerDisabled = buttonLayerDisabled;
                                        layerNodes_InFunc[2].children[nodeIndex].name = $("#editFLName").val();
                                        layerNodes_InFunc[2].children[nodeIndex].url = $("#editFLAds").val();
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
                                    console.log(treeNode.checked);
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
                                        layerID = treeNode.data;
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
        layerNodesObj_Template=$.fn.zTree.init($("#doMapTree_Template"), setting, layerNodes_InFunc);

        // doMapIndex_Template=1;
        // }

    }

    layui.use('layer', function () {
        var tTreeLayer = layui.layer;
        console.log(mapName);
        tTreeLayer.open({
            title: mapName,
            skin: "layui-layer-lan",
            type: 1,
            shade: 0,
            resize: true,
            maxmin:true,
            closeBtn:0,
            area: ['250', '350px'],
            //btn: ['确定'],
            content: $('#doMapTree_Template'),
            yes: function(index, layero) {//确定后执行回调
                alert("添加响应事件")
            },
            cancel: function(index, layero){
                if(confirm('确定要关闭么,将清除所有操作图层')){ //只有当点击confirm框的确定时，该层才会关闭

                    layer.close(index)
                }
                return false;
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
                    for(var i=1; i<layerIndex+1; i++){
                        $("#layui-layer"+ i).css("display", "none");
                    }
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
            map.removeAllLayers();

            map.addLayer(baseMap);
            //清空之前交互制图中添加的图层节点
            var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
            if (treeObj){
                var nodes = treeObj.getNodesByParam("isParent", true, null);
                console.log(nodes);
                treeObj.removeChildNodes(nodes[1]);
                treeObj.removeChildNodes(nodes[2]);
                treeObj.refresh();
            }

            //$("#doMap").click();

            doMap();
            if (blankFlag == 0)
                blankClassNo = layer.index;
            blankFlag = 1;
            $("#layui-layer"+ (blankClassNo)).css("display", "block");
            typeFlag = 1;
            $(".layui-layer-title").text('交互制图');
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
    console.log(layerIndex);
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
            content: 'indexMini.html',
            yes: function(index, layero) {//确定后执行回调

            }
        });
    });
})

//图层check事件
function layerOncheck(treeId, treeNode) {
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
                map.removeLayer(baseMap);
            }else {
                require(["esri/layers/WebTiledLayer","esri/layers/ArcGISDynamicMapServiceLayer"],function (WebTiledLayer,ArcGISDynamicMapServiceLayer) {
                    var mate=treeNode.getParentNode().children;
                    for (var  i = 0; i < mate.length; i++) {
                        if(mate[i].id==treeNode.id)
                            continue;
                        var node = layerNodesObj.getNodeByTId(mate[i].tId);
                        layerNodesObj.checkNode(node, false, true);
                    }
                    map.removeLayer(baseMap);
                    baseMap = new WebTiledLayer(
                        treeNode.url
                    );
                    map.addLayer(baseMap)
                    map.reorderLayer(baseMap,1);
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


                    var serviceUrl = treeNode.url;
                    var serviceUrlstr=serviceUrl.substring(0,serviceUrl.lastIndexOf("/"));
                    try{
                        var layer = new ArcGISDynamicMapServiceLayer(serviceUrlstr,{id:serviceUrl});
                    }catch (e) {
                        alert("服务地址有误！")
                        return false;
                    }
                    var showindex=serviceUrl.substring(serviceUrl.lastIndexOf("/")+1,serviceUrl.length);
                    layer.setVisibleLayers([showindex]);
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
                map.removeLayer(thisLayer);
            }
        }
        if (treeNode.getParentNode().id===3) {//如果操作的是要素图层
            var dataUrl_template= treeNode.data;
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

                        //var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                        var layer = new FeatureLayer(dataUrl_template, {
                            mode: FeatureLayer.MODE_SNAPSHOT,
                            outFields: ["*"],
                            opacity: "1",
                            //infoTemplate: infoTemplate,
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
                                    "type": "esriSMS",
                                    "style": "esriSMSSquare",
                                    "color": [76, 115, 0, 255],
                                    "size": 8,
                                    "angle": 0,
                                    "xoffset": 0,
                                    "yoffset": 0,
                                    "outline":
                                        {
                                            "color": [152, 230, 0, 255],
                                            "width": 1
                                        }
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
                        map.addLayer(layer);
                    })
                }
                else {
                    console.log(treeNode.textLayerChecked);
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
                            if (dataUrl===lastDataUrl) {
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

                                //var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                                var layer = new FeatureLayer(dataUrl, {
                                    mode: FeatureLayer.MODE_SNAPSHOT,
                                    outFields: ["*"],
                                    opacity: "1",
                                    //infoTemplate: infoTemplate,
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
                                            "type": "esriSMS",
                                            "style": "esriSMSSquare",
                                            "color": [76, 115, 0, 255],
                                            "size": 8,
                                            "angle": 0,
                                            "xoffset": 0,
                                            "yoffset": 0,
                                            "outline":
                                                {
                                                    "color": [152, 230, 0, 255],
                                                    "width": 1
                                                }
                                        }
                                    };
                                    var rend;
                                    require(["esri/renderers/SimpleRenderer"
                                    ], function (SimpleRenderer) {
                                        switch (layer.geometryType) {
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
                        thisLayer.hide()
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
                        console.log(treeNode["nodePath"][treeNode["nodePath"].length - 1]);
                        if (layerNow1)
                            layerNow1.hide();
                    }
                }
                return;
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
                map.removeLayer(baseMap);
            }else {
                require(["esri/layers/WebTiledLayer","esri/layers/ArcGISDynamicMapServiceLayer"],function (WebTiledLayer,ArcGISDynamicMapServiceLayer) {
                    var mate=treeNode.getParentNode().children;
                    for (var  i = 0; i < mate.length; i++) {
                        if(mate[i].id==treeNode.id)
                            continue;
                        var node = layerNodesObj.getNodeByTId(mate[i].tId);
                        layerNodesObj.checkNode(node, false, true);
                    }
                    map.removeLayer(baseMap);
                    baseMap = new WebTiledLayer(
                        treeNode.url
                    );
                    map.addLayer(baseMap)
                    map.reorderLayer(baseMap,1);
                });

            }
        }
        if(treeNode.getParentNode().id==2){
            //专题服务
            if(treeNode.checked){
                ServerLayerArr.filter(function (p) {
                    var id=treeNode.name+"_"+(treeNode.url);
                    if(p.id==id){
                        map.removeLayer(map.getLayer(id));
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
                console.log(treeNode.textLayerChecked);
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

                            //var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                            var layer = new FeatureLayer(dataUrl, {
                                mode: FeatureLayer.MODE_SNAPSHOT,
                                outFields: ["*"],
                                opacity: "1",
                                //infoTemplate: infoTemplate,
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
        console.log(keyId);
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
                console.log(data);
                //requestSucceeded(data);
                poiData = JSON.parse(data);
                console.log(poiData);
                thematicData.data = poiData;
                thematicData.id = treeNode["nodePath"][treeNode["nodePath"].length-1].id;
                thematicData.name = treeNode["nodePath"][treeNode["nodePath"].length-1].name;
                //将thematicData赋给节点，并将其重置
                treeNode["thematicData"] = thematicData;
                thematicData = [];
                console.log(treeNode);
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
