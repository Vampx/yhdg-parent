<@app.html>
    <@app.head>
    <script type="text/javascript" src="${app.toolPath}/ueditor-1.4.3/ueditor.config.js" charset="utf-8"></script>
    <script type="text/javascript" src="${app.toolPath}/ueditor-1.4.3/ueditor.all.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="${app.toolPath}/ueditor-1.4.3/lang/zh-cn/zh-cn.js" charset="utf-8"></script>

    <script type="text/javascript">
        var ue;
        $().ready(function () {
            ue = UE.getEditor('editor', {
                toolbars: [
                    ['fullscreen', 'source', 'undo', 'redo', 'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
                        'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
                        'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
                        'directionalityltr', 'directionalityrtl', 'indent', '|',
                        'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
                        'link', 'anchor', 'simpleupload', 'insertimage', 'emotion', 'scrawl', '|',
                        'horizontal', 'date', 'time', 'spechars', 'preview', 'searchreplace', 'help']
                ],
                initialFrameHeight: 500,
                autoHeightEnabled: false,
                scaleEnabled: true,
                autoFloatEnabled: true,
                maximumWords: 50000,
                catchRemoteImageEnable: true
            });
            ue.ready(function () {

            });
        });


        function customer_guide() {
            App.dialog.show({
                css: 'width:860px;height:512px;overflow:visible;',
                title: '分类',
                href: "${contextPath}/security/basic/customer_guide/view.htm",
                event: {
                    onClose: function () {
                        var tree = $('#customer_guide_tree');
                        tree.tree('reload')
                    }
                }
            });
        }

        function onClick(node) {

            var tree = $('#customer_guide_tree');

            var groupId = tree.tree('getSelected');
            if (groupId) {
                groupId = groupId.id || '';
            } else {
                groupId = '';
            }
            var isLeaf = tree.tree('isLeaf',node.target);
            if(isLeaf) {
                ue.setEnabled();
                $.ajax({
                    async : true,
                    cache:false,
                    type: 'POST',
                    dataType : "html",
                    data:{id:groupId},
                    url: '${contextPath}/security/basic/customer_guide/read.htm',
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
            var tree = $('#customer_guide_tree');
            var selectedNode = tree.tree('getSelected');
            if (selectedNode) {
                var isLeaf = tree.tree('isLeaf', selectedNode.target);
                if (isLeaf) {
                    $.post('${contextPath}/security/basic/customer_guide/save.htm', {
                        id: selectedNode.id,
                        content: ue.getContent()
                    }, function (json) {
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
                    <div class="ztree">
                        <div class="ztree_head">
                            <a class="a_red" href="javascript:customer_guide()">分类管理</a>
                            <h3>指南分类</h3>
                        </div>
                        <div class="ztree_body easyui-tree" id="customer_guide_tree"
                             url="${contextPath}/security/basic/customer_guide/tree.htm?dummy=${'所有'?url}"
                             lines="true"
                             data-options="onBeforeSelect: App.tree.toggleSelect,
                                onClick:function(node){
                                    onClick(node)
                                }">
                        </div>
                    </div>
                </div>
                <div class="panel grid_wrap" style="margin-left:250px; margin-top: -60px;">
                    <div class="settings_body">
                    <textarea id="editor" name="content" class="editor" style="width: 100%; bottom: 15px;">
                    </textarea>
                    </div>
                    <div class="settings_btn">
                        <@app.has_oper perm_code='basic.CustomerGuide:save'>
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
