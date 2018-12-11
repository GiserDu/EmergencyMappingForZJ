define("esri/layers/WebTiledLayer4326", "dojo/_base/declare dojo/_base/lang dojo/_base/array dojo/_base/url dojo/sniff dojo/string ../config ../kernel ../request ../urlUtils ../SpatialReference ../geometry/Extent ./TiledMapServiceLayer ./TileInfo".split(" "), function (f, h, k, u, p, q, v, w, x, l, m, r, y, z) {
    function A(c, a) {
        var b = [],
            d, e;
        if (c && a && (a.customLayerParameters || a.customParameters)) {
                d = h.clone(a.customParameters || {});
                h.mixin(d, a.customLayerParameters || {});
                e = l.urlToObject(c);
                for (var g in e.query) d.hasOwnProperty(g) || b.push(g + "\x3d" + e.query[g]);
                c = e.path + (b.length ? "?" + b.join("\x26") : "")
            }
        return c
    }
	debugger
    f = f(y, {
        declaredClass: "esri.layers.WebTiledLayer",
        constructor: function (c, a) {
            a || (a = {});
            this.urlTemplate = c = A(c, a.wmtsInfo);
            var b = new r(-180, -90, 180, 90, new m({
                wkid: 4326
            })),
                d = new r(-180, -90, 180, 90, new m({
                    wkid: 4326
                }));
            this.initialExtent = a.initialExtent || b;
            this.fullExtent = a.fullExtent || d;
            this.tileInfo = a.tileInfo ? a.tileInfo : new z({
                    dpi: 90.71428571427429,
                    rows: 256,
                    cols: 256,
                    origin: {
                        x: -180,
                        y: 90
                    },
                    spatialReference: {
                        wkid: 4326
                    },
                    lods : [
						{level : 2,levelValue: 2, resolution : 0.3515625, scale : 147748796.52937502},
						{level : 3,levelValue: 3, resolution : 0.17578125, scale : 73874398.264687508},
						{level : 4,levelValue: 4, resolution : 0.087890625, scale : 36937199.132343754},
						{level : 5,levelValue: 5, resolution : 0.0439453125, scale : 18468599.566171877},
						{level : 6,levelValue: 6, resolution : 0.02197265625, scale : 9234299.7830859385},
						{level : 7,levelValue: 7, resolution : 0.010986328125, scale : 4617149.8915429693},
						{level : 8,levelValue: 8, resolution : 0.0054931640625, scale : 2308574.9457714846},
						{level : 9,levelValue: 9, resolution : 0.00274658203125, scale : 1154287.4728857423},
						{level : 10,levelValue: 10, resolution : 0.001373291015625, scale : 577143.73644287116},
						{level : 11,levelValue: 11, resolution : 0.0006866455078125, scale : 288571.86822143558},
						{level : 12,levelValue: 12, resolution : 0.00034332275390625, scale : 144285.93411071779},
						{level : 13,levelValue: 13, resolution : 0.000171661376953125, scale : 72142.967055358895},
						{level : 14,levelValue: 14, resolution : 8.58306884765625e-005, scale : 36071.483527679447},
						{level : 15,levelValue: 15, resolution : 4.291534423828125e-005, scale : 18035.741763839724},
						{level : 16,levelValue: 16, resolution : 2.1457672119140625e-005, scale : 9017.8708819198619},
						{level : 17,levelValue: 17, resolution : 1.0728836059570313e-005, scale : 4508.9354409599309},
						{level : 18,levelValue: 18, resolution : 5.3644180297851563e-006, scale : 2254.4677204799655},
						{level : 19,levelValue: 19, resolution: 2.68220901489257815e-006, scale: 1127.23386023998275 },
						{level : 20,levelValue: 20, resolution: 1.341104507446289075e-006, scale: 563.616930119991375 }
					]
                });
            this.spatialReference = new m(this.tileInfo.spatialReference.toJson());
            this.copyright = a.copyright || "";
            var e = new u(c),
                b = e.scheme + "://" + e.authority + "/";
            this.urlPath = c.substring(b.length);
            this.tileServers = a.tileServers || []; - 1 === e.authority.indexOf("{subDomain}") && this.tileServers.push(b);
            if (a.subDomains && 0 < a.subDomains.length && 1 < e.authority.split(".").length) {
                    this.subDomains = a.subDomains;
                    var g;
                    k.forEach(a.subDomains, function (a) {
                        -1 < e.authority.indexOf("${subDomain}") ? g = e.scheme + "://" + q.substitute(e.authority, {
                            subDomain: a
                        }) + "/" : -1 < e.authority.indexOf("{subDomain}") && (g = e.scheme + "://" + e.authority.replace(/\{subDomain\}/gi, a) + "/");
                        this.tileServers.push(g)
                    }, this)
                }
            this.tileServers = k.map(this.tileServers, function (a) {
                    "/" !== a.charAt(a.length - 1) && (a += "/");
                    return a
                });
            this._levelToLevelValue = [];
            var f = 0;
            k.forEach(this.tileInfo.lods, function (a, b) {
                    this._levelToLevelValue[a.level] = a.levelValue || a.level;
                    0 === b && (f = this._levelToLevelValue[a.level])
                }, this);
            this._wmtsInfo = a.wmtsInfo;
            var n = h.hitch(this, function () {
                    this.loaded = !0;
                    this.onLoad(this)
                });
            if (p("chrome")) {
                    var b = this.getTileUrl(f, 0, 0),
                        d = v.defaults.io,
                        t = "with-credentials" === d.useCors ? l.canUseXhr(b, !0) : -1;
                        (d = -1 < t ? d.corsEnabledServers[t] : null) && d.withCredentials ? x({
                            url: b,
                            handleAs: "arraybuffer"
                        }).addBoth(function () {
                            n()
                        }) : n()
                } else n()
        },
        getTileUrl: function (c, a, b) {
            c = this._levelToLevelValue[c];
            var d = this.tileServers[a % this.tileServers.length] + q.substitute(this.urlPath, {
                level: c,
                col: b,
                row: a
            }),
                d = d.replace(/\{level\}/gi, c).replace(/\{row\}/gi, a).replace(/\{col\}/gi, b),
                d = this._appendCustomLayerParameters(d),
                d = this.addTimestampToURL(d);
            return l.addProxy(d)
        },
        _appendCustomLayerParameters: function (c) {
            var a, b;
            if (this._wmtsInfo && (this._wmtsInfo.customLayerParameters || this._wmtsInfo.customParameters)) for (a in b = h.clone(this._wmtsInfo.customParameters || {}), h.mixin(b, this._wmtsInfo.customLayerParameters || {}), b) c += (-1 === c.indexOf("?") ? "?" : "\x26") + a + "\x3d" + encodeURIComponent(b[a]);
            return c
        }
    });
    p("extend-esri") && h.setObject("layers.WebTiledLayer", f, w);
    return f
});