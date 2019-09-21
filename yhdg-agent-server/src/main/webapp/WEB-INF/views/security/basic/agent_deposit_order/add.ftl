<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="100" align="right">充值金额（元）：</td>
                    <td><input type="text" class="easyui-numberbox" style="height: 35px" data-options="min:0,precision:2" required="true" name="dMoney" value="${(entity.money/100)!''}"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/agent_deposit_order/create_by_weixin_mp.htm',
                onSubmit: function(param) {
                    if(!form.form('validate')) {
                        return false;
                    }
                    var tree = $('#oper_tree');
                    var operIds = [];
                    var nodes = tree.tree('getChecked');
                    for(var i = 0; i < nodes.length; i++) {
                        var node = nodes[i];
                        if(node.attributes && node.attributes.id) {
                            operIds.push(node.attributes.id);
                        }
                    }
                    param.operIds = operIds;
                    return true;
                },
                success: function(text) {
                    var json = $.evalJSON(text);
                    <@app.json_jump/>
                    if(json.code==0) {
                        console.log(json.data.codeUrl);
                        qrcode(json.data.codeUrl,json.data.id);
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();

    function qrcode(url,id) {
        App.dialog.show({
            css: 'width:550px;height:462px;overflow:visible;',
            title: '二维码',
            href: "${contextPath}/security/basic/agent_deposit_order/qrcode.htm?url=" + url+"&id="+id,
            event: {
                onClose: function() {
                    reload();
                }
            }
        });
    }

</script>