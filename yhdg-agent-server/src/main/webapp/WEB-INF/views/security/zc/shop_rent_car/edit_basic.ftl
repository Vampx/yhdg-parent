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
            <input type="hidden" name="agentId" value="${Session['SESSION_KEY_USER'].agentId}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="left">*门店名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="shopName"
                               style="width: 242px; height: 28px;"
                               maxlength="30" value="${(entity.shopName)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">*门店地址：</td>
                    <td colspan="2"><input type="text" class="text easyui-validatebox" maxlength="50"
                                           style="width: 275px;" value="${(entity.address)!''}"/>&nbsp;
                    </td>
                    <td>
                        <a href="javascript:getLocation()">
                            <div style="width: 50px;height: 33px;line-height: 33px;text-align: center;color: #FFFFFF;font-size: 14px;margin-left: 20px;background-color: #556bd8;border-radius: 6px;">
                                定位
                            </div>
                        </a>
                    </td>
                </tr>
                <tr>
                    <td align="left">*门店联系人：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="11"
                               style="width: 275px; height: 28px;"
                               name="linkname" value="${(entity.linkname)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">*门店联系电话：</td>
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
                    <td align="left">*门店余额（元）：</td>
                    <td><input type="text" class="easyui-numberspinner" id="shop_balance_${pid}" name="shopBalance"
                               disabled maxlength="5" value="${(entity.balance/100)!''}"
                               style="width:173px;height: 28px;"
                               data-options="min:0.00,precision:2"><#if entity.id ??>&nbsp;&nbsp;<a
                            href="javascript:showInOutMoney()">门店流水</a></#if></td>
                </tr>
                <tr>
                    <td align="left">*是否启用：</td>
                    <td>
                        <select name="activeStatus" id="active_status_${pid}" class="easyui-combobox"
                                editable="false"
                                style="width: 173px; height: 28px;">
                        <#list activeStatusEnum as e>
                            <option value="${e.getValue()}"
                                    <#if entity.activeStatus?? && entity.activeStatus == e.getValue()>selected</#if>>${e.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="left">*设备控制：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isAllowOpenBox" id="is_allow_open_box_1"
                                   <#if entity.isAllowOpenBox?? && entity.isAllowOpenBox == 1>checked</#if> value="1"/><label for="is_allow_open_box_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isAllowOpenBox" id="is_allow_open_box_0"
                                   <#if entity.isAllowOpenBox?? && entity.isAllowOpenBox == 0>checked</#if> value="0" /><label for="is_allow_open_box_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="left">*门店类型：</td>
                    <td>
                        <span>
                            <input type="checkbox" checked disabled
                                   class="checkbox exchange_checkbox"/><label>租车</label>
                        </span>
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

    function getLocation() {
        var win = $('#${pid}');
        App.dialog.show({
            css: 'width:610px;height:525px;overflow:visible;',
            title: '地图定位',
            href: "${contextPath}/security/zc/shop_rent_car/edit_location.htm?id=${(entity.id)!''}",
            event: {
                onClose: function () {
                    var win = $('#${pid}');
                    win.window('close');
                    App.dialog.show({
                        css: 'width:1050px;height:650px;overflow:visible;',
                        title: '修改',
                        href: "${contextPath}/security/zc/shop_rent_car/edit.htm?id=${entity.id}",
                        event: {
                            onClose: function () {
                                query();
                            },
                            onLoad: function () {
                            }
                        }
                    });
                }
            }
        });
    }

    function remove_cabinet(cabinetId) {
        $.messager.confirm('提示信息', '确认删除?', function (ok) {
            if (ok) {
                $.post("${contextPath}/security/hdg/cabinet/unbind_shop.htm?cabinetId=" + cabinetId, function (json) {
                    if (json.success) {
                        var win = $('#${pid}');
                        win.window('close');
                        App.dialog.show({
                            css: 'width:1050px;height:650px;overflow:visible;',
                            title: '修改',
                            href: "${contextPath}/security/zc/shop_rent_car/edit.htm?id=${entity.id}",
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
            href: "${contextPath}/security/hdg/cabinet/unbind_shop_cabinet_page.htm?agentId=${(entity.agentId)!''}&shopId=${(entity.id)!''}",
            windowData: {
                ok: function(rows) {
                    if(rows.length > 0) {
                        var cabinetIdList = [];
                        for(var i = 0; i < rows.length; i++) {
                            cabinetIdList.push(rows[i].cabinetId);
                        }
                        $.post('${contextPath}/security/hdg/cabinet/batch_bind_shop.htm?shopId=${(entity.id)!''}',{
                            cabinetIdList: cabinetIdList
                        }, function(json) {
                            if(json.success) {
                                var win = $('#${pid}');
                                win.window('close');
                                App.dialog.show({
                                    css: 'width:1050px;height:650px;overflow:visible;',
                                    title: '修改',
                                    href: "${contextPath}/security/zc/shop_rent_car/edit.htm?id=${entity.id}",
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

    function showInOutMoney() {
        App.dialog.show({
            css: 'width:1000px;height:518px;overflow:visible;',
            title: '门店流水',
            href: "${contextPath}/security/basic/shop_in_out_money/alert_page.htm?shopId=${(entity.id)!''}",
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

            var snapshot = $.toJSON({
                id: '${entity.id}',
                agentId: '${entity.agentId}',
                shopName: '${(entity.shopName)!''}',
                linkname: '${(entity.linkname)!''}',
                tel: '${(entity.tel)!''}',
                workTime: '${(entity.workTime)!''}',
                isAllowOpenBox: '${(entity.isAllowOpenBox)!''}',
                activeStatus: '${(entity.activeStatus)!''}'
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
                    agentId: ${Session['SESSION_KEY_USER'].agentId},
                    shopName: form.shopName.value,
                    linkname: form.linkname.value,
                    tel: form.tel.value,
                    workTime: workTime,
                    isAllowOpenBox: form.isAllowOpenBox.value,
                    activeStatus: form.activeStatus.value
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
                        url: '${contextPath}/security/zc/shop_rent_car/update_basic.htm',
                        dataType: 'json',
                        data: values,
                        success: function (json) {
                        <@app.json_jump/>
                            if (json.success) {
                            } else {
                                $.messager.alert('提示信息', json.message, 'info');
                            }
                        }
                    });
                }else {
                }
                return success;
            }
        win.data('ok', ok);
    })();
</script>