<script type="text/css">
   .tab_item table tr td{
        width: 120px;
    }
</script>
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
<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td style="font-weight: 600;height: 32px;line-height: 32px;font-size: 16px;">基础设置</td>
                </tr>
                <tr>
                    <td style="width: 70px;" align="left">*设备编号：</td>
                    <td><span>${entity.id}</span></td>
                    <td width="70" align="right">*设备SN：</td>
                    <td><span>${(entity.mac)!''}</span></td>
                </tr>
                <tr>
                    <td width="70" align="left">*设备名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="cabinetName"
                               maxlength="30" value="${(entity.cabinetName)!''}"/></td>
                    <td width="140" align="right">*所属运营商：</td>
                    <td><input name="agentId" required="true" class="easyui-combotree"
                               editable="false" style="width: 183px; height: 28px;" id="agent_id_${pid}" disabled
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto',
                                onClick: function () {
                                       swich_agent_${pid}();
                                }"
                               value="${(entity.agentId)!''}"/>
                </tr>
                <tr>
                    <td width="70" align="left">设备位置：</td>
                    <td colspan="2"><input type="text" class="text easyui-validatebox" readonly maxlength="50" style="width: 290px;"
                               value="${(entity.address)!''}"/>&nbsp;&nbsp;</td>
                    <td>
                        <a href="javascript:getLocation()">
                            <div style="width: 50px;height: 33px;line-height: 33px;text-align: center;color: #FFFFFF;font-size: 14px;margin-left: 30px;background-color: #556bd8;border-radius: 6px;">定位
                            </div>
                        </a>
                    </td>
                </tr>
                <tr>
                <td align="left">工作时间：</td>
                <td><input type="text" class="text easyui-timespinner" maxlength="5" value="${(beginTime)!''}" data-options="showSeconds:false" style="width:86px; height:28px;" name="beginTime"/>--<input type="text" class="text easyui-timespinner" maxlength="5" value="${(endTime)!''}" data-options="showSeconds:false,min:'00:01',max:'23:59' " style="width:86px; height:28px;" name="endTime"/></td>
                <td width="70" align="right">终端id：</td>
                <td><input type="text" class="text easyui-validatebox" name="terminalId" readonly
                value="${(entity.terminalId)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">*满电电量：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="chargeFullVolume"
                               data-options="min:0, max:100" style="width: 183px; height: 28px;" required="true"
                               value="${(entity.chargeFullVolume)!0}"/></td>
                    <td align="right">*可换进电量：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="permitExchangeVolume"
                               data-options="min:0, max:100" style="width: 183px; height: 28px;" required="true"
                               value="${(entity.permitExchangeVolume)!0}"/></td>
                </tr>
                <tr>
                    <td width="70" align="left">SIM卡号：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="25" name="simMemo" value="${(entity.simMemo)!''}"/></td>
                    <td align="right">是否启用：</td>
                    <td>
                        <select name="activeStatus" id="active_status_${pid}" class="easyui-combobox" editable="false"
                                style="width: 183px; height: 28px;">
                        <#list activeStatusEnum as e>
                            <option value="${e.getValue()}"
                                    <#if entity.activeStatus?? && entity.activeStatus == e.getValue()>selected</#if>>${e.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="150" align="left">电价（元/度）：</td>
                    <td><input class="easyui-numberspinner" id="price_${pid}" name="price" maxlength="3" <#if entity ??><#if entity.price ??> value="${(entity.price)!0}"</#if> </#if>  style="width:182px;height: 28px;" data-options="min:0.00,precision:2"></td>

                    <td align="right">共享类型：</td>
                    <td>
                        <select name="viewType" id="view_type_${pid}" style="width: 183px; height: 28px;">
                        <#list ViewTypeEnum as s>
                            <option value="${s.getValue()}"
                                    <#if entity.viewType==s.getValue()>selected="selected"</#if>>${s.getName()}</option>
                        </#list>
                        </select>
                    </td>

                </tr>
                <tr>
                    <td align="left">联系人：</td>
                    <td><input type="text" class="text easyui-validatebox" name="linkname" maxlength="40"
                               value="${(entity.linkname)!''}"/></td>
                    <td width="70" align="right">电话：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="11" name="tel"
                               value="${(entity.tel)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">最低可换电量：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="minExchangeVolume"
                               data-options="min:10, max:${entity.chargeFullVolume}-1" style="width: 183px; height: 28px;"
                               value="${(entity.minExchangeVolume)!''}"/></td>
                </tr>
                <tr>
                    <td align="left" valign="top" style="padding-top:10px;">备注：</td>
                    <td colspan="3"><textarea style="width:505px;" maxlength="20" name="memo">${(entity.memo)!''}</textarea></td>
                </tr>
                <#--<tr>-->
                    <#--<td style="font-weight: 600;height: 32px;line-height: 32px;font-size: 16px;">上线入口</td>-->
                <#--</tr>-->
                <#--<tr>-->
                    <#--<td align="left">*上线状态：</td>-->
                    <#--<td>-->
                        <#--<span class="radio_box">-->
                            <#--<input type="radio" class="radio" name="upLineStatus" id="up_line_status_2"-->
                                   <#--<#if entity.upLineStatus?? && entity.upLineStatus == 2>checked</#if> value="2"/><label for="up_line_status_2">上线</label>-->
                        <#--</span>-->
                        <#--<span class="radio_box">-->
                            <#--<input type="radio" class="radio" name="upLineStatus" id="up_line_status_0"-->
                                   <#--<#if entity.upLineStatus?? && entity.upLineStatus == 2><#else >checked</#if> value="0" /><label for="up_line_status_0">下线</label>-->
                        <#--</span>-->
                    <#--</td>-->
                    <#--<td align="right">*上线时间：</td>-->
                    <#--<td>-->
                        <#--<input type="text" id="up_line_time_${pid}" editable="false" class="text easyui-datetimebox" name="upLineTime"  value="${(entity.upLineTime?string('yyyy-MM-dd HH:mm:ss'))!''}" style="width: 183px; height: 28px;"/>-->
                    <#--</td>-->
                <#--</tr>-->
                <tr>
                    <td style="font-weight: 600;height: 32px;line-height: 32px;font-size: 16px;">安全设置</td>
                </tr>
                <tr>
                    <td width="140" align="left">*最大充电功率：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="maxChargePower"  style="width: 183px; height: 28px;" required="true" value="${(entity.maxChargePower)!0}" /></td>
                    <td width="140" align="right">*最大充电数量：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="maxChargeCount" data-options="min:1, max:100" style="width: 183px; height: 28px;" required="true" value="${(entity.maxChargeCount)!0}" /></td>
                </tr>
                <tr>
                    <td width="140" align="left">格口功率范围：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="boxMinPower" style="width: 85px; height: 28px;" required="true" value="${(entity.boxMinPower)!0}" />&nbsp;-&nbsp;<input type="text" class="text easyui-numberspinner" name="boxMaxPower" style="width: 85px; height: 28px;" value="${(entity.boxMaxPower)!''}"/></td>
                    <td width="140" align="right">自停涓流时间(秒)：</td>
                    <td><input type="text" class="text easyui-numberspinner"   name="boxTrickleTime" style="width: 183px; height: 28px;" value="${(entity.boxTrickleTime)!''}"/></td>
                </tr>
                <tr>
                    <td width="140" align="left">开启风扇温度：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="activeFanTemp" data-options="min:1, max:100" style="width: 184px; height: 28px;" required="true" value="${(entity.activeFanTemp)!0}" /></td>
                </tr>
                <tr>
                    <td align="left">动态码：</td>
                    <td>
                        <input name="dynamicCode" id="dynamic_code_${pid}" value="${(entity.dynamicCode)!''}" maxlength="4" style="width: 185px;height: 28px;" type="text" class="easyui-numberbox">
                    </td>
                    <td align="right">子类型：</td>
                    <td>
                        <select name="subtype" id="sub_type_${pid}" style="width: 183px; height: 28px;">
                        <#list SubtypeEnum as s>
                            <option value="${s.getValue()}"
                                    <#if entity.subtype==s.getValue()>selected="selected"</#if>>${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    function swich_agent_${pid}() {
        $('#priceSettingId_${pid}').val('');
        $('#chargePriceSettingName_${pid}').val('');
        var agentId = $('#agent_id_${pid}').combotree('getValue');
    }

    function getLocation() {
        App.dialog.show({
            css: 'width:650px;height:525px;overflow:visible;',
            title: '地图定位',
            href: "${contextPath}/security/hdg/cabinet/edit_new_location.htm?id=${entity.id}",
            event: {
                onClose: function () {
                    var win = $('#${pid}');
                    win.window('close');
                    App.dialog.show({
                        css: 'width:830px;height:620px;overflow:visible;',
                        title: '修改',
                        href: "${contextPath}/security/hdg/cabinet/edit.htm?id=${entity.id}",
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

    (function () {
        <#--$("#up_line_time_${pid}").datetimebox().datetimebox('calendar').calendar({-->
            <#--validator: function(date){-->
                <#--var now = new Date();-->
                <#--var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());-->
                <#--return d1<=date;-->
            <#--}-->
        <#--});-->

        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        win.find('input[name=terminalId],input[name=terminalId]').click(function () {
            selectNotAssociatedTerminal();
        });
        function selectNotAssociatedTerminal() {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '未关联终端',
                href: "${contextPath}/security/yms/not_associated_terminal/select_not_associated_terminal.htm",
                windowData: {
                    ok: function (config) {
                        win.find('input[name=terminalId]').val(config.notAssociatedTerminal.id);
                    }
                },
                event: {
                    onClose: function () {
                    }
                }
            });
        }

        var snapshot = $.toJSON({
            id: '${entity.id}',
            cabinetName: '${(entity.cabinetName)!''}',
            price: '${(entity.price)!''}',
            activeStatus: '${(entity.activeStatus)!''}',
            agentId: '${(entity.agentId)!''}',
            linkname: '${(entity.linkname)!''}',
            workTime: '${(entity.workTime)!''}',
            <#--upLineTime:  '<#if entity.upLineTime ??>${app.format_date_time(entity.upLineTime)!''}</#if>',-->
            terminalId: '${(entity.terminalId)!''}',
            chargeFullVolume: '${(entity.chargeFullVolume)!''}',
            permitExchangeVolume: '${(entity.permitExchangeVolume)!''}',
            dynamicCode: '${(entity.dynamicCode)!''}',
            tel: '${(entity.tel)!''}',
            simMemo: '${(entity.simMemo)!''}',
            activeFanTemp: '${(entity.activeFanTemp)!''}',
            maxChargeCount: '${(entity.maxChargeCount)!''}',
            maxChargePower: '${(entity.maxChargePower)!''}',
            boxMaxPower: '${(entity.boxMaxPower)!''}',
            boxMinPower: '${(entity.boxMinPower)!''}',
            boxTrickleTime: '${(entity.boxTrickleTime)!''}',
            subtype: '${(entity.type)!''}',
            viewType: '${(entity.viewType)!''}',
            <#--upLineStatus:'${(entity.upLineStatus)!''}',-->
            minExchangeVolume: '${(entity.minExchangeVolume)!''}',
            memo: '${(entity.memo)!''}'
        });

        var ok = function () {
            if (!jform.form('validate')) {
                return false;
            }
            var beginTime = win.find('input[name=beginTime]').val();
            var endTime = win.find('input[name=endTime]').val();
            var beginTimeNumber = win.find('input[name=beginTime]').timespinner('getValue');
            var endTimeNumber = win.find('input[name=endTime]').timespinner('getValue');
            if(beginTimeNumber > endTimeNumber) {
                var time = beginTime;
                beginTime = endTime;
                endTime = time;
            }
            var workTime = beginTime + "-" + endTime;

            var price = $('#price_${pid}').numberspinner('getValue');

            var success = true;
            var values = {
                id: '${entity.id}',
                cabinetName: form.cabinetName.value,
                price: price,
                activeStatus: $('#active_status_${pid}').combobox('getValue'),
                agentId: $('#agent_id_${pid}').combotree('getValue'),
                linkname: form.linkname.value,
                workTime: workTime,
            /*    upLineTime: form.upLineTime.value,*/
                terminalId: form.terminalId.value,
                chargeFullVolume: form.chargeFullVolume.value,
                permitExchangeVolume: form.permitExchangeVolume.value,
                dynamicCode: form.dynamicCode.value,
                tel: form.tel.value,
                simMemo: form.simMemo.value,
                activeFanTemp: form.activeFanTemp.value,
                maxChargeCount: form.maxChargeCount.value,
                maxChargePower: form.maxChargePower.value,
                boxMaxPower: form.boxMaxPower.value,
                boxMinPower: form.boxMinPower.value,
                boxTrickleTime: form.boxTrickleTime.value,
                subtype: form.subtype.value,
                viewType: form.viewType.value,
           /*     upLineStatus: form.upLineStatus.value,*/
                minExchangeVolume: form.minExchangeVolume.value,
                memo: form.memo.value
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
                    url: '${contextPath}/security/hdg/cabinet/update_basic.htm',
                    dataType: 'json',
                    data: values,
                    success: function (json) {
                    <@app.json_jump/>
                        if (json.success) {
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                            success = false;
                        }
                    }
                });
            }

            return success;
        };

        win.data('ok', ok);
    })();
</script>