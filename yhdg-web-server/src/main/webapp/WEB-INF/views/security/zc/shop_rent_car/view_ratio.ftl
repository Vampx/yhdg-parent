<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="left" style="font-weight: 650;">分成管理&nbsp;&nbsp;&nbsp;</td>
                </tr>
                <tr>
                    <td width="70" align="left">*系统分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="zc_platform_ratio_${pid}" style="height: 28px;width: 185px;" name="zcPlatformRatio" readonly  data-options="min:0,max:100" value="${(entity.zcPlatformRatio)!''}"/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*运营商分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="zc_agent_ratio_${pid}" style="height: 28px;width: 185px;" name="zcAgentRatio" readonly  data-options="min:0,max:100" value="${(entity.zcAgentRatio)!''}"/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*省代分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="zc_province_agent_ratio_${pid}" style="height: 28px;width: 185px;" readonly name="zcProvinceAgentRatio"  data-options="min:0,max:100" value="${(entity.zcProvinceAgentRatio)!''}"/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*市代分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="zc_city_agent_ratio_${pid}" style="height: 28px;width: 185px;" readonly name="zcCityAgentRatio"  data-options="min:0,max:100" value="${(entity.zcCityAgentRatio)!''}"/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*门店分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" id="zc_shop_ratio_${pid}" style="height: 28px;width: 185px;" name="zcShopRatio" readonly  data-options="min:0,max:100" value="${(entity.zcShopRatio)!''}"/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*门店固定分成金额（元）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" readonly id="zc_shop_fixed_money_${pid}" style="height: 28px;width: 185px;" name="zcShopFixedMoney"  data-options="min:0.00,precision:2" <#if entity.zcShopFixedMoney ??>value="${(entity.zcShopFixedMoney/100)!''}"<#else> value="0"</#if> /></td>
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