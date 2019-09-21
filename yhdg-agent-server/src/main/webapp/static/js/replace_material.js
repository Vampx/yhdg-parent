App.replaceMaterial = function(config) {

    var pid;
    var swfupload;
    var uploadCount = 0;
    var current_file;

    var getId = function(id) {
        return id + "_" + pid;
    }
    var getObj = function(id) {
        return $('#' + getId(id));
    }
    var installScroll = function() {
        var selected_file = getObj('selected_file');
        var count = 0, size = 0;
        selected_file.find('li').each(function() {
            count++;
            size += parseInt($(this).attr('size'));
        });
        getObj('file_count').html(count);
        getObj('file_size').html(App.fileSize(size));
    }
    var removeFile = function(obj) {
        var line = $(obj).closest('li');
        var fileId = line.attr('file_id');
        swfupload.cancelUpload(fileId, false);
        line.remove();
        installScroll();
    }
    var start = function() {
        var selectedFile = getObj('selected_file');

        if(selectedFile.find('li').length == 0) {
            alert('请先选择文件');
        } else {
            selectedFile.find('li a').remove();
            swfupload.startUpload();
        }
    }
    var onDialogStart = function() {
        swfupload.stopUpload();
        var selected_file = getObj('selected_file');
        selected_file.find('li').each(function() {
            swfupload.cancelUpload($(this).attr('file_id'), false);
        });
        selected_file.empty();
    }
    var onFileQueued = function(file) {
        var selected_file = getObj('selected_file');
        var html = [
            '<li file_id="FILE_ID" file_name="FILE_NAME" size="SIZE" size_desc="SIZE_DESC" suffix="SUFFIX">',
            '<p class="name">FILE_NAME</p>',
            '<p class="progress">',
            '<span class="progress_box">',
            '<span class="progress_bar" style="width:0;"></span>',
            '</span>',
            '<span class="progress_text"></span>',
            '<a href="javascript:void(0)">删除</a>',
            '</p>',
            '<p class="tips">',
            '<span class="complete_size">状态：等待上传</span>',
            '<span class="speed">速度：0kb/s</span>',
            '<span>文件大小：SIZE_DESC</span>',
            '</p>',
            '</li>'];

        html = html.join('');
        selected_file.append(html.replace(/FILE_NAME/g, file.name).replace(/FILE_ID/g, file.id).replace(/SIZE_DESC/g, App.fileSize(file.size)).replace(/SIZE/g, file.size).replace(/SUFFIX/g, file.type.substring(1)));
    }
    var onDialogComplete = function() {
        installScroll();
    }
    var onUploadStart = function(file) {
        var obj = getObj('selected_file').find('li[file_id=' + file.id + ']');
        obj.find('.uploaded_size').html('正在上传');

        current_file = { id: file.id};
        current_file.lastTime = new Date().getTime();
        current_file.lastComplete = 0;

        var param = {
            materialName: encodeURIComponent(App.trimFileName(obj.attr('file_name').replace(/\s/g, '_'), 40)),
            ownerId: App.sessionUser.id,
            ownerName: encodeURIComponent(App.sessionUser.realName),
            agentId: getObj('agent_id').val(),
            id: config.id
        }
        swfupload.setPostParams(param);
        swfupload.refreshCookies();
    }
    var onUploadProgress = function(file, bytesComplete, totalBytes) {
        var obj = getObj('selected_file').find('li[file_id=' + file.id + ']');
        var progress = Math.ceil((bytesComplete / totalBytes) * 100);
        obj.find('.progress_bar').css('width', progress + '%');
        obj.find('.complete_size').html(App.format('已上传: {0}', App.fileSize(bytesComplete)));

        var now = new Date().getTime();
        if(now - current_file.lastTime  > 1000) {
            var time = Math.max(now - current_file.lastTime, 1) / 1000;
            var bytes = bytesComplete - current_file.lastComplete;
            var speed = parseInt(bytes / time);
            speed = App.fileSize(speed);
            obj.find('.speed').html(App.format('速度：{0}/秒', speed));

            current_file.lastTime = now;
            current_file.lastComplete = bytesComplete;
        }

        if(bytesComplete == totalBytes) {
            obj.find('.tips').html('正在处理...');
        }
    }
    var onUploadSuccess = function(file, response) {
        var obj = getObj('selected_file').find('li[file_id=' + file.id + ']');
        obj.find('.progress_bar').css('width', '100%')

        var json = $.evalJSON(response);
        if(json.success) {
            obj.find('.tips').html('上传成功');
            obj.find('.name').prepend('<span class="tick_icon"></span>');
        } else {
            obj.find('.tips').html(json.message);
            obj.find('.name').prepend('<span class="wrong_icon"></span>');
        }

        uploadCount++;
        if(swfupload.getStats().files_queued > 0) {
            swfupload.startUpload();
        } else {
            uploadCount = 0;
            $.messager.alert('提示信息', '上传完成', 'info');
        }
    }
    var onUploadError = function(file, errorCode, message) {
        var obj = getObj('selected_file').find('li[file_id=' + file.id + ']');
        obj.find('.tips').html('上传失败');
        obj.find('.name').prepend('<span class="wrong_icon"></span>');
        alert(App.format("File: {0} ErrorCode: {1} Message: {2}", file.name, errorCode, message));
    }
    var onFileError = function(file, errorCode) {
        var message = '';
        switch(errorCode){
            case -100 : message = '替换广告只能选择一个图片或视频，请重新选择！';
                break;
            case -110 : message = '文件太大，不能选择！';
                break;
            case -120 : message = '该文件大小为0，不能选择！';
                break;
            case -130 : message = '该文件类型不可以上传！';
                break;
        }
        alert(App.format(" ErrorCode: {0} Message: {1}", errorCode, message));
    }
    function setupSwfUpload() {
        getObj('group_path').html(config.groupPath);

        swfupload = new SWFUpload( {
            button_placeholder_id : getId('select_file'),
            upload_url: App.staticUrl + "/security/material/replace.htm",
            flash_url: App.staticUrl + "/static/tool/SWFUpload/swfupload.swf",
            file_post_name: "file",
            use_query_string: true,
            requeue_on_error: false,
            http_success: [200],
            assume_success_timeout: 0,
            file_types: config.suffix,
            file_types_description: config.description,
            file_size_limit: 0,
            file_upload_limit: 1,
            debug: false,
            button_cursor : SWFUpload.CURSOR.HAND,
            button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
            custom_settings : {
                scope : this
            },
            button_width : 90,
            button_height : 30,

            button_text: '<a href="javascript:void(0)" style="color:#ffffff;">选择文件</a>',
            button_image_url: App.contextPath + '/static/images/upload_btn_bg.jpg',
            button_text_style : 'text-align:center; color:#ffffff; border:1px solid #008af5; border-radius: 2px; display:block; line-height:30px; height:30px;',
            button_text_top_padding: 6,
            button_text_left_padding: 18,

            file_queued_handler : onFileQueued,
            swfupload_loaded_handler : function(){},// 当Flash控件成功加载后触发的事件处理函数
            file_dialog_start_handler : onDialogStart,// 当文件选取对话框弹出前出发的事件处理函数
            file_dialog_complete_handler : onDialogComplete,//当文件选取对话框关闭后触发的事件处理
            upload_start_handler : onUploadStart,// 开始上传文件前触发的事件处理函数
            upload_success_handler : onUploadSuccess,// 文件上传成功后触发的事件处理函数
            swfupload_loaded_handler : function(){},// 当Flash控件成功加载后触发的事件处理函数
            upload_progress_handler : onUploadProgress,
            upload_complete_handler : function(){},
            upload_error_handler : onUploadError,
            file_queue_error_handler : onFileError
        } );

        getObj('selected_file').click(function(evt) {
            var target = $(evt.target);
            if(target) {
                if(target.prop('tagName').toLowerCase() == 'a') {
                    removeFile(evt.target);
                }
            }
        });
        getObj('start_upload').click(function() {
            // if(!config.agentId) {
            //     $.messager.alert('提示消息', '请选择运营商', 'info');
            //     return;
            // }

            start();
        });
    }

    App.dialog.show({
        css: 'width:640px;height:500px;padding:5px;',
        title: '替换广告',
        href: App.contextPath + "/security/yms/material/replace_material.htm?id=" + config.id,
        event: {
            onLoad: function(id) {
                pid = $(this).attr('id');
                setupSwfUpload();
            },
            onClose: function() {
                swfupload.destroy();
                if(config.close) {
                    config.close();
                }
            }
        }
    });
}

