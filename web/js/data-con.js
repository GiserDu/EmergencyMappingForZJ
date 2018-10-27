/**
 * Created by Administrator on 2017/10/5.
 */

//isGraded是判断是否是分级指标的字段
// var fieldArray=[
//     {"tablename":"人口与就业","fields":["常住人口","户籍人口","男性人口","女性人口"],"isGraded":["0","0","0","0"]},
//     {"tablename":"水系覆盖","fields":["水系覆盖1-1","水域面积","水域结构"],"isGraded":["1","1","1"]},
//     {"tablename":"土地利用","fields":["土壤配比1-1","土壤面积","土壤结构","土地覆盖"],"isGraded":["0","0","0","1"]}
// ];

//存放选中的要素的value
var thematic = 0;
var table = 0;
var field_cn=0;
var field =0;
initThemeList();

function initThemeList() {
    //动态获取指标数据集,并进行初始化[测试完成]
    var array;
    $.ajax({
        url: "./servlet/GetCatagoryServlet",
        type: 'POST',
        async:false,//设置为同步操作就可以给全局变量赋值成功
        dataType: 'json',
        cache:false,
        scriptCharset: 'utf-8',
        success: function (data) {
            // console.log(data);
            array = data;
            $("#themeaccordion").html('');
            for (var k in array){
                $("#themeaccordion").append('<div class="panel panel-accordion panel-default leftMenu" id="panel'+k+'"></div>');
                $("#panel"+k).append(
                    '<div class="panel-heading collapsed" ' +
                    'id="data-themeHeading-'+k+'" data-toggle="collapse" ' +
                    'data-target="#data-theme-'+k+'" data-parent="#themeaccordion" ' +
                    'aria-expanded="false" aria-controls="data-theme-'+k+'">');
                $("#data-themeHeading-"+k).append('<h4 class="panel-title"><a>'+array[k].tablename+'<span class="glyphicon glyphicon-chevron-down right"></span></a></h4>');
                $("#panel"+k).append('<div id="data-theme-'+k+ '" class="panel-collapse collapse" role="tabpanel" aria-labelledby="data-themeHeading-'+k+'"></div>');
                $("#data-theme-"+k).append('<ul class="list-group" id="list-group'+k+'"></ul>');
                var idname='list-group'+k;
                $("#"+idname).html('');
                for (var j in array[k].fields){
                    $("#"+idname).append(
                        // '<li class="list-group-item"><label class="menu-item-left"><input type="checkbox" value="'+array[k].fields[j]+'">'+array[k].fields[j]+'</label></li>'
                        // '<li class="list-group-item"><label class="menu-item-left"><input type="checkbox" value="'+array[k].fields[j]+'" pTable="'+array[k].tablename+'">'+array[k].fields[j]+'</label></li>'
                        '<li class="list-group-item"><label class="menu-item-left"><input type="checkbox" value="'+array[k].fields[j]+'" name="'+array[k].names[j]+'" gradeOption="'+array[k].isGraded[j]+'"pTable="'+array[k].tablename+'">'+array[k].fields[j]+'</label></li>'
                    )    //gradeOption：判断是否分级设色的指标。
                }
                break;
            }

            $("input:checkbox").each(function () {
                $(this).change(function () {
                    // console.log($(this).val());
                    //进行了指标的选择(目前允许多选,若规定为单选,则每次选择清除上一次所选的指标即可!)
                    if($(this).prop('checked')){
                        //指标所属表名
                        table = $(this).attr('pTable');
                        // console.log(table);

                        //判断是否是分级设色的指标
                        var isGraded=$(this).attr('gradeOption');
                        //清除其他主题的指标的选择？
                        $(this).parent().parent().parent().parent().parent().siblings().find("input:checkbox").prop("checked",false);
                        console.log($(this).val());
                        //所选指标属于新的主题
                        if(thematic != table){
                            thematic = table;
                            //允许跨主题进行分级指标+统计指标的组合!!!(但不允许跨主题进行统计指标之间的组合)
                            if(isGraded==0){
                                indi = [];//清空指标数组
                                // var field_cn = $(this).val(); //中文指标名
                                indi.push(table);
                                var chartfield = $(this).attr('name'); //英文指标名
                                indi.push(chartfield);
                                // thematic = table;
                                // console.log(indi.join());
                            }
                            else {
                                field_cn = $(this).val(); //中文指标名
                                field = $(this).attr('name'); //英文指标名
                                console.log(field);
                                refreshClassLyr();
                                //应用按钮在此时样式要发生变化
                                // $(".slider-menu #apply").css("opacity","8");
                                // $("#mapDiv .legend").css("background", "url(" + classifyImg_url + ")");
                            }
                        }
                        //所选指标属于当前指标
                        else{
                            if(isGraded==0){
                                if(indi[0]!=table){
                                    indi = [];
                                }
                                if(indi.length ==0){
                                    indi.push(table);
                                }
                                // var field = $(this).val();
                                var chartfield = $(this).attr('name'); //英文指标名
                                indi.push(chartfield);
                                console.log(indi);
                            }
                            else {
                                //在当前主题中选择了另一个分级指标时
                                var fieldsArray = $(this).parent().parent().parent().parent().find("input:checkbox");
                                // var gradeoption = $(this).attr("gradeoption");

                                for(var k=0;k<fieldsArray.length;k++){
                                    // console.log(fieldsArray[k]);
                                    if(fieldsArray[k].getAttribute("gradeOption")=="1" && fieldsArray[k].getAttribute("name")!= $(this).attr("name")){
                                        // fieldsArray[k].setAttribute("checked","FALSE");
                                        fieldsArray[k].checked=false;
                                        break;
                                    }
                                }
                                field_cn = $(this).val();
                                field = $(this).attr('name'); //英文指标名
                                refreshClassLyr();
                                //应用按钮的样式发生变化
                                // $(".slider-menu #apply").css("opacity","8");
                            }
                        }
                        if(indi.length !=0){
                            refreshChartLyr(indi);
                        }
                    }
                    //取消了当前指标的选择
                    else{
                        // var unCheckIndex = $(this).val();
                        var unCheckIndex = $(this).attr('name');
                        //判断是否是分级设色的指标
                        var isGraded=$(this).attr('gradeOption');
                        // console.log(unCheckIndex);
                        //indi.length==2代表仅剩下一个所选指标,此时进行取消指标选择
                        if(isGraded ==0){
                            if(indi.length==2 ){
                                indi = [];
                                for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                                    if ((map.getLayer(map.graphicsLayerIds[i])).name == "chartGLayer") {
                                        var layer = map.getLayer(map.graphicsLayerIds[i]);
                                        map.removeLayer(layer);
                                        break;
                                    }
                                }
                                if(field_cn==0){
                                    // $("#mapDiv .legend").remove();
                                    $("#legend-container .legend").remove();

                                    legendFlag=0;
                                    thematic = 0;
                                }else if(legendFlag!=0){
                                    $("#classifyLegend").click();
                                }
                            }
                            else {
                                for(var i=0;i<indi.length;i++){
                                    if(indi[i]==unCheckIndex){
                                        indi.splice(i, 1);
                                        break;
                                    }
                                }
                                if(indi.length !=0){
                                    refreshChartLyr(indi);
                                }
                            }
                        }
                        else {
                            //应用按钮变为灰色
                            // $(".slider-menu #apply").css("opacity","0.45");
                            //清空分级图层
                            for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                                if ((map.getLayer(map.graphicsLayerIds[i])).name == "classGLayer") {
                                    var layer = map.getLayer(map.graphicsLayerIds[i]);
                                    map.removeLayer(layer);
                                    // flag = 1;
                                    break;
                                }
                            }
                            table = 0;
                            field_cn = 0;
                            field = 0;
                            classIndex = undefined;

                            //清空图例
                            if(field_cn==0&&indi.length==0){
                                // $("#mapDiv .legend").remove();
                                $("#legend-container .legend").remove();
                                legendFlag=0;
                                // clearInterval(charLegendMonitor);
                                // clearInterval(classifyLegendMonitor);
                            }
                            else if(legendFlag!=0){
                                $("#chartLegend").click();
                            }

                            //回调初始图层
                            var url;
                            if(regionParam==1){
                                url="http://223.75.52.36:26080/arcgis/rest/services/HuBeiShen/MapServer";
                                // map.infoWindow.hide();
                                var center = {x:"112.021395",y:"31.01224452",zoom:"5"};
                                changeMap(url,center,5);
                                if(indi.length>=2){
                                    refreshChartLyr(indi);
                                    map.infoWindow.hide();
                                    map.setMapCursor("default");
                                    baseLayerURL = url;
                                }
                            }
                            else {
                                for(i in cityArray){
                                    if(i==rgnName){
                                        url=cityArray[i];
                                    }
                                }
                                if(url!=undefined){      //确保url不为空，默认为湖北省全图
                                    changeMap(url,geometry,7);
                                }
                                // else {
                                //     // changeMap(url,geometry,7);
                                // }
                                if(indi.length>=2){
                                    refreshChartLyr(indi);
                                    map.infoWindow.hide();
                                    map.setMapCursor("default");
                                    baseLayerURL = url;
                                }
                            }
                        }
                    }

                })
            })

            //展开和收起小按钮
            $(".panel-group").find(".panel").each(function () {
                // alert($(this));
                $(this).on('show.bs.collapse',function () {
                    $(this).find("span").toggleClass("glyphicon-chevron-up");
                    $(this).find("span").toggleClass("glyphicon-chevron-down");
                })
                $(this).on('hide.bs.collapse',function () {
                    $(this).find("span").toggleClass("glyphicon-chevron-down");
                    $(this).find("span").toggleClass("glyphicon-chevron-up");
                })
            });

        },
        error: function (xhr, status, errMsg) {
            console.log(errMsg);
        }
    });
}
