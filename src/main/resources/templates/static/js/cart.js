function loadCart(optionId) {
    var userId = $('.loginInfo').val();
    // TODO 수량 설정기능
    addCart(userId, optionId, 1);
    $('#div-sales-item-detail').addClass('d-none');
    $('#div-cart').removeClass('d-none');
}
function addCart(userId, optionId, count){
    if(optionId === undefined){
        alert('옵션을 확인해 주세요.');
        return false;
    }
    var data = {
        "itemId" : optionId,
        "count" : count
    };
    $.ajax({
        type: 'POST',
        url: '/api/mall/member/'+ userId +'/cart',
        dataType: 'json',
        contentType:'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).done(function(response) {
        //console.log(response);
        cart.init(userId);
    }).fail(function (error) {
        alert(JSON.stringify(error));
        if(userId === undefined){
            alert('로그인 후 이용해 주세요.');
            location.href='/mall/login'
            return false;
        }
    });
}

var cart = {
    div : $('#div-cart'),
    userId : '',
    init : function(userId){
        var _this = this;
        _this.userId = userId;
        _this.load();
        _this.div.off().on('click', '.btn-delete', function(){
           var cartId = $(this).parents('tr').find('input[name="id"]').val();
            //console.log(cartId);
           _this.delete(cartId);
        });
    },
    load : function(){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/member/' + _this.userId + '/cart/list',
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
                const row = '<tr>'
                    + '<td>'
                    +   '<input type="checkbox" class="cartItemChk"  name="id" value="' + cartItem.id + '" selected />'
                    + '</td>'
                    + '<td class="itemInfo">'
                    + '  <a>'
                    + '    <img class="thumbnail mr-3" src="'+ display.itemMaster.thumbnail +'">'
                    +      display.itemDisplayName
                    + '    </br>'
                    + '    옵션 : ' + option.color + '/' + option.size
                    + '  </a>'
                    +'</td>'
                    + '<td class="itemCount">'+ cartItem.count +'</td>'
                    + '<td class="itemCount">'+ cartItem.count * display.salePrice +'</td>'
                    + '<td>'
                    + '  <a class="btn btn-sm btn-outline-dark btn-modify-count">수량변경</a>'
                    + '  <a class="btn btn-sm btn-outline-dark btn-delete">삭제</a>'
                    + '</td>'
                    + '</tr>';
                $('#cart-items').append(row);
            });
        }).fail(function (error) {
            alert(JSON.stringify(error));
            if(_this.userId === undefined){
                alert('로그인 후 이용해 주세요.');
                location.href='/mall/login'
            }
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
    }
};