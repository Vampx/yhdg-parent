<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="transferImagePath" id="transfer_image_path_${pid}" value="${(entity.transferImagePath)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text easyui-validatebox"  name="fullName" value="${(entity.fullName)!''}" style="width: 175px; height: 28px;"/></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td align="right">状态：</td>
                    <td>
                        <select name="status" class="easyui-combobox" style="width: 184px; height: 28px;" editable="false">
                        <#list StatusEnum as e>
                            <option value="${e.getValue()}" <#if entity.status?? && (entity.status == e.value)>selected</#if>> ${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">运营商名称：</td>
                    <td><input type="text" class="text easyui-validatebox"    name="agentName" value="${(entity.agentName)!''}" /></td>
                </tr>
                <tr>
                    <td align="right">OpenId：</td>
                    <td><input type="text" class="text easyui-validatebox"    name="openId" value="${(entity.openId)!''}" /></td>
                    <td align="right">金额：</td>
                    <td><input  class="easyui-numberspinner"  style="width:160px;height:28px " required="required" value="${(entity.money)/ 100 !''}" data-options="min:0.00,precision:2">&nbsp;&nbsp;元</td>
                </tr>
                <tr>
                    <td align="right">处理时间：</td>
                    <td><input type="text" class="text easyui-validatebox"    name="handleTime" value="<#if (entity.handleTime)?? >${app.format_date_time(entity.handleTime)}</#if>" /></td>
                    <td align="right">确认时间：</td>
                    <td><input type="text" class="text easyui-validatebox"    name="confirmTime" value="<#if (entity.confirmTime)?? >${app.format_date_time(entity.confirmTime)}</#if>" /></td>
                </tr>

                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">处理结果：</td>
                    <td><textarea style="width:200px;" name="handleResult" maxlength="400">${(entity.handleResult)!''}</textarea></td>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:200px;" name="memo" maxlength="200">${(entity.memo)!''}</textarea></td>
                </tr>
                <tr>
                    <td width="70" align="right" valign="top" rowspan="2" style="padding-top:10px;">凭证：</td>
                    <td rowspan="2">
                        <div class="portrait">
                            <a href="javascript:void(0)"><img id="image_${pid}" src=<#if entity.transferImagePath ?? && entity.transferImagePath != ''>'${staticUrl}${(entity.transferImagePath)!''}' <#else>'${app.imagePath}/user.jpg'</#if> /></a>
                        </div>
                    </td>
                </tr>

            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var ok = function() {
            var success = true;
            return success;
        };
        win.data('ok', ok);
    })();
</script>