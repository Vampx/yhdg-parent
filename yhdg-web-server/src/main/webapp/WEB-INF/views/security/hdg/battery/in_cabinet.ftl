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
            url: "${contextPath}/security/hdg/battery/page.htm",
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
                    {
                        title: '电池编号',
                        align: 'center',
                        field: 'id',
                        width: 50
                    },
                    {
                        title: 'IMEI',
                        align: 'center',
                        field: 'code',
                        width: 80
                    },
                    {
                        title: '外壳编号',
                        align: 'center',
                        field: 'shellCode',
                        width: 60
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'statusName',
                        width: 60,
                        formatter: function (val, row) {
                            return row.statusName + '/' + row.chargeStatusName;
                        }

                    },
                    {
                        title: '上线状态',
                        align: 'center',
                        field: 'upLineStatusName',
                        width: 60
                    },
                    {
                        title: '当前电量',
                        align: 'center',
                        field: 'volume',
                        width: 50
                    },
                    {
                        title: '版本',
                        align: 'center',
                        field: 'version',
                        width: 30
                    },
                    {
                        title: '在线',
                        align: 'center',
                        field: 'isOnline',
                        width: 30,
                        formatter: function (val, row) {
                            return val == 1 ? '<a style="color: #00FF00;">是</a>' : '<a style="color: #ff0000;">否</a>';
                        }
                    },
                    {
                        title: '电池类型',
                        align: 'center',
                        field: 'batteryType',
                        width: 60
                    },
                    {
                        title: '上报时间',
                        align: 'center',
                        field: 'reportTime',
                        width: 90
                    },
                    {
                        title: '当前信号',
                        align: 'center',
                        field: 'currentSignal',
                        width: 40
                    },
                    {
                        title: '品牌',
                        align: 'center',
                        field: 'brandName',
                        width: 40
                    },
                    {title: '客户姓名', align: 'center', field: 'customerFullname', width: 40},
                    {title: '客户手机', align: 'center', field: 'customerMobile', width: 40},
                    {title: '所在站点', align: 'center', field: 'cabinetName', width: 40},
                    {title: '所在柜口', align: 'center', field: 'boxNum', width: 40}
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            },onDblClickRow:  function(rowIndex, rowData) {
                select_${pid}(rowData.id);
            },
            queryParams:{
                isOnline: '${(entity.isOnline)!''}',
                agentId: '${(entity.agentId)!''}',
                cabinetId: '${(entity.cabinetId)!''}',
                minVolume: '${(entity.minVolume)!''}',
                maxVolume: '${(entity.maxVolume)!''}',
                queryBeginTime:  <#if entity.queryBeginTime ??>'${app.format_date_time(entity.queryBeginTime)}', <#else> '${(entity.queryBeginTime)!''}',</#if>
                queryEndTime: <#if entity.queryEndTime ??>'${app.format_date_time(entity.queryEndTime)}', <#else> '${(entity.queryEndTime)!''}',</#if>
                status: '${(entity.status)!''}',
                chargeStatus: '${(entity.chargeStatus)!''}',
                type: '${(entity.type)!''}',

                id: '${(entity.id)!''}',
                simMemo: '${(entity.simMemo)!''}',
                code: '${(entity.code)!''}',
                shellCode: '${(entity.shellCode)!''}',
                qrcode: '${(entity.qrcode)!''}',
                customerFullname: '${(entity.customerFullname)!''}',
                customerMobile: '${(entity.customerMobile)!''}',
                version: '${(entity.version)!''}',
                queryAntiVersion: '${(entity.queryAntiVersion)!''}',

                inCabinetFlag:'${(entity.inCabinetFlag)!''}'
            }
        });

    })();

    function select_${pid}(id) {
        App.dialog.show({
            css: 'width:780px;height:512px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/hdg/battery/view.htm?id=" + id
        });
    }

    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });

</script>

