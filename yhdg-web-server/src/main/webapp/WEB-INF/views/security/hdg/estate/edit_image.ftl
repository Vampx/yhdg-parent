<div class="popup_body" style="height: 180px">
    <form method="post" style="height: 180px">
        <input type="hidden" name="id" value="${entity.id}">
        <input type="hidden" name="priceGroupId" value="${(entity.priceGroupId)!''}">
        <input type="hidden" name="workTime" value="${(entity.workTime)!''}">
        <fieldset style="height: 180px">
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100">
                            <i class="fa fa-fw fa-close" shop_id="${(entity.id)!0}" num="${1}"></i>

                            <div class="portrait" num="1">
                                <a href="javascript:void(0)"><img id="image_1_${pid}" src="<#if entity.imagePath1?? && entity.imagePath1?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.imagePath1}<#else>${app.imagePath}/user.jpg</#if>" /><span>修改图片</span></a>
                                <input  type="hidden" id="image_path_1_${pid}" name="imagePath1" value="${(entity.imagePath1)!''}"/>
                            </div>
                        </td>

                        <td width="100">
                            <i class="fa fa-fw fa-close" shop_id="${(entity.id)!0}" num="${2}"></i>

                            <div class="portrait" num="2">
                                <a href="javascript:void(0)"><img id="image_2_${pid}" src="<#if entity.imagePath2?? && entity.imagePath2?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.imagePath2}<#else>${app.imagePath}/user.jpg</#if>" /><span>修改图片</span></a>
                                <input  type="hidden" id="image_path_2_${pid}" name="imagePath2" value="${(entity.imagePath2)!''}"/>
                            </div>
                        </td>

                        <#--<td width="100">-->
                            <#--<div class="portrait" num="3">-->
                                <#--<a href="javascript:void(0)"><img id="image_3_${pid}" src="<#if entity.imagePath3?? && entity.imagePath3?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.imagePath3}<#else>${app.imagePath}/user.jpg</#if>" /><span>修改图片</span></a>-->
                                <#--<input  type="hidden" id="image_path_3_${pid}" name="imagePath3" value="${(entity.imagePath3)!''}"/>-->
                            <#--</div>-->
                        <#--</td>-->
                    </tr>
                </table>
            </div>
        </fieldset>
    </form>
</div>

<script>
    function set_image(param) {
        $('#image_' +  param.num  + '_${pid}').attr('src', '${(controller.appConfig.staticUrl)!''}' + param.filePath);
        $('#image_path_' +  param.num  + '_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }

    $('.fa-close').click(function () {
        var shopId = $(this).attr("shop_id");
        var num = $(this).attr("num");
        $.messager.confirm("提示信息", "确认删除图片？", function (ok) {
            if (ok) {
                $.post('${contextPath}/security/hdg/shop/clear_image.htm?id=' + shopId + '&num=' + num, function (json) {
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        if(num==1) {
                            $("#image_path_1_${pid}").val('');
                            document.getElementById("image_1_${pid}").src = "${app.imagePath}/user.jpg";
                        }else if(num==2) {
                            $("#image_path_2_${pid}").val('');
                            document.getElementById("image_2_${pid}").src = "${app.imagePath}/user.jpg";
                        }
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }, 'json');
            }
        })
    });


    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('.portrait').click(function() {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传',
                href: "${contextPath}/security/hdg/shop/image.htm?num=" + $(this).attr('num'),
                event: {
                    onClose: function() {
                    }
                }
            });
        });
        var snapshot = $.toJSON({
            id: $("#id_${pid}").val(),
            priceGroupId: win.find('input[name=priceGroupId]').val(),
            workTime: win.find('input[name=workTime]').val(),
            imagePath1: $("#image_path_1_${pid}").val(),
            imagePath2: $("#image_path_2_${pid}").val()
            <#--imagePath3: $("#image_path_3_${pid}").val()-->

        });

        var ok=function(){
            if(!form.form('validate')){
                return false;
            }
            var success = true;

            var values = {
                id: '${entity.id}',
                priceGroupId: win.find('input[name=priceGroupId]').val(),
                workTime: win.find('input[name=workTime]').val(),
                imagePath1: form[0].imagePath1.value,
                imagePath2: form[0].imagePath2.value,
//                imagePath3: form[0].imagePath3.value
            };

            if(snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/hdg/shop/update_image.htm',
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


