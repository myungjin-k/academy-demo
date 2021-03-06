function loadCart(itemList) {
    var userId = $('.loginInfo').val();
    addCart(userId, itemList);
}
function goCart() {
    var userId = $('.loginInfo').val();
    cart.init(userId);
}
function addCart(userId, itemList){
    if(itemList.length === 0){
        alert('옵션을 확인해 주세요.');
        return false;
    } else {
        const uri = '/api/mall/member/'+ userId +'/cart';
        $.ajax({
            type: 'POST',
            url: uri,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(itemList)
        }).done(function(response) {
            //console.log(response);
            cart.init(userId);
        }).fail(function (error) {
            if(userId === undefined){
                alert('로그인 후 이용해 주세요.');
            } else {
                alert('오류가 발생했습니다. 관리자에게 문의하십시오. \n' + error.message );
            }
            location.href='/mall/login';
        });
    }
}

var cart = {
    div : $('#div-cart'),
    userId : '',
    init : function(userId){
        var _this = this;
        _this.userId = userId;
        _this.load();
        $('.contentDiv').not(_this.div).addClass('d-none');
        _this.div.removeClass('d-none');

        _this.div.find('#cart-items').off('click').on('click', '.btnTd .btn', function(){
            var cartId = $(this).parents('tr').find('input[name="id"]').val();
            if($(this).hasClass('btn-delete'))
                _this.delete(cartId);
            else if($(this).hasClass('btn-modify-count')){
                var count = $(this).parents('tr').find('input[name="count"]').val();
                _this.modifyCount(cartId, count);
            }
        });

        _this.div.find('#cart-items').on('click', '.itemInfo', function(){
            var itemId = $(this).find('input[name="itemId"]').val();
            //console.log(cartId);
            itemDetail.init(itemId);
        });

        _this.div.find('#cart-items').on('click', '.itemCount .btn', function(){
            //console.log(this);
            $(this).adjustCount();
        });

        _this.div.find('#btn-order').unbind('click').bind('click', function(){
            var cartItems = [];
            _this.div.find('#cart-items tr').each(function() {
                if($(this).find('.cartItemChk').prop('checked')){
                    cartItems.push({
                        "id" : $(this).find("input[name='id']").val(),
                        "itemName" : $(this).find(".itemInfo").text(),
                        "count" : $(this).find("input[name='count']").val(),
                        "oriPrice" : $(this).find(".itemPrice .oriPrice").text(),
                        "salePrice" : $(this).find(".itemPrice .salePrice").text()
                    });
                }
            });
            //console.log(cartItems);
            newOrder(cartItems);
        });

    },
    load : function(){
        var _this = this;
        const uri = '/api/mall/member/' + _this.userId + '/cart/list';
        $.ajax({
            type: 'GET',
            url: uri,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var data = response.response;
            //console.log(data);
            $('#cart-items').empty();
            $.each(data, function(){
                var cartItem = this;
                var option = cartItem.itemOption;
                var display = option.itemDisplay;
                var countEl = $("<span class='count'/>")
                    .append($("<input class='mr-2' type='text' name='count' style='width: 20px' readonly/>").val(cartItem.count))
                    .append($("<button class='btn plus p-0 mr-2''>+</button>"))
                    .append($("<button class='btn minus p-0 ''>-</button>"));

                const row = $('<tr/>');
                const chkbox = $('<input class="cartItemChk" type="checkbox"/>')
                    .prop('name', 'id')
                    .prop('checked', true)
                    .val(cartItem.id);

                const itemLink = $('<a href="" onclick="return false;"/>')
                    .append($('<input type="hidden"/>')
                            .prop('name', 'itemId')
                            .val(display.id))
                    .append($('<img class="thumbnail mr-3"/>')
                        .prop('src', display.itemMaster.thumbnail))
                    .append(display.itemDisplayName)
                    .append($('<br/>'))
                    .append('\n' + option.color + '/' + option.size);

                const btn = '<a class="btn btn-sm btn-outline-dark"/>';

                const btnTd = $('<td class="btnTd"/>');
                    btnTd.append($(btn).addClass('btn-modify-count').text('수량변경'));
                    btnTd.append($(btn).addClass('btn-delete').text('삭제'))

                const oriPrice = $('<div class="discount oriPrice"/>').text(display.itemMaster.price * cartItem.count);
                const salePrice = $('<div class="salePrice"/>').text(display.salePrice * cartItem.count);

                row
                    .append($('<td/>').append(chkbox))
                    .append($('<td class="itemInfo"/>').append(itemLink))
                    .append($('<td class="itemCount"/>').append(countEl))
                    .append($('<td class="itemPrice"/>').append(oriPrice).append(salePrice))
                    .append(btnTd);

                $('#cart-items').append(row);
            });
        }).fail(function (error) {
            if(_this.userId === undefined){
                alert('로그인 후 이용해 주세요.');
            } else {
                alert('오류가 발생했습니다. 관리자에게 문의하십시오. \n' + error.message );
            }
            location.href='/mall/login';
        });
    },
    delete : function (id){
        var _this = this;
        $.ajax({
            type: 'DELETE',
            url: '/api/mall/member/' + _this.userId + '/cart/' + id
        }).done(function(response) {
            //console.log(response);
            _this.load();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    modifyCount : function (id, updatedCnt){
        var _this = this;
        $.ajax({
            type: 'PATCH',
            url: '/api/mall/member/' + _this.userId + '/cart/' + id +'?count=' + updatedCnt
        }).done(function(response) {
            //console.log(response);
            alert('변경되었습니다.');
            _this.load();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};