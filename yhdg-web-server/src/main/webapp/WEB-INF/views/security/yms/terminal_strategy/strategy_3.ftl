<div class="step_item step_item_3">
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
                    <td width="90" align="right">默认音量：</td>
                    <td><input type="text" class="text easyui-timespinner" data-options="showSeconds:true, disabled:true" style="width:152px; height:28px;" value="00:00:00" disabled="disabled" /></td>
                    <td align="right"></td>
                    <td><input type="text" class="text easyui-timespinner" data-options="showSeconds:true, disabled:true" style="width:152px; height:28px;" value="24:00:00" disabled="disabled" /></td>
                    <td><input type="text" class="text easyui-numberspinner" data-options="min:0, max:15" required="true" style="width:50px; height:28px;" name="volume"/></td>
                </tr>
            <#list 0..7 as e>
                <tr>
                    <td align="right">
                                    <span class="check_box">
                                        <input type="checkbox" class="checkbox" name="active"/>
                                        <label>启用</label>
                                    </span>
                    </td>
                    <td width="90" align="right">时间段：</td>
                    <td><input type="text" class="text easyui-timespinner" data-options="showSeconds:true" style="width:152px; height:28px;" name="begin" id="begin_${e}_${pid}"/></td>
                    <td align="right">~</td>
                    <td><input type="text" class="text easyui-timespinner" data-options="showSeconds:true" style="width:152px; height:28px;" name="end" id="end_${e}_${pid}"/></td>
                    <td><input type="text" class="text easyui-numberspinner" data-options="min:0, max:15" style="width:50px; height:28px;" name="value"/></td>
                </tr>
            </#list>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var container = win.find('.step_item_3');
        var form = container.find('form');
        var volume = container.find('input[name=volume]');
        var weekList = container.find('input[name=week]');
        var activeList = container.find('input[name=active]');
        var beginList = [$('#begin_0_${pid}'), $('#begin_1_${pid}'),$('#begin_2_${pid}'),$('#begin_3_${pid}'),$('#begin_4_${pid}'),$('#begin_5_${pid}'),$('#begin_6_${pid}'),$('#begin_7_${pid}')];
        var endList = [$('#end_0_${pid}'), $('#end_1_${pid}'),$('#end_2_${pid}'),$('#end_3_${pid}'),$('#end_4_${pid}'),$('#end_5_${pid}'),$('#end_6_${pid}'),$('#end_7_${pid}')];
        var valueList = container.find('input[name=value]');

        var showValue = function() {

            var values = win.data('values');
            var setting = values.volumeSetting;
            volume.numberspinner('setValue', setting.volume)

            for(var i = 0; i < setting.week.length; i++) {
                weekList.eq([setting.week[i] - 1]).attr('checked', true);
            }

            for(var i = 0; i < setting.time.length; i++) {
                var line = setting.time[i];
                activeList.eq(i).attr('checked', line.active == 1?true:false);
                beginList[i].timespinner('setValue', line.begin);
                endList[i].timespinner('setValue', line.end);
                valueList.eq(i).numberspinner('setValue', line.volume);
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

            setting.volume = volume.numberspinner('getValue');

            container.find('input[name=week]:checked').each(function() {
                setting.week.push(this.value);
            });

            if(setting.week.length == 0) {
                $.messager.alert('提示信息', '请至少选择一天');
                return false;
            }
            for(var i = 0; i < activeList.length; i++) {
                if(activeList.eq(i).attr('checked')) {
                    var b = beginList[i].timespinner('getValue');
                    var e = endList[i].timespinner('getValue');
                    var v = valueList.eq(i).numberspinner('getValue');
                    if(b == '') {
                        $.messager.alert('提示信息', '开始时间不能为空');
                        return false;
                    }
                    if(e == '') {
                        $.messager.alert('提示信息', '结束时间不能为空');
                        return false;
                    }

                    var b_time = new Date(2017,5,15, beginList[i].timespinner('getHours'), beginList[i].timespinner('getMinutes'), beginList[i].timespinner('getSeconds'));
                    var e_time = new Date(2017,5,15, endList[i].timespinner('getHours'), endList[i].timespinner('getMinutes'), endList[i].timespinner('getSeconds'));
                    if(b_time >= e_time) {
                        $.messager.alert('提示信息', '结束时间必须大于开始时间');
                        return false;
                    }
                    setting.time.push({active: (activeList.eq(i).attr('checked') ? 1 : 0), begin: b, end: e, volume: v});
                }
            }
            for(var i = 0; i < setting.time.length; i++) {
                if(i > 0 && setting.time[i-1].end > setting.time[i].begin) {
                    $.messager.alert('提示信息', '开始时间' + setting.time[i].begin + '必须小于上一段结束时间');
                    return false;
                }
            }
            values.volumeSetting = setting;
            return true;
        }

        win.data('showValue3', showValue);
        win.data('collectValue3', collectValue);
    })();
</script>