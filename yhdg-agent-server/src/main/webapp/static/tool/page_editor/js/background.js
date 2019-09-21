(function() {
    function showProperty(values) {
        App.dialog.show({
            css: 'width:700px;height:170px;',
            title: '背景',
            href: App.contextPath + '/security/yms/page_editor/background.htm',
            windowData: {
                values: values
            }
        });
    }

    Editor.Background = new Class({
        Extends: Editor.Control,
        initialize: function () {
            this.parent(1);
            this.type = 'background';
        },
        toJson: function(getNextId) {
            var json = this.parent(getNextId);
            json.bgColor = this.bgColor || '';
            json.material = [];
            for(var i = 0; i < this.material.length; i++) {
                json.material.push($.extend({}, this.material[i]));
            }
            return json;
        },
        getLength: function() {
            var length = 0;
            var material = this.material;
            for(var i = 0; i < material.length; i++) {
                length += parseInt(material[i].length, 10);
            }
            return length;
        },
        addToEditor: function() {
            var html = this.getHtml();
            Editor.get(Editor.control_board_id).prepend(html);
        },
        showProperty: function() {
            showProperty(this);
        }
    });


    Editor.Background.extend({
        showProperty: function(values) {
            showProperty(values);
        }
    });

})();