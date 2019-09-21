<div class="popup_body">
    <div class="ui_table">
        <input type="hidden" name="id" value="${(entity.id)!''}">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td>
                    <button class="btn btn_blue" onclick="exchange()">按次收入</button>
                </td>
                <td>
                    <button class="btn btn_blue" onclick="packet()">包时段收入</button>
                </td>
            </tr>
        </table>
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

    function exchange() {
        App.dialog.show({
            css: 'width:1050px;height:510px;overflow:visible;',
            title: '市代按次收入',
            href: "${contextPath}/security/basic/balance_record/view_city_income_exchange.htm?id=${(entity.id)}"
        });
    }

    function packet() {
        App.dialog.show({
            css: 'width:1050px;height:510px;overflow:visible;',
            title: '市代包时段收入',
            href: "${contextPath}/security/basic/balance_record/view_city_income_packet.htm?id=${(entity.id)}"
        });
    }

</script>
