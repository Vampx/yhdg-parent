<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" id="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">客户姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly
                               value="${(entity.customerFullname)!''}"/></td>
                    <td width="70" align="right">客户手机：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly
                               value="${(entity.customerMobile)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">审核人员：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly
                               value="${(entity.operator)!''}"/></td>
                    <td width="70" align="right">审核状态：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly
                               value="${(entity.stateName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">审核时间：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly
                               value="<#if (entity.handleTime)?? >${app.format_date_time(entity.handleTime)}</#if>"/></td>
                    <td width="70" align="right">申请时间：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">离职原因：</td>
                    <td colspan="3">
                        <textarea style="width:500px;" maxlength="256" readonly>${(entity.content)!''}</textarea>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">审批备注：</td>
                    <td colspan="3">
                        <textarea style="width:500px;" maxlength="256" readonly id="reason">${(entity.reason)!''}</textarea>
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
    (function () {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.close').click(function () {
            win.window('close');
        });
    })();
</script>