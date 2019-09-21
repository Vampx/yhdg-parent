<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <input id="settingId_${pid}" type="hidden" value="${(settingId)!''}">
            <button class="btn btn_red" id="ok_${pid}">点击添加</button>
        </div>
        <h4>
            <div>
                设备编号：
                <input style="width: 100px" class="text" id="code_${pid}" type="text">&nbsp;&nbsp;
                <button class="btn btn_yellow" id="query_${pid}">搜索</button>
            </div>
        </h4>
    </div>
    <div style="width:700px; height:310px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>

    <script>
        (function() {
            var pid='${pid}';
            var wins =$('#${urlPid}');
            var win = $('#${pid}'),
                    windowData = win.data('windowData');
            var datagrid = $('#page_table_${pid}');
            datagrid.datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/hdg/exchange_installment_station/page.htm?agentId="+${(agentId)!''},
                fitColumns: true,
                pageSize: 10,
                pageList: [10, 50, 100],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {
                            field: 'checkbox', checkbox: true
                        },
                        {
                            title: '站点编号',
                            align: 'center',
                            field: 'id',
                            width: 180
                        },
                        {
                            title: '站点名称',
                            align: 'center',
                            field: 'stationName',
                            width: 180
                        },
                        {
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 180
                        },
                        {
                            title: '启用',
                            align: 'center',
                            field: 'activeStatus',
                            width: 40,
                            formatter: function (val, row) {
                                var activeStatus = row.activeStatus == 1 ? '是' : '否';
                                return activeStatus;
                            }
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    datagrid.datagrid('clearChecked');
                    datagrid.datagrid('clearSelections');
                }
            });

            $('#query_${pid}').click(function() {
                datagrid.datagrid('options').queryParams = {
                    id: $('#code_${pid}').val()
                };
                datagrid.datagrid('load');
            });

            $('#ok_${pid}').click(function() {
                var settingId = $('#settingId_${pid}').val();
                var checked = datagrid.datagrid('getChecked');
                if(checked.length > 0) {
                    var boolean = true;
                    if(settingId !=null && settingId !="" && settingId !=undefined){
                        var stationIds = [];
                        for(var i=0; i<checked.length; i++){
                            stationIds.push(checked[i].id);
                        }
                        if(stationIds.length>0){
                            $.ajax({
                                cache: false,
                                async: false,
                                type: 'GET',
                                url: '${contextPath}/security/hdg/exchange_installment_station/add_installment_station.htm',
                                data: {
                                    stationIds:stationIds,
                                    settingId:settingId
                                },
                                dataType: 'json',
                                success: function (json) {
                                <@app.json_jump/>
                                    if (json.success) {
                                        $.messager.alert('提示信息', json.message, 'info');
                                    } else {
                                        boolean = false;
                                        $.messager.alert('提示信息', json.message, 'info');
                                    }
                                }
                            })
                        }
                    }
                    wins.data("checked",checked);
                    if(boolean){
                        win.window('close');
                    }
                } else {
                    $.messager.alert('提示信息', '请选择设备');
                }

            });


            $('#close_${pid}').click(function() {
                $('#${pid}').window('close');
            });

        })();

    </script>