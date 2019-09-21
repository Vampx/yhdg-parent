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
            url: "${contextPath}/security/basic/phoneapp/page_rotate_image.htm",
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
                        width: 60,
                        formatter: function (val, row) {
                            return "<img style='width:100px; height:70px;top: 0px;' onclick='preview(\""+ row.imagePath +"\" , \"${controller.appConfig.staticUrl}"+ val +"\")' src='${controller.appConfig.staticUrl}"+ val +"' />"
                        }
                    },
                    {
                        title: '来源',
                        align: 'center',
                        field: 'stringType',
                        width: 70},
                    {
                        title: '排序',
                        align: 'center',
                        field: 'orderNum',
                        width: 40,
                        formatter: function(val, row) {
                            return App.formatSecond(val);
                        }
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
    function view_rotate_image_view(id) {
        App.dialog.show({
            css: 'width:380px;height:300px;',
            title: '查看',
            href: "${contextPath}/security/basic/phoneapp/view_rotate_image_view.htm?id=" + id,
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