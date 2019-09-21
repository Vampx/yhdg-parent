<div class="popup_body" style="height: 180px">
    <form method="post" style="height: 180px">
        <input type="hidden" name="imagePath" id="portrait_${pid}">
        <fieldset style="height: 180px">
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0">
                    <tr >
                        <td width="60" height="60" align="right" valign="top" style="padding-top:10px;">点击图片：</td>
                        <td >
                            <div class="portrait">
                                <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.imagePath?? && entity.imagePath?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.imagePath}<#else>${app.imagePath}/user.jpg</#if>" /><span>查看图片</span></a>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" valign="right" style="padding-top: 10px">排序：</td>
                        <td>
                            <input name="orderNum" class="easyui-numberspinner" style="width:180px; height: 30px;" type="text" value="${(entity.orderNum)!''}">
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
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');
        win.find('.portrait').click(function() {
            App.dialog.show({
                options:'maximized:true',
                title: '查看',
                href: "${contextPath}/security/main/preview.htm?path=" + "${entity.imagePath}"
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
