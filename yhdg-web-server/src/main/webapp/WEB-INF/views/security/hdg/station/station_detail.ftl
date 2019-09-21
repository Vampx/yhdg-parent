<@app.html>
    <@app.head>
    <style type="text/css">
        .main .left_bar {
            height: 0%;
        }
        .param-three .combo-arrow{
            margin-top: 20px;
            margin-left: 10px;
        }
    </style>
    <script>
        $.extend($.fn.validatebox.defaults.rules, {
            positiveInt:{//验证整数
                validator:function(value,param){
                    if (value){
                        return /^[-\+]?\d+$/.test(value);
                    } else {
                        return true;
                    }
                },
                message:'只能输入整数.'
            }
        });
    </script>
    </@app.head>
    <@app.body>
        <#function show_temp temp >
            <#return (temp - 2731) / 10 >
        </#function>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <div class="left_bar">
                <div class="nav">
                    <@app.menu_index/>
                </div>
            </div>

            <div class="c-detail">
                <div class="c-detail-up c-detail-up-site">
                    <div class="c-d-tips">
                        <span><i></i>站点管理</span>
                        <button class="btn btn_blue_1" id="back" >返回</button>
                    </div>
                    <form id="station_parameter" >
                        <div class="c-d-two c-d-two-site">
                            <li>
                                <p><span>站点编号：</span>${(entity.id)!''}</p>
                                <p><span>*站点名称：</span><input type="text" name="stationName"   value="${(entity.stationName)!''}"></p>
                            </li>
                            <li>
                                <p><span>*站点地址：</span><input type="text" style="width: 320px" name="address"  value="${(entity.address)!''}"></p>
                                <a href="javascript:getLocation()" class="pio">定位</a>
                                <div>
                                    <p><span>经纬度：</span><span id="lng_lat">${(entity.lng)!''}/${(entity.lat)!''}</span></p>
                                    <p><span>*营业时间：</span>
                                        <input type="text" class="text easyui-timespinner" value="${(beginTime)!''}" maxlength="5"
                                               data-options="showSeconds:false" style="width:103px; height:28px;" name="beginTime"/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <input type="text" class="text easyui-timespinner" value="${(endTime)!''}" maxlength="5"
                                               data-options="showSeconds:false,min:'00:01',max:'23:59' "
                                               style="width:103px; height:28px;" name="endTime"/>
                                    </p>
                                </div>
                            </li>
                            <li>
                                <p><span>*站点联系人：</span><input type="text" name="linkname"  validType="mobile[]" value="${(entity.linkname)!''}"></p>
                                <p><span>*站点电话：</span><input type="text" name="tel"   value="${(entity.tel)!''}"></p>
                            </li>
                            <li>
                                <p>
                                    <span>拓展人员：</span><input type="text" name="stationBizUserId" value="${(fullnameList)!''}">&nbsp;&nbsp;&nbsp;
                                    <a href="javascript:deleteBizUser()" style="font-weight: bold;padding-left: 3px; font-size: 12px">解绑</a>
                                </p>
                                <p>
                                    <span>*站点状态：</span>
                                    <select name="activeStatus" id="active_status" class="easyui-combobox"
                                            editable="false"
                                            style="width: 173px; height: 28px;">
                                        <#list activeStatusEnum as e>
                                            <option value="${e.getValue()}"
                                                    <#if entity.activeStatus?? && entity.activeStatus == e.getValue()>selected</#if>>${e.getName()}</option>
                                        </#list>
                                    </select>
                                </p>
                            </li>
                            <li>
                                <p>
                                    <span>二维码地址：</span>
                                    <textarea style="width:434px;height:20px;line-height: 20px;" maxlength="450" readonly >${(qrCodeAddress)!''}</textarea>
                                </p>
                            </li>
                        </div>
                    </form>
                    <button class="but" id="station_update">保存</button>
                    <div class="c-d-three-tabs c-d-three-tabs-site">
                        <span class="active" data-id="0">余额管理</span>
                        <span data-id="1" id="pay_people">收费方式</span>
                        <span data-id="2" id="distribution">分成管理</span>
                        <span data-id="3" id="station_role">站点角色</span>
                        <span data-id="4" id="station_user">站点账户</span>

                    </div>
                </div>
                <div class="c-detail-down c-detail-down-site">
                    <div class="c-d-three-0">

                        <div class="cdtc-right" style="height: 520px;">
                            <div class="cdtc-right-one cdtc-right-one-site">
                                当前余额：<span style="color: #FF0000">${(entity.balance)!''}元</span>
                            </div>

                            <div id="station_in_out_money">
                                <#include 'station_in_out_money.ftl'>
                            </div>

                        </div>
                    </div>

                    <div id="setting_pay_people">
                        <#include 'setting_pay_people.ftl'>
                    </div>

                    <div id="setting_station_distribution">
                        <#include 'setting_station_distribution.ftl'>
                    </div>

                    <div id="setting_station_role">
                        <#include 'setting_station_role.ftl'>
                    </div>

                    <div id="setting_station_user">
                        <#include 'setting_station_user.ftl'>
                    </div>

                </div>
            </div>

        </div>
        </@app.container>
    </@app.body>
</@app.html>
    <script>

        $("body").on("input",".number_money",function(){
            var reg = $(this).val().match(/\d+\.?\d{0,2}/);
            var txt = '';
            if (reg != null) {
                txt = reg[0];
            }
            $(this).val(txt);
        });

        $("body").on("input",".number_percent",function(){
            var reg = $(this).val().match(/\d+\.?\d{0,2}/);
            var txt = '';
            if (reg != null) {
                txt = reg[0];
            }
            $(this).val(txt);
        });

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

        $('.c-d-three-tabs').on('click', 'span', (e) => {
            $('.c-d-three-tabs span').attr('class', '');
        $('.c-d-three-tabs span').eq(e.currentTarget.dataset.id).attr('class', 'active');
        $('.c-d-three-0').hide()
        $('.c-d-three-1').hide()
        $('.c-d-three-2').hide()
        $('.c-d-three-3').hide()
        $('.c-d-three-4').hide()
        $('.c-d-three-' + e.currentTarget.dataset.id).show();
        });

        //加载收款人信息
        $.post('${contextPath}/security/hdg/station/setting_pay_people.htm', {
            stationId: '${(entity.id)!''}'
        }, function (html) {
            $("#setting_pay_people").html(html);
        }, 'html');

        //加载站点分成
        $.post('${contextPath}/security/hdg/station/setting_station_distribution.htm', {
            stationId: '${(entity.id)!''}'
        }, function (html) {
            $("#setting_station_distribution").html(html);
        }, 'html');

        showStationInOutMoneyTable();

        $('#station_role').click(function () {
            $('.c-d-three-3').show();
            showStationRoleTable();
        });

        $('#station_user').click(function () {
            $('.c-d-three-4').show();
            showStationUserTable();
        });

        $('input[name=stationBizUserId]').click(function() {
            selectUser(${(entity.agentId)!''});
        });

    </script>
    <script type="text/javascript">

        $(function () {

            var jform = $('form');
            var form = jform[0];

            var beginTime = $('#station_parameter').find('input[name=beginTime]').val();
            var endTime = $('#station_parameter').find('input[name=endTime]').val();
            var beginTimeNumber = $('#station_parameter').find('input[name=beginTime]').timespinner('getValue');
            var endTimeNumber = $('#station_parameter').find('input[name=endTime]').timespinner('getValue');
            if (beginTimeNumber > endTimeNumber) {
                var time = beginTime;
                beginTime = endTime;
                endTime = time;
            }
            var workTime = beginTime + "-" + endTime;
            $('#station_update').click(function () {
               $.messager.confirm('提示信息', '确认修改站点参数?', function (ok) {
                    if(ok) {
                        var ok = function () {
                            if (!jform.form('validate')) {
                                return false;
                            }
                            var success = true;
                            var values = {
                                id: '${(entity.id)!''}',
                                stationName: form.stationName.value,
                                linkname: form.linkname.value,
                                tel: form.tel.value,
                                workTime: workTime,
                                activeStatus: form.activeStatus.value
                            };

                            var reg = /^(20|21|22|23|[0-1]\d):[0-5]\d-(20|21|22|23|[0-1]\d):[0-5]\d$/;
                            var regExp = new RegExp(reg);
                            //只要不是都为空，就要进行格式检验
                            if ((beginTime == null || beginTime == '') && (endTime == null || endTime == '')) {

                            } else {
                                if (!regExp.test(workTime)) {
                                    $.messager.alert("提示信息", "时间格式不正确，正确格式为：08:00-17:00", "info");
                                    return;
                                }
                            }

                            $.ajax({
                                cache: false,
                                async: false,
                                type: 'POST',
                                url: '${contextPath}/security/hdg/station/update_basic.htm',
                                dataType: 'json',
                                data: values,
                                success: function (json) {
                                <@app.json_jump/>
                                    if (json.success) {
                                    } else {
                                        $.messager.alert('提示信息', json.message, 'info');
                                        success = false;
                                    }
                                }
                            });

                            return success;
                        };

                        $('#station_parameter').data('ok', ok);

                        var go = $('#station_parameter').data('ok')();
                        if(go) {
                            //刷新或后退
                            window.location.href = "${contextPath}/security/hdg/station/index.htm";
                        }
                    }
                });
            });

        });

        $('.nav').hide();
        $('.main .index_con').css("left", 0);

        $('#back').click(function () {
            window.close();
        });

        function getLocation() {
            App.dialog.show({
                css: 'width:610px;height:525px;overflow:visible;',
                title: '地图定位',
                href: "${contextPath}/security/hdg/station/edit_location.htm?id=${(entity.id)!''}",
                windowData: {
                    ok: function(config) {
                        $('#lng_lat').html(config.values.lng + "/" + config.values.lat)
                   }
                },
                event: {
                    onClose: function () {

                    }
                }
            });
        }



        function selectUser(agentId) {
            App.dialog.show({
                css: 'width:680px;height:530px;',
                title: '选择人员',
                href: "${contextPath}/security/hdg/station_biz_user/select_biz_user.htm?agentId="+agentId,
                windowData: {
                    ok: function (rows) {
                        var ids = '', fullnameList = '';
                        if (rows.length > 0) {
                            for (var i = 0; i < rows.length; i++) {
                                ids += rows[i].userId + ',';
                                fullnameList += rows[i].fullname + ',';
                            }
                            ids = ids.substring(0, ids.lastIndexOf(','));
                            var values = {
                                stationId: ${(entity.id)!''},
                                ids: ids
                            };
                            $.ajax({
                                cache: false,
                                async: false,
                                type: 'POST',
                                url: '${contextPath}/security/hdg/station_biz_user/update_station_biz.htm',
                                dataType: 'json',
                                data: values,
                                success: function (json) {
                                <@app.json_jump/>
                                    if (json.success) {
                                        //$('input[name=stationBizUserId]').val(fullnameList);
                                        //更新拓展人员
                                        findBizUser();
                                    } else {
                                        $.messager.alert('提示信息', json.message, 'info');
                                    }
                                }
                            });
                            return true;
                        } else {
                            $.messager.alert('提示信息', '请选择拓展人员', 'info');
                            return false;
                        }
                    }
                },
                event: {
                    onClose: function() {
                    }
                }
            });
        }

        function findBizUser(taskId) {
            $.ajax({
                type: 'POST',
                url: '${contextPath}/security/hdg/station/find_biz_user.htm',
                dataType: 'json',
                data: {
                    stationId: ${(entity.id)!''}
                },
                success: function (json) {
                    if (json.success) {
                        var data = json.message;
                        $('input[name=stationBizUserId]').val(data);
                    }
                }
            });
        };

        function deleteBizUser() {
            $.messager.confirm('提示信息', '确认解绑拓展人员?', function(ok) {
                if(ok) {
                    var values = {
                        stationId: ${(entity.id)!''}
                    };
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/hdg/station/delete_station_user.htm',
                        dataType: 'json',
                        data: values,
                        success: function (json) {
                        <@app.json_jump/>
                            if (json.success) {
                                $('input[name=stationBizUserId]').val("");
                            }
                        }
                    });
                }
            });
        }

</script>
