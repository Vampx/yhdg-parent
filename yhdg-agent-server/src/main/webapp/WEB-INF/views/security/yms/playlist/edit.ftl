<div class="delete_detail">
<div class="popup_body popup_body_full clearfix">
    <div class="playlist_wrap">
        <div class="playlist_nav" id="detail_container_${pid}">
            <ul class="playlist_select">
            <#list playListDetails as detail>
                <li detail_id="${(detail.id)!0}" <#if detail_index == 0>class="selected"</#if>
                    url="${contextPath}/security/yms/playlist/detail.htm?detailId=${(detail.id)!0}&pid=${pid}">${(detail.detailName)!''}
                </li>
            </#list>
            </ul>
            <div class="add"><a href="javascript:add_playlist_detail_${pid}(${(id)!0})">+ 新建播放明细</a></div>
        </div>
        <div class="playlist_con" id="detail_content_${pid}">
            <#include 'detail.ftl'>
        </div>
    </div>
</div>
<div class="popup_btn popup_btn_full">
    <button class="btn btn_green ok">确定</button>
    <button class="btn btn_red apply_audit">申请审核</button>
    <button class="btn btn_border close">关闭</button>
</div>
</div>
<script>
    (function(){
        var pid = '${pid}';
        var win = $('#${pid}');

        var length = $('.playlist_select li').length;
        if (length == 0) {
            add_playlist_detail_${pid}(${id});
        }

        // 获取素材字节大小
        var mSize = 0;
        $('.playlist_con').find('dl').each(function() {
            $(this).find('dd.material').each(function() {
                var materialSize = $(this).attr('material_size');
                mSize = parseInt(mSize) + parseInt(materialSize);
            });
        });
        // 获取字幕字节大小
        var SuSize = 0;
        $('.playlist_con').find('dl').each(function() {
            $(this).find('dd.subtitle').each(function() {
                var subtitleSize = $(this).attr('subtitle_size');
                SuSize = parseInt(SuSize) + parseInt(subtitleSize);
            });
        });
        var size = parseInt(mSize) + parseInt(SuSize);
        $('.detail_name_class').append(" ["+App.fileSize(size)+"]");

        //点击播放明细切换
        $('#detail_container_${pid}').delegate("li", "click", function(){
            var me = $(this);
            if (me.attr('class') && me.attr('class').indexOf('selected') >= 0) {
                return;
            }

            $('.selected').removeClass('selected');
            me.addClass('selected');

            var detailId = $(this).attr('detail_id');
            $.post('${contextPath}/security/yms/playlist/detail.htm', {
                detailId: detailId,
                pid:pid
            }, function (html) {
                $('#detail_content_${pid}').html(html);
                //获取播放明细中所有素材大小
                var size = getSize();
                var detailName = $('.detail_name_class').attr('detail_name');
                var line = '<h3 class="detail_name_class" detail_name="DETAIL_NAME">'+ detailName+' ['+App.fileSize(size)+']' +'<h3>';
                var html = line.replace(/DETAIL_NAME/,detailName);
                $('.detail_name_class').replaceWith(html);
            }, 'html');
        });

        win.find('.close').click(function () {
            win.window('close');
        });

        //申请审核
        win.find('.btn_red.apply_audit').click(function (){
            $.messager.confirm('提示信息', '确认提交审核么?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/yms/playlist/apply_audit.htm', {
                    id: ${(id)!0}
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            document.location.reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        });

        //点击确定提交
        win.find('button.ok').click(function() {
            $.messager.confirm('提示信息', '确认修改?', function(ok) {
                if(ok) {
                    var options = $(".select_template option:selected");
                    var tempalteId = options.val();
                    var detailId = $('li.selected').attr('detail_id');
                    var detailName = $('.detail_name_class').attr('detail_name');
                    var beginTime = new Date(Date.parse($('.time span').attr('begin_time').replace(/-/g,"/"))).getTime();
                    var endTime = new Date(Date.parse($('.time span').attr('end_time').replace(/-/g,"/"))).getTime();
                    //获取<dl>下数据
                    var materials = getMaterials();
                    var playListDetail = {
                        'id': detailId,
                        'detailName': detailName,
                        'materials': materials,
                        'beginTime': beginTime,
                        'endTime': endTime
                    };

                    $.ajax({
                        url: "${contextPath}/security/yms/playlist_detail/update.htm",
                        type: "POST",
                        datatype: "json",
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify(playListDetail),
                        success: function (data, stats) {
                            if (stats == "success") {
                                $.post('${contextPath}/security/yms/playlist/detail.htm', {
                                    detailId: detailId,
                                    pid: pid
                                }, function (html) {
                                    $('#detail_content_${pid}').html(html);
                                    var size = getSize();
                                    $('.detail_name_class').append(" ["+App.fileSize(size)+"]");

                                }, 'html');
                            }
                        },
                        error: function (data) {
                            alert("修改失败");
                        }
                    });
                }
            });
        });

    })();

    //上移
    $("div").delegate(".up","click",function(){
        var cur = $(this).parent().parent();
        if(cur.prev().length != 0) {
            var clone = cur.clone();
            cur.prev().eq(0).before(clone);
            cur.remove();
            this.setupSelectedMaterialCallback();
        }
    });

    //下移
    $("div").delegate(".down","click",function(){
        var cur = $(this).parent().parent();
        if(cur.nextAll().length > 1) {
            var clone = cur.clone();
            cur.next().eq(0).after(clone);
            cur.remove();
            this.setupSelectedMaterialCallback();
        }
    });

    //删除
    $("div").delegate(".delete","click",function(){
        $(this).parent().parent().remove();
        var size = getSize();
        var detailName = $('.detail_name_class').attr('detail_name');
        var line = '<h3 class="detail_name_class" detail_name="DETAIL_NAME">'+ detailName+' ['+App.fileSize(size)+']' +'<h3>';
        var html = line.replace(/DETAIL_NAME/,detailName);
        $('.detail_name_class').replaceWith(html);
    });


    //添加播放明细
    function add_playlist_detail_${pid}(id) {
        var pid = '${pid}';
        App.dialog.show({
            css: 'width:320px;height:230px;',
            title: '新建',
            href: "${contextPath}/security/yms/playlist/add_playlist_detail.htm?id="+id
        });
    }

    var getMaterials = function() {
        //获取<dl>下数据
        var materials = [];
        $('.playlist_con').find('dl').each(function() {
            var num = 1;
            $(this).find('dd.material').each(function() {
                var orderNum = num;
                var materialId = $(this).attr('material_id');
                var duration = $(this).find('input').val();
                var areaId = $(this).attr('area_id');
                var material = {};
                material['orderNum'] = num;
                material['materialId'] = materialId;
                material['duration'] = duration;
                materials.push(material);
                num++;
            });
            num = 0;
        });
        return materials;
    };

    //获取播放明细字节大小
    var getSize = function() {
        // 获取素材字节大小
        var mSize = 0;
        $('.playlist_con').find('dl').each(function() {
            $(this).find('dd.material').each(function() {
                var materialSize = $(this).attr('material_size');
                mSize = parseInt(mSize) + parseInt(materialSize);
            });
        });
        // 获取字幕字节大小
        var SuSize = 0;
        $('.playlist_con').find('dl').each(function() {
            $(this).find('dd.subtitle').each(function() {
                var subtitleSize = $(this).attr('subtitle_size');
                SuSize = parseInt(SuSize) + parseInt(subtitleSize);
            });
        });
        var size = parseInt(mSize) + parseInt(SuSize);
        return size;
    }

</script>