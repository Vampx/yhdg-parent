<@app.html>
    <@app.head>
    <script src="${app.toolPath}/bootstrap-typeahead-1.2.2/bootstrap-typeahead.js" type="text/javascript"></script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>
            <div class="content">
                <div class="panel search">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="false"
                                       style="width: 200px;height: 28px;"
                                       value="${(agentId)!''}"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200'
                            "
                                >
                            </td>
                            <td align="right">电池编号：</td>
                            <td>
                                <input type="text" class="text" id="id"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <ul class="tab_menu clearfix" id="monitor_status">
                        <li class="selected" value="0">全部</li>
                        <li value="1">未使用</li>
                        <li value="2">骑行中</li>
                        <li value="3">充电中</li>
                        <li value="4">已充满</li>
                    </ul>
                    <div id="battery_list">
                        <#include 'battery_detail.ftl'>
                    </div>
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>
<script>
    (function () {
        var list = $('#monitor_status li');
        list.click(function () {
            var me = $(this);
            if (me.attr('class') && me.attr('class').indexOf('selected') >= 0) {
                return;
            }
            list.removeClass('selected');
            me.addClass('selected');
            query();
        });
    })();

    function query() {
        var agentId = $('#agent_id').combotree('getValue');
        var id = $('#id').val();
        var monitorStatus = $('#monitor_status .selected').val();
        $.post('${contextPath}/security/hdg/battery_factory_control/battery_detail.htm', {
            agentId: agentId,
            monitorStatus: monitorStatus,
            id: id
        }, function (html) {
            $('#battery_list').html(html)
        }, 'html');

    }

    function queryBatteryChargeOrder(id) {
        App.dialog.show({
            css: 'width:1200px;height:680px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/hdg/battery_charge_record/select_by_battery.htm?batteryId=" + id

        });
    }

    //    window.setInterval('query()',10000); //指定10秒刷新一次

</script>
