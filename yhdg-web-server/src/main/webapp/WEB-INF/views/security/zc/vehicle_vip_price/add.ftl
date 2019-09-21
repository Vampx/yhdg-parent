<div class="popup_body" style="padding-left:50px;padding-top: 40px;font-size: 14px;">
    <div class="tab_item" style="display:block;min-height: 72%;">
        <div class="ui_table">
            <form method="post" action="javascript:void(0);">
                <input type="hidden" name="batteryType">
                <input type="hidden" name="modelId">
                <input type="hidden" name="category">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="90" align="left">运营商：</td>
                        <td>
                            <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                                   editable="false" style="width: 184px; height: 28px;"
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                        switch_agent();
                                    }"
                            >
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="left">套餐名称：</td>
                        <td><input type="text" class="text easyui-validatebox" maxlength="40" name="priceName" required="true" style="width:170px;height: 30px "/></td>
                    </tr>
                    <tr>
                        <td width="100" align="left">租车套餐设置：</td>
                        <td>
                            <input name="priceSettingId" id="price_setting_id_${pid}" class="easyui-combotree" editable="false"
                                   style="width: 187px; height: 28px;" required="true"
                                   data-options="url:'${contextPath}/security/zc/price_setting/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto',
                                onClick: function(node) {
                                   switch_price_setting();
                                }
                            "/>
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="left">租车套餐：</td>
                        <td>
                            <input name="rentPriceId" id="rent_price_id_${pid}" class="easyui-combotree" editable="false"
                                   style="width: 187px; height: 28px;" required="true"
                                   data-options="url:'${contextPath}/security/zc/rent_price/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto',
                                onClick: function(node) {
                                   switch_rent_price();
                                }
                            "/>
                        </td>
                    </tr>
                    <tr>
                        <td align="left"></td>
                        <td>
                            <fieldset>
                                <div class="popup_body">
                                    <div class="tab_item" style="display:block;">
                                        <div class="ui_table">
                                            <table cellpadding="0" cellspacing="0">
                                                <tbody class="table_list">
                                                <tr>
                                                    <td width="80" align="left">押金：</td>
                                                    <td><input type="text" class="text easyui-validatebox number" required="true" maxlength="10" id="foregift_price_${pid}"  style="width:170px;height: 30px "/> 元</td>
                                                </tr>
                                                <tr>
                                                    <td width="80" align="left"></td>
                                                    <td>车辆：<input type="text" class="text easyui-validatebox number" required="true" maxlength="10" id="vehicle_foregift_price_${pid}" style="width:150px;height: 30px "/> 元</td>
                                                    <td class="batteryInfo">电池：<input type="text" class="text easyui-validatebox number" required="true" maxlength="10" id="battery_foregift_price_${pid}" style="width:150px;height: 30px "/> 元</td>
                                                </tr>
                                                <tr>
                                                    <td width="80" align="left">租金：</td>
                                                    <td><input type="text" class="text easyui-validatebox number" required="true" maxlength="10" id="rent_price_${pid}" style="width:170px;height: 30px "/> 元</td>
                                                    <td><input type="text" class="text easyui-validatebox dayNumber" required="true" maxlength="3"  name="dayCount" style="width:170px;height: 30px "/> 天</td>
                                                </tr>
                                                <tr>
                                                    <td width="80" align="left"></td>
                                                    <td>车辆：<input type="text" class="text easyui-validatebox number" required="true" maxlength="10" id="vehicle_rent_price_${pid}" style="width:150px;height: 30px "/> 元</td>
                                                    <td class="batteryInfo">电池：<input type="text" class="text easyui-validatebox number" required="true" maxlength="10" id="battery_rent_price_${pid}" style="width:150px;height: 30px "/> 元</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                        </td>
                    </tr>
                    <tr>
                        <td align="left">是否启用：</td>
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
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    $("body").on("input",".number",function(){
        var reg = $(this).val().match(/\d+\.?\d{0,2}/);
        var txt = '';
        if (reg != null) {
            txt = reg[0];
        }
        $(this).val(txt);
    });

    $("body").on("input",".dayNumber",function(){
        var $val = $(this).val();
        $(this).val($val.replace(/[^\d]/g,''));
    });

    function switch_agent() {
        var agentId = $('#agent_id_${pid}').combotree("getValue");
        var priceSettingCombotree = $('#price_setting_id_${pid}');
        priceSettingCombotree.combotree({
            url: "${contextPath}/security/zc/price_setting/tree.htm?agentId=" + agentId + ""
        });
        priceSettingCombotree.combotree('reload');
        priceSettingCombotree.combotree('setValue', null);

        var rentPriceCombotree = $('#rent_price_id_${pid}');
        rentPriceCombotree.combotree('reload');
        rentPriceCombotree.combotree('setValue', null);
    }

    function switch_price_setting() {
        var priceSettingId = $('#price_setting_id_${pid}').combotree('getValue');
        var rentPriceCombotree = $('#rent_price_id_${pid}');
        rentPriceCombotree.combotree({
            url: "${contextPath}/security/zc/rent_price/tree.htm?priceSettingId=" + priceSettingId + ""
        });
        rentPriceCombotree.combotree('reload');
        rentPriceCombotree.combotree('setValue', null);
    }
    
    function switch_rent_price() {
        var win = $('#${pid}');
        var rentPriceId = $('#rent_price_id_${pid}').combotree('getValue');
        $.ajax({
            type: 'POST',
            url: '${contextPath}/security/zc/rent_price/find_by_id.htm',
            dataType: 'json',
            data: {rentPriceId: rentPriceId},
            success: function (json) {
                if (json.success) {
                    var rentPrice = json.data;
                    win.find('input[name=modelId]').val(rentPrice.modelId);
                    win.find('input[name=batteryType]').val(rentPrice.batteryType);
                    win.find('input[name=category]').val(rentPrice.category);
                    $('#foregift_price_${pid}').val(rentPrice.foregiftPrice / 100);
                    $('#vehicle_foregift_price_${pid}').val(rentPrice.vehicleForegiftPrice / 100);
                    $('#battery_foregift_price_${pid}').val(rentPrice.batteryForegiftPrice / 100);
                    $('#rent_price_${pid}').val(rentPrice.rentPrice / 100);
                    win.find('input[name=dayCount]').val(rentPrice.dayCount);
                    $('#vehicle_rent_price_${pid}').val(rentPrice.vehicleRentPrice / 100);
                    $('#battery_rent_price_${pid}').val(rentPrice.batteryRentPrice / 100);
                    if(rentPrice.category == 3) {
                        $('#battery_foregift_price_${pid}').val('');
                        $('#battery_rent_price_${pid}').val('');
                        $('#battery_foregift_price_${pid}').validatebox({required: false});
                        $('#battery_rent_price_${pid}').validatebox({required: false});
                        $(".batteryInfo").hide();
                    }else{
                        $('#battery_foregift_price_${pid}').validatebox({required: true});
                        $('#battery_rent_price_${pid}').validatebox({required: true});
                        $(".batteryInfo").show();
                    }
                }else {
                    $.messager.alert('提示信息', json.messager, 'info');
                }
            }
        });
    }


    $(function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function () {

            form.form('submit', {
                url: '${contextPath}/security/zc/vehicle_vip_price/create.htm',
                onSubmit: function(param) {
                    var isValid = form.form('validate');
                    if (!isValid) {
                        return false;
                    }

                    var foregiftPrice = $('#foregift_price_${pid}').val();
                    var vehicleForegiftPrice = $('#vehicle_foregift_price_${pid}').val();
                    var batteryForegiftPrice = $('#battery_foregift_price_${pid}').val();
                    var rentPrice = $('#rent_price_${pid}').val();
                    var vehicleRentPrice = $('#vehicle_rent_price_${pid}').val();
                    var batteryRentPrice = $('#battery_rent_price_${pid}').val();

                    param.foregiftPrice = parseInt(Math.round(foregiftPrice * 100));
                    param.vehicleForegiftPrice = parseInt(Math.round(vehicleForegiftPrice * 100));
                    param.batteryForegiftPrice = parseInt(Math.round(batteryForegiftPrice * 100));
                    param.rentPrice = parseInt(Math.round(rentPrice * 100));
                    param.vehicleRentPrice = parseInt(Math.round(vehicleRentPrice * 100));
                    param.batteryRentPrice = parseInt(Math.round(batteryRentPrice * 100));

                    return true;
                },
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
        win.find('button.close').click(function () {
            win.window('close');
        });

    })
</script>
