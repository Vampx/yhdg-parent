<div class="tab_item" style="display: block;">
    <div class="ui_table">
        <table cellpadding="0" cellspacing="0">
        <#list 0..9 as i>
            <tr>
                <td width="70" align="right">
                        <span class="check_box">
                            <input type="checkbox" class="checkbox active_checkbox" <#if i < list?size><#if list[i].isActive?? && list[i].isActive == 1>checked</#if></#if> /><label>启用</label>
                        </span>
                </td>
                <td align="right">变量名：</td>
                <td><input type="text" class="text property_name_text" style="width:120px;" <#if i < list?size && list[i].propertyName??>value="${list[i].propertyName}"</#if> /> </td>
                <td width="60" align="right">属性值：</td>
                <td><input type="text" class="text property_value_text" style="width:175px;" <#if i < list?size && list[i].propertyValue??>value="${list[i].propertyValue}"</#if> /> </td>
            </tr>
        </#list>
        </table>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var activeCheckbox = win.find('.active_checkbox');
        var propertyNameText = win.find('.property_name_text');
        var propertyNameValue = win.find('.property_value_text');

        var snapshot = $.toJSON({
            id: '${entity.id}',
            active: [<#list list as e><#if e.isActive?? && e.isActive == 1>1<#else>0</#if><#if e_index + 1 lt list?size>,</#if></#list>],
            property: [<#list list as e>'${(e.propertyName)!''}'<#if e_index + 1 lt list?size>,</#if></#list>],
            value: [<#list list as e>'${(e.propertyValue)!''}'<#if e_index + 1 lt list?size>,</#if></#list>]
        });

        var ok = function() {
            var map = {};

            var values = {
                id: '${entity.id}',
                active: [],
                property: [],
                value: []
            };

            for(var i = 0; i < activeCheckbox.length; i++) {
                var property = propertyNameText.eq(i).val();
                var value = propertyNameValue.eq(i).val();
                if(property && value) {
                    values.active.push(activeCheckbox.eq(i).attr('checked') ? 1 : 0);
                    values.property.push(property);
                    values.value.push(value);

                    if(map[property]) {
                        $.messager.alert('提示信息', '属性名 ' + property + ' 重复', 'info');
                        return false;
                    } else {
                        map[property] = true;
                    }
                } else if(!property && value) {
                    $.messager.alert('提示信息', '属性名不能为空', 'info');
                    return false;
                } else if(property && !value) {
                    $.messager.alert('提示信息', '属性值不能为空', 'info');
                    return false;
                }
            }

            var success = true;

            if(snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/yms/terminal/update_property.htm',
                    dataType: 'json',
                    data: values,
                    success: function (json) {
                    <@app.json_jump/>
                        if (json.success) {
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                            success = false;
                        }
                    }
                });
            }


            return success;
        }

        win.data('ok', ok);
    })();
</script>