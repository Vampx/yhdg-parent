<div class="step_item step_item_4">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">选择日期：</td>
                    <td colspan="5">
                        <span class="check_box">
                            <input type="checkbox" class="checkbox" name="week" value="1"/>
                            <label>周一</label>
                        </span>
                        <span class="check_box">
                            <input type="checkbox" class="checkbox" name="week" value="2"/>
                            <label>周二</label>
                        </span>
                        <span class="check_box">
                            <input type="checkbox" class="checkbox" name="week" value="3"/>
                            <label>周三</label>
                        </span>
                        <span class="check_box">
                            <input type="checkbox" class="checkbox" name="week" value="4"/>
                            <label>周四</label>
                        </span>
                        <span class="check_box">
                            <input type="checkbox" class="checkbox" name="week" value="5"/>
                            <label>周五</label>
                        </span>
                        <span class="check_box">
                            <input type="checkbox" class="checkbox" name="week" value="6"/>
                            <label>周六</label>
                        </span>
                        <span class="check_box">
                            <input type="checkbox" class="checkbox" name="week" value="7"/>
                            <label>周日</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right"></td>
                    <td width="90" align="right">默认速度：</td>
                    <td><input type="text" class="text easyui-timespinner" data-options="showSeconds:true, disabled:true" style="width:152px; height:28px;" value="00:00:00" disabled="disabled" /></td>
                    <td align="right"></td>
                    <td><input type="text" class="text easyui-timespinner" data-options="showSeconds:true, disabled:true" style="width:152px; height:28px;" value="24:00:00" disabled="disabled" /></td>
                    <td><input type="text" class="text easyui-numberspinner" data-options="min:0, max:100" required="true" style="width:50px; height:28px;" name="speed"/>KB/S</td>
                </tr>
            <#list 0..2 as e>
                <tr>
                    <td align="right">
                        <span class="check_box">
                            <input type="checkbox" class="checkbox" name="active"/>
                            <label>启用</label>
                        </span>
                    </td>
                    <td width="90" align="right">时间段：</td>
                    <td><input type="text" class="text easyui-timespinner" data-options="showSeconds:true" style="width:152px; height:28px;" name="begin"/></td>
                    <td align="right">~</td>
                    <td><input type="text" class="text easyui-timespinner" data-options="showSeconds:true" style="width:152px; height:28px;" name="end"/></td>
                    <td><input type="text" class="text easyui-numberspinner" data-options="min:0, max:100" style="width:50px; height:28px;" name="value"/>KB/S</td>
                </tr>
            </#list>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var container = win.find('.step_item_4');
        var form = container.find('form');

        var speed = container.find('input[name=speed]');
        var weekList = container.find('input[name=week]');
        var activeList = container.find('input[name=active]');
        var beginList = container.find('input[name=begin]');
        var endList = container.find('input[name=end]');
        var valueList = container.find('input[name=value]');

        var showValue = function() {

            var values = win.data('values');
            var setting = values.downloadSetting;

            speed.numberspinner('setValue', setting.speed)

            for(var i = 0; i < setting.week.length; i++) {
                weekList.eq([setting.week[i] - 1]).attr('checked', true);
            }

            for(var i = 0; i < setting.time.length; i++) {
                var line = setting.time[i];
                activeList.eq(i).attr('checked', line.active == 1?true:false);
                beginList.eq(i).timespinner('setValue', line.begin);
                endList.eq(i).timespinner('setValue', line.end);
                valueList.eq(i).numberspinner('setValue', line.speed);
            }

        }
        var collectValue = function() {
            var values = win.data('values');
            var setting = {
                week: [], time: []
            };

            if(!form.form('validate')) {
                return false;
            }

            setting.speed = speed.timespinner('getValue');

            container.find('input[name=week]:checked').each(function() {
                setting.week.push(this.value);
            });

            if(setting.week.length == 0) {
                $.messager.alert('提示信息', '请至少选择一天');
                return false;
            }

            var len = activeList.length;
            for(var i = 0; i < len; i++) {
                if(activeList.eq(i).attr('checked')) {
                    var b = beginList.eq(i).timespinner('getValue');
                    var e = endList.eq(i).timespinner('getValue');
                    var v = valueList.eq(i).numberspinner('getValue');

                    if(b == '') {
                        $.messager.alert('提示信息', '开始时间不能为空');
                        return false;
                    }
                    if(e == '') {
                        $.messager.alert('提示信息', '结束时间不能为空');
                        return false;
                    }
                    if(b >= e) {
                        $.messager.alert('提示信息', '结束时间必须大于开始时间');
                        return false;
                    }
                }
            }

            for(var i = 0; i < setting.time.length; i++) {
                if(i > 0 && setting.time[i].end < setting.time[i].begin) {
                    $.messager.alert('提示信息', '结束时间' + setting.time[i].end + '必须大于上一段开始时间');
                    return false;
                }
            }

            for(var i = 0; i < len; i++) {
                setting.time.push({active: (activeList.eq(i).attr('checked') ? 1 : 0), begin: b, end: e, speed: v});
            }

            values.downloadSetting = setting;
            return true;
        }

        win.data('showValue4', showValue);
        win.data('collectValue4', collectValue);
    })();
</script>