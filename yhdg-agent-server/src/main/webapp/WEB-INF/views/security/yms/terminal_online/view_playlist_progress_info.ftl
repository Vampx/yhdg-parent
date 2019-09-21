<div class="popup_body" style="height: 310px;">
    <div class="search" style="margin:0 0; padding:2px 0 0 0; border-bottom:none;">
        <table cellspacing="0" cellpadding="0" border="0">
            <tr>
                <td>&nbsp;&nbsp;&nbsp;播放列表序号：</td>
                <td name="id">
                    <input type="text" class="text" style="width: 90px" value="${(entity.playlistUid)!''}">
                </td>
                <td>&nbsp;&nbsp;&nbsp;下载进度：</td>
                <td name="percent">
                    <input type="text" class="text" style="width: 55px" <#if (entity.percent)??> value="${(entity.percent*100)?string("#")}%" </#if>>
                </td>
                <td>&nbsp;&nbsp;&nbsp;下载速度：</td>
                <td name="speed">
                    <input type="text" class="text" style="width: 55px" <#if (entity.speed)??> value="${(entity.speed)?string("#")}" </#if> >Kb/s
                </td>
            </tr>
        </table>
    </div>
    <div class="select_routes" style="width: 100%" >
        <div class="select_body" style="width: 100%; top:29px; ">
            <div class="grid" style="width: 100%; height:290px;">
                <table id="page_table_${pid}"></table>
            </div>
        </div>
    </div>
</div>
<script>
    (function() {
        $('#page_table_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: false,
            url: "${contextPath}/security/yms/terminal_online/page_playlist_progress_info.htm",
            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '路径',
                        align: 'center',
                        field: 'path',
                        width: 80
                    },
                    {
                        title: '名称',
                        align: 'center',
                        field: 'name',
                        width: 60
                    },
                    {
                        title: '文件大小',
                        align: 'center',
                        field: 'length',
                        width: 30,
                        formatter: function (val, row) {
                            return App.fileSize(val);
                        }
                    },
                    {
                        title: '下载进度',
                        align: 'center',
                        field: 'percent',
                        width: 30,
                        formatter: function (val, row) {
                            return (new Number(row.percent*100)).toFixed(2) + "%";
                        }
                    }
                ]
            ],
            queryParams: {
            id: "${id}"
            }
        });
    })();
</script>