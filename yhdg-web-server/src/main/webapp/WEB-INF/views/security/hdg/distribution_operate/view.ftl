<div class="popup_body" style="padding-left:50px;padding-top: 18px;font-size: 14px;height: 82%;">
    <div class="tab_item" style="display:block;">
        <div class="ui_table">
            <form method="post" id="pay_people_parameter">
                <input type="hidden" name="id" value="${entity.id}">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td align="left">所属运营商：</td>
                        <td>
                            <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                                   editable="false" style="width: 190px; height: 28px;" value="${(entity.agentId)!''}"
                                   disabled
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200'
                                "
                            >
                        </td>
                    </tr>
                    <tr>
                        <td align="left">分成体名称：</td>
                        <td><input type="text" name="distributionName" value="${(entity.distributionName)!''}" maxlength="40"
                                   class="text easyui-validatebox" readonly style="width: 180px;height: 28px;"
                                   required="true" /></td>
                    </tr>
                    <tr>
                        <td align="left">分成体账号：</td>
                        <td><input type="text" name="loginName" readonly value="${(entity.loginName)!''}" maxlength="40"
                                   class="text easyui-validatebox" style="width: 180px;height: 28px;"
                                   required="true" /></td>
                    </tr>
                    <tr>
                        <td align="left">密码：</td>
                        <td><input type="text" class="text easyui-validatebox"  readonly value="${(entity.password)!''}" maxlength="40" class="text" style="width: 180px;height: 28px;"
                                   name="password"/></td>
                    </tr>
                    <tr>
                        <td align="left">启用：</td>
                        <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_1" readonly
                                       <#if entity.isActive?? && entity.isActive == 1>checked</#if>
                                       value="1"/><label for="is_active_1">启用</label>
                            </span>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_0" readonly
                                       <#if entity.isActive?? && entity.isActive == 1><#else>checked</#if>
                                       value="0"/><label
                                    for="is_active_0">禁用</label>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td align="left">微信账号 ：</td>
                        <td>
                            <input type="text" class="text easyui-validatebox" name="payPeopleMpOpenId" readonly value="${(entity.payPeopleMpOpenId)!''}"/>
                            <a href="javascript:selectPayeeCustomer()" style="font-weight: bold;padding-left: 3px;">+选择</a>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="left">收款人姓名 ：</td>
                        <td>
                            <input type="text" class="text easyui-validatebox" name="payPeopleName"  readonly value="${(entity.payPeopleName)!''}"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" width="90px;">手机号 ：</td>
                        <td>
                            <input type="text" class="text easyui-validatebox" name="payPeopleMobile" readonly value="${(entity.payPeopleMobile)!''}"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" width="90px;">支付宝账号 ：</td>
                        <td>
                            <input type="text" class="text easyui-validatebox" name="payPeopleFwOpenId" readonly value="${(entity.payPeopleFwOpenId)!''}"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" width="90px;">提现密码 ：</td>
                        <td>
                            <input type="text" class="text easyui-validatebox" name="payPassword" readonly value="${(entity.payPassword)!''}"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
<div class="popup_btn" style="height: 10%;">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })()
</script>