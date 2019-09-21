<script type="text/javascript">
    <#if success>
    parent.set_product_image({
        filePath: '${(filePath)!''}',
        fileName: '${(fileName)!''}',
        pid: '${pid}',
        num: '${num}'
    });
    <#else>
    alert('${message}');
    </#if>
</script>