<div class="c-d-three-1 c-d-three-1-site" style="display: none;">
    <button class="export" onclick="pay_people_export_excel()">导出</button>
    <form id="pay_people_parameter">
        <div class="box">
            <li>
                <span>微信账号：</span>
                <input type="text" name="payPeopleMpOpenId" value="${(entity.payPeopleMpOpenId)!''}">
                <p>
                    <a href="javascript:selectPayeeCustomer()" style="font-weight: bold;padding-left: 3px;">+选择人员</a>
                </p>
            </li>
            <li>
                <span>收款人姓名：</span>
                <input type="text" name="payPeopleName" value="${(entity.payPeopleName)!''}">
            </li>
            <li>
                <span>手机号：</span>
                <input type="text" name="payPeopleMobile" value="${(entity.payPeopleMobile)!''}">
            </li>
            <li>
                <span>提现密码：</span>
                <input type="text" name="payPassword" value="${(entity.payPassword)!''}">
            </li>
            <p>注意：这里的微信是微信在公众号里面的sn，可以通过注册公众号后，获取
                <img src="${app.imagePath}/shili.png" alt="">
            </p>
            <li>
                <span>支付宝账号：</span>
                <input type="text" name="payPeopleFwOpenId" value="${(entity.payPeopleFwOpenId)!''}">
            </li>
        </div>
    </form>
    <button class="but" id="pay_people_update">保存</button>
</div>

<script>

    function pay_people_export_excel() {
        $.messager.confirm('提示信息', '确认导出数据?', function (ok) {
            if (ok) {
                window.location.href = "${contextPath}/security/hdg/station/pay_people_export_excel.htm?stationId=${(entity.id)!''}";
            }
        });
    }

    $('#pay_people_update').click(function () {
        $.messager.confirm('提示信息', '确认修改收款人信息?', function (ok) {
            if(ok) {
                var ok = function () {
                    var success = true;
                    var values = {
                        id: '${(entity.id)!''}',
                        payPeopleName: $('#pay_people_parameter').find('input[name=payPeopleName]').val(),
                        payPeopleMpOpenId: $('#pay_people_parameter').find('input[name=payPeopleMpOpenId]').val(),
                        payPeopleFwOpenId: $('#pay_people_parameter').find('input[name=payPeopleFwOpenId]').val(),
                        payPeopleMobile: $('#pay_people_parameter').find('input[name=payPeopleMobile]').val(),
                        payPassword: $('#pay_people_parameter').find('input[name=payPassword2]').val()
                    };

                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/hdg/station/update_pay_people.htm',
                        dataType: 'json',
                        data: values,
                        success: function (json) {
                        <@app.json_jump/>
                            if (json.success) {
                                $.messager.alert('提示信息', '操作成功', 'info');
                            } else {
                                $.messager.alert('提示信息', json.message, 'info');
                                success = false;
                            }
                        },
                        error: function (text) {
                            $.messager.alert('提示信息', text, 'info');
                            success = false;
                        }
                    });

                    return success;
                };

                $('#pay_people_parameter').data('ok', ok);

                var go = $('#pay_people_parameter').data('ok')();
                if(go) {
                    //刷新或后退
                    $('#pay_people').click();
                }
            }
        });
    });

    function selectPayeeCustomer() {
        App.dialog.show({
            css: 'width:826px;height:522px;overflow:visible;',
            title: '选择收款人',
            href: "${contextPath}/security/basic/customer/select_payee_customer.htm?agentId=${(entity.agentId)!0}"  ,
            windowData: {
                ok: function(config) {
                    $('#pay_people_parameter').find('input[name=payPeopleName]').val(config.customer.fullname);
                    $('#pay_people_parameter').find('input[name=payPeopleMobile]').val(config.customer.mobile);
                    $('#pay_people_parameter').find('input[name=payPeopleMpOpenId]').val(config.customer.mpOpenId);
                }
            },
            event: {
                onClose: function() {
                }
            }
        });
    }

</script>