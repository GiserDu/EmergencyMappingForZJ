$(document).ready(function() {
    $("table div").mouseenter(function(){
      $( $(this).find("button")).css("visibility","visible");
    });
    $("table div").mouseleave(function(){
        $( $(this).find("button")).css("visibility","hidden");
      });
    $(".disasterStatus_btn").click(function(){
      //将当前的灾害状态和灾种记录到localstorage中
      disaster_status=$(this).attr("value");
      disaster_type=$($(this).parent()).attr("id");
      localStorage.setItem("disaster_status", disaster_status)
      localStorage.setItem("disaster_type",disaster_type)
      //跳转到模板选择页面
        if(self!=top){
            //关闭子窗口并弹出新的layUI
            window.location.href='templateMini.html';
        }
        else{
            window.location.href='template.html';
        }


    });
      
});

//新建空白模板
function blank_btnClick() {
    var index=parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);

    parent.blank_btnClick();
}