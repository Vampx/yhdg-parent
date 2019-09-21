<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">
        <#if editFlag ==1>
            <button class="btn btn_green add">新建</button>
        </#if>
        </div>
        <h3>包时段套餐明细</h3>
    </div>
    <div class="grid" style="height:325px;">
        <table id="page_table_point_${pid}"></table>
    </div>
</div>

<script>
    (function () {
        var win = $('#${pid}');
        $('#page_table_point_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            url: "${contextPath}/security/hdg/packet_price_detail/page.htm?priceId=${priceId}",
            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {field: 'checkbox', checkbox: true},
                    {
                        title: '时段类型',
                        align: 'center',
                        field: 'type',
                        width: 50,
                        formatter: function (val) {
                        <#list TypeEnum as e>
                            if (${e.getValue()} == val)
                            return '${e.getName()}';
                        </#list>
                        }
                    },
                    {
                        title: '价格(元)',
                        align: 'center',
                        field: 'price',
                        width: 50,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'id',
                        width: 60,
                        formatter: function(val, row) {
                            var html = '<a href="javascript:view_${pid}(TYPE)">查看</a>';
                        <#if editFlag ==1>
                            html += ' <a href="javascript:edit_${pid}(TYPE)">修改</a>';
                            html += ' <a href="javascript:del_${pid}(TYPE)">删除</a>';
                        </#if>
                            return html.replace(/TYPE/g, row.type);
                        }
                    }
                ]
            ]
        });

        win.find('button.add').click(function () {
            App.dialog.show({
                css: 'width:475px;height:280px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/hdg/packet_price_detail/add.htm?priceId=${priceId}",
                event: {
                    onClose: function () {
                        list_point_reload();
                    }
                }
            });
        });

        var ok = function () {
            return true;
        };

        win.data('ok', ok);
    })();

    function view_${pid}(type) {
        App.dialog.show({
            css: 'width:480px;height:280px;',
            title: '查看',
            href: "${contextPath}/security/hdg/packet_price_detail/view.htm?priceId=${priceId}&type=" + type,
            event: {
                onClose: function () {
                    list_point_reload();
                }
            }
        });
    }

    function edit_${pid}(type) {
        App.dialog.show({
            css: 'width:586px;height:280px;overflow:visible;',
            title: '修改',
            href: "${contextPath}/security/hdg/packet_price_detail/edit.htm?priceId=${priceId}&type=" + type,
            event: {
                onClose: function () {
                    list_point_reload();
                }
            }
        });
    }

    function del_${pid}(type) {
        $.messager.confirm('提示信息', '确认删除?', function(ok) {
            if (ok) {
                $.post("${contextPath}/security/hdg/packet_price_detail/delete.htm?priceId=${priceId}&type=" + type, function (json) {
                    if (json.success) {
                        list_point_reload();
                        $.messager.alert('提示消息', '操作成功', 'info');
                        reload();
                    } else {
                        $.messager.alert('提示消息', json.message, 'info');
                    }
                }, 'json');
            }
        });
    }

    function list_point_reload() {
        var datagrid = $('#page_table_point_${pid}');
        datagrid.datagrid('reload');
    }

</script>