<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">客户姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly value="${(entity.fullname)!''}"/></td>
                    <td align="right">手机号：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly value="${(entity.mobile)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">支付类型：</td>
                    <td>
                        <select class="easyui-combobox" required="true" readonly  name="payType" style="width:182px;height:28px ">
                        <#list PayTypeEnum as s>
                            <option value="${s.getValue()}" <#if entity.payType?? && entity.payType == s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                    <td align="right">支付状态：</td>
                    <td>
                        <select name="status" readonly="readonly" class="easyui-combobox" style="width: 182px; height: 28px;">
                            <option value="">无</option>
                        <#list StatusEnum as e>
                            <option value="${e.getValue()}" <#if entity.status?? && (entity.status == e.value)>selected</#if>> ${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="100" align="right">本次押金金额（元）：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly style="width: 182px; height: 28px;"  data-options="precision:2,min:0.00" value="${((entity.foregiftMoney)!0)/100}"/></td>
                    <td width="100" align="right">本次租金金额（元）：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly style="width: 182px; height: 28px;" data-options="precision:2,min:0.00" value="${((entity.packetMoney)!0)/100}"/></td>
                </tr>
                <tr>
                    <td width="160" align="right">本次保险金额（元）：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly style="width: 182px; height: 28px;" data-options="precision:2,min:0.00" value="${((entity.insuranceMoney)!0)/100}"/></td>
                    <td width="180" align="right">本次支付总金额（元）：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly style="width: 182px; height: 28px;" data-options="precision:2,min:0.00" value="${((entity.money)!0)/100}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">分期总金额（元）：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly style="width: 182px; height: 28px;"  data-options="precision:2,min:0.00" value="${((entity.totalMoney)!0)/100}"/></td>
                    <td align="right">过期时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:182px;height:28px " readonly="readonly"
                               value="<#if (entity.expireTime)?? >${app.format_date_time(entity.expireTime)}</#if>" name="expireTime">
                    </td>
                </tr>
                <tr>
                    <td align="right">支付时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:182px;height:28px " readonly="readonly"
                               value="<#if (entity.payTime)?? >${app.format_date_time(entity.payTime)}</#if>" name="payTime">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>