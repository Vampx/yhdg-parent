<div class="tab_item">
    <div class="search" style="padding-top: 10px">
        <div class="float_right" style="padding-right: 10px">
            <button class="btn btn_blue" onclick="pay_${pid}_index()">运动轨迹演示</button>
            <button class="btn btn_yellow" onclick="select_${pid}_index()">轨迹列表</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td style="padding-left: 10px" align="right">
                    <input id="begin_time" class="easyui-datetimebox"
                           type="text"
                           style="width:150px;height:27px;">
                    -
                    <input id="end_time" class="easyui-datetimebox" type="text"
                           style="width:150px;height:27px;">
                </td>
            </tr>
        </table>
    </div>
    <div style="width:1270px; height:620px;padding-top: 10px">
        <iframe id="map_frame_${pid}"
                src="${contextPath}/security/basic/baidu_map/view_map.htm" width="65%"
                height="100%" frameborder="0" style="border:0"></iframe>
        <div style="display: inline-block;width:34%;height:610px;">
            <table id="page_table_${pid}">
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">    (function () {
    var win = $('#${pid}'), windowData = win.data('windowData');
    var datagrid = $('#page_table_${pid}');
    datagrid.datagrid({
        fit: true,
        width: '100%',
        height: '100%',
        striped: true,
        pagination: true,
        url: "${contextPath}/security/hdg/battery_order_battery_report_log/select_page.htm?orderId=${id}",
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
                        var html = ' <a style="color: blue;" href="javascript:getLocation(\'lng\',\'lat\')">获取当前位置</a>';
                        html = html.replace(/lng/g, row.lng);
                        return html.replace(/lat/g, row.lat);
                    }
                }
            ]
        ],
        onClickRow : function(index, row){
            markerPoint(row);
        },
        onLoadSuccess: function () {
            datagrid.datagrid('clearChecked');
            datagrid.datagrid('clearSelections');
        }
    });
})();
var myGeo = new BMap.Geocoder();

function getLocation(lng, lat) {
    myGeo.getLocation(new BMap.Point(lng, lat), function (result) {
        if (!result) {
            alert("获取失败");
        } else {
            var address = result.address;
            var datagrid = $('#page_table_${pid}');
            var row = datagrid.datagrid('getSelected');
            if (row) {
                var doc = mapFrame.contentDocument || mapFrame.document;
                var childWindow = mapFrame.contentWindow || mapFrame;
                childWindow.markerAnimation(row.lng,row.lat);
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

function markerPoint(row){
    var childWindow = mapFrame.contentWindow || mapFrame;
    childWindow.markerAnimation(row.lng,row.lat);
}

$('#begin_time').datetimebox({
    onChange: function (date) {
        var startDate = $('#begin_time').datetimebox('getValue');
        var endDate = $('#end_time').datetimebox('getValue');

        if (endDate != '') {
            if (startDate > endDate) {
                $.messager.alert('提示消息', '结束日期必须大于等于开始日期', 'info');
                return;
            }
            $.post("${contextPath}/security/basic/baidu_map/map_data.htm", {
                orderId: '${(id)!''}',
                startDate: startDate,
                endDate: endDate
            }, function (json) {
                var doc = mapFrame.contentDocument || mapFrame.document;
                var childWindow = mapFrame.contentWindow || mapFrame;
                childWindow.createMap(json.data);
            }, 'json');
        }
    }
})

function select_${pid}_index() {
    var startDate = $('#begin_time').datetimebox('getValue');
    var endDate = $('#end_time').datetimebox('getValue');

    if (startDate != '' && startDate > endDate) {
        $.messager.alert('提示消息', '结束日期必须大于等于开始日期', 'info');
        return;
    }
    App.dialog.show({
        css: 'width:854px;height:470px;',
        title: '查看',
        href: "${contextPath}/security/hdg/battery_order_battery_report_log/select_index.htm?orderId=${(id)!''}&startDate=" + startDate + "&endDate=" + endDate
    });
}

function pay_${pid}_index() {
    var startDate = $('#begin_time').datetimebox('getValue');
    var endDate = $('#end_time').datetimebox('getValue');

    if (startDate != '' && startDate > endDate) {
        $.messager.alert('提示消息', '结束日期必须大于等于开始日期', 'info');
        return;
    }
    $.post("${contextPath}/security/basic/baidu_map/map_data.htm", {
        orderId: '${(id)!''}',
        startDate: startDate,
        endDate: endDate
    }, function (json) {
        var doc = mapFrame.contentDocument || mapFrame.document;
        var childWindow = mapFrame.contentWindow || mapFrame;
        childWindow.createWalkingMap(json.data);
    }, 'json');
}

$('#end_time').datetimebox({
    onChange: function (date) {
        var startDate = $('#begin_time').datetimebox('getValue');
        var endDate = $('#end_time').datetimebox('getValue');

        if (startDate != '' && startDate > endDate) {
            $.messager.alert('提示消息', '结束日期必须大于等于开始日期', 'info');
            return;
        }
        $.post("${contextPath}/security/basic/baidu_map/map_data.htm", {
            orderId: '${(id)!''}',
            startDate: startDate,
            endDate: endDate
        }, function (json) {
            var doc = mapFrame.contentDocument || mapFrame.document;
            var childWindow = mapFrame.contentWindow || mapFrame;
            childWindow.createMap(json.data);
        }, 'json');
    }
})

var mapFrame = document.getElementById('map_frame_${pid}');
setTimeout(function () {
    $.post("${contextPath}/security/basic/baidu_map/map_data.htm", {orderId: '${(id)!''}'}, function (json) {
        var doc = mapFrame.contentDocument || mapFrame.document;
        var childWindow = mapFrame.contentWindow || mapFrame;
        childWindow.createMap(json.data);
    }, 'json');
}, 500)
</script>
