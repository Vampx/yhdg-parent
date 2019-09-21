<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <input type="hidden" id="zc_db_shop_fixed_money_${pid}" <#if entity.zcShopFixedMoney ??> value="${(entity.zcShopFixedMoney/100)!''}"<#else> value="0"</#if> >
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="left" style="font-weight: 650;">分成管理&nbsp;&nbsp;&nbsp;</td>
                </tr>
                <tr>
                    <td width="70" align="left">*系统分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="zc_platform_ratio_${pid}" style="height: 28px;width: 185px;" name="zcPlatformRatio" required="true"  data-options="min:0,max:100" <#if entity.zcPlatformRatio ??>value="${(entity.zcPlatformRatio)!''}"<#else >value="0"</#if>/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*运营商分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="zc_agent_ratio_${pid}" style="height: 28px;width: 185px;" name="zcAgentRatio" required="true"  data-options="min:0,max:100" <#if entity.zcAgentRatio ??>value="${(entity.zcAgentRatio)!''}"<#else >value="100"</#if>/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*省代分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="zc_province_agent_ratio_${pid}" style="height: 28px;width: 185px;" name="zcProvinceAgentRatio" required="true"  data-options="min:0,max:100" <#if entity.zcProvinceAgentRatio ??>value="${(entity.zcProvinceAgentRatio)!''}"<#else >value="0"</#if>/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*市代分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="zc_city_agent_ratio_${pid}" style="height: 28px;width: 185px;" name="zcCityAgentRatio" required="true"  data-options="min:0,max:100" <#if entity.zcCityAgentRatio ??>value="${(entity.zcCityAgentRatio)!''}"<#else >value="0"</#if>/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*门店分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="zc_shop_ratio_${pid}" style="height: 28px;width: 185px;" name="zcShopRatio" required="true"  data-options="min:0,max:100" <#if entity.zcShopRatio ??>value="${(entity.zcShopRatio)!''}"<#else > value="0"</#if>/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*门店固定分成金额（元）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="zc_shop_fixed_money_${pid}" style="height: 28px;width: 185px;" name="zcShopFixedMoney" required="true"  data-options="min:0.00,precision:2" <#if entity.zcShopFixedMoney ??>value="${(entity.zcShopFixedMoney/100)!''}"<#else> value="0"</#if> /></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>

    (function () {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];
        var dbShopFixedMoney = $('#zc_db_shop_fixed_money_${pid}').val();
        var dbShopFixedMoneyValue = parseInt(Math.round(dbShopFixedMoney * 100));

        var snapshot = $.toJSON({
            id: '${entity.id}',
            zcPlatformRatio: '${(entity.zcPlatformRatio)!''}',
            zcAgentRatio: '${(entity.zcAgentRatio)!''}',
            zcProvinceAgentRatio: '${(entity.zcProvinceAgentRatio)!''}',
            zcCityAgentRatio: '${(entity.zcCityAgentRatio)!''}',
            zcShopRatio: '${(entity.zcShopRatio)!''}',
            zcShopFixedMoney: dbShopFixedMoneyValue
        });

        var ok = function () {
            if (!jform.form('validate')) {
                return false;
            }
            var platformRatio = form.zcPlatformRatio.value;
            var agentRatio = form.zcAgentRatio.value;
            var provinceAgentRatio = form.zcProvinceAgentRatio.value;
            var cityAgentRatio = form.zcCityAgentRatio.value;
            var shopRatio = form.zcShopRatio.value;
            var shopFixedMoney = $('#zc_shop_fixed_money_${pid}').numberspinner('getValue');
            var shopFixedMoneyValue = parseInt(Math.round(shopFixedMoney * 100));

            var platformRatioNumber = Number($('#zc_platform_ratio_${pid}').numberspinner('getValue'));
            var agentRatioNumber = Number($('#zc_agent_ratio_${pid}').numberspinner('getValue'));
            var provinceAgentRatioNumber = Number($('#zc_province_agent_ratio_${pid}').numberspinner('getValue'));
            var cityAgentRatioNumber = Number($('#zc_city_agent_ratio_${pid}').numberspinner('getValue'));
            var shopRatioNumber = Number($('#zc_shop_ratio_${pid}').numberspinner('getValue'));

            if(platformRatioNumber+agentRatioNumber+provinceAgentRatioNumber+cityAgentRatioNumber+shopRatioNumber != 100) {
                $.messager.alert('提示信息', '分成比例总和必须为100%', 'info');
                return;
            }

            var success = true;
            var values = {
                id: '${entity.id}',
                zcPlatformRatio: platformRatio,
                zcAgentRatio: agentRatio,
                zcProvinceAgentRatio: provinceAgentRatio,
                zcCityAgentRatio: cityAgentRatio,
                zcShopRatio: shopRatio,
                zcSshopFixedMoney: shopFixedMoneyValue
            };

            if (snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/zc/shop_rent_car/update_ratio.htm',
                    dataType: 'json',
                    data: values,
                    success: function (json) {
                    <@app.json_jump/>
                        if (json.success) {
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                            success = false;
                        }
                    }
                });
            }

            return success;
        };

        win.data('ok', ok);
    })();
</script>