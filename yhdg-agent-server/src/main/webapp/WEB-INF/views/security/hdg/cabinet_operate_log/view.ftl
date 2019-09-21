<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <input type="hidden" name="agentId" value="${entity.agentId}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">换电柜名称：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.cabinetName)!''}"/>
                    </td>

                </tr>
                <tr>
                    <td align="right">箱号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="title" readonly
                               value="${(entity.boxNum)!''}"/></td>
                    <td align="right" width="50">操作类型：</td>
                    <td><input type="text" class="text easyui-validatebox" name="title" readonly
                               value="${operateTypeName}"/></td>
                </tr>
                <tr>
                    <td align="right" width="50">操作人：</td>
                    <td><input type="text" class="text easyui-validatebox" name="title" readonly
                               value="${(entity.operator)!''}"/></td>
                    <td align="right" width="50">操作时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" name="create_time" readonly
                               style="width:184px;height:28px"
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">日志内容：</td>
                    <td colspan="4"><textarea style="width:433px; height: 110px;" name="content"
                                              readonly>${entity.content}</textarea></td>
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
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.close').click(function () {
            win.window('close');
        });

    })()
</script>
