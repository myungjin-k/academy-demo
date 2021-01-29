function newOrder(items) {
    var userId = $('.loginInfo').val();
    if(items.length === 0){
        alert('주문할 상품이 없습니다.');
        return false;
    } else {
        order.init(userId, items);
    }
}
var order = {
    userId : '',
    items : [],
    div : $('#div-order'),
    totalAmount : 0,
    init : function(userId, items){
        var _this = this;
        _this.userId = userId;
        _this.items = items;
        _this.loadOrderInfo();
        _this.setCartItems();
        $('.contentDiv').not(_this.div).addClass('d-none');
        _this.div.removeClass('d-none');

        _this.div.find('.amountInfo .usePoints #usePoints').change(function(){
            var p =  Number($(this).val());
            var usableP = Number(_this.div.find('.amountInfo .usablePoints').text());
            if(p > usableP)
                p = usableP;
            $(this).val(p);
            _this.div.find('.amountInfo .usedPoints').text(p);
            _this.div.find('.amountInfo .totalAmount').text(_this.totalAmount - p);
        });

        _this.div.find('.deliverInfo #useOrdererInfo').change(function(){
            if($(this).prop('checked'))
                _this.copyOrdererInfo();
        });

        _this.div.find('#btn-order-process').unbind().bind('click', function(){
           _this.save();
        });
    },
    clearForm : function (){
        var _this = this;
        _this.div.find('#form-save-order input').val('');
        _this.div.find('#useOrdererInfo').prop('checked', false);
    },
    copyOrdererInfo : function(){
        var _this = this;
        var orderInfo = _this.div.find('.ordererInfo');
        var deliverInfo = _this.div.find('.deliverInfo');
        deliverInfo.find('input[name="receiverName"]').val(orderInfo.find('input[name="name"]').val());
        deliverInfo.find('input[name="receiverTel"]').val(orderInfo.find('input[name="orderTel"]').val());
        deliverInfo.find('#receiverTel1').val(orderInfo.find('#orderTel1').val());
        deliverInfo.find('#receiverTel2').val(orderInfo.find('#orderTel2').val());
        deliverInfo.find('#receiverTel3').val(orderInfo.find('#orderTel3').val());
        deliverInfo.find('input[name="receiverAddr1"]').val(orderInfo.find('input[name="addr1"]').val());
        deliverInfo.find('input[name="receiverAddr2"]').val(orderInfo.find('input[name="addr2"]').val());
    },
    setCartItems : function(){
        var orderItems = $('#order-items');
        orderItems.empty();
        var _this = this;
        var totalItemPrice = 0;
        var totalAmount = 0;
        $(_this.items).each(function(){
           var item = this;
           var tr = $('<tr/>');
           var itemName = item.itemName.replaceAll('\n','<br/> └');
           var oriPrice = $('<div class="discount oriPrice"/>').text(item.oriPrice);
           var salePrice = $('<div class="salePrice"/>').text(item.salePrice);
           tr
               .append($('<td class="itemName" />').append(itemName))
               .append($('<td class="count" />').append(item.count))
               .append($('<td class="price" />').append(oriPrice).append(salePrice))
            orderItems.append(tr);
            totalItemPrice += Number(item.oriPrice);
            totalAmount += Number(item.salePrice);
        });
        _this.div.find('.amountInfo .totalItemPrice').text(totalItemPrice);
        _this.div.find('.amountInfo .totalDiscountPrice').text(totalItemPrice - totalAmount);
        _this.div.find('.amountInfo .totalAmount').text(totalAmount);
        _this.totalAmount = totalAmount;
    },
    loadOrderInfo : function(){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/member/' + _this.userId + '/orderMemberInfo',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            _this.clearForm();
            var data = response.response;
            //console.log(data);
            var ordererInfo = _this.div.find('.ordererInfo');
            ordererInfo.find('input[name="name"]').val(data.name);
            var tel = data.tel.split('-');
            ordererInfo.find('#orderTel1').val(tel[0]);
            ordererInfo.find('#orderTel2').val(tel[1]);
            ordererInfo.find('#orderTel3').val(tel[2]);
            ordererInfo.find('input[name="addr1"]').val(data.addr1);
            ordererInfo.find('input[name="addr2"]').val(data.addr2);
            ordererInfo.find('input[name="email"]').val(data.email);
            _this.div.find('.amountInfo .usePoints .usablePoints').text(data.points);
            //_this.div.find('.amountInfo .usePoints input[name="usePoints"]').val(data.points);
            //_this.div.find('.amountInfo .usedPoints').text(data.points);
        }).fail(function (error) {
            alert(JSON.stringify(error));
            if(_this.userId === undefined){
                alert('로그인 후 이용해 주세요.');
                location.href='/mall/login'
            }
        });
    },
    collectItemIds : function(){
        var itemIds = [];
        var _this = this;
        $(_this.items).each(function(){
           itemIds.push(this.id);
        });
        return itemIds;
    },
    save : function(){
        var _this = this;
        var form = _this.div.find('#form-save-order');
        var tel = form.find('#orderTel1').val() + '-' + form.find('#orderTel2').val() + '-' + form.find('#orderTel3').val();
        form.find('input[name="tel"]').val(tel);
        var receiverTel = form.find('#receiverTel1').val() + '-' + form.find('#receiverTel2').val() + '-' + form.find('#receiverTel3').val();
        form.find('input[name="receiverTel"]').val(receiverTel);
        var data = $('#form-save-order').serializeObject();
        data['cartItemIds'] = _this.collectItemIds();
        console.log(data);

        $.ajax({
            type: 'POST',
            url: '/api/mall/member/' + _this.userId + '/order',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            var data = response.response;
            console.log(data);
            // TODO 결제api 추가
            alert('주문이 완료되었습니다.');
            loadMyOrderDetail(data.id);
        }).fail(function (error) {
            alert(JSON.stringify(error));
            if(_this.userId === undefined){
                alert('로그인 후 이용해 주세요.');
                location.href='/mall/login'
            }
        });
    }
};

function orderer_execDaumPostcode() {
    new daum.Postcode({
        popupName: 'ordererPostcodePopup',
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            // 주소 정보를 해당 필드에 넣는다.
            document.getElementById("addr1").value = roadAddr;
        }
    }).open();
}

function receiver_execDaumPostcode() {
    new daum.Postcode({
        popupName: 'receiverPostcodePopup',
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            // 주소 정보를 해당 필드에 넣는다.
            document.getElementById("receiverAddr1").value = roadAddr;
        }
    }).open();
}