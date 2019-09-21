<script type="text/javascript">
    <#if success>
    parent.set_model_image({
        filePath: '${(filePath)!''}',
        fileName: '${(fileName)!''}',
        pid: '${pid}'
    });
    <#else>
    alert('${message}');
    </#if>
</script>