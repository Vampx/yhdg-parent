<div class="popup_body">
    <div class="ui_table">
        <iframe name="upload_iframe" style="display: none;"></iframe>
        <form method="post" id="form_${pid}" action="" method="post" target="upload_iframe" enctype="multipart/form-data">
            <table cellpadding="0" cellspacing="0">
                <input type="hidden" name="priceSettingId" id="price_setting_id">
                <tr>
                    <td width="90" align="right">运营商：</td>
                    <td>
                        <input id="agent_id" class="easyui-combotree" required="true"
                               editable="false" style="width: 195px; height: 28px;" readonly
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                        swich_shop();
                                    }" value="${Session['SESSION_KEY_USER'].agentId}"
                        >
                    </td>
                </tr>
                <tr>
                    <td width="60" align="right">门店：</td>
                    <td>
                        <input name="shopId" id="shop_id" class="easyui-combotree" editable="false"
                               style="width: 195px;height: 28px;" required="true"
                               data-options="url:'${contextPath}/security/hdg/shop/vehicle_tree.htm?dummy=${'所有'?url}&agentId=${Session['SESSION_KEY_USER'].agentId}&isVehicle=1',
                            method:'get',
                            valueField:'id',
                            textField:'text',
                            editable:false,
                            multiple:false,
                            panelHeight:'200',
                            onClick: function(node) {
                                $('#setting_name').val('');
                            }">
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">套餐名称：</td>
                    <td>
                        <input name="settingName" readonly id="setting_name" class="text easyui-validatebox" style="height: 28px;width: 185px;"  required="true"/>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">选择文件：</td>
                    <td><input type="file" class="filler" name="file" id="file_${pid}"></td>
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
    function swich_shop() {
        var agentId = $('#agent_id').combotree('getValue');
        var shop = $('#shop_id');
        shop.combotree({
            url: "${contextPath}/security/hdg/shop/vehicle_tree.htm?agentId=" + agentId + "&isVehicle=1"
        });
        shop.combotree('reload');
    }

    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form'), windowData = win.data('windowData');

        function getFileSuffix(fileName) {
            var num = fileName.lastIndexOf('.');
            var suffix = '';
            if(num > -1) {
                suffix = fileName.toLowerCase().substring(num + 1, fileName.length);
            }
            return suffix.toLowerCase();
        }

        function uploadAttachment(val) {
            if(val == '') {
                $.messager.alert("提示信息", '请先选择文件');
                return false;
            }
            var suffix = getFileSuffix(val);
            if(suffix == 'xls') {
                return true;
            } else {
                $.messager.alert("提示信息", '上传文件必须20003 Excel');
                return false;
            }
        }

        win.find('button.ok').click(function() {
            var val = $('#file_${pid}').val();
            if(uploadAttachment(val)) {
                form.form('submit', {
                    url: '${contextPath}/security/zc/shop_store_vehicle/btch_import_store_vehicle.htm',
                    success: function(text) {
                        var json = $.evalJSON(text);
                    <@app.json_jump/>
                        if(json.success) {
                            $.messager.alert('提示信息', json.message, 'info');
                            win.window('close');
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                });
            }
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

        win.find('input[name=settingName]').click(function() {
            selectPriceSetting();
        });

        function selectPriceSetting() {
            var shopId = $('#shop_id').combotree('getValue');
            var agentId = $('#agent_id').combotree('getValue');
            if (agentId == "" || agentId == null) {
                $.messager.alert('提示信息',"请选择运营商");
                return false;
            }
            if (shopId == "" || shopId == null) {
                $.messager.alert('提示信息',"请选择门店");
            }else {
                App.dialog.show({
                    css: 'width:826px;height:522px;overflow:visible;',
                    title: '选择套餐',
                    href: "${contextPath}/security/zc/shop_price_setting/select_shop_price_setting.htm?shopId="+shopId,
                    event: {
                        onClose: function() {
                            $('#vin_no').val("");
                        }
                    }
                });
            }
        }
    })();
</script>