<div class="ui_table" style="padding: 5px;">
    <table cellpadding="1" cellspacing="0" border="0">
        <tbody>
        <tr>
            <td align="right" width="70">运营商：</td>
            <td>
                <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" editable="true"
                       style="width: 170px;height: 28px;"
                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'系统'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                    swich_shop_${pid}();
                                }">&nbsp;&nbsp;
            </td>
            <td align="right">门店：</td>
            <td>
                <input name="shopId" id="shop_id_${pid}" class="easyui-combotree"
                       editable="false" style="width: 165px; height: 28px;">
            </td>
            <td align="right">时间：</td>
            <td>
                <input name="beginTime" id="begin_time_${pid}" class="easyui-datebox"
                       type="text"
                       style="width:150px;height:27px;">
                -
                <input name="endTime" id="end_time_${pid}" class="easyui-datebox" type="text"
                       style="width:150px;height:27px;">
            </td>
            <td align="right" width="250">
                <button class="btn btn_yellow" onclick="query_order_${pid}()">搜索</button>
            </td>
        </tr>
        <#--<tr>
            <td colspan="6">
                <h3>&nbsp;&nbsp;&nbsp;&nbsp;
                    金额：<input type="text" class="text" readonly id="money" value="" style="width: 70px;">元</h3>
            </td>
            <td align="right">
                <button class="btn btn_blue" onclick="clearing()">确定</button>
            </td>
        </tr>-->
        </tbody>
    </table>
</div>
<div class="grid" style="height: 470px;margin: 5px; ">
    <table id="page_table_${pid}"></table>
</div>
<h3>总金额：<input type="text" class="text" readonly id="balance_${pid}" value="" style="width: 70px;">元</h3>
<div class="popup_btn">
    <button class="btn btn_red ok" onclick="clearing()">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    $(function () {
        var win = $('#${pid}');
        $('#page_table_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: false,
            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '订单id',
                        align: 'center',
                        field: 'id',
                        width: 25
                    },
                    {
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 20
                    },
                    {
                        title: '柜子id',
                        align: 'center',
                        field: 'cabinetId',
                        width: 20
                    },
                    {
                        title: '柜子名称',
                        align: 'center',
                        field: 'cabinetName',
                        width: 15
                    },
                    {
                        title: '姓名',
                        align: 'center',
                        field: 'customerFullname',
                        width: 15
                    },
                    {
                        title: '手机号',
                        align: 'center',
                        field: 'customerMobile',
                        width: 15
                    },
                    {
                        title: '电池类型',
                        align: 'center',
                        field: 'batteryTypeName',
                        width: 15
                    },
                    {
                        title: '支付类型',
                        align: 'center',
                        field: 'payTypeName',
                        width: 15
                    },
                    {
                        title: '支付金额(元)',
                        align: 'center',
                        field: 'money',
                        width: 15,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '支付时间',
                        align: 'center',
                        field: 'payTime',
                        width: 20
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'statusName',
                        width: 15
                    },
                    {
                        title: '分成比例',
                        align: 'center',
                        field: 'ratio',
                        width: 15,
                        formatter: function (val) {
                            return val+"%";
                        }
                    },
                    {
                        title: '固定分成',
                        align: 'center',
                        field: 'hopFixedMoney',
                        width: 15,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2)
                        }
                    },
                    {
                        title: '分成金额',
                        align: 'center',
                        field: 'intoMoney',
                        width: 15,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2)
                        }
                    }
                ]
            ]
        })
        win.find('.close').click(function() {
            win.window('close');
        });
    });

    function swich_shop_${pid}() {
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var shopTree = $('#shop_id_${pid}');
        shopTree.combotree({
            url: "${contextPath}/security/hdg/shop/tree.htm?agentId=" + agentId
        });
        shopTree.combotree('reload');
        shopTree.combotree('setValue', null);
    }

    function query_order_${pid}() {
        if (!check()) {
            success = false;
        } else {
            var agentId = $('#agent_id_${pid}').combotree('getValue');
            var shopId = $('#shop_id_${pid}').combotree('getValue');
            var beginTime = $('#begin_time_${pid}').datetimebox('getValue')+' 00:00:00';
            var endTime = $('#end_time_${pid}').datetimebox('getValue')+' 00:00:00';
            var datagrid = $('#page_table_${pid}');
            $.ajax({
                method : 'GET',
                url : '${contextPath}/security/hdg/packet_period_order/page_for_clearing.htm?agentId='+agentId+'&shopId='+shopId+'&beginTime='+beginTime+'&endTime='+endTime,
                async : false,
                dataType : 'json',
                success : function(data) {
                    datagrid.datagrid('loadData', data.list);
                    $('#balance_${pid}').val(data.totalMoney/100);
                },
                error : function() {
                    $.messager.alert('提示信息', "数据错误", 'error');
                }
            });
            datagrid.datagrid('load');
        }
    }

    function check() {
        var win = $('#${pid}');
        if (win.find('input[name="agentId"]').val() == '') {
            $.messager.alert('提示信息', "请选择运营商", 'error');
            return false;
        }
        if (win.find('input[name="shopId"]').val() == '') {
            $.messager.alert('提示信息', "请选择门店", 'error');
            return false;
        }
        if (win.find('input[name="beginTime"]').val() == '') {
            $.messager.alert('提示信息', "请选择日期", 'error');
            return false;
        }
        if (win.find('input[name="endTime"]').val() == '') {
            $.messager.alert('提示信息', "请选择日期", 'error');
            return false;
        }
        return true;
    }

    function clearing() {
        var success = check();
        var rows = $("#page_table_${pid}").datagrid("getRows");
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var shopId = $('#shop_id_${pid}').combotree('getValue');
        var beginTime = $('#begin_time_${pid}').datetimebox('getValue')+' 00:00:00';
        var endTime = $('#end_time_${pid}').datetimebox('getValue')+' 00:00:00';
        var balance = $('#balance_${pid}').val();
        if (success && rows.length == 0 || rows == null) {
            $.messager.alert('提示信息', "数据不可为空", 'error');
        } else {
            $.messager.confirm('提示信息', '确认转账?', function (ok) {
                var ids = [];
                for (var i=0;i<rows.length;i++) {
                    ids.push(rows[i].id)
                }
                if (ok) {
                    $.post('${contextPath}/security/basic/agent_agent/clearing.htm',{
                        ids: ids,
                        agentId:agentId,
                        shopId:shopId,
                        beginTime:beginTime,
                        endTime:endTime,
                        balance:balance*100
                    }, function(json) {
                        if(json.success) {
                            $.messager.alert('提示信息', "操作成功", 'info');
                        }else{
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        }
    }
</script>