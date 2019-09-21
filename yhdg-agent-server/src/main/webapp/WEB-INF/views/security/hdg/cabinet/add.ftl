<div class="popup_body clearfix">
    <ul class="tab_nav" id="tab_container_${pid}">
        <li class="selected">基本信息</li>
    </ul>
    <div class="tab_con">
        <div class="tab_item" style="display:block;">
            <div class="ui_table">
                <form method="post">
                    <table cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="70" align="right">设备编号：</td>
                            <td><input type="text" class="text easyui-validatebox" readonly maxlength="6" value="${id}"
                                       name="id"/></td>
                            <td width="70" align="right">设备名称：</td>
                            <td><input type="text" class="text easyui-validatebox" required="true" maxlength="10"
                                       name="cabinetName" value=""/></td>
                        </tr>
                        <tr>
                            <td width="70" align="right">运营商：</td>
                            <td align="right">
                                <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                                       editable="false" style="width: 184px; height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                       swich_agent_${pid}();
                                    }
                                "
                                >
                            </td>
                        </tr>
                        <tr>
                            <td align="right">联系人：</td>
                            <td><input type="text" class="text easyui-validatebox" name="linkname" maxlength="40"
                                       value=""/></td>
                            <td width="70" align="right">电话：</td>
                            <td><input type="text" class="text easyui-validatebox" maxlength="11"
                                       name="tel" value=""/></td>
                        </tr>

                        <tr>
                            <td width="70" align="right">终端id：</td>
                            <td><input type="text" class="text easyui-validatebox" maxlength="6" readonly
                                       name="terminalId"/></td>
                            <td align="right">是否启用：</td>
                            <td>
                                <select name="activeStatus" class="easyui-combobox" editable="false"
                                        style="width: 184px; height: 28px;">
                                <#list activeStatusEnum as e>
                                    <option value="${e.getValue()}">${e.getName()}</option>
                                </#list>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">动态码：</td>
                            <td>
                                <input name="dynamicCode" id="dynamic_code_${pid}" maxlength="4" style="width: 185px;height: 28px;" type="text" class="easyui-numberbox">
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>

<script>

    function selectCompany_${pid}() {
        App.dialog.show({
            css: 'width:826px;height:522px;overflow:visible;',
            title: '选择站点公司',
            href: "${contextPath}/security/hdg/cabinet_company/select_cabinet_company.htm",
            windowData: {
                ok: function (config) {
                    $("#cabinet_company_id_${pid}").val(config.company.id);
                    $("#cabinet_company_name_${pid}").val(config.company.companyName);
                }
            },
            event: {
                onClose: function () {
                }
            }
        });
    }
    function swich_agent_${pid}() {
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var groupCombotree = $('#group_id_${pid}');
        var priceGroupCombotree = $('#price_group_id_${pid}');
        groupCombotree.combotree({
            url: "${contextPath}/security/hdg/cabinet_group/tree.htm?agentId=" + agentId + ""
        });
        groupCombotree.combotree('reload');
        groupCombotree.combotree('setValue', null);

        priceGroupCombotree.combotree({
            url: "${contextPath}/security/hdg/price_group/tree.htm?agentId=" + agentId + ""
        });
        priceGroupCombotree.combotree('reload');
        priceGroupCombotree.combotree('setValue', null);

        var shopComboTree = $('#shop_id_${pid}');

        shopComboTree.combotree({
            url: "${contextPath}/security/hdg/shop/tree.htm?agentId=" + agentId + ""
        });
        shopComboTree.combotree('reload');

    }
    (function () {
        var win = $('#${pid}'), form = win.find('form');

        function selectNotAssociatedTerminal() {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '未关联终端',
                href: "${contextPath}/security/yms/not_associated_terminal/select_not_associated_terminal.htm",
                windowData: {
                    ok: function (config) {
                        win.find('input[name=terminalId]').val(config.notAssociatedTerminal.id);
                    }
                },
                event: {
                    onClose: function () {
                    }
                }
            });
        }

        win.find('button.ok').click(function () {
            form.form('submit', {
                url: '${contextPath}/security/hdg/cabinet/create.htm',
                success: function (text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function () {
            win.window('close');
        });
    })();
</script>


