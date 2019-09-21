<div class="popup_body popup_body_full">
    <div class="ui_table">
        <table cellpadding="0" cellspacing="0" border="0" width="100%" >
            <tr>
                <td align="right">推送模板：</td>
                <td>
                    <select class="easyui-combobox" id="templateId_${pid}" name="templateId"  style="width:250px;height: 30px "
                            data-options="
                                onSelect: function (data) {
                                      find_by_templateId();
                                      find_templateId();
                                      eliminate();
                                      find_templateId_detail();
                                }
                         ">
                        <option value="0">无</option>
                    <#list pushMessageList as m>
                        <option value="${m.id}" >${m.templateName}</option>
                    </#list>
                    </select>
                </td>
                <td width="70" align="right">操作人：</td>
                <td><input type="text" class="text easyui-validatebox" id="operatorName_${pid}" name="operatorName" maxlength="40" readonly value="${(Session['SESSION_KEY_USER'].username)!''}" /></td>
            </tr>
            <tr>
                <td width="70" align="right">模板名称：</td>
                <td><input type="text" class="text easyui-validatebox" id="templateName_${pid}" readonly name="templateName" maxlength="40" /></td>
            </tr>
            <tr>
                <td width="70" align="right">标题：</td>
                <td><input type="text" class="text easyui-validatebox" id="title_${pid}" name="title" maxlength="40" /></td>
            </tr>
            <tr>
                <td width="70" align="right">URL：</td>
                <td><input type="text" class="text easyui-validatebox" id="url_${pid}" name="url" maxlength="512"style="width:500px;height: 30px " /></td>
            </tr>
            <tr>
                <td align="right" width="75"></td>
                <td>(批量输入11位手机号以,相隔 如：18888888888,17777777777 注意逗号格式 ！)&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn btn_red" onclick="addBeatch()">批量添加手机号</button></td>
            </tr>
            <tr>
                <td align="right" width="75">手机号：</td>
                <td colspan="6"><textarea id="mobile_${pid}"  name="mobile"  style="width:99%;height: 200px"></textarea></td>
            </tr>
            <tr>
                <td align="right" width="75">推送内容：</td>
                <td colspan="6" id="variable_container_${pid}">
                    <div class="grid" style="height:205px; margin-top: 10px;">
                        <table  id="batch_mobile_message_${pid}">
                        </table>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>
<div class="popup_btn popup_btn_full">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    function eliminate(){
        var templateId = $("#templateId_${pid}").combobox('getValue');
        if(templateId == 0){
            var datagrid = $('#batch_mobile_message_${pid}');
            datagrid.datagrid('loadData', []);//清空
            $("#url_${pid}").val("");
            $("#title_${pid}").val("");
            $("#templateName_${pid}").val("");
        }
    }


    function find_by_templateId() {
        $.post('${contextPath}/security/basic/batch_weixinmp_template_message/load_template.htm', {
            templateId: $("#templateId_${pid}").combobox('getValue')
        }, function (json) {
            if (json.success) {
                $("#title_${pid}").val(json.data.template.templateName);
                $("#templateName_${pid}").val(json.data.template.templateName);
            } else {
                $.messager.alert('提示消息', json.message, 'info');
            }
        }, 'json');
    }

    function find_templateId() {
        $.post('${contextPath}/security/basic/batch_weixinmp_template_message/template.htm', {
            templateId: $("#templateId_${pid}").combobox('getValue')
        }, function (json) {
            if (json.success) {
                for(var i = 0; i < json.data.template.length; i++) {
                    $("#content_${pid}").val(json.data.template[i].keywordName); + $("#content_${pid}").val(json.data.template[i].keywordValue);
                }
            } else {
                $.messager.alert('提示消息', json.message, 'info');
            }
        }, 'json');
    }

    function find_templateId_detail() {
        $.post('${contextPath}/security/basic/batch_weixinmp_template_message/load_template_detail.htm', {
            templateId: $("#templateId_${pid}").combobox('getValue')
        }, function (json) {
            if (json.success) {
                var datagrid = $('#batch_mobile_message_${pid}');
                datagrid.datagrid('loadData', []);//清空
                for(var i = 0; i < json.data.variable.length; i++) {
                    datagrid.datagrid('appendRow', {
                        variable: json.data.variable[i],
                        content: ""
                    });
                }
            } else {
                $.messager.alert('提示消息', json.message, 'info');
            }
        }, 'json');
    }

    function addBeatch() {
        App.dialog.show({
            css: 'width:800px;height:530px;',
            title: '批量添加',
            href: "${contextPath}/security/basic/customer/select_customers_mobile.htm",
            windowData: {
                ok: function(rows) {
                    var mobiles = [];
                    if(rows.length > 0) {
                        for(var i = 0; i < rows.length; i++) {
                            mobiles.push(rows[i].mobile);
                        }
                        $("#mobile_${pid}").val(mobiles);
                        return true;
                    }else {
                        $.messager.alert('提示信息', '请选择客户', 'info');
                        return false;
                    }
                }
            }
        });
    }


    (function () {
        $('#batch_mobile_message_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: false,

            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '变量',
                        align: 'center',
                        field: 'variable',
                        width: 20
                    },
                    {
                        title: '内容',
                        align: 'center',
                        field: 'content',
                        width: 40,
                        formatter: function(val, row) {
                            return '<input type="text" name="variable_input" style="width: 400px; height:30px;text-align: center;" variable_name="' + row.variable +'"/>'
                        }
                    }
                ]
            ]
        });
    })();

    (function() {
        var pid = '${pid}',
                win = $('#' + pid);
        win.find('button.ok').click(function() {
            var checked = $('#batch_mobile_message_${pid}').datagrid('getRows');
            if(checked.length == 0){
                $.messager.confirm('提示信息','请仔细核对后再确认提交？',function(ok){
                    if(ok){
                        var variables = [];
                        var contents = [];
                        var templateId = $("#templateId_${pid}").combobox('getValue');
                        var content = $('#content_${pid}').val();
                        var url = $("#url_${pid}").val();
                        var mobile = $('#mobile_${pid}').val();
                        var title = $('#title_${pid}').val();
                        var templateName = $("#templateName_${pid}").val();
                        for(var i = 0; i< checked.length; i++){
                            variables.push(checked[i].variable);
                        }
                        $('#variable_container_${pid} input[name=variable_input]').each(function(){
                            /*alert($(this).attr('variable_name'));
                            alert($(this).val());*/
                            //contents = $(this).val();
                            contents.push($(this).val());
                        });

                        $.post('${contextPath}/security/basic/batch_weixinmp_template_message/create.htm',{
                            variables: variables,
                            contents: contents,
                            url: url,
                            templateId: templateId,
                            mobile: mobile,
                            title: title,
                            templateName: templateName,
                            checked:checked.length
                        },function(json){
                            if(json.success){
                                $.messager.alert('info','操作成功','info');
                                win.window('close');
                            }else{
                                $.messager.alert('提示消息',json.message,'info');
                            }
                        },'json');
                    }
                });
            }else if (checked.length >0){
                $.messager.confirm('提示信息','请仔细核对后再确认提交？',function(ok){
                    if(ok){
                        var variables = [];
                        var contents = [];
                        var templateId = $("#templateId_${pid}").combobox('getValue');
                        var content = $('#content_${pid}').val();
                        var url = $("#url_${pid}").val();
                        var mobile = $('#mobile_${pid}').val();
                        var title = $('#title_${pid}').val();
                        var templateName = $("#templateName_${pid}").val();
                        for(var i = 0; i< checked.length; i++){
                            variables.push(checked[i].variable);
                        }
                        $('#variable_container_${pid} input[name=variable_input]').each(function(){
                            /*alert($(this).attr('variable_name'));
                            alert($(this).val());*/
                            //contents = $(this).val();
                            contents.push($(this).val());
                        });

                        $.post('${contextPath}/security/basic/batch_weixinmp_template_message/create.htm',{
                            variables: variables,
                            contents: contents,
                            url: url,
                            templateId: templateId,
                            mobile: mobile,
                            title: title,
                            templateName: templateName,
                            checked:checked.length
                        },function(json){
                            if(json.success){
                                $.messager.alert('info','操作成功','info');
                                win.window('close');
                            }else{
                                $.messager.alert('提示消息',json.message,'info');
                            }
                        },'json');
                    }
                });
            }else {
                $.messager.alert('提示信息', '数据不能为空', 'info');
                return false;
            }

        });

        win.find('button.close').click(function() {
            win.window('close');
        });

    })()


</script>
