<div class="popup_body">
    <div class="ui_table" style="height: 300px">
        <form id="table_form">
            <input type="hidden" name="id" id="id_${pid}">
            <input type="hidden" name="packetPeriodOrderId" id="packet_period_order_id_${pid}">
            <input type="hidden" name="foregiftType" id="foregift_type_${pid}">
            <input type="hidden" name="customerId" id="customer_id_${pid}">

            <div style="width: 70%;height: 100%;float: left">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td align="right">二维码：</td>
                        <td><input type="text" id="qr_code_${pid}" class="text" required="true"/></td>
                    </tr>
                    <tr>
                        <td align="right">车辆编号：</td>
                        <td><input type="text" name="vehicleId" id="vehicle_id_${pid}" class="text" required="true"/></td>
                    </tr>
                    <tr>
                        <td align="right">电池编号：</td>
                        <td><input type="text" name="batteryId" id="battery_id_${pid}" class="text" required="true"/></td>
                    </tr>
                </table>
            </div>
        </form>
        <div id="showForegiftInfo"  style="display: none;width: 30%;height: 100%;float: right">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td>订单信息</td>
                </tr>
                <tr>
                    <td>用户名称：</td>
                    <td><span id="customerFullname"></span></td>
                </tr>
                <tr>
                    <td>手机号码：</td>
                    <td><span id="customerMobile"></span></td>
                </tr>
                <tr>
                    <td>产品名称：</td>
                    <td><span id="bizType"></span></td>
                </tr>
                <tr>
                    <td>车辆型号：</td>
                    <td><span id="vehicleModel"></span></td>
                </tr>
                <tr>
                    <td>电池型号：</td>
                    <td><span id="batteryType"></span></td>
                </tr>
                <tr>
                    <td>押金：</td>
                    <td><span id="foregiftMoney"></span></td>
                </tr>
                <tr>
                    <td>租金：</td>
                    <td><span id="packetPeriodPrice"></span> </td>
                </tr>
                <tr>
                    <td>状态：</td>
                    <td><span id="statusName"></span> </td>
                </tr>
            </table>
        </div>
        <span id="fail_result_${pid}"></span>
    </div>
</div>

<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        $('#qr_code_${pid}').bind('keyup', function(event) {
            if (event.keyCode == "13") {
                var qrCode = $('#qr_code_${pid}').val();
                showForegiftOrder(qrCode);
            }
        });

        function showForegiftOrder(qrCode) {
            $.ajax({
                type:'POST',
                url:'${contextPath}/security/basic/customer_foregift_order/find_foregift.htm',
                dataType:'json',
                data:{
                    qrCode:qrCode
                },
                success:function (json) {
                    if(json.success) {
                        $('#fail_result_${pid}').html("");
                        document.getElementById("showForegiftInfo").style.display = "block";
                        var data = json.data;
                        if(data.foregiftType==${(EXCHANGE)!''}) {
                            $('#bizType').html("换电");
                        }else if(data.foregiftType==${(EXCHANGE_RENT_SELL)!''}) {
                            $('#bizType').html("换电以租代售");
                        }else if(data.foregiftType==${(VEHICLE)!''}) {
                            $('#bizType').html("租车");
                        }else if(data.foregiftType==${(VEHICLE_RENT_SELL)!''}) {
                            $('#bizType').html("租车以租代售");
                        }
                        if(data.packetPeriodOrderId!='') {
                            $.ajax({
                                type: 'POST',
                                url: '${contextPath}/security/hdg/packet_period_order/find_period_order.htm',
                                dataType: 'json',
                                data: {
                                    id: data.packetPeriodOrderId
                                },
                                success: function (json) {
                                    if (json.success) {
                                        var data2=json.data;
                                        $('#packetPeriodPrice').html(data2.money/100+"元");
                                    }
                                }
                            })
                            win.find('input[name=packetPeriodOrderId]').val(data.packetPeriodOrderId);
                            win.find('input[name=foregiftType]').val(data.foregiftType);
                        }else {
                            $('#packetPeriodPrice').html('');
                        }
                        $('#batteryType').html(data.batteryTypeName);
                        $('#vehicleModel').html(data.vehicleModelName);
                        $('#foregiftMoney').html(data.money/100+"元");
                        $('#statusName').html(data.statusName);
                        $('#customerMobile').html(data.customerMobile);
                        $('#customerFullname').html(data.customerFullname);
                        win.find('input[name=id]').val(data.id);
                        win.find('input[name=customerId').val(data.customerId);
                    }else {
                        win.find('input[name=id]').val('');
                        document.getElementById("showForegiftInfo").style.display = "none";
                        $('#fail_result_${pid}').html("该订单不存在");
                    }
                }
            })
        }

        win.find('button.ok').click(function() {
            var qrCode = $('#qr_code_${pid}').val();
            if(qrCode == ""){
                $.messager.alert('提示信息','二维码不能为空！','info');
                return false;
            }

            var foregiftType = $('#foregift_type_${pid}').val();
            if(foregiftType == ${(EXCHANGE)!''} || foregiftType == ${(EXCHANGE_RENT_SELL)!''} ) {
                $.messager.alert('提示信息','只支持租车业务！','info');
                return false;
            }else if(foregiftType == ${(VEHICLE)!''} || foregiftType == ${(VEHICLE_RENT_SELL)!''} ) {
                var vehicleId = $('#vehicle_id_${pid}').val();
                if(vehicleId == ""){
                    $.messager.alert('提示信息','车辆编号不能为空！','info');
                    return false;
                }
                var batteryId = $('#battery_id_${pid}').val();
                if(batteryId == ""){
                    $.messager.alert('提示信息','电池编号不能为空！','info');
                    return false;
                }
            }

            var id = $('#id_${pid}').val();
            if(id == ''){
                $.messager.alert('提示信息','订单不存在！','info');
                return false;
            }
            $.ajax({
                type: 'POST',
                url: '${contextPath}/security/hdg/vehicle_order/create.htm',
                dataType: 'json',
                data: {
                    customerForegiftOrderId:$('#id_${pid}').val(),
                    packetPeriodOrderId: $('#packet_period_order_id_${pid}').val(),
                    customerId:$('#customer_id_${pid}').val(),
                    vehicleId:$('#vehicle_id_${pid}').val(),
                    batteryId:$('#battery_id_${pid}').val()
                },
                success: function (json) {
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
    })()
</script>
