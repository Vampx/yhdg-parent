<div class="state-list">
 <table id="vip_price_station_list">
     <tr>
         <td>
         </td>
     </tr>
 </table>
</div>
<div class="addstate price_station_add">
    添加站点
</div>

<script>

    $(function() {
        $(".price_station_add").click(function () {
            var agentId = $('#page_agent_id').combotree('getValue');
            if(agentId ==null ||agentId =="" ||agentId ==undefined){
                $.messager.alert('提示信息', '请先选择运营商', 'info');
                return;
            }
            App.dialog.show({
                css: 'width:680px;height:530px;',
                title: '新建',
                href: "${contextPath}/security/hdg/station/vip_price_station_page.htm?agentId=" + agentId,
                windowData: {
                    ok: function (rows) {
                        var ids = '';
                        if (rows.length > 0) {
                            for (var i = 0; i < rows.length; i++) {
                                ids += rows[i].stationId + ',';
                                add_vip_station_table(rows[i].stationId ,rows[i].stationName);
                            }
                            ids = ids.substring(0, ids.lastIndexOf(','));
                            var values = {
                                priceId: 0,
                                ids: ids
                            };
                            $.ajax({
                                cache: false,
                                async: false,
                                type: 'POST',
                                url: '${contextPath}/security/hdg/vip_price_station/create.htm',
                                dataType: 'json',
                                data: values,
                                success: function (json) {
                                <@app.json_jump/>
                                    if (json.success) {
                                    }
                                }
                            });
                            return true;
                        } else {
                            $.messager.alert('提示信息', '请选择站点', 'info');
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
    });


    function add_vip_station_table(stationId,stationName) {

        var station_table=$("#vip_price_station_list");
        var trlength = station_table.find("tr").length;
        var boolean = true;
        station_table.find("tr").each(
                function () {
                    var stationIdnew = $(this).find("input[name='stationId']").val();
                    if(stationId == stationIdnew){
                        $(this).find("input[name='stationName']").val(stationName);
                        boolean=false;
                        return
                    }
                }
        );
        if(boolean){
            var html ='<tr>\n'+
                    '    <input type="hidden" name="stationId" id="station_id" value="'+stationId+'">\n'+
                    '    <input type="hidden" name="stationName" id="station_name" value="'+stationName+'">\n'+
                    '   <td>\n'+
                    '    <input type="text" readonly class="text easyui-validatebox" maxlength="40" style="width:175px;height:28px " value="'+stationName+'"\n'+
                    '   </td>\n'+
                    '   <td>\n'+
                    '      <img style="cursor: pointer;" onclick="station_delete(this)" src="${app.imagePath}/delete.png" alt="">\n'+
                    '   </td>\n'+
                    '  </tr>\n';
            station_table.find('tr').eq(trlength-1).before(html);
        }

    }

    function station_delete(obj) {
        $.messager.confirm('提示信息', '确认解绑该站点?', function(ok) {
            if(ok) {
                var standard= $(obj);
                standard.parent().parent().remove();
            }
        });
    }

</script>