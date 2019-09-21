<div class="popup_body">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="90" align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                               editable="false" style="width: 195px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                        swich_shop_${pid}();
                                    }"
                        >
                    </td>
                </tr>
                <tr>
                    <td width="60" align="right">门店：</td>
                    <td>
                        <input name="shopId" id="shop_id_${pid}" class="easyui-combotree" editable="false"
                               style="width: 195px;height: 28px;" required="true"
                               data-options="url:'${contextPath}/security/hdg/shop/vehicle_tree.htm?dummy=${'所有'?url}',
                            method:'get',
                            valueField:'id',
                            textField:'text',
                            editable:false,
                            multiple:false,
                            panelHeight:'200',
                            onClick: function(node) {
                                $('#price_setting_name').val('');
                            }">
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">套餐：</td>
                    <td>
                        <input name="priceSettingName" readonly id="price_setting_name" class="text easyui-validatebox" style="height: 28px;width: 185px;"  required="true"/>
                        <input name="priceSettingId" type="hidden" id="price_setting_id"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn"  style="margin-right: 5px;">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    function swich_shop_${pid}() {
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var shop = $('#shop_id_${pid}');
        shop.combotree({
            url: "${contextPath}/security/hdg/shop/vehicle_tree.htm?agentId=" + agentId + "&isVehicle=1"
        });
        shop.combotree('reload');
    }

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/zc/shop_price_setting/create.htm',
                success: function(text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });


        win.find('input[name=priceSettingName]').click(function() {
            selectPriceSetting();
        });

        function selectPriceSetting() {
            var agentId = $('#agent_id_${pid}').combotree('getValue');
            if (agentId == "" || agentId == null) {
                $.messager.alert('提示信息',"请选择运营商");
            }else {
                App.dialog.show({
                    css: 'width:826px;height:522px;overflow:visible;',
                    title: '选择套餐',
                    href: "${contextPath}/security/zc/price_setting/show_price_setting.htm?agentId="+agentId,
                    event: {
                        onClose: function() {
                        }
                    }
                });
            }
        }

    })();
</script>
