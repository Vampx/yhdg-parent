<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">运营商：</td>
                    <td align="right">
                        <input name="agentId" id="agent_id_${pid}" readonly="readonly" class="easyui-combotree" required="true"
                               editable="false" style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                        swich_agent();
                                    }" value="${(entity.agentId)!''}"/>
                    </td>
                    <td align="right">押金金额（元）：</td>
                    <td>
                        <input type="text" class="easyui-numberspinner" readonly="readonly" style="width: 184px; height: 28px;" data-options="min:0.00, precision:2" name="foregiftMoney" value="${(entity.foregiftMoney/100) !'0'}" maxlength="20"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">租金金额（元）：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly="readonly" style="width: 184px; height: 28px;" data-options="min:0.00, precision:2" name="rentMoney" maxlength="10"
                               value="${(entity.rentMoney/100) !'0'}"/></td>
                    <td width="70" align="right">租金周期（月）：</td>
                    <td><input type="text" class="text easyui-numberspinner" data-options="min:0" maxlength="20" name="periodType" value="${(entity.periodType)!''}"
                               style="width: 184px; height: 28px;"/></td>
                </tr>

                <tr>
                    <td width="80" align="right">租金截至时间：</td>
                    <td><input type="text" class="easyui-datebox" readonly="readonly" style="width: 184px; height: 28px;" value="<#if entity.rentExpireTime?? >${app.format_date_time(entity.rentExpireTime)}</#if>"/></td>
                    <td align="right">是否免审：</td>
                    <td>
                        <select name="isReview" readonly="readonly" class="easyui-combobox" editable="false"
                                style="width: 184px; height: 28px;">
                        <#list isReviewEnum as e>
                            <option value="${e.getValue()}" <#if entity.isReview?? && entity.isReview == e.getValue()>selected</#if>> ${(e.getName())!''}</option>
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
