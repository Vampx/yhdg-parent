<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="90" align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agentId" class="easyui-combotree"
                               editable="false" style="width: 184px; height: 28px;" readonly
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                        swich_agent();
                                    }" value="${(entity.agentId)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">车型：</td>
                    <td>
                        <input name="modelId" id="modelId" readonly style="height: 28px;width: 183px;" class="easyui-combobox"  editable="false" required="true"
                               data-options="url:'${contextPath}/security/zc/vehicle_model/list.htm',
                                method:'get',
                                valueField:'id',
                                textField:'modelName',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onSelect: function(node) {
                                }" value="${(entity.modelId)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">车架号：</td>
                    <td><input id="vin_no" type="text" class="text" readonly name="vinNo" value="${entity.vinNo}" style="height: 28px;width: 185px;" maxlength="100"></td>
                </tr>
                <tr>
                    <td width="90" align="right">是否上线：</td>
                    <td>
                        <select id="up_line_status" name="upLineStatus" style="width: 184px; height: 28px;" disabled="disabled">
                            <option value="0"  <#if entity.upLineStatus == 0>selected</#if>>下线</option>
                            <option value="1" <#if entity.upLineStatus == 1>selected</#if>>上线</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">上线时间：</td>
                    <td>
                        <input class="easyui-datetimebox" readonly type="text"style="width:150px;height:27px;" name="upLineTime" value="${(entity.upLineTime?string('yyyy-MM-dd HH:mm:ss'))!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">是否启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1"
                                   <#if entity.isActive == 1>checked</#if> value="1"/>启用
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0"
                                   <#if entity.isActive == 0>checked</#if> value="0" />禁用
                        </span>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">备注：</td>
                    <td><textarea id="memo" name="memo" readonly style="width:405px;height:60px;" maxlength="450">${(entity.memo)!''}</textarea></td>
                </tr>
                <tr>
                    <td width="70" align="left">二维码地址：</td>
                    <td colspan="3"><textarea style="width:505px;height:60px;" maxlength="450" readonly >${(qrCodeAddress)!''}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        var ok = function() {
            if(!jform.form('validate')) {
                return false;
            }
            var success = true;

            return success;
        };

        win.data('ok', ok);
    })();
</script>
