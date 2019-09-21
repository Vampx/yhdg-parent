<@app.html>
    <@app.head>
    <style type="text/css">
        .main .left_bar {
            height: 0%;
        }
        .param-three .combo-arrow{
            margin-top: 20px;
            margin-left: 10px;
        }
    </style>
    </@app.head>
    <@app.body>
        <#function show_temp temp >
            <#return (temp - 2731) / 10 >
        </#function>
        <@app.container>
            <@app.banner/>
        <div class="main battery_edit">
            <div class="left_bar">
                <div class="nav">
                    <@app.menu_index/>
                </div>
            </div>

            <div class="c-detail">

                <form method="post" id="agent_battery_type_form">
                    <input type="hidden" name="batteryType" id="battery_type" value="${(entity.batteryType)!0}">
                    <input type="hidden" name="agentId" id="agent_id" value="${(entity.agentId)!0}">
                    <input type="hidden" name="id" value="${(entity.id)!0}">

                    <div class="c-detail-up">
                        <span><i></i>vip套餐管理</span>
                        <button class="btn btn_blue_1" id="back" >返回</button>
                    </div>

                    <div class="c-line">
                        <div class="c-line-left">
                            <b>*</b>所属运营商
                        </div>
                        <div class="c-line-right">
                            <input name="agentId" id="page_agent_id" class="easyui-combotree" required="true" readonly
                                   editable="false" style="width: 255px; height: 28px;" value="${(entity.agentId)!0}"
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
                        </div>
                    </div>

                    <div class="c-line">
                        <div class="c-line-left">
                            <b>*</b>电池型号名称
                        </div>
                        <div class="c-line-right">
                            <input type="text" name="typeName" readonly value="${(entity.batteryTypeName)!''}"/>
                        </div>
                        <div class="c-line-tips">
                            <a href="javascript:selectBatteryType()" style="font-weight: bold;padding-left: 3px; font-size: 12px">选择电池类型</a>
                        </div>
                    </div>

                    <div class="c-line">
                        <div class="c-line-left">
                            <b>*</b>VIP套餐名称
                        </div>
                        <div class="c-line-right">
                            <input type="text" maxlength="40"  name="priceName" value="${(entity.priceName)!''}"/>
                        </div>
                    </div>

                    <div class="c-line">
                        <div class="c-line-left">
                            <b>*</b>开始时间
                        </div>
                        <div class="c-line-right">
                            <input type="text" class="text easyui-datebox" editable="false" style="width:175px;height:28px " id="begin_time" value="<#if (entity.beginTime)?? >${app.format_date(entity.beginTime)}</#if>" required="true">
                        </div>
                    </div>

                    <div class="c-line">
                        <div class="c-line-left">
                            <b>*</b>结束时间
                        </div>
                        <div class="c-line-right">
                            <input type="text" class="text easyui-datebox" editable="false" style="width:175px;height:28px " id="end_time"   value="<#if (entity.endTime)?? >${app.format_date(entity.endTime)}</#if>" required="true">
                        </div>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left">
                            <b>*</b>是否启用
                        </div>
                        <div class="c-line-right">
                            <div class="shang font1">
                                <div>
                                    <i>
                                        <input type="radio" class="radio" name="isActive" <#if entity.isActive?? && entity.isActive == 1>checked</#if> value="1"/>
                                    </i>
                                    开启
                                </div>
                                <div style="margin-left: 19px;">
                                    <i>
                                        <input type="radio" class="radio" name="isActive" <#if entity.isActive?? && entity.isActive == 1><#else >checked</#if> value="0"/>
                                    </i>
                                    不开启
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="c-line">
                        <div class="c-line-left">
                            <b>*</b>电池押金
                        </div>
                        <div class="c-line-right" id="exchange_battery_foregift">
                            <#include '../vip_exchange_battery_foregift/vip_exchange_battery_foregift.ftl'>
                        </div>
                        <table id="foregift_reduce_money_table">
                            <tr></tr>
                        </table>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left">
                            <b>*</b>电池租金
                        </div>
                        <div class="c-line-right" id="exchange_packet_period_price">
                            <#include '../vip_packet_period_price/vip_exchange_packet_period_price_edit.ftl'>
                        </div>
                        <table id="vip_packet_period_price_table">
                            <tr></tr>
                        </table>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left">
                            <b></b>换电柜
                        </div>
                        <div class="c-line-right" id="vip_price_cabinet">
                            <#include '../vip_price_cabinet/vip_price_cabinet_edit.ftl'>
                        </div>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left padding-t">
                            <b></b>站点
                        </div>
                        <div class="c-line-right" id="vip_price_station">
                            <#include '../vip_price_station/vip_price_station_edit.ftl'>
                        </div>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left padding-t">
                            <b></b>绑定骑手
                        </div>
                        <div class="c-line-right" id="vip_price_customer">
                            <#include '../vip_price_customer/vip_price_customer_edit.ftl'>
                        </div>
                    </div>
                </form>

                <div class="c-line" style="margin-bottom: 100px;">
                    <div class="c-line-left"></div>
                    <div class="c-line-right">
                        <div class="btns">
                            <button class="btn btn_blue_1 vip_edit_ok" style="width: 100px;">确定</button>
                            <button class="btn btn_blue_2 vip_edit_close" style="width: 100px;">取消</button>
                        </div>
                    </div>
                </div>

            </div>

        </div>
        </@app.container>
    </@app.body>
</@app.html>
<script>

    $(function () {
        $("#begin_time").datebox().datebox('calendar').calendar({
            validator: function(date){
                var now = new Date();
                var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return d1<=date;
            }
        });

        $("#end_time").datebox().datebox('calendar').calendar({
            validator: function(date){
                var now = new Date();
                var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return d1<=date;
            }
        });
    })
</script>
<script>

    var page = ${(page)!1};
    var rows = ${(rows)!10};

    //VIP押金
    $.post('${contextPath}/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift.htm', {
        batteryType: ${(entity.batteryType)!0},
        agentId: ${(entity.agentId)!0},
        priceId: ${(entity.id)!0},
        index:0
    }, function (html) {
        $("#exchange_battery_foregift").html(html);
    }, 'html');

    //绑定的柜子
    $.post('${contextPath}/security/hdg/vip_price_cabinet/vip_price_cabinet_edit.htm', {
        priceId: ${(entity.id)!0},
        agentId: ${(entity.agentId)!0}
    }, function (html) {
        $("#vip_price_cabinet").html(html);
    }, 'html');

    //绑定的站点
    $.post('${contextPath}/security/hdg/vip_price_station/vip_price_station_edit.htm', {
        priceId: ${(entity.id)!0},
        agentId: ${(entity.agentId)!0}
    }, function (html) {
        $("#vip_price_station").html(html);
    }, 'html');


    //绑定的骑手
    $.post('${contextPath}/security/hdg/vip_price_customer/vip_price_customer_edit.htm', {
        priceId: ${(entity.id)!0},
        agentId: ${(entity.agentId)!0}
    }, function (html) {
        $("#vip_price_customer").html(html);
    }, 'html');


    function selectBatteryType() {
        var agentId = $('#page_agent_id').combobox('getValue');
        if(agentId == '') {
            $.messager.alert('提示信息', '请选择运营商', 'info');
            return;
        }
        App.dialog.show({
            css: 'width:826px;height:522px;overflow:visible;',
            title: '选择电池类型',
            href: "${contextPath}/security/basic/system_battery_type/select_battery_type.htm",
            windowData: {
                getOk: function(config) {
                    $('#agent_battery_type_form').find('input[name=batteryType]').val(config.systemBatteryType.id);
                    $('#agent_battery_type_form').find('input[name=typeName]').val(config.systemBatteryType.typeName);
                    $('#agent_battery_type_form').find('input[name=agentId]').val($('#page_agent_id').combobox('getValue'));

                    //押金
                    $.post('${contextPath}/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift.htm', {
                        batteryType: config.systemBatteryType.id,
                        agentId: $('#page_agent_id').combobox('getValue'),
                        priceId: ${(entity.id)!0},
                        index:0
                    }, function (html) {
                        $("#exchange_battery_foregift").html(html);
                    }, 'html');


                }
            },
            event: {
                onClose: function() {
                }
            }
        });
    }


    $('.nav').hide();
    $('.main .index_con').css("left", 0);

    $('#back').click(function () {
        window.close();
    });

    (function () {

        $('.vip_edit_ok').click(function () {

            var agentId = $('#page_agent_id').combobox('getValue');
            if(agentId == '') {
                $.messager.alert('提示信息', '请选择运营商', 'info');
                return;
            }
            var batteryType = $('#agent_battery_type_form').find('input[name=batteryType]').val();

            if (batteryType == '' || batteryType == undefined) {
                $.messager.alert('提示信息', '请选择电池类型', 'info');
                return;
            }

            var beginTime = $('#begin_time').datetimebox('getValue');
            var endTime = $('#end_time').datetimebox('getValue');
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
            var id = $('#agent_battery_type_form').find('input[name=id]').val();

            var batteryType = $('#agent_battery_type_form').find('input[name=batteryType]').val();
            var priceName = $('#agent_battery_type_form').find('input[name=priceName]').val();
            var isActive = $('#agent_battery_type_form').find('input[name="isActive"]:checked').val();

            var exchangeInstallmentCustomer = $("#vip_price_customer_mobile");//绑定骑手手机
            var customerMobiles=[];
            exchangeInstallmentCustomer.find('tr').each(function () {
                var customerMobile=$(this).find('#customer_mobile').val();
                if(customerMobile != undefined){
                    customerMobiles.push(customerMobile);
                }
            });
            var cabinetTable = $("#vip_price_cabinet_list");//绑定柜子
            var cabinetIds = [];
            cabinetTable.find('tr').each(function () {
                var cabinetId = $(this).find('#cabinet_id').val();
                if(cabinetId != undefined){
                    cabinetIds.push(cabinetId);
                }
            });

            var stationTable = $("#vip_price_station_list");//绑定柜子
            var stationIds = [];
            stationTable.find('tr').each(function () {
                var stationId = $(this).find('#station_id').val();
                if(stationId != undefined){
                    stationIds.push(stationId);
                }
            });

            var values = {
                id: id,
                agentId: agentId,
                priceName: priceName,
                batteryType: batteryType,
                beginTime: beginTime + " 00:00:00",
                endTime: endTime + " 23:59:59",
                isActive: isActive,
                customerMobileList:customerMobiles,
                cabinetIdList:cabinetIds,
                stationIdList:stationIds
            };
            $.ajax({
                cache: false,
                async: false,
                type: 'POST',
                url: '${contextPath}/security/hdg/vip_price/update.htm',
                dataType: 'json',
                data: values,
                success: function (json) {
                <@app.json_jump/>
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        window.location.href = "${contextPath}/security/main/module.htm?moduleId=3&url=${contextPath}/security/hdg/vip_price/index.htm";
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                },
                error: function (text) {
                    $.messager.alert('提示信息', text, 'info');
                }
            });

        });

        $('.vip_edit_close').click(function () {
            window.close();
        });

    })();


</script>
