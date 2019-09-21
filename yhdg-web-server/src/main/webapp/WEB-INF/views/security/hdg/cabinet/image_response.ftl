<script type="text/javascript">
    <#if success>
    parent.set_image({
        filePath1: '${(filePath1)!''}',
        filePath2: '${(filePath2)!''}',
        fileName1: '${(fileName1)!''}',
        fileName2: '${(fileName2)!''}',
        pid: '${pid}',
        num: '${num}'
    });
    <#else>
    alert('${message}');
    </#if>
</script>