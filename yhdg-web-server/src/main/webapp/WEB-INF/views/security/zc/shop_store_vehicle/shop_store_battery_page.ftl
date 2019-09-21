<div class="popup_body">
    <div class="select_routes" >
        <div class="select_box" style="width: 150px; height:402px; float:right;">
            <div class="select_head" >
                <h3>已选列表</h3><a class="a_red" href="javascript:deleteAll()">清空</a>
                <span class="msg">双击添加页面</span>
            </div>
            <div class="select_body" style="top:59px; overflow: hidden;">
                <ul id="selected_container_${pid}">
                <#if batteryList ??>
                    <#list batteryList as battery>
                        <li id="${battery.id}"><span class="text" >${(battery.id)!''}</span></li>
                    </#list>
                </#if>
                </ul>
            </div>
        </div>
    </div>
    <div class="search" style="margin:0 0; padding:2px 0 0 0; border-bottom:none;">
        <table cellspacing="0" cellpadding="0" border="0">
            <tr>
                <td align="right">电池编号：</td>
                <td><input type="text" style="width: 100px" class="text" id="battery_id_${pid}"/>&nbsp;&nbsp;</td>
                <td align="right" width="80">
                    <button class="btn btn_yellow">搜索</button>
                </td>
            </tr>
        </table>
    </div>
    <div class="select_routes" >
        <div class="select_body" style="margin-right: 20px; top:29px; overflow: hidden;">
            <div class="grid" style="width: 490px; height:360px; margin-right: 20px;">
                <table id="table_${pid}"></table>
            </div>
        </div>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red page_ok">确定</button>
    <button class="btn btn_border page_close">关闭</button>
</div>

<script>
    var query_${pid};

    (function() {
        var win=$('#${pid}'), windowData = win.data('windowData');
        var selectedContainer = $('#selected_container_${pid}');
        var datagrid = $('#table_${pid}');

        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/battery/find_not_store_page.htm?type=${batteryType}&category=${category}",
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
                        title: '电池编号',
                        align: 'center',
                        field: 'id',
                        width: 80
                    },
                    {
                        title: 'IMEI',
                        align: 'center',
                        field: 'code',
                        width: 140
                    },
                    {
                        title: '外壳编号',
                        align: 'center',
                        field: 'shellCode',
                        width: 100
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'statusName',
                        width: 120,
                        formatter: function (val, row) {
                            return row.statusName + '/' + row.chargeStatusName;
                        }
                    },
                    {
                        title: '在线',
                        align: 'center',
                        field: 'isOnline',
                        width: 45,
                        formatter: function (val, row) {
                            return val == 1 ? '<a style="color: #00FF00;">是</a>' : '<a style="color: #ff0000;">否</a>';
                        }
                    },
                    {
                        title: '电池类型',
                        align: 'center',
                        field: 'batteryType',
                        width: 85
                    },
                    {
                        title: '业务类型',
                        align: 'center',
                        field: 'categoryName',
                        width: 85
                    }
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            },
            onDblClickRow: function(index, row) {
                if(!windowData.limit || getValues().length < windowData.limit) {
                    add(row);
                } else {
                    if(windowData.limit) {
                        $.messager.alert('提示信息', '只允许选择' + ${batteryCount} + '条记录', 'info');
                    }
                }
            },
            onCheckAll: function(){
                var rows = datagrid.datagrid("getRows");
                for (var i=0; i<rows.length; i++){
                    if(!windowData.limit || getValues().length < windowData.limit) {
                        add(rows[i]);
                    } else {
                        if(windowData.limit) {
                            $.messager.alert('提示信息', '只允许选择' + ${batteryCount} + '条记录', 'info');
                        }
                    }
                }
            },
            onCheck: function(index, row) {
                if(!windowData.limit || getValues().length < windowData.limit) {
                    add(row);
                } else {
                    if(windowData.limit) {
                        $.messager.alert('提示信息', '只允许选择' + ${batteryCount} + '条记录', 'info');
                    }
                }
            },
            queryParams:{
                agentId:${(agentId)!''},
                status: 1
            }
        });

        var line =
                '<li id="ID">' +
                '<span class="text">ID</span>' +
                '<p class="icon_btn"><a class="delete" title="删除" href="javascript:void(0)" onclick="deleteRow(this)"><span class="icon"></span></a>' +
                '</li>';


        var add = function(row) {
            if($('#selected_container_${pid} li[id="' + row.id +'"]').length > 0) {
                alert("电池已存在");
                return;
            }
            var batteryIds = $(".shop_store_vehicle_battery p");
            for (var j = 0; j < batteryIds.length; j++) {
                if(batteryIds[j].id == row.id) {
                    alert("电池已存在");
                    return;
                }
            }
            var html = line.replace(/ID/g, row.id);
            selectedContainer.append(html);
        };

        var getSelected = function() {
            var list = [];
            selectedContainer.find('li').each(function() {
                var me = $(this);
                list.push({
                    id: me.attr('id')
                });
            });
            return list;
        };

        var setValues = function(list) {
            add(list);
        };

        var getValues = function() {
            return getSelected();
        };

        var query = function() {
            datagrid.datagrid('options').queryParams = {
                id: $('#battery_id_${pid}').val(),
                agentId:${(agentId)!''},
                category:${category},
                status: 1
            };
            datagrid.datagrid('load');
        };
        window.query_${pid} = query;

        win.find('.search .btn_yellow').click(function() {
            query();
        });

        win.find('button.page_ok').click(function() {
            var values = getValues();
            if(values) {
                if (values.length == 0) {
                    $.messager.alert('提示信息', '请选择电池', 'info');
                    return false;
                }
                var batteryCount = ${batteryCount};
                var liBattery = $(".shop_store_vehicle_battery p").length;
                var valuesBattery = values.length;
                if (liBattery == 0) {
                    if(values.length != ${batteryCount}) {
                        $.messager.alert('提示信息', '只允许选择' + batteryCount + '条记录', 'info');
                        return;
                    }
                }
                var selectBattery = batteryCount-liBattery;
                if (valuesBattery != selectBattery) {
                    $.messager.alert('提示信息', '只允许选择' + selectBattery + '条记录', 'info');
                    return;
                }
                if(windowData.ok(values)) {
                    win.window('close');
                }
            }
        });
        win.find('button.page_close').click(function() {
            win.window('close');
        });

        if(windowData.values) {
            setValues(windowData.values);
        }

    })();

    function deleteRow(obj) {
        $(obj).closest('li').remove();
    }

    function deleteAll() {
        $('#selected_container_${pid}').html("");
    }

</script>