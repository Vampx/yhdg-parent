<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">终端编号：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="terminalId" value="${(entity.terminalId)!''}"/></td>
                    <td width="70" align="right">当前：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="now" value="${(entity.now)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">标签：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="tag" value="${(entity.tag)!''}"/></td>
                    <td width="70" align="right">内容：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="content" value="${(entity.content)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">创建时间：</td>
                    <td><input  class="text easyui-datetimebox" data-options="required:true,showSeconds:true" style="width:183px;height: 28px " required="true" name="createTime" value="${((entity.createTime)?string("yyyy-MM-dd HH:mm:ss"))!''}"/></td>
                    <td width="70" align="right">报表时间：</td>
                    <td><input  class="text easyui-datetimebox" data-options="required:true,showSeconds:true" style="width:183px;height: 28px " required="true"  name="reportTime" value="${((entity.reportTime)?string("yyyy-MM-dd HH:mm:ss"))!''}"/></td>
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
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>