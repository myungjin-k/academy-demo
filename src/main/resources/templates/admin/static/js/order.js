var main = {
    init : function() {
        deliveryItem.init();
        //delivery.init();

        /*$(document).on('click', '.btn-search-codes', function(){
            var id = $(this).parents('tr').find('input[name="id"]').val();
            $('#div-code-group').removeClass('active');
            $('#div-code').addClass('active').addClass('show');

            $('#tab-code-group').removeClass('active').prop('aria-selected', false);
            $('#tab-code').show().addClass('active').prop('aria-selected', true);
            commonCode.list(id, 1);
        });*/
    }
}

//TODO 배송상태변경
//TODO 배송상품 추가, 삭제
//TODO 배송 추가, 삭제
var deliveryItem = {
    firstPage: 1,
    lastPage: 5,
    init : function () {
        var _this = this;
        $(document).on('click', '#pagination-delivery-item .page-link', function(){
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
        $('#btn-search-delivery-item').click(function (){
            _this.firstPage = 1;
            _this.lastPage = 5;
            _this.list(_this.firstPage);
        });
    },
    clearTable : function(){
        $('#deliveryItems').empty();
    },
    list : function (page){
        var _this = this;
        this.clearTable();
        var param = $('#input-search-delivery-item').val();
        $.ajax({
            type: 'GET',
            url: '/api/admin/order/search?orderId='+ param +'&page=' + page +'&size=' + 5 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var resultData = response.response;
            console.log(resultData);
            if(resultData.totalElements > 0){
                $('#pagination-order').setPagination(
                    page,
                    _this.firstPage,
                    Math.min(resultData.totalPages, _this.lastPage),
                    5,
                    resultData.totalPages
                );
                $.each(resultData.content, function(){
                    //console.log(this);
                    const item = this;
                    const row = '<tr>'
                        + '<input type="hidden" name="id" value="' + item.deliveryItemId + '"/>'
                        + '<td class="orderId">' + item.orderId +'</td>'
                        + '<td class="itemName">' + item.itemName +'</td>'
                        + '<td class="option">' + item.color + '/' + item.size +'</td>'
                        + '<td class="count">' + item.count +'</td>'
                        + '<td class="deliveryStatus">' + item.deliveryStatus +'</td>'
                        + '<td><a class="btn btn-sm btn-outline-dark btn-modify-status">배송정보변경</a></td>'
                        + '</tr>';
                    $('#deliveryItems').append(row);
                });
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    save : function (){
        var _this = this;
        var data = $('#form-save-group').serializeObject();
        $.ajax({
            type: 'POST',
            url: '/api/admin/codeGroup',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            //console.log(response);
            _this.list(1);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    setData : function(data){
        var form = $('#form-save-group');
        form.find('input[name="id"]').val(data.id);
        form.find('input[name="code"]').val(data.code);
        form.find('input[name="nameEng"]').val(data.nameEng);
        form.find('input[name="nameKor"]').val(data.nameKor);
    },
    update : function (){
        var _this = this;
        var data = $('#form-save-group').serializeObject();
        $.ajax({
            type: 'PUT',
            url: '/api/admin/codeGroup/' + data.id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            //console.log(response);
            _this.list(1);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    delete : function (id){
        var _this = this;
        $.ajax({
            type: 'DELETE',
            url: '/api/admin/codeGroup/' + id
        }).done(function(response) {
            //console.log(response);
            _this.list(1);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }
};
main.init();