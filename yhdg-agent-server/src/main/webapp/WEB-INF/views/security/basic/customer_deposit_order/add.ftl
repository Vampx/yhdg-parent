<div class="popup_body clearfix">
    <ul class="tab_nav" id="tab_container_${pid}">
        <li class="selected">基本信息</li>
    </ul>
    <div class="tab_con">
        <div class="tab_item" style="display: block;">
            <div class="ui_table">
                <form>
                    <input type="hidden" name="customerId" >
                    <table cellpadding="0" cellspacing="0">
                        <tr>
                            <td align="right">客户：</td>
                            <td><input type="text" class="text easyui-validatebox"  value=""maxlength="40" name="customerFullname"  style="width: 184px; height: 28px;"/></td>
                        </tr>
                        <tr>
                            <td width="70" align="right">手机号：</td>
                            <td><input type="text" class="text easyui-validatebox" maxlength="11" name="customerMobile" value="" style="width: 184px; height: 28px;"/></td>
                        </tr>
                        <tr>
                            <td width="70" align="right">充值金额：</td>
                            <td><input id="money_${pid}" class="easyui-numberspinner"  style="width: 197px; height: 28px;" required="required" data-options="min:0.01,precision:2,max:10000">&nbsp;&nbsp;元</td>
                        </tr>
                        <tr>
                            <td align="right" valign="top" style="padding-top:10px;">充值备注：</td>
                            <td><textarea style="width:330px;" name="memo" maxlength="120"></textarea></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function(){
        var win = $('#${pid}'), jform = win.find('form');
        var form = jform[0];
        win.find('input[name=customerFullname],input[name=customerMobile]').click(function() {
            selectCustomer();
        });

        function selectCustomer() {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择客户',
                href: "${contextPath}/security/basic/customer/select_customer.htm",
                windowData: {
                    ok: function(config) {
                        win.find('input[name=customerId]').val(config.customer.id);
                        win.find('input[name=customerFullname]').val(config.customer.fullname);
                        win.find('input[name=customerMobile]').val(config.customer.mobile);
                    }
                },
                event: {
                    onClose: function() {
                    }
                }
            });
        }

        win.find('button.ok').click(function () {
            $.messager.confirm('提示信息', '确认充值?', function(ok) {
                if (ok) {
                    jform.form('submit',{
                        url:'${contextPath}/security/basic/customer_deposit_order/create.htm',
                        onSubmit: function(param) {
                            var isValid = $(this).form('validate');
                            var money = $('#money_${pid}').numberspinner('getValue');
                            param.money = parseInt(Math.round(money * 100));
                            if (!isValid){
                                return false;
                            }
                            return true;
                        },
                        success:function(text){
                            var json = $.evalJSON(text);
                        <@app.json_jump/>
                            if(json.success){
                                $.messager.alert('提示信息','充值成功','info');
                                win.window('close');
                            }else{
                                $.messager.alert('提示信息',json.message,'info');
                            }
                        }
                    });
                }
            });
        });
        win.find('button.close').click(function(){
            win.window('close');
        });
    })();
</script>

