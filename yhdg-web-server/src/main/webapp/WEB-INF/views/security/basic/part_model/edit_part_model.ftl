<div class="popup_body" style="min-height: 65%;">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <#--<tr>-->
                    <#--<td align="right">运营商：</td>-->
                    <#--<td>-->
                        <#--<input name="agentId" class="easyui-combotree" editable="true" style="width: 182px;height: 28px;"-->
                               <#--data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',-->
                                <#--method:'get',-->
                                <#--valueField:'id',-->
                                <#--textField:'text',-->
                                <#--editable:true,-->
                                <#--multiple:false,-->
                                <#--panelHeight:'200',-->
                                <#--onClick: function(node) {-->
                                <#--}-->
                            <#--"-->
                        <#-->-->
                    <#--</td>-->
                <#--</tr>-->
                <tr>
                    <td>角色模板类型：</td>
                    <td>
                        ${(partModelTypeName)!''}
                        <input type="hidden" name="partModelType" value="${(partModelType)!''}">
                    </td>
                </tr>
                <tr>
                    <td>角色模板名称：</td>
                    <td>
                        <input type="text" name="partModelName" class="text easyui-validatebox" required="true" value="${(entity.partModelName)!''}" />
                    </td>
                </tr>
                    <tr>
                        <td align="right" valign="top" style="padding-top:10px;">权限分配：</td>
                        <td>
                            <div style="width:330px; height:150px; padding:5px; border:1px solid #ddd; overflow:auto;">
                                <ul id="perm_tree" class="easyui-tree" url="${contextPath}/security/basic/part/tree.htm?id=${(entity.id)!''}" checkbox="true" cascadeCheck="true" lines="true"></ul>
                            </div>
                        </td>
                    </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn" style="height: 10%">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>


    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });

    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                 windowData = win.data('windowData');
        var form = win.find('form');

        win.find('button.ok').click(function () {
            if(!form.form('validate')) {
                return false;
            }
            var partModelType = win.find('input[name=partModelType]').val();
            var partModelName = win.find('input[name=partModelName]').val();
            var tree = $('#perm_tree');
            var permIds = [];
            var nodes = tree.tree('getChecked');
            for(var i = 0; i < nodes.length; i++) {
                var node = nodes[i];
                if(node.attributes && node.attributes.id) {
                    permIds.push(node.attributes.id);
                }
            }
            var partModel = {
                id: ${(entity.id)!''},
                partModelType: partModelType,
                partModelName: partModelName,
                permIds: permIds
            }
            windowData.ok(partModel);
            win.window('close');
        })

        win.find('button.close').click(function () {
            win.window('close');
        });
    })()

</script>






