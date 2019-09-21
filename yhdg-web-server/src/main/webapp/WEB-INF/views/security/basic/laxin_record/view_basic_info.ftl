<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td><input type="text" class="text" name="customerId" readonly value="${(entity.agentName)!''}"/></td>
                    <td align="right">拉新手机号：</td>
                    <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.laxinMobile)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">拉新金额：</td>
                    <td><input type="text" class="text" name="money" readonly value="${((entity.laxinMoney)/ 100 + "元")!''}"/></td>
                    <td align="right">客户手机号：</td>
                    <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.targetMobile)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">客户姓名：</td>
                    <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.targetFullname)!''}"/></td>
                    <td align="right">佣金类型：</td>
                    <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.incomeTypeName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">付款方式：</td>
                    <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.payTypeName)!''}"/></td>
                    <td align="right">付款状态：</td>
                    <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.statusName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">付款时间：</td>
                    <td><input type="text" class="text"  readonly value="<#if (entity.payTime)?? >${app.format_date_time(entity.payTime)}</#if>"></td>
                    <td align="right">创建时间：</td>
                    <td><input type="text" class="text"  readonly value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"></td>
                </tr>
                <tr>
                    <td align="right">订单id：</td>
                    <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.orderId)!''}"/></td>
                    <td align="right">MpOpenId:</td>
                    <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.mpOpenId)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">账户姓名：</td>
                    <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.accountName)!''}"/></td>
                    <td align="right"></td>
                    <td></td>
                </tr>
                <tr>
                    <td align="right">取消原因：</td>
                    <td colspan="3"><textarea type="text" class="text easyui-validatebox" name="statusMessage" readonly
                                              style="width: 560px;height: 40px;">${(entity.cancelCanuse)!}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
