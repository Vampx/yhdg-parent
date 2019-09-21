<div class="popup_body">
    <div class="select_routes" >
        <div class="select_box" style="width: 200px; height:402px; float:right;">
            <div class="select_head" >
                <h3>已选列表</h3><a class="a_red" href="javascript:deleteAll()">清空</a>
                <span class="msg">双击添加页面</span>
            </div>
            <div class="select_body" style="top:59px; overflow: hidden;">
                <ul id="selected_container_${pid}">
                <#if agentBatteryTypeList ??>
                    <#list agentBatteryTypeList as agentBatteryType>
                        <li batteryType="${agentBatteryType.batteryType}" typeName ="${(agentBatteryType.typeName)!''}"><span class="text" >${(agentBatteryType.typeName)!''}</span></li>
                    </#list>
                </#if>
                </ul>
            </div>
        </div>
    </div>
    <div class="search" style="margin:0 0; padding:2px 0 0 0; border-bottom:none;">
        <table cellspacing="0" cellpadding="0" border="0">
            <tr>
                <td align="right">类型名称：</td>
                <td><input type="text" style="width: 100px" class="text"  id="type_name_${pid}"/></td>
                <td align="right" width="80">
                    <button class="btn btn_yellow">搜索</button>
                </td>
            </tr>
        </table>
    </div>
    <div class="select_routes" >
        <div class="select_body" style="margin-right: 20px; top:29px; overflow: hidden;">
            <div class="grid" style="width: 420px; height:360px; margin-right: 20px;">
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
            url: "${contextPath}/security/basic/agent_battery_type/page.htm?agentId=${(agentId)!''}&cabinetId=${(cabinetId)!''}&unbind=1",
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
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 40
                    },
                    {
                        title: '类型名称',
                        align: 'center',
                        field: 'typeName',
                        width: 80
                    },
                    {
                        title: '额定电压(V)',
                        align: 'center',
                        field: 'ratedVoltage',
                        width: 80,
                        formatter: function (val) {
                            return Number(val / 1000);
                        }
                    },
                    {
                        title: '额定容量(Ah)',
                        align: 'center',
                        field: 'ratedCapacity',
                        width: 80,
                        formatter: function (val) {
                            return Number(val / 1000);
                        }
                    },
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
                        $.messager.alert('提示信息', '只允许选择' + windowData.limit + '条记录', 'info');
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
                            $.messager.alert('提示信息', '只允许选择' + windowData.limit + '条记录', 'info');
                        }
                    }
                }
            },
            onCheck: function(index, row) {
                if(!windowData.limit || getValues().length < windowData.limit) {
                    add(row);
                } else {
                    if(windowData.limit) {
                        $.messager.alert('提示信息', '只允许选择' + windowData.limit + '条记录', 'info');
                    }
                }
            }
        });

        var line =
                '<li batteryType="BATTERY_TYPE" typeName="TYPE_NAME">' +
//                '<span class="text">BATTERY_TYPE/TYPE_NAME</span>' +
                '<span class="text">TYPE_NAME</span>' +
                '<p class="icon_btn"><a class="delete" title="删除" href="javascript:void(0)" onclick="deleteRow(this)"><span class="icon"></span></a>' +
                '</li>';


        var add = function(agentBatteryType) {
            if($('#selected_container_${pid} li[batteryType="' + agentBatteryType.batteryType +'"]').length > 0) {
                alert("电池类型已存在");
                return;
            }
            var typeName;
            if (agentBatteryType.typeName != null) {
                typeName = agentBatteryType.typeName;
            } else {
                typeName = "";
            }
            var html = line.replace(/BATTERY_TYPE/g, agentBatteryType.batteryType).replace(/TYPE_NAME/g, typeName);
            selectedContainer.append(html);
        };

        var getSelected = function() {
            var list = [];
            selectedContainer.find('li').each(function() {
                var me = $(this);
                list.push({
                    batteryType: me.attr('batteryType')
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
                typeName: $('#type_name_${pid}').val()
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
                if(windowData.limit && windowData.limit != values.length) {
                    $.messager.alert('提示信息', '只允许选择' + windowData.limit + '条记录', 'info');
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