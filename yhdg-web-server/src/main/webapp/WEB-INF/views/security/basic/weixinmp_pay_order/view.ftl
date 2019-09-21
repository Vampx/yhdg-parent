<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="agentId" value="${Session['SESSION_KEY_USER'].agentId}">
            <input type="hidden" name="photoPath" id="photoPath_${pid}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">客户ID：</td>
                    <td><input type="text" class="text" name="customerId" readonly value="${(entity.customerId)!''}"/></td>
                    <td align="right">客户名称：</td>
                    <td><input type="text" class="text" maxlength="40"  readonly name="customerName" value="${(entity.customerName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">金额：</td>
                    <td><input type="text" class="text" name="money" readonly value="${((entity.money)/ 100 + "元")!''}"/></td>
                    <td align="right">订单单号：</td>
                    <td><input type="text" class="text"maxlength="32" readonly name="sourceId" value="${(entity.sourceId)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">订单类型：</td>
                    <td>
                        <select class="easyui-combobox" required="true" readonly="readonly" id="SourceType_${pid}" name="SourceType" style="width:180px;height: 30px ">
                        <#list SourceTypeEnum as s>
                            <option value="${s.getValue()}" <#if entity.SourceType?? && entity.SourceType==s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                    <td align="right">状态：</td>
                    <td>
                        <select class="easyui-combobox" required="true" readonly="readonly" id="orderStatus_${pid}" name="orderStatus" style="width:180px;height: 30px ">
                        <#list StatusEnum as s>
                            <option value="${s.getValue()}" <#if entity.orderStatus?? && entity.orderStatus==s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">退款时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" readonly style="width:180px;height:28px " value="<#if (entity.refundTime)?? >${app.format_date_time(entity.refundTime)}</#if>"></td>
                    <td align="right">退款金额：</td>
                    <td><input type="text" class="text" name="refundMoney" readonly value="<#if (entity.refundMoney)?? >${(entity.refundMoney/ 100 + "元")!''}</#if>"/></td>
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
                win = $('#' + pid),
                form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>