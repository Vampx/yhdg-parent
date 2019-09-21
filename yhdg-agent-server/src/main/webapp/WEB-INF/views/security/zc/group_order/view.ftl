<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">商户：</td>
                    <td>
                        <input name="partnerId" id="partner_id_${pid}" style="height: 28px;width: 183px;" class="easyui-combobox"  editable="false" disabled
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
                    <td width="90" align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agentId" class="easyui-combotree"
                               editable="false" style="width: 184px; height: 28px;" readonly
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                        swich_agent();
                                    }" value="${(entity.agentId)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">姓名：</td>
                    <td>
                        <input type="text" class="text" readonly name="customerFullname" value="${(entity.customerFullname)!''}" style="height: 28px;width: 185px;" maxlength="100">
                    </td>
                    <td width="90" align="right">手机号：</td>
                    <td>
                        <input type="text" class="text" readonly name="customerMobile" value="${(entity.customerMobile)!''}" style="height: 28px;width: 185px;" maxlength="100">
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">车辆型号：</td>
                    <td>
                        <input type="text" class="text" readonly name="modelName" value="${(entity.modelName)!''}" style="height: 28px;width: 185px;" maxlength="100">
                    </td>
                    <td width="90" align="right">车辆配置：</td>
                    <td>
                        <input type="text" class="text" readonly name="vehicleName" value="${(entity.vehicleName)!""}" style="height: 28px;width: 185px;" maxlength="100">
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">支付金额：</td>
                    <td><input type="text" class="text" readonly name="money" value="<#if (entity.money)??>${((entity.money) / 100 + "元")!''}<#else></#if>" style="height: 28px;width: 185px;" maxlength="100"></td>
                    <td width="90" align="right">支付时间：</td>
                    <td><input type="text" class="text" readonly name="payTime" value="${(entity.payDate)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                </tr>
                <tr>
                    <td width="90" align="right">车辆押金金额：</td>
                    <td><input  type="text" class="text" readonly name="vehicleForegiftMoney" value="<#if (entity.vehicleForegiftMoney)??>${((entity.vehicleForegiftMoney) / 100 + "元")!''}<#else></#if>" style="height: 28px;width: 185px;" maxlength="100"></td>
                    <td width="90" align="right">电池押金金额：</td>
                    <td><input  type="text" class="text" readonly name="batteryForegiftMoney" value="<#if (entity.batteryForegiftMoney)??>${((entity.batteryForegiftMoney) / 100 + "元")!''}<#else></#if>" style="height: 28px;width: 185px;" maxlength="100"></td>
                </tr>
                <#if entity.foregiftTicketMoney ?? && entity.foregiftTicketMoney!=0>
                    <tr>
                        <td width="90" align="right">押金优惠券id：</td>
                        <td><input  type="text" class="text" readonly name="vehicleForegiftMoney" value="${(entity.foregiftCouponTicketId)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                        <td width="90" align="right">押金优惠券金额：</td>
                        <td><input  type="text" class="text" readonly name="batteryForegiftMoney" value="<#if (entity.foregiftTicketMoney)??>${((entity.foregiftTicketMoney) / 100 + "元")!''}<#else></#if>" style="height: 28px;width: 185px;" maxlength="100"></td>
                    </tr>
                </#if>
                <tr>
                    <td width="90" align="right">车辆租金金额：</td>
                    <td><input  type="text" class="text" readonly name="vehiclePeriodMoney" value="<#if (entity.vehiclePeriodMoney)??>${((entity.vehiclePeriodMoney) / 100 + "元")!''}<#else></#if>" style="height: 28px;width: 185px;" maxlength="100"></td>
                    <td width="90" align="right">电池租金金额：</td>
                    <td><input  type="text" class="text" readonly name="batteryRentPeriodMoney" value="<#if (entity.batteryRentPeriodMoney)??>${((entity.batteryRentPeriodMoney) / 100 + "元")!''}<#else></#if>" style="height: 28px;width: 185px;" maxlength="100"></td>
                </tr>
                <#if entity.rentTicketMoney ?? && entity.rentTicketMoney!=0>
                    <tr>
                        <td width="90" align="right">租金优惠券id：</td>
                        <td><input  type="text" class="text" readonly name="vehiclePeriodMoney" value="${(entity.rentCouponTicketId)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                        <td width="90" align="right">租金优惠券金额：</td>
                        <td><input  type="text" class="text" readonly name="batteryRentPeriodMoney" value="<#if (entity.rentTicketMoney)??>${((entity.rentTicketMoney) / 100 + "元")!''}<#else></#if>" style="height: 28px;width: 185px;" maxlength="100"></td>
                    </tr>
                </#if>
                <#if entity.deductionTicketMoney ?? && entity.deductionTicketMoney!=0>
                    <tr>
                        <td width="90" align="right">抵扣券id：</td>
                        <td><input  type="text" class="text" readonly name="vehiclePeriodMoney" value="${(entity.deductionTicketId)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                        <td width="90" align="right">抵扣券金额：</td>
                        <td><input  type="text" class="text" readonly name="batteryRentPeriodMoney" value="<#if (entity.deductionTicketMoney)??>${((entity.deductionTicketMoney) / 100 + "元")!''}<#else></#if>" style="height: 28px;width: 185px;" maxlength="100"></td>
                    </tr>
                </#if>
                <tr>
                    <td width="90" align="right">支付方式：</td>
                    <td><input  type="text" class="text" readonly name="payType" value="${(entity.payTypeName)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                    <td width="90" align="right">支付状态：</td>
                    <td><input  type="text" class="text" readonly name="status" value="${(entity.statusName)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                </tr>
                <tr>
                    <td width="90" align="right">退款金额：</td>
                    <td><input  type="text" class="text" readonly name="refundMoney" value="<#if (entity.refundMoney)??>${((entity.refundMoney) / 100 + "元")!''}<#else></#if>" style="height: 28px;width: 185px;" maxlength="100"></td>
                    <td width="90" align="right">退款时间：</td>
                    <td><input  type="text" class="text" readonly name="refundTime" value="${(entity.refundDate)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                </tr>
                <tr>
                    <td width="90" align="right">退款审核人：</td>
                    <td><input  type="text" class="text" readonly name="refundOperator" value="${(entity.refundOperator)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
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
                win = $('#' + pid);
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>
