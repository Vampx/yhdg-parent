<div class="popup_body" style=" width: 770px; height: 465px;">
    <div class="step_con">
        <div class="ui_table" style="padding: 20px 0 0 10px;">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="120" align="right">选择运营商：</td>
                    <td align="right">
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                               editable="false" style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                       swich_agent_${pid}();
                                    }"
                        >
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">选择门店：</td>
                    <td>
                        <select name="shopId" id="shop_id_${pid}" class="easyui-combotree" required="true" editable="false"
                                style="width: 184px; height: 28px;"
                                url="${contextPath}/security/hdg/shop/tree.htm"
                                data-options="url:'${contextPath}/security/hdg/shop/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                    }"
                        >
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="step_con" style="display: none;">
        <div class="select_box" style="height:480px; float:right;">
            <div class="select_head">
                <a class="a_red" href="javascript:deleteAll()">清空</a>
                <h3>已选列表(双击添加页面)</h3>
            </div>
            <div class="select_body" style="top:29px;">
                <ul id="selected_container_${pid}">
                </ul>
            </div>
        </div>
        <div class="toolbar toolbar_border clearfix" style="height: 34px; margin: -10px 210px 10px 0;">
            <ul class="nav">
                <li class="current" id="choose">列表选择</li>
            </ul>
        </div>
        <div class="tab-item" style="width: 560px; height: 402px; margin:0 210px 0 0;">
            <div class="search" style="border: none; ">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tbody><tr>
                        <td align="center">车辆编号：</td>
                        <td><input type="text" id="id_${pid}" class="text">
                        <input type="hidden" id="count">
                        </td>
                        <td align="center">车型名称：</td>
                        <td><input type="text" id="model_name_${pid}" class="text"></td>
                        <td align="right" width="80">
                            <button class="btn btn_yellow" onclick="query()">搜索</button>
                        </td>
                    </tr>
                    </tbody></table>
            </div>
            <div class="grid" style=" height:360px; border: 1px solid #ddd; ">
                <table id="page_table_${pid}" cellpadding="0" cellspacing="0" border="0">
                </table>
            </div>
        </div>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_step btn_red">下一步</button>
    <button class="btn btn_save btn_red ok" style="display: none;">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>

<script>
    function swich_agent_${pid}() {
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var shopCombotree = $('#shop_id_${pid}');
        shopCombotree.combotree({
            url: "${contextPath}/security/hdg/shop/tree.htm?agentId=" + agentId + ""
        });
        shopCombotree.combotree('reload');
    };

    var selectedContainer = $('#selected_container_${pid}');
    var getValues;

    function deleteRow(obj) {
        $(obj).closest('li').remove();
    }

    function deleteAll() {
        $('#selected_container_${pid}').html("");
    }

    function query() {
        var datagrid = $('#page_table_${pid}');
        var id = $('#id_${pid}').val();
        var modelName = $('#model_name_${pid}').val();
        datagrid.datagrid('options').queryParams = {
            id: id,
            modelName: modelName
        };
        datagrid.datagrid('load');
    }

    $(".btn_step").click(function(){
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var shopId = $('#shop_id_${pid}').combotree('getValue');
        if (agentId == "") {
            $.messager.alert('提示信息', "请选择运营商", 'info');
            return;
        }
        if (shopId == "") {
            $.messager.alert('提示信息', "请选择门店", 'info');
            return;
        }
        $(this).hide();
        $(".btn_save").show();
        $(".step_con").eq(1).show().siblings(".step_con").hide();

        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/shop_vehicle/unbound_page.htm",
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
                    {title: '车辆编号', align: 'center', field: 'id', width: 60},
                    {title: '车型编号', align: 'center', field: 'modelId', width: 60},
                    {title: '车型名称', align: 'center', field: 'modelName', width: 60}
                ]
            ],
            onLoadSuccess: function () {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            },
            onDblClickRow: function(index, row) {
                add(row);
            },
            onCheckAll: function(){
                var rows = datagrid.datagrid("getRows");
                for (var i=0; i<rows.length; i++){
                    add(rows[i]);
                }
            },
            onCheck: function(index, row) {
                add(row);
            }
        });

        var line =
                '<li vehicle_id="VEHICLE_ID" moedel_name="MODEL_NAME">' +
                '<span class="text">VEHICLE_ID[MODEL_NAME]</span>' +
                '<p class="icon_btn"><a class="delete" title="删除" href="javascript:void(0)" onclick="deleteRow(this)"><span class="icon"></span></a>' +
                '</p></li>';

        var add = function(vehicle) {
            if($('#selected_container_${pid} li[vehicle_id="' + vehicle.id +'"]').length > 0) {
                $.messager.alert('提示信息', '车辆已存在', 'info');
                return;
            }
            if(vehicle.modelName!=null) {
                var html = line.replace(/VEHICLE_ID/g, vehicle.id).replace(/MODEL_NAME/g, vehicle.modelName);
            }else{
                var html = line.replace(/VEHICLE_ID/g, vehicle.id).replace(/MODEL_NAME/g, "");
            }
            selectedContainer.append(html);
        };

        var getSelected = function() {
            var list = [];
            selectedContainer.find('li').each(function() {
                var me = $(this);
                list.push({
                    vehicleId: me.attr('vehicle_id')
                });
            });
            return list;
        };

        getValues = function() {
            return getSelected();
        };
    });

    (function () {
        var win = $('#${pid}'), form = win.find('form');

        win.find('button.ok').click(function() {
            var list = [];
            selectedContainer.find('li').each(function() {
                var me = $(this);
                list.push(me.attr('vehicle_id'));
            });
            if(list.length == 0) {
                $.messager.alert('提示信息', '请选择绑定车辆', 'info');
                return;
            }
            var agentId = $('#agent_id_${pid}').combotree('getValue');
            var shopId = $('#shop_id_${pid}').combotree('getValue');
            if(list) {
                $.post('${contextPath}/security/hdg/shop_vehicle/update_agent_shop.htm', {
                    id: list,
                    agentId: agentId,
                    shopId: shopId
                }, function (json) {
                    if (json.success) {
                        $.messager.alert('提示信息', json.message, 'info');
                        win.window('close');
                        reload();
                    } else {
                        $.messager.alert('提示消息', json.message, 'info');
                    }
                }, 'json');
            }
        });

        win.find('button.close').click(function () {
            win.window('close');
        });
    })();
</script>


