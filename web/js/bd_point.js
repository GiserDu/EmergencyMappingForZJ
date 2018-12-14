// 图层控制控件
var layerControl = L.control.layers(
    {
        "天地图影像底图":imgLayerGroup,
        "天地图矢量底图":vecLayerGroup,
    },{
        "浙江省矢量瓦片":zj_vecTileGroup,
    }
).addTo(map);

// 热力图图层
var heapMapSource,heatLayer;
var heapmapOptions = {
    minOpacity:0,
    maxZoom: 15,
    max:0.8,
    radius:20,
    blur:25,
    // gradient:{0.2:'green', 0.4: 'lime', 0.6: 'yellow', 0.8: 'orange', 1: 'red'}
};

// echarts图层
var echartsLayer,echartsOption,placeList;

// 点聚合图层
var geoJsonData,geoJsonLayer,markers;

layui.use(['layer','upload'], function(){
    var $ = layui.jquery, layer=layui.layer, upload = layui.upload;
    layer.open({
        type: 1,
        title: ['上传可视化数据'],
        shadeClose: false,
        skin:"layui-layer-lan",
        shade: 0,
        area:['400px','300px'],
        // area:['600px','370px'],
        content:
        '<div class="layui-form" lay-filter="userDataField" id="userDataField">'+
        '<div id="shpFileUploadControl" class="layui-upload-drag">\n' +
        '<i class="layui-icon layui-icon-upload-drag"></i>\n' +
        '<p>点击或拖拽上传文件</p>\n' +
        '</div>\n' +
        // '<div>\n' +
        // '<button type="button" class="layui-btn layui-btn-primary" id="shpLoadConfirmBtn">上传</button>' +
        // '</div>\n' +
        '</div>'
    });
    //拖拽上传
    upload.render({
        elem: '#shpFileUploadControl',
        url: './servlet/fileUploadServlet',
        auto: true,
        accept: 'file', //普通文件
        exts: 'json|geojson', //只允许上传压缩文件
        // bindAction: '#shpLoadConfirmBtn',
        done: function(res){
            // console.log(res);
            swal({
                title: "数据上传成功",
                text: "请在图层控件中选择查看！",
                // type: "success",
                imageUrl:"image/success.png",
                imageWidth:40,
                imageHeight:40,
                animation:false,
                showCancelButton: false,
                confirmButtonText: "确定",
                closeOnConfirm: false,
                closeOnCancel: false
            });
            var dataUrl = res.saveFilePath;
            fetch(dataUrl).then(response=>response.json())
            .then(data=>{
                // 添加echarts散点图
                placeList = data.features;
                placeList = placeList.map(function (p) { return { name:p.properties[featureName], geoCoord:p.geometry.coordinates,value:Math.random()*10}; });
                echartsOption = {
                    backgroundColor: 'transparent',
                    color: [
                        'rgba(255, 255, 255, 0.8)',
                        'rgba(14, 241, 242, 0.8)',
                        'rgba(37, 140, 249, 0.8)'
                    ],
                    // title: {
                    //     text: '浙江省山塘分布散点图',
                    //     // subtext: '纯属虚构',
                    //     x: 'center',
                    //     textStyle: {
                    //         color: '#fff'
                    //     }
                    // },
                    legend: {
                        orient: 'vertical',
                        x: 'right',
                        y:'bottom',
                        data: ['强', '中', '弱'],
                        textStyle: {
                            color: '#fff'
                        },
                        // show:false
                    },
                    // toolbox: {
                    //     show : true,
                    //     orient : 'vertical',
                    //     x: 'right',
                    //     y: 'center',
                    //     feature : {
                    //         mark : {show: true},
                    //         dataView : {show: true, readOnly: false},
                    //         restore : {show: true},
                    //         saveAsImage : {show: true}
                    //     }
                    // },
                    series: [
                        {
                            name: '弱',
                            type: 'map',
                            mapType: 'none',
                            itemStyle: {
                                normal: {
                                    borderColor: 'rgba(100,149,237,1)',
                                    borderWidth: 1.5,
                                    areaStyle: {
                                        color: '#1b1b1b'
                                    }
                                }
                            },
                            data: [],
                            markPoint: {
                                symbolSize: 2,
                                large: true,
                                effect: {
                                    show: true,
                                    type: 'scale',
                                    loop: true,
                                    period: 5,
                                    scaleSize : 2,
                                    bounceDistance: 10,
                                    color : null,
                                    shadowColor : null,
                                    shadowBlur : 1
                                },
                                data: (function () {
                                    var data = [];
                                    var len = 2000;
                                    var geoCoord
                                    while (len--) {
                                        geoCoord = placeList[len % placeList.length].geoCoord;
                                        data.push({
                                            name: placeList[len % placeList.length].name + len,
                                            value: 50,
                                            geoCoord: [
                                                geoCoord[0] + Math.random() * 0.5 - 0.1,
                                                geoCoord[1] + Math.random() * 0.5 + 0.1
                                            ]
                                        })
                                    }
                                    return data;
                                })()
                            }
                        },
                        {
                            name: '中',
                            type: 'map',
                            mapType: 'none',
                            data: [],
                            markPoint: {
                                symbolSize: 2,
                                large: true,
                                effect: {
                                    show: true,
                                    type: 'scale',
                                    loop: true,
                                    period: 10,
                                    scaleSize : 3,
                                    bounceDistance: 10,
                                    color : null,
                                    shadowColor : null,
                                    shadowBlur : 2
                                },
                                data: (function () {
                                    var data = [];
                                    var len = placeList.length;
                                    while (len--) {
                                        if(placeList[len].value>5){
                                            data.push({
                                                name: placeList[len].name,
                                                value: 90,
                                                geoCoord: placeList[len].geoCoord
                                            })
                                        }
                                    }
                                    return data;
                                })()
                            }
                        },
                        {
                            name: '强',
                            type: 'map',
                            mapType: 'none',
                            hoverable: false,
                            roam: false,
                            data: [],
                            markPoint: {
                                symbol: 'diamond',
                                symbolSize: 3,
                                large: true,
                                effect: {
                                    show: true,
                                    type: 'scale',
                                    loop: true,
                                    period: 15,
                                    scaleSize : 10,
                                    bounceDistance: 10,
                                    color : null,
                                    shadowColor : null,
                                    shadowBlur : 3
                                },
                                data: (function () {
                                    var data = [];
                                    var len = placeList.length;
                                    while (len--) {
                                        if(placeList[len].value>7){
                                            data.push({
                                                name: placeList[len].name,
                                                value: 90,
                                                geoCoord: placeList[len].geoCoord
                                            })
                                        }

                                    }
                                    return data;
                                })()
                            }
                        }
                    ]
                };
                // L.flowEcharts(echartsOption).addTo(map);
                echartsLayer = L.flowEcharts(echartsOption);
                layerControl.addOverlay(echartsLayer, '散点图');


                // 添加点聚合图
                geoJsonData = data.features;
                var featureName =  Object.keys(data.features[0].properties)[0];
                markers = L.markerClusterGroup({
                    spiderfyShapePositions: function(count, centerPt) {
                        var distanceFromCenter = 35,
                            markerDistance = 45,
                            lineLength = markerDistance * (count - 1),
                            lineStart = centerPt.y - lineLength / 2,
                            res = [],
                            i;
                        res.length = count;
                        for (i = count - 1; i >= 0; i--) {
                            res[i] = new Point(centerPt.x + distanceFromCenter, lineStart + markerDistance * i);
                        }
                        return res;
                    }
                });
                geoJsonLayer = L.geoJson(geoJsonData, {
                    onEachFeature: function (feature, layer) {
                        layer.bindPopup(feature.properties[featureName]);
                    }
                });
                markers.addLayer(geoJsonLayer);
                // map.addLayer(markers);
                layerControl.addOverlay(markers, '点聚合图');
                // map.fitBounds(markers.getBounds());

                // 添加热力图
                heapMapSource = data.features;
                heapMapSource = heapMapSource.map(function (p) { return [p.geometry.coordinates[1],p.geometry.coordinates[0]]; });
                heatLayer = L.heatLayer(heapMapSource,heapmapOptions);
                // heatLayer = L.heatLayer(heapMapSource,heapmapOptions).addTo(map);
                layerControl.addOverlay(heatLayer, '热力图');


            });
            // 上传完毕后自动关闭上传窗口
            layer.close(layer.index);
        }
    });

});

// 添加点击获取坐标信息
// var mypop = L.popup();
// map.on('click', function(e) {
//     var content = '你临幸了这个点：<br>';
//     content += e.latlng.toString();
//     mypop.setLatLng(e.latlng)
//         .setContent(content)
//         .openOn(map);
//     // heatLayer.setOptions({
//     //     minOpacity:0.1,
//     //     maxZoom: 15,
//     //     max:0.85,
//     //     radius:20,
//     //     blur:15,
//     //     gradient:{0.2:'green', 0.4: 'lime', 0.6: 'yellow', 0.8: 'orange', 1: 'red'}
//     // });
// });