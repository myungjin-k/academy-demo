var main = {
    init : function() {
        reviewList.init();
        /*$('#div-review-list').off('click').on('click', '.reviewLink', function(){
            var reviewId = $(this).text();
            $('#div-review-list').removeClass('active');
            $('#div-review-detail').addClass('active').addClass('show');

            $('#tab-review-list').removeClass('active').prop('aria-selected', false);
            $('#tab-review-detail').show().addClass('active').prop('aria-selected', true);
            reviewDetail.init(reviewId);
        });*/
    }
}
// TODO 리뷰 코멘트 CRUD 프론트 구현
let reviewList = {
    firstPage: 1,
    lastPage: 5,
    div : $('#div-review-list'),
    init : function () {
        var _this = this;
        _this.div.on('click', '#pagination-review .page-link', function(){
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
        /*_this.div.find('#btn-search-review').click(function (){
            _this.firstPage = 1;
            _this.lastPage = 5;
            _this.list(_this.firstPage);
        });*/

        _this.loadAll();

        _this.div.off('click').on('click', '.reviewLink', function(){
           const reviewId = $(this).parents('tr').find('.reviewId').text();
           goReviewDetail(reviewId);
        });

    },
    clearTable : function(){
        $('#reviews').empty();
    },
    loadAll : function (){
        var _this = this;
        this.clearTable();
        $.ajax({
            type: 'GET',
            url: '/api/admin/review/list?&page=1&size=' + 5 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var resultData = response.response;
            //console.log(resultData);
            if(resultData.totalElements > 0){
                $('#pagination-review').setPagination(
                    _this.currPage,
                    _this.firstPage,
                    Math.min(resultData.totalPages, _this.lastPage),
                    5,
                    resultData.totalPages
                );
                $.each(resultData.content, function(){
                    //console.log(this);
                    const review = this;
                    const row = '<tr>'
                        + '<td class="reviewId">' + '<a class="reviewLink" href="" onclick="return false;">' + review.id + '</a>' +'</td>'
                        + '<td class="reviewItem">' + review.itemInfo +'</td>'
                        + '<td class="content">' + review.content.split('\n')[0] +'</td>'
                        + '<td class="score">' + review.score +'</td>'
                        + '<td class="createAt">' + review.createAt +'</td>'
                        + '</tr>';
                    $('#reviews').append(row);
                });
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    list : function (page){
        var _this = this;
        this.clearTable();
        var param = $('#input-search-review').val();
        $.ajax({
            type: 'GET',
            url: '/api/admin/review/search?reviewId='+ param +'&page=' + page +'&size=' + 5 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var resultData = response.response;
            //console.log(resultData);
            if(resultData.totalElements > 0){
                $('#pagination-review').setPagination(
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
                        + '<td class="reviewId">' + '<a class="reviewLink" href="" onclick="return false;">' + item.reviewId + '</a>' +'</td>'
                        + '<td class="itemName">' + item.itemName +'</td>'
                        + '<td class="option">' + item.color + '/' + item.size +'</td>'
                        + '<td class="count">' + item.count +'</td>'
                        + '<td class="deliveryStatus">' + item.deliveryStatus +'</td>'
                        + '</tr>';
                    $('#reviews').append(row);
                });
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }
};
function goReviewDetail(reviewId){
    $('.contentDiv').not('#div-review-detail').removeClass('active');
    $('#div-review-detail').addClass('active').addClass('show');

    $('.contentTab').not('#tab-review-detail').removeClass('active').prop('aria-selected', false);
    $('#tab-review-detail').show().addClass('active').prop('aria-selected', true);

    reviewDetail.init(reviewId);
}
let reviewDetail = {
    div : $('#div-review-detail'),
    reviewId : '',
    init : function(reviewId){
        const _this = this;
        _this.reviewId = reviewId;
        _this.load();
    },
    load : function(){
         const _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/admin/review/' + _this.reviewId,
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var review = response.response;
            console.log(review);
            const reviewInfo = _this.div.find('.reviewInfo');
            reviewInfo.find('#reviewId').text(review.id);
            reviewInfo.find('#writerUserId').text(review.writerUserId);
            reviewInfo.find('input[name="writerId"]').val(review.writerId);
            reviewInfo.find('#itemInfo').text(review.itemInfo);
            reviewInfo.find('#score').text(review.score);
            reviewInfo.find('#createAt').text(review.createAt);
            const content = review.content.replaceAll('\n', '<br/>');
            reviewInfo.find('#content').text(content);

            const commentInfo = _this.div.find('.commentInfo');
            if(review.comments.length > 0){
                $(review.comments).each(function(){
                    const comment = this;

                    let row = $('<tr />')
                        .append($('<input type="hidden" name="commentId"/>').val(comment.id))
                        .append($('<td width="10%" class="adminId"/>').text(comment.writer.adminId))
                        .append($('<td class="content"/>').text(comment.content))
                        .append($('<td width="10%" class="createAt"/>').text(comment.createAt));

                    commentInfo.find('#review-comments').append(row);
                });
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
main.init();