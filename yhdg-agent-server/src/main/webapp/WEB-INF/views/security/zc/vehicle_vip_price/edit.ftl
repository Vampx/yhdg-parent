<div class="popup_body" style="padding-left:50px;padding-top: 40px;font-size: 14px;">
    <div class="tab_item" style="display:block;">
        <div class="ui_table">
            <form method="post" action="javascript:void(0);">
                <input type="hidden" name="id" value="${(entity.id)!''}">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="90" align="left">运营商：</td>
                        <td>
                            <input name="agentId" id="agent_id" class="easyui-combotree" required="true"
                                   editable="false" style="width: 184px; height: 28px;"  value="${(entity.agentId)!''}" disabled
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200' "
                            >
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="left">套餐名称：</td>
                        <td><input type="text" class="text easyui-validatebox" maxlength="40" name="priceName" value="${(entity.priceName)}" required="true" style="width:170px;height: 30px "/></td>
                    </tr>
                    <tr>
                        <td width="100" align="left">租车套餐设置：</td>
                        <td>
                            <input name="priceSettingId" id="price_setting_id" class="easyui-combotree" editable="false"
                                   style="width: 187px; height: 28px;" required="true" value="${(entity.priceSettingId)!''}" disabled
                                   data-options="url:'${contextPath}/security/zc/price_setting/tree.htm?agentId=${(entity.agentId)!''}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto' "/>
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="left">租车套餐：</td>
                        <td>
                            <input name="rentPriceId" id="rent_price_id" class="easyui-combotree" editable="false"
                                   style="width: 187px; height: 28px;" required="true" value="${(entity.rentPriceId)!''}" disabled
                                   data-options="url:'${contextPath}/security/zc/rent_price/tree.htm?priceSettingId=${(entity.priceSettingId)!''}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto' "/>
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
                                                    <td><input type="text" class="easyui-numberbox" required="true" data-options="precision:2" maxlength="10" id="foregift_price_${pid}" value="${(entity.foregiftPrice)/100}"  style="width:170px;height: 30px "/> 元</td>
                                                </tr>
                                                <tr>
                                                    <td width="80" align="left"></td>
                                                    <td>车辆：<input type="text" class="easyui-numberbox" required="true" data-options="precision:2" maxlength="10" id="vehicle_foregift_price_${pid}" value="${(entity.vehicleForegiftPrice)/100}" style="width:150px;height: 30px "/> 元</td>
                                                    <td class="batteryInfo">电池：<input type="text" class="easyui-numberbox"  data-options="precision:2" maxlength="10" id="battery_foregift_price_${pid}" <#if entity.batteryForegiftPrice ?? >required="true" value="${(entity.batteryForegiftPrice)/100}"</#if> style="width:150px;height: 30px "/> 元</td>
                                                </tr>
                                                <tr>
                                                    <td width="80" align="left">租金：</td>
                                                    <td><input type="text" class="easyui-numberbox" required="true" data-options="precision:2" maxlength="10" id="rent_price_${pid}" value="${(entity.rentPrice)/100}" style="width:170px;height: 30px "/> 元</td>
                                                    <td><input type="text" class="easyui-numberbox" required="true"  maxlength="3"  name="dayCount" value="${(entity.dayCount)!''}" style="width:170px;height: 30px "/> 天</td>
                                                </tr>
                                                <tr>
                                                    <td width="80" align="left"></td>
                                                    <td>车辆：<input type="text" class="easyui-numberbox" required="true" data-options="precision:2" maxlength="10" id="vehicle_rent_price_${pid}" value="${(entity.vehicleRentPrice)/100}" style="width:150px;height: 30px "/> 元</td>
                                                    <td class="batteryInfo">电池：<input type="text" class="easyui-numberbox"  data-options="precision:2" maxlength="10" id="battery_rent_price_${pid}" <#if entity.batteryRentPrice ?? >required="true" value="${(entity.batteryRentPrice)/100}"</#if> style="width:150px;height: 30px "/> 元</td>
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
                            <input type="radio" class="radio" name="isActive" <#if entity.isActive?? && entity.isActive == 1>checked</#if>
                                   value="1"/><label for="is_active_1">启用</label>
                        </span>
                            <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" <#if entity.isActive?? && entity.isActive == 0>checked</#if>
                                   value="0"/><label for="is_active_0">禁用</label>
                        </span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">绑定骑手号码：</td>
                    </tr>
                    <tr id="vehicle_vip_price_customer">
                    <#include '../vehicle_vip_price_customer/vehicle_vip_price_customer.ftl'>
                    </tr>
                    <tr>
                        <td colspan="2">绑定门店：</td>
                    </tr>
                    <tr id="vehicle_vip_price_shop">
                    <#include '../vehicle_vip_price_shop/vehicle_vip_price_shop.ftl'>
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

    //绑定的门店
    $.post('${contextPath}/security/zc/vehicle_vip_price_shop/vehicle_vip_price_shop.htm', {
        priceId:${(entity.id)!0},
        agentId: ${(entity.agentId)!0}
    }, function (html) {
        $("#vehicle_vip_price_shop").html(html);
    }, 'html');

    //绑定的客户
    $.post('${contextPath}/security/zc/vehicle_vip_price_customer/vehicle_vip_price_customer.htm', {
        priceId:${(entity.id)!0},
        agentId: ${(entity.agentId)!0}
    }, function (html) {
        $("#vehicle_vip_price_customer").html(html);
    }, 'html');

    $(function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        var category =  ${(entity.category)!''};
        if (category == 3) {
            $(".batteryInfo").hide();
            $('#battery_foregift_price_${pid}').validatebox({required: false});
            $('#battery_rent_price_${pid}').validatebox({required: false});
        }

        win.find('button.ok').click(function () {

            form.form('submit', {
                url: '${contextPath}/security/zc/vehicle_vip_price/update.htm',
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
