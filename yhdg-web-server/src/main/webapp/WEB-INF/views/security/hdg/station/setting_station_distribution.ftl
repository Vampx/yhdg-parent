
    <div class="c-d-three-2 c-d-three-2-site" style="display: none;" >
        <button class="export" onclick="distribution_export_excel()">导出</button>

        <form id="station_distribution_parameter">
        <ul>
            <#if stationDistributionList?? && (stationDistributionList?size>0) >

                <#list stationDistributionList as stationDistribution>
                    <#if stationDistribution.deptType == 1>
                        <li>
                            <p class="title operate_dept" operate_dept = 1>系统分成：</p>
                            <label for="t1_a" class="t1">
                                <input id="t1_a" type="radio" name="a" value="1" <#if stationDistribution.isNotFixed == 1>checked</#if> >不分成
                            </label>
                            <label for="t1_b"  class="t1">
                                <input id="t1_b" type="radio" name="a" value="2" <#if stationDistribution.isFixed == 1>checked</#if> >固定分成
                            </label>
                            <label for="t1_c" class="t1">
                                <input id="t1_c" type="radio" name="a" value="3"  <#if stationDistribution.isFixedPercent == 1>checked</#if> >百分比
                            </label>
                            <div class="t1_1">
                                <span>金额：</span>
                                <input type="text" name="money" class="number_money" maxlength="10" value="${(stationDistribution.money/100)?string('0.00')}"> 元
                            </div>
                            <div class="t1_2" style="display: none">
                                <span>百分之：</span>
                                <input type="hidden" name="num" class="operate_mum" value="0">
                                <input type="hidden" name="operateId_0" value="0">
                                <input type="text" name="percent" class="number_percent" maxlength="5"value="${(stationDistribution.percent)!0}"> %
                            </div>
                        </li>
                    <#elseif stationDistribution.deptType == 2>
                        <li>
                            <p class="title operate_dept" operate_dept = 2>运营商分层：</p>
                            <label for="t2_a" class="t2">
                                <input id="t2_a" type="radio" name="b"  value="1" <#if stationDistribution.isNotFixed == 1>checked</#if>>不分成
                            </label>
                            <label for="t2_b"  class="t2">
                                <input id="t2_b" type="radio" name="b" value="2" <#if stationDistribution.isFixed == 1>checked</#if>>固定分成
                            </label>
                            <label for="t2_c" class="t2">
                                <input id="t2_c" type="radio" name="b" value="3" <#if stationDistribution.isFixedPercent == 1>checked</#if>>百分比
                            </label>
                            <div class="t2_1">
                                <span>金额：</span>
                                <input type="text" name="money" class="number_money" maxlength="10" value="${(stationDistribution.money/100)?string('0.00')}"> 元
                            </div>
                            <div class="t2_2" style="display: none">
                                <span>百分之：</span>
                                <input type="hidden" name="num" class="operate_mum" value="0">
                                <input type="hidden" name="operateId_0" value="0">
                                <input type="text" name="percent" class="number_percent" maxlength="5"value="${(stationDistribution.percent)!0}"> %
                            </div>
                        </li>
                    <#elseif stationDistribution.deptType == 3>
                        <li>
                            <p class="title operate_dept" operate_dept = 3>站点分层：</p>
                            <label for="t3_a" class="t3">
                                <input id="t3_a" type="radio" name="c" value="1" <#if stationDistribution.isNotFixed == 1>checked</#if>>不分成
                            </label>
                            <label for="t3_b"  class="t3">
                                <input id="t3_b" type="radio" name="c" value="2" <#if stationDistribution.isFixed == 1>checked</#if>>固定分成
                            </label>
                            <label for="t3_c" class="t3">
                                <input id="t3_c" type="radio" name="c" value="3" <#if stationDistribution.isFixedPercent == 1>checked</#if>>百分比
                            </label>
                            <div class="t3_1">
                                <span>金额：</span>
                                <input type="text" name="money" class="number_money" maxlength="10" value="${(stationDistribution.money/100)?string('0.00')}"> 元
                            </div>
                            <div class="t3_2" style="display: none">
                                <span>百分之：</span>
                                <input type="hidden" name="num" class="operate_mum" value="0">
                                <input type="hidden" name="operateId_0" value="0">
                                <input type="text" name="percent" class="number_percent" maxlength="5"value="${(stationDistribution.percent)!0}"> %
                            </div>
                        </li>
                    <#elseif stationDistribution.deptType == 4 && stationDistribution.num == 1>
                        <li>
                            <p class="title operate_dept" operate_dept = 4>其他分成1：</p>
                            <label for="t4_a" class="t4">
                                <input id="t4_a" type="radio" name="d" value="1" <#if stationDistribution.isNotFixed == 1>checked</#if>>不分成
                            </label>
                            <label for="t4_b" class="t4">
                                <input id="t4_b" type="radio" name="d" value="2" <#if stationDistribution.isFixed == 1>checked</#if>>固定分成
                            </label>
                            <label for="t4_c" class="t4">
                                <input id="t4_c" type="radio" name="d" value="3" <#if stationDistribution.isFixedPercent == 1>checked</#if>>百分比
                            </label>
                            <div class="t4_1">
                                <span>金额：</span>
                                <input type="text" name="money" class="number_money" maxlength="10" value="${(stationDistribution.money/100)?string('0.00')}"> 元
                            </div>
                            <div class="t4_2" style="display: none">
                                <span>百分之：</span>
                                <input type="text" name="percent" class="number_percent" maxlength="5"value="${(stationDistribution.percent)!0}"> %
                            </div>
                            <div>
                                <span>运营体：</span>
                                <input type="hidden" name="num" class="operate_mum" value="${(stationDistribution.num)!1}">
                                <input type="hidden" name="operateId_${(stationDistribution.num)!1}" value="${(stationDistribution.operateId)!0}">
                                <input type="text" name="operateName_${(stationDistribution.num)!1}" value="${(stationDistribution.operateName)!''}">
                                <span class="opt" onclick="selectDistributionOperate_1(${(stationDistribution.num)!1})" >选择运营体</span>
                            </div>
                        </li>
                    <#elseif stationDistribution.deptType == 4 && stationDistribution.num == 2 >
                        <li>
                            <p class="title operate_dept" operate_dept = 4>其他分成2：</p>
                            <label for="t5_a" class="t5">
                                <input id="t5_a" type="radio" name="e" value="1"  <#if stationDistribution.isNotFixed == 1>checked</#if>>不分成
                            </label>
                            <label for="t5_b" class="t5">
                                <input id="t5_b" type="radio" name="e" value="2" <#if stationDistribution.isFixed == 1>checked</#if>>固定分成
                            </label>
                            <label for="t5_c" class="t5">
                                <input id="t5_c" type="radio" name="e" value="3" <#if stationDistribution.isFixedPercent == 1>checked</#if>>百分比
                            </label>
                            <div class="t5_1">
                                <span>金额：</span>
                                <input type="text" name="money" class="number_money" maxlength="10" value="${(stationDistribution.money/100)?string('0.00')}"> 元
                            </div>
                            <div class="t5_2" style="display: none">
                                <span>百分之：</span>
                                <input type="text" name="percent" class="number_percent" maxlength="5"value="${(stationDistribution.percent)!0}"> %
                            </div>
                            <div>
                                <span>运营体：</span>
                                <input type="hidden" name="num" class="operate_mum" value="${(stationDistribution.num)!2}">
                                <input type="hidden" name="operateId_${(stationDistribution.num)!2}" value="${(stationDistribution.operateId)!0}">
                                <input type="text" name="operateName_${(stationDistribution.num)!2}" value="${(stationDistribution.operateName)!''}">
                                <span class="opt" onclick="selectDistributionOperate_3(${(stationDistribution.num)!2})" >选择运营体</span>
                            </div>
                        </li>
                    <#elseif stationDistribution.deptType == 4 && stationDistribution.num == 3>
                        <li>
                            <p class="title operate_dept" operate_dept = 4>其他分成3：</p>
                            <label for="t6_a" class="t6">
                                <input id="t6_a" type="radio" name="f" value="1" <#if stationDistribution.isNotFixed == 1>checked</#if>>不分成
                            </label>
                            <label for="t6_b" class="t6">
                                <input id="t6_b" type="radio" name="f" value="2" <#if stationDistribution.isFixed == 1>checked</#if>>固定分成
                            </label>
                            <label for="t6_c" class="t6">
                                <input id="t6_c" type="radio" name="f" value="3" <#if stationDistribution.isFixedPercent == 1>checked</#if>>百分比
                            </label>
                            <div class="t6_1">
                                <span>金额：</span>
                                <input type="text" name="money" class="number_money" maxlength="10" value="${(stationDistribution.money/100)?string('0.00')}"> 元
                            </div>
                            <div class="t6_2" style="display: none">
                                <span>百分之：</span>
                                <input type="text" name="percent" class="number_percent" maxlength="5" value="${(stationDistribution.percent)!0}"> %
                            </div>
                            <div>
                                <span>运营体：</span>
                                <input type="hidden" name="num" class="operate_mum" value="${(stationDistribution.num)!3}">
                                <input type="hidden" name="operateId_${(stationDistribution.num)!3}" value="${(stationDistribution.operateId)!0}">
                                <input type="text" name="operateName_${(stationDistribution.num)!3}" value="${(stationDistribution.operateName)!''}">
                                <span class="opt" onclick="selectDistributionOperate_3(${(stationDistribution.num)!3})" >选择运营体</span>
                            </div>
                        </li>
                    </#if>
                 </#list>

            <#else>
                <li>
                    <p class="title operate_dept" operate_dept = 1>系统分成：</p>
                    <label for="t1_a" class="t1">
                        <input id="t1_a" type="radio" name="a" value="1" >不分成
                    </label>
                    <label for="t1_b"  class="t1">
                        <input id="t1_b" type="radio" name="a" value="2" checked>固定分成
                    </label>
                    <label for="t1_c" class="t1">
                        <input id="t1_c" type="radio" name="a" value="3" >百分比
                    </label>
                    <div class="t1_1">
                        <span>金额：</span>
                        <input type="text" name="money" class="number_money" maxlength="10" > 元
                    </div>
                    <div class="t1_2" style="display: none">
                        <span>百分之：</span>
                        <input type="hidden" name="num" class="operate_mum" value="0">
                        <input type="hidden" name="operateId_0" value="0">
                        <input type="text" name="percent" class="number_percent" maxlength="5"> %
                    </div>
                </li>
                <li>
                    <p class="title operate_dept" operate_dept = 2>运营商分层：</p>
                    <label for="t2_a" class="t2">
                        <input id="t2_a" type="radio" name="b"  value="1">不分成
                    </label>
                    <label for="t2_b"  class="t2">
                        <input id="t2_b" type="radio" name="b" value="2" checked>固定分成
                    </label>
                    <label for="t2_c" class="t2">
                        <input id="t2_c" type="radio" name="b" value="3">百分比
                    </label>
                    <div class="t2_1">
                        <span>金额：</span>
                        <input type="text" name="money" class="number_money" maxlength="10" > 元
                    </div>
                    <div class="t2_2" style="display: none">
                        <span>百分之：</span>
                        <input type="hidden" name="num" class="operate_mum" value="0">
                        <input type="hidden" name="operateId_0" value="0">
                        <input type="text" name="percent" class="number_percent" maxlength="5"> %
                    </div>
                </li>
                <li>
                    <p class="title operate_dept" operate_dept = 3>站点分层：</p>
                    <label for="t3_a" class="t3">
                        <input id="t3_a" type="radio" name="c" value="1">不分成
                    </label>
                    <label for="t3_b"  class="t3">
                        <input id="t3_b" type="radio" name="c" value="2" checked>固定分成
                    </label>
                    <label for="t3_c" class="t3">
                        <input id="t3_c" type="radio" name="c" value="3">百分比
                    </label>
                    <div class="t3_1">
                        <span>金额：</span>
                        <input type="text" name="money" class="number_money" maxlength="10" > 元
                    </div>
                    <div class="t3_2" style="display: none">
                        <span>百分之：</span>
                        <input type="hidden" name="num" class="operate_mum" value="0">
                        <input type="hidden" name="operateId_0" value="0">
                        <input type="text" name="percent" class="number_percent" maxlength="5"> %
                    </div>
                </li>
                <li>
                    <p class="title operate_dept" operate_dept = 4>其他分成1：</p>
                    <label for="t4_a" class="t4">
                        <input id="t4_a" type="radio" name="d" value="1">不分成
                    </label>
                    <label for="t4_b" class="t4">
                        <input id="t4_b" type="radio" name="d" value="2" checked>固定分成
                    </label>
                    <label for="t4_c" class="t4">
                        <input id="t4_c" type="radio" name="d" value="3" >百分比
                    </label>
                    <div class="t4_1">
                        <span>金额：</span>
                        <input type="text" name="money" class="number_money" maxlength="10" > 元
                    </div>
                    <div class="t4_2" style="display: none">
                        <span>百分之：</span>
                        <input type="text" name="percent" class="number_percent" maxlength="5"> %
                    </div>
                    <div>
                        <span>运营体：</span>
                        <input type="hidden" name="num" class="operate_mum" value="1">
                        <input type="hidden" name="operateId_1" value="0">
                        <input type="text" name="operateName_1">
                        <span class="opt" onclick="selectDistributionOperate_1(1)">选择运营体</span>
                    </div>
                </li>
                <li>
                    <p class="title operate_dept" operate_dept = 4>其他分成2：</p>
                    <label for="t5_a" class="t5">
                        <input id="t5_a" type="radio" name="e" value="1" >不分成
                    </label>
                    <label for="t5_b" class="t5">
                        <input id="t5_b" type="radio" name="e" value="2" checked>固定分成
                    </label>
                    <label for="t5_c" class="t5">
                        <input id="t5_c" type="radio" name="e" value="3" >百分比
                    </label>
                    <div class="t5_1">
                        <span>金额：</span>
                        <input type="text" name="money" class="number_money" maxlength="10" > 元
                    </div>
                    <div class="t5_2" style="display: none">
                        <span>百分之：</span>
                        <input type="text" name="percent" class="number_percent" maxlength="5"> %
                    </div>
                    <div>
                        <span>运营体：</span>
                        <input type="hidden" name="num" class="operate_mum" value="2">
                        <input type="hidden" name="operateId_2" value="0">
                        <input type="text" name="operateName_2">
                        <span class="opt" onclick="selectDistributionOperate_2(2)">选择运营体</span>
                    </div>
                </li>
                <li>
                    <p class="title operate_dept" operate_dept = 4>其他分成3：</p>
                    <label for="t6_a" class="t6">
                        <input id="t6_a" type="radio" name="f" value="1">不分成
                    </label>
                    <label for="t6_b" class="t6">
                        <input id="t6_b" type="radio" name="f" value="2" checked>固定分成
                    </label>
                    <label for="t6_c" class="t6">
                        <input id="t6_c" type="radio" name="f" value="3" >百分比
                    </label>
                    <div class="t6_1">
                        <span>金额：</span>
                        <input type="text" name="money" class="number_money" maxlength="10" > 元
                    </div>
                    <div class="t6_2" style="display: none">
                        <span>百分之：</span>
                        <input type="text" name="percent" class="number_percent" maxlength="5" > %
                    </div>
                    <div>
                        <span>运营体：</span>
                        <input type="hidden" name="num" class="operate_mum" value="3">
                        <input type="hidden" name="operateId_3" value="0">
                        <input type="text" name="operateName_3">
                        <span class="opt" onclick="selectDistributionOperate_3(3)">选择运营体</span>
                    </div>
                </li>
             </#if>
        </ul>
        </form>
        <button class="but" id="station_distribution">保存</button>

    </div>

<script>

    function selectDistributionOperate_1(num) {
        App.dialog.show({
            css: 'width:826px;height:522px;overflow:visible;',
            title: '选择分成体',
            href: "${contextPath}/security/hdg/distribution_operate/select_distribution_operate.htm?agentId=${(entity.agentId)!0}"  ,
            windowData: {
                ok: function(config) {
                    $('#station_distribution_parameter').find('input[name=operateId_'+num+']').val(config.distribution.id);
                    $('#station_distribution_parameter').find('input[name=operateName_'+num+']').val(config.distribution.distributionName);
                }
            },
            event: {
                onClose: function() {
                }
            }
        });
    }

    function selectDistributionOperate_2(num) {
        App.dialog.show({
            css: 'width:826px;height:522px;overflow:visible;',
            title: '选择分成体',
            href: "${contextPath}/security/hdg/distribution_operate/select_distribution_operate.htm?agentId=${(entity.agentId)!0}"  ,
            windowData: {
                ok: function(config) {
                    $('#station_distribution_parameter').find('input[name=operateId_'+num+']').val(config.distribution.id);
                    $('#station_distribution_parameter').find('input[name=operateName_'+num+']').val(config.distribution.distributionName);
                }
            },
            event: {
                onClose: function() {
                }
            }
        });
    }

    function selectDistributionOperate_3(num) {
        App.dialog.show({
            css: 'width:826px;height:522px;overflow:visible;',
            title: '选择分成体',
            href: "${contextPath}/security/hdg/distribution_operate/select_distribution_operate.htm?agentId=${(entity.agentId)!0}"  ,
            windowData: {
                ok: function(config) {
                    $('#station_distribution_parameter').find('input[name=operateId_'+num+']').val(config.distribution.id);
                    $('#station_distribution_parameter').find('input[name=operateName_'+num+']').val(config.distribution.distributionName);
                }
            },
            event: {
                onClose: function() {
                }
            }
        });
    }

    function distribution_export_excel() {
        $.messager.confirm('提示信息', '确认导出数据?', function (ok) {
            if (ok) {
                window.location.href = "${contextPath}/security/hdg/station/distribution_export_excel.htm?stationId=${(stationId)!''}";
            }
        });
    }

    var t1 = $("input[name='a']:checked").val();
    if(t1 == 1){
        $(".t1_1").hide();
        $(".t1_2").hide();
    }else if(t1 ==2){
        $(".t1_1").show();
        $(".t1_2").hide();
    }else{
        $(".t1_1").hide();
        $(".t1_2").show();
    }

    var t2 = $("input[name='b']:checked").val();
    if(t2 == 1){
        $(".t2_1").hide();
        $(".t2_2").hide();
    }else if(t2 ==2){
        $(".t2_1").show();
        $(".t2_2").hide();
    }else{
        $(".t2_1").hide();
        $(".t2_2").show();
    }
    var t3 = $("input[name='c']:checked").val();
    if(t3 == 1){
        $(".t3_1").hide();
        $(".t3_2").hide();
    }else if(t3 ==2){
        $(".t3_1").show();
        $(".t3_2").hide();
    }else{
        $(".t3_1").hide();
        $(".t3_2").show();
    }

    var t4 = $("input[name='d']:checked").val();
    if(t4 == 1){
        $(".t4_1").hide();
        $(".t4_2").hide();
    }else if(t4 ==2){
        $(".t4_1").show();
        $(".t4_2").hide();
    }else{
        $(".t4_1").hide();
        $(".t4_2").show();
    }

    var t5 = $("input[name='e']:checked").val();
    if(t5 == 1){
        $(".t5_1").hide();
        $(".t5_2").hide();
    }else if(t5 ==2){
        $(".t5_1").show();
        $(".t5_2").hide();
    }else{
        $(".t5_1").hide();
        $(".t5_2").show();
    }

    var t6 = $("input[name='f']:checked").val();
    if(t6 == 1){
        $(".t6_1").hide();
        $(".t6_2").hide();
    }else if(t6 ==2){
        $(".t6_1").show();
        $(".t6_2").hide();
    }else{
        $(".t6_1").hide();
        $(".t6_2").show();
    }

    $('.t1').on('click','input',()=>{
        var t1 = $("input[name='a']:checked").val();
        if(t1 == 1){
            $(".t1_1").hide();
            $(".t1_2").hide();
        }else if(t1 ==2){
            $(".t1_1").show();
            $(".t1_2").hide();
        }else{
            $(".t1_1").hide();
            $(".t1_2").show();
        }
    })

    $('.t2').on('click','input',()=>{
        var t2 = $("input[name='b']:checked").val();
        if(t2 == 1){
            $(".t2_1").hide();
            $(".t2_2").hide();
        }else if(t2 ==2){
            $(".t2_1").show();
            $(".t2_2").hide();
        }else{
            $(".t2_1").hide();
            $(".t2_2").show();
        }
    })

    $('.t3').on('click','input',()=>{
        var t3 = $("input[name='c']:checked").val();
        if(t3 == 1){
            $(".t3_1").hide();
            $(".t3_2").hide();
        }else if(t3 ==2){
            $(".t3_1").show();
            $(".t3_2").hide();
        }else{
            $(".t3_1").hide();
            $(".t3_2").show();
        }
    })

    $('.t4').on('click','input',()=>{
        var t4 = $("input[name='d']:checked").val();
        if(t4 == 1){
            $(".t4_1").hide();
            $(".t4_2").hide();
        }else if(t4 ==2){
            $(".t4_1").show();
            $(".t4_2").hide();
        }else{
            $(".t4_1").hide();
            $(".t4_2").show();
        }
    })

    $('.t5').on('click','input',()=>{
        var t5 = $("input[name='e']:checked").val();
        if(t5 == 1){
            $(".t5_1").hide();
            $(".t5_2").hide();
        }else if(t5 ==2){
            $(".t5_1").show();
            $(".t5_2").hide();
        }else{
            $(".t5_1").hide();
            $(".t5_2").show();
        }
    })

    $('.t6').on('click','input',()=>{
        var t6 = $("input[name='f']:checked").val();
        if(t6 == 1){
            $(".t6_1").hide();
            $(".t6_2").hide();
        }else if(t6 ==2){
            $(".t6_1").show();
            $(".t6_2").hide();
        }else{
            $(".t6_1").hide();
            $(".t6_2").show();
        }
    })

    $('#station_distribution').click(function () {
        $.messager.confirm('提示信息', '确认修改分成信息?', function (ok) {
            if(ok) {
                var ok = function () {
                    var success = true;

                    var arr1 = [],arr2 = [],arr3 = [];
                    var val1 = $("input[name='a']:checked").val();
                    var val2 = $("input[name='b']:checked").val();
                    var val3 = $("input[name='c']:checked").val();
                    var val4 = $("input[name='d']:checked").val();
                    var val5 = $("input[name='e']:checked").val();
                    var val6 = $("input[name='f']:checked").val();

                    if(val1 == 1){
                        arr1.push(1)
                        arr2.push(0)
                        arr3.push(0)
                    } else if(val1 == 2){
                        arr1.push(0)
                        arr2.push(1)
                        arr3.push(0)
                    } else {
                        arr1.push(0)
                        arr2.push(0)
                        arr3.push(1)
                    }

                    if(val2 == 1){
                        arr1.push(1)
                        arr2.push(0)
                        arr3.push(0)
                    } else if(val2 == 2){
                        arr1.push(0)
                        arr2.push(1)
                        arr3.push(0)
                    } else {
                        arr1.push(0)
                        arr2.push(0)
                        arr3.push(1)
                    }

                    if(val3 == 1){
                        arr1.push(1)
                        arr2.push(0)
                        arr3.push(0)
                    } else if(val3 == 2){
                        arr1.push(0)
                        arr2.push(1)
                        arr3.push(0)
                    } else {
                        arr1.push(0)
                        arr2.push(0)
                        arr3.push(1)
                    }

                    if(val4 == 1){
                        arr1.push(1)
                        arr2.push(0)
                        arr3.push(0)
                    } else if(val4 == 2){
                        arr1.push(0)
                        arr2.push(1)
                        arr3.push(0)
                    } else {
                        arr1.push(0)
                        arr2.push(0)
                        arr3.push(1)
                    }

                    if(val5 == 1){
                        arr1.push(1)
                        arr2.push(0)
                        arr3.push(0)
                    } else if(val5 == 2){
                        arr1.push(0)
                        arr2.push(1)
                        arr3.push(0)
                    } else{
                        arr1.push(0)
                        arr2.push(0)
                        arr3.push(1)
                    }

                    if(val6 == 1){
                        arr1.push(1)
                        arr2.push(0)
                        arr3.push(0)
                    } else if(val6 == 2){
                        arr1.push(0)
                        arr2.push(1)
                        arr3.push(0)
                    } else {
                        arr1.push(0)
                        arr2.push(0)
                        arr3.push(1)
                    }

                    var numberMoneyList = [], numberPercentList = [], numList = [], deptTypeList = [], operateIdList = [];
                    var numberMoney = '', numberPercent = '', num = '', operateId, deptType;

                    var numberMoneyLength = $(".c-d-three-2-site .number_money").length;
                    for(var i = 0; i < numberMoneyLength; i++){
                        numberMoney = $(".c-d-three-2-site .number_money").eq(i).attr("value");
                        numberMoneyList.push(parseFloat(numberMoney));
                    }

                    var numberPercentLength = $(".c-d-three-2-site .number_percent").length;
                    for(var i = 0; i < numberPercentLength; i++){
                        numberPercent = $(".c-d-three-2-site .number_percent").eq(i).attr("value");
                        numberPercentList.push(parseFloat(numberPercent));
                    }

                    var numLength = $(".c-d-three-2-site .operate_mum").length;
                    for(var i = 0; i < numLength; i++){
                        num = $(".c-d-three-2-site .operate_mum").eq(i).attr("value");
                        numList.push(num);
                    }

                    for(var i = 0; i < numLength; i++){
                        num = $(".c-d-three-2-site .operate_mum").eq(i).attr("value");
                        var oper = $('input[name=operateId_'+num+']').val();
                        if (oper == null) {
                            operateIdList.push(0);
                        } else {
                            operateIdList.push(oper);
                        }
                    }

                    var deptTypeLength = $(".c-d-three-2-site .operate_dept").length;
                    for(var i = 0; i < deptTypeLength; i++){
                        deptType = $(".c-d-three-2-site .operate_dept").eq(i).attr("operate_dept");
                        deptTypeList.push(deptType);
                    }

                    var values = {
                        stationId: '${(stationId)!''}',
                        deptTypeList: deptTypeList,
                        isNotFixedList: arr1,
                        isFixedList: arr2,
                        isFixedPercentList: arr3,
                        moneyList: numberMoneyList,
                        percentList: numberPercentList,
                        numList: numList,
                        operateIdList: operateIdList
                    };

                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/hdg/station/update_station_distribution.htm',
                        dataType: 'json',
                        data: values,
                        success: function (json) {
                        <@app.json_jump/>
                            if (json.success) {
                                $.messager.alert('提示信息', '操作成功', 'info');
                            } else {
                                $.messager.alert('提示信息', json.message, 'info');
                                success = false;
                            }
                        },
                        error: function (text) {
                            $.messager.alert('提示信息', text, 'info');
                            success = false;
                        }
                    });
                    return success;
                };

                $('#station_distribution_parameter').data('ok', ok);

                var go = $('#station_distribution_parameter').data('ok')();
                if(go) {
                    //刷新或后退
                    $('#distribution').click();
                }
            }
        });
    });
</script>