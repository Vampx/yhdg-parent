<@app.html>
    <@app.head>
    <style type="text/css">
        .login_content{
            background: url("${app.imagePath}/img/bg.png") no-repeat top center/100% 100%;
        }

        .login_content .c-left div span:nth-child(2){
            background: url("${app.imagePath}/img/icon2.png") no-repeat center/100% auto;
        }
    </style>
    <script>
        function showMsg(msg) {
            $('.tips').css("color","red");
            $('.tips').html(msg);
            $.messager.alert('提示信息', msg, 'info');
        }
        function authImg(obj) {
            obj.src = '${contextPath}/security/main/auth_img.htm?_' + new Date().getTime();
        }
        function ok() {
            if ($('#username').val() == '' || $('#username').val() == '用户名') {
                showMsg('请录入用户名');
                $('#username').focus();
                return;
            }
            if ($('#password').val() == '' || $('#password').val() == '密码') {
                showMsg('请录入密码');
                $('#password').focus();
                return;
            }
//            if ($('#auth_code').val() == '' || $('#auth_code').val() == '验证码') {
//                showMsg('请录入验证码');
//                $('#auth_code').focus();
//                return;
//            }

            $.post('${contextPath}/security/main/login.htm', {
                username: $('#username').val(),
//                authCode: $('#auth_code').val(),
                password: $('#password').val()
            }, function (json) {
                if (json.success) {
                    document.location.href = '${contextPath}/security/main/index.htm';


                } else {
                    showMsg(json.message);
                }
            }, 'json');
        }
        $(function () {
            var username = $.cookie('username');
            if(username != undefined && username != '') {
                $('#cookie_flag').attr('checked','checked');
                $('#username').val(username);
                $('#password').focus();
            } else {
                $('#username').focus();
            }

            $('#username').keydown(function (event) {
                if (event.keyCode == 13) {
                    if ($('#username').val() == '') {
                        showMsg('请录入用户名');
                        $('#username').focus();

                    } else {
                        $('#password').focus();
                    }

                }
            });
            $('#password').keydown(function (event) {
                if (event.keyCode == 13) {
                    if ($('#password').val() == '') {
                        showMsg('请录入密码');
                        $('#password').focus();

                    } else {
                        $('#auth_code').focus();
                    }
                }
            });
            $('#auth_code').keydown(function (event) {
                if (event.keyCode == 13) {
                    if ($('#auth_code').val() == '') {
                        showMsg('请录入验证码');
                        $('#auth_code').focus();

                    } else {
                        ok();
                    }
                }
            });
        })
    </script>
    <#--    <style>
            .login_wrap .login_box .login_con p.submit a{ text-align:center; font-size:20px; color:#fff; display:block; height:45px; line-height:44px; background:url(${app.imagePath}/login_btn_bg_${(controller.appConfig.brandImageSuffix)!''}.jpg) repeat;}
            .login_wrap .login_box .login_con p.submit a:hover{ background:url(${app.imagePath}/login_btn_bg1.jpg) repeat;}
        </style>-->
    </@app.head>
    <@app.body>
    <#--<div class="container login_wrap">
         <div class="login_box">
                <div class="login_img"><img src="${app.imagePath}/login_bg_zl.jpg"></div>
                <div class="login_con">
                    <div class="logo_box"><img src="${app.imagePath}/login_logo_zl.png" /></div>
                    <h2>登录您的${(controller.appConfig.brandPlatformName)!''}账户</h2>
                    <p class="input_bg">
                        <label class="username">用户名</label>
                        <input type="text" class="text" maxlength="18" placeholder="请输入您的用户名" id="username" />
                    </p>
                    <p class="input_bg">
                        <label class="password">密码</label>
                        <input type="password" class="password" maxlength="16" placeholder="请输入您的密码" id="password"/>
                    </p>
                    &lt;#&ndash;<p class="input_bg">&ndash;&gt;
                        &lt;#&ndash;<label class="captcha">验证码</label>&ndash;&gt;
                        &lt;#&ndash;<input type="text" class="text" maxlength="4" placeholder="请输入验证码" id="auth_code"/>&ndash;&gt;
                        &lt;#&ndash;<img src="${contextPath}/security/main/auth_img.htm" class="captcha_img" onclick="authImg(this)"/>&ndash;&gt;
                    &lt;#&ndash;</p>&ndash;&gt;
                    <p class="submit"><a href="javascript:void(0)" onclick="ok()">登 录</a></p>
                    <p class="tips">如果您是运营商请点击，<a class="a${(controller.appConfig.brandImageSuffix)}" target="_blank" href="${(configValue)!''}">运营商登录</a></p>
                </div>
            </div>
    </div>-->
    <div class="wraper">
        <div class="login_top">
            <img src="${app.imagePath}/img/login/logo.png" />
            <div>
                <span>这锂换电</span>
                <span>V1.0.0</span>
            </div>
        </div>
        <div class="login_content">
            <div class="c-left">
                <div>
                    <span>这锂换电</span>
                    <span>突破传统，引领移动智能时代</span>
                </div>
            </div>
            <div class="c-right">
                <div class="cr-left">
                    <img src="${app.imagePath}/img/login/icon1.png"/>
                </div>
                <!--账号密码登录-->
                <div class="cr-right" id="pwd">
                    <div class="tips">欢迎登录</div>
                <#--<div class="role">-->
                <#--<i><img src="${app.imagePath}/img/operater.png"/></i>-->
                <#--<select>-->
                <#--<option>运营商</option>-->
                <#--<option>用户</option>-->
                <#--</select>-->
                <#--</div>-->
                    <div class="role">
                        <i></i>
                    </div>
                    <div class="account">
                        <i><img src="${app.imagePath}/img/username.png"/></i>
                        <input type="text" placeholder="请输入您的账号" id="username" />
                    </div>
                    <div class="pwd">
                        <i><img src="${app.imagePath}/img/pwd.png"/></i>
                        <input type="password" placeholder="请输入您的密码" id="password" />
                    </div>
                <#--<div class="btn" onclick="window.location.href='index.html'">登录</div>-->
                    <a href="#">
                        <div class="btn_login" id="login_btn" onclick="ok()">登 录</div>
                    </a>
                <#--<div class="qimg"><img src="${app.imagePath}/img/qrcode.png"/></div>-->
                </div>
                <!--扫码登录-->
                <div class="cr-right" id="code" style="display: none;">
                    <div class="tips">扫码登录</div>
                    <div class="code">
                        <img src="${app.imagePath}/img/code.png" />
                        <div>
                            <img src="${app.imagePath}/img/refresh.png" />
                            <span>二维码已失效，请点击刷新</span>
                        </div>
                    </div>
                    <div class="gotips">请打开微信扫一扫，扫描二维码登录</div>
                    <div class="gopwd">密码登陆</div>
                </div>
            </div>
        </div>
        <div class="foot">Copyright 2019-2020</div>
    </div>
    <#--<script src="js/jquery-1.10.2.min.js" type="text/javascript"></script>-->
    <script>
        $('#password').bind('keyup', function (event) {
            if (event.keyCode == "13") {
                ok();
            }
        });

        let tab = 1
        $(function(){
            $('.qimg').click(()=>{
                tab = 2
                $('#code').show()
            $('#pwd').hide()
        })
            $('.gopwd').click(()=>{
                tab = 1
                $('#code').hide()
            $('#pwd').show()
        })
        })

    </script>
    </@app.body>
</@app.html>
