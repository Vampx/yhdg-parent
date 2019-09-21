<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0" style="width: 80%;">
                <tr>
                    <td align="right">客户名称：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly="readonly" name="customerFullname" value="${(entity.customerFullname)!''}" style="width:182px;height:28px " ></td>
                    <td align="right">客户手机号：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly="readonly" name="customerMobile" value="${(entity.customerMobile)!''}" style="width:182px;height:28px " ></td>
                </tr>

                <tr>
                    </td>
                    <td align="right">创建时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly="readonly"
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>" name="createTime">
                    </td>
                <#if entity.status==1>
                    <td align="right">还电时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly="readonly"
                               value="<#if (entity.backTime)?? >${app.format_date_time(entity.backTime)}</#if>" name="endTime">
                    </td>
                </#if>
                </tr>

                <tr>
                    <td align="right">骑行距离：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly style="width: 183px; height: 28px;"
                               value="${(entity.currentDistance)!''}"/></td>
                    <td align="right">当前电量：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly style="width: 183px; height: 28px;"
                               value="${(entity.currentVolume)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">状态：</td>
                    <td>
                        <select class="easyui-combobox" required="true" readonly="readonly"  name="status" style="width:195px;height:28px ">
                        <#list StatusEnum as s>
                            <option value="${s.getValue()}" <#if entity.status?? && entity.status == s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>

                </tr>
                <tr>
                    <td align="right" style="padding-top:10px;">备注：</td>
                    <td colspan="3"><textarea style="width:500px;height: 100px;" maxlength="20" readonly name="memo">${(entity.operatorMemo)!''}</textarea></td>
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






