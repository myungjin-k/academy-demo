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
        _this.loadMemberInfo();
        _this.list(_this.currPage);

        _this.div.find('#my-orders').off('click').on('click', '.orderDetailLink', function () {
            const orderId = $(this).find('.orderId').text();
            //console.log(orderId);
           loadMyOrderDetail(orderId);
        });

        $('#pagination-order').on('click', '.page-link', function(){
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
        });
    },
    clear : function (){
      this.div.find('#my-orders').empty();
    },
    clearMemberInfo : function(){
        const memberInfoDiv = this.div.find('.memberInfo');
        memberInfoDiv.find('.name').text('');
        memberInfoDiv.find('.currRating').text('');
        memberInfoDiv.find('.ratio').text('');
        memberInfoDiv.find('.nextRating').text('');
        memberInfoDiv.find('.remainingAmount').text('');
        memberInfoDiv.find('.orderedAmount').text('');
        memberInfoDiv.find('.reserves').text('');
    },
    loadMemberInfo : function(){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/member/' + _this.userId + '/ratingInfo',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            _this.clearMemberInfo();
            //console.log(response);
            const resultData = response.response;
            const memberInfoDiv = _this.div.find('.memberInfo');
            memberInfoDiv.find('.name').text(resultData.name);
            memberInfoDiv.find('.currRating').text(resultData.currRating);
            memberInfoDiv.find('.ratio').text(resultData.ratio);
            memberInfoDiv.find('.nextRating').text(resultData.nextRating);
            memberInfoDiv.find('.remainingAmount').text(resultData.remainingAmount);
            memberInfoDiv.find('.orderedAmount').text(resultData.orderedAmount);
            memberInfoDiv.find('.reserves').text(resultData.reserves);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    list : function (page){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/member/' + _this.userId + '/order/list?page=' + page + '&size=' + 5 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            _this.clear();
            //console.log(response);
            const resultData = response.response;
            $('#pagination-order').setPagination(
                page,
                _this.firstPage,
                Math.min(resultData.totalPages, _this.lastPage),
                5,
                resultData.totalPages
            );
            if(resultData.totalElements > 0){
                $.each(resultData.content, function(){
                    //console.log(this);
                    const order = this;
                    let row = $('<tr/>');
                    const detailLink = $('<a class="orderDetailLink" href="" onClick="return false;" />')
                        .append('[' + '<span class="orderId" >' + this.orderId + '</span>' + ']');
                    const orderInfo = $('<td class="orderInfo" />')
                        .append(this.orderDate)
                        .append('<br/>')
                        .append(detailLink);
                    const itemInfo = $('<td class="itemInfo" />')
                        .append(order.abbrItemName);
                    const price =  $('<td class="priceInfo" />')
                        .append(order.totalAmount);
                    const status = $('<td class="statusInfo" />')
                        .append(order.status);
                    row.append(orderInfo)
                        .append(itemInfo)
                        .append(price)
                        .append(status);
                    $('#my-orders').append(row);
                });
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
//TODO 배송준비 단계에서 배송정보 수정 기능
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
        this.div.find('#order-items').empty();
        const amountDiv = this.div.find('.amountInfo');
        amountDiv.find('#payAmount').text('');
        amountDiv.find('#orderAmount').text('');
        amountDiv.find('#discountedAmount').text('');
        amountDiv.find('#usedPoints').text('');
        const orderDiv = this.div.find('.orderInfo');
        orderDiv.find('#orderId').text('');
        orderDiv.find('#orderDate').text('');
        orderDiv.find('#orderName').text('');
        orderDiv.find('#orderStatus').text('');
        const deliverDiv = this.div.find('.deliverInfo');
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
            //console.log(response);
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