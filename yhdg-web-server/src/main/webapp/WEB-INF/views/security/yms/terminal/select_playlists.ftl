<div class="popup_body">
        <div class="select_playlists" >
            <div class="select_box" style="width: 200px; height:402px; float:right;">
                <div class="select_head" >
                    <h3>已选列表</h3><a class="a_red" href="javascript:deleteAll()">清空</a>
                    <span class="msg">双击添加页面</span>
                </div>
                <div class="select_body" style="top:59px; overflow: hidden;">
                    <ul id="selected_container_${pid}">
                    </ul>
                </div>
            </div>
        </div>
        <div class="search" style="margin:0 0; padding:2px 0 0 0; border-bottom:none;">
            <table cellspacing="0" cellpadding="0" border="0">
                <tr>
                    <td style="width:65px; text-align:right;">播放列表:&nbsp;</td>
                    <td style="width:140px;"><input type="text" class="text" style="width:120px;" id="playlist_name${pid}"/></td>
                    <td><a class="btn_yellow" href="javascript:query_${pid}()">搜 索</a></td>
                </tr>
            </table>
        </div>
        <div class="select_playlists" >
            <div class="select_body" style="margin-right: 20px; top:29px; overflow: hidden;">
                <div class="grid" style="width: 420px; height:360px; margin-right: 20px;">
                    <table id="table_${pid}"></table>
                </div>
            </div>
        </div>
    </div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>

<script>
    var query_${pid};

    (function() {
        var pid = '${pid}', win = $('#' + pid), windowData = win.data('windowData');
        var selectedContainer = $('#selected_container_${pid}');
        var datagrid = $('#table_${pid}');
        var groupTree = $('#group_tree_${pid}');

        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/yms/playlist/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
            fitColumns: true,
            pageSize: 10,
            pageList: [10, 50, 100],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: function () { return 'height:2.8em;'; },
            columns: [
                [
                    {
                        title: 'checkbox', filed: 'id', checkbox: true
                    },
                    {
                        title: '播放列表名称',
                        align: 'center',
                        field: 'playlistName',
                        width: 40
                    }
                ]
            ],
            onDblClickRow: function(index, row) {
                if(getValues().length < 1) {
                    add(row);
                } else {
                        $.messager.alert('提示信息', '只允许选择1条记录', 'info');
                }
            },
            onCheck: function(index, row) {
                if(getValues().length < 1) {
                    add(row);
                } else {
                        $.messager.alert('提示信息', '只允许选择1条记录', 'info');
                }
            }
        });

        var line =
                '<li playlist_id="PLAYLIST_ID" playlist_name="PLAYLIST_NAME">' +
                '<span class="text">PLAYLIST_NAME</span>' +
                '<p class="icon_btn">' +
                '<a class="delete" title="删除" href="javascript:void(0)" onclick="deleteRow(this)"><span class="icon"></span></a>' +
                '</p>' +
                '</li>';

        var add = function(playlist) {
            if($('#selected_container_${pid} li[playlist_id="' + playlist.id +'"]').length > 0) {
                alert("列表已存在");
                return;
            }
            var html = line.replace(/PLAYLIST_ID/, playlist.id).replace(/PLAYLIST_NAME/g, playlist.playlistName);
            selectedContainer.append(html);
        }

        var getSelected = function() {
            var list = [];
            selectedContainer.find('li').each(function() {
                var me = $(this);
                list.push({
                    playlistId: me.attr('playlist_id'),
                    playlistName: me.attr('playlist_name')
                });
            });
            return list;
        }

        var setValues = function(list) {
            add(list);
        }

        var getValues = function() {
            return getSelected();
        }

        var query = function() {
//            var groupId = groupTree.tree('getSelected');
//            if(groupId) {
//                groupId = groupId.id;
//            }
//            if(!groupId) {
//                groupId = '';
//            }

            datagrid.datagrid('options').queryParams = {
                playlistName: $('#playlist_name${pid}').val(),
//                groupId: groupId
            };

            datagrid.datagrid('load');
        };
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