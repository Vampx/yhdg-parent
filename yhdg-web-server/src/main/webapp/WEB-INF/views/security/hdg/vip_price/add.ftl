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
                    <input type="hidden" name="batteryType" id="battery_type">
                    <input type="hidden" name="agentId" id="agent_id">
                    <input type="hidden" name="id">

                    <div class="c-detail-up">
                        <span><i></i>vip套餐管理</span>
                        <button class="btn btn_blue_1" id="back" >返回</button>
                    </div>

                    <div class="c-line">
                        <div class="c-line-left">
                            <b>*</b>所属运营商
                        </div>
                        <div class="c-line-right">
                            <input name="agentId" required="true" id="page_agent_id" class="easyui-combotree"
                                   editable="false"
                                   style="width:187px;height:28px "
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:true,
                                    multiple:false,
                                    panelHeight:'200'
                                "/>
                        </div>
                    </div>

                    <div class="c-line">
                        <div class="c-line-left">
                            <b>*</b>电池型号名称
                        </div>
                        <div class="c-line-right">
                            <input type="text" name="typeName" readonly/>
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
                            <input type="text" maxlength="40"  name="priceName"/>
                        </div>
                    </div>

                  <div class="c-line">
                        <div class="c-line-left">
                            <b>*</b>开始时间
                        </div>
                        <div class="c-line-right">
                            <input type="text" class="text easyui-datebox" editable="false" style="width:175px;height:28px " id="begin_time" required="true">
                        </div>
                    </div>

                    <div class="c-line">
                        <div class="c-line-left">
                            <b>*</b>结束时间
                        </div>
                        <div class="c-line-right">
                            <input type="text" class="text easyui-datebox" editable="false" style="width:175px;height:28px " id="end_time" required="true">
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
                                        <input type="radio" class="radio" name="isActive" checked value="1"/>
                                    </i>
                                    开启
                                </div>
                                <div style="margin-left: 19px;">
                                    <i>
                                        <input type="radio" class="radio" name="isActive" value="0"/>
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
                        <div class="c-line-right" id="vip_exchange_battery_foregift_index">
                            <#include '../vip_exchange_battery_foregift/vip_exchange_battery_foregift_index.ftl'>
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
                            <#include '../vip_packet_period_price/vip_exchange_packet_period_price.ftl'>
                        </div>
                        <table id="vip_packet_period_price_table">
                            <tr></tr>
                        </table>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left">
                            <b></b>换电柜
                        </div>
                        <div class="c-line-right" id="price_cabinet">
                            <#include '../vip_price_cabinet/vip_price_cabinet.ftl'>
                        </div>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left padding-t">
                            <b></b>站点
                        </div>
                        <div class="c-line-right" id="price_station">
                            <#include '../vip_price_station/vip_price_station.ftl'>
                        </div>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left padding-t">
                            <b></b>绑定骑手
                        </div>
                        <div class="c-line-right" id="price_station">
                            <#include '../vip_price_customer/vip_price_customer.ftl'>
                        </div>
                    </div>
                </form>

                <div class="c-line" style="margin-bottom: 100px;">
                    <div class="c-line-left"></div>
                    <div class="c-line-right">
                        <div class="btns">
                            <button class="btn btn_blue_1 ok" style="width: 100px;">确定</button>
                            <button class="btn btn_blue_2 close" style="width: 100px;">取消</button>
                        </div>
                    </div>
                </div>

            </div>

        </div>
        </@app.container>
    </@app.body>
</@app.html>
<script>

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
                    $.post('${contextPath}/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift_index.htm', {
                        batteryType: config.systemBatteryType.id,
                        agentId: $('#page_agent_id').combobox('getValue'),
                        index: 0,
                        vipExchangeId: 0
                    }, function (html) {
                        $("#vip_exchange_battery_foregift_index").html(html);
                    }, 'html');

                }
            },
            event: {
                onClose: function() {
                }
            }
        });
    }

    $("body").on("input",".number_money",function(){
        var reg = $(this).val().match(/\d+\.?\d{0,2}/);
        var txt = '';
        if (reg != null) {
            txt = reg[0];
        }
        $(this).val(txt);
    });

    $("body").on("input",".number_percent",function(){
        var reg = $(this).val().match(/\d+\.?\d{0,2}/);
        var txt = '';
        if (reg != null) {
            txt = reg[0];
        }
        $(this).val(txt);
    });

    $('.nav').hide();
    $('.main .index_con').css("left", 0);

    $('#back').click(function () {
        window.close();
    });

    (function () {

        $('.ok').click(function () {

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

            var batteryType = $('#agent_battery_type_form').find('input[name=batteryType]').val();
            var priceName = $('#agent_battery_type_form').find('input[name=priceName]').val();
            var isActive = $('#agent_battery_type_form').find('input[name="isActive"]:checked').val();

            var vipExchangeIdTable = $("#foregift_reduce_money_table");
            var vipExchangeIdList = [];
            vipExchangeIdTable.find('tr').each(function () {
                var vipExchangeId = $(this).find('input[name=vipExchangeId]').val();
                if(vipExchangeId != undefined){
                    vipExchangeIdList.push(vipExchangeId);
                }
            });

            var vipPacketPriceIdTable = $("#vip_packet_period_price_table");
            var vipPacketPriceIdList = [];
            vipPacketPriceIdTable.find('tr').each(function () {
                var vipPacketId = $(this).find('input[name=vipPacketId]').val();
                if(vipPacketId != undefined){
                    vipPacketPriceIdList.push(vipPacketId);
                }
            });

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
            var foregiftId = $(".vip_battery_foregift_list").find(".active").attr("battery_foregift_id");
            console.log("选中"+foregiftId)
            var values = {
                foregiftId:foregiftId,
                agentId: agentId,
                priceName: priceName,
                batteryType: batteryType,
                beginTime: beginTime + " 00:00:00",
                endTime: endTime + " 23:59:59",
                isActive: isActive,
                vipExchangeIdList:vipExchangeIdList,
                vipPacketPriceIdList:vipPacketPriceIdList,
                customerMobileList:customerMobiles,
                cabinetIdList:cabinetIds,
                stationIdList:stationIds
            };
            $.ajax({
                cache: false,
                async: false,
                type: 'POST',
                url: '${contextPath}/security/hdg/vip_price/create.htm',
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

        $('.close').click(function () {
            window.location.href = "${contextPath}/security/main/module.htm?moduleId=3&url=${contextPath}/security/hdg/vip_price/index.htm";
        });

    })();
</script>