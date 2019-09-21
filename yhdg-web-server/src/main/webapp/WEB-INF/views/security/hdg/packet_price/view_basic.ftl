<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="90" align="right">运营商：</td>
                    <td>
                        <input class="text easyui-validatebox" style="width: 182px; height: 28px;" value="${(entity.agentName)!''}">
                    </td>
                </tr>
                <tr>
                    <td width="100" align="right">套餐名称：</td>
                    <td><input id="duration" class="text easyui-validatebox" name="priceName" value="${(entity.priceName)!''}" style="width:182px;height:28px " required="required" ></td>
                </tr>
                <tr>
                    <td width="100" align="right">创建时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px "
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>" name="createTime">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var ok = function() {
            var success = true;
            return success;
        };
        win.data('ok', ok);
    })();
</script>






