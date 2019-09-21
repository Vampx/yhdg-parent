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
            text: '电池${(batteryId)!''}——${(oneDay)!''}——电压/电流——时间'
        },
        grid: {
            bottom: 80
        },
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
        tooltip : {
            //提示框组件。
            trigger: 'axis',
            //坐标轴指示器配置项。
            //在坐标轴上显示当前选中点的坐标
            axisPointer: {
                type: 'cross'
            }
        },
        legend: {
            data:['电压','电流'],
            x: 'left'
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
        xAxis: {
            type: 'category',
            data: [<#list timeList as o>
                "${(o)?string('yyyy-MM-dd HH:mm:ss')}"<#if o_has_next>,</#if>
            </#list>].map(function (str) {
                return str.replace(' ', '\n')
            }),
            boundaryGap: false,
            axisTick: {
                show: false
            },
            axisLine: {onZero: false},
            axisLine: {
                lineStyle: {
                    color: '#609ee9'
                }
            },
            axisPointer: {
                type: 'line'
            }
        },
        yAxis: [
            {
                name: '电压(V)',
                type: 'value',
                splitLine: {
                    show: false
                },
                axisLine: {
                    lineStyle: {
                        color: '#f73f38'
                    }
                },
                axisTick: {            // 坐标轴小标记
                    show: false,       // 属性show控制显示与否，默认不显示
                    inside : false,    // 控制小标记是否在grid里
                    length :5,         // 属性length控制线长
                    lineStyle: {       // 属性lineStyle控制线条样式
                        color: '#f73f38',
                        width: 1
                    }
                },
                axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
                    show: true,
                    rotate: 0,
                    margin: 8,
                    // formatter: null,
                    textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                        fontSize: 12
                    },
                    formatter: '{value} V'
                },
                axisPointer: {
                    type: 'line'
                }
            },
            {
                name: '电流(A)',
                nameLocation: 'start',
                type: 'value',
                splitLine: {
                    show: false
                },
                inverse: true,
                axisLine: {
                    lineStyle: {
                        color: '#10650c'
                    }
                },
                axisTick: {            // 坐标轴小标记
                    show: false,       // 属性show控制显示与否，默认不显示
                    inside : false,    // 控制小标记是否在grid里
                    length :5,         // 属性length控制线长
                    alignWithLabel: true,
                    lineStyle: {       // 属性lineStyle控制线条样式
                        color: '#10650c',
                        width: 1
                    }
                },
                axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
                    show: true,
                    rotate: 0,
                    margin: 8,
                    // formatter: null,
                    textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                        fontSize: 12
                    },
                    formatter: '{value} A'
                },
                axisPointer: {
                    type: 'line'
                }
            }
        ],
        series: [
            {
                name:'电压',
                type:'line',
                animation: false,
                smooth: true,
                areaStyle: {
                },
                lineStyle: {
                    width: 1
                },
                data: [ <#list voltageList as o>
                    {
                        value: ${(o/100)!''}
                    }<#if o_has_next>,</#if>
                </#list>],
                itemStyle: {
                    normal: {
                        color: '#f73f38'
                    }
                },
                markLine: {
                    symbol: ['none', 'none'],
                    data: [{
                        yAxis: 20
                    }, {
                        yAxis: 65
                    }],
                    label: {
                        normal:{
                            formatter:'警戒电压          ',
                            position:'start'
                        }
                    }
                }
            },
            {
                name:'电流',
                type:'line',
                yAxisIndex:1,
                animation: false,
                smooth: true,
                areaStyle: {
                },
                lineStyle: {
                    width: 1
                },
                data: [ <#list electricityList as o>
                    {
                        value: ${(o/100)!''}
                    }<#if o_has_next>,</#if>
                </#list>],
                itemStyle: {
                    normal: {
                        color: '#10650c'
                    }
                },
                markLine: {
                    symbol: ['none', 'none'],
                    data: [{
                        yAxis: 20
                    }, {
                        yAxis: 65
                    }],
                    label: {
                        normal:{
                            formatter:'          警戒电流',
                            position:'end'
                        }
                    }
                }
            }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
</script>
</body>
</html>