var main = {
    init : function() {
        reviewList.init();
        /*$('#div-admin-review-list').off('click').on('click', '.reviewLink', function(){
            var reviewId = $(this).text();
            $('#div-admin-review-list').removeClass('active');
            $('#div-admin-review-detail').addClass('active').addClass('show');

            $('#tab-admin-review-list').removeClass('active').prop('aria-selected', false);
            $('#tab-admin-review-detail').show().addClass('active').prop('aria-selected', true);
            reviewDetail.init(reviewId);
        });*/
    }
}
let reviewList = {
    firstPage: 1,
    lastPage: 5,
    currPage: 1,
    div : $('#div-admin-review-list'),
    init : function () {
        var _this = this;

        _this.search(1);
        _this.div.on('click', '#pagination-reviews .page-link', function(){
            var link = $(this).text();
            if(link === 'prev'){
                _this.firstPage = _this.firstPage - 5;
                _this.lastPage = _this.lastPage - 5;
                _this.search(_this.firstPage);
            } else if(link === 'next'){
                _this.firstPage = _this.firstPage + 5;
                _this.lastPage = _this.lastPage + 5;
                _this.search(_this.firstPage);
            } else {
                _this.search(link);
            }
        });
        /*_this.div.find('#btn-search-review').click(function (){
            _this.firstPage = 1;
            _this.lastPage = 5;
            _this.list(_this.firstPage);
        });*/

        _this.div.find('#btn-load-unreplied-reviews').unbind('click').bind('click', function(){
            _this.search(1, true);
        });

        _this.div.off('click').on('click', '.reviewLink', function(){
           const reviewId = $(this).parents('tr').find('.reviewId').text();
           goReviewDetail(reviewId);
        });

        _this.div.find('#div-review-search #btn-search-review').unbind('click').bind('click', function(){
            _this.search( 1, null);
        });

    },
    clearTable : function(){
        $('#reviews').empty();
    },
    setData: function (resultData){
        const _this = this;
        $('#pagination-reviews').setPagination(
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
    },
    search : function (page, isUnReplied){
        const _this = this;
        _this.clearTable();
        const param = {
            'reviewId' : _this.div.find('#div-review-search input[name="id"]').val(),
            'writerUserId' : _this.div.find('#div-review-search input[name="userId"]').val(),
            'replyStatus' : isUnReplied ? 'UNREPLIED' : ''
        }
        $.ajax({
            type: 'GET',
            url: '/api/admin/review/search?reviewId=' + param.reviewId + '&writerUserId=' + param.writerUserId
                + '&replyStatus=' +  param.replyStatus
                + '&page='+ page + '&size=' + 5 + '&direction=DESC',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            const resultData = response.response;
            console.log(resultData);
            _this.currPage = page;
            if(resultData.totalElements > 0){
                _this.setData(resultData);
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
function goReviewDetail(reviewId){
    $('.contentDiv').not('#div-admin-review-detail').removeClass('active');
    $('#div-admin-review-detail').addClass('active').addClass('show');

    $('.contentTab').not('#tab-admin-review-detail').removeClass('active').prop('aria-selected', false);
    $('#tab-admin-review-detail').show().addClass('active').prop('aria-selected', true);

    reviewDetail.init(reviewId);
}
let reviewDetail = {
    div : $('#div-admin-review-detail'),
    reviewId : '',
    isPhotoReview : false,
    init : function(reviewId){
        const _this = this;
        _this.reviewId = reviewId;
        _this.load();

        _this.div.find('.reviewInfo').off('click').on('click', '#btn-pay-reserves', function(){
            const amount = _this.isPhotoReview ? 1000 : 500;
            _this.payReserves(amount);
        });

        _this.div.find('#btn-save-new-comment').unbind('click').bind('click', function(){
            const data = {"content" : _this.div.find('#new-comment-content').val()};
           _this.saveComment(data);
           _this.loadComment();
        });

        _this.div.off('click').on('click', '#review-comments .deleteBtn', function(){
            const commentId = $(this).parents('tr').find('input[name="commentId"]').val();
           _this.deleteComment(commentId);
        });
    },
    clear : function (){
        const _this = this;
        _this.div.find('.commentInfo #review-comments').empty();
        const reviewInfo = _this.div.find('.reviewInfo');
        reviewInfo.find('.field').text('');
        reviewInfo.find('input.field').val('');
        reviewInfo.find('#reviewImg').prop('src', '');
    },
    appendComments : function(comments){
        const _this = this;
        _this.div.find('.commentInfo #review-comments').empty();
        $(comments).each(function(){
            const comment = this;
            const deleteBtn = $('<a class="deleteBtn" href="" onclick="return false;" />').text('삭제');
            let row = $('<tr />')
                .append($('<input type="hidden" name="commentId"/>').val(comment.id))
                .append($('<td width="10%" class="adminId"/>').text(comment.writer.adminId))
                .append($('<td class="content"/>').text(comment.content))
                .append($('<td width="10%" class="createAt"/>').text(comment.createAt))
                .append($('<td width="10%" class="btnTd"/>').append(deleteBtn));

            _this.div.find('.commentInfo #review-comments').append(row);
        });
    },
    load : function(){
         const _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/admin/review/' + _this.reviewId,
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var review = response.response;
            //console.log(review);
            _this.clear();
            const reviewInfo = _this.div.find('.reviewInfo');
            reviewInfo.find('#reviewId').text(review.id);
            reviewInfo.find('#writerUserId').text(review.writerUserId);
            reviewInfo.find('input[name="writerId"]').val(review.writerId);
            reviewInfo.find('#itemInfo').text(review.itemInfo);
            reviewInfo.find('#score').text(review.score);
            const reserveBtn = '<button class="btn btn-sm btn-primary" id="btn-pay-reserves">지급하기</button>';
            reviewInfo.find('#isReservesPaid').html(review.reservesPaid ? '지급완료' : reserveBtn);
            reviewInfo.find('#createAt').text(review.createAt);
            const content = review.content.replaceAll('\n', '<br/>');
            reviewInfo.find('#content').text(content);
            if(review.reviewImgUrl != null){
                reviewInfo.find('#reviewImg').prop('src', review.reviewImgUrl);
                _this.isPhotoReview = true;
            }
            if(review.comments.length > 0){
                _this.appendComments(review.comments);
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    payReserves : function(amount) {
        const _this = this;
        $.ajax({
            type : 'PATCH',
            url : '/api/admin/review/' + _this.reviewId + '/payReserves?amount=' + amount,
            contentType : 'application/json; charset=UTF-8',
            async: false
        }).done(function(response){
            //console.log(response.response);
            const data = {"content" : "소중한 리뷰 감사드립니다. 적립금 " + amount +"원 지급 도와드리겠습니다~!"};
            _this.saveComment(data);
            _this.load();
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },
    saveComment : function(data){
        const _this = this;
        //console.log(data);
        $.ajax({
            type : 'POST',
            url : '/api/admin/review/' + _this.reviewId + '/comment',
            contentType : 'application/json; charset=UTF-8',
            dataType : 'json',
            data : JSON.stringify(data),
            async: false
        }).done(function(response){
            //console.log(response.response);
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },
    loadComment : function(){
        const _this = this;
        $.ajax({
            type : 'GET',
            url : '/api/admin/review/' + _this.reviewId + '/comment/list',
            contentType : 'application/json; charset=UTF-8'
        }).done(function(response){
            //console.log(response.response);
            const comments = response.response;
            _this.appendComments(comments);
            _this.div.find('#new-comment-content').val('');
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },
    deleteComment : function(commentId){
        const _this = this;
        $.ajax({
            type : 'DELETE',
            url : '/api/admin/review/' + _this.reviewId + '/comment/' + commentId,
            contentType : 'application/json; charset=UTF-8'
        }).done(function(response){
            console.log(response.response);
            _this.loadComment();
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    }
};
main.init();