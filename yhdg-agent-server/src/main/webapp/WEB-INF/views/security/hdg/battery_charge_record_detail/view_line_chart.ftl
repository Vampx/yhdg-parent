<@app.html>
    <@app.head>
    </@app.head>
<body>
<div id="map_echarts" style="width:90%;height:450px;"></div>

<script type="text/javascript">

    require.config({
        paths: {
            echarts: '${app.toolPath}/echarts-2.2.7'
        }
    });
    require(
            [
                'echarts',
                'echarts/chart/line'

            ],
            function (ec) {
                var myChart = ec.init(document.getElementById('map_echarts'));
                var option = {
                    tooltip : {
                        trigger: 'axis'
                    },
                    calculable : true,
                    xAxis : [
                        {
                            type : 'category',
                            boundaryGap : false,
                            data : [
                                <#if list??>
                                    <#list list as o>
                                        "${(o.reportTime)?string('HH:mm:ss')}"<#if o_has_next>,</#if>
                                    </#list>
                                </#if>
                            ]
                        }
                    ],
                    yAxis : [
                        {
                            type : 'value'
                        }
                    ],
                    series : [

                        {
                            name:'功率',
                            type:'line',
                            stack: '总量',
                            data:[
                                <#if list??>
                                    <#list list as o>
                                        {
                                            value: ${o.currentPower}
                                        }<#if o_has_next>,</#if>
                                    </#list>
                                </#if>
                            ]
                        }
                    ]
                };

                myChart.setOption(option, true);

            }
    );
</script>
</body>
</@app.html>