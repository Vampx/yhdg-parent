$(function () {
    var combo1 = $('#agent_id ~ span');
    var combo2 = $('#top_agent_id ~ span');
    var combo3 = $('#delivery_agent_id ~ span');
    var combop = $('.combo-p');
    var comboValue = '';

    //点击树形框，切换到autocomplete模式
    combo1.children('.combo-text').click(function(){
        // alert(combo1.html())
        combo1.hide();
        combop.hide();
        $('#agent_id').prop('class', 'easyui-combobox')
            .css('display','').css('height','26px').focus();

        $('#agent_id').typeahead({
            source: agentList,
            items: 20,
            itemSelected: function(item, val, text) {
                $('#agent_id').next('span').children('.combo-value').val(val);                
            }
        });
        $('.dropdown-menu').css('z-index', 99999);
        comboValue = $('.combo-value').val();
    });

    combo2.children('.combo-text').click(function(){
        // alert(combo3.html())
        combo2.hide();
        combop.hide();
        $('#top_agent_id').prop('class', 'easyui-combobox')
            .css('display','').css('height','26px').focus();

        $('#top_agent_id').typeahead({
            source: agentList,
            items: 20,
            itemSelected: function(item, val, text) {
                $('#top_agent_id').next('span').children('.combo-value').val(val);
            }
        });
        $('.dropdown-menu').css('z-index', 99999);
        comboValue = $('.combo-value').val();
    });

    combo3.children('.combo-text').click(function(){
        // alert(combo3.html())
        combo3.hide();
        combop.hide();
        $('#delivery_agent_id').prop('class', 'easyui-combobox')
            .css('display','').css('height','26px').focus();

        $('#delivery_agent_id').typeahead({
            source: agentList,
            items: 20,
            itemSelected: function(item, val, text) {
                $('#delivery_agent_id').next('span').children('.combo-value').val(val);
            }
        });
        $('.dropdown-menu').css('z-index', 99999);
        comboValue = $('.combo-value').val();
    });

    //删除全部autocomplete输入框的文字或者输入框失去焦点后，调用还原成树的function：
    $('#agent_id, #top_agent_id,#delivery_agent_id').keyup(function(event){
        if(8 == event.keyCode || 46 == event.keyCode) {
            if($(this).val() == '' || $(this).val().length == 0){
                $(this).prop('class', 'easyui-combotree')
                    .css('display','none').css('height','28px');
                $(this).next('span').show().children('.combo-value').val(comboValue);
            }
        }
    }).blur(function () {
        if($(this).val() == '' || $(this).val().length == 0){
            $(this).prop('class', 'easyui-combotree')
                .css('display','none').css('height','28px');
            $(this).next('span').show().children('.combo-value').val(comboValue);
        }
    });
})