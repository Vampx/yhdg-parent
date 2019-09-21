<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="batteryType" value="${(entity.batteryType)!''}">
            <input type="hidden" name="agentId" value="${(entity.agentId)!''}">
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
                    <td align="right">类型名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="typeName" readonly required="true" maxlength="40" value="${(entity.typeName)!''}"/></td>
                </tr>
            </table>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td colspan="2">电池保险：</td>
                </tr>
                <tr id="exchange_insurance">
                <#include '../../zd/rent_insurance/rent_insurance.ftl'>
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

        //保险
        $.post('${contextPath}/security/zd/rent_insurance/rent_insurance.htm', {
            batteryType: ${(entity.batteryType)!''},
            agentId: ${(entity.agentId)!''}
        }, function (html) {
            $("#exchange_insurance").html(html);
        }, 'html');


        var ok = function () {
            return true;
        }

        win.data('ok', ok);
    })();


</script>
