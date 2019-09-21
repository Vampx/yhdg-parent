<@app.html>
    <@app.head>
    <script>
        Date.prototype.Format = function (fmt) { //author: meizz
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        };
        function query() {
            var startDate = $("#startDate").datebox('getValue');
            var endDate = $("#endDate").datebox('getValue');

            if(startDate == '') {
                $.messager.alert('提示消息', '开始日期不能为空', 'info');
                return;
            }

            if(endDate == '') {
                $.messager.alert('提示消息', '结束日期不能为空', 'info');
                return;
            }

            if(startDate > endDate) {
                $.messager.alert('提示消息', '结束日期必须大于等于开始日期', 'info');
                return;
            }

            $.ajax({
                type: 'POST',
                url: '${contextPath}/security/hdg/cabinet_monitor/load_data.htm',
                dataType: 'json',
                data: {
                    agentId: $('#agent_id').combotree('getValue'),
                    cabinetId: $("input[name='cabinetId']").val(),
                    startDate: $("#startDate").datebox('getValue'),
                    endDate: $("#endDate").datebox('getValue')
                },
                success: function (json) {
                    if (json.success) {
                        var data = json.data;
                        var turnoverRate = 0, putRate = 0;
                        if (data.boxCount != 0) {
                            turnoverRate = parseInt(data.orderCount / data.boxCount * 100);
                            putRate = parseInt(data.putCount / data.boxCount * 100);
                        }
                        $("#turnoverRate").empty();
                        $("#turnoverRate").append(turnoverRate + "%");
                        $("#putRate").empty();
                        $("#putRate").append(putRate + "%");
                        Chart1(data.dateList, data.orderCountList);
                        Chart2(data.orderCount, data.boxCount);
                        Chart3(data.putCount, data.boxCount);
                        Chart4(data.growthRate, data.dateList, data.growthRateList);
                    }
                }
            });
        }
        $(function () {
            var date = new Date();
            date.setDate(date.getDate() - 1);
            $("#endDate").datebox('setValue', date.Format("yyyy-MM-dd"));
            date.setDate(date.getDate() - 6);
            $("#startDate").datebox('setValue', date.Format("yyyy-MM-dd"));

            query();
            $(".subnav .subnav_list ul .dropdown").click(function () {
                if (!$(this).hasClass("show")) {
                    if ($(this).siblings("li").find("a").hasClass("selected")) {
                        $(this).siblings("li").find("a").parent(".dropdown").addClass("selected")
                    }
                    $(this).addClass("show").siblings().removeClass("show")
                }
                if ($(this).hasClass("selected")) {
                    $(this).addClass("show").removeClass("selected");
                }
            });
        });
        function selectCabinet() {
            var agentId = $('#agent_id').combotree('getValue');
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择换电柜',
                href: "${contextPath}/security/hdg/cabinet/select_cabinets.htm?agentId="+agentId,
                windowData: {
                    ok: function (config) {
                        $("input[name='cabinetId']").val(config.cabinet.id);
                        $("input[name='cabinetName']").val(config.cabinet.cabinetName);
                    }
                },
                event: {
                    onClose: function () {
                    }
                }
            });
        }
        function Chart1(dateList, orderCount) {
            var chart = echarts.init(document.getElementById('chart1'));
            chart.setOption({
                tooltip: {
                    trigger: 'axis'
                },
                grid: {
                    top: 20,
                    left: 5,
                    right: 0,
                    bottom: 10,
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    axisLabel: {
                        textStyle: {
                            color: '#888',
                            fontFamily: '宋体'
                        }
                    },
                    axisLine: {
                        show: true,
                        lineStyle: {
                            color: '#ddd'
                        }
                    },
                    axisTick: {
                        show: false
                    },
                    data: dateList
                },
                yAxis: {
                    splitLine: {
                        show: true,
                        lineStyle: {
                            color: '#ddd'
                        }
                    },
                    axisLine: {
                        show: true,
                        lineStyle: {
                            color: '#ddd'
                        }
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        textStyle: {
                            color: '#888'
                        }
                    }
                },
                series: [
                    {
                        name: '当日换电',
                        type: 'line',
                        itemStyle: {
                            normal: {
                                color: '#74ddd8'
                            }
                        },
                        data: orderCount
                    }
                ]
            });
        }

        function Chart2(orderCount, boxCount) {
            var legendData = ['换电次数:' + orderCount, '设备格口数:' + boxCount];
            var seriesData = [{name: '换电次数:' + orderCount, value: orderCount}, {
                name: '设备格口数:' + boxCount,
                value: boxCount
            }];
            var chart = echarts.init(document.getElementById('chart2'));
            chart.setOption({
                legend: {
                    orient: 'vertical',
                    right: 0,
                    bottom: 'center',
                    itemWidth: 10,
                    itemHeight: 10,
                    itemGap: 30,
                    textStyle: {
                        color: '#333',
                        fontSize: 14
                    },
                    data: legendData
                },
                series: [
                    {
                        name: '周转率',
                        type: 'pie',
                        radius: ['60%', '80%'],
                        center: ['23%', '50%'],
                        color: ['#ff71a7', '#74ddd8'],
                        label: {
                            normal: {
                                show: false
                            }
                        },
                        data: seriesData
                    }
                ]
            });
        }
        function Chart3(putCount, boxCount) {
            var legendData = ['投电次数:' + putCount, '设备格口数:' + boxCount];
            var seriesData = [{name: '投电次数:' + putCount, value: putCount}, {
                name: '设备格口数:' + boxCount,
                value: boxCount
            }];
            var chart = echarts.init(document.getElementById('chart3'));
            chart.setOption({
                legend: {
                    orient: 'vertical',
                    right: 0,
                    bottom: 'center',
                    itemWidth: 10,
                    itemHeight: 10,
                    itemGap: 30,
                    textStyle: {
                        color: '#333',
                        fontSize: 14
                    },
                    data: legendData
                },
                series: [
                    {
                        name: '投电率',
                        type: 'pie',
                        radius: ['60%', '80%'],
                        center: ['23%', '50%'],
                        color: ['#6874c6', '#74ddd8'],
                        label: {
                            normal: {
                                show: false
                            }
                        },
                        data: seriesData
                    }
                ]
            });
        }
        function Chart4(growthRate, dateList, growthRateList) {
            var chart = echarts.init(document.getElementById('chart4'));
            chart.setOption({
                title: {
                    text: '换电增长率同期增长' + growthRate + '%',
                    left: 'center',
                    bottom: 0,
                    textStyle: {
                        fontSize: 16
                    }
                },
                grid: {
                    top: 0,
                    left: -40,
                    right: -20,
                    bottom: 20,
                    containLabel: true
                },
                xAxis: {
                    show: false,
                    type: 'category',
                    data: dateList
                },
                yAxis: {
                    show: false
                },
                series: [
                    {
                        name: '当日换电',
                        type: 'line',
                        itemStyle: {
                            normal: {
                                color: '#1775ef'
                            }
                        },
                        areaStyle: {
                            normal: {
                                color: '#dceafd'
                            }
                        },
                        data: growthRateList
                    }
                ]
            });
        }
    </script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>
            <div class="content hdg_chart">
                <div class="panel search hdg_chart_head">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="false"
                                       style="height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                   query();
                                }
                            "
                                        >
                            </td>
                            <td align="right">设备名称：</td>
                            <td><input onclick="selectCabinet()" type="text" class="text" name="cabinetName"/><input
                                    type="hidden" name="cabinetId"/></td>
                            <td align="right" width="60">时间：</td>
                            <td>
                                <input class="easyui-datebox" id="startDate" editable="false"
                                       type="text"
                                       style="width:150px;height:27px;" required="true">
                                至
                                <input class="easyui-datebox" type="text" id="endDate" editable="false"
                                       style="width:150px;height:27px;" required="true">
                            </td>
                        </tr>
                    </table>
                </div>

                <div class="panel hdg_chart_top">
                    <div class="toolbar clearfix">
                        <h3>换电记录</h3>
                    </div>
                    <div id="chart1">

                    </div>
                </div>
                <div class="row hdg_chart_bottom">
                    <div class="col-4">
                        <div class="panel">
                            <div class="chart" id="chart2"></div>
                            <div class="text">
                                <h3 id="turnoverRate"></h3>

                                <p>周转率</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="panel">
                            <div class="chart" id="chart3"></div>
                            <div class="text">
                                <h3 id="putRate"></h3>

                                <p>投电率</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="panel">
                            <div class="chart" id="chart4"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>