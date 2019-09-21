<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">客户名称：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly="readonly" name="customerFullname" value="${(entity.customerFullname)!''}" style="width:182px;height:28px " ></td>
                    <td align="right">客户手机号：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly="readonly" name="customerMobile" value="${(entity.customerMobile)!''}" style="width:182px;height:28px " ></td>
                </tr>
                <tr>
                    <td align="right">开始时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly="readonly"
                               value="<#if (entity.beginTime)?? >${app.format_date_time(entity.beginTime)}</#if>" name="beginTime">
                    </td>
                    <td align="right">结束时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly="readonly"
                               value="<#if (entity.endTime)?? >${app.format_date_time(entity.endTime)}</#if>" name="endTime">
                    </td>
                </tr>
                <tr>
                    <td align="right">支付类型：</td>
                    <td>
                        <select class="easyui-combobox" required="true" readonly="readonly"  name="type" style="width:195px;height:28px ">
                        <#list PayTypeEnum as t>
                            <option value="${t.getValue()}" <#if entity.payType?? && entity.payType == t.getValue()>selected</#if> >${t.getName()}</option>
                        </#list>
                        </select>
                    </td>
                    <td align="right">支付时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly="readonly"
                               value="<#if (entity.payTime)?? >${app.format_date_time(entity.payTime)}</#if>" name="payTime">
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
                    <td align="right">金额：</td>
                    <td><input type="text" class="text" style="width:182px;height:28px " name="money" readonly="readonly" value="<#if (entity.money)??>${((entity.money) / 100 + "元")!''}<#else></#if>"/></td>
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






