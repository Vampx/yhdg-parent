<div class="popup_body" style="padding-left:50px;padding-top: 40px;font-size: 14px;min-height: 72%;">
    <div class="tab_item" style="display:block;">
        <div class="ui_table">
            <form>
                <input type="hidden" name="id" value="${entity.id}">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td align="left">运营商：</td>
                        <td>
                            <input name="agentId" id="agent_id_${pid}" class="easyui-combotree"
                                   editable="false" style="width: 255px; height: 28px;" value="${(entity.agentId)!''}" disabled
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
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
                    </tr>
                    <tr>
                        <td align="left">运营公司名称：</td>
                        <td><input type="text" class="text easyui-validatebox" name="companyName"  style="width: 242px; height: 28px;" readonly
                                   maxlength="10" value="${(entity.companyName)!''}"/></td>
                    </tr>
                    <tr>
                        <td align="left">运营公司地址：</td>
                        <td colspan="2"><input type="text" class="text easyui-validatebox" maxlength="50" readonly
                                               style="width: 275px;" value="${(entity.address)!''}"/>&nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td align="left">运营公司联系人：</td>
                        <td><input type="text" class="text easyui-validatebox" maxlength="11" style="width: 275px; height: 28px;" readonly
                                   name="linkname" value="${(entity.linkname)!''}"/></td>
                    </tr>
                    <tr>
                        <td align="left">运营公司联系电话：</td>
                        <td><input type="text" class="text easyui-validatebox" maxlength="11" name="tel" style="width: 275px; height: 28px;" readonly
                                   value="${(entity.tel)!''}"/></td>
                    </tr>
                    <tr>
                        <td align="left">营业时间：</td>
                        <td><input type="text" class="text easyui-timespinner" readonly value="${(beginTime)!''}" maxlength="5" data-options="showSeconds:false" style="width:103px; height:28px;" name="beginTime"/>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  <input type="text" class="text easyui-timespinner" readonly value="${(endTime)!''}" maxlength="5" data-options="showSeconds:false,min:'00:01',max:'23:59' " style="width:103px; height:28px;" name="endTime"/></td>
                    </tr>
                    <tr>
                        <td align="left">运营公司余额（元）：</td>
                        <td><input type="text" class="easyui-numberspinner" id="agent_company_balance_${pid}" name="agentCompanyBalance"
                                   disabled maxlength="5" value="${(entity.balance/100)!''}"
                                   style="width:173px;height: 28px;"
                                   data-options="min:0.00,precision:2"><#if entity.id ??>&nbsp;&nbsp;<a
                                href="javascript:showInOutMoney()">运营公司流水</a></#if></td>
                    </tr>
                    <tr>
                        <td align="left">是否启用：</td>
                        <td>
                            <select name="activeStatus" id="active_status_${pid}" class="easyui-combobox"
                                    editable="false" readonly="readonly"
                                    style="width: 173px; height: 28px;">
                            <#list activeStatusEnum as e>
                                <option value="${e.getValue()}"
                                        <#if entity.activeStatus?? && entity.activeStatus == e.getValue()>selected</#if>>${e.getName()}</option>
                            </#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="left">运营公司类型：</td>
                        <td>
                            <span>
                                <input type="checkbox" checked disabled
                                       class="checkbox exchange_checkbox"/><label>换电</label>
                            </span>
                        </td>
                    </tr>
                    <tr style="height: 30px;"></tr>
                </table>
            </form>
        </div>
    </div>
</div>
<script>

    function showInOutMoney() {
        App.dialog.show({
            css: 'width:1000px;height:518px;overflow:visible;',
            title: '运营公司流水',
            href: "${contextPath}/security/basic/agent_company_in_out_money/alert_page.htm?agentCompanyId=${(entity.id)!''}",
            event: {
                onClose: function () {
                }
            }
        });
    }

    (function() {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        var ok = function() {
            var success = true;
            return success;
        };

        win.data('ok', ok);
    })();
</script>


