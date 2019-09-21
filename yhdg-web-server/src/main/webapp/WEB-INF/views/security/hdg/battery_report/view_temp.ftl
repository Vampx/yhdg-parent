<html>
<@app.head>
</@app.head>
<body>
<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
<div id="main" style="width: 1000px;height:500px;"></div>
<script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));

    // 指定图表的配置项和数据
    var option = {
        title: {
            x: 'center',
            text: '电池${(batteryId)!''}——${(oneDay)!''}——温度/时间'
        },
        grid: {
            bottom: 80
        },
        tooltip: {
            trigger: 'axis',
            //坐标轴指示器配置项。
            //在坐标轴上显示当前选中点的坐标
            axisPointer: {
                type: 'cross'
            }
        },
        legend: {
            x: 'left',
            data:[<#list numList as n>
                "温度${n+1}"<#if n_has_next>,</#if>
            </#list>]
        },
        //dataZoom 组件 用于区域缩放，从而能自由关注细节的数据信息，或者概览数据整体，或者去除离群点的影响。
        dataZoom: [
            {
                show: true,
                realtime: true,
                start: 40,
                end: 60
            },
            {
                //内置型数据区域缩放组件（dataZoomInside）：内置于坐标系中，使用户可以在坐标系上通过鼠标拖拽、鼠标滚轮、手指滑动（触屏上）来缩放或漫游坐标系。
                type: 'inside',
                realtime: true,
                start: 40,
                end: 60
            }
        ],
        toolbox: {
            show: true,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView: {readOnly: false},
                magicType: {type: ['line', 'bar']},
                restore: {},
                saveAsImage: {}
            }
        },
        xAxis:  {
            type: 'category',
            name: '时间',
            data: [<#list listReport as o>
                "${(o.createTime)?string('yyyy-MM-dd HH:mm:ss')}"<#if o_has_next>,</#if>
            </#list>].map(function (str) {
                return str.replace(' ', '\n')
            }),
            boundaryGap: false,
            axisTick: {
                show: false
            },
            axisLine: {
                lineStyle: {
                    color: '#609ee9'
                }
            },
            axisLabel: {
                margin: 10,
                textStyle: {
                    fontSize: 12
                }
            },
            axisPointer: {
                type: 'line'
            }
        },
        yAxis: {
            type: 'value',
            name: '温度',
            splitLine: {
                lineStyle: {
                    color: ['#D4DFF5']
                }
            },
            axisTick: {
                show: false
            },
            axisLine: {
                lineStyle: {
                    color: '#609ee9'
                }
            },
            axisLabel: {
                margin: 10,
                textStyle: {
                    fontSize: 12
                },
                formatter: '{value} °C'
            },
            axisPointer: {
                type: 'line'
            }
        },
        series: [
        <#list newTempList as tempList>
            {
                name:'温度${tempList_index+1}',
                type:'line',
                data:[ <#list tempList as o>
                    {
                        value: ${(o)!''}
                    }<#if o_has_next>,</#if>
                </#list>],
            }<#if tempList_has_next>,</#if>
        </#list>
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
</script>
</body>
</html>