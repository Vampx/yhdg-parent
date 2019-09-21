App.dialog = {
    sequence: 0,
    extra_param: 'extra_param',
    show: function(config) {
        this.sequence++;
        var id;
        var html = '<div id="ID" collapsible="COLLAPSIBLE" modal="MODEL" minimizable="MINIMIZABLE" maximizable="MAXIMIZABLE" data-options="OPTIONS" style="CSS"></div>';
        html = html.replace(/ID/, id = (config.id || 'win_' + this.sequence));
        html = html.replace(/COLLAPSIBLE/, config.collapsible || false);
        html = html.replace(/MODEL/, config.model || true);
        html = html.replace(/MINIMIZABLE/, config.minimizable || false);
        html = html.replace(/MAXMIZABLE/, config.maximizable || false);
        html = html.replace(/OPTIONS/, config.options || '');
        html = html.replace(/CSS/, config.css || 'width:700px;height:450px;padding:5px;');

        $(document.body).append(html);

        if(config.windowData) {
            $('#' + id).data('windowData', config.windowData);
        }

        var onClose = function() {
            $('#' + id).window('destroy');
        }

        if(config.href) {
            if(config.href.indexOf('.htm?') >= 0) {
                config.href = config.href + '&pid=' + id;
            } else {
                config.href = config.href + '?pid=' + id;
            }
        }

        var param = {
            title: config.title || '',
            href: config.href || '',
            onClose: onClose
        };

        if(config.event && config.event.onClose) {
            var f = config.event.onClose;
            config.event.onClose = function() {
                f();
                onClose();
            }
        }

        param = $.extend(param, config.event);

        $('#' + id).window(param);

        return id;
    }
}

App.formatSecond = function(time) {
    var h = parseInt(time / 3600);
    var m = parseInt((time - h * 3600) / 60);
    var s = time % 60;

    if(h > 23) {
        h = 23;
    }
    return (h >= 10 ? ('' + h) : ('0' + h)) + ":" + (m >= 10 ? ('' + m) : ('0' + m)) + ":" + (s >= 10 ? ('' + s) : ('0' + s));
}

App.formatSecond2 = function(time) {
    var h = parseInt(time / 3600);
    var m = parseInt((time - h * 3600) / 60);
    var s = time % 60;
    return (h >= 10 ? ('' + h) : ('0' + h)) + ":" + (m >= 10 ? ('' + m) : ('0' + m)) + ":" + (s >= 10 ? ('' + s) : ('0' + s));
}

App.showArea = function(config) {
    var pid = '7788_1';
    function get(id) {
        return $('#' + id + '_' + pid);
    }

    $.post(App.contextPath + '/security/basic/area/show_area.htm?pid=' + pid,
        config.data || {},
        function (html) {
            $(document.body).append('<div id="mask_' + pid + '" style="z-index:9999; background-color:#000;opacity: 0.5; filter:Alpha(opacity=50); position: absolute; top: 0px; left: 0px; bottom: 0px; right: 0px;"></div>');
            $(document.body).append(html);

            var width = 397;
            var parentWidth = $(window).width();
            var left = parentWidth / 2 - width / 2;
            var dialog = get('area_selector');
            var mask = get('mask');
            dialog.find('.close').click(function() {
                dialog.remove();
                mask.remove();
            });

            dialog.data('fn', config.fn);

            dialog.find('.ok').click(function() {
                var value = {
                    areaText: get('area_text').val(),
                    provinceId: get('province_id').val(),
                    cityId: get('city_id').val(),
                    districtId: get('district_id').val()
                };
                config.fn(value);
                dialog.remove();
                mask.remove();
            });

            dialog.css({
                'left': left + 'px',
                'display': 'block'
            });




    }, 'html');

}

App.tree = {
    toggleSelect: function(node) {
        var before = $(this).tree('getSelected');
        if(before && before.id == node.id) {
            $(node.target).removeClass('tree-node-selected');
            return false;
        } else {
            return true;
        }
    }
}

App.format = function(format) {
    var args = [];
    for(var i = 1; i < arguments.length; i++) {
        args.push(arguments[i]);
    }
    return format.replace(/\{(\d+)\}/g, function(m, i) {
        return args[i];
    });
}

App.getFileSuffix = function(fileName) {
    var index = fileName.indexOf('.');
    if(index < 0) {
        return '';
    } else {
        return fileName.substring(index + 1);
    }
}
App.getFileName = function(fileName) {
    var num = fileName.lastIndexOf('.');
    var name = '';
    if(num > -1) {
        name = fileName.substring(0, num);
    }
    return name;
}

App.trimFileName = function(name, length) {
    if(name.length <= length) {
        return name;
    } else {
        var first = this.getFileName(name);
        var last = this.getFileSuffix(name);
        length = length - last.length - 1;
        first = first.substring(0, length);
        return first + '.' + last;
    }
}

App.getDayOfMonth = function(year, month) {
    var days = [];
    var d = new Date(year, month - 1, 1);
    while(d.getMonth() == month - 1) {
        days.push(d.getDate());
        d = new Date(d.getTime() + 1000 * 3600 * 24);
    }
    return days;
}

App.sort = function(array, sortFn) {
    var length = array.length,
        i = 0,
        comparison,
        j, min, tmp;

    for (; i < length; i++) {
        min = i;
        for (j = i + 1; j < length; j++) {
            if (sortFn) {
                comparison = sortFn(array[j], array[min]);
                if (comparison < 0) {
                    min = j;
                }
            } else if (array[j] < array[min]) {
                min = j;
            }
        }
        if (min !== i) {
            tmp = array[i];
            array[i] = array[min];
            array[min] = tmp;
        }
    }

    return array;
}

App.fileSize = function(size) {
    if (size <= 0) {
        return "0 B"
    } else if (size >= 1024 * 1024 * 1024) { //文件大小大于或等于1024MB
        return parseInt(size / (1024 * 1024 * 1024)) + " GB";
    } else if (size >= 1024 * 1024) { //文件大小大于或等于1024KB
        return parseInt(size / (1024 * 1024)) + " MB";
    } else if (size >= 1024) { //文件大小大于等于1024bytes
        return parseInt(size / 1024) + " KB";
    } else {
        return size + " B";
    }
}

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
}

App.getTreeNodePath = function(jo, separator) {
    var tree = jo;
    var nodes = [];
    var node = tree.tree('getSelected');
    while(node) {
        nodes.push(node);
        node = tree.tree('getParent', node.target);
    }
    var html = [];

    for(var i = nodes.length - 1; i >= 0; i--) {
        html.push(nodes[i].text);
    }

    if(html.length) {
        html = html.join(separator);
    } else {
        html = '';
    }

    return html;
}
