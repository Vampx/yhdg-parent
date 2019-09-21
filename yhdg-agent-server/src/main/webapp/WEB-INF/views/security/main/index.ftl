<@app.html>
    <@app.head>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <div class="left_bar">
                <div class="nav">
                </div>
            </div>
            <div class="content index_con">
                <div class="panel" style="margin-bottom:0">
                    <div class="toolbar clearfix">
                        <div class="float_right"></div>
                        <h3>运营商信息</h3>
                    </div>
                    <div class="index_ent">
                        <ul class="clearfix">
                            <li>
                                <table>
                                    <tr>
                                        <td align="right" width="70">名称：</td>
                                        <td>${(Session['SESSION_KEY_USER'].agentName)!''}</td>
                                    </tr>
                                    <tr>
                                        <td align="right" width="70">备注：</td>
                                        <td>${(Session['SESSION_KEY_USER'].agentMemo)!''}</td>
                                    </tr>
                                    <tr>
                                        <td align="right"></td>
                                        <td></td>
                                    </tr>
                                </table>
                            </li>
                            <li>
                                <table>
                                    <tr>
                                        <td align="right">联系电话：</td>
                                        <td>${(Session['SESSION_KEY_USER'].agentTel)!''}</td>
                                    </tr>
                                    <tr>
                                        <td align="right"></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td align="right"></td>
                                        <td></td>
                                    </tr>
                                </table>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="index_total_data">
                    <ul>
                        <li>
                            <div class="box">
                                <div class="pic"><img src="${app.imagePath}/index_total_user.jpg"/></div>
                                <div class="txt">
                                    <h5>活动用户数</h5>

                                    <h3>${(todayActiveCustomerCount)!'0'}
                                        <small>人</small>
                                    </h3>
                                    <p>昨日+${(yesterdayActiveCustomerCount)!'0'}</p>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="box">
                                <div class="pic"><img src="${app.imagePath}/index_total_order.jpg"></div>
                                <div class="txt">
                                    <h5>今日订单数</h5>
                                    <h3>${(todayBatteryOrderCount)!'0'}
                                        <small>条</small>
                                    </h3>
                                    <p> 昨日+${(yesterdayBatteryOrderCount)!'0'}</p>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="box">
                                <div class="pic"><img src="${app.imagePath}/index_total_hdg.jpg"></div>
                                <div class="txt">
                                    <h5>总站点数</h5>
                                    <h3>${(cabinetCount)!'0'}
                                        <small>站</small>
                                    </h3>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="panel index_income_wrap">
                    <div class="toolbar clearfix">
                        <h3>今日收入</h3>
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
                                <span class="name">套餐收入</span>
                                <span class="price">￥<#if packetPeriodMoney??>${(packetPeriodMoney/100)?string("0.00")}<#else>0.00</#if></span>
                                <span class="bar"><span class="inner_bar" style="width: <#if incomeRatio??&& incomeRatio!=0>${packetPeriodMoney/incomeRatio*100}<#else>0</#if>%;"></span></span>
                            </li>
                            <li>
                                <span class="name">换电收入</span>
                                <span class="price">￥<#if batteryOrderMoney??>${(batteryOrderMoney/100)?string("0.00")}<#else>0.00</#if></span>
                                <span class="bar"><span class="inner_bar" style="width: <#if incomeRatio??&& incomeRatio!=0>${batteryOrderMoney/incomeRatio*100}<#else>0</#if>%;"></span></span>
                            </li>
                        </ul>
                        <ul class="expenses">
                            <li>
                                <span class="name">套餐退款支出</span>
                                <span class="price">￥<#if packetPeriodRefundMoney??>${(packetPeriodRefundMoney/100)?string("0.00")}<#else>0.00</#if></span>
                                <span class="bar"><span class="inner_bar" style="width: <#if refundRatio??&& refundRatio!=0>${packetPeriodRefundMoney/refundRatio*100}<#else>0</#if>%;"></span></span>
                            </li>
                            <li>
                                <span class="name">换电退款支出</span>
                                <span class="price">￥<#if batteryOrderRefundMoney??>${(batteryOrderRefundMoney/100)?string("0.00")}<#else>0.00</#if></span>
                                <span class="bar"><span class="inner_bar" style="width: <#if refundRatio??&& refundRatio!=0>${batteryOrderRefundMoney/refundRatio*100}<#else>0</#if>%;"></span></span>
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
                data: ['收入', '支出']

            },
            series: [
                {
                    name: '收入',
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
                        {name: '${'收入'}', value: (${(todayMoney)!'0'}/100).toFixed(2) },
                        {name: '${'支出'}', value: (${(refundTodayMoney)!'0'}/100).toFixed(2)}
                    ]
                }
            ]
        });
    }
    totalIncome();
</script>