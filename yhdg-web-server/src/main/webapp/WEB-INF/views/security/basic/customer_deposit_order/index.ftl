<@app.html>
    <@app.head>
    <script>
        $(function() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/customer_deposit_order/page.htm",
                fitColumns: true,
                pageSize: 50,
                pageList: [50, 100],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {title: '商户', align: 'center', field: 'partnerName', width: 40},
                        {
                            title: '手机号码',
                            align: 'center',
                            field: 'customerMobile',
                            width: 40,
                            formatter: function(val) {
                                return val;
                            }
                        },
                        {
                            title: '姓名',
                            align: 'center',
                            field: 'customerFullname',
                            width: 40
                        },
                        {
                            title: '充值金额(元)',
                            align: 'center',
                            field: 'money',
                            width: 40,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '优惠(元)',
                            align: 'center',
                            field: 'gift',
                            width: 40,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '实际到账金额(元)',
                            align: 'center',
                            field: 'realMoney',
                            width: 40,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '充值时间',
                            align: 'center',
                            field: 'createTime',
                            width: 50
                        },
                        {
                            title: '渠道',
                            align: 'center',
                            field: 'payTypeName',
                            width: 40
                        },
                        {
                            title: '充值状态',
                            align: 'center',
                            field: 'statusName',
                            width: 40
                        },
                        {
                            title: '备注',
                            align: 'center',
                            field: 'memo',
                            width: 40
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                  <@app.has_oper perm_code='basic.CustomerDepositOrder:view'>
                                   html += '<a href="javascript:view(ID)">查看</a>';
                                  </@app.has_oper>
                                <#--if (row.status == 2) {-->
                                    <#--&lt;#&ndash;<@app.has_oper perm_code='1_1_3_4'>&ndash;&gt;-->
                                        <#--html += '  <a href="javascript:refund(ID)">退款</a>';-->
                                    <#--&lt;#&ndash;</@app.has_oper>&ndash;&gt;-->
                                <#--}-->
                                return html.replace(/ID/g, "'" + row.id + "'");
                            }
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                },onDblClickRow: function(rowIndex, rowData) {
                    viewCustomer(rowData.customerId);
                },
                queryParams: {
                    appId: 0
                }
            });
        });

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var partnerId = $('#partner_id').combobox('getValue');
            var customerFullname = $('#customer_fullname').val();
            var customerMobile = $('#customer_mobile').val();
            var status = $('#status').val();
            var payType = $('#payType').val();
            var queryParams = {
                partnerId: partnerId,
                status: status,
                customerMobile: customerMobile,
                payType: payType,
                customerFullname: customerFullname
            };

            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function add(){
            App.dialog.show({
                css:'width:786px;height:515px;overflow:visible;',
                title:'新建',
                href:"${contextPath}/security/basic/customer_deposit_order/add.htm",
                event:{
                    onClose:function(){
                        var datagrid=$('#page_table');
                        datagrid.datagrid('reload');
                    },
                    onLoad:function(){
                    }
                }
            });
        }

        function refund(id) {
            App.dialog.show({
                css: 'width:425px;height:315px;',
                title: '退款',
                href: "${contextPath}/security/basic/customer_deposit_order/refund.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function viewCustomer(id) {
            App.dialog.show({
                css: 'width:752px;height:520px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/customer/view.htm?id=" + id
            });
        }

        function view_basic(id) {
            App.dialog.show({
                css: 'width:900px;height:470px;overflow:visible;',
                title: '历史',
                href: "${contextPath}/security/basic/customer_deposit_order/view_basic.htm?id=" + id
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:515px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/customer_deposit_order/view.htm?id=" + id

            });
        }

        <#--function edit(id) {-->
            <#--App.dialog.show({-->
                <#--css: 'width:330px;height:150px;overflow:visible;',-->
                <#--title: '修改金额',-->
                <#--href: "${contextPath}/security/basic/customer_deposit_order/edit.htm?id=" + id,-->
                <#--event: {-->
                    <#--onClose: function() {-->
                        <#--reload();-->
                    <#--},-->
                    <#--onLoad: function() {-->
                    <#--}-->
                <#--}-->
            <#--});-->
        <#--}-->

    </script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>
            <div class="content">
                <div class="panel search">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td>商户：</td>
                            <td>
                                <input name="partnerId" id="partner_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/basic/partner/list.htm?dummy=${'无'?url}',
                                        method:'get',
                                        valueField:'id',
                                        textField:'partnerName',
                                        editable:false,
                                        multiple:false,
                                        panelHeight:'200',
                                        onSelect: function(node) {
                                            query();
                                        }"
                                />
                            </td>
                            <td align="right">&nbsp;&nbsp;手机号：</td>
                            <td><input type="text" class="text" id="customer_mobile"/></td>
                            <td align="right" width="60">姓名：</td>
                            <td><input type="text" class="text" id="customer_fullname"/></td>

                            <td align="right" width="80">充值状态：</td>
                            <td>
                                <select style="width:90px;" id="status">
                                    <option value="">所有</option>
                                    <#list statusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="80">渠道：</td>
                            <td>
                                <select style="width:90px;" id="payType">
                                    <option value="">所有</option>
                                    <#list payTypeEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">

                        </div>
                        <h3>充值列表</h3>
                    </div>
                    <div class="grid">
                        <table id="page_table"></table>
                    </div>
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>

