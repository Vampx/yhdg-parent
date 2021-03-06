<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">商户：</td>
                    <td>
                        <input name="partnerId" id="partner_id_${pid}" style="height: 28px;width: 196px;" class="easyui-combobox"  editable="false" disabled
                               data-options="url:'${contextPath}/security/basic/partner/list.htm?dummy=${'无'?url}',
                                    method:'get',
                                    valueField:'id',
                                    textField:'partnerName',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                   onLoadSuccess:function() {
                           $('#partner_id_${pid}').combobox('setValue', '${(entity.partnerId)!''}');
                       }"
                        />
                    </td>
                </tr>
                <tr>
                    <td align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}"  readonly class="easyui-combotree" required="true"
                               editable="false" style="width: 196px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                        method:'get',
                                        valueField:'id',
                                        textField:'text',
                                        editable:false,
                                        multiple:false,
                                        panelHeight:'200',
                                        onClick: function(node) {
                                          swich_battery_type_${pid}();
                                        }"
                               value="${(entity.agentId)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">优惠券类型：</td>
                    <td>
                        <select class="easyui-combobox" readonly name="ticketType" style="width:196px;height: 30px ">
                        <#list TicketTypeEnum as s>
                            <option value="${s.getValue()}"
                                    <#if entity.ticketType?? && entity.ticketType == s.getValue()>selected</#if>>${s.getName()}</option>
                        </#list>
                        </select>
                        <#if entity.ticketType?? && entity.ticketType == 3><span id="ticket_info_${pid}" style="color: red;font-weight: 600;width: 100px;">此券是现金券，可提现</span></#if>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">优惠券名称：</td>
                    <td><input type="text" maxlength="40" style="width: 184px; height: 28px;" readonly="readonly" class="text easyui-validatebox" required="true" value="${(entity.ticketName)!''}" name="ticketName" /></td>
                </tr>
                <tr>
                    <td width="80" align="right">优惠券金额：</td>
                    <td><input id="money_${pid}" readonly="readonly" class="easyui-numberspinner" value="${((entity.money)/ 100 + "元")!''}"  style="width:196px;height: 30px " required="required" data-options="min:0.01,precision:2">&nbsp;&nbsp;元</td>
                </tr>
                <tr>
                    <td align="right">开始时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox" readonly style="width:196px;height:28px " id="begin_time_${pid}"
                               value="<#if (entity.beginTime)?? >${app.format_date_time(entity.beginTime)}</#if>" required="true" name="beginTime" >
                    </td>
                </tr>
                <tr>
                    <td align="right">结束时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox" readonly style="width:196px;height:28px " id="end_time_${pid}"
                               value="<#if (entity.expireTime)?? >${app.format_date_time(entity.expireTime)}</#if>" required="true" name="expireTime">
                    </td>
                </tr>
                <tr>
                    <td align="right">客户手机：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly="readonly"  maxlength="11" name="customerMobile"  value="${(entity.customerMobile)!''}" style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">操作人：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly="readonly"  maxlength="11" name="operator"  value="${(entity.operator)!''}" style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">创建时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox" readonly style="width:196px;height:28px " id="create_time_${pid}"
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>" required="true" name="createTime">
                    </td>
                </tr>
                <tr>
                    <td align="right">备注：</td>
                    <td>
                        <textarea id="memo_${pid}" name="memo" readonly="readonly" style="width:300px; height: 50px;" maxlength="40">${(entity.memo)!''}</textarea>
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
        var win = $('#${pid}');
        var list = $('#tab_container_${pid} li');

        win.find('.close').click(function() {
            win.window('close');
        });
    })();
</script>