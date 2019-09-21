<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td><input type="text" class="text" maxlength="40" name="agentName" readonly
                               value="${(entity.agentName)!''}"/>
                   <#-- <td align="right">电池类型：</td>
                    <td><input type="text" class="text" maxlength="40" name="batteryTypeName" readonly
                               value="${(entity.batteryTypeName)!''}"/>-->
                </tr>
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text" maxlength="40" name="fullname" readonly
                               value="${(entity.fullname)!''}"/>
                    </td>
                    <td align="right">手机号码：</td>
                    <td><input type="text" id="mobile" class="text" readonly name="mobile" maxlength="11"
                               value="${(entity.mobile)!''}"/></td>
                </tr>
                <tr>
                    <td width="140" align="right">押金金额（元）：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly style="width: 182px; height: 28px;"
                               data-options="precision:2,min:0.00" value="${((entity.foregiftMoney)!0)/100}"/></td>
                    <td width="140" align="right">租金金额（元）：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly style="width: 182px; height: 28px;"
                               data-options="precision:2,min:0.00" value="${((entity.packetMoney)!0)/100}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">手续费金额（元）：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly style="width: 182px; height: 28px;"
                               data-options="precision:2,min:0.00" value="${((entity.feeMoney)!0)/100}"/></td>
                    <td width="100" align="right">总金额（元）：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly style="width: 182px; height: 28px;"
                               id="total_money_${pid}" data-options="precision:2,min:0.00"
                               value="${(entity.totalMoney/100)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">状态：</td>
                    <td>
                        <select name="status" disabled class="easyui-combobox" style="width: 182px; height: 28px;">
                            <option value="">无</option>
                        <#list StatusEnum as e>
                            <option value="${e.getValue()}"
                                    <#if entity.status?? && (entity.status == e.value)>selected</#if>> ${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                    <td width="100" align="right">已支付金额：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly style="width: 182px; height: 28px;"
                               id="paid_money_${pid}" data-options="precision:2,min:0.00"
                               value="${(entity.paidMoney/100)!''}"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
