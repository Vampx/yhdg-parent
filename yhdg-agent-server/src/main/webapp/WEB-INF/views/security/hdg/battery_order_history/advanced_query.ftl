<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td>
                        <input id="agent_id_${pid}" class="easyui-combotree" editable="false"
                               style="width: 180px;height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                   reloadTree();
                                }
                            "
                                >
                    </td>
                </tr>
                <tr>
                    <td align="right" width="115">
                        <select style="width:100px;" id="query_name_${pid}">
                            <option value="id">订单编号</option>
                            <option value="takeCabinetId">取电柜子编号</option>
                            <option value="putCabinetId">放电柜子编号</option>
                            <option value="batteryId">电池编号</option>
                            <option value="customerFullname">客户名称</option>
                            <option value="customerMobile">客户手机</option>
                        </select>
                    </td>
                    <td><input type="text" class="text" id="query_value_${pid}" style="width: 170px;"/></td>
                </tr>
                <tr>
                    <td align="right" width="70">支付方式：</td>
                    <td>
                        <select style="width:80px;" id="pay_type_${pid}">
                            <option value="">所有</option>
                        <#list PayTypeEnum as e>
                            <option value="${e.getValue()}">${e.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">取电开始/结束日期：</td>
                    <td>
                        <input id="take_begin_time_${pid}" class="easyui-datetimebox"
                               type="text"
                               style="width:150px;height:27px;">
                        -
                        <input id="take_end_time_${pid}" class="easyui-datetimebox" type="text"
                               style="width:150px;height:27px;">
                    </td>
                </tr>
                <tr>
                    <td align="right">放电开始/结束日期：</td>
                    <td>
                        <input id="put_begin_time_${pid}" class="easyui-datetimebox"
                               type="text"
                               style="width:150px;height:27px;">
                        -
                        <input id="put_end_time_${pid}" class="easyui-datetimebox" type="text"
                               style="width:150px;height:27px;">
                    </td>
                </tr>
                <tr>
                    <td align="right">创建开始/结束日期：</td>
                    <td>
                        <input id="begin_time_${pid}" class="easyui-datetimebox"
                               type="text"
                               style="width:150px;height:27px;">
                        -
                        <input id="end_time_${pid}" class="easyui-datetimebox" type="text"
                               style="width:150px;height:27px;">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    $(function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');
        $('#query_value_${pid}').val($('#query_value').val());
        $('#query_name_${pid}').val($('#query_name').val())
        $('#pay_type_${pid}').val($('#pay_type').val());
        setTimeout(function () {
            if ($('#agent_id').combotree('getValue') != '') {
                $('#agent_id_${pid}').combotree('setValues', $('#agent_id').combotree('getValue'));
            }
            $('#begin_time_${pid}').datetimebox('setValue', $('#begin_time').val());
            $('#end_time_${pid}').datetimebox('setValue', $('#end_time').val());
            $('#take_begin_time_${pid}').datetimebox('setValue', $('#take_begin_time').val());
            $('#take_end_time_${pid}').datetimebox('setValue', $('#take_end_time').val());
            $('#put_begin_time_${pid}').datetimebox('setValue', $('#put_begin_time').val());
            $('#put_end_time_${pid}').datetimebox('setValue', $('#put_end_time').val());
        }, 100);
        win.find('button.ok').click(function () {
            if ($('#agent_id_${pid}').combotree('getValue') != '') {
                $('#agent_id').combotree('setValues', $('#agent_id_${pid}').combotree('getValue'))
            }
            $('#query_value').val($('#query_value_${pid}').val())
            $('#query_name').val($('#query_name_${pid}').val())
            $('#pay_type').val($('#pay_type_${pid}').val())
            $('#begin_time').val($('#begin_time_${pid}').datetimebox('getValue'));
            $('#end_time').val($('#end_time_${pid}').datetimebox('getValue'));
            $('#take_begin_time').val($('#take_begin_time_${pid}').datetimebox('getValue'));
            $('#take_end_time').val($('#take_end_time_${pid}').datetimebox('getValue'));
            $('#put_begin_time').val($('#put_begin_time_${pid}').datetimebox('getValue'));
            $('#put_end_time').val($('#put_end_time_${pid}').datetimebox('getValue'));
            query();
            win.window('close');
        });
        win.find('button.close').click(function () {
            win.window('close');
        });

    });

</script>
