<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">换电柜名称：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="cabinetName" value="${(entity.cabinetName)!''}" style="width:182px;height:28px " required="required" ></td>
                </tr>
                <tr>
                    <td align="right">电池箱号：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="boxNum" value="${(entity.boxNum)!''}" style="width:182px;height:28px " required="required" ></td>
                    <td align="right">客户名称：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="customerFullname" value="${(entity.customerFullname)!''}" style="width:182px;height:28px " required="required" ></td>
                </tr>
                <tr>
                    <td align="right">客户手机：</td>
                    <td><input id="duration" class="text easyui-validatebox" readonly name="customerMobile" value="${(entity.customerMobile)!''}" style="width:182px;height:28px " required="required" ></td>

                    <td align="right">订单状态：</td>
                    <td>
                        <select class="easyui-com
                        bobox" required="true"  name="orderStatus" disabled="disabled" style="width:195px;height:28px ">
                        <#list OrderStatusEnum as s>
                            <option value="${s.getValue()}" <#if entity.orderStatus?? && entity.orderStatus == s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>

                </tr>
                <tr>
                    <td width="100" align="right">取消时间：</td>
                    <td>
                        <input type="text" readonly class="text easyui-datetimebox"style="width:195px;height:28px "
                               value="<#if (entity.cancelTime)?? >${app.format_date_time(entity.cancelTime)}</#if>" name="cancelTime">
                    </td>
                    <td width="100" align="right">过期时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly
                               value="<#if (entity.expireTime)?? >${app.format_date_time(entity.expireTime)}</#if>" name="expireTime">
                    </td>
                </tr>
                <tr>
                    <td width="100" align="right">还电时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly
                               value="<#if (entity.backTime)?? >${app.format_date_time(entity.backTime)}</#if>" name="cancelTime">
                    </td>
                    <td width="100" align="right">创建时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>" name="expireTime">
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






