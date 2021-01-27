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
    div : $('#div-my-order-list'),
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
    load : function (){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/member/' + _this.userId + '/order/' + _this.orderId,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            console.log(response);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};