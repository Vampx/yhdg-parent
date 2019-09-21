<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0" style="width: 80%;">
                <tr>
                    <td align="right">客户名称：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly="readonly" name="fullname" value="${(entity.fullname)!''}" style="width:182px;height:28px " ></td>
                    <td align="right">客户手机号：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly="readonly" name="mobile" value="${(entity.mobile)!''}" style="width:182px;height:28px " ></td>
                </tr>
                <tr>
                    <td align="right">审核用户：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly style="width: 183px; height: 28px;"
                               value="${(entity.auditUser)!''}"/></td>
                    <td align="right">审核时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly="readonly"
                               value="<#if (entity.auditTime)?? >${app.format_date_time(entity.auditTime)}</#if>" name="autitTime">
                    </td>
                </tr>
                <tr>
                    <td align="right">状态：</td>
                    <td>
                        <select class="easyui-combobox" required="true" readonly="readonly"  name="status" style="width:195px;height:28px ">
                        <#list StatusEnum as s>
                            <option value="${s.getValue()}" <#if entity.status?? && entity.status == s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                    <td align="right">创建时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly="readonly"
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>" name="createTime">
                    </td>
                </tr>
                <tr>
                    <td align="right" style="padding-top:10px;">审核备忘录：</td>
                    <td colspan="3"><textarea style="width:500px;height: 100px;" maxlength="20" readonly name="auditMemo">${(entity.auditMemo)!''}</textarea></td>
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






