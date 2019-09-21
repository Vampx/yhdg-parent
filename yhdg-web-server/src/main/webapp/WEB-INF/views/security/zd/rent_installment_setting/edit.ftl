<div class="popup_body" style="padding-left:10px;padding-top: 20px;font-size: 14px;min-height: 85%;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" id="id_${pid}" value="${(entity.id)!''}">
            <input type="hidden" name="foregiftId" id="foregift_id" value="${(entity.foregiftId)!''}">
            <input type="hidden" name="foregiftMoney" id="foregift_money" value="${((entity.foregiftMoney)!0)/100}">
            <input type="hidden" name="packetId" id="packet_id" value="${(entity.packetId)!''}">
            <input type="hidden" name="packetMoney" id="packet_money" value="${((entity.packetMoney)!0)/100}">
            <input type="hidden" name="insuranceId" id="insurance_id" value="${(entity.insuranceId)!''}">
            <input type="hidden" name="insuranceMoney" id="insurance_money" value="${((entity.insuranceMoney)!0)/100}">
            <input type="hidden" name="totalMoney" id="total_money" value="${((entity.totalMoney)!0)/100}">
            <div>
                <table>
                    <tr>
                        <td width="300px;">
                            <table cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="90" align="right">运营商：</td>
                                    <td>
                                        <input name="agentId" required="true" id="page_agent_id" class="easyui-combotree"
                                               editable="false" disabled
                                               style="width:187px;height:28px "
                                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto'
                            " value="${(entity.agentId)!''}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="70" align="right">电池类型：</td>
                                    <td>
                                        <input name="batteryType" id="page_battery_type" class="easyui-combotree" editable="false"
                                               style="width: 187px; height: 28px;" required="true"
                                               data-options="url:'${contextPath}/security/basic/agent_battery_type/tree.htm?agentId=' + ${(entity.agentId)!''} + '',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto',
                                onClick: function(node) {
                                   switch_battery_type_${pid}();
                                }
                            " value="${(entity.batteryType)!''}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="90" align="right">手机号：</td>
                                    <td><input type="text" class="text easyui-validatebox" id="mobile_${pid}" required="true" maxlength="40"
                                               style="width:175px;height:28px " onkeyup="findCustomer()" validType="unique[${entity.id}]"
                                               name="mobile" value="${(entity.mobile)!''}"/></td>
                                </tr>
                                <tr>
                                    <td width="90" align="right">姓名：</td>
                                    <td><input type="text" class="text easyui-validatebox" maxlength="40"
                                               style="width:175px;height:28px " id="fullname_${pid}"
                                               name="fullname" value="${(entity.fullname)!''}"/></td>
                                </tr>
                                <tr>
                                    <td width="90" align="right">截至时间：</td>
                                    <td>
                                        <input type="text" class="text easyui-datebox" style="width:187px;height:28px " id="deadline_time_${pid}"
                                               name="deadlineTime" required="true" value="<#if (entity.deadlineTime)?? >${app.format_date_time(entity.deadlineTime)}</#if>">
                                    </td>
                                </tr>
                                <tr>
                                    <td width="90" align="right">分期总额：</td>
                                    <td><span id="money_detail" style="color: red;font-weight: 600;line-height: 30px;">${((entity.totalMoney)!0)/100}元</span></td>
                                </tr>
                                <tr>
                                    <td width="90" align="right">分期：</td>
                                    <td>
                                        <select class="easyui-combobox" id="num_${pid}"
                                                style="width:120px;height: 28px "
                                                data-options="
                                onSelect: function () {
                                      show_detail_${pid}();
                                }
                         ">
                                        <#if NumEnum??>
                                            <#list NumEnum as e>
                                                <option value="${e.getValue()}"
                                                        <#if e.getValue()==defaultNum>selected="selected" </#if>>${e.getName()}</option>
                                            </#list>
                                        </#if>
                                        </select>
                                        期
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="500px;">
                            <table>
                                <tr>
                                    <td width="80px;">电池押金：</td>
                                    <td id="rent_battery_foregift">
                                    <#include 'edit_rent_battery_foregift.ftl'>
                                    </td>
                                </tr>
                                <tr>
                                    <td>电池租金：</td>
                                    <td id="rent_packet_period_price">
                                    <#include 'edit_rent_packet_period_price.ftl'>
                                    </td>
                                </tr>
                                <tr>
                                    <td>电池保险：</td>
                                    <td id="rent_insurance">
                                    <#include 'edit_rent_insurance.ftl'>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>

            <table>
                <tr>
                    <td>
                        <i style="width: 4px;height: 17px;background-color: #4263ff;display: block;margin-right: 10px;"></i>
                    </td>
                    <td style="color: #000000;font-weight: 600;line-height: 30px;">
                        分期设置
                    </td>
                </tr>
            </table>

            <hr>

            <div id="installment_detail">
            <#if rentInstallmentDetailList ??>
                <#list rentInstallmentDetailList as detail>
                    <div style="padding-top: 10px;display: none;" id="num_${(detail.num)!0}">
                        <table>
                            <tr>
                                <td align="left" colspan="5">分期${(detail.num)!0}: &nbsp;&nbsp;&nbsp;&nbsp;<span
                                        style="color: red;">(<#if detail.payStatusName?? >${(detail.payStatusName)!''}<#else>未支付</#if>)</span>
                                    <input type="hidden" name="num" value="${(detail.num)!0}">
                                </td>
                                <td align="right"><a href="javascript:view_pay_detail(${(detail.num)!0})">查看支付详情</a>
                                </td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次押金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentForegiftMoney" maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px "
                                                          value="${((detail.foregiftMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次租金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentPacketMoney"
                                                          maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px "
                                                          value="${((detail.packetMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次保险支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentInsuranceMoney" maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px "
                                                          value="${((detail.insuranceMoney)!0)/100}"/>&nbsp;元
                                </td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次支付时间:</td>
                                <td colspan="2">
                                    <input type="text" class="text easyui-datebox" style="width:185px;height:28px "
                                           value="<#if (detail.expireTime)?? >${app.format_date_time(detail.expireTime)}</#if>"
                                           name="recentExpireTime">
                                </td>
                            </tr>
                        </table>
                        <div style="padding-top: 10px;">
                            <hr>
                        </div>
                    </div>
                </#list>
            </#if>

                <#if defaultNum+1 &lt;= 10>
                    <div style="padding-top: 10px;display: none;" id="num_${defaultNum+1}" >
                        <table>
                            <tr>
                                <td align="left" colspan="5">分期${defaultNum+1}: &nbsp;&nbsp;&nbsp;&nbsp;<span
                                        style="color: red;">(未支付)</span>
                                    <input type="hidden" name="num" value="${(defaultNum+1)!0}">
                                </td>
                                <td align="right"><a href="javascript:view_pay_detail(${(defaultNum+1)!0})">查看支付详情</a></td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次押金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentForegiftMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.foregiftMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次租金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentPacketMoney"
                                                          maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.packetMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次保险支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentInsuranceMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.insuranceMoney)!0)/100}"/>&nbsp;元
                                </td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次支付时间:</td>
                                <td colspan="2">
                                    <input type="text" class="text easyui-datebox" style="width:185px;height:28px " value="<#if (detail.expireTime)?? >${app.format_date_time(detail.expireTime)}</#if>"
                                           name="recentExpireTime" >
                                </td>
                            </tr>
                        </table>
                        <div style="padding-top: 10px;">
                            <hr>
                        </div>
                    </div>
                </#if>

                <#if defaultNum+2 &lt;= 10>
                    <div style="padding-top: 10px;display: none;" id="num_${defaultNum+2}" >
                        <table>
                            <tr>
                                <td align="left" colspan="5">分期${defaultNum+2}: &nbsp;&nbsp;&nbsp;&nbsp;<span
                                        style="color: red;">(未支付)</span>
                                    <input type="hidden" name="num" value="${(defaultNum+2)!0}">
                                </td>
                                <td align="right"><a href="javascript:view_pay_detail(${(defaultNum+2)!0})">查看支付详情</a></td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次押金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentForegiftMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.foregiftMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次租金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentPacketMoney"
                                                          maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.packetMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次保险支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentInsuranceMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.insuranceMoney)!0)/100}"/>&nbsp;元
                                </td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次支付时间:</td>
                                <td colspan="2">
                                    <input type="text" class="text easyui-datebox" style="width:185px;height:28px " value="<#if (detail.expireTime)?? >${app.format_date_time(detail.expireTime)}</#if>"
                                           name="recentExpireTime" >
                                </td>
                            </tr>
                        </table>
                        <div style="padding-top: 10px;">
                            <hr>
                        </div>
                    </div>
                </#if>

                <#if defaultNum+3 &lt;= 10>
                    <div style="padding-top: 10px;display: none;" id="num_${defaultNum+3}" >
                        <table>
                            <tr>
                                <td align="left" colspan="5">分期${defaultNum+3}: &nbsp;&nbsp;&nbsp;&nbsp;<span
                                        style="color: red;">(未支付)</span>
                                    <input type="hidden" name="num" value="${(defaultNum+3)!0}">
                                </td>
                                <td align="right"><a href="javascript:view_pay_detail(${(defaultNum+3)!0})">查看支付详情</a></td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次押金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentForegiftMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.foregiftMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次租金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentPacketMoney"
                                                          maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.packetMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次保险支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentInsuranceMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.insuranceMoney)!0)/100}"/>&nbsp;元
                                </td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次支付时间:</td>
                                <td colspan="2">
                                    <input type="text" class="text easyui-datebox" style="width:185px;height:28px " value="<#if (detail.expireTime)?? >${app.format_date_time(detail.expireTime)}</#if>"
                                           name="recentExpireTime" >
                                </td>
                            </tr>
                        </table>
                        <div style="padding-top: 10px;">
                            <hr>
                        </div>
                    </div>
                </#if>

                <#if defaultNum+4 &lt;= 10>
                    <div style="padding-top: 10px;display: none;" id="num_${defaultNum+4}" >
                        <table>
                            <tr>
                                <td align="left" colspan="5">分期${defaultNum+4}: &nbsp;&nbsp;&nbsp;&nbsp;<span
                                        style="color: red;">(未支付)</span>
                                    <input type="hidden" name="num" value="${(defaultNum+4)!0}">
                                </td>
                                <td align="right"><a href="javascript:view_pay_detail(${(defaultNum+4)!0})">查看支付详情</a></td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次押金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentForegiftMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.foregiftMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次租金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentPacketMoney"
                                                          maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.packetMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次保险支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentInsuranceMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.insuranceMoney)!0)/100}"/>&nbsp;元
                                </td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次支付时间:</td>
                                <td colspan="2">
                                    <input type="text" class="text easyui-datebox" style="width:185px;height:28px " value="<#if (detail.expireTime)?? >${app.format_date_time(detail.expireTime)}</#if>"
                                           name="recentExpireTime" >
                                </td>
                            </tr>
                        </table>
                        <div style="padding-top: 10px;">
                            <hr>
                        </div>
                    </div>
                </#if>

                <#if defaultNum+5 &lt;= 10>
                    <div style="padding-top: 10px;display: none;" id="num_${defaultNum+5}" >
                        <table>
                            <tr>
                                <td align="left" colspan="5">分期${defaultNum+5}: &nbsp;&nbsp;&nbsp;&nbsp;<span
                                        style="color: red;">(未支付)</span>
                                    <input type="hidden" name="num" value="${(defaultNum+5)!0}">
                                </td>
                                <td align="right"><a href="javascript:view_pay_detail(${(defaultNum+5)!0})">查看支付详情</a></td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次押金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentForegiftMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.foregiftMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次租金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentPacketMoney"
                                                          maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.packetMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次保险支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentInsuranceMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.insuranceMoney)!0)/100}"/>&nbsp;元
                                </td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次支付时间:</td>
                                <td colspan="2">
                                    <input type="text" class="text easyui-datebox" style="width:185px;height:28px " value="<#if (detail.expireTime)?? >${app.format_date_time(detail.expireTime)}</#if>"
                                           name="recentExpireTime" >
                                </td>
                            </tr>
                        </table>
                        <div style="padding-top: 10px;">
                            <hr>
                        </div>
                    </div>
                </#if>

                <#if defaultNum+6 &lt;= 10>
                    <div style="padding-top: 10px;display: none;" id="num_${defaultNum+6}" >
                        <table>
                            <tr>
                                <td align="left" colspan="5">分期${defaultNum+6}: &nbsp;&nbsp;&nbsp;&nbsp;<span
                                        style="color: red;">(未支付)</span>
                                    <input type="hidden" name="num" value="${(defaultNum+6)!0}">
                                </td>
                                <td align="right"><a href="javascript:view_pay_detail(${(defaultNum+6)!0})">查看支付详情</a></td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次押金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentForegiftMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.foregiftMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次租金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentPacketMoney"
                                                          maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.packetMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次保险支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentInsuranceMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.insuranceMoney)!0)/100}"/>&nbsp;元
                                </td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次支付时间:</td>
                                <td colspan="2">
                                    <input type="text" class="text easyui-datebox" style="width:185px;height:28px " value="<#if (detail.expireTime)?? >${app.format_date_time(detail.expireTime)}</#if>"
                                           name="recentExpireTime" >
                                </td>
                            </tr>
                        </table>
                        <div style="padding-top: 10px;">
                            <hr>
                        </div>
                    </div>
                </#if>

                <#if defaultNum+7 &lt;= 10>
                    <div style="padding-top: 10px;display: none;" id="num_${defaultNum+7}" >
                        <table>
                            <tr>
                                <td align="left" colspan="5">分期${defaultNum+7}: &nbsp;&nbsp;&nbsp;&nbsp;<span
                                        style="color: red;">(未支付)</span>
                                    <input type="hidden" name="num" value="${(defaultNum+7)!0}">
                                </td>
                                <td align="right"><a href="javascript:view_pay_detail(${(defaultNum+7)!0})">查看支付详情</a></td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次押金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentForegiftMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.foregiftMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次租金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentPacketMoney"
                                                          maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.packetMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次保险支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentInsuranceMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.insuranceMoney)!0)/100}"/>&nbsp;元
                                </td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次支付时间:</td>
                                <td colspan="2">
                                    <input type="text" class="text easyui-datebox" style="width:185px;height:28px " value="<#if (detail.expireTime)?? >${app.format_date_time(detail.expireTime)}</#if>"
                                           name="recentExpireTime" >
                                </td>
                            </tr>
                        </table>
                        <div style="padding-top: 10px;">
                            <hr>
                        </div>
                    </div>
                </#if>

                <#if defaultNum+8 &lt;= 10>
                    <div style="padding-top: 10px;display: none;" id="num_${defaultNum+8}" >
                        <table>
                            <tr>
                                <td align="left" colspan="5">分期${defaultNum+8}: &nbsp;&nbsp;&nbsp;&nbsp;<span
                                        style="color: red;">(未支付)</span>
                                    <input type="hidden" name="num" value="${(defaultNum+8)!0}">
                                </td>
                                <td align="right"><a href="javascript:view_pay_detail(${(defaultNum+8)!0})">查看支付详情</a></td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次押金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentForegiftMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.foregiftMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次租金支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentPacketMoney"
                                                          maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.packetMoney)!0)/100}"/>&nbsp;元
                                </td>
                                <td width="120px;" align="left">本次保险支付:</td>
                                <td width="170px;"><input type="text" class="text easyui-numberspinner"
                                                          name="recentInsuranceMoney"  maxlength="10"
                                                          data-options="min:0, precision:2"
                                                          style="width:120px;height:28px " value="${((detail.insuranceMoney)!0)/100}"/>&nbsp;元
                                </td>
                            </tr>
                            <tr>
                                <td width="120px;" align="left">本次支付时间:</td>
                                <td colspan="2">
                                    <input type="text" class="text easyui-datebox" style="width:185px;height:28px " value="<#if (detail.expireTime)?? >${app.format_date_time(detail.expireTime)}</#if>"
                                           name="recentExpireTime" >
                                </td>
                            </tr>
                        </table>
                        <div style="padding-top: 10px;">
                            <hr>
                        </div>
                    </div>
                </#if>
            </div>

        </form>
    </div>
</div>
<div class="popup_btn" style="height: 5%;">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>

<script>
    function showRecentMoney() {
        var foregiftMoney = parseInt(Math.round($('#foregift_money').val()*100));
        var packetMoney = parseInt(Math.round($('#packet_money').val()*100));
        var insuranceMoney = parseInt(Math.round($('#insurance_money').val()*100));

        var totalMoney = Number((foregiftMoney + packetMoney + insuranceMoney)/100);
        $('#money_detail').html(totalMoney+"元");
        $('#total_money').val(totalMoney);
    }

    function hidePriceInfo() {
        //套餐信息隐藏
        //押金
        $.post('${contextPath}/security/zd/rent_installment_setting/rent_battery_foregift.htm', {
            batteryType: 0,
            agentId: 0
        }, function (html) {
            $("#rent_battery_foregift").html(html);
        }, 'html');

        //换电包时段套餐
        $.post('${contextPath}/security/zd/rent_installment_setting/rent_packet_period_price.htm', {
            foregiftId: 0,
            batteryType: 0,
            agentId: 0
        }, function (html) {
            $("#rent_packet_period_price").html(html);
        }, 'html');

        //保险
        $.post('${contextPath}/security/zd/rent_installment_setting/rent_insurance.htm', {
            batteryType: 0,
            agentId: 0
        }, function (html) {
            $("#rent_insurance").html(html);
        }, 'html');

        $('#foregift_money').val('');
        $('#foregift_id').val('');
        $('#packet_money').val('');
        $('#packet_id').val('');
        $('#insurance_money').val('');
        $('#insurance_id').val('');
        $('#money_detail').html('');
        $('#total_money').val('');
    }

    function switch_battery_type_${pid}() {
        hidePriceInfo();

        var batteryType = $('#page_battery_type').combotree('getValue');
        var agentId = $('#page_agent_id').combotree('getValue');
        if (batteryType != null && agentId != null) {
            //获取套餐信息
            $.post("${contextPath}/security/zd/rent_installment_setting/find_price_info.htm?" + "batteryType=" + batteryType +"&agentId=" + agentId, function (json) {
                if (json.success) {
                    var data = json.data;
                    var batteryForegift = data[0];
                    var packetPeriodPrice = data[1];
                    var insurance = data[2];
                    if(batteryForegift != null) {
                        var foregiftMoney = batteryForegift.money;
                        var foregiftMoneyNum = new Number(foregiftMoney / 100).toFixed(2);
                        $('#foregift_id').val(batteryForegift.id);
                        $('#foregift_money').val(foregiftMoneyNum);
                    }
                    if(packetPeriodPrice != null) {
                        var packetMoney = packetPeriodPrice.price;
                        var packetMoneyNum = new Number(packetMoney / 100).toFixed(2);
                        $('#packet_id').val(packetPeriodPrice.id);
                        $('#packet_money').val(packetMoneyNum);
                    }
                    if(insurance != null) {
                        var insuranceMoney = insurance.price;
                        var insuranceMoneyNum = new Number(insuranceMoney / 100).toFixed(2);
                        $('#insurance_id').val(insurance.id);
                        $('#insurance_money').val(insuranceMoneyNum);
                    }
                    var foregiftMoney = parseInt(Math.round($('#foregift_money').val()*100));
                    var packetMoney = parseInt(Math.round($('#packet_money').val()*100));
                    var insuranceMoney = parseInt(Math.round($('#insurance_money').val()*100));

                    var totalMoney = Number((foregiftMoney + packetMoney + insuranceMoney)/100);
                    $('#money_detail').html(totalMoney+"元");
                    $('#total_money').val(totalMoney);

                    //押金
                    $.post('${contextPath}/security/zd/rent_installment_setting/rent_battery_foregift.htm', {
                        batteryType: batteryType,
                        agentId: agentId
                    }, function (html) {
                        $("#rent_battery_foregift").html(html);
                    }, 'html');

                    //换电包时段套餐
                    $.post('${contextPath}/security/zd/rent_installment_setting/rent_packet_period_price.htm', {
                        foregiftId: $('#foregift_id').val(),
                        batteryType: batteryType,
                        agentId: agentId
                    }, function (html) {
                        $("#rent_packet_period_price").html(html);
                    }, 'html');

                    //保险
                    $.post('${contextPath}/security/zd/rent_installment_setting/rent_insurance.htm', {
                        batteryType: batteryType,
                        agentId: agentId
                    }, function (html) {
                        $("#rent_insurance").html(html);
                    }, 'html');

                }
            }, 'json');
        }
    }

    function findCustomer() {
        var mobile = $('#mobile_${pid}').val();
        $.ajax({
            cache: false,
            async: false,
            type: 'POST',
            url: '${contextPath}/security/basic/customer/find_by_mobile.htm',
            data: {
                mobile: mobile
            },
            dataType: 'json',
            success: function (json) {
            <@app.json_jump/>
                if (json.success) {
                    var data = json.data;
                    $('#fullname_${pid}').val(data.fullname);
                }else{
                    $('#fullname_${pid}').val('');
                }
            }
        });
    }

    function view_pay_detail(num) {
        App.dialog.show({
            css: 'width:479px;height:505px;overflow:visible;',
            title: '支付详情',
            href: "${contextPath}/security/zd/rent_installment_setting/view_pay_detail.htm?settingId=${(entity.id)!''}" + "&num=" + num
        });
    }

    function show_detail_${pid}() {
        //清空分期详情表单值
        $('input[name=num]').each(function () {
            $(this).parent().parent().parent().find('input[name=recentForegiftMoney]').val('');
            $(this).parent().parent().parent().find('input[name=recentPacketMoney]').val('');
            $(this).parent().parent().parent().find('input[name=recentInsuranceMoney]').val('');
            $(this).parent().parent().parent().find('input[name=recentExpireTime]').val('');
        });
        var installmentNum = $('#num_${pid}').combobox('getValue');
        var num = new Number(installmentNum);
        if(num == 2) {
            document.getElementById("num_3").style.display = "none";
            document.getElementById("num_4").style.display = "none";
            document.getElementById("num_5").style.display = "none";
            document.getElementById("num_6").style.display = "none";
            document.getElementById("num_7").style.display = "none";
            document.getElementById("num_8").style.display = "none";
            document.getElementById("num_9").style.display = "none";
            document.getElementById("num_10").style.display = "none";
        }
        if(num == 3) {
            document.getElementById("num_3").style.display = "block";
            document.getElementById("num_4").style.display = "none";
            document.getElementById("num_5").style.display = "none";
            document.getElementById("num_6").style.display = "none";
            document.getElementById("num_7").style.display = "none";
            document.getElementById("num_8").style.display = "none";
            document.getElementById("num_9").style.display = "none";
            document.getElementById("num_10").style.display = "none";
        }
        if(num == 4) {
            document.getElementById("num_3").style.display = "block";
            document.getElementById("num_4").style.display = "block";
            document.getElementById("num_5").style.display = "none";
            document.getElementById("num_6").style.display = "none";
            document.getElementById("num_7").style.display = "none";
            document.getElementById("num_8").style.display = "none";
            document.getElementById("num_9").style.display = "none";
            document.getElementById("num_10").style.display = "none";
        }
        if(num == 5) {
            document.getElementById("num_3").style.display = "block";
            document.getElementById("num_4").style.display = "block";
            document.getElementById("num_5").style.display = "block";
            document.getElementById("num_6").style.display = "none";
            document.getElementById("num_7").style.display = "none";
            document.getElementById("num_8").style.display = "none";
            document.getElementById("num_9").style.display = "none";
            document.getElementById("num_10").style.display = "none";
        }
        if(num == 6) {
            document.getElementById("num_3").style.display = "block";
            document.getElementById("num_4").style.display = "block";
            document.getElementById("num_5").style.display = "block";
            document.getElementById("num_6").style.display = "block";
            document.getElementById("num_7").style.display = "none";
            document.getElementById("num_8").style.display = "none";
            document.getElementById("num_9").style.display = "none";
            document.getElementById("num_10").style.display = "none";
        }
        if(num == 7) {
            document.getElementById("num_3").style.display = "block";
            document.getElementById("num_4").style.display = "block";
            document.getElementById("num_5").style.display = "block";
            document.getElementById("num_6").style.display = "block";
            document.getElementById("num_7").style.display = "block";
            document.getElementById("num_8").style.display = "none";
            document.getElementById("num_9").style.display = "none";
            document.getElementById("num_10").style.display = "none";
        }
        if(num == 8) {
            document.getElementById("num_3").style.display = "block";
            document.getElementById("num_4").style.display = "block";
            document.getElementById("num_5").style.display = "block";
            document.getElementById("num_6").style.display = "block";
            document.getElementById("num_7").style.display = "block";
            document.getElementById("num_8").style.display = "block";
            document.getElementById("num_9").style.display = "none";
            document.getElementById("num_10").style.display = "none";
        }
        if(num == 9) {
            document.getElementById("num_3").style.display = "block";
            document.getElementById("num_4").style.display = "block";
            document.getElementById("num_5").style.display = "block";
            document.getElementById("num_6").style.display = "block";
            document.getElementById("num_7").style.display = "block";
            document.getElementById("num_8").style.display = "block";
            document.getElementById("num_9").style.display = "block";
            document.getElementById("num_10").style.display = "none";
        }
        if(num == 10) {
            document.getElementById("num_3").style.display = "block";
            document.getElementById("num_4").style.display = "block";
            document.getElementById("num_5").style.display = "block";
            document.getElementById("num_6").style.display = "block";
            document.getElementById("num_7").style.display = "block";
            document.getElementById("num_8").style.display = "block";
            document.getElementById("num_9").style.display = "block";
            document.getElementById("num_10").style.display = "block";
        }
    }

    (function () {
        var win = $('#${pid}'), form = win.find('form');

        var batteryType = ${(entity.batteryType)!''};
        var agentId = ${(entity.agentId)!''};

        //押金
        $.post('${contextPath}/security/zd/rent_installment_setting/edit_rent_battery_foregift.htm', {
            foregiftId: $('#foregift_id').val(),
            batteryType: batteryType,
            agentId: agentId,
        }, function (html) {
            $("#rent_battery_foregift").html(html);
        }, 'html');

        //换电包时段套餐
        $.post('${contextPath}/security/zd/rent_installment_setting/edit_rent_packet_period_price.htm', {
            foregiftId: $('#foregift_id').val(),
            packetId: $('#packet_id').val(),
            batteryType: batteryType,
            agentId: agentId
        }, function (html) {
            $("#rent_packet_period_price").html(html);
        }, 'html');

        //保险
        $.post('${contextPath}/security/zd/rent_installment_setting/edit_rent_insurance.htm', {
            insuranceId: $('#insurance_id').val(),
            batteryType: batteryType,
            agentId: agentId
        }, function (html) {
            $("#rent_insurance").html(html);
        }, 'html');

        $('#deadline_time_${pid}').datebox().datebox('calendar').calendar({
            validator: function(date){
                var now = new Date();
                var d1 = new Date(now.getYear(), now.getMonth(), now.getDate());
                return d1<=date;
            }
        });

        var installmentNum = ${(defaultNum)!0};
        var num = new Number(installmentNum);
        for(var i = 1; i <= num ; i++) {
            if(i == 2) {
                document.getElementById("num_1").style.display = "block";
                document.getElementById("num_2").style.display = "block";
            }
            if(i == 3) {
                document.getElementById("num_3").style.display = "block";
            }
            if(i == 4) {
                document.getElementById("num_4").style.display = "block";
            }
            if(i == 5) {
                document.getElementById("num_5").style.display = "block";
            }
            if(i == 6) {
                document.getElementById("num_6").style.display = "block";
            }
            if(i == 7) {
                document.getElementById("num_7").style.display = "block";
            }
            if(i == 8) {
                document.getElementById("num_8").style.display = "block";
            }
            if(i == 9) {
                document.getElementById("num_9").style.display = "block";
            }
            if(i == 10) {
                document.getElementById("num_10").style.display = "block";
            }
        }

        $('input[name=recentExpireTime]').each(function () {
            $(this).parent().parent().parent().find('input[name=recentExpireTime]').datebox().datebox('calendar').calendar({
                validator: function(date){
                    var now = new Date();
                    var d1 = new Date(now.getYear(), now.getMonth(), now.getDate());
                    return d1<=date;
                }
            });
        });

        win.find('button.ok').click(function () {
            if (!form.form('validate')) {
                return false;
            }

            var foregiftId = $('#foregift_id').val();
            var packetId = $('#packet_id').val();
            if(foregiftId == null || foregiftId== '' || foregiftId == 0) {
                $.messager.alert('提示信息', '请先配置押金', 'info');
                return;
            }
            if(packetId == null || packetId == '' || packetId == 0) {
                $.messager.alert('提示信息', '请先配置租金', 'info');
                return;
            }

            //时间校验
            //第一个分期详情的日期要大于截至日期，第二个分期详情的日期要大于第一个分期详情的日期。。。
            var deadlineDate = $('#deadline_time_${pid}').datebox('getValue');
            var lastDate;
            var success = true;
            if(deadlineDate != null && deadlineDate != '') {
                $('input[name=recentExpireTime]').each(function () {
                    var checkTime = $(this).parent().parent().parent().find('input[name=recentExpireTime]').val();
                    var num = $(this).parent().parent().parent().parent().find('input[name=num]').val();
                    var checkNum = new Number(num);
                    var endNum = $('#num_${pid}').combobox('getValue');
                    if(checkNum > endNum) {

                    }else{
                        if(checkTime <= deadlineDate) {
                            $.messager.alert('提示信息', '分期' + checkNum + '支付时间必须大于截止时间', 'info');
                            success = false;
                            return;
                        }
                        if(checkNum == 1) {
                            lastDate = checkTime;
                        }else{
                            if(checkTime <= lastDate) {
                                $.messager.alert('提示信息', '分期' + checkNum +'支付时间必须大于分期' + (new Number(checkNum - 1)) + '支付时间', 'info');
                                success = false;
                                return;
                            }
                            lastDate = checkTime;
                        }
                    }
                });
            }

            //金额校验
            var checkForegiftMoney = parseInt(Math.round($('#foregift_money').val()*100));
            var checkPacketMoney = parseInt(Math.round($('#packet_money').val()*100));
            var checkInsuranceMoney = parseInt(Math.round($('#insurance_money').val()*100));
            var checkTotalMoney = parseInt(Math.round($('#total_money').val()*100));
            var checkForegift = new Number(0);
            var checkPacket = new Number(0);
            var checkInsurance = new Number(0);
            var checkTotal = new Number(0);

            $('input[name=recentExpireTime]').each(function () {
                var checkRecentForegiftMoneyNum = $(this).parent().parent().parent().parent().find('input[name=recentForegiftMoney]').val();
                var checkRecentForegiftMoney = parseInt(Math.round(checkRecentForegiftMoneyNum*100));
                var checkRecentPacketMoneyNum = $(this).parent().parent().parent().parent().find('input[name=recentPacketMoney]').val();
                var checkRecentPacketMoney = parseInt(Math.round(checkRecentPacketMoneyNum*100));
                var checkRecentInsuranceMoneyNum = $(this).parent().parent().parent().parent().find('input[name=recentInsuranceMoney]').val();
                var checkRecentInsuranceMoney = parseInt(Math.round(checkRecentInsuranceMoneyNum*100));
                checkForegift = checkForegift + checkRecentForegiftMoney;
                checkPacket = checkPacket + checkRecentPacketMoney;
                checkInsurance = checkInsurance + checkRecentInsuranceMoney;
                checkTotal = checkForegift + checkPacket + checkInsurance;
            });

            if(checkForegift != checkForegiftMoney) {

                $.messager.alert('提示信息', '分期押金之和必须等于总押金', 'info');
                success = false;
                return;
            }
            if(checkPacket != checkPacketMoney) {
                $.messager.alert('提示信息', '分期租金之和必须等于总租金', 'info');
                success = false;
                return;
            }
            if(checkInsurance != checkInsuranceMoney) {
                $.messager.alert('提示信息', '分期保险金额之和必须等于总保险金额', 'info');
                success = false;
                return;
            }
            if(checkTotal != checkTotalMoney) {
                $.messager.alert('提示信息', '分期金额之和必须等于总金额', 'info');
                success = false;
                return;
            }


            if(success) {
                var values = {};
                var foregiftMoney = parseInt(Math.round($('#foregift_money').val()*100));
                var packetMoney = parseInt(Math.round($('#packet_money').val()*100));
                var insuranceMoney = parseInt(Math.round($('#insurance_money').val()*100));
                var totalMoney = parseInt(Math.round($('#total_money').val()*100));
                values["id"] = $('#id_${pid}').val();
                values["mobile"] = $('#mobile_${pid}').val();
                values["fullname"] = $('#fullname_${pid}').val();
                values["agentId"] = $('#page_agent_id').combotree('getValue');
                values["deadlineTime"] =  $('#deadline_time_${pid}').datebox('getValue').substring(0, 10) + " 23:59:59";
                values["totalMoney"] = totalMoney;
                values["batteryType"] = $('#page_battery_type').combotree('getValue');
                values["foregiftId"] = $('#foregift_id').val();
                values["foregiftMoney"] = foregiftMoney;
                values["packetId"] = $('#packet_id').val();
                values["packetMoney"] = packetMoney;
                values["insuranceId"] = $('#insurance_id').val();
                values["insuranceMoney"] = insuranceMoney;

                var detailList = [];
                $('input[name=num]').each(function () {
                    var recentForegiftMoneyNum = $(this).parent().parent().parent().find('input[name=recentForegiftMoney]').val();
                    var recentForegiftMoney = parseInt(Math.round(recentForegiftMoneyNum*100));
                    var recentPacketMoneyNum = $(this).parent().parent().parent().find('input[name=recentPacketMoney]').val();
                    var recentPacketMoney = parseInt(Math.round(recentPacketMoneyNum*100));
                    var recentInsuranceMoneyNum = $(this).parent().parent().parent().find('input[name=recentInsuranceMoney]').val();
                    var recentInsuranceMoney = parseInt(Math.round(recentInsuranceMoneyNum*100));
                    var num = $(this).parent().parent().parent().find('input[name=num]').val();
                    var recentExpireTime = $(this).parent().parent().parent().find('input[name=recentExpireTime]').val();
                    var detailMap = {};
                    detailMap["recentForegiftMoney"] = recentForegiftMoney;
                    detailMap["recentPacketMoney"] = recentPacketMoney;
                    detailMap["recentInsuranceMoney"] = recentInsuranceMoney;
                    detailMap["num"] = num;
                    detailMap["recentExpireTime"] = recentExpireTime.substring(0, 10) + " 23:59:59";
                    detailList.push(detailMap);
                });
                values["detailList"] = detailList;

                $.post('${contextPath}/security/zd/rent_installment_setting/update.htm', {
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
            }


        });

        win.find('button.close').click(function () {
            win.window('close');
        });

    })();

</script>


