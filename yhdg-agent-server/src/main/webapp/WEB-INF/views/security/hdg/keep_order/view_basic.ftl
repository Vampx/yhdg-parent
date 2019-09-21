<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">取电柜名称：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="takeCabinetName" value="${(entity.takeCabinetName)!''}" style="width:182px;height:28px " required="required" ></td>
                    <td align="right">取电箱号：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="takeBoxNum" value="${(entity.takeBoxNum)!''}" style="width:182px;height:28px " required="required" ></td>
                </tr>
                <tr>
                    <td align="right">初始电量：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="initVolume" value="${(entity.initVolume)!''}" style="width:182px;height:28px " required="required" ></td>
                    <td align="right">取电时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly
                               value="<#if (entity.takeTime)?? >${app.format_date_time(entity.takeTime)}</#if>" name="takeTime">
                    </td>
                </tr>
                <tr>
                    <td align="right">放电柜名称：</td>
                    <td><input id="duration" class="text easyui-validatebox" name="putCabinetName" readonly value="${(entity.putCabinetName)!''}" style="width:182px;height:28px " required="required" ></td>
                    <td align="right">放电箱号：</td>
                    <td><input id="duration" class="text easyui-validatebox" name="putBoxNum" readonly value="${(entity.putBoxNum)!''}" style="width:182px;height:28px " required="required" ></td>
                </tr>
                <tr>
                    <td align="right">初始电量：</td>
                    <td><input id="duration" class="text easyui-validatebox" name="currentVolume" readonly value="${(entity.currentVolume)!''}" style="width:182px;height:28px " required="required" ></td>
                    <td align="right">取电时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox" style="width:195px;height:28px " readonly
                               value="<#if (entity.putTime)?? >${app.format_date_time(entity.putTime)}</#if>" name="putTime">
                    </td>
                </tr>
                <tr>
                    <td align="right">维护人：</td>
                    <td><input id="duration" class="text easyui-validatebox" name="takeUserFullname" readonly value="${(entity.takeUserFullname)!''}" style="width:182px;height:28px " required="required" ></td>
                    <td align="right">订单状态：</td>
                    <td>
                        <select class="easyui-combobox" required="true"  name="orderStatus" readonly="" style="width:195px;height:28px ">
                        <#list OrderStatusEnum as s>
                            <option value="${s.getValue()}" <#if entity.orderStatus?? && entity.orderStatus == s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var ok = function() {
            var success = true;
            return success;
        };
        win.data('ok', ok);
    })();
</script>






