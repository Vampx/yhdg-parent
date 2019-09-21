<@app.html>
    <@app.head>
    <style type="text/css">
        .zj_list .zj_item {
            position: relative;
            width: 120px;
            height: 8px;
            padding: 10px 0;
            float: left;
            text-align: center;
            margin: 0 10px 10px 0;
            background: #f5f5f5;
            border-radius: 4px;
        }
        .zj_list .zj_item.selected {
            color: #fff;
            background: #4263ff;
        }
    </style>
    <script>
        $.extend($.fn.validatebox.defaults.rules, {
            integerbfb: {// 验证整数 可正负数
                validator: function (value, param) {
                    if (/^(?:[1-9]?\d|100)$/i.test(value) == false) {
                        $(param[0]).val(value.substring(0, value.length - 1));
                        return /^(?:[1-9]?\d|100)$/i.test(value);
                    } else {
                        return /^(?:[1-9]?\d|100)$/i.test(value);
                    }
                    //return /^([+]?[0-9])|([-]?[0-9])+\d*$/i.test(value);
                },
                message: '请输入1-100整数'
            },
            integerdobel: {// 验证整数 可保留两位小数
                validator: function (value, param) {
                    
                    if (/^(([1-9]{1}\d*)|(0{1}))(\.\d{0,2})?$/i.test(value) == false) {
                        $(param[0]).val(value.substring(0, value.length - 1));
                        return /^(([1-9]{1}\d*)|(0{1}))(\.\d{0,2})?$/i.test(value);
                    } else {
                        return /^(([1-9]{1}\d*)|(0{1}))(\.\d{0,2})?$/i.test(value);
                    }
                    //return /^([+]?[0-9])|([-]?[0-9])+\d*$/i.test(value);
                },
                message: '请输入整数最多保留两位小数'
            },
            unique: {
                validator: function (value, param) {
                    var success;
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/hdg/exchange_installment_setting/unique.htm',
                        data: {
                            mobile: value,
                            id: param.length ? param[0] : ''
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
                message: '该用户已配置过分期'
            }

        });

        $(function() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/hdg/exchange_installment_setting/page.htm",
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
                            title: '规则名称',
                            align: 'center',
                            field: 'fullname',
                            width: 60
                        },
                        {
                            title: '规则类型',
                            align: 'center',
                            field: 'settingName',
                            width: 60
                        },
                        {
                            title: '分期手机号数量',
                            align: 'center',
                            field: 'customermobileNum',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '<a href="javascript:customermobileNumView(ID)">'+val+'</a>';
                                return html.replace(/ID/g, row.id);
                            }
                        },
                        {
                            title: '绑定设备数量',
                            align: 'center',
                            field: 'cabinetNameNum',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '<a href="javascript:cabinetNameNumView(ID)">'+val+'</a>';
                                return html.replace(/ID/g, row.id);
                            }
                        },
                        /*{
                            title: '绑定站点数量',
                            align: 'center',
                            field: 'stationNameNum',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '<a href="javascript:stationNameNumView(ID)">'+val+'</a>';
                                return html.replace(/ID/g, row.id);
                            }
                        },*/
                        {
                            title: '是否启用',
                            align: 'center',
                            field: 'isActive',
                            width: 60,
                            formatter:function (val) {
                                return val==1?'是':'否';
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.ExchangeInstallmentSetting:view'>
                                    html += ' <a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.ExchangeInstallmentSetting:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.ExchangeInstallmentSetting:remove'>
                                    html += ' <a href="javascript:remove(ID)">删除</a>';
                                </@app.has_oper>
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
        });

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var agentId = $('#agent_id').combotree('getValue');
            var queryName = $('#queryName').val();
            var isActive = $('#isActive').val();
            var settingType = $('#settingType').val();
            var mobileAndSettingName = $('#mobileAndSettingName').val();
            var queryParams = {
                agentId: agentId,
                isActive:isActive,
                settingType:settingType
            };
            queryParams[queryName] = mobileAndSettingName;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:900px;height:700px;',
                title: '新建',
                href: "${contextPath}/security/hdg/exchange_installment_setting/add.htm?id=",
                event: {
                    onClose: function() {
                        query();
                    }
                }
            });
        }

        function customermobileNumView(id) {
            App.dialog.show({
                css: 'width:740px;height:485px;',
                title: '查看骑手信息',
                href: "${contextPath}/security/hdg/exchange_installment_customer/index.htm?settingId=" + id,
                event: {
                    onClose: function() {
                    }
                }
            });
        }

        function cabinetNameNumView(id) {
            App.dialog.show({
                css: 'width:740px;height:485px;',
                title: '查看设备信息',
                href: "${contextPath}/security/hdg/exchange_installment_cabinet/index_installment_cabint.htm?settingId=" + id,
                event: {
                    onClose: function() {
                    }
                }
            });
        }
        function stationNameNumView(id) {
            App.dialog.show({
                css: 'width:740px;height:485px;',
                title: '查看设备信息',
                href: "${contextPath}/security/hdg/exchange_installment_station/index_installment_station.htm?settingId=" + id,
                event: {
                    onClose: function() {
                    }
                }
            });
        }
        function edit(id) {
            App.dialog.show({
                css: 'width:839px;height:676px;',
                title: '修改',
                href: "${contextPath}/security/hdg/exchange_installment_setting/edit.htm?id=" + id ,
                event: {
                    onClose: function() {
                        query();
                    },
                    onLoad: function() {
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:839px;height:676px;',
                title: '查看',
                href: "${contextPath}/security/hdg/exchange_installment_setting/view.htm?id=" + id ,
                event: {
                    onClose: function() {
                    },
                    onLoad: function() {
                    }
                }
            });
        }

        function detail_list(id) {
            App.dialog.show({
                css: 'width:800px;height:480px;overflow:visible;',
                title: '分期明细',
                href: "${contextPath}/security/hdg/exchange_installment_detail/index.htm?settingId=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '删除分期设置将会删除关联的分期明细，确认删除?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/hdg/exchange_installment_setting/delete.htm', {
                        id: id
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('info', '操作成功', 'info');
                            query();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        }

    </script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>

            <div class="content">
                <div class="panel search" >
                    <table>
                        <tr>
                            <td>
                                <i style="width: 4px;height: 30px;background-color: #ccc;display: block;margin-right: 10px;"></i>
                            </td>
                            <td style="color: #000000;font-weight: 600;line-height: 30px;">
                                <h2 >分期规则设置</h2>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="top:45px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.ExchangeInstallmentSetting:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <div  class="float_right">
                            <button class="btn btn_yellow" onclick="query()">搜索</button>
                        </div>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td align="right">运营商：</td>
                                <td>
                                    <input id="agent_id" class="easyui-combotree" editable="true"
                                           style="width: 200px;height: 28px;"
                                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200'
                            "
                                    >&nbsp;&nbsp;
                                </td>
                                <td align="right">
                                    <select style="width:100px;" id="queryName">
                                        <option value="customermobile">手机号</option>
                                        <option value="fullname">规则名称</option>
                                        <option value="cabinetId">设备编号</option>
                                        <option value="stationId">站点编号</option>
                                    </select>
                                </td>
                                <td><input type="text" class="text" id="mobileAndSettingName"/>&nbsp;&nbsp;</td>
                                <td align="right">规则类型：</td>
                                <td>
                                    <select style="width:80px;" id="settingType">
                                        <option value="">所有</option>
                                        <#list settingType as e>
                                            <option value="${e.getValue()}">${e.getName()}</option>
                                        </#list>
                                    </select>
                                    &nbsp;&nbsp;
                                </td>
                                <td align="right">是否启用：</td>
                                <td>
                                    <select style="width:80px;" id="isActive">
                                        <option value="">所有</option>
                                        <#list isActive as e>
                                            <option value="${e.getValue()}">${e.getName()}</option>
                                        </#list>
                                    </select>
                                </td>
                            </tr>
                        </table>
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





