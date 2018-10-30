function creatARpanel(mapScale,element){
    $("#administrativeRegion").html("");
    //创建省级区域表单
    function creatProList(d) {
        var tab = document.getElementById(d);
        var link = document.createElement('div');
        link.setAttribute('class','province');
        var proname="";
        var procode="";
        $.ajax({
            url:"./servlet/GetAdministrativeRegion",
            type:"post",
            async:false,
            data:{type:"pro"},
            success:function (data) {
                data=data.split("s-p-l");
                proname=data[0].split(",");
                procode=data[1].split(",");

            }
        });
        html=[
            '<select id="provinceSelect" class="selectpicker provinceSelect" data-live-search="true">'
        ];
        for(var name in proname){
            html.push( '<option value="'+procode[name]+'">'+proname[name]+'</option>')
        }
        html.push( '</select>');
        html=html.join('');
        link.innerHTML = html;
        tab.appendChild(link);
    }
//创建市级区域表单
    function creatCityList(d) {
        var tab = document.getElementById(d);
        var link = document.createElement('div');
        link.setAttribute('class','province');
        var proname="";
        var procode="";
        var cityname="";
        var citycode="";

        $.ajax({
            url:"./servlet/GetAdministrativeRegion",
            type:"post",
            async:false,
            data:{type:"pro"},
            success:function (data) {
                data=data.split("s-p-l");
                proname=data[0].split(",");
                procode=data[1].split(",");

            }
        });
        html=[
            '<select id="provinceSelect" class="selectpicker provinceSelect"  data-live-search="true">'
        ];
        for(var name in proname){
            html.push( '<option value="'+procode[name]+'">'+proname[name]+'</option>')
        }
        html.push( '</select>');
        html=html.join('');
        link.innerHTML = html;
        tab.appendChild(link);
        $('#provinceSelect').selectpicker({
            'noneSelectedText': '请选择省份'
        });

        var selectedProcode=$('#provinceSelect').val();
        if (selectedProcode==""){
            return;
        }
        $.ajax({
            url:"./servlet/GetAdministrativeRegion",
            type:"post",
            async:false,
            data:{type:"city",proCode:selectedProcode},
            success:function (data) {
                data=data.split("s-p-l");
                cityname=data[0].split(",");
                citycode=data[1].split(",");

            }
        });

        var link = document.createElement('div');
        link.setAttribute('class','city');
        //这个地方要根据选择的省份查询该省份所包含的城市，然后将城市放进一个迭代器，循环添加进变量html
        var html=[
            '<select id="citySelect" class="selectpicker provinceSelect"  data-live-search="true">'
        ];
        for(var name in cityname){
            html.push( '<option value="'+citycode[name]+'">'+cityname[name]+'</option>')
        }
        html.push( '</select>');
        html=html.join('');
        link.innerHTML = html;
        tab.appendChild(link);
        $('#citySelect').selectpicker({
            'noneSelectedText': '请选择市'
        });

    }
//创建县级区域表单
    function creatCounList(d) {


        var tab = document.getElementById(d);
        var link = document.createElement('div');
        link.setAttribute('class','province');
        var proname="";
        var procode="";
        var cityname="";
        var citycode="";
        var coutname="";
        var coutcode="";
        $.ajax({
            url:"./servlet/GetAdministrativeRegion",
            type:"post",
            async:false,
            data:{type:"pro"},
            success:function (data) {
                data=data.split("s-p-l");
                proname=data[0].split(",");
                procode=data[1].split(",");

            }
        });
        html=[
            '<p> 省份：<select id="provinceSelect" class="selectpicker provinceSelect"  data-live-search="true">'
        ];
        for(var name in proname){
            html.push( '<option value="'+procode[name]+'">'+proname[name]+'</option>')
        }
        html.push( '</select></p>');
        html=html.join('');
        link.innerHTML = html;
        tab.appendChild(link);
        $('#provinceSelect').selectpicker({
            'noneSelectedText': '请选择省份'
        });


        var selectedProcode=$('#provinceSelect').val();
        if (selectedProcode==""){
            return;
        }
        $.ajax({
            url:"./servlet/GetAdministrativeRegion",
            type:"post",
            async:false,
            data:{type:"city",proCode:selectedProcode},
            success:function (data) {
                console.log(data);
                data=data.split("s-p-l");
                cityname=data[0].split(",");
                citycode=data[1].split(",");

            }
        });

        var link = document.createElement('div');
        link.setAttribute('class','city');
        //这个地方要根据选择的省份查询该省份所包含的城市，然后将城市放进一个迭代器，循环添加进变量html
        var html=[
            '<p> 城市：<select id="citySelect" class="selectpicker provinceSelect"  data-live-search="true">'
        ];
        for(var name in cityname){
            html.push( '<option value="'+citycode[name]+'">'+cityname[name]+'</option>')
        }
        html.push( '</select></p>');
        html=html.join('');
        link.innerHTML = html;
        tab.appendChild(link);
        $('#citySelect').selectpicker({
            'noneSelectedText': '请选择城市'
        });

        var selectedCitycode=$('#citySelect').val().trim();
        if (selectedCitycode==""){
            return;
        }
        var link = document.createElement('div');
        link.setAttribute('class','county');
        $.ajax({
            url:"./servlet/GetAdministrativeRegion",
            type:"post",
            async:false,
            data:{type:"cout",proCode:selectedProcode,cityCode:selectedCitycode},
            success:function (data) {

                data=data.split("s-p-l");
                coutname=data[0].split(",");
                coutcode=data[1].split(",");

            }
        });
        //这个地方要根据选择的城市查询该省份所包含的线，然后将县放进一个迭代器，循环添加进变量html
        var html=[
            '<p> 区县：<select id="coutSelect" class="selectpicker provinceSelect"  data-live-search="true">'
        ];
        for(var name in coutname){
            html.push( '<option value="'+coutcode[name]+'">'+coutname[name]+'</option>')
        }
        html.push( '</select></p>');
        html=html.join('');
        link.innerHTML = html;
        tab.appendChild(link);
        $('#coutSelect').selectpicker({
            'noneSelectedText': '请选择区县'
        });
    }
//获取选定的行政区边界
    //var html = "<ul id='accordion-plot'class='accordion'><li id='plot-point'><div class='link'><i class='fa fa-map'aria-hidden='true'></i>点符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(point)'>点</a></li><li><a href='#'>待完善1</a></li><li><a href='#'>待完善2</a></li></ul></li><li id='plot-polyline'><div class='link'><i class='fa fa-bandcamp'aria-hidden='true'></i>线符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(polyline)'>折线</a></li><li><a href='#'onclick='marking(freehand_polyline)'>自由线</a></li><li><a href='#'>待完善1</a></li><li><a href='#'>待完善2</a></li></ul></li><li id='plot-polygon'><div class='link'><i class='fa fa-pie-chart'aria-hidden='true'></i>面符号标绘<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(polygon)'>多边形</a></li><li><a href='#'onclick='marking(freehand_polygon)'>自由面</a></li><li><a href='#'onclick='marking(rectangle)'>矩形</a></li><li><a href='#'onclick='marking(circle)'>圆</a></li><li><a href='#'onclick='marking(ellipse)'>椭圆</a></li><li><a href='#'onclick='marking(arrow)'>箭头</a></li><li><a href='#'onclick='marking(triangle)'>三角形</a></li><li><a href='#'>待完善1</a></li></ul></li><li id='plot-tools'><div class='link'><i class='fa fa-paint-brush'aria-hidden='true'></i>工具<i class='fa fa-chevron-down'></i></div><ul class='submenu'><li><a href='#'onclick='marking(edit)'>编辑</a></li><li><a href='#' onclick='addMeasureInteraction()'>测量</a></li></ul></li></ul>";
    var html = [
    ].join('');
    var link = document.createElement('div');
    //link.setAttribute('class','settingsPanel');
    link.innerHTML = html;
    document.body.appendChild(link);
    var studyarea=document.getElementById(element);
    studyarea.appendChild(link)
    //template.mapScale="区域";
    switch (mapScale) {
        //统一mapScale的中文名字，不然不好switch
        case "国家":
            creatProList(element);
            $('#provinceSelect').change(function(e){
                    AROnChange(this,"proCode");
            }

            );
            break;
        case "省":
            creatCityList(element);
            $('#provinceSelect').change(function () {
                AROnChange(this,"cityCode");
                $("#citySelect").empty();
                var selectedProcode=$('#provinceSelect').val();
                if (selectedProcode==""){
                    return;
                }
                var cityname="";
                var citycode="";
                $.ajax({
                    url:"./servlet/GetAdministrativeRegion",
                    type:"post",
                    async:false,
                    data:{type:"city",proCode:selectedProcode},
                    success:function (data) {
                        data=data.split("s-p-l");
                        cityname=data[0].split(",");
                        citycode=data[1].split(",");

                    }
                });
                var html=[        ];
                for(var name in cityname){
                    html.push( '<option value="'+citycode[name]+'">'+cityname[name]+'</option>')
                }
                html=html.join('');
                $('#citySelect').append(html);
                $('#citySelect').selectpicker('render');
                $('#citySelect').selectpicker('refresh');
                $('#citySelect').selectpicker();

            });
            $('#citySelect').change(function(){
                AROnChange(this,"cityCode");
            });
            break;
        case "区域":
            creatCounList(element);
            $('#provinceSelect').change(function () {
                AROnChange(this,"cityCode");
                $("#citySelect").empty();
                var selectedProcode=$('#provinceSelect').val();
                if (selectedProcode==""){
                    return;
                }
                var cityname="";
                var citycode="";
                $.ajax({
                    url:"./servlet/GetAdministrativeRegion",
                    type:"post",
                    async:false,
                    data:{type:"city",proCode:selectedProcode},
                    success:function (data) {
                        data=data.split("s-p-l");
                        cityname=data[0].split(",");
                        citycode=data[1].split(",");

                    }
                });
                var html=[        ];
                for(var name in cityname){
                    html.push( '<option value="'+citycode[name]+'">'+cityname[name]+'</option>')
                }
                html=html.join('');
                $('#citySelect').append(html);
                $('#citySelect').selectpicker('render');
                $('#citySelect').selectpicker('refresh');
                $('#citySelect').selectpicker();

            });
            $('#citySelect').change(function () {
                $("#coutSelect").empty();
                var selectedProcode=$('#provinceSelect').val();
                if (selectedProcode==""){
                    return;
                }
                var selectedCitycode=$('#citySelect').val().trim();
                if (selectedCitycode==""){
                    return;
                }
                var coutname="";
                var coutcode="";
                $.ajax({
                    url:"./servlet/GetAdministrativeRegion",
                    type:"post",
                    async:false,
                    data:{type:"cout",proCode:selectedProcode,cityCode:selectedCitycode},
                    success:function (data) {

                        data=data.split("s-p-l");
                        coutname=data[0].split(",");
                        coutcode=data[1].split(",");

                    }
                });
                //这个地方要根据选择的城市查询该省份所包含的线，然后将县放进一个迭代器，循环添加进变量html
                var html=[
                    '<select id="coutSelect" class="selectpicker provinceSelect" multiple data-live-search="true">'
                ];
                for(var name in coutname){
                    html.push( '<option value="'+coutcode[name]+'">'+coutname[name]+'</option>')
                }
                html.push( '</select>');
                html=html.join('');
                $('#coutSelect').append(html);
                $('#coutSelect').selectpicker('render');
                $('#coutSelect').selectpicker('refresh');
                $('#coutSelect').selectpicker();

            });
            $('#coutSelect').change(function(){
                AROnChange(this,"coutCode");
            });
            break;
        default:
            alert("mapScale名称有误")
            return;
    }

//绑定定位事件
    function AROnChange(e,t){
        var regionCode=$(e).val();
        var selectedRegion=t;
        $.ajax({
            url:"./servlet/GetAdministrativeRegion",
            type:"post",
            async:false,
            //dateType:"json",
            data:{type:"boundary",selectedRegion:selectedRegion+"s-p-l"+regionCode},
            success:function (data) {
                data=eval("("+data+")");
                require(["esri/geometry/Polygon","esri/Color","esri/graphic","esri/layers/GraphicsLayer","esri/symbols/SimpleFillSymbol","esri/symbols/SimpleLineSymbol","esri/layers/FeatureLayer"],function (Polygon,Color,Graphic,GraphicsLayer,SimpleFillSymbol,SimpleLineSymbol,FeatureLayer) {
                    //制图区图层
                    studyAreaLayer.clear();
                    var geometry=new Polygon({"rings":data[0],"spatialReference":{"wkid":4326}});
                    for(var i=1;i<data.length;i++){
                        geometry.addRing(data[i][0]);
                    }
                    var symbol = new SimpleFillSymbol();
                    var graphic = new Graphic();
                    graphic.setGeometry(geometry);
                    symbol.color.a=0.01;
                    graphic.setSymbol(symbol);
                    studyAreaLayer.add(graphic);
                    //地图控件
                    map.setExtent(geometry.getExtent());
                });
            }
        });}

}


