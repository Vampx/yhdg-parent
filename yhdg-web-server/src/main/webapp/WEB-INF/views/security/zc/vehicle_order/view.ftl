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
                        <input  type="text" class="text" readonly name="customerFullname" value="${(entity.customerFullname)!''}" style="height: 28px;width: 185px;" maxlength="100">
                    </td>
                    <td width="90" align="right">手机号：</td>
                    <td>
                        <input  type="text" class="text" readonly name="customerMobile" value="${(entity.customerMobile)!''}" style="height: 28px;width: 185px;" maxlength="100">
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">车架号：</td>
                    <td><input  type="text" class="text" readonly name="vehicleId" value="${(entity.vinNo)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>

                    <td width="90" align="right">车辆配置：</td>
                    <td>
                        <input  type="text" class="text" readonly name="vehicleName" value="${(entity.vehicleName)!""}" style="height: 28px;width: 185px;" maxlength="100">
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">车辆型号：</td>
                    <td>
                        <input  type="text" class="text" readonly name="modelName" value="${(entity.modelName)!''}" style="height: 28px;width: 185px;" maxlength="100">
                    </td>
                    <td width="90" align="right">车辆状态：</td>
                    <td><input  type="text" class="text" readonly name="status" value="${(entity.statusName)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                </tr>
                <tr>
                    <td width="90" align="right">开始时间：</td>
                    <td><input id="vehicle_name" type="text" class="text" readonly name="createDate" value="${(entity.createDate)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                    <td width="90" align="right">还车时间：</td>
                    <td><input id="vehicle_name" type="text" class="text" readonly name="backDate" value="${(entity.backDate)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                </tr>
                <tr>
                    <td width="90" align="right">还车人：</td>
                    <td><input id="vehicle_name" type="text" class="text" readonly name="backOperator" value="${(entity.backOperator)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
                    <td width="90" align="right">电池编号：</td>
                    <td><input id="battery_id" type="text" class="text" readonly name="batteryId" value="${(entity.batteryId)!''}" style="height: 28px;width: 185px;" maxlength="100"></td>
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
