<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">救助站名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" readonly name="stationName" maxlength="20" value="${(entity.stationName)!''}"/></td>
                    <td align="right">联系电话：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" readonly name="tel" maxlength="11" value="${(entity.tel)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">省份城市：</td>
                    <td>
                        <#assign areaText=''>
                        <#if (entity.provinceName)??>
                            <#assign areaText=entity.provinceName>
                        </#if>
                        <#if (entity.cityName)??>
                            <#assign areaText=areaText + ' - ' + entity.cityName>
                        </#if>
                        <#if (entity.districtName)??>
                            <#assign areaText=areaText + ' - ' + entity.districtName>
                        </#if>
                        <input type="text" class="text easyui-validatebox" readonly value="${(areaText)!''}"/>
                    </td>
                    <td align="right">详细地址：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.street)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">经营范围：</td>
                    <td colspan="3"><textarea style="width:420px;"  name="introduce" readonly maxlength="256">${(entity.introduce)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right">救助站图片：</td>
                    <td>
                        <div class="image portrait" style="float: left; margin-right: 10px;">
                            <a href="javascript:void(0)"><img id="image_${pid}" src=<#if entity.imagePath ?? && entity.imagePath != ''>'${staticUrl}${entity.imagePath}' <#else>'${app.imagePath}/user.jpg'</#if> /><span>修改图片</span></a>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
