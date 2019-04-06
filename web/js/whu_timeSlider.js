//创建时间轴
var roffsettest = 0;
var tap = false;
var displayNow;
function createTimeSlider(graphicByDate,dateIndex) {
    require([
        "esri/dijit/TimeSlider",
        "esri/TimeExtent",
        "esri/layers/TimeInfo",
        "dojo/dom"
    ], function(
        TimeSlider,
        TimeExtent,
        TimeInfo,
        dom
    ) {
        $("#timeSliderInfo").css("display", "block");
        if($("#timeSliderInfo").children().length>1){//先销毁，再创建
            dijit.byId("timeSliderDiv").destroy();
            $("#timeSliderDiv").remove();
            var newDiv = document.createElement("div");
            newDiv.setAttribute('id','timeSliderDiv');
            $("#timeSliderDiv").css({
                'width': '500px',
                'height': '50px'
            })
            $("#timeSliderInfo").append(newDiv)
        }else{//创建
            var newDiv = document.createElement("div");
            newDiv.setAttribute('id','timeSliderDiv');
            var newDiv = document.createElement("div");
            newDiv.setAttribute('id','timeSliderDiv');
            $("#timeSliderDiv").css({
                'width': '500px',
                'height': '50px'
            })
            $("#timeSliderInfo").append(newDiv)
        }
        var timeSlider = new TimeSlider({ style: "width: 100%;"}, dom.byId("timeSliderDiv"));
        timeSlider.setThumbCount(1);
        var timeExtent = new TimeExtent();
        dateIndex = ForwardRankingDate(dateIndex);
        timeExtent.endTime = new Date(dateIndex[dateIndex.length-1]);
        timeExtent.startTime = new Date(dateIndex[0]);
        timeSlider.createTimeStopsByTimeInterval(timeExtent,1, TimeInfo.UNIT_DAYS);
        timeSlider.setThumbIndexes([0]);
        timeSlider.on("time-extent-change", function(e){
            displayMapByTime(e,graphicByDate)
        });
        timeSlider.startup();
        // map.setTimeSlider(timeSlider);
        timeSlider.setLoop(true);
        timeSlider.play();
    })

}

function displayMapByTime(e,graphicByDate){
    displayTimeInfo(e);
    if(displayNow){
        for(var i=0;i<displayNow.length;i++){
            displayNow[i].hide();
        }
    }
    var d = e.endTime.getDate();
    var m = e.endTime.getMonth()+1;
    var y = e.endTime.getFullYear();
    if(d<=9){
        d = "0"+d
    }
    if(m<=9){
        m = "0"+m
    }
    var date = y+'-'+m+'-'+d;
    displayNow = graphicByDate[date];
    console.log(displayNow)
    if(displayNow&&displayNow.length){
        for(var i=0;i<displayNow.length;i++){
            displayNow[i].show();
        }
    }
}
function displayTimeInfo(e) {
    var info = e.endTime.toUTCString();
    require([
        "dojo/dom"
    ], function(dom){
        dom.byId("timeInfo").innerHTML = info;
    })
}

function hasCreated(nameOf) {
    for (var i = 0; i < map.graphicsLayerIds.length; i++) {
        console.log(tjLayerName);
        if ((map.getLayer(map.graphicsLayerIds[i])).id == nameOf) {
            var layer = map.getLayer(map.graphicsLayerIds[i]);
            layer.clear();//清空所有graphics
            for (var i=0;i<classGraphics.length;i++){
                layer.add(classGraphics[i]);
            }
            if (layer.name != "classGLayer"){
                layer.name = "classGLayer";
                // var cLN = 0;
                // for (var i=0; i<map.graphicsLayerIds.length; i++){
                //     if (map.getLayer(map.graphicsLayerIds[i]).name == "chartGLayer"){
                //         var thisLayer = map.getLayer(map.graphicsLayerIds[i]);
                //         cLN++;
                //         var zoomLevel = map.getZoom();
                //         if (zoomLevel < 9)
                //             changeLayerOnZoom(thisLayer, "chartLayerData", "1", cLN);
                //         else
                //             changeLayerOnZoom(thisLayer, "chartLayerData", "2", cLN);
                //     }
                // }
                // if (cLN == 0)
                //     chartImg_url = "";
            }
            layer.content = allTjLayerContent;
            flag = 1;
            layer.setOpacity(0.95);
            treeNodeUrl = "";
            break;
        }
    }
}

//封装的日期排序方法
function ForwardRankingDate(data) {
    for (i = 0; i < data.length - 1; i++) {
         for (j = 0; j < data.length - 1 - i; j++) {
             console.log(Date.parse(data[j]));
             if (Date.parse(data[j]) > Date.parse(data[j+1])) {
                 var temp = data[j];
                 data[j] = data[j + 1];
                 data[j + 1] = temp;
             }
         }
     }
     return data;
 }