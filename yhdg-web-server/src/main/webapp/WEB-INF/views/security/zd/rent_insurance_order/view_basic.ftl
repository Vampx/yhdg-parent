<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0" style="width: 80%;">
                <tr>
                    <td width="70" align="right">商户：</td>
                    <td>
                        <input name="partnerId" id="partner_id_${pid}" style="height: 28px;width: 183px;" class="easyui-combobox"  editable="false"
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
                    <td><input class="text easyui-validatebox" readonly value="${(entity.agentName)!''}" style="width:182px;height:28px " required="required" ></td>
                    <td align="right">订单月份：</td>
                    <td><input class="text easyui-validatebox" readonly value="${(entity.monthCount)!''}" style="width:182px;height:28px " required="required" ></td>
                </tr>
                <tr>
                    <td align="right">开始时间：</td>
                    <td>
                    <input type="text" readonly class="text easyui-datetimebox"style="width:195px;height:28px "
                    value="<#if (entity.beginTime)?? >${app.format_date_time(entity.beginTime)}</#if>">
                    </td>
                    <td align="right">结束时间：</td>
                    <td>
                        <input type="text" readonly class="text easyui-datetimebox"style="width:195px;height:28px "
                               value="<#if (entity.endTime)?? >${app.format_date_time(entity.endTime)}</#if>">
                    </td>
                </tr>
                <tr>
                    <td align="right">订单状态：</td>
                    <td>
                        <select class="easyui-combobox" required="true" readonly style="width:192px;height:28px ">
                        <#list StatusEnum as s>
                            <option value="${s.getValue()}" <#if entity.status?? && entity.status == s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                    <td align="right">手机：</td>
                    <td><input class="text easyui-validatebox" readonly value="${(entity.customerMobile)!''}" style="width:182px;height:28px " required="required" ></td>
                </tr>
                <tr>
                    <td align="right">姓名：</td>
                    <td><input class="text easyui-validatebox" readonly value="${(entity.customerFullname)!''}" style="width:182px;height:28px " required="required" ></td>
                    <td align="right">支付类型：</td>
                    <td>
                        <select class="easyui-combobox" required="true" readonly style="width:192px;height:28px ">
                        <#list PayTypeEnum as s>
                            <option value="${s.getValue()}" <#if entity.payType?? && entity.payType == s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">支付金额：</td>
                    <td><input class="text easyui-validatebox" readonly  value="<#if (entity.money)??>${((entity.money) / 100 + "元")!''}<#else></#if>" style="width:182px;height:28px " required="required" ></td>
                    <td align="right">保费：</td>
                    <td><input class="text easyui-validatebox" readonly value="${((entity.price) / 100 + "元")!''}" style="width:182px;height:28px " required="required" ></td>
                </tr>
                <tr>
                    <td align="right">保额：</td>
                    <td><input class="text easyui-validatebox" readonly value="${((entity.paid) / 100 + "元")!''}" style="width:182px;height:28px " required="required" ></td>
<#--                    <td align="right">充值余额：</td>-->
<#--                    <td><input class="text easyui-validatebox" readonly value="${((entity.consumeDepositBalance) / 100 + "元")!''}" style="width:182px;height:28px " required="required" ></td>-->
                </tr>
                <#if entity.refundMoney??>
                    <tr>
                        <td align="right">退款时间：</td>
                        <td>
                            <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly="readonly"
                                   value="<#if (entity.refundTime)?? >${app.format_date_time(entity.refundTime)}</#if>" name="refundTime">
                        </td>
                        <td align="right">退款人：</td>
                        <td><input id="duration" class="text easyui-validatebox" readonly="readonly" name="refundOperator" value="${(entity.refundOperator)!''}" style="width:182px;height:28px " ></td>
                    </tr>
                    <tr>
                        <td align="right">退款金额：</td>
                        <td><input type="text" class="text" style="width:182px;height:28px " readonly="readonly" name="refundMoney" value="<#if (entity.refundMoney)??>${((entity.refundMoney) / 100 + "元")!''}<#else></#if>"/></td>
                    </tr>
                </#if>
            </table>
        </form>
    </div>
</div>