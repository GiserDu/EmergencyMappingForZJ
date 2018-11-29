var layerIndex;

function opentjMenuLayer() {
    layui.use(['layer','form','element'],function () {
        var layer = layui.layer;
        var element = layui.element;

        layerIndex=layer.open({
            type: 1,
            title: ['添加统计图层','background: #4476A7;color: #fff;border: none;'],
            shadeClose: false,
            shade: 0,
            area:['700px'],
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
            '                <a href="javascript:;" name="selectMappingTemplate">选择制图模板</a>\n' +
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
            '                                <span>提示：单击向下钻取，双击确认选择</span>\n' +
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
            '                    <div id="shpFileUploadControl" class="layui-upload-drag">\n' +
            '                        <i class="layui-icon layui-icon-upload-drag"></i>\n' +
            '                        <p>点击或拖拽上传，请以压缩文件的形式上传</p>\n' +
            '                    </div>\n' +
            '                    <div>\n' +
            '                        <button type="button" class="layui-btn" id="shpLoadConfirmBtn">开始上传</button>' +
            '                    </div>\n' +
            '                </div>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '    </div>\n' +
            '</div>\n',
        });

        var formSelects = layui.formSelects;

        formSelects.config('selectRegion', {
            direction: "down",
            success: function(id, url, val, result){
                console.log("success回调: " + url);
                // console.log(formSelects.config.height);
            },
            error: function(id, url, val, err){
                console.log("err回调: " + url);
            }
        });

        formSelects.data('selectRegion', 'server', {
            url: 'http://yapi.demo.qunar.com/mock/9813/layui/citys',
            linkage: true,
            linkageWidth: 130
        });

        //监听下拉框的打开
        layui.formSelects.opened('selectRegion', function(id){
            // 操作提示
            var selectTitleHeight=$(".xm-select-title").height();
            var optionPanelHeight=$(".xm-select-linkage").height();
            var layerHeight=selectTitleHeight+optionPanelHeight+43+'px';
            var layerContentHeight=selectTitleHeight+optionPanelHeight+'px';
            layer.style(layerIndex, {
                // width: '600px',
                height: layerHeight,
            });
            $(".layui-layer-content").css("height",layerContentHeight);
        });

        //监听下拉框的关闭
        formSelects.closed('selectRegion', function(id){
            var selectTitleHeight=$(".xm-select-title").height();
            var layerHeight=selectTitleHeight+214+'px';
            var layerContentHeight=selectTitleHeight+171+'px';
            layer.style(layerIndex, {
                // width: '600px',
                height: layerHeight,
            });
            $(".layui-layer-content").css("height",layerContentHeight);
            // console.log('合上了');
        });

        formSelects.on('selectRegion', function(id, vals, val, isAdd, isDisabled){
            var selectTitleHeight=$(".xm-select-title").height();
            var optionPanelHeight=$(".xm-select-linkage").height();
            // 监听面板打开或关闭时的高度变化
            if(optionPanelHeight!=0){
                var layerHeight=selectTitleHeight+optionPanelHeight+43+'px';
                var layerContentHeight=selectTitleHeight+optionPanelHeight+'px';
            }else {
                layerHeight=selectTitleHeight+214+'px';
                layerContentHeight=selectTitleHeight+171+'px';
            }
            layer.style(layerIndex, {
                // width: '700px',
                height: layerHeight,
            });
            $(".layui-layer-content").css("height",layerContentHeight);
        }, true);

        // 文件上传
        layui.use('upload', function(){
            var $ = layui.jquery
                ,upload = layui.upload;
            var uploadInst= upload.render({
                elem: '#shpFileUploadControl'
                ,url: '/upload/'
                ,auto: false
                ,accept: 'file' //普通文件
                ,exts: 'zip|rar|7z' //只允许上传压缩文件
                ,bindAction: '#shpLoadConfirmBtn'
                ,done: function(res){
                    console.log(res)
                }
            });
        })

        element.on('nav(navDemo)', function(elem){
            var leftMenuName=elem.attr('name');
            if(leftMenuName=="selectMappingRange"){

            }else if (leftMenuName=="selectStatistics") {
                // $(".tjPanel-content").empty();
            } else if(leftMenuName=="selectMappingTemplate") {
                // $(".tjPanel-content").empty();
            }
            // console.log(leftMenuName); //得到当前点击的DOM对象
        });
        element.render('nav')

    })
}
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

    // 关闭弹出层
    // layer.close(layerIndex);
    // console.log(selectedValues);
}












