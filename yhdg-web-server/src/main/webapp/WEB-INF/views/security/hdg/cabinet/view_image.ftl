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
        <fieldset style="height: 450px">
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="300">
                            <div class="portrait" num="1">
                                <a href="javascript:void(0)"><img id="image_1_${pid}" src="<#if entity.imagePath1?? && entity.imagePath1?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.imagePath1}<#else>${app.imagePath}/user.jpg</#if>" /></a>
                                <input  type="hidden" id="image_path_1_${pid}" name="imagePath1" value="${(entity.imagePath1)!''}"/>
                            </div>
                        </td>
                        <#--<td width="300">-->
                            <#--<div class="portrait" num="2">-->
                                <#--<a href="javascript:void(0)"><img id="image_2_${pid}" src="<#if entity.imagePath2?? && entity.imagePath2?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.imagePath2}<#else>${app.imagePath}/user.jpg</#if>" /></a>-->
                                <#--<input  type="hidden" id="image_path_2_${pid}" name="imagePath2" value="${(entity.imagePath2)!''}"/>-->
                            <#--</div>-->
                        <#--</td>-->
                        <#--<td width="100">-->
                            <#--<div class="portrait" num="3">-->
                                <#--<a href="javascript:void(0)"><img id="image_3_${pid}" src="<#if entity.imagePath3?? && entity.imagePath3?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.imagePath3}<#else>${app.imagePath}/user.jpg</#if>" /></a>-->
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
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        var ok=function(){
            if(!form.form('validate')){
                return false;
            }
            var success = true;

            var values = {
                id: '${entity.id}',
                imagePath1: form[0].imagePath1.value,
                imagePath2: form[0].imagePath2.value,
//                imagePath3: form[0].imagePath3.value
            };

            return success;
        }
        win.data('ok',ok);
    })();
</script>


