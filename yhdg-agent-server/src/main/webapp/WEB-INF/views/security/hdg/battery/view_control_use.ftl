<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <#if entity.status == 6>
                    <tr>
                        <td width="70" align="right">使用状态：</td>
                        <td><input type="text" class="text easyui-validatebox" value="${(entity.statusName)!''}" /></td>
                        <td width="90" align="right">客户取出时间：</td>
                        <td>
                            <input type="text" class="text easyui-datetimebox" style="width:195px; height:28px " value="<#if (entity.customerOutTime)?? >${app.format_date_time(entity.customerOutTime)}</#if>" />
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="right">客户id：</td>
                        <td><input type="text" class="text easyui-validatebox" value="${(entity.customerId)!''}"/></td>
                        <td width="90" align="right">客户手机号：</td>
                        <td><input type="text" class="text easyui-validatebox" value="${(entity.customerMobile)!''}" /></td>
                    </tr>
                    <tr>
                        <td width="70" align="right">客户姓名：</td>
                        <td><input type="text" class="text easyui-validatebox" value="${(entity.customerFullname)!''}"/></td>
                    </tr>
                <#elseif entity.status == 2 || entity.status == 3 || entity.status == 5>
                    <tr>
                        <td width="70" align="right">使用状态：</td>
                        <td><input type="text" class="text easyui-validatebox" value="${(entity.statusName)!''}" /></td>
                        <td width="70" align="right">放入时间：</td>
                        <td>
                            <input type="text" class="text easyui-datetimebox" style="width:195px; height:28px " value="<#if (entity.inBoxTime)?? >${app.format_date_time(entity.inBoxTime)}</#if>" />
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="right">换电柜id：</td>
                        <td><input type="text" class="text easyui-validatebox" value="${(entity.cabinetId)!''}" /></td>
                        <td width="90" align="right">换电柜名称：</td>
                        <td><input type="text" class="text easyui-validatebox" value="${(entity.cabinetName)!''}"/></td>
                    </tr>
                    <tr>
                        <td width="70" align="right">箱号：</td>
                        <td><input type="text" class="text easyui-validatebox" value="${(entity.boxNum)!''}"/></td>
                    </tr>
                <#elseif entity.status == 4>
                    <tr>
                        <td width="70" align="right">使用状态：</td>
                        <td><input type="text" class="text easyui-validatebox" value="${(entity.statusName)!''}" /></td>
                        <td width="90" align="right">维护取出时间：</td>
                        <td>
                            <input type="text" class="text easyui-datetimebox" style="width:195px; height:28px " value="<#if (entity.keeperOutTime)?? >${app.format_date_time(entity.keeperOutTime)}</#if>" />
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="right">维护人id：</td>
                        <td><input type="text" class="text easyui-validatebox" value="${(entity.keeperId)!''}" /></td>
                        <td width="70" align="right">维护人：</td>
                        <td><input type="text" class="text easyui-validatebox" value="${(entity.keeperName)!''}"/></td>
                    </tr>
                    <tr>
                        <td width="90" align="right">维护人手机号：</td>
                        <td><input type="text" class="text easyui-validatebox" value="${(entity.keeperMobile)!''}"/></td>
                    </tr>
                </#if>
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