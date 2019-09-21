<div class="popup_body" style="padding-left:50px;padding-top: 40px;font-size: 14px;">
    <div class="tab_item" style="display:block;">
        <div class="ui_table">
            <form method="post">
                <input type="hidden" name="type" value="${entity.type}">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                    <tr>
                        <td width="70" align="left">运营商：</td>
                        <td><input type="text" maxlength="40" readonly class="text easyui-validatebox" required="true" name="agentName" value="${(entity.agentName)!''}"/></td>
                    </tr>
                    </tr>
                    <tr>
                        <td width="80" align="left">赠送类型：</td>
                        <td>
                            <select class="easyui-combobox" readonly="readonly" required="true"  name="type" style="width:180px;height: 30px ">
                            <#list TypeEnum as s>
                                <option value="${s.getValue()}" <#if entity.type?? && entity.type == s.getValue()>selected</#if> >${s.getName()}</option>
                            </#list>
                            </select>
                        </td>
                    </tr>
                    <tr id="pay_count_${pid}">
                        <td width="70" align="left">购买天数：</td>
                        <td><input id="count_${pid}" type="text" maxlength="6" readonly class="easyui-numberspinner" value="${(entity.payCount)!''}"
                                   style="width: 180px; height: 28px;" name="payCount" data-options="min:0, precision:0"/>
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="left">有效天数：</td>
                        <td><input type="text" maxlength="6" class="easyui-numberspinner" readonly value="${(entity.dayCount)!''}" style="width: 180px; height: 28px;" name="dayCount"  data-options="min:1, precision:0" /></td>
                    </tr>
                    <tr>
                        <td width="80" align="left">金额：</td>
                        <td><input id="money_${pid}" class="easyui-numberspinner" readonly value="${((entity.money)/ 100 + "元")!''}"  style="width:180px;height: 30px " required="required" data-options="min:0.01,precision:2">&nbsp;&nbsp;元</td>
                    </tr>
                    <tr>
                        <td width="90" align="left">是否启用：</td>
                        <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="ais_active_1_${pid}" <#if entity.isActive?? && entity.isActive == 1>checked</#if> checked value="1"/><label for="ais_active_1_${pid}">启用</label>
                        </span>
                            <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="is_active_0_${pid}"  <#if entity.isActive?? && entity.isActive == 0>checked</#if> value="0"/><label for="is_active_0_${pid}">禁用</label>
                        </span>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var list = $('#tab_container_${pid} li');
        var type = win.find('input[name=type]').val();
        if(type == 3){
            $("#pay_count_${pid}").show();
        } else {
            $("#pay_count_${pid}").hide();
        }
        win.find('.close').click(function() {
            win.window('close');
        });
    })();
</script>