<div class="step_item step_item_5">
    <div class="ui_table">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td width="85" align="right">使用3G/4G：</td>
                <td colspan="4">
                    <span class="radio_box">
                        <input type="radio" class="radio" name="wireless" id="wireless_1_${pid}" value="1" checked/>
                        <label>允许</label>
                    </span>
                    <span class="radio_box">
                        <input type="radio" class="radio" name="wireless" id="wireless_0_${pid}" value="0"/>
                        <label>禁止</label>
                    </span>
                </td>
            </tr>
            <tr>
                <td width="85" align="right">日志上传：</td>
                <td colspan="4">
                    <span class="radio_box">
                        <input type="radio" class="radio" name="timeType" value="1" id="time_type_1_${pid}" onclick="$('#time_type_setting_1').show();$('#time_type_setting_2').hide();" checked/>
                        <label>时间段上传</label>
                    </span>
                    <span class="radio_box">
                        <input type="radio" class="radio" name="timeType" value="2" id="time_type_2_${pid}" onclick="$('#time_type_setting_1').hide();$('#time_type_setting_2').show();"/>
                        <label>实时上传</label>
                    </span>
                </td>
            </tr>
            <tr id="time_type_setting_1">
                <td align="right">上传时间段：</td>
                <td><input type="text" class="text easyui-timespinner" data-options="showSeconds:true" style="width:120px;height: 28px;" name="begin" value="00:00:00"/></td>
                <td align="right">~</td>
                <td><input type="text" class="text easyui-timespinner" data-options="showSeconds:true" style="width:120px;height: 28px;" name="end" value="01:00:00"/></td>
            </tr>
            <tr id="time_type_setting_2" style="display: none;">
                <td align="right">上传间隔：</td>
                <td><input type="text" class="text easyui-numberspinner" data-options="min: 10" style="width:50px;height: 28px;" name="interval" value="10"/>(分钟)</td>
            </tr>
        </table>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var container = $('#${pid}').find('.step_item_5');
        var begin = container.find('input[name=begin]');
        var end = container.find('input[name=end]');
        var interval = container.find('input[name=interval]');
        var form = win.find('form');

        function setTimeType(value) {
            if(value.timeType == 1) {
                $('#time_type_1_${pid}').attr('checked', true);
                $('#time_type_2_${pid}').attr('checked', false);
                $('#time_type_setting_1').show();
                $('#time_type_setting_2').hide();
                begin.timespinner('setValue', value.begin);
                end.timespinner('setValue', value.end);
            } else {
                $('#time_type_1_${pid}').attr('checked', false);
                $('#time_type_2_${pid}').attr('checked', true);
                $('#time_type_setting_1').hide();
                $('#time_type_setting_2').show();
                interval.numberspinner('setValue', value.interval);
            }
        }

        var showValue = function() {
            var values = win.data('values');
            var setting = values.logSetting;

            if(setting.wireless) {
                $('#wireless_1_${pid}').attr('checked', true);
                $('#wireless_0_${pid}').attr('checked', false);
            } else {
                $('#wireless_1_${pid}').attr('checked', false);
                $('#wireless_0_${pid}').attr('checked', true);
            }
            setTimeType(setting);
        }
        var collectValue = function() {

            var values = win.data('values');
            var setting = {};
            setting.wireless = $('#wireless_1_${pid}').attr('checked') ? 1 : 0;
            setting.timeType = $('#time_type_1_${pid}').attr('checked') ? 1 : 2;
            if(setting.timeType == 1) {
                if(begin.timespinner('getValue') == '') {
                    $.messager.alert('提示信息', '开始时间不能为空');
                    return false;
                }
                if(end.timespinner('getValue') == '') {
                    $.messager.alert('提示信息', '结束时间不能为空');
                    return false;
                }

                setting.begin = begin.timespinner('getValue');
                setting.end = end.timespinner('getValue');

                if(setting.begin >= setting.end) {
                    $.messager.alert('提示信息', '结束时间必须大于开始时间');
                    return false;
                }
            } else {
                if(interval.numberspinner('getValue') == '') {
                    $.messager.alert('提示信息', '间隔时间不能为空');
                    return false;
                }

                setting.interval = interval.numberspinner('getValue');
            }
            values.logSetting = setting;
            return true;
        }
        win.data('showValue5', showValue);
        win.data('collectValue5', collectValue);
    })();
</script>