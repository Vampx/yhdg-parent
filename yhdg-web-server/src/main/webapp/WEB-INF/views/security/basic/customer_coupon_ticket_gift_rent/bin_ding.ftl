<script>
        (function () {
            var win = $('#${pid}'), windowData = win.data('windowData');
            var datagrid = $('#page_table_${pid}');
            datagrid.datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/customer_coupon_ticket_gift_rent/bin_ding_page.htm",
                fitColumns: true,
                pageSize: 10,
                pageList: [10, 50, 100],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                queryParams: {
                    ticketGiftId: '${ticketGiftId!}'
                },
                columns: [
                    [
                        {
                            title: '运营商名称',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '赠送类型',
                            align: 'center',
                            field: 'newType',
                            width: 60},
                        {
                            title: '客户手机',
                            align: 'center',
                            field: 'customerMobile',
                            width: 60},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60 ,
                            formatter: function (val, row) {
                             var html = '';
                             <@app.has_oper perm_code='basic.CustomerCouponTicketGift:remove'>
                                 html += '<a href="javascript:untYing(ID)">解绑</a>';
                             </@app.has_oper>
                             return html.replace(/ID/g, row.id);
                            }}

                    ]
                ],
                onLoadSuccess:function() {
                    datagrid.datagrid('clearChecked');
                    datagrid.datagrid('clearSelections');
                },onClickRow:  function(rowIndex, rowData) {

                }
            });

        })();
        function untYing(id) {
            $.messager.confirm('提示信息', '确认解绑?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/customer_coupon_ticket_gift_rent/untYing.htm?id=" + id, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息',json.message, 'info');
                            reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        }
        function reload() {
            var win = $("#page_table_${pid}");
            win.datagrid('reload');

        }
        $("#close_${pid}").click(function () {
            $('#${pid}').window('close');
        })
        function add_riders_${pid}() {
            App.dialog.show({
                css: 'width:240px;height:160px;',
                title: '添加骑手手机号',
                href: "${contextPath}/security/basic/customer_coupon_ticket_gift_rent/add_rider.htm?ticketGiftId=" + '${ticketGiftId!}',
                event: {
                    onClose: function () {
                        reload();
                    },
                    onLoad: function () {
                    }
                }
            });
        }
        function query_${pid}() {
            var win = $("#page_table_${pid}");
            win.datagrid('options').queryParams = {
                customerMobile: $("#mobile_${pid}").val(),
                ticketGiftId: '${ticketGiftId!}'
            };
            win.datagrid('load');
        }


    </script>
    <div class="popup_body">
        <div class="search">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <div class="float_right">
                <button class="btn btn_green" onclick="add_riders_${pid}()">添加骑手</button>
            </div>
            <div class="float_right">
                <button class="btn btn_yellow" onclick="query_${pid}()">搜索</button>
            </div>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td align="right">手机号：</td>
                    <td><input type="text" class="text" id="mobile_${pid}"/></td>
                </tr>
            </table>
        </div>
        <input type="hidden" name="id" value="${ticketGiftId}">
        <div style="width:800px; height:360px; padding-top: 6px;">
            <table id="page_table_${pid}">
            </table>
        </div>
    </div>
    <div class="popup_btn">
        <button class="btn btn_border" id="close_${pid}">关闭</button>
    </div>
