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
                    <div class="c-detail-up">
                        <span><i></i>电池型号管理</span>
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
                                    panelHeight:'200',
                                    onClick: function(node) {
                                    }
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
                            <b>*</b>电池押金
                        </div>
                        <div class="c-line-right" id="exchange_battery_foregift">
                            <#include 'exchange_battery_foregift.ftl'>
                        </div>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left">
                            <b>*</b>电池租金
                        </div>
                        <div class="c-line-right" id="exchange_packet_period_price">
                            <#include 'exchange_packet_period_price.ftl'>
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
                                        <input type="radio" class="radio" name="activeSingleExchange" value="1"/>
                                    </i>
                                    开启
                                </div>
                                <div style="margin-left: 19px;">
                                    <i>
                                        <input type="radio" class="radio" name="activeSingleExchange" checked value="0"/>
                                    </i>
                                    不开启
                                </div>
                            </div>
                            <div class="xia" id="times_price"  style="display: none">单次价格 &nbsp;&nbsp;<input class="easyui-numberspinner" required="true" name="timesPrice" maxlength="5" style="width:184px;height: 28px;" data-options="min:0.01,precision:2">&nbsp;&nbsp;元/次</div>
                        </div>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left">
                            <b></b>换电柜
                        </div>
                        <div class="c-line-right" id="price_cabinet">
                            <#include 'price_cabinet.ftl'>
                        </div>
                    </div>

                    <div class="c-line" style="align-items: flex-start;">
                        <div class="c-line-left padding-t">
                            <b></b>站点
                        </div>
                        <div class="c-line-right" id="price_station">
                            <#include 'price_station.ftl'>
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

                    $.post('${contextPath}/security/basic/agent_battery_type/find_agent_battery_type.htm', {
                        batteryType: config.systemBatteryType.id,
                        agentId: $('#page_agent_id').combobox('getValue')
                    }, function (json) {
                        if (json.success) {
                            //押金
                            $.post('${contextPath}/security/basic/agent_battery_type/exchange_battery_foregift.htm', {
                                batteryType: config.systemBatteryType.id,
                                agentId: $('#page_agent_id').combobox('getValue')
                            }, function (html) {
                                $("#exchange_battery_foregift").html(html);
                            }, 'html');

                            //绑定的柜子
                            $.post('${contextPath}/security/basic/agent_battery_type/price_cabinet.htm', {
                                batteryType: config.systemBatteryType.id,
                                agentId: $('#page_agent_id').combobox('getValue')
                            }, function (html) {
                                $("#price_cabinet").html(html);
                            }, 'html');

                            //绑定的站点
                            $.post('${contextPath}/security/basic/agent_battery_type/price_station.htm', {
                                batteryType: config.systemBatteryType.id,
                                agentId: $('#page_agent_id').combobox('getValue')
                            }, function (html) {
                                $("#price_station").html(html);
                            }, 'html');

                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }, 'json');
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

    $('input[name="activeSingleExchange"]').click(function(){
        var value = $(this).val();  //获取选中的radio的值
        if (value == 1) {
            $("#times_price").show();
        } else {
            $("#times_price").hide();
        }
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
            var foregiftId = $(".battery_foregift_create").find(".active").attr("battery_foregift_id");
            if (foregiftId == '' || foregiftId == undefined) {
                $.messager.alert('提示信息', '请添加押金', 'info');
                return;
            }
            var agentId = $('#page_agent_id').combobox('getValue');
            var batteryType = $('#agent_battery_type_form').find('input[name=batteryType]').val();
            var typeName = $('#agent_battery_type_form').find('input[name=typeName]').val();
            var activeSingleExchange = $('#agent_battery_type_form').find('input[name="activeSingleExchange"]:checked').val();
            var timesPrice = $('#agent_battery_type_form').find('input[name=timesPrice]').val();
            var values = {
                agentId: agentId,
                batteryType: batteryType,
                typeName: typeName,
                activeSingleExchange: activeSingleExchange,
                timesPrice: timesPrice*100
            };
            $.ajax({
                cache: false,
                async: false,
                type: 'POST',
                url: '${contextPath}/security/basic/agent_battery_type/create.htm',
                dataType: 'json',
                data: values,
                success: function (json) {
                <@app.json_jump/>
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        window.location.href = "${contextPath}/security/main/module.htm?moduleId=3&url=${contextPath}/security/basic/agent_battery_type/index.htm";
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
            window.close();
        });

    })();
</script>