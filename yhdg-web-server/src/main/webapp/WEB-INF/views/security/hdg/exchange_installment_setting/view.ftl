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
    .btn_blue{background:#008ae7;}
</style>
<div class="popup_body clearfi" style="padding-left:10px;padding-top: 20px;font-size: 14px;min-height: 85%;">
    <div class="ui_table">
        <form method="post">

            <div>
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="110" align="right" style="font-size: 12px"><span style="color: #FF4242">*</span>所属运营商：</td>
                        <td>
                            <input readonly name="agentId" required="true" id="page_agent_id" class="easyui-combotree"
                                   editable="true" value="${(entity.agentId)!''}"
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
                        <td width="110" align="right" style="font-size: 12px"><span style="color: #FF4242">*</span>分期规则名称：</td>
                        <td><input readonly type="text" class="text easyui-validatebox" maxlength="40"
                                   style="width:175px;height:28px " id="fullname_${pid}"
                                   name="fullname" value="${(entity.fullname)!''}"/></td>
                    </tr>
                    <tr>
                        <td width="110" readonly align="right" style="font-size: 12px"><span style="color: #FF4242">*</span>截至时间：</td>
                        <td>
                            <input type="text" class="text easyui-datebox" style="width:187px;height:28px " id="deadline_time_${pid}"
                                   name="deadlineTime" required="true"
                                   value=" <#if (entity.deadlineTime)?? >${app.format_date(entity.deadlineTime)}</#if>">
                        </td>
                    </tr>
                    <tr>
                        <td width="110" align="right" style="font-size: 12px"><span style="color: #FF4242">*</span>是否启用：</td>
                        <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" readonly name="isActive" <#if entity.isActive??&&entity.isActive==1>checked<#else ></#if> id="is_active_1" value="1" />
                                      <label for="is_active_1">是</label>
                            </span>
                            <span class="radio_box">
                                <input type="radio" class="radio" readonly name="isActive" <#if entity.isActive??&&entity.isActive==1><#else >checked</#if> id="is_active_2"  value="0" />
                                    <label for="is_active_2">否</label>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td width="110" align="right" style="font-size: 12px"><span style="color: #FF4242">*</span>规则类型：</td>
                        <td>
                            <select readonly class="easyui-combobox" id="setting_type_${pid}"
                                    style="width:187px;height: 28px "
                                    data-options="
                                    onSelect: function () {
                                          setting_type_by();
                                    }
                             ">
                            <#if SettingType??>
                                <#list SettingType as e>
                                    <option value="${e.getValue()}"
                                            <#if e.getValue()==entity.settingType>selected="selected" </#if>>${e.getName()}</option>
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
                        <td width="110" valign="top" align="right" style="padding-top: 30px;font-size: 12px">标准分期：</td>
                        <td>
                            <table id="standard_num_table" class="standard_num_table">
                                <#if entity?? && entity.settingType == 1>
                                    <#list exchangeInstallmentCountList as exchangeInstallmentCount>
                                        <#if exchangeInstallmentCount_index+1 == 1 || (exchangeInstallmentCount_index+1)%3==1>
                                            <tr>
                                        </#if>
                                            <td>
                                                <div class="standard_num_div" >
                                                    <div class="tom">
                                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                                        <span>${(exchangeInstallmentCount.count)!''}期</span><br>
                                                        <span> 手续费:
                                                            <#if exchangeInstallmentCount.feeType ?? && exchangeInstallmentCount.feeType == 1>
                                                                ${(exchangeInstallmentCount.feePercentage/100)?string('0.00')}%
                                                            </#if>
                                                            <#if exchangeInstallmentCount.feeType ?? && exchangeInstallmentCount.feeType == 2>
                                                                ${(exchangeInstallmentCount.feeMoney/100)?string('0.00')}元
                                                            </#if>
                                                            <#if exchangeInstallmentCount.feeType ?? && exchangeInstallmentCount.feeType == 3>
                                                                无
                                                            </#if>
                                                        </span>
                                                    </div>
                                                    <div class="tail">

                                                    </div>
                                                </div>
                                            </td>
                                        <#if (exchangeInstallmentCount_index+1)%3==0 || exchangeInstallmentCountList?size == (exchangeInstallmentCount_index+1)>
                                            </tr>
                                        </#if>
                                    </#list>
                                </#if>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="custom_staging" style="display:none;">
                <table id="custom_staging_table">
                    <tr >
                        <td width="110" valign="top" align="right" style="padding-top: 24px;font-size: 12px">分期：</td>
                        <td>
                            <table id="custom_num_table">
                                <#if entity?? && entity.settingType == 2>
                                    <#list exchangeInstallmentCountList as exchangeInstallmentCount>
                                        <tr>
                                            <td>
                                                <table>
                                                    <tr>
                                                        <td>
                                                            <input type="hidden" id="count_id_${pid}" name="countId" value="${(exchangeInstallmentCount.id)!''}"/>
                                                            <input type="hidden" id="num_${pid}" name="num" value="${(exchangeInstallmentCount.count)!''}"/>
                                                            <input readonly type="text" class="text easyui-validatebox" maxlength="20" style="width:175px;height:28px "  value="${(exchangeInstallmentCount.count)!''}期"/>
                                                        </td>
                                                    </tr>
                                                        <#list exchangeInstallmentCountDetailMap["${exchangeInstallmentCount.id}"]?sort_by("num") as exchangeInstallmentCountDetail>
                                                        <tr >
                                                            <td colspan="3">
                                                                <textarea style="width: 321px;line-height: 22px;border-radius: 4px;" readonly>【第${(exchangeInstallmentCountDetail.num)!''}期】押金每次支付不少于${(exchangeInstallmentCountDetail.minForegiftPercentage/100)?string('0.00')}%；租金支付不少于${(exchangeInstallmentCountDetail.minPacketPeriodPercentage/100)?string('0.00')}%；手续费:<#if exchangeInstallmentCountDetail.feeType  == 2>${(exchangeInstallmentCountDetail.feeMoney/100)?string('0.00')}元<#elseif exchangeInstallmentCountDetail.feeType  == 1>${(exchangeInstallmentCountDetail.feePercentage/100)?string('0.00')}%</#if></textarea>
                                                            </td>
                                                        </tr>
                                                   </#list>
                                                </table>
                                            </td>
                                        </tr>
                                    </#list>
                                </#if>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <div>
                <table id="exchange_installment_customer">
                    <tr>
                        <td width="110" valign="top" align="right" style="padding-top: 18px;font-size: 12px">绑定骑手号码：</td>
                        <td>
                            <table id="exchange_installment_customer_mobile">
                                <#list exchangeInstallmentCustomerList as exchangeInstallmentCustomer>
                                    <tr>
                                        <input type="hidden" name="customerId" id="customer_id_${pid}" value="${(exchangeInstallmentCustomer.customerId)!''}">
                                        <input type="hidden" name="customerMobile" id="customer_mobile_${pid}" value="${(exchangeInstallmentCustomer.customerMobile)!''}">
                                        <input type="hidden" name="customerFullname" id="customer_fullname_${pid}" value="${(exchangeInstallmentCustomer.customerFullname)!''}">
                                        <td>
                                            <input type="text" class="text easyui-validatebox" readonly maxlength="40" style="width:175px;height:28px " id="fullname_${pid}" name="fullname" value="${(exchangeInstallmentCustomer.customerMobile)!''}<#if exchangeInstallmentCustomer.customerId??>(${(exchangeInstallmentCustomer.customerFullname)!''})</#if>"
                                        </td>
                                    </tr>
                                </#list>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <div>
                <table id="exchange_installment_cabinet">
                    <tr>
                        <td width="110" valign="top"  align="right" style="padding-top: 18px;font-size: 12px ">绑定柜子编号：</td>
                        <td>
                            <table id="exchange_installment_cabinet_id">
                                <#list exchangeInstallmentCabinetList as exchangeInstallmentCabinet>
                                    <tr>
                                        <input type="hidden" name="cabinetId" id="cabine_id_${pid}" value="${(exchangeInstallmentCabinet.cabinetId)!''}">
                                        <input type="hidden" name="cabinetName" id="cabine_name_${pid}" value="${(exchangeInstallmentCabinet.cabinetName)!''}">
                                        <td>
                                            <input type="text" readonly class="text easyui-validatebox" maxlength="40" style="width:175px;height:28px " value="${(exchangeInstallmentCabinet.cabinetName)!''}"
                                        </td>
                                    </tr>
                                </#list>
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
                                <#list exchangeInstallmentStationsList as exchangeInstallmentStation>
                                    <tr>
                                        <input type="hidden" name="stationId" id="station_id_${pid}" value="${(exchangeInstallmentStation.stationId)!''}">
                                        <input type="hidden" name="stationName" id="station_name_${pid}" value="${(exchangeInstallmentStation.stationName)!''}">
                                        <td>
                                            <input type="text" readonly class="text easyui-validatebox" maxlength="40" style="width:175px;height:28px " value="${(exchangeInstallmentStation.stationName)!''}"
                                        </td>
                                    </tr>
                                </#list>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>-->
        </form>
    </div>
</div>
<div class="popup_btn" style="height: 5%;">
    <button class="btn btn_border close">关闭</button>
</div>

<script>
    (function () {
        var win = $('#${pid}'), form = win.find('form');
        win.find('button.close').click(function () {
            win.window('close');
        });
        var setting_type_num= ${(entity.settingType)!''};
        setting_type_by(setting_type_num);
        function setting_type_by(setting_type_num) {
            if(setting_type_num ==2){
                $(".standard_staging").css('display','none');
                $(".custom_staging").css('display','block');
            }else{
                $(".custom_staging").css('display','none');
                $(".standard_staging").css('display','block');
            }
        }
    })();
</script>


