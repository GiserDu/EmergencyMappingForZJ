/**
 * Created by Administrator on 2017/10/25.
 */
//测试数据
var allMapList=[
    {"map_id":1,"map_name":"利川市地理国情普查图","map_tag":"地质灾害工作部署图","map_info":"该图于2016年编制完成，属于我国第一次地理国情普查成果之一","submit_time":"2018-12-24 20:52:44","edit_time":"2018-12-25 20:52:44","picture":""},
    {"map_id":2,"map_name":"东宝区单位面积河流分析图","map_tag":"地质灾害工作进展图","map_info":"该图于2016年编制完成，属于我国第一次地理国情普查成果之一","submit_time":"2018-12-21 20:52:44","edit_time":"2018-12-23 20:52:44","picture":""},
    {"map_id":3,"map_name":"东宝区植被覆盖面积统计图","map_tag":"地质灾害工作部署图","map_info":"该图于2016年编制完成，属于我国第一次地理国情普查成果之一","submit_time":"2018-12-13 20:52:44","edit_time":"2018-12-22 20:52:44","picture":""},
    {"map_id":4,"map_name":"交通便利度指数图","map_tag":"隐患点分布图","map_info":"该图于2016年编制完成，属于我国第一次地理国情普查成果之一","submit_time":"2018-12-21 20:52:44","edit_time":"2018-12-24 20:52:44","picture":""},
    {"map_id":5,"map_name":"交通发育指数","map_tag":"地质灾害工作部署图","map_info":"该图于2016年编制完成，属于我国第一次地理国情普查成果之一","submit_time":"2018-12-10 20:52:44","edit_time":"2018-12-20 20:52:44","picture":""},
    {"map_id":6,"map_name":"交通优势度指数图","map_tag":"隐患点分布图","map_info":"该图于2016年编制完成，属于我国第一次地理国情普查成果之一","submit_time":"2018-12-15 20:52:44","edit_time":"2018-12-19 20:52:44","picture":""},
    {"map_id":7,"map_name": "中学教育资源可达性分析图","map_tag":"避让搬迁分布图","map_info":"该图于2016年编制完成，属于我国第一次地理国情普查成果之一","submit_time":"2018-12-12 20:52:44","edit_time":"2018-12-18 20:52:44","picture": ""},
    {"map_id":8,"map_name": "小学教育资源可达性分析图","map_tag":"避让搬迁分布图","map_info":"该图于2016年编制完成，属于我国第一次地理国情普查成果之一","submit_time":"2018-12-12 20:52:44","edit_time":"2018-12-18 20:52:44","picture": ""}
]
// var allMapList = [];//全部专题图(有后台时使用)
var tempSelfList = [];//模板页显示专题图实际列表
var searchResultList = [];//查询结果
var filterResultList = [];//筛选结果
var filterTemp = [];//筛选临时存储

var thematicMapID=0;


initMapList();

$(function () { $("[data-toggle='tooltip']").tooltip(); });

// 按照图名模糊查询
function fuzzyQuery(key) {
    var searchResult = [];
    allMapList.forEach(item => {
        if(item.map_name.search(key)!== -1) {
            searchResult.push(item);
        }
    });
    searchResultList = searchResult;
    searchResult = [];
    showTemplate(filterResultList,searchResultList);
}


//最终显示的专题图，搜索与筛选结果求交
function showTemplate (filter, search) {
    var filter = new Set(filter);
    var search = new Set(search);
    tempSelfList = Array.from(new Set([...filter].filter(item => search.has(item))));
}

//类别筛选
function categoryFilter (key) {
    if (key === '全部专题图类别') {
        filterTemp = allMapList;
    }else{
        allMapList.forEach(item => {
            if(item.map_tag == key) {
                filterTemp.push(item);
            }
        });
    }
    filterResultList = filterTemp;
    filterTemp = [];
    showTemplate(filterResultList,searchResultList);
}

// 排序策略
function sortFilter (key) {
    switch (key) {
        case "图名排列":
            tempSelfList.sort(compare("map_name"));
            break;
        case "时间顺序":
            tempSelfList.sort(compare("submit_time"));
            break;
        case "时间倒序":
            tempSelfList.sort(compare("submit_time")).reverse();
            break;
    }
}

function compare (propertyName) {
    return function(object1, object2) {
        var value1 = object1[propertyName];
        var value2 = object2[propertyName];
        if (value2 < value1) {
            return 1;
        } else if (value2 > value1) {
            return -1;
        } else {
            return 0;
        }
    }
}

// 更新地图视图
function refreshMaps() {
    $(".gallery-row").html('');
    // $(".icon.info").tooltip('destroy');
    for (var i in tempSelfList){
        if(tempSelfList[i].picture ===""){
            tempSelfList[i].picture = "image/default-map.jpg";
        }
        $(".gallery-row").append(
            '<div class="col-md-2 img-container"><div class="img-box">'+
            '<span class="icon info" data-toggle="tooltip" data-placement="top" title="专题图详情"><a data-toggle="modal" data-target="#myEditModal"><i class="fa fa-picture-o"></i></a></span>'+
            '<span class="icon edit" data-toggle="tooltip" data-placement="top" title="编辑专题图"><i class="fa fa-edit"></i></span>'+
            '<span class="icon trash" data-toggle="tooltip" data-placement="top" title="删除专题图"><i class="fa fa-trash"></i></span>'+
            '<span class="description">'+tempSelfList[i].map_info+'</span>'+
            '<img class="img-responsive" src="'+tempSelfList[i].picture+'"  mapID="'+tempSelfList[i].map_id+'" mapTag="'+tempSelfList[i].map_tag+'" submitTime="'+tempSelfList[i].submit_time+'" editTime="'+tempSelfList[i].edit_time+'"> <p class="title">'+tempSelfList[i].map_name+'</p></div>');
        $(".icon.info").tooltip();
        $(".icon.edit").tooltip();
        $(".icon.trash").tooltip();
    }
}

// 按地图标签筛选
$(".selectpicker.show-tick.map-tag").change(function () {
    categoryFilter($(this).val());
    refreshMaps();
});

// 选择排序方式
$(".selectpicker.show-tick.sort-type").change(function () {
    sortFilter($(this).val());
    refreshMaps();
});

// 地图图名搜索按钮点击
$(".search-map-btn").click(function () {
    if($(".form-control.search-key").val()==="") return;
    fuzzyQuery($(".form-control.search-key").val());
    refreshMaps();
});

// 图名搜索输入回车
$('.form-control.search-key').bind('keypress',function(event){
    if(event.keyCode == "13")
    {
        if($(".form-control.search-key").val()==="") return;
        fuzzyQuery($(".form-control.search-key").val());
        refreshMaps();
    }
});

// 在输入框为空时，恢复原始视图
$(".form-control.search-key").bind("input propertychange",function(event){
    if($(this).val()===""){
        searchResultList = allMapList;
        showTemplate(filterResultList,searchResultList);
        refreshMaps();
    }
});

// 无后台版(测试用)
function initMapList(){
    tempSelfList = allMapList;
    searchResultList = allMapList;
    filterResultList = allMapList;
    // 动态生成地图标签下拉列表
    var mapTags = new Set();
    mapTags.add("全部专题图类别");
    for(let item of allMapList){
        mapTags.add(item.map_tag);
    }
    $('.selectpicker.map-tag').html('');
    for(let tag of mapTags.keys()){
        $('.selectpicker.map-tag').append('<option>'+tag+'</option>');
    }
    sortFilter("时间顺序");
    refreshMaps();
}

// （有后台版）先清空gallery-row，然后循环添加图片的div，最后添加添加图片的div
// function initMapList() {
//     var type = "initManage";
//     var init_url = "./servlet/GetThematicMapServlet?type=" + type;
//     $.ajax({
//         url: init_url,
//         type: 'POST',
//         dataType: 'json',
//         cache:false,
//         // async:false,//设置为同步操作就可以给全局变量赋值成功
//         scriptCharset: 'utf-8',
//         success: function (data) {
//             // console.log(data);
//             allMapList = data;
//             tempSelfList = allMapList;
//             searchResultList = allMapList;
//             filterResultList = allMapList;
//                 // 动态生成地图标签下拉列表
//                 var mapTags = new Set();
//                 mapTags.add("全部专题图类别");
//                 for(let item of allMapList){
//                     mapTags.add(item.map_tag);
//                 }
//                 $('.selectpicker.map-tag').html('');
//                 for(let tag of mapTags.keys()){
//                     $('.selectpicker.map-tag').append('<option>'+tag+'</option>');
//                 }
//             sortFilter("时间顺序");
//             refreshMaps();
//         },
//         error: function (xhr, status, errMsg) {
//             console.log(errMsg);
//             swal({
//                 title: "初始化失败",
//                 text: "信息初始化失败!",
//                 type: "warning",
//                 showCancelButton: false,
//                 confirmButtonText: "确定",
//                 closeOnConfirm: false,
//                 closeOnCancel: false
//             });
//
//         }
//     });
// }

$(document).on('mouseenter','.img-box',function(e){
    $(this).find(".icon").css("visibility","visible");
    $(this).find(".description").css("visibility","visible");
});

$(document).on('mouseleave','.img-box',function(e){
    $(this).find(".icon").css("visibility","hidden");
    $(this).find(".description").css("visibility","hidden");
});

// 点击专题图，进入专题图浏览页面
$(".img-responsive").click(function () {
    // 获取到地图id值
    console.log($(this).attr('mapid'));
});

// 点击编辑地图按钮，进入专题地图编辑页面
$(document).on('click','.icon.edit',function(e){
    // 获取到地图id值
    console.log($(this).parent().find("img").attr("mapid"));
});

// 点击地图详情按钮(为动态生成的元素绑定事件-->必须采用on函数的方式)
$(document).on('click','.icon.info',function(e){
    //模态框弹出来之后图名和权限的设置
    setModalValue($(this));
});

// 我要制图按钮点击事件
$(".to-mapping").click(function () {
    window.open("http://www.baidu.com");
});

function setModalValue(e) {
    var nameNow=e.parent().find('p')[0].innerHTML;
    var mapTag=e.parent().find("img").attr("maptag");
    var imgSrc=e.parent().find("img").attr("src");
    var mapId = e.parent().find("img").attr("mapid");
    var editTime = e.parent().find("img").attr("editTime");
    var submitTime = e.parent().find("img").attr("submitTime");
    var descriptionNow=e.parent().find(".description")[0].innerHTML;

    $(".modal-body.map-info").html('');
    $(".modal-body.map-info").append(
        '<img src="'+imgSrc+'" style="width: 100%">'+
        '<h3><strong>'+nameNow+'</strong></h3>'+
        '<h5><strong>地图标签：</strong>'+mapTag+'</h5>'+
        '<h5><strong>地图描述：</strong>'+descriptionNow+'</h5>'+
        '<h5><strong>创建时间：</strong>'+submitTime+'</h5>'+
        '<h5><strong>编辑时间：</strong>'+editTime+'</h5>'
    );

}

//点击删除按钮对应的事件(注意需要将对应修改部分进行动态)
$(document).on('click','.trash',function(e){
    var mapID =  $(this).parent().find('img').attr("mapid");
    var thematicMapDiv = $(this).parent().parent();
    var type = "delete";
    swal({
            title: "是否确定删除?",
            text: "删除后将无法复原该文件!",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "是的,删除文件!",
            cancelButtonText: "不,取消删除!",
            closeOnConfirm: false,
            closeOnCancel: false
        },
        function (isConfirm) {
            if (isConfirm) {
                // swal("删除成功!", "该专题图已经被删除", "success");
                var delete_url = "./servlet/EditMapServlet?id=" + mapID + "&type=" + type ;
                $.ajax({
                    url: delete_url,
                    type: 'POST',
                    dataType: 'json',
                    cache:false,
                    scriptCharset: 'utf-8',
                    success: function (data) {
                        if(data==1){
                            thematicMapDiv.remove();
                            for (var i in allMapList){
                                if(mapID == allMapList[i].mapID){
                                    allMapList.splice(i,1);
                                }
                            }
                            // swal("操作成功!", "专题图删除完成!", "info");
                            swal({
                                title: "操作成功",
                                text: "该专题图已经被删除!",
                                // type: "success",
                                imageUrl:"images/success.png",
                                imageWidth:40,
                                imageHeight:40,
                                animation:false,
                                showCancelButton: false,
                                confirmButtonText: "确定",
                                closeOnConfirm: false,
                                closeOnCancel: false
                            });
                        }
                        else {
                            // swal("操作失败!", "专题图删除失败!", "warning");
                            swal({
                                title: "操作失败",
                                text: "专题图删除失败!",
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
                        swal("操作失败!", "专题图删除失败!", "warning");
                    }
                });
            }
            else {
                // swal("取消删除", "该专题图得以保留", "warning");
                swal({
                    title: "取消删除",
                    text: "该专题图得以保留!",
                    type: "info",
                    showCancelButton: false,
                    confirmButtonText: "确定",
                    closeOnConfirm: false,
                    closeOnCancel: false
                });
                return false;
            }
        });
});



