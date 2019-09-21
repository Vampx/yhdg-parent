<div class="popup_body">
    <div class="select_routes">
        <div class="select_box" style="width: 200px; height:402px; float:right;">
            <div class="select_head">
                <h3>已选列表</h3><a class="a_red" href="javascript:deleteAll()">清空</a>
                <span class="msg">双击添加页面</span>
            </div>
            <div class="select_body" style="top:59px; overflow: hidden;">
                <ul id="selected_container_${pid}">
                </ul>
            </div>
        </div>
    </div>
    <div class="search" style="margin:0 0; padding:2px 0 0 0; border-bottom:none;">
        <table cellspacing="0" cellpadding="0" border="0">
            <tr>
                <td align="right">昵称：</td>
                <td><input type="text" style="width: 100px" class="text" id="fullname_${pid}"/>&nbsp;&nbsp;</td>
                <td align="right">手机号：</td>
                <td><input type="text" style="width: 100px" class="text" id="mobile_${pid}"/>&nbsp;&nbsp;</td>
                <#--<td><a class="btn_yellow" href="javascript:query_${pid}()">搜 索</a></td>-->
                <td>
                    <a href="javascript:query_${pid}()">
                        <button class="btn btn_yellow">搜索</button>
                    </a>
                </td>
            <#--<td><button class="btn btn_yellow" id="query_${pid}">搜索</button></td>-->
            </tr>

        </table>
    </div>
    <div class="select_routes">
        <div class="select_body" style="margin-right: 20px; top:29px; overflow: hidden;">
            <div class="grid" style="width: 420px; height:360px; margin-right: 20px;">
                <table id="table_${pid}"></table>
            </div>
        </div>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>

<script>
    var query_${pid};

    (function () {
        var pid = '${pid}', win = $('#' + pid), windowData = win.data('windowData');
        var selectedContainer = $('#selected_container_${pid}');
        var datagrid = $('#table_${pid}');

        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/customer/page.htm",
            fitColumns: true,
            pageSize: 10,
            pageList: [10, 50, 100],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: 'checkbox', checkbox: true
                    },
                    {
                        title: '客户昵称',
                        align: 'center',
                        field: 'fullname',
                        width: 60
                    },
                    {title: '手机号', align: 'center', field: 'mobile', width: 60},
                ]
            ],
            onLoadSuccess: function () {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            },
            onDblClickRow: function (index, row) {
                if (!windowData.limit || getValues().length < windowData.limit) {
                    add(row);
                } else {
                    if (windowData.limit) {
                        $.messager.alert('提示信息', '只允许选择' + windowData.limit + '条记录', 'info');
                    }
                }
            },
            onCheckAll: function () {
                var rows = datagrid.datagrid("getRows");
                for (var i = 0; i < rows.length; i++) {
                    if (!windowData.limit || getValues().length < windowData.limit) {
                        add(rows[i]);
                    } else {
                        if (windowData.limit) {
                            $.messager.alert('提示信息', '只允许选择' + windowData.limit + '条记录', 'info');
                        }
                    }
                }
            },
            onCheck: function (index, row) {
                if (!windowData.limit || getValues().length < windowData.limit) {
                    add(row);
                } else {
                    if (windowData.limit) {
                        $.messager.alert('提示信息', '只允许选择' + windowData.limit + '条记录', 'info');
                    }
                }
            }
        });

        var line =
                '<li customer_id="CUSTOMER_ID" fullname="NICKNAME" mobile="MOBILE">' +
                '<span class="text"><a>NICKNAME</a>' +
                '<a href="#" style="float:right;color: red;" onclick="deleteRow(this)">删除</a></span>' +
                '</li>';

        var add = function (customer) {
            if ($('#selected_container_${pid} li[customer_id="' + customer.id + '"]').length > 0) {
                alert("客户已存在");
                return;
            }
            var fullname = customer.fullname;
            if (!customer.fullname) {
                fullname = '未命名';
            }
            var html = line.replace(/CUSTOMER_ID/, customer.id).replace(/NICKNAME/g, fullname).replace(/MOBILE/g, customer.mobile);
            selectedContainer.append(html);
        };

        var getSelected = function () {
            var list = [];
            selectedContainer.find('li').each(function () {
                var me = $(this);
                list.push({
                    customerId: me.attr('customer_id'),
                    fullname: me.attr('fullname'),
                    mobile: me.attr('mobile')
                });
            });
            return list;
        };

        var setValues = function (list) {
            add(list);
        };

        var getValues = function () {
            return getSelected();
        };

        var query = function () {
            datagrid.datagrid('options').queryParams = {
                fullname: $('#fullname_${pid}').val(),
                mobile: $('#mobile_${pid}').val()
            };
            datagrid.datagrid('load');
        };
        window.query_${pid} = query;

        win.find('a.search_btn').click(function () {
            query();
        });

        win.find('button.ok').click(function () {
            var values = getValues();
            if (values) {
                if (windowData.limit && windowData.limit != values.length) {
                    $.messager.alert('提示信息', '只允许选择' + windowData.limit + '条记录', 'info');
                    return;
                }

                if (windowData.ok(values)) {
                    win.window('close');
                }
            }
        });
        win.find('button.close').click(function () {
            win.window('close');
        });

        if (windowData.values) {
            setValues(windowData.values);
        }

    })();

    function deleteRow(obj) {
        $(obj).closest('li').remove();
    }

    function deleteAll() {
        $('#selected_container_${pid}').html("");
    }

</script>