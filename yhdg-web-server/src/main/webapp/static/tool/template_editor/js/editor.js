(function() {
    Editor.TemplateEditor = new Class({
        initialize: function (config) {
            this.resolution = config.resolution;
            this.yui = config.yui;
            this.clipboard = false;

            this.controlStore = new Editor.ControlStore();
            this.sequence = Editor.buildSequence(1);
            this.focusManager = new Editor.FocusManager();
            this.zoom = 0;
        },

        getZoom: function() {
            return this.zoom;
        },

        zoomChange: function(zoom) {
            this.zoom = zoom;
            this.zoomControls(zoom);

        },

        zoomControls: function(zoom) {
            var self = this, elements = Editor.get('control_board').find('div[control_type]');
            elements.each(function() {
                var control = self.controlStore.get(this.getAttribute('control_id'));
                control.onZoom(zoom);
            });
        },

        loadControlsJson: function(controls) {
            var typeMap = {
                background: Editor.Background,
                area: Editor.Area
            };

            for(var i = 0; i < controls.length; i++) {
                var control =  controls[i];
                this.sequence.reset(control.id - 1);

                if(typeMap[control.type]) {
                    $.extend(control, this.getPercentControlRect(control));
                    this.createControl(typeMap[control.type], control, false);
                }
            }
        },

        checkAreaNum: function() {
            var controlBoard = Editor.get(Editor.control_board_id);
            var map = {};
            var ok = true;
            Editor.get(Editor.control_board_id).find('div[control_type=area]').each(function() {
                var jo = $(this);
                var control = map[this.getAttribute('control_id')];
                if(control) {
                    if(!map[control.num]) {
                        map[control.num] = control.num;
                    } else {
                        ok = false;
                    }
                }
            });

            return ok;
        },

        getBackground: function() {
            var id = Editor.get(Editor.control_board_id).find('div[control_type][control_type=background]').attr('control_id');
            if(id) {
                return this.controlStore.get(id);
            }
            return false;
        },

        getControlsJson: function() {
            var filter = function(json) {
                if(json.material) {
                    for(var i = 0 ; i < json.material.length; i++) {
                        json.material[i] = { id : json.material[i].id };
                    }
                }
                return json;
            };

            var sequence = Editor.buildSequence(this.getBackground() ? 0 : 1),
                json = {
                    background: [],
                    area:[]
                },
                map = {
                    background: 'background',
                    area: 'area'
                },
                self = this;

            var controls = Editor.get(Editor.control_board_id).find('div[control_type]');
            controls.each(function() {
                var control = self.controlStore.get(this.getAttribute('control_id'));
                if(typeof(control.parentControl) == 'undefined') {
                    json[map[control.type]].push(filter(control.toJson(sequence.next)));
                }
            });

            return json;
        },

        getInitControlRect: function() {
            var result;
            var resolution = this.resolution;
            var focusControl = this.focusManager.getFocus();

            if(focusControl) {
                result = {
                    rect: Editor.getRect(focusControl.domId),
                    left: focusControl.left,
                    top: focusControl.top,
                    width: focusControl.width,
                    height: focusControl.height,
                    resolutionLeft: parseInt(focusControl.left * resolution.width),
                    resolutionTop: parseInt(focusControl.top * resolution.height),
                    resolutionWidth: parseInt(focusControl.width * resolution.width),
                    resolutionHeight: parseInt(focusControl.height * resolution.height)
                };
            } else {
                var boardSize = Editor.getElementSize(Editor.control_board_id);
                var rect = Editor.getSuitableRect(boardSize);
                var percent = {};
                result = {
                    left: percent.left = rect.left / boardSize.width,
                    top: percent.top = rect.top / boardSize.height,
                    width: percent.width = rect.width / boardSize.width,
                    height: percent.height = rect.height / boardSize.height,
                    resolutionLeft: parseInt(percent.left * resolution.width),
                    resolutionTop: parseInt(percent.top * resolution.height),
                    resolutionWidth: parseInt(percent.width * resolution.width),
                    resolutionHeight: parseInt(percent.height * resolution.height),
                    rect: rect
                }
            }

            return result;
        },

        getPercentControlRect: function(percent) { //通过百分比来加载控件(一般在加载控件时候使用)
            var resolution = this.resolution;
            var boardSize = Editor.getElementSize(Editor.control_board_id);
            return {
                rect: {
                    left: percent.left * boardSize.width,
                    top: percent.top * boardSize.height,
                    width: percent.width * boardSize.width,
                    height: percent.height * boardSize.height
                },
                left: percent.left,
                top: percent.top,
                width: percent.width,
                height: percent.height,
                resolutionLeft: parseInt(percent.left * resolution.width),
                resolutionTop: parseInt(percent.top * resolution.height),
                resolutionWidth: parseInt(percent.width * resolution.width),
                resolutionHeight: parseInt(percent.height * resolution.height)
            };
        },

        getPoint: function() {
            var result = {
                resolution: this.resolution,
                boardSize: Editor.getElementSize(Editor.control_board_id)
            };
            if(this.focusManager.selectedControls.length == 1) {
                var focusControl = this.focusManager.selectedControls[0];
                result.point = Editor.getPosition(focusControl.domId);
            } else {
                var boardSize = Editor.getElementSize(Editor.control_board_id);
                result.point = Editor.getSuitablePoint(boardSize);
            }

            return result;
        },

        removeSelected: function() {
            var self = this;
            var focusControls = this.focusManager.selectedControls;
            if(focusControls.length) {
                this.focusManager.forceBlur();
                this.focusManager.selectedControls = [];

                function remove(control) {
                    Editor.get(control.domId).remove();
                    self.controlStore.remove(control.id);
                }

                for(var i = 0; i < focusControls.length; i++) {
                    remove(focusControls[i]);
                    var parent = focusControls[i].parentControl;
                    if(parent) {
                        parent.removeChild(focusControls[i].id);
                        if(parent.children.length == 0) {
                            remove(parent);
                        }
                    }
                }
            }
        },

        alignLeft: function(controls) {
            Editor.alignLeft(this.focusManager.selectedControls);
        },
        alignRight: function(controls) {
            Editor.alignRight(this.focusManager.selectedControls);
        },
        alignTop: function(controls) {
            Editor.alignTop(this.focusManager.selectedControls);
        },
        alignBottom: function (controls) {
            Editor.alignBottom(this.focusManager.selectedControls);
        },
        horizontalSplit: function(controls) {
            Editor.horizontalSplit(this.focusManager.selectedControls);
        },
        verticalSplit: function(controls) {
            Editor.verticalSplit(this.focusManager.selectedControls);
        },
        magnetLeft: function(controls) {
            return Editor.magnetLeft(this.focusManager.selectedControls);
        },
        magnetRight: function(controls) {
            return Editor.magnetRight(this.focusManager.selectedControls);
        },
        magnetTop: function(controls) {
            return Editor.magnetTop(this.focusManager.selectedControls);
        },
        magnetBottom: function(controls) {
            return Editor.magnetBottom(this.focusManager.selectedControls);
        },

        addControl: function(clazz) {
            var focusControls = this.focusManager.selectedControls;
            if(focusControls && focusControls.length) {
                $.messager.confirm('提示信息', '确认替换控件?', function(ok) {
                    if(ok) clazz.showProperty();
                });
            } else {
                clazz.showProperty();
            }
        },

        createControl: function(clazz, values, focus) {
            var self = this;
            var control = $.extend(new clazz(this.sequence.next()), {
                yui: this.yui,
                focusManager: this.focusManager,
                getZoom: function() { return self.getZoom() }
            }, values);

            this.removeSelected();
            this.controlStore.put(control);
            control.addToEditor(focus);
        },

        updateControl: function(values, focus) {
            var control = this.focusManager.getFocus();
            control = $.extend(control, values);
            Editor.get(control.domId).remove();
            control.addToEditor(focus);
        },

        addBackground: function() {
            var control = this.controlStore.get('1');
            if(control) {
                Editor.Background.showProperty(control);
            } else {
                Editor.Background.showProperty();
            }
        },
        createBackground: function(values) {
            var boardSize = Editor.getSize(Editor.control_board_id);
            var control = $.extend(new Editor.Background(this.sequence.next()), {
                rect: {
                    left: 0, top: 0, width: boardSize.width, height: boardSize.height
                },
                top: 0, left: 0, width: 1, height: 1
            }, values);
            this.controlStore.put(control);
            control.addToEditor();
        },
        updateBackground: function(values) {
            var control = this.controlStore.get(1);
            control = $.extend(control, values);
            Editor.get(control.domId).remove();
            control.addToEditor(false);
        },

        addArea: function() {
            console.log(Editor.Area);
            this.addControl(Editor.Area);
        },

        createArea: function(values) {
            $.extend(values, this.getInitControlRect());
            this.createControl(Editor.Area, values, true);
        },

        updateArea: function(values) {
            this.updateControl(values, true);
        },

        copy: function() {
            this.clipboard = {
                type: 'controls',
                data: this.focusManager.orderSelected()
            };
        },

        paste: function() {
            if(this.clipboard && this.clipboard.type) {
                if(this.clipboard.type == 'controls') {
                    this.focusManager.forceBlur();
                    this.pasteControls(this.clipboard);
                }

            } else {
                $.messager.alert('提示信息', '请先按 CTRL C进行复制', 'info');
            }
        },

        pasteControls: function(clipboard) {
            var controls = clipboard.data;
            if(!clipboard.offset) {
                clipboard.offset = 0;
            }
            clipboard.offset += 10;
            var resolution = this.resolution;
            var boardSize = Editor.getElementSize(Editor.control_board_id);
            var list = [], result = [], map = {};

            for(var i = 0; i < controls.length; i++) {
                var control = controls[i];
                if(control.parentControl) {
                    var v = map[control.parentControl.id];
                    if(!v) {
                        list.push(control.parentControl);
                        v = {};
                        map[control.parentControl.id] = v;
                    }
                    v[control.type] = control.type;
                } else {
                    list.push(control);
                }
            }

            for(var i = 0; i < list.length; i++) {
                var control = list[i];
                var json = control.toJson(this.sequence.next);
                if(json.children) {
                    for(var j = json.children.length - 1; j >= 0; j--) {
                        if(!map[control.id][json.children[j].type]) {
                            json.children.splice(j, 1);
                        }
                    }
                }

                result.push(json);
            }

            this.loadControlsJson(result);

            var controls = [];
            for(var i = 0; i < result.length; i++) {
                var control = this.controlStore.get(result[i].id);
                if(control.children) {
                    for(var j = 0; j < control.children.length; j++) {
                        controls.push(control.children[j]);
                    }
                } else {
                    controls.push(control);
                }
            }

            var direction = [2, 4, 3, 1], offset = clipboard.offset;
            for(var i = 0; i < direction.length; i++) {
                if(Editor.allowMove(direction[i], offset, controls)) {
                    Editor.move(direction[i], offset, controls);
                    break;
                }
            }
        },

        lockAll: function() {
            var self = this;
            Editor.get(Editor.control_board_id).find('div[control_type]').each(function() {
                var control = self.controlStore.get(this.getAttribute('control_id'));
                control.locked = true;
                if(control.selected || control.focused) {
                    control.showResizeBox();
                }
            });
        },
        unlockAll: function() {
            var self = this;
            Editor.get(Editor.control_board_id).find('div[control_type]').each(function() {
                var control = self.controlStore.get(this.getAttribute('control_id'));
                control.locked = false;
                if(control.selected || control.focused) {
                    control.showResizeBox();
                }
            });
        },
        clear: function() {
            this.focusManager.forceBlur();
            this.controlStore.clear();
            Editor.get(Editor.control_board_id).empty();
        },
        removeBackground: function() {
            this.controlStore.remove(1);
            Editor.get(Editor.background_id).remove();
        },
        positionControl: function(rect, source) {
            var control = this.focusManager.getFocus();
            if(!control) {
                $.messager.alert('提示信息', '请先选中控件', 'info');
                return;
            }

            if(isNaN(rect.left) || isNaN(rect.top) || isNaN(rect.width) || isNaN(rect.height)) {
                $.messager.alert('提示信息', '录入数据错误', 'info');
                return;
            }
            if(rect.width == 0 || rect.height == 0) {
                $.messager.alert('提示信息', '录入数据错误', 'info');
                return;
            }
            if(rect.left < 0 || rect.top < 0 || rect.width < 0 || rect.height < 0) {
                $.messager.alert('提示信息', '录入数据错误', 'info');
                return;
            }

            var resolution = Editor.getResolution();
            var boardSize = Editor.getSize(Editor.control_board_id);
            rect = Editor.round(rect);

            if(source == 'left') {
                if(rect.left + rect.width > resolution.width) {
                    rect.left = resolution.width - rect.width;
                }
                var left = parseInt(rect.left / resolution.width * boardSize.width);
                control.left = rect.left / resolution.width;
                Editor.setLeft(control.domId, left);
                control.onMove();

            } else if(source == 'top') {
                if(rect.top + rect.height > resolution.height) {
                    rect.top = resolution.height - rect.height;
                }
                var top = parseInt(rect.top / resolution.height * boardSize.height);
                control.top = rect.top / resolution.height;
                Editor.setTop(control.domId, top);
                control.onMove();

            } else if(source == 'width') {
                if(rect.left + rect.width > resolution.width) {
                    rect.width = resolution.width - rect.left;
                }
                var width = parseInt(rect.width / resolution.width * boardSize.width);
                control.width = rect.width / resolution.width;
                Editor.setWidth(control.domId, width);
                control.onMove();

            } else if(source == 'height') {
                if(rect.top + rect.height > resolution.height) {
                    rect.height = resolution.height - rect.top;
                }
                var height = parseInt(rect.height / resolution.height * boardSize.height);
                control.height = rect.height / resolution.height;
                Editor.setHeight(control.domId, height);
                control.onMove();
            }

        },

        registerClick: function() {
            var self = this;
            function clickHandler(target) {
                if(target && target.id) {
                    if(target.id == 'editor_container' || target.id == 'main_area' || target.id == 'control_board' || target.id == 'control_1' || (target.className && target.className.indexOf('parent_control')) >= 0) {
                        self.focusManager.forceBlur();
                    } else if(target.id.indexOf('control_') >= 0) {
                    }
                } else {
                    self.focusManager.forceBlur();
                }
            }

            Editor.get(Editor.container_id).bind('click', function(e) {
                var target = e.target;
                if(target.id) {
                    if(target.id == 'left' || target.id == 'top' || target.id == 'width' || target.id == 'height') {
                        return;
                    }

                    if(target.id == Editor.container_id || target.id == Editor.main_area_id || target.id == Editor.control_board_id || target.id == 'control_1' || target.id.indexOf('control_') >= 0) {
                        clickHandler(target);
                    } else {
                        var parent = $(target).closest('div[control_type]');
                        clickHandler(parent[0]);
                    }
                } else {
                    var parent = $(target).closest('div[control_type]');
                    clickHandler(parent[0]);
                }
            });
        },

        registerBoardDrag: function() {
            var dd = new this.yui.DD.Drag({
                node: '#' + Editor.control_board_mask_id
            });

            dd.on('drag:drag', function() {
                Editor.setPosition(Editor.control_board_id, Editor.getPosition(Editor.control_board_mask_id));
            });
        },

        registerKeyDown: function() {
            var self = this;

            $(document.body).keydown(function(event) {
                if(event.target.nodeName == 'INPUT'
                    || event.target.nodeName == 'TEXTAREA'
                    || event.target.nodeName == 'SELECT') {
                    return;
                }

                if(event.ctrlKey && event.altKey) {
                    var mask = Editor.get(Editor.control_board_mask_id);
                    if(mask.is(':visible')) {
                        mask.hide();
                    } else {
                        Editor.setRect(Editor.control_board_mask_id, Editor.getRect(Editor.control_board_id));
                        mask.show();
                    }
                    return;
                }

                var selected = self.focusManager.selectedControls;
                if(selected.length == 0) {
                    return;
                }

                if(event.which == 38) { // up
                    if(event.ctrlKey) {
                        if(Editor.allowResize(1, 1, selected)) {
                            Editor.resize(1, selected);
                        }
                    } else {
                        if(Editor.allowMove(1, 1, selected)) {
                            Editor.move(1, 1, selected);
                        }
                    }

                } else if(event.which == 40) { // down
                    if(event.ctrlKey) {
                        if(Editor.allowResize(2, 1, selected)) {
                            Editor.resize(2, selected);
                        }
                    } else {
                        if(Editor.allowMove(2, 1, selected)) {
                            Editor.move(2, 1, selected);
                        }
                    }

                } else if(event.which == 37) { // left
                    if(event.ctrlKey) {
                        if(Editor.allowResize(3, 1, selected)) {
                            Editor.resize(3, selected);
                        }
                    } else {
                        if(Editor.allowMove(3, 1, selected)) {
                            Editor.move(3, 1, selected);
                        }
                    }

                } else if(event.which == 39) { // right
                    if(event.ctrlKey) {
                        if(Editor.allowResize(4, 1, selected)) {
                            Editor.resize(4, selected);
                        }
                    } else {
                        if(Editor.allowMove(4, 1, selected)) {
                            Editor.move(4, 1, selected);
                        }

                    }

                } else if(event.which == 46) { //delete
                    self.removeSelected();
                } else if(event.which == 67) { //ctrl c
                    if(event.ctrlKey) {
                        self.copy();
                    }
                } else if(event.which == 86) { //ctrl v
                    if(event.ctrlKey) {
                        self.paste();
                    }
                }


            });
        },

        registerContextMenu: function() {
            var self = this;
            $('#board_context_menu').menu({
                onClick: function(item) {
                    if(item.name == 'lock_all') {
                        self.lockAll();
                    } else if(item.name == 'unlock_all') {
                        self.unlockAll();
                    } else if(item.name == 'clear') {
                        self.clear();
                    } else if(item.name == 'remove_background') {
                        self.removeBackground();
                    } else if(item.name == 'copy') {
                        self.copy();
                    } else if(item.name == 'paste') {
                        self.paste();
                    }
                }
            });
            $('#control_context_menu').menu({
                onClick: function(item) {
                    if(item.name == 'select_siblings') {
                        var focus = self.focusManager.getFocus();
                        var parent = focus.parentControl;
                        self.focusManager.forceBlur();
                        for(var i = 0; i < parent.children.length; i++) {
                            if(focus.id != parent.children[i].id) {
                                self.focusManager.selected(parent.children[i]);
                            }
                        }
                        self.focusManager.selected(focus);

                    } else if(item.name == 'lock_all') {
                        self.lockAll();
                    } else if(item.name == 'unlock_all') {
                        self.unlockAll();
                    } else if(item.name == 'lock') {
                        Editor.lock(self.focusManager.selectedControls);
                    } else if(item.name == 'unlock') {
                        Editor.unlock(self.focusManager.selectedControls);
                    } else if(item.name == 'remove') {
                        self.removeSelected();
                    } else if(item.name == 'clear') {
                        self.clear();
                    } else if(item.name == 'up') {
                        Editor.up(self.focusManager.selectedControls);
                    } else if(item.name == 'down') {
                        Editor.down(self.focusManager.selectedControls);
                    } else if(item.name == 'up_top') {
                        Editor.upTop(self.focusManager.selectedControls);
                    } else if(item.name == 'down_bottom') {
                        Editor.downBottom(self.focusManager.selectedControls);
                    } else if(item.name == 'copy') {
                        self.copy();
                    } else if(item.name == 'paste') {
                        self.paste();
                    }
                }
            });

            function enable(jo, text, enable) {
                var item = jo.menu("findItem", text);
                if(enable) {
                    jo.menu("enableItem", item.target);
                } else {
                    jo.menu("disableItem", item.target);
                }

            }

            function showBoardMenu(e) {
                var jo = $('#board_context_menu');

                enable(jo, "删除背景", Editor.get(Editor.background_id).length > 0);
                enable(jo, "复制", self.focusManager.selectedControls.length > 0);
                enable(jo, "粘贴", self.clipboard);

                jo.menu('show', {
                    left: e.pageX,
                    top: e.pageY
                });
            }
            function showControlMenu(e, control) {
                var jo = $('#control_context_menu');

                var focus = self.focusManager.getFocus();

                enable(jo, "解锁", focus.locked);
                enable(jo, "锁定", !focus.locked);

                enable(jo, "复制", self.focusManager.selectedControls.length > 0);
                enable(jo, "粘贴", self.clipboard);

                enable(jo, '选中同组', !!focus.parentControl);

                jo.menu('show', {
                    left: e.pageX,
                    top: e.pageY
                });
            }

            function clickHandler(target, e) {
                if(target && target.id) {
                    if(target.id == 'editor_container' || target.id == 'main_area' || target.id == 'control_board' || target.id == 'control_1' || (target.className && target.className.indexOf('parent_control')) >= 0) {
                        showBoardMenu(e);
                    } else if(target.id.indexOf('control_') >= 0) {
                        var controlId = target.getAttribute('control_id');
                        var control = self.controlStore.get(controlId);
                        if(!control.focused) {
                            self.focusManager.selected(control);
                        }
                        showControlMenu(e, control);
                    }
                } else {
                    showBoardMenu(e);
                }
            }

            Editor.get(Editor.container_id).bind('contextmenu', function(e) {
                e.preventDefault();

                var target = e.target;

                if(target.id) {
                    if(target.id == Editor.container_id || target.id == Editor.main_area_id || target.id == Editor.control_board_id || target.id == 'control_1' || target.id.indexOf('control_') >= 0) {
                        clickHandler(target, e);
                    } else {
                        var parent = $(target).closest('div[control_type]');
                        clickHandler(parent[0], e);
                    }
                } else {
                    var parent = $(target).closest('div[control_type]');
                    clickHandler(parent[0], e);
                }
            });
        },

        registerToolbar: function() {
            var self = this;
            $('#toolbar_up').click(function(event) {
                event.stopPropagation();
                Editor.up(self.focusManager.selectedControls);
            });
            $('#toolbar_down').click(function(event) {
                event.stopPropagation();

                Editor.down(self.focusManager.selectedControls);
            });
            $('#toolbar_up_top').click(function(event) {
                event.stopPropagation();
                Editor.upTop(self.focusManager.selectedControls);
            });
            $('#toolbar_down_bottom').click(function(event) {
                event.stopPropagation();
                Editor.downBottom(self.focusManager.selectedControls);
            });
            $('#toolbar_toggle_lock').click(function(event) {
                event.stopPropagation();

                Editor.toggleLock(self.focusManager.selectedControls);
                if(this.className == 'lock') {
                    this.className = 'unlock';
                } else {
                    this.className = 'lock';
                }
            });
            $('#toolbar_setting').click(function(event) {
                event.stopPropagation();

                self.focusManager.getFocus().showProperty();
            });
            $('#toolbar_delete').click(function(event) {
                event.stopPropagation();

                self.removeSelected();
            });
            $('#toolbar_full_screen').click(function(event) {
                event.stopPropagation();

                var control = self.focusManager.getFocus();
                self.focusManager.focusControl(control);
                control.fullScreen();
            });
            $('#toolbar_link').click(function(event) {
                event.stopPropagation();

            });
            $('#select_siblings').click(function(event) {
                event.stopPropagation();

                var focus = self.focusManager.getFocus();
                var parent = focus.parentControl;
                self.focusManager.forceBlur();
                for(var i = 0; i < parent.children.length; i++) {
                    if(focus.id != parent.children[i].id) {
                        self.focusManager.selected(parent.children[i]);
                    }
                }
                self.focusManager.selected(focus);
            });
        }
    });

})()