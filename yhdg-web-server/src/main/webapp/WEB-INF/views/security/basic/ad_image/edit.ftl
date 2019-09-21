<div class="popup_body" style="height: 180px">
    <form method="post" style="height: 180px">
        <input id="id_${pid}" type="hidden" name="id" value="${entity.id}">
        <input type="hidden" name="imagePath" id="portrait_${pid}" value="${(entity.imagePath)!''}">
        <fieldset style="height: 180px">
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="60" height="60" align="right" valign="top"  style="padding-top:10px;">点击图片：</td>
                        <td >
                            <div class="portrait">
                                <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.imagePath?? && entity.imagePath?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.imagePath}<#else>${app.imagePath}/user.jpg</#if>" /><span>修改图片</span></a>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" valign="right" style="padding-top: 10px">排序：</td>
                        <td>
                            <input id="order_num_${pid}" name="orderNum" class="easyui-numberspinner" required="required" style="width:180px; height: 30px;" type="text" value="${(entity.orderNum)!''}">
                            数值越小越靠前。
                            <div></div>
                        </td>
                    </tr>
                </table>
            </div>
        </fieldset>

    </form>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    function set_portrait(param) {
        $('#image_${pid}').attr('src', param.filePath);
        $('#portrait_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }
    (function() {
        var pid = '${pid}',
                win = $('#'+pid),
                jform = win.find('form'),
                form = jform[0];

        win.find('.portrait').click(function() {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传头像',
                href: "${contextPath}/security/basic/ad_image/portrait.htm",
                event: {
                    onClose: function() {
                    }
                }
            });
        });

        var snapshot = $.toJSON({
            id: $("#id_${pid}").val(),
            imagePath: $("#portrait_${pid}").val(),
            orderNum: $("#order_num_${pid}").val(),
        });
        var ok = function() {
            if(!jform.form('validate')) {
                return false;
            }
            var success = true;
            var values = {
                id: '${entity.id}',
                imagePath: form.imagePath.value,
                orderNum: form.orderNum.value,
            };

            if(snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/basic/ad_image/update.htm',
                    dataType: 'json',
                    data: values,
                    success: function (json) {
                    <@app.json_jump/>
                        if (json.success) {
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                            success = false;
                        }
                    }
                });
            }

            return success;
        }
        win.find('button.ok').click(function() {
            var ok = win.data('ok')();
            if(ok) {
                win.window('close');
            }
        });

        win.find('button.close').click(function() {
            win.window('close');
        });
        win.data('ok', ok);

    })();
</script>