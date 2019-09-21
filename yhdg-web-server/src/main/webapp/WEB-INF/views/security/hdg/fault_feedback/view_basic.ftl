<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">运营商名称：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(entity.agentName)!''}"
                               readonly="readonly" /></td>
                    <td align="right">电池编号：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" maxlength="11"
                               value="${(entity.batteryId)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">换电柜：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(entity.cabinetId)!''}(${(entity.cabinetName)!''})"
                               readonly="readonly" /></td>
                    <td align="right">换电柜名称：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" maxlength="11"
                               value="${(entity.cabinetName)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">故障名称：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" value="${(entity.faultName)!''}"/>
                    </td>
                    <td align="right">故障类型：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" value="${(entity.faultTypeName)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">客户姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(entity.customerName)!''}"
                               readonly="readonly" /></td>
                    <td align="right">客户手机：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" maxlength="11"
                               value="${(entity.customerMobile)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">客户编号：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(entity.customerId)!''}"
                               readonly="readonly" /></td>
                    <td align="right">处理人：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" maxlength="11"
                               value="${(entity.handlerName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">处理状态：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" value="${(entity.handleStatusName)!''}"/>
                    <td align="right">处理时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox" style="width: 184px; height: 28px;" disabled
                               value="${(entity.handleTime?string('yyyy-MM-dd HH:mm:ss'))!''}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">创建时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox" style="width: 184px; height: 28px;" disabled
                               value="${(entity.createTime?string('yyyy-MM-dd HH:mm:ss'))!''}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">故障内容：</td>
                    <td colspan="3"><textarea style="width:430px;" readonly>${(entity.content)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">处理结果：</td>
                    <td colspan="3"><textarea style="width:430px;" readonly>${(entity.handleResult)!''}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
