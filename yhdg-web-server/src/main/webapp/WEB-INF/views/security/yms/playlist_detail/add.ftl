<style>
    .panel .popup_body .datagrid-body .datagrid-cell{
        height: 70px;
        line-height: 70px;
    }
    .panel .popup_body .datagrid-body .datagrid-cell input{
        margin-top: 26px;
    }
</style>
<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_red" id="ok_${pid}">点击添加</button>
        </div>
        <h4>
            <div>
                素材名称：
                <input style="width: 100px" class="text" id="material_name_${pid}" type="text">&nbsp;&nbsp;
                <#--<a class="btn_yellow" id="query_win_5">搜 索</a>-->
                <button class="btn btn_yellow" onclick="page_query()">搜 索</button>
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
            var win = $('#${pid}'), windowData = win.data('windowData');
            var datagrid = $('#page_table_${pid}');
            datagrid.datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/yms/material/page.htm?agentId=${agentId!0}",
                fitColumns: true,
                pageSize: 10,
                pageList: [10, 50, 100],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: function () { return 'height:7.0em;'; },
                columns: [
                    [
                        {
                            field: 'checkbox', checkbox: true
                        },
                        {
                            title: '缩略图',
                            align: 'center',
                            field: 'coverPath',
                            width: 60,
                            formatter: function (val, row) {
                                return "<img style='width:100px; height:70px;top: 0px;' onclick='preview(\""+ row.coverPath +"\" , \"${controller.appConfig.staticUrl}"+ val +"\")' src='${controller.appConfig.staticUrl}"+ val +"' />"
                            }
                        },
                        {title: '名称', align: 'center', field: 'materialName', width: 70},
                        {
                            title: '时长',
                            align: 'center',
                            field: 'duration',
                            width: 40,
                            formatter: function(val, row) {
                                return App.formatSecond(val);
                            }
                        },
                        {
                            title: '转换进度',
                            align: 'center',
                            field: 'convertProgress',
                            width: 40
                        },
                        {
                            title: '大小',
                            align: 'center',
                            field: 'size',
                            width: 40,
                            formatter: function (val, row) {
                                return App.fileSize(val);
                            }
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    datagrid.datagrid('clearChecked');
                    datagrid.datagrid('clearSelections');
                }
            });

            $('#ok_${pid}').click(function() {
                var checked = datagrid.datagrid('getChecked');
                if(checked.length > 0) {
                    windowData.ok(checked);
                    win.window('close');
                } else {
                    $.messager.alert('提示信息', '请选择素材');
                }

            });


            $('#close_${pid}').click(function() {
                $('#${pid}').window('close');
            });

        })();

        function page_query() {
            var datagrid = $('#page_table_${pid}');
            var materialName = $('#material_name_${pid}').val();
            datagrid.datagrid('options').queryParams = {
                materialName: materialName
            };
            datagrid.datagrid('load');
        }
        function preview(path) {
            App.dialog.show({
                options:'maximized:true',
                title: '查看',
                href: "${contextPath}/security/main/preview.htm?path=" + path
            });
        }
    </script>


</div>