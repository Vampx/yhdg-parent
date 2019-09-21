(function($) {
    $.fn.switchRadio = function(method) {
        var methods = {
            init: function(options) {
                var settings = $.extend({}, $.fn.switchRadio.defaults, options);

                return this.each(function() {

                    var me = $(this), clazz = me.attr('selected_class');
                    me.click(function() {
                        if(this.className.indexOf(clazz) >= 0) {
                            me.removeClass(clazz);
                        } else {
                            me.addClass(clazz);
                        }
                        if(settings.onChange) {
                            settings.onChange.apply(me);
                        }
                    });

                    if(settings.value) {
                        me.addClass(clazz);
                    } else {
                        me.removeClass(clazz);
                    }
                });
            },
            val: function() {
                var clazz = this.attr('selected_class');
                if(arguments.length == 0) {
                    return this[0].className.indexOf(clazz) >= 0;
                } else {
                    if(arguments[0]) {
                        return this.addClass(clazz);
                    } else {
                        return this.removeClass(clazz);
                    }

                }
            }
        }

        if ( methods[method] ) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if ( typeof method === 'object' || ! method ) {
            return methods.init.apply( this, arguments );
        } else {
            $.error( 'Method ' +  method + ' does not exist on jQuery.tooltip' );
        }
    };
    $.fn.switchRadio.defaults = {
        value: true,
        onChange: function() {}
    };

})(jQuery);

(function($) {
    // 1 left 2 center 3 right
    $.fn.textAlign = function(method) {
        var methods = {
            init: function(options) {
                var settings = $.extend({}, $.fn.textAlign.defaults, options);

                return this.each(function() {

                    var me = $(this), clazz = 'selected';
                    me.click(function(event) {
                        var target = $(event.target);
                        if(target.prop('tagName').toLowerCase() == 'span') {
                            target = target.closest('a');
                        } else if(target.prop('tagName').toLowerCase() == 'a') {
                        } else {
                            target = false;
                        }
                        if(target) {
                            target.addClass('selected');
                            target.siblings().removeClass('selected');
                            if(settings.onChange) {
                                settings.onChange.apply(me)
                            }
                        }

                    });

                    if(settings.value) {
                        var item = me.find('a').eq(settings.value - 1);
                        item.addClass(clazz);
                    } else {
                        me.find('a').removeClass(clazz);
                    }
                });
            },
            val: function() {
                var clazz = 'selected';
                if(arguments.length == 0) {
                    var list = this.find('a');
                    var value = 0;
                    for(var i = 0; i < list.length; i++) {
                        if(list.eq(i).attr('class').indexOf(clazz) >= 0) {
                            value = i + 1;
                        }
                    }
                    return value;
                } else {
                    var list = this.find('a');
                    list.removeClass(clazz);
                    return list.eq(arguments[0] - 1).addClass(clazz);
                }
            }
        }

        if ( methods[method] ) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if ( typeof method === 'object' || ! method ) {
            return methods.init.apply( this, arguments );
        } else {
            $.error( 'Method ' +  method + ' does not exist on jQuery.tooltip' );
        }
    };
    $.fn.textAlign.defaults = {
        value: 1,
        onChange: function() {}
    };

})(jQuery);