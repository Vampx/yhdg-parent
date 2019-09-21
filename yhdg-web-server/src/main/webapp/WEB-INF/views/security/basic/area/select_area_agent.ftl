<#if pid??>
    <#assign selectorId=pid>
<#else>
    <#assign selectorId=''>
</#if>


<div class="select_city" id="area_selector_${selectorId}">
    <input type="text" id="area_text_${selectorId}" class="text" readonly
           value="${(areaText)!''}"
           <#if provinceName??>province_text="${provinceName}"</#if>
           <#if cityName??>city_text="${cityName}"</#if>
           <#if districtName??>district_text="${districtName}"</#if>
           onclick="$('#area_selector_${selectorId} .dialog').show();"/>
    <label><span class="icon" id="clear_${selectorId}"></span></label>
    <input type="hidden" id="province_id_${selectorId}"
           name="<#if namePrefix?? && namePrefix?length gt 0>${namePrefix}ProvinceId<#else>provinceId</#if>"
           value="${(provinceId)!''}">
    <input type="hidden" id="city_id_${selectorId}"
           name="<#if namePrefix?? && namePrefix?length gt 0>${namePrefix}CityId<#else>cityId</#if>"
           value="${(cityId)!''}">
    <input type="hidden" id="district_id_${selectorId}"
           name="<#if namePrefix?? && namePrefix?length gt 0>${namePrefix}DistrictId<#else>districtId</#if>"
           value="${(districtId)!''}">

    <div class="dialog city_box" style="top:31px; left:0; display:none;">
        <div class="dialog_title">
            <h3>请选择市区</h3>
            <a class="close" href="javascript:void(0)"><span class="icon"></span></a>
        </div>
        <div class="dialog_body">
            <script>
                $('#clear_${selectorId}').click(function () {
                    var areaText = $('#area_text_${selectorId}');
                    areaText.val('');
                    areaText.removeAttr('province_text');
                    areaText.removeAttr('city_text');
                    areaText.removeAttr('district_text');

                    $('#province_id_${selectorId}').val('');
                    $('#city_id_${selectorId}').val('');
                    $('#district_id_${selectorId}').val('');

                    $("#area_selector_${selectorId} .tab_nav li").removeClass("selected").eq(0).addClass("selected");
                    $("#area_selector_${selectorId} .tab_con .tab_item").hide().eq(0).show();
                });

                $(function () {
                    var i = 0;

                    function tab_nav(i) {
                        $("#area_selector_${selectorId} .tab_nav li").removeClass("selected").eq(i).addClass("selected");
                        $("#area_selector_${selectorId} .tab_con .tab_item").hide().eq(i).show();
                    }

                    function close_selector() {
                        $("#area_selector_${selectorId} .dialog").hide();
                    }

                    function next(id, tab, fn) {
                        $.post('${contextPath}/security/basic/area/children.htm', {
                            id: id
                        }, function (json) {
                        <@app.json_jump/>
                            if (json.success) {
                                if (json.data.length > 0) {
                                    var lines = [];
                                    for (var i = 0; i < json.data.length; i++) {
                                        lines.push('<a href="javascript:void(0)" area_id="' + json.data[i].id + '">' + json.data[i].areaName + '</a>');
                                    }
                                    $("#area_selector_${selectorId} .tab_con .tab_item:eq(" + tab + ") .city_list").html(lines.join(''));
                                    fn(lines);
                                } else {
                                    $("#area_selector_${selectorId} .tab_con .tab_item:eq(" + tab + ") .city_list").empty();
                                    $("#area_selector_${selectorId} .tab_con .tab_item:eq(" + tab + 1 + ") .city_list").empty();
                                    fn();
                                    close_selector();
                                }
                            }
                        }, 'json');
                    }

                    $(document).click(function (event) {
                    })

                    $("#area_selector_${selectorId} .tab_nav li").click(function (event) {
                        event.stopPropagation();
                        i = $(this).index();
                        if (i == 2) {
                            if ($('#province_id_${selectorId}').val() == '') {
                                $.messager.alert('提示信息', '请先选择省份');
                                return;
                            }
                        } else if (i == 3) {
                            if ($('#province_id_${selectorId}').val() == '') {
                                $.messager.alert('提示信息', '请先选择省份');
                                return;
                            }
                            if ($('#city_id_${selectorId}').val() == '') {
                                $.messager.alert('提示信息', '请先选择城市');
                                return;
                            }
                        }
                        tab_nav(i);
                    })
                    $("#area_selector_${selectorId} .dialog").click(function (event) {
                        event.stopPropagation();
                    })
                    $("#area_selector_${selectorId} .dialog .close").click(function () {
                        $("#area_selector_${selectorId} .dialog").hide();
                    })
                    $("#area_selector_${selectorId} .tab_con .tab_item:eq(0) .city_list").delegate('a', 'click', function () {
                        var me = $(this);
                        var areaId = me.attr('area_id'), areaLevel = me.attr('area_level'), areaName = me.html();
                        var parentId = me.attr('parent_id'), parentName = me.attr('parent_name');
                        var areaText = $('#area_text_${selectorId}');

                        if (areaLevel == 1) {
                            $('#province_id_${selectorId}').val(areaId);
                            areaText.val(areaName);
                            areaText.attr('province_text', areaName);
                            $('#province_id_${selectorId}').val(areaId);
                            $('#city_id_${selectorId}').val('');
                            $('#district_id_${selectorId}').val('');

                            next(areaId, 2, function (lines) {
                                if (lines) {
                                    tab_nav(2);
                                }
                            });
                        } else if (areaLevel == 2) {
                            $('#province_id_${selectorId}').val(parentId);
                            areaText.val(parentName + ' - ' + areaName);
                            areaText.attr('province_text', parentName);
                            areaText.attr('city_text', areaName);
                            $('#province_id_${selectorId}').val(parentId);
                            $('#city_id_${selectorId}').val(areaId);
                            $('#district_id_${selectorId}').val('');

                            next(areaId, 3, function (lines) {
                                if (lines) {
                                    tab_nav(3);
                                }
                            });
                        }

                    })
                    $("#area_selector_${selectorId} .tab_con .tab_item:eq(1) .city_list").delegate('a', 'click', function () {
                        var me = $(this);
                        var areaId = me.attr('area_id'), areaName = me.html();
                        $('#province_id_${selectorId}').val(areaId);
                        $('#city_id_${selectorId}').val('');
                        $('#district_id_${selectorId}').val('');
                        var areaText = $('#area_text_${selectorId}');
                        areaText.val(areaName);
                        areaText.attr('province_text', areaName);
                        areaText.removeAttr('city_text');
                        areaText.removeAttr('district_text');

                        next(areaId, 2, function (lines) {
                            if (lines) {
                                tab_nav(2);
                            }
                        });
                    })
                    $("#area_selector_${selectorId} .tab_con .tab_item:eq(2) .city_list").delegate('a', 'click', function () {
                        var me = $(this);
                        var areaId = me.attr('area_id'), areaName = me.html();
                        $('#city_id_${selectorId}').val(areaId);
                        $('#district_id_${selectorId}').val('');
                        var areaText = $('#area_text_${selectorId}');
                        areaText.attr('city_text', areaName);
                        areaText.val(areaText.attr('province_text') + ' - ' + areaName);
                        areaText.removeAttr('district_text');

                        next(areaId, 3, function (lines) {
                            if (lines) {
                                tab_nav(3);
                            }
                        });
                    })
                    $("#area_selector_${selectorId} .tab_con .tab_item:eq(3) .city_list").delegate('a', 'click', function () {
                        var me = $(this);
                        var areaId = me.attr('area_id'), areaName = me.html();
                        $('#district_id_${selectorId}').val(areaId);
                        var areaText = $('#area_text_${selectorId}');
                        areaText.attr('district_text', areaName);
                        areaText.val(areaText.attr('province_text') + ' - ' + areaText.attr('city_text') + ' - ' + areaName);
                        $("#area_selector_${selectorId} .dialog").hide();
                    })
                })
            </script>
            <ul class="tab_nav">
                <li class="selected">热门</li>
                <li>省份</li>
                <li>城市</li>
                <li>县区</li>
            </ul>
            <div class="tab_con">
                <div class="tab_item" style="display:block;">
                    <div class="city_list clearfix">
                        <a href="javascript:void(0)" area_id="110100" area_level="2" parent_id="110000" parent_name="北京">北京市</a>
                        <a href="javascript:void(0)" area_id="310100" area_level="2" parent_id="310000" parent_name="上海">上海市</a>
                        <a href="javascript:void(0)" area_id="440100" area_level="2" parent_id="440000" parent_name="广东省">广州市</a>
                        <a href="javascript:void(0)" area_id="330100" area_level="2" parent_id="330000" parent_name="浙江省">杭州市</a>
                        <a href="javascript:void(0)" area_id="320500" area_level="2" parent_id="320000" parent_name="江苏省">苏州市</a>
                        <a href="javascript:void(0)" area_id="120100" area_level="2" parent_id="120000" parent_name="天津">天津市</a>
                        <a href="javascript:void(0)" area_id="440300" area_level="2" parent_id="440000" parent_name="广东省">深圳市</a>
                        <a href="javascript:void(0)" area_id="510100" area_level="2" parent_id="510000" parent_name="四川省">成都市</a>
                        <a href="javascript:void(0)" area_id="420100" area_level="2" parent_id="420000" parent_name="湖北省">武汉市</a>
                    </div>
                </div>
                <div class="tab_item">
                    <dl class="city_list clearfix">
                        <dt>A - G</dt>
                        <dd>
                        <#list controller.appConfig.areaCache.rootList as area>
                            <#if area.letter == "a" || area.letter == "b" || area.letter == "c" || area.letter == "d" || area.letter == "e" || area.letter == "f" || area.letter == "g">
                                <a href="javascript:void(0)" area_id="${area.id}"
                                   area_level="${area.areaLevel}">${area.areaName}</a>
                            </#if>
                        </#list>
                        </dd>
                    </dl>
                    <dl class="city_list clearfix">
                        <dt>H - K</dt>
                        <dd>
                        <#list controller.appConfig.areaCache.rootList as area>
                            <#if area.letter == "h" || area.letter == "i" || area.letter == "j" || area.letter == "k">
                                <a href="javascript:void(0)" area_id="${area.id}"
                                   area_level="${area.areaLevel}">${area.areaName}</a>
                            </#if>
                        </#list>
                        </dd>
                    </dl>
                    <dl class="city_list clearfix">
                        <dt>L - S</dt>
                        <dd>
                        <#list controller.appConfig.areaCache.rootList as area>
                            <#if area.letter == "l" || area.letter == "m" || area.letter == "n" || area.letter == "o" || area.letter == "p" || area.letter == "q" || area.letter == "r" || area.letter == "s">
                                <a href="javascript:void(0)" area_id="${area.id}"
                                   area_level="${area.areaLevel}">${area.areaName}</a>
                            </#if>
                        </#list>
                        </dd>
                    </dl>
                    <dl class="city_list clearfix">
                        <dt>T - Z</dt>
                        <dd>
                        <#list controller.appConfig.areaCache.rootList as area>
                            <#if area.letter == "t" || area.letter == "u" || area.letter == "v" || area.letter == "w" || area.letter == "x" || area.letter == "y" || area.letter == "z">
                                <a href="javascript:void(0)" area_id="${area.id}"
                                   area_level="${area.areaLevel}">${area.areaName}</a>
                            </#if>
                        </#list>
                        </dd>
                    </dl>
                </div>
                <div class="tab_item">
                    <div class="city_list clearfix">
                    </div>
                </div>
                <div class="tab_item">
                    <div class="city_list clearfix">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>