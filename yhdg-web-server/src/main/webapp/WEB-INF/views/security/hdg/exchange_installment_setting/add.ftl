<style>
    .standard_num_table .standard_num_div{position:relative;width: 115px;
        border:1px solid #eaeaea;
        color: #333333;}
    /*.standard_num_table .standard_num_div:hover{background-color:#a3d0fd;}*/
    .standard_num_table .standard_num_div span{font-size:13px}
    .standard_num_table .standard_num_div .tom{position:relative; display: -moz-box;display: -webkit-box;
        -moz-box-align: center;
        -webkit-box-align: center;
        -moz-box-pack: center;
        -webkit-box-pack: center;
        text-align: center;
        padding:8px 0px;
        line-height: 18px;}
    .standard_num_table .standard_num_div .tail{
        position: absolute;
        top:2px;
        right:8px;
    }
    .standard_num_table .standard_num_div .tail a{
        color: #FF2C2C !important;
        font-size: 14px;
        font-weight: bold;
        cursor: pointer;
    }
    .standard_num_table .standard_num_div .tom span:nth-child(3){
        color: #999;
    }
    .btn_blue{background:#008ae7;}
</style>
<div class="popup_body clearfi" style="padding-left:10px;padding-top: 20px;font-size: 14px;min-height: 85%;">
    <div class="ui_table">
        <form method="post">
            <div>
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="110" align="left" style="font-size: 12px"><span style="color: #FF4242">*</span>所属运营商：</td>
                        <td>
                            <input name="agentId" required="true" id="page_agent_id" class="easyui-combotree"
                                   editable="false"
                                   style="width:187px;height:28px "
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                    method:'get',
                    valueField:'id',
                    textField:'text',
                    editable:true,
                    multiple:false,
                    panelHeight:'auto',
                    onClick: function(node) {
                    }
                "/>
                        </td>
                    </tr>
                    <tr>
                        <td width="110" align="left" style="font-size: 12px"><span style="color: #FF4242">*</span>分期规则名称：</td>
                        <td><input type="text" class="text easyui-validatebox" maxlength="40"
                                   style="width:175px;height:28px " id="fullname_${pid}"
                                   name="fullname" value=""/></td>
                    </tr>
                    <tr>
                        <td width="110" align="left" style="font-size: 12px"><span style="color: #FF4242">*</span>截至时间：</td>
                        <td>
                            <input type="text" class="text easyui-datebox" style="width:187px;height:28px " id="deadline_time_${pid}"
                                   name="deadlineTime" required="true">
                        </td>
                    </tr>
                    <tr>
                        <td width="110" align="left" style="font-size: 12px"><span style="color: #FF4242">*</span>是否启用：</td>
                        <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" checked id="is_active_1" value="1" />
                                      <label for="is_active_1">是</label>
                            </span>
                                <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_2"  value="0" />
                                    <label for="is_active_2">否</label>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td width="110" align="left" style="font-size: 12px"><span style="color: #FF4242">*</span>规则类型：</td>
                        <td>
                            <select class="easyui-combobox" id="setting_type_${pid}"
                                    style="width:187px;height: 28px "
                                    data-options="
                                    onSelect: function () {
                                          setting_type_${pid}();
                                    }
                             ">
                            <#if SettingType??>
                                <#list SettingType as e>
                                    <option value="${e.getValue()}"
                                            <#if e.getValue()==SettingTypeSS>selected="selected" </#if>>${e.getName()}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                    </tr>

                </table>
            </div>
            <div class="standard_staging">
                <table id="standard_staging_table">
                   <tr >
                        <td width="110" valign="top" align="left" style="padding-top: 30px;font-size: 12px">标准分期：</td>
                        <td>
                            <table id="standard_num_table" class="standard_num_table">
                                <tr rowspan="3">
                                    <td>
                                        <div>
                                            <button style="background: none;border: none; color: #4263FF;cursor: pointer;" type="button" class="btn btn_green" type="button" onclick="add_installment_count()">添加分期</button>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="custom_staging" style="display:none;">
                <table id="custom_staging_table">
                    <tr >
                        <td width="110" valign="top" align="left" style="padding-top: 24px;font-size: 12px">分期：</td>
                        <td>
                            <table id="custom_num_table">
                                <tr >
                                    <td>
                                        <div>
                                            <button style="background: none;border: none; color: #4263FF;cursor: pointer;" type="button" class="btn btn_green" type="button"  onclick="add_exchange_installment_count_detail()">添加分期</button>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <div>
                <table id="exchange_installment_customer">
                    <tr>
                        <td width="110" valign="top" align="left" style="padding-top: 18px;font-size: 12px">绑定骑手号码：</td>
                        <td>
                            <table id="exchange_installment_customer_mobile">
                                <tr>
                                    <td>
                                        <input type="text" id="mobile_${pid}" name="mobile"  class="text easyui-validatebox" maxlength="11"
                                               style="width:175px;height:28px ">
                                    </td>
                                    <td>
                                        <button class="btn btn_green" style="background: none;border: none; color: #4263FF;cursor: pointer;" type="button"  type="button" onclick="add_installment_customer_mobile()">添加</button>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <div>
                <table id="exchange_installment_cabinet">
                    <tr>
                        <td width="110" valign="top"  align="left" style="padding-top: 18px;font-size: 12px ">绑定柜子编号：</td>
                        <td>
                            <table id="exchange_installment_cabinet_id">
                                <tr>
                                    <td>
                                        <button class="btn btn_green" style="background: none;border: none; color: #4263FF;cursor: pointer;" type="button" type="button" onclick="add_exchange_installment_cabinet()">添加柜子</button>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <#--<div>
                <table id="exchange_installment_station">
                    <tr>
                        <td width="110" valign="top"  align="left" style="padding-top: 18px;font-size: 12px ">绑定站点编号：</td>
                        <td>
                            <table id="exchange_installment_station_id">
                                <tr>
                                    <td>
                                        <button class="btn btn_green" style="background: none;border: none; color: #4263FF;cursor: pointer;" type="button" type="button" onclick="add_exchange_installment_station()">添加站点</button>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>-->
        </form>
    </div>
    <div style="height: 47px;"></div>
</div>
<div class="popup_btn" style="    position: absolute;
    bottom: 0px;
    width: 100%;
    padding: 12px 0px;
">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>

<script>
    (function () {
        var win = $('#${pid}'), form = win.find('form');
        win.find('button.close').click(function () {
            win.window('close');
        });

        win.find('button.ok').click(function () {
            if(verifications()){
                var agentId = $('#page_agent_id').combotree('getValue');
                var fullname = $('#fullname_${pid}').val();
                var deadlineTime= $('#deadline_time_${pid}').datebox("getValue");
                var settingType=$('#setting_type_${pid}').combobox('getValue');
                var isActive = $('input[name="isActive"]:checked').val();
                var standardNumTable = $("#standard_num_table");//标准分期
                var customNumTable = $('#custom_num_table');//自定义分期
                var exchangeInstallmentCustomer = $("#exchange_installment_customer_mobile");//绑定骑手手机
                var cabinetTable=$("#exchange_installment_cabinet_id");//绑定柜子
                var stationTable=$("#exchange_installment_station_id");//绑定站点
                var standardCounts=[]; var standardFeeTypes=[]; var standardFeeMoneys=[]; var standardFeePercentages=[];
                standardNumTable.find('tr').each(function () {
                    $(this).find('td').each(function () {
                        var standardCount=$(this).find('#standard_count_${pid}').val();
                        var standardFeeType=$(this).find('#standard_fee_type_${pid}').val();
                        var standardFeeMoney=$(this).find('#standard_fee_money_${pid}').val();
                        var standardFeePercentage=$(this).find('#standard_fee_percentage_${pid}').val();
                        if(standardCount != undefined){
                            standardCounts.push(parseInt(standardCount));
                            standardFeeTypes.push(parseInt(standardFeeType));
                            standardFeeMoneys.push(parseFloat(standardFeeMoney));
                            standardFeePercentages.push(parseFloat(standardFeePercentage));
                        }

                    })
                });
                var nums=[]; var feeTypes=[]; var feeMoneys=[]; var feePercentages=[];var customNums=[];
                var minForegiftPercentages=[]; var minPacketPeriodPercentages=[];
                customNumTable.find('table').each(function () {
                    var customNums1=[];
                    var minForegiftPercentages1=[]; var minPacketPeriodPercentages1=[];
                    $(this).find('tr').each(function () {
                        var num=$(this).find('#num_${pid}').val();
                        var feeType=$(this).find('#fee_type_${pid}').val();
                        var customNum=$(this).find('#custom_num_${pid}').val();
                        var feeMoney=$(this).find('#fee_money_${pid}').val();
                        var feePercentage=$(this).find('#fee_percentage_${pid}').val();
                        var minForegiftPercentage=$(this).find('#min_foregift_percentage_${pid}').val();
                        var minPacketPeriodPercentage=$(this).find('#min_packet_period_percentage_${pid}').val();
                        var boolean =true;
                        if(num != undefined){
                            boolean=false;
                            feeTypes.push(parseInt(feeType));
                            feeMoneys.push(parseFloat(feeMoney));
                            feePercentages.push(parseFloat(feePercentage));
                            nums.push(parseInt(num));
                        }else{
                            boolean=true;
                        }
                        if(boolean){
                            customNums1.push(parseInt(customNum));
                            minForegiftPercentages1.push(parseInt(minForegiftPercentage));
                            minPacketPeriodPercentages1.push(parseInt(minPacketPeriodPercentage));
                        }else{
                            customNums.push(customNums1);
                            minForegiftPercentages.push(minForegiftPercentages1);
                            minPacketPeriodPercentages.push(minPacketPeriodPercentages1);
                        }
                    });
                });
                var customerMobiles=[];
                exchangeInstallmentCustomer.find('tr').each(function () {
                    var customerMobile=$(this).find('#customer_mobile_${pid}').val();
                    if(customerMobile != undefined){
                        customerMobiles.push(customerMobile);
                    }
                });
                var cabineIds=[];var cabineNames=[];
                cabinetTable.find('tr').each(function () {
                    var cabineId=$(this).find('#cabine_id_${pid}').val();
                    var cabineName=$(this).find('#cabine_name_${pid}').val();

                    if(cabineId != undefined){
                        cabineIds.push(cabineId);
                        cabineNames.push(cabineName);
                    }
                });
              /*  var stationIds=[];var stationNames=[];
                stationTable.find('tr').each(function () {
                    var stationId=$(this).find('#station_id_${pid}').val();
                    var stationName=$(this).find('#station_name_${pid}').val();

                    if(stationId != undefined){
                        stationIds.push(stationId);
                        stationNames.push(stationName);
                    }
                });*/

               // win.window('close');
                var settingMap = $.toJSON({
                    agentId:agentId,
                    fullname:fullname,
                    deadlineTime: deadlineTime,
                    settingType: settingType,
                    isActive:isActive,
                    //标准分期数据
                    standardCounts: standardCounts,
                    standardFeeTypes:standardFeeTypes,
                    standardFeeMoneys: standardFeeMoneys,
                    standardFeePercentages:standardFeePercentages,
                    //自定义分期数据
                    nums:nums,
                    feeTypes:feeTypes,
                    feeMoneys:feeMoneys,
                    feePercentages:feePercentages,
                    customNums:customNums,
                    minForegiftPercentages:minForegiftPercentages,
                    minPacketPeriodPercentages:minPacketPeriodPercentages,
                    //绑定骑手数据
                    customerMobiles:customerMobiles,
                    //绑定换电柜数据
                    cabineNames:cabineNames,
                    cabineIds:cabineIds
                    //绑定站点数据
                   /* stationIds:stationIds,
                    stationNames:stationNames,*/
                });
                var success =false;
                debugger
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/hdg/exchange_installment_setting/add_installment_setting.htm',
                    dataType: 'json',
                    data: {data:settingMap},
                    success: function (json) {
                    <@app.json_jump/>
                        if (json.success) {
                            success = true;
                            win.data('entityId', json.message);
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');

                        }
                    }
                });
                if(success){
                    win.window('close');
                }

            }
        });

    })();
    function setting_type_${pid}() {
        var num =$('#setting_type_${pid}').combobox('getValue');
        if(num ==2){
            $(".standard_staging").css('display','none');
            $(".custom_staging").css('display','block');
        }else{
            $(".custom_staging").css('display','none');
            $(".standard_staging").css('display','block');
        }
    }
    function add_installment_count() {
       var agentId = $('#page_agent_id').combotree('getValue');
       if(agentId ==null ||agentId =="" ||agentId ==undefined){
           $.messager.alert('提示信息', '请先选择运营商', 'info');
           return;
       }
        var win = $('#${pid}');
        App.dialog.show({
            css: 'width:450px;height:300px;',
            title: '新建标准分期',
            href: "${contextPath}/security/hdg/exchange_installment_count/add_installment_count.htm?urlPid=${pid}",
            event: {
                onClose: function() {
                    var count = win.data("count");
                    var feeType = win.data('feeType');
                    var feeMoney = win.data('feeMoney');
                    var feePercentage = win.data('feePercentage');
                    if(count!=null&&count!=""&&count!=undefined){
                        add_installment_count_table(count,feeType,feeMoney,feePercentage);
                    }
                    win.data("count","");
                    win.data('feeType',"");
                    win.data('feeMoney',"");
                    win.data('feePercentage',"");
                }
            }
        });

    }
    function add_custom_installment_count() {

    }
    function add_installment_customer_mobile() {
        var agentId = $('#page_agent_id').combotree('getValue');
        if(agentId ==null ||agentId =="" ||agentId ==undefined){
            $.messager.alert('提示信息', '请先选择运营商', 'info');
            return;
        }
        var mobile=$("#mobile_${pid}").val();
        var boolean = /^1\d{10}$/.test(mobile);
        if(boolean){
            var customerId ,customerFullname;
            var customerMobile = mobile;
            if(isCustomerMobileRepeat(customerMobile)){
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'GET',
                    url: '${contextPath}/security/hdg/exchange_installment_customer/add_installment_customer_mobile.htm?mobile='+mobile,
                    dataType: 'json',
                    success: function (test) {
                        if (test.success) {

                            var json = test.data;
                            if(json!=null){
                            customerId= json.id;
                            customerFullname= json.fullname;
                            }
                            add_installment_customer_mobile_table(customerId,customerMobile,customerFullname);
                        }else{
                            $.messager.alert('提示信息', test.message, 'info');
                        }
                    }
                });

            }else{
                $.messager.alert('提示信息', '手机号已添加,请勿二次添加', 'info');
            }
        }else{
            $.messager.alert('提示信息', '请输入正确手机号', 'info');
        }
    }

    function add_installment_count_table(count,feeType,feeMoney,feePercentage){

        var standardNumTable = $("#standard_num_table");
        var trlength =standardNumTable.find('tr').length;
        var tdlength =standardNumTable.find('tr').eq(trlength-2).find('td').length;
        var fee;
        if(feeType==1){
            fee=feePercentage+'%';
        }else if(feeType==2){
            fee=feeMoney+"元";
        }else if(feeType==3){
            fee="无";
        }
        var html=
                ' <td>\n'+
                '    <div class="standard_num_div" >\n'+
                '    <input type="hidden" name="standardCount" id="standard_count_${pid}" value="'+count+'">\n'+
                '    <input type="hidden" name="standardFeeType" id="standard_fee_type_${pid}" value="'+feeType+'">\n'+
                '    <input type="hidden" name="standardFeeMoney" id="standard_fee_money_${pid}" value="'+feeMoney+'">\n'+
                '    <input type="hidden" name="standardFeePercentage" id="standard_fee_percentage_${pid}" value="'+feePercentage+'">\n'+
                '        <div class="tom">\n'+
                '            <span>'+count+'期</span><br>\n'+
                '            <span>手续费:'+fee+'</span><br>\n'+
                '       </div>'+
                '        <div class="tail">\n'+
                '             <a href="javascript:void(0)" type="button" onclick="delete_standard_num(this)"><span>×</span></a>\n'+
                '        </div>\n'+
                '    </div>\n'+
                '</td>\n';
        var htmls=  '  <tr>\n'+html+ '  </tr>\n';
        if(trlength==1){
            standardNumTable.find('tr').eq(trlength-2).before(htmls);
        }else{
            if(tdlength<3){
                standardNumTable.find('tr').eq(trlength-2).find('td').eq(tdlength-1).after(html);
            }else{
                standardNumTable.find('tr').eq(trlength-2).after(htmls);
            }

        }
    }
    function update_standard_num(obj){
        var win =$('#${pid}');
        var standard= $(obj);

        var count = standard.parent().parent().parent().find('#standard_count_${pid}').val();
        var feeType =standard.parent().parent().parent().find('#standard_fee_type_${pid}').val();
        var feeMoney =standard.parent().parent().parent().find('#standard_fee_money_${pid}').val();
        var feePercentage = standard.parent().parent().parent().find('#standard_fee_percentage_${pid}').val();

        $.messager.confirm('提示信息', '确认修改此标准分期吗?', function(ok) {
            if(ok) {
                App.dialog.show({
                    css: 'width:450px;height:300px;',
                    title: '修改标准分期',
                    href: "${contextPath}/security/hdg/exchange_installment_count/edit_installment_count.htm?urlPid=${pid}&count="+count
                    +"&feeType="+feeType+"&feeMoney="+feeMoney+"&feePercentage="+feePercentage,
                    event: {
                        onClose: function() {
                            var count = win.data("count");
                            var feeType = win.data('feeType');
                            var feeMoney = win.data('feeMoney');
                            var feePercentage = win.data('feePercentage');
                            if(count!=null&&count!=""){
                                var fee;
                                if(feeType==1){fee=feePercentage+'%'}else if(feeType==2){fee=feeMoney+'元'}else if(feeType==3){fee='无'}
                                standard.parent().parent().parent().find('#standard_count_${pid}').val(count);
                                standard.parent().parent().parent().find('#standard_fee_type_${pid}').val(feeType)
                                standard.parent().parent().parent().find('#standard_fee_money_${pid}').val(feeMoney);
                                standard.parent().parent().parent().find('#standard_fee_percentage_${pid}').val(feePercentage);
                                standard.parent().parent().parent().find('.tom').find('span').eq(0).html(count+'期');
                                standard.parent().parent().parent().find('.tom').find('span').eq(1).html('手续费:'+fee);
                            }
                            win.data("count","");
                            win.data('feeType',"");
                            win.data('feeMoney',"");
                            win.data('feePercentage',"");
                        }
                    }
                });
            }
        });


    }
    function delete_standard_num(obj){
        $.messager.confirm('提示信息', '确认删除此标准分期吗?', function(ok) {
            if(ok) {
                var standard= $(obj);
                if(standard.parent().parent().parent().parent().find('td').length==1){
                    standard.parent().parent().parent().parent().remove();
                }else {
                    standard.parent().parent().parent().remove();
                }
            }
        });

    }
    function add_installment_customer_mobile_table(customerId,customerMobile,customerFullname){
        var exchangeInstallmentCustomer = $("#exchange_installment_customer_mobile");
        var trlength = exchangeInstallmentCustomer.find("tr").length;
        var mobileAndCustomerFullname
        if(customerFullname ==null ||customerFullname =="" ||customerFullname ==undefined){
            mobileAndCustomerFullname=customerMobile;
        }else{
            mobileAndCustomerFullname=customerMobile+'('+customerFullname+')';
        }
        var html =
                '  <tr>\n'+
                '    <input type="hidden" name="customerId" id="customer_id_${pid}" value="'+customerId+'">\n'+
                '    <input type="hidden" name="customerMobile" id="customer_mobile_${pid}" value="'+customerMobile+'">\n'+
                '    <input type="hidden" name="customerFullname" id="customer_fullname_${pid}" value="'+customerFullname+'">\n'+
                '   <td>\n'+
                '    <input type="text" readonly class="text easyui-validatebox" maxlength="40" style="width:175px;height:28px " id="fullname_${pid}" name="fullname" value="'+mobileAndCustomerFullname+'"\n'+
                '   </td>\n'+
                '   <td>\n'+
                '     <img style="cursor: pointer;" onclick="delect_installment_customer_mobile_table(this)" src="${app.imagePath}/delete.png" alt="">\n'+
                '   </td>\n'+
                '  </tr>\n';
        exchangeInstallmentCustomer.find('tr').eq(trlength-1).before(html);
        exchangeInstallmentCustomer.find('tr').eq(trlength).find('#mobile_${pid}').val('');
    }

    function delect_installment_customer_mobile_table(obj) {
        $.messager.confirm('提示信息', '确认解绑此骑手号码吗?', function(ok) {
            if(ok) {
                var standard= $(obj);
                standard.parent().parent().remove();
            }
        });
    }

    function isCustomerMobileRepeat(customerMobile) {

        var boolean =true;
        var exchangeInstallmentCustomer = $("#exchange_installment_customer_mobile");
        exchangeInstallmentCustomer.find("tr").each(
            function () {
                var customerMobileNew = $(this).find("input[name='customerMobile']").val();
                if(customerMobile == customerMobileNew){
                    boolean =false;
                    return
                }
            }
        );
        return boolean;
    }
    function add_exchange_installment_cabinet() {
        var agentId = $('#page_agent_id').combotree('getValue');
        if(agentId ==null ||agentId =="" ||agentId ==undefined){
            $.messager.alert('提示信息', '请先选择运营商', 'info');
            return;
        }
        var win = $('#${pid}');
        App.dialog.show({
            css: 'width:750px;height:500px;',
            title: '添加换电柜',
            href: "${contextPath}/security/hdg/exchange_installment_cabinet/index.htm?agentId="+agentId+"&urlPid=${pid}",
            event: {
                onClose: function() {

                    var checked = win.data("checked");
                    if(checked!=null&&checked!=""){
                        for(var i=0; i<checked.length; i++){
                            var cabinetId  =checked[i].id;
                            var cabinetName = checked[i].cabinetName;
                            add_exchange_installment_cabinet_table(cabinetId,cabinetName);
                        }
                    }
                    win.data("checked","");
                }
            }
        });
    }

   function add_exchange_installment_cabinet_table(cabinetId,cabinetName) {

       var cabinet_table=$("#exchange_installment_cabinet_id");
       var trlength = cabinet_table.find("tr").length;
       var boolean = true;
       cabinet_table.find("tr").each(
           function () {
               var cabinetIdnew = $(this).find("input[name='cabinetId']").val();
               if(cabinetId==cabinetIdnew){
                   $(this).find("input[name='cabinetName']").val(cabinetName);
                   boolean=false;
                   return
               }
           }
       );

       if(boolean){

           var html ='<tr>\n'+
                   '    <input type="hidden" name="cabinetId" id="cabine_id_${pid}" value="'+cabinetId+'">\n'+
                   '    <input type="hidden" name="cabinetName" id="cabine_name_${pid}" value="'+cabinetName+'">\n'+
                   '   <td>\n'+
                   '    <input type="text" readonly class="text easyui-validatebox" maxlength="40" style="width:175px;height:28px " value="'+cabinetName+'"\n'+
                   '   </td>\n'+
                   '   <td>\n'+
                   '      <img style="cursor: pointer;" onclick="delect_exchange_installment_cabinet_table(this)" src="${app.imagePath}/delete.png" alt="">\n'+
                   '   </td>\n'+
                   '  </tr>\n';
           cabinet_table.find('tr').eq(trlength-1).before(html);
       }
   }
   function delect_exchange_installment_cabinet_table(obj) {
       $.messager.confirm('提示信息', '确认解绑此换电柜吗?', function(ok) {
           if(ok) {
               var standard= $(obj);
               standard.parent().parent().remove();
           }
       });
   }

    function add_exchange_installment_station() {
        var agentId = $('#page_agent_id').combotree('getValue');
        if(agentId ==null ||agentId =="" ||agentId ==undefined){
            $.messager.alert('提示信息', '请先选择运营商', 'info');
            return;
        }
        var win = $('#${pid}');
        App.dialog.show({
            css: 'width:750px;height:500px;',
            title: '添加站点',
            href: "${contextPath}/security/hdg/exchange_installment_station/index.htm?agentId="+agentId+"&urlPid=${pid}",
            event: {
                onClose: function() {
                    var checked = win.data("checked");
                    debugger
                    if(checked!=null&&checked!=""){
                        for(var i=0; i<checked.length; i++){
                            var stationId  =checked[i].id;
                            var stationName = checked[i].stationName;
                            add_exchange_installment_station_table(stationId,stationName);
                        }
                    }
                    win.data("checked","");
                }
            }
        });
    }


    function add_exchange_installment_station_table(stationId,stationName) {
        var station_table=$("#exchange_installment_station_id");
        var trlength = station_table.find("tr").length;
        var boolean = true;
        station_table.find("tr").each(
                function () {
                    var stationIdnew = $(this).find("input[name='stationId']").val();
                    if(stationId==stationIdnew){
                        $(this).find("input[name='cabinetName']").val(stationName);
                        boolean=false;
                        return
                    }
                }
        );
        if(boolean){
            var html ='<tr>\n'+
                    '    <input type="hidden" name="stationId" id="station_id_${pid}" value="'+stationId+'">\n'+
                    '    <input type="hidden" name="stationName" id="station_name_${pid}" value="'+stationName+'">\n'+
                    '   <td>\n'+
                    '    <input type="text" readonly class="text easyui-validatebox" maxlength="40" style="width:175px;height:28px " value="'+stationName+'"\n'+
                    '   </td>\n'+
                    '   <td>\n'+
                    '      <img style="cursor: pointer;" onclick="delect_exchange_installment_station_table(this)" src="${app.imagePath}/delete.png" alt="">\n'+
                    '   </td>\n'+
                    '  </tr>\n';
            station_table.find('tr').eq(trlength-1).before(html);
        }
    }
    function delect_exchange_installment_station_table(obj) {
        $.messager.confirm('提示信息', '确认解绑此站点吗?', function(ok) {
            if(ok) {
                var standard= $(obj);
                standard.parent().parent().remove();
            }
        });
    }

   function add_exchange_installment_count_detail() {
       var win = $('#${pid}');
       var agentId = $('#page_agent_id').combotree('getValue');
       if(agentId ==null ||agentId =="" ||agentId ==undefined){
           $.messager.alert('提示信息', '请先选择运营商', 'info');
           return;
       }
       App.dialog.show({
           css: 'width:750px;height:500px;',
           title: '新建分期',
           href: "${contextPath}/security/hdg/exchange_installment_count_detail/add.htm?urlPid=${pid}",
           event: {
               onClose: function() {

                   var feeType = win.data("feeType");
                   var feeMoney = win.data("feeMoney");
                   var feePercentage = win.data("feePercentage");
                   var minForegiftPercentages = win.data("minForegiftPercentages");
                   var minPacketPeriodPercentages = win.data("minPacketPeriodPercentages");
                   var selectOrderStatus =win.data("selectOrderStatus");

                   add_exchange_installment_count_detail_table(selectOrderStatus,feeType,feeMoney,feePercentage,minForegiftPercentages,minPacketPeriodPercentages,0);
                   win.data("feeTypes","");
                   win.data("feeMoneys","");
                   win.data("feePercentages","");
                   win.data("minForegiftMoneys","");
                   win.data("minForegiftPercentages","");
                   win.data("minPacketPeriodMoneys","");
                   win.data("minPacketPeriodPercentages","");
                   win.data("selectOrderStatus","");

               }
           }
       });
   }


    function add_exchange_installment_count_detail_table(selectOrderStatus,feeType,feeMoney,feePercentage,minForegiftPercentages,minPacketPeriodPercentages,fori) {

        if(selectOrderStatus!=""&&selectOrderStatus!=null){
            var html;
            html =html1(selectOrderStatus,feeType,feeMoney,feePercentage);
            if(minForegiftPercentages.length==minPacketPeriodPercentages.length){

                for(var i=fori; i<minForegiftPercentages.length; i++){
                    var minForegiftPercentage  =minForegiftPercentages[i];
                    var minPacketPeriodPercentage  =minPacketPeriodPercentages[i];
                    var html21=html2().replace('value="customNum"','value="'+(i+1)+'"');
                    html21=html21.replace('value="minForegiftPercentage"','value="'+minForegiftPercentage+'"');
                    html21=html21.replace('value="minPacketPeriodPercentage"','value="'+minPacketPeriodPercentage+'"');
                    html21=html21.replace('【第nun_count期】','【第'+convertToChinese(i+1)+'期】');
                    html21=html21.replace('minPacketPeriodPercentage；',minPacketPeriodPercentage+"%；");
                    html21=html21.replace('minForegiftPercentage；',minForegiftPercentage+"%；");
                    html+=html21;

                }
            }
            html+=html3();
            var customNumTable = $('#custom_num_table');
            //customNumTable.children().append(html);
            var trlength =customNumTable.children().children().length;
            if(trlength==1){
                customNumTable.children().prepend(html);
            }else{
                customNumTable.children().children().eq(trlength-2).after(html);
            }
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
    function delect_custom_installment_count(obj) {
        $.messager.confirm('提示信息', '确认删除此自定义分期吗?', function(ok) {
            if(ok) {
                var standard= $(obj);
                standard.parent().parent().parent().parent().parent().parent().remove();
            }
        });
    }
    function verifications() {

        var agentId = $('#page_agent_id').combotree('getValue');
        var fullname = $('#fullname_${pid}').val();
        var deadlineTime= $('#deadline_time_${pid}').datebox("getValue");
        var settingType=$('#setting_type_${pid}').combobox('getValue');
        if(agentId ==null ||agentId =="" ||agentId ==undefined){
            $.messager.alert('提示信息', '请先选择运营商', 'info');
            return false;
        }
        if(fullname ==null ||fullname =="" ||fullname ==undefined){
            $.messager.alert('提示信息', '请输入分期规则名称', 'info');
            return false;
        }
        if(deadlineTime ==null ||deadlineTime =="" ||deadlineTime ==undefined){
            $.messager.alert('提示信息', '请选择截止时间', 'info');
            return false;
        }
        if(settingType ==null ||settingType =="" ||settingType ==undefined){
            $.messager.alert('提示信息', '请选择规则类型', 'info');
            return false;
        }
        return true;

    }
    function edit_custom_installment_count (obj) {
        var win =$('#${pid}');
       var num = $(obj).parent().parent().find('#num_${pid}').val();
        var standard= $(obj);
       var customNums=[],
               feeType,
               feeMoney,
               feePercentage,
               minForegiftPercentages=[],
               minPacketPeriodPercentages=[];
        feeType = $(obj).parent().find('#fee_type_${pid}').val();
        feeMoney= $(obj).parent().find('#fee_money_${pid}').val();
        feePercentage = $(obj).parent().find('#fee_percentage_${pid}').val();
       for(var i=1;i<(Number(num)+1);i++){
           customNums.push($(obj).parent().parent().parent().find('tr').eq(i).find('#custom_num_${pid}').val());
           minForegiftPercentages.push($(obj).parent().parent().parent().find('tr').eq(i).find('#min_foregift_percentage_${pid}').val());
           minPacketPeriodPercentages.push($(obj).parent().parent().parent().find('tr').eq(i).find('#min_packet_period_percentage_${pid}').val());
       }
        $.messager.confirm('提示信息', '确认修此自定义分期吗?', function(ok) {
            if(ok) {
                App.dialog.show({
                    css: 'width:750px;height:500px;',
                    title: '修此自定义分期',
                    href: "${contextPath}/security/hdg/exchange_installment_count_detail/edit.htm?urlPid=${pid}&customNums="+customNums+
                    "&feeType="+feeType+"&feeMoney="+feeMoney+"&feePercentage="+feePercentage+
                    "&minForegiftPercentages="+minForegiftPercentages+
                    "&minPacketPeriodPercentages="+minPacketPeriodPercentages+"&num="+num,
                    event: {
                        onClose: function() {
                            var feeType = win.data("feeType");
                            var feeMoney = win.data("feeMoney");
                            var feePercentage = win.data("feePercentage");
                            var minForegiftPercentages = win.data("minForegiftPercentages");
                            var minPacketPeriodPercentages = win.data("minPacketPeriodPercentages");
                            var selectOrderStatus =win.data("selectOrderStatus");
                            if(Number(selectOrderStatus)>Number(num)&&minForegiftPercentages!=""){
                                edit_exchange_installment_count_detail_table(selectOrderStatus,feeType,feeMoney,feePercentage,minForegiftPercentages,minPacketPeriodPercentages,Number(num),obj);
                                $(obj).parent().parent().find('#num_${pid}').val(selectOrderStatus);
                                var value = "";
                                if(feeType == 1){
                                    value=feePercentage+"%";
                                }else if(feeType == 2){
                                    value=feeMoney+"元";
                                }else{
                                    value="无";
                                }
                                $(obj).parent().find('#fee_type_${pid}').val(feeType);
                                $(obj).parent().find('#fee_money_${pid}').val(feeMoney);
                                $(obj).parent().find('#fee_percentage_${pid}').val(feePercentage);
                                $(obj).parent().parent().find('input').eq(4).val(selectOrderStatus+'期/手续费:'+value);
                                for(var i=1; i<Number(num)+1; i++) {
                                    var  text = '【第' + convertToChinese(i) + '期】押金每次支付不少于' + minForegiftPercentages[i-1] + '%；租金支付不少于' + minPacketPeriodPercentages[i-1] + '%；';
                                    $(obj).parent().parent().parent().find('tr').eq(i).find('#custom_num_${pid}').val(i);
                                    $(obj).parent().parent().parent().find('tr').eq(i).find('#min_foregift_percentage_${pid}').val(minForegiftPercentages[i - 1]);
                                    $(obj).parent().parent().parent().find('tr').eq(i).find('#min_packet_period_percentage_${pid}').val(minPacketPeriodPercentages[i - 1]);
                                    $(obj).parent().parent().parent().find('tr').eq(i).find('textarea').html(text);

                                }
                            }else if(Number(selectOrderStatus)<Number(num)&&minForegiftPercentages!=""){
                                for(i=num;i>Number(selectOrderStatus);i--){
                                    standard.parent().parent().parent().find('tr').eq(i).remove();
                                }
                                $(obj).parent().parent().find('#num_${pid}').val(selectOrderStatus);
                                var value = "";
                                if(feeType == 1){
                                    value=feePercentage+"%";
                                }else if(feeType == 2){
                                    value=feeMoney+"元";
                                }else{
                                    value="无";
                                }
                                $(obj).parent().find('#fee_type_${pid}').val(feeType);
                                $(obj).parent().find('#fee_money_${pid}').val(feeMoney);
                                $(obj).parent().find('#fee_percentage_${pid}').val(feePercentage);
                                $(obj).parent().parent().find('input').eq(4).val(selectOrderStatus+'期/手续费:'+value);
                                for(var i=1; i<Number(num)+1; i++) {
                                    var  text = '【第' + convertToChinese(i) + '期】押金每次支付不少于' + minForegiftPercentages[i-1] + '%；租金支付不少于' + minPacketPeriodPercentages[i-1] + '%；';
                                    $(obj).parent().parent().parent().find('tr').eq(i).find('#custom_num_${pid}').val(i);
                                    $(obj).parent().parent().parent().find('tr').eq(i).find('#min_foregift_percentage_${pid}').val(minForegiftPercentages[i - 1]);
                                    $(obj).parent().parent().parent().find('tr').eq(i).find('#min_packet_period_percentage_${pid}').val(minPacketPeriodPercentages[i - 1]);
                                    $(obj).parent().parent().parent().find('tr').eq(i).find('textarea').html(text);

                                }

                            }else if(Number(selectOrderStatus)==Number(num)&&minForegiftPercentages!=""){
                                $(obj).parent().parent().find('#num_${pid}').val(selectOrderStatus);
                                var value = "";
                                if(feeType == 1){
                                    value=feePercentage+"%";
                                }else if(feeType == 2){
                                    value=feeMoney+"元";
                                }else{
                                    value="无";
                                }
                                $(obj).parent().find('#fee_type_${pid}').val(feeType);
                                $(obj).parent().find('#fee_money_${pid}').val(feeMoney);
                                $(obj).parent().find('#fee_percentage_${pid}').val(feePercentage);
                                $(obj).parent().parent().find('input').eq(4).val(selectOrderStatus+'期/手续费:'+value);
                                for(var i=1; i<Number(num)+1; i++) {
                                    var  text = '【第' + convertToChinese(i) + '期】押金每次支付不少于' + minForegiftPercentages[i-1] + '%；租金支付不少于' + minPacketPeriodPercentages[i-1] + '%；';
                                    $(obj).parent().parent().parent().find('tr').eq(i).find('#custom_num_${pid}').val(i);
                                    $(obj).parent().parent().parent().find('tr').eq(i).find('#min_foregift_percentage_${pid}').val(minForegiftPercentages[i - 1]);
                                    $(obj).parent().parent().parent().find('tr').eq(i).find('#min_packet_period_percentage_${pid}').val(minPacketPeriodPercentages[i - 1]);
                                    $(obj).parent().parent().parent().find('tr').eq(i).find('textarea').html(text);

                                }
                            }
                            win.data("feeType","");
                            win.data("feeMoney","");
                            win.data("feePercentage","");
                            win.data("minForegiftPercentages","");
                            win.data("minPacketPeriodPercentages","");
                            win.data("selectOrderStatus","");
                        }
                    }
                });
            }
        });



    }

   function edit_exchange_installment_count_detail_table(selectOrderStatus,feeType,feeMoney,feePercentage,minForegiftPercentages,minPacketPeriodPercentages,fori,obj){
        if(selectOrderStatus!=""&&selectOrderStatus!=null){
            var html;
            if(minForegiftPercentages.length==minPacketPeriodPercentages.length){

                for(var i=fori; i<minForegiftPercentages.length; i++){
                    var minForegiftPercentage  =minForegiftPercentages[i];
                    var minPacketPeriodPercentage  =minPacketPeriodPercentages[i];
                    var html31=html2().replace('value="customNum"','value="'+(i+1)+'"');
                    html31=html31.replace('value="minForegiftPercentage"','value="'+minForegiftPercentage+'"');
                    html31=html31.replace('value="minPacketPeriodPercentage"','value="'+minPacketPeriodPercentage+'"');
                    html31=html31.replace('minPacketPeriodPercentage；',minPacketPeriodPercentage+"%；");
                    html31=html31.replace('minForegiftPercentage；',minForegiftPercentage+"%；");
                    html31=html31.replace('【第nun_count期】','【第'+convertToChinese(i+1)+'期】');
                    html+=html31;
                }
            }
            $(obj).parent().parent().parent().append(html);
        }
    }


    function html1(selectOrderStatus,feeType,feeMoney,feePercentage) {
       var value = "";
       if(feeType == 1){
           value=feePercentage+"%";
       }else if(feeType == 2){
           value=feeMoney+"元";
       }else{
           value="无";
       }
        var html1=
                '       <tr>\n'+
                '           <td>\n'+
                '               <table>\n'+
                '                   <tr>\n'+
                '                       <td>\n'+
                '                            <input type="hidden" id="num_${pid}" name="num" value="'+selectOrderStatus+'"/>\n'+
                '                            <input type="hidden" id="fee_type_${pid}" name="feeType" value="'+feeType+'"/>\n'+
                '                            <input type="hidden" id="fee_money_${pid}" name="feeMoney" value="'+feeMoney+'"/>\n'+
                '                            <input type="hidden" id="fee_percentage_${pid}" name="feePercentage" value="'+feePercentage+'"/>\n'+
                '                            <input type="text" class="text easyui-validatebox" maxlength="20" style="width:175px;height:28px "  value="'+selectOrderStatus+'期/手续费为:'+value+'"/>\n'+
                '                            <img style="cursor: pointer;" onclick="edit_custom_installment_count(this)" src="${app.imagePath}/edit.png" alt="">\n'+
                '                            <img style="cursor: pointer;" onclick="delect_custom_installment_count(this)" src="${app.imagePath}/delete.png" alt="">\n'+
                '                       </td>\n'+
                '                   </tr>\n';
        return html1;
    }
    function html2() {
        var html2=
                '                    <tr >\n'+
                '                           <input type="hidden" id="custom_num_${pid}" name="customNum" value="customNum"/>\n'+
                '                           <input type="hidden" id="min_foregift_percentage_${pid}" name="minForegiftPercentage" value="minForegiftPercentage"/>\n'+
                '                           <input type="hidden" id="min_packet_period_percentage_${pid}" name="minPacketPeriodPercentage" value="minPacketPeriodPercentage"/>\n'+
                '                        <td colspan="3">\n'+
                '                            <textarea style="width: 321px;line-height: 22px;border-radius: 4px;" readonly style="width: 321px">【第nun_count期】押金每次支付不少于minForegiftPercentage；租金支付不少于minPacketPeriodPercentage；</textarea>\n'+
                '                       </td>\n'+
                '                   </tr>\n';
        return html2;
    }
    function html3() {
        var html3=
                '               </table>\n'+
                '           </td>\n'+
                '       </tr>\n';
        return html3;
    }

    


</script>


