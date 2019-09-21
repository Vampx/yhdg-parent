<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="left" width="160">押金金额（元）：</td>
                    <td><input type="text" class="text easyui-numberspinner" data-options="min:0" maxlength="20" name="foregiftMoney" value="${((entity.foregiftMoney)!0)/100}"
                               style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="90" align="left">租金周期（月）：</td>
                    <td><input type="text" class="text easyui-numberspinner" data-options="min:0" maxlength="20" name="rentPeriodType" value="${(entity.rentPeriodType)!''}"
                               style="width: 184px; height: 28px;"/></td>
                    <td align="left">租金金额（元）：</td>
                    <td><input type="text" class="text easyui-numberspinner" data-options="min:0" maxlength="20" name="rentMoney" value="${((entity.rentMoney)!0)/100}"
                               style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="140" align="left">租金收取时间：</td>
                    <td><input type="text" name="rentExpireTime" class="easyui-datetimebox" editable="false" style="width: 184px; height: 28px;" value="<#if entity.rentExpireTime?? >${app.format_date_time(entity.rentExpireTime)}</#if>"/></td>
                </tr>
                <tr>
                    <td width="80" align="left">启用扣除金额：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="activePlatformDeduct"  id="activePlatformDeduct_1" <#if entity.activePlatformDeduct?? && entity.activePlatformDeduct == 1>checked</#if>  value="1"/><label for="activePlatformDeduct_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="activePlatformDeduct" id="activePlatformDeduct_0"  <#if entity.activePlatformDeduct?? && entity.activePlatformDeduct == 1><#else >checked</#if>  value="0"/><label for="activePlatformDeduct_0">禁用</label>
                        </span>
                    </td>
                    <td width="120" align="left">运营商扣除：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" maxlength="3" name="platformDeductMoney" value="${((entity.platformDeductMoney)!0)/100}" />
                    </td>
                </tr>
                <tr>
                    <td width="80" align="left">运营商扣除过期时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" editable="false" style="width:185px;height:28px " id="begin_time_${pid}" name="platformDeductExpireTime"  value="${(entity.platformDeductExpireTime?string('yyyy-MM-dd HH:mm:ss'))!''}" ></td>
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

        var ok = function () {
            var success = true;
            var foregiftMoneyNum = form.foregiftMoney.value;
            var foregiftMoney = parseInt(Math.round(foregiftMoneyNum * 100));
            var rentMoneyNum = form.rentMoney.value;
            var rentMoney = parseInt(Math.round(rentMoneyNum * 100));
            var platformDeductMoneyNum = form.platformDeductMoney.value;
            var platformDeductMoney = parseInt(Math.round(platformDeductMoneyNum * 100));
            var values = {
                id: '${entity.id}',
                foregiftMoney: foregiftMoney,
                rentMoney: rentMoney,
                rentPeriodType: form.rentPeriodType.value,
                rentExpireTime: form.rentExpireTime.value,
                activePlatformDeduct: form.activePlatformDeduct.value,
                platformDeductMoney: platformDeductMoney,
                platformDeductExpireTime: form.platformDeductExpireTime.value
            };

            $.ajax({
                cache: false,
                async: false,
                type: 'POST',
                url: '${contextPath}/security/hdg/cabinet/update_online.htm',
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
            return success;
        };

        win.data('ok', ok);
    })();
</script>