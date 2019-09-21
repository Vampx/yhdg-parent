<div class="panel grid_wrap">
    <div class="toolbar clearfix" style="height: 60px">
        <div class="float_right">
            <table cellpadding="0" cellspacing="0" border="0" >
                <tr>
                    <td style="text-align:center;vertical-align:middle;" height="20" width="140" ><h4>运营商名称</h4></td>
                    <td style="text-align:center;vertical-align:middle;" height="20" width="140" ><h4>设备名称</h4></td>
                    <td style="text-align:center;vertical-align:middle;" height="20" width="140" ><h4>设备编号</h4></td>
                    <td style="text-align:center;vertical-align:middle;" height="20" width="140" ><h4>上线时间</h4></td>
                    <td style="text-align:center;vertical-align:middle;" height="20" width="140" ><h4>电价</h4></td>
                </tr>
                <tr>
                    <td style="text-align:center;vertical-align:middle;" height="20" width="140" >${(entity.agentName)!''}</td>
                    <td style="text-align:center;vertical-align:middle;" height="20" width="140" >${(entity.cabinetName)!''}</td>
                    <td style="text-align:center;vertical-align:middle;" height="20" width="140" >${(entity.cabinetId)!''}</td>
                    <td style="text-align:center;vertical-align:middle;" height="20" width="140" >${(entity.createTime)!''}</td>
                    <td style="text-align:center;vertical-align:middle;" height="20" width="140" >${(entity.degreePrice)!''}元/度</td>
                </tr>
                <tr>
                    <td  style="text-align:right;vertical-align:middle;" height="20" width="140" ><h4>总用电量:</h4></td>
                    <td style="text-align:left;vertical-align:middle;" height="20" width="140" >${(entity.totalElectricity)!''}</td>
                    <td style="text-align:center;vertical-align:middle;" height="20" width="140" ></td>
                    <td  style="text-align:right;vertical-align:middle;" height="20" width="140" ><h4>总用电费:</h4></td>
                    <td style="text-align:left;vertical-align:middle;" height="20" width="140" >${(entity.totalElectricityCharges)!''}</td>
                </tr>
            </table>

        <#--<h4><span>运营商名称:</span><br><span>1111</span></h4>
        <h4><span>设备名称:</span><br><span>2222</span></h4>
        <h4><span>设备编号:</span><br><span>3333</span></h4>
        <h4><span>上线时间:</span><br><span>4444</span></h4>
        <h4><span>电价:</span><br><span></span>5555</h4>-->
        </div>
        <h3>&nbsp;&nbsp;&nbsp;<b>录入记录详情</b></h3>
    </div>
    <div class="grid" style="height:425px;">
        <table id="page_table_${pid}"></table>
    </div>
</div>

<div id="close_div" class="popup_btn" style="height: 10%;">
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    (function () {
        $('#page_table_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/cabinet_degree_input/view_page.htm",
            queryParams:{'cabinetId':${(entity.cabinetId)!''}},
            pageSize: 10,
            pageList: [10, 50, 100],
            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '编号',
                        align: 'center',
                        field: 'index',
                        width: 60,
                        formatter:function(val,row,index){
                            var options = $("#page_table").datagrid('getPager').data("pagination").options;
                            var currentPage = options.pageNumber;
                            var pageSize = options.pageSize;
                            return (pageSize * (currentPage -1))+(index+1);
                        }
                    },
                    {
                        title: '时间段',
                        align: 'center',
                        field: 'beginTime',
                        width:120,
                        formatter:function(val,row){
                            return row.beginDate +"~"+row.endDate;
                        }
                    },
                    /*{
                        title: '周期',
                        align: 'center',
                        field: 'cycle',
                        width: 40,
                        formatter:function (val,row) {
                            return GetNumberOfDays(new Date(row.beginTime).toLocaleString().replace(/\//g, "-").substring(0,9),new Date(row.endTime).toLocaleString().replace(/\//g, "-").substring(0,9))
                        }
                    },*/
                    {
                        title: '电费',
                        align: 'center',
                        field: 'degreeMoney',
                        width: 80,
                        formatter:function (val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '上期录入表码',
                        align: 'center',
                        field: 'beginNum',
                        width: 80,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '本期录入表码',
                        align: 'center',
                        field: 'endNum',
                        width: 80,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '用电量',
                        align: 'center',
                        field: 'degree',
                        width: 80,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2);
                        }
                    }
                ]
            ]
        });
        var pid = '${pid}', win = $('#' + pid);
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
    function reload_${pid}() {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid('reload');
    }
    function GetNumberOfDays(date1,date2){//获得天数
        //date1：开始日期，date2结束日期
        var a1 = Date.parse(new Date(date1));
        var a2 = Date.parse(new Date(date2));
        var day = parseInt((a2-a1)/ (1000 * 60 * 60 * 24));//核心：时间戳相减，然后除以天数
        return day
    };


    $('#${pid}').data('ok', function() {
        return true;
    });


</script>