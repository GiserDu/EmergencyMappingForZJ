
function Domready(readyFn) {
    if (document.addEventListener) {
        document.addEventListener("DOMContentLoaded", function() {
            readyFn && readyFn()
        }, false)
    } else {
        var bReady = false;
        document.attachEvent("onreadystatechange", function() {
            if (bReady) {
                return
            }
            if (document.readyState == "complete" || document.readyState == "interactive") {
                bReady = true;
                readyFn && readyFn()
            }
        });
        setTimeout(checkDoScroll, 1)
    }
    function checkDoScroll() {
        try {
            document.documentElement.doScroll("left");
            if (bReady) {
                return
            }
            bReady = true;
            readyFn && readyFn()
        } catch (e) {
            setTimeout(checkDoScroll, 1)
        }
    }
};
Domready(function(){
    // 执行代码
    var arcgisJsPath=webUrl+"js/lib/arcgis_js_api/library/3.24/3.24/init.js";
    var interJsPath=webUrl+"js/whu_interactvieMapping_initial.js";
    var plotJsPath=webUrl+"js/whu_plotInteractive.js";
    var esriCssPath=webUrl+"js/lib/arcgis_js_api/library/3.24/3.24/esri/css/esri.css";
    var claroCssPath=webUrl+"js/lib/arcgis_js_api/library/3.24/3.24/dijit/themes/claro/claro.css";
    addCssByLink(esriCssPath);
    addCssByLink(claroCssPath);
    loadScript(arcgisJsPath,function(){
        loadScript(interJsPath,function () {
            loadScript(plotJsPath,function () {
                console.log("动态加载完成")
            })
        })
    });
});
function addCssByLink(url){
    var doc=document;
    var link=doc.createElement("link");
    link.setAttribute("rel", "stylesheet");
    link.setAttribute("type", "text/css");
    link.setAttribute("href", url);

    var heads = doc.getElementsByTagName("head");
    if(heads.length)
        heads[0].appendChild(link);
    else
        doc.documentElement.appendChild(link);
}

function loadScript(url, callback){
    var script = document.createElement("script");
    script.type = "text/javascript";
    if(script.readyState){ // IE
        script.onreadystatechange = function(){
            if(script.readyState == "loaded" || script.readyState == "complete"){
                script.onreadystatechange = null;
                callback();
            }
        };
    }else{ // FF, Chrome, Opera, ...
        script.onload = function(){
            callback();
        };
    }
    script.src = url;
    document.getElementsByTagName("head")[0].appendChild(script);
}