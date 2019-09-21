var Editor = {};

//utils method
(function () {
    var get = function (id) {
        return $('#' + id);
    }

    Editor = $.extend(Editor, {
        container_id: 'container',
        main_area_id: 'main_area',
        control_board_id: 'control_board',
        background_id: 'control_1',
        control_board_mask_id: 'control_board_mask',
        left_id: 'left',
        top_id: 'top',
        width_id: 'width',
        height_id: 'height',
        get: get,
        getResolution: function() {
            return page.resolution;
        },
        getSuitableBoardSize: function (resolution, mainAreaSize, margin) {
            var width = mainAreaSize.width - margin.left - margin.right;
            var height = mainAreaSize.height - margin.top - margin.bottom;

            var newWidth = parseInt(height * resolution.width / resolution.height);
            if(newWidth <= width) {
                return {
                    width: newWidth, height: height
                }
            } else {
                var newHeight = parseInt(width * resolution.height / resolution.width);
                return {
                    width: width, height: newHeight
                }
            }
        },
        getElementSize: function (id) {
            var node = document.getElementById(id);
            return {
                'width': node.offsetWidth || node.getAttribute('offsetWidth'),
                'height': node.offsetHeight || node.getAttribute('offsetHeight')
            }
        },
        getElementRect: function (id) {
            var node = document.getElementById(id);
            return {
                'width': node.offsetWidth || node.getAttribute('offsetWidth'),
                'height': node.offsetHeight || node.getAttribute('offsetHeight'),
                'left': parseInt(node.style.left) || 0,
                'top': parseInt(node.style.top) || 0
            }
        },
        setRect: function (id, rect) {
            get(id).css({
                'top': rect.top + 'px',
                'left': rect.left + 'px',
                'width': rect.width + 'px',
                'height': rect.height + 'px'
            });
        },
        setPosition: function (id, position) {
            get(id).css({
                'top': position.top + 'px',
                'left': position.left + 'px'
            });
        },
        offsetPosition: function (id, offset) {
            var node = get(id);

            var pos = {
                top: parseInt(node.css('top')),
                left: parseInt(node.css('left'))
            };
            pos.top += offset.top;
            pos.left += offset.left;

            node.css({
                'top': pos.top + 'px',
                'left': pos.left + 'px'
            });
        },
        setSize: function (id, size) {
            get(id).css({
                'width': size.width + 'px',
                'height': size.height + 'px'
            });
        },
        getSize: function (id) {
            var node = get(id);

            return {
                width: parseInt(node.css('width')),
                height: parseInt(node.css('height'))
            };
        },
        getRect: function (id) {
            var node = get(id);

            return {
                top: parseInt(node.css('top')),
                left: parseInt(node.css('left')),
                width: parseInt(node.css('width')),
                height: parseInt(node.css('height'))
            };
        },
        getPosition: function(id) {
            var node = get(id);

            return {
                top: parseInt(node.css('top')),
                left: parseInt(node.css('left'))
            };
        },
        getLeft: function(id) {
            var node = get(id);
            return parseInt(node.css('left'))
        },
        setLeft: function(id, val) {
            var node = get(id);
            node.css('left', val + 'px');
        },
        getTop: function(id) {
            var node = get(id);
            return parseInt(node.css('top'))
        },
        setTop: function(id, val) {
            var node = get(id);
            node.css('top', val + 'px');
        },
        getWidth: function(id) {
            var node = get(id);
            return parseInt(node.css('width'))
        },
        setWidth: function(id, val) {
            var node = get(id);
            node.css('width', val + 'px');
        },
        getHeight: function(id) {
            var node = get(id);
            return parseInt(node.css('height'))
        },
        setHeight: function(id, val) {
            var node = get(id);
            node.css('height', val + 'px');
        },
        getRectPercent: function (rect, size) {
            return {
                left: rect.left / size.width,
                top: rect.top / size.height,
                width: rect.width / size.width,
                height: rect.height / size.height
            };
        },
        getRectFromPercent: function (percent, size) {
            return {
                left: percent.left * size.width,
                top: percent.top * size.height,
                width: percent.width * size.width,
                height: percent.height * size.height
            };
        },
        round: function (rect) {
            return {
                left: parseInt(rect.left),
                top: parseInt(rect.top),
                width: parseInt(rect.width),
                height: parseInt(rect.height)
            }
        },
        getCenterPoint: function (outSize, inSize) {
            return {
                'left': parseInt((outSize.width - inSize.width) / 2),
                'top': parseInt((outSize.height - inSize.height) / 2)
            };
        },
        sizeEquals: function (a, b) {
            return a.width == b.width && a.height == b.height;
        },
        getSuitableRect: function (size) {
            if (size.width > size.height) {
                return {
                    left: parseInt(size.width / 5),
                    top: parseInt(size.height / 5),
                    width: parseInt(size.width / 3),
                    height: parseInt(size.height / 3)
                }
            } else {
                return {
                    left: parseInt(size.width / 5),
                    top: parseInt(size.height / 5),
                    width: parseInt(size.height / 3),
                    height: parseInt(size.width / 3)
                }
            }
        },
        getSuitablePoint: function(size) {
            return {
                left: parseInt(size.width / 5),
                top: parseInt(size.height / 5)
            }
        },
        getMergeRect: function(controls) {
            var left, right, top, bottom;
            for(var i = 0; i < controls.length; i++) {
                var rect = Editor.getRect(controls[i].domId);
                if(i == 0) {
                    left = rect.left;
                    right = rect.left + rect.width;
                    top = rect.top;
                    bottom = rect.top + rect.height;
                } else {
                    if(rect.left < left) {
                        left = rect.left;
                    }
                    if(rect.left + rect.width > right) {
                        right = rect.left + rect.width;
                    }
                    if(rect.top < top) {
                        top = rect.top;
                    }
                    if(rect.top + rect.height > bottom) {
                        bottom = rect.top + rect.height;
                    }
                }
            }

            return {
                left: left, right: right, top: top, bottom: bottom
            }
        },
        adjustPosition: function(controls) {
            var rect = Editor.getMergeRect(controls), boardSize = Editor.getSize(Editor.control_board_id);
            var offset = {
                left: 0, top: 0
            };

            if(rect.left < 0) {
                offset.left = -rect.left;
            }
            if(rect.top < 0) {
                offset.top = -rect.top;
            }
            if(rect.right > boardSize.width) {
                offset.left  =  boardSize.width - rect.right;
            }
            if(rect.bottom > boardSize.height) {
                offset.top = boardSize.height - rect.bottom;
            }

            if(offset.top != 0 || offset.left != 0) {
                for(var i = 0; i < controls.length; i++) {
                    Editor.offsetPosition(controls[i].domId, offset);
                    controls[i].onMove();
                }
            }
        },
        buildSequence: function (num) {
            var i = num || 0;
            return {
                next: function () {
                    return ++i;
                },
                reset: function (num) {
                    i = num || 0;
                }
            }
        },
        activeDragResize: function(control) {
            var yui = control.yui;
            var focusManager = control.focusManager;
            var controlId = '#' + control.domId,
                constrainId = '#control_board';
            var ctrlKey = false;
            var dd = new yui.DD.Drag({
                node: controlId
            });

            dd.on('drag:mouseDown', function(evt) {
                evt.stopPropagation();
                evt.ev.stopPropagation();

                var drag = evt.target;
                drag.set('lock', control.locked || false);
                ctrlKey = evt.ev.ctrlKey;
            });

            dd.on('drag:start', function(evt) {
                if(!control.focused) {
                    if(ctrlKey) {
                        control.focusManager.selected(control);
                    } else {
                        control.focusManager.focusControl(control);
                    }

                }
                if(control.focused) {
                    control.hideToolbar();
                }
            });

            dd.on('drag:end', function(evt) {
                Editor.adjustPosition(control.focusManager.selectedControls);
                if(control.focused) {
                    control.showToolbar();
                }
            });

            dd.on('drag:drag', function(evt) {
                var controls = control.focusManager.selectedControls;

                control.onMove();
                for(var i = 0; i < controls.length; i++) {
                    if(control.id != controls[i].id) {
                        Editor.offsetPosition(controls[i].domId, { left: evt.info.delta[0], top: evt.info.delta[1]});
                        controls[i].onMove();
                    }
                }
            });

            var resize = new yui.Resize({
                node: controlId
            });
            resize.plug(yui.Plugin.ResizeConstrained, {
                constrain: constrainId
            });

            resize.on('resize:start', function(evt) {
                if(!control.focused) {
                    focusManager.focusControl(control);
                }
                var drag = evt.target;
                var lock_flag = drag.get('node').getAttribute('lock_flag');
                drag.set('lock', control.locked || false);
            });
            resize.on('resize:resize', function(evt) {
                control.onResize();
            });
        },

        allowMove: function(direction, offset, controls) {
            var boardSize = Editor.getSize(Editor.control_board_id);
            var allow = false;
            var rect = Editor.getMergeRect(controls);
            if(direction == 1) { //up
                if(rect.top - offset >= 0) {
                    allow = true;
                }

            } else if(direction == 2) { //down
                if(rect.bottom + offset <= boardSize.height) {
                    allow = true;
                }

            } else if(direction == 3) { //left
                if(rect.left - offset >= 0) {
                    allow = true;
                }
            } else if(direction == 4) { //right
                if(rect.right + offset <= boardSize.width) {
                    allow = true;
                }
            }

            return allow;
        },
        allowResize: function(direction, offset, controls) {
            var boardSize = Editor.getSize(Editor.control_board_id);
            var allow = false;
            var rect = Editor.getMergeRect(controls);
            if(direction == 1) { //up
                if(rect.bottom - offset > rect.top) {
                    allow = true;
                }

            } else if(direction == 2) { //down
                if(rect.bottom + offset <= boardSize.height) {
                    allow = true;
                }

            } else if(direction == 3) { //left
                if(rect.right - offset > rect.left) {
                    allow = true;
                }
            } else if(direction == 4) { //right
                if(rect.right + offset <= boardSize.width) {
                    allow = true;
                }
            }

            return allow;
        },
        move: function(direction, offset, controls) {
            for(var i = 0; i < controls.length; i++) {
                var control = controls[i];
                control.move(direction, offset);
            }
        },
        resize: function(direction, controls) {
            for(var i = 0; i < controls.length; i++) {
                var control = controls[i];
                control.resize(direction);
            }
        },

        alignLeft: function(controls) {
            var val;
            for(var i = 0; i < controls.length; i++) {
                var left = Editor.getLeft(controls[i].domId);
                if(i == 0) {
                    val = left;
                } else {
                    if(left < val) {
                        val = left;
                    }
                }
            }

            for(var i = 0; i < controls.length; i++) {
                Editor.setLeft(controls[i].domId, val);
                controls[i].onMove();
            }
        },
        alignRight: function(controls) {
            var val;
            for(var i = 0; i < controls.length; i++) {
                var rect = Editor.getRect(controls[i].domId);
                var right = rect.left + rect.width;
                if(i == 0) {
                    val = right;
                } else {
                    if(right > val) {
                        val = right;
                    }
                }
            }

            for(var i = 0; i < controls.length; i++) {
                var rect = Editor.getRect(controls[i].domId);
                var left = val - rect.width;
                Editor.setLeft(controls[i].domId, left);
                controls[i].onMove();
            }


        },
        alignTop: function(controls) {
            var val = 0;
            for(var i = 0; i < controls.length; i++) {
                var top = Editor.getTop(controls[i].domId);
                if(i == 0) {
                    val = top;
                } else {
                    if(top < val) {
                        val = top;
                    }
                }
            }

            for(var i = 0; i < controls.length; i++) {
                Editor.setTop(controls[i].domId, val);
                controls[i].onMove();
            }
        },
        alignBottom: function (controls) {
            var val;
            for(var i = 0; i < controls.length; i++) {
                var rect = Editor.getRect(controls[i].domId);
                var bottom = rect.top + rect.height;
                if(i == 0) {
                    val = bottom;
                } else {
                    if(bottom > val) {
                        val = bottom;
                    }
                }
            }

            for(var i = 0; i < controls.length; i++) {
                var rect = Editor.getRect(controls[i].domId);
                var top = val - rect.height;
                Editor.setTop(controls[i].domId, top);
                controls[i].onMove();
            }
        },
        horizontalSplit: function(controls) {
            var controlWidth = 0, boardWidth = Editor.getWidth(Editor.control_board_id);
            for(var i = 0; i < controls.length; i++) {
                controlWidth += Editor.getWidth(controls[i].domId);
            }

            var margin = (boardWidth - controlWidth) /(controls.length + 1);
            var left = 0;

            for(var i = 0; i < controls.length; i++) {
                left += margin;
                Editor.setLeft(controls[i].domId, parseInt(left));
                controls[i].onMove();
                left += Editor.getWidth(controls[i].domId);
            }

        },
        verticalSplit: function(controls) {
            var val = 0, boardHeight = Editor.getHeight(Editor.control_board_id);
            for(var i = 0; i < controls.length; i++) {
                val += Editor.getHeight(controls[i].domId);
            }

            var margin = (boardHeight - val) /(controls.length + 1);
            var top = 0;

            for(var i = 0; i < controls.length; i++) {
                top += margin;
                Editor.setTop(controls[i].domId, top);
                controls[i].onMove();
                top += Editor.getHeight(controls[i].domId);
            }
        },
        magnetLeft: function(controls) {
            var size = Editor.getSize(Editor.control_board_id);
            var index = {stay: 0, move: 1};
            var stay = Editor.getRect(controls[0].domId), move = Editor.getRect(controls[1].domId);

            if(stay.left <= move.left) { //
            } else {
                var temp = stay;
                stay = move;
                move = temp;
                index = {stay: 1, move: 0};
            }

            var left = stay.left + stay.width;
            if(left + move.width > size.width) {
                return false;
            } else {
                Editor.setLeft(controls[index.move].domId, left);
                controls[index.move].onMove();
                return true;
            }
        },
        magnetRight: function(controls) {
            var size = Editor.getSize(Editor.control_board_id);
            var index = {stay: 0, move: 1};
            var stay = Editor.getRect(controls[0].domId), move = Editor.getRect(controls[1].domId);

            if(stay.left >= move.left) { //
            } else {
                var temp = stay;
                stay = move;
                move = temp;
                index = {stay: 1, move: 0};
            }

            var left = stay.left - move.width;
            if(left < 0) {
                return false;
            } else {
                Editor.setLeft(controls[index.move].domId, left);
                controls[index.move].onMove();
                return true;
            }
        },
        magnetTop: function(controls) {
            var size = Editor.getSize(Editor.control_board_id);
            var index = {stay: 0, move: 1};
            var stay = Editor.getRect(controls[0].domId), move = Editor.getRect(controls[1].domId);

            if(stay.top <= move.top) { //
            } else {
                var temp = stay;
                stay = move;
                move = temp;
                index = {stay: 1, move: 0};
            }

            var top = stay.top + stay.height;
            if(top + move.height > size.height) {
                return false;
            } else {
                Editor.setTop(controls[index.move].domId, top);
                controls[index.move].onMove();
                return true;
            }
        },
        magnetBottom: function(controls) {
            var size = Editor.getSize(Editor.control_board_id);
            var index = {stay: 0, move: 1};
            var stay = Editor.getRect(controls[0].domId), move = Editor.getRect(controls[1].domId);

            if(stay.top >= move.top) { //
            } else {
                var temp = stay;
                stay = move;
                move = temp;
                index = {stay: 1, move: 0};
            }

            var top = stay.top - move.height;
            if(top < 0) {
                return false;
            } else {
                Editor.setTop(controls[index.move].domId, top);
                controls[index.move].onMove();
                return true;
            }
        },
        up: function(controls) {
            var map = {};
            for(var i = 0; i < controls.length; i++) {
                var id = controls[i].parentControl ? controls[i].parentControl.id : controls[i].id;
                map[id] = id;
            }

            Editor.get(Editor.control_board_id).find('div[control_type]').each(function() {
               var jo = $(this);
                if(map[this.getAttribute('control_id')]) {
                    var next = jo.next();
                    if(next.length > 0 && !map[next.prop('id')]) {
                        next.after(this);
                    }
                }
            });
        },
        down: function(controls) {
            var map = {};
            for(var i = 0; i < controls.length; i++) {
                var id = controls[i].parentControl ? controls[i].parentControl.id : controls[i].id;
                map[id] = id;
            }

            Editor.get(Editor.control_board_id).find('div[control_type]').each(function() {
                var jo = $(this);
                if(map[this.getAttribute('control_id')]) {
                    var prev = jo.prev();
                    if(prev.length > 0 && prev.prop('id') != 'control_1' && !map[prev.prop('id')]) {
                        prev.before(this);
                    }
                }
            });
        },
        upTop: function(controls) {
            var map = {};
            for(var i = 0; i < controls.length; i++) {
                var id = controls[i].parentControl ? controls[i].parentControl.id : controls[i].id;
                map[id] = id;
            }

            var board = Editor.get(Editor.control_board_id);
            board.find('div[control_type]').each(function() {
                if(map[this.getAttribute('control_id')]) {
                    board.append(this);
                }
            });
        },
        downBottom: function(controls) {
            var map = {};
            for(var i = 0; i < controls.length; i++) {
                var id = controls[i].parentControl ? controls[i].parentControl.id : controls[i].id;
                map[id] = id;
            }

            var list = [];
            var background = Editor.get(Editor.background_id);
            var board = Editor.get(Editor.control_board_id);
            board.find('div[control_type]').each(function() {
                if(map[this.getAttribute('control_id')]) {
                    list.push(this);
                }
            });

            for(var i = list.length - 1; i >= 0; i--) {
                if(background.length) {
                    background.after(list[i]);
                } else {
                    board.prepend(list[i]);
                }
            }
        },
        lock: function(controls) {
            for(var i = 0; i < controls.length; i++) {
                controls[i].locked = true;
                if(controls[i].focused || controls[i].selected) {
                    controls[i].showResizeBox();
                }
            }
        },
        unlock: function(controls) {
            for(var i = 0; i < controls.length; i++) {
                controls[i].locked = false;
                if(controls[i].focused || controls[i].selected) {
                    controls[i].showResizeBox();
                }
            }
        },
        toggleLock: function(controls) {
            for(var i = 0; i < controls.length; i++) {
                controls[i].locked = !controls[i].locked;
                controls[i].showResizeBox();
                if(controls[i].focused || controls[i].selected) {
                    controls[i].showResizeBox();
                }
            }
        }

    });
})();

(function () {
    var focusHtml = [
        '<div class="control_border hide_border"></div>',
        '<div class="lock-drag lock-drag-tl hide-resize-handle"></div>',
        '<div class="lock-drag lock-drag-tm hide-resize-handle"></div>',
        '<div class="lock-drag lock-drag-tr hide-resize-handle"></div>',
        '<div class="lock-drag lock-drag-ml hide-resize-handle"></div>',
        '<div class="lock-drag lock-drag-mr hide-resize-handle"></div>',
        '<div class="lock-drag lock-drag-bl hide-resize-handle"></div>',
        '<div class="lock-drag lock-drag-bm hide-resize-handle"></div>',
        '<div class="lock-drag lock-drag-br hide-resize-handle"></div>'
    ].join('');


    Editor.Control = new Class({
        initialize: function (id) {
            this.id = id;
            this.left = 0;
            this.top = 0;
            this.locked = false;
            this.type = false;
            this.focused = false;
            this.selected = false;
            this.focusManager = false;
            this.draggable = true;
            this.domId = 'control_' + id;
            this.focusHtml = focusHtml;
            this.linkEnable = false;
            this.link = '';
        },
        jo: function() {
            return Editor.get(this.domId);
        },
        getHtml: function() {
            return TrimPath.processDOMTemplate(this.type + '_html', {'control': this});
        },
        getPlayTime: function() {
            return 0;
        },
        getLength: function() {
            return 0;
        },
        toJson: function(getNextId) {
            return {
                id: getNextId(),
                top: this.top,
                left: this.left,
                width: this.width,
                height: this.height,
                type: this.type,
                lock: this.locked ? 1 : 0
            };
        },
        registerClick: function() {
            var self = this;
            this.jo().click(function(event) {
                if(event.ctrlKey) {
                    self.focusManager.selected(self);
                } else {
                    self.focusManager.focusControl(self);
                }
            });
            this.jo().dblclick(function(event) {
                self.showProperty();
            });
        },
        zoomFontSize: function(zoom) {
            var fontSize = Math.round(this.fontSize * zoom / 100);
            this.jo().find('div.editor_control_content').css('font-size', App.format('{0}px', fontSize));
        },
        onFocus: function() {

            this.focused = true;
            this.selected = true;
            this.showResizeBox();
            this.showBorder();
            this.showToolbar();
            this.showMask();
            if(this.parentControl) {
                var children = this.parentControl.children;
                for(var i = 0; i < children.length; i++) {
                    if(children[i].id != this.id && !children[i].focused && !children[i].selected) {
                        children[i].showSiblingBorder();
                    }
                }
            }

            this.showCoordinate()
        },
        onBlur: function() {

            this.focused = false;
            this.selected = false;
            this.hideResizeBox();
            this.hideBorder();
            this.hideToolbar();
            this.hideMask();
            if(this.parentControl) {
                var children = this.parentControl.children;
                for(var i = 0; i < children.length; i++) {
                    children[i].hideSiblingBorder();
                }
            }
        },
        onSelect: function() {
            this.focused = false;
            this.selected = true;
            this.showResizeBox();
            this.showBorder();
            this.hideToolbar();
            this.showMask();
        },
        onUnselect: function() {
            this.focused = false;
            this.selected = false;
            this.hideResizeBox();
            this.hideBorder();
            this.hideToolbar();
            this.hideMask();
        },
        onMove: function() {
            var rect = Editor.getRect(this.domId),
                boardSize = Editor.getElementSize(Editor.control_board_id);
            this.left = rect.left /  boardSize.width;
            this.top = rect.top / boardSize.height;
            this.rect = rect;

            if(this.focused) {
                this.showCoordinate();
            }
        },
        onResize: function() {
            var rect = Editor.getRect(this.domId),
                boardSize = Editor.getElementSize(Editor.control_board_id);
            this.left = rect.left /  boardSize.width;
            this.top = rect.top / boardSize.height;
            this.width = rect.width /  boardSize.width;
            this.height = rect.height / boardSize.height;
            this.rect = rect;

            if(this.focused) {
                this.showCoordinate();
            }
        },
        onZoom: function(zoom) {
            var boardSize = Editor.getElementSize(Editor.control_board_id);
            Editor.setRect(this.domId, {
                left: this.left * boardSize.width,
                top: this.top * boardSize.height,
                width: this.width * boardSize.width,
                height: this.height * boardSize.height
            });
        },
        showBorder: function() {
            var jo = this.jo();
            jo.find('.control_border').removeClass('hide_border');
        },
        hideBorder: function() {
            var jo = this.jo();
            jo.find('.control_border').addClass('hide_border');
        },
        showSiblingBorder: function() {
            var jo = this.jo();
            jo.find('.control_border').addClass('sibling_border');
        },
        hideSiblingBorder: function() {
            var jo = this.jo();
            jo.find('.control_border').removeClass('sibling_border');
        },
        showMask: function() {
            var jo = this.jo();
            jo.find('.editor_control_mask').show();
        },
        hideMask: function() {
            var jo = this.jo();
            jo.find('.editor_control_mask').hide();
        },
        showResizeBox: function() {
            var jo = this.jo();
            if(this.locked) {
                jo.find('.yui3-resize-handles-wrapper').addClass('hide-resize-handle');
                jo.find('.lock-drag').removeClass('hide-resize-handle');
            } else {
                jo.find('.yui3-resize-handles-wrapper').removeClass('hide-resize-handle');
                jo.find('.lock-drag').addClass('hide-resize-handle');
            }
        },
        hideResizeBox: function() {
            var jo = this.jo();
            jo.find('.yui3-resize-handles-wrapper').addClass('hide-resize-handle');
            jo.find('.lock-drag').addClass('hide-resize-handle');
        },
        showToolbar: function() {
            var offset = Editor.get(this.domId).offset();
            var top = offset.top - 40, left = offset.left;

            if(top < 0) { top = 70; }
            if(left < 0) { left = 170; }

            if(this.locked) {
                $('#toolbar_toggle_lock').attr('class', 'unlock');
            } else {
                $('#toolbar_toggle_lock').attr('class', 'lock');
            }

            if(this.linkEnable) {
                $('#toolbar_toggle_link').show();
            } else {
                $('#toolbar_toggle_link').hide();
            }

            if(this.parentControl) {
                $('#select_siblings').show();
            } else {
                $('#select_siblings').hide();
            }

            $('#editor_toolbar').css({
                left: left + 'px', top: top + 'px', display: 'block'
            });
        },
        hideToolbar: function() {
            $('#editor_toolbar').hide();
        },
        fullScreen: function() {
            Editor.setRect(this.domId, $.extend({
                left: 0, top: 0
            }, Editor.getSize(Editor.control_board_id)));

            this.onMove();
            this.onResize();
        },
        lock: function() {
            this.locked = true;
            if(this.focused || this.selected) {
                this.showResizeBox();
            }
        },
        unlock: function() {
            this.locked = false;
            if(this.focused || this.selected) {
                this.showResizeBox();
            }
        },
        showCoordinate: function() {
            var rect = Editor.round(Editor.getRectFromPercent(this, Editor.getResolution()));
            if(!this._coordinateInput) {
                this._coordinateInput = {
                    left: document.getElementById(Editor.left_id),
                    top: document.getElementById(Editor.top_id),
                    width: document.getElementById(Editor.width_id),
                    height: document.getElementById(Editor.height_id)
                }
            }
            this._coordinateInput.left.value = rect.left;
            this._coordinateInput.top.value = rect.top;
            this._coordinateInput.width.value = rect.width;
            this._coordinateInput.height.value = rect.height;
        },
        move: function(direction, offset) {
            var boardSize = Editor.getElementSize(Editor.control_board_id);
            var rect = Editor.getElementRect(this.domId);

            if(direction == 1) { //up
                if(rect.top - offset >= 0) {
                    rect.top -= offset;
                    Editor.setRect(this.domId, rect);
                    this.onMove();
                }

            } else if(direction == 2) {
             //down
                if(rect.top + rect.height + offset <= boardSize.height) {
                    rect.top += offset;
                    Editor.setRect(this.domId, rect);
                    this.onMove();
                }

            } else if(direction == 3) { //left
                if(rect.left - offset >= 0) {
                    rect.left -= offset;
                    Editor.setRect(this.domId, rect);
                    this.onMove();
                }
            } else if(direction == 4) { //right
                if(rect.left + rect.width + offset <= boardSize.width) {
                    rect.left += offset;
                    Editor.setRect(this.domId, rect);
                    this.onMove();
                }
            }
        },
        resize: function(direction) {
            var boardSize = Editor.getElementSize(Editor.control_board_id);
            var rect = Editor.getElementRect(this.domId);

            if(direction == 1) { //up
                if(rect.height - 1 >= 0) {
                    rect.height--;
                    Editor.setRect(this.domId, rect);
                    this.onResize();
                }

            } else if(direction == 2) { //down
                if(rect.top + rect.height + 1 <= boardSize.height) {
                    rect.height++;
                    Editor.setRect(this.domId, rect);
                    this.onResize();
                }

            } else if(direction == 3) { //left
                if(rect.width - 1 >= 0) {
                    rect.width--;
                    Editor.setRect(this.domId, rect);
                    this.onResize();
                }
            } else if(direction == 4) { //right
                if(rect.left + rect.width + 1 <= boardSize.width) {
                    rect.width++;
                    Editor.setRect(this.domId, rect);
                    this.onResize();
                }
            }
        }

    });

})();

(function () {
    Editor.FocusManager = new Class({
        selectedControls: [],
        orderSelected: function() {
            var map = {}, list = [];
            for(var i = 0; i < this.selectedControls.length; i++) {
                map[this.selectedControls[i].id] = this.selectedControls[i];
            }

            var controls = Editor.get(Editor.control_board_id).find('div[control_type]');
            controls.each(function() { //这样做因为复制要保证控件的顺序
                var v = map[this.getAttribute('control_id')];
                if(v) {
                    list.push(v);
                }
            });
            return list;
        },
        selected: function(control) {
            var controls = this.selectedControls;

            if(controls.length == 0) {
                this.focusControl(control);
            } else {
                var contain = false;
                for(var i = 0; i < controls.length; i++) {
                    if(controls[i].id == control.id) {
                        contain = true;
                        break;
                    }
                }

                if(controls.length > 1) {
                    for(var i = 0; i < controls.length; i++) {
                        var e = controls[i];
                        if(e.id != control.id)
                            if(e.focused) {
                                e.onBlur();
                            }
                        if(!e.selected) {
                            e.onSelect();
                        }
                    }
                }

                if(!contain) {
                    controls.push(control);
                }

                if(!control.focused) {
                    control.onFocus();
                } else {
                    control.showToolbar();
                }
            }
        },
        getFocus: function() {
            var focus = false;
            var controls = this.selectedControls;
            for(var i = 0; i < controls.length; i++) {
                if(controls[i].focused) {
                    focus = controls[i];
                    break;
                }
            }

            return focus;
        },
        focusControl: function(control) {

            this.forceBlur();
            this.selectedControls = [control];
            if(!control.focused) {
                control.onFocus();
            }
        },
        forceBlur: function() {
            for(var i = 0; i < this.selectedControls.length; i++) {
                var control = this.selectedControls[i];
                control.onBlur();
            }
            this.selectedControls = [];
        }
    });
})();

(function () {
    Editor.ControlStore = new Class({
        initialize: function () {
            this.store = {};
        },
        get: function(id) {
            return this.store[id];
        },
        put: function(control) {
            this.store[control.id] = control;
        },
        getEntrySet: function() {
            var set = [];
            for(var key in this.store) {
                set.push({
                    k: key, v: this.store[key]
                });
            }
            return set;
        },
        remove: function(id) {
            if(this.store[id]) {
                delete this.store[id]
            }
        },
        clear: function() {
            this.store = {};
        }
    });

})();