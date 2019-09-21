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
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" style="height: 28px;width: 185px;" readonly name="platformRatio"  data-options="min:0,max:100" value="${(entity.platformRatio)!''}"/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*运营商分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" style="height: 28px;width: 185px;" readonly name="agentRatio" data-options="min:0,max:100" value="${(entity.agentRatio)!''}"/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*省代分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" style="height: 28px;width: 185px;" readonly name="provinceAgentRatio"  data-options="min:0,max:100" value="${(entity.provinceAgentRatio)!''}"/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*市代分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" style="height: 28px;width: 185px;" readonly name="cityAgentRatio"  data-options="min:0,max:100" value="${(entity.cityAgentRatio)!''}"/></td>
                </tr>
            <#--    <tr>
                    <td width="140" align="left">*门店分成（%）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" style="height: 28px;width: 185px;" readonly name="shopRatio"  data-options="min:0,max:100" value="${(entity.shopRatio)!''}"/></td>
                </tr>
                <tr>
                    <td width="140" align="left">*门店固定分成金额（元）：</td>
                    <td><input type="text"  maxlength="3" class="easyui-numberspinner" readonly id="shop_fixed_money_${pid}" style="height: 28px;width: 185px;" name="shopFixedMoney"  data-options="min:0.00,precision:2" <#if entity.shopFixedMoney ??>value="${(entity.shopFixedMoney/100)!''}"<#else> value="0"</#if> /></td>
                </tr>-->
            </table>
        </form>
    </div>
</div>
<script>

    (function () {
        var win = $('#${pid}');

        var ok = function () {
            return true;
        };

        win.data('ok', ok);
    })();
</script>