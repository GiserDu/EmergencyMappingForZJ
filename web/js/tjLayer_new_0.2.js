var layerIndex;
var symbolSizeSliderValue=0;
var symbolOpacitySliderValue=0;
var classNumSliderValue=5;

var userLoadSpfilename;  //自定义上传空间数据的文件名


var allTjLayerContent={};
// 制图范围数据的json格式
// var tjPanel1={"tabID":"1","identityField":"value","regionData":"","fileName":""};
var tjPanel1={};
var tjPanel2 = {"tabId":"1","dataAddress":"","tableName":"","spatialId":"","fieldsName":{},"fieldsNum":""};
//tjPanel3中有一个key是type，1表示统计图表，2表示分级符号
var tjPanel3={};



function opentjMenuLayer() {
    layui.use(['layer','form','element'],function () {
        var layer = layui.layer
            ,element = layui.element
            ,form=layui.form;

        layerIndex=layer.open({
            type: 1,
            title: ['添加统计图层'],
            shadeClose: false,
            skin:"layui-layer-lan tjLayerContent",
            shade: 0,
            area:['700px','480px'],
            // area:['600px','370px'],
            content:$('#tjPanel'),
            // yes: function(index, layero){
            //     //do something
            //     console.log("hh");
            //     $("#tjPanel").removeClass('layui-hide').addClass('layui-show');
            // }
        });

        multiSelectRender('selectRegion');
        userLoadSpatialData();

        element.on('nav(navDemo)', function(elem){
            console.log(elem);
            var leftMenuName=elem.attr('name');
            var selectedIndexNum;

            if(leftMenuName=="selectMappingRange"){
                // $(".tjPanel-content").html(html1);
                $("#tjPanel-content1").show();
                $("#tjPanel-content2").hide();
                $("#tjPanel-content3").hide();
                $("#tjPanel-content4").hide();

                // 省、市、区联动选择框的重新渲染
                multiSelectRender('selectRegion');
                userLoadSpatialData();
                form.render('select');

            }else if (leftMenuName=="selectStatistics") {
                // $(".tjPanel-content").html(html2);
                $("#tjPanel-content1").hide();
                $("#tjPanel-content2").show();
                $("#tjPanel-content3").hide();
                $("#tjPanel-content4").hide();
                opentjPanel2();
                form.render();

            } else if(leftMenuName=="selectMappingTemplate") {

                selectedIndexNum=tjPanel2.fieldsNum;

                if (selectedIndexNum==0){
                    alert('请选择统计指标');

                } else if(selectedIndexNum==1){
                    // $(".tjPanel-content").html(html4);

                    $("#tjPanel-content1").hide();
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
                    renderSlider();
                    form.render();

                    clickAndLoadAllInfo();


                }else {
                    // $(".tjPanel-content").html(html3);
                    $("#tjPanel-content1").hide();
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
                    renderSlider();
                    form.render();
                    userDefineChartColor();
                    clickAndLoadAllInfo();

                }
            }
        });
        // element.render('nav');
    })
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
        var modelName=$('#model option:selected').val();
        tjPanel3={
            "type":"2",
            "classNumSliderValue":classNumSliderValue,
            "colors":colors,
            "modelName":modelName,
            "symbolOpacitySliderValue":symbolOpacitySliderValue
        }
    } else if(selectedIndexNum>1){
        // -----获得用户选择的有关统计符号的值-----
        var chartID = $("#chart-selected>.select_title>img").attr("src").slice(-10,-4);
        var colors=[];
        // 获得所有颜色选择器
        var chartIndexColorpick=$(".userDefineColors").find('.chartColorPicker');
        for(var i=0;i<chartIndexColorpick.length;i++){
            colors[i]=$(".userDefineColors").find('.chartColorPicker').find('.layui-colorpicker-trigger-span')[i].style.backgroundColor;
        }
        tjPanel3={
            "type":"1",
            "chartID":chartID,
            "colors":colors,
            "symbolSizeSliderValue":symbolOpacitySliderValue,
            "symbolOpacitySliderValue":symbolOpacitySliderValue
        }
    }

    console.log(tjPanel3);
}

function clickAndLoadAllInfo() {
    $(".tjInfoSubmit").bind('click',function () {
        constructTjJson3();
        var index=layer.open({
            type: 0,
            title:"统计图层名称",
            skin:"layui-layer-lan",
            content:' <div style="margin-left:-24px">\n' +
            '             <label class="layui-form-label">图层名</label>\n' +
            '             <div class="layui-input-block" style="margin-left: 88px">\n' +
            '                  <input type="text" name="tjLayerName" lay-verify="required" placeholder="请输入统计图层名称" autocomplete="off" class="layui-input">\n' +
            '             </div>\n' +
            '          </div>',
            yes:function (index,layero) {
                console.log("OK");

                var tjLayerName=$("input[ name='tjLayerName' ]").val();

                if(tjLayerName==""){
                    alert("请输入名称");
                }

                allTjLayerContent={
                    "name":tjLayerName,
                    "spatialdata":tjPanel1,
                    "statisticdata":tjPanel2,
                    "cartographydata":tjPanel3
                }
                console.log( allTjLayerContent);
                layer.close(index);
                layer.close(layerIndex);
            }
        });
    });
}

// 省市区县多级联动的渲染
function multiSelectRender(selectName) {
    var formSelects = layui.formSelects;

    formSelects.config(selectName, {
        direction: "down",
        success: function(id, url, val, result){
            console.log("success回调: " + url);
            // console.log(formSelects.config.height);
        },
        error: function(id, url, val, err){
            console.log("err回调: " + url);
        }
    });

    formSelects.data(selectName, 'server', {
        url: 'http://yapi.demo.qunar.com/mock/9813/layui/citys',
        linkage: true,
        linkageWidth: 130
    });

    //监听下拉框的打开
    formSelects.opened(selectName, function(id){
        var selectTitleHeight=$(".xm-select-title").height();
        if (selectTitleHeight>36){
            var layerHeight=470+(selectTitleHeight-36)+'px';
            var layerContentHeight=428+(selectTitleHeight-36)+'px';
        }
        layer.style(layerIndex, {
            // width: '600px',
            height: layerHeight,
        });
        $(".layui-layer-content").css("height",layerContentHeight);
    });

    //监听下拉框的关闭
    formSelects.closed(selectName, function(id){
        var layerHeight=470+'px';
        var layerContentHeight=428+'px';
        layer.style(layerIndex, {
            height: layerHeight,
        });
        $(".layui-layer-content").css("height",layerContentHeight);
    });

    formSelects.on(selectName, function(id, vals, val, isAdd, isDisabled){
        var selectTitleHeight=$(".xm-select-title").height();
        var optionPanelHeight=$('.xm-select-linkage').height();
        // 监听面板打开或关闭时的高度变化
        if(optionPanelHeight!=0 && selectTitleHeight>36){
            var layerHeight=470+(selectTitleHeight-36)+'px';
            var layerContentHeight=428+(selectTitleHeight-36)+'px';
        }else {
            layerHeight=470+'px';
            layerContentHeight=428+'px';
        }
        layer.style(layerIndex, {
            // width: '700px',
            height: layerHeight,
        });
        $(".layui-layer-content").css("height",layerContentHeight);
    }, true);
}

// 文件上传
function userLoadSpatialData() {
    layui.use(['upload','form','layer'], function(){
        var index;
        var form=layui.form
            ,$ = layui.jquery
            ,layer=layui.layer
            ,upload = layui.upload;
        var uploadInst= upload.render({
            elem: '#shpFileUploadControl'
            ,url: './servlet/fileUploadServlet'
            ,auto: false
            ,accept: 'file' //普通文件
            ,exts: 'zip|rar' //只允许上传压缩文件
            ,bindAction: '#shpLoadConfirmBtn'
            ,choose:function (obj) {
                obj.preview(function (index, file, result) {
                    userLoadSpfilename=file.name;
                    var fileNameSpan="<span class=\"layui-inline layui-upload-choose\">"+userLoadSpfilename+"</span>";
                    $("#userDataField").find('input').after(fileNameSpan);
                    // console.log(file.name); //得到文件对象
                })
            }
            ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
                index=layer.load(); //上传loading
            }
            ,done: function(res){
                console.log(res);
                alert(res["message"]+":"+res["saveFilePath"]);
                layer.close(index);
                console.log(res["geoJsonURL"]);
                // alert(res["fieldsName"]);

                var geojsonUrl=res["geoJsonURL"];
                addGeojsonLayer(geojsonUrl);
                var fieldNames= new Array();
                fieldNames=res["fieldsName"].split(",");
                var optionHtml="";
                for (var fieldNum=0;fieldNum<fieldNames.length;fieldNum++){
                    optionHtml=optionHtml+"<option value=\""+fieldNames[fieldNum] +"\">"+fieldNames[fieldNum]+"</option>"
                }
                var spatialID= '<div class="layui-form-item" style="margin-top: 15px;">\n' +
                    '                  <label class="layui-form-label" style="line-height: 41px;width: 93px;padding: 0">空间标识字段</label>\n' +
                    '                  <div class="layui-input-block">\n' +
                    '                        <select name="userDataField" id="userDataField">\n' +
                                            optionHtml+
                    '                         </select>\n' +
                    '                   </div>\n' +
                    '            </div>\n' +
                    '            <div>\n' +
                    '                    <button type="button" class="layui-btn" id="comfirmUserSpDataBtn" onclick="constructTjJson12()">下一步</button>' +
                    '            </div>\n';
                // 先删除原有的select,再添加新的select
                $('#shpLoadConfirmBtn').parent().nextAll().remove();
                $('#shpLoadConfirmBtn').parent().after(spatialID);
                form.render('select');
            }
            ,error:function () {
                layer.close(index);
            }
        });
    })
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
                '<li><img  style="width:100%;" name="'
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
    var length=0;
    for(var a in selectedStatisticIndex){
        length++;
    }
    console.log(length);

    layui.use('colorpicker',function () {

        var $ = layui.$
            ,colorpicker = layui.colorpicker;

        $('.userDefineColors').empty();

        for(var i=0;i<length;i++){
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
            ,input: true //输入框
            ,change:function (value) {
                symbolSizeSliderValue=value;
            }
        });

        slider.render({
            elem: '#symbolOpacity1'
            ,input: true //输入框
           ,change:function (value) {
               symbolOpacitySliderValue=value;
           }
        });

        slider.render({
            elem: '#symbolOpacity2'
            ,input: true //输入框
            ,change:function (value) {
                symbolOpacitySliderValue=value;
            }
        });

       slider.render({
            elem: '#classNum'
            ,input: true //输入框
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
    layui.use(['element'], function(){
        var element = layui.element;
        getTableTree();
        submitFields();
        element.on('tab(nav2)', function(data){
            //console.log(this); //当前Tab标题所在的原始DOM元素
            //console.log(data.index); //得到当前Tab的所在下标
            //console.log(data.elem); //得到当前的Tab大容器
            if((data.index+1)==1){
                tjPanel2.tabId = 1;
                getTableTree();
                submitFields();
            }else if((data.index+1)==2){
                tjPanel2.tabId = 2;
                OtherDatabase();
                tjPanel2.tableName = "";
            }else if((data.index+1)==3){
                tjPanel2.tabId = 3;
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
    var fieldslist;
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
                //console.log(node.name);
                tjPanel2.tableName = node.name;
                // $.ajax({
                //     type: 'post',
                //     url:"",
                //     async:"false",
                //     data:{"tableName":node.name,"inputType":"dataFromLocalDb"},
                //     success: function (data) { //返回json结果(表头)
                //         //tableFields = JSON.parse(data);
                //         //tableFields = $.parseJSON(data);
                //         displayFields(fieldslist,tableFields);
                //     },
                //     error:function(){
                //         alert("sorry!")
                //     }
                // });
                //构造
                var tableFields1 = {"0":"id","1":"username","2":"email","3":"sex","4":"city","5":"sign","6":"experience","7":"ip","8":"logins","9":"joinTime"};
                var tableFields2 = {"0":"id","1":"username","2":"sex","3":"city","4":"sign","5":"experience","6":"logins","7":"wealth","8":"classify","9":"score"};
                if (node.name=="表格1"){
                    tableFields=tableFields1;
                }else if (node.name=="表格2"){
                    tableFields=tableFields2;
                }else{
                    tableFields={};
                }
                displayFields($("#fieldslist1"),tableFields);
            }
        });
    })
}

//渲染字段
function displayFields(fieldslist,tableFields){
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
            layer.alert(JSON.stringify(data.field), {
                title: '最终的提交信息'
            });
            tjPanel2.fieldsNum = 0;
            tjPanel2.fieldsName = {};
            $.each(data.field, function(index,item){
                //console.log(index,item);
                if(tjPanel2.fieldsNum!=0){
                    tjPanel2.fieldsName[tjPanel2.fieldsNum] = item;
                }
                tjPanel2.fieldsNum++;
            });
            tjPanel2.fieldsNum = tjPanel2.fieldsNum - 1;
            $.ajax({
                type: 'post',
                url:"",
                async:"false",
                data:data.field,
                success: function (data) {
                    //alert(data);
                    // var tableFields = data;
                },
                error:function(){
                    alert("sorry!")
                }
            });
            console.log(tjPanel2);
            return false;
        });
    })
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
            tjPanel2.dataAddress = data.field.address;
            $.ajax({
                type: 'post',
                url:"",
                async:"false",
                data:data.field,
                success: function (data) { //返回tabletree
                    //alert(data);
                    // var tableFields = data;
                },
                error:function(){
                    alert("sorry!")
                }
            });

            //构造
            var tableFields = {"0":"id","1":"username","2":"email","3":"sex","4":"city","5":"sign","6":"experience","7":"ip","8":"logins","9":"joinTime"};

            displayFields("#fieldslist2",tableFields);
            submitFields();
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
                var tableFields = {"0":"id","1":"username","2":"email","3":"sex","4":"city","5":"sign","6":"experience","7":"ip","8":"logins","9":"joinTime"};

                displayFields("#fieldslist3",tableFields);

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














