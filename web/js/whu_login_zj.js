var Geoway = {
	casServer : 'http://118.178.118.177:8080/cas',
	root: window.location.origin + "/platform-main"
}
var LoginCas = {
		
	login: function(){
		var target = window.location.href;
		var _url = Geoway.casServer + '/login?service=' + target;
		window.open(_url, '_self');
	},
	logout: function(){
		var target = window.location.href;
		var _url = Geoway.casServer + '/logoutCas?service=' + target;
        $('.username a').text("");
		window.open(_url, '_self');
	},
	checkIsLogin: function(fnSuccess, fnFailure, openLoginPage, context){
		var that = this;
		var queryUrl = Geoway.root + "/app/login/checkLogin.do?dc=" + Math.random();
		$.ajax({
			type : 'GET',
			dataType : 'json',
			url : queryUrl,
			success : function (result) {
				if (result.status == "OK" && result.checkResult) {
					if(fnSuccess){
						fnSuccess.bind(context)(result.message);
					}
				}
			},
			error : function (result) {
				if(fnFailure){
					fnFailure.bind(context)(false);
				}
				if(openLoginPage){
					that.login();
				}
			}
		});
	}
}


var that = this;
/*头部登录控制*/
$(function(){
		LoginCas.checkIsLogin(
				function(username){
					// $('#logout-ul').hide();
					$('#login-ul').show();
					$('.username a').text(username);
				},
				function(){
					// $('#logout-ul').show();
					$('#login-ul').hide();
				}, false, that);

})
$(function(){
	$('#logout').click(function(){
		LoginCas.logout();
	});
	$('#login').click(function(){
		LoginCas.login();
	})
})