<div class="popup_body">
    <div class="select_routes" >
        <div class="select_box" style="width: 200px; height:402px; float:right;">
            <div class="select_head" >
                <h3>已选列表</h3><a class="a_red" href="javascript:deleteAll()">清空</a>
                <span class="msg">双击添加页面</span>
            </div>
            <div class="select_body" style="top:59px; overflow: hidden;">
                <ul id="selected_container_${pid}">
                <#if cabinetList ??>
                    <#list cabinetList as cabinet>
                        <li cabinetId="${cabinet.id}" cabinetName ="${(cabinet.cabinetName)!''}"><span class="text" >${(cabinet.cabinetName)!''}</span></li>
                    </#list>
                </#if>
                </ul>
            </div>
        </div>
    </div>
    <div class="search" style="margin:0 0; padding:2px 0 0 0; border-bottom:none;">
        <table cellspacing="0" cellpadding="0" border="0">
            <tr>
                <td align="right">换电站编号：</td>
                <td><input type="text" style="width: 100px" class="text" id="cabinet_id_${pid}"/>&nbsp;&nbsp;</td>
                <td align="right">换电站名称：</td>
                <td><input type="text" style="width: 100px" class="text"  id="cabinet_name_${pid}"/></td>
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
            url: "${contextPath}/security/hdg/cabinet/page.htm?agentId=${(agentId)!''}&priceFlag=1",
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
                        title: '所属运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 60
                    },
                    {
                        title: '换电站编号',
                        align: 'center',
                        field: 'id',
                        width: 60
                    },
                    {title: '换电站名称', align: 'center', field: 'cabinetName', width: 60},
                    {
                        title: '地址', align: 'center', field: 'address', width: 60
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
                '<li cabinetId="CABINET_ID" cabinetName="CABINET_NAME">' +
                '<span class="text">CABINET_ID/CABINET_NAME</span>' +
                '<p class="icon_btn"><a class="delete" title="删除" href="javascript:void(0)" onclick="deleteRow(this)"><span class="icon"></span></a>' +
                '</li>';


        var add = function(cabinet) {
            if($('#selected_container_${pid} li[cabinetId="' + cabinet.id +'"]').length > 0) {
                alert("换电柜已存在");
                return;
            }
            var name;
            if (cabinet.cabinetName != null) {
                name = cabinet.cabinetName;
            } else {
                name = "";
            }
            var html = line.replace(/CABINET_ID/g, cabinet.id).replace(/CABINET_NAME/g, name);
            selectedContainer.append(html);
        };

        var getSelected = function() {
            var list = [];
            selectedContainer.find('li').each(function() {
                var me = $(this);
                list.push({
                    cabinetId: me.attr('cabinetId')
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
                id: $('#cabinet_id_${pid}').val(),
                cabinetName: $('#cabinet_name_${pid}').val()
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