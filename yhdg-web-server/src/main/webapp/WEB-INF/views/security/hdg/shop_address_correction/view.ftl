<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">客户名称：</td>
                    <td><input type="text" maxlength="40" readonly value="${(entity.customerFullname)!''}" class="text easyui-validatebox" /></td>
                    <td align="right">客户手机：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly maxlength="11" value="${(entity.customerMobile)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">省份城市：</td>
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
                        <input type="text" readonly class="text easyui-validatebox" value="${(areaText)!''}"/>
                    </td>
                    <td width="70" align="right">设备名称：</td>
                    <td><input type="text" readonly  value="${(entity.shopName)!''}" class="text easyui-validatebox" /></td>
                </tr>
                <tr>
                    <td align="right">详细地址：</td>
                    <td><input type="text" readonly class="text easyui-validatebox"maxlength="40" value="${(entity.street)!''}"/></td>
                    <td align="right">状态：</td>
                    <td><input type="text" readonly class="text easyui-validatebox"maxlength="40" value="${(status)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">经度：</td>
                    <td><input type="text"required="true" readonly name="receiverMobile"maxlength="11" value="${(entity.lng)!''}" class="text easyui-validatebox" required="true"  /></td>
                    <td align="right">纬度：</td>
                    <td><input type="text" readonly class="text easyui-validatebox"maxlength="40" value="${(entity.lat)!''}" /></td>
                </tr>
                <tr>
                    <td align="right">创建时间：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"/></td>
                    <td align="right">备注：</td>
                    <td><textarea style="width:200px;" readonly maxlength="400">${(entity.memo)!''}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>