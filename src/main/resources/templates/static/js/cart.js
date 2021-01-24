function loadCart(optionId) {
    console.log(optionId);
    var userId = $('.loginInfo').val();

    console.log(userId);
    cart.init(optionId, userId);
}
var cart = {
    optionId : '',
    userId : '',
    init : function(optionId, userId){
        var _this = this;
        _this.optionId = optionId;
        _this.userId = userId;
        if(_this.optionId === ''){
            alert('옵션을 확인해 주세요.');
            return false;
        }
        _this.load();
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
            console.log(response);
            /*
            $('#div-sub-cates').empty();
            $.each(data, function(){
                var cate = this;
                var el = '<a class="sub mr-3" href="" onclick="return false;">' + cate.nameKor +
                    '<input type="hidden" name="id" value="' + cate.id +'"' +
                    '</a>';
                $('#div-sub-cates').append(el);
            });*/
        }).fail(function (error) {
            alert(JSON.stringify(error));

            if(_this.userId === undefined){
                alert('로그인 후 이용해 주세요.');
                location.href='/mall/login'
            }
        });
    }
};