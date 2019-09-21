var App = {};
App.format = function(format) {
    var args = [];
    for(var i = 1; i < arguments.length; i++) {
        args.push(arguments[i]);
    }
    return format.replace(/\{(\d+)\}/g, function(m, i) {
        return args[i];
    });
};
App.allSelect = function(checkboxName, checked) {
    var checkboxs = document.getElementsByName(checkboxName);
    for(var i = 0; i < checkboxs.length; i++) {
        checkboxs[i].checked = checked ? true : false;
    }
};

App.dateFormat = function(date, fmt) {
    var o = {
        "M+" : date.getMonth()+1,                 //月份
        "d+" : date.getDate(),                    //日
        "h+" : date.getHours(),                   //小时
        "m+" : date.getMinutes(),                 //分
        "s+" : date.getSeconds(),                 //秒
        "q+" : Math.floor((date.getMonth()+3)/3), //季度
        "S"  : date.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
};

App.formatNum = function(num) {
    if(num < 10) {
        return '0' + num;
    } else {
        return num;
    }
};
App.getDayOfMonth = function(year, month) {
    var days = [];
    var d = new Date(year, month - 1, 1);
    while(d.getMonth() == month - 1) {
        days.push(d.getDate());
        d = new Date(d.getTime() + 1000 * 3600 * 24);
    }
    return days;
};
App.slice = function(text, length) {
    if(!text) {
        return '';
    } else {
        if(length >= text.length) {
            return text;
        } else {
            return text.substring(0, 16) + '...';
        }
    }
};
App.NextNumber = function(initNum) {
    this.data = initNum || 0;
};
App.NextNumber.prototype = {
    addAndGet: function() {
        return ++this.data;
    },
    set: function(num) {
        this.data = num;
    },
    get: function() {
        return this.data;
    }
};
App.dialogNextNumber = new App.NextNumber();
