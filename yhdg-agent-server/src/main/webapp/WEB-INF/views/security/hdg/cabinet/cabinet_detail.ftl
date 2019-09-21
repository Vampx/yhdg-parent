<@app.html>
    <@app.head>
    <style type="text/css">
        .main .left_bar {
            height: 0%;
        }
        .boxlist a:hover{
            color: blue;
        }
    </style>
    <script>
        function datagrid() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/hdg/fault_log/page.htm",
                fitColumns: true,
                pageSize: 50,
                pageList: [50, 100],
                idField: 'id',
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                emptyMsg:'<span>暂无数据</span>',
                queryParams: {
                    agentId: ${(entity.agentId)!},
                    cabinetId: ${(entity.id)!}
                },
                columns: [
                    [
                        {
                            title: '级别',
                            align: 'center',
                            field: 'faultLevelName',
                            width: 20
                        },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: '订单ID',
                            align: 'center',
                            field: 'orderId',
                            width: 40
                        },
                        {
                            title: '换电柜',
                            align: 'center',
                            field: 'cabinetId',
                            width: 40,
                            formatter: function (val, row) {
                                if (val != null) {
                                    return val + '(' + row.cabinetName + ')'
                                } else {
                                    return ''
                                }
                            }
                        },
                        {
                            title: '格口',
                            align: 'center',
                            field: 'boxNum',
                            width: 30
                        },
                        {
                            title: '电池',
                            align: 'center',
                            field: 'batteryId',
                            width: 40,
                            formatter: function (val) {
                                if (val != null) {
                                    return '<a href="#" onclick="battery_view(\''+val+'\')"><u>'+val+'</u></a>';
                                } else {
                                    return '';
                                }
                            }
                        },
                        {
                            title: '品牌',
                            align: 'center',
                            field: 'brandName',
                            width: 40
                        },
                        {
                            title: '故障类型',
                            align: 'center',
                            field: 'faultTypeName',
                            width: 40
                        },
                        {
                            title: '故障内容',
                            align: 'center',
                            field: 'faultContent',
                            width: 100
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 30
                        },
                        {
                            title: '处理时间',
                            align: 'center',
                            field: 'handleTime',
                            width: 60
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    // $('#page_table').datagrid('clearChecked');
                    // $('#page_table').datagrid('clearSelections');
                }
                // ,
                // onLoadError: function () {
                //     $('#base').hide();
                //     $('#alarm').show();
                // }
            });
        }
    </script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <div class="left_bar">
                <div class="nav">
                    <@app.menu_index/>
                </div>
            </div>

            <div class="cabinet_list">
                <div class="c-detail">
                    <div class="c-detail-up">
                        <div class="c-d-tips">
                            <span><i></i>设备详情</span>
                            <div>
                                <button class="btn btn_blue" id="switch">切换</button>
                                <button class="btn btn_blue" id="back">返回</button>
                            </div>
                        </div>
                        <div class="cdd-tabs">
                            <span class="active" data-id="0" style="cursor:pointer">基本信息</span>
                            <span data-id="1" style="cursor:pointer">告警信息</span>
                        </div>
                    </div>
                    <!--基本信息-->
                    <div class="c-detail-down" id="base" style="overflow-y: auto;">

                        <div class="cdd-detail-left">
                            <div>
                                <i></i>柜子实时数据<div style="margin-left: 30px;width: 30px;color: blue;cursor:pointer" onclick="edit()">修改</div>
                            </div>
                            <span>设备名称：${(entity.cabinetName)!''}</span>
                            <span>设备编号：${(entity.id)!''}</span>
                            <span>设备SN：  ${(entity.mac)!''}</span>
                            <span>广告屏ID：${(entity.terminalId)!''}</span>
                            <span>柜子温度1：${((entity.temp1)!0)/100}℃</span>
                            <span>柜子温度2：${((entity.temp2)!0)/100}℃</span>
                            <span>用电量：${(((endNum)!0)/100)?string('0.00')}度</span>
                            <span>电价：${((entity.price)!0)?string('0.00')}元/度</span>
                            <span>实时电费：${((entity.price!0)*((endNum)!0)/100)?string('0.00')}元</span>
                            <span>可换电量：${(entity.chargeFullVolume)!0}%</span>
                            <span>可换进电量：${(entity.permitExchangeVolume)!0}%</span>
                            <span>单个格口功率范围：${(entity.boxMinPower)!0}~${(entity.boxMaxPower)!''}W</span>
                            <span>柜子最大充电功率：${(entity.maxChargePower)!0}W</span>
                        </div>
                        <div class="cdd-detail-right">
                            <div class="handlebtns">
                                <button class="btn btn_blue" style="min-width: 100px;" onclick="open_box()">开箱</button>
                                <button class="btn btn_blue" style="min-width: 100px;margin-left: 15px" onclick="completeOrder()">结束订单</button>
                                <button class="btn btn_blue" style="min-width: 100px;margin-left: 15px" onclick="doActive()">启用</button>
                                <button class="btn btn_blue" style="min-width: 100px;margin-left: 15px" onclick="doUnActive()">禁用</button>
                                <button class="btn btn_blue" style="min-width: 100px;margin-left: 15px" onclick="doAbnormal()">标识异常</button>
                                <button class="btn btn_blue" style="min-width: 100px;margin-left: 15px" onclick="doNormal()">解除异常</button>
                                <button class="btn btn_blue" style="min-width: 100px;margin-left: 15px" onclick="edit_up_line_status()">电池上线</button>
                            </div>
                            <div class="adr-tips">
                                <span>
									<i style="background: #48bcff;"></i>空闲
								</span>
                                <span>
									<i style="background: #36c640;"></i>可换
								</span>
                                <span>
									<i style="background: #ff4242;"></i>充电中
								</span>
                                <span>
									<i style="background: #f0f0f0;"></i>未上线
								</span>
                                <span>
									<i style="background: #ffb239;"></i>锁定
								</span>
                                <span>
									<i style="background: #999999;"></i>禁用
								</span>
                                <span>
									<i style="background: #2f4f4f;"></i>异常标识电池
								</span>
                            </div>
                            <div class="boxlist">
                                <#list cabinetBoxList as box>
                                    <div class="catbox" style="background: ${(box.background)!};color:${((box.battery.upLineStatus==0&&box.isActive==1)?string('#999999',''))!}" boxNum="${(box.boxNum)!0}"
                                         boxStatus="${(box.boxStatus)!0}" orderId="${(box.battery.orderId)!0}" isActive="${(box.isActive)!1}" batteryId="${(box.battery.id)!""}" upLineStatus="${(box.battery.upLineStatus)!''}"
                                         background="${(box.background)!}">
                                        <div>
                                            <span>${(box.boxNum)!}</span>
                                            <span>
                                                 <a style="color: #FFFFFF;" onmouseover="addCss($(this))" onmouseout="removeCss($(this))" href="${contextPath}/security/main/module.htm?moduleId=3&url=${contextPath}/security/hdg/battery/battery_detail.htm?id=${(box.battery.id)!''}"  target="view_window2">
                                                 ${(box.battery.code)!''}
                                                </a>
                                            </span>
                                        </div>
                                        <div>
                                            <#if (box.statusName)??>
                                            ${(box.statusName)!}
                                            <#else>
                                            ${(box.battery.volume)!}<#if (box.battery.volume)??>%</#if>
                                            </#if>
                                        </div>
                                        <div style="display: <#if !((box.battery)??)>none</#if>">
                                            <#if box.isActive == 1>
                                                <#if box.battery?? && box.battery.isNormal == 1>
                                                    <span>${(box.power)!}<#if (box.power)??>W</#if>&nbsp;电流:<#if !(box.battery.electricity)??>0<#else>${(box.battery.electricity)/1000}</#if> A</span>
                                                    <img src="${app.imagePath}/img/inside/deng${((box.battery.upLineStatus==0&&box.isActive==1)?string('1',''))!}.png"/>
                                                </#if>
                                            </#if>
                                        </div>
                                        <div>
                                            <img src="${app.imagePath}/img/inside/${(box.isOpen==1)?string('opendoor','closedoor')}${((box.battery.upLineStatus==0&&box.isActive==1)?string('1',''))!}.png" />
                                        </div>
                                        <b class="boxselect" style="display: none;"><img src="${app.imagePath}/img/inside/selected.png"/></b>
                                        <#if (box.isActive) == 0>
                                            <a onclick="viewCause(${box.cabinetId},'${box.boxNum}')">禁用原因:</a>
                                            ${(box.forbiddenCause)!''}
                                        </#if>
                                        <#if box.isActive == 1>
                                            <#if box.battery?? && box.battery.isNormal == 0>
                                                <a onclick="viewAbnormalCause('${box.battery.id}')">异常标识原因</a>
                                            </#if>
                                        </#if>
                                    </div>
                                </#list>
                            </div>
                        </div>
                    </div>

                    <!--告警信息-->
                    <div class="c-detail-down1" id="alarm" style="display:none;overflow-y: auto;">
                    <#--                        <div class="alarm-one">-->
                    <#--                            柜子类型<select>-->
                    <#--                            <option>超水位</option>-->
                    <#--                            <option>超水位</option>-->
                    <#--                            <option>超水位</option>-->
                    <#--                        </select>-->
                    <#--                        </div>-->
                        <table id="page_table"></table>
                    </div>
                    <!--告警信息-->
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>
<script>
    var init=0;
    $('.cdd-tabs').on('click','span',(e)=>{
        if(e.target.dataset.id== 0){
        $('#base').show()
        $('#alarm').hide()
        $('.cdd-tabs span').attr('class','')
        $(e.target).attr('class','active')
    }else{
        $('#alarm').css("padding-left","");
        $('#base').hide()
        $('#alarm').show()
        $('.cdd-tabs span').attr('class','')
        $(e.target).attr('class','active')
        if(init==0){
            datagrid();
            init=1;
        }
    }
    })
</script>
<script type="text/javascript">
    $('.nav').hide();
    $('.main .index_con').css("left", 0);

    $('#back').click(function () {
        window.close();
    })

    $('#switch').click(function () {
        //选择换电柜
        App.dialog.show({
            css: 'width:1360px;height:522px;overflow:visible;',
            title: '选择换电柜',
            href: "${contextPath}/security/hdg/cabinet/switch_cabinet.htm",
            windowData: {
                ok: function(config) {
                    //切换柜子
                    window.location.href = "${contextPath}/security/hdg/cabinet/cabinet_detail.htm?id=" + config.cabinet.id;
                }
            },
            event: {
                onClose: function() {
                }
            }
        });
    });

    $('.catbox').on('click',(e)=>{
        $(e.currentTarget).find('.boxselect').toggle();
    })
</script>
<script>
    function open_box() {
        var catboxs=$('.boxlist .boxselect :visible').parent().parent();
        if(catboxs.length==0){
            $.messager.alert('提示消息', "请选择格口", 'error');
        }else {
            $.messager.confirm('提示信息', '确认开箱吗?', function (ok) {
                if (ok) {
                    open(catboxs,0);
                }
            });
        }
    }
    function open(catboxs,index) {
        var catbox=catboxs.eq(index);
        if(catbox.length>0) {
            var boxNum=catbox.attr("boxNum");
            var door=catbox.find('img').eq(1);
            var src=door.attr('src');
            // if(src.indexOf('opendoor')>=0){
            //     $.messager.alert('提示消息', '格口（' + boxNum + '）已开启', 'error');
            //     open(catboxs,++index);
            // }else {
            $.post('${contextPath}/security/hdg/cabinet_box/open_box.htm',
                    {
                        cabinetId: ${(entity.id)!''},
                        boxNum: boxNum
                    }, function (json) {
                        if(json.message.indexOf('成功')>=0){
                            // $.messager.show({title: '提示信息', msg: '格口（' + boxNum + '）操作成功', style: {}});
                            door.attr('src', src.replace('closedoor','opendoor'));
                        }
                        $.messager.alert('提示消息', '格口（' + boxNum + '）'+json.message, 'info');
                        open(catboxs,++index);
                    }, 'json');
        }
        // }
    }
    function completeOrder() {
        var catboxs=$('.boxlist .boxselect :visible').parent().parent();
        if(catboxs.length==0){
            $.messager.alert('提示消息', "请选择格口", 'error');
        }else {
            $.messager.confirm('提示信息', '是否结束订单?', function (ok) {
                if (ok) {
                    complete(catboxs,0);
                }
            });
        }
    }
    function complete(catboxs,index) {
        var catbox=catboxs.eq(index);
        if(catbox.length>0) {
            var boxNum = catbox.attr("boxNum");
            var orderId = catbox.attr("orderId");
            if (orderId == 0) {
                $.messager.alert('提示消息', '格口（' + boxNum + '）无订单', 'error');
                complete(catboxs, ++index);
            } else {
                $.post("${contextPath}/security/hdg/battery_order/complete.htm?id=" + orderId, function (json) {
                    if (json.success) {
                        $.messager.alert('提示消息', '格口（' + boxNum + '）操作成功', 'info');
                        // $.messager.show({title: '提示信息', msg: '格口（' + boxNum + '）操作成功', style: {}});
                        catbox.attr("orderId", 0);
                    } else {
                        $.messager.alert('提示消息', json.message, 'info');
                    }
                    complete(catboxs, ++index);
                }, 'json');
            }
        }
    }
    function doActive() {
        var catboxs=$('.boxlist .boxselect :visible').parent().parent();
        if(catboxs.length==0){
            $.messager.alert('提示消息', "请选择格口", 'error');
        }else {
            $.messager.confirm('提示信息', '是否启用?', function (ok) {
                if (ok) {
                    doActiv(catboxs,0)
                }
            });
        }
    }
    var needReload=0;
    function doActiv(catboxs,index) {
        var catbox=catboxs.eq(index);
        if(catbox.length>0) {
            var boxNum = catbox.attr("boxNum");
            var isActive = catbox.attr("isActive");
            if (isActive == 1) {
                $.messager.alert('提示消息', '格口（' + boxNum + '）已启用', 'error');
                doActiv(catboxs, ++index)
            } else {
                $.post('${contextPath}/security/hdg/cabinet_box/updateIsActive.htm',
                        {
                            cabinetId: ${(entity.id)!},
                            boxNum: boxNum,
                            isActive: 1
                        },
                        function (json) {
                            if (json.success) {
                                // $.messager.show({title: '提示信息', msg: '格口（' + boxNum + '）操作成功', style: {}});
                                needReload=1;
                            } else {
                                $.messager.show({title: '提示信息', msg: json.message, style: {}});
                            }
                            doActiv(catboxs, ++index)
                        }, 'json');
            }
        }
        if(needReload == 1 && index >= catboxs.length){
            reload();
        }
    }
    function doUnActive() {
        var catboxs=$('.boxlist .boxselect :visible').parent().parent();
        if(catboxs.length==0){
            $.messager.alert('提示消息', "请选择格口", 'error');
        }else {
            App.dialog.show({
                css: 'width:320px;height:185px;overflow:visible;',
                title: '禁用原因',
                href: "${contextPath}/security/hdg/cabinet_box/forbidden.htm"
            });
        }
    }

    function confirm() {
        var catboxs=$('.boxlist .boxselect :visible').parent().parent();
        doUnActiv(catboxs, 0);
    };
    function confirmAbnormal() {
        var catboxs=$('.boxlist .boxselect :visible').parent().parent();
        doToAbnormal(catboxs, 0);
    };
    function viewCause(cabinetId,boxNum) {
        App.dialog.show({
            css: 'width:320px;height:265px;overflow:visible;',
            title: '禁用原因',
            href: "${contextPath}/security/hdg/cabinet_box/cause.htm?cabinetId="+cabinetId+"&boxNum="+boxNum+""
        });
    }
    function viewAbnormalCause(batteryId) {

        App.dialog.show({
            css: 'width:320px;height:265px;overflow:visible;',
            title: '异常标识原因',
            href: "${contextPath}/security/hdg/cabinet_box/abnormal_cause.htm?batteryId=" + batteryId + ""
        });
    }
    function doUnActiv(catboxs,index) {
        var catbox=catboxs.eq(index);
        if(catbox.length>0) {
            var boxNum = catbox.attr("boxNum");
            var isActive=catbox.attr("isActive");
            var forbiddenCause = $('#forbidden_cause').val();
            // var boxStatus = catbox.attr("boxStatus");
            // if (boxStatus != 1) {
            //     $.messager.alert('提示消息', '格口（'+boxNum+'）未空闲，不可禁用', 'error');
            //     doUnActiv(catboxs,++index);
            // }else
            if(isActive == 0) {
                $.messager.alert('提示消息', '格口（' + boxNum + '）已禁用', 'info');
                doUnActiv(catboxs,++index);
            }else {
                if(forbiddenCause==null || forbiddenCause == ""||forbiddenCause ==undefined){
                    $.messager.alert('提示消息', '请输入禁用原因', 'info');
                    return;
                }
                $.post('${contextPath}/security/hdg/cabinet_box/updateIsActive.htm',
                        {
                            cabinetId: ${(entity.id)!},
                            boxNum: boxNum,
                            isActive: 0,
                            forbiddenCause: forbiddenCause
                        },
                        function (json) {
                            if (json.success) {
                                // $.messager.show({title: '提示信息', msg: '格口（' + boxNum + '）操作成功', style: {}});
                                needReload=1;
                            } else {
                                $.messager.show({title: '提示信息', msg: json.message, style: {}});
                            }
                            doUnActiv(catboxs,++index);
                        }, 'json');
            }
        };
        if(needReload == 1 && index >= catboxs.length){
            win.find('button.close').click(function() {
                win.window('close');
            });
            reload();
        }
    }
    function doNormal() {
        var catboxs=$('.boxlist .boxselect :visible').parent().parent();
        if(catboxs.length==0){
            $.messager.alert('提示消息', "请选择格口", 'error');
        }else {
            $.messager.confirm('提示信息', '是否解除异常?', function (ok) {
                if (ok) {
                    doToNormal(catboxs,0)
                }
            });
        }
    }

    function doToNormal(catboxs,index) {
        var catbox=catboxs.eq(index);
        if(catbox.length>0) {
            var batteryId = catbox.attr("batteryId");
            if(batteryId != null && batteryId != '' ) {
                var isNormal = catbox.attr("isNormal");
                if (isNormal == 1) {
                    $.messager.alert('提示消息', '电池（' + batteryId + '）已解除异常', 'error');
                    doToNormal(catboxs, ++index)
                } else {
                    $.post('${contextPath}/security/hdg/cabinet_box/update_is_normal.htm',
                            {
                                id: batteryId,
                                isNormal: 1
                            },
                            function (json) {
                                if (json.success) {
                                    needReload = 1;
                                } else {
                                    $.messager.show({title: '提示信息', msg: json.message, style: {}});
                                }
                                doToNormal(catboxs, ++index);
                            }, 'json');
                }
            } else{
                $.messager.alert('提示消息', '无电池，无法解除异常', 'info');
                needReload=1;
                doToNormal(catboxs,++index);
            }
        };
        if(needReload == 1 && index >= catboxs.length){
            reload();
        }
    }
    function doToAbnormal(catboxs,index) {
        var catbox=catboxs.eq(index);
        if(catbox.length>0) {
            var batteryId = catbox.attr("batteryId");
            if(batteryId != null && batteryId != '' ) {
                var isNormal = catbox.attr("isNormal");
                var abnormalCause = $('#abnormal_cause').val();
                if(isNormal == 0) {
                    $.messager.alert('提示消息', '电池（' + batteryId + '）已标识为异常', 'info');
                    doToAbnormal(catboxs,++index);
                }else {
                    if(abnormalCause==null || abnormalCause == ""||abnormalCause ==undefined){
                        $.messager.alert('提示消息', '请输入异常原因', 'info');
                        return;
                    }
                    $.post('${contextPath}/security/hdg/cabinet_box/update_is_normal.htm',
                            {
                                id: batteryId,
                                isNormal: 0,
                                abnormalCause: abnormalCause
                            },
                            function (json) {
                                if (json.success) {
                                    needReload=1;
                                } else {
                                    $.messager.show({title: '提示信息', msg: json.message, style: {}});
                                }
                                doToAbnormal(catboxs,++index);
                            }, 'json');
                }
            } else{
                $.messager.alert('提示消息', '无电池，无法标识为异常', 'info');
                needReload=1;
                doToAbnormal(catboxs,++index);
            }
        };
        if(needReload == 1 && index >= catboxs.length){
            win.find('button.close').click(function() {
                win.window('close');
            });
            reload();
        }
    }

    function doAbnormal() {
        var catboxs=$('.boxlist .boxselect :visible').parent().parent();
        if(catboxs.length==0){
            $.messager.alert('提示消息', "请选择格口", 'error');
        }else {
            App.dialog.show({
                css: 'width:320px;height:185px;overflow:visible;',
                title: '异常标识原因',
                href: "${contextPath}/security/hdg/cabinet_box/abnormal.htm"
            });
        }
    }

    function edit_up_line_status() {
        var catboxs=$('.boxlist .boxselect :visible').parent().parent();
        if(catboxs.length==0){
            $.messager.alert('提示消息', "请选择格口", 'error');
        }else {
            $.messager.confirm('提示信息', '确认电池上线?', function (ok) {
                if (ok) {
                    App.dialog.show({
                        css: 'width:320px;height:185px;overflow:visible;',
                        title: '电池上线',
                        href: "${contextPath}/security/hdg/cabinet_box/edit_up_line_status.htm"
                    });
                }
            });
        }
    }
    function confirmUpLineStatus() {
        var catboxs=$('.boxlist .boxselect :visible').parent().parent();
        doToUpLine(catboxs, 0);
    };
    function doToUpLine(catboxs,index) {
        var catbox=catboxs.eq(index);
        if(catbox.length>0) {
            var batteryId = catbox.attr("batteryId");
            if(batteryId != null && batteryId != '' ) {
                var upLineStatus = catbox.attr("upLineStatus");
                var agentId = $('#batteryAgentId').combotree('getValue');
                if(upLineStatus == 1) {
                    $.messager.alert('提示消息', '电池（' + batteryId + '）已上线', 'info');
                    doToUpLine(catboxs,++index);
                }else {
                    $.post('${contextPath}/security/hdg/cabinet_box/update_up_line_status.htm',
                            {
                                id: batteryId,
                                agentId: agentId
                            },
                            function (json) {
                                if (json.success) {
                                    needReload=1;
                                } else {
                                    $.messager.show({title: '提示信息', msg: json.message, style: {}});
                                }
                                doToUpLine(catboxs,++index);
                            }, 'json');
                }
            } else{
                $.messager.alert('提示消息', '无电池，无法上线', 'info');
                needReload=1;
                doToUpLine(catboxs,++index);
            }
        };
        if(needReload == 1 && index >= catboxs.length){
            win.find('button.close').click(function() {
                win.window('close');
            });
            reload();
        }
    }



    function reload() {
        window.location.href='${contextPath}/security/main/module.htm?moduleId=3&url=${contextPath}/security/hdg/cabinet/cabinet_detail.htm?id=${(entity.id)!}';
    }
    function battery_detail_alert(batteryId) {
        window.location.href='${contextPath}/security/main/module.htm?moduleId=3&url=${contextPath}/security/hdg/battery/battery_detail.htm?id=' + batteryId;
    }
    function addCss(obj) {
        obj.css("color","blue");
    }
    function removeCss(obj) {
        obj.css("color","#FFFFFF");
    }
    function edit() {
        App.dialog.show({
            css: 'width:830px;height:620px;overflow:visible;',
            title: '修改',
            href: "${contextPath}/security/hdg/cabinet/edit.htm?id=${entity.id}",
            event: {
                onClose: function () {
                    reload();
                },
                onLoad: function () {
                }
            }
        });
    }
</script>
