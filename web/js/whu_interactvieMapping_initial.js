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
var layerNodesObj;
var layerNodes =[
    {id:1, pId:0, name:"地理底图", open:true, "nocheck":true},
    {id:101, pId:1, name:"矢量图",url:"http://cache1.arcgisonline.cn/arcgis/rest/services/ChinaOnlineStreetPurplishBlue/MapServer",checked:true},
    {id:102, pId:1, name:"影像图",url:"http://cache1.arcgisonline.cn/arcgis/rest/services/ChinaOnlineStreetGray/MapServer"},
    {id:103, pId:1, name:"地形图",url:"http://cache1.arcgisonline.cn/arcgis/rest/services/ChinaOnlineCommunityENG/MapServer"},

    {id:2, pId:0, name:"专题服务图层",isParent:true, open:false, "nocheck":true},

    {id:3, pId:0, name:"要素图层", isParent:true,open:false, "nocheck":true},

];
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
        require(["esri/map","esri/layers/WebTiledLayer","esri/layers/ArcGISDynamicMapServiceLayer"],function (Map,WebTiledLayer,ArcGISDynamicMapServiceLayer) {
            map = new Map("mapContainer", {
               // basemap:"osm",
                center: [104,35],
                //zoom: 12
            });
            baseMap = new ArcGISDynamicMapServiceLayer(
                'http://cache1.arcgisonline.cn/arcgis/rest/services/ChinaOnlineStreetPurplishBlue/MapServer'
            );
            map.addLayer(baseMap)
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
            beforeCheck: layerOncheck
        }
    };
    function addHoverDom(treeId, treeNode) {
        var aObj = $("#" + treeNode.tId + "_a");

        if(treeNode.isParent){
            if ($("#doMapAdd_"+treeNode.id).length>0) return;
            var editStr = "<span id='doMapAdd_"+treeNode.id+"' class='button doMapAdd'  onfocus='this.blur();'></span>";
            aObj.append(editStr);


            var btn = $("#doMapAdd_"+treeNode.id);
            if (btn) btn.bind("click", function(){alert("添加" + treeNode.name);});

        }
        else{
            if ($("#doMapEdit_"+treeNode.id).length>0) return;
            var editStr = "<span id='doMapEdit_"+treeNode.id+"' class='button doMapEdit'  onfocus='this.blur();'></span>"+
                "<span id='doMapRemove_"+treeNode.id+"' class='button doMapRemove'  onfocus='this.blur();'></span>";
            aObj.append(editStr);


            var btn = $("#doMapEdit_"+treeNode.id);
            if (btn) btn.bind("click", function(){alert("编辑" + treeNode.name);});
            var btn1 = $("#doMapRemove_"+treeNode.id);
            if (btn1) btn1.bind("click", function(){alert("删除" + treeNode.name);});
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


    layerNodesObj=$.fn.zTree.init($("#doMapTree"), setting, layerNodes);
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

                }
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
        if(treeNode.pId==1){
            //地理底图
            require(["esri/layers/WebTiledLayer","esri/layers/ArcGISDynamicMapServiceLayer"],function (WebTiledLayer,ArcGISDynamicMapServiceLayer) {
                var mate=treeNode.getParentNode().children;
                for (var  i = 0; i < mate.length; i++) {
                    if(mate[i].id==treeNode.id)
                        continue;
                    var node = layerNodesObj.getNodeByTId(mate[i].tId);
                    layerNodesObj.checkNode(node, false, true);

                }


                map.removeLayer(baseMap);
                baseMap = new ArcGISDynamicMapServiceLayer(
                    treeNode.url
                );
                map.addLayer(baseMap)

                map.reorderLayer(baseMap,1);
            });




        }
    }
}
