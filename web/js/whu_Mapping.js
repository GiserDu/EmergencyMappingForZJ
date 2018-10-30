var map;//没有使用var声明的变量，会成为全局对象window的属性,这里仅仅声明map为一个全局变量!
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
// var isLogin = window.location.href;
var cityArray = {
    "湖北省":"http://223.75.52.36:26080/arcgis/rest/services/HuBeiShen/MapServer",
    "荆门市":"http://223.75.52.36:26080/arcgis/rest/services/JingMenShi/MapServer",
};
var chartImg_url=undefined;//饼图图例的url
// var charLegendMonitor;//饼图图例的定时器
var legendFlag=0;
var classifyImg_url=undefined;//分级统计图图例的url
// var classifyLegendMonitor;//分级统计图图例的定时器
var mapExtentChange;
//制图区域
var studyAreaLayer;

$(document).ready(function() {
    regionParam=localStorage.getItem("regionParam");


    findDimensions();
    $("#mapContainer").height(winHeight);
    $("#nav-right").height(winHeight);
    initMap();
    function creatSettingspanel1(){
        //创建省级区域表单
        function creatProList(d) {
            var tab = document.getElementById(d);
            var link = document.createElement('div');
            link.setAttribute('class','province');
            var proname="";
            var procode="";
            $.ajax({
                url:"./servlet/GetAdministrativeRegion",
                type:"post",
                async:false,
                data:{type:"pro"},
                success:function (data) {
                    data=data.split("s-p-l");
                    proname=data[0].split(",");
                    procode=data[1].split(",");

                }
            });
            html=[
                '<select id="provinceSelect" class="selectpicker provinceSelect" multiple  data-live-search="true">'
            ];
            for(var name in proname){
                html.push( '<option value="'+procode[name]+'">'+proname[name]+'</option>')
            }
            html.push( '</select>');
            html=html.join('');
            link.innerHTML = html;
            tab.appendChild(link);
            $('#provinceSelect').selectpicker({
                'noneSelectedText': '请选择省份'
            });
        }
//创建市级区域表单
        function creatCityList(d) {
            var tab = document.getElementById(d);
            var link = document.createElement('div');
            link.setAttribute('class','province');
            var proname="";
            var procode="";
            var cityname="";
            var citycode="";

            $.ajax({
                url:"./servlet/GetAdministrativeRegion",
                type:"post",
                async:false,
                data:{type:"pro"},
                success:function (data) {
                    data=data.split("s-p-l");
                    proname=data[0].split(",");
                    procode=data[1].split(",");

                }
            });
            html=[
                '<select id="provinceSelect" class="selectpicker provinceSelect"  data-live-search="true">'
            ];
            for(var name in proname){
                html.push( '<option value="'+procode[name]+'">'+proname[name]+'</option>')
            }
            html.push( '</select>');
            html=html.join('');
            link.innerHTML = html;
            tab.appendChild(link);
            $('#provinceSelect').selectpicker({
                'noneSelectedText': '请选择省份'
            });
            $('#provinceSelect').change(function () {
                $("#citySelect").empty();
                var selectedProcode=$('#provinceSelect').val();
                if (selectedProcode==""){
                    return;
                }
                var cityname="";
                var citycode="";
                $.ajax({
                    url:"./servlet/GetAdministrativeRegion",
                    type:"post",
                    async:false,
                    data:{type:"city",proCode:selectedProcode},
                    success:function (data) {
                        data=data.split("s-p-l");
                        cityname=data[0].split(",");
                        citycode=data[1].split(",");

                    }
                });
                var html=[        ];
                for(var name in cityname){
                    html.push( '<option value="'+citycode[name]+'">'+cityname[name]+'</option>')
                }
                html=html.join('');
                $('#citySelect').append(html);
                $('#citySelect').selectpicker('render');
                $('#citySelect').selectpicker('refresh');
                $('#citySelect').selectpicker();

            });

            var selectedProcode=$('#provinceSelect').val();
            if (selectedProcode==""){
                return;
            }
            $.ajax({
                url:"./servlet/GetAdministrativeRegion",
                type:"post",
                async:false,
                data:{type:"city",proCode:selectedProcode},
                success:function (data) {
                    console.log(data);
                    data=data.split("s-p-l");
                    cityname=data[0].split(",");
                    citycode=data[1].split(",");

                }
            });

            var link = document.createElement('div');
            link.setAttribute('class','city');
            //这个地方要根据选择的省份查询该省份所包含的城市，然后将城市放进一个迭代器，循环添加进变量html
            var html=[
                '<select id="citySelect" class="selectpicker provinceSelect" multiple data-live-search="true">'
            ];
            for(var name in cityname){
                html.push( '<option value="'+citycode[name]+'">'+cityname[name]+'</option>')
            }
            html.push( '</select>');
            html=html.join('');
            link.innerHTML = html;
            tab.appendChild(link);
            $('#citySelect').selectpicker({
                'noneSelectedText': '请选择市'
            });

        }
//创建县级区域表单
        function creatCounList(d) {


            var tab = document.getElementById(d);
            var link = document.createElement('div');
            link.setAttribute('class','province');
            var proname="";
            var procode="";
            var cityname="";
            var citycode="";
            var coutname="";
            var coutcode="";
            $.ajax({
                url:"./servlet/GetAdministrativeRegion",
                type:"post",
                async:false,
                data:{type:"pro"},
                success:function (data) {
                    data=data.split("s-p-l");
                    proname=data[0].split(",");
                    procode=data[1].split(",");

                }
            });
            html=[
                '<select id="provinceSelect" class="selectpicker provinceSelect"  data-live-search="true">'
            ];
            for(var name in proname){
                html.push( '<option value="'+procode[name]+'">'+proname[name]+'</option>')
            }
            html.push( '</select>');
            html=html.join('');
            link.innerHTML = html;
            tab.appendChild(link);
            $('#provinceSelect').selectpicker({
                'noneSelectedText': '请选择省份'
            });
            $('#provinceSelect').change(function () {
                $("#citySelect").empty();
                var selectedProcode=$('#provinceSelect').val();
                if (selectedProcode==""){
                    return;
                }
                var cityname="";
                var citycode="";
                $.ajax({
                    url:"./servlet/GetAdministrativeRegion",
                    type:"post",
                    async:false,
                    data:{type:"city",proCode:selectedProcode},
                    success:function (data) {
                        data=data.split("s-p-l");
                        cityname=data[0].split(",");
                        citycode=data[1].split(",");

                    }
                });
                var html=[        ];
                for(var name in cityname){
                    html.push( '<option value="'+citycode[name]+'">'+cityname[name]+'</option>')
                }
                html=html.join('');
                $('#citySelect').append(html);
                $('#citySelect').selectpicker('render');
                $('#citySelect').selectpicker('refresh');
                $('#citySelect').selectpicker();
                $('#citySelect').change();
            });

            var selectedProcode=$('#provinceSelect').val();
            if (selectedProcode==""){
                return;
            }
            $.ajax({
                url:"./servlet/GetAdministrativeRegion",
                type:"post",
                async:false,
                data:{type:"city",proCode:selectedProcode},
                success:function (data) {
                    console.log(data);
                    data=data.split("s-p-l");
                    cityname=data[0].split(",");
                    citycode=data[1].split(",");

                }
            });

            var link = document.createElement('div');
            link.setAttribute('class','city');
            //这个地方要根据选择的省份查询该省份所包含的城市，然后将城市放进一个迭代器，循环添加进变量html
            var html=[
                '<select id="citySelect" class="selectpicker provinceSelect"  data-live-search="true">'
            ];
            for(var name in cityname){
                html.push( '<option value="'+citycode[name]+'">'+cityname[name]+'</option>')
            }
            html.push( '</select>');
            html=html.join('');
            link.innerHTML = html;
            tab.appendChild(link);
            $('#citySelect').selectpicker({
                'noneSelectedText': '请选择市'
            });
            $('#citySelect').change(function () {
                $("#coutSelect").empty();
                var selectedProcode=$('#provinceSelect').val();
                if (selectedProcode==""){
                    return;
                }
                var selectedCitycode=$('#citySelect').val().trim();
                if (selectedCitycode==""){
                    return;
                }
                var coutname="";
                var coutcode="";
                $.ajax({
                    url:"./servlet/GetAdministrativeRegion",
                    type:"post",
                    async:false,
                    data:{type:"cout",proCode:selectedProcode,cityCode:selectedCitycode},
                    success:function (data) {

                        data=data.split("s-p-l");
                        coutname=data[0].split(",");
                        coutcode=data[1].split(",");

                    }
                });
                //这个地方要根据选择的城市查询该省份所包含的线，然后将县放进一个迭代器，循环添加进变量html
                var html=[
                    '<select id="coutSelect" class="selectpicker provinceSelect" multiple data-live-search="true">'
                ];
                for(var name in coutname){
                    html.push( '<option value="'+coutcode[name]+'">'+coutname[name]+'</option>')
                }
                html.push( '</select>');
                html=html.join('');
                $('#coutSelect').append(html);
                $('#coutSelect').selectpicker('render');
                $('#coutSelect').selectpicker('refresh');
                $('#coutSelect').selectpicker();

            });

            var selectedCitycode=$('#citySelect').val().trim();
            if (selectedCitycode==""){
                return;
            }
            var link = document.createElement('div');
            link.setAttribute('class','county');
            $.ajax({
                url:"./servlet/GetAdministrativeRegion",
                type:"post",
                async:false,
                data:{type:"cout",proCode:selectedProcode,cityCode:selectedCitycode},
                success:function (data) {

                    data=data.split("s-p-l");
                    coutname=data[0].split(",");
                    coutcode=data[1].split(",");

                }
            });
            //这个地方要根据选择的城市查询该省份所包含的线，然后将县放进一个迭代器，循环添加进变量html
            var html=[
                '<select id="coutSelect" class="selectpicker provinceSelect" multiple data-live-search="true">'
            ];
            for(var name in coutname){
                html.push( '<option value="'+coutcode[name]+'">'+coutname[name]+'</option>')
            }
            html.push( '</select>');
            html=html.join('');
            link.innerHTML = html;
            tab.appendChild(link);
            $('#coutSelect').selectpicker({
                'noneSelectedText': '请选择区县'
            });
        }
//获取选定的行政区边界
        //var html = "<ul id='accordion-plot'class='accordion'><li id='plot-point'><div class='link'><i class='fa fa-map'aria-hidden='true'></i>点符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(point)'>点</a></li><li><a href='#'>待完善1</a></li><li><a href='#'>待完善2</a></li></ul></li><li id='plot-polyline'><div class='link'><i class='fa fa-bandcamp'aria-hidden='true'></i>线符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(polyline)'>折线</a></li><li><a href='#'onclick='marking(freehand_polyline)'>自由线</a></li><li><a href='#'>待完善1</a></li><li><a href='#'>待完善2</a></li></ul></li><li id='plot-polygon'><div class='link'><i class='fa fa-pie-chart'aria-hidden='true'></i>面符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(polygon)'>多边形</a></li><li><a href='#'onclick='marking(freehand_polygon)'>自由面</a></li><li><a href='#'onclick='marking(rectangle)'>矩形</a></li><li><a href='#'onclick='marking(circle)'>圆</a></li><li><a href='#'onclick='marking(ellipse)'>椭圆</a></li><li><a href='#'onclick='marking(arrow)'>箭头</a></li><li><a href='#'onclick='marking(triangle)'>三角形</a></li><li><a href='#'>待完善1</a></li></ul></li><li id='plot-tools'><div class='link'><i class='fa fa-paint-brush'aria-hidden='true'></i>工具<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(edit)'>编辑</a></li><li><a href='#' onclick='addMeasureInteraction()'>测量</a></li></ul></li></ul>";
        var html = [

            '<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">',
            '<ul class="layui-tab-title" style="color: white">',
            '<li class="layui-this">行政区</li>',
            '<li>地图选择</li>',
            '<li>矢量文件</li>',
            '</ul>',
            '<div class="layui-tab-content" style="height: auto;">',
            '<div class="layui-tab-item layui-show" id="areaSelectTab"></div>',
            '<div class="layui-tab-item"></div>',
            '<div class="layui-tab-item"></div>',
            '</div>',
            '</div>',

        ].join('');
        var link = document.createElement('div');
        //link.setAttribute('class','settingsPanel');
        link.innerHTML = html;
        document.body.appendChild(link);
        var studyarea=document.getElementById("studyareaSort");
        studyarea.appendChild(link)
        //template.mapScale="区域";
        switch (template.mapScale) {
            //统一mapScale的中文名字，不然不好switch
            case "国家":
                creatProList("studyareaSort");
                break;
            case "省":
                creatCityList("studyareaSort");
                break;
            case "区域":
                creatCounList("studyareaSort");
                break;
            default:
                alert("mapScale名称有误")
                return;
        }


    }
    creatSettingspanel1();
       $('#provinceSelect').change(function(cc){
        var regionCode=$(this).val();
        $.ajax({
            url:"./servlet/GetAdministrativeRegion",
            type:"post",
            async:false,
            //dateType:"json",
            data:{type:"boundary",selectedRegion:"proCode"+"s-p-l"+regionCode},
            success:function (data) {
                //data=data.split("s-p-l")[0].trim();
                data=eval("("+data+")");
                //var gson=regionDecode(data[0]);
                //console.log(gson);

                require(["esri/geometry/Polygon","esri/Color","esri/graphic","esri/layers/GraphicsLayer","esri/symbols/SimpleFillSymbol","esri/symbols/SimpleLineSymbol","esri/layers/FeatureLayer"],function (Polygon,Color,Graphic,GraphicsLayer,SimpleFillSymbol,SimpleLineSymbol,FeatureLayer) {
                    studyAreaLayer.clear();
                    var geometry=new Polygon({"rings":data[0],"spatialReference":{"wkid":4326}});


                    for(var i=1;i<data.length;i++){
                        geometry.addRing(data[i][0]);
                    }
                    var symbol = new SimpleFillSymbol();
                    var graphic = new Graphic();
                    graphic.setGeometry(geometry);
                    symbol.color.a=0.01;
                    graphic.setSymbol(symbol);
                    studyAreaLayer.add(graphic);
                     map.setExtent(geometry.getExtent());
                });




            }
        });}
    );
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
        $("#nav-right").height(winHeight);
    });
    // var sr = esri.SpatialReference(4490);

    //初始化地图
    function initMap() {
        require(["esri/map","js/TDTLayer","js/TDTAnnoLayer","esri/layers/ArcGISDynamicMapServiceLayer","esri/layers/GraphicsLayer","esri/layers/WebTiledLayer","esri/geometry/Point","esri/SpatialReference","esri/geometry/Extent", "dojo/domReady!"],
            function (Map,TDTLayer,TDTAnnoLayer,DynamicMap,GraphicsLayer,WebTiledLayer,Extent,SpatialReference) {
                map = new Map("mapContainer", {
                    basemap:"osm",



                    center: [113.2561395,31.01224452],
                    //  center:   [120.78484503768885,28.933081922343742],
                    zoom: 7,
                    logo : false, //logo
                    nav: false,
                    showAttribution: false,
                    force3DTransforms: true,
                    optimizePanAnimation: true,
                    spatialReference:new SpatialReference({wkid:3857  })
                });//使用map.setBasemap(basemap)可以动态更换底图
                var baseLayer1 = new WebTiledLayer(
                    'http://satellite.casm.ac.cn:8020/geowinmap/ds?serviceproviderid=map.cachedtms&serviceid=gettile&tilename=map&y=${row}&x=${col}&z=${level}'
                );
                // var baseLayer1 = new LocalTiledLayer();
               // map.addLayer(baseLayer1)
                //初始化分级和统计图层
                //map.spatialReference= new SpatialReference({wkid:3857}),
                classGLayer=new GraphicsLayer('',{id:"classGLayer",name:"classGLayer"});
                classGLayer.name = "classGLayer";
                chartGLayer=new GraphicsLayer('',{id:"chartGLayer",name:"chartGLayer"});
                chartGLayer.name = "chartGLayer";

                studyAreaLayer=new GraphicsLayer('',{id:"studyAreaLayer",name:"studyAreaLayer"});
                studyAreaLayer.name = "studyAreaLayer";
                map.addLayer(studyAreaLayer);
                map.addLayer(classGLayer);
                map.addLayer(chartGLayer);
                //加载天地图底图
                /* var baseLayer = new TDTLayer();
                 map.addLayer(baseLayer);*/
                /* var annoLayer = new TDTAnnoLayer();
                 map.addLayer(annoLayer);*/
                /*var baseLayer = new DynamicMap("http://cache1.arcgisonline.cn/arcgis/rest/services/ChinaOnlineCommunity/MapServer");

                map.addLayer(baseLayer);*/
                ;


                //加载湖北省地图服务
                /* baseLayerURL = "http://223.75.52.36:26080/arcgis/rest/services/HuBeiShen/MapServer";
                 baseLayerHB = new DynamicMap(baseLayerURL);
                 map.addLayer(baseLayerHB);
                 baseLayerHB.setOpacity(0.95);

                  map.on("click",function(e) {
                      console.log(e.mapPoint.getLongitude() + "," + e.mapPoint.getLatitude());
                 });*/

                mapExtentChange = map.on("zoom-end", function zoomed() {
                    var zoomLevel = map.getZoom();

                    /*if(regionParam!=1 && zoomLevel<9){
                        url="http://223.75.52.36:26080/arcgis/rest/services/HuBeiShen/MapServer";
                        // map.infoWindow.hide();
                        var center = {x:"112.021395",y:"31.01224452",zoom:"7"};
                        changeMap(url,center,7);
                        regionParam = 1;
                        if(classIndex!=undefined){
                            for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                                if ((map.getLayer(map.graphicsLayerIds[i])).name == "classGLayer") {
                                    var layer = map.getLayer(map.graphicsLayerIds[i]);
                                    map.removeLayer(layer);//清空所有graphics
                                    break;
                                }
                            }
                            if(field_cn){
                                refreshClassLyr();
                            }
                        }
                        refreshChartLyr(indi);
                        map.infoWindow.hide();
                        baseLayerURL = url;
                    }*/
                });
                var initStatisticMap = map.on("extent-change",autoLoad );
                function autoLoad (evt){
                    loadAllLayer();
                    var statisticIndexs=[];
                    var statisticLayermoduless=template.statisticLayer.modules;
                    for(var i=0;i<statisticLayermoduless.length;i++){
                        var indexjson=statisticLayermoduless[i].statisticData.index;
                        var type=statisticLayermoduless[i].type;
                        if(type=="classLayer"){
                            for(var key in indexjson){
                                statisticIndexs.push(key);
                            }
                        }
                        else if(type=="chartLayer"){
                            for(var j=0;j<indexjson.length;j++){
                                for(var key in indexjson[j]){
                                    statisticIndexs.push(key);
                                }
                            }
                        }

                    }
                    autoTriggerLoadStatisticMap(statisticIndexs);

                    //getFeatureLayerExtent();
                    initStatisticMap.remove();

                }


                /*                map.on("click", function(evt){
                                    var extent = getFeatureLayerExtent();
                                    if(extent.spatialReference.wkid!=map.spatialReference.wkid ){
                                        require(["esri/tasks/GeometryService","esri/config"], function(GeometryService,config) {
                                            //配置代理
                                            config.defaults.io.proxyUrl = "../esriproxy/";
                                            config.defaults.io.alwaysUseProxy = false;
                                            var geometryService = new GeometryService(ESRI_GeometyService);
                                            geometryService.project([extent],map.spatialReference, function (p) {
                                                map.setExtent(p[0]);
                                                console.log(p);
                                            });
                                        });
                                    }else {
                                        map.setExtent(extent);
                                    }
                                });*/
                //map.centerAndZoom(new Point( {"x":  107.45535825000002, "y": 31.023230848124996, "spatialReference":4326}),5);
            });

    };

    //地理底图列表点击事件
    $("#baseLayer li").click(function(){
        var baseType = $(this).find('a').text();
        $(this).find('a').css("background",'#11324e');//addClass("fa fa-heart");
        $(this).find('i').addClass("fa fa-heart");
        $(this).siblings().find('a').css("background",'#202832');//removeClass("fa fa-heart")
        $(this).siblings().find('i').removeClass("fa fa-heart");
        if(baseType == "矢量底图"){
            changeVecBaseLayer();
        }else if(baseType == "影像底图"){
            changeImgBaseLayer();
        }else{
            changeTerBaseLayer();//地形地图
        }
    });

    //更换地理底图图层
    function changeVecBaseLayer(){
        /*   require(["esri/map", "js/TDTLayer","js/TDTAnnoLayer","dojo/domReady!"],
               function (Map, TDTLayer,TDTAnnoLayer) {
                   map.removeLayer(map.getLayer(map.layerIds[0]));
                   map.removeLayer(map.getLayer(map.layerIds[0]));
                   var baseLayer = new TDTLayer();
                   map.addLayer(baseLayer);
                   map.reorderLayer(baseLayer,0);
                   var annoLayer = new TDTAnnoLayer();
                   map.addLayer(annoLayer);
                   map.reorderLayer(annoLayer,1);
                   //将baseLayer的参数写入制图模板
               });*/
        map.setBasemap("osm");
    };
    function changeImgBaseLayer(){
        /*require(["esri/map", "js/TDTLayer_img","js/TDTAnnoLayer_img","dojo/domReady!"],
            function (Map, TDTLayer,TDTAnnoLayer) {
                map.removeLayer(map.getLayer(map.layerIds[0]));
                map.removeLayer(map.getLayer(map.layerIds[0]));
                var baseLayer = new TDTLayer();
                map.addLayer(baseLayer);
                map.reorderLayer(baseLayer,0);
                var annoLayer = new TDTAnnoLayer();
                map.addLayer(annoLayer);
                map.reorderLayer(annoLayer,1);
                //将baseLayer的参数写入制图模板
            });*/
        map.setBasemap("satellite");
    };
    function changeTerBaseLayer(){
        /* require(["esri/map", "js/TDTLayer_ter","js/TDTAnnoLayer_ter","dojo/domReady!"],
             function (Map, TDTLayer,TDTAnnoLayer) {
                 map.removeLayer(map.getLayer(map.layerIds[0]));
                 map.removeLayer(map.getLayer(map.layerIds[0]));
                 var baseLayer = new TDTLayer();
                 map.addLayer(baseLayer);
                 map.reorderLayer(baseLayer,0);
                 var annoLayer = new TDTAnnoLayer();
                 map.addLayer(annoLayer);
                 map.reorderLayer(annoLayer,1);
                 //将baseLayer的参数写入制图模板
             });*/
        map.setBasemap("topo");
    };


    // 初始化色带和图表的选择框
    $(".sym-selected").each(function(e){
        var That = $(this);
        // console.log(That);
        That.find(".select_title").on({
            click:function(){
                // console.log(That);
                var state=That.find(".select_content").css("display");
                if (state=="flex"||state=="block"){
                    That.find(".select_content").hide();
                } else if (state=="none"){
                    That.find(".select_content").show();
                }
                // That.find(".select_content").show();
            }
        });

    });

    $(".sym-selected").on('click', '.select_content li', function(event) {
        $(this).parent().parent().find('.select_title').html($(this).html());
        $(this).parent().hide();
    });

    var iconArray=['010101','010102','010103','010104','010105','010106','010107','010108','010109','010111','010201','010202','010203','010204','020101','020102','020103','020104','020202','020203'];
    selecterInit(iconArray);

    var solutionArray=['--请选择--','黄红色系','蓝色色系','红色色系','黄绿色系','黄棕色系','青黄色系'];
    colorSolutionInit(solutionArray);

    var colorArray = {
        "1" : [ "#FFFDB1", "#C9513D" ],
        "2" : [ "#e5f3fc", "#0083BA" ],
        "3" : [ "#fcebf1", "#9E2B65" ],
        "4" : [ "#fffee2", "#00935B" ],
        "5" : [ "#fefdbd", "#ABA71A" ],
        "6" : [ "#7393a2", "#f0c897" ],
        "7" : [ "#eecca6", "#bcca99" ],
        "8" : [ "#FFFEE3", "#9f5f3b" ],
        "9" : [ "#fcebf1", "#6c5394" ],
        "10" : [ "#edf6f1", "#558881" ]
    };
    colorSelecterInit(colorArray);

    function colorSelecterInit(e) {
        $("#color-selected").find('.select_content').html('');
        for ( var k in e) {
            $("#color-selected").find('.select_content').append(
                '<li class="col-md-12"><img  style="width:100%;" color1="'
                + e[k][0] + '" color2="' + e[k][1]
                + '" src="./image/gradeIcon/9/' + k + '.jpg"></li>');
        }
    };

    function colorSolutionInit(e) {
        $("#color-solution").find('.select_content').html('');
        for ( var k=1;k<e.length;k++) {
            $("#color-solution").find('.select_content').append(
                '<li class="col-md-12"><img  style="width:100%;" name="'
                + e[k]
                + '" src="./image/gradeIcon/10/' + k + '.jpg"></li>');
        }
    };

    function selecterInit(e){
        $("#chart-selected").find('.select_content').html('');
        e.forEach(function(u,v,w){
            $("#chart-selected").find('.select_content').append('<li value="'+u+'"><img src="./image/chartIcon/'+u+'.png"></li>');
        })
    };

    // 样式调节的面板
    $('#chartSize').slider({
        formatter: function(value) {
            return '当前值: ' + value;
        }
    });

    $('#chartOpacity').slider({
        formatter: function(value) {
            return '当前值: ' + value;
        }
    });
    $('#classOpacity').slider({
        formatter: function(value) {
            return '当前值: ' + value;
        }
    });

    // 图例按钮的事件
    $("#map-legend").bind({
        mouseover:function () {
            // $("#twoLegend").css("visibility","visible");
            $("#twoLegend").css("width","148px");
            $("#mapContainer .legend-menu").css("width","58px");
            $("#mapContainer .legend-menu").css("border","1px solid rgba(179,209,193,0.6)");
        },
        mouseleave:function () {
            setTimeout(function () {
                if($("#twoLegend").css("width")!="0px"){
                    $("#twoLegend").css("width","0px");
                    $("#mapContainer .legend-menu").css("width","0px");
                    $("#mapContainer .legend-menu").css("border","0px");
                }
            },2500)
        }
    });


    //点击右上角统计图图例按钮时的点击事件
    $("#chartLegend").click(function () {
        if (indi.length==0){
            swal({
                title: "温馨提示",
                text: "您还未选择统计指标",
                type: "info",
                showCancelButton: false,
                confirmButtonText: "确定",
                closeOnConfirm: false,
                closeOnCancel: false
            });
        }else {
            // clearInterval(classifyLegendMonitor);
            legendFlag='chart';
            // $("#mapDiv .legend").remove();
            // $("#mapDiv").append('<img id="legend" class="legend">');
            // $("#mapDiv .legend").attr("src",chartImg_url);

            $("#legend-container .legend").remove();
            $("#legend-remove").css("display","block");
            $("#legend-container").append('<img id="legend" class="legend">');
            $("#legend-container").css("display","block");
            $("#legend-container .legend").attr("src",chartImg_url);


        }
        //图例列表回收
        $("#twoLegend").css("width","0px");
        $("#mapContainer .legend-menu").css("width","0px");
        $("#mapContainer .legend-menu").css("border","0px");

    });


    // 鼠标移入图例后，X按钮变成黑色
    $("#legend-container").mouseover(function () {
        $("#legend-remove").css("color","black");
    })
    // 鼠标移出图例后，X按钮重新变成灰色
    $("#legend-container").mouseout(function () {
        $("#legend-remove").css("color","#F2EFE6");
    })
    //点击图例上的X按钮关闭图例
    $("#legend-remove").click(function () {
        $("#legend-container .legend").remove();
        $("#legend-remove").css("display","none");
        legendFlag = 0;
    });


    //更改制图符号时激活
    $("#chart-selected").on(
        'click',
        '.select_content li',
        function(event) {
            var chartid = $(this).children('img').attr("src");       //获取选中符号的chartid
            $("#chart_select>img").attr("src",chartid);        //选择窗口中显示选中的图表符号
            // console.log(chartid);
            if(indi.length !=0){
                refreshChartLyr(indi);
            }
            // map.on("extent-change", refreshChartLyr);
        }
    );

    //更改色系时激活
    $("#color-solution").on(
        'click',
        '.select_content li',
        function(event) {
            var name = $(this).children('img').attr("name");
            var url = $(this).children('img').attr("src");
            $("#color-solution>.select_title>img").attr("name",name);
            $("#color-solution>.select_title>img").attr("src",url);
            if(indi.length !=0){
                refreshChartLyr(indi);
            }
        }
    );

    //大小、厚度等制图参数的数值改变时激活
    $(".title6 #chartSize").on(
        {
            keyup : function() {
                if(indi.length !=0){
                    refreshChartLyr(indi);
                }
            },
            slideStop:function () {
                if(indi.length !=0){
                    refreshChartLyr(indi);
                }
            }
            // change : function() {
            //     if(indi.length !=0){
            //         refreshChartLyr(indi);
            //     }
            //     alert($("[cata=daxiao]").val());
            // }
        }
    );

    $(".title6 #chartOpacity").on(
        {
            keyup : function() {
                var opacity = $("[cata=chartOpacity]").val()/100.0;
                for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                    if ((map.getLayer(map.graphicsLayerIds[i])).name == "chartGLayer") {
                        var layer = map.getLayer(map.graphicsLayerIds[i]);
                        layer.setOpacity(opacity);
                        layer.redraw();
                        break;
                    }
                }
                // layer.setOpacity(opacity);
                // layer.redraw();
            },
            change : function() {
                var opacity = $("[cata=chartOpacity]").val()/100.0;
                for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                    if ((map.getLayer(map.graphicsLayerIds[i])).name == "chartGLayer") {
                        var layer = map.getLayer(map.graphicsLayerIds[i]);
                        layer.setOpacity(opacity);
                        layer.redraw();
                        break;
                    }
                }
                // layer.setOpacity(opacity);
                // layer.redraw();
            }
        }
    );
    /*函数中声明的所有变量，无论是在哪里声明的，在整个函数中它们都是有定义的。*/
    var mouseMove;
    var mouseOut;
    var mouseClick;
    //具体实现统计符号和图例绘制、加载的函数
    /*注意,这里的refreshChartLyr函数以变量的方式声明,不能加var表示作为window的全局变量,否则无法被data-con.js调用!!!*/
    refreshChartLyr = function(indicators){
        //这里indi为用户当前选取的指标
        indi = indicators;
        if(indi.length ==0){
            return false;
        }
        var WC = map.extent.xmin + "," + map.extent.ymin + "," + map.extent.xmax
            + "," + map.extent.ymax;
        // var WC = "104.99442075,27.1450570,119.179235203,37.87943202";
        var DC = "0,0"+"," + map.width + "," + map.height;
        //获取符号定制参数
        var values = getAllValues();
        // console.log(values);
        var chartID = values[0];
        var color = values[1];
        var size = values[2];
        var opacity = values[3];
        //时间定制测试
        var year = 2016;//时间定制测试
        // var isLabel = $("#isLabel").is(':checked');
        console.log(WC);

        //这里用encodeURI()对中文参数进行默认UTF-8编码,确保IE浏览器传输中文参数不乱码
        var chart_url = "./servlet/chartLayerServlet?wc=" + WC + "&dc=" + DC + "&CHARTID=" + chartID
            + "&WIDTH=" + size + "&CHARTDATA=" + encodeURI(indi) + "&colorRampSchema=" + encodeURI(color)+
            "&regionParam=" +regionParam +"&year=" +year;
        console.log(chart_url);
        $.ajax({
            url: chart_url,
            type: 'GET',
            dataType: 'json',
            cache:false,
            contentType: "charset=utf-8",
            // async:false,//设置为同步操作就可以给全局变量赋值成功
            scriptCharset: 'utf-8',
            success: function (data) {
                // var timeData = data.year;
                // console.log(data);
                chartImg_url= "data:image/png;base64," + data.chartLegend;
                if(legendFlag!=0 && legendFlag == 'chart'){
                    $("#chartLegend").click();                   //触发chartlegend的点击事件
                }
                var dataSource = data.source;
                // $("#mapDiv .legend").css("background", "url(" + img_url + ")");
                // $("#mapDiv .legend").css("background-size", "100% 100%");
                //处理统计图表
                var charts = data.charts;
                var indiNum = charts[0].attributes.indiNum;//所选指标数目
                // console.log(charts);
                var graphics = new Array();
                graphics = initChartLayer(charts);
                // charts.forEach(alert);
                if (map.graphicsLayerIds.length == 0) {
                    //可以为graphicLayer添加mouse-over,click等事件;
                    var graphicLayer = new esri.layers.GraphicsLayer();
                    graphicLayer.name = "chartGLayer";
                    //Graphic(geometry,symbol,attributes,infoTemplate)-->infoTemlate为弹出窗体,用以显示信息
                    for (var i=0;i<graphics.length;i++){
                        graphicLayer.add(graphics[i]);
                    }
                    map.addLayer(graphicLayer);
                } else {
                    var flag = 0;// 用于判断是否有画图图层
                    for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                        if ((map.getLayer(map.graphicsLayerIds[i])).name == "chartGLayer") {
                            var layer = map.getLayer(map.graphicsLayerIds[i]);
                            layer.clear();//清空所有graphics
                            // dojo.disconnect(mouseMove);
                            // dojo.disconnect(mouseOut);
                            for (var i=0;i<graphics.length;i++){
                                layer.add(graphics[i]);
                            }
                            flag = 0;
                        } else// 第一个不是chart图层
                        {
                            flag = 1;
                        }
                    }

                    if (flag == 1)// 现有图层中没有画图图层
                    {
                        var graphicLayer = new esri.layers.GraphicsLayer();
                        graphicLayer.name = "chartGLayer";
                        for (var i=0;i<graphics.length;i++){
                            graphicLayer.add(graphics[i]);
                        }
                        map.addLayer(graphicLayer);
                    }

                }
                //添加鼠标响应事件
                //var chartLayer;
                for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                    if ((map.getLayer(map.graphicsLayerIds[i])).name == "chartGLayer") {
                        chartLayer = map.getLayer(map.graphicsLayerIds[i]);
                        //取消事件绑定
                        if(mouseMove!=undefined && mouseOut!=undefined){
                            dojo.disconnect(mouseMove);                               //dojo：事件监听
                            dojo.disconnect(mouseOut);
                            // dojo.disconnect(mouseClick);
                        }
                        var chartWidth=0;
                        var chartHeight = 0;
                        var chartImg = 0;
                        var rgnCode = 0;
                        mouseMove = dojo.connect(chartLayer, "onMouseOver", function mouseMove(evt) {
                            //这里动态赋予graphicinfoTemplate,如果在生成是就初始化会默认添加鼠标点击事件!!!
                            var g = evt.graphic;
                            if(rgnCode!= g.attributes.rng_code){
                                rgnCode = g.attributes.rng_code;
                                // console.log(g.symbol);
                                chartWidth = g.symbol.width;
                                chartHeight = g.symbol.height;
                                chartImg = g.symbol.url;
                                var symbol = new esri.symbol.PictureMarkerSymbol(chartImg,chartWidth*1.15,chartHeight*1.15);
                                g.setSymbol(symbol);

                                var content = initInfoTemplate(g.attributes,indiNum,dataSource);
                                var title = "统计符号信息";
                                map.infoWindow.setContent(content);
                                map.infoWindow.setTitle(title);
                                map.infoWindow.show(evt.screenPoint,map.getInfoWindowAnchor(evt.screenPoint));
                                map.infoWindow.resize(200,300);

                                map.setMapCursor("pointer");
                            }
                            else {
                                return false;
                            }
                        });
                        mouseOut = dojo.connect(chartLayer, "onMouseOut", function mouseOut(evt) {
                            var g = evt.graphic;
                            var symbol = new esri.symbol.PictureMarkerSymbol(chartImg,chartWidth,chartHeight);
                            // console.log(symbol);
                            g.setSymbol(symbol);
                            map.infoWindow.hide();
                            map.setMapCursor("default");
                            rgnCode = 0;
                        });
                        // mouseClick = dojo.connect(chartLayer, "onClick", function mouseOut(evt) {
                        //     var g = evt.graphic;
                        //     var codeParam = g.attributes.rng_code;
                        //     var rgn_name = g.attributes.rng_name;
                        //     // rgnName = g.attributes.rng_name;
                        //     var url;
                        //     geometry = g.geometry;
                        //     for(i in cityArray){
                        //         if(i==rgn_name){
                        //             url=cityArray[i];
                        //             rgnName = rgn_name;
                        //         }
                        //     }
                        //     if(url===undefined){      //确保url不为空，默认为湖北省全图
                        //         // swal("温馨提示", "您所选择的区域暂无相关数据", "info");
                        //         swal({
                        //             title: "温馨提示",
                        //             text: "您所选择的区域暂无相关数据",
                        //             type: "info",
                        //             showCancelButton: false,
                        //             confirmButtonText: "确定",
                        //             closeOnConfirm: false,
                        //             closeOnCancel: false
                        //         });
                        //     }
                        //     else {
                        //         changeMap(url,geometry,9);
                        //         regionParam = codeParam;
                        //         if(classIndex!=undefined){
                        //             // for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                        //             //     if ((map.getLayer(map.graphicsLayerIds[i])).name == "classGLayer") {
                        //             //         var layer = map.getLayer(map.graphicsLayerIds[i]);
                        //             //         map.removeLayer(layer);//清空所有graphics
                        //             //         break;
                        //             //     }
                        //             // }
                        //             if(field_cn){
                        //                 refreshClassLyr();
                        //             }
                        //         }
                        //         refreshChartLyr(indi);
                        //         map.infoWindow.hide();
                        //         map.setMapCursor("default");
                        //         baseLayerURL = url;
                        //     }
                        // });
                        break;
                    }
                }
            },
            error: function (xhr, status, errMsg) {
                alert('error');
                console.log(errMsg);
            }
        })

    };

    changeMap = function(mapUrl,center,zoom)  {
        require(
            [ "esri/map", "esri/layers/ArcGISDynamicMapServiceLayer",
                "esri/geometry/Extent","esri/geometry/Point","esri/SpatialReference","dojo/domReady!" ],
            function(Map, DynamicMap, Extent,Point) {
                // map.removeAllLayers();
                //map.removeLayer(baseLayerHB);//清除行政区划底图
                //清空统计符号图层
                for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                    if ((map.getLayer(map.graphicsLayerIds[i])).name == "chartGLayer") {
                        var layer = map.getLayer(map.graphicsLayerIds[i]);
                        map.removeLayer(layer);
                        // break;
                    }
                }
                $("#mapContainer .legend").css("background", "url()");                 //清空图例？
                if(classIndex==undefined){
                    baseLayerHB = new DynamicMap(mapUrl);
                    baseLayerHB.setOpacity(0.95);
                }
                if(classIndex==undefined){map.addLayer(baseLayerHB);}
                map.setZoom(zoom);
                // var point = new Point(center.x,center.y,new SpatialReference({wkid:4490}));
                var point = new Point(center.x,center.y);
                map.centerAt(point);

            });
    };

    //动态生成graphic的弹出窗口
    function initInfoTemplate(attributes,indiNum,dataSource) {
        var attrString = '<p><strong>区域名称 : </strong>' + attributes.rng_name + '</p>';
        for (var j=0;j<indiNum;j++){                                //显示选中的各项指标
            var indi = "indi" + j;
            var value = "value" + j;
            //object[]可以用变量进行取值,而object.xxx只能用常量取值
            attrString += '<p><strong>'+attributes[indi]+' : </strong>'+ attributes[value]+ '</p>';
        }
        attrString += '<p><strong>数据来源 : </strong>'+dataSource+'</p>';
        // attrString += '<p>注:单击可进入下一级行政区</p>';
        // attrString += '<img  style="width:150px;height:150px;" src="'+chartImg+'">';
        return attrString;
    }

    //根据后台传输回来的数据进行graphic的生成,并进行ChartLayer的添加
    function initChartLayer (charts) {
        var graphicArray= new Array();
        // var infoTemplateArray= new Array();
        require(["esri/InfoTemplate","esri/geometry/webMercatorUtils"], function(InfoTemplate,webMercatorUtils) {
            for(var i=0;i<charts.length;i++){
                var point = new esri.geometry.Point(
                    charts[i].point_x,
                    charts[i].point_y,
                    new esri.SpatialReference({wkid : 4326})
                    /* wym 修改 将4490 改为4326*/
                );
                // var point_XY = webMercatorUtils.geographicToWebMercator(point);//经纬度转墨卡托
                var width = charts[i].imgWidth;
                var height = charts[i].imgHeight;
                var url = "data:image/png;base64," + charts[i].img;
                var symbol = new esri.symbol.PictureMarkerSymbol(url,width,height);
                var attributes = charts[i].attributes;
                var graphic = new esri.Graphic(point,symbol,attributes);
                // graphic.setInfoTemplate(infoTemplate);
                graphicArray.push(graphic);
            }
        });
        return graphicArray;
    }


    //获取制图的参数(符号,配色,大小,透明度,厚度)
    function getAllValues() {
        //注意:这里的.var()是获取要素的value属性值,而使用attr()则是获取任意所需属性的值
        var chartID = $("#chart_select>img").attr("src").slice(-9,-4);
        // console.log(chartID);
        // var chartID = "10101";
        var color = $("#color-solution>.select_title>img").attr("name");
        var size = $("[cata=chartSize]").val();
        var opacity = $("[cata=chartOpacity]").val();
        var year = $("#year_select").val();
        // var thickness = $("[cata=houdu]").val();
        // var d4 = $("[cata=yuanlv]").val();
        // var d5 = $("[cata=huanlv]").val();
        return ([chartID,color,size,opacity,year])
    }
});

function showSliderDiv() {
    // var oWidth=parseInt($("#mapContainer").css("width"));
    // var contentWidth=parseInt($(".slider-content").css("width"));
    hidePlotDiv();
    $(".slider-content").css("width","280px");
    $("#theme-select").css("display","block");
    // map.centerAndZoom([112.021395,31.01224452], 7);
    field = 0;
    field_cn = 0;
    $("#mapNameInfo").animate({right:"522px"},500);
    $("#info").animate({right:"522px"},500);
    // initVectorMap();
};

function hideSliderDiv() {
    // var oWidth=parseInt($("#mapContainer").css("width"));
    // var contentWidth=parseInt($(".slider-content").css("width"));
    $(".slider-content").css("width", "0px");
    $("#theme-select").css("display", "none");
    // map.centerAndZoom([107.46, 34.92], 4);
    // removeVectorMap();
    $("#mapNameInfo").animate({right:"242px"},500);
    $("#info").animate({right:"242px"},500);
};



