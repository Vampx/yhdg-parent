<div class="popup_body"  style="padding-left:20px;font-size: 14px;min-height: 85%;">
    <div class="ui_table">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td width="120" align="right">源类型：</td>
                <td><input type="text" class="text easyui-validatebox" readonly name="type" value="<#list sourceTypeEnum as type><#if type.getValue() == entity.sourceType >${type.getName()}</#if></#list>"></td>
                <td width="120" align="right">手机号码：</td>
                <td><input type="text" class="text easyui-validatebox" readonly name="mobile" value="${(entity.mobile)!''}"></td>
            </tr>
            <tr>
                <td width="70" align="right">处理时间：</td>
                <td><input type="text" class="text easyui-validatebox" readonly name="createTime" value="<#if (entity.handleTime)?? >${app.format_date_time(entity.handleTime)}</#if>"/></td>
                <td width="70" align="right">创建时间：</td>
                <td><input type="text" class="text easyui-validatebox" readonly name="createTime" value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"/></td>
            </tr>
            <tr>
                <td width="70" align="right">模板id：</td>
                <td><input type="text" class="text easyui-validatebox" readonly name="type" value=" ${(entity.type)!''}"></td>
                <td width="70" align="right">延迟时间：</td>
                <td><input type="text" class="text easyui-validatebox" readonly name="delay" value="${entity.delay!''}"/></td>
            </tr>
            <tr>
                <td width="70" align="right">重发次数：</td>
                <td><input type="text" class="text easyui-validatebox" readonly name="resendNum" value="${(entity.resendNum)!''}"></td>
                <td width="70" align="right">消息状态：</td>
                <td><input type="text" class="text easyui-validatebox" readonly name="status"  value="<#list messageStatusEnum as type><#if type.getValue() == entity.status >${type.getName()}</#if></#list>"></td>
            </tr>
            <tr>
                <td width="70" align="right">阅读次数：</td>
                <td><input type="text" class="text easyui-validatebox" readonly name="readCount" value="${(entity.readCount)!''}"></td>
                <td width="70" align="right">昵称：</td>
                <td><input type="text" class="text easyui-validatebox" readonly name="nickname"  value="${(entity.nickname)!''}"></td>
            </tr>
        <#list detailList as detail>
            <tr class="keyword" keyword_id="${(detail.id)!''}" style="display: none">
                <td id="keywordName" align="right">${(detail.keywordName)!''}</td>
                <td><input id="keywordValue" type="text" class="text" style="width: 450px;" value="${(detail.keywordValue)!''}"/></td>
                <td colspan="2"><input id="keywordColor" type="text" class="text easyui-validatebox" style="background-color:${(detail.color)!''}; width: 25px;" value="${(detail.color)!''}"/></td>
            </tr>
        </#list>
        </table>
        <div>
            <table style="float: left;">
                <tr>
                    <td align="left" valign="top" style="padding-top:10px;">变量</td>
                </tr>
                <tr>
                    <td><textarea style="width:300px;height: 240px;font-size: 14px;" id="variable" maxlength="512" >${(entity.variable)!''}</textarea></td>
                </tr>
            </table>

            <table style="float: right;">
                <tr>
                    <td align="left"  style="padding-top:10px;">预览</td>
                </tr>
                <tr>
                    <td>
                        <fieldset style="width: 400px;">
                            <div id="preview_text" style="width: 400px;height: 240px;margin-top: 10px;margin-left: 10px;">
                            </div>
                        </fieldset>
                    </td>
                </tr>
            </table>
        </div>

    </div>
</div>
<div class="popup_btn" >
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    function preview() {
        var templateName = '${(templateName)!''}';
        var date = new Date();

        var html = '<div class="message_preview_bd preview">' +
                '<p>' + templateName + '</p>' +
                '<p style="color: #999; padding-bottom: 5px;">' + (date.getMonth() + 1) + '月' + date.getDate() + '日</p>' + '<br>';

        $(".ui_table .keyword").each(function () {
            var keywordId = $(this).attr("keyword_id");
            var keywordName = $(this).find("#keywordName").text() + ': ';
            var keywordValue = $(this).find("#keywordValue").val();
            var keywordColor = $(this).find("#keywordColor").val();
            if (keywordId == 'first' || keywordId == 'remark') {
                keywordName = '';
            }
            html += '<p>' + keywordName + '<span style="color: ' + keywordColor + '; padding-bottom: 5px;">' + keywordValue + '</span></p>'  + '<br>';
        });

        html += '</div>';

        $('#preview_text').html(html);
    }

    (function() {
        //点击生成预览
        preview();

        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();


</script>