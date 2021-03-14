function goMyQna(){
    var userId = $('#loginUserId').val();
    myQna.init(userId);
}
var myQna = {
    div : $('#div-my-qna-list'),
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

        _this.div.find('#my-qnas').off('click').on('click', '.orderDetailLink', function () {
            const qnaSeq = $(this).find('.qnaSeq').text();
            //console.log(orderId);
            loadMyOrderDetail(orderId);
        });

        $('#pagination-qna').on('click', '.page-link', function(){
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
        this.div.find('#my-qnas').empty();
    },
    list : function (page){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/member/' + _this.userId + '/qna/list?page=' + page + '&size=' + 5 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            _this.clear();
            //console.log(response);
            const resultData = response.response;
            $('#pagination-qna').setPagination(
                page,
                _this.firstPage,
                Math.min(resultData.totalPages, _this.lastPage),
                5,
                resultData.totalPages
            );
            if(resultData.totalElements > 0){
                $.each(resultData.content, function(){
                    console.log(this);
                    const qna = this;
                    let row = $('<tr/>');

                    const detailLink = $('<a class="qnaDetailLink" href="" onClick="return false;" />')
                        .append('<span class="qnaTitle" >' + qna.title + '</span>');

                    const title = $('<td class="title" />')
                        .append(detailLink);
                    const category = $('<td class="category" />')
                        .append(qna.category);
                    const createAt =  $('<td class="createAt" />')
                        .append(qna.createAt);
                    const status = $('<td class="status" />')
                        .append(qna.status);

                    row.append($('<input type="hidden" name="qnaSeq" />').val(qna.seq))
                        .append(category)
                        .append(title)
                        .append(createAt)
                        .append(status);
                    $('#my-qnas').append(row);
                });
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};