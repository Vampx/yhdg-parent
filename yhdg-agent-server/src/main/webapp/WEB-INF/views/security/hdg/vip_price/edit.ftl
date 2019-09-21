<div class="popup_body" style="padding-left:50px;padding-top: 40px;font-size: 14px;min-height: 72%;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="left">*运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true" readonly
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
                    <td align="left">*套餐名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="priceName"
                               style="width: 242px; height: 28px;"
                               maxlength="40" value="${(entity.priceName)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">电池类型：</td>
                    <td>
                        <input name="batteryType" id="battery_type_${pid}" class="easyui-combotree" editable="false" required="true"
                               style="width: 184px; height: 28px;"
                               data-options="url:'',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto',
                                onClick: function(node) {
                                   swich_battery_type_foregift_${pid}();
                                }
                            "
                               url="${contextPath}/security/basic/agent_battery_type/tree.htm?agentId=${entity.agentId}"
                               value="${(entity.batteryType)!''}">
                    </td>
                </tr>
                <tr>
                    <td colspan="2">*电池押金：</td>
                </tr>
                <tr id="exchange_battery_foregift">
                    <#include '../vip_exchange_battery_foregift/vip_exchange_battery_foregift.ftl'>
                </tr>
                <tr>
                    <td colspan="2">电池租金：</td>
                </tr>
                <tr id="exchange_packet_period_price">
                    <#include '../vip_packet_period_price/vip_exchange_packet_period_price.ftl'>
                </tr>
                <tr>
                    <td colspan="2">绑定骑手号码：</td>
                </tr>
                <tr id="vip_price_customer">
                    <#include '../vip_price_customer/vip_price_customer.ftl'>
                </tr>
                <tr>
                    <td colspan="2">绑定柜子：</td>
                </tr>
                <tr id="vip_price_cabinet">
                    <#include '../vip_price_cabinet/vip_price_cabinet.ftl'>
                </tr>
                <tr>
                    <td colspan="2">绑定门店：</td>
                </tr>
                <tr id="vip_price_shop">
                <#include '../vip_price_shop/vip_price_shop.ftl'>
                </tr>
                <tr>
                    <td colspan="2">绑定运营公司：</td>
                </tr>
                <tr id="vip_price_agent_company">
                <#include '../vip_price_agent_company/vip_price_agent_company.ftl'>
                </tr>
                <tr>
                    <td align="left">开始时间：</td>
                    <td>
                        <input type="text" class="text easyui-datebox"  style="width:185px;height:28px " id="begin_time_${pid}"
                               value="<#if (entity.beginTime)?? >${app.format_date(entity.beginTime)}</#if>" required="true" >
                    </td>
                </tr>
                <tr>
                    <td align="left">结束时间：</td>
                    <td>
                        <input type="text" class="text easyui-datebox"  style="width:185px;height:28px " id="end_time_${pid}"
                               value="<#if (entity.endTime)?? >${app.format_date(entity.endTime)}</#if>" required="true" >
                    </td>
                </tr>
                <tr>
                    <td align="left">*是否启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive"
                            <#if entity.isActive?? && entity.isActive == 1>checked</#if>
                                   value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive"
                            <#if entity.isActive?? && entity.isActive == 1><#else >checked</#if>
                                   value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn" style="height: 10%">
    <button class="btn btn_red vip_ok">确定</button>
    <button class="btn btn_border vip_close">关闭</button>
</div>
<script>
    //VIP押金
    $.post('${contextPath}/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift.htm', {
        batteryType: ${(entity.batteryType)!0},
        agentId: ${(entity.agentId)!0},
        priceId:${entity.id},
        index:0
    }, function (html) {
        $("#exchange_battery_foregift").html(html);
    }, 'html');

    function swich_battery_type_foregift_${pid}() {

        if ($('#battery_type_${pid}').combotree('getValue') != null) {
            //VIP押金
            $.post('${contextPath}/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift.htm', {
                batteryType: $('#battery_type_${pid}').combotree('getValue'),
                agentId: $('#agent_id_${pid}').combotree('getValue'),
                priceId:${entity.id},
                index:0
            }, function (html) {
                $("#exchange_battery_foregift").html(html);
            }, 'html');
        }
    }

    //绑定的骑手
    $.post('${contextPath}/security/hdg/vip_price_customer/vip_price_customer.htm', {
        priceId:${entity.id}
    }, function (html) {
        $("#vip_price_customer").html(html);
    }, 'html');

    //绑定的柜子
    $.post('${contextPath}/security/hdg/vip_price_cabinet/vip_price_cabinet.htm', {
        priceId: ${entity.id},
        agentId: ${(entity.agentId)!0}
    }, function (html) {
        $("#vip_price_cabinet").html(html);
    }, 'html');

    //绑定的门店
    $.post('${contextPath}/security/hdg/vip_price_shop/vip_price_shop.htm', {
        priceId: ${entity.id},
        agentId: ${(entity.agentId)!0}
    }, function (html) {
        $("#vip_price_shop").html(html);
    }, 'html');

    //绑定的运营公司
    $.post('${contextPath}/security/hdg/vip_price_agent_company/vip_price_agent_company.htm', {
        priceId: ${entity.id},
        agentId: ${(entity.agentId)!0}
    }, function (html) {
        $("#vip_price_agent_company").html(html);
    }, 'html');


    (function () {

        var win = $('#${pid}'), form = win.find('form');
        $("#begin_time_${pid}").datebox().datebox('calendar').calendar({
            validator: function(date){
                var now = new Date();
                var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return d1<=date;
            }
        });

        $("#end_time_${pid}").datebox().datebox('calendar').calendar({
            validator: function(date){
                var now = new Date();
                var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return d1<=date;
            }
        });

        win.find('button.vip_ok').click(function () {
            if (!form.form('validate')) {
                return false;
            }

            var beginTime = $('#begin_time_${pid}').datetimebox('getValue');
            var endTime = $('#end_time_${pid}').datetimebox('getValue');
            if(beginTime == '') {
                $.messager.alert('提示信息', '请选择开始时间', 'info');
                return;
            }
            if(endTime == '') {
                $.messager.alert('提示信息', '请选择结束时间', 'info');
                return;
            }
            if(beginTime != '' && endTime != '' && beginTime > endTime) {
                $.messager.alert('提示信息', '结束时间必须大于开始时间', 'info');
                return;
            }

            form.form('submit', {
                url: '${contextPath}/security/hdg/vip_price/update.htm',
                onSubmit: function(param) {
                    param.beginTime = beginTime + " 00:00:00";
                    param.endTime = endTime + " 23:59:59";
                    return true;
                },
                success: function (text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if (json.success) {
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.vip_close').click(function () {
            win.window('close');
        });
    })();
</script>