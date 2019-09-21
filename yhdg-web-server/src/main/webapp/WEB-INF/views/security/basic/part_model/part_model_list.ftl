<td colspan="2">
    <div class="zj_add part_model_add" align="left"
         style="cursor: pointer;width: 96px;height: 38px;text-align: center;margin: 0 10px 10px 0px;background: #ffffff;border: 1px solid;border-color: rgba(215, 215, 215, 1);">
        <p class="icon"><i class="fa fa-fw fa-plus"></i></p>
        <p class="text" style="margin-top: 5px;">添加角色模板</p>
    </div>
    <div class="zj_list">
        <#if partModelList?? && (partModelList?size>0) >
            <#list partModelList as partModel>
                <div class="zj_item part_model">
                    <span class="icon">
                         <i class="fa fa-fw fa-edit"  part_model_id="${(partModel.id)!''}" part_model_type="${(partModel.partModelType)!''}"></i>
                        <i class="fa fa-fw fa-close" part_model_id="${(partModel.id)!''}" part_model_type="${(partModel.partModelType)!''}"></i>
                    </span>
                    <p class="text"><#if partModel.partModelName??>${partModel.partModelName}<#else></#if></p>
                </div>
            </#list>
        </#if>
    </div>
</td>
<script>

    $(function() {
        $(".part_model").click(function () {
            if($(this).hasClass("active")) {
                $(this).removeClass("active");
            }else{
                $(this).addClass("active").siblings().removeClass("active");
            }

            //查询该角色模板对应的权限
            var partModelId = $(this).find(".fa-close").attr("part_model_id");
            var valuePermTree = $('#value_perm_tree');
            valuePermTree.tree({
                url: "${contextPath}/security/basic/part/tree.htm?id=" + partModelId
            });
            valuePermTree.tree('reload');
        });

        $(".part_model_add").click(function () {
            //获取角色类型
            var partType = $(this).parent().parent().parent().parent().parent().find(".person_part.active").attr("part_type");
            //添加角色模板
            App.dialog.show({
                css: 'width:500px;height:355px;overflow:visible;',
                title: '新建角色模板',
                href: "${contextPath}/security/basic/part_model/add_part_model.htm?partModelType=" + partType,
                windowData: {
                    ok: function (partModel) {
                        //在bas_part_model添加一条数据
                        var values = {};
                        values["partModelType"] = partModel.partModelType;
                        values["partModelName"] = partModel.partModelName;
                        values["permIds"] = partModel.permIds;
                        $.post("${contextPath}/security/basic/part_model/create_part_model.htm", {
                            data : $.toJSON(values)
                        }, function (json) {
                            if (json.success) {
                                $.post('${contextPath}/security/basic/part_model/part_model_list.htm', {
                                    partModelType: partType
                                }, function (html) {
                                    $("#part_model").html(html);
                                }, 'html');
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                }
            });
        });

        $(".part_model .fa-edit").click(function () {
            var id = $(this).attr("part_model_id");
            //获取角色类型
            var partModelType = $(this).attr("part_model_type");
            //修改角色模板
            App.dialog.show({
                css: 'width:500px;height:355px;overflow:visible;',
                title: '修改角色模板',
                href: "${contextPath}/security/basic/part_model/edit_part_model.htm?id=" + id,
                windowData: {
                    ok: function (partModel) {
                        var values = {};
                        values["id"] = partModel.id;
                        values["partModelType"] = partModel.partModelType;
                        values["partModelName"] = partModel.partModelName;
                        values["permIds"] = partModel.permIds;
                        $.post("${contextPath}/security/basic/part_model/update_part_model.htm", {
                            data : $.toJSON(values)
                        }, function (json) {
                            if (json.success) {
                                $.post('${contextPath}/security/basic/part_model/part_model_list.htm', {
                                    partModelType: partModelType
                                }, function (html) {
                                    $("#part_model").html(html);
                                }, 'html');
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                }
            });
        });

        $(".part_model .fa-close").click(function () {
            var id = $(this).attr("part_model_id");
            //获取角色类型
            var partModelType = $(this).attr("part_model_type");
            $.messager.confirm('提示信息', '确认删除该角色模板?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/part_model/delete.htm?id=" + id, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/basic/part_model/part_model_list.htm', {
                                partModelType: partModelType
                            }, function (html) {
                                $("#part_model").html(html);
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