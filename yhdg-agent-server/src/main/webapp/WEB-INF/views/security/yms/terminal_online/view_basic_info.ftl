<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">CPU：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" required="true" id="cpu_${pid}" name="cpu"  /></td>
                    <td width="70" align="right">内存：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" required="true" name="memory" id="memory_${pid}" /></td>
                </tr>
                <tr>
                    <td width="70" align="right">SD卡容量：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" required="true" name="cardCapacity" <#if (entity.cardCapacity)??> value="${app.format_file_size((entity.cardCapacity))}" </#if> /></td>
                    <td width="70" align="right">SD卡剩余数量：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" required="true" name="restCapacity" <#if (entity.restCapacity)??> value="${app.format_file_size((entity.restCapacity))}" </#if>/></td>
                </tr>
                <tr>
                    <td width="70" align="right">正常标志：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" required="true" name="isNormal" value="${(entity.isNormal)!''}" /></td>
                    <td width="70" align="right">在线状态：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" required="true" name="isOnline" value="${(entity.isOnline)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">播放音量：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" required="true" name="playVolume" value="${(entity.playVolume)!''}"/></td>
                    <td width="70" align="right">状态信息：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" required="true" name="statusInfo" value="${(entity.statusInfo)!''}" /></td>
                </tr>
                <tr>
                    <td width="70" align="right">状态信息：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" required="true" name="statusInfo" value="${(entity.statusInfo)!''}" /></td>
                    <td width="70" align="right">策略Uid：</td>
                    <td><input type="text" readonly class="text easyui-validatebox" required="true" name="strategyUid" value="${(entity.strategyUid)!''}" /></td>
                </tr>
            </table>
        </form>
    </div>
</div>

<script>
    (function() {
        $('#cpu_${pid}').val(parseInt(${(entity.cpu*100)!''} )+ "%");
        $('#memory_${pid}').val(parseInt(${(entity.memory*100)!''})+ "%");
    })();
</script>




