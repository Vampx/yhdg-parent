<div class="state-list">
    <table id="vip_price_cabinet_list">
        <tr>
            <td>
            </td>
        </tr>
    </table>
</div>
<div class="addstate vip_price_cabinet_add">
    添加柜子
</div>

<script>

    $(function() {
        $(".vip_price_cabinet_add").click(function () {
            var agentId = $('#page_agent_id').combotree('getValue');
            if(agentId ==null ||agentId =="" ||agentId ==undefined){
                $.messager.alert('提示信息', '请先选择运营商', 'info');
                return;
            }
            App.dialog.show({
                css: 'width:680px;height:530px;',
                title: '新建',
                href: "${contextPath}/security/hdg/cabinet/vip_price_cabinet_page.htm?agentId=" + agentId,
                windowData: {
                    ok: function (rows) {
                        var ids = '';
                        if (rows.length > 0) {
                            for (var i = 0; i < rows.length; i++) {
                                ids += rows[i].cabinetId + ',';
                                add_vip_cabinet_table(rows[i].cabinetId ,rows[i].cabinetName);
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
                                url: '${contextPath}/security/hdg/vip_price_cabinet/create.htm',
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
                            $.messager.alert('提示信息', '请选择柜子', 'info');
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


    function add_vip_cabinet_table(cabinetId,cabinetName) {

        var cabinet_table=$("#vip_price_cabinet_list");
        var trlength = cabinet_table.find("tr").length;
        var boolean = true;
        cabinet_table.find("tr").each(
                function () {
                    var cabinetIdnew = $(this).find("input[name='cabinetId']").val();
                    if(cabinetId==cabinetIdnew){
                        $(this).find("input[name='cabinetName']").val(cabinetName);
                        boolean=false;
                        return
                    }
                }
        );
        if(boolean){
            var html ='<tr>\n'+
                    '    <input type="hidden" name="cabinetId" id="cabinet_id" value="'+cabinetId+'">\n'+
                    '    <input type="hidden" name="cabinetName" id="cabinet_name" value="'+cabinetName+'">\n'+
                    '   <td>\n'+
                    '    <input type="text" readonly class="text easyui-validatebox" maxlength="40" style="width:175px;height:28px " value="'+cabinetName+'"\n'+
                    '   </td>\n'+
                    '   <td>\n'+
                    '      <img style="cursor: pointer;" onclick="cabinet_delete(this)" src="${app.imagePath}/delete.png" alt="">\n'+
                    '   </td>\n'+
                    '  </tr>\n';
            cabinet_table.find('tr').eq(trlength-1).before(html);
        }

    }

    function cabinet_delete(obj) {
        $.messager.confirm('提示信息', '确认解绑该柜子?', function(ok) {
            if(ok) {
                var standard= $(obj);
                standard.parent().parent().remove();
            }
        });
    }


</script>