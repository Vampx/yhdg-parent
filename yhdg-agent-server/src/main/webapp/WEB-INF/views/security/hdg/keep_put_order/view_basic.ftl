<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">运营商：</td>
                    <td><input type="text" maxlength="40" readonly class="text easyui-validatebox" value="${(entity.agentName)!''}"/></td>
                    <td width="90" align="right">换电柜编号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.cabinetId)!''}" /></td>
                </tr>
                <tr>
                    <td width="90" align="right">换电柜名称：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.cabinetName)!''}"/></td>
                    <td width="70" align="right">订单数量：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.orderCount)!''}"/></td>
                </tr>
                <tr>
                    <td width="90" align="right">最后投电时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" readonly style="width:180px;height:30px;" value="<#if (entity.lastTime)?? >${app.format_date_time(entity.lastTime)}</#if>"></td>
                    <td width="70" align="right">创建时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" readonly style="width:180px;height:30px;" value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"></td>
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
            if(!jform.form('validate')) {
                return false;
            }
            var success = true;

            return success;
        };

        win.data('ok', ok);
    })();
</script>