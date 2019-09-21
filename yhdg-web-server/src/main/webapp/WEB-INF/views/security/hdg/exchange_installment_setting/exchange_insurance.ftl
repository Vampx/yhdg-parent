<td colspan="2">
    <div class="zj_list">
    <#if insuranceList?? && (insuranceList?size>0) >
        <#list insuranceList as insurance>
            <div style="width: 190px;" <#if insurance_index == 0>class="zj_item insurance_rent selected"<#else>class="zj_item insurance_rent"</#if>
                    insurance_id="${(insurance.id)!0}"
                    insurance_money="<#if insurance.price??>${(insurance.price)/100}<#else>0</#if>">
                <p class="num"><#if insurance.price??>${(insurance.price)/100}<#else>0</#if> 元 保额:<#if insurance.paid??>${(insurance.paid)/100}<#else>0</#if> 元/${(insurance.monthCount)!0} 月</p>
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