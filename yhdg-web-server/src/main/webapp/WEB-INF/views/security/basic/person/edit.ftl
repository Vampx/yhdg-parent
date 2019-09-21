<style type="text/css">
    .popup_body .person_part {
        height: 30px;
        margin-top: 10px;
        margin-bottom: 10px;
        width: 120px;
        text-align: center;
        border: 1px solid #670f0f;
        cursor: pointer;
    }
    .person_part.active {
        color: #ffffff;
        background: #4263FF;
        border: 1px solid #4263FF;
    }
    .c-line .zj_list .zj_item{
        cursor: pointer;
    }
    .c-line .zj_item.active{
        color: #ffffff;
        background: #38ffdc;
        border: 1px solid #38ffdc;
    }
    .person_part_model .zj_list .zj_item{
        cursor: pointer;
    }
    .person_part_model .zj_item.active{
        color: #ffffff;
        background: #ff2141;
        border: 1px solid #ff2141;
    }


</style>
<div class="popup_body" style="min-height: 85%;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="photoPath" id="portrait_${pid}" value="${(entity.photoPath)!''}">
            <input type="hidden" name="partId" id="value_part_id">
            <div style="width: 100%;">
                <div class="c-d-tips" style="width: 100%;height: auto;display: flex;margin-bottom: 5px;align-items: center;justify-content: space-between;">
                    <span style="display: flex;align-items: center;color: #333333;font-size: 14px;"><i style="width: 4px;height: 17px;background-color: #4263ff;display: block;margin-right: 10px;"></i>基础信息</span>
                </div>
                <table cellpadding="0" cellspacing="0" style="margin-top: 10px;">
                    <tr>
                        <td width="100" align="left">手机号码：</td>
                        <td><input type="text" class="text easyui-validatebox" name="mobile" value="${(entity.mobile)!''}" readonly required="ture" validType="mobile[${entity.id}]" maxlength="11" /></td>
                        <td width="260" align="right">姓名：</td>
                        <td><input type="text" class="text" maxlength="10" name="fullname" value="${(entity.fullname)!''}"/></td>
                    </tr>
                    <tr>
                        <td align="left">密码：</td>
                        <td><input type="password" maxlength="40" class="text easyui-validatebox" name="password"
                                   id="password_${pid}" /></td>
                        <td align="right">启用：</td>
                        <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if>
                                       value="1"/><label for="is_active_1">启用</label>
                            </span>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_0" <#if entity.isActive?? && entity.isActive == 1><#else>checked</#if>
                                       value="0"/><label for="is_active_0">禁用</label>
                            </span>
                        </td>
                    </tr>
                </table>
                <table style="float: left;">
                    <tr>
                        <td align="left">确认密码：</td>
                        <td><input type="password" maxlength="40" class="text easyui-validatebox" name="password2"
                                   validType="equals['#password_${pid}']"/></td>
                    </tr>
                    <tr>
                        <td width="100" align="left">核心管理员：</td>
                        <td>
                                <span class="radio_box">
                                    <input type="radio" class="radio" name="isAdmin" id="is_admin_1" <#if entity.isAdmin?? && entity.isAdmin == 1>checked</#if> value="1"/><label for="is_admin_1">是</label>
                                </span>
                            <span class="radio_box">
                                    <input type="radio" class="radio" name="isAdmin" id="is_admin_0" <#if entity.isAdmin?? && entity.isAdmin == 0>checked</#if> value="0"/><label for="is_admin_0">否</label>
                                </span>
                        </td>
                    </tr>
                    <tr>
                        <td width="100" align="left">是否受保护：</td>
                        <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isProtected" id="is_protected_1" <#if entity.isProtected?? && entity.isProtected == 1>checked</#if>
                                       value="1"/><label for="is_protected_1">是</label>
                            </span>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isProtected" id="is_protected_0" <#if entity.isProtected?? && entity.isProtected == 0>checked</#if>
                                       value="0"/><label for="is_protected_0">否</label>
                            </span>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td width="260" align="right" >头像：</td>
                        <td rowspan="5">
                            <div class="portrait">
                                <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.photoPath?? && entity.photoPath?length gt 0>${(staticUrl)!''}${entity.photoPath}<#else>${app.imagePath}/user.jpg</#if>" /><span>修改头像</span></a>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>

            <#--角色模板-->
            <div class="person_part_model" style="width: 320px;float: right;">
                <div class="c-d-tips" style="margin-top: 10px;width: 320px;">
                    <span style="display: flex;align-items: center;color: #333333;font-size: 14px;"><i style="width: 4px;height: 17px;background-color: #4263ff;display: block;margin-right: 10px;"></i>角色模板</span>
                </div>
                <div id="part_model" style="margin-top: 10px;overflow:scroll;float: right;width: 320px;height: 130px;">
                    <#include '../part_model/part_model_list.ftl'>
                </div>
            </div>

            <div style="float: left;margin-top: 20px;">
                <div class="c-d-tips" style="width: 100%;height: auto;display: flex;margin-bottom: 5px;align-items: center;justify-content: space-between;">
                    <span style="display: flex;align-items: center;color: #333333;font-size: 14px;"><i style="width: 4px;height: 17px;background-color: #4263ff;display: block;margin-right: 10px;"></i>角色类型</span>
                </div>
                <#--角色类型枚举-->
                <div class="c-line" style="overflow:scroll;width: 320px;height: 380px;">
                    <#if partTypeList?? && (partTypeList?size>0) >
                        <#list partTypeList as partType>
                            <div style="border: 1px solid blue;margin-top: 10px;">
                                <div <#if partType_index == 0 >class="person_part active"<#else>class="person_part"</#if>
                                     part_type="${(partType.partType)!0}">
                                    <p style="line-height: 30px;">${(partType.partTypeName)!''}</p>
                                </div>
                                <div class="zj_add part_type_add" align="left"
                                     <#if partType.partType == 1>id="part_platform_add"</#if>
                                     <#if partType.partType == 2>id="part_agent_add"</#if>
                                     <#if partType.partType == 8>id="part_export_add"</#if>
                                     style="cursor: pointer;width: 66px;height: 68px;text-align: center;margin: 0 10px 10px 10px;background: #ffffff;border: 1px solid;border-color: rgba(215, 215, 215, 1);
                                         <#if partType.partType == 1 && entity.platformList?size &gt; 0>display:none;</#if>
                                         <#if partType.partType == 8 && entity.exportList?size &gt; 0>display:none;</#if>
                                             ">
                                    <p class="icon" style="margin-top: 25px;"><i class="fa fa-fw fa-plus"></i></p>
                                    <p class="text" style="margin-top: 5px;">添加角色</p>
                                </div>
                                <#if partType.partType == 1>
                                    <div id="part_platform">
                                        <#include '../part/part_platform.ftl'>
                                    </div>
                                </#if>
                                <#if partType.partType == 2>
                                    <div id="part_agent">
                                        <#include '../part/part_agent.ftl'>
                                    </div>
                                </#if>
                                <#if partType.partType == 8>
                                    <div id="part_export">
                                        <#include '../part/part_export.ftl'>
                                    </div>
                                </#if>
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>
            <div style="float: right;width: 320px;height: 220px;">
            <#--权限操作树-->
                <div class="c-d-tips" style="width: 100%;height: auto;display: flex;margin-top: 5px;margin-bottom: 5px;align-items: center;justify-content: space-between;">
                    <span style="display: flex;align-items: center;color: #333333;font-size: 14px;"><i style="width: 4px;height: 17px;background-color: #4263ff;display: block;margin-right: 10px;"></i>角色权限</span>
                </div>

                <div id="part_perm_base"></div>

                <#--<div id="agent_part_perm">-->
                    <#--<span>后台权限分配：</span>-->
                    <#--<div style="width:308px; height:100px; padding:5px; border:1px solid #ddd; overflow:auto;">-->
                        <#--<ul id="agent_web_tree" class="easyui-tree" url="${contextPath}/security/basic/agent_part/agent_web_tree.htm" checkbox="true" cascadeCheck="true" lines="true"></ul>-->
                    <#--</div>-->
                    <#--<span>手机端权限分配：</span>-->
                    <#--<div style="width:308px; height:100px; padding:5px; border:1px solid #ddd; overflow:auto;">-->
                        <#--<ul id="agent_app_tree" class="easyui-tree" url="${contextPath}/security/basic/agent_part/agent_app_tree.htm" checkbox="true" cascadeCheck="true" lines="true"></ul>-->
                    <#--</div>-->
                <#--</div>-->

            </div>
        </form>
    </div>
</div>
<div style="position: absolute;right: 30px;top: 40px;">
    <button class="btn btn_blue" id="ok_update_${pid}" >确定</button>
    <button class="btn btn_blue" id="ok_close_${pid}" >关闭</button>
</div>
<script>
    function set_portrait(param) {
        $('#image_${pid}').attr('src', '${(controller.appConfig.staticUrl)!''}' + param.filePath);
        $('#portrait_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        var html = '<div id="part_perm">\n' +
                '                    <span>后台权限分配：</span>\n' +
                '                    <div style="width:308px; height:225px; padding:5px; border:1px solid #ddd; overflow:auto;">\n' +
                '                        <ul id="value_perm_tree" class="easyui-tree" url="${contextPath}/security/basic/part/tree.htm" checkbox="true" cascadeCheck="true" lines="true"></ul>\n' +
                '                    </div>\n' +
                '                </div>';

        $('#part_perm_base').before(html);

        //获取选中的角色类型
        var partType = win.find(".person_part.active").attr("part_type");
        //利用角色类型查找角色模板和角色权限
        $.post('${contextPath}/security/basic/part_model/part_model_list.htm', {
            partModelType: partType
        }, function (html) {
            $("#part_model").html(html);
        }, 'html');

        //随点击切换角色类型
        $('.person_part').click(function () {
            $(this).addClass("active").parent().siblings().find(".person_part").removeClass("active");
            //获取角色类型value
            var partType = $(this).attr("part_type");
        //利用角色类型查找角色模板和角色权限
            $.post('${contextPath}/security/basic/part_model/part_model_list.htm', {
                partModelType: partType
            }, function (html) {
                $("#part_model").html(html);
            }, 'html');
        });

        //获取所有平台用户角色
        $.post('${contextPath}/security/basic/part/part_platform.htm', {
            mobile: '${(entity.mobile)!''}'
        }, function (html) {
            $("#part_platform").html(html);
        }, 'html');

        //获取所有运营商用户角色
        $.post('${contextPath}/security/basic/part/part_agent.htm', {
            mobile: '${(entity.mobile)!''}'
        }, function (html) {
            $("#part_agent").html(html);
        }, 'html');

        //获取所有发货员角色
        $.post('${contextPath}/security/basic/part/part_export.htm', {
            mobile: '${(entity.mobile)!''}'
        }, function (html) {
            $("#part_export").html(html);
        }, 'html');

        $(".part_type_add").click(function () {
            $(this).parent().find(".person_part").addClass("active").parent().siblings().find('.person_part').removeClass("active");
            //获取角色类型value
            var partType = $(this).parent().find(".person_part").attr("part_type");
            //利用角色类型查找角色模板和角色权限
            $.post('${contextPath}/security/basic/part_model/part_model_list.htm', {
                partModelType: partType
            }, function (html) {
                $("#part_model").html(html);
            }, 'html');
            //如果选中的是平台用户
            if(partType == 1) {
                App.dialog.show({
                    css: 'width:350px;height:155px;overflow:visible;',
                    title: '新建平台用户角色',
                    href: "${contextPath}/security/basic/part/add_part_platform.htm",
                    windowData: {
                        ok: function (partName) {
                            //在bas_part添加一条数据
                            $.post("${contextPath}/security/basic/part/create_part_platform.htm?partType=" + partType +"&mobile=" + '${(entity.mobile)!''}' +"&partName=" + partName, function (json) {
                                if (json.success) {
                                    $.post('${contextPath}/security/basic/part/part_platform.htm', {
                                        mobile: '${(entity.mobile)!''}'
                                    }, function (html) {
                                        $("#part_platform").html(html);
                                    }, 'html');
                                    //新增按钮隐藏
                                    document.getElementById("part_platform_add").style.display = "none";
                                } else {
                                    $.messager.alert('提示消息', json.message, 'info');
                                }
                            }, 'json');
                        }
                    }
                });
            }
            //如果选中的是运营商用户
            if(partType == 2) {
                App.dialog.show({
                    css: 'width:350px;height:190px;overflow:visible;',
                    title: '新建运营商用户角色',
                    href: "${contextPath}/security/basic/part/add_part_agent.htm",
                    windowData: {
                        ok: function (partName, agentId) {
                            //在bas_part添加一条数据
                            $.post("${contextPath}/security/basic/part/create_part_agent.htm?partType=" + partType +"&mobile=" + '${(entity.mobile)!''}' +"&partName=" + partName + "&agentId=" + agentId, function (json) {
                                if (json.success) {
                                    $.post('${contextPath}/security/basic/part/part_agent.htm', {
                                        mobile: '${(entity.mobile)!''}'
                                    }, function (html) {
                                        $("#part_agent").html(html);
                                    }, 'html');
                                } else {
                                    $.messager.alert('提示消息', json.message, 'info');
                                }
                            }, 'json');
                        }
                    }
                });
            }
            //如果选中的是发货员，只能加一个角色
            if(partType == 8) {
                App.dialog.show({
                    css: 'width:350px;height:155px;overflow:visible;',
                    title: '新建发货员角色',
                    href: "${contextPath}/security/basic/part/add_part_export.htm",
                    windowData: {
                        ok: function (partName) {
                            //在bas_part添加一条数据
                            $.post("${contextPath}/security/basic/part/create_part_export.htm?partType=" + partType +"&mobile=" + '${(entity.mobile)!''}' +"&partName=" + partName, function (json) {
                                if (json.success) {
                                    $.post('${contextPath}/security/basic/part/part_export.htm', {
                                        mobile: '${(entity.mobile)!''}'
                                    }, function (html) {
                                        $("#part_export").html(html);
                                    }, 'html');
                                    //新增按钮隐藏
                                    document.getElementById("part_export_add").style.display = "none";
                                } else {
                                    $.messager.alert('提示消息', json.message, 'info');
                                }
                            }, 'json');
                        }
                    }
                });
            }
        });


        $('#ok_update_${pid}').click(function() {
            var password = $('#password_${pid}').val();
            var password2 = $('#password2_${pid}').val();
            if (password != "" && password2 == "") {
                $.messager.alert('提示信息', '请确认密码', 'info');
                return false;
            }

            var partId = $('#value_part_id').val();
            if( partId==null || partId == '') {
                $.messager.alert('提示信息', '请选中一个角色', 'info');
                return false;
            }

            form.form('submit', {
                url: '${contextPath}/security/basic/person/update.htm',
                onSubmit: function (param) {
                    if (!form.form('validate')) {
                        return false;
                    }
                    var tree = $('#value_perm_tree');
                    var permIds = [];
                    var nodes = tree.tree('getChecked');
                    for (var i = 0; i < nodes.length; i++) {
                        var node = nodes[i];
                        if (node.attributes && node.attributes.id) {
                            permIds.push(node.attributes.id);
                        }
                    }
                    param.permIds = permIds;
                    return true;
                },
                success: function (text) {
                    var json = $.evalJSON(text);
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        $('#ok_close_${pid}').click(function() {
            win.window('close');
        });

        win.find('.portrait').click(function() {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传头像',
                href: "${contextPath}/security/basic/person/photo_path.htm",
                event: {
                    onClose: function() {
                    }
                }
            });
        });

    })()
</script>