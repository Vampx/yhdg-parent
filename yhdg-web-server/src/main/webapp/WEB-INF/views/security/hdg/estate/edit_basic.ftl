<script>
    $.extend($.fn.validatebox.defaults.rules, {
        mobile: {
            validator: function (value) {
                if (value != "") {
                    return /^1\d{10}$/.test(value);
                }

            },
            message: '请正确输入手机号码'
        }
    });
</script>
<div class="popup_body" style="padding-left:50px;font-size: 14px;min-height: 72%;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="left">*运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                               editable="false" style="width: 255px; height: 28px;" value="${(entity.agentId)!''}"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                    }
                                "
                        >
                    </td>
                </tr>

                <tr>
                    <td align="left">*物业名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="estateName"
                               style="width: 242px; height: 28px;"
                               maxlength="30" value="${(entity.estateName)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">*物业地址：</td>
                    <td colspan="2"><input type="text" class="text easyui-validatebox" maxlength="50" name="address"
                                           style="width: 275px;" value="${(entity.address)!''}"/>&nbsp;
                    </td>
                </tr>
                <tr>
                    <td align="left">*物业联系人：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="11"
                               style="width: 275px; height: 28px;"
                               name="linkname" value="${(entity.linkname)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">*物业联系电话：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="11" name="tel"
                               style="width: 275px; height: 28px;"
                               validType="mobile[]"
                               value="${(entity.tel)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">*营业时间：</td>
                    <td><input type="text" class="text easyui-timespinner" value="${(beginTime)!''}" maxlength="5"
                               data-options="showSeconds:false" style="width:103px; height:28px;" name="beginTime"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="text" class="text easyui-timespinner"
                                                                    value="${(endTime)!''}" maxlength="5"
                                                                    data-options="showSeconds:false,min:'00:01',max:'23:59' "
                                                                    style="width:103px; height:28px;" name="endTime"/>
                    </td>
                </tr>
                <tr>
                    <td align="left">*物业余额（元）：</td>
                    <td><input type="text" class="easyui-numberspinner" id="estate_balance_${pid}" name="balance"
                               disabled maxlength="5" value="${(entity.balance/100)!''}"
                               style="width:173px;height: 28px;"
                               data-options="min:0.00,precision:2"><#if entity.id ??>&nbsp;&nbsp;<a
                            href="javascript:estateInOutMoney()">物业流水</a></#if></td>
                </tr>
                <tr>
                    <td align="left">*认证类型：</td>
                    <td>
                        <select name="authType" class="easyui-combobox" editable="false"
                                style="width: 173px; height: 28px;">
                        <#list AuthTypeEnum as e>
                            <option value="${e.getValue()}">${e.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="left">*是否启用：</td>
                    <td>
                        <select name="isActive" id="is_active_${pid}" class="easyui-combobox"
                                editable="false"
                                style="width: 173px; height: 28px;">
                        <#list activeStatusEnum as e>
                            <option value="${e.getValue()}"
                                    <#if entity.isActive?? && entity.isActive == e.getValue()>selected</#if>>${e.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr style="height: 30px;"></tr>
                <tr>
                    <td align="left">&nbsp;&nbsp;物业柜子：</td>
                    <td>
                        <div style="width: 350px;">
                            <div class="zj_add cabinet_add" align="left"
                                 style="cursor: pointer;width: 80px;height: 100px;float: left;text-align: center;margin: 0 10px 10px 0;background: #f5f5f5;">
                                <p class="icon" style="margin-top: 25px;"><i class="fa fa-fw fa-plus"></i></p>
                                <p class="text" style="margin-top: 5px;">添加柜子</p>
                            </div>
                            <div align="right">
                            <#if entity.cabinetList ??>
                                <#list entity.cabinetList as cabinet>
                                    <span>${cabinet.cabinetName}【${cabinet.id}】</span>
                                    <a href="javascript:remove_cabinet(${cabinet.id})"><i class="fa fa-fw fa-close"></i></a><br><br>
                                </#list>
                            </#if>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<#--<div class="popup_btn" style="height: 10%">-->
    <#--<button class="btn btn_red shop_ok">确定</button>-->
    <#--<button class="btn btn_border shop_close">关闭</button>-->
<#--</div>-->
<script>



    function remove_cabinet(cabinetId) {
        $.messager.confirm('提示信息', '确认删除?', function (ok) {
            if (ok) {
                $.post("${contextPath}/security/hdg/cabinet/unbind_estate.htm?cabinetId=" + cabinetId, function (json) {
                    if (json.success) {
                        var win = $('#${pid}');
                        win.window('close');
                        App.dialog.show({
                            css: 'width:1050px;height:650px;overflow:visible;',
                            title: '修改',
                            href: "${contextPath}/security/hdg/estate/edit.htm?id=${entity.id}",
                            event: {
                                onClose: function() {
                                    query();
                                },
                                onLoad: function() {
                                }
                            }
                        });
                    } else {
                        $.messager.alert('提示消息', json.message, 'info');
                    }
                }, 'json');
            }
        });
    }

    $(".cabinet_add").click(function () {
        App.dialog.show({
            css: 'width:680px;height:530px;',
            title: '新建',
            href: "${contextPath}/security/hdg/cabinet/unbind_estate_cabinet_page.htm?agentId=${(entity.agentId)!''}&estateId=${(entity.id)!''}",
            windowData: {
                ok: function(rows) {
                    if(rows.length > 0) {
                        var cabinetIdList = [];
                        for(var i = 0; i < rows.length; i++) {
                            cabinetIdList.push(rows[i].cabinetId);
                        }
                        $.post('${contextPath}/security/hdg/cabinet/batch_bind_estate.htm?estateId=${(entity.id)!''}',{
                            cabinetIdList: cabinetIdList
                        }, function(json) {
                            if(json.success) {
                                var win = $('#${pid}');
                                win.window('close');
                                App.dialog.show({
                                    css: 'width:1050px;height:650px;overflow:visible;',
                                    title: '修改',
                                    href: "${contextPath}/security/hdg/estate/edit.htm?id=${entity.id}",
                                    event: {
                                        onClose: function () {
                                            query();
                                        },
                                        onLoad: function () {
                                        }
                                    }
                                });
                            }else{
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                        return true;
                    }else {
                        $.messager.alert('提示信息', '请选择换电站', 'info');
                        return false;
                    }
                }
            }
        });
    });

    function estateInOutMoney() {
        App.dialog.show({
            css: 'width:1000px;height:518px;overflow:visible;',
            title: '物业流水',
            href: "${contextPath}/security/basic/estate_in_out_money/alert_page.htm?estateId=${(entity.id)!''}",
            event: {
                onClose: function () {
                }
            }
        });
    }

    (function () {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

//        $(".shop_close").click(function () {
//            win.window('close');
//        });

//        $(".shop_ok").click(function () {
            var snapshot = $.toJSON({
                id: '${entity.id}',
                agentId: '${entity.agentId}',
                authType: '${entity.authType}',
                estateName: '${(entity.estateName)!''}',
                linkname: '${(entity.linkname)!''}',
                tel: '${(entity.tel)!''}',
                workTime: '${(entity.workTime)!''}',
                isActive: '${(entity.isActive)!''}'
            });

            var ok = function () {
                if (!jform.form('validate')) {
                    return false;
                }
                var beginTime = win.find('input[name=beginTime]').val();
                var endTime = win.find('input[name=endTime]').val();
                var beginTimeNumber = win.find('input[name=beginTime]').timespinner('getValue');
                var endTimeNumber = win.find('input[name=endTime]').timespinner('getValue');
                if (beginTimeNumber > endTimeNumber) {
                    var time = beginTime;
                    beginTime = endTime;
                    endTime = time;
                }
                var workTime = beginTime + "-" + endTime;
                var success = true;
                var values = {
                    id: '${entity.id}',
                    agentId: $('#agent_id_${pid}').combotree('getValue'),
                    estateName: form.estateName.value,
                    linkname: form.linkname.value,
                    tel: form.tel.value,
                    workTime: workTime,
                    isActive: form.isActive.value,
                    authType: form.authType.value
                };
                if (snapshot != $.toJSON(values)) {
                    var reg = /^(20|21|22|23|[0-1]\d):[0-5]\d-(20|21|22|23|[0-1]\d):[0-5]\d$/;
                    var regExp = new RegExp(reg);
                    //只要不是都为空，就要进行格式检验
                    if ((beginTime == null || beginTime == '') && (beginTime == null || endTime == '')) {

                    } else {
                        if (!regExp.test(workTime)) {
                            $.messager.alert("提示信息", "时间格式不正确，正确格式为：08:00-17:00", "info");
                            return;
                        }
                    }
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/hdg/estate/update_basic.htm',
                        dataType: 'json',
                        data: values,
                        success: function (json) {
                        <@app.json_jump/>
                            if (json.success) {
//                                $.messager.alert('提示信息', '操作成功', 'info');
//                                win.window('close');
                            } else {
                                $.messager.alert('提示信息', json.message, 'info');
                            }
                        }
                    });
                }else {
//                    win.window('close');
                }
                return success;
            }
        win.data('ok', ok);
//        })
    })();
</script>