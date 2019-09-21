<@app.html>
    <@app.head>
        <script type="text/javascript" src="${app.toolPath}/SWFUpload/swfupload.js"></script>
        <script type="text/javascript" src="${app.toolPath}/SWFUpload/swfupload.cookies.js"></script>
        <script type="text/javascript" src="${app.jsPath}/upload_material.js"></script>
        <script type="text/javascript" src="${app.jsPath}/replace_material.js"></script>
        <script>
            $(function() {
                $('#page_table').datagrid({
                    fit: true,
                    width: '100%',
                    striped: true,
                    pagination: true,
                    url: "${contextPath}/security/yms/material/page.htm",
                    fitColumns: true,
                    pageSize: 5,
                    pageList: [5, 10, 20],
                    idField: 'id',
                    singleSelect: true,
                    selectOnCheck: false,
                    checkOnSelect: false,
                    autoRowHeight: false,
                    rowStyler: function () { return 'height:7.0em;'; },
                    columns: [
                        [
                            {
                                title: '缩略图',
                                align: 'center',
                                field: 'coverPath',
                                width: 60,
                                formatter: function (val, row) {
                                    return "<img style='width:100px; height:70px;top: 0px;' onclick='preview(\""+ row.filePath +"\")' src='${controller.appConfig.staticUrl}"+ val +"' />"
                                }
                            },
                            {title: '名称', align: 'center', field: 'materialName', width: 70},
                            {
                                title: '状态',
                                align: 'center',
                                field: 'convertStatus',
                                width: 50,
                                formatter:function(val,row){
                                    <#list videoConvertStatusEnum as convertStatus>
                                        if(val == ${convertStatus.getValue()})
                                            return '${convertStatus.getName()}';
                                    </#list>
                                }
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
                                title: '转换进度',
                                align: 'center',
                                field: 'convertProgress',
                                width: 40
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
                            {title: '上传者', align: 'center', field: 'ownerName', width: 60},
                            {
                                title: '创建时间',
                                align: 'center',
                                field: 'createTime',
                                width: 60
                            },
                            {
                                title: '操作',
                                align: 'center',
                                field: 'id',
                                width: 90,
                                formatter: function(val, row)  {
                                    var html = '';

                                        html += '<a href="javascript:view(ID)">查看</a>';

                                        html += ' <a href="javascript:edit(ID)">修改</a>';

                                        html += ' <a href="javascript:remove(ID)">删除</a>';

                                        html += ' <a href="javascript:replace(ID)">替换</a>';

                                        html += ' <a href="javascript:download(ID)">下载</a>';

                                    return html.replace(/ID/g, row.id);
                                }
                            }
                        ]
                    ],
                    queryParams: {
                        materialName: $('#material_name').val()
                    },
                    onLoadSuccess:function() {
                        $('#page_table').datagrid('clearChecked');
                        $('#page_table').datagrid('clearSelections');
                    }
                });
            });



            function reload() {
                var datagrid = $('#page_table');
                datagrid.datagrid('reload');
            }

            function query() {
                var datagrid = $('#page_table');
                var materialName = $('#material_name').val();

                var agentId = $('#agent_id').combotree("getValue");

                datagrid.datagrid('options').queryParams = {
                    materialName: materialName,
                    agentId: agentId
                };
                datagrid.datagrid('load');
            }

            function add() {

                App.uploadMaterial({
                    close: function() {
                        query();
                    },
                    suffix: '<#list videoSuffixList as e>*.${e};</#list><#list imageSuffixList as e>*.${e}<#if e_has_next>;</#if></#list>',
                    description: "视频/图片"
                });
            }
            function edit(id) {
                App.dialog.show({
                    css: 'width:500px;height:200;',
                    title: '修改',
                    href: "${contextPath}/security/yms/material/edit.htm?id=" + id,
                    event: {
                        onClose: function() {
                            var datagrid = $('#page_table');
                            datagrid.datagrid('reload');
                        },
                        onLoad: function() {
                        }
                    }
                });
            }

            function view(id) {
                App.dialog.show({
                    css: 'width:600px;height:305px;',
                    title: '查看',
                    href: "${contextPath}/security/yms/material/view.htm?id=" + id
                });
            }

            function preview(path) {
                App.dialog.show({
                    options:'maximized:true',
                    title: '查看',
                    href: "${controller.appConfig.staticUrl}/security/material/preview.htm?path=" + path
                });
            }

            function remove(id) {
                $.messager.confirm('提示信息', '确认删除?', function(ok) {
                    if(ok) {
                        $.post('${contextPath}/security/yms/material/delete.htm', {
                            id: id
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('info', '操作成功', 'info');
                                reload();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                });
            }



            function switchAgent(agentId, descendant) {
                var groupTree = $('#group_id');
                groupTree.tree({
                    url: '${contextPath}/security/yms/material_group/tree.htm?agentId='+ agentId +'&dummy=${'所有'?url}',
                    onLoadSuccess: function() {
                        query();
                    }
                });
            }
            function download(id) {
                document.location.href = "${controller.appConfig.staticUrl}/security/material/download_material.htm?id=" + id
            }

            function replace(id) {
                App.replaceMaterial({
                    close: function() {
                        query();
                    },
                    suffix: '<#list videoSuffixList as e>*.${e};</#list><#list imageSuffixList as e>*.${e}<#if e_has_next>;</#if></#list>',
                    description: "视频/图片",
                    id: id
                });
            }

        </script>
    </@app.head>
<style>
    .content .datagrid-body .datagrid-cell{
        height: 70px;
        line-height: 70px;
    }
    .content .datagrid-body .datagrid-cell input{
        margin-top: 26px;
    }
    datagrid-cell datagrid-cell-c2-position
</style>
    <@app.body>
        <@app.container>
            <@app.banner/>
            <div class="main">
                <@app.menu/>
                    <div class="content">

                    <div class="panel search" >
                        <div class="float_right">
                            <button class="btn btn_yellow" onclick="query()">搜索</button>
                        </div>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td align="right">运营商：</td>
                                <td>
                                    <input name="agentId" id="agent_id" class="easyui-combotree" editable="false" style="width: 200px;height: 28px;"
                                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                                            method:'get',
                                                            valueField:'id',
                                                            textField:'text',
                                                            editable:false,
                                                            multiple:false,
                                                            panelHeight:'200',
                                                            onClick: function(node) {
                                                            }
                                                        "
                                    >
                                </td>
                                <td width="80" align="right">素材名称：</td>
                                <td><input type="text" id="material_name" class="text" /></td>
                            </tr>
                        </table>
                    </div>
                    <div class="panel grid_wrap" >
                        <div class="toolbar clearfix">
                            <div class="float_right">

                                    <button class="btn btn_green" onclick="add()">上传</button>

                            </div>
                            <h3>素材列表</h3>
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
