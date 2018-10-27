/**
 * Created by Administrator on 2017/10/30.
 */
function getClassValues() {
    //获取分级统计的各参数
    var startColor=$("#color-selected .select_title img").attr('color1');//起始颜色
    var endColor=$("#color-selected .select_title img").attr('color2');//结束颜色
    // var field=$("#theme").val();//分级字段(占比或系数等无量纲指标)
    var breakNum=$("#changeQuantity").val();//分级数量(最好固定在3-7级!!!)
    var breakMethod = $("#model").val();//分级方法
    // classIndex = field;
    var colors = getColorbar(breakNum,startColor,endColor);
    var colorString = "";
    for (var i=0;i<colors.length;i++){
        colorString += colors[i];
        colorString += ";";
    }
    colorString = colorString.substring(0, colorString.length - 1);
    var year = $("#year_select").val();

    return ([colorString,breakNum,breakMethod,year])
}

/**
 * 处理分级数量的增加和减少
 */
// $(".title6 #daxiao").on(
//     {
//         keyup : function() {
//             if(indi.length !=0){
//                 refreshChartLyr(indi);
//             }
//         },
//         change : function() {
//             if(indi.length !=0){
//                 refreshChartLyr(indi);
//             }
//         }
//     }
// );
//控制分级数目在3-7
$(".quantity-form #changeQuantity").on(
    {
        change : function() {
            var pernum = $("#changeQuantity").val();
            // var clickid = $(this).attr("id");

            if(pernum>7) {
                $(this).val(7);
                swal({
                    title: "提示",
                    text: "您选择的分级数量过大!",
                    type: "info",
                    showCancelButton: false,
                    confirmButtonText: "确定",
                    closeOnConfirm: false,
                    closeOnCancel: false
                });
            }
            else if(pernum<3){
                $(this).val(3);
                swal({
                    title: "提示",
                    text: "您选择的分级数量过小!",
                    type: "info",
                    showCancelButton: false,
                    confirmButtonText: "确定",
                    closeOnConfirm: false,
                    closeOnCancel: false
                });
            }
            // else {
            //     return;
            // }
            if(indi.length==0 && field_cn==0){
                swal({
                    title: "指标为空",
                    text: "请您先选择专题指标!",
                    type: "info",
                    showCancelButton: false,
                    confirmButtonText: "确定",
                    closeOnConfirm: false,
                    closeOnCancel: false
                });
            }
            else{
                // console.log($(this).val());
                refreshClassLyr(field,field_cn,table);
            }
        }
    });

// $("#apply").click(function(e) {
//     if(field_cn){
//         refreshClassLyr(field,field_cn,table);
//     }
//     else {
//         swal({
//             title: "温馨提示",
//             text: "请您先选择要查看的分级指标",
//             type: "info",
//             showCancelButton: false,
//             confirmButtonText: "确定",
//             closeOnConfirm: false,
//             closeOnCancel: false
//         });
//     }
// });

var mouseMoveClassLyr;
var mouseOutClassLyr;
var mouseClickClassLyr;

refreshClassLyr = function() {
    var values = getClassValues();
    // console.log(values);
    //颜色为十六进制,开头为#,需要使用encodeURIComponent转码
    // var startColor = values[0];
    // var endColor = values[1];
    var colors = values[0];
    // var field = values[1];
    // var field = "男性比例";//先用来测试
    // var field = field;
    classIndex = field_cn;
    var breakNum = values[1];
    var breakMethod = values[2];
    var year = 2016;//时间定制测试

    var class_url = "./servlet/ClassLayerServlet?colors=" + encodeURIComponent(colors) + "&field=" +
        field + "&breakNum=" + breakNum + "&breakMethod=" + encodeURIComponent(breakMethod) + "&regionParam=" +regionParam+
        "&table=" +encodeURIComponent(table) + "&year=" +year + "&field_cn=" + encodeURIComponent(field_cn);
    $.ajax({
        url: class_url,
        type: 'GET',
        dataType: 'json',
        cache:false,
        // async:false,//设置为同步操作就可以给全局变量赋值成功
        scriptCharset: 'utf-8',
        success: function (data) {
            console.log(data);
            var classLegend = data.classLegend;
            classifyImg_url = "data:image/png;base64," + classLegend;
            if(legendFlag!=0 &&legendFlag == 'classify'){
                $("#classifyLegend").click();                    //触发classifylegend的点击事件
            }
            var dataSource = data.dataSource;
            // console.log(data.dataClassArray);
            var classGraphics = initClassLayer(data.classDataArray);
            if (map.graphicsLayerIds.length == 0) {
                //可以为graphicLayer添加mouse-over,click等事件;
                //map.removeLayer(baseLayerHB);
                var graphicLayer = new esri.layers.GraphicsLayer();
                graphicLayer.name = "classGLayer";
                //Graphic(geometry,symbol,attributes,infoTemplate)-->infoTemlate为弹出窗体,用以显示信息
                for (var i=0;i<classGraphics.length;i++){
                    graphicLayer.add(classGraphics[i]);
                }
                map.addLayer(graphicLayer);
                graphicLayer.setOpacity(0.95);
            }
            else {
                //map.removeLayer(baseLayerHB);
                var flag = 0;
                for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                    if ((map.getLayer(map.graphicsLayerIds[i])).name == "chartGLayer") {
                        var layer = map.getLayer(map.graphicsLayerIds[i]);
                        layer.clear();//清空所有graphics
                       // map.removeLayer(layer);
                        break;
                    }
                }
                for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                    if ((map.getLayer(map.graphicsLayerIds[i])).name == "classGLayer") {
                        var layer = map.getLayer(map.graphicsLayerIds[i]);
                        layer.clear();//清空所有graphics
                        for (var i=0;i<classGraphics.length;i++){
                            layer.add(classGraphics[i]);
                        }
                        flag = 1;
                        layer.setOpacity(0.95);
                        break;
                    }
                }
                if(flag==0){
                    var graphicLayer = new esri.layers.GraphicsLayer();
                    graphicLayer.name = "classGLayer";
                    //Graphic(geometry,symbol,attributes,infoTemplate)-->infoTemlate为弹出窗体,用以显示信息
                    for (var i=0;i<classGraphics.length;i++){
                        graphicLayer.add(classGraphics[i]);
                    }
                    map.addLayer(graphicLayer);
                    graphicLayer.setOpacity(0.95);
                }
                // map.removeLayer(baseLayerHB);
                // graphicLayer.setOpacity(0.9);
                refreshChartLyr(indi);
            }

            //添加鼠标响应事件
            var classLayer;
            for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                if ((map.getLayer(map.graphicsLayerIds[i])).name == "classGLayer") {
                    classLayer = map.getLayer(map.graphicsLayerIds[i]);
                    //取消事件绑定
                    if(mouseMoveClassLyr!=undefined && mouseOutClassLyr!=undefined){
                        dojo.disconnect(mouseMoveClassLyr);
                        dojo.disconnect(mouseOutClassLyr);
                        // dojo.disconnect(mouseClickClassLyr);
                    }
                    var rgnCode = 0;
                    var polyColor;
                    var polyOutline;
                    var polyStyle;
                    mouseMoveClassLyr = dojo.connect(classLayer, "onMouseOver", function mouseMove(evt) {
                        //这里动态赋予graphicinfoTemplate,如果在生成是就初始化会默认添加鼠标点击事件!!!
                        var g = evt.graphic;
                        if(rgnCode!= g.attributes.rng_code){
                            rgnCode = g.attributes.rng_code;
                            // console.log(g.symbol);
                            polyColor = g.symbol.color;
                            polyOutline = g.symbol.outline;
                            polyStyle = g.symbol.style;
                            var outline = new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID,new esri.Color([92,68,187]),3);
                            var symbol = new esri.symbol.SimpleFillSymbol(polyStyle,outline,polyColor);
                            g.setSymbol(symbol);

                            var content = initClassInfoTemplate(g.attributes,dataSource);
                            var title = g.attributes.rgn_name;
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
                    mouseOutClassLyr = dojo.connect(classLayer, "onMouseOut", function mouseOut(evt) {
                        var g = evt.graphic;
                        var symbol = new esri.symbol.SimpleFillSymbol(polyStyle,polyOutline,polyColor);
                        // console.log(symbol);
                        g.setSymbol(symbol);
                        map.infoWindow.hide();
                        map.setMapCursor("default");
                        rgnCode = 0;
                    });
                    // mouseClickClassLyr = dojo.connect(classLayer, "onClick", function mouseOut(evt) {
                    //     var g = evt.graphic;
                    //     var codeParam = g.attributes.rgn_code;
                    //     var rgn_name = g.attributes.rgn_name;
                    //     // rgnName = g.attributes.rgn_name;
                    //     var url;
                    //     // var geometry = new Object();
                    //     geometry.x = g.attributes.centerX;
                    //     geometry.y = g.attributes.centerY;
                    //
                    //     for(i in cityArray){
                    //         if(i==rgn_name){
                    //             url=cityArray[i];
                    //             rgnName = rgn_name;
                    //         }
                    //     }
                    //     if(url===undefined){      //确保url不为空，默认为湖北省全图
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
                    //         // for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                    //         //     if ((map.getLayer(map.graphicsLayerIds[i])).name == "classGLayer") {
                    //         //         var layer = map.getLayer(map.graphicsLayerIds[i]);
                    //         //         map.removeLayer(layer);//清空所有graphics
                    //         //         break;
                    //         //     }
                    //         // }
                    //         refreshClassLyr(field,field_cn,table);
                    //         if(indi.length !=0){
                    //             refreshChartLyr(indi);
                    //         }
                    //         map.infoWindow.hide();
                    //         map.setMapCursor("default");
                    //         baseLayerURL = url;
                    //     }
                    // });
                    break;
                }
            }
            // return data;
        },
        error: function (xhr, status, errMsg) {
            console.log(errMsg);
        }
    })
};

//根据后台传输回来的数据进行面状graphic的生成,并进行ClassLayer的添加
function initClassLayer (classGraphics) {
    var graphicArray= new Array();
    // var infoTemplateArray= new Array();
    require(["esri/Color","esri/symbols/SimpleFillSymbol","esri/symbols/SimpleLineSymbol","esri/geometry/webMercatorUtils"],
        function(Color,SimpleFillSymbol,SimpleLineSymbol,webMercatorUtils) {
        for(var i=0;i<classGraphics.length;i++){
            //var color = Color.fromRgb("rgb(202,0,19)")可以直接用传输过来的字符串构造:FillSymbol(color,outline,type)
            var polygon = new esri.geometry.Polygon(classGraphics[i].geometry);
            var polygonXY = webMercatorUtils.geographicToWebMercator(polygon); //经纬度转墨卡托
            var color = new Color.fromRgb(classGraphics[i].color);
            var outline = new SimpleLineSymbol(SimpleFillSymbol.STYLE_SOLID,new Color([255,245,238]),1);
            var symbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID,outline,color);
            var attributes = classGraphics[i].attributes;
            var graphic = new esri.Graphic(polygonXY,symbol,attributes);
            // graphic.setInfoTemplate(infoTemplate);
            graphicArray.push(graphic);
        }
    });
    return graphicArray;
}

//动态生成graphic的弹出窗口
function initClassInfoTemplate(attributes,dataSource) {
    // var attrString = classIndex + ":" + attributes.data;
    var attrString = '<p><strong>'+classIndex+' : </strong>' + attributes.data + '</p>';
    attrString += '<p><strong>分级级别 : </strong>' + attributes.rgn_class + '</p>';
    attrString += '<p><strong>数据来源 : </strong>' + dataSource + '</p>';
    // classifyImg_url = "data:image/png;base64," + classLegend;
    // attrString += '<img src="'+url+'">';
    // var domObj = $("#color-selected .select_title img")[0];
    // var attrString = classIndex + ":" + attributes.data + "<br/>";
    // attrString += "注:单击可进入下一级行政区";
    // attrString.substring(0,attrString.length-5);
    return attrString;
}

$("#color-selected").on(
    'click',
    '.select_content li',
    function(event) {
        // var name = $(this).children('img').attr("name");
        var url = $(this).children('img').attr("src");
        var startColor = $(this).children('img').attr("color1");
        var endColor = $(this).children('img').attr("color2");
        // $("#color-selected>.select_title>img").attr("name",name);
        $("#color-selected>.select_title>img").attr("src",url);
        $("#color-selected>.select_title>img").attr("color1",startColor);
        $("#color-selected>.select_title>img").attr("color2",endColor);
        // console.log(name);
        if(field !=0 ){
            refreshClassLyr(field,field_cn,table);
        }
    }
);

$("#model").on(
    {
        change: function () {
            if(indi.length==0 && field_cn==0){
                swal({
                    title: "指标为空",
                    text: "请您先选择专题指标!",
                    type: "info",
                    showCancelButton: false,
                    confirmButtonText: "确定",
                    closeOnConfirm: false,
                    closeOnCancel: false
                });
            }
            else{
                // console.log($(this).val());
                refreshClassLyr(field,field_cn,table);
            }

        }
    }
);

$(".title6 #classOpacity").on(
    {
        keyup : function() {
            var opacity = $("[cata=classOpacity]").val()/100.0;
            for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                if ((map.getLayer(map.graphicsLayerIds[i])).name == "classGLayer") {
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
            var opacity = $("[cata=classOpacity]").val()/100.0;
            for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                if ((map.getLayer(map.graphicsLayerIds[i])).name == "classGLayer") {
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


/**
 * 自定义颜色选择时，重新设置颜色预览
 */
function colorPreview(){
    $("#colorpreview").html('');
    var gradeNum = $("#gradenuminput").val();
    var colors = getColorbar(gradeNum,$("#choosestartcolor").val(),$("#chooseendcolor").val());
    var divw = 100/gradeNum;

    var app='';
    for(var i=0;i<colors.length;i++){
        var ap = "<div style='background-color:"+colors[i]+";width:"+divw+"%;'></div>";
        app=app+ap;
    }
    $("#colorpreview").append(app);
}

/**
 * 重新计算颜色的函数
 */
//输出为RGBA格式
function getColorbar(customNum,colorLow,colorHigh){
    var rl = parseInt(colorLow.substr(1,2),16);
    var gl = parseInt(colorLow.substr(3,2),16);
    var bl = parseInt(colorLow.substr(5,2),16);
    var rh = parseInt(colorHigh.substr(1,2),16);
    var gh = parseInt(colorHigh.substr(3,2),16);
    var bh = parseInt(colorHigh.substr(5,2),16);

    var rmax = (rl+rh)>255?255:(rl+rh);
    var gmax = (gl+gh)>255?255:(gl+gh);
    var bmax = (bl+bh)>255?255:(bl+bh);
    var colors = [];
    for(var i=0;i<customNum;i++){
        var r = rl + (2*rmax-rl-rh)*i/(customNum<2?1:(customNum-1));
        var g = gl + (2*gmax-gl-gh)*i/(customNum<2?1:(customNum-1));
        var b = bl + (2*bmax-bl-bh)*i/(customNum<2?1:(customNum-1));
        colors.push("rgba("+parseInt(r>rmax?2*rmax-r:r)+", "+parseInt(g>gmax?2*gmax-g:g)+", "+parseInt(b>bmax?2*bmax-b:b)+","+1+")")
    }
    return colors;
}


//右上角分级统计图图例的按钮点击事件
$("#classifyLegend").click(function () {
    if (field_cn==0){
        swal({
            title: "温馨提示",
            text: "您还未选择分级指标",
            type: "info",
            showCancelButton: false,
            confirmButtonText: "确定",
            closeOnConfirm: false,
            closeOnCancel: false
        });
    }else {
        // clearInterval(charLegendMonitor);
        legendFlag='classify';
        $("#legend-container .legend").remove();

        // $("#mapDiv .legend").remove();

        // $("#mapDiv").append('<div id="legend" class="legend"></div>');

        // $("#mapDiv").append('<img id="legend" class="legend">');

        $("#legend-container").append('<img id="legend" class="legend">');
        // classifyLegendMonitor=setInterval(function () {
        //     // $("#mapDiv .legend").css("background", "url(" + classifyImg_url + ")");
        //     // $("#mapDiv .legend").css("background-size", "100% 100%");
        //     $("#mapDiv .legend").attr("src", classifyImg_url);
        // },400);
        $("#legend-container").css("display","block");
        $("#legend-container .legend").attr("src", classifyImg_url);

        // $("#mapDiv .legend").attr("src", classifyImg_url);
    }
    //图例列表回收
    $("#twoLegend").css("width","0px");
    $("#legend-remove").css("display","block");
    $("#mapContainer .legend-menu").css("width","0px");
    $("#mapContainer .legend-menu").css("border","0px");

})
