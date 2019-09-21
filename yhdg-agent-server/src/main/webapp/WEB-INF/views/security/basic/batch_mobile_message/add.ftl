<div class="popup_body popup_body_full">
    <div class="ui_table">
        <table cellpadding="0" cellspacing="0" border="0" width="100%" >
            <tr>
                <td align="right">短信模板：</td>
                <td>
                    <select class="easyui-combobox" id="templateId_${pid}" name="templateId"  style="width:180px;height: 30px "
                            data-options="
                                onSelect: function (data) {
                                      find_by_templateId();
                                      find_templateId();
                                      eliminate();
                                }
                         ">
                        <option value="0">无</option>
                    <#list mobileMessageList as m>
                        <option value="${m.id}" >${m.mobileMessageTemplateTitle}</option>
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
                <td align="right" width="75">内容：</td>
                <td colspan="6"><textarea id="content_${pid}" readonly name="content" maxlength="200" style="width:99%;"></textarea></td>
            </tr>
            <tr>
                <td align="right" width="75"></td>
                <td>(批量输入11位手机号以,相隔 如：18888888888,17777777777 注意逗号格式 ！)</td>
            </tr>
            <tr>
                <td align="right" width="75">手机号：</td>
                <td colspan="6"><textarea id="mobile_${pid}"  name="mobile" maxlength="200" style="width:99%;"></textarea></td>
            </tr>
            <tr>
                <td align="right" width="75">短信内容：</td>
                <td colspan="6" id="variable_container_${pid}">
                    <div class="grid" style="height:305px; margin-top: 10px;">
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
            $("#content_${pid}").val("");
            $("#title_${pid}").val("");
            $("#templateName_${pid}").val("");
        }
    }


    function find_by_templateId() {
        $.post('${contextPath}/security/basic/batch_mobile_message/load_template.htm', {
            templateId: $("#templateId_${pid}").combobox('getValue')
        }, function (json) {
            if (json.success) {
                $("#content_${pid}").val(json.data.template.content);
                $("#title_${pid}").val(json.data.template.title);
                $("#templateName_${pid}").val(json.data.template.title);
            } else {
                $.messager.alert('提示消息', json.message, 'info');
            }
        }, 'json');
    }

    function find_templateId() {
        $.post('${contextPath}/security/basic/batch_mobile_message/load_template.htm', {
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
            if(checked.length > 0){
                $.messager.confirm('提示信息','请仔细核对后再确认提交？',function(ok){
                    if(ok){
                        var variables = [];
                        var contents = [];
                        var templateId = $("#templateId_${pid}").combobox('getValue');
                        var content = $('#content_${pid}').val();
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

                        $.post('${contextPath}/security/basic/batch_mobile_message/create.htm',{
                            variables: variables,
                            contents: contents,
                            templateId: templateId,
                            content: content,
                            mobile: mobile,
                            title: title,
                            templateName: templateName
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
