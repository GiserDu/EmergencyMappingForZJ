var layerIndex;
var selectedIndexNum;
var symbolSizeSliderValue=0;
var symbolOpacitySliderValue=0;
var classNumSliderValue=5;
var modelName;

// 假设用户选中的统计指标是以下数组
var selectedStatisticIndex=["男生人数","女生人数","老人人数","小孩人数"];

function opentjMenuLayer() {
    layui.use(['layer','form','element','colorpicker'],function () {
        var layer = layui.layer
            ,element = layui.element
            ,form=layui.form
            ,$ = layui.$
            ,colorpicker = layui.colorpicker;

        layerIndex=layer.open({
            type: 1,
            title: ['添加统计图层'],
            shadeClose: false,
            skin:"layui-layer-lan",
            shade: 0,
            area:['700px','480px'],
            // area:['600px','370px'],
            content:'<div class="tjPanel">\n' +
            '    <div class="tjPanel-leftBar">\n' +
            '        <ul class="layui-nav layui-nav-tree layui-inline" lay-filter="navDemo" >\n' +
            '            <li class="layui-nav-item layui-this" name="selectMappingRange">\n' +
            '                <a href="javascript:;" name="selectMappingRange">选择制图范围</a>\n' +
            '            </li>\n' +
            '            <li class="layui-nav-item" name="selectStatistics">\n' +
            '                <a href="javascript:;" name="selectStatistics">选择统计数据</a>\n' +
            '            </li>\n' +
            '            <li class="layui-nav-item" name="selectMappingTemplate">\n' +
            '                <a href="javascript:;" name="selectMappingTemplate">选择制图符号</a>\n' +
            '            </li>\n' +
            '        </ul>\n' +
            '    </div>\n' +
            '    <div class="tjPanel-content">\n' +
            '        <div class="layui-tab layui-tab-brief">\n' +
            '            <ul class="layui-tab-title">\n' +
            '                <li class="layui-this">载入行政区划</li>\n' +
            '                <li>上传自定义数据</li>\n' +
            '            </ul>\n' +
            '            <div class="layui-tab-content" style="font-size: 14px">\n' +
            '                <div class="layui-tab-item layui-show">\n' +
            '                    <div class="layui-form regionSelectItem">\n' +
            '                        <div class="layui-form-item ">\n' +
            '                            <label class="layui-form-label select-region-label">选择区域</label>\n' +
            '                            <div class="layui-input-block">\n' +
            '                                <select name="city" xm-select="selectRegion">\n' +
            '                                    <option value="">请选择</option>\n' +
            '                                </select>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                        <div class="layui-form-item ">\n' +
            '                            <div class="adminSelectTips">\n' +
            '                                <span>提示：单击进入下一级，双击确认选择</span>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                        <div class="layui-form-item">\n' +
            '                            <div class="layui-input-block  admin-form-btn-group">\n' +
            '                                <button class="layui-btn layui-btn-default admin-form-btn" onclick="getSpatialData()">下一步</button>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                </div>\n' +
            '                <div class="layui-tab-item">\n' +
            '                    <div class="layui-form" lay-filter="userDataField" id="userDataField">'+
            '                    <div id="shpFileUploadControl" class="layui-upload-drag">\n' +
            '                        <i class="layui-icon layui-icon-upload-drag"></i>\n' +
            '                        <p>点击或拖拽上传，请以压缩文件的形式上传</p>\n' +
            '                    </div>\n' +
            '                    <div>\n' +
            '                        <button type="button" class="layui-btn layui-btn-primary" id="shpLoadConfirmBtn">上传</button>' +
            '                    </div>\n' +
            '                    </div>'+
            '                </div>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '    </div>\n' +
            '</div>\n',
        });
        multiSelectRender('selectRegion');
        userLoadSpatialData();

        element.on('nav(navDemo)', function(elem){
            var leftMenuName=elem.attr('name');
            if(leftMenuName=="selectMappingRange"){
                $(".tjPanel-content").html(html1);
                // 省、市、区联动选择框的重新渲染
                multiSelectRender('selectRegion');
                userLoadSpatialData();
                form.render('select');

            }else if (leftMenuName=="selectStatistics") {
                $(".tjPanel-content").html(html2);
                opentjPanel2();
                form.render();


                // $(".tjPanel-content").html(html3);
                // initTjSymbol();
                // renderSlider();
                // form.render();
                //
                // $("#tjInfoSubmit").bind('click',function () {
                //     var chartSymbolValues=getchartSymbolValues();
                //     // console.log(a);
                // });

            } else if(leftMenuName=="selectMappingTemplate") {

                if (selectedIndexNum==0){
                    alert('请选择统计指标');

                } else if(selectedIndexNum==1){
                    $(".tjPanel-content").html(html3);
                    initTjSymbol();
                    renderSlider();
                    form.render();
                    $("#tjInfoSubmit").bind('click',function () {
                        var chartSymbolValues=getchartSymbolValues();
                        // console.log(a);
                    })

                }else {
                    $(".tjPanel-content").html(html4);
                    initTjSymbol();
                    renderSlider();
                    form.on('select(graduatedModel)', function(data){
                        modelName=data.value;
                    });
                    form.render();
                    $("#tjInfoSubmit").bind('click',function () {
                        var graduatedSymbolValues=getgraduatedSymbolValues();
                        console.log(graduatedSymbolValues);
                    });

                }
            }
        });
        element.render('nav');
    })
}

// 获取用户选择的行政区划
// 点击确定按钮后执行这个函数，后面可能要用ajax传值给后台
function getSpatialData() {
    // 获取名字
    // var selectedNum=$("xm-select").attr('title');
    // console.log(selectedNum);

    // 获取value值
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
    layui.use(['upload','form'], function(){
        var form=layui.form;
        var $ = layui.jquery
            ,upload = layui.upload;
        var uploadInst= upload.render({
            elem: '#shpFileUploadControl'
            ,url: '/upload/'
            ,auto: false
            ,accept: 'file' //普通文件
            ,exts: 'zip|rar' //只允许上传压缩文件
            ,bindAction: '#shpLoadConfirmBtn'
            ,done: function(res){
                console.log(res);
                // var spatialID= '<div class="layui-form-item" style="margin-top: 15px;">\n' +
                //     '                  <label class="layui-form-label" style="line-height: 41px;width: 93px;padding: 0">空间标识字段</label>\n' +
                //     '                  <div class="layui-input-block">\n' +
                //     '                        <select name="userDataField">\n' +
                //     '                                 <option value=" ">名称</option>\n' +
                //     '                                 <option value=" ">长度</option>\n' +
                //     '                                 <option value=" ">面积</option>\n' +
                //     '                                 <option value=" ">自定义</option>\n' +
                //     '                         </select>\n' +
                //     '                   </div>\n' +
                //     '            </div>\n' ;
                // $('#shpLoadConfirmBtn').parent().after(spatialID);
                // form.render('select');
            }
            ,error:function () {
                var spatialID= '<div class="layui-form-item" style="margin-top: 15px;">\n' +
                    '                  <label class="layui-form-label" style="line-height: 41px;width: 93px;padding: 0">空间标识字段</label>\n' +
                    '                  <div class="layui-input-block">\n' +
                    '                        <select name="userDataField">\n' +
                    '                                 <option value=" ">名称</option>\n' +
                    '                                 <option value=" ">长度</option>\n' +
                    '                                 <option value=" ">面积</option>\n' +
                    '                                 <option value=" ">自定义</option>\n' +
                    '                         </select>\n' +
                    '                   </div>\n' +
                    '            </div>\n' +
                    '            <div>\n' +
                    '                    <button type="button" class="layui-btn" id="comfirmUserSpDataBtn">下一步</button>' +
                    '            </div>\n';
                $('#shpLoadConfirmBtn').parent().after(spatialID);
                form.render('select');
            }
        });
    })
}

// 初始化色带和图表的选择框
function initTjSymbol() {

    $(".sym-selected").each(function(e){
        var That = $(this);
        var width=$('.select_title').width();
        $(".select_content").css('width',width);
        That.find(".select_title").on({
            click:function(){
                var state=That.find(".select_content").css("display");
                if (state=="flex"||state=="block"){
                    That.find(".select_content").hide();
                } else if (state=="none"){
                    That.find(".select_content").show();
                }
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
                '<li><img  style="width:100%;" color1="'
                + e[k][0] + '" color2="' + e[k][1]
                + '" src="./assets/imgs/gradeIcon/9/' + k + '.jpg"></li>');
        }
    }

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

// 用户自定义统计图表的符号
function userDefineChartColor() {
    colorpicker.render({
        elem: '#test3'
        ,color: 'rgb(68,66,66)'
        ,format: 'rgb' //默认为 hex
        ,size:'lg'
    });
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
            elem: '#symbolOpacity'
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

// 获得用户选择的有关统计符号的值
function getchartSymbolValues() {
    var chartID = $("#chart-selected>.select_title>img").attr("src").slice(-10,-4);
    var color = $("#color-solution>.select_title>img").attr("name");
    return ([chartID,color,symbolSizeSliderValue,symbolOpacitySliderValue]);
}

// 获得用户选择的有关分级符号的值
function getgraduatedSymbolValues() {
    var color1=$("#color-selected>.select_title>img").attr("color1");
    var color2=$("#color-selected>.select_title>img").attr("color2");
    return ([classNumSliderValue,color1,color2,modelName,symbolOpacitySliderValue]);

}


//打开第二个页面
var tjPanel2 = {"nav":"nav2","tabId":"","tableName":"","fieldId":"","fieldsName":""};
function opentjPanel2(){
    layui.use(['element'], function(){
        var element = layui.element;
        tableTree();
        submitFields();
        element.on('tab(nav2)', function(data){
            //console.log(this); //当前Tab标题所在的原始DOM元素
            //console.log(data.index); //得到当前Tab的所在下标
            //console.log(data.elem); //得到当前的Tab大容器
            if((data.index+1)==1){
                tableTree();
                submitFields();
            }else if((data.index+1)==2){
                OtherDatabase();
            }else if((data.index+1)==3){
                EXCELupload();
            }
        });
    });
}

//获取tabletree,并显示表头
function tableTree(){
    var type = $(".layui-tab-title").find(".layui-this");
    //console.log(type.html());
    var treeName;
    var treeElement;
    var createTree = [];
    var tableFields = {};
    var fieldslist = [];
    if(type.html()=="平台数据库"){
        treeName = "SystemDatabase";
        treeElement = '#treedemo1';
        fieldslist = $("#fieldslist1");
        tjPanel2.tabId = 1;
    }else if(type.html()=="其他数据库"){
        treeName = "OtherDatabase";
        treeElement = '#treedemo2';
        fieldslist = $("#fieldslist2");
        tjPanel2.tabId = 2;
    }else if(type.html()=="上传EXCEL文件"){
        fieldslist = $("#fieldslist3");
        tjPanel2.tabId = 3;
    }
    $.ajax({//返回tabletree
        type: 'post',
        url:"",
        async:"false",
        data:{ name :type.html()},
        success: function (data) { //返回json结果
            alert(data);
            createTree = data;
        },
        error:function(){
            alert("sorry!")
        }
    });
    //构造
    createTree = [{name: '表格1'},{name: '表格2'},{name: '表格3'}];
    $("#treedemo1,#treedemo2").empty();
    layui.use(['tree','form'],function(){
        var tree = layui.tree;
        var form = layui.form;
        layui.tree({
            elem: treeElement, //传入元素选择器
            nodes: createTree,
            click:function (node) {
                //console.log(node.name);
                tjPanel2.tableName = node.name;
                $.ajax({
                    type: 'post',
                    url:"",
                    async:"false",
                    data:{ "tableName":node.name},
                    success: function (data) { //返回json结果(表头)
                        fieldslist.empty();
                        $.each(tableFields, function(index,item) {
                            fieldslist.append('<input type="checkbox" name="'+item+'" title="'+item+'" value="'+item+'">');
                        });
                        form.render();
                    },
                    error:function(){
                        alert("sorry!")
                    }
                });
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
                fieldslist.empty();
                $.each(tableFields, function(index,item) {
                    fieldslist.append('<input type="checkbox" name="'+item+'" title="'+item+'" value="'+item+'">');
                });
                form.render();
            }
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
            tjPanel2.fieldsName = data.field;
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
            $("#OtherDatabase1").hide();
            $("#OtherDatabase2").removeClass('layui-hide');
            tableTree();
            submitFields();
            return false;
        });
        $('#rechooseDatabase').click(function(){
            $("#OtherDatabase2").addClass('layui-hide');
            $("#OtherDatabase1").show();
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
            ,url: '/upload/'
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
                //上传成功，返回表头
                fieldslist.empty();
                $.each(tableFields, function(index,item) {
                    fieldslist.append('<input type="checkbox" name="'+item+'" title="'+item+'" value="'+item+'">');
                });
                form.render();

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
        $("#EXCELupload").click(function(){
            $("#EXCELupload1").hide();
            $("#EXCELupload2").removeClass("layui-hide");
            return false;
        });
        $('#rechooseExcel').click(function(){
            $("#EXCELupload2").addClass('layui-hide');
            $("#EXCELupload1").show();
            return false;
        });
    });

}


// 选择空间数据的html
var html1='<div class="layui-tab layui-tab-brief">\n' +
    '            <ul class="layui-tab-title">\n' +
    '                <li class="layui-this">载入行政区划</li>\n' +
    '                <li>上传自定义数据</li>\n' +
    '            </ul>\n' +
    '            <div class="layui-tab-content" style="font-size: 14px">\n' +
    '                <div class="layui-tab-item layui-show">\n' +
    '                    <div class="layui-form regionSelectItem">\n' +
    '                        <div class="layui-form-item ">\n' +
    '                            <label class="layui-form-label select-region-label">选择区域</label>\n' +
    '                            <div class="layui-input-block">\n' +
    '                                <select name="city" xm-select="selectRegion">\n' +
    '                                    <option value="">请选择</option>\n' +
    '                                </select>\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                        <div class="layui-form-item ">\n' +
    '                            <div class="adminSelectTips">\n' +
    '                                <span>提示：单击进入下一级，双击确认选择</span>\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                        <div class="layui-form-item">\n' +
    '                            <div class="layui-input-block  admin-form-btn-group">\n' +
    '                                <button class="layui-btn layui-btn-default admin-form-btn"\n' +
    '                                        onclick="getSpatialData()">下一步\n' +
    '                                </button>\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '                <div class="layui-tab-item">\n' +
    '                    <div class="layui-form" lay-filter="userDataField">\n' +
    '                        <div id="shpFileUploadControl" class="layui-upload-drag">\n' +
    '                            <i class="layui-icon layui-icon-upload-drag"></i>\n' +
    '                            <p>点击或拖拽上传，请以压缩文件的形式上传</p>\n' +
    '                        </div>\n' +
    '                        <div>\n' +
    '                            <button type="button" class="layui-btn" id="shpLoadConfirmBtn">开始上传</button>\n' +
    '                        </div>\n' +
    // '                        <div class="layui-form-item" style="margin-top: 15px;">\n' +
    // '                            <label class="layui-form-label"\n' +
    // '                                   style="line-height: 41px;width: 93px;padding: 0">空间标识字段</label>\n' +
    // '                            <div class="layui-input-block">\n' +
    // '                                <select name="userDataField">\n' +
    // '                                    <option value=" ">名称</option>\n' +
    // '                                    <option value=" ">长度</option>\n' +
    // '                                    <option value=" ">面积</option>\n' +
    // '                                    <option value=" ">自定义</option>\n' +
    // '                                </select>\n' +
    // '                            </div>\n' +
    // '                        </div>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </div>';

//选择统计数据的html
var html2='<div class="layui-tab layui-tab-brief" lay-filter="nav2">\n' +
    '            <ul class="layui-tab-title">\n' +
    '                <li class="layui-this">平台数据库</li>\n' +
    '                <li>其他数据库</li>\n' +
    '                <li>上传EXCEL文件</li>\n' +
    '            </ul>\n' +
    '            <div class="layui-tab-content">\n' +
    '                <div class="layui-tab-item layui-show">\n' +
    '                    <form class="layui-form" action="" lay-filter="">\n' +
    '                        <div class="layui-form-item">\n' +
    '                            <div class="layui-row layui-col-space10">\n' +
    '                                <div class="layui-col-md3">\n' +
    '                                    <fieldset class="layui-elem-field">\n' +
    '                                        <legend class="Panel2_Legend" style="font-size:14px">平台数据库</legend>\n' +
    '                                        <div class="layui-field-box" style="padding: 5px;">\n' +
    '                                            <div class="layui-row layui-col-space10">\n' +
    '                                                <div class="layui-col-md12">\n' +
    '                                                    <ul class="tabletree" style="text-align:center" id="treedemo1"></ul>\n' +
    '                                                </div>\n' +
    '                                            </div>\n' +
    '                                        </div>\n' +
    '                                    </fieldset>\n' +
    '                                </div>\n' +
    '                                <div class="layui-col-md9">\n' +
    '                                    <fieldset class="layui-elem-field" style="text-align:center">\n' +
    '                                        <legend class="Panel2_Legend" style="font-size:14px">数据表可选字段</legend>\n' +
    '                                        <form class="layui-form" action="" lay-filter="">\n' +
    '                                            <div class="layui-form-item" id="fieldslist1"></div>\n' +
    '                                            <div class="layui-form-item">\n' +
    '                                                <button class="layui-btn" lay-submit="" lay-filter="fields">立即提交</button>\n' +
    '                                                <button type="reset" class="layui-btn layui-btn-primary">重置</button>\n' +
    '                                            </div>\n' +
    '                                        </form>\n' +
    '                                    </fieldset>\n' +
    '                                </div>\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                    </form>\n' +
    '                </div>\n' +
    '                <div class="layui-tab-item" layui-filter="tjPanel22">\n' +
    '                    <form class="layui-form" action="" lay-filter="OtherDatabase1" id="OtherDatabase1">\n' +
    '                        <div class="layui-form-item">\n' +
    '                            <select name="databaseType" lay-filter="shujuku">\n' +
    '                                <option value="Oracle" selected="">Oracle</option>\n' +
    '                                <option value="MySQL">MySQL</option>\n' +
    '                                <option value="SQL server">SQL server</option>\n' +
    '                            </select>\n' +
    '                        </div>\n' +
    '                        <div class="layui-form-item">\n' +
    '                            <input type="text" name="address" lay-verify="address" autocomplete="off" placeholder="请输入数据库链接地址" class="layui-input">\n' +
    '                        </div>\n' +
    '                        <div class="layui-form-item">\n' +
    '                            <input type="text" name="username" lay-verify="username" placeholder="请输入用户名" autocomplete="off" class="layui-input">\n' +
    '                        </div>\n' +
    '                        <div class="layui-form-item">\n' +
    '                            <input type="password" name="password" lay-verify="pass" placeholder="请输入密码" autocomplete="off" class="layui-input">\n' +
    '                        </div>\n' +
    '                        <div class="layui-form-item" style="text-align:center">\n' +
    '                            <button class="layui-btn" lay-submit="" lay-filter="otherdatabase" id="chooseDatabase">立即提交</button>\n' +
    '                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>\n' +
    '                        </div>\n' +
    '                    </form>\n' +
    '                    <form class="layui-form layui-hide" action="" lay-filter="OtherDatabase2" id="OtherDatabase2">\n' +
    '                        <div class="layui-form-item">\n' +
    '                            <div class="layui-row layui-col-space10">\n' +
    '                                <div class="layui-col-md3">\n' +
    '                                    <fieldset class="layui-elem-field" >\n' +
    '                                        <legend class="Panel2_Legend" style="font-size:14px">其他数据库</legend>\n' +
    '                                        <div class="layui-field-box" style="padding: 5px;">\n' +
    '                                            <div class="layui-row layui-col-space10">\n' +
    '                                                <div class="layui-col-md12">\n' +
    '                                                    <ul class="tabletree" style="text-align:center" id="treedemo2"></ul>\n' +
    '                                                </div>\n' +
    '                                            </div>\n' +
    '                                        </div>\n' +
    '                                    </fieldset>\n' +
    '                                </div>\n' +
    '                                <div class="layui-col-md9">\n' +
    '                                    <fieldset class="layui-elem-field" style="text-align:center">\n' +
    '                                        <legend class="Panel2_Legend" style="font-size:14px">数据表可选字段</legend>\n' +
    '                                        <form class="layui-form" action="" lay-filter="">\n' +
    '                                            <div class="layui-form-item" id="fieldslist2"></div>\n' +
    '                                            <div class="layui-form-item">\n' +
    '                                                <button class="layui-btn" lay-submit="" lay-filter="fields">立即提交</button>\n' +
    '                                                <button type="reset" class="layui-btn layui-btn-primary">重置</button>\n' +
    '                                            </div>\n' +
    '                                        </form>\n' +
    '                                    </fieldset>\n' +
    '                                </div>\n' +
    '                            </div>\n' +
    '                            <button class="layui-btn" id="rechooseDatabase">重新接入数据库</button>\n' +
    '                        </div>\n' +
    '                    </form>\n' +
    '                </div>\n' +
    '                <div class="layui-tab-item">\n' +
    '                    <div class="layui-upload-drag layui-input-inline" style="padding: 10px;" id="EXCELupload1">\n' +
    '                        <i class="layui-icon layui-icon-upload-drag"></i>\n' +
    '                        <p id="fileName">点击上传，或将文件拖拽到此处</p>\n' +
    '                        <button id="EXCELupload" type="button" class="layui-btn layui-input-inline" style="text-align:center">开始上传</button>\n' +
    '                    </div>\n' +
    '                    <form class="layui-form layui-hide" action="" lay-filter="EXCELupload2" id="EXCELupload2">\n' +
    '                        <div class="layui-form-item">\n' +
    '                            <div class="layui-row layui-col-space10">\n' +
    '                                <fieldset class="layui-elem-field" style="text-align:center">\n' +
    '                                    <legend class="Panel2_Legend" style="font-size:14px">Excel表可选字段</legend>\n' +
    '                                    <form class="layui-form" action="" lay-filter="">\n' +
    '                                        <div class="layui-form-item" id="fieldslist3"></div>\n' +
    '                                        <div class="layui-form-item">\n' +
    '                                            <button class="layui-btn" lay-submit="" lay-filter="fields">立即提交</button>\n' +
    '                                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>\n' +
    '                                        </div>\n' +
    '                                    </form>\n' +
    '                                </fieldset>\n' +
    '                            </div>\n' +
    '                            <button class="layui-btn" id="rechooseExcel">重新上传EXCEL表</button>\n' +
    '                        </div>\n' +
    '                    </form>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </div>';

// 统计图表的html
var html3='    <fieldset class="layui-elem-field layui-field-title" style="margin-top:20px;margin-bottom: 5px">\n' +
    '            <legend class="symbolLegend">统计符号</legend>\n' +
    '        </fieldset>\n' +
    '        <div style="padding: 15px;">\n' +
    '            <div class="layui-row layui-col-space15">\n' +
    '                <div class="layui-card">\n' +
    '                    <div class="layui-card-body chartSymbolCardbody">\n' +
    '                        <h6>图表类型：</h6>\n' +
    '                        <div class="sym-selected" id="chart-selected">\n' +
    '                            <div class="select_title">\n' +
    '                                <img src="assets/imgs/chartIcon/010101.png">\n' +
    '                            </div>\n' +
    '                            <ul class="select_content">\n' +
    '                            </ul>\n' +
    '                        </div>\n' +
    '                        <h6>色彩方案：</h6>\n' +
    '                        <div class="sym-selected" id="color-solution">\n' +
    '                            <div class="select_title">\n' +
    '                                <img name="青黄色系" src="assets/imgs/gradeIcon/10/6.jpg" style="width: 100%">\n' +
    '                            </div>\n' +
    '                            <ul class="select_content">\n' +
    '                            </ul>\n' +
    '                        </div >\n' +
    // '                        <div id="test3"></div>'+
    '                        <div class="userDefineColors">' +
    '                            <label class="selectedStaIndexsLabel">男生人数</label>'+
    '                            <div id="test3"></div>'+
    '                        </div>'+
    '                        <h6>符号大小：</h6>\n' +
    '                        <div id="symbolSize"></div>\n' +
    '                        <h6>透明度：</h6>\n' +
    '                        <div id="symbolOpacity"></div>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '                <div id="symbolBtn" style="position: absolute;right: 0;padding: 0px 7px">\n' +
    '                    <button class="layui-btn" id="tjInfoSubmit" >确定</button>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </div>';

// 分级设色的html
var html4='   <fieldset class="layui-elem-field layui-field-title" style="margin-top:20px;margin-bottom: 5px">\n' +
    '            <legend class="symbolLegend">分级符号</legend>\n' +
    '        </fieldset>\n' +
    '        <div style="padding: 15px;" class="layui-form">\n' +
    '            <div class="layui-row layui-col-space15">\n' +
    '                <div class="layui-card">\n' +
    '                    <div class="layui-card-body chartSymbolCardbody">\n' +
    '                        <h6>分级数量：</h6>\n' +
    '                        <div id="classNum"></div>\n' +
    '                        <h6>分级色系：</h6>\n' +
    '                        <div class="sym-selected" id="color-selected">\n' +
    '                            <div class="select_title">\n' +
    '                                <img style="width:100%;" color1="#FFFEE3" color2="#00935B"\n' +
    '                                     src="assets/imgs/gradeIcon/10/4.jpg">\n' +
    '                            </div>\n' +
    '                            <ul class="select_content">\n' +
    '                            </ul>\n' +
    '                        </div>\n' +
    '                        <div class="graduatedColorInverseInput">'+
    '                             <input type="checkbox" name="graduatedColorInverse" lay-skin="primary" title="反色" >'+
    '                        </div>'+
    '                        <h6>分级模型：</h6>\n' +
    '                        <div class="layui-form-item">\n' +
    '                            <select id="model" class="form-control" name="graduatedModel" lay-filter="graduatedModel">\n' +
    '                                <option value="界限等分模型">界限等分模型</option>\n' +
    '                                <option value="间隔等分模型">间隔等分模型</option>\n' +
    '                                <option value="界限等比模型">界限等比模型</option>\n' +
    '                                <option value="间隔等比模型">间隔等比模型</option>\n' +
    '                            </select>\n' +
    '                        </div>\n' +
    '                        <h6>透明度：</h6>\n' +
    '                        <div id="symbolOpacity"></div>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '                <div id="symbolBtn" style="position: absolute;right: 0;padding: 0px 7px">\n' +
    '                    <button class="layui-btn" id="tjInfoSubmit">确定</button>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </div>';














