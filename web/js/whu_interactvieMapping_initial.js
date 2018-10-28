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
        require(["esri/map"],function (Map) {
            map = new Map("mapContainer", {
                basemap:"osm",
                center: [113.2561395,31.01224452],
                zoom: 7
                //  center:   [120.78484503768885,28.933081922343742],

            });
            mapExtentChange = map.on("zoom-end", function zoomed() {
                var zoomLevel = map.getZoom();
                var zoomLevel = map.getZoom();
            });
        });
      }
});

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

