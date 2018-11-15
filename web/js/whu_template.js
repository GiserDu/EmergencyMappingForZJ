var winWidth = 0;
var winHeight = 0;

//创建div
$(function () {
    return;
});


$(document).ready(function () {
    findDimensions();
    suitWin();

    //浏览器尺寸发生变化是动态适应页面
    $(window).resize(function () {
        findDimensions();
        $(".flow-default img").width(0.24 * (winWidth - 240));
        $(".flow-default img").height(0.27 * winHeight);
        $(".flow-default li").width(0.24 * (winWidth - 240));
        $(".flow-default li").height(0.27 * winHeight);
    });
    //初始化当前模板,填充当前主题及第一个主题下的模板
    $.sidebarMenu($('#templateTypeList')); //侧边栏的交互
    createRule();
    createImg( $($($("#templateTypeList").children()[0]).children(0)).text());
})
//返回上一级判断函数
function backClick() {
//     if($(this).parents().find('iframe').selector=="iframe") {
//         window.location.href = "indexMini.html";
//     }
//     else {
//         window.location.href = "index.html";
//     }
    if(self!=top){
        window.location.href = "indexMini.html";
    }else {
         window.location.href = "index.html";
    }
}
function suitWin() {
    $(".flow-default img").width(0.24 * (winWidth - 240));
    $(".flow-default img").height(0.27 * winHeight);
    $(".flow-default li").width(0.24 * (winWidth - 240));
    $(".flow-default li").height(0.27 * winHeight);
};

//获取当前窗口尺寸
function findDimensions() {
    //获取窗口宽度
    if (window.innerWidth)
        winWidth = window.innerWidth;
    else if (document.body && document.body.clientWidth)
        winWidth = document.body.clientWidth;
    if (window.innerHeight)
        winHeight = window.innerHeight;
    else if (document.body && document.body.clientHeight)
        winHeight = document.body.clientHeight;
    //通过深入Document内部对body进行检测，获取窗口大小
    if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth) {
        winHeight = document.documentElement.clientHeight;
        winWidth = document.documentElement.clientWidth;
    }
};
//构造主题
function createRule() {
    $("#templateTypeList").empty();
    var disaster_status = localStorage.getItem("disaster_status");
    var disaster_type = localStorage.getItem("disaster_type");
    var url = "./servlet/GetTemplateContent?disasterStatus="+disaster_status+"&disasterType="+disaster_type+"&queryType=queryTheme";
    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        cache:false,
        contentType: "charset=utf-8",
        async:false,//设置为同步操作就可以给全局变量赋值成功
        scriptCharset: 'utf-8',
        success: function (data) {
            console.log(data);
            var str = "";
            for(var i=0;i<data.length;i++){
                str += "<li><div class='link' onclick=" + "createImg('" + data[i]["SIX_LZJTU_MAPGROUP"] + "')>";
                str += data[i]["SIX_LZJTU_MAPGROUP"] + "</div></li>";
            }
            //添加页面
            $("#templateTypeList").html(str);

        },
        error: function (xhr, status, errMsg) {
            alert('error');
            console.log(errMsg);
        }
    });

}

//构造主题下的模板	
function createImg(thistheme) {
    localStorage.setItem("template_theme", thistheme);
    $("#LAY_demo2").empty();
    var disaster_status = localStorage.getItem("disaster_status");
    var disaster_type = localStorage.getItem("disaster_type");
    var url = "./servlet/GetTemplateContent?disasterStatus="+disaster_status+"&disasterType="+disaster_type+"&queryType=queryMaps&disasterGroup="+thistheme;
    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        cache:false,
        contentType: "charset=utf-8",
        async:false,//设置为同步操作就可以给全局变量赋值成功
        scriptCharset: 'utf-8',
        success: function (data) {
            console.log(data);
            var maps=[];
            var imgSrcs=[]
            for(var i=0;i<data.length;i++){
                if(maps.indexOf(data[i]["SIX_LZJTU_MAP"])<0 ){+
                    maps.push(data[i]["SIX_LZJTU_MAP"]);
                    imgSrcs.push(data[i]["SIX_LZJTU_MAPLOC"]);
                }
            }
            var str = "";
            var defaultSrc ="'image/null.jpg;this.onerror=null'" ;
            for(var i=0;i<maps.length;i++){
                str += '<li>' + '<div class="hover ehover1">' + '<img src="';
                str += imgSrcs[i] + '"'+'alt="图片" onerror="javascript:this.src='+defaultSrc+'"'+'/>';
                str += '<div class="overlay">'
                for(var j=0;j<data.length;j++){
                    if(maps[i]== data[j]["SIX_LZJTU_MAP"]){
                        str += '<button class="info" data-toggle="modal" name="'+maps[i]+'" data-target="#modal1">' + data[j]["SIX_LZJTU_SCALE"] + '</button>';
                    }
                }
                str += '</div></div>' + maps[i]+ '</li>';
            }
            //添加页面
            $("#LAY_demo2").append($(str).hide().fadeIn(800));
            findDimensions();
            suitWin();
            //绑定事件
            $(".info").click(function () {
                var scale=$(this).html();
                var map=$(this).attr("name");
                var regionParam=0;
                if(scale=="国家"){
                    regionParam=0;
                }else if(scale.indexOf("省")!=-1){
                    regionParam=1;
                }
                localStorage.setItem("regionParam", regionParam);
                localStorage.setItem("template_scale", scale);
                localStorage.setItem("template_map", map);
                localStorage.setItem("templateHome", "1");


                // window.location.href='Mapping.html';
                if(self!=top){

                    var index=parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                    parent.sweetAlert1(map);

                    }
                else{
                    window.location.href='Mapping.html';
                }

            });
        },
        error: function (xhr, status, errMsg) {
            alert('error');
            console.log(errMsg);
        }
    });

}


function searchImg(){
    var keywords = document.getElementById("inputSearch").value;
    localStorage.setItem("template_name", keywords);
    if(keywords == '')
        alert("请输入关键字！");
    else{
        console.log(keywords);
        $("#LAY_demo2").empty();
        var disaster_status = localStorage.getItem("disaster_status");
        var disaster_type = localStorage.getItem("disaster_type");
        var url = "./servlet/GetTemplateContent?disasterStatus="+disaster_status+"&disasterType="+disaster_type+"&queryType=searchMaps&disasterName="+keywords;
        $.ajax({
            url: url,
            type: 'GET',
            dataType: 'json',
            cache:false,
            contentType: "charset=utf-8",
            async:false,//设置为同步操作就可以给全局变量赋值成功
            scriptCharset: 'utf-8',
            success: function (data) {
                console.log(data);
                var maps=[];
                var imgSrcs=[]
                for(var i=0;i<data.length;i++){
                    if(maps.indexOf(data[i]["SIX_LZJTU_MAP"])<0 ){+
                        maps.push(data[i]["SIX_LZJTU_MAP"]);
                        imgSrcs.push(data[i]["SIX_LZJTU_MAPLOC"]);
                    }
                }
                var str = "";
                var defaultSrc ="'image/null.jpg;this.onerror=null'" ;
                for(var i=0;i<maps.length;i++){
                    str += '<li>' + '<div class="hover ehover1">' + '<img src="';
                    str += imgSrcs[i] + '"'+'alt="图片" onerror="javascript:this.src='+defaultSrc+'"'+'/>';
                    str += '<div class="overlay">'
                    for(var j=0;j<data.length;j++){
                        if(maps[i]== data[j]["SIX_LZJTU_MAP"]){
                            str += '<button class="info" data-toggle="modal" name="'+maps[i]+'" data-target="#modal1">' + data[j]["SIX_LZJTU_SCALE"] + '</button>';
                        }
                    }
                    str += '</div></div>' + maps[i]+ '</li>';
                }
                //添加页面
                $("#LAY_demo2").append($(str).hide().fadeIn(800));
                findDimensions();
                suitWin();
                //绑定事件
                $(".info").click(function () {
                    var scale=$(this).html();
                    var map=$(this).attr("name");
                    var regionParam=0;
                    if(scale=="国家"){
                        regionParam=0;
                    }else if(scale.indexOf("省")!=-1){
                        regionParam=1;
                    }
                    localStorage.setItem("regionParam", regionParam);
                    localStorage.setItem("template_scale", scale);
                    localStorage.setItem("template_map", map);
                    window.location.href='Mapping.html';


                });
            },
            error: function (xhr, status, errMsg) {
                alert('error');
                console.log(errMsg);
            }
        });
    }
}

$.sidebarMenu = function (menu) {
    var animationSpeed = 300;

    $(menu).on('click', 'li div', function (e) {
        var $this = $(this);
        var checkElement = $this;

        if (checkElement.is('.link') && checkElement.is('.active')) {
            return;
            checkElement.slideUp(animationSpeed, function () {
                checkElement.removeClass('menu-open');
            });
            checkElement.parent("li").removeClass("active");
        } else if ((checkElement.is('.link')) && (!checkElement.is('.active'))) {
            var actived = $('.link');
            for (i = 0; i < actived.length; i++) {
                if ($(actived[i]).hasClass("active"))
                    $(actived[i]).removeClass("active");
            }
            checkElement.addClass("active");
        };
    })
}