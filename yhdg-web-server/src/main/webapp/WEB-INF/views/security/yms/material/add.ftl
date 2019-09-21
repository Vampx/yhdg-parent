<div class="popup_body">
    <div class="upload_toolbar" style="width: 650px">
        <p class="button"><a href="javascript:void(0)" id="select_file_${pid}">选择文件</a></p>
        <p class="path">运营商：
            <input id="agent_id_${pid}" name="agentId" class="easyui-combotree" required="true" editable="false" style="width: 120px; height: 28px;"
                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto'
                            "
            >
        </p>
        <p class="tips">已选择 <span id="file_count_${pid}">0</span> 条记录，共 <span id="file_size_${pid}">0 MB</span> </p>
    </div>
    <div style="width: 640px" class="upload_list scroll" id="scroll_${pid}">
        <div class="scroll_con">
            <ul id="selected_file_${pid}">
            </ul>
        </div>
    </div>
</div>
<div class="popup_btn" style="text-align: right;">
    <button class="btn btn_red ok" id="start_upload_${pid}">开始上传</button>
    <button class="btn btn_border close" onclick="$('#${pid}').window('close')">关 闭</button>
</div>
