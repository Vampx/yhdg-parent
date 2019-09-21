<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">ID：</td>
                    <td><input type="text"  readonly class="text easyui-validatebox" required="true" value="${(entity.id)!''}"/></td>
                </tr>
                <tr>
                    <td width="120" align="right">商户账户类型：</td>
                    <td><input type="text"  readonly class="text easyui-validatebox" required="true" value="${(entity.partnerTypeName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">业务id：</td>
                    <td><input type="text"  readonly class="text easyui-validatebox" required="true" value="${(entity.bizId)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">业务类型：</td>
                    <td><input type="text"  readonly class="text easyui-validatebox" required="true" value="${(entity.bizTypeName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">类型：</td>
                    <td><input type="text"  readonly class="text easyui-validatebox" required="true" value="${(entity.typeName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">金额：</td>
                    <td><input type="text"  readonly class="text easyui-validatebox" required="true" value="${(entity.money/100)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">操作人：</td>
                    <td><input type="text"  readonly class="text easyui-validatebox" required="true" value="${(entity.operator)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">创建时间：</td>
                    <td><input type="text"  readonly class="text easyui-validatebox" required="true" value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"/></td>
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
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>