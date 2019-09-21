<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">客户姓名：</td>
                    <td><input id="duration" class="text easyui-validatebox" name="customerFullname" value="${(entity.customerFullname)!''}" style="width:182px;height:28px " ></td>
                   <#-- <td width="70" align="right" valign="top" rowspan="2" style="padding-top:10px;">退款照片：</td>
                    <td rowspan="2">
                        <div class="portrait">
                            <a onclick="preview(${(refundPhoto)!''});"><img id="refund_photo"  src="${app.imagePath}/${(refundPhoto)!'user.jpg'}" /><span>退款照片</span></a>
                        </div>
                    </td>-->
                    <td width="100" align="right">客户手机：</td>
                    <td><input id="duration" class="text easyui-validatebox" name="customerMobile" value="${(entity.customerMobile)!''}" style="width:182px;height:28px " ></td>
                </tr>
                <tr>
                    <td align="right">退款状态：</td>
                    <td>
                        <select class="easyui-combobox" required="true"  name="refundStatus" style="width:195px;height:28px ">
                        <#list RefundStatus as s>
                            <option value="${s.getValue()}" <#if entity.refundStatus?? && entity.refundStatus == s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                    <td align="right">金额：</td>
                    <td><input type="text" class="text" style="width:182px;height:28px " name="money" value="<#if (entity.money)??>${((entity.money) / 100 + "元")!''}<#else></#if>"/></td>
                </tr>
                <tr>
                    <td align="right">申请退款时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px "
                               value="<#if (entity.applyRefundTime)?? >${app.format_date_time(entity.applyRefundTime)}</#if>" name="applyRefundTime">
                    </td>
                    <td align="right">退款时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px "
                               value="<#if (entity.refundTime)?? >${app.format_date_time(entity.refundTime)}</#if>" name="refundTime">
                    </td>
                </tr>
                <tr>
                    <td align="right">退款人：</td>
                    <td><input id="duration" class="text easyui-validatebox" name="refundOperator" value="${(entity.refundOperator)!''}" style="width:182px;height:28px " ></td>
                    <td align="right">退款金额：</td>
                    <td><input type="text" class="text" style="width:182px;height:28px " name="refundMoney" value="<#if (entity.refundMoney)??>${((entity.refundMoney) / 100 + "元")!''}<#else></#if>"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td colspan="3"><textarea style="width:490px;" maxlength="20" name="refundReason">${(entity.refundReason)!''}</textarea></td>
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

    function preview(path) {
        App.dialog.show({
            options:'maximized:true',
            title: '查看',
            href: "${contextPath}/security/main/preview.htm?path=" + path
        });
    }

</script>






