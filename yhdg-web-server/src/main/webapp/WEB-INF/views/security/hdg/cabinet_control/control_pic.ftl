<@app.html>
    <@app.head>
    <script>
        function queryPage(pageNo) {
            var cabinetName = $('#cabinet_name').val();
            var agentId = $('#agent_id').combotree('getValue');
            $.post('${contextPath}/security/hdg/cabinet_control/index_cabinet.htm', {
                page: pageNo || 1,
                pageSize: 10,
                cabinetName : cabinetName,
                agentId: agentId
            }, function(html) {
                $('.site_item').remove();
                $('.paging').remove();
                $('.site_list').append(html);
            }, 'html');
        }

    </script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>

            <div class="content">
                <div class="panel grid_wrap" style="top: 0;">
                    <div class="site_monitor">
                        <div class="site_count">
                            <div class="float_left">系统共计
                                <span>${(cabinetCount)!''}个充电站点</span>
                            </div>
                            <div class="float_left" style="margin-left: 15px;">
                                运营商：
                                <td>
                                    <input id="agent_id" class="easyui-combotree" editable="false" style="height: 30px;"
                                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                            method:'get',
                                            valueField:'id',
                                            textField:'text',
                                            editable:false,
                                            multiple:false,
                                            panelHeight:'200',
                                            onClick: function(node) {
                                            }
                                        "
                                    >
                                </td>
                                换电柜名称：<input type="text" class="text" id="cabinet_name"/>
                                <button class="btn btn_yellow" onclick="queryPage()">搜索</button>
                            </div>
                            <div class="float_right">总计：
                                <span id="total_full_volume_count"></span>
                                <span id="total_wait_take_count"></span>
                                <span id="total_empty_box_count"></span>
                            </div>
                        </div>
                        <div class="site_list">
                            <#include "index_cabinet.ftl"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>
