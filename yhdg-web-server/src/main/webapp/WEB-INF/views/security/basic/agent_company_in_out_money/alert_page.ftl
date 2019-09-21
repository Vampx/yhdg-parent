<#assign toolPath='${contextPath}/static/tool' >
<script type="text/javascript" src="${toolPath}/jquery-easyui-1.3.6/jquery.easyui.min.js"></script>
<div class="popup_body">
    <div class="panel search">
        <div class="float_right">
            <button class="btn btn_yellow" onclick="alert_page_query()">搜索</button>
        </div>
        <table>
            <tr>
                <td align="right">月份：</td>
                <td>
                    <input style="height: 30px" class="easyui-datebox" editable="false" id="datetime"
                           name="queryTime"/>
                </td>
            </tr>
        </table>
    </div>

    <div style="height:360px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="page_close_${pid}">关闭</button>
</div>
<script>
    $(function () {
        $('#page_table_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/agent_company_in_out_money/page.htm?agentCompanyId=${(agentCompanyId)!''}",
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
                    {
                        title: '运营公司名称',
                        align: 'center',
                        field: 'agentCompanyName',
                        width: 40
                    },
                    {
                        title: '业务类型',
                        align: 'center',
                        field: 'bizTypeName',
                        width: 40
                    },
                    {
                        title: '类型',
                        align: 'center',
                        field: 'typeName',
                        width: 40
                    },
                    {
                        title: '金额',
                        align: 'center',
                        field: 'money',
                        width: 40,
                        formatter:function(val) {
                            return val / 100;
                        }
                    },
                    {
                        title: '剩余金额',
                        align: 'center',
                        field: 'balance',
                        width: 40,
                        formatter:function(val) {
                            return val / 100;
                        }
                    },
                    {
                        title: '操作人',
                        align: 'center',
                        field: 'operator',
                        width: 40
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 30
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 40,
                        formatter: function(val, row) {
                            var html = '';
                            html += '<a href="javascript:view_record(ID)">查看</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ],
            onLoadSuccess: function () {
                $('#page_table_${pid}').datagrid('clearChecked');
                $('#page_table_${pid}').datagrid('clearSelections');
            }
        });

        var p = $('#datetime').datebox('panel'), //日期选择对象
                tds = false, //日期选择对象中月份
                aToday = p.find('a.datebox-current'),
                yearIpt = p.find('input.calendar-menu-year'),//年份输入框
                //显示月份层的触发控件
                span = p.find('div.calendar-title span');//1.3.x版本

        aToday.unbind('click').click(function () {
            var now = new Date();
            $('#datetime').datebox('hidePanel').datebox('setValue', now.getFullYear() + '-' + (now.getMonth() + 1));
        });
        $('#datetime').datebox({
            onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
                span.trigger('click'); //触发click事件弹出月份层
                if (p.find('div.calendar-menu').is(':hidden')) p.find('div.calendar-menu').show();
                if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                    tds = p.find('div.calendar-menu-month-inner td');
                    tds.click(function (e) {
                        e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
                        var year = /\d{4}/.exec(span.html())[0]//得到年份
                                , month = parseInt($(this).attr('abbr'), 10); //月份，这里不需要+1
                        if (month < 10) {
                            month = "0" + month;
                        }
                        $('#datetime').datebox('hidePanel')//隐藏日期对象
                                .datebox('setValue', year + '-' + month); //设置日期的值
                    });
                }, 0);
                yearIpt.unbind();//解绑年份输入框中任何事件
            },
            parser: function (s) {
                if (!s) return new Date();
                var arr = s.split('-');
                return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
            },
            formatter: function (d) {
                return d.getFullYear() + '-' + (d.getMonth() + 1);
            }
        });

    });

    function reload() {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid('reload');
    }

    function alert_page_query() {
        var datagrid = $('#page_table_${pid}');
        var queryTime = $('input[name="queryTime"]').val();
        var queryParams = {
            queryTime: queryTime
        };

        datagrid.datagrid('options').queryParams = queryParams;

        datagrid.datagrid('load');
    }

    $('#page_close_${pid}').click(function () {
        var win = $('#${pid}');
        win.window('close');
    })

    function view_record(id) {
        App.dialog.show({
            css: 'width:350px;height:400px;',
            title: '查看',
            href: "${contextPath}/security/basic/agent_company_in_out_money/view.htm?id=" + id,
            event: {
                onClose: function() {
                }
            }
        });
    }

</script>

