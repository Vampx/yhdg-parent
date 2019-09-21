<div class="popup_body">
    <div class="ui_table">
        <form id="form_${pid}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">源类型：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="type" value="<#list sourceTypeEnum as type><#if type.getValue() == entity.sourceType >${type.getName()}</#if></#list>"></td>
                    <td width="70" align="right">手机号码：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="mobile" value="${(entity.mobile)!''}"></td>
                </tr>
                <tr>
                    <td width="70" align="right">处理时间：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="createTime" value="<#if (entity.handleTime)?? >${app.format_date_time(entity.handleTime)}</#if>"/></td>
                    <td width="70" align="right">创建时间：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="createTime" value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">模板id：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="type" value=" ${(entity.type)!''}"></td>
                    <td width="70" align="right">延迟时间：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="delay" value="${entity.delay!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">重发次数：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="resendNum" value="${(entity.resendNum)!''}"></td>
                    <td width="70" align="right">消息状态：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="status"  value="<#list messageStatusEnum as type><#if type.getValue() == entity.status >${type.getName()}</#if></#list>"></td>
                </tr>
                <tr>
                    <td width="70" align="right">阅读次数：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="readCount" value="${(entity.readCount)!''}"></td>
                    <td width="70" align="right">昵称：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="nickname"  value="${(entity.nickname)!''}"></td>
                </tr>
                <tr>
                    <td align="right">变量：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true" name="color" style="width:435px; height:200px;" readonly>${(entity.variable)!''}</textarea>
                    </td>
                </tr>
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