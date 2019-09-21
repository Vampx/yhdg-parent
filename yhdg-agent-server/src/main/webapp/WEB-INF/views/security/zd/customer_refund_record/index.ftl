<div class="popup_body">
<#--    <div class="search">-->
<#--        <h4>-->
<#--            <div>-->
<#--                退款记录-->
<#--            </div>-->
<#--            <div>-->
<#--                姓名：${(entity.customerFullname)!''}-->
<#--            </div>-->
<#--            <div>-->
<#--                设备名称：-->
<#--                <input style="width: 100px" class="text" id="cabinet_name_${pid}" type="text">&nbsp;&nbsp;-->
<#--                <button class="btn btn_yellow" onclick="page_query()">搜 索</button>-->
<#--            </div>-->
<#--        </h4>-->
<#--    </div>-->
    <div style="height:560px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>

<script>
    (function() {
        var win = $('#${pid}'), windowData = win.data('windowData');
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/zd/customer_refund_record/page.htm",
            fitColumns: true,
            pageSize: 10,
            pageList: [10, 50, 100],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            queryParams: {
                customerId: '${entity.customerId}'
            },
            columns: [
                [
                    {
                        title: '客户姓名',
                        align: 'center',
                        field: 'fullname',
                        width: 180
                    },
                    {
                        title: '手机号',
                        align: 'center',
                        field: 'mobile',
                        width: 180
                    },
                    {
                        title: '商户单号',
                        align: 'center',
                        field: 'ptPayOrderId',
                        width: 430
                    },
                    {
                        title: '源订单ID',
                        align: 'center',
                        field: 'sourceId',
                        width: 260
                    },
                    {
                        title: '订单类型',
                        align: 'center',
                        field: 'sourceType',
                        width: 180,
                        formatter: function (val) {
                            if (val == 1) {
                                return '押金订单';
                            } else if (val == 2) {
                                return '租金订单';
                            } else if (val == 3) {
                                return '保险订单';
                            }
                        }
                    },
                    {
                        title: '退款方式',
                        align: 'center',
                        field: 'refundType',
                        width: 180,
                        formatter: function (val) {
                            if (val == 2) {
                                return '原路退回';
                            } else if (val == 1) {
                                return '退到余额';
                            }
                        }
                    },
                    {
                        title: '退款金额',
                        align: 'center',
                        field: 'refundMoney',
                        width: 180,
                        formatter: function (val) {
                            return val/100+"元";
                        }
                    },
                    {
                        title: '退款时间',
                        align: 'center',
                        field: 'refundTime',
                        width: 240
                    },
                    {
                        title: '退款状态',
                        align: 'center',
                        field: 'status',
                        width: 180,
                        formatter: function (val) {
                            if (val == 1) {
                                return '审核中';
                            } else if (val == 2) {
                                return '退款完成';
                            } else if (val == 3) {
                                return '退款失败';
                            } else if (val == 4) {
                                return '已取消';
                            }
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 180,
                        formatter: function (val, row) {
                            var html = '<a href="javascript:view(\'SOURCEID\', SOURCETYPE)">订单详情</a>';
                            return html.replace(/SOURCEID/g, row.sourceId).replace(/SOURCETYPE/g, row.sourceType);
                        }
                    }
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });

        $('#ok_${pid}').click(function() {
            var checked = datagrid.datagrid('getChecked');
            if(checked.length > 0) {
                windowData.ok(checked);
                win.window('close');
            } else {
                $.messager.alert('提示信息', '请选择设备');
            }

        });


        $('#close_${pid}').click(function() {
            $('#${pid}').window('close');
        });

    })();

    function page_query() {
        var datagrid = $('#page_table_${pid}');
        var cabinetName = $('#cabinet_name_${pid}').val();
        datagrid.datagrid('options').queryParams = {
            cabinetName: cabinetName
        };
        datagrid.datagrid('load');
    }


    function view(id, type) {
        if(type == 4){
            App.dialog.show({
                css: 'width:1000px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/zd/rent_foregift_order/view.htm?id=" + id
            });
        } else if(type == 5){
            App.dialog.show({
                css: 'width:1000px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/zd/rent_period_order/view.htm?id=" + id
            });
        } else if(type == 6){
            App.dialog.show({
                css: 'width:1000px;height:515px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/zd/rent_insurance_order/view.htm?id="+id
            });
        }
    }

</script>


</div>