<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="faultType" value="${(entity.faultType)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">处理人：</td>
                    <td><input type="text" class="text easyui-validatebox" id="handler_name_${pid}" name="handlerName" maxlength="40" readonly value="${(Session['SESSION_KEY_USER'].username)!''}" /></td>
                </tr>
                <tr>
                    <td width="70" align="right">处理时间：</td>
                    <td><input id="handle_time_${pid}" name="handleTime" class="easyui-datetimebox"  style="width:184px;height:28px " required="required" value="time1" ></td>
                </tr>
                <tr>
                    <td align="right">创建时间：</td>
                    <td><input  id="create_time_${pid}"type="text" class="text easyui-datetimebox" style="width:184px;height:28px " value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"/></td>
                </tr>
                <tr>
                  <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:330px;" name="handleMemo" maxlength="200"></textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    Date.prototype.format = function(fmt) {
        var o = {
            "M+" : this.getMonth()+1,                 //月份
            "d+" : this.getDate(),                    //日
            "h+" : this.getHours(),                   //小时
            "m+" : this.getMinutes(),                 //分
            "s+" : this.getSeconds(),                 //秒
            "q+" : Math.floor((this.getMonth()+3)/3), //季度
            "S"  : this.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt)) {
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        }
        for(var k in o) {
            if(new RegExp("("+ k +")").test(fmt)){
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
            }
        }
        return fmt;
    }
    var time1 = new Date().format("yyyy-MM-dd hh:mm:ss");


    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/hdg/fault_log/update.htm',
                onSubmit: function() {
                    var queryBeginTime = $('#handle_time_${pid}').datetimebox('getValue');
                    var queryEndTime = $('#create_time_${pid}').datetimebox('getValue');

                    if(queryBeginTime <= queryEndTime){
                        $.messager.alert('提示信息', '处理日期必须大于创建日期', 'info');
                        return false;
                    }
                    return true;
                },
                success: function(text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
