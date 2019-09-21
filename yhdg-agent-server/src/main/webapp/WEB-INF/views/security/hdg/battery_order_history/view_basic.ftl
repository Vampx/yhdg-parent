<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">取电柜名称：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="takeCabinetName" value="${(entity.takeCabinetName)!''}" style="width:182px;height:28px "  ></td>
                    <td align="right">取电箱号：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="takeBoxNum" value="${(entity.takeBoxNum)!''}" style="width:182px;height:28px "  ></td>
                </tr>
                <tr>
                    <td align="right">初始电量：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="initVolume" value="${(entity.initVolume)!''}" style="width:182px;height:28px "  ></td>
                    <td align="right">取电时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly
                               value="<#if (entity.takeTime)?? >${app.format_date_time(entity.takeTime)}</#if>" name="takeTime">
                    </td>
                </tr>
                <tr>
                    <td align="right">放电柜名称：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="putCabinetName" value="${(entity.putCabinetName)!''}" style="width:182px;height:28px "  ></td>
                    <td align="right">放电箱号：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="putBoxNum" value="${(entity.putBoxNum)!''}" style="width:182px;height:28px "  ></td>
                </tr>
                <tr>
                    <td align="right">初始电量：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="currentVolume" value="${(entity.currentVolume)!''}" style="width:182px;height:28px "  ></td>
                    <td align="right">取电时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly
                               value="<#if (entity.putTime)?? >${app.format_date_time(entity.putTime)}</#if>" name="putTime">
                    </td>
                </tr>
                <tr>
                    <td align="right">支付类型：</td>
                    <td>
                        <select class="easyui-combobox" required="true" disabled="disabled"  name="payType" style="width:192px;height:28px ">
                        <#list PayTypeEnum as s>
                            <option value="${s.getValue()}" <#if entity.payType?? && entity.payType == s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                    <td align="right">支付金额：</td>
                    <td><input class="text easyui-validatebox" name="money" readonly  value="<#if (entity.money)??>${((entity.money) / 100 + "元")!''}<#else></#if>" style="width:182px;height:28px "  ></td>
                </tr>
                <tr>
                    <td align="right">骑行距离：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="currentDistance" value="${(entity.currentDistance)!''}" style="width:182px;height:28px "  ></td>
                    <td align="right">支付时间：</td>
                    <td><input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly
                               value="<#if (entity.payTime)?? >${app.format_date_time(entity.payTime)}</#if>" name="payTime">
                    </td>
                </tr>
                <tr>
                    <td align="right">客户名称：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="customerFullname" value="${(entity.customerFullname)!''}" style="width:182px;height:28px "  ></td>
                    <td align="right">客户手机：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="customerMobile" value="${(entity.customerMobile)!''}" style="width:182px;height:28px "  ></td>
                </tr>
                <tr>
                    <td align="right">退款状态：</td>
                    <td><input type="text" class="text" style="width:182px;height:28px " readonly name="refundMoney" value="${refundStatus!''}"/></td>
                    <td align="right">退款金额：</td>
                    <td><input type="text" class="text" style="width:182px;height:28px " readonly name="refundMoney"  value="<#if (entity.refundMoney)??>${((entity.refundMoney) / 100 + "元")!''}<#else></#if>"/></td>
                </tr>
                <tr>
                    <td align="right">订单状态：</td>
                    <td>
                        <select class="easyui-combobox" required="true" readonly=""  name="orderStatus" style="width:192px;height:28px ">
                        <#list OrderStatusEnum as s>
                            <option value="${s.getValue()}" <#if entity.orderStatus?? && entity.orderStatus == s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                    <td width="100" align="right">创建时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>" name="createTime">
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






