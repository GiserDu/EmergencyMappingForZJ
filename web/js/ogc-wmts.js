/*
* OGC-WMTS调用
* http://t1.tianditu.cn/img_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=c&TILEMATRIX=%d&TILEROW=%d&TILECOL=%d&FORMAT=tiles
* url:img_c/cia_c/vec_c/cva_c
* layer:img影像/cia影像注记,vec矢量/cva矢量注记
*/
(function (factory) {
    //define an AMD module that relies on 'leaflet'
    if (typeof define === 'function' && define.amd) {
        define(['leaflet'], function (L) {
            return factory(L);
        });
        //define a common js module that relies on 'leaflet'
    } else if (typeof module === 'object' && typeof module.exports === 'object') {
        module.exports = factory(require('leaflet'));
    }

    if (typeof window !== 'undefined' && window.L) {
        factory(window.L);
    }
}(function (L) {
    var ogc = {
        VERSION: '1.0.0',
        Layers: {},
        Support: {
            CORS: !!(window.XMLHttpRequest && 'withCredentials' in new XMLHttpRequest()),
            pointerEvents: document.documentElement.style.pointerEvents === ''
        }
    };

    if (typeof window !== 'undefined' && window.L) {
        window.L.ogc = ogc;
    }

    L.ogc.Layers.OGCWMTSLayer = L.TileLayer.extend({
        params: {
            service: "WMTS",
            request: "GetTile",
            version: "1.0.0",
            layer: "img",
            style: "default",
            tilematrixset: "c",
            tilematrix: "{z}",
            tilerow: "{y}",
            tilecol: "{x}",
            format: "tiles",
            token: ""
        },

        initialize: function (url, options) {
            var param = [];
            for (var att in options) {
                if (att == "context" || att == "success" || att == "error") continue;
                if (att in this.params)
                    this.params[att] = options[att];
            }

            for (var att in this.params) {
                if (att == "context" || att == "success" || att == "error") continue;
                param.push(att + "=" + this.params[att]);
            }
            //http://ditu.zj.cn/services/wmts/zjemap_bou?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=zjemap_bou&STYLE=default&TILEMATRIXSET=default028mm&TILEMATRIX=" + level + "&TILEROW=" + row + "&TILECOL=" + col + "&FORMAT=image/jpgpng
            // set the urls
            this.url = url;
            this.tileUrl = url + "?" + param.join("&");
            this._service = new L.esri.Services.MapService(this.url, options);
            this._service.on('authenticationrequired requeststart requestend requesterror requestsuccess', this._propagateEvent, this);

            // init layer by calling TileLayers initialize method
            L.TileLayer.prototype.initialize.call(this, this.tileUrl, options);
        }
    });

    L.ogc.Layers.tiledMapLayer = function (url, options) {
        return new L.ogc.Layers.OGCWMTSLayer(url, options);
    };

    L.ogc.tiledMapLayer = function (url, options) {
        return new L.ogc.Layers.OGCWMTSLayer(url, options);
    };

    L.ogc.TiledMapLayer = L.ogc.Layers.tiledMapLayer;


    L.ogc.Layers.ZJWMTSLayer = L.TileLayer.extend({
        params: {
            service: "WMTS",
            request: "GetTile",
            version: "1.0.0",
            layer: "img",
            style: "default",
            tilematrixset: "c",
            tilematrix: "{z}",
            tilerow: "{y}",
            tilecol: "{x}",
            format: "tiles",
            token: ""
        },

        initialize: function (url, options) {
            var param = [];
            for (var att in options) {
                if (att == "context" || att == "success" || att == "error") continue;
                if (att in this.params)
                    this.params[att] = options[att];
            }

            for (var att in this.params) {
                if (att == "context" || att == "success" || att == "error") continue;
                param.push(att + "=" + this.params[att]);
            }
            //http://ditu.zj.cn:8088/zjditu/rest/services/UNTMZJEMAP/MapServer/WMTS/tile/1.0.0/ZJSZF_IMGZT/default/nativeTileMatrixSet/12/677/3414
            // set the urls
            this.url = url;
            //this.tileUrl = url + "?" + param.join("&");
            this.tileUrl = url + "/WMTS/tile/1.0.0/ZJSZF_IMGZT/default/nativeTileMatrixSet/{z}/{y}/{x}"
            this._service = new L.esri.Services.MapService(this.url, options);
            this._service.on('authenticationrequired requeststart requestend requesterror requestsuccess', this._propagateEvent, this);

            // init layer by calling TileLayers initialize method
            L.TileLayer.prototype.initialize.call(this, this.tileUrl, options);
        }
    });

    L.ogc.Layers.zjTiledMapLayer = function (url, options) {
        return new L.ogc.Layers.ZJWMTSLayer(url, options);
    };

    L.ogc.zjTiledMapLayer = function (url, options) {
        return new L.ogc.Layers.ZJWMTSLayer(url, options);
    };
}));