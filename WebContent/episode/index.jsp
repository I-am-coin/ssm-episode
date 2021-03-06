<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="bean.pojo.User,java.util.Enumeration,bean.pojo.Episode,java.util.List,java.util.ArrayList"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	//----登出处理
	if (request.getParameter("logout") != null
		&& request.getParameter("logout").equals("1")) {
		session.removeAttribute("user");
		session.removeAttribute("flag");
	}
	//----获取用户数据
	int flag = session.getAttribute("flag")==null?0:(Integer)session.getAttribute("flag");
	User user = new User();
	if (flag == 1 && request.getAttribute("flag")==null) {
		user = (User)session.getAttribute("user");
	}
	else {
		//登录操作获取用户数据
		flag = request.getAttribute("flag")==null?0:1;
		if (flag == 1) {
			user = (User)request.getAttribute("user");
			session.setAttribute("user", user);
		}
		session.setAttribute("flag", flag);
	}
	//----注册处理
	String user_id = null;
	if (request.getAttribute("user_id") != null) {
		user_id = request.getAttribute("user_id").toString();
		System.out.println("1"+user_id);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%= basePath %>>"></base>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>【爱段社】</title>
<link href="favicon.ico" rel="icon" type="image/x-icon" />
<link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
<link href="css/index-style.css" rel="stylesheet" type="text/css"/>
<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/index.js" type="text/javascript"></script>
</head>
<body class="page-channel">
	<div class="header-top"></div>
	<div class="header-search">
		<div class="content">
			<a href="episode/index.jsp" class="logo iconfont icon-logo"></a>
			<div class="suggestion-search-box">
				<input placeholder="点击搜索" value="" class="input input-search"/>
			</div>
			<button class="btn btn-search"  onclick="javascript:alert('没实现，谢谢！');">搜  索</button>
			<div class="float-right header-links">
				<button class="btn btn-login js-to-login" style="display: <%= flag==0?"inline-block":"none"%>;">登录</button>
				<!-- 登陆成功，个人信息 -->
				<div class="user-info-box" style="display: <%= flag==1?"block":"none"%>;">
					<a class="user-info">
						<img src="images/<%= user.getUser_image()==null?"she.png":user.getUser_image()%>" class="profile"/>
						<span class="nickname"><%= user.getUser_nickname()==null?"":user.getUser_nickname()%></span>
						<span class="iconfont icon-down"></span>
					</a>
					<div class="user-info-menu" style="display: none;">
						<a href="episode/personal_center.jsp">个人中心</a>
						<a class="logout-btn">退出</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="main index-main">
		<!-- 右侧广告 -->
		<div class="aside">
			<!-- <div class="normal-channel-info js-channel-subscribe-item">
				<img src="images/she.png" alt="icon" class="channel-image"/>
				<p class="user-info"></p>
			</div> -->
			<a href="<%= flag==1?"episode/personal_center.jsp":"episode/index.jsp"%>">
				<div class="profile-wrapper">
					<img src="images/<%= user.getUser_image()==null?"she.png":user.getUser_image()%>" />
					<p id="p-nick" class="nickname"><%= user.getUser_nickname()==null?"":user.getUser_nickname()%></p>
					<p><%= user.getUser_id()==null?"":user.getUser_id()%></p>
				</div>
			</a>
			<div class="advertisement">
				<p>老<br />板<br />，<br />打<br />广<br />告<br />吗<br />？</p>
			</div>
		</div>
		<!-- 左侧段子 -->
		<div id="episode" class="channel-news channel-new-0">
		</div>
		<div class="no-more-episodes" style="display: inline-block;">加载数据中...</div>
	</div>
	
	<div class="footer-mini-wrapper">
		<div class="footer-mini">
			<p>
				<a>
					<span class="icon-jgw"></span><span>京公网安备 xxxxxxxxxxxxx号</span>
				</a>
				<span>蜀网文[2017]xxxx-xxx号</span> <span>蜀ICP证xxxxxx号</span>
			</p>
			<p>
				<a href="mailto:wlx-959@qq.com">举报邮箱：wlx.959@qq.com</a>
				<span>公司名称：明日之星概不负责公司</span>
			</p>
		</div>
	</div>
	
	<!-- 登录界面 -->
	<div class="dialog login-register-dialog login-dialog" style="display: none;">
		<div class="close iconfont icon-close"></div>
		<div class="logo iconfont icon-logo"></div>
		<form name="login-form" class="login-form" action="user/userLogin" method="post">
			<div class="input-box input-username-box">
				<input type="text" class="input-username" name="username" placeholder="用户名" value="">
			</div>
			<div class="input-box input-password-box">
				<input type="password" class="input-password" name="password" placeholder="密码" value="">
			</div>
			<div class="error-msg-inner">
				<p class="error-msg"></p>
			</div>
			<input type="hidden" value="index" name="page"/>
			<button type="button" class="btn-login" id="click-to-login" style="pointer-events: auto;">登录</button>
			<button type="button" class="btn-register js-to-register btn-has-border">注册</button>
		</form>
	</div>
	<!-- 注册界面 -->
	<div class="dialog login-register-dialog register-dialog" style="display: none;">
		<div class="close iconfont icon-close"></div>
		<div class="logo iconfont icon-logo"></div>
		<form name="register-form" class="register-form" action="user/userRegister" method="post">
			<div class="input-box input-username-box">
				<input type="text" class="input-username" name="nickname" placeholder="给自己起一个昵称吧" value="">
			</div>
			<div class="input-box input-password-box">
				<input type="password" class="input-password" name="psw" placeholder="在这里输入您的密码" value="">
			</div>
			<div class="input-box input-password-box">
				<input type="password" class="input-password2" name="psw2" placeholder="请重新确认一遍密码" value="">
			</div>
			<div class="error-msg-inner">
				<p class="error-msg"></p>
			</div>
			<button type="button" class="btn-register" id="click-to-register">注册</button>
		</form>
	</div>
	<!-- 注册成功界面 -->
	<div class="dialog register-ok-dialog" style="display: <%= user_id==null?"none":"block"%>;">
		<div class="close iconfont icon-close"></div>
		<div class="logo iconfont icon-logo"></div>
		<p>注册成功！<br />您的账号为：<%= user_id%>,快用账号登录吧！</p>
	</div>
	<div class="mask" style="display: <%= user_id==null?"none":"block"%>;"></div>
	<div class="widget-tool">
	 	<div class="item back-up icon iconfont icon-back-up anim" style="display:none;"></div>
	 	<div class="item refresh icon iconfont icon-refresh anim"></div>
	</div>
	
<script type="text/javascript">
	$(document).ready(function() {
		var page_data = null;
        var loading = false;//标志未加载
		//获取段子数据
		$.ajax({
			type : "post",
			url : "episode/getEpisodes_ajax",
			data : {
				"page_num" : 1
			},
			dataType:"json",
			success : function(data) {
				page_data = data.data;
				fnCallback_episode(page_data);
			},
			error : function(msg) {
				alert("请求失败！");
			}
		});
		//滚动加载数据
		$(window).scroll(function(){
            var srollPos = $(window).scrollTop();    //滚动条距顶部距离(页面超出窗口的高度)
            var totalheight = parseFloat($(window).height()) + parseFloat(srollPos);
            
            if(($(document).height()) <= totalheight + 50) {//执行添加一页数据!!滚动过快重复加载数据
            	if (page_data.hasNextPage && !loading) {//有下一页,且没有加载
            		loading = true;
            		//获取下一页段子数据
            		$.ajax({
            			type : "post",
            			url : "episode/getEpisodes_ajax",
            			data : {
            				"page_num" : page_data.nextPage
            			},
            			dataType:"json",
            			success : function(data) {
            				page_data = data.data;//更新 page_data 数据
            				fnCallback_episode(page_data);
            			},
            			error : function(msg) {
            				alert("请求失败！");
            			}
            		}).done(function() {
            			loading = false;
            		})
            	}
            	else if (!page_data.hasNextPage) {//数据加载完成
            		$(".no-more-episodes").html("已加载全部内容");
            	}
            }
        });
	})
	function fnCallback_episode(page_data) {
		var list = page_data.list;//段子分页数据
		var content = $("#episode");
		
		if (list != null) {
			for (var i=0; i<list.length; i++) {
				content.append('<a href="episode/getEpisodeById?episode_id='+list[i].episode_id+'" target="_blank" class="item doc doc-joke">'
						+'<div class="doc-title">'+list[i].episode_content.substr(0,10)+'...</div>'
						+'<h3 class="doc-summary">'+list[i].episode_content+'</h3>'
						+'<div class="doc-info"></div></a>');
			}
		}
		else {
			alert("读取数据失败，请稍后重试！");
		}
	}

 	function fnCallback(data) {
		if (data.flag == 1) {
			$(".login-form").submit();
		}
		else {
			$(".login-dialog .error-msg-inner").css("display", "block");
 			$(".login-dialog .error-msg").html('用户名或密码错误！');
		}
	}
	//处理登录AJAX
 	$("#click-to-login").click(function() {
 		if ($("input[name='username']").val() == ''
 				|| $("input[name='password']").val() == '') {
 			$(".login-dialog .error-msg-inner").css("display", "block");
 			$(".login-dialog .error-msg").html('用户名和密码不能为空！');
 		}
 		else if ($("input[name='username']").val().length != 8) {
 			$(".login-dialog .error-msg-inner").css("display", "block");
 			$(".login-dialog .error-msg").html('用户名必须为8个字符！');
 		}
 		else if ($("input[name='password']").val().length > 20
 				|| $("input[name='password']").val().length < 6) {
 			$(".login-dialog .error-msg-inner").css("display", "block");
 			$(".login-dialog .error-msg").html('密码必须为6-20个字符！');
 		}
 		else {
			$.ajax({
				type : "post",
				url : "user/userLogin_ajax",
				data : {
					"username" : $("input[name='username']").val(),
					"password" : $("input[name='password']").val()
				},
				dataType:"json",
				success : function(data) {
					fnCallback(data);
				},
				error : function(msg) {
					alert("请求失败！");
				}
			});
 		}
	})
	//处理退出登录
	$(".logout-btn").click(function() {
		window.location.replace("episode/index.jsp?logout=1");
	})
	//处理注册输入正确性
	$("#click-to-register").click(function() {
		if ($("input[name='nickname']").val().length < 2
				|| $("input[name='nickname']").val().length > 10) {
 			$(".register-dialog .error-msg-inner").css("display", "block");
 			$(".register-dialog .error-msg").html('昵称必须为2-20个字符！');
		}
		else if ($("input[name='psw']").val().length < 6
				|| $("input[name='psw']").val().length > 20) {
			$(".register-dialog .error-msg-inner").css("display", "block");
 			$(".register-dialog .error-msg").html('密码必须为6-20个字符！');
		}
		else if ($("input[name='psw']").val() != $("input[name='psw2']").val()) {
			$(".register-dialog .error-msg-inner").css("display", "block");
 			$(".register-dialog .error-msg").html('两次密码输入不同，请重新确认！');
		}
		else {
			$(".register-form").submit();
		}
	})
</script>
</body>
</html>