<div class="popup_body clearfix">
    <div class="ui_table">
    <table cellpadding="0" cellspacing="0">
        <tr>
            <td align="right">分组：</td>
            <td>
                <input name="groupId" id="group_id_${pid}" readonly class="easyui-combotree" editable="false" disabled style="width: 184px; height: 28px;" url="${contextPath}/security/yms/material_group/tree.htm?agentId=${(Session['SESSION_KEY_USER'].agentId)!''}" value="${(entity.groupId)!''}">
            </td>
            <td width="80" align="right">运营商名称：</td>
            <td>
                <input name="agentId" id="agent_id_${pid}" readonly class="easyui-combotree" editable="false" <#if (entity.agentId)??>disabled</#if> style="width: 184px; height: 28px;" url="${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}" value="${(entity.agentId)!''}">
            </td>
        </tr>
        <tr>
            <td width="80" align="right">节目名：</td>
            <td><input type="text" class="text easyui-validatebox" readonly name="materialName" value="${(entity.materialName)!''}" /></td>
            <td align="right">节目时长：</td>
            <td><input type="text" class="text easyui-validatebox" readonly name="duration" value="${(entity.duration)!''}" /></td>
        </tr>
        <tr>
            <td width="80" align="right">节目状态：</td>
            <td>
                <select class="easyui-combobox" required="true" readonly="readonly" name="convertStatus" style="width:180px;height: 30px ">
                <#list videoConvertStatusEnum as v>
                    <option value="${v.getValue()}" <#if entity.convertStatus?? && entity.convertStatus==v.getValue()>selected</#if> >${v.getName()}</option>
                </#list>
                </select>
            </td>
            <td align="right">文件大小：</td>
            <td><input type="text" class="text easyui-validatebox" readonly name="size" value="${(app.format_file_size(entity.size))!''}" /></td>
        </tr>
        <tr>
            <td align="right">宽：</td>
            <td><input type="text" class="text easyui-validatebox" readonly name="width" value="${(entity.width)!''}" /></td>
            <td align="right">高：</td>
            <td><input type="text" class="text easyui-validatebox" readonly name="height" value="${(entity.height)!''}" /></td>
        </tr>
        <tr>
            <td align="right">文件路径：</td>
            <td><input type="text" class="text easyui-validatebox" readonly name="filePath" value="${(entity.filePath)!''}" /></td>
            <td align="right">业主：</td>
            <td><input type="text" class="text easyui-validatebox" readonly name="ownerName" value="${(entity.ownerName)!''}" /></td>
            <td align="right"></td>
            <td></td>
        </tr>
    </table>
</div>
<div class="popup_btn">
    <input type="button" style="width: 80px;height: 28px;" onclick="preview_${pid}('${(entity.filePath)!""}')" value="预览" >
    <button class="btn btn_border close" style="margin-right: 15px;">关闭</button>
</div>

<script>
    (function() {
        var win = $('#${pid}');
        var list = $('#tab_container_${pid} li');
        list.click(function() {
            var me = $(this);
            if(me.attr('class') && me.attr('class').indexOf('selected') >= 0) {
                return;
            }

            list.removeClass('selected');
            me.addClass('selected');
            $('#page_${pid}').panel('refresh', me.attr('url'));
        });


        win.find('.close').click(function() {
            win.window('close');
        });
    })();
    function preview_${pid}(path) {
        App.dialog.show({
            options:'maximized:true',
            title: '查看',
            href: "${controller.appConfig.staticUrl}/security/material/preview.htm?path=" + path
        });
    }
</script>