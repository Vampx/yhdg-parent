if(!contextPath) {
    contextPath = '';
}
var MoveText = function(moveObj, moveStyle, moveSpeed, callback) {
    this.moveObj = moveObj;
    this.moveStyle = moveStyle; // 1 向上滚动 2 向左滚动 3 向右滚动
    this.moveSpeed = moveSpeed;
    this.callback = callback || function(){};

    this.parentWidth = parseInt($(moveObj).parent().css('width'), 10);
    this.parentHeight = parseInt($(moveObj).parent().css('height'), 10);
}
MoveText.prototype = {
    getWidth: function() {
        return this.moveObj.offsetWidth;
    },
    getHeight: function() {
        return this.moveObj.offsetHeight;
    },
    getTop: function() {
        return parseInt(this.moveObj.style.top, 10);
    },
    getLeft: function() {
        return parseInt(this.moveObj.style.left, 10);
    },
    setTop: function(v) {
        return this.moveObj.style.top = v + 'px';
    },
    setLeft: function(v) {
        return this.moveObj.style.left = v + 'px';
    },
    move: function() {
        var self  = this;
        if(self.moveStyle == 1) {
            self.setTop(self.parentHeight);
        } else if(self.moveStyle == 2) {
            self.setLeft(self.parentWidth);
        } else if(self.moveStyle == 3) {
            self.setLeft(-this.getWidth());
        }
        var ref = setInterval(function() {
            self.move0();
        }, 300 - (self.moveSpeed - 1) * 30);
        return ref;
    },
    move0: function() { // 1 上 2 下 3 左 4右
        if(this.moveStyle == 1) {
            if(this.getTop() + this.getHeight()  <= 0) {
                this.callback();
                this.setTop(this.parentHeight);
            } else {
                this.setTop(this.getTop() - 1);
            }

        } else if(this.moveStyle == 2) {
            if(this.getLeft() + this.getWidth() <= 0) {
                this.callback();
                this.setLeft(this.parentWidth);
            } else {
                this.setLeft(this.getLeft() - 1);
            }
        } else if(this.moveStyle == 3) {
            if(this.getLeft() >= this.parentWidth) {
                this.callback();
                this.setLeft(-this.getWidth());
            } else {
                this.setLeft(this.getLeft() + 1);
            }
        }
    }
}

var Page = function(pageSize, resultSet) {
    this.pageNo = 0;
    this.pageSize = pageSize;
    this.resultSet = resultSet;
    this.totalPages = parseInt(resultSet.length / pageSize) + ((resultSet.length % pageSize) == 0 ? 0 : 1);
}
Page.prototype.getResult = function() {
    if(this.pageNo >= this.totalPages) {
        this.pageNo = 0;
    }
    var offset = (this.pageNo++) * this.pageSize;
    return this.resultSet.slice(offset, offset + this.pageSize);
}

var getControl = function(id) {
    return $('#control_' + id);
}
var round = function(rect) {
    rect.left = Math.round(rect.left);
    rect.top = Math.round(rect.top);
    rect.width = Math.round(rect.width);
    rect.height = Math.round(rect.height);
}
var rect = function(control) {
    var rect = {
        left : control.left * screenSize.width,
        top : control.top * screenSize.height,
        width : control.width * screenSize.width,
        height : control.height * screenSize.height
    };
    round(control);
    getControl(control.id).css({
        'top' : rect.top + 'px',
        'left' : rect.left + 'px',
        'width' : rect.width + 'px',
        'height' : rect.height + 'px'
    });
}

var buildControl = function(control) {
    var html = TrimPath.processDOMTemplate(control.type + '_html', {control: control});
    $(document.body).append(html);
    rect(control);
}

var buildText = function(control) {
    control.fontSize = control.fontSize * screenSize.zoom;
    control.getShowText = function() {
        var text = this.content || '';
        text = text.replace(/ /g, '&nbsp;');
        if(this.moveStyle && (this.moveStyle == 2 || this.moveStyle == 3)) {
            text = text.replace(/\n/g, '&nbsp;');
        } else {
            text = text.replace(/\n/g, '<br/>');
        }
        return text;
    };
    var html = TrimPath.processDOMTemplate(control.type + '_html', {control: control});
    $(document.body).append(html);
    rect(control);

    if(control.moveStyle) {
        var domObj = getControl(control.id).find('.editor_control_content')[0];
        var moveText = new MoveText(domObj, control.moveStyle, control.moveSpeed);
        moveText.move();
    } else {
        getControl(control).find('.editor_control_content').css({
            width: '100%',
            height: '100%'
        })
    }
}

var buildClock = function(control) {
    for(var i = 0; i < control.children.length; i++) {
        var child = control.children[i];
        child.fontSize = child.fontSize * screenSize.zoom;
    }
    buildControl(control);
    for(var i = 0; i < control.children.length; i++) {
        rect(control.children[i]);
    }
}

var buildWeather = function(control) {
    for(var i = 0; i < control.children.length; i++) {
        var child = control.children[i];
        child.fontSize = child.fontSize * screenSize.zoom;
    }
    buildControl(control);
    for(var i = 0; i < control.children.length; i++) {
        rect(control.children[i]);
    }
}

var buildTable = function(control) {
    control.zoom = function(zoom) {
        var tds = getControl(control.id).find('td'),
            borderWidth = zoom * this.borderWidth,
            self = this;
        if(borderWidth != 0 && borderWidth < 1) {
            borderWidth = 1;
        }
        tds.css({
            'border-width': borderWidth + 'px',
            'border-color': this.borderColor
        });

        tds.each(function() {
            var el = $(this);
            var key = el.attr('x') + '-' + el.attr('y');
            var v = self.gridStyle[key];
            if(v) {
                if(v) {
                    var css = {}, attr = {};

                    if(v.fontSize) {
                        css['font-size'] = v.fontSize * zoom + 'px'
                    }
                    css['color'] = v.fgColor ? v.fgColor : '';
                    css['background-color'] = v.bgColor ? v.bgColor : '';

                    if(v.textAlign == 1) {
                        attr['align'] = 'left';
                    } else if(v.textAlign == 2) {
                        attr['align'] = 'center';
                    } else if(v.textAlign == 3) {
                        attr['align'] = 'right';
                    }

                    if(v.verticalAlign == 1) {
                        attr['valign'] = 'top';
                    } else if(v.verticalAlign == 2) {
                        attr['valign'] = 'middle';
                    } else if(v.verticalAlign == 3) {
                        attr['valign'] = 'bottom';
                    }

                    el.css(css);
                    el.attr(attr);
                }
            }
        });
    }
    control.getLines = function() {
        var lines = [];
        for(var i = 0; i < this.pageSize; i++) {
            lines.push(1);
        }
        return lines;
    }

    control.fontSize = 14 * screenSize.zoom;
    buildControl(control);
    control.zoom(screenSize.zoom);

    var pageHelper;
    var createPageHelper = function() {
        var pageSize = control.style == 1 ? control.pageSize : 1;
        var page = new Page(pageSize, control.resultSet);

        var jo = getControl(control.id), rows, cells;
        if(control.style == 1) {
            rows = jo.find('tr.editor_table_row');
        } else {
            cells = jo.find('td.editor_table_cell');
        }

        var write = function(list) {
            if(control.style == 1) {
                for(var i = 0; i < pageSize; i++) {
                    var grids = rows.eq(i).find('td.editor_table_cell');

                    if(i < list.length) {
                        var record = list[i];
                        for(var j = 0; j < control.column.length; j++) {
                            var v = record[control.column[j].columnName];
                            grids.eq(j).html(v.value || '&nbsp;');
                        }
                    } else {
                        grids.html('&nbsp;');
                    }
                }
            } else {
                var record = list[0];
                for(var i = 0; i < control.column.length; i++) {
                    var v = record[control.column[i].columnName];
                    cells.eq(i).html(v.value || '&nbsp;');
                }
            }
        }

        var next = function() {
            var list = page.getResult();
            write(list);
        }

        return {
            next: next
        };
    }

    function next() {
        pageHelper.next()
    }

    $.post(contextPath + '/security/yms/query/static_data_' + control.source + '_2.htm', {
    }, function(json) {
        if(json.data.length) {
            control.resultSet = json.data;
            pageHelper = createPageHelper();

            next();
            setInterval(next, control.pageTime * 1000);
        }

    }, 'json');
}

var buildProcessTable = function(control) {
    control.zoom = function(zoom) {
        var tds = getControl(control.id).find('td'),
            borderWidth = zoom * this.borderWidth,
            self = this;
        if(borderWidth != 0 && borderWidth < 1) {
            borderWidth = 1;
        }
        tds.css({
            'border-width': borderWidth + 'px',
            'border-color': this.borderColor
        });

        tds.each(function() {
            var el = $(this);
            var key = el.attr('x') + '-' + el.attr('y');
            var v = self.gridStyle[key];
            if(v) {
                if(v) {
                    var css = {}, attr = {};

                    if(v.fontSize) {
                        css['font-size'] = v.fontSize * zoom + 'px'
                    }
                    css['color'] = v.fgColor ? v.fgColor : '';
                    css['background-color'] = v.bgColor ? v.bgColor : '';

                    if(v.textAlign == 1) {
                        attr['align'] = 'left';
                    } else if(v.textAlign == 2) {
                        attr['align'] = 'center';
                    } else if(v.textAlign == 3) {
                        attr['align'] = 'right';
                    }

                    if(v.verticalAlign == 1) {
                        attr['valign'] = 'top';
                    } else if(v.verticalAlign == 2) {
                        attr['valign'] = 'middle';
                    } else if(v.verticalAlign == 3) {
                        attr['valign'] = 'bottom';
                    }

                    el.css(css);
                    el.attr(attr);
                }
            }
        });
    }
    control.getLines = function() {
        var lines = [];
        for(var i = 0; i < this.pageSize; i++) {
            lines.push(1);
        }
        return lines;
    }

    control.fontSize = 14 * screenSize.zoom;
    buildControl(control);
    control.zoom(screenSize.zoom);
}

var buildProcessArea = function(control) {
    for(var i = 0; i < control.children.length; i++) {
        var child = control.children[i];
        child.fontSize = (child.fontSize || 14) * screenSize.zoom;
    }
    buildControl(control);
    for(var i = 0; i < control.children.length; i++) {
        rect(control.children[i]);
    }
}

var buildVariableArea = function(control) {
    for(var i = 0; i < control.children.length; i++) {
        var child = control.children[i];
        child.fontSize = (child.fontSize || 14) * screenSize.zoom;
    }
    buildControl(control);
    for(var i = 0; i < control.children.length; i++) {
        rect(control.children[i]);
    }
}

var buildDataArea = function(control) {
    for(var i = 0; i < control.children.length; i++) {
        var child = control.children[i];
        child.fontSize = child.fontSize * screenSize.zoom;
    }
    buildControl(control);
    for(var i = 0; i < control.children.length; i++) {
        rect(control.children[i]);
    }

    var jo = getControl(control.id);
    var write = function(record) {
        for(var k in record) {
            var v = record[k];
            if(v.type == 1 || v.type == 2) {
                var cell = jo.find('td[cell_name="' + k + '"]');
                if(cell.length) {
                    cell.html(v.value);
                }
            } else if(v.type == 3) {
                var cell = jo.find('img[cell_name="' + k + '"]');
                cell.attr('src', contextPath + v.value);
            }
        }
    }

    var index = 0;
    var next = function() {
        if(index >= control.resultSet.length) {
            index = 0;
        }
        write(control.resultSet[index]);
        index++;
    }

    $.post(contextPath + '/security/yms/query/static_data_' + control.source + '_2.htm', {
        id: control.source
    }, function(json) {
        if(json.data.length) {
            control.resultSet = json.data;
            next();
            setInterval(next, control.switchTime * 1000);
        }

    }, 'json');
}

var builder = {
    background: buildControl,
    text: buildText,
    image: buildControl,
    video: buildControl,
    video_stream: buildControl,
    web_page: buildControl,
    process_table: buildProcessTable,
    process_area: buildProcessArea,
    variable_area: buildVariableArea,
    clock: buildClock,
    weather: buildWeather,
    table: buildTable,
    data_area: buildDataArea
}
var screenSize = {
    width: screen.width,
    height: screen.height,
    zoom: 1
};

if(window.parent && window.parent._SCREEN && window.parent._SCREEN.size) {
     $.extend(screenSize, window.parent._SCREEN.size);
    screenSize.zoom = Math.min(screenSize.width / resolution.width, screenSize.height / resolution.height);
}
