<div class="popup_body" style="padding-left:10px;padding-top: 20px;font-size: 14px;min-height: 69%;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" id="standard_staging_id" name="standardStagingId" value="${(settingId)!''}">
            <table id="exchange_standard_staging_table">
                <tr>
                    <td width="110" align="left"><span style="color: #990000">*</span>分期期数：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="2"
                               style="width:245px;height:28px " id="count_${pid}" data-options="required:true,validType:'integer'"
                               name="count" value=""/ ></td>
                </tr>
                <tr>
                    <td width="110" align="left"><span style="color: #990000">*</span>手续费类型：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="feeType" checked id="fee_type_1" value="1" />
                                  <label for="fee_type_1">费率</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="feeType" id="fee_type_2"
                                   value="2" /><label for="fee_type_2">固定手续费</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="feeType" id="fee_type_3" value="3" />
                                  <label for="fee_type_3">无手续费</label>
                        </span>
                    </td>
                </tr>
                <tr id="fee_${pid}">
                    <td width="110" align="left"><span style="color: #990000">*</span>手续费：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" validType="integerdobel['#fee_money_${pid}']" maxlength="10" data-options="min:0.00,precision:2" style="width:245px;height:28px " id="fee_money_${pid}"
                               name="feeMoney" value=""/>
                        <input type="text" class="text easyui-validatebox" validType="integerdobel['#fee_percentage_${pid}']" maxlength="4" data-options="min:0.00,precision:2" style="width:245px;height:28px " id="fee_percentage_${pid}"
                               name="feePercentage" value=""/>
                    </td>
                </tr>
            </table>
        </form>
        <b><span style="color: #990000; line-height: 22px; display: block; margin-top: 5px;"">注意：如果为费率，请填写百分之多少，如果为固定值，<br>表示不管分期金额多少，按照固定手续费收取</span></b>
    </div>
</div>
<div class="popup_btn" style="height: 5%;">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>

<script>
    (function () {
        var val = $('input[name="feeType"]:checked').val();
        feeType(val);
        var win = $('#${pid}');
        win.find('button.close').click(function () {
            win.window('close');
        });

        win.find('button.ok').click(function () {
            if(verification()){
                var settingId = $('#standard_staging_id').val();
                var wins = $('#${urlPid}');
                var count =$("#count_${pid}").val();
                var feeType = $('input[name="feeType"]:checked').val();
                var feeMoney = $("#fee_money_${pid}").val();
                var feePercentage = $("#fee_percentage_${pid}").val();
                var boolean =true;
                if(settingId !=null &&settingId !="" &&settingId !=undefined){
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'GET',
                        url: '${contextPath}/security/hdg/exchange_installment_count/insert_installment_count.htm',
                        data: {
                            settingId: settingId,
                            count:count,
                            feeType:feeType,
                            feeMoney:feeMoney*100,
                            feePercentage:feePercentage*100
                        },
                        dataType: 'json',
                        success: function (json) {
                            <@app.json_jump/>
                            if (json.success) {
                                wins.data('entityId', json.data);
                            } else {
                                boolean =false;
                                $.messager.alert('提示信息', json.message, 'info');
                            }
                        }
                    })
                }
                if(boolean){
                    wins.data('count', count);
                    wins.data('feeType', feeType);
                    wins.data('feeMoney', feeMoney);
                    wins.data('feePercentage', feePercentage);
                    win.window('close');
                }
            }
        });
        function feeType(val) {
            if(val==null || val=="" || val==undefined ||val==3){
                $("#fee_${pid}").hide();
            }else if(val == '2'){
                $("#fee_${pid}").show();
                $("#fee_percentage_${pid}").hide();
                $("#fee_money_${pid}").show();
            }else if(val == '1'){
                $("#fee_${pid}").show();
                $("#fee_money_${pid}").hide();
                $("#fee_percentage_${pid}").show();
            }
        }
    })();
    $('input[name="feeType"]').click(function () {
        feeType($(this).val());
    });

    function feeType(val){
        if(val==null||val==""||val==undefined|| val==3){
            $("#fee_${pid}").hide();
        }else if(val==1){
            $("#fee_${pid}").show();
            $("#fee_money_${pid}").hide();
            $("#fee_percentage_${pid}").show();
        }else if(val==2){
            $("#fee_${pid}").show();
            $("#fee_percentage_${pid}").hide();
            $("#fee_money_${pid}").show();
        }
    }

    $.extend($.fn.validatebox.defaults.rules, {
        integer: {// 验证整数 不可正负数
            validator: function (value) {
                var st=/^[+]?[1-9]+\d*$/i.test(value);
                if(!st){
                    $("#count_${pid}").val('');
                }
                return st;

                //return /^([+]?[0-9])|([-]?[0-9])+\d*$/i.test(value);
            },
            message: '请输入整数'
        }
    });
    function verification() {
        
        var success = true;
        var count =$("#count_${pid}").val();
        var val = $('input[name="feeType"]:checked').val();
        var feeMoney = $("#fee_money_${pid}").val();
        var feePercentage = $("#fee_percentage_${pid}").val();
        if(count ==""|| count==null){
            success=false;
            $.messager.alert('提示信息', '分期期数不能为空', 'info');
            return success;

        }else if(val ==""|| val==null) {
            success = false;
            $.messager.alert('提示信息', '手续费类型不能为空', 'info');
            return success;
        }
        if(val ==2){
            if(feeMoney ==""|| feeMoney==null|| feeMoney==0){
                success = false;
                $.messager.alert('提示信息', '手续费不能为空', 'info');
                return success;
            }
        }else if(val ==1){
            if(feePercentage ==""|| feePercentage==null|| feePercentage==0){
                success = false;
                $.messager.alert('提示信息', '手续费不能为空', 'info');
                return success;
            }
        }

        return success;
    }
</script>