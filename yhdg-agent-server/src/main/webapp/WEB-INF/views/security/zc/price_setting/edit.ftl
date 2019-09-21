<div class="popup_body" style="padding-left:50px;padding-top: 40px;font-size: 14px;">
    <div class="tab_item" style="display:block;">
        <div class="ui_table">
            <form method="post" action="javascript:void(0);">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="90" align="left">运营商：</td>
                        <td>
                            <input name="agentId" id="agentId" class="easyui-combotree" required="true" readonly
                                   editable="false" style="width: 182px; height: 28px;" value="${(entity.agentId)!''}"
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                    swich_agent();
                                    }
                                "
                            >
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="left">套餐名称：</td>
                        <td><input type="text" class="text easyui-validatebox" maxlength="40" name="settingName" required="true" value="${(entity.settingName)!''}" style="width:170px;height: 30px "/></td>
                    </tr>
                    <tr>
                        <td width="90" align="left">车辆型号：</td>
                        <td>
                            <input name="modelId" id="model_id" class="easyui-combotree" required="true"
                                   editable="false" style="width: 184px; height: 28px;"
                                   data-options="url:'${contextPath}/security/zc/vehicle_model/tree.htm?agentId=${entity.agentId}',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                    }" value="${(entity.modelId)!''}"/>
                        </td>
                    </tr>
                    <tr>
                        <td width="80" align="left">业务类型：</td>
                        <td>
                            <select class="easyui-combobox"  data-options="editable:false" required="true" name="category" readonly id="category_${pid}" style="width:180px;height: 30px ">
                            <#list CategoryEnum as s>
                                <option value="${s.getValue()}"
                                        <#if entity.category?? && entity.category == s.getValue()>selected</#if>>${s.getName()}</option>
                            </#list>
                            </select>
                        </td>
                    </tr>
                    <tr id="battery_count_${pid}">
                        <td width="70" align="left">电池数量：</td>
                        <td><input type="text" class="text easyui-validatebox number" maxlength="40" readonly name="batteryCount" value="${(entity.batteryCount)!''}" id="battery_num_${pid}" style="width:170px;height: 30px "/></td>
                    </tr>
                    <tr id="battery_type_${pid}">
                        <td align="left">电池型号：</td>
                        <td id="setting_battery_type">
                         <#include 'setting_battery_type.ftl'>
                        </td>
                    </tr>
                    <tr>
                        <td align="left">是否启用：</td>
                        <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive"
                                   <#if entity.isActive?? && entity.isActive == 1>checked</#if>
                                   value="1"/><label for="is_active_1">启用</label>
                        </span>
                            <span class="radio_box">
                            <input type="radio" class="radio" name="isActive"
                                   <#if entity.isActive?? && entity.isActive == 1><#else >checked</#if>
                                   value="0"/><label for="is_active_0">禁用</label>
                        </span>
                        </td>
                    </tr>
                    <tr>
                        <td align="left">车辆配置：</td>
                        <td colspan="2"><textarea style="width:250px;height:60px;" maxlength="450" name="vehicleName">${(entity.vehicleName)!''}</textarea></td>
                    </tr>
                    <tr>
                        <td align="left">套餐设置：</td>
                        <td></td>
                    </tr>
                    <tbody id="setting_rent_price">
                       <#include 'setting_rent_price.ftl'>
                    </tbody>
                    <tr>
                        <td width="70" align="left"></td>
                        <td><input type="button" class="btn btn-plus" style="width: 560px;height: 35px; color:#fff; border:1px solid; background: #40bbea;" value="+添加新套餐"/></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
   var category =  ${(entity.category)!0};
   if (category != 3) {
        //电池型号
        $.post('${contextPath}/security/zc/price_setting/setting_battery_type.htm', {
            category: ${(entity.category)!0},
            agentId: ${(entity.agentId)!0},
            priceSettingId: ${(entity.id)!0}
        }, function (html) {
            $("#setting_battery_type").html(html);
        }, 'html');
    } else {
       $.post('${contextPath}/security/zc/price_setting/setting_rent_price.htm', {
           batteryType: null,
           agentId: ${(entity.agentId)!0},
           priceSettingId: ${(entity.id)!0}
       }, function (html) {
           $("#setting_rent_price").html(html);
       }, 'html');
       $("#battery_count_${pid}").hide();
       $("#battery_type_${pid}").hide();
       $(".battery_foregift_price").hide();
    }

   function swich_agent() {
       var agentId = $('#agentId').combotree('getValue');
       var modelId = $('#model_id');
       modelId.combotree({
           url: "${contextPath}/security/zc/vehicle_model/tree.htm?agentId=" + agentId + ""
       });
       modelId.combotree('reload');
       find_hd_battery_count(agentId);
   }

   $('#category_${pid}').combobox({
       onSelect:function(){
           if($('#category_${pid}').combobox('getValue') == 3){
               $("#battery_count_${pid}").hide();
               $("#battery_type_${pid}").hide();
               $(".battery_foregift_price").hide();
           } else {
               $("#battery_count_${pid}").show();
               $("#battery_type_${pid}").show();
           }
           var agentId = $('#agentId').combotree('getValue');
           if ($('#category_${pid}').combobox('getValue') == 1) {
               find_hd_battery_count(agentId);
               setting_battery_type($('#category_${pid}').combobox('getValue'), agentId, ${(entity.id)!0});
           } else if ($('#category_${pid}').combobox('getValue') == 2) {
               find_zd_battery_count(agentId);
               setting_battery_type($('#category_${pid}').combobox('getValue'), agentId, ${(entity.id)!0});
           }
       }
   });

   function find_hd_battery_count(agentId) {
       $.ajax({
           type: 'POST',
           url: '${contextPath}/security/zc/price_setting/find_hd_battery_count.htm',
           dataType: 'json',
           data: {agentId: agentId},
           success: function (json) {
               if (json.success) {
                   var data = json.data;
                   $('#battery_num_${pid}').val(data);
               }
           }
       });
   };

   function find_zd_battery_count(agentId) {
       $.ajax({
           type: 'POST',
           url: '${contextPath}/security/zc/price_setting/find_zd_battery_count.htm',
           dataType: 'json',
           data: {agentId: agentId},
           success: function (json) {
               if (json.success) {
                   var data = json.data;
                   $('#battery_num_${pid}').val(data);
               }
           }
       });
   };

   function setting_battery_type(category, agentId, priceSettingId) {
       //电池型号
       $.post('${contextPath}/security/zc/price_setting/setting_battery_type.htm', {
           category: category,
           agentId: agentId,
           priceSettingId: priceSettingId
       }, function (html) {
           $("#setting_battery_type").html(html);
       }, 'html');
   };

    $("body").on("input",".number",function(){
        var reg = $(this).val().match(/\d+\.?\d{0,2}/);
        var txt = '';
        if (reg != null) {
            txt = reg[0];
        }
        $(this).val(txt);
    });

   $("body").on("input",".dayNumber",function(){
       var $val = $(this).val();
       $(this).val($val.replace(/[^\d]/g,''));
   });

    $("table").on("click",".btn-minus",function(){
        var parent = $(this).parents("tr");
        var priceId = $(this).attr("price_id");
        $.messager.confirm("提示信息", "确认删除?", function (ok) {
            if (ok) {
                if (priceId != undefined) {
                    $.post('${contextPath}/security/zc/rent_price/find_group_by_price.htm', {
                        id: priceId
                    }, function (json) {
                        if (json.success) {
                            $.post("${contextPath}/security/zc/rent_price/delete.htm?id=" + priceId, function (json) {
                                if (json.success) {
                                    $.messager.alert("提示信息", "删除成功", "info");
                                    parent.remove();
                                } else {
                                    $.messager.alert("提示信息", json.message, "info")
                                }
                            }, 'json');
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                } else {
                    parent.remove();
                }
            }
        });
    });

    $("table").on("click",".btn-plus",function(){
        var html= '  <tr>\n' +
                '                        <td align="left"></td>\n' +
                '                        <td>\n' +
                '                            <fieldset>\n' +
                '                                <div class="popup_body">\n' +
                '                                    <div class="tab_item" style="display:block;">\n' +
                '                                        <div class="ui_table">\n' +
                '                                            <table cellpadding="0" cellspacing="0">\n' +
                '                                                <tbody class="table_list">\n' +
                '                                                <tr>\n' +
                '                                                    <td width="80" align="left">套餐名称：</td>\n' +
                '                                                    <td><input type="hidden" name="priceId" value="0"><input type="text" class="text easyui-validatebox" maxlength="40" name="priceName" style="width:170px;height: 30px "/></td>\n' +
                '                                                </tr>\n' +
                '                                                <tr>\n' +
                '                                                    <td width="80" align="left">押金：</td>\n' +
                '                                                    <td><input type="text" class="text easyui-validatebox number" maxlength="10" name="foregiftPrice"  style="width:170px;height: 30px "/> 元</td>\n' +
                '                                                </tr>\n' +
                '                                                <tr>\n' +
                '                                                    <td width="80" align="left"></td>\n' +
                '                                                    <td>车辆：<input type="text" class="text easyui-validatebox number" maxlength="10" name="vehicleForegiftPrice" style="width:150px;height: 30px "/> 元</td>\n' +
                '                                                    <td class="battery_foregift_price">电池：<input type="text" class="text easyui-validatebox number" maxlength="10" name="batteryForegiftPrice" style="width:150px;height: 30px "/> 元</td>\n' +
                '                                                </tr>\n' +
                '                                                <tr>\n' +
                '                                                    <td width="80" align="left">租金：</td>\n' +
                '                                                    <td><input type="text" class="text easyui-validatebox number" maxlength="10" name="rentPrice" style="width:170px;height: 30px "/> 元</td>\n' +
                '                                                    <td><input type="text" class="text easyui-validatebox dayNumber" maxlength="3" name="dayCount" style="width:170px;height: 30px "/> 天</td>\n' +
                '                                                </tr>\n' +
                '                                                <tr>\n' +
                '                                                    <td width="80" align="left"></td>\n' +
                '                                                    <td>车辆：<input type="text" class="text easyui-validatebox number" maxlength="10" name="vehicleRentPrice" style="width:150px;height: 30px "/> 元</td>\n' +
                '                                                    <td class="battery_foregift_price">电池：<input type="text" class="text easyui-validatebox number" maxlength="10" name="batteryRentPrice" style="width:150px;height: 30px "/> 元</td>\n' +
                '                                                </tr>\n' +
                '                                                </tbody>\n' +
                '                                            </table>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                </div>\n' +
                '                            </fieldset>\n' +
                '                        </td>\n' +
                '                        <td><button class="btn btn_border btn-minus" style="width: 100px;height: 35px; background:red; color:#fff;">删除</button></td>\n' +
                '                    </tr>';
        $('tr:last').before(html);
        if (category == 3) {
            $(".battery_foregift_price").hide();
        }
    });

    $(function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function () {

            var settingName = win.find('input[name=settingName]').val();
            if ($.trim(settingName) == '') {
                $.messager.alert('提示信息', "请输入套餐名称", 'info');
                return false;
            }
            var batteryType = null;
            if ($('#category_${pid}').combobox('getValue') != 3) {
                batteryType = $(".zj_list").find(".selected").attr("battery_type");
                if (batteryType == undefined) {
                    $.messager.alert('提示信息', "请选择电池型号", 'info');
                    return false;
                }
            }
            //套餐名称集合
            var priceNameLength = $(".table_list").find('input[name = "priceName"]').length;
            var priceNameList = [];
            for(var i = 0; i < priceNameLength; i++){
                var priceName = $(".table_list").find('input[name = "priceName"]').eq(i).attr("value");
                priceNameList.push(priceName);
            }
            //押金集合
            var foregiftPriceLength = $(".table_list").find('input[name = "foregiftPrice"]').length;
            var foregiftPriceList = [];
            var foregiftPrice = 0;
            for(var i = 0; i < foregiftPriceLength; i++){
                foregiftPrice = $(".table_list").find('input[name = "foregiftPrice"]').eq(i).attr("value");
                foregiftPriceList.push(parseFloat(foregiftPrice));
            }
            //车辆押金集合
            var vehicleForegiftPriceLength = $(".table_list").find('input[name = "vehicleForegiftPrice"]').length;
            var vehicleForegiftPriceList = [];
            var vehicleForegiftPrice = 0;
            for(var i = 0; i < vehicleForegiftPriceLength; i++){
                vehicleForegiftPrice = $(".table_list").find('input[name = "vehicleForegiftPrice"]').eq(i).attr("value");
                vehicleForegiftPriceList.push(parseFloat(vehicleForegiftPrice));
            }

            var batteryForegiftPriceList = [];
            if (category != 3) {
                //车辆电池押金集合
                var batteryForegiftPriceLength = $(".table_list").find('input[name = "batteryForegiftPrice"]').length;
                var batteryForegiftPrice = 0;
                for(var i = 0; i < batteryForegiftPriceLength; i++){
                    batteryForegiftPrice = $(".table_list").find('input[name = "batteryForegiftPrice"]').eq(i).attr("value");
                    batteryForegiftPriceList.push(parseFloat(batteryForegiftPrice));
                }
            } else {
                batteryForegiftPriceList = null;
            }

            //车辆租金集合
            var rentPriceLength = $(".table_list").find('input[name = "rentPrice"]').length;
            var rentPriceList = [];
            var rentPrice = 0;
            for(var i = 0; i < rentPriceLength; i++){
                rentPrice = $(".table_list").find('input[name = "rentPrice"]').eq(i).attr("value");
                rentPriceList.push(parseFloat(rentPrice));
            }
            //车辆租金天数集合
            var dayCountLength = $(".table_list").find('input[name = "dayCount"]').length;
            var dayCountList = [];
            var dayCount = 0;
            for(var i = 0; i < dayCountLength; i++){
                dayCount = $(".table_list").find('input[name = "dayCount"]').eq(i).attr("value");
                dayCountList.push(dayCount);
            }

            //车辆租金集合
            var vehicleRentPriceLength = $(".table_list").find('input[name = "vehicleRentPrice"]').length;
            var vehicleRentPriceList = [];
            var vehicleRentPrice = 0;
            for(var i = 0; i < vehicleRentPriceLength; i++){
                vehicleRentPrice = $(".table_list").find('input[name = "vehicleRentPrice"]').eq(i).attr("value");
                vehicleRentPriceList.push(parseFloat(vehicleRentPrice));
            }

            var batteryRentPriceList = [];
            if (category != 3) {
                //车辆租金电池集合
                var batteryRentPriceLength = $(".table_list").find('input[name = "batteryRentPrice"]').length;
                var batteryRentPrice = 0;
                for(var i = 0; i < batteryRentPriceLength; i++){
                    batteryRentPrice = $(".table_list").find('input[name = "batteryRentPrice"]').eq(i).attr("value");
                    batteryRentPriceList.push(parseFloat(batteryRentPrice));
                }
            } else {
                batteryRentPriceList = null;
            }

            if($.trim(priceNameList[0]) == ''){
                $.messager.alert('提示信息', '请输入套餐名称', 'info');
                return false;
            }

            //小套餐id
            var priceIdLength = $(".table_list").find('input[name = "priceId"]').length;
            var priceIdList = [];
            var priceId = 0;
            for(var i = 0; i < priceIdLength; i++){
                priceId = $(".table_list").find('input[name = "priceId"]').eq(i).attr("value");
                priceIdList.push(priceId);
            }

            form.form('submit', {
                url: '${contextPath}/security/zc/price_setting/update.htm',
                onSubmit: function(param) {
                    param.id = ${(entity.id)!0};
                    param.batteryType = batteryType;
                    param.priceNameList = priceNameList;
                    param.foregiftPriceList = foregiftPriceList;
                    param.vehicleForegiftPriceList = vehicleForegiftPriceList;
                    param.batteryForegiftPriceList = batteryForegiftPriceList;
                    param.rentPriceList = rentPriceList;
                    param.dayCountList = dayCountList;
                    param.vehicleRentPriceList = vehicleRentPriceList;
                    param.batteryRentPriceList = batteryRentPriceList;
                    param.priceIdList = priceIdList;
                    return true;
                },
                success: function(text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function () {
            win.window('close');
        });

    })
</script>
