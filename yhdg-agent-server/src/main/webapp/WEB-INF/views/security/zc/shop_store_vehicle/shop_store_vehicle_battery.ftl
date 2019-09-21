<td colspan="2">
    <div class="zj_list" id="list">
    <#if shopStoreVehicleBatteryList?? && (shopStoreVehicleBatteryList?size>0)>
        <#list shopStoreVehicleBatteryList as storeBattery>
            <div class="zj_item shop_store_vehicle_battery">
                <span class="icon">
                    <a href='javascript:void(0)' onclick='delete_battery(this)'><i class="fa fa-fw fa-close" shop_store_vehicle_battery_battery_id="${(storeBattery.batteryId)!''}"></i></a>
                </span>
                <p class="text" id="${storeBattery.batteryId}">${storeBattery.batteryId}</p>
            </div>
        </#list>
    </#if>
    <div id="add_battery">
        <#if (shopStoreVehicleBatteryList?size!=batteryCount)>
            <a href='javascript:void(0)' onclick='add_battery()' id="batt">
                <div class="zj_add shop_store_vehicle_battery_add" id="add"">
                    <p class="icon"><i class="fa fa-fw fa-plus"></i></p>
                    <p class="text">添加电池</p>
                </div>
            </a>
        </#if>
    </div>
    </div>
</td>

<script>

    function delete_battery(obj) {
        $(obj).parent().parent().remove();
        var items = $(".shop_store_vehicle_battery p");
        if (items.length < ${batteryCount}) {
            var html = "<a href='javascript:void(0)' onclick='add_battery()'>" +
                    "       <div class='zj_add shop_store_vehicle_battery_add'>\n" +
                    "            <p class='icon'><i class='fa fa-fw fa-plus'></i></p>\n" +
                    "            <p class='text'>添加电池</p>\n" +
                    "        </div>" +
                    "   </a>";
            $("#add_battery").html(html);
        }
    }

        function add_battery() {
            if (${batteryType} == 0) {
                $.messager.alert('提示信息',"套餐无电池类型");
                return false;
            }
            var agentId = ${Session['SESSION_KEY_USER'].agentId};
            if (agentId == "" || agentId == null) {
                $.messager.alert('提示信息',"请选择运营商");
            }else {
                App.dialog.show({
                    css: 'width:680px;height:530px;',
                    title: '新建',
                    href: "${contextPath}/security/zc/shop_store_vehicle/shop_store_battery_page.htm?agentId=" + agentId+"&batteryCount=${batteryCount}&category=${category}&batteryType=${(batteryType)!''}",
                    windowData: {
                        ok: function (rows) {
                            var html = "";
                            if (rows.length <= ${batteryCount}) {
                                for (var i = 0; i < rows.length; i++) {
                                    html += "<div class='zj_item shop_store_vehicle_battery'>\n" +
                                            "     <span class='icon'>\n" +
                                            "         <a href='javascript:void(0)' onclick='delete_battery(this)'><i class='fa fa-fw fa-close' shop_store_vehicle_battery_battery_id='"+rows[i].id+"'></i></a>\n" +
                                            "     </span>\n" +
                                            "     <p class='text' id='"+rows[i].id+"'>"+rows[i].id+"</p>\n" +
                                            "</div>"
                                }
                                html += "<div id='add_battery'></div>";
                                $("#add_battery").html("");
                                $("#list").append(html);
                                return true;
                            }
                        }
                    }
                });
            }
        }
</script>