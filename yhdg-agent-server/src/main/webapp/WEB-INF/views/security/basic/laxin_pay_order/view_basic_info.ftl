<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">订单id：</td>
                    <td><input type="text" class="text" readonly="readonly" value="${(entity.id)!''}"/></td>
                    <td align="right">卡号：</td>
                    <td><input type="text" class="text" readonly="readonly" value="${(entity.agentName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">金额：</td>
                    <td><input type="text" class="text" readonly="readonly" value="${(entity.money/100)!''}"/></td>
                    <td align="right">记录数：</td>
                    <td><input type="text" class="text" readonly="readonly" value="${(entity.recordCount)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">付款类型：</td>
                    <td><input type="text" class="text" readonly="readonly" value="${(entity.payStatusName)!''}"/></td>
                    <td align="right">状态：</td>
                    <td><input type="text" class="text" readonly="readonly" value="${(entity.statusName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">付款时间：</td>
                    <td><input type="text" class="text"  readonly value="<#if (entity.payTime)?? >${app.format_date_time(entity.payTime)}</#if>"></td>
                    <td align="right">创建时间：</td>
                    <td><input type="text" class="text"  readonly value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"></td>
                </tr>
            </table>
        </form>
    </div>
</div>
