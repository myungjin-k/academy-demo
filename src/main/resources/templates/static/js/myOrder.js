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
    firstPage: 1,
    lastPage: 5,
    currPage: 1,
    init : function(userId) {
        var _this = this;
        _this.userId = userId;
        $('.contentDiv').not(_this.div).addClass('d-none');
        _this.div.removeClass('d-none');
        _this.list(_this.currPage);


        /*$('#pagination-item-display-option').on('click', '.page-link', function(){
            var link = $(this).text();
            if(link === 'prev'){
                _this.firstPage = _this.firstPage - 5;
                _this.lastPage = _this.lastPage - 5;
                _this.list(_this.firstPage);
            } else if(link === 'next'){
                _this.firstPage = _this.firstPage + 5;
                _this.lastPage = _this.lastPage + 5;
                _this.list(_this.firstPage);
            } else {
                _this.list(link);
            }
        });*/
    },
    list : function (page){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/member/' + _this.userId + '/order/list?page=' + page + '&size=' + 5 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            console.log(response);
            const resultData = response.response;
            $('#pagination-order').setPagination(
                page,
                _this.firstPage,
                Math.min(resultData.totalPages, _this.lastPage),
                5,
                resultData.totalPages
            );

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
    clear : function(){
        _this.div.find('#order-items').empty();const amountDiv = _this.div.find('.amountInfo');
        amountDiv.find('#payAmount').text('');
        amountDiv.find('#orderAmount').text('');
        amountDiv.find('#discountedAmount').text('');
        amountDiv.find('#usedPoints').text('');
        const orderDiv = _this.div.find('.orderInfo');
        orderDiv.find('#orderId').text('');
        orderDiv.find('#orderDate').text('');
        orderDiv.find('#orderName').text('');
        orderDiv.find('#orderStatus').text('');
        const deliverDiv = _this.div.find('.deliverInfo');
        deliverDiv.find('#receiverName').text('');
        deliverDiv.find('#receiverTel').text('');
        deliverDiv.find('#receiverAddr').text('');
        deliverDiv.find('#message').text('');
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
            _this.clear();
            // TODO  ui 정리
            const items = order.items;
            const itemDiv = _this.div.find('#order-items');
            $(items).each(function(){
                const row = _this.makeItemRow(this);
                itemDiv.append(row);
            });
            const amountDiv = _this.div.find('.amountInfo');
            amountDiv.find('#payAmount').text(order.payAmount);
            amountDiv.find('#orderAmount').text(order.orderAmount);
            amountDiv.find('#discountedAmount').text(order.discountedAmount);
            amountDiv.find('#usedPoints').text(order.usedPoints);
            const orderDiv = _this.div.find('.orderInfo');
            orderDiv.find('#orderId').text(order.orderId);
            orderDiv.find('#orderDate').text(order.orderDate);
            orderDiv.find('#orderName').text(order.orderName);
            orderDiv.find('#orderStatus').text(order.orderStatus);
            const deliverDiv = _this.div.find('.deliverInfo');
            deliverDiv.find('#receiverName').text(order.deliveryName);
            deliverDiv.find('#receiverTel').text(order.deliveryTel);
            deliverDiv.find('#receiverAddr').text(order.deliveryAddr);
            deliverDiv.find('#message').text(order.deliveryMessage);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};