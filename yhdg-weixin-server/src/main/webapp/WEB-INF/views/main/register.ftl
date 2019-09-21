<@app.html>
    <@app.head>
    </@app.head>
    <@app.body>
    <div class="page register_page">
        <div class="page__bd">
            <div class="weui-cells weui-cells_form">
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">手机号</label>
                    </div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="number" pattern="[0-11]*" placeholder="请输入您的手机号" id="mobile">
                    </div>
                    <div class="weui-cell__ft">

                    </div>
                </div>
                <div class="weui-cell weui-cell_vcode">
                    <div class="weui-cell__hd">
                        <label class="weui-label">验证码</label>
                    </div>
                    <div class="weui-cell__bd">

                        <input class="weui-input" type="number" placeholder="请输入验证码" id="mobile_auth_code">
                    </div>
                    <div class="weui-cell__ft">
                        <button class="weui-vcode-btn" id="mobile_auth_code_button">获取验证码</button>
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label for="" class="weui-label">密码</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="password" value="" id="password" placeholder="请输入6位数字密码">
                    </div>
                    <div class="weui-cell__ft">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label for="" class="weui-label">确认密码</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="password" value="" id="confirm_password" placeholder="请再次输入密码">
                    </div>
                    <div class="weui-cell__ft">
                    </div>
                </div>
            </div>
            <div class="weui-btn-area">
                <a href="javascript:register();" class="weui-btn weui-btn_primary weui-btn_default" id="register_button">注册</a>
            </div>
        </div>
    </div>
    </@app.body>
</@app.html>
<script  type="text/javascript">

    $('#mobile_auth_code_button').tap(function() {
        var verifyPass = true;
        $('#mobile').closest('.weui-cell').find('.weui-cell__ft').find('.weui-icon-warn').remove();
        if($('#mobile').val() == '' || $('#mobile').val().length != 11 || $('#mobile').val() != parseInt($('#mobile').val())) {
            $('#mobile').closest('.weui-cell').find('.weui-cell__ft').append('<i class="weui-icon-warn" style="display: block"></i>');
            verifyPass = false;
        } else {
            $('#mobile').closest('.weui-cell').find('.weui-cell__ft').find('.weui-icon-warn').remove();
        }
        if(!verifyPass) {
            return;
        }

        var self = $(this);
        if(self.text() == '获取验证码') {
            showLoading();
            $.post('${contextPath}/main/mobile_auth_code.htm', {
                mobile: $('#mobile').val(),
                type: "register"
            }, function(json) {
                <@app.json_ajax_callbak/>

                if(json.success) {
                    var time = 60;
                    var interval = setInterval(function() {
                        time--;
                        if(time <= 0) {
                            self.text('获取验证码');
                            clearInterval(interval);
                        } else {
                            self.text('' + time + ' 秒');
                        }
                    }, 1000);

                } else {
                    showError(json.message || "获取手机验证码失败");
                }

            }, 'json');


        } else {
            showError('请稍后点击');
        }
    });

    $('#mobile_auth_code').blur(function() {
        if($('#mobile_auth_code').val() == '') {
            return;
        }

        $.ajax({
            type: "POST",
            url: "${contextPath}/main/check_mobile_auth_code.htm",
            data: { mobileAuthCode: $('#mobile_auth_code').val() },
            dataType: "json",
            success: function(json){
                <@app.json_ajax_callbak/>
                if(!json.success) {
                    showError("手机验证码错误");
                }
            }
        });
    });

    function register() {
        var verifyPass = true;

        $('#mobile').closest('.weui-cell').find('.weui-cell__ft').find('.weui-icon-warn').remove();
        $('#mobile_auth_code').closest('.weui-cell').find('.weui-cell__ft').find('.weui-icon-warn').remove();
        $('#password').closest('.weui-cell').find('.weui-cell__ft').find('.weui-icon-warn').remove();
        $('#confirm_password').closest('.weui-cell').find('.weui-cell__ft').find('.weui-icon-warn').remove();


        if($('#mobile').val() == '') {
            $('#mobile').closest('.weui-cell').find('.weui-cell__ft').append('<i class="weui-icon-warn" style="display: block"></i>');

            verifyPass = false;
            return;
        } else {
            $('#mobile').closest('.weui-cell').find('.weui-cell__ft').find('.weui-icon-warn').remove();
        }

        if($('#mobile_auth_code').val() == '') {
            $('#mobile_auth_code').closest('.weui-cell').find('.weui-cell__ft').prepend('<i class="weui-icon-warn" style="display: block"></i>');
            verifyPass = false;
            return;
        } else {
            $('#mobile_auth_code').closest('.weui-cell').find('.weui-cell__ft').find('.weui-icon-warn').remove();
        }

        if($('#password').val() == '' || !(/^\d+$/.test($('#password').val())) || $('#password').val().length < 6) {
            $('#password').closest('.weui-cell').find('.weui-cell__ft').append('<i class="weui-icon-warn" style="display: block"></i>');
            verifyPass = false;

            if(!(/^\d+$/.test($('#password').val()))) {
                showError('密码必须是数字');
            } else if($('#password').val().length < 6) {
                showError('密码长度至少是6位');
            }
            return;

        } else {
            $('#password').closest('.weui-cell').find('.weui-cell__ft').find('.weui-icon-warn').remove();
        }

        if($('#confirm_password').val() == '' || !(/^\d+$/.test($('#confirm_password').val())) || $('#confirm_password').val().length < 6) {
            $('#confirm_password').closest('.weui-cell').find('.weui-cell__ft').append('<i class="weui-icon-warn" style="display: block"></i>');
            verifyPass = false;
            if(!(/^\d+$/.test($('#confirm_password').val()))) {
                showError('确认密码必须是数字');
            } else if($('#confirm_password').val().length < 6) {
                showError('确认密码长度至少是6位');
            }
            return;
        } else {
            $('#confirm_password').closest('.weui-cell').find('.weui-cell__ft').find('.weui-icon-warn').remove();
        }

        if($('#confirm_password').val() != '') {
            if($('#password').val() != $('#confirm_password').val()) {
                $('#confirm_password').closest('.weui-cell').find('.weui-cell__ft').append('<i class="weui-icon-warn" style="display: block"></i>');
                verifyPass = false;
                showError('密码和确认密码不一致');
                return;
            } else {
                $('#confirm_password').closest('.weui-cell').find('.weui-cell__ft').find('.weui-icon-warn').remove();
            }
        }

        if(!verifyPass) {
            return;
        }

        showLoading();
        $.post('${contextPath}/main/register.htm', {
            mobile: $('#mobile').val(),
            password: $('#password').val(),
            mobileAuthCode: $('#mobile_auth_code').val()
        }, function(json) {
            <@app.json_ajax_callbak/>
            closeLoading();
            if(json.success) {
                showToast('注册成功', function() {
                    <#if (Session['LOGIN_AFTER_REDIRECT'])??>
                        <#if Session['LOGIN_AFTER_REDIRECT'] == 'my_order'>
                            document.location.href = '${contextPath}/my_order/index.htm';
                        <#else>
                            document.location.href = '${contextPath}/scan_code/index.htm';
                        </#if>
                    <#else>
                        document.location.href = '${contextPath}/my_order/index.htm';
                    </#if>
                });
            } else {
                showError(json.message || '注册失败');
            }
        }, 'json');
    }

    function registerProtocol() {
        showHtml({
            href: '${contextPath}/main/register_protocol.htm',
            event: {
                showAfter: function() {
                    $('div.container').first().hide();
                },
                closeAfter: function() {
                    $('div.container').first().show();
                }
            }
        })
    }

    (function (_D){
        var _self = {};
        _self.Html = _D.getElementsByTagName("html")[0];
        _self.widthProportion = function(){var p = (_D.body&&_D.body.clientWidth||_self.Html.offsetWidth)*0.16/320;return p>1?1:p;};
        _self.changePage = function(){_self.Html.setAttribute("style","font-size:"+_self.widthProportion()*100+"px !important");}
        _self.changePage();
        setInterval(_self.changePage,100);
    })(document);

    $('input').bind('focus',function(){
        $('header').removeClass("header_fixed");
    })
    $('input').bind('blur',function(){
        setInterval(function () {
            $('header').addClass("header_fixed");
        }, 800);
    });
</script>
