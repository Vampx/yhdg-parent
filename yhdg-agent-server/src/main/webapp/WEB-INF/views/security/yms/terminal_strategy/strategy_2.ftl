<div class="step_item step_item_2">
    <div class="ui_table">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td width="80" align="right">选择日期：</td>
                <td colspan="4" style="padding-:10px;">
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
        <#list 0..2 as e>
            <tr>
                <td align="right">
                        <span class="check_box">
                            <input type="checkbox" class="checkbox" name="active"/>
                            <label>启用</label>
                        </span>
                </td>
                <td width="90" align="right">开机时间段：</td>
                <td><input type="text" class="text easyui-timespinner" data-options="showSeconds:true" style="width:152px; height:28px;" name="begin"/></td>
                <td align="right">~</td>
                <td><input type="text" class="text easyui-timespinner" data-options="showSeconds:true" style="width:152px; height:28px;" name="end"/></td>
            </tr>
        </#list>
        <#list 0..2 as e>
            <tr>
                <td align="right">
                        <span class="check_box">
                            <input type="checkbox" class="checkbox" name="active"/>
                            <label>启用</label>
                        </span>
                </td>
                <td align="right">重启时间：</td>
                <td><input type="text" class="text easyui-timespinner" data-options="showSeconds:true" style="width:152px; height:28px;"  name="rebootTime"/></td>
                <td align="right"></td>
                <td></td>
            </tr>
        </#list>
        </table>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var container = win.find('.step_item_2');
        var form = container.find('form');

        var weekList = container.find('input[name=week]');
        var activeList = container.find('input[name=active]');
        var beginList = container.find('input[name=begin]');
        var endList = container.find('input[name=end]');
        var rebootTimeList = container.find('input[name=rebootTime]');

        var showValue = function() {
            var values = win.data('values');
            var setting = values.switchSetting;

            for(var i = 0; i < setting.week.length; i++) {
                weekList.eq([setting.week[i] - 1]).attr('checked', true);
            }

            for(var i = 0; i < setting.openTime.length; i++) {
                var line = setting.openTime[i];
                activeList.eq(i).attr('checked', line.active == 1?true:false);
                beginList.eq(i).timespinner('setValue', line.begin);
                endList.eq(i).timespinner('setValue', line.end);
            }

            for(var i = 0; i < setting.rebootTime.length; i++) {
                var line = setting.rebootTime[i];
                activeList.eq(3 + i).attr('checked', line.active == 1?true:false);
                rebootTimeList.eq(i).timespinner('setValue', line.time);
            }

        }
        var collectValue = function() {
            var values = win.data('values');
            var setting = {
                week: [], openTime: [], rebootTime: []
            }

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

                    if(i < 3) {
                        var b = beginList.eq(i).timespinner('getValue');
                        var e = endList.eq(i).timespinner('getValue');

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
            }

            var openTimes = 0;
            for(var i = 0; i < len; i++) {
                var active = activeList.eq(i).attr('checked') ? 1 : 0;
                if(i < 3) {
                    var b = beginList.eq(i).timespinner('getValue');
                    var e = endList.eq(i).timespinner('getValue');
                    setting.openTime.push({active: active, begin: b, end: e});
                    if(active) {
                        openTimes++;
                    }
                } else {
                    var time = rebootTimeList.eq(i - 3).timespinner('getValue');
                    setting.rebootTime.push({active: active, time: time});
                }
            }

            if(openTimes == 0) {
                $.messager.alert('提示信息', '请至少勾选一个开机时间段');
                return false;
            }

            for(var i = 0; i < setting.openTime.length; i++) {
                if(i > 0 && setting.openTime[i].end < setting.openTime[i].begin) {
                    $.messager.alert('提示信息', '结束时间' + setting.openTime[i].end + '必须大于上一段开始时间');
                    return false;
                }
            }

            var map = {};
            for(var i = 0; i < setting.rebootTime.length; i++) {
                if(setting.rebootTime[i].active) {
                    if(!map[setting.rebootTime[i].time]) {
                        map[setting.rebootTime[i].time] = true;
                    } else {
                        $.messager.alert('提示信息', '重启时间重复', 'info');
                        return false;
                    }
                }

            }

            values.switchSetting = setting;
            return true;
        }

        win.data('showValue2', showValue);
        win.data('collectValue2', collectValue);
    })();
</script>