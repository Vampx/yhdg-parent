<div class="popup_body">
    <div class="ui_table">
        <div id="qrcode_${pid}" style="margin-left: 130px; margin-top: 20px;"></div>
    </div>
</div>
<div class="popup_btn" style="margin-right: 0px;">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
            win = $('#' + pid);

        new QRCode(document.getElementById("qrcode_${pid}"), "http://www.runoob.com");  // 设置要生成二维码的链接

        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
