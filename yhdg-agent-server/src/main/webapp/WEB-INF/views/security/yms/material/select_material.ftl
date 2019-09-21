
 <div class="popup_body">
 <style>
     .popup_body .datagrid-body .datagrid-cell{
         height: 70px;
         line-height: 70px;
     }
 </style>
        <div class="select_box" style="height:402px; float:right;">
            <div class="select_head">
                <h3>已选列表</h3><a class="a_red" href="javascript:deleteAll()">清空</a>
                <span class="msg">双击添加页面</span>
            </div>
            <div class="select_body" style="top:59px;">
                <ul id="selected_container_${pid}">
                <#list materialList as material>
                    <li material_id="${material.id}" material_name="${(material.routeName)!''}">
                        <span class="text">${(material.materialName)!""}</span>
                        <a class="delete" title="删除" href="javascript:void(0)" onclick="deleteRow(this)"><span class="icon"></span></a>
                    </li>
                </#list>
                </ul>
            </div>
        </div>
        <div class="ztree" style="height:402px;">
            <div class="ztree_body easyui-tree" id="group_tree_${pid}" url="${contextPath}/security/yms/material_group/tree.htm?dummy=${'所有素材'?url}" lines="true"
                 data-options="
                onClick: function(node) {
                    query_${pid}();
                }
            ">
            </div>
        </div>
        <div class="search" style="margin:0 210px; padding:2px 0 0 0; border-bottom:none;">
            <table cellspacing="0" cellpadding="0" border="0">
                <tr>
                    <td style="width:65px; text-align:right;">素材名称:&nbsp;</td>
                    <td style="width:140px;">
                        <input type="text" class="text" style="width:120px;" id="material_name_${pid}"/>
                    </td>
                    <td><a class="a_btn a_btn_y" href="javascript:query_${pid}()">搜 索</a></td>
                </tr>
            </table>
        </div>
        <div class="grid" style=" height:360px; margin:0 210px;">
            <table id="table_${pid}"></table>
        </div>
    </div>
    <div class="popup_btn">
        <button class="btn btn_red ok">确定</button>
        <button class="btn btn_border close">关闭</button>
</div>

<script>
    function gridRowStyler() { return 'height:6.0em;'; }
    var query_${pid};
    (function() {
        var pid = '${pid}', win = $('#' + pid), windowData = win.data('windowData');
        var selectedContainer = $('#selected_container_${pid}');
        var datagrid = $('#table_${pid}');
        var groupTree = $('#group_tree_${pid}');

        datagrid.datagrid({
            fit: true,
            width: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/yms/material/page.htm?convertStatus="+${status} + "&agentId=${(agentId)!0}",
            fitColumns: true,
            pageSize: 4,
            pageList: [4, 8, 20],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '缩略图',
                        align: 'center',
                        field: 'coverPath',
                        width: 70,
                        formatter: function (val, row) {
                            return "<img style='width:70px; height:70px;top: 0px;' onclick='preview(\""+ row.filePath +"\")' src='${controller.appConfig.staticUrl}"+ val +"' />"
                        }
                    },
                    {
                        title: '名称',
                        align: 'center',
                        field: 'materialName',
                        width: 80
                    },
                    {
                        title: '时长',
                        align: 'center',
                        field: 'duration',
                        width: 40,
                        formatter: function(val, row) {
                            return App.formatSecond(val);
                        }
                    },
                    {
                        title: '大小',
                        align: 'center',
                        field: 'size',
                        width: 40,
                        formatter: function (val, row) {
                            return App.fileSize(val);
                        }
                    },
                    {title: '上传者', align: 'center', field: 'ownerName', width: 40},
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 90
                    }
                ]
            ],
            onDblClickRow: function(index, row) {
                if(!windowData.limit || getValues().length < windowData.limit) {
                    add(row);
                } else {
                    if(windowData.limit) {
                        $.messager.alert('提示信息', '只允许选择' + windowData.limit + '条记录', 'info');
                    }
                }
            }
        });

        var line =
                '<li material_id="MATERIAL_ID" material_name="MATERIAL_NAME">' +
                '<span class="text">MATERIAL_NAME</span>' +
                '<p class="icon_btn">' +
                '<a class="delete" title="删除" href="javascript:void(0)" onclick="deleteRow(this)"><span class="icon"></span></a>' +
                '</p>' +
                '</li>';

        var add = function(material) {
                <#--if($('#selected_container_${pid} li[material_id="' + material.id +'"]').length > 0) {-->
                    <#--alert("素材已存在");-->
                    <#--return;-->
                <#--}-->
                var html = line.replace(/MATERIAL_ID/, material.id).replace(/MATERIAL_NAME/g, material.materialName);
                selectedContainer.append(html);
        };

        var getSelected = function() {
            var list = [];
            selectedContainer.find('li').each(function() {
                var me = $(this);
                list.push({
                    materialId: me.attr('material_id'),
                    materialName: me.attr('material_name')
                });
            });
            return list;
        };

        var setValues = function(list) {
            add(list);
        };

        var getValues = function() {
            return getSelected();
        };

        var query = function() {
            var groupId = groupTree.tree('getSelected');
            if(groupId) {
                groupId = groupId.id;
            }
            if(!groupId) {
                groupId = '';
            }

            datagrid.datagrid('options').queryParams = {
                materialName: $('#material_name_${pid}').val(),
                groupId: groupId
            };

            datagrid.datagrid('load');
        };
        function preview(path) {
            App.dialog.show({
                options:'maximized:true',
                title: '查看',
                href: "${controller.appConfig.staticUrl}/security/material/preview.htm?Path=" + path
            });
        }
        window.query_${pid} = query;

        win.find('a.search_btn').click(function() {
            query();
        });

        win.find('button.ok').click(function() {
            var values = getValues();
            if(values) {
                if(windowData.limit && windowData.limit != values.length) {
                    $.messager.alert('提示信息', '只允许选择' + windowData.limit + '条记录', 'info');
                    return;
                }

                if(windowData.ok(values)) {
                    win.window('close');
                }
            }
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

        if(windowData.values) {
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


