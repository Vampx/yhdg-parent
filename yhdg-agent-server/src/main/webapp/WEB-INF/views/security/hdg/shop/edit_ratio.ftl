<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <input type="hidden" id="db_shop_fixed_money_${pid}" <#if entity.shopFixedMoney ??> value="${(entity.shopFixedMoney/100)!''}"<#else> value="0"</#if> >
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="left" style="font-weight: 650;">分成管理&nbsp;&nbsp;&nbsp;</td>
                </tr>
                <tr>
                    <td width="70" align="left">*系统分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="platform_ratio_${pid}" disabled style="height: 28px;width: 185px;" name="platformRatio" required="true"  data-options="min:0,max:100" <#if entity.platformRatio ??>value="${(entity.platformRatio)!''}"<#else >value="0"</#if>/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*运营商分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="agent_ratio_${pid}" style="height: 28px;width: 185px;" name="agentRatio" required="true"  data-options="min:0,max:100" <#if entity.agentRatio ??>value="${(entity.agentRatio)!''}"<#else >value="100"</#if>/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*省代分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="province_agent_ratio_${pid}" style="height: 28px;width: 185px;" name="provinceAgentRatio" required="true"  data-options="min:0,max:100" <#if entity.provinceAgentRatio ??>value="${(entity.provinceAgentRatio)!''}"<#else >value="0"</#if>/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*市代分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="city_agent_ratio_${pid}" style="height: 28px;width: 185px;" name="cityAgentRatio" required="true"  data-options="min:0,max:100" <#if entity.cityAgentRatio ??>value="${(entity.cityAgentRatio)!''}"<#else >value="0"</#if>/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*门店分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="shop_ratio_${pid}" style="height: 28px;width: 185px;" name="shopRatio" required="true"  data-options="min:0,max:100" <#if entity.shopRatio ??>value="${(entity.shopRatio)!''}"<#else > value="0"</#if>/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*门店固定分成金额（元）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="shop_fixed_money_${pid}" style="height: 28px;width: 185px;" name="shopFixedMoney" required="true"  data-options="min:0.00,precision:2" <#if entity.shopFixedMoney ??>value="${(entity.shopFixedMoney/100)!''}"<#else> value="0"</#if> /></td>
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
        var dbShopFixedMoney = $('#db_shop_fixed_money_${pid}').val();
        var dbShopFixedMoneyValue = parseInt(Math.round(dbShopFixedMoney * 100));

        var snapshot = $.toJSON({
            id: '${entity.id}',
            platformRatio: '${(entity.platformRatio)!''}',
            agentRatio: '${(entity.agentRatio)!''}',
            provinceAgentRatio: '${(entity.provinceAgentRatio)!''}',
            cityAgentRatio: '${(entity.cityAgentRatio)!''}',
            shopRatio: '${(entity.shopRatio)!''}',
            shopFixedMoney: dbShopFixedMoneyValue
        });

        var ok = function () {
            if (!jform.form('validate')) {
                return false;
            }
            var platformRatio = form.platformRatio.value;
            var agentRatio = form.agentRatio.value;
            var provinceAgentRatio = form.provinceAgentRatio.value;
            var cityAgentRatio = form.cityAgentRatio.value;
            var shopRatio = form.shopRatio.value;
            var shopFixedMoney = $('#shop_fixed_money_${pid}').numberspinner('getValue');
            var shopFixedMoneyValue = parseInt(Math.round(shopFixedMoney * 100));

            var platformRatioNumber = Number($('#platform_ratio_${pid}').numberspinner('getValue'));
            var agentRatioNumber = Number($('#agent_ratio_${pid}').numberspinner('getValue'));
            var provinceAgentRatioNumber = Number($('#province_agent_ratio_${pid}').numberspinner('getValue'));
            var cityAgentRatioNumber = Number($('#city_agent_ratio_${pid}').numberspinner('getValue'));
            var shopRatioNumber = Number($('#shop_ratio_${pid}').numberspinner('getValue'));

            if(platformRatioNumber+agentRatioNumber+provinceAgentRatioNumber+cityAgentRatioNumber+shopRatioNumber != 100) {
                $.messager.alert('提示信息', '分成比例总和必须为100%', 'info');
                return;
            }

            var success = true;
            var values = {
                id: '${entity.id}',
                platformRatio: platformRatio,
                agentRatio: agentRatio,
                provinceAgentRatio: provinceAgentRatio,
                cityAgentRatio: cityAgentRatio,
                shopRatio: shopRatio,
                shopFixedMoney: shopFixedMoneyValue
            };

            if (snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/hdg/shop/update_ratio.htm',
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