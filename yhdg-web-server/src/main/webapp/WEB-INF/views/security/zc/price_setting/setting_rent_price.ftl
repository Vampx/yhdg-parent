<#if rentPriceList?? && (rentPriceList?size>0) >
    <#list rentPriceList as price>
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
                                        <td width="80" align="left">套餐名称：</td>
                                        <td><input type="hidden" name="priceId" value="${(price.id)!0}"><input type="text" class="text easyui-validatebox" maxlength="40" value="${(price.priceName)!''}" name="priceName" style="width:170px;height: 30px "/></td>
                                    </tr>
                                    <tr>
                                        <td width="80" align="left">押金：</td>
                                        <td><input type="text" class="text easyui-validatebox number" maxlength="10" value="${(price.foregiftPrice/100)?string('0.00')}" name="foregiftPrice"  style="width:170px;height: 30px "/> 元</td>
                                    </tr>
                                    <tr>
                                        <td width="80" align="left"></td>
                                        <td>车辆：<input type="text" class="text easyui-validatebox number" maxlength="10" value="${(price.vehicleForegiftPrice/100)?string('0.00')}" name="vehicleForegiftPrice" style="width:150px;height: 30px "/> 元</td>
                                        <td class="battery_foregift_price">电池：<input type="text" class="text easyui-validatebox number" maxlength="10" value="<#if price.batteryForegiftPrice??>${(price.batteryForegiftPrice/100)?string('0.00')}</#if>" name="batteryForegiftPrice" style="width:150px;height: 30px "/> 元</td>
                                    </tr>
                                    <tr>
                                        <td width="80" align="left">租金：</td>
                                        <td><input type="text" class="text easyui-validatebox number" maxlength="10" value="${(price.rentPrice/100)?string('0.00')}"  name="rentPrice" style="width:170px;height: 30px "/> 元</td>
                                        <td><input type="text" class="text easyui-validatebox dayNumber" maxlength="3"  value="${(price.dayCount)}"  name="dayCount" style="width:170px;height: 30px "/> 天</td>
                                    </tr>
                                    <tr>
                                        <td width="80" align="left"></td>
                                        <td>车辆：<input type="text" class="text easyui-validatebox number" maxlength="10" value="${(price.vehicleRentPrice/100)?string('0.00')}"  name="vehicleRentPrice" style="width:150px;height: 30px "/> 元</td>
                                        <td class="battery_foregift_price">电池：<input type="text" class="text easyui-validatebox number" maxlength="10" value="<#if price.batteryRentPrice??>${(price.batteryRentPrice/100)?string('0.00')}</#if>"  name="batteryRentPrice" style="width:150px;height: 30px "/> 元</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </fieldset>
        </td>
        <#if price_index != 0 >
            <td><button class="btn btn_border btn-minus" price_id = ${(price.id)!0} style="width: 100px;height: 35px; background:red; color:#fff;">删除</button></td>
        </#if>
        </tr>
    </#list>
<#else>
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
                                <td width="80" align="left">套餐名称：</td>
                                <td><input type="hidden" name="priceId" value="0"><input type="text" class="text easyui-validatebox" maxlength="40" name="priceName" style="width:170px;height: 30px "/></td>
                            </tr>
                            <tr>
                                <td width="80" align="left">押金：</td>
                                <td><input type="text" class="text easyui-validatebox number" maxlength="10" name="foregiftPrice"  style="width:170px;height: 30px "/> 元</td>
                            </tr>
                            <tr>
                                <td width="80" align="left"></td>
                                <td>车辆：<input type="text" class="text easyui-validatebox number" maxlength="10" name="vehicleForegiftPrice" style="width:150px;height: 30px "/> 元</td>
                                <td class="battery_foregift_price">电池：<input type="text" class="text easyui-validatebox number" maxlength="10" name="batteryForegiftPrice" style="width:150px;height: 30px "/> 元</td>
                            </tr>
                            <tr>
                                <td width="80" align="left">租金：</td>
                                <td><input type="text" class="text easyui-validatebox number" maxlength="10" name="rentPrice" style="width:170px;height: 30px "/> 元</td>
                                <td><input type="text" class="text easyui-validatebox dayNumber" maxlength="3"  name="dayCount" style="width:170px;height: 30px "/> 天</td>
                            </tr>
                            <tr>
                                <td width="80" align="left"></td>
                                <td>车辆：<input type="text" class="text easyui-validatebox number" maxlength="10" name="vehicleRentPrice" style="width:150px;height: 30px "/> 元</td>
                                <td class="battery_foregift_price">电池：<input type="text" class="text easyui-validatebox number" maxlength="10" name="batteryRentPrice" style="width:150px;height: 30px "/> 元</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </fieldset>
    </td>
</tr>
</#if>

<script>
    var category =  ${(category)!0};
    if (category == 3) {
        $(".battery_foregift_price").hide();
    }

</script>