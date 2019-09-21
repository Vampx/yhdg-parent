<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">
        <#if editFlag ==1>
            <#--<button class="btn btn_green add">新建</button>-->
            <#--<button class="btn btn_green batch_add">批量新建</button>-->
        </#if>
        </div>
        <h3>箱体信息</h3>
    </div>
    <div class="grid" style="height:440px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>


<script>

    (function () {
        var win = $('#${pid}');
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            url: "${contextPath}/security/hdg/cabinet_box/page.htm?cabinetId=${cabinetId}",
            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {field: 'checkbox', checkbox: true},
                    {
                        title: '电池编号',
                        align: 'center',
                        field: 'batteryId',
                        width: 50
                    },
                    {
                        title: '电量',
                        align: 'center',
                        field: 'volume',
                        width: 25
                    },
                    {
                        title: '箱号',
                        align: 'center',
                        field: 'boxNum',
                        width: 25
                    },
                    {
                        title: '功率',
                        align: 'center',
                        field: 'power',
                        width: 30
                    },
                    {
                        title: '温度',
                        align: 'center',
                        field: 'boxTemp',
                        width: 30
                    },
                    {
                        title: '转速',
                        align: 'center',
                        field: 'fanSpeed',
                        width: 30
                    },
//                    {
//                        title: '类型',
//                        align: 'center',
//                        field: 'batteryTypeName',
//                        width: 30,
//                        formatter: function (val) {
//                            if (val == null) {
//                                return "不限";
//                            }else {
//                                return val;
//                            }
//                        }
//                    },
                    {
                        title: '激活/在线',
                        align: 'center',
                        field: 'isActive',
                        width: 40,
                        formatter: function (val, row) {
                            return showFlagName(val)+"/"+showFlagName(row.isOnline);
                        }
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'boxStatusName',
                        width: 30
                    },
                    {
                        title: '充电状态',
                        align: 'center',
                        field: 'chargeStatusName',
                        width: 40
                    },
                    {
                        title: '箱门状态',
                        align: 'center',
                        field: 'isOpen',
                        width: 40,
                        formatter: function (val, row) {
                            return showOpenName(val);
                        }
                    },
                <#if editFlag?? && (editFlag==1)>
                    {
                        title: '操作',
                        align: 'center',
                        field: 'id',
                        width: 80,
                        formatter: function (val, row) {
                            return '<a href="javascript:edit_box_record(\'SUBCABINET_ID\', \'BOX_NUM\')">修改</a>  <a href="javascript:delete_box_record(\'SUBCABINET_ID\', \'BOX_NUM\')">删除</a>  <a href="javascript:open_box(\'SUBCABINET_ID\', \'BOX_NUM\')">开箱</a>'.replace(/SUBCABINET_ID/g, row.cabinetId).replace(/BOX_NUM/g, row.boxNum);
                        }
                    }
                </#if>
                ]
            ]
        });

        win.find('button.add').click(function () {

            App.dialog.show({
                css: 'width:475px;height:210px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/hdg/cabinet_box/add.htm?cabinetId=${cabinetId}",
                event: {
                    onClose: function () {
                        list_box_reload();
                    }
                }
            });
        });

        win.find('button.batch_add').click(function () {
            App.dialog.show({
                css: 'width:475px;height:310px;overflow:visible;',
                title: '批量新建',
                href: "${contextPath}/security/hdg/cabinet_box/batch_add.htm?cabinetId=${cabinetId}",
                event: {
                    onClose: function () {
                        list_box_reload();
                    }
                }
            });
        });

        var ok = function () {
            return true;
        };

        win.data('ok', ok);
    })();

    function delete_box_record(cabinetId, boxNum) {
        $.messager.confirm('提示信息', '确认删除?', function (ok) {
            if (ok) {
                $.post('${contextPath}/security/hdg/cabinet_box/delete.htm',
                        {
                            cabinetId: cabinetId,
                            boxNum: boxNum
                        }, function (json) {
                            if (json.success) {
                                list_box_reload();
                                $.messager.alert('提示消息', '操作成功', 'info');
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
            }
        });
    }

    function open_box(cabinetId, boxNum) {
        $.messager.confirm('提示信息', '确认开箱吗?', function (ok) {
            if (ok) {
                $.post('${contextPath}/security/hdg/cabinet_box/open_box.htm',
                        {
                            cabinetId: cabinetId,
                            boxNum: boxNum
                        }, function (json) {
                            $.messager.alert('提示消息', json.message, 'info');
                        }, 'json');
            }
        });
    }

    function edit_box_record(cabinetId, boxNum) {
        App.dialog.show({
            css: 'width:475px;height:210px;overflow:visible;',
            title: '修改',
            href: "${contextPath}/security/hdg/cabinet_box/edit.htm?cabinetId=" + cabinetId + "&boxNum=" + boxNum,
            event: {
                onClose: function () {
                    list_box_reload();
                }
            }
        });
    }

    function view_box_record(cabinetId, boxNum) {
        App.dialog.show({
            css: 'width:566px;height:190px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/hdg/cabinet_box/view.htm?cabinetId=" + cabinetId + "&boxNum=" + boxNum,
            event: {
                onClose: function () {
                }
            }
        });
    }

    function list_box_reload() {
        var datagrid = $('#page_table_box_${pid}');
        datagrid.datagrid('reload');
    }

    var showFlagName = function (flag) {
        if (flag == 1) {
            return "是";
        }
        return "否";
    };
    var showOpenName = function (flag) {
        if (flag == 1) {
            return "开";
        }
        return "关";
    }

</script>