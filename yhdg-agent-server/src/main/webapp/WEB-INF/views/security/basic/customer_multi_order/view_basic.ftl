<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0" style="width: 80%;">
                <tr>
                    <td width="70" align="right">订单编号：</td>
                    <td><input class="text easyui-validatebox" readonly="readonly" value="${(entity.id)!''}" style="width:182px;height:28px " required="required" ></td>
                    <td align="right">总金额：</td>
                    <td>
                        <input class="text easyui-validatebox" readonly="readonly" value="<#if (entity.totalMoney)??>${((entity.totalMoney) / 100 + "元")!''}<#else></#if>" style="width:182px;height:28px " required="required">
                    </td>
                </tr>
                <tr>
                    <td align="right">待付金额：</td>
                    <td><input class="text easyui-validatebox" readonly="readonly" value="<#if (entity.debtMoney)??>${((entity.debtMoney) / 100 + "元")!''}<#else></#if>" style="width:182px;height:28px " required="required" ></td>
                    <td align="right">客户姓名：</td>
                    <td><input class="text easyui-validatebox" readonly="readonly" value="${(entity.fullname)!''}" style="width:182px;height:28px " required="required" ></td>
                </tr>
                <tr>
                    <td width="110" align="right">客户手机号：</td>
                    <td><input class="text easyui-validatebox" readonly="readonly" value="${(entity.mobile)!''}" style="width:182px;height:28px " required="required" ></td>
                    <td width="110" align="right">运营商：</td>
                    <td><input class="text easyui-validatebox" readonly="readonly" value="${(entity.agentName)!''}" style="width:182px;height:28px " required="required" ></td>
                </tr>
                <tr>
                    <td width="100" align="right">创建时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:195px;height:28px " readonly="readonly"
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>" name="createTime">
                    </td>
                    <td align="right">状态：</td>
                    <td>
                        <select name="status" id="status" style="width: 195px; height: 28px;">
                        <#list statusEnum as status>
                            <option value="${status.getValue()}"
                                    <#if entity.status==status.getValue()>selected="selected"</#if>>${status.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>






