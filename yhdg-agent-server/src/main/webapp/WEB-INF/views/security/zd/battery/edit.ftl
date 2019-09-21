<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">电池编号：</td>
                    <td><input type="text" maxlength="40" class="text" name="id" readonly value="${(entity.id)!''}"
                               style="width: 173px; height: 28px;"/></td>
                    <td align="right">运营商：</td>
                    <td><input name="agentId" required="true" id="agent_id_${pid}" class="easyui-combotree"
                               editable="false" style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto',
                                onClick: function(node) {
                                clear_${pid}();
                                }"
                               value="${(entity.agentId)!''}"/>
                </tr>
                <tr>
                    <td align="right" width="90">充满电量%：</td>
                    <td><input id="charge_complete_volume_${pid}" name="chargeCompleteVolume"
                               class="easyui-numberspinner"
                               style="width: 184px; height: 28px;" required="required"
                               data-options="min:1, max:100, editable:true" value="${(entity.chargeCompleteVolume)!''}"></td>
                    <td align="right">&nbsp;&nbsp;品牌：</td>
                    <td>
                        <select id="brand" name="brand" class="easyui-combobox" style="width: 184px; height: 28px;">
                            <option value="">无</option>
                        <#if brandList??>
                            <#list brandList as e>
                                <option <#if entity.brand?? && entity.brand == e.itemValue>selected</#if>
                                        value="${(e.itemValue)!''}">${(e.itemName)!''}</option>
                            </#list>
                        </#if>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">电池类型：</td>
                    <td>
                        <input name="type" id="battery_type_${pid}" class="easyui-combotree" editable="false"
                               style="width: 184px; height: 28px;"
                               url="${contextPath}/security/basic/agent_battery_type/tree.htm?agentId=${entity.agentId}"
                               value="${(entity.type)!''}">
                    </td>
                    <td align="right">IMEI：</td>
                    <td><input maxlength="40" class="text easyui-validatebox" name="code" required="true"
                               value="${(entity.code)!''}" validType="uniqueCode['${entity.id}']"
                               style="width: 173px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive"
                                   <#if entity.isActive?? && entity.isActive == 1>checked</#if> id="is_active_1"
                                   value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive"
                                   <#if entity.isActive?? && entity.isActive == 0>checked</#if> id="is_active_0"
                                   value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
                    <td align="right">锁定状态：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="lockSwitch"
                                   <#if entity.lockSwitch?? && entity.lockSwitch == 0>checked</#if> id="lock_switch_0"
                                   value="0"/><label for="lock_switch_0">不控制</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="lockSwitch"
                                   <#if entity.lockSwitch?? && entity.lockSwitch == 1>checked</#if> id="lock_switch_1"
                                   value="1"/><label for="lock_switch_1">放电关</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="lockSwitch"
                                   <#if entity.lockSwitch?? && entity.lockSwitch == 2>checked</#if> id="lock_switch_2"
                                   value="2"/><label for="lock_switch_2">放电开</label>
                        </span>
                    </td>
                </tr>
                <tr>

                    <td align="right">外壳编号：</td>
                    <td><input type="text" class="text easyui-validatebox" validType="uniqueShellCode['${entity.id}']"
                               name="shellCode" value="${(entity.shellCode)!''}"
                               style="width: 173px; height: 28px;"/></td>
                    <td align="right">返修状态：</td>
                    <td>
                        <select name="repairStatus" class="easyui-combobox"
                                style="width: 184px; height: 28px;">
                        <#if repairStatusList??>
                            <#list repairStatusList as e>
                                <option <#if entity.repairStatus?? && entity.repairStatus == e.getValue()>selected</#if>
                                        value="${e.getValue()}">${(e.getName())!''}</option>
                            </#list>
                        </#if>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">所属站点：</td>
                    <td><input onclick="selectCabinet_${pid}()" value="${(entity.cabinetName)!''}" type="text" class="text" readonly
                               name="cabinetName"/><input
                            type="hidden" name="cabinetId" value="${(entity.cabinetId)!''}"/></td>
                    <td align="right">二维码：</td>
                    <td><input type="text" class="text" maxlength="40" validType="uniqueQrcode['${entity.id}']"
                               name="qrcode" value="${(entity.qrcode)!''}" style="width: 173px; height: 28px;"/></td>
                    <#--<td align="right">上线状态：</td>-->
                    <#--<td>-->
                        <#--<select name="upLineStatus" class="easyui-combobox" editable="false"-->
                                <#--style="width: 184px; height: 28px;">-->
                            <#--<option value="1" <#if entity.upLineStatus?? && entity.upLineStatus == 1>selected</#if>>上线</option>-->
                            <#--<option value="0" <#if entity.upLineStatus?? && entity.upLineStatus == 0>selected</#if>>下线</option>-->
                        <#--</select>-->
                    <#--</td>-->
                </tr>
                <#--<tr>-->
                    <#---->
                    <#--&lt;#&ndash;<td align="right">上线时间：</td>&ndash;&gt;-->
                    <#--&lt;#&ndash;<td>&ndash;&gt;-->
                        <#--&lt;#&ndash;<input type="text" editable="false" class="text easyui-datetimebox" name="upLineTime" readonly value="${(entity.upLineTime?string('yyyy-MM-dd HH:mm:ss'))!''}" style="width: 183px; height: 28px;"/>&ndash;&gt;-->
                    <#--&lt;#&ndash;</td>&ndash;&gt;-->
                <#--</tr>-->
                <tr>
                    <td width="70" align="left">二维码地址：</td>
                    <td colspan="3"><textarea style="width:505px;height:60px;" maxlength="450" >${(qrCodeAddress)!''}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function () {
            form.form('submit', {
                url: '${contextPath}/security/zd/battery/update.htm',
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
    })()

    function clear_${pid}() {
        $("input[name='cabinetId']").val('');
        $("input[name='cabinetName']").val('');
    }
    function selectCabinet_${pid}() {
        var agentId = $('#agent_id_${pid}').combotree('getValue');

        App.dialog.show({
            css: 'width:826px;height:522px;overflow:visible;',
            title: '选择换电柜',
            href: "${contextPath}/security/hdg/cabinet/select_cabinets.htm?agentId=" + agentId,
            windowData: {
                ok: function (config) {
                    $("input[name='cabinetId']").val(config.cabinet.id);
                    $("input[name='cabinetName']").val(config.cabinet.cabinetName);
                }
            },
            event: {
                onClose: function () {
                }
            }
        });
    }
</script>