<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td style="padding-top:10px;">实名照片：</td>
                    <td>
                    <img width="180" height="180"
                            src=<#if entity.authFacePath ?? && entity.authFacePath != ''>'${staticUrl}${(entity.authFacePath)!''}'
                         onclick="preview_${pid}('${(entity.authFacePath)!""}')"<#else>
                        '${app.imagePath}/user.jpg'</#if> />
                    </td>
                </tr>
                <#--<tr>-->
                    <#--<td style="padding-top:10px;">身份证反面：</td>-->
                    <#--<td>-->
                    <#--<img width="300" height="180"-->
                            <#--src=<#if entity.idCardRear ?? && entity.idCardRear != ''>'${staticUrl}${(entity.idCardRear)!''}'-->
                         <#--onclick="preview_${pid}('${(entity.idCardRear)!""}')"<#else>-->
                        <#--'${app.imagePath}/user.jpg'</#if> />-->
                    <#--</td>-->
                <#--</tr>-->
            </table>
        </form>
    </div>
</div>
<script>
    function preview_${pid}(path) {
        App.dialog.show({
            options: 'maximized:true',
            title: '查看',
            href: "${controller.appConfig.staticUrl}/security/material/preview.htm?path=" + path
        });
    }
</script>