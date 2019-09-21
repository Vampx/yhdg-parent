<div class="popup_body">
    <div style="width:1050px; height:360px; padding-top: 6px;">
        <table id="normal_heart_report_table">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
    <script>
        $(function () {
            $('#normal_heart_report_table').datagrid({
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                fit: true,
                striped: true,
                singleSelect: true,
                collapsible: true,
                pagination: true,
                url: "${contextPath}/security/hdg/battery_report/view_normal_heart_report_page.htm?batteryId=${(batteryId)!''}&createTime=${app.format_date_time(createTime)}",
                pageSize: 20,
                pageList: [20],
                idField: 'batteryId',
                frozenColumns: [[
                    {
                        title: '电池编号',
                        align: 'center',
                        field: 'batteryId',
                        width: 100
                    },
                    {
                        title: '上报时间',
                        align: 'center',
                        field: 'createTime',
                        width: 140
                    },
                ]],
                columns: [
                    [
                        {
                            title: '电压',
                            align: 'center',
                            field: 'voltage',
                            width: 50,
                            formatter: function (val) {
                                return Number(val / 1000).toFixed(2) + "V";
                            }
                        },
                        {
                            title: '电流',
                            align: 'center',
                            field: 'electricity',
                            width: 50,
                            formatter: function (val) {
                                return Number(val / 1000).toFixed(2) + "A";
                            }
                        },
                        {
                            title: '剩余电量',
                            align: 'center',
                            field: 'volume',
                            width: 60,
                            formatter: function (val) {
                                if(val == null){
                                    return "";
                                }
                                return  val  + "%";
                            }
                        },
                        {
                            title: '温度',
                            align: 'center',
                            field: 'temp',
                            width: 60
                        },
                        {
                            title: '经度/纬度',
                            align: 'center',
                            field: 'lat',
                            width: 80,
                            formatter: function(val, row) {
                                var lng = row.lng;
                                var lat = row.lat;
                                if(lng == null && lat == null){
                                    return "";
                                }
                                return Number(row.lng).toFixed(6) + "/" + Number(row.lat).toFixed(6);
                            }
                        },
                        {
                            title: '位置类型',
                            align: 'center',
                            field: 'locTypeName',
                            width: 60
                        },
                        {
                            title: '信号',
                            align: 'center',
                            field: 'currentSignal',
                            width: 50
                        },
                        {
                            title: '类型',
                            align: 'center',
                            field: 'heartTypeName',
                            width: 50
                        },
                        {
                            title: '位置',
                            align: 'center',
                            field: 'address',
                            width: 180
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 150,
                            formatter: function (val, row) {
                                var html = '';
                                    html += '<a href="javascript:view(\'ID\',\'ReportTime\')">查看</a>';
                                    html += ' <a style="color: blue;" href="javascript:getLocation(\'lng\',\'lat\')">获取当前位置</a>';
                                return html.replace(/ID/g, row.batteryId).replace(/ReportTime/g, row.createTime).replace(/lng/g, row.lng).replace(/lat/g, row.lat);
                            }
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#normal_heart_report_table').datagrid('clearChecked');
                    $('#normal_heart_report_table').datagrid('clearSelections');
                }
            });
        });

        var myGeo = new BMap.Geocoder();

        function getLocation(lng, lat) {
            if(lng == null || lat == null || lng == 'null'  || lat == 'null'){
                $.messager.alert('提示信息', '经纬度不能为空', 'info');
                return;
            }
            myGeo.getLocation(new BMap.Point(lng, lat), function (result) {
                if (!result) {
                    alert("获取失败");
                } else {
                    var address = result.address;
                    var datagrid = $('#normal_heart_report_table');
                    var row = datagrid.datagrid('getSelected');
                    if (row) {
                        var index = datagrid.datagrid('getRowIndex', row);
                        datagrid.datagrid('updateRow', {index: index, row: {address: address}});
                    }
                }
            });
        }

        function view(batteryId, createTime) {
            App.dialog.show({
                css: 'width:886px;height:647px;overflow:scroll;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery_report/view.htm?batteryId=" + batteryId + "&createTime=" + createTime
            });
        }

        $('#close_${pid}').click(function() {
            $('#${pid}').window('close');
        });

    </script>