<div class="popup_body">
    <div style="width:1350px; height:360px; padding-top: 6px;">
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
            url: "${contextPath}/security/basic/customer/page.htm",
            fitColumns: true,
            pageSize: 50,
            pageList: [50, 100],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {field: 'checkbox', checkbox: true},
                    {title: '商户', align: 'center', field: 'partnerName', width: 40},
                    {title: '运营商', align: 'center', field: 'agentName', width: 60},
                    {title: '姓名', align: 'center', field: 'fullname', width: 30},
                    {
                        title: '手机号码',
                        align: 'center',
                        field: 'mobile',
                        width: 50,
                        formatter: function (val) {
                            return val;
                        }
                    },
                    {
                        title: '充值/赠送',
                        align: 'center',
                        field: 'balance',
                        width: 60,
                        formatter: function (val, row) {
                            return Number(row.balance / 100).toFixed(2) + "/" + Number(row.giftBalance / 100).toFixed(2) ;
                        }
                    },
                    {title: '注册时间', align: 'center', field: 'createTime', width: 70},
                    {
                        title: '来源',
                        align: 'center',
                        field: 'registerTypeName',
                        width: 20
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'isActive',
                        width: 30,
                        formatter: function (val) {
                            if (val == 1) {
                                return '启用';
                            } else {
                                return '禁用';
                            }
                        }
                    },
                    {
                        title: '注册终端',
                        align: 'center',
                        field: 'belongCabinetName',
                        width: 50
                    },
                    {
                        title: '当前终端',
                        align: 'center',
                        field: 'balanceCabinetName',
                        width: 50
                    }
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            },onDblClickRow:  function(rowIndex, rowData) {
                select_${pid}(rowData.id);
            },
            queryParams:{
                type: '${(entity.type)!''}',
                partnerId: '${(entity.partnerId)!''}',
                fullname: '${(entity.fullname)!''}',
                mobile: '${(entity.mobile)!''}',
                batteryId: '${(entity.batteryId)!''}',
                belongCabinetId: '${(entity.belongCabinetId)!''}',
                belongCabinetName: '${(entity.belongCabinetName)!''}',
                agentId: '${(entity.agentId)!''}',

                zdRefundedForegiftFlag:'${(entity.zdRefundedForegiftFlag)!''}'
            }
        });

    })();

    function select_${pid}(id) {
        App.dialog.show({
            css: 'width:850px;height:510px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/basic/customer/view.htm?id=" + id
        });
    }

    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });

</script>

