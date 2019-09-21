<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" id="id_${pid}" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td style="font-weight: 600;height: 32px;line-height: 32px;font-size: 14px;">参数信息</td>
                </tr>
                <tr>
                    <td width="70" align="right">*参数：</td>
                    <td>
                        <select name="parameter" id="parameter_${pid}" style="width: 183px; height: 28px;">
                        <#list TBIT_Parameter as s>
                            <option value="${s.getValue()}" ><#if s.getEnableWrite()== 1>可写-</#if>  ${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                    <td  width="120" align="right">
                        <div class="ok_btn"
                             style="	width: 100px;height: 33px;line-height: 33px;text-align: center;color: #FFFFFF;font-size: 14px;margin-top: 10px;background-color: #556bd8;border-radius: 6px;"
                             id="ok_btn" onclick="query()">查询
                        </div>

                    </td>
                    <td width="150" align="right">
                        <span id="show_value"></span>
                    </td>
                </tr>

                <tr>
                    <td width="70" align="right">写入值：</td>
                    <td><input id="write_value" type="text" class="text easyui-validatebox"  name="write_value" style="height: 28px;width: 180px;"  maxlength="100"></td>
                    <td width="120" align="right">
                        <div class="ok_btn"
                             style="width: 100px;height: 33px;line-height: 33px;text-align: center;color: #FFFFFF;font-size: 14px;margin-top: 10px;background-color: red;border-radius: 6px;"
                             id="ok_btn" onclick="set()">写入
                        </div>
                    </td>
                    <td width="150" align="right">
                        <span id="set_value"></span>
                    </td>
                </tr>
            </table>

        </form>
    </div>
</div>

<script>
    function query() {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        if (!jform.form('validate')) {
            return false;
        }

        var values = {
            id: '${entity.id}',
            parameter: form.parameter.value
        };

        $("#show_value").html("");

        $.ajax({
            cache: false,
            async: false,
            type: 'POST',
            url: '${contextPath}/security/zc/vehicle/query_parameter.htm',
            dataType: 'json',
            data: values,
            success: function (json) {
            <@app.json_jump/>
                if (json.code == 0) {
                    $("#show_value").html(json.data);
                } else {
                    $("#show_value").html(json.message);
                }
            }
        });
    }

    function set() {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];



        if (!jform.form('validate')) {
            return false;
        }

        var values = {
            id: '${entity.id}',
            parameter: form.parameter.value,
            writeValue: form.write_value.value
        };

        $("#set_value").html("");

        $.ajax({
            cache: false,
            async: false,
            type: 'POST',
            url: '${contextPath}/security/zc/vehicle/set_parameter.htm',
            dataType: 'json',
            data: values,
            success: function (json) {
            <@app.json_jump/>
                $("#set_value").html(json.message);
            }
        });
    }

</script>