//一些全局变量
var layui_element, layui_layer;
var gaodeLayer = null;
var mapView = null;
var measurementWidget = null;
var editToolbar = null;
var pointFeatureLayer, polylineFeatureLayer, polygonFeatureLayer;
var drawTool = null;
var pointFeatureLayerClick = null;
var polylineFeatureLayerClick = null;
var polygonFeatureLayerClick = null;
var defaultBaseMap = null;
var customDefineBaseMap = null;
var baseMap = null;
var markingLayer = null;
var measureLayer = null;
var drawType;
var lastDrawFeature;
var markStyle = {
    textMarker:{
        text:'请输入标注',
        font:'STYLE_NORMAL',
        color:'rgba(255,0,0,1)'
    },
    label: {
        fontSize: 12,
        fontColor: 'rgba(170,51,0,1)',
        fontName: 'Arial'
    },
    point: {
        icon: 'assets/img/pointIcon/8.png' 
    },
    line: {
        lineColor: 'rgba(255,69,0,0.6)',
        lineWidth: 2
    },
    polygon: {
        borderColor: 'rgba(255,215,0,0.6)',
        borderWidth: 2,
        fillColor: 'rgba(255,69,0,0.5)'
    }
};

//dom文档树加载完和所有文件加载完之后，执行下列代码
$(document).ready(function() {
 	layui.use(['layer', 'element', 'form'], function() {
 		layui_layer = layui.layer;
 	});
	require([
		"esri/map",
		"esri/dijit/Scalebar", 
		"esri/dijit/BasemapToggle",
		"esri/geometry/webMercatorUtils",
		"esri/dijit/Measurement",
		"esri/toolbars/edit",
		"dojo/dom",
		"esri/layers/FeatureLayer",
		"esri/toolbars/draw", 
		"esri/geometry/Point",
		"esri/geometry/Polyline",
		"esri/geometry/Polygon", 
		"esri/graphic", 
		"esri/symbols/SimpleLineSymbol",
		"esri/symbols/SimpleFillSymbol",
		"esri/symbols/PictureMarkerSymbol",
		"esri/Color", 
		"esri/symbols/TextSymbol",
        "esri/symbols/Font",
		"esri/layers/LabelClass",
		"esri/SpatialReference", 
		"esri/dijit/PopupTemplate",
		"dojo/domReady!"
	], function(
			Map, Scalebar, BasemapToggle,
			WebMercatorUtils, Measurement, Edit, Dom, FeatureLayer,
			Draw, Point, Polyline, Polygon, Graphic, SimpleLineSymbol, 
			SimpleFillSymbol, PictureMarkerSymbol, Color, TextSymbol,Font,
			LabelClass, SpatialReference, PopupTemplate
		) {
			var scalebar = new Scalebar({
				map: map,
				scalebarUnit: "metric"
			});
//			measurementWidget = new Measurement({
//				map: map
//			}, Dom.byId("measurementDiv"));
//			measurementWidget.startup();
            var node = document.getElementById("map-mark-edit")
			editToolbar = new Edit(map,{
                textSymbolEditorHolder:node
            });
			var statesColor = new Color("#666");
			var statesLabel = new TextSymbol().setColor(statesColor);
			statesLabel.font.setSize("14pt");
			statesLabel.font.setFamily("arial");
			var pointJson = {
				"labelPlacement": "below-center",
				"labelExpressionInfo": {
					"value": "{name}"
				}
			};
			var polylineJson = {
				"labelPlacement": "below-end",
				"labelExpressionInfo": {
					"value": "{name}"
				}
			};
			var polygonJson = {
				"labelPlacement": "always-horizontal",
				"labelExpressionInfo": {
					"value": "{name}"
				}
			};
			var pointLabelClass = new LabelClass(pointJson);
			pointLabelClass.symbol = statesLabel;
			var polylineLabelClass = new LabelClass(polylineJson);
			polylineLabelClass.symbol = statesLabel;
			var polygonLabelClass = new LabelClass(polygonJson);
			polygonLabelClass.symbol = statesLabel;
			var pointLayerDefinition = {
				"geometryType": "esriGeometryPoint",
				"fields": [{
						"name": "ObjectID",
						"alias": "ObjectID",
						"type": "esriFieldTypeOID"
					},
					{
						"name": "id",
						"type": "esriFieldTypeString",
						"alias": "id"
					},
					{
						"name": "name",
						"type": "esriFieldTypeString",
						"alias": "名称"
					},
					{
						"name": "remark",
						"type": "esriFieldTypeString",
						"alias": "备注"
					}
				]
			};
			pointFeatureLayer = new FeatureLayer({
				layerDefinition: pointLayerDefinition,
				featureSet: null
			}, {
				showLabels: true,
                id:"pointPlotLayer"
			});
			pointFeatureLayer.setLabelingInfo([pointLabelClass]);
            pointFeatureLayer.renderer.label="标绘点";
			map.addLayer(pointFeatureLayer);
			var polylineLayerDefinition = {
				"geometryType": "esriGeometryPolyline",
				"fields": [{
						"name": "ObjectID",
						"alias": "ObjectID",
						"type": "esriFieldTypeOID"
					},
					{
						"name": "id",
						"type": "esriFieldTypeString",
						"alias": "id"
					},
					{
						"name": "name",
						"type": "esriFieldTypeString",
						"alias": "名称"
					},
					{
						"name": "remark",
						"type": "esriFieldTypeString",
						"alias": "备注"
					}
				]
			};
			polylineFeatureLayer = new FeatureLayer({
				layerDefinition: polylineLayerDefinition,
				featureSet: null
			}, {
				showLabels: true,
                id:"linePlotLayer"
			});
			polylineFeatureLayer.setLabelingInfo([polylineLabelClass]);
            polylineFeatureLayer.renderer.label="标绘线";
			map.addLayer(polylineFeatureLayer);
			var polygonLayerDefinition = {
				"geometryType": "esriGeometryPolygon",
				"fields": [{
						"name": "ObjectID",
						"alias": "ObjectID",
						"type": "esriFieldTypeOID"
					},
					{
						"name": "id",
						"type": "esriFieldTypeString",
						"alias": "id"
					},
					{
						"name": "name",
						"type": "esriFieldTypeString",
						"alias": "名称"
					},
					{
						"name": "remark",
						"type": "esriFieldTypeString",
						"alias": "备注"
					}
				],
                label: "Transportation District"
			};
			polygonFeatureLayer = new FeatureLayer({
				layerDefinition: polygonLayerDefinition,
				featureSet: null
			}, {
				showLabels: true,
                id:"polygonPlotLayer"
			});
			polygonFeatureLayer.setLabelingInfo([polygonLabelClass]);
			polygonFeatureLayer.renderer.label="标绘面";
			map.addLayer(polygonFeatureLayer);
            drawTool = new Draw(map);
            //在地图界面显示坐标信息
			/* map.on('mouse-move', function(evt) {
				var point = map.toMap({
					x: evt.x,
					y: evt.y
				});
				var lngLat = WebMercatorUtils.xyToLngLat(point.x, point.y);
				document.getElementById('mouse-coordinate').innerHTML = '当前鼠标位置：' + formatDegree(lngLat[0], 0, 2) + '&nbsp;&nbsp;' + formatDegree(lngLat[1], 1, 2)
			});
			map.on('zoom-end', function() {
				document.getElementById('zoom-level').innerHTML = "缩放级别：" + Math.round(map.getZoom())
			});
			map.on("extent-change", function(data) {
				var center = data.extent.getCenter();
				var lngLat = WebMercatorUtils.xyToLngLat(center.x, center.y);
				document.getElementById('map-center').innerHTML = '地图视图中心点：' + formatDegree(lngLat[0], 0, 2) + '&nbsp;&nbsp;' + formatDegree(lngLat[1], 1, 2)
			}); */
			map.on('click', function(evt) {});
			var arcgisFeatureCacheListCache = localstorageGet('arcgisFeatureCacheList');
			if(arcgisFeatureCacheListCache) {
				var pointFeature = [];
				for(var index = 0; index < arcgisFeatureCacheListCache.length; index++) {
					var featureObj = arcgisFeatureCacheListCache[index];
					var graphic = new Graphic();
					var symbol = null;
					var geom = null;
					switch(featureObj.type) {
						case "point":
							geom = new Point(featureObj.coordinates);
							symbol = new PictureMarkerSymbol(featureObj.style.point.icon, 32, 32);
							geom.spatialReference = new SpatialReference(3857);
							graphic.setGeometry(geom);
							graphic.setSymbol(symbol);
							graphic.setAttributes({
								"id": featureObj.id,
								"style": featureObj.style,
								"name": featureObj.name,
								'remark': featureObj.remark
							});
							pointFeatureLayer.add(graphic);
							break;
                        case "textsymbol":
                            geom = new Point(featureObj.coordinates);
                            symbol = new TextSymbol(featureObj.style);
                            if(featureObj.name){
                                symbol.setText(featureObj.name);
                            }else{
                                symbol.setText(markStyle.textMarker.text);
                            }
                            geom.spatialReference = new SpatialReference(3857);
                            graphic.setGeometry(geom);
                            graphic.setSymbol(symbol);
                            graphic.setAttributes({
                                "id": featureObj.id,
                                "style": featureObj.style,
                                "name": featureObj.name,
                                'remark': featureObj.remark
                            });
                            pointFeatureLayer.add(graphic);
                            break;
						case "polyline":
							geom = new Polyline(featureObj.coordinates);
							symbol = new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, Color.fromRgb(featureObj.style.line.lineColor), featureObj.style.line.lineWidth);
							geom.spatialReference = new SpatialReference(3857);
							graphic.setGeometry(geom);
							graphic.setSymbol(symbol);
							graphic.setAttributes({
								"id": featureObj.id,
								"style": featureObj.style,
								"name": featureObj.name,
								'remark': featureObj.remark
							});
							polylineFeatureLayer.add(graphic);
							break;
						case "polygon":
							geom = new Polygon(featureObj.coordinates);
							symbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID, new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, Color.fromRgb(featureObj.style.polygon.borderColor), featureObj.style.polygon.borderWidth), Color.fromRgb(featureObj.style.polygon.fillColor));
							geom.spatialReference = new SpatialReference(3857);
							graphic.setGeometry(geom);
							graphic.setSymbol(symbol);
							graphic.setAttributes({
								"id": featureObj.id,
								"style": featureObj.style,
								"name": featureObj.name,
								'remark': featureObj.remark
							});
							polygonFeatureLayer.add(graphic);
							break
					}
				}
			}
		});
});

//marking函数，开始标绘，打开面板
var marking  = function (type) {
    drawType = null;
    clearAllMapInteraction();
    var geometryFunction, panelHeadTitle, panelTip;
    var pointStyleItem = document.getElementById('point-style-item');
    var lineStyleItem = document.getElementById('line-style-item');
    var polygonStyleItem = document.getElementById('polygon-style-item');
    switch (type) {
     case 'TEXT':
        panelHeadTitle = "绘制字符标注";
        panelTip = "地图上待标注位置鼠标左键点击地图添加一个字符标注，可填写名称、备注，点击保存生效！";
        removeClass(pointStyleItem, "show");
        removeClass(lineStyleItem, "show");
        removeClass(polygonStyleItem, "show");
        break;
    case 'POINT':
        panelHeadTitle = "绘制点标注";
        panelTip = "地图上待标注位置鼠标左键点击地图添加一个点标注，可填写名称、备注、图标，点击保存生效！";
        addClass(pointStyleItem, "show");
        removeClass(lineStyleItem, "show");
        removeClass(polygonStyleItem, "show");
        break;
    case 'POLYLINE':
        panelHeadTitle = "绘制线标注";
        panelTip = "地图上待标注位置鼠标左键依次点击地图绘制线，双击鼠标左键结束绘制，可填写名称、备注、线样式，点击保存生效！";
        removeClass(pointStyleItem, "show");
        addClass(lineStyleItem, "show");
        removeClass(polygonStyleItem, "show");
        break;
    case 'FREEHAND_POLYLINE':
        panelHeadTitle = "绘制线标注";
        panelTip = "长按鼠标左键开始绘制，释放结束绘制，可填写名称、备注、线样式，点击保存生效！";
        removeClass(pointStyleItem, "show");
        addClass(lineStyleItem, "show");
        removeClass(polygonStyleItem, "show");
        break;
    case 'POLYGON':
        panelHeadTitle = "绘制多边形标注";
        panelTip = "地图上待标注位置鼠标左键依次点击地图绘制面，双击鼠标左键结束绘制，可填写名称、备注、面样式，点击保存生效！";
        removeClass(pointStyleItem, "show");
        removeClass(lineStyleItem, "show");
        addClass(polygonStyleItem, "show");
        break;
    case 'FREEHAND_POLYGON':
        panelHeadTitle = "自由绘制多边形标注";
        panelTip = "地图上待标注位置鼠标左键依次点击地图绘制面，双击鼠标左键结束绘制，可填写名称、备注、面样式，点击保存生效！";
        removeClass(pointStyleItem, "show");
        removeClass(lineStyleItem, "show");
        addClass(polygonStyleItem, "show");
        break;
    case 'RECTANGLE':
        panelHeadTitle = "绘制矩形标注";
        panelTip = "地图上长按鼠标左键开始绘制，释放结束绘制，可填写名称、备注、面样式，点击保存生效！";
        removeClass(pointStyleItem, "show");
        removeClass(lineStyleItem, "show");
        addClass(polygonStyleItem, "show");
        break;
    case 'ARROW':
        panelHeadTitle = "绘制箭头标注";
        panelTip = "地图上长按鼠标左键开始绘制，释放结束绘制，可填写名称、备注、面样式，点击保存生效！";
        removeClass(pointStyleItem, "show");
        removeClass(lineStyleItem, "show");
        addClass(polygonStyleItem, "show");
        break;
    case 'TRIANGLE':
        panelHeadTitle = "绘制箭头标注";
        panelTip = "地图上长按鼠标左键开始绘制，释放结束绘制，可填写名称、备注、面样式，点击保存生效！";
        removeClass(pointStyleItem, "show");
        removeClass(lineStyleItem, "show");
        addClass(polygonStyleItem, "show");
        break;
    case 'CIRCLE':
        panelHeadTitle = "绘制圆形标注";
        panelTip = "地图上长按鼠标左键开始绘制，释放结束绘制，可填写名称、备注、圆样式，点击保存生效！";
        removeClass(pointStyleItem, "show");
        removeClass(lineStyleItem, "show");
        addClass(polygonStyleItem, "show");
        break;
    case 'ELLIPSE':
        panelHeadTitle = "绘制圆形标注";
        panelTip = "地图上长按鼠标左键开始绘制，释放结束绘制，可填写名称、备注、圆样式，点击保存生效！";
        removeClass(pointStyleItem, "show");
        removeClass(lineStyleItem, "show");
        addClass(polygonStyleItem, "show");
        break;
    case 'EDIT':
        panelHeadTitle = "标注编辑";
        panelTip = "选择需要编辑的标注进行编辑！";
        break
    }
    if (type != 'EDIT') {
        addDrawInteraction(type);
        openPanelContent(['map-marking-panel', 'map-marking-panel-tip'])
    } else {
        openPanelContent(['map-marking-panel', 'map-marking-panel-tip', 'map-mark-edit']);
        addSelectInteraction()
    }
    document.getElementById('map-marking-panel-title').innerHTML = panelHeadTitle;
    document.getElementById('map-marking-panel-tip').innerHTML = panelTip
};

//关闭所有交互
var clearAllMapInteraction = function () {
    var obj1 = document.getElementById('map-marking-info');
    removeClass(obj1, "show");
    if (measurementWidget) {
        measurementWidget.clearResult();
        measurementWidget.setTool("location", false);
        measurementWidget.setTool("area", false);
        measurementWidget.setTool("distance", false)
    }
    if (drawTool) {
        drawTool.deactivate()
    }
    if (editToolbar) {
        editToolbar.deactivate()
    }
    if (pointFeatureLayerClick) {
        pointFeatureLayerClick.remove()
    }
    if (polylineFeatureLayerClick) {
        polylineFeatureLayerClick.remove()
    }
    if (polygonFeatureLayerClick) {
        polygonFeatureLayerClick.remove()
    }
};

//增加标绘交互
var addDrawInteraction = function (type) {
    clearAllMapInteraction();
    if (map && drawTool) {
        require(["esri/toolbars/draw", "esri/symbols/SimpleLineSymbol", "esri/symbols/SimpleFillSymbol", "esri/symbols/PictureMarkerSymbol", "esri/Color", "esri/graphic", "esri/symbols/TextSymbol","esri/symbols/Font"], function (Draw, SimpleLineSymbol, SimpleFillSymbol, PictureMarkerSymbol, Color, Graphic,TextSymbol,Font) {
            switch (type) {
            case 'POINT':
                drawTool.activate(Draw.POINT);
                break;
            case 'TEXT':
                drawTool.activate(Draw.POINT);
                break;
            case 'POLYLINE':
                drawTool.activate(Draw.POLYLINE);
                break;
            case 'FREEHAND_POLYLINE':
                drawTool.activate(Draw.FREEHAND_POLYLINE);
                break;
            case 'POLYGON':
                drawTool.activate(Draw.POLYGON);
                break;
            case 'FREEHAND_POLYGON':
                drawTool.activate(Draw.FREEHAND_POLYGON);
                break;
            case 'RECTANGLE':
                drawTool.activate(Draw.RECTANGLE);
                break;
            case 'ARROW':
                drawTool.activate(Draw.ARROW);
                break;
            case 'TRIANGLE':
                drawTool.activate(Draw.TRIANGLE);
                break;
            case 'CIRCLE':
                drawTool.activate(Draw.CIRCLE);
                break;
            case 'ELLIPSE':
                drawTool.activate(Draw.ELLIPSE);
                break
            }
            var onDrawEnd = drawTool.on("draw-end", function (evt) {
                document.getElementById('mark-name').value = "";
                document.getElementById('mark-remark').value = "";
                switch (evt.geometry.type) {
                case "point":
                case "multipoint":
                    if(type==='TEXT'){
                        symbol = new TextSymbol();
                        symbol.setColor(new Color([230, 0, 0, 1]));
                        symbol.setText("请输入标注");
                        var graphic = new Graphic(evt.geometry, symbol);
                        graphic.setAttributes({
                            "id": newId(),
                            "style": markStyle,
                            "name": "",
                            'remark': ''
                        });
                        lastDrawFeature = graphic;
                        pointFeatureLayer.add(graphic);
                    }else{
                        symbol = new PictureMarkerSymbol(markStyle.point.icon, 32, 32);
                        var graphic = new Graphic(evt.geometry, symbol);
                        graphic.setAttributes({
                            "id": newId(),
                            "style": markStyle,
                            "name": "",
                            'remark': ''
                        });
                        lastDrawFeature = graphic;
                        pointFeatureLayer.add(graphic);
                    }
                    break;
                case "polyline":
                    symbol = new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, Color.fromRgb(markStyle.line.lineColor), markStyle.line.lineWidth);
                    var graphic = new Graphic(evt.geometry, symbol);
                    graphic.setAttributes({
                        "id": newId(),
                        "style": markStyle,
                        "name": "",
                        'remark': ''
                    });
                    lastDrawFeature = graphic;
                    polylineFeatureLayer.add(graphic);
                    break;
                default:
                    symbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID, new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, Color.fromRgb(markStyle.polygon.borderColor), markStyle.polygon.borderWidth), Color.fromRgb(markStyle.polygon.fillColor));
                    var graphic = new Graphic(evt.geometry, symbol);
                    graphic.setAttributes({
                        "id": newId(),
                        "style": markStyle,
                        "name": "",
                        'remark': ''
                    });
                    lastDrawFeature = graphic;
                    polygonFeatureLayer.add(graphic);
                    break
                }
                openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip']);
                addFeatureToLocalstorage(lastDrawFeature);
                //因诸多不便之处，取消连续绘制
                if (drawTool) {
                    drawTool.deactivate()
                }
                //注销时间的监听，以防止多次绘制
                onDrawEnd.remove();
            })
        })
    }
};

//打开信息面板
var openPanelContent = function (idList) {
    var obj0 = document.getElementById('map-marking-panel');
    removeClass(obj0, "show");
    var obj1 = document.getElementById('map-marking-info');
    removeClass(obj1, "show");
    var obj2 = document.getElementById('map-marking-panel-tip');
    removeClass(obj2, "show");
    var obj3 = document.getElementById('point-style-edit');
    removeClass(obj3, "show");
    var obj4 = document.getElementById('line-style-edit');
    removeClass(obj4, "show");
    var obj5 = document.getElementById('polygon-style-edit');
    removeClass(obj5, "show");
    var obj6 = document.getElementById('map-mark-edit');
    removeClass(obj6, "show");
    var obj7 = document.getElementById('measure-info');
    removeClass(obj7, "show");
   // var obj8 = document.getElementById('map-config');
   // removeClass(obj8, "show");
    if (idList) {
        for (var index = 0; index < idList.length; index++) {
            var id = idList[index];
            var obj = document.getElementById(id);
            addClass(obj, "show")
        }
    }
};

//将标绘图形缓存到本地
var addFeatureToLocalstorage = function (feature) {
    require(["esri/symbols/SimpleLineSymbol", "esri/symbols/SimpleFillSymbol", "esri/symbols/PictureMarkerSymbol", "esri/Color", "esri/symbols/TextSymbol","esri/symbols/Font"], function (SimpleLineSymbol, SimpleFillSymbol, PictureMarkerSymbol, Color,TextSymbol,Font) {
        var arcgisFeatureCacheList = localstorageGet('arcgisFeatureCacheList');
        if (!arcgisFeatureCacheList) {
            arcgisFeatureCacheList = [];
            localstorageSet('arcgisFeatureCacheList', [])
        };
        var saveFeature = null;
        var symbol = null;
        var geom = feature.geometry;
        var type = geom.type;
        var coordinates = [];
        switch (type) {
        case 'point':
            coordinates = [geom.x, geom.y];
            if(feature.symbol.type==="textsymbol"){
                if(feature.symbol){
                    symbol = new TextSymbol(feature.symbol);
                }
                if(feature.attributes['name']){
                    symbol.setText(feature.attributes['name']);
                }else{
                    symbol.setText(markStyle.textMarker.text);
                }
            }else{
                symbol = new PictureMarkerSymbol(markStyle.point.icon, 32, 32);
            }

            break;
        case 'polyline':
            coordinates = geom.paths;
            symbol = new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, Color.fromRgb(markStyle.line.lineColor), markStyle.line.lineWidth);
            break;
        case 'polygon':
            coordinates = geom.rings;
            symbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID, new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, Color.fromRgb(markStyle.polygon.borderColor), markStyle.polygon.borderWidth), Color.fromRgb(markStyle.polygon.fillColor));
            break
        }
        feature.setSymbol(symbol);
        saveFeature = {
            id: feature.attributes["id"],
            name: feature.attributes["name"],
            remark: feature.attributes["remark"],
            type: feature.symbol.type==="textsymbol"?"textsymbol":type,
            coordinates: coordinates,
            style: feature.symbol.type==="textsymbol"?feature.symbol:feature.attributes["style"]
        };
        if (saveFeature) {
            var existed = false;
            for (var index = 0; index < arcgisFeatureCacheList.length; index++) {
                var element = arcgisFeatureCacheList[index];
                if (element.id == saveFeature.id) {
                    arcgisFeatureCacheList[index] = saveFeature;
                    existed = true;
                    break
                }
            }
            if (!existed) {
                arcgisFeatureCacheList.push(saveFeature)
            }
            localstorageSet('arcgisFeatureCacheList', arcgisFeatureCacheList)
        }
        pointFeatureLayer.refresh();
        polylineFeatureLayer.refresh();
        polygonFeatureLayer.refresh()
    })
};

//保存为标绘图形增加的属性信息
var infoSave = function () {
    var markName = document.getElementById('mark-name');
    var markRemark = document.getElementById('mark-remark');
    if(lastDrawFeature){
    lastDrawFeature.attributes['name'] = markName.value;
    lastDrawFeature.attributes['remark'] = markRemark.value;
    lastDrawFeature.attributes['style'] = markStyle;
    addFeatureToLocalstorage(lastDrawFeature);
    }
    var obj1 = document.getElementById('map-marking-info');
    removeClass(obj1, "show");
    marking(edit);
};

//改变标绘点的图标
var changeIcon = function () {
    // openPanelContent(['map-marking-panel', 'point-style-edit']);
    // var obj = document.getElementById('point-style-edit');
    // for (var i = 0; i < obj.children[0].children.length; i++) {
    //     obj.children[0].children[i].onclick = function () {
    //         var img = this.children[0];
    //         document.getElementById('pointIcon').src = img.src;
    //         markStyle.point.icon = img.src;
    //         if (drawType == 'edit') {
    //             openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip', 'map-mark-edit'])
    //         } else {
    //             addDrawInteraction('Point');
    //             openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip'])
    //         }
    //     }
    // }
    openPanelContent(['map-marking-panel', 'point-style-edit']);
    var obj = document.getElementsByClassName('emergencyPlotIcon');
    $(obj).click(function(e){
        var thisDom = e.target;
        document.getElementById('pointIcon').src = thisDom.src;
        markStyle.point.icon = thisDom.src;
        if (drawType == 'edit') {
            openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip', 'map-mark-edit'])
        } else {
            addDrawInteraction('Point');
            openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip'])
        }
    });
};

//关闭标绘信息面板
var closeMarkingPanel = function () {
    clearAllMapInteraction();
    openPanelContent(null)
};

//添加选择交互
var addSelectInteraction = function () {
    clearAllMapInteraction();
    pointFeatureLayerClick = pointFeatureLayer.on("click", function (evt) {
        var feature = evt.graphic;
        selectedFunc(feature)
    });
    polylineFeatureLayerClick = polylineFeatureLayer.on("click", function (evt) {
        var feature = evt.graphic;
        selectedFunc(feature)
    });
    polygonFeatureLayerClick = polygonFeatureLayer.on("click", function (evt) {
        var feature = evt.graphic;
        selectedFunc(feature)
    });
    document.getElementById('map-mark-edit-tip').innerHTML = '鼠标左键点击选择标注对象，查看和编辑详情'
};

//对选择的要素进行属性编辑
var selectedFunc = function (feature) {
    var pointStyleItem = document.getElementById('point-style-item');
    var lineStyleItem = document.getElementById('line-style-item');
    var polygonStyleItem = document.getElementById('polygon-style-item');
    switch (feature.geometry.type) {
    case "point":
        if(feature.symbol.type==="textsymbol"){
            removeClass(pointStyleItem, "show");
            removeClass(lineStyleItem, "show");
            removeClass(polygonStyleItem, "show");
            lastDrawFeature = feature;
            document.getElementById('mark-name').value = feature.attributes['name'];
            document.getElementById('mark-remark').value = feature.attributes['remark'];
            openPanelContent(['map-marking-panel', 'map-marking-panel-tip', 'map-mark-edit', 'map-marking-info']);
            document.getElementById('map-mark-edit-tip').innerHTML = "鼠标左键点击选择标注对象"
            return;
        }
        addClass(pointStyleItem, "show");
        removeClass(lineStyleItem, "show");
        removeClass(polygonStyleItem, "show");
        break;
    case "polyline":
        removeClass(pointStyleItem, "show");
        addClass(lineStyleItem, "show");
        removeClass(polygonStyleItem, "show");
        break;
    case 'polygon':
        removeClass(pointStyleItem, "show");
        removeClass(lineStyleItem, "show");
        addClass(polygonStyleItem, "show");
        break
    }
    lastDrawFeature = feature;
    document.getElementById('mark-name').value = feature.attributes['name'];
    document.getElementById('mark-remark').value = feature.attributes['remark'];
    var style = feature.attributes['style'];
    markStyle = style;
    document.getElementById('pointIcon').src = style.point.icon;
    var lineStyle = document.getElementById('line-style');
    lineStyle.style.backgroundColor = style.line.lineColor;
    lineStyle.style.height = style.line.lineWidth + 'px';
    var polygonStyle = document.getElementById('polygon-style');
    polygonStyle.style.backgroundColor = style.polygon.fillColor;
    polygonStyle.style.border = style.polygon.borderWidth + 'px solid ' + style.polygon.borderColor;
    openPanelContent(['map-marking-panel', 'map-marking-panel-tip', 'map-mark-edit', 'map-marking-info']);
    document.getElementById('map-mark-edit-tip').innerHTML = "鼠标左键点击选择标注对象"
};

//编辑已标绘图层（有个坑需要填：样式）
var addModifyInteraction = function () {
    clearAllMapInteraction();
    pointFeatureLayerClick = pointFeatureLayer.on("click", function (evt) {
        var graphic = evt.graphic;
        modifyFunc(graphic)
    });
    polylineFeatureLayerClick = polylineFeatureLayer.on("click", function (evt) {
        var graphic = evt.graphic;
        modifyFunc(graphic)
    });
    polygonFeatureLayerClick = polygonFeatureLayer.on("click", function (evt) {
        var graphic = evt.graphic;
        modifyFunc(graphic)
    });
    document.getElementById('map-mark-edit-tip').innerHTML = '鼠标左键点击选择待编辑的标注对象，选中之后点标注可拖动，线和面标注可添加、删除、拖动节点等操作'
};

//编辑节点
var modifyFunc = function (graphic) {
    require(["esri/toolbars/edit"], function (Edit) {
        var tool = 0;
        tool = tool | Edit.MOVE;
        tool = tool | Edit.EDIT_VERTICES;
        tool = tool | Edit.SCALE;
        tool = tool | Edit.ROTATE;
        tool = tool | Edit.EDIT_TEXT;
        var options = {
            allowAddVertices: true,
            allowDeleteVertices: true,
            uniformScaling: true,
            //textSymbolEditorHolder:true
        };
        editToolbar.activate(tool, graphic, options);
        editToolbar.on('graphic-move-stop', function (evt) {
            var feature = evt.graphic;
            var style = feature.attributes['style'];
            if(feature.symbol.type!="textsymbol")
            {
                markStyle = style;
            }
            addFeatureToLocalstorage(feature)
        })
    })
};



//删除标绘图形
var addDeleteInteraction = function () {
    clearAllMapInteraction();
    pointFeatureLayerClick = pointFeatureLayer.on("click", function (evt) {
        var graphic = evt.graphic;
        deleteFunc(graphic)
    });
    polylineFeatureLayerClick = polylineFeatureLayer.on("click", function (evt) {
        var graphic = evt.graphic;
        deleteFunc(graphic)
    });
    polygonFeatureLayerClick = polygonFeatureLayer.on("click", function (evt) {
        var graphic = evt.graphic;
        deleteFunc(graphic)
    });
    document.getElementById('map-mark-edit-tip').innerHTML = '鼠标左键点击待删除标注，弹出提示框选择“确定”删除标注'
};

//删除全部图形
var addAllDeleteInteraction = function(){
    layui_layer.confirm('确定删除所有的标注?', {
        icon: 3,
        title: '提示'
    }, function (layui_index) {
        polygonFeatureLayer.clear();
        polylineFeatureLayer.clear();
        pointFeatureLayer.clear();
        deleteAllFeatureFromLocalstorage();
        layer.close(layui_index)
    })
};

//本地删除全部图形
var deleteAllFeatureFromLocalstorage = function () {
    var arcgisFeatureCacheList = localstorageGet('arcgisFeatureCacheList');
    if (!arcgisFeatureCacheList) {
        return
    } else {
        arcgisFeatureCacheList.splice(0, arcgisFeatureCacheList.length);
        localstorageSet('arcgisFeatureCacheList', arcgisFeatureCacheList);
    }
};

//删除
var deleteFunc = function (graphic) {
	 layui_layer.confirm('确定删除选择中的标注?', {
		 icon: 3,
		 title: '提示'
	 }, function (layui_index) {
        var geom = graphic.geometry;
        var type = geom.type;
        switch (type) {
        case 'point':
            pointFeatureLayer.remove(graphic);
            break;
        case 'polyline':
            polylineFeatureLayer.remove(graphic);
            break;
        case 'polygon':
            polygonFeatureLayer.remove(graphic);
            break
        }
        deleteFeatureFromLocalstorage(graphic);
        layer.close(layui_index)
   })
};

//从本地记录中删除
var deleteFeatureFromLocalstorage = function (feature) {
    var arcgisFeatureCacheList = localstorageGet('arcgisFeatureCacheList');
    if (!arcgisFeatureCacheList) {
        return
    } else {
        for (var index = 0; index < arcgisFeatureCacheList.length; index++) {
            var element = arcgisFeatureCacheList[index];
            if (element.id == feature.attributes['id']) {
                arcgisFeatureCacheList.splice(index, 1);
                localstorageSet('arcgisFeatureCacheList', arcgisFeatureCacheList);
                break
            }
        }
    }
};

//测量工具
var addMeasureInteraction = function () {
    document.getElementById('map-marking-panel-title').innerHTML = '测量工具';
    openPanelContent(['map-marking-panel', 'measure-info']);
    require(["esri/dijit/Measurement","dojo/dom",],function(Measurement,Dom){
    	measurementWidget = new Measurement({
				map: map
			}, Dom.byId("measurementDiv"));
		measurementWidget.startup();
    })
};


var changeIconBack = function () {
    if (drawType == 'edit') {
        openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip', 'map-mark-edit'])
    } else {
        openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip'])
    }
};
var changeLineStyle = function () {
    openPanelContent(['map-marking-panel', 'line-style-edit']);
    var rgba = tinycolor(markStyle.line.lineColor).toRgb();
    var opacity = rgba.a;
    var colorHex = tinycolor(markStyle.line.lineColor).toHexString();
    document.getElementById('line-color').value = colorHex;
    document.getElementById('line-opacity').value = opacity;
    document.getElementById('line-width').value = markStyle.line.lineWidth;
    var preview = document.getElementById('line-style-preview');
    preview.style.backgroundColor = markStyle.line.lineColor;
    preview.style.height = markStyle.line.lineWidth + 'px'
};
var lineStyleChange = function () {
    var colorHex = document.getElementById('line-color').value;
    var rgb = tinycolor(colorHex).toRgb();
    var opacity = document.getElementById('line-opacity').value;
    var rgba = 'rgba(' + rgb.r + ',' + rgb.g + ',' + rgb.b + ',' + opacity + ')';
    var lineWidth = document.getElementById('line-width').value;
    var preview = document.getElementById('line-style-preview');
    preview.style.backgroundColor = rgba;
    preview.style.height = lineWidth + 'px'
};
var changeLineStyleBack = function () {
    if (drawType == 'edit') {
        openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip', 'map-mark-edit'])
    } else {
        openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip'])
    }
};
var saveLineStyle = function () {
    var colorHex = document.getElementById('line-color').value;
    var rgb = tinycolor(colorHex).toRgb();
    var opacity = document.getElementById('line-opacity').value;
    var rgba = 'rgba(' + rgb.r + ',' + rgb.g + ',' + rgb.b + ',' + opacity + ')';
    var lineWidth = document.getElementById('line-width').value;
    markStyle.line.lineColor = rgba;
    markStyle.line.lineWidth = lineWidth;
    var lineStyle = document.getElementById('line-style');
    lineStyle.style.backgroundColor = rgba;
    lineStyle.style.height = lineWidth + 'px';
    if (drawType == 'edit') {
        openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip', 'map-mark-edit'])
    } else {
        addDrawInteraction('LineString');
        openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip'])
    }
};
var changePolygonStyle = function () {
    openPanelContent(['map-marking-panel', 'polygon-style-edit']);
    var border_rgba = tinycolor(markStyle.polygon.borderColor).toRgb();
    var border_opacity = border_rgba.a;
    var border_colorHex = tinycolor(markStyle.polygon.borderColor).toHexString();
    document.getElementById('polygon-border-color').value = border_colorHex;
    document.getElementById('polygon-border-opacity').value = border_opacity;
    document.getElementById('polygon-border-width').value = markStyle.polygon.borderWidth;
    var fill_rgba = tinycolor(markStyle.polygon.fillColor).toRgb();
    var fill_opacity = fill_rgba.a;
    var fill_colorHex = tinycolor(markStyle.polygon.fillColor).toHexString();
    document.getElementById('polygon-fill-color').value = fill_colorHex;
    document.getElementById('polygon-fill-opacity').value = fill_opacity;
    var preview = document.getElementById('polygon-style-preview');
    preview.style.backgroundColor = markStyle.polygon.fillColor;
    preview.style.border = markStyle.polygon.borderWidth + 'px solid ' + markStyle.polygon.borderColor
};
var polygonStyleChange = function () {
    var border_colorHex = document.getElementById('polygon-border-color').value;
    var border_opacity = document.getElementById('polygon-border-opacity').value;
    var border_width = document.getElementById('polygon-border-width').value;
    var border_rgb = tinycolor(border_colorHex).toRgb();
    var border_rgba = 'rgba(' + border_rgb.r + ',' + border_rgb.g + ',' + border_rgb.b + ',' + border_opacity + ')';
    var fill_colorHex = document.getElementById('polygon-fill-color').value;
    var fill_opacity = document.getElementById('polygon-fill-opacity').value;
    var fill_rgb = tinycolor(fill_colorHex).toRgb();
    var fill_rgba = 'rgba(' + fill_rgb.r + ',' + fill_rgb.g + ',' + fill_rgb.b + ',' + fill_opacity + ')';
    var preview = document.getElementById('polygon-style-preview');
    preview.style.backgroundColor = fill_rgba;
    preview.style.border = border_width + 'px solid ' + border_rgba
};
var changePolygonStyleBack = function () {
    if (drawType == 'edit') {
        openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip', 'map-mark-edit'])
    } else {
        openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip'])
    }
};
var savePolygonStyle = function () {
    var border_colorHex = document.getElementById('polygon-border-color').value;
    var border_opacity = document.getElementById('polygon-border-opacity').value;
    var border_width = document.getElementById('polygon-border-width').value;
    var border_rgb = tinycolor(border_colorHex).toRgb();
    var border_rgba = 'rgba(' + border_rgb.r + ',' + border_rgb.g + ',' + border_rgb.b + ',' + border_opacity + ')';
    var fill_colorHex = document.getElementById('polygon-fill-color').value;
    var fill_opacity = document.getElementById('polygon-fill-opacity').value;
    var fill_rgb = tinycolor(fill_colorHex).toRgb();
    var fill_rgba = 'rgba(' + fill_rgb.r + ',' + fill_rgb.g + ',' + fill_rgb.b + ',' + fill_opacity + ')';
    markStyle.polygon.borderColor = border_rgba;
    markStyle.polygon.borderWidth = border_width;
    markStyle.polygon.fillColor = fill_rgba;
    var polygonStyle = document.getElementById('polygon-style');
    polygonStyle.style.backgroundColor = fill_rgba;
    polygonStyle.style.border = border_width + 'px solid ' + border_rgba;
    if (drawType == 'edit') {
        openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip', 'map-mark-edit'])
    } else {
        addDrawInteraction(drawType);
        openPanelContent(['map-marking-panel', 'map-marking-info', 'map-marking-panel-tip'])
    }
};

$("#iMLegendDiv").on('DOMNodeInserted',function(){
    //当修改节点时执行,修正图例，去掉标绘的图例
    $("#iMLegendDiv_polygonPlotLayer").css('display','none');
    $("#iMLegendDiv_linePlotLayer").css('display','none');
    $("#iMLegendDiv_pointPlotLayer").css('display','none');
});