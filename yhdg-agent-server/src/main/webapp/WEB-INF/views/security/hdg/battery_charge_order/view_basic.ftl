<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">运营商：</td>
                    <td><input type="text" maxlength="40" class="text easyui-validatebox" value="${(entity.agentName)!''}"/></td>
                    <td width="90" align="right">电池编号：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(entity.batteryId)!''}" /></td>
                </tr>
                <tr>
                    <td width="90" align="right">客户名称：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(entity.customerFullname)!''}"/></td>
                    <td width="70" align="right">客户手机：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(entity.customerMobile)!''}"/></td>
                </tr>
                <tr>
                    <td width="90" align="right">初始电量：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(entity.initVolume)!''}"/></td>
                    <td width="70" align="right">当前电量：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(entity.currentVolume)!''}"/></td>
                </tr>
                <tr>
                    <td width="90" align="right">价格：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${((entity.price) / 100 + "元")!''}"/></td>
                    <td width="70" align="right">订单收入：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${((entity.money) / 100 + "元")!''}"/></td>
                </tr>
                <tr>
                    <td width="90" align="right">时长：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(entity.duration)!''}"/></td>
                    <td width="70" align="right">状态：</td>
                    <td>
                        <select class="easyui-combobox" required="true" id="status_${pid}" name="status" style="width:180px;height: 30px ">
                            <#list StatusEnum as s>
                                <option value="${s.getValue()}" <#if entity.status?? && entity.status==s.getValue()>selected</#if> >${s.getName()}</option>
                            </#list>
                        </select>
                    </td>
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