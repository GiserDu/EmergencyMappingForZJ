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
var ARIndex=0;//行政区目录树表示，0表示为构造，1表示构造
var studyAreaLayer;//制图区域
var ServerLayerArr=[];//专题服务数组
var layerNodesObj;
var layerNodes =[
    {id:1, pId:0, name:"地理底图", open:true, "nocheck":true,children:[
            {id:101, name:"矢量图",url:"http://qk.casm.ac.cn:9090/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=map&y=${row}&x=${col}&z=${level}",checked:true},
            {id:102, name:"影像图",url:"http://qk.casm.ac.cn:9090/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=sate&y=${row}&x=${col}&z=${level}"}
        ]},

    {id:2, pId:0, name:"专题服务图层",isParent:true, open:false,children:[], "nocheck":true},

    {id:3, pId:0, name:"要素图层", isParent:true,open:false,children:[], "nocheck":true},

];
var newCount = 100;
var thematicData={};
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
                'http://qk.casm.ac.cn:9090/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=map&y=${row}&x=${col}&z=${level}',{id:"baseMap"}
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


});
//框选定位
$("#RecNav").click(function () {
    require(["esri/toolbars/draw"],function (Draw) {
        tb= new Draw(map);
        tb.on("draw-end", function(evt){
            map.setExtent(evt.geometry.getExtent());
            tb.deactivate();
            map.showZoomSlider();
        });
        map.disableMapNavigation();
        tb.activate(Draw.EXTENT);    //激活相应的图形
    })
});
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
            yes: function(index, layero) {//确定后执行回调

            }
        });
    });
});
//制图
$("#doMap").click(function () {
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
            if(treeNode.getParentNode().id==1){
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
                                ], function (ArcGISDynamicMapServiceLayer, InfoTemplate, PopupTemplate) {
                                    var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                                    var serviceUrl =$("#newSLAds").val();
                                    var layer = new ArcGISDynamicMapServiceLayer(serviceUrl,{id: $("#newSLName").val()+"_"+$("#newSLAds").val()});
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
                            content: "<div id='zeo'><p style='padding-left: 12px'>要素名称：<input id='newFLName'></input></p><br/><p class='FLS_p'><input id='textLayer' name='layer' value='text' type='radio' onclick='changeSource(this)'/>服务地址：<input id='newFLAds' disabled></input><i class='FLS_i fa fa-cog'></i></p>"
                                // @YH改：
                                + "<br/><p class='tree_p'><input id='buttonLayer' name='layer' value='button' type='radio' onclick='changeSource(this)' />专题服务：<button id='selectButton' class='layui-btn layui-btn-sm layui-btn-disabled' disabled>选择要素</button></p></div>",
                            //----------
                            yes: function(index, layero) {//确定后执行回调
                                if($("#newFLName").val()==""||$("#newFLAds").val()==""){
                                    alert("属性不能为空！");
                                    return;
                                }
                                var textLayerChecked = $("#textLayer").is(":checked")?"checked":"unchecked";
                                var buttonLayerChecked = $("#buttonLayer").is(":checked")?"checked":"unchecked";
                                var textLayerDisabled = $("#newFLAds").prop("disabled")==true?"disabled":"undisabled";
                                var buttonLayerDisabled = $("#selectButton").prop("disabled")==true?"disabled":"undisabled";
                                var newnode={name:$("#newFLName").val(),url:$("#newFLAds").val(),textLayerChecked:textLayerChecked,buttonLayerChecked:buttonLayerChecked,textLayerDisabled:textLayerDisabled,buttonLayerDisabled:buttonLayerDisabled,lastUrl:"0"};
                                layerNodes[2].children.push(newnode);
                                var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                                treeObj.addNodes(treeNode,-1, newnode);
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
                                ServerLayerArr.filter(function (p) {
                                    var id=treeNode.naume+"_"+(treeNode.url);
                                    if(p.id==id){
                                        p.url=$("#editSLAds").val();
                                        p._url.path=$("#editSLAds").val();
                                        //如果地图中已经有这个图层
                                        if($.inArray(id,map.layerIds)!=-1){
                                           // p.refresh();
                                            map.getLayer(id).refresh();
                                            //map.removeLayer(map.getLayer(id));
                                        };
                                    }
                                });
                                treeNode.name=$("#editSLName").val();
                                treeNode.url=$("#editSLAds").val();
                                var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                                treeObj.updateNode(treeNode);

                                layer.close(index);
                            }});
                    });

                }
                if(treeNode.getParentNode().id==3){//如果是要素服务
                    //alert("编辑要素数据");
                    layui.use('layer', function (layui_index) {
                        var layer = layui.layer;
                        layer.open({
                            title: '编辑要素服务',
                            skin: "layui-layer-lan",
                            type: 0,
                            shade: 0,
                            //content:"<div><p>要素名称：<input id='editFLName' value='"+treeNode.name+"'></input></p><br/><p>要素地址：<input id='editFLAds' value='"+treeNode.url+"'></input></p></div>",
                            content: "<div id='zeo'><form><p style='padding-left: 12px'>要素名称：<input id='editFLName' value='"+treeNode.name+"'></input></p><br/><p class='FLS_p'><input id='textLayer' name='layer' value='text' type='radio' onclick='changeSource(this)' "+treeNode.textLayerChecked+"/>服务地址：<input id='editFLAds' "+treeNode.textLayerDisabled+" value='"+treeNode.url+"'></input><i class='FLS_i fa fa-cog'></i></p>"
                             + "<br/><p class='tree_p'><input id='buttonLayer' name='layer' value='button' type='radio' onclick='changeSource(this)' "+treeNode.buttonLayerChecked+"/>专题服务：<button id='selectButton' class='layui-btn layui-btn-sm layui-btn-disabled' "+treeNode.buttonLayerDisabled+">选择要素</button></p><form/></div>",
                            yes: function(index, layero) {//确定后执行回调
                                //  var editnode={name:$("#newSLName").val(),url:$("#newSLAds").val()};
                                if($("#editFLName").val()==""||$("#editFLAds").val()==""){
                                    alert("属性不能为空！");
                                    return;
                                }
                                //存储标签的加载方式
                                var textLayerChecked = $("#textLayer").is(":checked")?"checked":"unchecked";
                                var buttonLayerChecked = $("#buttonLayer").is(":checked")?"checked":"unchecked";
                                var textLayerDisabled = $("#editFLAds").prop("disabled")==true?"disabled":"undisabled";
                                var buttonLayerDisabled = $("#selectButton").prop("disabled")==true?"disabled":"undisabled";
                                treeNode.textLayerChecked = textLayerChecked;
                                treeNode.buttonLayerChecked = buttonLayerChecked;
                                treeNode.textLayerDisabled = textLayerDisabled;
                                treeNode.buttonLayerDisabled = buttonLayerDisabled;
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


                                var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                                treeObj.updateNode(treeNode);
                                //layerNodes=treeObj.transformToArray(treeObj.getNodes());
                                layer.close(index);
                            }});
                    });

                }
                /*var zTree = $.fn.zTree.getZTreeObj("doMapTree");
                zTree.selectNode(treeNode);
                zTree.editName(treeNode);*/
            });
            if(treeNode.getParentNode().id!=1){
                var btn1 = $("#doMapRemove_"+treeNode.id);
                if (btn1) btn1.bind("click", function(){
                    //编辑，根据父节点不同，功能不同
                    if(treeNode.getParentNode().id==2){//如果是专题服务
                        alert("删除专题数据");
                        var index=0;
                        ServerLayerArr.filter(function (p) {
                            var id=treeNode.name+"_"+(treeNode.url);
                            if(p.id==id){
                                ServerLayerArr.splice(index,1);
                                //如果地图中已经有这个图层
                                if($.inArray(id,map.layerIds)!=-1){
                                    // p.refresh();
                                    map.removeLayer(map.getLayer(id));
                                    //map.removeLayer(map.getLayer(id));
                                };
                                var treeObj = $.fn.zTree.getZTreeObj("doMapTree");
                                treeObj.removeNode(treeNode,true);
                                treeNode.getParentNode().isParent=true;
                                treeObj.refresh();
                            }
                            index=index+1;
                        });

                    }
                    if(treeNode.getParentNode().id==3){//如果是专题服务
                        alert("删除要素数据");

                    }
                    //alert("删除" + treeNode.name);
                    /* var zTree = $.fn.zTree.getZTreeObj("doMapTree");
                     zTree.selectNode(treeNode);
                     zTree.removeNode(treeNode,true);*/
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
        var zTree = $.fn.zTree.getZTreeObj("doMapTree");
        zTree.selectNode(treeNode);
        return confirm("确认删除 节点 -- " + treeNode.name + " 吗？");
    }
    if(doMapIndex==0){
        layerNodesObj=$.fn.zTree.init($("#doMapTree"), setting, layerNodes);
        doMapIndex=1;
    }

    layui.use('layer', function (layui_index) {
        var layer = layui.layer;
        layer.open({
            title: '交互制图',
            skin: "layui-layer-lan",
            type: 1,
            shade: 0,
            content:$('#doMapTree'),
            yes: function(index, layero) {//确定后执行回调
            }});
    });
})

//专题目录
$("#featureContent").click(function () {
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
                beforeExpand: function (treeId, treeNode) {

                    $.getJSON("http://qk.casm.ac.cn:9090/ythjzweb/tucengbygl/getleveljson.it?pid=" + treeNode["pid"], function (data2) {
                        // console.log(treeNode);

                        for (var j=0; j<data2.length; j++){
                            data2[j].name = data2[j]["title"];
                            data2[j].isParent = data2[j]["isFolder"];
                            if (data2[j].isFolder){
                                data2[j]["nocheck"] = true;
                            }
                        }
                        console.log(data2);
                        treeNode["children"] = data2;
                        var treeObj = $.fn.zTree.getZTreeObj("treeContent");

                        treeObj.updateNode(treeNode);
                        treeObj.refresh();
                    });
                },
                beforeCheck: beforeThematicLayerAdd
            }
        };

        featureLayerTree = $.fn.zTree.init($("#treeContent"), setting1, zNodes);
        var test = document.getElementById("treeContent");
        console.log(test);

    });
    layui.use('layer', function () {
        var layer1 = layui.layer;
        layer1.open({
            title: '专题图层',
            skin: "layui-layer-lan",
            type: 1,
            shade: 0,
            content: $('#treeContent'),
            yes: function(index, layero) {//确定后执行回调

            }
        });
    });
})

//图层check事件
function layerOncheck(treeId, treeNode) {
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
            var dataUrl = treeNode.url;
            var lastDataUrl = treeNode.lastUrl;//上一次存储的url。加载时，应先将上一次存储的url代表的要素删去
            if(isChecked){//如果被勾选
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
                        map.addLayer(layer);

                    })
                }
            }else{//如果取消勾选
                //hide当前url的图层
                if(map&&(map.getLayer(dataUrl))){
                    var thisLayer = map.getLayer(dataUrl);
                    thisLayer.hide()
                }
                return;
            }
        }
    }
}

//专题数据check之前回调，用于请求数据
function beforeThematicLayerAdd(treeId, treeNode) {
    if(treeNode.checked){//如果已经加载，则移除图层
        var layerNow = map.getLayer(treeNode.id);
        if(layerNow){
            map.removeLayer(layerNow);
        }
    }else{//如果没有加载，则先移除其他图层，再加载
        var layerNow = map.getLayer(thematicData.id);
        if(layerNow){
            map.removeLayer(layerNow);
        }
        var keyId = treeNode.key;
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
                thematicData.data = poiData;
                thematicData.id = treeNode.id;
                thematicData.name = treeNode.name;
                addThematicLayer();
            }
        });
    }
}

//专题数据加载，用于加载数据
function addThematicLayer() {
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
    require([
        "esri/layers/FeatureLayer",
    ], function(FeatureLayer) {
        featureLayer = new FeatureLayer(featureCollection, {
            id: thematicData.id
        });
    });
    map.on("layers-add-result", function(results) {
        var features = [];
        require([
            "esri/graphic", "esri/tasks/FeatureSet","esri/geometry/Point"
        ], function(Graphic,FeatureSet,Point) {
            for(i in thematicData.data){
                var geometry = new Point(thematicData.data[i].point);
                var graphic = new Graphic(geometry);
                features.push(graphic);
            }
        });
        featureLayer.applyEdits(features, null, null);
    });
    map.addLayers([featureLayer]);
}

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

