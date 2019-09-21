<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <input type="hidden" name="type" value="${entity.type}">
                <tr>
                    <td width="80" align="right">赠送类型：</td>
                    <td>
                        <select class="easyui-combobox" readonly="readonly" required="true"  name="type" style="width:180px;height: 30px ">
                        <#list TypeEnum as s>
                            <option value="${s.getValue()}" <#if entity.type?? && entity.type == s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr id="pay_count_${pid}">
                    <td width="70" align="right">购买天数：</td>
                    <td><input id="count_${pid}" type="text" maxlength="6" readonly class="easyui-numberspinner" value="${(entity.payCount)!''}"
                               style="width: 180px; height: 28px;" name="payCount" data-options="min:0, precision:0"/>
                    </td>
                </tr>
                <tr id="wages_day_${pid}">
                    <td width="70" align="right">工资日：</td>
                    <td><input id="wages_${pid}" type="text" maxlength="2" readonly class="easyui-numberspinner" value="${(entity.wagesDay)!''}"
                               style="width: 180px; height: 28px;" name="wagesDay" data-options="min:1,max:31, precision:0"/>&nbsp;号
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">有效天数：</td>
                    <td><input type="text" maxlength="6" class="easyui-numberspinner" readonly value="${(entity.dayCount)!''}" style="width: 180px; height: 28px;" name="dayCount"  data-options="min:1, precision:0" /></td>
                </tr>
                <tr>
                    <td width="80" align="right">金额：</td>
                    <td><input id="money_${pid}" class="easyui-numberspinner" readonly value="${((entity.money)/ 100 + "元")!''}"  style="width:180px;height: 30px " required="required" data-options="min:0.01,precision:2">&nbsp;&nbsp;元</td>
                </tr>
                <tr>
                    <td width="90" align="right">是否启用：</td>
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
        if(type == 4){
            $("#wages_day_${pid}").show();
        }else{
            $("#wages_day_${pid}").hide();
        }
        win.find('.close').click(function() {
            win.window('close');
        });
    })();
</script>