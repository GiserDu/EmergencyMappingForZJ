var layerIndex;
// var symbolSizeSlider, symbolOpacitySlider1, symbolOpacitySlider2, classNumSlider;
var symbolSizeSliderValue=0;
var symbolOpacitySliderValue=0;
var classNumSliderValue=5;

var userLoadSpfilename;  //自定义上传空间数据的文件名
var mouseMoveClassLyr;
var fieldslist;
var mouseMove;
var mouseOut;
var allTjLayerContent={};
// 制图范围数据的json格式
// var tjPanel1={"tabID":"1","identityField":"value","regionData":"","fileName":""};
var tjPanel1={};
var tjPanel2 = {"tabId":"1","dataAddress":"","tableName":"","spatialId":"","timeId":"","tableFields":[],"fieldsName":[],"fieldsNum":0};
//tjPanel3中有一个key是type，1表示统计图表，2表示分级符号
var tjPanel3={};
var zoomOutFlag = 0;
var zoomGraphics;
var thisZoomLayer;
var chartImg_url = undefined;//饼图图例的url
// var chartImg_url2 = undefined;
// var charLegendMonitor;//饼图图例的定时器
var legendFlag = 0;
var classifyImg_url = undefined;//分级统计图图例的url

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
        $("#legend-container #legend").attr("src",chartImg_url);

    }

    //图例列表回收
    $("#twoLegend").css("width","0px");
    $("#mapContainer .legend-menu").css("width","0px");
    $("#mapContainer .legend-menu").css("border","0px");

});
$("#classifyLegend").click(function () {
    if (field_cn==""){
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

// 当用户新建时弹出的面板
function opentjMenuLayer() {
    layui.use(['layer','form','element'],function () {
        var layer = layui.layer
            ,element = layui.element
            ,form=layui.form;

        opentjPanel2();
        form.render();

        symbolSizeSliderValue=50;symbolOpacitySliderValue=0;classNumSliderValue=5;

        element.on('nav(navDemo)', function(elem){
            // console.log(elem);
            var leftMenuName=elem.attr('name');
            var selectedIndexNum;

            if (leftMenuName=="selectStatistics") {
                // $(".tjPanel-content").html(html2);

                $("#tjPanel-content2").show();
                $("#tjPanel-content3").hide();
                $("#tjPanel-content4").hide();

                // opentjPanel2();
                // form.render();

            } else if(leftMenuName=="selectMappingTemplate") {
                $("#fieldslist"+tjPanel2.tabId).next().find("button[lay-filter='fields']")[0].click();//辅助用户点击“下一步”，获取当前指标

                // var a=$("#fieldslist"+tjPanel2.tabId).next().find("button")[0];
                // a.click();

                selectedIndexNum=tjPanel2.fieldsNum;

                if (selectedIndexNum==0){
                    alert('请选择统计指标');
                    elem.parent().removeClass("layui-this");
                    elem.parent().prev().addClass("layui-this");

                } else if(selectedIndexNum==1){
                    // $(".tjPanel-content").html(html4);

                    $("#tjPanel-content2").hide();
                    $("#tjPanel-content3").hide();
                    $("#tjPanel-content4").show();

                    var isSymbolLoaded=$("#tjPanel-content4").attr("isloaded");
                    if (isSymbolLoaded=="false") {
                        initTjGraduatedSymbol();
                        $("#tjPanel-content4").attr("isloaded","true");
                    };
                    listenOnSymbolTitleClick();

                    setSlidersValue(symbolSizeSliderValue,symbolOpacitySliderValue,symbolOpacitySliderValue,classNumSliderValue);
                    form.render();



                }else {
                    // $(".tjPanel-content").html(html3);
                    $("#tjPanel-content2").hide();
                    $("#tjPanel-content3").show();
                    $("#tjPanel-content4").hide();

                    var isSymbolLoaded=$("#tjPanel-content3").attr("isloaded");
                    if (isSymbolLoaded=="false") {
                        initTjChartSymbol();
                        $("#tjPanel-content3").attr("isloaded","true");
                    };
                    listenOnSymbolTitleClick();

                    setSlidersValue(symbolSizeSliderValue,symbolOpacitySliderValue,symbolOpacitySliderValue,classNumSliderValue);
                    form.render();
                    userDefineChartColor();
                }
            }
        });
        element.render('nav');
    })
}

// 当用户修改时弹出的面板
function modifytjMenuLayer(symbolInfo) {
    var symPara1=0,
        symPara2=0,
        symPara3=0,
        symPara4=0;

    var type=parseInt(symbolInfo.type);
    if(type==1){
        symPara1=symbolInfo.symbolSizeSliderValue;
        symPara2=symbolInfo.symbolOpacitySliderValue;
        $("#xOffset").val(symbolInfo.xoffset);
        $("#yOffset").val(symbolInfo.yoffset);

    }else if(type==2){
        symPara3=symbolInfo.symbolOpacitySliderValue;
        symPara4=symbolInfo.classNumSliderValue;
    }


    layui.use(['layer','form','element'],function () {
        var layer = layui.layer
            ,element = layui.element
            ,form=layui.form;

        opentjPanel2();

        //复原第二个面板
        //获取select和checkbox
        var select = $("input[class='layui-input layui-unselect']").val();
        var checkboxes = $(fieldslist).find(".layui-form-checkbox.layui-form-checked");
        var checkName = [];
        $.each(checkboxes,function(index,item){
            checkName.push($(item).find("span").html());
        });
        //重新渲染并赋值
        form.render();
        //方式一：保留dom
        $("#spatialId"+tjPanel2.tabId).siblings("div.layui-form-select").find("dl").find("dd[lay-value="+select+"]").click();
        $.each(checkName,function(index,item){
            $("input[name="+item+"]").prop('checked', true);
            $("input[name="+item+"]").next().addClass('layui-form-checked');
        });
        // //方式二：个别存值
        // $("#spatialId"+tjPanel2.tabId).siblings("div.layui-form-select").find("dl").find("dd[lay-value="+ allTjLayerContent.statisticdata.spatialId+"]").click();
        // $.each(allTjLayerContent.statisticdata.fieldsName,function(index,item){
        //     $("input[name="+item+"]").prop('checked', true);
        //     $("input[name="+item+"]").next().addClass('layui-form-checked');
        // });

        $("#fieldslist"+tjPanel2.tabId).next().find("button[lay-filter='fields']")[0].click();//辅助用户点击“下一步”，获取当前指标

        // 复原第三个面板
        // 获取分级模型的select
        var selectedModelName=symbolInfo.modelName;
        $("#model").siblings("div.layui-form-select").find("dl").find("dd[lay-value="+selectedModelName+"]").click();
        var isColorInverse=symbolInfo.isColorInverse;
        if(isColorInverse){
            $('#isColorInverse').prop('checked', true);
            $('#isColorInverse').next().addClass('layui-form-checked');
        }
        // ======================
        listenOnSymbolTitleClick();

        userDefineChartColor();
        // ==========================

        // var slidersValues=[1,2,3,4];
        setSlidersValue(symPara1,symPara2,symPara3,symPara4);

        element.on('nav(navDemo)', function(elem){
            // console.log(elem);

            var leftMenuName=elem.attr('name');
            var selectedIndexNum;

            if (leftMenuName=="selectStatistics") {
                // $(".tjPanel-content").html(html2);

                $("#tjPanel-content2").show();
                $("#tjPanel-content3").hide();
                $("#tjPanel-content4").hide();
                // opentjPanel2();


            } else if(leftMenuName=="selectMappingTemplate") {

                $("#fieldslist"+tjPanel2.tabId).next().find("button[lay-filter='fields']")[0].click();//辅助用户点击“下一步”，获取当前指标

                selectedIndexNum=tjPanel2.fieldsNum;

                if (selectedIndexNum==0){
                    alert('请选择统计指标');
                    elem.parent().removeClass("layui-this");
                    elem.parent().prev().addClass("layui-this");

                } else if(selectedIndexNum==1){
                    // $(".tjPanel-content").html(html4);

                    $("#tjPanel-content2").hide();
                    $("#tjPanel-content3").hide();
                    $("#tjPanel-content4").show();

                    var isSymbolLoaded=$("#tjPanel-content4").attr("isloaded");
                    if (isSymbolLoaded=="false") {
                        initTjGraduatedSymbol();
                        $("#tjPanel-content4").attr("isloaded","true");
                    };
                    listenOnSymbolTitleClick();


                    // initTjGraduatedSymbol();
                    // renderSlider();
                    // setSlidersValue(symbolSizeSliderValue,symbolOpacitySliderValue,symbolOpacitySliderValue,classNumSliderValue);
                    // form.render();

                    // clickAndLoadAllInfo();


                }else {
                    // $(".tjPanel-content").html(html3);
                    $("#tjPanel-content2").hide();
                    $("#tjPanel-content3").show();
                    $("#tjPanel-content4").hide();

                    var isSymbolLoaded=$("#tjPanel-content3").attr("isloaded");
                    if (isSymbolLoaded=="false") {
                        initTjChartSymbol();
                        $("#tjPanel-content3").attr("isloaded","true");
                    };
                    listenOnSymbolTitleClick();

                    // initTjChartSymbol();
                    // renderSlider();
                    // setSlidersValue(symbolSizeSliderValue,symbolOpacitySliderValue,symbolOpacitySliderValue,classNumSliderValue);
                    form.render();
                    userDefineChartColor();
                    // clickAndLoadAllInfo();

                }
            }
        });
        element.render('nav');
    })
}

// 当用户修改时弹出的面板_新_测试
function modifytjMenuLayer_new(allTjLayerContent) {
    layui.use(['layer','form','element'],function () {
        var layer = layui.layer
            ,element = layui.element
            ,form=layui.form;

        opentjPanel2();
        form.render();


        //==================================================
        var statisticInfo=allTjLayerContent.statisticdata;
        var symbolInfo=allTjLayerContent.cartographydata;

        // 复原第二个面板的信息
        $("#tab"+allTjLayerContent.statisticdata.tabId).click();
        displayFields($("#fieldslist"+allTjLayerContent.statisticdata.tabId),allTjLayerContent.statisticdata.tableFields);
        $("#spatialId"+allTjLayerContent.statisticdata.tabId).siblings(".layui-form-select").find("dd[lay-value="+ allTjLayerContent.statisticdata.spatialId+"]").click();
        $("#timeId"+allTjLayerContent.statisticdata.tabId).siblings(".layui-form-select").find("dd[lay-value="+ allTjLayerContent.statisticdata.timeId+"]").click();
        $.each(allTjLayerContent.statisticdata.fieldsName,function(index,item){
            $("#fieldslist"+allTjLayerContent.statisticdata.tabId).find("input[value="+item+"]").next().click();
        });
        $("#fieldslist"+tjPanel2.tabId).next().find("button[lay-filter='fields']")[0].click();//辅助用户点击“下一步”，获取当前指标

        // 复原第三个面板的信息
        var type=parseInt(symbolInfo.type);

        var symPara1=0,
            symPara2=0,
            symPara3=0,
            symPara4=0;

        if(type==1){
            symPara1=symbolInfo.symbolSizeSliderValue;
            symPara2=symbolInfo.symbolOpacitySliderValue;
        }else if(type==2){
            symPara3=symbolInfo.symbolOpacitySliderValue;
            symPara4=symbolInfo.classNumSliderValue;
        }

        // listenOnSymbolTitleClick();
        //
        // userDefineChartColor();
        //
        // setSlidersValue(symPara1,symPara2,symPara3,symPara4);
        //=============================================================================================

        element.on('nav(navDemo)', function(elem){
            // console.log(elem);
            var leftMenuName=elem.attr('name');
            var selectedIndexNum;

            if (leftMenuName=="selectStatistics") {
                // $(".tjPanel-content").html(html2);

                $("#tjPanel-content2").show();
                $("#tjPanel-content3").hide();
                $("#tjPanel-content4").hide();

                // opentjPanel2();
                // form.render();

            } else if(leftMenuName=="selectMappingTemplate") {
                $("#fieldslist"+tjPanel2.tabId).next().find("button[lay-filter='fields']")[0].click();//辅助用户点击“下一步”，获取当前指标

                // selectedIndexNum=statisticInfo.fieldsNum;
                selectedIndexNum=tjPanel2.fieldsNum;

                if (selectedIndexNum==0){
                    alert('请选择统计指标');
                    elem.parent().removeClass("layui-this");
                    elem.parent().prev().addClass("layui-this");

                } else if(selectedIndexNum==1){

                    $("#tjPanel-content2").hide();
                    $("#tjPanel-content3").hide();
                    $("#tjPanel-content4").show();

                    var isSymbolLoaded=$("#tjPanel-content4").attr("isloaded");
                    if (isSymbolLoaded=="false") {
                        initTjGraduatedSymbol();
                        $("#tjPanel-content4").attr("isloaded","true");
                    };
                    listenOnSymbolTitleClick();
                    // form.render();

                    var selectedModelName=symbolInfo.modelName;
                    $("#model").siblings("div.layui-form-select").find("dl").find("dd[lay-value="+selectedModelName+"]").click();
                    var isColorInverse=symbolInfo.isColorInverse;
                    if(isColorInverse){
                        $('#isColorInverse').prop('checked', true);
                        $('#isColorInverse').next().addClass('layui-form-checked');
                    }
                    var imgSrc=symbolInfo.colorSolutionSrc;
                    $("#color-selected>.select_title>img").attr("src",imgSrc);

                    setSlidersValue(symbolSizeSliderValue,symbolOpacitySliderValue,symbolOpacitySliderValue,classNumSliderValue);

                }else {
                    // $(".tjPanel-content").html(html3);
                    $("#tjPanel-content2").hide();
                    $("#tjPanel-content3").show();
                    $("#tjPanel-content4").hide();

                    var isSymbolLoaded=$("#tjPanel-content3").attr("isloaded");
                    if (isSymbolLoaded=="false") {
                        initTjChartSymbol();
                        $("#tjPanel-content3").attr("isloaded","true");
                    };
                    listenOnSymbolTitleClick();

                    var chartID=symbolInfo.chartID;
                    var chartSrc="assets/imgs/chartIcon/"+chartID+".png";
                    $("#chart-selected>.select_title>img").attr("src",chartSrc);

                    var solutionSrc=symbolInfo.colorSolutionSrc;
                    $("#color-solution>.select_title>img").attr("src",solutionSrc);

                    $("#xOffset").val(symbolInfo.xoffset);
                    $("#yOffset").val(symbolInfo.yoffset);

                    setSlidersValue(symbolSizeSliderValue,symbolOpacitySliderValue,symbolOpacitySliderValue,classNumSliderValue);
                    // form.render();
                    userDefineChartColor();
                }
            }
        });
        element.render('nav');

    });
}

// 构造载入行政区划事件下的json
function constructTjJson11() {
    // 载入行政区划的选项卡下，获取选中区域的name值
    var selectedRegionName=$(".xm-select").attr('title');
    // 获取选中区域的value值
    var selectedValues=[];
    var selectedItem=$(".xm-select-label span");

    for (var i=0;i<selectedItem.length;i++){
        // 根据斜杠个数判断是省、市、区中的哪一级
        var selectedItemValue=selectedItem[i].getAttribute('value');
        var valuePart=selectedItemValue.split("/");
        var adminValue=valuePart[valuePart.length-1];
        // console.log(adminValue);
        selectedValues[i]=adminValue;
    }

    tjPanel1={
        "tabID":"1",
        "identityField":"value",
        "regionDataName":selectedRegionName,
        "regionDataValue":selectedValues,
        "fileName":""
    };
    console.log(tjPanel1);
}

// 构造用户上传自定义空间数据事件下的json
function constructTjJson12() {
    // 获得用户选取的空间标识的字段
    var identityField=$('#userDataField option:selected').val();;
    tjPanel1={
        "tabID":"2",
        "identityField":identityField,
        "regionDataName":"",
        "regionDataValue":"",
        "fileName":userLoadSpfilename
    };
    console.log(tjPanel1);

}

// 构造符号的json

function constructTjJson3() {
    var selectedIndexNum=tjPanel2.fieldsNum;

    if(selectedIndexNum==null || selectedIndexNum==0){
        alert("您还未选择用于制图的统计指标");
    } else if(selectedIndexNum==1){
        // -----获得用户选择的有关分级符号的值-----
        var solutionSrc=$("#color-selected>.select_title>img").attr("src");
        var color1=$("#color-selected>.select_title>img").attr("color1");
        var color2=$("#color-selected>.select_title>img").attr("color2");
        var isChecked=$('#isColorInverse').is(':checked'); ;
        if(isChecked){
            var tmp;
            tmp=color1;
            color1=color2;
            color2=tmp;
        };
        var colors=getColorbar(classNumSliderValue,color1,color2);
        var colorString = "";
        for (var i=0;i<colors.length;i++){
            colorString += colors[i];
            colorString += ";";
        }
        colorString = colorString.substring(0, colorString.length - 1);
        var modelName=$('#model option:selected').val();
        tjPanel3={
            "type":"2",
            "classNumSliderValue":classNumSliderValue,
            "colorSolutionSrc":solutionSrc,
            "colors":colorString,
            "isColorInverse":isChecked,
            "modelName":modelName,
            "symbolOpacitySliderValue":symbolOpacitySliderValue
        }
    } else if(selectedIndexNum>1){
        // -----获得用户选择的有关统计符号的值-----
        var chartID = $("#chart-selected>.select_title>img").attr("src").slice(-10,-4);
        var colorName= $("#color-solution>.select_title>img").attr("name");
        var solutionSrc= $("#color-solution>.select_title>img").attr("src");
        var xoffset=$("input[ name='x-offset' ]").val();
        var yoffset=$("input[ name='y-offset' ]").val();
        var colors=[];
        // 获得所有颜色选择器
        var chartIndexColorpick=$(".userDefineColors").find('.chartColorPicker');
        for(var i=0;i<chartIndexColorpick.length;i++){
            colors[i]=$(".userDefineColors").find('.chartColorPicker').find('.layui-colorpicker-trigger-span')[i].style.backgroundColor;
        }
        tjPanel3={
            "type":"1",
            "chartID":chartID,
            "colorName":colorName,
            "colorSolutionSrc":solutionSrc,
            "colors":colors,
            "symbolSizeSliderValue":symbolSizeSliderValue,
            "symbolOpacitySliderValue":symbolOpacitySliderValue,
            "xoffset":xoffset,
            "yoffset":yoffset
        }
    }

    console.log(tjPanel3);
}

// 当修改时创建json
function constructTjJson3_modify(content) {

    var statisticInfo=content.statisticdata;
    var selectedIndexNum=statisticInfo.fieldsNum;

    if(selectedIndexNum==null || selectedIndexNum==0){
        alert("您还未选择用于制图的统计指标");
    } else if(selectedIndexNum==1){
        // -----获得用户选择的有关分级符号的值-----
        var solutionSrc=$("#color-selected>.select_title>img").attr("src");
        var color1=$("#color-selected>.select_title>img").attr("color1");
        var color2=$("#color-selected>.select_title>img").attr("color2");
        var isChecked=$('#isColorInverse').is(':checked'); ;
        if(isChecked){
            var tmp;
            tmp=color1;
            color1=color2;
            color2=tmp;
        };
        var colors=getColorbar(classNumSliderValue,color1,color2);
        var colorString = "";
        for (var i=0;i<colors.length;i++){
            colorString += colors[i];
            colorString += ";";
        }
        colorString = colorString.substring(0, colorString.length - 1);
        var modelName=$('#model option:selected').val();
        tjPanel3={
            "type":"2",
            "classNumSliderValue":classNumSliderValue,
            "colorSolutionSrc":solutionSrc,
            "colors":colorString,
            "isColorInverse":isChecked,
            "modelName":modelName,
            "symbolOpacitySliderValue":symbolOpacitySliderValue
        }
    } else if(selectedIndexNum>1){
        // -----获得用户选择的有关统计符号的值-----
        var chartID = $("#chart-selected>.select_title>img").attr("src").slice(-10,-4);
        var colorName= $("#color-solution>.select_title>img").attr("name");
        var solutionSrc= $("#color-solution>.select_title>img").attr("src");
        var xoffset=$("input[ name='x-offset' ]").val();
        var yoffset=$("input[ name='y-offset' ]").val();
        var colors=[];
        // 获得所有颜色选择器
        var chartIndexColorpick=$(".userDefineColors").find('.chartColorPicker');
        for(var i=0;i<chartIndexColorpick.length;i++){
            colors[i]=$(".userDefineColors").find('.chartColorPicker').find('.layui-colorpicker-trigger-span')[i].style.backgroundColor;
        }
        tjPanel3={
            "type":"1",
            "chartID":chartID,
            "colorName":colorName,
            "colorSolutionSrc":solutionSrc,
            "colors":colors,
            "symbolSizeSliderValue":symbolSizeSliderValue,
            "symbolOpacitySliderValue":symbolOpacitySliderValue,
            "xoffset":xoffset,
            "yoffset":yoffset
        }
    }

    console.log(tjPanel3);
}

// 初始化统计符号图片和颜色的的选择框
function initTjChartSymbol() {
    var width=$('.select_title').width();
    $(".select_content").css('width',width);
    var iconArray=['010101','010102','010103','010104','010105','010106','010107','010108','010109','010111','010201','010202','010203','010204','020101','020102','020103','020104','020202','020203'];
    selecterInit(iconArray);

    var solutionArray=['--请选择--','黄红色系','蓝色色系','红色色系','黄绿色系','黄棕色系','青黄色系'];
    colorSolutionInit(solutionArray);


    function colorSolutionInit(e) {
        $("#color-solution").find('.select_content').html('');
        for ( var k=1;k<e.length;k++) {
            $("#color-solution").find('.select_content').append(
                '<li><img  style="width:100%;"  name="'
                + e[k]
                + '" src="./assets/imgs/gradeIcon/10/' + k + '.jpg"></li>');
        }
    }

    function selecterInit(e){
        $("#chart-selected").find('.select_content').html('');
        e.forEach(function(u,v,w){
            $("#chart-selected").find('.select_content').append('<li value="'+u+'"><img src="./assets/imgs/chartIcon/'+u+'.png"></li>');
        })
    }
}

// 初始化分级符号色带的选择框
function initTjGraduatedSymbol() {
    var width=$('#color-selected').outerWidth(true);
    $(".select_content").css('width',width);

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
                '<li><img style="width:100%;"  color1="'
                + e[k][0] + '" color2="' + e[k][1]
                + '" src="./assets/imgs/gradeIcon/9/' + k + '.jpg"></li>');
        }
    }
}

// 监听符号下拉列表的选择
function listenOnSymbolTitleClick() {
    $(".sym-selected").each(function(e){
        var That = $(this);
        // var width=$('.select_title').width();
        // $(".select_content").css('width',width);

        That.find(".select_title").off('click').on('click',function(){
            var state=That.find(".select_content").css("display");
            if (state=="flex"||state=="block"){
                That.find(".select_content").hide();
            } else if (state=="none"){
                That.find(".select_content").show();
            };
            // 一个下拉列表打开时，其他下拉列表关闭
            $(".sym-selected").not(That).find(".select_content").hide();
        });



    });

    $(".sym-selected").on('click', '.select_content li', function(event) {
        $(this).parent().parent().find('.select_title').html($(this).html());
        $(this).parent().hide();
    });
}

// 用户自定义统计图表的符号
function userDefineChartColor() {
    var defaultColorTable=[];
    var currentColorName=$("#color-solution").find('img').attr('name');
    for (var key in colorTable){
        if(currentColorName==key){
            for (var newkey in colorTable[key]) {
                defaultColorTable.push(colorTable[key][newkey]);
            }
        }
    }
    // var defaultColorTable=['#ED1B24','#262164','#F2F101','#21B24B','#92278F','#F18C24','#FFE375','#ABE94A','#FBA723','#D8350C'];
    var selectedStatisticIndex=tjPanel2.fieldsName;
    // var length=0;
    // for(var a in selectedStatisticIndex){
    //     length++;
    // }
    // console.log(length);

    layui.use('colorpicker',function () {

        var $ = layui.$
            ,colorpicker = layui.colorpicker;

        $('.userDefineColors').empty();

        for(var i=0;i<selectedStatisticIndex.length;i++){
            // 根据用户选中的指标数量动态生成颜色选择器

            var indexColorDiv= '<div class=" chartColorPickerDIV"><label class="selectedStaIndexsLabel">'+selectedStatisticIndex[i+1]+'</label>'+
                '               <div id="color'+i+'" class="chartColorPicker" style="margin-right: 20px"></div></div>';
            $('.userDefineColors').append(indexColorDiv);

            var colorPickId='#color'+i;

            colorpicker.render({
                elem: colorPickId
                ,color: defaultColorTable[i]
                ,format: 'rgb' //默认为 hex
                ,size:'lg'
            });

        }
    })

    listenOnChartColorChange();

}

// 监听图表配色方案的选择，配色方案改变后各个颜色选择器的颜色也依次改变
function listenOnChartColorChange() {
    $('#color-solution').on('click','.select_content li',function (event) {
        var colorSolutionName = $("#color-solution>.select_title>img").attr("name");

        // 获得所有颜色选择器
        var chartIndexColorpick=$(".userDefineColors").find('.chartColorPicker');
        layui.use(['form','colorpicker'],function () {
            var $ = layui.$
                ,colorpicker = layui.colorpicker;

            for (var key in colorTable){
                if (colorSolutionName==key) {
                    for(var i=0;i<chartIndexColorpick.length;i++){
                        var colorPickID="color"+i;
                        colorpicker.render({
                            elem: "#"+colorPickID
                            ,color: colorTable[key][colorPickID]
                            ,size:'lg'
                        });
                    }
                }
            }
        })
    })
}

//滑块的渲染
function renderSlider() {
    layui.use(['slider','form'],function () {
        var slider=layui.slider;

        // 滑块
       slider.render({
            elem: '#symbolSize'
            // ,input: true //输入框
            ,diabled:"false"
            ,change:function (value) {
                symbolSizeSliderValue=value;
            }
        });

        slider.render({
            elem: '#symbolOpacity1'
            // ,input: true //输入框
            ,diabled:"false"
           ,change:function (value) {
               symbolOpacitySliderValue=value;
           }
        });

        slider.render({
            elem: '#symbolOpacity2'
            // ,input: true //输入框
            ,diabled:"false"
            ,change:function (value) {
                symbolOpacitySliderValue=value;
            }
        });

       slider.render({
            elem: '#classNum'
            // ,input: true //输入框
            ,diabled:"false"
            ,value: 5 //默认初始值
            ,step:1
            ,min:1 //最小值
            ,max:7 //最大值
           ,change:function (value) {
               classNumSliderValue=value;
           }
        });
    })
}

//滑块的赋值
function setSlidersValue(symbolSize,symbolOpacity1,symbolOpacity2,classNum){
    layui.use(['slider','form'],function () {
        var slider=layui.slider;

        // 滑块
        slider.render({
            elem: '#symbolSize'
            // ,input: true //输入框
            ,value:symbolSize
            ,diabled:"false"
            ,change:function (value) {
                hh=value;
                symbolSizeSliderValue=value;
            }
        });

        slider.render({
            elem: '#symbolOpacity1'
            // ,input: true //输入框
            ,value:symbolOpacity1
            ,diabled:"false"
            ,change:function (value) {
                symbolOpacitySliderValue=value;
            }
        });

        slider.render({
            elem: '#symbolOpacity2'
            // ,input: true //输入框
            ,value:symbolOpacity2
            ,diabled:"false"
            ,change:function (value) {
                symbolOpacitySliderValue=value;
            }
        });

        slider.render({
            elem: '#classNum'
            // ,input: true //输入框
            ,diabled:"false"
            ,value: classNum //默认初始值
            ,step:1
            ,min:1 //最小值
            ,max:7 //最大值
            ,change:function (value) {
                classNumSliderValue=value;
            }
        });
    })
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


//打开第二个页面
function opentjPanel2(){
    layui.use(['element','form'], function(){

        var element = layui.element
            ,form=layui.form;
        if(tjPanel2.tabId==1){
            tjPanel2.dataAddress="";
            getTableTree();
            submitFields();
        }else if(tjPanel2.tabId==2){
            tjPanel2.tableName = "";
            OtherDatabase();
        }else if(tjPanel2.tabId==3){
            tjPanel2.dataAddress="";
            EXCELupload();
        }
        element.on('tab(nav2)', function(data){
            //console.log(this); //当前Tab标题所在的原始DOM元素
            //console.log(data.index); //得到当前Tab的所在下标
            //console.log(data.elem); //得到当前的Tab大容器
            if((data.index+1)==1){
                tjPanel2.tabId = 1;
                tjPanel2.dataAddress="";
                getTableTree();
                submitFields();
            }else if((data.index+1)==2){
                tjPanel2.tabId = 2;
                tjPanel2.tableName = "";
                OtherDatabase();
            }else if((data.index+1)==3){
                tjPanel2.tabId = 3;
                tjPanel2.dataAddress="";
                EXCELupload();
            }
        });
    });
}

//获取tabletree
function getTableTree(){
    var treeName;
    var treeElement;
    var createTree = [];
    var tableFields = {};

    if(tjPanel2.tabId == 1){
        treeName = "SystemDatabase";
        treeElement = '#treedemo1';
        fieldslist = $("#fieldslist1");
    }else if(tjPanel2.tabId == 2){
        treeName = "OtherDatabase";
        treeElement = '#treedemo2';
        fieldslist = $("#fieldslist2");
    }else if(tjPanel2.tabId == 3){
        fieldslist = $("#fieldslist3");
    }
    $.ajax({//返回tabletree
        type: 'post',
        url:"./servlet/fileUploadServlet",
        dataType:"json",
        async:"false",
        data:{ "inputType":"localDatabase"},
        success: function (data) { //返回json结果
            //alert(data);
            createTree =data.dataEx;
            displayTableTree(treeElement,createTree);
        },
        error:function(){
            alert("sorry!")
        }
    });
    //构造
    //createTree = [{name: '表格1'},{name: '表格2'},{name: '表格3'}];

}

//渲染tabletree，并监听
function displayTableTree(treeElement,createTree){
    $(treeElement).empty();
    layui.use(['tree','form'],function(){
        var tree = layui.tree;
        var form = layui.form;
        layui.tree({
            elem: treeElement, //传入元素选择器
            nodes: createTree,
            click:function (node) {
                console.log(node.name);
                var nodeName;
                switch (node.name){
                    case "交通专题":
                        nodeName = "transportation";
                        break;
                    case "人口专题":
                        nodeName = "population";
                        break;
                    case "经济专题":
                        nodeName = "economy";
                        break;
                }
                tjPanel2.tableName = nodeName;
                $.ajax({
                    type: 'post',
                    url:"./servlet/fileUploadServlet",
                    dataType:"json",
                    async:"false",
                    data:{ "tableName":nodeName, "inputType":"column"},
                    success: function (data) { //返回json结果(表头)
                        //tableFields = JSON.parse(data);
                        //tableFields = $.parseJSON(data);
                        tableFields = data.tableField;
                        // displayFields(fieldslist,tableFields);
                        displayFields(fieldslist,tableFields);
                    },
                    error:function(){
                        alert("sorry!")
                    }
                });
                //构造
                // var tableFields1 = {"0":"id","1":"username","2":"email","3":"sex","4":"city","5":"sign","6":"experience","7":"ip","8":"logins","9":"joinTime"};
                // var tableFields2 = {"0":"id","1":"username","2":"sex","3":"city","4":"sign","5":"experience","6":"logins","7":"wealth","8":"classify","9":"score"};
                // if (node.name=="表格1"){
                //     tableFields=tableFields1;
                // }else if (node.name=="表格2"){
                //     tableFields=tableFields2;
                // }else{
                //     tableFields={};
                // }


            }
        });
    })
}

//渲染字段
function displayFields(fieldslist,tableFields){
    tjPanel2.tableFields = tableFields;
    layui.use(['form'],function() {
        var form = layui.form;
        fieldslist.empty();
        var spatialIdentity = $("");
        if(tjPanel2.tabId == "1"){
            spatialIdentity = $("#spatialId1");
        }else if(tjPanel2.tabId == "2"){
            spatialIdentity = $("#spatialId2");
        }else if(tjPanel2.tabId == "3"){
            spatialIdentity = $("#spatialId3");
        };
        spatialIdentity.empty();
        $.each(tableFields, function(index,item) {
            if(index=="0"){
                spatialIdentity.append('<option selected="" value="'+item+'">'+item+'</option>');
                tjPanel2.spatialId = item;
            }else {
                spatialIdentity.append('<option value="'+item+'">'+item+'</option>');
                fieldslist.append('<input type="checkbox" name="'+item+'" title="'+item+'" value="'+item+'">');
            }
        });
        form.render();
        form.on('select(spatialId)', function(data){
            //console.log(data.value); //得到被选中的值
            tjPanel2.spatialId = data.value;
            fieldslist.empty();
            $.each(tableFields, function(index,item) {
                if(item!=data.value){
                    fieldslist.append('<input type="checkbox" name="'+item+'" title="'+item+'" value="'+item+'">');
                }
            });
            form.render();
        });
    })

}

//提交选择的字段
function submitFields(){
    layui.use(['form'],function(){
        var form = layui.form;
        form.on('submit(fields)', function (data) {
            //console.log(data.field.database);
            // layer.alert(JSON.stringify(data.field), {
            //     title: '最终的提交信息'
            // });
            tjPanel2.fieldsName = [];
            tjPanel2.fieldsNum = 0;
            $.each(data.field, function(index,item){
                //console.log(index,item);
                if(tjPanel2.fieldsNum==0){

                }else if(tjPanel2.fieldsNum==1){
                    tjPanel2.timeId = item;
                }else {tjPanel2.fieldsName.push(item);}
                    tjPanel2.fieldsNum++
            });
            tjPanel2.fieldsNum = tjPanel2.fieldsNum - 2;

            console.log(tjPanel2);
            $("#selectMappingTemplate").click();
            return false;
        });
    })
}

function initTjLayer(allTjLayerContent, tjType, regionParamVar) {
    var url;
    if (tjType == "chartLayerData"){
        var chartLayerNum = 1; //当前添加的统计图层数量
        for (var i=0; i<map.graphicsLayerIds.length; i++){
            if (map.getLayer(map.graphicsLayerIds[i]).name == "chartGLayer")
                chartLayerNum++;
        }
        console.log(chartLayerNum);
    }
    if (tjPanel2.tabId=="1"){
        url= "./servlet/fileUploadServlet?allTjLayerContent=" + encodeURI(allTjLayerContent);
    }else if(tjPanel2.tabId=="2"){
        url= "./servlet/chartLayerFromAPIServlet?allTjLayerContent="+ encodeURIComponent(allTjLayerContent);
    }

    console.log(tjType);
    $.ajax({
        type: 'POST',
        url: url,
        async:"false",
        dataType:"json",
        // data:{"inputType":"test"},
        data:{"inputType": tjType, "regionParam": regionParamVar, "chartLayerNum": chartLayerNum},
        success: function (data) {
            tjLayerName=JSON.parse(allTjLayerContent).name;
            console.log(url);
            if (data.type==="chartLayer"){
                doChartLayer(data);
                return;
            }
            console.log(data);
            var classLegend = data.classLegend;
            field_cn = fieldsOrIndi;
            console.log(field_cn);
            classifyImg_url = "data:image/png;base64," + classLegend;
            // if(legendFlag!=0 &&legendFlag == 'classify'){
            //     $("#classifyLegend").click();                    //触发classifylegend的点击事件
            // }
            //ar dataSource = data.dataSource;
            // console.log(data.dataClassArray);
            var classGraphics = initClassLayer(data.classDataArray);
            // if (map.graphicsLayerIds.length == 0) {
            //     //可以为graphicLayer添加mouse-over,click等事件;
            //     //map.removeLayer(baseLayerHB);
            //     var graphicLayer = new esri.layers.GraphicsLayer();
            //     graphicLayer.name = "classGLayer";
            //     //Graphic(geometry,symbol,attributes,infoTemplate)-->infoTemlate为弹出窗体,用以显示信息
            //     for (var i=0;i<classGraphics.length;i++){
            //         graphicLayer.add(classGraphics[i]);
            //     }
            //     map.addLayer(graphicLayer);
            //     graphicLayer.setOpacity(0.95);
            // }
            // else {
                //map.removeLayer(baseLayerHB);
                var flag = 0;
                for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                    console.log(tjLayerName);
                    if ((map.getLayer(map.graphicsLayerIds[i])).id == tjLayerName) {
                        var layer = map.getLayer(map.graphicsLayerIds[i]);
                        layer.clear();//清空所有graphics
                        for (var i=0;i<classGraphics.length;i++){
                            layer.add(classGraphics[i]);
                        }
                        layer.content = allTjLayerContent;
                        flag = 1;
                        layer.setOpacity(0.95);
                        break;
                    }
                }
                if(flag==0){
                    var graphicLayer = new esri.layers.GraphicsLayer();
                    graphicLayer.name = "classGLayer";
                    graphicLayer.id = tjLayerName;
                    graphicLayer.content = allTjLayerContent;
                    //Graphic(geometry,symbol,attributes,infoTemplate)-->infoTemlate为弹出窗体,用以显示信息
                    for (var i=0;i<classGraphics.length;i++){
                        graphicLayer.add(classGraphics[i]);
                    }
                    map.addLayer(graphicLayer);
                    graphicLayer.setOpacity(0.95);
                }
                // map.removeLayer(baseLayerHB);
                // graphicLayer.setOpacity(0.9);
                // refreshChartLyr(indi);
            // }

            //添加鼠标响应事件
            var classLayer;
            for (var i = 0; i < map.graphicsLayerIds.length; i++) {
                if ((map.getLayer(map.graphicsLayerIds[i])).name == "classGLayer") {
                    classLayer = map.getLayer(map.graphicsLayerIds[i]);
                    //取消事件绑定
                    /*if(mouseMoveClassLyr!=undefined && mouseOutClassLyr!=undefined){
                        dojo.disconnect(mouseMoveClassLyr);
                        dojo.disconnect(mouseOutClassLyr);
                        // dojo.disconnect(mouseClickClassLyr);
                    }*/
                    var rgnCode = 0;
                    var polyColor;
                    var polyOutline;
                    var polyStyle;
                    dojo.connect(classLayer, "onMouseOver", function mouseMove(evt) {
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

                            //var fieldNameClass = (JSON.parse(classLayer.content)).statisticdata.fieldsName[0];
                            var content = initClassInfoTemplate(g.attributes);
                            var title = g.attributes.rgn_name;
                            map.infoWindow.setContent(content);
                            map.infoWindow.setTitle("分级图层信息");
                            map.infoWindow.show(evt.screenPoint,map.getInfoWindowAnchor(evt.screenPoint));
                            map.infoWindow.resize(200,300);
                            map.setMapCursor("pointer");
                        }
                        else {
                            return false;
                        }
                    });
                    dojo.connect(classLayer, "onMouseOut", function mouseOut(evt) {
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
                    // break;
                }
            }
            // return data;

        },
        error:function(){
            alert("sorry!")
        }
    });
}

function changeLayerOnZoom(thisLayer, tjType, regionParamVar) {
    // var thisLayerContent = JSON.stringify(thisLayer.content);
    var url = "./servlet/fileUploadServlet?allTjLayerContent=" + encodeURI(thisLayer.content);
    console.log(tjType);
    $.ajax({
        type: 'POST',
        url: url,
        async: "false",
        dataType: "json",
        // data:{"inputType":"test"},
        data: {"inputType": tjType, "regionParam": regionParamVar},
        success: function (data) {
            if (data.type==="chartLayer"){
                var xOffset = parseInt(JSON.parse(thisLayer.content).cartographydata.xoffset);
                var yOffset = parseInt(JSON.parse(thisLayer.content).cartographydata.yoffset);
                zoomGraphics = initChartLayer(data.charts, xOffset, yOffset);
                // var layer = thisZoomLayer;
                thisLayer.clear();//清空所有graphics
                // dojo.disconnect(mouseMove);
                // dojo.disconnect(mouseOut);
                for (var i=0;i<zoomGraphics.length;i++){
                    thisLayer.add(zoomGraphics[i]);
                }
                // thisLayer.content = allTjLayerContent;
                zoomOutFlag = 0;
            }
            else {
                zoomGraphics = initClassLayer(data.classDataArray);
                // console.log(zoomGraphics);
                // var layer = thisZoomLayer;
                thisLayer.clear();//清空所有graphics
                for (var i=0;i<zoomGraphics.length;i++){
                    thisLayer.add(zoomGraphics[i]);
                }
                // thisZoomLayer.content = allTjLayerContent;
                thisLayer.setOpacity(0.95);
                zoomOutFlag = 0;
            }
        }
    });
}

function onZoomInLevelAbove10(){
    // var classLayer;
    for (var i = 0; i < map.graphicsLayerIds.length; i++){
        if ((map.getLayer(map.graphicsLayerIds[i])).name == "classGLayer"){
            // regionParamVar = "2";
            thisZoomLayer = map.getLayer(map.graphicsLayerIds[i]);
            var thisLayerContent = map.getLayer(map.graphicsLayerIds[i]).content;
            console.log(thisLayerContent);
            zoomOutFlag = 1;
            changeLayerOnZoom(thisZoomLayer, "classLayerData", "2");

        }
        else if ((map.getLayer(map.graphicsLayerIds[i])).name == "chartGLayer"){
            // regionParamVar = "2";
            thisZoomLayer = map.getLayer(map.graphicsLayerIds[i]);
            var thisLayerContent = map.getLayer(map.graphicsLayerIds[i]).content;
            console.log(thisLayerContent);
            zoomOutFlag = 1;
            changeLayerOnZoom(thisZoomLayer, "chartLayerData", "2");
        }
    }
}

function onZoomInLevelBelow10() {
    for (var i = 0; i < map.graphicsLayerIds.length; i++){
        if ((map.getLayer(map.graphicsLayerIds[i])).name == "classGLayer"){
            // regionParamVar = "2";
            thisZoomLayer = map.getLayer(map.graphicsLayerIds[i]);
            var thisLayerContent = map.getLayer(map.graphicsLayerIds[i]).content;
            console.log(thisLayerContent);
            zoomOutFlag = 1;
            changeLayerOnZoom(thisZoomLayer, "classLayerData", "1");
        }
        else if ((map.getLayer(map.graphicsLayerIds[i])).name == "chartGLayer"){
            // regionParamVar = "2";
            thisZoomLayer = map.getLayer(map.graphicsLayerIds[i]);
            var thisLayerContent = map.getLayer(map.graphicsLayerIds[i]).content;
            console.log(thisLayerContent);
            zoomOutFlag = 1;
            changeLayerOnZoom(thisZoomLayer, "chartLayerData", "1");
        }
    }
}

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
    //dt测试卷
    // var chart_url = "./servlet/chartLayerFromAPIServlet?wc=" + WC + "&dc=" + DC + "&CHARTID=" + chartID
    //     + "&WIDTH=" + size + "&CHARTDATA=" + encodeURI(indi) + "&colorRampSchema=" + encodeURI(color)+
    //     "&regionParam=" +regionParam +"&year=" +year;
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
                    /*if(mouseMove!=undefined && mouseOut!=undefined){
                        dojo.disconnect(mouseMove);                               //dojo：事件监听
                        dojo.disconnect(mouseOut);
                        // dojo.disconnect(mouseClick);
                    }*/
                    var chartWidth=0;
                    var chartHeight = 0;
                    var chartImg = 0;
                    var rgnCode = 0;
                    dojo.connect(chartLayer, "onMouseOver", function mouseMove(evt) {
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
                    dojo.connect(chartLayer, "onMouseOut", function mouseOut(evt) {
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
                    // break;
                }
            }
        },
        error: function (xhr, status, errMsg) {
            alert('error');
            console.log(errMsg);
        }
    })

};
//动态生成graphic的弹出窗口
function initClassInfoTemplate(attributes) {
    // var attrString = classIndex + ":" + attributes.data;

    var attrString = '<p><strong>区域名称 : </strong>' + attributes.rgn_code + '</p>';
        attrString += '<p><strong>'+ attributes.label +' : </strong>' + attributes.data + '</p>';
    attrString += '<p><strong>分级级别 : </strong>' + attributes.rgn_class + '</p>';
    // attrString += '<p><strong>数据来源 : </strong>' + dataSource + '</p>';
    // classifyImg_url = "data:image/png;base64," + classLegend;
    // attrString += '<img src="'+url+'">';
    // var domObj = $("#color-selected .select_title img")[0];
    // var attrString = classIndex + ":" + attributes.data + "<br/>";
    // attrString += "注:单击可进入下一级行政区";
    // attrString.substring(0,attrString.length-5);
    return attrString;
}

//其他数据库
function OtherDatabase(){
    layui.use(["form","element"],function () {
        var form = layui.form,
            element = layui.element;
        //链接数据库
        form.on('submit(otherdatabase)', function (data) {
            layer.alert(JSON.stringify(data.field), {
                title: '最终的提交信息'
            });
            tjPanel2.dataAddress = data.field.dataAddress;
            $.ajax({
                type: 'post',
                url:"./servlet/fileUploadServlet",
                async:"false",
                dataType:"json",
                data:{"inputType":"APIData","apiUrl": data.field.dataAddress},
                success: function (data) { //返回tabletree
                    //alert(data);
                    var tableFields = data.apiCallbackData;
                    displayFields($("#fieldslist2"),tableFields);
                    submitFields();
                },
                error:function(){
                    alert("sorry!")
                }
            });

            //构造
            //var tableFields = {"0":"id","1":"username","2":"email","3":"sex","4":"city","5":"sign","6":"experience","7":"ip","8":"logins","9":"joinTime"};


            return false;
        });
    })
}

//上传EXCEL文件
function EXCELupload(){
    layui.use('upload', function(){
        var $ = layui.jquery
            ,upload = layui.upload;
        var uploadInst= upload.render({
            elem: '#EXCELupload1'
            ,url: './servlet/fileUploadServlet'
            ,auto: false
            ,accept: 'file' //普通文件
            ,exts: 'xlsx|xls' //文件类型
            ,bindAction: '#EXCELupload'
            ,done: function(res){
                console.log(res)
                //如果上传失败
                if(res.code > 0){
                    return layer.msg('上传失败');
                }
                //上传成功，渲染字段

                var tableFields=new Array();

                tjPanel2.tableName = $(".layui-inline.layui-upload-choose").html();

                tableFields=res["tableFields"];//tableFields为指标数组
                //构造
                //var tableFields = {"0":"id","1":"username","2":"email","3":"sex","4":"city","5":"sign","6":"experience","7":"ip","8":"logins","9":"joinTime"};

                displayFields($("#fieldslist3"),tableFields);

                //提交选择的字段
                submitFields();
            }
            ,error: function(){
                //演示失败状态，并实现重传
                var demoText = $('#demoText');
                demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
                demoText.find('.demo-reload').on('click', function(){
                    uploadInst.upload();
                });
            }
        });
    });

}

//基于geojson增加图层
function addGeojsonLayer(geojsonUrl) {
    require([
            "esri/layers/geojsonlayer",
        ],
        function ( GeoJsonLayer) {
            //判断是否有geojson图层，有的话删掉

            if (map.getLayer("geojsonLayerID")){
                map.removeLayer(map.getLayer("geojsonLayerID"))
            }

            addGeoJsonLayer(geojsonUrl);

            function addGeoJsonLayer(url) {
                // 创建图层
                var geoJsonLayer2 = new GeoJsonLayer({
                    id:"geojsonLayerID",
                    url: url
                });
                // 缩放至图层
                geoJsonLayer2.on("update-end", function (e) {
                    map.setExtent(e.target.extent.expand(1.2));
                });
                // 添加到地图
                map.addLayer(geoJsonLayer2);
            }
        });
}

// 统计图表颜色的json
var colorTable={
    "青黄色系":{
        "color0":"#ED1B24",
        "color1":"#262164",
        "color2":"#F2F101",
        "color3":"#21B24B",
        "color4":"#92278F",
        "color5":"#F18C24",
        "color6":"#FFE375",
        "color7":"#ABE94A",
        "color8":"#FBA723",
        "color9":"#D8350C",
    },
    "黄红色系":{
        "color0":"#CA513E",
        "color1":"#E56643",
        "color2":"#E98145",
        "color3":"#F0A54A",
        "color4":"#F6C55F",
        "color5":"#FBE474",
        "color6":"#FFFA8A",
        "color7":"#FFFCB0",
        "color8":"#FEFECA",
        "color9":"#FFFEE2",
    },
    "蓝色色系":{
        "color0":"#016E99",
        "color1":"#0082BC",
        "color2":"#0091D2",
        "color3":"#02A1E2",
        "color4":"#12AEE9",
        "color5":"#57BCEA",
        "color6":"#7FC8F1",
        "color7":"#A4D7F6",
        "color8":"#C4E6F9",
        "color9":"#E5F3FC",
    },
    "红色色系":{
        "color0":"#9E2B66",
        "color1":"#BC2F73",
        "color2":"#D52E7C",
        "color3":"#E54F8F",
        "color4":"#E96FA0",
        "color5":"#ED88B2",
        "color6":"#F2A1C2",
        "color7":"#F8BAD3",
        "color8":"#FBD2E4",
        "color9":"#FCEBF1",
    },
    "黄绿色系":{
        "color0":"#00935C",
        "color1":"#12A962",
        "color2":"#58B662",
        "color3":"#90C75F",
        "color4":"#BADA6B",
        "color5":"#E4ED7A",
        "color6":"#FFFA8A",
        "color7":"#FFFBB1",
        "color8":"#FFFDCA",
        "color9":"#FFFEE2",
    },
    "黄棕色系":{
        "color0":"#AAA71A",
        "color1":"#D1CA00",
        "color2":"#F0E800",
        "color3":"#FFF734",
        "color4":"#FFF86A",
        "color5":"#FEFA8C",
        "color6":"#FFFBA4",
        "color7":"#FFFCBD",
        "color8":"#FFFDD6",
        "color9":"#FFFEEC",
    }
}
function doChartLayer(data){
    indi = fieldsOrIndi;
    console.log(indi);
    chartImg_url= "data:image/png;base64," + data.chartLegend;
    // if(legendFlag!=0 && legendFlag == 'chart'){
    //     $("#chartLegend").click();                   //触发chartlegend的点击事件
    // }
    var dataSource = data.source;
    // $("#mapDiv .legend").css("background", "url(" + img_url + ")");
    // $("#mapDiv .legend").css("background-size", "100% 100%");
    //处理统计图表
    var charts = data.charts;
    var indiNum = charts[0].attributes.indiNum;//所选指标数目
    // console.log(charts);
    var graphics = new Array();
    var xOffset = parseInt(tjPanel3.xoffset);
    var yOffset = parseInt(tjPanel3.yoffset);

    graphics = initChartLayer(charts, xOffset, yOffset);
    // charts.forEach(alert);
    // if (map.graphicsLayerIds.length == 0) {
    //     //可以为graphicLayer添加mouse-over,click等事件;
    //     var graphicLayer = new esri.layers.GraphicsLayer();
    //     graphicLayer.name = "chartGLayer";
    //     //Graphic(geometry,symbol,attributes,infoTemplate)-->infoTemlate为弹出窗体,用以显示信息
    //     for (var i=0;i<graphics.length;i++){
    //         graphicLayer.add(graphics[i]);
    //     }
    //     map.addLayer(graphicLayer);
    // } else {
        var flag = 0;// 用于判断是否有画图图层
        for (var i = 0; i < map.graphicsLayerIds.length; i++) {
            if ((map.getLayer(map.graphicsLayerIds[i])).id == tjLayerName) {
                var layer = map.getLayer(map.graphicsLayerIds[i]);
                layer.clear();//清空所有graphics
                // dojo.disconnect(mouseMove);
                // dojo.disconnect(mouseOut);
                for (var i=0;i<graphics.length;i++){
                    layer.add(graphics[i]);
                }
                layer.content = allTjLayerContent;
                layer.legend = chartImg_url;
                // console.log(tjLayerName);
                // layer.setId = tjLayerName;
                flag = 0;
            }
            else// 第一个不是chart图层
            {
                flag = 1;
            }
        }
    //
        if (flag == 1)// 现有图层中没有画图图层
        {
            var graphicLayer = new esri.layers.GraphicsLayer();
            graphicLayer.name = "chartGLayer";
            graphicLayer.id = tjLayerName;
            graphicLayer.content = allTjLayerContent;
            graphicLayer.legend = chartImg_url;
            for (var i=0;i<graphics.length;i++){
                graphicLayer.add(graphics[i]);
            }
            map.addLayer(graphicLayer);
        }

    // }
    //添加鼠标响应事件
    var chartLayer;
    for (var i = 0; i < map.graphicsLayerIds.length; i++) {
        if ((map.getLayer(map.graphicsLayerIds[i])).name == "chartGLayer") {
            chartLayer = map.getLayer(map.graphicsLayerIds[i]);
            //取消事件绑定
            // if(mouseMove!=undefined && mouseOut!=undefined){
            //     dojo.disconnect(mouseMove);                               //dojo：事件监听
            //     dojo.disconnect(mouseOut);
            //     // dojo.disconnect(mouseClick);
            // }
            var chartWidth=0;
            var chartHeight = 0;
            var chartImg = 0;
            var rgnCode = 0;
            dojo.connect(chartLayer, "onMouseOver", function mouseMove(evt) {
                //这里动态赋予graphicinfoTemplate,如果在生成是就初始化会默认添加鼠标点击事件!!!
                var g = evt.graphic;
                if(rgnCode!= g.attributes.rng_code){
                    rgnCode = g.attributes.rng_code;
                    // console.log(g.symbol);
                    chartWidth = g.symbol.width;
                    chartHeight = g.symbol.height;
                    chartImg = g.symbol.url;
                    // var symbol = new esri.symbol.PictureMarkerSymbol(chartImg,chartWidth*1.15,chartHeight*1.15);
                    // if (parseInt(JSON.parse(chartLayer.content).cartographydata.xoffset) != 0 || parseInt(JSON.parse(chartLayer.content).cartographydata.yoffset) != 0)
                    //     symbol.setOffset(parseInt(JSON.parse(chartLayer.content).cartographydata.xoffset), parseInt(JSON.parse(chartLayer.content).cartographydata.yoffset));
                    //g.setSymbol(symbol);
                   /* g.symbol.height = 1.15*g.symbol.height;
                    g.symbol.width = 1.15*g.symbol.width;*/
                    var symbol=g.symbol;
                    symbol.height = 1.15*symbol.height;
                    symbol.width = 1.15*symbol.width;
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
            dojo.connect(chartLayer, "onMouseOut", function mouseOut(evt) {
                var g = evt.graphic;
                //var symbol = new esri.symbol.PictureMarkerSymbol(chartImg,chartWidth,chartHeight);
                // if (parseInt(JSON.parse(chartLayer.content).cartographydata.xoffset) != 0 || parseInt(JSON.parse(chartLayer.content).cartographydata.yoffset) != 0)
                    //symbol.setOffset(parseInt(JSON.parse(chartLayer.content).cartographydata.xoffset), parseInt(JSON.parse(chartLayer.content).cartographydata.yoffset));
                // console.log(symbol);
                var symbol=g.symbol;
                symbol.height = symbol.height/1.15;
                symbol.width = symbol.width/1.15;
                g.setSymbol(symbol);
                map.infoWindow.hide();
                map.setMapCursor("default");
                rgnCode = 0;
            });

            // break;
        }
    }
}
//根据后台传输回来的数据进行graphic的生成,并进行ChartLayer的添加
function initChartLayer (charts, xoffset, yoffset) {
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
            symbol.setOffset(xoffset, yoffset);
            var attributes = charts[i].attributes;
            var graphic = new esri.Graphic(point,symbol,attributes);
            // graphic.setInfoTemplate(infoTemplate);
            graphicArray.push(graphic);
        }
    });
    return graphicArray;
}
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

var originalTjLayerContent='<div class="tjPanel" id="tjPanel">\n' +
    '    <div class="tjPanel-leftBar">\n' +
    '        <ul class="layui-nav layui-nav-tree layui-inline" lay-filter="navDemo" >\n' +
    '            <li class="layui-nav-item layui-this" name="selectStatistics">\n' +
    '                <a href="javascript:;" name="selectStatistics" id="selectStatistics">选择统计数据</a>\n' +
    '            </li>\n' +
    '            <li class="layui-nav-item" name="selectMappingTemplate">\n' +
    '                <a href="javascript:;" name="selectMappingTemplate" id="selectMappingTemplate">选择制图符号</a>\n' +
    '            </li>\n' +
    '        </ul>\n' +
    '    </div>\n' +
    '    <div class="tjPanel-content">\n' +
    '        <div id="tjPanel-content2" style="display:block">\n' +
    '            <div class="layui-tab layui-tab-brief" lay-filter="nav2">\n' +
    '                <ul class="layui-tab-title">\n' +
    '                    <li class="layui-this" id="tab1">平台数据库</li>\n' +
    '                    <li id="tab2">API数据</li>\n' +
    '                    <li id="tab3">上传EXCEL文件</li>\n' +
    '                </ul>\n' +
    '                <div class="layui-tab-content">\n' +
    '                    <div class="layui-tab-item layui-show">\n' +
    '                        <form class="layui-form" action="" lay-filter="">\n' +
    '                            <div class="layui-form-item">\n' +
    '                                <div class="layui-row layui-col-space10">\n' +
    '                                    <div class="layui-col-md3">\n' +
    '                                        <fieldset class="layui-elem-field">\n' +
    '                                            <legend class="Panel2_Legend" style="font-size:14px">平台数据库</legend>\n' +
    '                                            <div class="layui-field-box" style="padding: 5px;">\n' +
    '                                                <div class="layui-row layui-col-space10">\n' +
    '                                                    <div class="layui-col-md12">\n' +
    '                                                        <ul class="tabletree" style="text-align:center" id="treedemo1"></ul>\n' +
    '                                                    </div>\n' +
    '                                                </div>\n' +
    '                                            </div>\n' +
    '                                        </fieldset>\n' +
    '                                    </div>\n' +
    '                                    <div class="layui-col-md9">\n' +
    '                                        <fieldset class="layui-elem-field" style="text-align:center">\n' +
    '                                            <legend class="Panel2_Legend" style="font-size:14px">数据表可选字段</legend>\n' +
    '                                            <form class="layui-form" action="" lay-filter="">\n' +
    '                                                <div class="layui-form-item">\n' +
    '                                                    <label class="layui-form-label" style="width:unset;margin-left: 10%;">空间唯一标识</label>\n' +
    '                                                    <div class="layui-input-inline">\n' +
    '                                                        <select class="spatialId" name="spatialId" lay-filter="spatialId" id="spatialId1"></select>\n' +
    '                                                    </div>\n' +
    '                                                </div>\n' +
    '                                                <div class="layui-form-item">\n' +
    '                                                    <label class="layui-form-label" style="width:unset;margin-left:10%">时间唯一标识</label>\n' +
    '                                                    <div class="layui-input-inline">\n' +
    '                                                        <select class="timeId" name="timeId" lay-filter="timeId"\n' +
    '                                                                id="timeId1">\n' +
    '                                                            <option value="2016">2016</option>\n' +
    '                                                            <option value="2017">2017</option>\n' +
    '                                                            <option value="2018">2018</option>\n' +
    '                                                        </select>\n' +
    '                                                    </div>\n' +
    '                                                </div>'+
    '                                                <div class="layui-form-item" id="fieldslist1"></div>\n' +
    '                                                <div class="layui-form-item">\n' +
    '                                                    <button class="layui-btn" lay-submit="" lay-filter="fields">下一步</button>\n' +
    '                                                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>\n' +
    '                                                </div>\n' +
    '                                            </form>\n' +
    '                                        </fieldset>\n' +
    '                                    </div>\n' +
    '                                </div>\n' +
    '                            </div>\n' +
    '                        </form>\n' +
    '                    </div>\n' +
    '                    <div class="layui-tab-item" layui-filter="tjPanel22">\n' +
    '                        <form class="layui-form" action="" lay-filter="OtherDatabase1" id="OtherDatabase1">\n' +
    '                            <div class="layui-form-item" style="text-align:center">\n' +
    '                                <input type="text" name="dataAddress" lay-verify="dataAddress" autocomplete="off" placeholder="请输入数据链接地址" class="layui-input" style="width: 82%;display: unset;">\n' +
    '                                <button class="layui-btn" lay-submit="" lay-filter="otherdatabase" id="chooseDatabase">链接数据</button>\n' +
    '                            </div>\n' +
    '                        </form>\n' +
    '                        <form class="layui-form" action="" lay-filter="OtherDatabase2" id="OtherDatabase2">\n' +
    '                            <div class="layui-form-item">\n' +
    '                                <div class="layui-row layui-col-space10">\n' +
    '                                    <fieldset class="layui-elem-field" style="text-align:center">\n' +
    '                                        <legend class="Panel2_Legend" style="font-size:14px">数据表可选字段</legend>\n' +
    '                                        <form class="layui-form" action="" lay-filter="">\n' +
    '                                            <div class="layui-form-item">\n' +
    '                                                <label class="layui-form-label" style="width:unset;margin-left: 20%;">空间唯一标识</label>\n' +
    '                                                <div class="layui-input-inline">\n' +
    '                                                    <select class="spatialId" name="spatialId" lay-filter="spatialId" id="spatialId2"></select>\n' +
    '                                                </div>\n' +
    '                                            </div>\n' +
    '                                            <div class="layui-form-item">\n' +
    '                                                    <label class="layui-form-label" style="width:unset;margin-left:20%">时间唯一标识</label>\n' +
    '                                                    <div class="layui-input-inline">\n' +
    '                                                        <select class="timeId" name="timeId" lay-filter="timeId"\n' +
    '                                                                id="timeId2">\n' +
    '                                                            <option value="2016">2016</option>\n' +
    '                                                            <option value="2017">2017</option>\n' +
    '                                                            <option value="2018">2018</option>\n' +
    '                                                        </select>\n' +
    '                                                    </div>\n' +
    '                                                </div>'+
    '                                            <div class="layui-form-item" id="fieldslist2"></div>\n' +
    '                                            <div class="layui-form-item">\n' +
    '                                                <button class="layui-btn" lay-submit="" lay-filter="fields">下一步</button>\n' +
    '                                                <button type="reset" class="layui-btn layui-btn-primary">重置</button>\n' +
    '                                            </div>\n' +
    '                                        </form>\n' +
    '                                    </fieldset>\n' +
    '                                </div>\n' +
    '                            </div>\n' +
    '                        </form>\n' +
    '                    </div>\n' +
    '                    <div class="layui-tab-item">\n' +
    '                        <form class="layui-form">\n' +
    '                            <div class="layui-upload-drag" style="margin-bottom: 10px;width: 400px;" id="EXCELupload1">\n' +
    '                                <i class="layui-icon layui-icon-upload-drag"></i>\n' +
    '                                <p id="fileName">将文件拖拽到此处</p>\n' +
    '                            </div>\n' +
    '                            <button id="EXCELupload" type="button" class="layui-btn layui-input-inline" style="top:20px">开始上传</button>\n' +
    '                        </form>\n'+
    '                        <form class="layui-form">\n' +
    '                            <div class="layui-form-item">\n' +
    '                                <div class="layui-row layui-col-space10">\n' +
    '                                    <fieldset class="layui-elem-field" style="text-align:center">\n' +
    '                                        <legend class="Panel2_Legend" style="font-size:14px">Excel表可选字段</legend>\n' +
    '                                        <form class="layui-form" action="" lay-filter="">\n' +
    '                                            <div class="layui-form-item">\n' +
    '                                                <label class="layui-form-label" style="width:unset;margin-left: 20%;">空间唯一标识</label>\n' +
    '                                                <div class="layui-input-inline">\n' +
    '                                                    <select class="spatialId" name="spatialId" lay-filter="spatialId" id="spatialId3"></select>\n' +
    '                                                </div>\n' +
    '                                            </div>\n' +
    '                                            <div class="layui-form-item ">\n' +
    '                                                    <label class="layui-form-label" style="width:unset;margin-left:20%">时间唯一标识</label>\n' +
    '                                                    <div class="layui-input-inline">\n' +
    '                                                        <select class="timeId" name="timeId" lay-filter="timeId"\n' +
    '                                                                id="timeId3">\n' +
    '                                                            <option value="2016">2016</option>\n' +
    '                                                            <option value="2017">2017</option>\n' +
    '                                                            <option value="2018">2018</option>\n' +
    '                                                        </select>\n' +
    '                                                    </div>\n' +
    '                                                </div>'+
    '                                            <div class="layui-form-item" id="fieldslist3"></div>\n' +
    '                                            <div class="layui-form-item">\n' +
    '                                                <button class="layui-btn" lay-submit="" lay-filter="fields">下一步</button>\n' +
    '                                                <button type="reset" class="layui-btn layui-btn-primary">重置</button>\n' +
    '                                            </div>\n' +
    '                                        </form>\n' +
    '                                    </fieldset>\n' +
    '                                </div>\n' +
    '                            </div>\n' +
    '                        </form>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </div>\n' +
    '        <div id="tjPanel-content3" style="display: none" isloaded="false" >\n' +
    '            <fieldset class="layui-elem-field layui-field-title" style="margin-top:20px;margin-bottom: 5px">\n' +
    '                <legend class="symbolLegend">统计符号</legend>\n' +
    '            </fieldset>\n' +
    '            <div style="padding: 15px;">\n' +
    '                <div class="layui-row layui-col-space15">\n' +
    '                    <div class="layui-card">\n' +
    '                        <div class="layui-card-body chartSymbolCardbody">\n' +
    '                            <h6>图表类型：</h6>\n' +
    '                            <div class="sym-selected" id="chart-selected">\n' +
    '                                <div class="select_title">\n' +
    '                                    <img src="assets/imgs/chartIcon/010101.png">\n' +
    '                                </div>\n' +
    '                                <ul class="select_content">\n' +
    '                                </ul>\n' +
    '                            </div>\n' +
    '                            <h6>色彩方案：</h6>\n' +
    '                            <div class="sym-selected" id="color-solution">\n' +
    '                                <div class="select_title">\n' +
    '                                    <img name="青黄色系" src="assets/imgs/gradeIcon/10/6.jpg" style="width: 100%">\n' +
    '                                </div>\n' +
    '                                <ul class="select_content">\n' +
    '                                </ul>\n' +
    '                            </div>\n' +
    '                            <div class="userDefineColors">\n' +
    '                                <label class="selectedStaIndexsLabel">男生人数</label>\n' +
    '                                <div id="test3"></div>\n' +
    '                            </div>\n' +
    '                            <h6>符号大小：</h6>\n' +
    '                            <div id="symbolSize"></div>\n' +
    '                            <h6 style="display: none">透明度：</h6>\n' +
    '                            <div id="symbolOpacity1" style="display: none"></div>\n' +
    '                            <h6 style="margin-top: 10px">偏移量：</h6>'+
    '                            <div class="chartSymbolOffsetItem">' +
    '                            <div class="layui-form-item offsetItem">\n' +
    '                                 <label class="layui-form-label">X偏移量：</label>\n' +
    '                                 <div class="layui-input-block">\n' +
    '                                     <input type="text" name="x-offset"  id="xOffset" lay-verify="required"  autocomplete="off" class="layui-input" value="0">\n' +
    '                                 </div>\n' +
    '                            </div>\n' +
    '                            <div class="layui-form-item offsetItem">\n' +
    '                                <label class="layui-form-label">Y偏移量：</label>\n' +
    '                                <div class="layui-input-block">\n' +
    '                                     <input type="text" name="y-offset" id="yOffset" lay-verify="required"  autocomplete="off" class="layui-input" value="0">\n' +
    '                               </div>\n' +
    '                            </div>'+
    '                            </div>'+
    '                        </div>\n' +
    '                    </div>\n' +
    '                    <div id="symbolBtn1" style="float:right">\n' +
    '                        <button class="layui-btn tjInfoSubmit">确定</button>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </div>\n' +
    '        <div id="tjPanel-content4" style="display: none" isloaded="false" >\n' +
    '            <fieldset class="layui-elem-field layui-field-title" style="margin-top:20px;margin-bottom: 5px">\n' +
    '                <legend class="symbolLegend">分级符号</legend>\n' +
    '            </fieldset>\n' +
    '            <div style="padding: 15px;" class="layui-form">\n' +
    '                <div class="layui-row layui-col-space15">\n' +
    '                    <div class="layui-card">\n' +
    '                        <div class="layui-card-body chartSymbolCardbody">\n' +
    '                            <h6>分级数量：</h6>\n' +
    '                            <div id="classNum"></div>\n' +
    '                            <h6>分级色系：</h6>\n' +
    '                            <div class="sym-selected" id="color-selected">\n' +
    '                                <div class="select_title">\n' +
    '                                    <img style="width:100%;" color1="#FFFEE3" color2="#00935B"\n' +
    '                                         src="assets/imgs/gradeIcon/10/4.jpg">\n' +
    '                                </div>\n' +
    '                                <ul class="select_content" style="width: 100%">\n' +
    '                                </ul>\n' +
    '                            </div>\n' +
    '                            <div class="graduatedColorInverseInput">\n' +
    '                                <input type="checkbox" name="graduatedColorInverse" lay-skin="primary" title="反色"\n' +
    '                                       id="isColorInverse">\n' +
    '                            </div>\n' +
    '                            <h6>分级模型：</h6>\n' +
    '                            <div class="layui-form-item">\n' +
    '                                <select id="model" class="form-control" name="graduatedModel"\n' +
    '                                        lay-filter="graduatedModel">\n' +
    '                                    <option value="界限等分模型">界限等分模型</option>\n' +
    '                                    <option value="间隔等分模型">间隔等分模型</option>\n' +
    '                                    <option value="界限等比模型">界限等比模型</option>\n' +
    '                                    <option value="间隔等比模型">间隔等比模型</option>\n' +
    '                                </select>\n' +
    '                            </div>\n' +
    '                            <h6 style="display: none">透明度：</h6>\n' +
    '                            <div id="symbolOpacity2" style="display: none"></div>\n' +
    '                        </div>\n' +
    '                    </div>\n' +
    '                    <div id="symbolBtn" style="float: right;">\n' +
    '                        <button class="layui-btn tjInfoSubmit">确定</button>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </div>\n' +
    '    </div>\n' +
    '</div>';









