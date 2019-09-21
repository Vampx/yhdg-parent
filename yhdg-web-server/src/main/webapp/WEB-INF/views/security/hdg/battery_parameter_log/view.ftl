<div class="popup_body">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="110" align="right">参数：</td>
                    <td><input type="text" class="text easyui-validatebox"  value="${(entity.paramId)!''}"/></td>
                </tr>
                <tr>
                    <td width="110" align="right">参数名称：</td>
                    <td><input type="text" class="text easyui-validatebox"  value="${(entity.paramName)!''}"/></td>
                </tr>
                <tr>
                    <td width="110" align="right">旧值：</td>
                    <td><input type="text" class="text easyui-validatebox"  value="${(entity.oldValue)!''}"/></td>
                </tr>
                <tr>
                    <td width="110" align="right">新值：</td>
                    <td><input type="text" class="text easyui-validatebox"  value="${(entity.newValue)!''}"/></td>
                </tr>
                <tr>
                    <td width="110" align="right">状态：</td>
                    <td><input type="text" class="text easyui-validatebox"  value="${(entity.statusName)!''}"/></td>
                </tr>
                <tr>
                    <td width="110" align="right">上报时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" name="reportTime" readonly
                               style="width:184px;height:28px"
                               value="<#if (entity.reportTime)?? >${app.format_date_time(entity.reportTime)}</#if>"/>
                    </td>
                </tr>
                <tr>
                    <td width="110" align="right">上报人：</td>
                    <td><input type="text" class="text easyui-validatebox"  value="${(entity.operator)!''}"/></td>
                </tr>
                <tr>
                    <td width="110" align="right">创建时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" name="reportTime" readonly
                               style="width:184px;height:28px"
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"/>
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
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.close').click(function () {
            win.window('close');
        });

    })()
</script>
