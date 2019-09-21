<@app.html>
    <@app.head>
    </@app.head>

    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>
            <div class="content">
                <div class="panel settings_wrap">
                    <form method="post">
                        <div class="settings_body">
                            <div class="panel search">
                                <div class="float_right">
                                </div>
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>商户：</td>
                                        <td>
                                            <input id="partner_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                                   data-options="url:'${contextPath}/security/basic/partner/list.htm?dummy=${'无'?url}',
                                                    method:'get',
                                                    valueField:'id',
                                                    textField:'partnerName',
                                                    editable:false,
                                                    multiple:false,
                                                    panelHeight:'200',
                                                    onSelect: function(node) {
                                                         reloadIndex();
                                                    }"
                                                   <#if partnerId?? >value="${partnerId}"</#if>
                                            />
                                        </td>
                                </table>
                            </div>
                            <fieldset>
                                <legend>用户充值优惠</legend>
                                <div class="ui_table">
                                    <table id="page_table" cellpadding="0" cellspacing="0">
                                        <#list 1..26 as e>
                                            <#assign money=0.00>
                                            <#assign gift=0.00>
                                            <#if customerGiftList?? && e_index lt customerGiftList?size>
                                                <#assign money=customerGiftList[e_index].money>
                                                <#assign gift=customerGiftList[e_index].getGift()>
                                            </#if>
                                            <#if e % 2 == 1>
                                            <tr>
                                            </#if>
                                                <td><input type="hidden" name="partnerId" value="${partnerId!''}"></td>
                                                <td width="100" align="right">用户充值：</td>
                                                <td><input type="text" id="money_${e}" class="text easyui-numberspinner"
                                                           style="width:110px; height: 28px;" name="money"
                                                           data-options="min:0.00, precision:2" value="${(money/100)?string('0.00')}"
                                                           required="required"/>元
                                                </td>
                                              <#--  <td width="100" align="right">赠送：</td>
                                                <td><input type="text" class="text easyui-numberspinner"
                                                           style="width:110px; height: 28px;" id="gift_${e}" name="gift"
                                                           data-options="min:0.00, precision:2"
                                                           value="${(gift/100)?string('0.00')}" required="required"/>元
                                                    <a id="btn" href="javascript:void(0)" onclick="clean(${e})" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"></a>
                                                </td>-->
                                            <#if e % 2 != 1>
                                            </tr>
                                            </#if>
                                        </#list>
                                    </table>
                                </div>
                            </fieldset>
                        </div>
                    </form>
                    <div class="settings_btn">
                        <@app.has_oper perm_code='basic.CustomerDepositGift:save'>
                            <button class="btn btn_red ok">保 存</button>
                        </@app.has_oper>
                        <@app.has_oper perm_code='basic.CustomerDepositGift:reset'>
                            <button class="btn btn_border close">重 置</button>
                        </@app.has_oper>

                    </div>
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>
<script>
    (function () {
        var form = $('form');

        $('button.ok').click(function () {
            var partnerId = $('#partner_id').combobox('getValue');
            if(partnerId == null || partnerId =='') {
                $.messager.alert('提示信息', '请选择商户', 'info');
                return;
            }else{
                form.form('submit', {
                    url: '${contextPath}/security/basic/customer_deposit_gift/update.htm',
                    success: function (text) {
                        var json = $.evalJSON(text);
                    <@app.json_jump/>
                        if (json.success) {
                            $.messager.alert('提示信息', '操作成功', 'info');
                            document.location.href = '${contextPath}/security/basic/customer_deposit_gift/index.htm?partnerId=${partnerId!''}'
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                });
            }
        });
        $('button.close').click(function () {
            document.location.href = '${contextPath}/security/basic/customer_deposit_gift/index.htm'
        });

    })();

    function clean(id) {
        $('#money_'+id).numberspinner('setValue', 0.00);
        $('#gift_'+id).numberspinner('setValue', 0.00);
    }

    function reloadIndex() {
        var partnerId = $('#partner_id').combobox('getValue');
        document.location.href = '${contextPath}/security/basic/customer_deposit_gift/index.htm?partnerId=' + partnerId;
    }
</script>

