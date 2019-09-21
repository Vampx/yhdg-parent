<script type="text/javascript">
    <#if success>
    parent.set_transfer_image_path({
        filePath: '${(filePath)!''}',
        fileName: '${(fileName)!''}',
        pid: '${pid}'
    });
    <#else>
    alert('${message}');
    </#if>
</script>