<td colspan="2">
    <div class="zj_list">
    <#if vehicleVipPriceShopList?? && (vehicleVipPriceShopList?size>0) >
        <#list vehicleVipPriceShopList as vehicleVipPrice>
            <div class="zj_item vehicle_vip_price_shop">
                <span class="icon">
                    <i class="fa fa-fw fa-close" vehicle_vip_price_shop_id="${(vehicleVipPrice.id)!''}"  price_id="${(vehicleVipPrice.priceId)!''}"></i>
                </span>
                <p class="text"><#if vehicleVipPrice.shopId??>${(vehicleVipPrice.shopName)!''}</br>${vehicleVipPrice.shopId}<#else></#if></p>
            </div>
        </#list>
    </#if>
        <div class="zj_add vehicle_vip_price_shop_add">
            <p class="icon"><i class="fa fa-fw fa-plus"></i></p>
            <p class="text">添加门店</p>
        </div>
    </div>
</td>

<script>

    $(function() {
        $(".vehicle_vip_price_shop_add").click(function () {
            App.dialog.show({
                css: 'width:680px;height:530px;',
                title: '新建',
                href: "${contextPath}/security/zc/shop_rent_car/vehicle_vip_price_shop_page.htm?agentId=${(agentId)!0}",
                windowData: {
                    ok: function (rows) {
                        var ids = '';
                        if (rows.length > 0) {
                            for (var i = 0; i < rows.length; i++) {
                                ids += rows[i].shopId + ',';
                            }
                            ids = ids.substring(0, ids.lastIndexOf(','));
                            var values = {
                                priceId: ${(priceId)!0},
                                ids: ids
                            };
                            $.ajax({
                                cache: false,
                                async: false,
                                type: 'POST',
                                url: '${contextPath}/security/zc/vehicle_vip_price_shop/create.htm',
                                dataType: 'json',
                                data: values,
                                success: function (json) {
                                <@app.json_jump/>
                                    if (json.success) {
                                        $.messager.alert('提示信息', '操作成功', 'info');
                                    } else {
                                        $.messager.alert('提示信息', json.message, 'info');
                                    }
                                }
                            });
                            return true;
                        } else {
                            $.messager.alert('提示信息', '请选择门店', 'info');
                            return false;
                        }
                    }
                },
                event: {
                    onClose: function() {
                        $.post('${contextPath}/security/zc/vehicle_vip_price_shop/vehicle_vip_price_shop.htm', {
                            priceId:${(priceId)!0},
                            agentId: ${(agentId)!0}
                        }, function (html) {
                            $("#vehicle_vip_price_shop").html(html);
                        }, 'html');
                    }
                }
            });
        });

        $(".vehicle_vip_price_shop .fa-close").click(function () {
            var id = $(this).attr("vehicle_vip_price_shop_id");
            var priceId = $(this).attr("price_id");
            $.messager.confirm('提示信息', '确认删除该门店?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/zc/vehicle_vip_price_shop/delete.htm?id=" + id + "&priceId=" + priceId, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/zc/vehicle_vip_price_shop/vehicle_vip_price_shop.htm', {
                                priceId:${(priceId)!0},
                                agentId: ${(agentId)!0}
                            }, function (html) {
                                $("#vehicle_vip_price_shop").html(html);
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