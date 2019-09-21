<div class="tab_item" style="display:block;">
    <div class="ui_table" style="height: 380px">
        <form>
            <table cellpadding="0" cellspacing="0">
            <#if flag == "customer">
                <tr>
                    <td align="right">客户手机号：</td>
                    <td><input type="text" maxlength="40" class="text" name="customerMobile" readonly
                               value="${(entity.customerMobile)!''}"/></td>
                    <td align="right">客户昵称：</td>
                    <td><input type="text" class="text" maxlength="40" name="customerFullname" readonly
                               value="${(entity.customerFullname)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">客户取出时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" readonly name="customerOutTime" style="width:184px;height:28px" value="<#if (entity.customerOutTime)?? >${app.format_date_time(entity.customerOutTime)}</#if>" /></td>
                </tr>
            <#elseif flag=="keeper">
                <tr>
                    <td align="right">维护昵称：</td>
                    <td><input type="text" maxlength="40" class="text" name="keeperName" readonly
                               value="${(entity.keeperName)!''}"/></td>
                    <td align="right">维护手机号：</td>
                    <td><input type="text" class="text" maxlength="40" name="keeperMobile" readonly
                               value="${(entity.keeperMobile)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">维护取出时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" readonly name="keeperOutTime" style="width:184px;height:28px" value="<#if (entity.keeperOutTime)?? >${app.format_date_time(entity.keeperOutTime)}</#if>" /></td>
                </tr>
            <#else>
                <tr>
                    <td align="right">换电柜ID：</td>
                    <td><input type="text" maxlength="40" class="text" name="cabinetId" readonly
                               value="${(entity.cabinetId)!''}"/></td>
                    <td align="right">换电柜名称：</td>
                    <td><input type="text" class="text" maxlength="40" name="cabinetName" readonly
                               value="${(entity.cabinetName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">箱体编号：</td>
                    <td><input type="text" maxlength="40" class="text" name="boxNum" readonly
                               value="${(entity.boxNum)!''}"/></td>
                    <td align="right">入箱时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" readonly name="inBoxTime" style="width:184px;height:28px" value="<#if (entity.inBoxTime)?? >${app.format_date_time(entity.inBoxTime)}</#if>" /></td>
                </tr>
            </#if>
            </table>
        </form>
    </div>
</div>
<script>
    (function () {
        var win = $('#${pid}');
        var ok = function () {
            var success = true;
            return success;
        };
        win.data('ok', ok);
    })();
</script>