<td colspan="2">
    <div class="zj_list">
    <#if vipPriceCustomerList?? && (vipPriceCustomerList?size>0) >
        <#list vipPriceCustomerList as vipPriceCustomer>
            <div class="zj_item vip_price">
                <span class="icon">
                    <i class="fa fa-fw fa-close" vip_price_customer_id="${(vipPriceCustomer.id)!''}" price_id="${(vipPriceCustomer.priceId)!''}"></i>
                </span>
                <p class="text"><#if vipPriceCustomer.mobile??>${vipPriceCustomer.mobile}<#else></#if></p>
            </div>
        </#list>
    </#if>
        <div class="zj_add vip_customer_add">
            <p class="icon"><i class="fa fa-fw fa-plus"></i></p>
            <p class="text">添加骑手</p>
        </div>
    </div>
</td>

<script>

    $(function() {
        $(".vip_customer_add").click(function () {
            App.dialog.show({
                css: 'width:300px;height:165px;',
                title: '新建',
                href: "${contextPath}/security/hdg/vip_price_customer/add.htm?priceId="+ ${(priceId)!0} + "&agentId=" +${(agentId)!0},
                windowData: {
                },
                event: {
                    onClose: function() {
                        $.post('${contextPath}/security/hdg/vip_price_customer/vip_price_customer.htm', {
                            priceId: ${(priceId)!0}
                        }, function (html) {
                            $("#vip_price_customer").html(html);
                        }, 'html');
                    }
                }
            });
        });

        $(".vip_price .fa-close").click(function () {
            var id = $(this).attr("vip_price_customer_id");
            var priceId = $(this).attr("price_id");
            $.messager.confirm('提示信息', '确认删除该骑手?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/vip_price_customer/delete.htm?id=" + id + "&priceId=" + priceId, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/hdg/vip_price_customer/vip_price_customer.htm', {
                                priceId: ${(priceId)!0}
                            }, function (html) {
                                $("#vip_price_customer").html(html);
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