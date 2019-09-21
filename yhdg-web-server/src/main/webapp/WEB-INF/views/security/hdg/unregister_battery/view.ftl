<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <div class="popup_body">
                    <div class="report_table">
                        <table>
                            <tbody>
                            <tr>
                                <th colspan="2">${(entity.id)!''}的信息</th>
                            </tr>
                            <tr>
                                <td width="20%">上报时间:</td>
                                <td width="80%"><#if (entity.reportTime)?? >${app.format_date_time(entity.reportTime)}</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">总电压:</td>
                                <td width="80%"><#if (entity.voltage)?? >${entity.voltage/1000}</#if>V</td>
                            </tr>
                            <tr>
                                <td width="20%">电流:</td>
                                <td width="80%"><#if (entity.electricity)?? >${entity.electricity/1000}</#if>A</td>
                            </tr>
                            <tr>
                                <td width="20%">当前电量:</td>
                                <td width="80%"><#if (entity.currentCapacity)?? >${entity.currentCapacity/1000}</#if>AH</td>
                            </tr>
                            <tr>
                                <td width="20%">温度:</td>
                                <td width="80%">${(entity.temp)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">经纬度:</td>
                                <td width="80%">${(entity.lng)!''}/${(entity.lat)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">电池状态:</td>
                                <td width="80%">${(entity.fetStatusName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">信号:</td>
                                <td width="80%">${(entity.currentSignal)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">信号类型:</td>
                                <td width="80%">${(entity.signalTypeName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">电池串数:</td>
                                <td width="80%">${(entity.strand)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">单体电压(mv):</td>
                                <td width="80%">${(entity.singleVoltage)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">材质:</td>
                                <td width="80%">${(entity.materialName)!''}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
            </div>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>