<div class="popup_body" style="padding-left:50px;padding-top: 40px;font-size: 14px;">
    <div class="tab_item" style="display:block;">
        <div class="ui_table">
            <form method="post">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="90" align="left">*运营商：</td>
                        <td>
                            <input name="agentId" required="true" id="agent_id_${pid}" class="easyui-combotree" editable="false"
                                   style="width:185px;height:28px "
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto',
                                onClick: function(node) {
                                   swich_battery_type_${pid}();
                                }
                            "/>
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="left">*套餐名称：</td>
                        <td><input type="text" class="text easyui-validatebox" required="true" maxlength="40"   style="width:175px;height:28px "
                                   name="priceName" value=""/></td>
                    </tr>
                    <tr>
                        <td width="70" align="left">*电池类型：</td>
                        <td>
                            <input name="batteryType" id="battery_type_${pid}" class="easyui-combotree" editable="false" required="true"
                                    style="width: 185px; height: 28px;"
                                   data-options="url:'',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto',
                                onClick: function(node) {
                                   swich_battery_type_foregift_${pid}();
                                }
                            "/>
                        </td>
                    </tr>
                    <tr>
                        <td align="left">*开始时间：</td>
                        <td><input type="text" class="text easyui-datebox" editable="false" style="width:185px;height:28px " id="begin_time_${pid}" required="true"></td>
                    </tr>
                    <tr>
                        <td align="left">*结束时间：</td>
                        <td><input type="text" class="text easyui-datebox" editable="false" style="width:185px;height:28px " id="end_time_${pid}" required="true"></td>
                    </tr>
                    <tr>
                        <td align="left">*是否启用：</td>
                        <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" checked
                                   value="1"/><label for="is_active_1">启用</label>
                        </span>
                            <span class="radio_box">
                            <input type="radio" class="radio" name="isActive"
                                   value="0"/><label for="is_active_0">禁用</label>
                        </span>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
<div class="popup_btn" >
    <button class="btn btn_red vip_ok">确定</button>
    <button class="btn btn_border vip_close">关闭</button>
</div>

<script>

    var agentId;
    function swich_battery_type_${pid}() {
        agentId = $('#agent_id_${pid}').combotree('getValue');
        var batteryTypeCombotree = $('#battery_type_${pid}');
        batteryTypeCombotree.combotree({
            url: "${contextPath}/security/basic/agent_battery_type/tree.htm?agentId=" + agentId + ""
        });
        batteryTypeCombotree.combotree('reload');
        batteryTypeCombotree.combotree('setValue', null);
    }

    function swich_battery_type_foregift_${pid}() {

          if ($('#battery_type_${pid}').combotree('getValue') != null) {
            //VIP押金
            $.post('${contextPath}/security/zd/vip_rent_battery_foregift/vip_rent_battery_foregift.htm', {
                batteryType: $('#battery_type_${pid}').combotree('getValue'),
                agentId: $('#agent_id_${pid}').combotree('getValue')
            }, function (html) {
                $("#exchange_battery_foregift").html(html);
            }, 'html');
          }
    }

    (function () {

        var win = $('#${pid}'), form = win.find('form');
        windowData = win.data('windowData');
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

        win.find('button.vip_ok').click(function () {
            if (!form.form('validate')) {
                return false;
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

            form.form('submit', {
                url: '${contextPath}/security/zd/vip_rent_price/create.htm',
                onSubmit: function(param) {
                    param.beginTime = beginTime + " 00:00:00";
                    param.endTime = endTime + " 23:59:59";
                    return true;
                },
                success: function (text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if (json.success) {
                        windowData.ok(json.data);
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.vip_close').click(function () {
            win.window('close');
        });
    })();

</script>


