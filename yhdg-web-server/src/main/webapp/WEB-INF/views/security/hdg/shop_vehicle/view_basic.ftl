<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td><input name="agentId" class="easyui-combotree" readonly
                               editable="false" style="width: 184px; height: 28px;" id="agent_id_${pid}"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto',
                                onClick: function () {
                                       swich_agent_${pid}();
                                }"
                               value="${(entity.agentId)!''}"/>
                    <td width="70" align="right">车型名称：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(entity.modelName)!''}" readonly/></td>
                </tr>
                <tr>
                    <td align="right">车辆编号：</td>
                    <td><input type="text" class="text" readonly value="${(entity.id)!''}"/></td>
                    <td align="right">车辆型号：</td>
                    <td><input type="text" class="text" readonly value="${(entity.modelCode)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">续航里程：</td>
                    <td><input type="text" class="text" readonly value="${(entity.endurance)!''}"/></td>
                    <td align="right">最高时速：</td>
                    <td><input type="text" class="text" readonly value="${(entity.speed)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">仪表里程：</td>
                    <td><input type="text" class="text" readonly value="${(entity.meter)!''}"/></td>
                    <td align="right">防盗：</td>
                    <td><input type="text" class="text" readonly value="${(entity.burglarproof)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">刹车：</td>
                    <td><input type="text" class="text" readonly value="${(entity.brake)!''}"/></td>
                    <td align="right">轮胎：</td>
                    <td><input type="text" class="text" readonly value="${(entity.tyre)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">长宽高：</td>
                    <td><input type="text" class="text" readonly value="${(entity.volume)!''}"/></td>
                    <td align="right">车重：</td>
                    <td><input type="text" class="text" readonly value="${(entity.weight)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">电池编号：</td>
                    <td><input type="text" class="text" readonly value="${(entity.batteryId)!''}"/></td>
                    <td align="right">订单编号：</td>
                    <td><input type="text" class="text" readonly value="${(entity.orderId)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">状态：</td>
                    <td>
                        <select name="status" class="easyui-combobox" readonly="readonly" style="width: 184px; height: 28px;">
                        <#list StatusEnum as e>
                            <option <#if entity.status?? && entity.status == e.getValue()>selected</#if>
                                    value="${e.getValue()}">${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                    <td align="right">使用次数：</td>
                    <td><input type="text" class="text" readonly value="${(entity.useCount)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">当前电量：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="volume" readonly value="${(entity.volume)!''}"
                               style="width: 184px; height: 28px;"/></td>
                    <td align="right">当前信号：</td>
                    <td><input type="text" class="text" readonly value="${(entity.currentSignal)!''}"/></td>
                </tr>
                <tr>

                    <td align="right">订单距离：</td>
                    <td><input type="text" maxlength="40" class="text" name="orderDistance" readonly
                               value="${(entity.orderDistance)!''}"/></td>
                    <td align="right">总距离：</td>
                    <td><input type="text" class="text" maxlength="40" name="totalDistance" readonly
                               value="${(entity.totalDistance)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">经度：</td>
                    <td><input type="text" maxlength="40" class="text" readonly name="lng" value="${(entity.lng)!''}"/></td>
                    <td align="right">纬度：</td>
                    <td><input type="text" class="text" maxlength="40" readonly name="lat" value="${(entity.lat)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">电池类型：</td>
                    <td><input maxlength="40" class="text easyui-validatebox" readonly value="${(entity.batteryType)!''}"/></td>
                    <td align="right">上报时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" name="reportTime" readonly
                               style="width:184px;height:28px"
                               value="<#if (entity.reportTime)?? >${app.format_date_time(entity.reportTime)}</#if>"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" disabled
                                   <#if entity.isActive?? && entity.isActive == 1>checked</#if> id="is_active_1"
                                   value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" disabled
                                   <#if entity.isActive?? && entity.isActive == 0>checked</#if> id="is_active_0"
                                   value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        var ok = function() {
            if(!jform.form('validate')) {
                return false;
            }
            var success = true;

            return success;
        };

        win.data('ok', ok);
    })();
</script>