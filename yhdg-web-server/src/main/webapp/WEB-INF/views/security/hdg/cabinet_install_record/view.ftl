<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">运营商：</td>
                    <td>
                        <input id="agent_id_${pid}" readonly="readonly" class="easyui-combotree" required="true"
                               editable="false" style="width: 196px; height: 30px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200'"
                               value="${(entity.agentId)!''}"/>
                    </td>
                    <td width="70" align="right">设备编号：</td>
                    <td>
                        <input type="text" class="text" readonly="readonly" style="width: 184px; height: 28px;" value="${(entity.cabinetId) !''}">
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">设备名称：</td>
                    <td>
                        <input type="text" class="text" readonly="readonly" style="width: 184px; height: 28px;" value="${(entity.cabinetName) !''}">
                    </td>
                    <td width="70" align="right">地址：</td>
                    <td>
                        <input type="text" class="text" readonly="readonly" style="width: 184px; height: 28px;" value="${(entity.address) !''}">
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">可换进电量：</td>
                    <td>
                        <input type="text" class="text" style="width: 184px; height: 28px;" readonly="readonly" value="${(entity.permitExchangeVolume) !0}">
                    </td>
                    <td width="70" align="right">满电电量：</td>
                    <td>
                        <input type="text" class="text" style="width: 184px; height: 28px;" readonly="readonly" value="${(entity.chargeFullVolume) !0}">
                    </td>
                </tr>
                <tr>
                    <td align="right">租金金额：</td>
                    <td><input type="text" class="text" readonly="readonly" style="width: 184px; height: 28px;" maxlength="10"
                            value="${(entity.rentMoney) !0}"/></td>
                    <td align="right">押金金额：</td>
                    <td>
                        <input type="text" class="text" readonly="readonly" style="width: 184px; height: 28px;" value="${(entity.foregiftMoney) !0}" maxlength="20"/>
                    </td>
                </tr>

                <tr>
                    <td width="80" align="right">租金截至：</td>
                    <td><input type="text" class="easyui-datebox" readonly="readonly" style="width: 196px; height: 30px;" value="<#if entity.rentExpireTime?? >${app.format_date_time(entity.rentExpireTime)}</#if>"/></td>
                    <td width="70" align="right">租金周期(月)：</td>
                    <td><input type="text" class="text easyui-numberspinner" data-options="min:0" maxlength="20" name="rentPeriodType" value="${(entity.rentPeriodType)!''}"
                               style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">电价：</td>
                    <td>
                        <input type="text" class="text" readonly="readonly" style="width: 184px; height: 28px;" value="${((entity.price) !0)}" maxlength="10"/>
                    </td>
                    <td align="right">审核状态：</td>
                    <td>
                        <select readonly="readonly" class="easyui-combobox" style="width: 196px;height: 30px;" editable="false">
                        <#list statusEnum as e>
                            <option value="${e.getValue()}" <#if entity.status?? && entity.status == e.getValue()>selected</#if>> ${(e.getName())!''}</option>
                        </#list>
                        </select>
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
        var pid = '${pid}',
                win = $('#' + pid);
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>
