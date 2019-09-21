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
                    <input type="hidden" name="batteryType" value="${(entity.batteryType)!0}">
                    <input type="hidden" name="agentId" value="${(entity.agentId)!0}">
                    <div class="c-detail-up">
                        <span><i></i>电池型号管理</span>
                        <button class="btn btn_blue_1" id="back" >返回</button>
                    </div>

                    <div class="c-line">
                        <div class="c-line-left">
                            <b>*</b>所属运营商
                        </div>
                        <div class="c-line-right">
                            <input name="agentId" required="true" id="page_agent_id" class="easyui-combotree" readonly
                                   editable="false"
                                   style="width:187px;height:28px "
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:true,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                    }
                                " value="${(entity.agentId)!''}"/>
                        </div>
                    </div>

                    <div class="c-line">
                        <div class="c-line-left">
                            <b>*</b>电池型号名称
                        </div>
                        <div class="c-line-right">
                            <input type="text" name="typeName" readonly value="${(entity.typeName)!''}"/>
                        </div>
                    </div>

                    <div class="c-line">
                        <div class="c-line-left">
                            <b>*</b>电池押金
                        </div>
                        <div class="c-line-right" id="exchange_battery_foregift">
                            <#include 'view_exchange_battery_foregift.ftl'>
                        </div>
                        <div class="deposit-tips" id="exchange_battery_foregift_memo">
                            <#include 'exchange_battery_foregift_memo.ftl'>
                        </div>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left">
                            <b>*</b>电池租金
                        </div>
                        <div class="c-line-right" id="exchange_packet_period_price">
                            <#include 'view_exchange_packet_period_price.ftl'>
                        </div>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left">
                            <b>*</b>按次收费
                        </div>
                        <div class="c-line-right">
                            <div class="shang font1">
                                <div>
                                    <i>
                                        <input type="radio" class="radio" disabled name="activeSingleExchange" <#if entity.activeSingleExchange?? && entity.activeSingleExchange == 1>checked</#if> value="1"/>
                                    </i>
                                    开启
                                </div>
                                <div style="margin-left: 19px;">
                                    <i>
                                        <input type="radio" class="radio" disabled name="activeSingleExchange" <#if entity.activeSingleExchange?? && entity.activeSingleExchange == 1><#else >checked</#if> value="0"/>
                                    </i>
                                    不开启
                                </div>
                            </div>
                            <div class="xia" id="times_price"  style="display: none">单次价格
                                &nbsp;&nbsp;
                                <input class="easyui-numberspinner" readonly required="true" name="timesPrice" value="<#if entity.timesPrice??>${((entity.timesPrice)/100)!0}</#if>" maxlength="5" style="width:184px;height: 28px;" data-options="min:0.01,precision:2"> &nbsp;&nbsp;元/次
                            </div>
                        </div>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left">
                            <b>*</b>换电柜
                        </div>
                        <div class="c-line-right" id="price_cabinet">
                            <#include 'view_price_cabinet.ftl'>
                        </div>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left padding-t">
                            <b>*</b>站点
                        </div>
                        <div class="c-line-right" id="price_station">
                            <#include 'view_price_station.ftl'>
                        </div>
                    </div>
                </form>

                <div class="c-line" style="margin-bottom: 100px;">
                    <div class="c-line-left"></div>
                    <div class="c-line-right">
                        <div class="btns">
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


    $('.nav').hide();
    $('.main .index_con').css("left", 0);

    $('#back').click(function () {
        window.close();
    });

    if (${(entity.activeSingleExchange)!0} == 1) {
        $("#times_price").show();
    } else {
        $("#times_price").hide();
    }

    //押金
    $.post('${contextPath}/security/basic/agent_battery_type/view_exchange_battery_foregift.htm', {
        batteryType: ${(entity.batteryType)!0},
        agentId: ${(entity.agentId)!0}
    }, function (html) {
        $("#exchange_battery_foregift").html(html);
    }, 'html');

    //绑定的柜子
    $.post('${contextPath}/security/basic/agent_battery_type/view_price_cabinet.htm', {
        batteryType: ${(entity.batteryType)!0},
        agentId: ${(entity.agentId)!0}
    }, function (html) {
        $("#price_cabinet").html(html);
    }, 'html');

    //绑定的站点
    $.post('${contextPath}/security/basic/agent_battery_type/view_price_station.htm', {
        batteryType: ${(entity.batteryType)!0},
        agentId: ${(entity.agentId)!0}
    }, function (html) {
        $("#price_station").html(html);
    }, 'html');

    (function () {

        $('.close').click(function () {
            window.close();
        });

    })();
</script>