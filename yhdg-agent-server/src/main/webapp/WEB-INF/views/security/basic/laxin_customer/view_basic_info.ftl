<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td><input type="text" class="text" name="customerId" readonly value="${(entity.agentName)!''}"/></td>
                    <td align="right">运营商编号：</td>
                    <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.agentCode)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">拉新手机号：</td>
                    <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.laxinMobile)!''}"/></td>
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
                    <td align="right">购买押金时间：</td>
                    <td><input type="text" class="text"  readonly value="<#if (entity.foregiftTime)?? >${app.format_date_time(entity.foregiftTime)}</#if>"></td>
                    <td align="right">创建时间：</td>
                    <td><input type="text" class="text"  readonly value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"></td>
                </tr>
                <#if entity.incomeType?? && entity.incomeType == 1>
                    <tr>
                        <td align="right">按次金额：</td>
                        <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.laxinMoney / 100.0)!''}"/></td>
                        <td align="right"></td>
                        <td></td>
                    </tr>
                </#if>
                <#if entity.incomeType?? && entity.incomeType == 2>
                    <tr>
                        <td align="right">按月金额：</td>
                        <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.packetPeriodMoney / 100.0)!''}"/></td>
                        <td align="right">过期月份:</td>
                        <td><input type="text" class="text" maxlength="40"  readonly value="${(entity.packetPeriodMonth)!''}"/></td>
                    </tr>
                </#if>

            </table>
        </form>
    </div>
</div>
