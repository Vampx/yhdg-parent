<style type="text/css">
    .popup_body .ui_table table tr td .portrait a img {
        width: 420px;
        height: 420px;
    }
    .popup_body .ui_table table tr td .portrait {
        width: 420px;
        height: 420px;
    }
</style>
<div class="popup_body" style="height: 180px">
    <form method="post" style="height: 180px">
        <input type="hidden" name="id" value="${entity.id}">
        <input type="hidden" name="priceGroupId" value="${(entity.priceGroupId)!''}">
        <fieldset style="height: 450px">
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="300">
                            <div class="portrait" num="1">
                                <a href="javascript:void(0)"><img id="image_1_${pid}" src="<#if entity.imagePath1?? && entity.imagePath1?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.imagePath1}<#else>${app.imagePath}/user.jpg</#if>" /><span>修改图片</span></a>
                                <input  type="hidden" id="image_path_1_${pid}" name="imagePath1" value="${(entity.imagePath1)!''}"/>
                                <input  type="hidden" id="image_path_2_${pid}" name="imagePath2" value="${(entity.imagePath2)!''}"/>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </fieldset>
    </form>
</div>

<script>
    function set_image(param) {
        $('#image_' +  param.num  + '_${pid}').attr('src', '${(controller.appConfig.staticUrl)!''}' + param.filePath1);
        $('#image_path_' +  param.num  + '_${pid}').val(param.filePath1);
        $('#image_path_2_${pid}').val(param.filePath2);
        $('#' + param.pid).window('close');
    }
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('.portrait').click(function() {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传',
                href: "${contextPath}/security/zc/shop_rent_car/image.htm?num=" + $(this).attr('num'),
                event: {
                    onClose: function() {
                    }
                }
            });
        });
        var snapshot = $.toJSON({
            id: $("#id_${pid}").val(),
            priceGroupId: win.find('input[name=priceGroupId]').val(),
            imagePath1: $("#image_path_1_${pid}").val(),
            imagePath2: $("#image_path_2_${pid}").val(),

        });

        var ok=function(){
            if(!form.form('validate')){
                return false;
            }
            var success = true;

            var values = {
                id: '${entity.id}',
                priceGroupId: win.find('input[name=priceGroupId]').val(),
                imagePath1: form[0].imagePath1.value,
                imagePath2: form[0].imagePath2.value,
            };

            if(snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/zc/shop_rent_car/update_image.htm',
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
        win.data('ok',ok);
    })();
</script>


