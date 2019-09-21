
<div class="popup_body clearfi" style="padding-left:10px;padding-top: 20px;font-size: 14px;min-height: 85%;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" id="count_id_${pid}" name="countId" value="${(countId)!''}"/ >
            <div>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="110" align="left"><span style="color: #FF4242">*</span>分期：</td>
                    <td>
                        <select  class="easyui-combobox" id="select_order_status" style="height:26px; line-height:26px;" data-options="required:true">
                            <#list StagingTime as e>
                                <option value="${e.getValue()}"
                                    <#if e.getValue()==num>selected="selected" </#if>>${e.getName()}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="110" align="left"><span style="color: #FF4242">*</span>手续费类型：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio_feeType" name="feeType_1" Onchange="radio_is(this)" id="fee_type_1_1" <#if feeType??&&feeType==1 >checked</#if>  value="1" />
                                  <label for="fee_type_1_1">分期金额费率</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio_feeType" name="feeType_1" Onchange="radio_is(this)" <#if feeType??&&feeType==2 >checked</#if>
                                   value="2" id="fee_type_2_1"/><label for="fee_type_2_1">固定</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio_feeType" name="feeType_1" Onchange="radio_is(this)" <#if feeType??&&feeType==3 >checked</#if>
                                   value="3" id="fee_type_3_1"/>
                                  <label for="fee_type_3_1">无手续费</label>
                        </span>
                    </td>
                </tr>
                <tr id="fee_${pid}">
                    <td width="110" align="left"><span style="color: #FF4242">*</span><span><#if feeType==1>手续费(%)：<#elseif feeType==2>手续费(元)：<#else >手续费：</#if></span></td>
                    <td>
                        <input type="text" class="text easyui-validatebox" validType="integerdobel['#fee_money_${pid}']" maxlength="10" style="width:245px;height:28px " id="fee_money_${pid}"
                               name="feeMoney" value="${(feeMoney)?string('0.00')}" />
                        <input type="text" class="text easyui-validatebox" validType="integerdobel['#fee_percentage_${pid}']" maxlength="4" style="width:245px;height:28px " id="fee_percentage_${pid}"
                               name="feePercentage" value="${(feePercentage)?string('0.00')}" />
                    </td>
                </tr>
                <tr>
                    <td  width="110" align="left">分期：</td>
                    <td>
                        <table id="exchange_installment_count_detail_table">
                            <#list minForegiftPercentages as feeType>
                                <tr>
                                    <td>
                                        <fieldset style="padding: 12px;">
                                            <table>
                                                    <tr>
                                                        <td>
                                                            <#list StagingTime as e>
                                                                <#if e.getValue()==customNums[feeType_index]><span style="font-weight: bold">第${e.getName()}</span></#if>
                                                            </#list>
                                                            <#if customNums[feeType_index]==1><span style="font-weight: bold">第一期</span></#if>
                                                        </td>
                                                    </tr>
                                                    <tr id="min_foregift_${pid}">
                                                        <td width="120" align="left">押金最低金额(%)：</td>
                                                        <td>
                                                            <input type="text" class="text easyui-validatebox" <#if feeType_index+1==minForegiftPercentages?size>readonly</#if> maxlength="3" style="width:245px;height:28px " id="min_foregift_percentage_${pid}"
                                                                   name="minForegiftPercentage" value="${(minForegiftPercentages[feeType_index])!0}"/>
                                                        </td>
                                                    </tr>
                                                    <tr id="min_packet_period_${pid}">
                                                        <td width="120" align="left">租金最低金额(%)：</td>
                                                        <td>
                                                            <input type="text" class="text easyui-validatebox" <#if feeType_index+1==minForegiftPercentages?size>readonly</#if> maxlength="3" style="width:245px;height:28px " id="min_packet_period_percentage_${pid}"
                                                                   name="minPacketPeriodPercentage" value="${(minPacketPeriodPercentages[feeType_index])!0}"/>
                                                        </td>
                                                    </tr>
                                            </table>
                                        </fieldset>
                                    </td>
                                </tr>
                            </#list>
                        </table>
                    </td>
                </tr>
            </table>
            </div>
        </form>
    </div>
    <div style="height: 47px;"></div>
</div>
<div class="popup_btn" style="position: absolute; bottom: 0px; width: 100%; padding: 12px 0px;">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close" style="margin-right: 16px;">关闭</button>
</div>

    <script>
        (function() {
            var win = $('#${pid}');
            var countDetailTable = $("#exchange_installment_count_detail_table");
            var lengthNum =0;
            var val = $('input[name="feeType_1"]:checked').val();
            feeType(val);
            function feeType(val) {
                if(val == 1){
                    $('#fee_${pid}').show();
                    $('#fee_money_${pid}').hide();
                    $('#fee_percentage_${pid}').show();
                    $('#fee_${pid}').find('td').eq(0).find('span').eq(1).html("手续费(%)：");
                }else if(val == 2){
                    $('#fee_${pid}').show();
                    $('#fee_money_${pid}').show();
                    $('#fee_percentage_${pid}').hide();
                    $('#fee_${pid}').find('td').eq(0).find('span').eq(1).html("手续费(元)：");
                }else {
                    $('#fee_${pid}').hide();
                    $('#fee_money_${pid}').hide();
                    $('#fee_percentage_${pid}').hide();
                }
            }
            $("#select_order_status").combobox({
                onSelect:function(record) {
                    var val = $('#select_order_status').combobox('getValue');
                    countDetailTableAdd(val);
                }
            });
            function countDetailTableAdd(num) {
                var val = num;
                var html =
                        '    <tr>\n' +
                        '        <td>\n' +
                        '            <fieldset style="padding: 12px;">\n' +
                        '                <table>\n' +
                        '                    <tr>\n' +
                        '                     <td>\n' +
                        '                          <span style="font-weight: bold">第order_status期</span>\n' +
                        '                     </td>\n' +
                        '                    </tr>\n' +
                        '                    <tr id="min_foregift_${pid}">\n' +
                        '                      <td width="120" align="left">押金最低金额(%)：</td>\n' +
                        '                      <td>\n' +
                        '                           <input type="text" class="text easyui-validatebox" maxlength="3" style="width:245px;height:28px " id="min_foregift_percentage_${pid}" name="minForegiftPercentage" value=""/>\n' +
                        '                       </td>\n' +
                        '                    </tr>\n' +
                        '                    <tr id="min_packet_period_${pid}">\n' +
                        '                       <td width="120" align="left">租金最低金额(%)：</td>\n' +
                        '                       <td>\n' +
                        '                           <input type="text" class="text easyui-validatebox" maxlength="3" style="width:245px;height:28px " id="min_packet_period_percentage_${pid}" name="minPacketPeriodPercentage" value=""/>\n' +
                        '                      </td>\n' +
                        '                    </tr>\n' +
                        '                </table>\n' +
                        '             </fieldset>\n' +
                        '         </td>\n' +
                        '     </tr>\n';
                var countDetailTable = $("#exchange_installment_count_detail_table");
                countDetailTable.children().children('tr:last').find('table').find('input').each(function () {
                    $(this).removeAttr("readonly");
                    $(this).val('');
                });
                var trlength = countDetailTable.children().children().length;
                if (trlength > val) {
                    var trlengths = trlength - val;
                    for (var i = 0; i < trlengths; i++) {
                        countDetailTable.children().children().eq(trlength-i-1).remove()
                    }
                } else if (trlength < val) {
                    var trlengths = val - trlength;
                    for (var i = 0; i < trlengths; i++) {
                        var htmls=html.replace("第order_status期","第"+convertToChinese(trlength+i+1)+"期");
                        countDetailTable.children().append(htmls);
                    }
                }
                countDetailTable.children().children('tr:last').find('table').find('input').each(function () {
                    $(this).val(0);
                    $(this).attr("readonly","readonly")
                });
            }
            

            win.find('button.close').click(function () {
                win.window('close');
            });

            win.find('button.ok').click(function () {
                if(verification()){
                    var val = $('#select_order_status').combobox('getValue');
                    var feeType = $('input[name="feeType_1"]:checked').val();
                    var feeMoney = $('#fee_money_${pid}').val();
                    var feePercentage = $('#fee_percentage_${pid}').val();
                    var countId=$('#count_id_${pid}').val();
                    var wins = $('#${urlPid}');
                    var num=1;
                    var countDetailTable=$("#exchange_installment_count_detail_table");
                    var minForegiftPercentages=[];var minPacketPeriodPercentages=[];
                    countDetailTable.find('table').each(function () {
                        var minForegiftPercentage = $(this).find('#min_foregift_percentage_${pid}').val();
                        var minPacketPeriodPercentage = $(this).find('#min_packet_period_percentage_${pid}').val();
                        minForegiftPercentages.push(minForegiftPercentage== ''?0:parseFloat(minForegiftPercentage));
                        minPacketPeriodPercentages.push(minPacketPeriodPercentage== ''?0:parseFloat(minPacketPeriodPercentage));
                        num++;
                    });
                    var boolean =true;
                    if(countId != null &&countId !="" &&countId !=undefined){
                        $.ajax({
                            cache: false,
                            async: false,
                            type: 'GET',
                            url: '${contextPath}/security/hdg/exchange_installment_count_detail/update_installment_count_detail.htm',
                            data: {
                                countId:countId,
                                feeType:feeType,
                                feeMoney:feeMoney,
                                feePercentage:feePercentage,
                                minForegiftPercentages:minForegiftPercentages,
                                minPacketPeriodPercentages:minPacketPeriodPercentages
                            },
                            dataType: 'json',
                            success: function (json) {
                            <@app.json_jump/>
                                if (json.success) {
                                    $.messager.alert('提示信息', json.message, 'info');

                                } else {
                                    boolean = false
                                    $.messager.alert('提示信息', json.message, 'info');
                                }
                            }
                        })
                    }
                    if(boolean){
                        wins.data('countId', countId);
                        wins.data('selectOrderStatus', val);
                        wins.data('feeType', feeType);
                        wins.data('feeMoney', feeMoney);
                        wins.data('feePercentage', feePercentage);
                        wins.data('minForegiftPercentages', minForegiftPercentages);
                        wins.data('minPacketPeriodPercentages', minPacketPeriodPercentages);
                        win.window('close');
                    }
                }

            });
        })();
        function radio_is(obj) {
            feeType($(obj).val());
        }
        function feeType(val){
            if(val == 1){
                $('#fee_${pid}').show();
                $('#fee_money_${pid}').hide();
                $('#fee_percentage_${pid}').show();
                $('#fee_${pid}').find('td').eq(0).find('span').eq(1).html("手续费(%)：");
            }else if(val == 2){
                $('#fee_${pid}').show();
                $('#fee_money_${pid}').show();
                $('#fee_percentage_${pid}').hide();
                $('#fee_${pid}').find('td').eq(0).find('span').eq(1).html("手续费(元)：");
            }else {
                $('#fee_${pid}').hide();
                $('#fee_money_${pid}').hide();
                $('#fee_percentage_${pid}').hide();
            }
        }
        function convertToChinese(num) {
            var N = [
                "零", "一", "二", "三", "四", "五", "六", "七", "八", "九"
            ];
            var str = num.toString();
            var len = num.toString().length;
            var C_Num = [];
            for (var i = 0; i < len; i++) {
                C_Num.push(N[str.charAt(i)]);
            }
            return C_Num.join('');
        }
        function verification() {

            var success = true;
            var lengthNum = 1;
            var count = $('#select_order_status').combobox('getValue');
            var countDetailTable=$("#exchange_installment_count_detail_table");
            var val = $('input[name="feeType_1"]:checked').val();
            var feeMoney = $('#fee_money_${pid}').val();
            var feePercentage = $('#fee_percentage_${pid}').val();
            if(count ==""|| count==null){
                success=false;
                $.messager.alert('提示信息', '分期期数不能为空', 'info');
                return success;

            }else if(val ==""|| val==null) {
                success = false;
                $.messager.alert('提示信息', '手续费类型不能为空', 'info');
                return success;
            }
            if(val==1){
                if(feePercentage==null||feePercentage==""||feePercentage==0||feePercentage==undefined){
                    success = false;
                    $.messager.alert('提示信息', '手续费费率不能为空', 'info');
                    return success;
                }
            }else if(val==2){
                if(feeMoney==null||feeMoney==""||feeMoney==0||feeMoney==undefined){
                    success = false;
                    $.messager.alert('提示信息', '手续费金额不能为空', 'info');
                    return success;
                }
            }
            var minForegiftPercentageNum =0;
            var minPacketPeriodPercentageNum =0;
            var patrm1 = /^(?:[1-9]?\d|100)$/;
            
            countDetailTable.children().children().each(function () {
                var minForegiftPercentage = $(this).find('#min_foregift_percentage_${pid}').val();
                var minPacketPeriodPercentage = $(this).find('#min_packet_period_percentage_${pid}').val();
                if(minForegiftPercentage ==""|| minForegiftPercentage==null){
                    success = false;
                    $.messager.alert('提示信息', '第'+convertToChinese(lengthNum)+'期押金最低金额费率不能为空', 'info');
                    return success;
                }else if(!patrm1.test(minForegiftPercentage)){
                success = false;
                $.messager.alert('提示信息', '第'+convertToChinese(lengthNum)+'期押金最低金额费率必须为整数并且不能大于100', 'info');
                return success;
                }
                if(minPacketPeriodPercentage ==""|| minPacketPeriodPercentage==null){
                    success = false;
                    $.messager.alert('提示信息', '第'+convertToChinese(lengthNum)+'期租金最低金额费率不能为空', 'info');
                    return success;
                }else if(!patrm1.test(minPacketPeriodPercentage)){
                    success = false;
                    $.messager.alert('提示信息', '第'+convertToChinese(lengthNum)+'期租金最低金额费率必须为整数并且不能大于100', 'info');
                    return success;
                }
                minForegiftPercentageNum+=Number(minForegiftPercentage);
                minPacketPeriodPercentageNum+=Number(minPacketPeriodPercentage);
                lengthNum++;
            });

            if(minForegiftPercentageNum>100){
                success = false;
                $.messager.alert('提示信息', '所有押金最低金额费率相加不能大于100', 'info');
                return success;
            }else if(minPacketPeriodPercentageNum>100){
                success = false;
                $.messager.alert('提示信息', '所有租金最低金额费率相加不能大于100', 'info');
                return success;
            }
            return success;
        }
    </script>