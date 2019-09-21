<@app.html>
    <@app.head>
    <script>
        $.extend($.fn.validatebox.defaults.rules, {
            unique: {
                validator: function (value, param) {
                    var success;
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/hdg/cabinet/unique.htm',
                        data: {
                            id: value
                        },
                        dataType: 'json',
                        success: function (json) {
                            <@app.json_jump/>
                            if (json.success) {
                                success = true;
                            } else {
                                success = false;
                            }
                        }
                    });

                    return success;
                },
                message: '终端编号重复'
            }
        });
        $(function () {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/hdg/cabinet/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                        {field: 'checkbox', checkbox: true},
                        {
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '换电柜编号',
                            align: 'center',
                            field: 'id',
                            width: 60
                        },
                        {title: '换电柜名称', align: 'center', field: 'cabinetName', width: 60},
                        {
                            title: '版本',
                            align: 'center',
                            field: 'version',
                            width: 30
                        },
                        {
                            title: '地址', align: 'center', field: 'address', width: 60
                        },
                        {
                            title: 'SIM卡号',
                            align: 'center',
                            field: 'simMemo',
                            width: 40
                        },
//                        {
//                            title: '风扇转速',
//                            align: 'center',
//                            field: 'fanSpeed',
//                            width: 40
//                        },
                        {
                            title: '温度',
                            align: 'center',
                            field: 'temp1',
                            width: 30,
                            formatter: function (val, row) {
                                var temp1 = row.temp1;
                                var temp2 = row.temp2;
                                if (temp1 == null) {
                                    temp1 = "--"
                                }else{
                                    temp1 = temp1 / 100;
                                }
                                if (temp2 == null) {
                                    temp2 = "--"
                                }else{
                                    temp2 = temp2 / 100;
                                }
                                return temp1 + "/" + temp2
                            }
                        },
                        {
                            title: '电池数/充电中',
                            align: 'center',
                            field: 'batteryNum',
                            width: 55,
                            formatter: function (val, row) {
                                var batteryNum = (row.batteryNum == null) ? '' : row.batteryNum;
                                var chargeBatteryNum = (row.chargeBatteryNum == null) ? '' : row.chargeBatteryNum;
                                return batteryNum + "/" + chargeBatteryNum;
                            }
                        },
                        {
                            title: '禁用格口',
                            align: 'center',
                            field: 'unActiveBoxNum',
                            width: 35
                        },
                        {
                            title: '启用/在线',
                            align: 'center',
                            field: 'activeStatus',
                            width: 40,
                            formatter: function (val, row) {
                                var activeStatus = row.activeStatus == 1 ? '是' : '否';
                                var isOnline = row.isOnline == 1 ? '<a style="color: #00FF00;">是</a>' : '<a style="color: #ff0000;">否</a>';
                                return activeStatus + "/" + isOnline;
                            }
                        },
                        {
                            title: '最后连接',
                            align: 'center',
                            field: 'heartTime',
                            width: 80
                        },
//                        {
//                            title: '类型',
//                            align: 'center',
//                            field: 'subtype',
//                            width: 40,
//                            formatter: function (val) {
//                                if (val == 1) {
//                                    return '换电';
//                                } else if (val == 2) {
//                                    return '换/充电';
//                                }
//                            }
//                        },
                        {
                            title: '上线状态',
                            align: 'center',
                            field: 'upLineStatusName',
                            width: 60
                        },
                        {
                            title: '上线时间',
                            align: 'center',
                            field: 'upLineTime',
                            width: 80
                        },
//                        {
//                            title: '信号',
//                            align: 'center',
//                            field: 'currentSignal',
//                            width: 20
//                        },
//                        {title: '广告终端', align: 'center', field: 'terminalId', width: 60},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 100,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.Cabinet:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.Cabinet:edit'>
                                    html += ' <a href="javascript:edit(\'ID\')">修改</a>';
                                </@app.has_oper>
                                <#--<@app.has_oper perm_code='hdg.Cabinet:delete'>-->
                                    <#--html += ' <a href="javascript:remove(\'ID\')">删除</a>';-->
                                <#--</@app.has_oper>-->
                                html += ' <a href="${contextPath}/security/main/module.htm?moduleId=2&url=${contextPath}/security/hdg/cabinet/cabinet_detail.htm?id=ID" style="color:blue;" target="view_window" >换电柜详情</a>';
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        });

        function cabinet_group() {
            App.dialog.show({
                css: 'width:860px;height:512px;overflow:visible;',
                title: '分组',
                href: "${contextPath}/security/hdg/cabinet_group/index.htm",
                event: {
                    onClose: function () {
                        var tree = $('#cabinet_tree');
                        tree.tree('reload')
                    }
                }
            });
        }

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var tree = $('#cabinet_tree');
            var datagrid = $('#page_table');
 	    var queryVersionName = $('#query_version_name').val();
            var queryVersionValue = $('#query_version_value').val();
            var mac = $('#mac').val();
            var cabinetName = $('#cabinet_name').val();
            var id = $('#id').val();

            var groupId  = '';
            var provinceId = $('#province_id_${''}').val();
            var cityId = $('#city_id_${''}').val();
            var districtId = $('#district_id_${''}').val();

	    var queryParams = {
                cabinetName: cabinetName,
                id: id,
                groupId: groupId,
                mac: mac,
                provinceId: provinceId,
                cityId: cityId,
                districtId: districtId
            };
  	    queryParams[queryVersionName] = queryVersionValue;

            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:786px;height:515px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/hdg/cabinet/add.htm",
                event: {
                    onClose: function () {
                        query();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:830px;height:620px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/hdg/cabinet/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        query();
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:830px;height:620px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/cabinet/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post('${contextPath}/security/hdg/cabinet/delete.htm', {
                        id: id
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('提示信息', '操作成功', 'info');
                            query();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        }

        function view_operate_log() {
            App.dialog.show({
                css: 'width:1000px;height:680px;overflow:visible;',
                title: '换电柜操作日志',
                href: "${contextPath}/security/hdg/cabinet_operate_log/select_cabinet_operate_log.htm"
            });
        }

        function view_day_degree_log() {
            App.dialog.show({
                css: 'width:1000px;height:680px;overflow:visible;',
                title: '换电柜日用电统计',
                href: "${contextPath}/security/hdg/cabinet_day_degree_stats/select_cabinet_day_degree_stats.htm"
            });
        }

        function view_log(id) {
            App.dialog.show({
                css: 'width:550px;height:360px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/cabinet_operate_log/view.htm?id=" + id
            });
        }

        function select_battery_stats() {
            App.dialog.show({
                css: 'width:1000px;height:680px;overflow:visible;',
                title: '换电柜电池统计',
                href: "${contextPath}/security/hdg/cabinet_battery_stats/select_cabinet_battery_stats.htm"
            });
        }

        function view_battery_stats(id) {
            App.dialog.show({
                css: 'width:600px;height:460px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/cabinet_battery_stats/view.htm?id=" + id
            });
        }

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
            var tree = $('#cabinet_tree');
            tree.tree({
                url: "${contextPath}/security/hdg/cabinet_group/tree.htm?agentId=" + agentId + "&dummy=${'所有'?url}"
            });
            tree.tree('reload');
        }

    </script>
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
                    <table cellpadding="0" cellspacing="0" border="0" style="border-collapse:separate; border-spacing: 0px 3px;">
                        <tr>
			                <td align="right" width="100">设备SN：</td>
                            <td><input type="text" class="text" id="mac" /></td>
                            <td align="right" width="100">换电柜编号：</td>
                            <td><input type="text" class="text" id="id" /></td>
                            <td align="right" width="100">换电柜名称：</td>
                            <td><input type="text" class="text" id="cabinet_name" /></td>
                        </tr>
                        <tr>
                            <td width="70" align="right">所在地区：</td>
                            <td>
                                <div class="select_city" style="width:210px;" >
                                    <#include '../../basic/area/select_area.ftl'>
                                </div>
                            </td>
                            <td align="right" width="85">
                                <select style="width:70px;height: 25px;" id="query_version_name">
                                    <option value="version">版本=</option>
                                    <option value="queryAntiVersion">版本≠</option>
                                </select>
                            </td>
                            <td><input type="text" style="height: 23px;" class="text" id="query_version_value"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.Cabinet:viewOperateLog'>
                                <button class="btn btn_blue" onclick="view_operate_log()">操作日志</button>
                            </@app.has_oper>
                        </div>
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.Cabinet:viewDayDegreeLog'>
                                <button class="btn btn_blue" onclick="view_day_degree_log()">日用电统计</button>
                            </@app.has_oper>
                        </div>
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.Cabinet:batteryStats'>
                                <button class="btn btn_blue" onclick="select_battery_stats()">电池统计</button>
                            </@app.has_oper>
                        </div>
                        <h3>换电柜信息</h3>
                    </div>
                    <div class="grid">
                        <table id="page_table"></table>
                    </div>
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>





