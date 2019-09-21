
<div class="state-list">
    <table>
        <tr>
            <td>
                <table id="vip_price_customer_mobile">
                    <#if vipPriceCustomerList?? && (vipPriceCustomerList?size>0) >
                        <#list vipPriceCustomerList as vipPriceCustomer>
                            <tr>
                                <input type="hidden" name="customerMobile" id="customer_mobile" value="${(vipPriceCustomer.mobile)!''}">
                                <input type="hidden" name="customerFullname" id="customer_fullname" value="${(vipPriceCustomer.customerFullname)!''}">
                                <td>
                                    <input type="text" class="text easyui-validatebox" readonly maxlength="40" style="width:175px;height:28px " id="fullname" name="fullname" value="${(vipPriceCustomer.mobile)!''}<#if vipPriceCustomer.mobile??>(${(vipPriceCustomer.customerFullname)!''})</#if>"
                                </td>
                                <td>
                                    <img style="cursor: pointer;" onclick="delete_vip_customer_mobile(this)" src="${app.imagePath}/delete.png">
                                </td>
                            </tr>
                        </#list>
                    </#if>
                    <tr>
                        <td>
                            <input type="text" id="mobile" name="mobile"  class="text easyui-validatebox" maxlength="11"
                                   style="width:175px;height:28px ">
                        </td>
                        <td>
                            <button class="btn btn_green" style="background: none;border: none; color: #4263FF;cursor: pointer;" type="button"  type="button" onclick="add_vip_customer_mobile()">添加</button>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>

<script>

    function add_vip_customer_mobile() {
        var agentId = ${(agentId)!0};
        var mobile = $("#mobile").val();
        var boolean = /^1\d{10}$/.test(mobile);
        if(boolean){
            var customerId ,customerFullname;
            var customerMobile = mobile;
            if(isCustomerMobileRepeat(customerMobile)){
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'GET',
                    url: '${contextPath}/security/hdg/vip_price_customer/add_vip_customer_mobile.htm?mobile='+mobile,
                    dataType: 'json',
                    success: function (test) {
                        if (test.success) {
                            var json = test.data;
                            if(json != null){
                                customerId= json.id;
                                customerFullname= json.fullname;
                            }
                            add_vip_customer_mobile_table(customerId,customerMobile,customerFullname);
                        }else{
                            $.messager.alert('提示信息', test.message, 'info');
                        }
                    }
                });

            }else{
                $.messager.alert('提示信息', '该手机号已存在', 'info');
            }
        }else{
            $.messager.alert('提示信息', '请输入正确手机号', 'info');
        }
    }

    function isCustomerMobileRepeat(customerMobile) {

        var boolean =true;
        var exchangeInstallmentCustomer = $("#vip_price_customer_mobile");
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

    function add_vip_customer_mobile_table(customerId,customerMobile,customerFullname){
        var exchangeInstallmentCustomer = $("#vip_price_customer_mobile");
        var trlength = exchangeInstallmentCustomer.find("tr").length;
        var mobileAndCustomerFullname
        if(customerFullname ==null ||customerFullname =="" ||customerFullname ==undefined){
            mobileAndCustomerFullname=customerMobile;
        }else{
            mobileAndCustomerFullname=customerMobile+'('+customerFullname+')';
        }
        var html =
                '  <tr>\n'+
                '    <input type="hidden" name="customerId" id="customer_id" value="'+customerId+'">\n'+
                '    <input type="hidden" name="customerMobile" id="customer_mobile" value="'+customerMobile+'">\n'+
                '    <input type="hidden" name="customerFullname" id="customer_fullname" value="'+customerFullname+'">\n'+
                '   <td>\n'+
                '    <input type="text" readonly class="text easyui-validatebox" maxlength="40" style="width:175px;height:28px " id="fullname" name="fullname" value="'+mobileAndCustomerFullname+'"\n'+
                '   </td>\n'+
                '   <td>\n'+
                '     <img style="cursor: pointer;" onclick="delete_vip_customer_mobile(this)" src="${app.imagePath}/delete.png" alt="">\n'+
                '   </td>\n'+
                '  </tr>\n';
        exchangeInstallmentCustomer.find('tr').eq(trlength-1).before(html);
        exchangeInstallmentCustomer.find('tr').eq(trlength).find('#mobile').val('');
    }

    function delete_vip_customer_mobile(obj) {
        $.messager.confirm('提示信息', '确认解绑此骑手号码吗?', function(ok) {
            if(ok) {
                var standard = $(obj);
                standard.parent().parent().remove();
            }
        });
    }

</script>