<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="agentId" value="${Session['SESSION_KEY_USER'].agentId}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">优惠券类型：</td>
                    <td>
                        <div style="float: left;">
                            <select class="easyui-combobox" editable="false" id="ticket_type_${pid}" name="ticketType" style="width:180px;height: 30px">
                            <#list TicketTypeEnum as s>
                                <option value="${s.getValue()}" >${s.getName()}</option>
                            </#list>
                            </select>
                        </div>
                        <div style="float: right;">
                            <span id="ticket_info_${pid}" style="color: red;font-weight: 600;display: none;line-height: 30px;">此券是现金券，可提现</span>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">优惠券名称：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" name="ticketName" required="true" style="width:170px;height: 30px "/></td>
                </tr>
                <tr>
                    <td width="80" align="right">优惠券金额：</td>
                    <td><input type="text" id="money_${pid}" class="text easyui-numberspinner"
                               style="width:180px;height: 30px " data-options="min:0.01, precision:2"
                               required="required"/>&nbsp;元
                    </td>
                </tr>
                <tr>
                    <td align="right">开始时间：</td>
                    <td><input type="text" class="text easyui-datebox" editable="false" style="width:185px;height:28px " id="begin_time_${pid}" required="true"></td>
                </tr>
                <tr>
                    <td align="right">结束时间：</td>
                    <td><input type="text" class="text easyui-datebox" editable="false" style="width:185px;height:28px " id="end_time_${pid}" required="true"></td>
                </tr>
                <tr>
                    <td align="right">客户手机：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="11" name="customerMobile" required="true" style="width:170px;height: 30px "/></td>
                </tr>
                <tr>
                    <td align="right">备注：</td>
                    <td>
                        <textarea id="memo_${pid}" name="memo" style="width:300px; height: 50px;"
                                  maxlength="40"></textarea>
                    </td>
                </tr>
                <textarea id="ids_${pid}" name="ids" style="width:300px; height: 50px; display: none;"></textarea>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <input type="hidden" value="false" id="committed"/>
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    $('#ticket_type_${pid}').combobox({
        onSelect: function(){
            var ticketType = $('#ticket_type_${pid}').combobox('getValue');
            if(ticketType == 3) {
                document.getElementById("ticket_info_${pid}").style.display = "block";
            }else {
                document.getElementById("ticket_info_${pid}").style.display = "none";
            }
        }
    });

    $(function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        $("#begin_time_${pid}").datebox().datebox('calendar').calendar({
            validator: function(date){
                var now = new Date();
                var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return d1<=date;
            }
        });

        $("#end_time_${pid}").datebox().datebox('calendar').calendar({
            validator: function(date){
                var now = new Date();
                var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return d1<=date;
            }
        });

        win.find('button.ok').click(function () {
            var isCommitted = $("#committed").val();

            if(isCommitted=="false") {
                var ticketName = $('input[name = "ticketName"]').val();
                if(ticketName == '') {
                    $.messager.alert('提示信息', '请输入优惠券名称', 'info');
                    return;
                }
                var money = $('#money_${pid}').numberspinner('getValue');
                if(money == '') {
                    $.messager.alert('提示信息', '请输入优惠券金额', 'info');
                    return;
                }
                var beginTime = $('#begin_time_${pid}').datetimebox('getValue');
                var endTime = $('#end_time_${pid}').datetimebox('getValue');
                if(beginTime == '') {
                    $.messager.alert('提示信息', '请选择开始时间', 'info');
                    return;
                }
                if(endTime == '') {
                    $.messager.alert('提示信息', '请选择结束时间', 'info');
                    return;
                }
                if(beginTime != '' && endTime != '' && beginTime > endTime) {
                    $.messager.alert('提示信息', '结束时间必须大于开始时间', 'info');
                    return;
                }
                var customerMobile = $('input[name = "customerMobile"]').val();
                if ($.trim(customerMobile) == '') {
                    $.messager.alert('提示信息', '请输入客户手机', 'info');
                    return;
                }
                $("#committed").val("true");

                form.form('submit', {
                    url: '${contextPath}/security/basic/customer_coupon_ticket/create.htm',
                    onSubmit: function (param) {
                        var money = $('#money_${pid}').numberspinner('getValue');
                        param.money = parseInt(Math.round(money * 100));
                        param.beginTime = beginTime + " 00:00:00";
                        param.expireTime = endTime + " 23:59:59";
                        return true;
                    },
                    success: function (text) {
                        var json = $.evalJSON(text);
                    <@app.json_jump/>
                        if (json.success) {
                            $.messager.alert('提示信息', '操作成功', 'info');
                            win.window('close');
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                });

            }

        });
        win.find('button.close').click(function () {
            win.window('close');
        });

    })
</script>
