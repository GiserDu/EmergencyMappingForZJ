var map = L.map('map', {
	// crs: crs,
	// center: [29.231858, 120.105835],
	// zoom: 6
	crs:L.CRS.CustomEPSG4326,
	center: {lon:120.33999, lat:29.231858},
	zoom:8,
	inertiaDeceleration:15000,
	maxZoom:12
});

/* 国家天地图WMTS服务 img_c/cia_c/vec_c/cva_c为EPSG4326坐标系，img_w/cia_w/vec_w/cva_w为EPSG3857坐标系   */
// 影像底图
var img = L.tileLayer("http://t{s}.tianditu.cn/img_c/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=img&tileMatrixSet=c&TileMatrix={z}&TileRow={y}&TileCol={x}&style=default&format=tiles", {
	subdomains: ["0", "1", "2", "3", "4", "5", "6", "7"]
});
// 影像地名标注
var img_lab = L.tileLayer("http://t{s}.tianditu.cn/cia_c/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=cia&tileMatrixSet=c&TileMatrix={z}&TileRow={y}&TileCol={x}&style=default&format=tiles", {
	subdomains: ["0", "1", "2", "3", "4", "5", "6", "7"]
});
// 边界
var img_boundary = L.tileLayer("http://t{s}.tianditu.cn/ibo_c/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=ibo&tileMatrixSet=c&TileMatrix={z}&TileRow={y}&TileCol={x}&style=default&format=tiles", {
	subdomains: ["0", "1", "2", "3", "4", "5", "6", "7"]
});

// 矢量底图
var vec = L.tileLayer("http://t{s}.tianditu.cn/vec_c/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=vec&tileMatrixSet=c&TileMatrix={z}&TileRow={y}&TileCol={x}&style=default&format=tiles", {
	subdomains: ["0", "1", "2", "3", "4", "5", "6", "7"]
});
// 矢量地名标注
var vec_lab = L.tileLayer("http://t{s}.tianditu.cn/cva_c/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=cva&tileMatrixSet=c&TileMatrix={z}&TileRow={y}&TileCol={x}&style=default&format=tiles", {
	subdomains: ["0", "1", "2", "3", "4", "5", "6", "7"]
});
// 矢量边界
var vec_boundary = L.tileLayer("http://t{s}.tianditu.cn/vbo_c/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=vbo&tileMatrixSet=c&TileMatrix={z}&TileRow={y}&TileCol={x}&style=default&format=tiles", {
	subdomains: ["0", "1", "2", "3", "4", "5", "6", "7"]
});

// 影像底图图层组
var imgLayerGroup = L.layerGroup([img,img_boundary]);
// 矢量底图图层组
var vecLayerGroup = L.layerGroup([vec,vec_boundary]);

map.addLayer(imgLayerGroup);
// map.addLayer(vecLayerGroup);

// 测试加载矢量瓦片底图
var zjVecTilelayer = new L.GXYZ('http://ditu2.zjzwfw.gov.cn/mapserver/vmap/zjvmap/getMAP?x={x}&y={y}&l={z}&styleId=kejiyangshi2_2017',{tileSize:512,maxZoom:20});
// 添加注记图层
var zjVecTileLablayer = new L.GWVTAnno({tileSize:512,opacity:1});
var dataSource = new Custom.URLDataSource();
url = 'http://ditu.zjzwfw.gov.cn/mapserver/label/zjvmap_new/getDatas?x=${x}&y=${y}&l=${z}&styleId=lidong_new1';
dataSource.url ='http://ditu.zjzwfw.gov.cn/mapserver/label/zjvmap/getDatas?x=${x}&y=${y}&l=${z}&styleId=tdt_kejiganyangshi_2017';
zjVecTileLablayer.addDataSource(dataSource);

var zj_vecTileGroup = L.layerGroup([zjVecTilelayer,zjVecTileLablayer]);
map.addLayer(zj_vecTileGroup);