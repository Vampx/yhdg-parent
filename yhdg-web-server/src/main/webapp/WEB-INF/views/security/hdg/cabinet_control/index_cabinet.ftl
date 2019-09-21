<div class="site_item">
<#if page.result??>
    <#list page.result as cabinet>
        <div class="site_info">
            <div class="site_pic"><img src="${contextPath}/static/images/hdz_site.png" /></div>
            <div class="site_name">${cabinet.cabinetName}</div>
        </div>

        <div class="site_details">
            <ul class="site_battery_count" id="list_${(cabinet.id)!''}">
                <li>
                    <div class="green">
                        <strong id="busy_${(cabinet.id)!''}"></strong>
                        <small>可租电池总计</small>
                    </div>
                </li>
                <li>
                    <div class="yellow">
                        <strong id="use_${(cabinet.id)!''}"></strong>
                        <small>可收电池总计</small>
                    </div>
                </li>
                <li>
                    <div class="blue">
                        <strong id="free_${(cabinet.id)!''}"></strong>
                        <small>可还电位</small>
                    </div>
                </li>
            </ul>
            <div class="toolbar">
                <h3>电池监控明细列表：</h3>
            </div>
            <div class="grid">
                <div class="grid_table">
                    <table cellpadding="0" cellspacing="0" border="0" id="table_${(cabinet.id)!''}">
                        <thead>
                        <tr>
                            <th>型号</th>
                            <th>可租电池</th>
                            <th>可收电池</th>
                            <th>可还电位</th>
                        </tr>
                        </thead>
                        <tbody id="cabinet_id_${(cabinet.id)!''}">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </#list>
</#if>
</div>
<div class="paging">
<@pageHtml/>
</div>
<#macro pageHtml method="queryPage" page=page>
<div class="page">
    <ul class="page_l">
        <#if page.totalPages gt 0>
            <#if page.hasPrePage()><li><a href="javascript:${method}(${page.pageNo - 1})">上一页</a></li></#if>
            <#if page.hasNextPage()><li><a href="javascript:${method}(${page.nextPage})">下一页</a></li></#if>
        </#if>
    </ul>
    <ul class="page_r" id="${method}_page_size" page="${page.pageSize}">
        <li>共${page.totalItems}条记录&nbsp;&nbsp;</li>
    </ul>
</div>
<script type="text/javascript">
    var ${method}_page_size = function() {
        return parseInt($('#${method}_page_size .selected').attr('page_size'));
    };
    var ${method}_page_no = function() {

    };
    $('#${method}_page_size a').click(function() {
        $(this).addClass('selected').parent().siblings().find('a').removeClass('selected');
    });

    $(function () {

        var cabinetIdList = [];
        <#if page.result??>
            <#list page.result as cabinet>
                cabinetIdList.push('${cabinet.id}');
            </#list>
        </#if>

        $.post('${contextPath}/security/hdg/cabinet_control/find_battery_list.htm',
                {
                    cabinetId: cabinetIdList
                }, function (json) {
                    if(json.success) {
                        var data = json.data;
                        var sum1 = 0;
                        var sum2 = 0;
                        var sum3 = 0;
                        for (var cabinetId in data) {
                            var str = "";
                            var tbody = $('#cabinet_id_' + cabinetId);
                            //动态拼接电池列表
                            for(var i = 0; i < data[cabinetId].length; i++) {
                                var line = data[cabinetId][i];
                                str += "<tr class='even'>" +
                                        "<td>" + line.batteryTypeName + "</td>" +
                                        "<td>" + line.fullVolumeCount  + "</td>" +
                                        "<td>" + line.waitTakeCount  + "</td>" +
                                        "<td>" + line.emptyBoxCount + "</td>" +
                                        "</tr>";
                            }
                            tbody.html(str);

                            //电池列表循环求和
                            var totalBusy = 0;
                            var totalUse = 0;
                            var totalFree = 0;
                            $("#table_" + cabinetId).find("tr").each(function() {
                                $(this).find('td:eq(1)').each(function(){
                                    totalBusy += parseInt($(this).text());
                                });
                                $(this).find('td:eq(2)').each(function(){
                                    totalUse += parseInt($(this).text());
                                });
                                $(this).find('td:eq(3)').each(function(){
                                    totalFree = parseInt($(this).text());
                                });
                            });
                            $('#busy_'+ cabinetId).text(totalBusy);
                            $('#use_'+ cabinetId).text(totalUse);
                            $('#free_'+ cabinetId).text(totalFree);

                            //电池总计循环求和
                            $("#list_" + cabinetId).each(function(){
                                $(this).find('#busy_'+ cabinetId).each(function(){
                                    sum1 = sum1 + Number($(this).text());
                                });
                                $(this).find('#use_'+ cabinetId).each(function(){
                                    sum2 = sum2 + Number($(this).text());
                                });
                                $(this).find('#free_'+ cabinetId).each(function(){
                                    sum3 = sum3 + Number($(this).text());
                                });
                            });
                            $('#total_full_volume_count').text("可租电池总计：" + sum1 +"块");
                            $('#total_wait_take_count').text("可收电池："+ sum2 +"块");
                            $('#total_empty_box_count').text("可还电位："+ sum3 +"位");
                        }
                    }

                }, 'json');
    });

</script>
</#macro>