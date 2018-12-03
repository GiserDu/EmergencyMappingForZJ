var layerIndex;
var selectedIndexNum;
var symbolSizeSliderValue=0;
var symbolOpacitySliderValue=0;
var classNumSliderValue=5;
var modelName;

function opentjMenuLayer() {
    layui.use(['layer','form','element'],function () {
        var layer = layui.layer;
        var element = layui.element;
        var form=layui.form;

        layerIndex=layer.open({
            type: 1,
            title: ['添加统计图层'],
            shadeClose: false,
            skin:"layui-layer-lan",
            shade: 0,
            area:['700px','470px'],
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
            '        <div class="layui-tab">\n' +
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
            '                                <button class="layui-btn layui-btn-default admin-form-btn" onclick="getAllSelectedData()">确定</button>\n' +
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
            '                        <button type="button" class="layui-btn" id="shpLoadConfirmBtn">开始上传</button>' +
            '                    </div>\n' +
            // '                    <div class="layui-form-item" style="margin-top: 15px;">' +
            // '                         <label class="layui-form-label"  style="line-height: 41px;width: 93px;padding: 0">空间标识字段</label>\n' +
            // '                         <div class="layui-input-block">\n' +
            // '                                <select name="userDataField">' +
            // '                                      <option value=" ">名称</option>\n' +
            // '                                      <option value=" ">长度</option>\n' +
            // '                                      <option value=" ">面积</option>\n' +
            // '                                      <option value=" ">自定义</option>'+
            // '                                </select>' +
            // '                         </div>\n' +
            // '                    </div>'+
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
                form.render('select');

            } else if(leftMenuName=="selectMappingTemplate") {

                if (selectedIndexNum==0){
                    alert('请选择统计指标');

                } else if(selectedIndexNum==1){
                    $(".tjPanel-content").html(html3);
                    initTjSymbol();
                    renderSlider();
                    // form.render('select');
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
                    form.render('select');
                    $("#tjInfoSubmit").bind('click',function () {
                        var graduatedSymbolValues=getgraduatedSymbolValues();
                        console.log(graduatedSymbolValues);
                    })
                }
            }
        });
        element.render('nav');
    })
}

// 获取用户选择的行政区划
// 点击确定按钮后执行这个函数，后面可能要用ajax传值给后台
function getAllSelectedData() {
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

function userLoadSpatialData() {
    // 文件上传
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
                    '            </div>\n' ;
                $('#shpLoadConfirmBtn').parent().after(spatialID);
                form.render('select');
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
function opentjPanel2(){
    layui.use(['tree','table','form'], function(){
        var tree = layui.tree;
        var form = layui.form;
        var createTree = [{ //节点
            name: '文件夹1'
            ,children: [{
                name: '表格11'
            },{
                name: '表格12'
            }]
        },{
            name: '文件夹2'
            ,children: [{
                name: '文件夹21'
                ,children: [{
                    name: '表格211'
                }]
            }]
        }];
        var tableJson11= [{
            "id": "10001"
            ,"username": "杜甫"
            ,"email": "xianxin@layui.com"
            ,"sex": "男"
            ,"city": "浙江杭州"
            ,"sign": "点击此处，显示更多。当内容超出时，点击单元格会自动显示更多内容。"
            ,"experience": "116"
            ,"ip": "192.168.0.8"
            ,"logins": "108"
            ,"joinTime": "2016-10-14"
        }, {
            "id": "10002"
            ,"username": "李白"
            ,"email": "xianxin@layui.com"
            ,"sex": "男"
            ,"city": "浙江杭州"
            ,"sign": "君不见，黄河之水天上来，奔流到海不复回。 君不见，高堂明镜悲白发，朝如青丝暮成雪。 人生得意须尽欢，莫使金樽空对月。 天生我材必有用，千金散尽还复来。 烹羊宰牛且为乐，会须一饮三百杯。 岑夫子，丹丘生，将进酒，杯莫停。 与君歌一曲，请君为我倾耳听。(倾耳听 一作：侧耳听) 钟鼓馔玉不足贵，但愿长醉不复醒。(不足贵 一作：何足贵；不复醒 一作：不愿醒/不用醒) 古来圣贤皆寂寞，惟有饮者留其名。(古来 一作：自古；惟 通：唯) 陈王昔时宴平乐，斗酒十千恣欢谑。 主人何为言少钱，径须沽取对君酌。 五花马，千金裘，呼儿将出换美酒，与尔同销万古愁。"
            ,"experience": "12"
            ,"ip": "192.168.0.8"
            ,"logins": "106"
            ,"joinTime": "2016-10-14"
        }, {
            "id": "10003"
            ,"username": "王勃"
            ,"email": "xianxin@layui.com"
            ,"sex": "男"
            ,"city": "浙江杭州"
            ,"sign": "人生恰似一场修行"
            ,"experience": "65"
            ,"ip": "192.168.0.8"
            ,"logins": "106"
            ,"joinTime": "2016-10-14"
        }, {
            "id": "10004"
            ,"username": "李清照"
            ,"email": "xianxin@layui.com"
            ,"sex": "女"
            ,"city": "浙江杭州"
            ,"sign": "人生恰似一场修行"
            ,"experience": "666"
            ,"ip": "192.168.0.8"
            ,"logins": "106"
            ,"joinTime": "2016-10-14"
        }, {
            "id": "10005"
            ,"username": "冰心"
            ,"email": "xianxin@layui.com"
            ,"sex": "女"
            ,"city": "浙江杭州"
            ,"sign": "人生恰似一场修行"
            ,"experience": "86"
            ,"ip": "192.168.0.8"
            ,"logins": "106"
            ,"joinTime": "2016-10-14"
        }];
        var tableJson12=[{
            "id":10000,
            "username":"user-0",
            "sex":"女",
            "city":"城市-0",
            "sign":"签名-0",
            "experience":255,
            "logins":24,
            "wealth":82830700,
            "classify":"作家",
            "score":57
        },{
            "id":10001,
            "username":"user-1",
            "sex":"男",
            "city":"城市-1",
            "sign":"签名-1",
            "experience":884,
            "logins":58,
            "wealth":64928690,
            "classify":"词人",
            "score":27
        },{
            "id":10002,
            "username":"user-2",
            "sex":"女",
            "city":"城市-2",
            "sign":"签名-2",
            "experience":650,
            "logins":77,
            "wealth":6298078,
            "classify":"酱油",
            "score":31
        }];
        var tableJson=[[]];
        $("#treedemo").empty();
        layui.tree({
            elem: '#treedemo1,#treedemo2', //传入元素选择器
            nodes: createTree,
            click:function (node) {
                //console.log(node.name);
                if (node.name=="表格11"){
                    tableJson=tableJson11;
                }else if (node.name=="表格12"){
                    tableJson=tableJson12;
                }else{
                    tableJson=[[]];
                }
                $("#fieldslist1,#fieldslist2,#fieldslist3").empty();
                $.each(tableJson[0], function(index,item) {
                    $("#fieldslist1,#fieldslist2,#fieldslist3").append('<input type="checkbox" name="'+index+'" title="'+index+'" value="'+index+'">');
                });
                form.render();
                //$('#tjPanel2').css("height","700px");
                //$(".layui-layer-content").css("height","435px");
                // $.ajax({
                //     type: 'post',
                //     url:"",
                //     async:"false",
                //     data:{name:""},
                //     success: function (data) { //返回json结果
                //         //alert(data);
                //         //var tablejson = data;
                //         $.each(data, function(index,item) {
                //             $.("#fieldslist").append('<input type="checkbox" name="xxx" value="'+item.xx+'" lay-skin="switch">');
                //         });
                //         form.render();
                //     },
                //     error:function(){
                //         alert("sorry!")
                //     }
                // });
            }
        });
    });
    OtherDatabase();
    EXCELupload();
}

//其他数据库
function OtherDatabase(){
    layui.use(["form","element"],function () {
        var form = layui.form,
            element = layui.element;
        form.on('submit(fields)', function (data) {
            //console.log(data.field.database);
            layer.alert(JSON.stringify(data.field), {
                title: '最终的提交信息'
            });
            // $.ajax({
            //     type: 'post',
            //     url:"",
            //     async:"false",
            //     data:data.field,
            //     success: function (data) { //返回json结果
            //         //alert(data);
            //         // var tablejson = data;
            //     },
            //     error:function(){
            //         alert("sorry!")
            //     }
            // });
            return false;
        });
        $('#chooseDatabase').click(function(){
            $("#OtherDatabase1").hide();
            $("#OtherDatabase2").removeClass('layui-hide');
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
                //上传成功

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
var html1='<div class="layui-tab">\n' +
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
    '                                        onclick="getAllSelectedData()">确定\n' +
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
var html2=' <div class="layui-tab layui-tab-brief">\n' +
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
    '                                    <fieldset class="layui-elem-field" style="text-align:center">\n' +
    '                                        <legend>平台数据库</legend>\n' +
    '                                        <div class="layui-field-box" style="padding: 5px;">\n' +
    '                                            <div class="layui-row layui-col-space10">\n' +
    '                                                <div class="layui-col-md12">\n' +
    '                                                    <ul id="treedemo1"></ul>\n' +
    '                                                </div>\n' +
    '                                            </div>\n' +
    '                                        </div>\n' +
    '                                    </fieldset>\n' +
    '                                </div>\n' +
    '                                <div class="layui-col-md9">\n' +
    '                                    <fieldset class="layui-elem-field" style="text-align:center">\n' +
    '                                        <legend>数据表可选字段</legend>\n' +
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
    '                                    <fieldset class="layui-elem-field" style="text-align:center">\n' +
    '                                        <legend>其他数据库</legend>\n' +
    '                                        <div class="layui-field-box" style="padding: 5px;">\n' +
    '                                            <div class="layui-row layui-col-space10">\n' +
    '                                                <div class="layui-col-md12">\n' +
    '                                                    <ul id="treedemo2"></ul>\n' +
    '                                                </div>\n' +
    '                                            </div>\n' +
    '                                        </div>\n' +
    '                                    </fieldset>\n' +
    '                                </div>\n' +
    '                                <div class="layui-col-md9">\n' +
    '                                    <fieldset class="layui-elem-field" style="text-align:center">\n' +
    '                                        <legend>数据表可选字段</legend>\n' +
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
    '                                    <legend>Excel表可选字段</legend>\n' +
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
    '                        </div>\n' +
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














