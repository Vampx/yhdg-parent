<@app.html>
    <@app.head>
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
            <div class="content index_con">
                <div class="index_total_data">
                    <ul>
                        <li>
                            <div class="box">
                                <div class="pic"><img src="${app.imagePath}/index_total_hdg.jpg"></div>
                                <div class="txt">
                                    <h5>总站点数</h5>

                                    <h3>${(cabinetCount)!'0'}
                                        <small>站</small>
                                    </h3>
                                    <p>今日+${(todayCabinetCount)!'0'} 昨日+${(yesterdayCabinetCount)!'0'}</p>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="box">
                                <div class="pic"><img src="${app.imagePath}/index_total_user.jpg"/></div>
                                <div class="txt">
                                    <h5>总用户数</h5>

                                    <h3>${(customerCount)!'0'}
                                        <small>人</small>
                                    </h3>
                                    <p>今日+${(todayCustomerCount)!'0'} 昨日+${(yesterdayCustomerCount)!'0'}</p>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="box">
                                <div class="pic"><img src="${app.imagePath}/index_total_order.jpg"></div>
                                <div class="txt">
                                    <h5>总订单数</h5>

                                    <h3>${(batteryOrderCount)!'0'}
                                        <small>条</small>
                                    </h3>
                                    <p>今日+${(todayBatteryOrderCount)!'0'} 昨日+${(yesterdayBatteryOrderCount)!'0'}</p>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="panel index_income_wrap">
                    <div class="toolbar clearfix">
                        <h3>平台收入</h3>
                    </div>
                    <div class="index_income" id="total_income">
                    </div>
                </div>
                <div class="panel index_income_details_wrap">
                    <div class="toolbar clearfix">
                        <h3>收入明细</h3>
                    </div>
                    <div class="index_income_details">
                        <ul class="income">
                            <li>
                                <span class="name">押金收入</span>
                                <span class="price">￥<#if foregiftMoney??>${(foregiftMoney/100)?string("0.00")}<#else>
                                    0.00</#if></span>
                                <span class="bar"><span class="inner_bar"
                                                        style="width:  <#if incomeRatio?? && incomeRatio!=0>${foregiftMoney/incomeRatio*100}<#else>0</#if>%;"></span></span>
                            </li>
                            <li>
                                <span class="name">充值收入</span>
                                <span class="price">￥<#if depositMoney??>${(depositMoney/100)?string("0.00")}<#else>
                                    0.00</#if></span>
                                <span class="bar"><span class="inner_bar"
                                                        style="width: <#if incomeRatio?? && incomeRatio!=0>${depositMoney/incomeRatio*100}<#else>0</#if>%;"></span></span>
                            </li>
                            <li>
                                <span class="name">套餐收入</span>
                                <span class="price">￥<#if packetPeriodMoney??>${(packetPeriodMoney/100)?string("0.00")}<#else>
                                    0.00</#if></span>
                                <span class="bar"><span class="inner_bar"
                                                        style="width: <#if incomeRatio?? && incomeRatio!=0>${packetPeriodMoney/incomeRatio*100}<#else>0</#if>%;"></span></span>
                            </li>
                            <li>
                                <span class="name">换电收入</span>
                                <span class="price">￥<#if batteryOrderMoney??>${(batteryOrderMoney/100)?string("0.00")}<#else>
                                    0.00</#if></span>
                                <span class="bar"><span class="inner_bar"
                                                        style="width: <#if incomeRatio?? && incomeRatio!=0>${batteryOrderMoney/incomeRatio*100}<#else>0</#if>%;"></span></span>
                            </li>
                        </ul>
                        <ul class="expenses">
                            <li>
                                <span class="name">押金退款支出</span>
                                <span class="price">￥<#if foregiftOrderRefundMoney??>${(foregiftOrderRefundMoney/100)?string("0.00")}<#else>
                                    0.00</#if></span>
                                <span class="bar"><span class="inner_bar"
                                                        style="width:  <#if refundRatio?? && refundRatio!=0>${foregiftOrderRefundMoney/refundRatio*100}<#else>0</#if>%;"></span></span>
                            </li>
                            <li>
                                <span class="name">套餐退款支出</span>
                                <span class="price">￥<#if packetPeriodRefundMoney??>${(packetPeriodRefundMoney/100)?string("0.00")}<#else>
                                    0.00</#if></span>
                                <span class="bar"><span class="inner_bar"
                                                        style="width:  <#if refundRatio?? && refundRatio!=0>${packetPeriodRefundMoney/refundRatio*100}<#else>0</#if>%;"></span></span>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="panel index_fault_wrap">
                    <div class="toolbar clearfix">
                        <h3>故障信息</h3>
                    </div>
                    <div class="index_fault">
                        <ul>
                            <li>
                                <div class="box"
                                     onclick="window.open('${contextPath}/security/main/module.htm?moduleId=2&url=${contextPath}/security/hdg/fault_feedback/index.htm?faultType=2','_self')">
                                    <div class="pic"><img src="${app.imagePath}/index_fault_battery.png"></div>
                                    <div class="txt">
                                        <h3>${(batteryFaultFeedbackCount)!'0'}</h3>

                                        <p>电池故障数</p>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div class="box" onclick="window.open('${contextPath}/security/main/module.htm?moduleId=2&url=${contextPath}/security/hdg/fault_feedback/index.htm?faultType=1','_self')">
                                    <div class="pic"><img src="${app.imagePath}/index_fault_hdg.png"></div>
                                    <div class="txt">
                                        <h3>${(cabinetFaultFeedbackCount)!'0'}</h3>

                                        <p>柜子故障数</p>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div class="box" onclick="window.open('${contextPath}/security/main/module.htm?moduleId=2&url=${contextPath}/security/basic/feedback/index.htm?today=1','_self')">
                                    <div class="pic"><img src="${app.imagePath}/index_fault_suggest.png"></div>
                                    <div class="txt">
                                        <h3>${(todayFeedbackCount)!'0'}</h3>

                                        <p>今日建议数</p>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div class="box" onclick="window.open('${contextPath}/security/main/module.htm?moduleId=2&url=${contextPath}/security/basic/feedback/index.htm','_self')">
                                    <div class="pic"><img src="${app.imagePath}/index_fault_total_suggest.png"></div>
                                    <div class="txt">
                                        <h3>${(feedbackCount)!'0'}</h3>

                                        <p>总建议数</p>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>
<script type="text/javascript">

    //故障统计
    function totalIncome() {
        $('.nav').hide();
        $('.main .index_con').css("left", 0);
        var chart = echarts.init(document.getElementById('total_income'));
        chart.setOption({
            tooltip: {
                trigger: 'item',
                padding: [5, 10],
                formatter: "{b}:￥{c} ({d}%)"
            },
            legend: {
                left: 'center',
                bottom: 0,
                itemWidth: 10,
                itemHeight: 10,
                textStyle: {
                    color: '#333'
                },
                data: ['总收入', '总支出']

            },
            series: [
                {
                    name: '总收入',
                    type: 'pie',
                    radius: ['20%', '55%'],
                    center: ['50%', '50%'],
                    color: ['#40bbea', '#ff6767'],
                    label: {
                        normal: {
                            formatter: '{b}:￥{c} ',
                            textStyle: {
                                fontSize: 14
                            }
                        }
                    },
                    data: [
                        {name: '${'总收入'}', value: (${(sumMoney)!'0'}/100).toFixed(2) },
                        {name: '${'总支出'}', value: (${(refundTotalMoney)!'0'}/100).toFixed(2)}
                    ]
                }
            ]
        });
    }

    totalIncome();
</script>