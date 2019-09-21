<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商名称：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" editable="false"
                               <#if (entity.agentId)??>disabled</#if> style="width: 184px; height: 28px;"
                               url="${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}"
                               value="${(entity.agentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">保险名称：</td>
                    <td><input type="text" class="text easyui-validatebox"  name="insuranceName" value="${(entity.insuranceName)!''}" readonly maxlength="40" /></td>
                </tr>
                <tr>
                    <td align="right">价格：</td>
                    <td><input type="text"  class="text easyui-validatebox"  id="price_${pid}" value="${(entity.price/100)!''}" readonly  data-options="precision:2" style="width:170px;height: 28px;" maxlength="10" >元</td>
                </tr>
                <tr>
                    <td align="right">保额：</td>
                    <td><input type="text"  class="text easyui-validatebox"  id="paid_${pid}" value="${(entity.paid/100)!''}" readonly  data-options="precision:2" style="width:170px;height: 28px;" maxlength="10" >元</td>
                </tr>
                <tr>
                    <td align="right">时长：</td>
                    <td><input type="text" class="text easyui-numberspinner" readonly  name="monthCount" value="${(entity.monthCount)!''}" maxlength="3"
                               data-options="min:0, max:12" style="width: 184px; height: 28px;"/>月</td>
                </tr>
                <tr>
                    <td align="right">是否有效：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="is_active_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if>  value="1"/><label for="is_active_1">有效</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="is_active_0" <#if entity.isActive?? && entity.isActive == 1><#else >checked</#if>  value="0"/><label for="is_active_0">无效</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td colspan="3"><textarea style="width:300px; height: 110px;" name="memo" readonly maxlength="200">${(entity.memo)!''}</textarea></td>
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