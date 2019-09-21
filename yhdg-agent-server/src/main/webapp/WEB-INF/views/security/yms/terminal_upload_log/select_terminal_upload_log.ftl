<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_red" onclick="uploadLog()">上传日志</button>
        </div>
        <div class="float_right">
            <button class="btn btn_yellow" onclick="query()">搜索</button>&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right">终端ID：</td>
                <td>
                    <select id="terminal_id" class="easyui-combobox"  style="height:26px; line-height:26px;" data-options="filter: filterCombo">
                        <option value=""></option>
                    <#list terminalList as e>
                        <option value="${e.id}">${(e.id)!''}</option>
                    </#list>
                    </select>
                </td>
                <td align="right" width="70">类型：</td>
                <td>
                    <select style="width:70px;" id="type">
                        <option value="">所有</option>
                    <#list typeEnum as e>
                        <option value="${e.getValue()}">${e.getName()}</option>
                    </#list>
                    </select>
                </td>
                <td align="right" width="80">日志日期：</td>
                <td><input type="text" class="easyui-datebox" id="query_log_time"  style="height: 28px;"/></td>
            </tr>
        </table>
    </div>
    <div style="width:960px; height:520px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>

<script>
    $(function() {
        $('#page_table_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/yms/terminal_upload_log/page.htm",
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
                        title: '终端编号',
                        align: 'center',
                        field: 'terminalId',
                        width: 30
                    },
                    {
                        title: '类型',
                        align: 'center',
                        field: 'typeName',
                        width: 40
                    },
                    {
                        title: '日志时间',
                        align: 'center',
                        field: 'logTime',
                        width: 60
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'statusName',
                        width: 60
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 60
                    },
                    {
                        title: '上传时间',
                        align: 'center',
                        field: 'uploadTime',
                        width: 60
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 40,
                        formatter: function(val, row) {
                            var html = "";
//                            if(row.filePath != null && row.filePath != ''){
                                html += ' <a href="javascript:download(ID)">下载</a>';
//                            }
//                            if(row.status != 3){
                                html += ' <a href="javascript:remove(ID)">删除</a>';
//                            }
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ],
            onLoadSuccess:function() {
                $('#page_table_${pid}').datagrid('clearChecked');
                $('#page_table_${pid}').datagrid('clearSelections');
            }
        });
    })

    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });
    function reload() {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid('reload');
    }
    $('#query_${pid}').click(function() {
        query();
    });

    function filterCombo(q, row){
        var opts = $(this).combobox('options');
        return row[opts.textField].indexOf(q) > -1 ;
    }

    function query() {
        var datagrid = $('#page_table_${pid}');
        var terminalId = $('#terminal_id').combobox('getValue');
        var logTime = $('#query_log_time').datebox('getValue');
        var type = $('#type').val();
        var queryParams = {
            terminalId: terminalId,type: type,logTime : logTime
        };
        datagrid.datagrid('options').queryParams = queryParams;
        datagrid.datagrid('load');
    }

    function download(id) {
        window.location.href = "${contextPath}/security/yms/terminal_upload_log/download_log.htm?id=" + id;
    }

    function uploadLog(){
        var terminalId = $('#terminal_id').combobox('getValue');
        var type = $('#type').val();
        var queryLogTime = $('#query_log_time').datebox('getValue');
        if(terminalId == null || terminalId == '' ){
            $.messager.alert('提示信息', '请选择终端', '提示');
            return;
        }
        if(type == null || type == '' ){
            $.messager.alert('提示信息', '请选择要上传类型', '提示');
            return;
        }
        if(queryLogTime == null || queryLogTime == '' ){
            $.messager.alert('提示信息', '请选择要日志时间', '提示');
            return;
        }

        $.messager.confirm('提示信息', '确认上传日志?', function(ok){
            if(ok){
                $.post('${contextPath}/security/yms/terminal_upload_log/notice_upload.htm', {
                    terminalId: terminalId,type: type,queryLogTime : queryLogTime
                }, function (json) {
                    if (json.success) {
                        $.messager.alert('info', '操作成功', 'info');
                        reload();
                    } else {
                        $.messager.alert('提示消息', json.message, 'info');
                        reload();
                    }
                }, 'json');
            }
        });
    }

    function remove(id) {
        $.messager.confirm('提示信息', '确认删除?', function(ok) {
            if(ok) {
                $.post('${contextPath}/security/yms/terminal_upload_log/delete.htm', {
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
</script>