<div class="head" pid="${pid}">
    <div class="btn">
        <h3>${(playListDetail.detailName)!''}</h3>
    </div>
    <div class="time">
        <span class="text">播放时间：<#if publishedPlaylistDetail??&&publishedPlaylistDetail!=''>${app.format_date_time(publishedPlaylistDetail.beginTime)} - ${app.format_date_time(publishedPlaylistDetail.endTime)}</#if></span>
    </div>
</div>
<dl>
    <dt>播放素材:</dt>
    <#if materials??>
        <#list materials as material>
            <dd class="view_material" material_id="${(material.materialId)!0}" material_version="${(material.version)!0}">
                <i class="num">${(material.orderNum)!0}</i>
                <span class="text">${(material.materialName)!''}（${(material.duration)!''}秒）</span>
            </dd>
        </#list>
    </#if>
</dl>
<script>
    $('dl').delegate("dd.view_material", "click", function(){
        var id = $(this).attr('material_id');
        var version = $(this).attr('material_version');
        App.dialog.show({
            css: 'width:580px;height:310px;',
            title: '查看',
            href: "${contextPath}/security/yms/published_material/view.htm?id=" + id + "&version=" + version
        });
    });
</script>
