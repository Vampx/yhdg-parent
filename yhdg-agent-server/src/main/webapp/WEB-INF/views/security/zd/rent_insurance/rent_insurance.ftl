<td colspan="2">
    <div class="zj_list">
    <#if rentInsuranceList?? && (rentInsuranceList?size>0) >
        <#list rentInsuranceList as insurance>
            <div class="zj_item insurance_rent">
                <span class="icon">
                    <i class="fa fa-fw fa-edit"  insurance_rent_id="${(insurance.id)!0}"></i>
                    <i class="fa fa-fw fa-close" insurance_rent_id="${(insurance.id)!0}"></i>
                </span>
                <p class="num">价格:<#if insurance.price??>${(insurance.price)/100}<#else>0</#if> 元</p>
                <p class="num2">保额:<#if insurance.paid??>${(insurance.paid)/100}<#else>0</#if> 元</p>
                <p class="text">时长:${(insurance.monthCount)!0} 月</p>
            </div>
        </#list>
    </#if>
        <div class="zj_add insurance_rent_add">
            <p class="icon"><i class="fa fa-fw fa-plus"></i></p>
            <p class="text">添加保险</p>
        </div>
    </div>
</td>

<script>
    $(function() {

        $(".insurance_rent_add").click(function () {
            App.dialog.show({
                css: 'width:500px;height:500px;overflow:visible;',
                title: '添加保险',
                href: "${contextPath}/security/zd/rent_insurance/rent_insurance_add.htm?batteryType="+ ${(entity.batteryType)!''}+ "&agentId="+ ${(entity.agentId)!''},
                windowData:{
                },
                event: {
                }
            });
        });

        $(".insurance_rent .fa-edit").click(function () {
            var id = $(this).attr("insurance_rent_id");
            App.dialog.show({
                css: 'width:500px;height:500px;overflow:visible;',
                title: '修改保险',
                href: "${contextPath}/security/zd/rent_insurance/rent_insurance_edit.htm?id=" + id,
                windowData:{
                },
                event: {
                }
            });
        });

        $(".insurance_rent .fa-close").click(function () {
            var id = $(this).attr("insurance_rent_id");
            $.messager.confirm('提示信息', '删除保险设置将删除对应的分期设置，确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/zd/rent_insurance/delete.htm?id=" + id, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/zd/rent_insurance/rent_insurance.htm', {
                                batteryType: ${(entity.batteryType)!''},
                                agentId: ${(entity.agentId)!''}
                            }, function (html) {
                                $("#exchange_insurance").html(html);
                            }, 'html');
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        });
    });
</script>