<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${entity.id}">
            <input type="hidden" name="image1" value="${(entity.photoPath1)!''}" id="image1_${pid}">
            <input type="hidden" name="image2" value="${(entity.photoPath2)!''}" id="image2_${pid}">
            <input type="hidden" name="image3" value="${(entity.photoPath3)!''}" id="image3_${pid}">
            <input type="hidden" name="image4" value="${(entity.photoPath4)!''}" id="image4_${pid}">
            <input type="hidden" name="image5" value="${(entity.photoPath5)!''}" id="image5_${pid}">
            <input type="hidden" name="image6" value="${(entity.photoPath6)!''}" id="image6_${pid}">
            <fieldset>
                <legend>故障图片</legend>
                <div class="ui_table">

                    <table id="page_table_box_${pid}">
                        <tr>
                            <td align="right" valign="top" style="padding-left:20px;">图片1：</td>
                            <td>
                                <div class="portrait" filed_name="image1">
                                    <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.photoPath1?? && entity.photoPath1?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.photoPath1}<#else>${app.imagePath}/user.jpg</#if>" /></a>
                                </div>
                            </td>
                            <td align="right" valign="top" style="padding-left:20px;">图片2：</td>
                            <td>
                                <div class="portrait" filed_name="image2">
                                    <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.photoPath2?? && entity.photoPath2?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.photoPath2}<#else>${app.imagePath}/user.jpg</#if>" /></a>
                                </div>
                            </td>
                            <td align="right" valign="top" style="padding-left:20px;"></td>
                            <td>

                            </td>
                        </tr>
                        <tr>
                            <td align="right" valign="top" style="padding-left:20px;">图片3：</td>
                            <td>
                                <div class="portrait" filed_name="image3">
                                    <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.photoPath3?? && entity.photoPath3?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.photoPath3}<#else>${app.imagePath}/user.jpg</#if>" /></a>
                                </div>
                            </td>
                            <td align="right" valign="top" style="padding-left:20px;">图片4：</td>
                            <td>
                                <div class="portrait" filed_name="image4">
                                    <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.photoPath4?? && entity.photoPath4?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.photoPath4}<#else>${app.imagePath}/user.jpg</#if>" /></a>
                                </div>
                            </td>
                            <td align="right" valign="top" style="padding-left:20px;"></td>
                            <td>

                            </td>
                        </tr>
                        <tr>
                            <td align="right" valign="top" style="padding-left:20px;">图片5：</td>
                            <td>
                                <div class="portrait" filed_name="image5">
                                    <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.photoPath5?? && entity.photoPath5?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.photoPath5}<#else>${app.imagePath}/user.jpg</#if>" /></a>
                                </div>
                            </td>
                            <td align="right" valign="top" style="padding-left:20px;">图片6：</td>
                            <td>
                                <div class="portrait" filed_name="image6">
                                    <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.photoPath6?? && entity.photoPath6?length gt 0>${(controller.appConfig.staticUrl)!''}${entity.photoPath6}<#else>${app.imagePath}/user.jpg</#if>" /></a>
                                </div>
                            </td>
                            <td align="right" valign="top" style="padding-left:20px;"></td>
                            <td>

                            </td>
                        </tr>
                    </table>
                </div>
            </fieldset>
        </form>
    </div>
</div>


