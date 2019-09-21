<script type="text/javascript">
    <#if success>
    parent.set_image_path({
        filePath: '${(filePath)!''}',
        fileName: '${(fileName)!''}',
        pid: '${pid}'
    });
    <#else>
    alert('${message}');
    </#if>
</script>