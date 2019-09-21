<div class="popup_body" style="padding-left:50px;font-size: 14px;min-height: 85%;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(template.id)!''}">
            <input type="hidden" name="weixinmpId" value="${(template.weixinmpId)!''}">
            <div style="height: 100px;">
                <table cellpadding="0" cellspacing="0" style="float: left;">
                    <tr>
                        <td width="70" align="left">模板名称：</td>
                        <td>
                            <span>${(template.templateName)!''}</span>
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="left">公众号：</td>
                        <td><input type="text" class="text easyui-validatebox" name="mpCode" value="${(template.mpCode)!''}"
                                   style="width: 300px; height: 28px;"/></td>
                    </tr>
                    <tr>
                        <td align="left">启用：</td>
                        <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1"
                                   <#if template.isActive?? && template.isActive == 1>checked</#if> value="1"/><label
                                for="is_active_1">启用</label>
                        </span>
                            <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0"
                                   <#if template.isActive?? && template.isActive == 0>checked</#if> value="0"/><label
                                    for="is_active_0">禁用</label>
                        </span>
                        </td>
                    </tr>
                </table>
                <table style="float: right;">
                    <tr>
                        <td align="left" valign="top" style="padding-top:10px;">变量</td>
                    </tr>
                    <tr>
                        <td><textarea style="width:330px;height: 100px;font-size: 14px;" id="variable" maxlength="512" >${(template.variable)!''}</textarea></td>
                    </tr>
                </table>
            </div>

            <div style="height: 20px;"></div>

            <div>
                <fieldset style="margin-top: 10px;width: 580px;float: left;">
                    <legend>
                <span>
                    <label>模板内容</label>
                </span>
                    </legend>
                    <div class="ui_table" style="width: 580px;">
                        <table cellpadding="0" cellspacing="0" class="times_table_list" style="padding-top: 20px;margin-left: 20px;">
                        <#list templateDetailList as detail>
                            <tr class="keyword" keyword_id="${(detail.id)!''}">
                                <td>
                                <span id="keyword_name">
                                     ${(detail.keywordName)!''}
                                </span>
                                </td>
                                <td>
                                    <textarea style="width:360px;height:30px;" id="keyword_value" name="keywordValue" maxlength="200">${(detail.keywordValue)!''}</textarea>
                                </td>
                                <td>
                                    <input type="text" id="keyword_color" style="width: 80px;height: 28px;background-color:${(detail.color)!''};text-align: center;" name="color" value="${(detail.color)!''}" >
                                </td>
                            </tr>
                        </#list>
                        </table>
                        <table style="margin-bottom: 5px;">
                            <a href="#">
                                <div class="ok_btn"
                                     style="	width: 140px;height: 40px;line-height: 40px;text-align: center;color: #FFFFFF;font-size: 14px;margin-top: 10px;margin-left: 200px;background-color: #556bd8;border-radius: 6px;"
                                     id="ok_btn" onclick="preview()">生成模板
                                </div>
                            </a>
                        </table>
                    </div>
                </fieldset>

                <table style="float: right;">
                    <tr>
                        <td align="left"  style="padding-top:10px;">预览</td>
                    </tr>
                    <tr>
                        <td>
                            <fieldset style="width: 340px;">
                                <div id="preview_text" style="width: 300px;height: 280px;margin-top: 10px;margin-left: 10px;">
                                </div>
                            </fieldset>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    function preview() {
        var templateName = '${(template.templateName)!''}';
        var date = new Date();
        var html =  '<p>' + templateName + '</p>' + '<br>' +
                '<p style="color: #999; padding-bottom: 5px;">'+ (date.getMonth() + 1) +'月'+date.getDate()+'日</p>' + '<br>';
        $(".ui_table .keyword").each(function(){
            var keywordId = $(this).attr("keyword_id");
            var keywordName = $(this).find("#keyword_name").text() + ': ';
            var keywordValue = $(this).find("#keyword_value").val();
            var keywordColor = $(this).find("#keyword_color").val();
            if (keywordId == 'first' || keywordId == 'remark') {
                keywordName = '';
            }
            html += '<p>'+keywordName+'<span style="color: ' + keywordColor + '; padding-bottom: 5px;">'+keywordValue+'</span></p>' + '<br>';
        });

        $('#preview_text').html(html);
    }

    (function() {

        preview();

        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('input[name=color]').colpick({
            onSubmit: function(hsb, color, rgb, target) {
                var jo = $(target);
                if(color) {
                    color = '#' + color;
                    jo.attr('value', color);
                    jo.css('background-color', color);
                } else {
                    jo.attr('value', '#000000');
                    jo.css('background-color', '#000000');
                }
                jo.colpickHide();
            }
        });

        win.find('button.ok').click(function() {
            var values = {};
            var id = win.find('input[name=id]').val();
            var weixinmpId = win.find('input[name=weixinmpId]').val();
            var variable = $('#variable').val();
            var mpCode = win.find('input[name=mpCode]').val();
            var isActive = $('input[name=isActive]:checked').val();
            values["id"] = id;
            values["weixinmpId"] = weixinmpId;
            values["variable"] = variable;
            values["mpCode"] = mpCode;
            values["isActive"] = isActive;

            var detailList = [];
            $(".ui_table .keyword").each(function() {
                var detailMap = {};
                var detailId = $(this).attr("keyword_id");
                var keywordValue = $(this).find("#keyword_value").val();
                var keywordColor = $(this).find("#keyword_color").val();
                detailMap["detailId"] = detailId;
                detailMap["weixinmpId"] = weixinmpId;
                detailMap["templateId"] = id;
                detailMap["keywordValue"] = keywordValue;
                detailMap["color"] = keywordColor;
                detailList.push(detailMap);
            });
            values["detailList"] = detailList;
            $.post('${contextPath}/security/basic/mp_push_message_template/update.htm', {
                data : $.toJSON(values)
            }, function(json) {
            <@app.json_jump/>
                if(json.success) {
                    $.messager.alert('提示信息', '操作成功', 'info');
                    win.window('close');
                } else {
                    $.messager.alert('提示信息', json.message, 'info');
                }
            }, 'json');

        });
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
