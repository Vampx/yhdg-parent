<td colspan="2">
    <div class="zj_list">
    <#if vipPriceAgentCompanyList?? && (vipPriceAgentCompanyList?size>0) >
        <#list vipPriceAgentCompanyList as vipPriceAgentCompany>
            <div class="zj_item vip_price_agent_company">
                <span class="icon">
                    <i class="fa fa-fw fa-close" vip_price_agent_company_id="${(vipPriceAgentCompany.id)!''}"  price_id="${(vipPriceAgentCompany.priceId)!''}"></i>
                </span>
                <p class="text"><#if vipPriceAgentCompany.agentCompanyId??>${(vipPriceAgentCompany.companyName)!''}</br>${vipPriceAgentCompany.agentCompanyId}<#else></#if></p>
            </div>
        </#list>
    </#if>
        <div class="zj_add vip_price_agent_company_add">
            <p class="icon"><i class="fa fa-fw fa-plus"></i></p>
            <p class="text">添加运营公司</p>
        </div>
    </div>
</td>

<script>

    $(function() {
        $(".vip_price_agent_company_add").click(function () {
            App.dialog.show({
                css: 'width:680px;height:530px;',
                title: '新建',
                href: "${contextPath}/security/basic/agent_company/vip_price_agent_company_page.htm?agentId=${(agentId)!0}",
                windowData: {
                    ok: function (rows) {
                        var ids = '';
                        if (rows.length > 0) {
                            for (var i = 0; i < rows.length; i++) {
                                ids += rows[i].agentCompanyId + ',';
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
                                url: '${contextPath}/security/hdg/vip_price_agent_company/create.htm',
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
                            $.messager.alert('提示信息', '请选择', 'info');
                            return false;
                        }
                    }
                },
                event: {
                    onClose: function() {
                        $.post('${contextPath}/security/hdg/vip_price_agent_company/vip_price_agent_company.htm', {
                            priceId:${(priceId)!0},
                            agentId: ${(agentId)!0}
                        }, function (html) {
                            $("#vip_price_agent_company").html(html);
                        }, 'html');
                    }
                }
            });
        });

        $(".vip_price_agent_company .fa-close").click(function () {
            var id = $(this).attr("vip_price_agent_company_id");
            var priceId = $(this).attr("price_id");
            $.messager.confirm('提示信息', '确认删除该运营公司?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/vip_price_agent_company/delete.htm?id=" + id + "&priceId=" + priceId, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/hdg/vip_price_agent_company/vip_price_agent_company.htm', {
                                priceId:${(priceId)!0},
                                agentId: ${(agentId)!0}
                            }, function (html) {
                                $("#vip_price_agent_company").html(html);
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