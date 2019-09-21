<div class="tab_item" style="display:block;">
    <div class="ui_table" style="height: 380px">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right" width="140">停留心跳间隔(s)：</td>
                    <td><input  name="stayHeartbeat" class="easyui-numberspinner"  style="width: 184px; height: 28px;"  readonly value="${entity.stayHeartbeat!''}"></td>
                    <td align="right" width="140">移动心跳间隔(s)：</td>
                    <td><input  name="moveHeartbeat" class="easyui-numberspinner" data-options="min:2" readonly style="width: 184px; height: 28px;" value="${entity.moveHeartbeat!''}"></td>
                </tr>
                <tr>
                    <td align="right" width="140">存储心跳间隔(s)：</td>
                    <td><input  name="standbyHeartbeat" class="easyui-numberspinner"  style="width: 184px; height: 28px;"  readonly value="${entity.standbyHeartbeat!''}"></td>
                    <td align="right" width="140">休眠心跳间隔(s)：</td>
                    <td><input  name="dormancyHeartbeat" class="easyui-numberspinner" readonly style="width: 184px; height: 28px;" value="${entity.dormancyHeartbeat!''}"></td>
                </tr>
                <tr>
                    <td align="right" width="90">通电心跳间隔(s)：</td>
                    <td><input  name="electrifyHeartbeat" class="easyui-numberspinner" readonly  style="width: 184px; height: 28px;" value="${entity.electrifyHeartbeat!''}"></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var ok = function() {
            var success = true;
            return success;
        };
        win.data('ok', ok);
    })();
</script>