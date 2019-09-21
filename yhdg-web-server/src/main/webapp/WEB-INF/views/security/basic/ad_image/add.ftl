<div class="popup_body" style="height: 180px">
    <form method="post" style="height: 180px">
        <input type="hidden" name="imagePath" id="portrait_${pid}">
        <fieldset style="height: 180px">
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="60" height="60" align="right" valign="top"  style="padding-top:10px;">点击图片：</td>
                        <td >
                            <div class="portrait">
                                <a href="javascript:void(0)"><img id="image_${pid}" src="${app.imagePath}/user.jpg" /><span>上传</span></a>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" valign="right" style="padding-top: 10px">排序：</td>
                        <td>
                            <input name="orderNum" class="easyui-numberspinner" required="required" style="width:180px; height: 30px;" type="text" value="${(entity.orderNum)!''}">
                            数值越小越靠前。
                            <div></div>
                        </td>
                    </tr>
                </table>
            </div>
        </fieldset>

    </form>
</div>
<div class="popup_btn" >
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    function set_portrait(param){
        $('#image_${pid}').attr('src', param.filePath);
        $('#portrait_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/ad_image/create.htm',
                success: function(text) {
                    var json = $.evalJSON(text);
                    <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('.portrait').click(function() {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传',
                href: "${contextPath}/security/basic/ad_image/portrait.htm",
                event: {
                    onClose: function() {
                    }
                }
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

    })();
</script>


