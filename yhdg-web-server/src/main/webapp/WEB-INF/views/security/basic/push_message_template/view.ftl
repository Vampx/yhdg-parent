<div class="popup_body">
    <div class="ui_table">
        <form id="form_${pid}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">标题：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="title" value="${(entity.title)!''}" maxlength="40" readonly/></td>
                    <td width="70" align="right"></td>
                    <td>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">接收人：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" readonly name="receiver" value="${(entity.receiver)!''}" maxlength="40"/></td>
                    <td width="70" align="right"></td>
                    <td>
                    </td>
                </tr>
                <tr>
                    <td align="right">内容：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true" maxlength="160" readonly name="content" style="width:435px; height: 32px;" readonly>${(entity.content)!''}</textarea>
                    </td>
                </tr>
                <tr>
                    <td align="right">变量：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true" maxlength="80" name="variable" style="width:435px; height: 32px;" readonly>${(entity.variable)!''}</textarea>
                    </td>
                </tr>
                <td width="90" align="right">是否朗读：</td>
                <td>
                     <span class="radio_box">
                        <input type="radio" class="radio" name="isPlay" disabled id="isPlay_1" <#if entity.isPlay?? && entity.isPlay == 1>checked</#if>  value="1"/><label for="isPlay_1">是</label>
                     </span>
                    <span class="radio_box">
                        <input type="radio" class="radio" name="isPlay" disabled id="isPlay_0"  <#if entity.isPlay?? && entity.isPlay == 0>checked</#if> value="0"/><label for="isPlay_0">否</label>
                     </span>
                </td>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>

<script type="text/javascript">
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();


</script>