//示例数据，浙江给的
var testData = {
    "type": "FeatureCollection",
    "features": [
        {
            "type": "Feature",
            "geometry": {
                "type": "Point",
                "coordinates": [
                    119.52814,
                    29.879936
                ]
            },
            "properties": {
                "市": "杭州市",
                "隐患点": 781,
                "避让搬迁": 115,
                "工程治理": 218
            }
        },
        {
            "type": "Feature",
            "geometry": {
                "type": "Point",
                "coordinates": [
                    121.565296,
                    29.658292
                ]
            },
            "properties": {
                "市": "宁波市",
                "隐患点": 276,
                "避让搬迁": 43,
                "工程治理": 37
            }
        },
        {
            "type": "Feature",
            "geometry": {
                "type": "Point",
                "coordinates": [
                    120.438594,
                    27.882016
                ]
            },
            "properties": {
                "市": "温州市",
                "隐患点": 1545,
                "避让搬迁": 290,
                "工程治理": 258
            }
        },
        {
            "type": "Feature",
            "geometry": {
                "type": "Point",
                "coordinates": [
                    119.859578,
                    30.780919
                ]
            },
            "properties": {
                "市": "湖州市",
                "隐患点": 160,
                "避让搬迁": 45,
                "工程治理": 62
            }
        },
        {
            "type": "Feature",
            "geometry": {
                "type": "Point",
                "coordinates": [
                    120.556913,
                    29.7612356
                ]
            },
            "properties": {
                "市": "绍兴市",
                "隐患点": 289,
                "避让搬迁": 63,
                "工程治理": 218
            }
        },
        {
            "type": "Feature",
            "geometry": {
                "type": "Point",
                "coordinates": [
                    119.9997208,
                    29.1041079
                ]
            },
            "properties": {
                "市": "金华市",
                "隐患点": 643,
                "避让搬迁": 70,
                "工程治理": 97
            }
        },
        {
            "type": "Feature",
            "geometry": {
                "type": "Point",
                "coordinates": [
                    118.6833012,
                    28.8731117
                ]
            },
            "properties": {
                "市": "衢州市",
                "隐患点": 616,
                "避让搬迁": 141,
                "工程治理": 102
            }
        },
        {
            "type": "Feature",
            "geometry": {
                "type": "Point",
                "coordinates": [
                    122.225178,
                    30.238917
                ]
            },
            "properties": {
                "市": "舟山市",
                "隐患点": 220,
                "避让搬迁": 0,
                "工程治理": 0
            }
        },
        {
            "type": "Feature",
            "geometry": {
                "type": "Point",
                "coordinates": [
                    121.1135732,
                    28.683662
                ]
            },
            "properties": {
                "市": "台州市",
                "隐患点": 330,
                "避让搬迁": 51,
                "工程治理": 141
            }
        },
        {
            "type": "Feature",
            "geometry": {
                "type": "Point",
                "coordinates": [
                    119.5667332,
                    28.1894542
                ]
            },
            "properties": {
                "市": "丽水市",
                "隐患点": 894,
                "避让搬迁": 191,
                "工程治理": 70
            }
        }
    ]
}

var chartOption;

var barOption = [];
//处理传回的统计数据
function processResults(results){
    require([
        "esri/map",
        "esri/geometry/Point",
        "esri/layers/FeatureLayer",
        "esri/layers/ArcGISDynamicMapServiceLayer",
        "esri/symbols/SimpleLineSymbol",
        "esri/symbols/SimpleFillSymbol",
        "esri/renderers/SimpleRenderer",
        "esri/Color",
        'esri/layers/GraphicsLayer',
        'esri/graphic',
        "esri/tasks/query",
        "esri/tasks/QueryTask",
        //添加自定义类型的引用
        "chartModules/ChartInfoWindow",
        "chartModules/geometryUtils",
        "dojo/dom",
        "dojo/dom-geometry",
        "dojo/_base/array",
        "dojo/dom-construct",
        "dojo/_base/window",
        "dojo/domReady!"
    ], function (
        Map, Point,FeatureLayer, ArcGISDynamicMapServiceLayer,
        SimpleLineSymbol, SimpleFillSymbol,
        SimpleRenderer, Color,GraphicsLayer,Graphic,Query,QueryTask,
        ChartInfoWindow,geometryUrils,dom,domGeometry,
        array, domConstruct, win
    ) {
        var chartDiv = [];
        chartOption = [];
        var chartWindow = [];
        console.log(results);
        for(var i=0; i< results.features.length; i++){
            var chartData= null;
            chartData= [];
            if(results.features[i].properties["隐患点"]!= null
                &&results.features[i].properties["避让搬迁"]!= null
                &&results.features[i].properties["工程治理"]!= null){
                var nodeChart = null;
                nodeChart = domConstruct.create("div", {id: 'nodeTest'+ i,class: 'nodeTest', style: "width:80px;height:80px;" }, win.body());
                chartDiv.push(nodeChart);
                var myChart = echarts.init(document.getElementById("nodeTest"+ i));
                //饼状图
                option = {
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} 个"
                    },
                    series : [
                        {
                            name:'生产总值',
                            type:'pie',
                            //radius : '60%',
                            center: ['50%', '50%'],
                            label: {
                                normal: {
                                    show: false,
                                    position: 'inside',
                                    formatter:"{b}:{d}%"
                                }
                            },
                            data:[
                                {value: results.features[i].properties["隐患点"], name:'隐患点'},
                                {value: results.features[i].properties["避让搬迁"], name:'避让搬迁'},
                                {value: results.features[i].properties["工程治理"], name:'工程治理'}
                            ]
                        }
                    ]
                };
                var barOption_ = {
                        tooltip: {
                            show: true
                        },
                        grid: {
                            top:'2%',
                            left: '2%',
                            right: '2%',
                            bottom: '3%',
                            //containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data : ["隐患点","避让搬迁","工程治理"]
                            }
                        ],
                        yAxis : [
                            {
                                show:false,
                                type : 'value'
                            }
                        ],
                        series : [
                            {
                                "name":"总量",
                                "type":"bar",
                                data:[
                                    {value: results.features[i].properties["隐患点"], name:'隐患点'},
                                    {value: results.features[i].properties["避让搬迁"], name:'避让搬迁'},
                                    {value: results.features[i].properties["工程治理"], name:'工程治理'}
                                ]
                            }
                        ]
                }
                myChart.setOption(option);
                chartOption.push(myChart);
                barOption.push(barOption_);
                var chartPoint= null;
                //chartPoint= geometryUrils.getPolygonCenterPoint(results.features[i].geometry);
                chartPoint= new Point(results.features[i].geometry.coordinates);
                var screenPoint = map.toScreen(chartPoint);
                //screenPoint.y = screenPoint.y+75;
                var mapPoint = map.toMap(screenPoint);

                var tempChart =  new ChartInfoWindow({
                    map: map,
                    chart: nodeChart,
                    chartPoint: mapPoint
                });
                chartWindow.push(tempChart);
            }
        }
    })
}

//创建更改符号的按钮
function createStyleButton(){
    var buttonGroup = $("#toolsMenu");
    buttonGroup.empty();
    var html = "       <div class=\"btn-group\">\n" +
        "        <div class=\"btn-group\">\n" +
        "            <button type=\"button\" class=\"btn btn-default dropdown-toggle\"\n" +
        "                    data-toggle=\"dropdown\">\n" +
        "                定位 <span class=\"caret\"></span>\n" +
        "            </button>\n" +
        "            <ul class=\"dropdown-menu\" role=\"menu\">\n" +
        "                <li>\n" +
        "                    <a id=\"adminNav\" href=\"#\">行政区</a>\n" +
        "                </li>\n" +
        "                <li>\n" +
        "                    <a id=\"RecNav\" href=\"#\">框选</a>\n" +
        "                </li>\n" +
        "                <li>\n" +
        "                    <a id=\"cancelSelect\" href=\"#\">清除</a>\n" +
        "                </li>\n" +
        "            </ul>\n" +
        "        </div>\n" +
        "        <div class=\"btn-group\">\n" +
        "            <button type=\"button\" class=\"btn btn-default dropdown-toggle\"\n" +
        "                    data-toggle=\"dropdown\">\n" +
        "                标绘 <span class=\"caret\"></span>\n" +
        "            </button>\n" +
        "            <ul class=\"dropdown-menu\" role=\"menu\">\n" +
        "                <li ><a class=\"fa  fa-map-pin\" href=\"#\" id=\"pointPlot\" onclick=\"marking(point)\">&nbsp&nbsp点</a></li>\n" +
        "                <li class=\"divider\"></li>\n" +
        "                <li><a class=\"fa fa-ellipsis-h\" href=\"#\" id=\"polylinePlot\" onclick=\"marking(polyline)\">&nbsp&nbsp折线</a></li>\n" +
        "                <li><a class=\"fa fa-ellipsis-h\" href=\"#\" id=\"freeHandPolylinePlot\" onclick=\"marking(freehand_polyline)\">&nbsp&nbsp自由线</a></li>\n" +
        "                <li class=\"divider\"></li>\n" +
        "                <li><a class=\"fa fa-map-o\" href=\"#\" id=\"polygonPlot\" onclick=\"marking(polygon )\">&nbsp&nbsp多边形</a></li>\n" +
        "                <li><a class=\"fa fa-map-o\" href=\"#\" id=\"rectanglePlot\" onclick=\"marking(rectangle)\">&nbsp&nbsp矩形</a></li>\n" +
        "                <li><a class=\"fa fa-map-o\" href=\"#\" id=\"arrowPlot\" onclick=\"marking(arrow)\">&nbsp&nbsp箭头</a></li>\n" +
        "                <li><a class=\"fa fa-map-o\" href=\"#\" id=\"trianglePlot\" onclick=\"marking(triangle)\">&nbsp&nbsp三角形</a></li>\n" +
        "                <li><a class=\"fa fa-map-o\" href=\"#\" id=\"circlePlot\" onclick=\"marking(circle)\">&nbsp&nbsp圆形</a></li>\n" +
        "                <li><a class=\"fa fa-map-o\" href=\"#\" id=\"ellipsePlot\" onclick=\"marking(ellipse)\">&nbsp&nbsp椭圆形</a></li>\n" +
        "                <li><a class=\"fa fa-map-o\" href=\"#\" id=\"freeHandPolygonPlot\" onclick=\"marking(freehand_polygon)\">&nbsp&nbsp自由面</a></li>\n" +
        "                <li class=\"divider\"></li>\n" +
        "                <li><a class=\"fa fa-edit\" href=\"#\" id=\"editPlot\" onclick=\"marking(edit)\">&nbsp&nbsp编辑</a></li>\n" +
        "                <li><a class=\"fa fa-tags\" href=\"#\" id=\"measurePlot\" onclick=\"addMeasureInteraction()\">&nbsp&nbsp测量</a></li>\n" +
        "            </ul>\n" +
        "        </div>\n" +
        "            <button id=\"templateMap\" type=\"button\" class=\"btn btn-default\">制图模板</button>\n" +
        "        </div>\n" +
        "       </div>\n" +
        "            <button id=\"styleButton\" type=\"button\" class=\"btn btn-default\">更改符号样式</button>\n" +
        "        </div>" +
        "        <a id=\"backHome\" href=\"index.html\" type=\"button\" class=\"btn btn-default  fa  fa-home\"></a>"
    buttonGroup.html(html);
    $("#styleButton").click(function(){
        changeSymbolStyle();
    });
}

//更改统计符号样式
function changeSymbolStyle() {
    for(var i = 0;i<chartOption.length;i++){
        chartOption[i].setOption(barOption[i])
    }
}
createStyleButton();