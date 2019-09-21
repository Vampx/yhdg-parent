<div class="deposit">
    <div class="deposit-list vip_battery_foregift_list">
    <#if exchangeBatteryForegiftList?? && (exchangeBatteryForegiftList?size>0) >
        <#list exchangeBatteryForegiftList as exchangeBatteryForegift>
            <div <#if index == exchangeBatteryForegift_index >class="active battery_foregift"<#else>class="battery_foregift"</#if>
                 battery_foregift_id="${(exchangeBatteryForegift.id)!0}">
                <p>金额:<#if exchangeBatteryForegift.money??>${(exchangeBatteryForegift.money)/100}<#else>0</#if> 元</p>
                <p class="battery_reduce">减免:<#if exchangeBatteryForegift.reduceMoney??>${(exchangeBatteryForegift.reduceMoney)/100}<#else>0</#if> 元</p>
                <div >
                    <img class="edit_foregift" vip_exchange_id="${(exchangeBatteryForegift.vipExchangeId)!0}"
                         vip_index = ${exchangeBatteryForegift_index} foregift_id="${(exchangeBatteryForegift.foregiftId)!0}"
                         src="${app.imagePath}/edit2.png" />
                </div>
            </div>
        </#list>
    <#else>

    </#if>

    </div>

</div>
<div class="deposit-tips">
    <#include 'vip_exchange_battery_foregift_memo.ftl'>
</div>
<script>

    $(function() {

        $(".edit_foregift").click(function () {
            var foregiftId = $(this).attr("foregift_id");
            var vipExchangeId = $(this).attr("vip_exchange_id");
            var index = $(this).attr("vip_index");
            App.dialog.show({
                css: 'width:400px;height:335px;overflow:visible;',
                title: '修改减免金额',
                href: "${contextPath}/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift_add.htm?foregiftId="+ foregiftId + "&vipExchangeId=" + vipExchangeId,
                windowData:{
                    batteryType : ${(batteryType)!0},
                    agentId : ${(agentId)!0},
                    foregiftId : foregiftId,
                    index: index,
                    ok: function (config) {
                        if (config) {
                            $(".deposit-list").find(".active").find(".battery_reduce").html("减免: "+config.reduceMoney/100+" 元");
                            $(".deposit-tips").html("押金备注： " + config.memo);
                            $(".deposit-list").find(".active").find(".edit_foregift").attr("vip_exchange_id",config.vipExchangeId);
                            if (vipExchangeId != config.vipExchangeId) {
                                add_foregift_reduce_money_table(config.vipExchangeId);
                            }
                            return true;
                        } else {
                            $.messager.alert('提示信息', '', 'info');
                            return false;
                        }
                    }
                },
                event: {
                    onClose: function() {

                    }
                }
            });

        });

        //随点击切换 包时段套餐
        $('.vip_battery_foregift_list .battery_foregift').click(function () {
            $(this).addClass("active").siblings().removeClass("active");
            //获取押金id
            var batteryForegiftId = $(this).attr("battery_foregift_id");
            var vipExchangeId = $(this).find(".edit_foregift").attr("vip_exchange_id");

            //利用押金id去查找租金
            $.post('${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price.htm', {
                foregiftId: batteryForegiftId,
                batteryType: ${(batteryType)!0},
                agentId: ${(agentId)!0},
                vipForegiftId: vipExchangeId
            }, function (html) {
                $("#exchange_packet_period_price").html(html);
            }, 'html');

            if (vipExchangeId != 0) {
                //利用押金id去查找押金备注
                $.post('${contextPath}/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift_memo.htm', {
                    vipExchangeId: vipExchangeId
                }, function (html) {
                    $(".deposit-tips").html(html);
                }, 'html');
            }

        });
    });

    function add_foregift_reduce_money_table(vipExchangeId){
        var foregiftReduceMoney = $("#foregift_reduce_money_table");
        var length = foregiftReduceMoney.find("tr").length;
        var html =
                '  <tr>\n'+
                '    <input type="hidden" name="vipExchangeId" value="'+vipExchangeId+'">\n'+
                '  </tr>';
        foregiftReduceMoney.find('tr').eq(length-1).before(html);
    }
</script>
