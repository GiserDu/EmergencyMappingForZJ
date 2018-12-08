$("#printbt").click(function (e) {
	print();
});

$("#map-save").click(function (e) {
	setCenter();

});

$("#layout").on(
	{
		change : function() {
			var layout = $("#layout option:selected").attr("layout");
			$("#layoutPreview").attr("src",layout);
		}
	}
);

function setCenter() {
	require(["esri/map", "esri/geometry/Extent","esri/geometry/Point","esri/SpatialReference"],
		function(Map,Extent,Point) {
			if(regionParam == 1){
				// var center = {x:"112.356395",y:"31.01224452",zoom:"5"};
				//
				// var point = new Point(center.x,center.y);
				// map.centerAt(point);
			}
		});
}

function print(){
	require(["esri/map", "esri/tasks/PrintTask", "esri/tasks/PrintParameters","esri/tasks/PrintTemplate"],
		function(Map, PrintTask, PrintParameters,PrintTemplate) {
		// $('#loading').show();
        // esri.config.defaults.io.proxyUrl= "http://192.168.21.2:8090/Java/proxy.jsp";
        // esri.config.defaults.io.alwaysUseProxy= false;
		$("#loading").css("display","block");
		// var printTask = new PrintTask("http://223.75.52.36:26080/arcgis/rest/services/Utilities/PrintingTools/GPServer/Export%20Web%20Map%20Task");
		var printTask = new PrintTask("http://47.96.162.249:6080/arcgis/rest/services/Utilities/PrintingTools/GPServer/Export%20Web%20Map%20Task");
		var params = new PrintParameters();
		params.map = map;
		var template = new PrintTemplate();

		var width = $("#mapContainer").width();
		var height = $("#mapContainer").height();
		var mapTitle = document.getElementById("mapTitle").value;
		// var authorText = document.getElementById("authorText").value;
		var copyrightText = document.getElementById("copyrightText").value;
		var dpi=document.getElementById("dpi").value;
		var format=document.getElementById("format").value;
		var layout = document.getElementById("layout").value;
		var layoutpaper = document.getElementById("layoutpaper").value;
		var layoutID = layout + "_" + layoutpaper;
		var outScale;
		//动态设置比例尺大小
		if(regionParam == 1){
			if(layoutpaper=="A3"){
				if(layout.split("_")[0]=="Landscape"){
					outScale = 2500000;
				}
				else {
					outScale = 3500000;
				}
			}
			else {
				if(layout.split("_")[0]=="Landscape"){
					outScale = 3500000;
				}
				else {
					outScale = 4700000;
				}
			}
		}
		else {
			outScale = 1000000;
		}

		template.exportOptions = {
			width: width,
			height: height,
			dpi: dpi
		};
		template.layoutOptions = {
			titleText:mapTitle,
			// authorText:authorText,
			copyrightText:copyrightText
		};
		template.format = format;
		template.layout = layoutID;
		// template.layout ='A4 Landscape';
		// template.preserveScale = true;
		// template.outScale = outScale;

		params.template = template;
		printTask.execute(params, printResult,printError);
	});
}
function printResult(renderer) {
	if (renderer != null) {
		window.open(renderer.url);
		console.log(renderer.url);
        // var imgURL = renderer.url;//本机测试用
        // //var imgURL = renderer.url.replace(/223.75.52.36:26080/, "localhost:6080");//实际部署用
        //
        // var printLegendFlag;
        // if(indi.length==0 &&field_cn ==0){
			// printLegendFlag = 'none';
        // }
        // else if(indi.length!=0 &&field_cn ==0){
			// printLegendFlag = 'chart';
        // }
        // else if(indi.length==0 &&field_cn !=0){
			// printLegendFlag = 'class';
        // }
        // else {
			// printLegendFlag = 'both';
        // }
        //
        // var layout = document.getElementById("layout").value;
        // var layoutpaper = document.getElementById("layoutpaper").value;
        // var layoutID = layout + "_" + layoutpaper;
        // var dpi=document.getElementById("dpi").value;
        // var print_url = "./servlet/printMapServlet?img=" + imgURL + "&flag=" + printLegendFlag +
			// "&layoutID=" + layoutID + "&dpi=" + dpi;
        //
        // if(printLegendFlag!="none"){
        //     $.ajax({
        //         url: print_url,
        //         type: 'POST',
        //         dataType: 'text',
        //         cache:false,
        //         scriptCharset: 'utf-8',
        //         success: function (data) {
        //             if(data!=null){
        //                 window.open(data);
        //             }
        //             else {
        //                 swal({
        //                     title: "操作失败",
        //                     text: "地图输出失败",
        //                     type: "warning",
        //                     showCancelButton: false,
        //                     confirmButtonText: "确定",
        //                     closeOnConfirm: false,
        //                     closeOnCancel: false
        //                 });
        //             }
        //         },
        //         error: function (xhr, status, errMsg) {
        //             console.log(errMsg);
        //         }
        //     });
        // }
        // else {
        //     window.open(renderer.url);
        // }
		$("#loading").css("display","none");
	}
}

function printError() {
	$("#loading").css("display","none");
	swal({
		title: "输出失败",
		text: "请确认地图服务器正常启动",
		type: "warning",
		showCancelButton: false,
		confirmButtonText: "确定",
		closeOnConfirm: false,
		closeOnCancel: false
	});
}