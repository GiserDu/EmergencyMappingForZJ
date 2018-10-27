//根据制图模板，构造前端样式
generateServiceLayer();
function generateServiceLayer() {
//首先判断制图模板中serviceLayer是否为空，不为空才有下一步
    if(typeof  template.serviceLayer =="undefined"||template.serviceLayer.modules.length==0||template.serviceLayer.modules[0].url==""){
        $("#serviceLayer").css("display", "none");
        return;
    }
    if(typeof  template.serviceLayer !="undefined"){
         //设置前端样式为可见
        if(template.serviceLayer.modules[0].url=="")
        {
            $("#serviceLayer").css("display", "none");
            return;
        }
         //构造之前先清空
         $("#serviceLayer ul").empty();
         //分析制图模板，为每一个serviceLayer构造前端样式
         for (var index = 0; index < template.serviceLayer.modules.length; index++) {
             //每一个serviceLayer
             var serviceLayerItem = template.serviceLayer.modules[index];
             //字体颜色 如果有服务地址，则为亮 否则为暗
             var serviceNameColor;
             if (serviceLayerItem["url"] == "") {
                 serviceNameColor = "gray";
             } else if (serviceLayerItem["url"] != "") {
                 serviceNameColor = "yellow";
             }
             //构造样式
             var styleItem = '<li><div class="serviceLayerCss"  data-index="' + index +'"><a state="0" href="#" style="color:' + serviceNameColor + '">' + serviceLayerItem["name"] + '</a><i class="serviceLayerCssI2 fa fa-cog"  aria-hidden="true"></i></div></li>'
             //增加到页面
             $("#serviceLayer ul").append(styleItem);
         }
         //要素图层点击事件
         $("#serviceLayer a").click(function () {
             //当前要素图层选中状态 0：未选中 1：选中
             state = $(this).attr("state");
             itemClicked = $(this);
             //获取当前要素服务在制图模板中信息
             serviceLayerItem = template.serviceLayer.modules[$($(this).parent()).attr("data-index")]
             if (state == 0) {
                 //判断要素服务地址是否存在
                 if (serviceLayerItem.url == "") {
                     layui.use('layer', function () {
                         var lay = layui.layer;
                         lay.open({
                             title: '提示'
                             ,content: '请先设置要素图层地址！'
                         });
                     })
                 } else if((map.getLayer(serviceLayerItem.url))&&(!((map.getLayer(serviceLayerItem.url)).visible))){
                     if(!(map.getLayer((template.serviceLayer.modules[$($(this).parent()).attr("data-index")]).url)))
                     {return};
                     (map.getLayer((template.serviceLayer.modules[$($(this).parent()).attr("data-index")]).url)).show();
                     var layerExtent=(map.getLayer((template.serviceLayer.modules[$($(this).parent()).attr("data-index")]).url)).fullExtent;
                     layui.use('layer', function () {
                         var lay = layui.layer;
                         lay.confirm('加载成功，缩放到该图层?', {
                             icon: 3,
                             title: '提示'
                         }, function (layui_index) {
                             try {
                                 //如果图层与地图坐标系不同，转换一下再设置全局范围
                                 if(layerExtent.spatialReference.wkid!=map.spatialReference.wkid ){
                                     require(["esri/tasks/GeometryService","esri/config"], function(GeometryService,config) {
                                         //配置代理
                                         config.defaults.io.proxyUrl = "../esriproxy/";
                                         config.defaults.io.alwaysUseProxy = false;
                                         var geometryService = new GeometryService(ESRI_GeometyService);
                                         geometryService.project([layerExtent],map.spatialReference, function (p) {
                                             console.log(p);
                                             map.setExtent(p[0]);
                                         });
                                     });
                                 }else {
                                     map.setExtent(layerExtent);
                                 }
                             }catch (e) {
                                 lay.open({
                                     title: '提示'
                                     ,content: '服务图层与底图图层坐标系统不同，暂时无法缩放！您可手动缩放到该图层！'
                                 });
                                 console.log(e);
                             }
                             lay.close(layui_index);
                         })
                     })
                     $(this).attr("state", "1");
                     $($(this).parent()).find(".serviceLayerCssI1").css("color", ' rgb(0, 253, 255)');
                 }else if((map.getLayer(serviceLayerItem.url))&&((map.getLayer(serviceLayerItem.url)).visible)){
                     $(this).attr("state", "1");
                     $($(this).parent()).find(".serviceLayerCssI1").css("color", ' rgb(0, 253, 255)');
                 }else{

                    require([
                         "esri/layers/ArcGISDynamicMapServiceLayer",
                         "esri/InfoTemplate", "esri/dijit/PopupTemplate"
                     ], function (ArcGISDynamicMapServiceLayer, InfoTemplate, PopupTemplate) {
                        var infoTemplate = new InfoTemplate("${NAME}", "${*}");
                        var serviceUrl = serviceLayerItem.url;
                        serviceUrlstr=serviceUrl.substring(0,serviceUrl.lastIndexOf("/"));
                          var layer = new ArcGISDynamicMapServiceLayer(serviceUrlstr,{id: serviceLayerItem.url});
                          var showindex=serviceUrl.substring(serviceUrl.lastIndexOf("/")+1,serviceUrl.length);
                          layer.setVisibleLayers([showindex]);
                           map.addLayer(layer);
                         map.on("layer-add-result",function(e){
                             if(e.error){
                                 error.errorMessage = e.error;
                                 error.id=e.layer.id;
                                 layui.use('layer', function () {
                                     var layer = layui.layer;
                                     layer.open({
                                         title: '服务地址有误'
                                         ,content: "服务地址有误，请确认！地址是"+error.id
                                     });
                                 })
                             }else{
                                 style();
                                                               layui.use('layer', function () {
                                     var lay = layui.layer;
                                     lay.confirm('加载成功，缩放到该图层?', {
                                         icon: 3,
                                         title: '提示'
                                     }, function (layui_index) {
                                         try {
                                             layerExtent=layer.fullExtent;
                                             //如果图层与地图坐标系不同，转换一下再设置全局范围
                                             if(layerExtent.spatialReference.wkid!=map.spatialReference.wkid ){
                                                 require(["esri/tasks/GeometryService","esri/config"], function(GeometryService,config) {
                                                     //配置代理
                                                     config.defaults.io.proxyUrl = "../esriproxy/";
                                                     config.defaults.io.alwaysUseProxy = false;
                                                     var geometryService = new GeometryService(ESRI_GeometyService);
                                                     geometryService.project([layerExtent],map.spatialReference, function (p) {
                                                         console.log(p);
                                                         map.setExtent(p[0]);
                                                     });
                                                 });
                                             }else {
                                                 map.setExtent(layerExtent);
                                             }
                                         }catch (e) {
                                             lay.open({
                                                 title: '提示'
                                                 ,content: '服务图层与底图图层坐标系统不同，暂时无法缩放！您可手动缩放到该图层！'
                                             });
                                             console.log(e);
                                         }
                                         lay.close(layui_index);
                                     })
                                 })
                             }
                         },this)
                     });
                 }

             } else {
                 //获取当前要素服务在制图模板中信息
                 serviceLayerItem = template.serviceLayer.modules[$($(this).parent()).attr("data-index")]
                 //获取当前要取消的图层
                 thisFLayer = map.getLayer(serviceLayerItem.url);
                 thisFLayer.hide()
                 //map.removeLayer(thisFLayer)
                 $(this).attr("state", "0")
                 $($(this).parent()).find("a").css("color", 'yellow');
             }
             //alert($(this).text());
             var style = function(){
                 $(itemClicked).attr("state", "1");
                 $($(itemClicked).parent()).find("a").css("color", ' rgb(0, 253, 255)');
             }
         });
         //要素图层样式设置点击事件
         $(".serviceLayerCssI2").click(function () {
             //获取当前要素服务在制图模板中信息
             serviceLayerItem = template.serviceLayer.modules[$($(this).parent()).attr("data-index")]
             templateIndex = $($(this).parent()).attr("data-index");
             layui.use('layer', function () {
                 var layer = layui.layer;
                 layer.open({
                     title: '设置',
                     skin: "layui-layer-lan",

                     yes: function (index, layero) {
                         $($(this).parent()).find(".serviceLayerCssI1").css("color", '#e2e2e2');
                         index = $("#FLAddress").attr("templateIndex");
                         var url = template.serviceLayer.modules[index].url;
                         if(url !== null && url !== undefined && url !== ''){
                             thisFLayer = map.getLayer(url)
                             if(thisFLayer){
                                 map.removeLayer(thisFLayer)
                             }
                             $(this).attr("state", "0")
                         }
                         template.serviceLayer.modules[index].url = $("#FLAddress").val();
                         generateServiceLayer();
                         var index1 = layer.open();
                         layer.close(index1);

                     },
                     content: "<br/><p class='FLS_p'>服务地址：<input id='FLAddress' templateIndex='" + templateIndex + "'  value='"+serviceLayerItem.url+"'></input><i class='FLS_i fa fa-cog'></i></p>"
                 });

             });


         });

     }

}


