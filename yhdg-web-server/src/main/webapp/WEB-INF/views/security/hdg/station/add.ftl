<script>
    $.extend($.fn.validatebox.defaults.rules, {
        mobile: {
            validator: function (value) {
                if (value != "") {
                    return /^1\d{10}$/.test(value);
                }

            },
            message: '请正确输入手机号码'
        }
    });
</script>
<div class="popup_body" style="padding-left:50px;padding-top: 40px;font-size: 14px;">
    <div class="tab_item" style="display:block;">
        <div class="ui_table">
            <form method="post">
                <input type="hidden" name="id" value="${id}">
                <input type="hidden" name="lng">
                <input type="hidden" name="lat" >
                <input type="hidden" name="geoHash" >
                <input type="hidden" name="street" >
                <input type="hidden" name="provinceName">
                <input type="hidden" name="cityName">
                <input type="hidden" name="districtName">
                <input type="hidden" name="streetName">
                <input type="hidden" name="streetNumber">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="90" align="left">*运营商：</td>
                        <td>
                            <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                                   editable="true" style="width: 255px; height: 28px;"
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:true,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                    }
                                "
                            >
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="left">*站点名称：</td>
                        <td><input type="text" class="text easyui-validatebox" required="true" maxlength="30"  style="width: 242px; height: 28px;" required="true"
                                   name="stationName" value=""/></td>
                    </tr>
                    <tr>
                        <td width="70" align="left">*站点地址：</td>
                        <td colspan="2"><input type="text" class="text easyui-validatebox" name="address" readonly required="true" maxlength="50" style="width: 275px;"/>&nbsp;</td>
                        <td>
                            <a href="javascript:getLocation()">
                                <div style="width: 50px;height: 33px;line-height: 33px;text-align: center;color: #FFFFFF;font-size: 14px;margin-left: 20px;background-color: #556bd8;border-radius: 6px;">定位
                                </div>
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="left">*站点联系人：</td>
                        <td><input type="text" class="text easyui-validatebox" maxlength="11" style="width: 275px; height: 28px;" required="true"
                                   name="linkname" value=""/></td>
                    </tr>
                    <tr>
                        <td width="120" align="left">*站点联系电话：</td>
                        <td><input type="text" class="text easyui-validatebox" maxlength="11" style="width: 275px; height: 28px;" validType="mobile[]" required="true"
                                   name="tel" value=""/></td>
                    </tr>
                    <td align="left">*营业时间：</td>
                    <td><input type="text" class="text easyui-timespinner" maxlength="5" data-options="showSeconds:false" style="width:103px; height:28px;" name="beginTime"/>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  <input type="text" class="text easyui-timespinner" maxlength="5" data-options="showSeconds:false,min:'00:01',max:'23:59' " style="width:103px; height:28px;" name="endTime"/></td>
                    <tr>
                        <td align="left">*是否启用：</td>
                        <td>
                            <select name="activeStatus" class="easyui-combobox" editable="false"
                                    style="width: 173px; height: 28px;">
                            <#list activeStatusEnum as e>
                                <option value="${e.getValue()}">${e.getName()}</option>
                            </#list>
                            </select>
                        </td>
                    </tr>
                    <tr style="height: 120px;"></tr>
                </table>
            </form>
        </div>
    </div>
</div>
<div class="popup_btn">
    <input type="hidden" value="false" id="committed"/>
    <button class="btn btn_red station_ok">确定</button>
    <button class="btn btn_border station_close">关闭</button>
</div>

<script>

    (function () {
        var win = $('#${pid}'), form = win.find('form');

        win.find('button.station_ok').click(function () {
            if (!form.form('validate')) {
                return false;
            }

            var isCommitted = $("#committed").val();

            if(isCommitted=="false") {
                var beginTime = win.find('input[name=beginTime]').val();
                var endTime = win.find('input[name=endTime]').val();
                var beginTimeNumber = win.find('input[name=beginTime]').timespinner('getValue');
                var endTimeNumber = win.find('input[name=endTime]').timespinner('getValue');
                if(beginTimeNumber > endTimeNumber) {
                    var time = beginTime;
                    beginTime = endTime;
                    endTime = time;
                }
                var workTime = beginTime + "-" + endTime;
                var reg = /^(20|21|22|23|[0-1]\d):[0-5]\d-(20|21|22|23|[0-1]\d):[0-5]\d$/;
                var regExp = new RegExp(reg);
                //只要不是都为空，就要进行格式检验
                if ((beginTime == null || beginTime == '') && (beginTime == null || endTime == '')) {

                } else {
                    if (!regExp.test(workTime)) {
                        $.messager.alert("提示信息", "时间格式不正确，正确格式为：08:00-17:00", "info");
                        return;
                    }
                }

                $("#committed").val("true");

                form.form('submit', {
                    url: '${contextPath}/security/hdg/station/create.htm',
                    onSubmit: function(param) {
                        param.workTime = workTime;
                        return true;
                    },
                    success: function (text) {
                        var json = $.evalJSON(text);
                    <@app.json_jump/>
                        if (json.success) {
                            $.messager.alert('提示信息', '操作成功', 'info');
                            win.window('close');
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                });
            }
        });
        win.find('button.station_close').click(function () {
            win.window('close');
        });
    })();

    function getLocation() {
        App.dialog.show({
            css: 'width:610px;height:525px;overflow:visible;',
            title: '地图定位',
            href: "${contextPath}/security/hdg/station/add_location.htm",
            windowData: {
                ok: function(config) {
                    var win = $('#${pid}');
                    win.find('input[name=lng]').val(config.shop.lng);
                    win.find('input[name=lat]').val(config.shop.lat);
                    win.find('input[name=address]').val(config.shop.address);
                    win.find('input[name=street]').val(config.shop.street);
                    win.find('input[name=provinceName]').val(config.shop.provinceName);
                    win.find('input[name=cityName]').val(config.shop.cityName);
                    win.find('input[name=districtName]').val(config.shop.districtName);
                    win.find('input[name=streetName]').val(config.shop.streetName);
                    win.find('input[name=streetNumber]').val(config.shop.streetNumber);
                }
            }
        });
    }

</script>


