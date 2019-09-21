<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
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
                        <input id="vin_no" type="text" class="text" readonly name="customerFullname" value="${(entity.customerFullname)!''}" style="height: 28px;width: 185px;" maxlength="100">
                    </td>
                    <td width="90" align="right">手机号：</td>
                    <td>
                        <input id="vin_no" type="text" class="text" readonly name="customerMobile" value="${(entity.customerMobile)!''}" style="height: 28px;width: 185px;" maxlength="100">
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">车辆配置：</td>
                    <td>
                        <input id="vin_no" type="text" class="text" readonly name="vehicleName" value="${(entity.vehicleName)!""}" style="height: 28px;width: 185px;" maxlength="100">
                    </td>
                    <td width="90" align="right">车辆型号：</td>
                    <td>
                        <input id="vin_no" type="text" class="text" readonly name="modelName" value="${(entity.modelName)!''}" style="height: 28px;width: 185px;" maxlength="100">
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">租金金额：</td>
                    <td><input id="vin_no" type="text" class="text" readonly name="money" value="<#if (entity.price)??>${((entity.price) / 100 + "元")!''}<#else></#if>" style="height: 28px;width: 185px;" maxlength="100"></td>
                    <td width="90" align="right">支付时间：</td>
                    <td><input id="vin_no" type="text" class="text" readonly name="payTime" value="${(entity.payDate)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                </tr>
                <tr>
                    <td width="90" align="right">支付方式：</td>
                    <td><input id="vehicle_name" type="text" class="text" readonly name="payType" value="${(entity.payTypeName)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                    <td width="90" align="right">支付状态：</td>
                    <td><input id="vehicle_name" type="text" class="text" readonly name="status" value="${(entity.statusName)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                </tr>
                <tr>
                    <td width="90" align="right">开始时间：</td>
                    <td><input id="vehicle_name" type="text" class="text" readonly name="beginDate" value="${(entity.beginDate)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                    <td width="90" align="right">结束时间：</td>
                    <td><input id="vehicle_name" type="text" class="text" readonly name="endDate" value="${(entity.endDate)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                </tr>
                <tr>
                    <td width="90" align="right">退款金额：</td>
                    <td><input id="vehicle_name" type="text" class="text" readonly name="refundMoney" value="<#if (entity.refundMoney)??>${((entity.refundMoney) / 100 + "元")!''}<#else></#if>" style="height: 28px;width: 185px;" maxlength="100"></td>
                    <td width="90" align="right">退款时间：</td>
                    <td><input id="vehicle_name" type="text" class="text" readonly name="refundTime" value="${(entity.refundDate)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                </tr>
                <tr>
                    <td width="90" align="right">退款审核人：</td>
                    <td><input id="vehicle_name" type="text" class="text" readonly name="refundOperator" value="${(entity.refundOperator)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
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
