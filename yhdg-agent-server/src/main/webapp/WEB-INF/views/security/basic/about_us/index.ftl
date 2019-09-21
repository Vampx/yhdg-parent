<@app.html>
    <@app.head>
        <script type="text/javascript" src="${contextPath}/static/tool/editor/kindeditor.js"></script>
        <script type="text/javascript" src="${contextPath}/static/tool/editor/lang/zh_CN.js"></script>
        <script type="text/javascript">
            KindEditor.ready(function(K) {
                window.editor = K.create('#editor_id', {
                    height: '680px',
                    filterMode : false,
                    resizeType : 1,
                    allowPreviewEmoticons : false,
                    allowImageUpload : false,
                    items : [
                        'source', '|', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                        'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                        'insertunorderedlist', '|', 'emoticons', 'image', 'link']
                });

                $.post('${contextPath}/security/basic/about_us/load_html.htm', {
                    agentId: '${Session['SESSION_KEY_USER'].agentId}'
                }, function(html) {
                    window.editor.html(html);
                }, 'html');

            });

            function ok() {
                $.post('${contextPath}/security/basic/about_us/update.htm', {
                    agentId: '${Session['SESSION_KEY_USER'].agentId}',
                    content: window.editor.html()
                }, function(json) {
                    <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示消息', '操作成功', 'info');
                    } else {
                        $.messager.alert('提示消息', json.message, 'info');
                    }
                }, 'json');
            }
            function cancel() {
                $.messager.confirm('提示信息', '确认重置?', function(ok) {
                    if(ok) {
                        document.location.href = '${contextPath}/security/basic/about_us/index.htm'
                    }
                })
            }
        </script>
    </@app.head>

    <@app.body>
        <@app.container>
            <@app.banner/>

            <div class="main">
                <@app.menu/>

                <div class="content">
                    <div class="panel settings_wrap">
                        <div class="settings_body">
                            <textarea id="editor_id" name="content" style=" position: absolute; top:0; left: 0; width: 100%; bottom: 15px;">
                            </textarea>
                        </div>
                        <div class="settings_btn">
                            <button class="btn btn_red" onclick="ok()">保 存</button>
                            <button class="btn btn_border" onclick="cancel()">重 置</button>
                        </div>
                    </div>
                </div>
            </div>
        </@app.container>
    </@app.body>
</@app.html>

