<style>
    .panel .popup_body .datagrid-body .datagrid-cell{
        height: 70px;
        line-height: 70px;
    }
    .panel .popup_body .datagrid-body .datagrid-cell input{
        margin-top: 26px;
    }
</style>
<div class="panel grid_wrap">
    <div class="toolbar clearfix">
        <div class="float_right">
            <button class="btn btn_green" onclick="add_rotate_image()">添加</button>
        </div>
        <h3>轮播图上传</h3>
    </div>
    <div class="grid" style="height:425px;">
        <input type="hidden" id="sourceId" value="${(entity.id)!''}">
        <table id="page_table_${pid}"></table>
    </div>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid);
        var ok = function () {
            var s=1;
            return true;
        }
        var entityId =win.data('entityId');
        win.data('ok', ok);
        $("#page_table_${pid}").datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/alipayfw/page_rotate_image.htm",
            queryParams:{'sourceId':entityId},
            pageSize: 10,
            pageList: [10, 50, 100],
            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: function () { return 'height:7.0em;'; },
            columns: [
                [
                    {
                        title: '缩略图',
                        align: 'center',
                        field: 'imagePath',
                        width: 100,
                        formatter: function (val, row) {
                            return "<img style='width:100px; height:70px;' onclick='preview(\""+ row.imagePath +"\" , \"${controller.appConfig.staticUrl}"+ val +"\")' src='${controller.appConfig.staticUrl}"+ val +"' />"
                        }
                    },
                    {
                        title: '来源',
                        align: 'center',
                        field: 'stringType',
                        width: 70
                    },
                    {
                        title: '类型',
                        align: 'center',
                        field: 'categoryType',
                        width: 70
                    },
                    {
                        title: '排序',
                        align: 'center',
                        field: 'orderNum',
                        width: 40
                    },
                    {
                        title: '是否显示',
                        align: 'center',
                        field: 'isShow',
                        width: 60,
                        formatter: function(val, row) {
                            return val == 1 ? "是" : "否";
                        }
                    },
                    {
                        title: '链接',
                        align: 'center',
                        field: 'url',
                        width: 100
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'id',
                        width: 90,
                        formatter: function(val, row)  {
                            var html = '<a href="javascript:view_rotate_image_view(ID)">查看</a>';
                            html += ' <a href="javascript:edit_rotate_image_edit(ID)">修改</a>'
                            html += ' <a href="javascript:remove_rotate_image_remove(ID)">删除</a>'
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ],
            onLoadSuccess:function() {
                $('#page_table').datagrid('clearChecked');
                $('#page_table').datagrid('clearSelections');
            }
        });
    })()
    function add_rotate_image() {
        App.dialog.show({
            css: 'width:380px;height:300px;',
            title: '新建',
            href: "${contextPath}/security/basic/alipayfw/add_rotate_image.htm?sourceId="+$('#sourceId').val(),
            event: {
                onClose: function() {
                    reload();
                }
            }
        });
    }
    function edit_rotate_image_edit(id) {
        App.dialog.show({
            css: 'width:380px;height:300px;',
            title: '修改',
            href: "${contextPath}/security/basic/alipayfw/edit_rotate_image_edit.htm?id=" + id,
            event: {
                onClose: function() {
                    var datagrid = $('#page_table');
                    datagrid.datagrid('reload');
                },
                onLoad: function() {
                }
            }
        });
    }
    function view_rotate_image_view(id) {
        App.dialog.show({
            css: 'width:380px;height:300px;',
            title: '修改',
            href: "${contextPath}/security/basic/alipayfw/view_rotate_image_view.htm?id=" + id,
            event: {
                onClose: function() {
                    var datagrid = $('#page_table');
                    datagrid.datagrid('reload');
                },
                onLoad: function() {
                }
            }
        });
    }

    function remove_rotate_image_remove(id) {
        $.messager.confirm('提示信息', '确认删除?', function(ok) {
            if(ok) {
                $.post('${contextPath}/security/basic/alipayfw/remove_rotate_image_remove.htm', {
                    id: id
                }, function (json) {
                    if (json.success) {
                        $.messager.alert('info', '操作成功', 'info');
                        reload();
                    } else {
                        $.messager.alert('提示消息', json.message, 'info');
                    }
                }, 'json');
            }
        });
    }
    function reload() {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid('reload');
    }
    function preview(path) {
        App.dialog.show({
            options:'maximized:true',
            title: '查看',
            href: "${contextPath}/security/main/preview.htm?path=" + path
        });
    }

</script>