<div class="popup_body">
    <div class="step_box step_box_5">
        <ul class="clearfix">
            <li class="first selected" step="1"><p><i>1.</i>策略名称</p></li>
            <li step="2"><p><i>2.</i>开关机策略</p></li>
            <li step="3"><p><i>3.</i>音量策略</p></li>
            <#--<li step="4"><p><i>4.</i>下载策略</p></li>-->
            <#--<li class="last" step="5"><p><i>5.</i>日志策略</p></li>-->
        </ul>
    </div>
<#include 'strategy_1.ftl'>
<#include 'strategy_2.ftl'>
<#include 'strategy_3.ftl'>
<#--<#include 'strategy_4.ftl'>-->
<#--<#include 'strategy_5.ftl'>-->
</div>

<div class="popup_btn" style="margin-top: 88px">
    <button class="btn btn_border prev" style="display: none;">上一步</button>
    <button class="btn btn_red next">下一步</button>
    <button class="btn btn_red ok" style="display: none;">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var prevButton = win.find('.prev');
        var nextButton = win.find('.next');
        var okButton = win.find('.ok');

        var values = {
            strategyName: '',
            switchSetting: {
                openTime: [
                    {active:1, begin: '00:00:00', end: '23:59:59'}
                ],
                rebootTime: [
                    {active:1, time: '06:00:00'}
                ],
                week: [1, 2, 3, 4, 5, 6, 7]
            },
            volumeSetting: {
                volume: 10,
                stationVolume:10,
                time: [
                    {begin: '00:00:00', end: '23:59:59', active: 1, volume: 10}
                ],
                week: [1, 2, 3, 4, 5, 6, 7]
            },
            downloadSetting: {
                speed: 0,
                time: [
                    {begin: '00:00:00', end: '23:59:59', active: 1, speed: 0}
                ],
                week: [1, 2, 3, 4, 5, 6, 7]
            },
            logSetting: {
                wireless: 1,
                timeType: 1,
                begin: '06:00:00',
                end: '07:00:00'
            }

        };
        win.data('values', values);

        function toggleButton(step) {
            var prev = step.prev();
            var next = step.next();

            if(prev.length) {
                prevButton.show();
            } else {
                prevButton.hide();
            }

            if(next.length) {
                nextButton.show();
                okButton.hide();
            } else {
                nextButton.hide();
                okButton.show();
            }
        }

        function getStepNum() {
            var step = win.find('.step_box li.selected');
            return step.attr('step');
        }

        function collectValue(step) {
            return win.data('collectValue' + step)();
        }

        function showValue(step) {
            win.data('showValue' + step)();
        }

        win.find('.prev').click(function() {
            var step = win.find('.step_box li.selected');
            var prev = step.prev();
            if(prev.length && collectValue(step.attr('step'))) {
                step.removeClass('selected');
                prev.addClass('selected');
                var num = parseInt(step.attr('step'));
                win.find('.step_item_' + num).hide();
                num--;
                win.find('.step_item_' + num).show();
                toggleButton(prev);
                showValue(prev.attr('step'));
            }
        })
        win.find('.next').click(function() {
            var step = win.find('.step_box li.selected');
            var next = step.next();
            if(next.length && collectValue(step.attr('step'))) {
                step.removeClass('selected');
                next.addClass('selected');
                var num = parseInt(step.attr('step'));
                win.find('.step_item_' + num).hide();
                num++;
                win.find('.step_item_' + num).show();
                toggleButton(next);
                showValue(next.attr('step'));
            }
        })
        win.find('.ok').click(function() {
            if(collectValue(3)) {
                $.post('${contextPath}/security/yms/terminal_strategy/create.htm', {
                    agentId: values.agentId,
                    strategyName: values.strategyName,
                    content: $.toJSON(values)
                }, function(json) {
                <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }, 'json');
            }

        })
        win.find('.close').click(function() {
            win.window('close');
        })
    })();
</script>
