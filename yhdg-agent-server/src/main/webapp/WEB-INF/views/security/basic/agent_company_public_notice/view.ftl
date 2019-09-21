<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" readonly
                               editable="false" style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                       swich_agent_${pid}();
                                    }"
                               value="${(entity.agentId)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">公告名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="title" readonly value="${entity.title}"/></td>
                </tr>
                <tr>
                    <td align="right" width="50">公告类型：</td>
                    <td>
                        <select style="width:185px;" name="noticeType" disabled>
                            <option value="1">客户公告</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">公告内容：</td>
                    <td colspan="3"><textarea style="width:400px; height: 110px;" name="content" readonly>${entity.content}</textarea></td>
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
