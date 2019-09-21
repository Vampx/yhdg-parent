<script type="text/javascript">
    <#if success>
    parent.set_portrait({
        filePath: '${(filePath)!''}',
        fileName: '${(fileName)!''}',
        pid: '${pid}',
        typs: '${(typs)!''}'
    });
    <#else>
    alert('${message}');
    </#if>
</script>