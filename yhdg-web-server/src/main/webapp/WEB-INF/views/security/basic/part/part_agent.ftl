<td colspan="2">
    <div class="zj_list">
        <#if agentPartList?? && (agentPartList?size>0) >
            <#list agentPartList as part>
                <div class="zj_item part_agent">
                    <span class="icon">
                        <i class="fa fa-fw fa-close" part_id="${(part.id)!''}" part_type="${(part.partType)!0}" mobile="${(part.mobile)!''}"></i>
                    </span>
                    <p class="text">${(part.agentName)!''}</p>
                    <p class="text" style="line-height: 25px;">${(part.partName)!''}</p>
                </div>
            </#list>
        </#if>
    </div>
</td>
<script>

    $(function() {
        $(".part_agent").click(function () {
            $(this).addClass("active").siblings().removeClass("active");
            $(this).parent().parent().parent().parent().find(".person_part").addClass("active").parent().siblings().find(".person_part").removeClass("active");
            $(this).addClass("active").parent().siblings().find(".person_part").removeClass("active");
            $(this).parent().parent().parent().parent().siblings().find(".zj_item").removeClass("active");

            //查询该类型对应的所有角色模板
            var partType = $(this).find(".fa-close").attr("part_type");
            $.post('${contextPath}/security/basic/part_model/part_model_list.htm', {
                partModelType: partType
            }, function (html) {
                $("#part_model").html(html);
            }, 'html');

            //查询该角色对应的所有权限
            $('#part_perm').remove();

            var partId = $(this).find(".fa-close").attr("part_id");
            var valuePermTree = $('#value_perm_tree');
            valuePermTree.tree({
                url: "${contextPath}/security/basic/part/tree.htm?id=" + partId
            });
            valuePermTree.tree('reload');

            //将partId保存在主页面
            $('#value_part_id').val('');
            $('#value_part_id').val(partId);
        });

        $(".part_agent .fa-close").click(function () {
            var id = $(this).attr("part_id");
            var mobile = $(this).attr("mobile");
            $.messager.confirm('提示信息', '确认删除该角色?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/part/delete.htm?id=" + id, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/basic/part/part_agent.htm', {
                                mobile: mobile
                            }, function (html) {
                                $("#part_agent").html(html);
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