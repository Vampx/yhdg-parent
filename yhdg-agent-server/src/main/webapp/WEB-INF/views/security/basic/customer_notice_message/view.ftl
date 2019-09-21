<div class="popup_body">
    <div class="ui_table">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td width="70" align="right">通知标题：</td>
                <td><input type="text" class="text" readonly value="${(entity.title)!''}"/></td>
            </tr>
            <tr>
                <td width="70" align="right">客户姓名：</td>
                <td><input type="text" class="text" readonly value="${(entity.customerFullname)!''}"/></td>
            </tr>
            <tr>
                <td width="70" align="right">手机号码：</td>
                <td><input type="text" class="text" readonly value="${(entity.customerMobile)!''}"/></td>
            </tr>
            <tr>
                <td width="70" align="right">通知类型：</td>
                <td><input type="text" class="text" readonly value="<#list typeEnum as e><#if e.getValue() == entity.type>${e.getName()}</#if></#list>"/></td>
            </tr>
            <tr>
                <td width="70" align="right">创建时间：</td>
                <td><input type="text" class="text" readonly value="<#if (entity.createTime)??>${app.format_date_time(entity.createTime)}</#if>"/></td>
            </tr>
            <tr>
                <td align="right" valign="top" style="padding-top:10px;">通知内容：</td>
                <td><textarea  maxlength="256" readonly style="width:330px; height:100px;">${(entity.content)!''}</textarea></td>
            </tr>
        </table>
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
