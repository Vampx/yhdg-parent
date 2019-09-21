<td colspan="2">
    <div class="zj_list">
    <#if rentInsuranceList?? && (rentInsuranceList?size>0) >
        <#list rentInsuranceList as rentInsurance>
            <div style="width: 190px;" <#if rentInsurance_index == 0>class="zj_item insurance_rent selected"<#else>class="zj_item insurance_rent"</#if>
                    insurance_id="${(rentInsurance.id)!0}"
                    insurance_money="<#if rentInsurance.price??>${(rentInsurance.price)/100}<#else>0</#if>">
                <p class="num"><#if rentInsurance.price??>${(rentInsurance.price)/100}<#else>0</#if> 元 保额:<#if rentInsurance.paid??>${(rentInsurance.paid)/100}<#else>0</#if> 元/${(rentInsurance.monthCount)!0} 月</p>
            </div>
        </#list>
    </#if>
    </div>
</td>

<script>
    (function() {
        $('.insurance_rent').click(function () {
            $(this).addClass("selected").siblings().removeClass("selected");
            //获取保险id
            var insuranceId = $(this).attr("insurance_id");
            var insuranceMoney = $(this).attr("insurance_money");
            $('#insurance_id').val('');
            $('#insurance_money').val('');
            $('#insurance_id').val(insuranceId);
            $('#insurance_money').val(insuranceMoney);
            showRecentMoney();
        });
    })();
</script>