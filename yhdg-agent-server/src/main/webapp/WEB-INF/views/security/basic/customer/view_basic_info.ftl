<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="photoPath" id="photo_path_${pid}">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text" maxlength="40" name="fullname" readonly="readonly" value="${(entity.fullname)!''}"/>
                    </td>
                    <td width="100" align="right" valign="top" rowspan="2" style="padding-top:10px;">头像：</td>
                    <td rowspan="2">
                        <div class="portrait">
                            <a href="javascript:void(0)"><img
                                    id="image_${pid}" src=<#if entity.photoPath ?? && entity.photoPath != ''>'${staticUrl}${(entity.photoPath)!''}' <#else>
                                '${app.imagePath}/user.jpg'</#if> /><span>修改头像</span></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="right">昵称：</td>
                    <td><input type="text" name="nickname" maxlength="40" readonly="readonly" class="text easyui-validatebox"
                               value="${(entity.nickname)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">手机号码：</td>
                    <td><input type="text" id="mobile" class="text" readonly="readonly" name="mobile" maxlength="11" validType="mobile"
                               value="${(entity.mobile)!''}"/></td>
                    <td align="right">卡号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly="readonly" maxlength="40" name="icCard"
                               value="${(entity.icCard)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">身份证卡号：</td>
                    <td><input type="text" class="text" maxlength="40" readonly="readonly" name="idCard" value="${(entity.idCard)!''}"/>
                    </td>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled="disabled" name="isActive" id="isActive_1"
                                   <#if entity.isActive?? && entity.isActive == 1>checked</#if> value="1"/><label
                                for="isActive_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled="disabled" name="isActive" id="isActive_0"
                                   <#if entity.isActive?? && entity.isActive == 0>checked</#if> value="0"/><label
                                for="isActive_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">公司：</td>
                    <td>
                        <select id="company" name="company" readonly="readonly" class="easyui-combobox" style="width: 184px; height: 28px;">
                        <#if companyList??>
                            <#list companyList as e>
                                <option <#if entity.company?? && entity.company?c == e.itemValue>selected</#if>
                                        value="${(e.itemValue)!''}">${(e.itemName)!''}</option>
                            </#list>
                        </#if>
                        </select>
                    </td>
                    <td align="right">电池类型：</td>
                    <td>
                        <select id="brand" name="batteryType" readonly="readonly" class="easyui-combobox"
                                style="width: 184px; height: 28px;">
                        <#if batteryTypeList??>
                            <#list batteryTypeList as e>
                                <option <#if entity.batteryType?? && entity.batteryType?c == e.itemValue>selected</#if>
                                        value="${(e.itemValue)!''}">${(e.itemName)!''}</option>
                            </#list>
                        </#if>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">推送token：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly="readonly"
                               value="${(entity.pushToken)!''}"/></td>
                    <td align="right">推送类型：</td>
                    <td>
                        <select name="status" readonly="readonly" class="easyui-combobox" style="width: 184px; height: 28px;">
                        <#list pushTypeEnum as e>
                            <option <#if entity.pushType?? && entity.pushType == e.getValue()>selected</#if>
                                    value="${e.getValue()}">${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">绑定电池：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly="readonly"
                               value="${(entity.batteryId)!''}"/></td>
                    <td align="right">绑定电池时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" style="width:195px;height:28px " readonly="readonly"
                               value="<#if (entity.batteryBindingTime)?? >${app.format_date_time(entity.batteryBindingTime)}</#if>"
                               name="batteryBindingTime"></td>
                </tr>
            </table>
        </form>
    </div>
</div>
