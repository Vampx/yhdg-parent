<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">运营商：</td>
                    <td><input type="text" class="text easyui-validatebox" name="agentName" value="${(entity.agentName)!''}"/></td>
                    <td width="70" align="right">电池编号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="batteryId" value="${(entity.batteryId)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">充电类型：</td>
                    <td><input type="text" class="text easyui-validatebox" name="typeName" value="${(entity.typeName)!''}"/></td>
                    <td width="70" align="right">开始电量：</td>
                    <td><input type="text" class="text easyui-validatebox" name="beginVolume" value="${(entity.beginVolume)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">当前电量：</td>
                    <td><input type="text" class="text easyui-validatebox" name="currentVolume" value="${(entity.currentVolume)!''}"/></td>
                    <td width="70" align="right">开始时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" name="beginTime" style="width:184px;height:28px" value="<#if (entity.beginTime)?? >${app.format_date_time(entity.beginTime)}</#if>" /></td>
                </tr>
                <tr>
                    <td width="70" align="right">充电结束时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" name="endTime" style="width:184px;height:28px" value="<#if (entity.endTime)?? >${app.format_date_time(entity.endTime)}</#if>" /></td>
                    <td width="70" align="right">计划时长：</td>
                    <td><input type="text" class="text easyui-validatebox" name="duration" value="${(entity.duration)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">客户名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="customerFullname" value="${(entity.customerFullname)!''}"/></td>
                    <td width="70" align="right">客户手机：</td>
                    <td><input type="text" class="text easyui-validatebox" name="customerMobile" value="${(entity.customerMobile)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">柜子名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="cabinetName" value="${(entity.cabinetName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">箱号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="boxNum" value="${(entity.boxNum)!''}"/></td>
                    <td width="70" align="right">维护员：</td>
                    <td><input type="text" class="text easyui-validatebox" name="keeperName" value="${(entity.keeperName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">创建时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" name="createTime" style="width:184px;height:28px" value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>" /></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var ok = function() {
            return true;
        };

        win.data('ok', ok);
    })();
</script>