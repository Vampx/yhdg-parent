<div class="popup_body" style="padding-left:50px;padding-top: 40px;font-size: 14px;min-height: 72%;">
    <div class="tab_item" style="display:block;">
        <div class="ui_table">
            <form>
                <input type="hidden" name="id" value="${entity.id}">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td align="left">*运营商：</td>
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
                        <td align="left">*物业名称：</td>
                        <td><input type="text" class="text easyui-validatebox" name="estateName"  style="width: 242px; height: 28px;" readonly
                                   maxlength="10" value="${(entity.estateName)!''}"/></td>
                    </tr>
                    <tr>
                        <td align="left">*物业地址：</td>
                        <td colspan="2"><input type="text" class="text easyui-validatebox" maxlength="50" readonly
                                               style="width: 275px;" value="${(entity.address)!''}"/>&nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td align="left">*物业联系人：</td>
                        <td><input type="text" class="text easyui-validatebox" maxlength="11" style="width: 275px; height: 28px;" readonly
                                   name="linkname" value="${(entity.linkname)!''}"/></td>
                    </tr>
                    <tr>
                        <td align="left">*物业联系电话：</td>
                        <td><input type="text" class="text easyui-validatebox" maxlength="11" name="tel" style="width: 275px; height: 28px;" readonly
                                   value="${(entity.tel)!''}"/></td>
                    </tr>
                    <tr>
                        <td align="left">*营业时间：</td>
                        <td><input type="text" class="text easyui-timespinner" readonly value="${(beginTime)!''}" maxlength="5" data-options="showSeconds:false" style="width:103px; height:28px;" name="beginTime"/>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  <input type="text" class="text easyui-timespinner" readonly value="${(endTime)!''}" maxlength="5" data-options="showSeconds:false,min:'00:01',max:'23:59' " style="width:103px; height:28px;" name="endTime"/></td>
                    </tr>
                    <tr>
                        <td align="left">*物业余额（元）：</td>
                        <td><input type="text" class="easyui-numberspinner" id="shop_balance_${pid}" name="shopBalance"
                                   disabled maxlength="5" value="${(entity.balance/100)!''}"
                                   style="width:173px;height: 28px;"
                                   data-options="min:0.00,precision:2"><#if entity.id ??>&nbsp;&nbsp;<a
                                href="javascript:estateInOutMoney()">物业流水</a></#if></td>
                    </tr>
                    <tr>
                        <td align="left">*认证类型：</td>
                        <td>
                            <select name="authType" class="easyui-combobox" editable="false"
                                    style="width: 173px; height: 28px;">
                            <#list AuthTypeEnum as e>
                                <option value="${e.getValue()}">${e.getName()}</option>
                            </#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="left">*是否启用：</td>
                        <td>
                            <select name="isActive" id="active_status_${pid}" class="easyui-combobox"
                                    editable="false" readonly="readonly"
                                    style="width: 173px; height: 28px;">
                            <#list activeStatusEnum as e>
                                <option value="${e.getValue()}"
                                        <#if entity.isActive?? && entity.isActive == e.getValue()>selected</#if>>${e.getName()}</option>
                            </#list>
                            </select>
                        </td>
                    </tr>
                    <tr style="height: 30px;"></tr>
                    <tr>
                        <td align="left">&nbsp;&nbsp;物业柜子：</td>
                        <td>
                            <div style="width: 350px;">
                                <div >
                                <#if entity.cabinetList ??>
                                    <#list entity.cabinetList as cabinet>
                                        <span>${cabinet.cabinetName}【${cabinet.id}】</span><br><br>
                                    </#list>
                                </#if>
                                </div>
                                <div class="zj_add cabinet_add"
                                     style="cursor: pointer;width: 80px;height: 100px;float: left;text-align: center;margin: 0 10px 10px 0;background: #fff;">
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
<#--<div class="popup_btn" style="height: 10%">-->
    <#--<button class="btn btn_border close">关闭</button>-->
<#--</div>-->
<script>
    function estateInOutMoney() {
        App.dialog.show({
            css: 'width:1000px;height:518px;overflow:visible;',
            title: '物业流水',
            href: "${contextPath}/security/basic/estate_in_out_money/alert_page.htm?estateId=${(entity.id)!''}",
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


