<div class="popup_body">
    <div class="ui_table">
        <form id="form_${pid}">
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">字典分类：</td>
                    <td>
                        <input class="text easyui-combobox" id="category_id" required="true" readonly name="categoryId" style="width:180px; height: 28px;"
                               data-options="
                               valueField:'id',
                               textField:'text',
                               editable: false,
                               url:'${contextPath}/security/basic/dict_item/find_category.htm',
                               onLoadSuccess:function() {
                                $('#category_id').combobox('setValue','${(entity.categoryId)!''}')
                               }" />
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">字典名称：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" required="true" name="itemName" value="${(entity.itemName)!''}" maxlength="40" readonly/>
                    </td>
                </tr>
                <tr>
                    <td align="right">条目值：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="memo" value="${(entity.itemValue)!''}" readonly/></td>

                </tr>
                <tr>
                    <td align="right">排序：</td>
                    <td><input type="text" maxlength="6" readonly class="easyui-numberspinner" style="width:180px; height: 28px;" data-options="min:0,max:100" value="${(entity.orderNum)!''}"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>

<script type="text/javascript">
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');

        win.find('button.close').click(function() {
            win.window('close');
        });

    })();


</script>