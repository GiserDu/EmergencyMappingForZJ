$("#printbt").click(function (e) {
	print();
});

$("#map-save").click(function (e) {
	if(!!document.querySelector('#iMLegendDiv').innerText){
		getFeatureLegend();
	}
});

$("#layout").on(
	{
		change : function() {
			var layout = $("#layout option:selected").attr("layout");
			$("#layoutPreview").attr("src",layout);
		}
	}
);
var canvas2;
function getFeatureLegend() {
	canvas2 = document.createElement("canvas");
	var _canvas = document.querySelector('#iMLegendDiv');
	var w = parseInt(window.getComputedStyle(_canvas).width);
	var h = parseInt(window.getComputedStyle(_canvas).height);
	//将canvas画布放大若干倍，然后盛放在较小的容器内，就显得不模糊了
	canvas2.width = w * 2;
	canvas2.height = h * 2;
	canvas2.style.width = w + "px";
	canvas2.style.height = h + "px";
	//可以按照自己的需求，对context的参数修改,translate指的是偏移量
	//  var context = canvas.getContext("2d");
	//  context.translate(0,0);
	var context = canvas2.getContext("2d");
	context.scale(2, 2);

	// 隐藏标绘图例
	document.querySelector('#iMLegendDiv_polygonPlotLayer').style.display = "none";
	document.querySelector('#iMLegendDiv_linePlotLayer').style.display = "none";
	document.querySelector('#iMLegendDiv_pointPlotLayer').style.display = "none";

	//以下是对svg的处理
	var svgElem = $("#iMLegendDiv").find('svg image');//divReport为需要截取成图片的dom的id
	svgElem.each(function (index, node){
		console.log(node.href.baseVal);
		getImage(node.href.baseVal).catch((error)=> {
			console.log('oh no', error);
	}).then((img)=>{
			var base64 = getBase64Image(img);
		node.href.baseVal = base64;
	});
	});

	/**
	 * 异步获取图片
	 * @param  {[type]} src [description]
	 * @return {[type]}     [description]
	 */
	function getImage( src ){
		return new Promise( ( resolve, reject ) => {
				var img = new Image();
		img.setAttribute("crossOrigin",'anonymous');
		img.src = src;
		img.onload = function () {
			resolve( this );
		};
		img.onerror = function(err) {
			// var timeStamp = +new Date();
			// getImage(src+'?'+timeStamp);
		};
	})
	}

	//将图片转为 base64
	function getBase64Image(img) {
		// img.crossOrigin = 'Anonymous';
		var canvas = document.createElement("canvas");
		canvas.width = img.width;
		canvas.height = img.height;
		var ctx = canvas.getContext("2d");
		ctx.drawImage(img, 0, 0, img.width, img.height);
		// var ext = img.src.substring(img.src.lastIndexOf(".")+1).toLowerCase();
		var dataURL = canvas.toDataURL("image/png");
		return dataURL;
	}
}

function print(){
	require(["esri/map", "esri/tasks/PrintTask", "esri/tasks/PrintParameters","esri/tasks/PrintTemplate"],
		function(Map, PrintTask, PrintParameters,PrintTemplate) {
			// 打印时隐藏标绘图例
			["polygonPlotLayer","linePlotLayer","pointPlotLayer"].forEach(function (item) {
			    map.getLayer(item).hide();
			});
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
		// 判断是否有统计图层
		var vertifyNodesObj;
		if(layerNodesObj_Template) vertifyNodesObj = layerNodesObj_Template;
		if(layerNodesObj) vertifyNodesObj=layerNodesObj;
		var isSLayerExit = false;
		var statisticLayers = vertifyNodesObj.getNodes()[3].children;
		if(statisticLayers.length!=0){
			for(var index in statisticLayers){
				if(statisticLayers[index]){
					isSLayerExit = true;
					break;
				}
			}
		}

		var statisticLegendFlag;
		var imgURL = renderer.url;//本机测试用
		console.log(renderer.url);

		// 要素图例存在的情况
		if(!!document.querySelector('#iMLegendDiv').innerText){
			html2canvas(document.querySelector('#iMLegendDiv'), {
				canvas: canvas2
			}).then(function (canvas) {
				var legendsrc = canvas.toDataURL("image/png");
				// console.log(legendsrc);
				// 显示标绘图例
				// document.querySelector('#iMLegendDiv_polygonPlotLayer').style.display = "block";
				// document.querySelector('#iMLegendDiv_linePlotLayer').style.display = "block";
				// document.querySelector('#iMLegendDiv_pointPlotLayer').style.display = "block";
				var layout = document.getElementById("layout").value;
				var layoutpaper = document.getElementById("layoutpaper").value;
				var layoutID = layout + "_" + layoutpaper;
				var dpi = document.getElementById("dpi").value;
				// 如果没有统计图层
				if(statisticLayers.length==0 || isSLayerExit === false){
					// [仅存在要素图层]
					$.ajax({
						url: "./servlet/printMapServlet",
						type: 'POST',
						dataType: 'text',
						// contentType : 'application/json;charset=utf-8',
						data:{img:imgURL,flag:"none",layoutID:layoutID,dpi:dpi,legendsrc:legendsrc},
						success: function (data) {
							if(data!=null){
								window.open(data);
							}
							else {
								swal({
									title: "操作失败",
									text: "地图输出失败",
									type: "warning",
									showCancelButton: false,
									confirmButtonText: "确定",
									closeOnConfirm: false,
									closeOnCancel: false
								});
							}
						},
						error: function (xhr, status, errMsg) {
							console.log(errMsg);
						}
					});

				}else{
					// [要素图层 + 统计图层]
					if(indi.length==0 &&field_cn ==0){
						statisticLegendFlag = 'none';
					}
					else if(indi.length!=0 &&field_cn ==0){
						statisticLegendFlag = 'chart';
					}
					else if(indi.length==0 &&field_cn !=0){
						statisticLegendFlag = 'class';
					}
					else {
						statisticLegendFlag = 'both';
					}
					$.ajax({
						url: "./servlet/printMapServlet",
						type: 'POST',
						dataType: 'text',
						// contentType : 'application/json;charset=utf-8',
						data:{img:imgURL,flag:statisticLegendFlag,layoutID:layoutID,dpi:dpi,legendsrc:legendsrc},
						success: function (data) {
							if(data!=null){
								window.open(data);
							}
							else {
								swal({
									title: "操作失败",
									text: "地图输出失败",
									type: "warning",
									showCancelButton: false,
									confirmButtonText: "确定",
									closeOnConfirm: false,
									closeOnCancel: false
								});
							}
						},
						error: function (xhr, status, errMsg) {
							console.log(errMsg);
						}
					});
				}
			});
		}
		// 要素图例不存在的情况
		else {
			// 如果没有统计图层
			if(statisticLayers.length==0 || isSLayerExit === false){
				// [各图层都没有]
				window.open(renderer.url);
			}else{
				// [仅存在统计图层]
				//var imgURL = renderer.url.replace(/223.75.52.36:26080/, "localhost:6080");//实际部署用
				if(indi.length==0 &&field_cn ==0){
					statisticLegendFlag = 'none';
				}
				else if(indi.length!=0 &&field_cn ==0){
					statisticLegendFlag = 'chart';
				}
				else if(indi.length==0 &&field_cn !=0){
					statisticLegendFlag = 'class';
				}
				else {
					statisticLegendFlag = 'both';
				}

				var layout = document.getElementById("layout").value;
				var layoutpaper = document.getElementById("layoutpaper").value;
				var layoutID = layout + "_" + layoutpaper;
				var dpi=document.getElementById("dpi").value;
				var print_url = "./servlet/printMapServlet?img=" + imgURL + "&flag=" + statisticLegendFlag +
					"&layoutID=" + layoutID + "&dpi=" + dpi + "&legendsrc=none";

				if(statisticLegendFlag!="none"){
					$.ajax({
						url: print_url,
						type: 'POST',
						dataType: 'text',
						cache:false,
						scriptCharset: 'utf-8',
						success: function (data) {
							if(data!=null){
								window.open(data);
							}
							else {
								swal({
									title: "操作失败",
									text: "地图输出失败",
									type: "warning",
									showCancelButton: false,
									confirmButtonText: "确定",
									closeOnConfirm: false,
									closeOnCancel: false
								});
							}
						},
						error: function (xhr, status, errMsg) {
							console.log(errMsg);
						}
					});
				}
				else {
					window.open(renderer.url);
				}
			}
		}
		$("#loading").css("display","none");
	}
}

function printError(e) {
	console.log(e.error);
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