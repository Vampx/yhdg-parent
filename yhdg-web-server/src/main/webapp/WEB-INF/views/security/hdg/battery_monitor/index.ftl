<@app.html>
    <@app.head>
    <script>
        function detail(id) {
            var mapFrame = document.getElementById('map_frame');
            var doc = mapFrame.contentDocument || mapFrame.document;
            var childWindow = mapFrame.contentWindow || mapFrame;
            childWindow.detail(id);
        }

        window.view = function (id) {
            App.dialog.show({
                css: 'width:780px;height:540px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery/view.htm?id=" + id
            });
        };

        function reload() {
            var agentId = $('#agent_id').combotree('getValue');
            window.location.replace(window.location.pathname + "?agentId=" + agentId);
        }

        function query(id, agentId) {
            $.post("${contextPath}/security/hdg/battery_monitor/query.htm?id=" + id + "&agentId=" + agentId, {
                        page: 1,
                        rows: 20
                    },
                    function (json) {
                        if (json.success) {
                            var list = json.rows;
                            var markers = new Array();
                            var size = json.total;
                            var totalHtml = "共" + size + "条搜索结果";
                            $("#total").empty();
                            $("#total").append(totalHtml);
                            var listHtml = "";
                            $.each(list, function (index, data) {
                                if (data.lng != null && data.lat != null && data.lng != '' && data.lat != '') {
                                    markers.push(data)
                                }
                                listHtml += "<div class='battery_list_item'  onclick='detail(\"" + data.id + "\")'>"
                                        + "<div class='pic'><img src='${app.imagePath}/battery_map_" + data.imageSuffix + ".png'></div>"
                                        + "<div class='text'><h3>编号：" + data.id + "</h3>"
                                        + "<p><span class='state'>" + data.statusName + "</span><span>电量:" + data.volume + "%</span></p></div></div>";
                            })
                            $("#list").empty();
                            $("#list").append(listHtml);
                            var mapFrame = document.getElementById('map_frame');
                            var doc = mapFrame.contentDocument || mapFrame.document;
                            var childWindow = mapFrame.contentWindow || mapFrame;
                            childWindow.addMarker(markers);
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json'
            )
        }

        $(function () {
            $('#queryText').keydown(function (e) {
                if (e.keyCode == 13) {
                    var id = $('#queryText').val();
                    var agentId = $('#agent_id').combotree('getValue');
                    query(id, agentId)
                }
            });
        });
    </script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>
            <div class="content battery_monitor_map">
                <div class="panel">
                    <div class="toolbar clearfix">
                        <h3>电池位置监控</h3>
                    </div>
                    <a>运营商：</a>
                    <input id="agent_id" class="easyui-combotree" editable="false"
                           style="height: 28px;" value="${(agentId)!''}"
                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                   reload();
                                }
                            "
                    >
                </div>
                <div class="panel map_wrap">
                    <iframe id="map_frame" src="${contextPath}/security/basic/baidu_map/monitor_map.htm" width="100%"
                            height="95%" frameborder="0" style="border:0"></iframe>
                    <div class="battery_total">
                        <ul class="row">
                            <li class="col-3">
                                <div class="box">
                                    <div class="pic"></div>
                                    <p>当前设备总数(${batteryTotal}台)</p>
                                </div>
                            </li>
                            <li class="col-3">
                                <div class="box">
                                    <div class="pic"><img src="${app.imagePath}/battery_map_1.png"></div>
                                    <p>设备运动状态(${motionTotal}台)</p>
                                </div>
                            </li>
                            <li class="col-3">
                                <div class="box">
                                    <div class="pic"><img src="${app.imagePath}/battery_map_2.png"></div>
                                    <p>设备静止状态(${staticTotal}台)</p>
                                </div>
                            </li>
                            <li class="col-3">
                                <div class="box">
                                    <div class="pic"><img src="${app.imagePath}/battery_map_3.png"></div>
                                    <p>设备离线状态(${notOnlineTotal}台)</p>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="battery_list">
                    <div class="battery_list_head">
                        <input type="text" class="text" id="queryText" placeholder="例如搜索'A0000001'"/>

                        <p id="total"></p>
                    </div>
                    <div class="battery_list_body" id="list">
                    </div>
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>