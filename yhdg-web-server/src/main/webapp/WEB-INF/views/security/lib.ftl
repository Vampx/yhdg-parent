
<#assign jsPath='${contextPath}/static/js' >
<#assign imagePath='${contextPath}/static/images' >
<#assign cssPath='${contextPath}/static/css' >
<#assign fontAwesomeCssPath='${contextPath}/static/font-awesome/css' >
<#assign toolPath='${contextPath}/static/tool' >
<#assign gridRowHeight='height:2.8em;'>
<#macro has_oper perm_code>
    <#if Session['SESSION_KEY_USER'].hasOper(perm_code)><#nested></#if>
</#macro>
<#macro has_any_oper perm_codes>
    <#if Session['SESSION_KEY_USER'].hasAnyOper(perm_codes)><#nested></#if>
</#macro>
<#macro has_any_module perm_code>
    <#if Session['SESSION_KEY_USER'].hasAnyModule(perm_code)><#nested></#if>
</#macro>
<#function contain_oper perm_code>
    <#return Session['SESSION_KEY_USER'].hasOper(perm_code)>
</#function>
<#function has_multi_oper perm_code>
    <#return Session['SESSION_KEY_USER'].containOperCount(perm_code) gt 1>
</#function>

<#macro json_jump json="json">
if(${json}.timeout) { document.location.href = App.loginUrl; return false; }
</#macro>

<#macro html>
<!doctype html>
<html>
    <#nested>
</html>
</#macro>

<#macro head>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="icon" href="${imagePath}/favicon_zl.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="${imagePath}/favicon_zl.ico"
          type="image/x-icon"/>
    <script type="text/javascript" src="${toolPath}/jquery/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="${toolPath}/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${toolPath}/jquery/jquery.json-2.4.js"></script>
    <script type="text/javascript" src="${toolPath}/jquery/jquery.nanoscroller.js"></script>
    <script type="text/javascript" src="${toolPath}/jquery/qrcode.min.js"></script>
    <script type="text/javascript" src="${toolPath}/jquery-easyui-1.3.6/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${toolPath}/jquery-easyui-1.3.6/easyui-combotreezj.js"></script>
    <script type="text/javascript" src="${toolPath}/jquery-easyui-1.3.6/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${toolPath}/bootstrap-typeahead-1.2.2/bootstrap-typeahead.js"></script>
    <script type="text/javascript" src="${toolPath}/echarts/echarts.min.js"></script>
    <link href="${toolPath}/jquery-easyui-1.3.6/themes/gray/easyui.css" rel="stylesheet" type="text/css"/>
    <link href="${toolPath}/jquery-easyui-1.3.6/themes/icon.css" rel="stylesheet" type="text/css"/>
    <link href="${fontAwesomeCssPath}/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="${cssPath}/login.css" rel="stylesheet" type="text/css"/>
    <link href="${cssPath}/index.css" rel="stylesheet" type="text/css"/>
    <link href="${cssPath}/battery-detail.css" rel="stylesheet" type="text/css"/>
    <link href="${cssPath}/cabinet_list.css?v=2" rel="stylesheet" type="text/css"/>
    <link href="${cssPath}/global.css?v=3" rel="stylesheet" type="text/css"/>
    <link href="${cssPath}/main.css" rel="stylesheet" type="text/css"/>
    <link href="${cssPath}/nanoscroller.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${jsPath}/app-template.js"></script>
    <script type="text/javascript" src="${contextPath}/static/tool/My97DatePicker/WdatePicker.js"></script>
    <#--<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=${controller.appConfig.mapKey}"></script>-->

    <title>${(controller.appConfig.pageTitle)!''}</title>
    <script type="text/javascript">
        jQuery.ajaxSettings.traditional = true;
        var App = {
            contextPath: '${contextPath}',
            imagePath: '${imagePath}',
            licenceUrl: '${contextPath}/security/licence/index.htm',
            loginUrl: '${contextPath}/security/main/index.htm',
            staticUrl: '${controller.appConfig.staticUrl}',
            sessionUser: {
                id: '${(Session['SESSION_KEY_USER'].id)!''}',
                realName: '${(Session['SESSION_KEY_USER'].realName)!''}'
            }
        };
        function jump(json) {
            if (json.timeout) {
                document.location.href = App.loginUrl;
                return false;
            }
            return true;
        }
        function password() {
            App.dialog.show({url: '${contextPath}/security/main/password.htm', drag: true})
        }
        function gridRowStyler() {
            return '${app.gridRowHeight}';
        }

    </script>
    <script type="text/javascript" src="${jsPath}/common.js"></script>
    <#nested>
</head>
</#macro>

<#macro body clazz=''>
<body>
    <#nested>
<script>
    $(function () {
        /*   faultLog();
           var ref = setInterval(function () {
               faultLog();
           }, 10000);*/

        $(".subnav .subnav_list ul .dropdown").click(function () {

            if (!$(this).hasClass("show")) {
                if ($(this).siblings("li").find("a").hasClass("selected")) {
                    $(this).siblings("li").find("a.selected").parents(".dropdown").addClass("selected")
                }
                $(this).addClass("show").siblings().removeClass("show")
            }

            if ($(this).hasClass("selected")) {
                $(this).addClass("show").removeClass("selected");
            }
        });
    });

    function faultLog() {
        <#if Session['SESSION_KEY_USER']??>
            $.post("${contextPath}/security/hdg/fault_log/findCount.htm", {},
                    function (json) {
                        if (json.success) {
                            var faultLogCount = json.data;
                            $("#faultLog").empty();
                            $("#faultLog").removeClass("label");
                            if (faultLogCount != 0) {
                                $("#faultLog").append(faultLogCount);
                                $("#faultLog").addClass("label");
                            }
                        }
                    }, 'json'
            );
        </#if>
    }
    function password() {
        App.dialog.show({
            css: 'width:528px;height:230px;overflow:visible;',
            title: '修改密码',
            href: "${contextPath}/security/main/password.htm"
        });
    }

    $(document).click(function () {
        $(".user_menu").hide();
    });
    $(".user_info").click(function (event) {
        event.stopPropagation();
        if ($(".user_menu").is(":hidden")) {
            $(".user_menu").show();
        } else {
            $(".user_menu").hide();
        }
    });

    $('#switch_menu').click(function () {
        $('.left_bar .subnav').hide();
        $('.content').css("left", 0);
    });
</script>
</body>
</#macro>

<#macro container>
<div class="container">
    <#nested>
</div>
</#macro>

<#macro banner >

<div class="header">
    <div class="header_top">
        <div class="t_left">
            <img  style="width: auto !important;" src="${imagePath}/img/logo.png" />
            <div>
                <span>这锂换电</span>
                <span>V1.0.0</span>
            </div>
        </div>
        <div class="t_right">
            <div class="tr_left">
                <div>
                    <i>
                        <img style="width: auto !important;" src="${imagePath}/img/battery_set.png" />
                    </i>
                    <a href="${contextPath}/security/main/module.htm?moduleId=3&url=${contextPath}/security/hdg/fault_log/index.htm?grade=0">
                        &nbsp;故障消息
                    </a>
                </div>
                <div>
                    <i>
                        <img  style="width: auto !important;" src="${imagePath}/img/login/msg.png"/>
                    <#--<b>22</b>-->
                    </i>
                    &nbsp;消息中心
                </div>
                <div>
                    <i>
                        <img  style="width: auto !important;" src="${imagePath}/img/login/setting.png"/>
                    <#--<b>22</b>-->
                    </i>
                    &nbsp;账户设置
                </div>
            </div>
            <div class="user_info">
                <i><img src="<#if Session['SESSION_KEY_USER'].portrait?? && Session['SESSION_KEY_USER'].portrait?length gt 0>${Session['SESSION_KEY_USER'].portrait}<#else>${imagePath}/user.jpg</#if>"/></i>
                <b>欢迎您，${Session['SESSION_KEY_USER'].username}<img style="width: auto !important;" src="${imagePath}/img/yousan.png"  /></b>

                <div class="user_menu">
                    <a href="javascript:password()">修改密码</a>
                    <a href="${contextPath}/security/main/logout.htm">退出</a>
                </div>
            </div>
        </div>

    </div>
    <@menu_index/>
</div>
</#macro>

<#macro menu_index>
<div class="index_menu">
    <div class="m_left">
        <a class="<#if Session['SESSION_KEY_USER'].moduleId == 0>selected</#if>"
           href="${contextPath}/security/main/module.htm?moduleId=0&url=${contextPath}/security/main/index.htm">
                <span class="active" id="hide_home">
                    <#--<span class="icon icon_home" ></span>-->
                        <span class="text">主页</span>
                </span>
        </a>

        <@app.has_any_oper perm_codes=['basic.Customer:list','basic.CustomerDepositOrder:list',
        'basic.CustomerManualAuthRecord:list',
        'basic.PublicNotic:list',
        'basic.SystemBatteryType:list','basic.Weixinma:list',
        'hdg.BatteryCellModel:list','hdg.BatteryCellFormat:list', 'hdg.BatteryCell:list', 'hdg.BatteryFormat:list', 'hdg.BatteryCheck:list',
        'hdg.CabinetInstallRecord:list','hdg.BatteryTypeIncomeRatio:list','hdg.CabinetIncomeTemplate:list',
        'basic.CustomerGuide:list','basic.RichText:list',
        'basic.AgentSystemConfig:list','hdg.ReliefStation:list','basic.Feedback:list','basic.CustomerDepositGift:list',
        'basic.PlatformAccount:list','basic.Partner:list','basic.Weixinmp:list', 'basic.Alipayfw:list', 'basic.Phoneapp:list',
        'basic.Withdraw:list']>
            <#if contain_cper('basic.Customer:list')>
                <#assign url="/security/basic/customer/index.htm">
            <#elseif  contain_cper('basic.CustomerDepositOrder:list')>
                <#assign url="/security/basic/customer_deposit_order/index.htm">
            <#elseif  contain_cper('basic.CustomerManualAuthRecord:list')>
                <#assign url="/security/basic/customer_manual_auth_record/index.htm">
            <#elseif  contain_cper('basic.PublicNotic:list')>
                <#assign url="/security/basic/public_notice/index.htm">
            <#elseif  contain_cper('basic.Agent:list')>
                <#assign url="/security/basic/agent/index.htm">
            <#elseif  contain_cper('basic.UserAgent:list')>
                <#assign url="/security/basic/user_agent/index.htm">
            <#elseif  contain_cper('basic.SystemBatteryType:list')>
                <#assign url="/security/basic/system_battery_type/index.htm">
            <#elseif  contain_cper('hdg.BatteryCellModel:list')>
                <#assign url="/security/hdg/battery_cell_model/index.htm">
            <#elseif  contain_cper('hdg.BatteryCellFormat:list')>
                <#assign url="/security/hdg/battery_cell_format/index.htm">
            <#elseif  contain_cper('hdg.BatteryCell:list')>
                <#assign url="/security/hdg/battery_cell/index.htm">
            <#elseif  contain_cper('hdg.BatteryFormat:list')>
                <#assign url="/security/hdg/battery_format/index.htm">
            <#elseif  contain_cper('hdg.BatteryCheck:list')>
                <#assign url="/security/hdg/battery_check/index.htm">
            <#elseif  contain_cper('hdg.CabinetInstallRecord:list')>
                <#assign url="/security/hdg/cabinet_install_record/index.htm">
            <#elseif  contain_cper('hdg.BatteryTypeIncomeRatio:list')>
                <#assign url="/security/hdg/battery_type_income_ratio/index.htm">
            <#elseif  contain_cper('hdg.CabinetIncomeTemplate:list')>
                <#assign url="/security/hdg/cabinet_income_template/index.htm">
            <#elseif  contain_cper('basic.CustomerGuide:list')>
                <#assign url="/security/basic/customer_guide/index.htm">
            <#elseif  contain_cper('basic.RichText:list')>
                <#assign url="/security/basic/rich_text/index.htm">
            <#elseif  contain_cper('basic.AgentSystemConfig:list')>
                <#assign url="/security/basic/agent_system_config/index.htm">
            <#elseif  contain_cper('hdg.ReliefStation:list')>
                <#assign url="/security/hdg/relief_station/index.htm">
            <#elseif  contain_cper('basic.Feedback:list')>
                <#assign url="/security/basic/feedback/index.htm">
            <#elseif  contain_cper('basic.CustomerDepositGift:list')>
                <#assign url="/security/basic/customer_deposit_gift/index.htm">
            <#elseif  contain_cper('basic.PlatformAccount:list')>
                <#assign url="/security/basic/platform_account/index.htm">
            <#elseif  contain_cper('basic.Partner:list')>
                <#assign url="/security/basic/partner/index.htm">
            <#elseif  contain_cper('basic.Weixinmp:list')>
                <#assign url="/security/basic/weixinmp/index.htm">
            <#elseif  contain_cper('basic.Alipayfw:list')>
                <#assign url="/security/basic/alipayfw/index.htm">
            <#elseif  contain_cper('basic.Phoneapp:list')>
                <#assign url="/security/basic/phoneapp/index.htm">
            <#elseif  contain_cper('basic.Withdraw:list')>
                <#assign url="/security/basic/withdraw/index.htm">
            <#elseif  contain_cper('basic.Weixinma:list')>
                <#assign url="/security/basic/weixinma/index.htm">
            </#if>
            <li>
                <a class="<#if Session['SESSION_KEY_USER'].moduleId == 1>selected</#if>"
                   href="${contextPath}/security/main/module.htm?moduleId=1&url=${contextPath}${url}">
                         <span>
                        <#--<span class="icon icon_operator"></span>-->
                            <span class="text">平台</span>
                                </span>
                </a>
            </li>
        </@app.has_any_oper>

        <@app.has_any_oper perm_codes=['basic.AgentAgent:list', 'basic.AgentUser:list', 'basic.AgentRole:list', 'basic.AgentDepositOrder:list', 'basic.AgentForegift:list',
        'hdg.Shop:list','hdg.ShopUser:list', 'basic.ShopRole:list', 'hdg.ShopStoreBattery:list', 'hdg.ShopBatteryLog:list',
        'hdg.Estate:list', 'hdg.EstateUser:list', 'hdg.CabinetDegreeInput:list',
        'basic.AgentCompany:list', 'basic.AgentCompanyUser:list', 'basic.AgentCompanyRole:list',
        'basic.AgentPublicNotice:list',
        'hdg.CabinetAddressCorrection:list','hdg.CabinetAddressCorrectionExemptReview:list',
        'basic.Laxin:list', 'basic.LaxinRecord:list', 'basic.LaxinPayOrder:list', 'basic.LaxinCustomer:list', 'basic.LaxinSetting:list',
        'basic.CustomerWhitelist:list', 'basic.WithdrawAgent:list']>
            <#if  contain_cper('basic.AgentAgent:list')>
                <#assign url="/security/basic/agent_agent/index.htm">
            <#elseif  contain_cper('basic.AgentUser:list')>
                <#assign url="/security/basic/agent_user/index.htm">
            <#elseif  contain_cper('basic.AgentRole:list')>
                <#assign url="/security/basic/agent_role/index.htm">
            <#elseif  contain_cper('basic.AgentDepositOrder:list')>
                <#assign url="/security/basic/agent_deposit_order/index.htm">
            <#elseif  contain_cper('basic.AgentForegift:list')>
                <#assign url="/security/basic/agent_foregift/index.htm">
            <#elseif  contain_cper('hdg.Shop:list')>
                <#assign url="/security/hdg/shop/index.htm">
            <#elseif  contain_cper('hdg.ShopUser:list')>
                <#assign url="/security/hdg/shop_user/index.htm">
            <#elseif  contain_cper('basic.ShopRole:list')>
                <#assign url="/security/basic/shop_role/index.htm">
            <#elseif  contain_cper('hdg.ShopStoreBattery:list')>
                <#assign url="/security/hdg/shop_store_battery/index.htm">
            <#elseif  contain_cper('hdg.ShopBatteryLog:list')>
                <#assign url="/security/hdg/shop_battery_log/index.htm">
            <#elseif  contain_cper('hdg.Estate:list')>
                <#assign url="/security/hdg/estate/index.htm">
            <#elseif  contain_cper('hdg.EstateUser:list')>
                <#assign url="/security/hdg/estate_user/index.htm">
            <#elseif  contain_cper('hdg.CabinetDegreeInput:list')>
                <#assign url="/security/hdg/cabinet_degree_input/index.htm">
            <#elseif  contain_cper('basic.AgentCompany:list')>
                <#assign url="/security/basic/agent_company/index.htm">
            <#elseif  contain_cper('basic.AgentCompanyUser:list')>
                <#assign url="/security/basic/agent_company_user/index.htm">
            <#elseif  contain_cper('basic.AgentCompanyRole:list')>
                <#assign url="/security/basic/agent_company_role/index.htm">
            <#elseif  contain_cper('basic.AgentPublicNotice:list')>
                <#assign url="/security/basic/agent_public_notice/index.htm">
            <#elseif  contain_cper('hdg.CabinetAddressCorrection:list')>
                <#assign url="/security/hdg/cabinet_address_correction/index.htm">
            <#elseif  contain_cper('hdg.CabinetAddressCorrectionExemptReview:list')>
                <#assign url="/security/hdg/cabinet_address_correction_exempt_review/index.htm">
            <#elseif  contain_cper('basic.Laxin:list')>
                <#assign url="/security/basic/laxin/index.htm">
            <#elseif  contain_cper('basic.LaxinRecord:list')>
                <#assign url="/security/basic/laxin_record/index.htm">
            <#elseif  contain_cper('basic.LaxinPayOrder:list')>
                <#assign url="/security/basic/laxin_pay_order/index.htm">
            <#elseif  contain_cper('basic.LaxinCustomer:list')>
                <#assign url="/security/basic/laxin_customer/index.htm">
            <#elseif  contain_cper('basic.LaxinSetting:list')>
                <#assign url="/security/basic/laxin_setting/index.htm">
            <#elseif  contain_cper('basic.CustomerWhitelist:list')>
                <#assign url="/security/basic/customer_whitelist/index.htm">
            <#elseif  contain_cper('basic.WithdrawAgent:list')>
                <#assign url="/security/basic/withdraw_agent/index.htm">
            </#if>
            <a class="<#if Session['SESSION_KEY_USER'].moduleId == 2>selected</#if>"
               href="${contextPath}/security/main/module.htm?moduleId=2&url=${contextPath}${url}">
                    <span>
                    <#--<span class="icon icon_member"></span>-->
                        <span class="text">运营商</span>
                         </span>
            </a>
        </@app.has_any_oper>

        <@app.has_any_oper perm_codes=['basic.AgentBatteryType:list',
        'hdg.Battery:list',
        'hdg.Cabinet:list',
        'yms.Terminal:list',
        'hdg.PacketPeriodActivity:list',
        'hdg.ExchangeWhitelist:list',
        'basic.CustomerForegiftOrder:list','hdg.PacketPeriodOrder:list','hdg.BatteryOrder:list','hdg.BackBatteryOrder:list','hdg.InsuranceOrder:list','hdg.BespeakOrder:list','basic.CustomerMultiOrder:list',
        'hdg.FaultLog:list',
        'hdg.FaultFeedback:list',
        'hdg.ExchangeRefund:list',
        'hdg.VipPrice:list',
        'basic.CustomerCouponTicketGift:list', 'basic.CustomerCouponTicket:list',
        'hdg.ExchangeInstallmentSetting:list', 'basic.CustomerInstallmentRecord:list']>
            <#if  contain_cper('basic.AgentBatteryType:list')>
                <#assign url="/security/basic/agent_battery_type/index.htm">
            <#elseif  contain_cper('hdg.Battery:list')>
                <#assign url="/security/hdg/battery/index.htm">
            <#elseif  contain_cper('hdg.Cabinet:list')>
                <#assign url="/security/hdg/cabinet/index.htm">
            <#elseif  contain_cper('hdg.PacketPeriodActivity:list')>
                <#assign url="/security/hdg/packet_period_activity/index.htm">
            <#elseif  contain_cper('hdg.ExchangeWhitelist:list')>
                <#assign url="/security/hdg/exchange_whitelist/index.htm">
            <#elseif  contain_cper('basic.CustomerForegiftOrder:list')>
                <#assign url="/security/basic/customer_foregift_order/index.htm">
            <#elseif  contain_cper('hdg.PacketPeriodOrder:list')>
                <#assign url="/security/hdg/packet_period_order/index.htm">
            <#elseif  contain_cper('hdg.BatteryOrder:list')>
                <#assign url="/security/hdg/battery_order/index.htm">
            <#elseif  contain_cper('hdg.BackBatteryOrder:list')>
                <#assign url="/security/hdg/back_battery_order/index.htm">
            <#elseif  contain_cper('hdg.InsuranceOrder:list')>
                <#assign url="/security/hdg/insurance_order/index.htm">
            <#elseif  contain_cper('hdg.BespeakOrder:list')>
                <#assign url="/security/hdg/bespeak_order/index.htm">
            <#elseif  contain_cper('basic.CustomerMultiOrder:list')>
                <#assign url="/security/basic/customer_multi_order/index.htm">
            <#elseif  contain_cper('hdg.FaultLog:list')>
                <#assign url="/security/hdg/fault_log/index.htm">
            <#elseif  contain_cper('hdg.FaultFeedback:list')>
                <#assign url="/security/hdg/fault_feedback/index.htm">
            <#elseif  contain_cper('hdg.ExchangeRefund:list')>
                <#assign url="/security/hdg/exchange_refund/index.htm">
            <#elseif  contain_cper('hdg.VipPrice:list')>
                <#assign url="/security/hdg/vip_price/index.htm">
            <#elseif  contain_cper('basic.CustomerCouponTicketGift:list')>
                <#assign url="/security/basic/customer_coupon_ticket_gift/index.htm">
            <#elseif  contain_cper('basic.CustomerCouponTicket:list')>
                <#assign url="/security/basic/customer_coupon_ticket/index.htm">
            <#elseif  contain_cper('hdg.ExchangeInstallmentSetting:list')>
                <#assign url="/security/hdg/exchange_installment_setting/index.htm">
            <#elseif  contain_cper('basic.CustomerInstallmentRecord:list')>
                <#assign url="/security/basic/customer_installment_record/index.htm">
            </#if>
            <li>
                <a class="<#if Session['SESSION_KEY_USER'].moduleId == 3>selected</#if>"
                   href="${contextPath}/security/main/module.htm?moduleId=3&url=${contextPath}${url}">
                <span >
                <span class="text">换电</span>
                </span>
                </a>
            </li>
        </@app.has_any_oper>

        <@app.has_any_oper perm_codes=['zd.RentBatteryType:list',
        'zd.Battery:list',
        'zd.RentPeriodActivity:list',
        'zd.RentForegiftOrder:list', 'zd.RentPeriodOrder:list', 'zd.RentInsuranceOrder:list', 'zd.RentOrder:list', 'zd.CustomerMultiOrder:list',
        'basic.CustomerCouponTicketGiftRent:list', 'basic.CustomerCouponTicketRent:list',
        'zd.RentRefund:list',
        'zd.VipRentPrice:list',
        'zd.RentInstallmentSetting:list','zd.ZdCustomerInstallmentRecord:list']>
            <#if  contain_cper('zd.RentBatteryType:list')>
                <#assign url="/security/zd/rent_battery_type/index.htm">
            <#elseif  contain_cper('zd.Battery:list')>
                <#assign url="/security/zd/battery/index.htm">
            <#elseif  contain_cper('zd.RentPeriodActivity:list')>
                <#assign url="/security/zd/rent_period_activity/index.htm">
            <#elseif  contain_cper('zd.RentForegiftOrder:list')>
                <#assign url="/security/zd/rent_foregift_order/index.htm">
            <#elseif  contain_cper('zd.RentPeriodOrder:list')>
                <#assign url="/security/zd/rent_period_order/index.htm">
            <#elseif  contain_cper('zd.RentInsuranceOrder:list')>
                <#assign url="/security/zd/rent_insurance_order/index.htm">
            <#elseif  contain_cper('zd.RentOrder:list')>
                <#assign url="/security/zd/rent_order/index.htm">
            <#elseif  contain_cper('zd.RentOrder:list')>
                <#assign url="/security/zd/rent_order/index.htm">
            <#elseif  contain_cper('zd.CustomerMultiOrder:list')>
                <#assign url="/security/zd/zd_customer_multi_order/index.htm">
            <#elseif  contain_cper('zd.RentRefund:list')>
                <#assign url="/security/zd/rent_refund/index.htm">
            <#elseif  contain_cper('zd.VipRentPrice:list')>
                <#assign url="/security/zd/vip_rent_price/index.htm">
            <#elseif  contain_cper('zd.RentInstallmentSetting:list')>
                <#assign url="/security/zd/rent_installment_setting/index.htm">
            <#elseif  contain_cper('zd.ZdCustomerInstallmentRecord:list')>
                <#assign url="/security/zd/zd_customer_installment_record/index.htm">
            </#if>
            <li>
                <a class="<#if Session['SESSION_KEY_USER'].moduleId == 4>selected</#if>"
                   href="${contextPath}/security/main/module.htm?moduleId=4&url=${contextPath}${url}">
                <span >
                <span class="text">租电</span>
                </span>
                </a>
            </li>
        </@app.has_any_oper>
        <@app.has_any_oper perm_codes=['zc.VehicleModel:list', 'zc.Vehicle:list','zc.VehicleOrder:list','zc.GroupOrder:list', 'zc.VehicleForegiftOrder:list', 'zc.VehiclePeriodOrder:list', 'zc.ShopInventoryBattery:list', 'zc.ShopPriceSetting:list', 'zc.CustomerVehicleInfo:list', 'zc.ShopVehicle:list', 'zc.VehicleRentOrder:list',
        'zc.VehicleCustomerForegiftOrder:list', 'zc.VehiclePacketPeriodOrder:list', 'zc.VehicleBatteryOrder:list', 'zc.VehicleBespeakOrder:list', 'zc.VehicleRentForegiftOrder:list', 'zc.VehicleRentPeriodOrder:list']>
            <#if  contain_cper('zc.VehicleModel:list')>
                <#assign url="/security/zc/vehicle_model/index.htm">
            <#elseif  contain_cper('zc.Vehicle:list')>
                <#assign url="/security/zc/vehicle/index.htm">
            <#elseif  contain_cper('zc.ShopPriceSetting:list')>
                <#assign url="/security/zc/shop_price_setting/index.htm">
            <#elseif  contain_cper('zc.ShopStoreVehicle:list')>
                <#assign url="/security/zc/shop_store_vehicle/index.htm">
            <#elseif  contain_cper('zc.ShopRentCar:list')>
                <#assign url="/security/zc/shop_rent_car/index.htm">
            <#elseif  contain_cper('zc.ShopInventoryBattery:list')>
                <#assign url="/security/zc/shop_inventory_battery/index.htm">
            <#elseif  contain_cper('zc.VehicleOrder:list')>
                <#assign url="/security/zc/vehicle_order/index.htm">
            <#elseif  contain_cper('zc.GroupOrder:list')>
                <#assign url="/security/zc/group_order/index.htm">
            <#elseif  contain_cper('zc.VehicleForegiftOrder:list')>
                <#assign url="/security/zc/vehicle_foregift_order/index.htm">
            <#elseif  contain_cper('zc.RentPeriodOrder:list')>
                <#assign url="/security/zc/rent_period_order/index.htm">
            <#elseif  contain_cper('zc.CustomerVehicleInfo:list')>
                <#assign url="/security/zc/customer_vehicle_info/index.htm">
            <#elseif  contain_cper('zc.ZcCustomerMultiOrder:list')>
                <#assign url="/security/zc/zc_customer_multi_order/index.htm">
            <#elseif  contain_cper('zc.VehiclePeriodOrder:list')>
                <#assign url="/security/zc/vehicle_period_order/index.htm">
            <#elseif  contain_cper('zc.ShopVehicle:list')>
                <#assign url="/security/zc/shop_vehicle/index.htm">
            <#elseif  contain_cper('basic.CustomerMultiOrder:list')>
                <#assign url="/security/basic/customer_multi_order/index.htm">
            <#elseif  contain_cper('zc.VehicleCustomerForegiftOrder:list')>
                <#assign url="/security/zc/vehicle_customer_foregift_order/index.htm">
            <#elseif  contain_cper('zc.VehiclePacketPeriodOrder:list')>
                <#assign url="/security/zc/vehicle_packet_period_order/index.htm">
            <#elseif  contain_cper('zc.VehicleBatteryOrder:list')>
                <#assign url="/security/zc/vehicle_battery_order/index.htm">
            <#elseif  contain_cper('zc.VehicleBespeakOrder:list')>
                <#assign url="/security/zc/vehicle_bespeak_order/index.htm">
            <#elseif  contain_cper('zc.VehicleRentForegiftOrder:list')>
                <#assign url="/security/zc/vehicle_rent_foregift_order/index.htm">
            <#elseif  contain_cper('zc.VehicleRentPeriodOrder:list')>
                <#assign url="/security/zc/vehicle_rent_period_order/index.htm">
            <#elseif  contain_cper('zc.VehicleRentOrder:list')>
                <#assign url="/security/zc/vehicle_rent_order/index.htm">

            </#if>
            <li>
                <a class="<#if Session['SESSION_KEY_USER'].moduleId == 5>selected</#if>"
                   href="${contextPath}/security/main/module.htm?moduleId=5&url=${contextPath}${url}">
                <span >
                <span class="text">租车</span>
                </span>
                </a>
            </li>
        </@app.has_any_oper>

        <@app.has_any_oper perm_codes=['basic.SmsConfig:list','basic.MobileMessageTemplate:list','basic.MobileMessage:list','basic.BatchMobileMessage:list',
        'basic.PushMessageTemplate:list','basic.PushMessage:list','basic.CustomerNoticeMessage:list','basic.UserNoticeMessage:list',
        'basic.MpPushMessageTemplate:list','basic.WeixinmpTemplateMessage:list','basic.BatchWeixinmpTemplateMessage:list',
        'basic.FwPushMessageTemplate:list','basic.AlipayfwTemplateMessage:list',
        'basic.VoiceConfig:list', 'basic.VoiceMessageTemplate:list', 'basic.VoiceMessage:list']>
            <#if  contain_cper('basic.SmsConfig:list')>
                <#assign url="/security/basic/sms_config/index.htm">
            <#elseif  contain_cper('basic.MobileMessageTemplate:list')>
                <#assign url="/security/basic/mobile_message_template/index.htm">
            <#elseif  contain_cper('basic.MobileMessage:list')>
                <#assign url="/security/basic/mobile_message/index.htm">
            <#elseif  contain_cper('basic.BatchMobileMessage:list')>
                <#assign url="/security/basic/batch_mobile_message/index.htm">
            <#elseif  contain_cper('basic.PushMessageTemplate:list')>
                <#assign url="/security/basic/push_message_template/index.htm">
            <#elseif  contain_cper('basic.PushMessage:list')>
                <#assign url="/security/basic/push_message/index.htm">
            <#elseif  contain_cper('basic.CustomerNoticeMessage:list')>
                <#assign url="/security/basic/customer_notice_message/index.htm">
            <#elseif  contain_cper('basic.UserNoticeMessage:list')>
                <#assign url="/security/basic/user_notice_message/index.htm">
            <#elseif  contain_cper('basic.MpPushMessageTemplate:list')>
                <#assign url="/security/basic/mp_push_message_template/index.htm">
            <#elseif  contain_cper('basic.WeixinmpTemplateMessage:list')>
                <#assign url="/security/basic/weixinmp_template_message/index.htm">
            <#elseif  contain_cper('basic.BatchWeixinmpTemplateMessage:list')>
                <#assign url="/security/basic/batch_weixinmp_template_message/index.htm">
            <#elseif  contain_cper('basic.FwPushMessageTemplate:list')>
                <#assign url="/security/basic/fw_push_message_template/index.htm">
            <#elseif  contain_cper('basic.AlipayfwTemplateMessage:list')>
                <#assign url="/security/basic/alipayfw_template_message/index.htm">
            <#elseif  contain_cper('basic.VoiceConfig:list')>
                <#assign url="/security/basic/voice_config/index.htm">
            <#elseif  contain_cper('basic.VoiceMessageTemplate:list')>
                <#assign url="/security/basic/voice_message_template/index.htm">
            <#elseif  contain_cper('basic.VoiceMessage:list')>
                <#assign url="/security/basic/voice_message/index.htm">
            </#if>
            <li>
                <a class="<#if Session['SESSION_KEY_USER'].moduleId == 6>selected</#if>"
                   href="${contextPath}/security/main/module.htm?moduleId=6&url=${contextPath}${url}">
                <span data-id="4">
                <span class="text">消息</span>
                </span>
                </a>
            </li>
        </@app.has_any_oper>

        <@app.has_any_oper perm_codes=['basic.BalanceRecord:list','hdg.AgentMaterialDayStats:list',
        'basic.WeixinPayOrder:list','basic.AlipayPayOrder:list','basic.WeixinmpPayOrder:list','basic.AlipayfwPayOrder:list',
        'basic.IdCardAuthRecord:list','basic.WeixinmaPayOrder:list',
        'basic.PartnerInOutCash:list','basic.AgentDayInOutMoney:list']>
            <#if  contain_cper('basic.BalanceRecord:list')>
                <#assign url="/security/basic/balance_record/index.htm">
            <#elseif  contain_cper('hdg.AgentMaterialDayStats:list')>
                <#assign url="/security/hdg/agent_material_day_stats/index.htm">
            <#elseif  contain_cper('basic.WeixinPayOrder:list')>
                <#assign url="/security/basic/weixin_pay_order/index.htm">
            <#elseif  contain_cper('basic.AlipayPayOrder:list')>
                <#assign url="/security/basic/alipay_pay_order/index.htm">
            <#elseif  contain_cper('basic.WeixinmpPayOrder:list')>
                <#assign url="/security/basic/weixinmp_pay_order/index.htm">
            <#elseif  contain_cper('basic.WeixinmaPayOrder:list')>
                <#assign url="/security/basic/weixinma_pay_order/index.htm">
            <#elseif  contain_cper('basic.AlipayfwPayOrder:list')>
                <#assign url="/security/basic/alipayfw_pay_order/index.htm">
            <#elseif  contain_cper('basic.IdCardAuthRecord:list')>
                <#assign url="/security/basic/id_card_auth_record/index.htm">
            <#elseif  contain_cper('basic.PartnerInOutCash:list')>
                <#assign url="/security/basic/partner_in_out_cash/index.htm">
            <#elseif  contain_cper('basic.AgentDayInOutMoney:list')>
                <#assign url="/security/basic/agent_day_in_out_money/index.htm">
            </#if>
            <li>
                <a class="<#if Session['SESSION_KEY_USER'].moduleId == 7>selected</#if>"
                   href="${contextPath}/security/main/module.htm?moduleId=7&url=${contextPath}${url}">
                <span data-id="5">
                <span class="text">结算</span>
                </span>
                </a>
            </li>
        </@app.has_any_oper>

        <@app.has_any_oper perm_codes=['hdg.AgentDayStats:list','hdg.AgentMonthStats:list', 'hdg.AgentTotalStats:list',
        'hdg.ShopDayStats:list', 'hdg.ShopTotalStats:list',
        'basic.AgentCompanyDayStats:list', 'basic.AgentCompanyTotalStats:list',
        'hdg.CabinetDayStats:list','hdg.CabinetMonthStats:list', 'hdg.CabinetTotalStats:list', 'hdg.CabinetBoxProhibitStats:list', 'hdg.BattaryAbnormalStats:list', 'hdg.PacketPeriodOrderSoonExpireStats:list',
        'hdg.CustomerDayStats:list','hdg.PlatformDayStats:list','hdg.PlatformMonthStats:list']>
            <#if contain_cper('hdg.AgentDayStats:list')>
                <#assign url="/security/hdg/agent_day_stats/index.htm">
            <#elseif  contain_cper('hdg.AgentMonthStats:list')>
                <#assign url="/security/hdg/agent_month_stats/index.htm">
            <#elseif contain_cper('hdg.AgentTotalStats:list')>
                    <#assign url="/security/hdg/agent_total_stats/index.htm">
            <#elseif  contain_cper('hdg.ShopDayStats:list')>
                <#assign url="/security/hdg/shop_day_stats/index.htm">
            <#elseif  contain_cper('hdg.ShopTotalStats:list')>
                <#assign url="/security/hdg/shop_total_stats/index.htm">
            <#elseif  contain_cper('basic.AgentCompanyDayStats:list')>
                <#assign url="/security/basic/agent_company_day_stats/index.htm">
            <#elseif  contain_cper('basic.AgentCompanyTotalStats:list')>
                <#assign url="/security/basic/agent_company_total_stats/index.htm">
            <#elseif  contain_cper('hdg.CabinetDayStats:list')>
                <#assign url="/security/hdg/cabinet_day_stats/index.htm">
            <#elseif  contain_cper('hdg.CabinetMonthStats:list')>
                <#assign url="/security/hdg/cabinet_month_stats/index.htm">
            <#elseif  contain_cper('hdg.CabinetTotalStats:list')>
                <#assign url="/security/hdg/cabinet_total_stats/index.htm">
            <#elseif  contain_cper('hdg.CabinetBoxProhibitStats:list')>
                <#assign url="/security/hdg/cabinet_box_prohibit_stats/index.htm">
            <#elseif  contain_cper('hdg.BattaryAbnormalStats:list')>
                <#assign url="/security/hdg/battary_abnormal_stats/index.htm">
            <#elseif  contain_cper('hdg.PacketPeriodOrderSoonExpireStats:list')>
                <#assign url="/security/hdg/packet_period_order_soon_expire_stats/index.htm">
            <#elseif  contain_cper('hdg.CustomerDayStats:list')>
                <#assign url="/security/hdg/customer_day_stats/index.htm">
            <#elseif  contain_cper('hdg.PlatformDayStats:list')>
                <#assign url="/security/hdg/platform_day_stats/index.htm">
            <#elseif  contain_cper('hdg.PlatformMonthStats:list')>
                <#assign url="/security/hdg/platform_month_stats/index.htm">
            </#if>
            <li>
                <a class="<#if Session['SESSION_KEY_USER'].moduleId == 8>selected</#if>"
                   href="${contextPath}/security/main/module.htm?moduleId=8&url=${contextPath}${url}">
                         <span data-id="6">
                        <#--<span class="icon icon_chart"></span>-->
                            <span class="text">统计</span>
                         </span>
                </a>
            </li>
        </@app.has_any_oper>

        <@app.has_any_oper perm_codes=['yms.material:list']>
            <#if  contain_cper('yms.material:list')>
                <#assign url="/security/yms/material/index.htm">
            <#elseif  contain_cper('yms.Terminal:list')>
                <#assign url="/security/yms/terminal/index.htm">
            <#elseif  contain_cper('yms.playlist:list')>
                <#assign url="/security/yms/playlist/index.htm">
            </#if>
            <li>
                <a class="<#if Session['SESSION_KEY_USER'].moduleId == 9>selected</#if>"
                   href="${contextPath}/security/main/module.htm?moduleId=9&url=${contextPath}${url}">
                    <span data-id="10">
                    <span class="text">广告</span>
                    </span>
                </a>
            </li>
        </@app.has_any_oper>

        <@app.has_any_oper perm_codes=['basic.User:list','basic.Role:list',
        'basic.UpgradePack:list','basic.ScreenUpgradePack:list','basic.BatteryUpgradePack:list','basic.TerminalUpgradePack:list','basic.ChargerUpgradePack:list',
        'basic.Device:list','basic.DeviceUpgradePack:list','basic.DeviceCommand:list','basic.DeviceReportLog:list',
        'basic.SystemConfig:list','basic.DictItem:list']>
            <#if contain_cper('basic.User:list')>
                <#assign url="/security/basic/user/index.htm">
            <#elseif  contain_cper('basic.Role:list')>
                <#assign url="/security/basic/role/index.htm">
            <#elseif  contain_cper('basic.UpgradePack:list')>
                <#assign url="/security/basic/upgrade_pack/index.htm">
            <#elseif  contain_cper('basic.ScreenUpgradePack:list')>
                <#assign url="/security/basic/screen_upgrade_pack/index.htm">
            <#elseif  contain_cper('basic.BatteryUpgradePack:list')>
                <#assign url="/security/basic/battery_upgrade_pack/index.htm">
            <#elseif  contain_cper('basic.TerminalUpgradePack:list')>
                <#assign url="/security/basic/terminal_upgrade_pack/index.htm">
            <#elseif  contain_cper('basic.ChargerUpgradePack:list')>
                <#assign url="/security/basic/charger_upgrade_pack/index.htm">
            <#elseif  contain_cper('basic.Device:list')>
                <#assign url="/security/basic/device/index.htm">
            <#elseif  contain_cper('basic.DeviceUpgradePack:list')>
                <#assign url="/security/basic/device_upgrade_pack/index.htm">
            <#elseif  contain_cper('basic.DeviceCommand:list')>
                <#assign url="/security/basic/device_command/index.htm">
            <#elseif  contain_cper('basic.DeviceReportLog:list')>
                <#assign url="/security/basic/device_report_log/index.htm">
            <#elseif  contain_cper('basic.SystemConfig:list')>
                <#assign url="/security/basic/system_config/index.htm">
            <#elseif  contain_cper('basic.DictItem:list')>
                <#assign url="/security/basic/dict_item/index.htm">
            </#if>
            <li>
                <a class="<#if Session['SESSION_KEY_USER'].moduleId == 10>selected</#if>"
                   href="${contextPath}/security/main/module.htm?moduleId=10&url=${contextPath}${url}">
                        <span data-id="7">
                        <#--<span class="icon icon_system"></span>-->
                            <span class="text">系统</span>
                        </span>
                </a>
            </li>
        </@app.has_any_oper>
    </div>
</div>
</#macro>

<#macro agent_selector parentId='' descendant=-1 >
    <#assign _parent_id=''>
    <#if parentId?? && parentId?length gt 0>
        <#assign _parent_id=parentId/>
    <#elseif Session['SESSION_KEY_USER'].agentId?? && Session['SESSION_KEY_USER'].agentId?length gt 0>
        <#assign _parent_id=Session['SESSION_KEY_USER'].agentId/>
    <#elseif Session['SESSION_KEY_USER'].rootAgentId?? && Session['SESSION_KEY_USER'].rootAgentId?length gt 0>
        <#assign _parent_id=Session['SESSION_KEY_USER'].rootAgentId/>
    </#if>

    <#assign _descendant=0>
    <#if descendant != -1>
        <#assign _descendant=descendant/>
    <#else>
        <#assign _descendant=Session['SESSION_KEY_USER'].descendant/>
    </#if>

<input type="text" id="agent_id" class="easyui-combotree" style="height: 28px;" data-options="
        url:'${contextPath}/security/main/agent_tree.htm',
        onClick: function(node) {
            switchAgent($('#agent_id').combotree('getValue'), $('#descendant').combotree('getValue'));
        }
        " value="${_parent_id}"/>&nbsp;&nbsp;
<select id="descendant" class="easyui-combobox" data-options="
        onSelect: function(node) {
            switchAgent($('#agent_id').combotree('getValue'), $('#descendant').combotree('getValue'));
        }
        "
        style="height: 28px;width:45px;">
    <option value="0" <#if _descendant == 0>selected</#if> >本级</option>
    <option value="1" <#if _descendant == 1>selected</#if>>本下级</option>
</select>
</#macro>

<#macro menu>
<div class="left_bar">

    <div class="nav icon_fold">
    </div>

    <div class="subnav">
        <#if Session['SESSION_KEY_USER'].moduleId == 1>
            <div class="subnav_list">
                <ul>
                    <@has_any_oper perm_codes=['basic.Customer:list', 'basic.CustomerDepositOrder:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.Customer:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.CustomerDepositOrder:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/kehu.png" />&nbsp;会员管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.Customer:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.Customer:list'>selected</#if>"
                                           href="${contextPath}/security/basic/customer/index.htm">会员管理</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.CustomerDepositOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.CustomerDepositOrder:list'>selected</#if>"
                                           href="${contextPath}/security/basic/customer_deposit_order/index.htm">会员充值</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['basic.CustomerManualAuthRecord:list']>
                        <ul>
                            <#if contain_cper('basic.CustomerManualAuthRecord:list')>
                                <li><a class="<#if menuCode?? && menuCode =='basic.CustomerManualAuthRecord:list'>selected</#if>"
                                       href="${contextPath}/security/basic/customer_manual_auth_record/index.htm"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/manual_auth.png"/>&nbsp;认证审核&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['basic.PublicNotic:list']>
                        <ul>
                            <#if contain_cper('basic.PublicNotic:list')>
                                <li><a class="<#if menuCode?? && menuCode =='basic.PublicNotic:list'>selected</#if>"
                                       href="${contextPath}/security/basic/public_notice/index.htm"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/notice.png"/>&nbsp;平台公告&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.SystemBatteryType:list']>
                        <#if contain_cper('basic.SystemBatteryType:list')>
                            <li><a class="<#if menuCode?? && menuCode =='basic.SystemBatteryType:list'>selected</#if>"
                                   href="${contextPath}/security/basic/system_battery_type/index.htm"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/system_battery.png" />&nbsp;系统电池型号&nbsp;</a>
                            </li>
                        </#if>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.BatteryCellModel:list', 'hdg.BatteryCellFormat:list', 'hdg.BatteryCell:list', 'hdg.BatteryFormat:list', 'hdg.BatteryCheck:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='hdg.BatteryCellModel:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.BatteryCellFormat:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.BatteryCell:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.BatteryFormat:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.BatteryCheck:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/battery_set.png" />&nbsp;电池配置&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('hdg.BatteryCellModel:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.BatteryCellModel:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/battery_cell_model/index.htm">电芯型号管理</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.BatteryCellFormat:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.BatteryCellFormat:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/battery_cell_format/index.htm">电芯规格管理</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.BatteryCell:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.BatteryCell:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/battery_cell/index.htm">电芯检验</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.BatteryFormat:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.BatteryFormat:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/battery_format/index.htm">电池规格管理</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.BatteryCheck:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.BatteryCheck:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/battery_check/index.htm">电池检验</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.ExportRecord:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.ExportRecord:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/export_record/index.htm">发货管理</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.CabinetInstallRecord:list', 'hdg.BatteryTypeIncomeRatio:list', 'hdg.CabinetIncomeTemplate:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='hdg.CabinetInstallRecord:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.BatteryTypeIncomeRatio:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.CabinetIncomeTemplate:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left" style="width: auto !important;" src="${imagePath}/img/upgrade.png" />&nbsp;上线管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('hdg.CabinetInstallRecord:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.CabinetInstallRecord:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/cabinet_install_record/index.htm">换电柜上线审核</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.BatteryTypeIncomeRatio:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.BatteryTypeIncomeRatio:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/battery_type_income_ratio/index.htm">电池分成模板</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.CabinetIncomeTemplate:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.CabinetIncomeTemplate:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/cabinet_income_template/index.htm">换电柜分成模板</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.CustomerGuide:list', 'basic.RichText:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.CustomerGuide:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.RichText:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/guide.png" />&nbsp;用户指南协议&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.CustomerGuide:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.CustomerGuide:list'>selected</#if>"
                                           href="${contextPath}/security/basic/customer_guide/index.htm">用户指南</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.RichText:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.RichText:list'>selected</#if>"
                                           href="${contextPath}/security/basic/rich_text/index.htm">协议管理(富文本)</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.AgentSystemConfig:list', 'hdg.ReliefStation:list', 'basic.Feedback:list', 'basic.CustomerDepositGift:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.AgentSystemConfig:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.ReliefStation:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.Feedback:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.CustomerDepositGift:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/system_set.png" />&nbsp;其他终端配置&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.AgentSystemConfig:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.AgentSystemConfig:list'>selected</#if>"
                                           href="${contextPath}/security/basic/agent_system_config/index.htm">运营商配置管理</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.ReliefStation:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.ReliefStation:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/relief_station/index.htm">移动救援</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.Feedback:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.Feedback:list'>selected</#if>"
                                           href="${contextPath}/security/basic/feedback/index.htm">意见反馈</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.CustomerDepositGift:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.CustomerDepositGift:list'>selected</#if>"
                                           href="${contextPath}/security/basic/customer_deposit_gift/index.htm">充值配置</a>
                                    </li>
                                </#if>
                                <#--<#if contain_cper('1_8_5_1')>-->
                                    <#--<li><a class="<#if menuCode?? && menuCode =='010805'>selected</#if>"-->
                                           <#--href="${contextPath}/security/basic/customer_white_list/index.htm">白名单用户配置</a>-->
                                    <#--</li>-->
                                <#--</#if>-->
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.PlatformAccount:list', 'basic.Partner:list', 'basic.Weixinmp:list', 'basic.Alipayfw:list', 'basic.Phoneapp:list', 'basic.Weixinma:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.PlatformAccount:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.Partner:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.Weixinmp:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.Alipayfw:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.Phoneapp:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.Weixinma:list'>dropdown show

                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/partner_platform.png" />&nbsp;商户平台管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.PlatformAccount:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.PlatformAccount:list'>selected</#if>"
                                           href="${contextPath}/security/basic/platform_account/index.htm">平台账户管理</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.Partner:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.Partner:list'>selected</#if>"
                                           href="${contextPath}/security/basic/partner/index.htm">商户管理</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.Weixinmp:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.Weixinmp:list'>selected</#if>"
                                           href="${contextPath}/security/basic/weixinmp/index.htm">公众号配置</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.Alipayfw:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.Alipayfw:list'>selected</#if>"
                                           href="${contextPath}/security/basic/alipayfw/index.htm">生活号配置</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.Phoneapp:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.Phoneapp:list'>selected</#if>"
                                           href="${contextPath}/security/basic/phoneapp/index.htm">APP配置</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.Weixinma:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.Weixinma:list'>selected</#if>"
                                           href="${contextPath}/security/basic/weixinma/index.htm">小程序配置</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.Withdraw:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.Withdraw:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/withdraw.png" />&nbsp;提现管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.Withdraw:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.Withdraw:list'>selected</#if>"
                                           href="${contextPath}/security/basic/withdraw/index.htm">提现申请</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                </ul>
            </div>

        <#elseif Session['SESSION_KEY_USER'].moduleId == 2>
        <#--<div class="subnav_title">运营商</div>-->
            <div class="subnav_list">
                <ul>
                    <@has_any_oper perm_codes=['basic.AgentAgent:list', 'basic.AgentUser:list', 'basic.AgentRole:list', 'basic.AgentDepositOrder:list', 'basic.AgentForegift:list' ]>
                        <ul>
                            <li class="dropdown <#if menuCode?? && menuCode =='basic.AgentAgent:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.AgentUser:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.AgentRole:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.AgentDepositOrder:list'>dropdown show
                         <#elseif menuCode?? && menuCode =='basic.AgentForegift:list'>dropdown show
                        </#if>">
                                <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/yun.png" />&nbsp;运营商管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                                <ul>
                                    <#if contain_cper('basic.AgentAgent:list')>
                                        <li><a class="<#if menuCode?? && menuCode =='basic.AgentAgent:list'>selected</#if>"
                                               href="${contextPath}/security/basic/agent_agent/index.htm">运营商管理</a>
                                        </li>
                                    </#if>
                                    <#if contain_cper('basic.AgentUser:list')>
                                        <li><a class="<#if menuCode?? && menuCode =='basic.AgentUser:list'>selected</#if>"
                                               href="${contextPath}/security/basic/agent_user/index.htm">运营商账户管理</a>
                                        </li>
                                    </#if>
                                    <#if contain_cper('basic.AgentRole:list')>
                                        <li><a class="<#if menuCode?? && menuCode =='basic.AgentRole:list'>selected</#if>"
                                               href="${contextPath}/security/basic/agent_role/index.htm">运营商角色管理</a>
                                        </li>
                                    </#if>
                                    <#if contain_cper('basic.AgentDepositOrder:list')>
                                        <li><a class="<#if menuCode?? && menuCode =='basic.AgentDepositOrder:list'>selected</#if>"
                                               href="${contextPath}/security/basic/agent_deposit_order/index.htm">运营商充值</a>
                                        </li>
                                    </#if>
                                    <#if contain_cper('basic.AgentForegift:list')>
                                        <li><a class="<#if menuCode?? && menuCode =='basic.AgentForegift:list'>selected</#if>"
                                               href="${contextPath}/security/basic/agent_foregift/index.htm">押金池管理</a>
                                        </li>
                                    </#if>
                                </ul>
                            </li>
                        </ul>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.Shop:list', 'hdg.ShopUser:list', 'basic.ShopRole:list', 'hdg.ShopStoreBattery:list', 'hdg.ShopBatteryLog:list']>
                        <ul><li class="dropdown <#if menuCode?? && menuCode =='hdg.Shop:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.ShopUser:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.ShopRole:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.ShopStoreBattery:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.ShopBatteryLog:list'>dropdown show
                        </#if>">
                                <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/shop.png" />&nbsp;门店管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                                <ul>
                                    <#if contain_cper('hdg.Shop:list')>
                                        <li><a class="<#if menuCode?? && menuCode =='hdg.Shop:list'>selected</#if>"
                                               href="${contextPath}/security/hdg/shop/index.htm">门店管理</a>
                                        </li>
                                    </#if>
                                    <#if contain_cper('hdg.ShopUser:list')>
                                        <li><a class="<#if menuCode?? && menuCode =='hdg.ShopUser:list'>selected</#if>"
                                               href="${contextPath}/security/hdg/shop_user/index.htm">门店账户管理</a>
                                        </li>
                                    </#if>
                                    <#if contain_cper('basic.ShopRole:list')>
                                        <li><a class="<#if menuCode?? && menuCode =='basic.ShopRole:list'>selected</#if>"
                                               href="${contextPath}/security/basic/shop_role/index.htm">门店角色管理</a>
                                        </li>
                                    </#if>
                                    <#if contain_cper('hdg.ShopStoreBattery:list')>
                                        <li><a class="<#if menuCode?? && menuCode =='hdg.ShopStoreBattery:list'>selected</#if>"
                                               href="${contextPath}/security/hdg/shop_store_battery/index.htm">门店库存电池</a>
                                        </li>
                                    </#if>
                                    <#if contain_cper('hdg.ShopBatteryLog:list')>
                                        <li><a class="<#if menuCode?? && menuCode =='hdg.ShopBatteryLog:list'>selected</#if>"
                                               href="${contextPath}/security/hdg/shop_battery_log/index.htm">门店电池日志</a>
                                        </li>
                                    </#if>
                                </ul>
                            </li>
                        </ul>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.Estate:list','hdg.EstateUser:list','hdg.CabinetDegreeInput:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='hdg.Estate:list'>dropdown show
                            <#elseif menuCode?? && menuCode =='hdg.EstateUser:list'>dropdown show
                            <#elseif menuCode?? && menuCode =='hdg.CabinetDegreeInput:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/estate.png" />&nbsp;物业管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('hdg.Estate:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.Estate:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/estate/index.htm">物业管理</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.EstateUser:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.EstateUser:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/estate_user/index.htm">物业账户管理</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.CabinetDegreeInput:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.CabinetDegreeInput:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/cabinet_degree_input/index.htm">物业电费管理</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.AgentCompany:list', 'basic.AgentCompanyUser:list', 'basic.AgentCompanyRole:list']>
                        <ul>
                            <li class="dropdown <#if menuCode?? && menuCode =='basic.AgentCompany:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.AgentCompanyUser:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.AgentCompanyRole:list'>dropdown show
                        </#if>">
                                <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/agent_company.png" />&nbsp;运营公司管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                                <ul>
                                    <#if contain_cper('basic.AgentCompany:list')>
                                        <li><a class="<#if menuCode?? && menuCode =='basic.AgentCompany:list'>selected</#if>"
                                               href="${contextPath}/security/basic/agent_company/index.htm">运营公司管理</a>
                                        </li>
                                    </#if>
                                    <#if contain_cper('basic.AgentCompanyUser:list')>
                                        <li><a class="<#if menuCode?? && menuCode =='basic.AgentCompanyUser:list'>selected</#if>"
                                               href="${contextPath}/security/basic/agent_company_user/index.htm">运营公司账户管理</a>
                                        </li>
                                    </#if>
                                    <#if contain_cper('basic.AgentCompanyRole:list')>
                                        <li><a class="<#if menuCode?? && menuCode =='basic.AgentCompanyRole:list'>selected</#if>"
                                               href="${contextPath}/security/basic/agent_company_role/index.htm">运营公司角色管理</a>
                                        </li>
                                    </#if>
                                    <#--<#if contain_cper('basic.AgentCompanyPublicNotice:list')>-->
                                        <#--<li><a class="<#if menuCode?? && menuCode =='basic.AgentCompanyPublicNotice:list'>selected</#if>"-->
                                               <#--href="${contextPath}/security/basic/agent_company_public_notice/index.htm"><img class="img_left"-->
                                                                                                                       <#--style="width: auto !important;" src="${imagePath}/img/notice.png"/>&nbsp;运营公司公告&nbsp;</a>-->
                                        <#--</li>-->
                                    <#--</#if>-->
                                </ul>
                            </li>
                        </ul>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.AgentPublicNotice:list']>
                        <ul>
                            <#if contain_cper('basic.AgentPublicNotice:list')>
                                <li><a class="<#if menuCode?? && menuCode =='basic.AgentPublicNotice:list'>selected</#if>"
                                       href="${contextPath}/security/basic/agent_public_notice/index.htm"><img class="img_left" 
                                                                                                               style="width: auto !important;" src="${imagePath}/img/notice.png"/>&nbsp;运营商公告&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.CabinetAddressCorrection:list', 'hdg.CabinetAddressCorrectionExemptReview:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='hdg.CabinetAddressCorrection:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.CabinetAddressCorrectionExemptReview:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/correction.png" />&nbsp;位置纠错&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('hdg.CabinetAddressCorrection:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.CabinetAddressCorrection:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/cabinet_address_correction/index.htm">位置纠错</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.CabinetAddressCorrectionExemptReview:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.CabinetAddressCorrectionExemptReview:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/cabinet_address_correction_exempt_review/index.htm">纠错免审人员</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>



                    <@has_any_oper perm_codes=['basic.Laxin:list', 'basic.LaxinRecord:list', 'basic.LaxinPayOrder:list', 'basic.LaxinCustomer:list', 'basic.LaxinSetting:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.Laxin:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.LaxinRecord:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.LaxinPayOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.LaxinCustomer:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.LaxinSetting:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/laxin.png" />&nbsp;拉新管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.Laxin:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.Laxin:list'>selected</#if>"
                                           href="${contextPath}/security/basic/laxin/index.htm">拉新人员</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.LaxinRecord:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.LaxinRecord:list'>selected</#if>"
                                           href="${contextPath}/security/basic/laxin_record/index.htm">拉新记录</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.LaxinPayOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.LaxinPayOrder:list'>selected</#if>"
                                           href="${contextPath}/security/basic/laxin_pay_order/index.htm">拉新支付订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.LaxinCustomer:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.LaxinCustomer:list'>selected</#if>"
                                           href="${contextPath}/security/basic/laxin_customer/index.htm">拉新客户</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.LaxinSetting:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.LaxinSetting:list'>selected</#if>"
                                           href="${contextPath}/security/basic/laxin_setting/index.htm">拉新规则</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                    <ul>
                        <#if contain_cper('basic.CustomerWhitelist:list')>
                            <li><a class="<#if menuCode?? && menuCode =='basic.CustomerWhitelist:list'>selected</#if>"
                                   href="${contextPath}/security/basic/customer_whitelist/index.htm"><img class="img_left"
                                                                                            style="width: auto !important;" src="${imagePath}/img/customer_whitelist.png"/>&nbsp;客户白名单&nbsp;</a>
                            </li>
                        </#if>
                        <#if contain_cper('basic.WithdrawAgent:list')>
                            <li><a class="<#if menuCode?? && menuCode =='basic.WithdrawAgent:list'>selected</#if>"
                                   href="${contextPath}/security/basic/withdraw/agent_index.htm"><img class="img_left"
                                                                                            style="width: auto !important;" src="${imagePath}/img/withdraw.png"/>&nbsp;提现管理&nbsp;</a>
                            </li>
                        </#if>
                    </ul>
                </ul>
            </div>
        <#elseif Session['SESSION_KEY_USER'].moduleId == 3>
        <#--<div class="subnav_title">换电</div>-->
            <div class="subnav_list">
                <ul>
                    <@has_any_oper perm_codes=['basic.AgentBatteryType:list']>
                        <ul>
                            <#if contain_cper('basic.AgentBatteryType:list')>
                                <li><a class="<#if menuCode?? && menuCode =='basic.AgentBatteryType:list'>selected</#if>"
                                       href="${contextPath}/security/basic/agent_battery_type/index.htm"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/battery_type.png" />&nbsp;电池型号管理&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    <#--</li>-->
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.Battery:list']>
                        <ul>
                            <#if contain_cper('hdg.Battery:list')>
                                <li><a class="<#if menuCode?? && menuCode =='hdg.Battery:list'>selected</#if>"
                                       href="${contextPath}/security/hdg/battery/index.htm"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/battery.png" />&nbsp;电池管理&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.Cabinet:list']>
                        <ul>
                            <#if contain_cper('hdg.Cabinet:list')>
                                <li><a class="<#if menuCode?? && menuCode =='hdg.Cabinet:list'>selected</#if>"
                                       href="${contextPath}/security/hdg/cabinet/index.htm"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/cabinet.png" />&nbsp;换电柜管理&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    <#--</li>-->
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['hdg.PacketPeriodActivity:list']>
                        <ul>
                            <#if contain_cper('hdg.PacketPeriodActivity:list')>
                                <li><a class="<#if menuCode?? && menuCode =='hdg.PacketPeriodActivity:list'>selected</#if>"
                                       href="${contextPath}/security/hdg/packet_period_activity/index.htm"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/activity.png" />&nbsp;活动管理&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    <#--</li>-->
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['hdg.ExchangeWhitelist:list']>
                        <ul>
                            <#if contain_cper('hdg.ExchangeWhitelist:list')>
                                <li>
                                    <a class="<#if menuCode?? && menuCode =='hdg.ExchangeWhitelist:list'>selected</#if>"
                                       href="${contextPath}/security/hdg/exchange_whitelist/index.htm"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/whitelist.png"/>&nbsp;白名单管理&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['basic.CustomerForegiftOrder:list', 'hdg.PacketPeriodOrder:list', 'hdg.BatteryOrder:list', 'hdg.BackBatteryOrder:list','hdg.InsuranceOrder:list','hdg.BespeakOrder:list','basic.CustomerMultiOrder:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.CustomerForegiftOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.PacketPeriodOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.BatteryOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.BackBatteryOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.InsuranceOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.BespeakOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.CustomerMultiOrder:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/order.png" />&nbsp;订单管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.CustomerForegiftOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.CustomerForegiftOrder:list'>selected</#if>"
                                           href="${contextPath}/security/basic/customer_foregift_order/index.htm">押金订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.PacketPeriodOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.PacketPeriodOrder:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/packet_period_order/index.htm">租金订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.BatteryOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.BatteryOrder:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/battery_order/index.htm">换电订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.BackBatteryOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.BackBatteryOrder:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/back_battery_order/index.htm">退租订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.InsuranceOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.InsuranceOrder:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/insurance_order/index.htm">保险订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.BespeakOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.BespeakOrder:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/bespeak_order/index.htm">预约订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.CustomerMultiOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.CustomerMultiOrder:list'>selected</#if>"
                                           href="${contextPath}/security/basic/customer_multi_order/index.htm">多通道订单</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.FaultLog:list']>
                        <ul>
                            <#if contain_cper('hdg.FaultLog:list')>
                                <li><a class="<#if menuCode?? && menuCode =='hdg.FaultLog:list'>selected</#if>"
                                       href="${contextPath}/security/hdg/fault_log/index.htm"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/battery_set.png" />&nbsp;故障管理&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.FaultFeedback:list']>
                        <ul>
                            <#if contain_cper('hdg.FaultFeedback:list')>
                                <li>
                                    <a class="<#if menuCode?? && menuCode =='hdg.FaultFeedback:list'>selected</#if>"
                                       href="${contextPath}/security/hdg/fault_feedback/index.htm"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/feedback.png"/>&nbsp;故障反馈&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>


                    <@has_any_oper perm_codes=['hdg.ExchangeRefund:list']>
                        <ul>
                            <#if contain_cper('hdg.ExchangeRefund:list')>
                                <li><a class="<#if menuCode?? && menuCode =='hdg.ExchangeRefund:list'>selected</#if>"
                                       href="${contextPath}/security/hdg/exchange_refund/index.htm"><img class="img_left" style="width: auto !important;" src="${imagePath}/img/refund.png"/>&nbsp;退款管理&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.VipPrice:list']>
                        <ul>
                            <#if contain_cper('hdg.VipPrice:list')>
                                <li><a class="<#if menuCode?? && menuCode =='hdg.VipPrice:list'>selected</#if>"
                                       href="${contextPath}/security/hdg/vip_price/index.htm"><img class="img_left" style="width: auto !important;" src="${imagePath}/img/vip.png"/>&nbsp;VIP套餐管理&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['basic.CustomerCouponTicketGift:list', 'basic.CustomerCouponTicket:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.CustomerCouponTicketGift:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.CustomerCouponTicket:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/coupon_ticket.png" />&nbsp;优惠券管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.CustomerCouponTicketGift:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.CustomerCouponTicketGift:list'>selected</#if>"
                                           href="${contextPath}/security/basic/customer_coupon_ticket_gift/index.htm">优惠券配置</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.CustomerCouponTicket:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.CustomerCouponTicket:list'>selected</#if>"
                                           href="${contextPath}/security/basic/customer_coupon_ticket/index.htm">优惠券订单</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.ExchangeInstallmentSetting:list', 'basic.CustomerInstallmentRecord:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='hdg.ExchangeInstallmentSetting:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='basic.CustomerInstallmentRecord:list'>dropdown show
                    </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/installment_setting.png" />&nbsp;分期规则管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('hdg.ExchangeInstallmentSetting:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.ExchangeInstallmentSetting:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/exchange_installment_setting/index.htm">分期规则设置</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.CustomerInstallmentRecord:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.CustomerInstallmentRecord:list'>selected</#if>"
                                           href="${contextPath}/security/basic/customer_installment_record/index.htm">客户分期记录</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.StationBizUser:list']>
                        <ul>
                            <#if contain_cper('hdg.StationBizUser:list')>
                                <li><a class="<#if menuCode?? && menuCode =='hdg.StationBizUser:list'>selected</#if>"
                                       href="${contextPath}/security/hdg/station_biz_user/index.htm"><img class="img_left" style="width: auto !important;" src="${imagePath}/img/vip.png"/>&nbsp;拓展人员管理&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.Station:list']>
                        <ul>
                            <#if contain_cper('hdg.Station:list')>
                                <li><a class="<#if menuCode?? && menuCode =='hdg.Station:list'>selected</#if>"
                                       href="${contextPath}/security/hdg/station/index.htm"><img class="img_left" style="width: auto !important;" src="${imagePath}/img/vip.png"/>&nbsp;站点管理&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['hdg.DistributionOperate:list']>
                        <ul>
                            <#if contain_cper('hdg.DistributionOperate:list')>
                                <li><a class="<#if menuCode?? && menuCode =='hdg.DistributionOperate:list'>selected</#if>"
                                       href="${contextPath}/security/hdg/distribution_operate/index.htm"><img class="img_left" style="width: auto !important;" src="${imagePath}/img/vip.png"/>&nbsp;分成体管理&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>

                </ul>
            </div>
        <#elseif Session['SESSION_KEY_USER'].moduleId == 4>
            <div class="subnav_list">
                <ul>
                    <@has_any_oper perm_codes=['zd.RentBatteryType:list']>
                    <ul>
                        <#if contain_cper('zd.RentBatteryType:list')>
                            <li>
                                <a class="<#if menuCode?? && menuCode =='zd.RentBatteryType:list'>selected</#if>" href="${contextPath}/security/zd/rent_battery_type/index.htm">
                                    <img class="img_left" style="width: auto !important;" src="${imagePath}/img/battery_type.png" />&nbsp;电池型号管理&nbsp;
                                </a>
                            </li>
                        </#if>
                    </ul>
                    </@has_any_oper>
                <@has_any_oper perm_codes=['zd.Battery:list']>
                    <ul>
                        <#if contain_cper('zd.Battery:list')>
                            <li>
                                <a class="<#if menuCode?? && menuCode =='zd.Battery:list'>selected</#if>" href="${contextPath}/security/zd/battery/index.htm">
                                    <img class="img_left" style="width: auto !important;" src="${imagePath}/img/battery.png" />&nbsp;电池管理&nbsp;
                                </a>
                            </li>
                        </#if>
                    </ul>
                </@has_any_oper>
                <@has_any_oper perm_codes=['zd.RentPeriodActivity:list']>
                    <ul>
                        <#if contain_cper('zd.RentPeriodActivity:list')>
                            <li>
                                <a class="<#if menuCode?? && menuCode =='zd.RentPeriodActivity:list'>selected</#if>" href="${contextPath}/security/zd/rent_period_activity/index.htm">
                                    <img class="img_left" style="width: auto !important;" src="${imagePath}/img/activity.png" />&nbsp;活动管理&nbsp;
                                </a>
                            </li>
                        </#if>
                    </ul>
                </@has_any_oper>
                <@has_any_oper perm_codes=['zd.RentForegiftOrder:list', 'zd.RentPeriodOrder:list', 'zd.RentInsuranceOrder:list','zd.RentOrder:list','zd.CustomerMultiOrder:list']>
                    <li class="dropdown <#if menuCode?? && menuCode =='zd.RentForegiftOrder:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='zd.RentPeriodOrder:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='zd.RentInsuranceOrder:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='zd.RentOrder:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='zd.CustomerMultiOrder:list'>dropdown show
                    </#if>">
                        <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/order.png" />&nbsp;订单管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                        <ul>
                            <#if contain_cper('zd.RentForegiftOrder:list')>
                                <li><a class="<#if menuCode?? && menuCode =='zd.RentForegiftOrder:list'>selected</#if>"
                                       href="${contextPath}/security/zd/rent_foregift_order/index.htm">押金订单</a>
                                </li>
                            </#if>
                            <#if contain_cper('zd.RentPeriodOrder:list')>
                                <li><a class="<#if menuCode?? && menuCode =='zd.RentPeriodOrder:list'>selected</#if>"
                                       href="${contextPath}/security/zd/rent_period_order/index.htm">租金订单</a>
                                </li>
                            </#if>
                            <#if contain_cper('zd.RentOrder:list')>
                                <li><a class="<#if menuCode?? && menuCode =='zd.RentOrder:list'>selected</#if>"
                                       href="${contextPath}/security/zd/rent_order/index.htm">租电订单</a>
                                </li>
                            </#if>
                            <#if contain_cper('zd.RentInsuranceOrder:list')>
                                <li><a class="<#if menuCode?? && menuCode =='zd.RentInsuranceOrder:list'>selected</#if>"
                                       href="${contextPath}/security/zd/rent_insurance_order/index.htm">保险订单</a>
                                </li>
                            </#if>
                            <#if contain_cper('zd.CustomerMultiOrder:list')>
                                <li><a class="<#if menuCode?? && menuCode =='zd.CustomerMultiOrder:list'>selected</#if>"
                                       href="${contextPath}/security/zd/customer_multi_order/index.htm">多通道订单</a>
                                </li>
                            </#if>
                        </ul>
                    </li>
                </@has_any_oper>
                 <@has_any_oper perm_codes=['basic.CustomerCouponTicketGiftRent:list', 'basic.CustomerCouponTicketRent:list']>
                    <li class="dropdown <#if menuCode?? && menuCode =='basic.CustomerCouponTicketGiftRent:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='basic.CustomerCouponTicketRent:list'>dropdown show
                    </#if>">
                        <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/coupon_ticket.png" />&nbsp;优惠券管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                        <ul>
                            <#if contain_cper('basic.CustomerCouponTicketGiftRent:list')>
                                <li><a class="<#if menuCode?? && menuCode =='basic.CustomerCouponTicketGiftRent:list'>selected</#if>"
                                       href="${contextPath}/security/basic/customer_coupon_ticket_gift_rent/index.htm">优惠券配置</a>
                                </li>
                            </#if>
                            <#if contain_cper('basic.CustomerCouponTicketRent:list')>
                                <li><a class="<#if menuCode?? && menuCode =='basic.CustomerCouponTicketRent:list'>selected</#if>"
                                       href="${contextPath}/security/basic/customer_coupon_ticket_rent/index.htm">优惠券订单</a>
                                </li>
                            </#if>
                        </ul>
                    </li>
                 </@has_any_oper>
                <@has_any_oper perm_codes=['zd.RentRefund:list']>
                    <ul>
                        <#if contain_cper('zd.RentRefund:list')>
                            <li><a class="<#if menuCode?? && menuCode =='zd.RentRefund:list'>selected</#if>"
                                   href="${contextPath}/security/zd/rent_refund/index.htm"><img class="img_left" style="width: auto !important;" src="${imagePath}/img/refund.png"/>&nbsp;退款管理&nbsp;</a>
                            </li>
                        </#if>
                    </ul>
                </@has_any_oper>
                    <@has_any_oper perm_codes=['zd.VipRentPrice:list']>
                        <ul>
                            <#if contain_cper('zd.VipRentPrice:list')>
                                <li><a class="<#if menuCode?? && menuCode =='hdg.VipRentPrice:list'>selected</#if>"
                                       href="${contextPath}/security/zd/vip_rent_price/index.htm"><img class="img_left" style="width: auto !important;" src="${imagePath}/img/vip.png"/>&nbsp;VIP套餐管理&nbsp;</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['zd.RentInstallmentSetting:list', 'zd.ZdCustomerInstallmentRecord:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='zd.RentInstallmentSetting:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='zd.ZdCustomerInstallmentRecord:list'>dropdown show
                    </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/installment_setting.png" />&nbsp;分期管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('zd.RentInstallmentSetting:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zd.RentInstallmentSetting:list'>selected</#if>"
                                           href="${contextPath}/security/zd/rent_installment_setting/index.htm">分期设置</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zd.ZdCustomerInstallmentRecord:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zd.ZdCustomerInstallmentRecord:list'>selected</#if>"
                                           href="${contextPath}/security/zd/zd_customer_installment_record/index.htm">客户分期记录</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                </ul>
            </div>
        <#elseif Session['SESSION_KEY_USER'].moduleId == 5>
            <div class="subnav_list">
                <ul>
                    <@has_any_oper perm_codes=['zc.VehicleModel:list']>
                        <ul>
                            <#if contain_cper('zc.VehicleModel:list')>
                                <li>
                                    <a class="<#if menuCode?? && menuCode =='zc.VehicleModel:list'>selected</#if>" href="${contextPath}/security/zc/vehicle_model/index.htm">
                                        <img class="img_left" style="width: auto !important;" src="${imagePath}/img/vehicle_model.png" />&nbsp;车型管理&nbsp;
                                    </a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['zc.Vehicle:list']>
                        <ul>
                            <#if contain_cper('zc.Vehicle:list')>
                                <li>
                                    <a class="<#if menuCode?? && menuCode =='zc.Vehicle:list'>selected</#if>" href="${contextPath}/security/zc/vehicle/index.htm">
                                        <img class="img_left" style="width: auto !important;" src="${imagePath}/img/vehicle.png" />&nbsp;车辆管理&nbsp;
                                    </a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['zc.GroupOrder:list', 'zc.VehicleForegiftOrder:list', 'zc.VehiclePeriodOrder:list', 'zc.VehicleOrder:list', 'zc.ZcCustomerMultiOrder:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='zc.GroupOrder:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='zc.VehicleForegiftOrder:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='zc.VehiclePeriodOrder:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='zc.VehicleOrder:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='zc.ZcCustomerMultiOrder:list'>dropdown show
                    </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/order.png" />&nbsp;订单管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('zc.GroupOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.GroupOrder:list'>selected</#if>"
                                           href="${contextPath}/security/zc/group_order/index.htm">组合订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.VehicleOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.VehicleOrder:list'>selected</#if>"
                                           href="${contextPath}/security/zc/vehicle_order/index.htm">租车订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.VehicleForegiftOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.VehicleForegiftOrder:list'>selected</#if>"
                                           href="${contextPath}/security/zc/vehicle_foregift_order/index.htm">租车押金订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.VehiclePeriodOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.VehiclePeriodOrder:list'>selected</#if>"
                                           href="${contextPath}/security/zc/vehicle_period_order/index.htm">租车租金订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.ZcCustomerMultiOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.ZcCustomerMultiOrder:list'>selected</#if>"
                                           href="${contextPath}/security/zc/zc_customer_multi_order/index.htm">多通道订单</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['zc.VehicleCustomerForegiftOrder:list', 'zc.VehiclePacketPeriodOrder:list', 'zc.VehicleBatteryOrder:list', 'zc.VehicleBespeakOrder:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='zc.VehicleCustomerForegiftOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='zc.VehiclePacketPeriodOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='zc.VehicleBatteryOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='zc.VehicleBespeakOrder:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/exchange_order.png" />&nbsp;换电订单管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('zc.VehicleCustomerForegiftOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.VehicleCustomerForegiftOrder:list'>selected</#if>"
                                           href="${contextPath}/security/zc/vehicle_customer_foregift_order/index.htm">押金订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.VehiclePacketPeriodOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.VehiclePacketPeriodOrder:list'>selected</#if>"
                                           href="${contextPath}/security/zc/vehicle_packet_period_order/index.htm">租金订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.VehicleBatteryOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.VehicleBatteryOrder:list'>selected</#if>"
                                           href="${contextPath}/security/zc/vehicle_battery_order/index.htm">换电订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.VehicleBespeakOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.VehicleBespeakOrder:list'>selected</#if>"
                                           href="${contextPath}/security/zc/vehicle_bespeak_order/index.htm">预约订单</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['zc.VehicleRentForegiftOrder:list', 'zc.VehicleRentPeriodOrder:list', 'zc.VehicleRentOrder:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='zc.VehicleRentForegiftOrder:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='zc.VehicleRentPeriodOrder:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='zc.VehicleRentOrder:list'>dropdown show
                    </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/rent_order.png" />&nbsp;租电订单管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('zc.VehicleRentForegiftOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.VehicleRentForegiftOrder:list'>selected</#if>"
                                           href="${contextPath}/security/zc/vehicle_rent_foregift_order/index.htm">押金订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.VehicleRentPeriodOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.VehicleRentPeriodOrder:list'>selected</#if>"
                                           href="${contextPath}/security/zc/vehicle_rent_period_order/index.htm">租金订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.VehicleRentOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.VehicleRentOrder:list'>selected</#if>"
                                           href="${contextPath}/security/zc/vehicle_rent_order/index.htm">租电订单</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['zc.CustomerCouponTicketGiftVehicle:list', 'zc.CustomerCouponTicketVehicle:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='zc.CustomerCouponTicketGiftVehicle:list'>dropdown show
                    <#elseif menuCode?? && menuCode =='zc.CustomerCouponTicketVehicle:list'>dropdown show
                    </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/coupon_ticket.png" />&nbsp;优惠券管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('zc.CustomerCouponTicketGiftVehicle:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.CustomerCouponTicketGiftVehicle:list'>selected</#if>"
                                           href="${contextPath}/security/basic/customer_coupon_ticket_gift_vehicle/index.htm">优惠券配置</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.CustomerCouponTicketVehicle:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.CustomerCouponTicketVehicle:list'>selected</#if>"
                                           href="${contextPath}/security/basic/customer_coupon_ticket_vehicle/index.htm">优惠券订单</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['zc.ShopRentCar:list', 'zc.ShopInventoryBattery:list', 'zc.ShopPriceSetting:list',
                        'zc.ShopStoreVehicle:list', 'zc.CustomerVehicleInfo:list', 'zc.ShopVehicle:list']>
                        <ul><li class="dropdown <#if menuCode?? && menuCode =='zc.ShopRentCar:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='zc.ShopInventoryBattery:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='zc.ShopPriceSetting:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='zc.ShopStoreVehicle:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='zc.CustomerVehicleInfo:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='zc.ShopVehicle:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/shop.png" />&nbsp;门店管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('zc.ShopRentCar:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.ShopRentCar:list'>selected</#if>"
                                    href="${contextPath}/security/zc/shop_rent_car/index.htm">门店管理</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.ShopPriceSetting:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.ShopPriceSetting:list'>selected</#if>"
                                           href="${contextPath}/security/zc/shop_price_setting/index.htm">门店套餐</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.ShopStoreVehicle:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.ShopStoreVehicle:list'>selected</#if>"
                                           href="${contextPath}/security/zc/shop_store_vehicle/index.htm">门店库存套餐</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.ShopInventoryBattery:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.ShopInventoryBattery:list'>selected</#if>"
                                    href="${contextPath}/security/zc/shop_inventory_battery/index.htm">门店库存电池</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.ShopVehicle:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.ShopVehicle:list'>selected</#if>"
                                    href="${contextPath}/security/zc/shop_vehicle/index.htm">门店车辆</a>
                                    </li>
                                </#if>
                                <#if contain_cper('zc.CustomerVehicleInfo:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='zc.CustomerVehicleInfo:list'>selected</#if>"
                                    href="${contextPath}/security/zc/customer_vehicle_info/index.htm">门店用户</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                        </ul>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['zc.PriceSetting:list', 'zc.VehicleVipPrice:list']>
                        <ul>
                            <#if contain_cper('zc.PriceSetting:list')>
                                <li>
                                    <a class="<#if menuCode?? && menuCode =='zc.PriceSetting:list'>selected</#if>" href="${contextPath}/security/zc/price_setting/index.htm">
                                        <img class="img_left" style="width: auto !important;" src="${imagePath}/img/battery_type.png" />&nbsp;套餐管理&nbsp;
                                    </a>
                                </li>
                            </#if>
                            <#if contain_cper('zc.VehicleVipPrice:list')>
                                <li>
                                    <a class="<#if menuCode?? && menuCode =='zc.VehicleVipPrice:list'>selected</#if>" href="${contextPath}/security/zc/vehicle_vip_price/index.htm">
                                        <img class="img_left" style="width: auto !important;" src="${imagePath}/img/vip.png" />&nbsp;VIP套餐管理&nbsp;
                                    </a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>
                </ul>
            </div>
        <#elseif Session['SESSION_KEY_USER'].moduleId == 6>
        <#--<div class="subnav_title">消息</div>-->
            <div class="subnav_list">
                <ul>
                    <@has_any_oper perm_codes=['basic.SmsConfig:list', 'basic.MobileMessageTemplate:list', 'basic.MobileMessage:list', 'basic.BatchMobileMessage:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.SmsConfig:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.MobileMessageTemplate:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.MobileMessage:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.BatchMobileMessage:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/message.png" />&nbsp;短信消息&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.SmsConfig:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.SmsConfig:list'>selected</#if>"
                                           href="${contextPath}/security/basic/sms_config/index.htm">短信接口</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.MobileMessageTemplate:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.MobileMessageTemplate:list'>selected</#if>"
                                           href="${contextPath}/security/basic/mobile_message_template/index.htm">短信模板</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.MobileMessage:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.MobileMessage:list'>selected</#if>"
                                           href="${contextPath}/security/basic/mobile_message/index.htm">短信记录</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.BatchMobileMessage:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.BatchMobileMessage:list'>selected</#if>"
                                           href="${contextPath}/security/basic/batch_mobile_message/index.htm">自定义短信</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.PushMessageTemplate:list', 'basic.PushMessage:list', 'basic.CustomerNoticeMessage:list', 'basic.UserNoticeMessage:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.PushMessageTemplate:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.PushMessage:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.CustomerNoticeMessage:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.UserNoticeMessage:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/app_message.png" />&nbsp;app消息&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.PushMessageTemplate:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.PushMessageTemplate:list'>selected</#if>"
                                           href="${contextPath}/security/basic/push_message_template/index.htm">消息推送模板</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.PushMessage:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.PushMessage:list'>selected</#if>"
                                           href="${contextPath}/security/basic/push_message/index.htm">消息记录</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.CustomerNoticeMessage:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.CustomerNoticeMessage:list'>selected</#if>"
                                           href="${contextPath}/security/basic/customer_notice_message/index.htm">会员通知消息</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.UserNoticeMessage:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.UserNoticeMessage:list'>selected</#if>"
                                           href="${contextPath}/security/basic/user_notice_message/index.htm">用户通知消息</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.MpPushMessageTemplate:list', 'basic.WeixinmpTemplateMessage:list', 'basic.BatchWeixinmpTemplateMessage:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.MpPushMessageTemplate:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.WeixinmpTemplateMessage:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.BatchWeixinmpTemplateMessage:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/mp_message.png" />&nbsp;公众号消息&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.MpPushMessageTemplate:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.MpPushMessageTemplate:list'>selected</#if>"
                                           href="${contextPath}/security/basic/mp_push_message_template/index.htm">公众号模板</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.WeixinmpTemplateMessage:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.WeixinmpTemplateMessage:list'>selected</#if>"
                                           href="${contextPath}/security/basic/weixinmp_template_message/index.htm">公众号消息记录</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.BatchWeixinmpTemplateMessage:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.BatchWeixinmpTemplateMessage:list'>selected</#if>"
                                           href="${contextPath}/security/basic/batch_weixinmp_template_message/index.htm">自定义消息</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.FwPushMessageTemplate:list','basic.AlipayfwTemplateMessage:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.FwPushMessageTemplate:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.AlipayfwTemplateMessage:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/fw_message.png" />&nbsp;生活号消息&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.FwPushMessageTemplate:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.FwPushMessageTemplate:list'>selected</#if>"
                                           href="${contextPath}/security/basic/fw_push_message_template/index.htm">生活号模板</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.AlipayfwTemplateMessage:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.AlipayfwTemplateMessage:list'>selected</#if>"
                                           href="${contextPath}/security/basic/alipayfw_template_message/index.htm">生活号消息记录</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.VoiceConfig:list', 'basic.VoiceMessageTemplate:list', 'basic.VoiceMessage:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.VoiceConfig:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.VoiceMessageTemplate:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.VoiceMessage:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/voice_message.png" />&nbsp;语音消息&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.VoiceConfig:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.VoiceConfig:list'>selected</#if>"
                                           href="${contextPath}/security/basic/voice_config/index.htm">语音接口</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.VoiceMessageTemplate:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.VoiceMessageTemplate:list'>selected</#if>"
                                           href="${contextPath}/security/basic/voice_message_template/index.htm">语音模板</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.VoiceMessage:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.VoiceMessage:list'>selected</#if>"
                                           href="${contextPath}/security/basic/voice_message/index.htm">语音记录</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                </ul>
            </div>
        <#elseif Session['SESSION_KEY_USER'].moduleId == 7>
        <#--<div class="subnav_title">资金</div>-->
            <div class="subnav_list">
                <ul>
                    <@has_any_oper perm_codes=['basic.BalanceRecord:list', 'hdg.AgentMaterialDayStats:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.BalanceRecord:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.AgentMaterialDayStats:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/day_balance_record.png" />&nbsp;结算记录&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.BalanceRecord:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.BalanceRecord:list'>selected</#if>"
                                           href="${contextPath}/security/basic/balance_record/index.htm">结算记录</a>
                                    </li>
                                </#if>
                                <#--<#if contain_cper('5_1_2_1')>-->
                                    <#--<li><a class="<#if menuCode?? && menuCode =='050102'>selected</#if>"-->
                                           <#--href="${contextPath}/security/basic/balance_transfer_order/index.htm">结算转账记录</a>-->
                                    <#--</li>-->
                                <#--</#if>-->
                                <#if contain_cper('hdg.AgentMaterialDayStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.AgentMaterialDayStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/agent_material_day_stats/index.htm">运营商支出</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.WeixinPayOrder:list', 'basic.AlipayPayOrder:list', 'basic.WeixinmaPayOrder:list', 'basic.WeixinmpPayOrder:list', 'basic.AlipayfwPayOrder:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.WeixinPayOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.AlipayPayOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.WeixinmpPayOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.WeixinmaPayOrder:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.AlipayfwPayOrder:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/proof.png" />&nbsp;支付凭证&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.WeixinPayOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.WeixinPayOrder:list'>selected</#if>"
                                           href="${contextPath}/security/basic/weixin_pay_order/index.htm">APP-微信订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.AlipayPayOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.AlipayPayOrder:list'>selected</#if>"
                                           href="${contextPath}/security/basic/alipay_pay_order/index.htm">APP-支付宝订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.WeixinmpPayOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.WeixinmpPayOrder:list'>selected</#if>"
                                           href="${contextPath}/security/basic/weixinmp_pay_order/index.htm">公众号订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.WeixinmaPayOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.WeixinmaPayOrder:list'>selected</#if>"
                                           href="${contextPath}/security/basic/weixinma_pay_order/index.htm">小程序订单</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.AlipayfwPayOrder:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.AlipayfwPayOrder:list'>selected</#if>"
                                           href="${contextPath}/security/basic/alipayfw_pay_order/index.htm">生活号订单</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.IdCardAuthRecord:list']>
                        <ul>
                            <#if contain_cper('basic.IdCardAuthRecord:list')>
                                <li><a class="<#if menuCode?? && menuCode =='basic.IdCardAuthRecord:list'>selected</#if>"
                                       href="${contextPath}/security/basic/id_card_auth_record/index.htm">实名认证记录</a>
                                </li>
                            </#if>
                        </ul>
                    </@has_any_oper>

                    <@has_any_oper perm_codes=['basic.PartnerInOutCash:list', 'basic.AgentDayInOutMoney:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.PartnerInOutCash:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.AgentDayInOutMoney:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/in_out_manage.png" />&nbsp;流水管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.PartnerInOutCash:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.PartnerInOutCash:list'>selected</#if>"
                                           href="${contextPath}/security/basic/partner_in_out_cash/index.htm">平台现金流水</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.AgentDayInOutMoney:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.AgentDayInOutMoney:list'>selected</#if>"
                                           href="${contextPath}/security/basic/agent_day_in_out_money/index.htm">运营商收入支出流水</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                </ul>
            </div>
        <#elseif Session['SESSION_KEY_USER'].moduleId == 8>
        <#--<div class="subnav_title">统计</div>-->
            <div class="subnav_list">
                <ul>
                    <@has_any_oper perm_codes=['hdg.AgentDayStats:list', 'hdg.AgentMonthStats:list', 'hdg.AgentTotalStats:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='hdg.AgentDayStats:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.AgentMonthStats:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.AgentTotalStats:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/agent_stats.png" />&nbsp;运营商统计&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('hdg.AgentDayStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.AgentDayStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/agent_day_stats/index.htm">运营商日统计</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.AgentMonthStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.AgentMonthStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/agent_month_stats/index.htm">运营商月统计</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.AgentTotalStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.AgentTotalStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/agent_total_stats/index.htm">运营商总统计</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['hdg.ShopDayStats:list', 'hdg.ShopTotalStats:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='hdg.ShopDayStats:list'>dropdown show
                            <#elseif menuCode?? && menuCode =='hdg.ShopTotalStats:list'>dropdown show
                            </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/shop_stats.png" />&nbsp;门店统计&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('hdg.ShopDayStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.ShopDayStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/shop_day_stats/index.htm">门店日统计</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.ShopTotalStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.ShopTotalStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/shop_total_stats/index.htm">门店总统计</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['basic.AgentCompanyDayStats:list', 'basic.AgentCompanyTotalStats:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.AgentCompanyDayStats:list'>dropdown show
                            <#elseif menuCode?? && menuCode =='basic.AgentCompanyTotalStats:list'>dropdown show
                            </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/company_stats.png" />&nbsp;运营公司统计&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.AgentCompanyDayStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.AgentCompanyDayStats:list'>selected</#if>"
                                           href="${contextPath}/security/basic/agent_company_day_stats/index.htm">运营公司日统计</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.AgentCompanyTotalStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.AgentCompanyTotalStats:list'>selected</#if>"
                                           href="${contextPath}/security/basic/agent_company_total_stats/index.htm">运营公司总统计</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['hdg.CabinetDayStats:list', 'hdg.CabinetMonthStats:list','hdg.CabinetTotalStats:list','hdg.CabinetBoxProhibitStats:list','hdg.BattaryAbnormalStats:list','hdg.PacketPeriodOrderSoonExpireStats:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='hdg.CabinetDayStats:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.CabinetMonthStats:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.CabinetTotalStats:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.CabinetBoxProhibitStats:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.BattaryAbnormalStats:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.PacketPeriodOrderSoonExpireStats:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/cabinet_stats.png" />&nbsp;换电统计&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('hdg.CabinetDayStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.CabinetDayStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/cabinet_day_stats/index.htm">换电柜日统计</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.CabinetMonthStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.CabinetMonthStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/cabinet_month_stats/index.htm">换电柜月统计</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.CabinetTotalStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.CabinetTotalStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/cabinet_total_stats/index.htm">换电柜总统计</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.CabinetBoxProhibitStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.CabinetBoxProhibitStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/cabinet_box_prohibit_stats/index.htm">禁用箱门总统计</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.BattaryAbnormalStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.BattaryAbnormalStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/battary_abnormal_stats/index.htm">异常电池总统计</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.PacketPeriodOrderSoonExpireStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.PacketPeriodOrderSoonExpireStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/packet_period_order_soon_expire_stats/index.htm">要到期包时总统计</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['hdg.CustomerDayStats:list', 'hdg.PlatformDayStats:list', 'hdg.PlatformMonthStats:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='hdg.CustomerDayStats:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.PlatformDayStats:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='hdg.PlatformMonthStats:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/platform_stats.png" />&nbsp;平台统计&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('')>
                                    <li><a class="<#if menuCode?? && menuCode ==''>selected</#if>"
                                           href="#">客户日活</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.CustomerDayStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.CustomerDayStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/customer_day_stats/index.htm">客单量统计</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.PlatformDayStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.PlatformDayStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/platform_day_stats/index.htm">平台日统计</a>
                                    </li>
                                </#if>
                                <#if contain_cper('hdg.PlatformMonthStats:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='hdg.PlatformMonthStats:list'>selected</#if>"
                                           href="${contextPath}/security/hdg/platform_month_stats/index.htm">平台月统计</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                </ul>
            </div>
        <#elseif Session['SESSION_KEY_USER'].moduleId ==9>
            <div class="subnav_list">
                <ul>

                    <@has_any_oper perm_codes=['yms.Material:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='yms.Material:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/order.png" />&nbsp;素材管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('yms.Material:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='yms.Material:list'>selected</#if>"
                                           href="${contextPath}/security/yms/material/index.htm">素材</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>

                        <li class="dropdown <#if menuCode?? && menuCode =='yms.Terminal:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/screen.png" />&nbsp;终端管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('yms.Terminal:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='yms.Terminal:list'>selected</#if>"
                                           href="${contextPath}/security/yms/terminal/index.htm">终端</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>

                        <li class="dropdown <#if menuCode?? && menuCode =='yms.Playlist:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/order.png" />&nbsp;播放&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('yms.Playlist:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='yms.Playlist:list'>selected</#if>"
                                           href="${contextPath}/security/yms/playlist/index.htm">播放列表</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>


                </ul>
            </div>
        <#elseif Session['SESSION_KEY_USER'].moduleId == 10>
        <#--<div class="subnav_title">系统配置</div>-->
            <div class="subnav_list">
                <ul>
                    <@has_any_oper perm_codes=['basic.User:list', 'basic.Role:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.User:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.Role:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/role.png" />&nbsp;账户权限管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.User:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.User:list'>selected</#if>"
                                           href="${contextPath}/security/basic/user/index.htm">用户管理</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.Person:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.Person:list'>selected</#if>"
                                           href="${contextPath}/security/basic/person/index.htm">用户管理(新)</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.Role:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.Role:list'>selected</#if>"
                                           href="${contextPath}/security/basic/role/index.htm">角色管理</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['basic.UpgradePack:list', 'basic.ScreenUpgradePack:list',
                    'basic.BatteryUpgradePack:list', 'basic.TerminalUpgradePack:list', 'basic.ChargerUpgradePack:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.UpgradePack:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.ScreenUpgradePack:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.BatteryUpgradePack:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.ChargerUpgradePack:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.TerminalUpgradePack:list'>dropdown show</#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/upgrade.png" />&nbsp;升级管理&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.UpgradePack:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.UpgradePack:list'>selected</#if>"
                                           href="${contextPath}/security/basic/upgrade_pack/index.htm">手机端升级</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.ScreenUpgradePack:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.ScreenUpgradePack:list'>selected</#if>"
                                           href="${contextPath}/security/basic/screen_upgrade_pack/index.htm">屏幕升级</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.BatteryUpgradePack:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.BatteryUpgradePack:list'>selected</#if>"
                                           href="${contextPath}/security/basic/battery_upgrade_pack/index.htm">电池升级</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.TerminalUpgradePack:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.TerminalUpgradePack:list'>selected</#if>"
                                           href="${contextPath}/security/basic/terminal_upgrade_pack/index.htm">换电柜升级</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.ChargerUpgradePack:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.ChargerUpgradePack:list'>selected</#if>"
                                           href="${contextPath}/security/basic/charger_upgrade_pack/index.htm">充电器升级</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['basic.Device:list', 'basic.DeviceUpgradePack:list',
                     'basic.DeviceCommand:list', 'basic.DeviceReportLog:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.Device:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.DeviceUpgradePack:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.DeviceCommand:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.DeviceReportLog:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/other_settings.png" />&nbsp;终端配置&nbsp;<img style="width: auto !important;" src="${imagePath}/img/san.png" /></a>
                            <ul>
                                <#if contain_cper('basic.Device:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.Device:list'>selected</#if>"
                                           href="${contextPath}/security/basic/device/index.htm">终端</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.DeviceUpgradePack:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.DeviceUpgradePack:list'>selected</#if>"
                                           href="${contextPath}/security/basic/device_upgrade_pack/index.htm">终端升级</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.DeviceCommand:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.DeviceCommand:list'>selected</#if>" href="${contextPath}/security/basic/device_command/index.htm">终端命令</a></li>
                                </#if>
                                <#if contain_cper('basic.DeviceReportLog:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.DeviceReportLog:list'>selected</#if>" href="${contextPath}/security/basic/device_report_log/index.htm">终端上报日志</a></li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                    <@has_any_oper perm_codes=['basic.SystemConfig:list', 'basic.DictItem:list']>
                        <li class="dropdown <#if menuCode?? && menuCode =='basic.SystemConfig:list'>dropdown show
                        <#elseif menuCode?? && menuCode =='basic.DictItem:list'>dropdown show
                        </#if>">
                            <a href="javascript:void(0)"><img class="img_left"  style="width: auto !important;" src="${imagePath}/img/system_set.png" />&nbsp;系统配置&nbsp;<img  style="width: auto !important;" src="${imagePath}/img/san.png"  /></a>
                            <ul>
                                <#if contain_cper('basic.SystemConfig:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.SystemConfig:list'>selected</#if>"
                                           href="${contextPath}/security/basic/system_config/index.htm">系统配置</a>
                                    </li>
                                </#if>
                                <#if contain_cper('basic.DictItem:list')>
                                    <li><a class="<#if menuCode?? && menuCode =='basic.DictItem:list'>selected</#if>"
                                           href="${contextPath}/security/basic/dict_item/index.htm">字典管理</a>
                                    </li>
                                </#if>
                            </ul>
                        </li>
                    </@has_any_oper>
                </ul>
            </div>
        </#if>

    </div>

    <div class="splitter"><span class="split_left"></span></div>
    <script>
        $(function () {
            $(".splitter span").click(function () {
                if ($(".left_bar").hasClass("none")) {
                    $(".left_bar").removeClass("none");
                    $(".content").css({"left": "210px"});
                    $(this).removeClass("split_right").addClass("split_left");
                } else {
                    $(".left_bar").addClass("none");
                    $(".content").css({"left": "9px"});
                    $(this).addClass("split_right").removeClass("split_left");
                }
            });
            $(".nano").nanoScroller({alwaysVisible: true});
        });
    </script>
</#macro>

<#macro page_html method="queryPage" style="" page=page>
    <div class="paging" style="${style}">
        <div class="bg">

            <table cellspacing="0" cellpadding="0" border="0">
                <tbody>
                <tr>
                    <td>
                        <select class="pagination-page-list" id="${method}_page_size_select" onchange="${method}(1)">
                            <option <#if page.pageSize == 20>selected</#if>>10</option>
                            <option <#if page.pageSize == 50>selected</#if>>50</option>
                            <option <#if page.pageSize == 100>selected</#if>>100</option>
                        </select>
                    </td>
                    <td>
                        <div class="pagination-btn-separator"></div>
                    </td>
                    <td>
                        <a href="javascript:void(0)" class="l-btn <#if !page.hasPrePage()>l-btn-disabled</#if>"
                           <#if page.hasPrePage()>onclick="${method}(1)"</#if>>
                        <span class="l-btn-left">
                            <span class="l-btn-text"><span class="l-btn-empty pagination-first">&nbsp;</span></span>
                        </span>
                        </a>
                    </td>
                    <td>
                        <a href="javascript:void(0)" class="l-btn <#if !page.hasPrePage()>l-btn-disabled</#if>"
                           <#if page.hasPrePage()>onclick="${method}(${page.pageNo - 1})"</#if>>
                        <span class="l-btn-left">
                            <span class="l-btn-text"><span class="l-btn-empty pagination-prev">&nbsp;</span></span>
                        </span>
                        </a>
                    </td>
                    <td>
                        <div class="pagination-btn-separator"></div>
                    </td>
                    <td><span style="padding-left:6px;">第</span></td>
                    <td><input class="pagination-num" style="width:30px;" type="text" value="${page.pageNo}"
                               id="${method}_page_no"></td>
                    <td><span style="padding-right:6px;">共${page.totalPages}页</span></td>
                    <td>
                        <div class="pagination-btn-separator"></div>
                    </td>
                    <td>
                        <a href="javascript:void(0)" class="l-btn <#if !page.hasNextPage()>l-btn-disabled</#if>"
                           <#if page.hasNextPage()>onclick="${method}(${page.pageNo + 1})"</#if>>
                        <span class="l-btn-left">
                            <span class="l-btn-text"><span class="l-btn-empty pagination-next">&nbsp;</span></span>
                        </span>
                        </a>
                    </td>
                    <td>
                        <a href="javascript:void(0)" class="l-btn <#if !page.hasNextPage()>l-btn-disabled</#if>"
                           <#if page.hasNextPage()>onclick="${method}(${page.totalPages})"</#if>>
                        <span class="l-btn-left">
                            <span class="l-btn-text"><span class="l-btn-empty pagination-last">&nbsp;</span></span>
                        </span>
                        </a>
                    </td>
                    <td>
                        <div class="pagination-btn-separator"></div>
                    </td>
                    <td>
                        <a href="javascript:void(0)" class="l-btn "
                           <#if page.hasNextPage()>onclick="${method}(${page.pageNo})"</#if>>
                        <span class="l-btn-left">
                            <span class="l-btn-text"><span class="l-btn-empty pagination-load">&nbsp;</span></span>
                        </span>
                        </a>
                    </td>
                </tr>
                </tbody>
            </table>
            <div class="pagination-info">显示${page.offset}
                到<#if page.result??>${page.offset + page.result?size}<#else>${page.offset}</#if>,共${page.totalItems}条记录
            </div>
        </div>
    </div>
    <script>
        var ${method}Size = function () {
            return $('#${method}_page_size_select').val();
        };
        var ${method}No = function () {

        };
        $('#${method}_page_no').keydown(function (event) {
            if (event.keyCode == 13) {
                var val = this.value, totalPages = ${page.totalPages};
                if (isNaN(val)) {
                    this.value = ${page.pageNo};
                }
                val = parseInt(val);
                if (val < 1) {
                    val = 1;
                } else if (val > totalPages) {
                    val = totalPages;
                }
                this.value = val;
                ${method}(val);
            }
        });

        $('#hide_home').click(function () {
            document.location.href = "${contextPath}/security/main/module.htm?moduleId=0&url=${contextPath}/security/main/index.htm";
        })

    </script>
</#macro>


<#--<#macro has_oper perm_code>-->
<#--&lt;#&ndash;    <#if Session['SESSION_KEY_USER'].hasOper(perm_code)><#nested></#if>&ndash;&gt;-->
    <#--<#nested>-->
<#--</#macro>-->
<#--<#macro has_any_oper perm_codes>-->
<#--&lt;#&ndash;    <#if Session['SESSION_KEY_USER'].hasAnyOper(perm_codes)><#nested></#if>&ndash;&gt;-->
    <#--<#nested>-->
<#--</#macro>-->
<#function contain_cper perm_code>
    <#return Session['SESSION_KEY_USER'].hasOper(perm_code)>
</#function>

<#function format_time d>
    <#return d?string('HH:mm:ss')>
</#function>

<#function format_date d>
    <#return d?string('yyyy-MM-dd')>
</#function>

<#function format_date_minute d>
    <#return d?string('yyyy-MM-dd HH:mm')>
</#function>

<#function format_date_time d>
    <#return d?string('yyyy-MM-dd HH:mm:ss')>
</#function>

<#function format_mobile mobile>
    <#if mobile?length gte 11>
        <#return mobile?replace((mobile ?substring(3,4)),"*")>
    <#else>
        <#return mobile>
    </#if>
</#function>

<#function format_file_size size>
    <#if size lte 0>
        <#return "0B">
    <#elseif size gte 1024 * 1024 * 1024>
        <#return (size / (1024 * 1024 * 1024))?string("#.##") + "GB">
    <#elseif size gte 1024 * 1024>
        <#return (size / (1024 * 1024))?string("#.##") + "MB">
    <#elseif size gte 1024>
        <#return (size / (1024))?string("#.##") + "KB">
    <#else>
        <#return size?c + "B">
    </#if>
</#function>

</div>