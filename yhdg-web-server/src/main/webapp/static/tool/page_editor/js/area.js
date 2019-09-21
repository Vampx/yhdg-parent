(function() {
    function showProperty(values) {
        App.dialog.show({
            css: 'width:468px;height:230px;',
            title: '区域',
            href: App.contextPath + '/security/yms/page_editor/area.htm',
            windowData: {
                values: values
            }
        });
    }

    Editor.Area = new Class({
        Extends: Editor.Control,
        initialize: function (id) {
            this.parent(id);
            this.type = 'area';
            this.linkEnable = true;
        },
        toJson: function(getNextId) {
            var json = this.parent(getNextId);
            json.num = this.num;
            json.areaType = this.areaType;
            return json;
        },
        addToEditor: function(focus) {
            var html = this.getHtml();
            Editor.get(Editor.control_board_id).append(html);
            this.zoom(this.getZoom())
            if(this.draggable) {
                Editor.activeDragResize(this);
                this.registerClick();
            }

            this.focusManager.forceBlur();
            if(focus) {
                this.focusManager.focusControl(this);
            } else {
                this.hideResizeBox();
            }
        },
        zoom: function(zoom) {
            this.zoomFontSize(zoom);
        },
        onZoom: function(zoom) {
            this.parent(zoom);
            this.zoom(zoom);
        },
        showProperty: function() {
            showProperty(this);
        },
        getShowText: function() {
            if(this.areaType == 1) {
                return '区域' + this.num + ': 文字';
            } else if(this.areaType == 2) {
                return '区域' + this.num + ': 天气';
            } else if(this.areaType == 3) {
                return '区域' + this.num + ': 素材';
            }
        }
    });


    Editor.Area.extend({
        showProperty: function(values) {
            showProperty(values);
        }
    });

})();