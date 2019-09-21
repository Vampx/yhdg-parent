<div class="popup_body">
    <div class="ui_table">
        <iframe name="upload_iframe" style="display: none;"></iframe>
        <form method="post" id="form_${pid}" action="${contextPath}/security/zc/shop_rent_car/image.htm?pid=${pid}&num=${num}" target="upload_iframe" enctype="multipart/form-data">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">选择图片：</td>
                    <td><input id="file_${pid}" type="file" class="filler" name="file"></td>
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

        function verifyAttachment(val) {
            if(val == '') {
                alert('请先选择文件');
                return false;
            }
            var suffix = getFileSuffix(val);
            if(suffix == 'jpg' || suffix == 'png' || suffix == 'bmp' || suffix == 'ico') {
                return true;
            } else {
                alert('上传文件必须是图片格式');
                return false;
            }
        }

        win.find('button.ok').click(function() {
            var val = $('#file_${pid}').val();
            if(verifyAttachment(val)) {
                $("#form_${pid}").submit();
            }
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>