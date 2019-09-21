<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="imagePath" id="image_path_${pid}">
            <input type="hidden" name="sourceId" id="sourceId" value="${sourceId}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right" valign="top"  style="padding-top:10px;">图片：</td>
                    <td >
                        <div class="portrait">
                            <a href="javascript:void(0)"><img id="image_${pid}" src="${app.imagePath}/user.jpg" /><span>修改图片</span></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right" valign="top"  style="padding-top:10px;">类型：</td>
                    <td >
                        <select style="width:80px;" id="category" name="category" >
                            <#list Category as e>
                                <option value="${e.getValue()}">${e.getName()}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">是否显示：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isShow" id="isShow_1" checked value="1"/><label for="isShow_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isShow" id="isShow_0" value="0"/><label for="isShow_0">否</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">排序号：</td>
                    <td><input style="height: 28px" class="easyui-numberspinner"  name="orderNum" value="1"/></td>
                </tr>
                <tr>
                    <td align="right">链接URL：</td>
                    <td><input type="text" class="text" maxlength="120" name="url"/></td>
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
    function set_image_path (param) {
        $('#image_${pid}').attr('src', '${(controller.appConfig.staticUrl)!''}' + param.filePath);
        $('#image_path_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/weixinmp/create_rotate_image.htm',
                success: function(text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
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
        win.find('.portrait').click(function() {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传图片',
                href: "${contextPath}/security/basic/weixinmp/photo_path.htm?typs=0",
                event: {
                    onClose: function() {
                    }
                }
            });
        });
    })()
    function set_portrait (param) {
        $('#image_${pid}').attr('src', '${(controller.appConfig.staticUrl)!''}' + param.filePath);
        $('#image_path_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }
</script>