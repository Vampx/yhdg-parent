<@app.html>
    <@app.head>
    </@app.head>

    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>
            <div class="content">
                <div class="panel search">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="false"
                                       style="width: 200px;height: 28px;"
                                       value="${(agentId)!''}"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'系统默认'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                   reloadTree();
                                }
                            "
                                >
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <form method="post">
                        <input type="hidden" name="id" value="${(entity.id)!''}">
                        <div class="settings_body">

                            <fieldset style="height: 440px;">
                                <legend>运营商设置</legend>
                                <div class="ui_table">
                                    <table cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td width="100" align="right">套餐选择：</td>
                                            <td>
                                                <select class="easyui-combobox" style="width:185px; height: 28px;"
                                                        name="exchangeFeeMode">
                                                    <#list agentSetting as e>
                                                        <option value="${e.value}" <#if ((entity.exchangeFeeMode)?? && entity.exchangeFeeMode == e.value)>
                                                                selected="selected" </#if>>${e.getName()!}</option>
                                                    </#list>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="120" align="right">微信收钱的openId：</td>
                                            <td>
                                                <input type="text" maxlength="40" class="text easyui-validatebox"
                                                       name="accountOpenId" value="${(entity.accountOpenId)!''}"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="120" align="right">微信收钱的姓名：</td>
                                            <td>
                                                <input type="text" maxlength="40" class="text easyui-validatebox"
                                                       name="accountFullName" value="${(entity.accountFullName)!''}"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="120" align="right">微信收钱的手机号：</td>
                                            <td>
                                                <input type="text" maxlength="11" class="text easyui-validatebox"
                                                       name="accountMobile" value="${(entity.accountMobile)!''}"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="100" align="right">MOS间隔时长(秒)：</td>
                                            <td><input type="text" class="text easyui-numberspinner"
                                                       name="mosIntervalTime" data-options="min:0"
                                                       style="width: 184px; height: 28px;" required="true"
                                                       value="${(entity.mosIntervalTime)!0}"/></td>
                                        </tr>
                                    </table>
                                </div>
                            </fieldset>
                        </div>
                    </form>
                    <div class="settings_btn">
                        <@app.has_oper perm_code='1_1_19_2'>
                            <button class="btn btn_red ok">保 存</button>
                        </@app.has_oper>
                        <button class="btn btn_border close">重 置</button>
                    </div>
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>
<script>

    (function () {
        var form = $('form');
        var agent = $('#agent_id');

        $('button.ok').click(function () {
            var agentId = agent.combotree('getValue');
            form.form('submit', {
                url: '${contextPath}/security/basic/agent_setting/update.htm?id=' + agentId,
                success: function (text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        document.location.href = '${contextPath}/security/basic/agent_setting/index.htm?agentId=' + agentId;
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });

        $('button.close').click(function () {
            document.location.href = '${contextPath}/security/basic/agent_setting/index.htm'
        });

    })();

    function reloadTree() {
        document.location.href = '${contextPath}/security/basic/agent_setting/index.htm?agentId=' + $('#agent_id').combotree('getValue');
    }

</script>

