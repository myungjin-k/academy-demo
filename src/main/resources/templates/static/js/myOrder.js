function goMyOrder(){
    var userId = $('#loginUserId').val();
    myOrder.init(userId);
}
function loadMyOrderDetail(orderId){
    var userId = $('#loginUserId').val();
    myOrderDetail.init(userId, orderId);
}
var myOrder = {
    div : $('#div-my-order-list'),
    userId : '',
    init : function(userId) {
        var _this = this;
        _this.userId = userId;
        _this.load();
        $('.contentDiv').not(_this.div).addClass('d-none');
        _this.div.removeClass('d-none');
    },
    list : function (){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/' + _this.userId + '/order/list',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            console.log(response);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
var myOrderDetail = {
    div : $('#div-my-order-detail'),
    userId : '',
    orderId : '',
    init : function(userId, orderId) {
        var _this = this;
        _this.userId = userId;
        _this.orderId = orderId;
        _this.load();
        $('.contentDiv').not(_this.div).addClass('d-none');
        _this.div.removeClass('d-none');
    },
    makeItemRow : function(item){
      let row = $('<tr/>');

      const itemLink = $('<a href="" onclick="return false;"/>')
            .append($('<input type="hidden"/>')
                .prop('name', 'itemDisplayId')
                .val(item.itemDisplayId))
            .append($('<img class="thumbnail mr-3"/>')
                .prop('src', item.thumbnail))
            .append(item.itemName)
            .append($('<br/>'))
            .append('\n' + item.color + '/' + item.size);

        //const salePrice = $('<div class="salePrice"/>').text(display.salePrice * cartItem.count);

        row
            .append($('<td class="itemInfo"/>').append(itemLink))
            .append($('<td class="itemCount"/>').append(item.count))
            .append($('<td class="itemPrice"/>').append(item.itemPrice))
            .append($('<td class="deliveryStatus"/>').append(item.deliveryStatus + '\n' + item.invoiceNum));
        return row;
    },
    load : function (){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/member/' + _this.userId + '/order/' + _this.orderId,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            console.log(response);
            const order = response.response;
            _this.div.find('input[name="orderId"]').val(order.orderId);

            // TODO 정가 있어야함
            const items = order.items;
            const itemDiv = _this.div.find('#order-items');
            itemDiv.empty();
            $(items).each(function(){
                const row = _this.makeItemRow(this);
                itemDiv.append(row);
            });
            const orderDiv = _this.div.find('.orderInfo');
            orderDiv.find('#orderName').text(order.orderName);
            orderDiv.find('#orderTel').text(order.orderTel);
            const deliverDiv = _this.div.find('.deliverInfo');
            deliverDiv.find('#receiverName').text(order.deliveryName);
            deliverDiv.find('#receiverTel').text(order.deliveryTel);
            deliverDiv.find('#receiverAddr').text(order.deliveryAddr);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};