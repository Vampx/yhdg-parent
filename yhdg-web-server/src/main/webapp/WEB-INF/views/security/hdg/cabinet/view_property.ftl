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

        var ok = function() {
            var map = {};

            var values = {
                id: '${entity.id}',
                active: [],
                property: [],
                value: []
            };

            var success = true;

            return success;
        }

        win.data('ok', ok);
    })();
</script>