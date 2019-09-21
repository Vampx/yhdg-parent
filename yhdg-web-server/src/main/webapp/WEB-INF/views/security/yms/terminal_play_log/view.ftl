<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">运营商：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="agentId" value="${(entity.agentId)!''}"/></td>
                    <td width="70" align="right">终端编号：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="terminalId" value="${(entity.terminalId)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">显示区域编号：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="routeId" value="${(entity.areaNum)!''}"/></td>
                    <td width="70" align="right">节目名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="programName" value="${(entity.materialName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">开始时间：</td>
                    <td><input  class="text easyui-datetimebox" data-options="required:true,showSeconds:true" style="width:183px;height: 28px " required="true" name="beginTime" value="${((entity.beginTime)?string("yyyy-MM-dd HH:mm:ss"))!''}"/></td>
                    <td width="70" align="right">结束时间：</td>
                    <td><input  class="text easyui-datetimebox" data-options="required:true,showSeconds:true" style="width:183px;height: 28px " required="true"  name="endTime" value="${((entity.endTime)?string("yyyy-MM-dd HH:mm:ss"))!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">创建时间：</td>
                    <td><input  class="text easyui-datetimebox" data-options="required:true,showSeconds:true" style="width:183px;height: 28px " required="true" name="createTime" value="${((entity.createTime)?string("yyyy-MM-dd HH:mm:ss"))!''}"/></td>
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