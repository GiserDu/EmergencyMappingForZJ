function autoTriggerLoadStatisticMap(layernames){
    for(index in layernames){
        $("input:checkbox").each(function (e) {
            var name=$("input:checkbox")[e].value;
            if(name==layernames[index]){
                $($(this)[0]).prop("checked", true);
                $($(this)[0]).change();}
        });
    }
}

//根据制图模板，构造前端样式
generateStatisticLayer();
function generateStatisticLayer() {
//首先判断制图模板中serviceLayer是否为空，不为空才有下一步
    if(typeof  template.statisticLayer =="undefined"||template.serviceLayer.modules.length==0){
        $("#statisticLayer").css("display", "none");
        return;
    }
    if(typeof  template.statisticLayer !="undefined"){
        //设置前端样式为可见
        if(typeof  template.statisticLayer.modules[0].name =="undefined"||template.statisticLayer.modules[0].name=="")
        {
            $("#statisticLayer").css("display", "none");
            return;
        }
        //构造之前先清空
        $("#statisticLayer ul").empty();
        //分析制图模板，为每一个statisticLayer构造前端样式
        for (var index = 0; index < template.statisticLayer.modules.length; index++) {
            //每一个statisticLayer
            var statisticLayerItem = template.statisticLayer.modules[index];
            //判断统计图层类型，设置样式：分级图、统计图
            statisticType = "";
            switch (statisticLayerItem["type"]) {
                case "chartLayer":
                    statisticType = "fa-square";
                    break;
                case "classLayer":
                    statisticType = "fa-ellipsis-h";
                    break;
            }
            //字体颜色 如果有服务地址，则为亮 否则为暗
            var statisticNameColor;
            if (statisticLayerItem["statisticData"] == "") {
                statisticNameColor = "gray";
            } else if (statisticLayerItem["statisticData"] != "") {
                statisticNameColor = "yellow";
            }
            var iconColor;
            if(map&&(map.getLayer(featureLayerItem.data))&&((map.getLayer(featureLayerItem.data)).visible)){
                iconColor='rgb(0, 253, 255)';
            }else{
                iconColor='#e2e2e2';
            }
            //构造样式
            styleItem = '<li><div class="statisticLayerCss" data-type="' + statisticLayerItem["type"] + '" data-index="' + index + '"><i class="statisticLayerCssI1 fa ' + statisticType + '" aria-hidden="true" style="color:'+iconColor+'"></i><a state="0" href="#" style="color:' + statisticNameColor + '">' + statisticLayerItem["name"] + '</a><i class="statisticLayerCssI2 fa fa-cog"  aria-hidden="true"></i></div></li>';
            //增加到页面
            $("#statisticLayer ul").append(styleItem);
        }
        //统计图层点击事件
        $("#statisticLayer a").click(function () {
            //如果图层已经加载，则点击会隐藏图层
            //如果图层没有加载，则点击显示图层
            //如果图层没有加载，也没有统计数据来源，则点击会提示“请设置图层数据来源”
            alert("todo")
        });
        //统计图层样式设置点击事件
        $(".statisticLayerCssI2").click(function () {
            //弹出符号设置窗口
            showSettingsanel();
        });
    }
}

//弹出统计图层设置面板
function showSettingsanel() {
    //先创建统计图层设置面板
    var div;
    slideInfos(1,502);
    if(!($(".settingsPanel").length >0)){//判断是否已经生成
        div=creatSettingspanel(); //动态生成面板
    }
    if($(".settingsPanel").width()==259){//判断是否已经弹出
        return;
    }
    $(".settingsPanel").animate({width:"185px"},50);
    $(".settingsPanel").animate({width:"260px"},500);
}

//创建统计图层设置面板
function creatSettingspanel(){
    //var html = "<ul id='accordion-plot'class='accordion'><li id='plot-point'><div class='link'><i class='fa fa-map'aria-hidden='true'></i>点符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(point)'>点</a></li><li><a href='#'>待完善1</a></li><li><a href='#'>待完善2</a></li></ul></li><li id='plot-polyline'><div class='link'><i class='fa fa-bandcamp'aria-hidden='true'></i>线符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(polyline)'>折线</a></li><li><a href='#'onclick='marking(freehand_polyline)'>自由线</a></li><li><a href='#'>待完善1</a></li><li><a href='#'>待完善2</a></li></ul></li><li id='plot-polygon'><div class='link'><i class='fa fa-pie-chart'aria-hidden='true'></i>面符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(polygon)'>多边形</a></li><li><a href='#'onclick='marking(freehand_polygon)'>自由面</a></li><li><a href='#'onclick='marking(rectangle)'>矩形</a></li><li><a href='#'onclick='marking(circle)'>圆</a></li><li><a href='#'onclick='marking(ellipse)'>椭圆</a></li><li><a href='#'onclick='marking(arrow)'>箭头</a></li><li><a href='#'onclick='marking(triangle)'>三角形</a></li><li><a href='#'>待完善1</a></li></ul></li><li id='plot-tools'><div class='link'><i class='fa fa-paint-brush'aria-hidden='true'></i>工具<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(edit)'>编辑</a></li><li><a href='#' onclick='addMeasureInteraction()'>测量</a></li></ul></li></ul>";
    var html = [
        '<div class="areaSelection">',
        '<i class="fa fa-globe" aria-hidden="true"></i>',
        '区域选择',
        '<i id="closeSettingsPanel" class="fa fa-window-close-o" aria-hidden="true" style="    position: absolute;' +
        '    top: 4px;' +
        '    right: 1px;' +
        '    cursor:pointer;'+
        '    font-size: 13px;' +
        '    color: #e2e2e2;"></i>',
        '</div>',
        '<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">',
        '<ul class="layui-tab-title" style="color: white">',
        '<li class="layui-this">行政区</li>',
        '<li>地图选择</li>',
        '<li>矢量文件</li>',
        '</ul>',
        '<div class="layui-tab-content" style="height: 100px;">',
        '<div class="layui-tab-item layui-show" id="areaSelectTab">内容1区域选择要根据前面传过来的制图区域动态生成</div>',
        '<div class="layui-tab-item">内容2</div>',
        '<div class="layui-tab-item">内容3</div>',
        '</div>',
        '</div>',
        '<div class="areaSelection">',
        '<i class="fa fa-globe" aria-hidden="true"></i>',
        '数据选择',
        '</div>',
        '<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">',
        '<ul class="layui-tab-title" style="color: white">',
        '<li class="layui-this">平台数据库</li>',
        '<li>其他数据库</li>',
        '<li>excel</li>',
        '</ul>',
        '<div class="layui-tab-content" style="height: 100px;">',
        '<div class="layui-tab-item layui-show" id="areaSelectTab">内容1区域选择要根据前面传过来的制图区域动态生成',
        '<div>',
        '<form >',
        '<input type="radio" name="sex" value="男" title="男">male<br>',
        '<input type="radio" name="sex" value="女" title="女" checked>female',
        '</form>',
        '</div>',
        '</div>',
        '<div class="layui-tab-item">内容2</div>',
        '<div class="layui-tab-item">内容3</div>',
        '</div>',
        '</div>',
        '<div class="areaSelection">',
        '<i class="fa fa-globe" aria-hidden="true"></i>',
        '统计符号选择',
        '</div>',
        '<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">',
        '<ul class="layui-tab-title" style="color: white">',
        '<li class="layui-this">行政区</li>',
        '<li>地图选择</li>',
        '<li>矢量文件</li>',
        '</ul>',
        '<div class="layui-tab-content" style="height: 100px;">',
        '<div class="layui-tab-item layui-show" id="areaSelectTab">内容1区域选择要根据前面传过来的制图区域动态生成</div>',
        '<div class="layui-tab-item">内容2</div>',
        '<div class="layui-tab-item">内容3</div>',
        '</div>',
        '</div>'
    ].join('');
    var link = document.createElement('div');
    link.setAttribute('class','settingsPanel');
    link.innerHTML = html;
    document.body.appendChild(link);
    switch (template.mapScale) {
        //统一mapScale的中文名字，不然不好switch
        case "省":
            creatProList();
            break;
        case "市":
            creatCityList();
            break;
        case "区域":
            creatCounList();
            break;
        default:
            alert("mapScale名称有误")
            return;
    }
    $('.provinceSelect').selectpicker({
        'noneSelectedText': '请选择省份'
    });
/*    $('.provinceSelect').click(function () {
        $('.provinceSelect').toggleClass("open");
    })*/

    $("#closeSettingsPanel").click(function () {
        node =  $($($(this).parent()).parent());
        node.animate({width:"185px"},500);
        node.animate({width:"0px"},50);
        slideInfos(1,242);
    });
}

//创建省级区域表单
function creatProList() {
    var tab = document.getElementById("areaSelectTab");
    var link = document.createElement('div');
    link.setAttribute('class','province');
    html=[
        '<select id="provinceSelect" class="selectpicker provinceSelect" multiple data-live-search="true">',
        '<option>安徽省</option>',
        '<option>河南省</option>',
        '<option>湖北省</option>',
        '<option >北京市</option>',
        '<option>上海市</option>',
        '</optgroup>',
        '</select>',
    ].join('');
    link.innerHTML = html;
    tab.appendChild(link);
}
//创建市级区域表单
function creatCityList() {
    var tab = document.getElementById("areaSelectTab");
    var link = document.createElement('div');
    link.setAttribute('class','city');
    //这个地方要根据选择的省份查询该省份所包含的城市，然后将城市放进一个迭代器，循环添加进变量html
    html=[
        '<select id="provinceSelect" class="selectpicker provinceSelect" multiple data-live-search="true">',
        '<option>A市</option>',
        '<option>B市</option>',
        '<option>C市</option>',
        '<option >D市</option>',
        '</optgroup>',
        '</select>',
    ].join('');
    link.innerHTML = html;
    tab.appendChild(link);
}
//创建县级区域表单
function creatCounList() {
    var tab = document.getElementById("areaSelectTab");
    var link = document.createElement('div');
    link.setAttribute('class','county');
    //这个地方要根据选择的城市查询该省份所包含的线，然后将县放进一个迭代器，循环添加进变量html
    html=[
        '<select id="provinceSelect" class="selectpicker provinceSelect" multiple data-live-search="true">',
        '<option>A县</option>',
        '<option>B县</option>',
        '<option>C县</option>',
        '</optgroup>',
        '</select>',
    ].join('');
    link.innerHTML = html;
    tab.appendChild(link);
}

//获得指定省份所包含的市

//获得指定省份所包含的县