<div class="popup_body">
    <div class="ui_table">
        <iframe name="upload_iframe" style="display: none;"></iframe>
        <form method="post" id="form_${pid}" action="${contextPath}/security/basic/weixinma/photo_path.htm?pid=${pid}" method="post" target="upload_iframe" enctype="multipart/form-data">
            <input type="hidden" name="typs" value="${(typs)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">选择图片：</td>
                    <td><input type="file" class="filler" name="file" id="file_${pid}"></td>
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
        var pid = '${pid}', win = $('#' + pid), form = win.find('form'), windowData = win.data('windowData');
        function getFileSuffix(fileName) {
            var num = fileName.lastIndexOf('.');
            var suffix = '';
            if(num > -1) {
                suffix = fileName.toLowerCase().substring(num + 1, fileName.length);
            }
            return suffix.toLowerCase();
        }

        function uploadAttachment(val) {
            if(val == '') {
                alert('请先选择文件');
                return false;
            }
            var suffix = getFileSuffix(val);
            if(suffix == 'jpg' || suffix == 'png' || suffix == 'bmp' || suffix == 'ico' || suffix=='gif') {
                return true;
            } else {
                alert('上传文件必须是图片格式');
                return false;
            }
        }

        win.find('button.ok').click(function() {
            var val = $('#file_${pid}').val();
            if(uploadAttachment(val)) {
                document.forms["form_${pid}"].submit();
            }
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>