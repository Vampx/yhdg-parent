<@app.html>
    <@app.head>
    <script type="text/javascript" src="${app.toolPath}/ueditor-1.4.3/ueditor.config.js" charset="utf-8" ></script>
    <script type="text/javascript" src="${app.toolPath}/ueditor-1.4.3/ueditor.all.min.js" charset="utf-8" ></script>
    <script type="text/javascript" src="${app.toolPath}/ueditor-1.4.3/lang/zh-cn/zh-cn.js" charset="utf-8" ></script>

    <script type="text/javascript">
        var ue;
        $().ready(function() {
            ue = UE.getEditor('editor', {
                toolbars:[
                    ['fullscreen', 'source', 'undo', 'redo', 'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
                        'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
                        'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
                        'directionalityltr', 'directionalityrtl', 'indent', '|',
                        'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
                        'link', 'anchor','simpleupload', 'insertimage', 'emotion', 'scrawl', '|',
                        'horizontal', 'date', 'time', 'spechars', 'preview', 'searchreplace', 'help']
                ],
                initialFrameHeight:500,
                autoHeightEnabled: false,
                scaleEnabled:true,
                autoFloatEnabled: true,
                maximumWords:50000,
                catchRemoteImageEnable: true
            });
            ue.ready(function() {

            });
        });

        function reload() {
            var tree = $('#rich_text_tree');
            tree.tree('reload');
        }

        function reloadMpIndex() {
            $('#alipayfw_id').combobox('clear');//清空选中项
            $('#phoneapp_id').combobox('clear');//清空选中项

            var weixinmpId = $('#weixinmp_id').combobox('getValue');
            document.location.href = '${contextPath}/security/basic/rich_text/index.htm?weixinmpId=' + weixinmpId;
        }

        function reloadFwIndex() {
            $('#weixinmp_id').combobox('clear');//清空选中项
            $('#phoneapp_id').combobox('clear');//清空选中项

            var alipayfwId = $('#alipayfw_id').combobox('getValue');
            document.location.href = '${contextPath}/security/basic/rich_text/index.htm?alipayfwId=' + alipayfwId;
        }

        function reloadAppIndex() {
            $('#weixinmp_id').combobox('clear'); //清空选中项
            $('#alipayfw_id').combobox('clear');//清空选中项

            var phoneappId = $('#phoneapp_id').combobox('getValue');
            document.location.href = '${contextPath}/security/basic/rich_text/index.htm?phoneappId=' + phoneappId;
        }

        function query() {
            var tree = $('#rich_text_tree');

        }

        function expandAll(){
            var node = $('#rich_text_tree').tree('getSelected');
            if (node) {
                $('#rich_text_tree').tree('expandAll', node.target);
            }
            else {
                $('#rich_text_tree').tree('expandAll');
            }
        }

        function onClick(node) {

            var tree = $('#rich_text_tree');
            var isLeaf = tree.tree('isLeaf',node.target);
            var weixinmpId = $('#weixinmp_id').combobox('getValue');
            var alipayfwId = $('#alipayfw_id').combobox('getValue');
            var phoneappId = $('#phoneapp_id').combobox('getValue');
            if(isLeaf) {
                ue.setEnabled();
                $.ajax({
                    async : true,
                    cache:false,
                    type: 'POST',
                    dataType : "html",
                    data:{id:node.id, weixinmpId:weixinmpId, alipayfwId:alipayfwId, phoneappId:phoneappId},
                    url: '${contextPath}/security/basic/rich_text/read.htm',
                    error: function () {
                        $.messager.alert('提示信息', '文件不存在', 'info');
                    },
                    success:function(data){
                        $('#daily_content').html(data);
                        var content = $('#daily_content').html();
                        ue.setContent(content);
                    }
                })
            }else{
                ue.setContent('');
                ue.setDisabled('fullscreen');
            }
        };

        function saveHtml() {
            var tree = $('#rich_text_tree');
            var selectedNode = tree.tree('getSelected');
            var weixinmpId = $('#weixinmp_id').combobox('getValue');
            var alipayfwId = $('#alipayfw_id').combobox('getValue');
            var phoneappId = $('#phoneapp_id').combobox('getValue');
            if (selectedNode) {
                var isLeaf = tree.tree('isLeaf', selectedNode.target);
                if (isLeaf) {
                    $.post('${contextPath}/security/basic/rich_text/save.htm', {
                        id : selectedNode.id,
                        weixinmpId:weixinmpId,
                        alipayfwId:alipayfwId,
                        phoneappId:phoneappId,
                        content: ue.getContent()
                    }, function(json) {
                        if (json.success) {
                            $.messager.alert('提示信息', '操作成功', 'info');
                        } else {
                            $.messager.alert('提示信息', '操作失败', 'info');
                        }
                    }, 'json');
                } else {
                    alert("请选择叶子节点");
                }
            } else {
                alert("请选择分类")
            }
        }

    </script>

    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>
            <div class="content">
                <div class="panel ztree_wrap">
                    <div class="settings_body">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>公众号：</td>
                                <td>
                                    <input name="weixinmpId" id="weixinmp_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                           data-options="url:'${contextPath}/security/basic/weixinmp/list.htm',
                                            method:'get',
                                            valueField:'id',
                                            textField:'appName',
                                            editable:false,
                                            multiple:false,
                                            panelHeight:'200',
                                            onSelect: function(node) {
                                                reloadMpIndex();
                                            }"
                                           <#if weixinmpId?? >value="${weixinmpId}"</#if>
                                    />
                                </td>
                            </tr>
                            <tr>
                            <tr>
                            <td>生活号：</td>
                            <td>
                                <input name="alipayfwId" id="alipayfw_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/basic/alipayfw/list.htm',
                                        method:'get',
                                        valueField:'id',
                                        textField:'appName',
                                        editable:false,
                                        multiple:false,
                                        panelHeight:'200',
                                        onSelect: function(node) {
                                            reloadFwIndex();
                                        }"
                                       <#if alipayfwId?? >value="${alipayfwId}"</#if>
                                />
                            </td>
                            </tr>
                            </tr>
                            <tr>
                                <td>app：</td>
                                <td>
                                    <input name="phoneappId" id="phoneapp_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                           data-options="url:'${contextPath}/security/basic/phoneapp/list.htm',
                                            method:'get',
                                            valueField:'id',
                                            textField:'appName',
                                            editable:false,
                                            multiple:false,
                                            panelHeight:'200',
                                            onSelect: function(node) {
                                                reloadAppIndex();
                                            }"
                                           <#if phoneappId?? >value="${phoneappId}"</#if>
                                    />
                                </td>
                            </tr>
                        </table>
                    </div><br>
                    <div class="">
                        <div class="ztree_head">
                            <h3>文本分类</h3>
                        </div><br>
                        <div class="ztree_body easyui-tree" id="rich_text_tree"
                             data-options="
                                 data: [{
                                    text: '所有分类',
                                    state: 'open',
                                    children: [
                                        <#list richTextEnum as e>
                                            {
                                                id: ${e.getValue()},
                                                text: '${e.getName()}',
                                                state: 'open'
                                            }
                                            <#if e_has_next>,</#if>
                                        </#list>
                                    ]
                                }],
                                onClick:function(node){
                                    onClick(node)
                                }
                            ">
                        </div>
                    </div>
                </div>
                <div class="panel grid_wrap" style="margin-left:250px; margin-top: -60px;">
                    <div class="settings_body">
                    <textarea id="editor" name="content" class="editor" style="width: 100%; bottom: 15px;">
                    </textarea>
                    </div>
                    <div class="settings_btn">
                        <@app.has_oper perm_code='basic.RichText:save'>
                            <button class="btn btn_red " onclick="saveHtml()">保 存</button>
                        </@app.has_oper>
                    </div>
                    <div style="display: none" id="daily_content"></div>
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>
