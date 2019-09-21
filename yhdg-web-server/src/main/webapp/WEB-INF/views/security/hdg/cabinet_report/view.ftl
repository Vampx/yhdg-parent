<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <div class="popup_body">
                <div class="report_table">
                    <table>
                        <tbody>
                        <tr>
                            <td width="15%">柜子ID:</td>
                            <td width="95%"><textarea style="width: 99%;height: 14px;">${(entity.cabinetId)!''}</textarea></td>
                        </tr>
                        <tr>
                            <td width="15%">请求体字节流:</td>
                            <td width="95%"><textarea style="width: 99%;height: 100px;">${(entity.requestBodyHex)!''}</textarea></td>
                        </tr>
                        <tr>
                            <td width="15%">请求体对象:</td>
                            <td width="95%"><textarea style="width: 99%;height: 150px;">${(entity.requestBodyObj)!''}</textarea></td>
                        </tr>
                        <tr>
                            <td width="15%">响应体对象:</td>
                            <td width="95%"><textarea style="width: 99%;height: 100px;">${(entity.responseBodyObj)!''}</textarea></td>
                        </tr>
                        <tr>
                            <td width="15%">上报时间:</td>
                            <td width="95%"><textarea style="width: 99%;height: 14px;">${((entity.createTime)?string('yyyy-MM-dd HH:mm:ss'))!}</textarea></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>