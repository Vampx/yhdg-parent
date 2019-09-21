<div class="popup_body clearfix">
    <div class="ui_table">
    <form method="post">
        <input type="hidden" name="version" value="${(entity.version)!''}" >
        <input type="hidden" name="materialType" value="${(entity.materialType)!''}" >
        <input type="hidden" name="id" value="${(entity.id)!''}">

    <#if  entity ?? &&( entity.materialType == 1)>
        <fieldset>
            <legend>播放时长</legend>
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td align="right" width="80">时长：</td>
                        <td>
                            <input  class="easyui-timespinner" name="duration" id="duration_${pid}"  style="width: 184px; height: 28px;"  required="required" data-options="showSeconds:true" value="">
                        </td>
                        <td align="right" width="80"></td>
                        <td></td>
                    </tr>
                </table>
            </div>
        </fieldset>
    </form>
    <#else >
        <input type="hidden" name="duration" value="${(entity.duration)!''}" >
        </form>
    </#if>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                jform = win.find('form'),
                form = jform[0];
        $("#duration_${pid}").val(App.formatSecond(${entity.duration}));

        var group = $('#group_id_${pid}');
        var snapshot = $.toJSON({
            id: '${entity.id}',
            version: '${(entity.version)!''}',
            materialType: '${(entity.materialType)!''}',
            duration: '${(entity.duration)!''}'

        });

        win.find('button.ok').click(function() {
            if(!jform.form('validate')) {
                return;
            }
            if(${entity.materialType} == 1) {
                var duration = $('#duration_${pid}').timespinner("getHours") * 3600
                        + $('#duration_${pid}').timespinner("getMinutes") * 60
                        + $('#duration_${pid}').timespinner("getSeconds");
            } else{
                var duration = ${entity.duration};
            }
            var values = {
                id: '${entity.id}',
                version: '${(entity.version)!''}',
                materialType: '${(entity.materialType)!''}',
                duration: duration
            };

            if(snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/yms/material/update.htm',
                    dataType: 'json',
                    data: values,
                    success: function (json) {
                    <@app.json_jump/>
                        if (json.success) {
                            $.messager.alert('提示信息', '操作成功', 'info');
                            win.window('close');
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                });
            }
        });

        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>