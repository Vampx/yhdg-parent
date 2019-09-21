    <style type="text/css">
        .main .left_bar {
            height: 0%;
        }
        .param-three .combo-arrow{
            margin-top: 20px;
            margin-left: 10px;
        }
        .c-d-three-1{
            width: 100%;
            height: 100%;
            padding: 2px;
            box-sizing: border-box;
            display: flex;
        }
        @media only screen and (min-width: 1366px) {
            .report_right_table{
                height: 440px;
                padding-top: 6px;
            }
        }
        @media only screen and (min-width: 1900px){
            .report_right_table{
                height: 520px;
                padding-top: 6px;
            }
        }
    </style>
        <div class="main" style="top:25px">
            <div class="batteryDetail">
                <div class="battery-c-detail">
                    <div class="battery-c-detail-up">
                        <div class="c-d-tips">
                            <span><i></i>充电器详情</span>
                            <div>
                                <button class="btn btn_blue" id="info_close">关闭</button>
                            </div>
                        </div>
                        <div class="c-d-two">
                            <span>换电柜编号：${(entity.cabinetId)!''}</span>
                            <span>格口号：${(entity.boxNum)!''}</span>
                        </div>
                        <div class="c-d-three-tabs">
                            <a href="#">
                                <span class="active" data-id="0">基本信息</span>
                            </a>
                            <a href="#" id="cabinet_charger_report">
                                <span data-id="1">上报信息</span>
                            </a>
                        </div>
                    </div>

                    <div class="battery-c-detail-down">
                        <div class="c-d-three-0">
                            <div class="cdtc-left">
                                <div class="cdtc-left-box">
                                    <div>
                                        <i></i>换电柜充电器实时数据
                                    </div>
                                </div>
                                <div class="cdtc-left-box">
                                    <div>
                                        <i></i>基础值
                                    </div>
                                    <div class="cdtc-text">
                                        充电器型号：${(entity.chargerModule)!''}&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        充电器版本：${(entity.chargerVersion)!''}&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        充电状态：<#if (entity.chargeState)?? >${(entity.chargeStateName)!''}</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        充电阶段：<#if (entity.chargeStage)?? >${(entity.chargeStageName)!''}</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        充电时长：${(entity.chargeTime)!''}&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        充电输出电压：<#if (entity.chargeVoltage)?? >${(entity.chargeVoltage)!0}mV</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        电池端检测电压：<#if (entity.batteryVoltage)?? >${(entity.batteryVoltage)!0}mV</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        充电器输出电流：<#if (entity.chargeCurrent)?? >${(entity.chargeCurrent)!0}mA</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        变压器温度：<#if (entity.transformerTemp)?? >${entity.transformerTemp}℃</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        散热片温度：<#if (entity.heatsinkTemp)?? >${entity.heatsinkTemp}℃</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        环境温度：<#if (entity.ambientTemp)?? >${entity.ambientTemp}℃</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        充电器故障：<#if (entity.chargerFault)?? >${(entity.chargerFaultName)!''}</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        BMS停止充电原因：<#if (entity.chargerFault)?? >${(entity.bmsStopFaultName)!''}</#if>&nbsp;
                                    </div>
                                </div>
                            </div>
                            <div class="cdtc-right">
                                <div class="cdtc-left-box">
                                    <div>
                                        <div style="width: 30px;color: blue;cursor:pointer" onclick="editCharger()">修改</div>
                                    </div>
                                </div>
                                <div class="cdtc-left-box">
                                    <div>
                                        <i></i>设置值
                                    </div>
                                    <div class="cdtc-text">
                                        是否充电：<#if (entity.enableCharge)?? >${entity.enableChargeName}</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        是否开启电池数据连接：<#if (entity.enableLink)?? >${(entity.enableLinkName)!''}</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        自动选择充电电压：<#if (entity.autoSwtichMode)?? >${(entity.autoSwtichModeName)!''}</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        阶段1(预充)最大充电电压：<#if (entity.maxChargeVoltageOfStage1)?? >${(entity.maxChargeVoltageOfStage1)!0}mV</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        阶段1(预充)充电电流：<#if (entity.chargeCurrentOfStage1)?? >${((entity.chargeCurrentOfStage1)!0)}mA</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        阶段2(恒流1)最大充电电压U3：<#if (entity.maxChargeVoltageOfStage2)?? >${(entity.maxChargeVoltageOfStage2)!0}mV</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        阶段2(恒流1)充电电流：<#if (entity.chargeCurrentOfStage2)?? >${(entity.chargeCurrentOfStage2)!0}mA</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        阶段3开始改变电流时电压：<#if (entity.slopeVoltage)?? >${(entity.slopeVoltage)!0}mV</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        阶段3(恒流2)充电电流：<#if (entity.chargeCurrentOfStage3)?? >${(entity.chargeCurrentOfStage3)!0}mA</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        电池满电电压：<#if (entity.fullVoltage)?? >${(entity.fullVoltage)!0}mV</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        满电电压时斜率电流：<#if (entity.slopeCurrent)?? >${(entity.slopeCurrent)!0}mA</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        阶段4最小充电电流：<#if (entity.minCurrentOfStage4)?? >${(entity.minCurrentOfStage4)!''}mA</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        低温环境充电模式：<#if (entity.lowTemperatureMode)?? >${(entity.lowTemperatureModeName)!''}</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        是否异常：<#if entity.abnormal?? && entity.abnormal == 1>异常<#elseif entity.abnormal?? && entity.abnormal == 0>正常</#if>&nbsp;
                                    </div>
                                    <div class="cdtc-text">
                                        使能NFC检测：<#if entity.enableNfc?? && entity.enableNfc == 1>使能<#elseif  entity.enableNfc?? && entity.enableNfc == 0>禁用</#if>&nbsp;
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="c-d-three-1" style="display: none;">
                            <div class="report-left" style="width: 150px;height:460px;">
                                <div class="log-tips">
                                    <i></i>上报日期
                                </div>
                                <div class="ztree_body easyui-tree" id="cabinet_charger_tree"
                                     data-options="
                                onBeforeSelect: App.tree.toggleSelect,
                                onClick: function(node) {
                                    tree_query();
                                }
                            ">
                                </div>
                            </div>

                            <div class="report-right">
                                <div class="report_right_table">
                                    <table id="cabinet_charger_report_page_table"></table>
                                </div>
                            </div>
                            <div style="position: absolute;right: 20px;top: 55px;">
                                <button class="btn btn_red" onclick="export_excel()">批量导出</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
<script>
    $('.c-d-three-tabs').on('click', 'span', (e) => {
        $('.c-d-three-tabs span').attr('class', '')
        $('.c-d-three-tabs span').eq(e.currentTarget.dataset.id).attr('class', 'active')
        $('.c-d-three-0').hide()
        $('.c-d-three-1').hide()
        $('.c-d-three-' + e.currentTarget.dataset.id).show()
    })

</script>
<script type="text/javascript">

    $('.nav').hide();
    $('.main .index_con').css("left", 0);


    $('#info_close').click(function () {
        var win = $('#${pid}');
        win.window('close');
    })

    //点击上报信息
    $('#cabinet_charger_report').click(function () {
        $('.c-d-three-1').show();
        var cabinetId = '${(entity.cabinetId)!''}';
        var boxNum = '${(entity.boxNum)!''}';

        $("#cabinet_charger_tree").tree({
            url: '${contextPath}/security/hdg/cabinet_charger_report_date/tree.htm?cabinetId=' + cabinetId + '&boxNum=' + boxNum ,
            onLoadSuccess: function (data) {
                eval(data);
            }
        });

        $('#cabinet_charger_report_page_table').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/cabinet_charger_report/page.htm",
            fitColumns: true,
            pageSize: 50,
            pageList: [50, 100],
            idField: 'cabinetId',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '柜子编号',
                        align: 'center',
                        field: 'cabinetId',
                        width: 100
                    },
                    {
                        title: '格口号',
                        align: 'center',
                        field: 'boxNum',
                        width: 50
                    },
                    {
                        title: '上报时间',
                        align: 'center',
                        field: 'createTime',
                        width: 140
                    },
                    {
                        title: '充电器版本',
                        align: 'center',
                        field: 'chargerVersion',
                        width: 140
                    },
                    {
                        title: '充电器型号',
                        align: 'center',
                        field: 'chargerModule',
                        width: 140
                    },
                    {
                        title: '充电状态',
                        align: 'center',
                        field: 'chargeStateName',
                        width: 100
                    },
                    {
                        title: '充电阶段',
                        align: 'center',
                        field: 'chargeStageName',
                        width: 100
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 150,
                        formatter: function (val, row) {
                            var html = '';
                            html += '<a href="javascript:viewCabinetChargerReport(\'CABINET_ID\',\'BOX_NUM\',\'CREATE_TIME\')">查看</a>';
                            return html.replace(/CABINET_ID/g, row.cabinetId).replace(/BOX_NUM/g, row.boxNum).replace(/CREATE_TIME/g, row.createTime);
                        }
                    }
                ]
            ],
            onLoadSuccess: function () {
                $('#cabinet_charger_report_page_table').datagrid('clearChecked');
                $('#cabinet_charger_report_page_table').datagrid('clearSelections');
            }
        });

        var cabinetChargerReportDatagrid = $('#cabinet_charger_report_page_table');

        cabinetChargerReportDatagrid.datagrid('options').queryParams = {
            cabinetId: cabinetId,
            boxNum: boxNum
        };
        cabinetChargerReportDatagrid.datagrid('load');
    });

    function tree_query() {
        var tree = $('#cabinet_charger_tree');
        var createTime = tree.tree('getSelected');
        if (createTime) {
            createTime = createTime.id || '';
        } else {
            createTime = '';
        }
        if (createTime.length >= 10) {
            var cabinetChargerReportDatagrid = $('#cabinet_charger_report_page_table');
            var cabinetId = '${(entity.cabinetId)!''}';
            var boxNum = '${(entity.boxNum)!''}';
            if (cabinetId != null && cabinetId != '' && boxNum != null && boxNum != '') {
                cabinetChargerReportDatagrid.datagrid('options').queryParams = {
                    cabinetId: cabinetId,
                    boxNum: boxNum,
                    createTime: createTime + " 00:00:00"
                };
                cabinetChargerReportDatagrid.datagrid('load');
            }
        }
    }

    function viewCabinetChargerReport(cabinetId, boxNum, createTime) {
        App.dialog.show({
            css: 'width:886px;height:490px;',
            title: '查看',
            href: "${contextPath}/security/hdg/cabinet_charger_report/view.htm?cabinetId=" + cabinetId + "&boxNum=" + boxNum + "&createTime=" + createTime
        });
    }

    function export_excel() {
        var tree = $('#cabinet_charger_tree');
        var cabinetId = '${(entity.cabinetId)!''}';
        var boxNum = '${(entity.boxNum)!''}';
        var createTime = tree.tree('getSelected');
        if (createTime) {
            createTime = createTime.id || '';
        } else {
            createTime = '';
        }
        if(createTime == '' || createTime.length < 10) {
            $.messager.alert('提示信息', '请选择年月日', 'info');
            return false;
        }
        $.messager.confirm('提示信息', '确认导出数据?', function (ok) {
            if (ok) {
                var queryParams = {
                    cabinetId: cabinetId,
                    boxNum: boxNum,
                    createTime: createTime
                };
                $.post('${contextPath}/security/hdg/cabinet_charger_report/export_excel.htm', queryParams, function (json) {
                    $.messager.progress('close');
                    if (json.success) {
                        document.location.href = '${contextPath}/security/hdg/cabinet_charger_report/download.htm?filePath=' + json.data[0] + "&formatDate=" + json.data[1] + "&cabinetId=" + json.data[2] + "&boxNum=" +json.data[3];
                    } else {
                        $.messager.alert('提示消息', json.message, 'info');
                    }
                }, 'json');
            }
        });
    }

    function reload() {
        var win = $('#${pid}');
        win.window('close');
        var cabinetId = '${(entity.cabinetId)!''}';
        var boxNum = '${(entity.boxNum)!''}';
        App.dialog.show({
            css: 'width:830px;height:620px;overflow:visible;',
            title: '充电器',
            href: "${contextPath}/security/hdg/cabinet_charger/info.htm?cabinetId=" + cabinetId + "&boxNum=" + boxNum
        });
    }

    function editCharger() {
        var cabinetId = '${(entity.cabinetId)!''}';
        var boxNum = '${(entity.boxNum)!''}';
        App.dialog.show({
            css: 'width:660px;height:610px;overflow:visible;',
            title: '修改',
            href: "${contextPath}/security/hdg/cabinet_charger/edit.htm?cabinetId=" + cabinetId + "&boxNum=" + boxNum,
            event: {
                onClose: function () {
                    reload();
                }
            }
        });
    }

</script>
