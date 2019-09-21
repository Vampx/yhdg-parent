<div class="popup_body">
    <div style="width:800px; height:400px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<script type="text/javascript">
    (function () {
        var win = $('#${pid}'), windowData = win.data('windowData');
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/battery_order_battery_report_log/select_page.htm?orderId=${orderId}&queryBeginTime=${(startDate)!''}&queryEndTime=${(endDate)!''}",
            fitColumns: true,
            pageSize: 50,
            pageList: [50, 100],
            idField: 'orderId',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: 'ID',
                        align: 'center',
                        field: 'id',
                        width: 10,
                        formatter: function (val, row) {
                            var options = datagrid.datagrid('getPager').data("pagination").options;
                            var page = options.pageNumber;//当前页数
                            var pageSize = options.pageSize;
                            var total = datagrid.datagrid('getData').total;
                            var id = (options.pageNumber - 1) * options.pageSize + datagrid.datagrid('getRowIndex', row) + 1;
                            if (id == 1) {
                                return "起点"
                            } else if (id == total) {
                                return "终点"
                            } else {
                                return id;
                            }
                        }
                    },
                    {
                        title: '上报时间',
                        align: 'center',
                        field: 'reportTime',
                        width: 25
                    },
                    {
                        title: '当前电量(%)',
                        align: 'center',
                        field: 'volume',
                        width: 15
                    },
                    {
                        title: '经度',
                        align: 'center',
                        field: 'lng',
                        width: 20,
                        formatter: function (val, row) {
                            return val.toFixed(6)
                        }
                    },
                    {
                        title: '纬度',
                        align: 'center',
                        field: 'lat',
                        width: 20,
                        formatter: function (val, row) {
                            return val.toFixed(6)
                        }
                    },
                    {
                        title: '经纬类型',
                        align: 'center',
                        field: 'coordinateType',
                        width: 20
                    },
                    {
                        title: '地址',
                        align: 'center',
                        field: 'address',
                        width: 25
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 15,
                        formatter: function (val, row) {
                            var html = ' <a style="color: blue;" href="javascript:getLocation_${pid}(\'lng\',\'lat\')">获取当前位置</a>';
                            html = html.replace(/lng/g, row.lng);
                            return html.replace(/lat/g, row.lat);
                        }
                    }
                ]
            ],
            onLoadSuccess: function () {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });
    })();
    var myGeo = new BMap.Geocoder();

    function getLocation_${pid}(lng, lat) {
        myGeo.getLocation(new BMap.Point(lng, lat), function (result) {
            if (!result) {
                alert("获取失败");
            } else {
                var address = result.address;
                var datagrid = $('#page_table_${pid}');
                var row = datagrid.datagrid('getSelected');
                if (row) {
                    var index = datagrid.datagrid('getRowIndex', row);
                    datagrid.datagrid('updateRow', {index: index, row: {address: address}});
                    $.post("${contextPath}/security/hdg/battery_order_battery_report_log/update_address.htm?", {
                        orderId: row.orderId,
                        reportTime: row.reportTime,
                        address: address,
                    }, function (json) {
                    }, 'json');
                }
            }
        });
    }
</script>

