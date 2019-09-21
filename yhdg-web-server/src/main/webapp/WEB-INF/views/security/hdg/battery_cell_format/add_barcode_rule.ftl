<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">规则名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="regularName" maxlength="40" value=""/></td>
                </tr>
                <tr><td colspan="2"><span class="border"></span></td></tr>
                <tr>
                    <td>流水号：</td>
                    <td class="left"><a href="javascript:void(0)" class="regular_element">NNNN</a></td>
                </tr>
                <tr><td colspan="2"><span class="border"></span></td></tr>
                <tr>
                    <td>年：</td>
                    <td class="left"><a href="javascript:void(0)" class="regular_element">YYYY</a>（例如:2012）或<a href="#" class="regular_element">YY</a>（例如:12）</td>
                </tr>
                <tr><td colspan="2"><span class="border"></span></td></tr>
                <tr>
                    <td>月：</td>
                    <td class="left"><a href="javascript:void(0)" class="regular_element">MM</a>（例如:10）</td>
                </tr>
                <tr><td colspan="2"><span class="border"></span></td></tr>
                <tr>
                    <td>日：</td>
                    <td class="left"><a href="javascript:void(0)" class="regular_element">DD</a>（例如:25)</td>
                </tr>
                <tr><td colspan="2"><span class="border"></span></td></tr>
                <tr>
                    <td>周：</td>
                    <td class="left"><a href="javascript:void(0)" class="regular_element">WW</a>（例如:第25周)</td>
                </tr>
                <tr><td colspan="2"><span class="border"></span></td></tr>
                <tr>
                    <td>清除方式：</td>
                    <td class="left">
                        <input type="radio" class="check" name="resetType" id="mm_${pid}" value="1" /><label for="mm_${pid}">月</label>
                        <input type="radio" class="check" name="resetType" id="ww_${pid}" value="2" /><label for="ww_${pid}">周</label>
                        <input type="radio" class="check" name="resetType" id="dd_${pid}" checked="checked" value="3" /><label for="dd_${pid}">日</label>
                    </td>
                </tr>
                <tr><td colspan="2"><span class="border"></span></td></tr>
                <tr>
                    <td>规则方式：</td>
                    <td class="left"><input type="text" class="text" style="width: 200px;" required="true" id="regular_${pid}" name="regular" /></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red rule_ok">确定</button>
    <button class="btn btn_border rule_close">关闭</button>
</div>
<script>

    $('a.regular_element').click(function() {
        $('#regular_${pid}').val( $('#regular_${pid}').val() + $(this).text() );
        $('#regular_${pid}').focus();
    });

    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.rule_ok').click(function () {
            var regularName = win.find('input[name=regularName]').val();
            if(regularName == null || regularName == '') {
                $.messager.alert('提示信息', '请输入规则名称', 'info');
                return false;
            }
            var regular = win.find('input[name=regular]').val();
            if(regular == null || regular == '') {
                $.messager.alert('提示信息', '请输入规则方式', 'info');
                return false;
            }

            if($('#regular_${pid}').val().indexOf('N') == -1) {
                $.messager.alert('提示信息', '编号规则错误(编号规则要包含流水号)', 'info');
                return false;
            }
            if($('#regular_${pid}').val().indexOf('YY') == -1 && $('#regular_${pid}').val().indexOf('YYYY') == -1) {
                $.messager.alert('提示信息', '编号规则错误(编号规则要包含日期YY或YYYY)', 'info');
                return false;
            }
            if($('#regular_${pid}').val().indexOf('MM') == -1) {
                $.messager.alert('提示信息', '编号规则错误(编号规则要包含日期MM)', 'info');
                return false;
            }
            if($('#regular_${pid}').val().indexOf('DD') == -1) {
                $.messager.alert('提示信息', '编号规则错误(编号规则要包含日期DD)', 'info');
                return false;
            }

            var resetType;
            if(document.getElementById('mm_${pid}').checked) {
                resetType = 1;
            } else if(document.getElementById('ww_${pid}').checked) {
                resetType = 2;
                if($('#regular_${pid}').val().indexOf('WW') == -1) {
                    $.messager.alert('提示信息', '按周清除时,编号规则要包含日期WW', 'info');
                    return false;
                }
            } else if(document.getElementById('dd_${pid}').checked) {
                resetType = 3;
            }

            var windowData = win.data('windowData');
            windowData.ok({
                regularType: 1,
                regular: win.find('input[name=regular]').val(),
                regularName: win.find('input[name=regularName]').val(),
                resetType: resetType
            });
            win.window('close');
        });

        win.find('button.rule_close').click(function () {
            win.window('close');
        });
    })()
</script>