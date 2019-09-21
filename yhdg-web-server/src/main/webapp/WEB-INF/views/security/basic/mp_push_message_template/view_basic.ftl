<div class="tab_item" style="display:block;">
    <div class="ui_table">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">模板名称：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" value="${(entity.templateName)!''}" style="width: 172px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">公众号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly  value="${(entity.mpCode)!''}" style="width: 172px; height: 28px;"/></td>
                </tr>
                <#list detailList as detail>
                    <tr class="keyword" keyword_id="${(detail.id)!''}">
                        <td id="keywordName" align="right">${(detail.keywordName)!''}</td>
                        <td><input id="keywordValue" type="text" class="text" style="width: 450px;" value="${(detail.keywordValue)!''}"/></td>
                        <td><input id="keywordColor" type="text" class="text easyui-validatebox" style="background-color:${(detail.color)!''}; width: 25px;" value="${(detail.color)!''}"/></td>
                    </tr>
                </#list>
                <#--<tr>-->
                    <#--<td width="70" align="right">生活号：</td>-->
                    <#--<td><input type="text" class="text easyui-validatebox"  value="${(entity.fwCode)!''}" style="width: 172px; height: 28px;"/></td>-->
                <#--</tr>-->
                <tr>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="isActive_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if>  value="1"/><label for="isActive_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="isActive_0"  <#if entity.isActive?? && entity.isActive == 0>checked</#if> value="0"/><label for="isActive_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <button class="btn btn_blue" id="preview">生成预览</button>
                    </td>
                </tr>
            </table>
    </div>
    <table>
        <tr>
            <td>
            <div class="message_preview">
                <div class="message_preview_hd">
                    <p>预览</p>
                </div>
                <div class="message_preview_bd preview">
                </div>
                <div class="message_preview_ft">
                    <p>详情</p>
                </div>
            </div>
            </td>
            <td>
            <div class="message_preview">
                <div class="message_preview_hd">
                    <p>变量</p>
                </div>
                <div class="message_preview_bd">
                    <textarea style="width:250px;height: 160px;" readonly name="variable" id="variable" maxlength="512">${(entity.variable)!''}</textarea>
                </div>
            </div>
            </td>
        </tr>
    </table>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var ok = function() {
            var success = true;
            return success;
        };
        win.data('ok', ok);
        win.find('.ui_table #keywordColor').colpick({
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

        //点击生成预览
        $(".btn_blue").click(function() {
            var templateName = '${(entity.templateName)!''}';
            var date = new Date();

            var html = '<div class="message_preview_bd preview">' +
                    '<p>' + templateName + '</p>' +
                    '<p style="color: #999; padding-bottom: 5px;">'+ (date.getMonth() + 1) +'月'+date.getDate()+'日</p>';

            $(".ui_table .keyword").each(function(){
                var keywordId = $(this).attr("keyword_id");
                var keywordName = $(this).find("#keywordName").text() + ': ';
                var keywordValue = $(this).find("#keywordValue").val();
                var keywordColor = $(this).find("#keywordColor").val();
                if (keywordId == 'first' || keywordId == 'remark') {
                    keywordName = '';
                }
                html += '<p>'+keywordName+'<span style="color: ' + keywordColor + '; padding-bottom: 5px;">'+keywordValue+'</span></p>'
            });

            html += '</div>';

            $(".message_preview_bd.preview").html(html);

        });
    })();
</script>