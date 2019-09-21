<div class="popup_box popup_full">
    <#--<div class="popup_head">-->
        <#--<h3>快递柜监控</h3>-->
        <#--<a class="close" href="javascript:void(0)"><span class="icon"></span></a>-->
    <#--</div>-->
    <div class="popup_body popup_body_full clearfix" style="top: 0px;">
        <div class="battery_monitor">
            <div class="battery_id">
                <p>${(entity.id)!''}<span>${(entity.statusName)!''}</span></p>
            </div>
            <ul class="battery_usage">
                <li>
                    <div class="green">
                        <small>当前电量</small>
                        <strong>${(entity.volume)!0}%</strong>
                    </div>
                </li>
                <li>
                    <div class="blue">
                        <small>行驶总里程</small>
                        <strong>${(totalDistance)!0}Km</strong>
                    </div>
                </li>
                <li>
                    <div class="yellow">
                        <small>换电记录</small>
                        <strong>${(entity.exchangeAmount)!0}次</strong>
                    </div>
                </li>
                <li>
                    <div class="red">
                        <small>报修次数</small>
                        <strong>0次</strong>
                    </div>
                </li>
            </ul>
            <ul class="battery_usage_list">
                <div id="operate_log_list">
                    <#if batteryOperateLogList??>
                       <#list batteryOperateLogList as bol>
                           <li OPER_ID="${bol.id}">
                               <div class="txt">
                                   <span>${app.format_date_time(bol.createTime)}</span>
                               </div>
                               <div class="txt">
                                   <span>类型：${(bol.operateTypeName)!''}</span>
                                   <#if bol.customerId??>
                                       <span>客户：${(bol.customerMobile)!''}</span>
                                   </#if>
                                   <#if bol.cabinetId?? && bol.subcabinetId??>
                                       <span>主柜：${(bol.cabinetName)!''}</span>
                                       <span>格口：${(bol.boxNum)!''}</span>
                                   </#if>
                                   <#if bol.keeperId??>
                                       <span>维护人员：${(bol.keeperMobile)!''}</span>
                                   </#if>
                                   <span>当前电量：${(bol.volume)!0}%</span>
                               </div>
                           </li>
                       </#list>
                    </#if>
                </div>
                <li class="more">
                    <a id="more_${pid}" href="javascript:void(0);" onclick="loadMore();">加载更多</a>
                </li>
            </ul>
        </div>
    </div>
    <div class="popup_btn popup_btn_full">
        <button class="btn btn_border">关闭</button>
    </div>
</div>
<script>
    var batteryId = '${(entity.id)!''}';
    var id = 0;

    function loadMore() {
        id = $('#operate_log_list > li').last().attr('OPER_ID');
        if (id != null && id != undefined) {
            $.post("${contextPath}/security/zd/battery/load_more.htm?id=" + id + "&batteryId=" + batteryId + "", function (html){
                $('#operate_log_list').append(html);
            }, 'html');
        }
    }

    (function() {
        var pid = '${pid}',
                win = $('#' + pid);
        win.find('button.btn_border').click(function() {
            win.window('close');
        });
    })();
</script>