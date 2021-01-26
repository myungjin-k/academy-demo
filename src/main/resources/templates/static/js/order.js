function newOrder(items) {
    var userId = $('.loginInfo').val();
    order.init(userId, items);
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

        _this.div.find('#btn-order-process').click(function(){
           _this.save();
        });
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
            var data = response.response;
            //console.log(data);
            var ordererInfo = _this.div.find('.ordererInfo');
            ordererInfo.find('input[name="orderName"]').val(data.name);
            var tel = data.tel.split('-');
            ordererInfo.find('#orderTel1').val(tel[0]);
            ordererInfo.find('#orderTel2').val(tel[1]);
            ordererInfo.find('#orderTel3').val(tel[2]);
            ordererInfo.find('input[name="addr1"]').val(data.addr1);
            ordererInfo.find('input[name="addr2"]').val(data.addr2);
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
            alert('주문이 완료되었습니다.');
        }).fail(function (error) {
            alert(JSON.stringify(error));
            if(_this.userId === undefined){
                alert('로그인 후 이용해 주세요.');
                location.href='/mall/login'
            }
        });
    }
};