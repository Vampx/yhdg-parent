<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="left" style="font-weight: 650;">分成管理&nbsp;&nbsp;&nbsp;</td>
                </tr>
                <tr>
                    <td width="140" align="left">*运营公司分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="company_ratio_${pid}" style="height: 28px;width: 185px;" name="companyRatio" readonly  data-options="min:0,max:100" value="${(entity.companyRatio)!''}"/></td>
                </tr>
                <tr>
                    <td width="180" align="left">*运营公司固定分成金额（元）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" readonly id="company_fixed_money_${pid}" style="height: 28px;width: 185px;" name="companyFixedMoney"  data-options="min:0.00,precision:2" <#if entity.companyFixedMoney ??>value="${(entity.companyFixedMoney/100)!''}"<#else> value="0"</#if> /></td>
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

    (function() {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        var ok = function() {
            var success = true;
            return success;
        };

        win.data('ok', ok);
    })();
</script>