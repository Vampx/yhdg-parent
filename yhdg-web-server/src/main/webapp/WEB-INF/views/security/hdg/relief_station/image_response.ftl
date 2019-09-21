<script type="text/javascript">
<#if success>
    parent.set_image({
        filePath: '${(filePath)!''}',
        fileName: '${(fileName)!''}',
        staticUrl: '${staticUrl}',
        pid: '${pid}'
    });
<#else>
alert('${message}');
</#if>
</script>