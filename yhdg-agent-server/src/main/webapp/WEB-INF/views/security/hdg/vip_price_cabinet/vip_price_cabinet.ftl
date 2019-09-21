<td colspan="2">
    <div class="zj_list">
    <#if vipPriceCabinetList?? && (vipPriceCabinetList?size>0) >
        <#list vipPriceCabinetList as vipPriceCabinet>
            <div class="zj_item vip_price_cabinet">
                <span class="icon">
                    <i class="fa fa-fw fa-close" vip_price_cabinet_id="${(vipPriceCabinet.id)!''}"  price_id="${(vipPriceCabinet.priceId)!''}"></i>
                </span>
                <p class="text"><#if vipPriceCabinet.cabinetId??>${(vipPriceCabinet.cabinetName)!''}</br>${vipPriceCabinet.cabinetId}<#else></#if></p>
            </div>
        </#list>
    </#if>
        <div class="zj_add vip_price_cabinet_add">
            <p class="icon"><i class="fa fa-fw fa-plus"></i></p>
            <p class="text">添加柜子</p>
        </div>
    </div>
</td>

<script>

    $(function() {
        $(".vip_price_cabinet_add").click(function () {
            App.dialog.show({
                css: 'width:680px;height:530px;',
                title: '新建',
                href: "${contextPath}/security/hdg/cabinet/vip_price_cabinet_page.htm?agentId=${(agentId)!0}",
                windowData: {
                    ok: function (rows) {
                        var ids = '';
                        if (rows.length > 0) {
                            for (var i = 0; i < rows.length; i++) {
                                ids += rows[i].cabinetId + ',';
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
                                url: '${contextPath}/security/hdg/vip_price_cabinet/create.htm',
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
                            $.messager.alert('提示信息', '请选择柜子', 'info');
                            return false;
                        }
                    }
                },
                event: {
                    onClose: function() {
                        $.post('${contextPath}/security/hdg/vip_price_cabinet/vip_price_cabinet.htm', {
                            priceId:${(priceId)!0},
                            agentId: ${(agentId)!0}
                        }, function (html) {
                            $("#vip_price_cabinet").html(html);
                        }, 'html');
                    }
                }
            });
        });

        $(".vip_price_cabinet .fa-close").click(function () {
            var id = $(this).attr("vip_price_cabinet_id");
            var priceId = $(this).attr("price_id");
            $.messager.confirm('提示信息', '确认删除该柜子?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/vip_price_cabinet/delete.htm?id=" + id + "&priceId=" + priceId, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/hdg/vip_price_cabinet/vip_price_cabinet.htm', {
                                priceId:${(priceId)!0},
                                agentId: ${(agentId)!0}
                            }, function (html) {
                                $("#vip_price_cabinet").html(html);
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