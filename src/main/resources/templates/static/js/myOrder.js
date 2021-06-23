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
var myOrderDetail = {
    div : $('#div-my-order-detail'),
    userId : '',
    orderId : '',
    paymentUid : '',
    init : function(userId, orderId) {
        var _this = this;
        _this.userId = userId;
        _this.orderId = orderId;
        _this.load();
        $('.contentDiv').not(_this.div).addClass('d-none');
        _this.div.removeClass('d-none');

        _this.div.find('#btn-update-delivery').unbind('click').click(function(){
            const deliveryId = _this.div.find('#form-update-delivery input[name="deliveryId"]').val();
            //console.log(deliveryId);
           _this.updateDelivery(deliveryId);
        });

        _this.div.find('#btn-edit-my-delivery').unbind('click').bind('click', function(){
            _this.div.find('.updateDelivery').removeClass('d-none');
            _this.div.find('.deliverInfo').addClass('d-none');
            _this.div.find('#btn-edit-my-delivery').addClass('d-none');
        });

        _this.div.find('#order-items').on('click', '.itemLink', function(){
            var itemId = $(this).find('input[name="itemDisplayId"]').val();
            itemDetail.init(itemId);
        });

        _this.div.find('#btn-cancel-order').unbind('click').bind('click', function(){
           _this.cancelOrder();
        });
    },
    cancelOrder : function(){
        const _this = this;
        const uri = '/api/mall/member/' + _this.userId + '/order/' + _this.orderId + '/cancel';
        $.ajax({
            type: 'PATCH',
            url: uri,
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var data = response.response;
            //console.log(data);
            alert('주문 취소되었습니다.');
            loadMyOrderDetail(_this.orderId);
            _this.div.find('.updateDelivery').addClass('d-none');
            _this.div.find('.deliverInfo').removeClass('d-none');
        }).fail(function (error) {
            if(_this.userId === undefined){
                alert('로그인 후 이용해 주세요.');
            } else {
                alert('오류가 발생했습니다. 관리자에게 문의하십시오. \n' + error.message );
            }
            location.href='/mall/login';
        });
    },
    updateDelivery : function(deliveryId){
        const _this = this;
        const form = _this.div.find('#form-update-delivery');
        const receiverTel = form.find('#receiverTel1').val() + '-' + form.find('#receiverTel2').val() + '-' + form.find('#receiverTel3').val();
        form.find('input[name="receiverTel"]').val(receiverTel);
        const data = form.serializeObject();
        //console.log(data);
        const uri = '/api/mall/member/' + _this.userId + '/order/' + _this.orderId + '/delivery/' +deliveryId;
        $.ajax({
            type: 'PUT',
            url: uri,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            var data = response.response;
            //console.log(data);
            alert('배송정보가 수정되었습니다.');
            loadMyOrderDetail(_this.orderId);
            _this.div.find('.updateDelivery').addClass('d-none');
            _this.div.find('.deliverInfo').removeClass('d-none');
        }).fail(function (error) {
            if(_this.userId === undefined){
                alert('로그인 후 이용해 주세요.');
            } else {
                alert('오류가 발생했습니다. 관리자에게 문의하십시오. \n' + JSON.stringify(error) );
            }
            location.href='/mall/login';
        });
    },
    clear : function(){
        this.div.find('#order-items').empty();
        const amountDiv = this.div.find('.amountInfo');
        amountDiv.find('span').text('');
        const orderDiv = this.div.find('.orderInfo');
        orderDiv.find('span').text('');
        const deliverDiv = this.div.find('.deliverInfo');
        deliverDiv.removeClass('d-none');
        deliverDiv.find('#receiverName').text('');
        deliverDiv.find('#receiverTel').text('');
        deliverDiv.find('#receiverAddr').text('');
        deliverDiv.find('#message').text('');
        this.div.find('#btn-edit-my-delivery').addClass('d-none');
        this.div.find('.updateDelivery').addClass('d-none');
        const deliverForm = this.div.find('#form-update-delivery');
        deliverForm.find('input[name="deliveryId"]').val('');
        deliverForm.find('#receiverName').val('');
        deliverForm.find('#receiverTel').val('');
        deliverForm.find('#receiverAddr1').val('');
        deliverForm.find('#receiverAddr2').val('');
        deliverForm.find('#message').val('');
    },
    makeItemRow : function(item){
      let row = $('<tr/>');

      const itemLink = $('<a class="itemLink" href="" onclick="return false;"/>')
            .append($('<input type="hidden"/>')
                .prop('name', 'itemDisplayId')
                .val(item.itemDisplayId))
            .append($('<img class="thumbnail mr-3"/>')
                .prop('src', item.thumbnail))
            .append(item.itemName)
            .append($('<br/>'))
            .append('\n' + item.color + '/' + item.size);

      const reviewChk = item.reviewId === '' ? '리뷰 작성' : '리뷰 수정';
      const reviewLink = item.deliveryStatus !== '배송완료' ?
          '' :
          $('<a class="btn-review" href="" data-toggle="modal" data-target="#reviewModal"/>')
              .append('<input type="hidden" name="orderItemId" value="' + item.orderItemId +'" />')
              .append('<input type="hidden" name="userId" value="' + this.userId +'"/>')
              .append('<input type="hidden" name="reviewId" value="' + item.reviewId +'"/>')
              .append(reviewChk);
      row
          .append($('<td class="itemInfo"/>').append(itemLink))
          .append($('<td class="itemCount"/>').append(item.count))
          .append($('<td class="itemPrice"/>').append(item.itemPrice))
          .append($('<td class="deliveryStatus"/>').append(item.deliveryStatus + '\n' + item.invoiceNum))
          .append($('<td class="reviewBtn"/>').append(reviewLink));

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
            amountDiv.find('#couponDiscountedAmount').text(order.couponDiscountedAmount);
            amountDiv.find('#usedPoints').text(order.usedPoints);
            const payInfo = pay.load(order.paymentUid);
            amountDiv.find('.payInfo #payMethod').text(payInfo.payMethod);
            _this.paymentUid = order.paymentUid;
            const orderDiv = _this.div.find('.orderInfo');
            orderDiv.find('#orderId').text(order.orderId);
            orderDiv.find('#orderDate').text(order.orderDate);
            orderDiv.find('#orderName').text(order.orderName);
            orderDiv.find('#orderStatus').text(order.orderStatus);
            const deliverDiv = _this.div.find('.deliverInfo');
            deliverDiv.find('#receiverName').text(order.deliveryName);
            deliverDiv.find('#receiverTel').text(order.deliveryTel);
            deliverDiv.find('#receiverAddr').text(order.deliveryAddr1 + ', ' + order.deliveryAddr2);
            deliverDiv.find('#message').text(order.deliveryMessage);

            if(order.orderStatus === '배송준비중' || order.orderStatus === '결제완료'){
                const deliverForm = _this.div.find('#form-update-delivery');
                deliverForm.find('input[name="deliveryId"]').val(order.deliveryId);
                deliverForm.find('#receiverName').val(order.deliveryName);
                deliverForm.find('#receiverTel').val(order.deliveryTel);
                const splitedTel = order.deliveryTel.split('-');
                deliverForm.find('#receiverTel1').val(splitedTel[0]);
                deliverForm.find('#receiverTel2').val(splitedTel[1]);
                deliverForm.find('#receiverTel3').val(splitedTel[2]);
                deliverForm.find('#receiverAddr1').val(order.deliveryAddr1);
                deliverForm.find('#receiverAddr2').val(order.deliveryAddr2);
                deliverForm.find('#message').val(order.deliveryMessage);
                _this.div.find('#btn-edit-my-delivery').removeClass('d-none');
            } else {
                orderDiv.find('#btn-cancel-order').addClass('d-none');
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

function delivery_update_execDaumPostcode() {
    new daum.Postcode({
        popupName: 'deliveryUpdatePostcodePopup',
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            // 주소 정보를 해당 필드에 넣는다.
            var input = document.querySelector('#div-my-order-detail .updateDelivery #receiverAddr1');
            input.value = roadAddr;
        }
    }).open();
}