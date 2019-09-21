<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <input type="hidden" id="db_company_fixed_money_${pid}" <#if entity.companyFixedMoney ??> value="${(entity.companyFixedMoney/100)!''}"<#else> value="0"</#if> >
            <input type="hidden" id="db_ratio_base_money_${pid}" <#if entity.ratioBaseMoney ??> value="${(entity.ratioBaseMoney/100)!''}"<#else> value="0"</#if> >
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="left" style="font-weight: 650;">分成管理&nbsp;&nbsp;&nbsp;</td>
                </tr>
                <tr>
                    <td width="140" align="left">*运营公司分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="company_ratio_${pid}" style="height: 28px;width: 185px;" name="companyRatio" required="true"  data-options="min:0,max:100" <#if entity.companyRatio ??>value="${(entity.companyRatio)!''}"<#else > value="0"</#if>/></td>
                </tr>
                <tr>
                    <td width="180" align="left">*运营公司固定分成金额（元）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="company_fixed_money_${pid}" style="height: 28px;width: 185px;" name="companyFixedMoney" required="true"  data-options="min:0.00,precision:2" <#if entity.companyFixedMoney ??>value="${(entity.companyFixedMoney/100)!''}"<#else> value="0"</#if> /></td>
                </tr>
                <tr>
                    <td width="180" align="left">*运营公司分成下限金额（元）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="ratio_base_money_${pid}" style="height: 28px;width: 185px;" name="ratioBaseMoney" required="true"  data-options="min:0.00,precision:2" <#if entity.ratioBaseMoney ??>value="${(entity.ratioBaseMoney/100)!''}"<#else> value="0"</#if> /></td>
                </tr>
                <tr>
                    <td align="left">*是否给门店分成：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="keepShopRatio" id="keep_shop_ratio_1"
                                   <#if entity.keepShopRatio?? && entity.keepShopRatio == 1>checked</#if> value="1"/><label for="keep_shop_ratio_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="keepShopRatio" id="keep_shop_ratio_0"
                                   <#if entity.keepShopRatio?? && entity.keepShopRatio == 0>checked</#if> value="0" /><label for="keep_shop_ratio_0">否</label>
                        </span>
                    </td>
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
        var dbCompanyFixedMoney = $('#db_company_fixed_money_${pid}').val();
        var dbCompanyFixedMoneyValue = parseInt(Math.round(dbCompanyFixedMoney * 100));
        var dbRatioBaseMoney = $('#db_ratio_base_money_${pid}').val();
        var dbRatioBaseMoneyValue = parseInt(Math.round(dbRatioBaseMoney * 100));

        var snapshot = $.toJSON({
            id: '${entity.id}',
            companyRatio: '${(entity.companyRatio)!''}',
            keepShopRatio: '${(entity.keepShopRatio)!''}',
            companyFixedMoney: dbCompanyFixedMoneyValue,
            ratioBaseMoney: dbRatioBaseMoneyValue
        });

        var ok = function () {
            if (!jform.form('validate')) {
                return false;
            }
            var companyRatio = form.companyRatio.value;
            var keepShopRatio = form.keepShopRatio.value;
            var companyFixedMoney = $('#company_fixed_money_${pid}').numberspinner('getValue');
            var companyFixedMoneyValue = parseInt(Math.round(companyFixedMoney * 100));
            var ratioBaseMoney = $('#ratio_base_money_${pid}').numberspinner('getValue');
            var ratioBaseMoneyValue = parseInt(Math.round(ratioBaseMoney * 100));

            var success = true;
            var values = {
                id: '${entity.id}',
                companyRatio: companyRatio,
                keepShopRatio: keepShopRatio,
                companyFixedMoney: companyFixedMoneyValue,
                ratioBaseMoney: ratioBaseMoneyValue
            };

            if (snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/basic/agent_company/update_ratio.htm',
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