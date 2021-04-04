function loadDetail(id, categoryId, categoryName, parentCategoryId, parentCategoryName){
    var breadcrumb = $('#breadcrumb');
    if(parentCategoryId !== "null"){
        breadcrumb.find('.main .categoryTitle').text(parentCategoryName);
        breadcrumb.find('.main input[name="categoryId"]').val(parentCategoryId);
        breadcrumb.find('.sub .categoryTitle').text(categoryName);
        breadcrumb.find('.sub input[name="categoryId"]').val(categoryId);
        breadcrumb.find('.sub').removeClass('d-none');
    } else {
        breadcrumb.find('.sub').addClass('d-none');
        breadcrumb.find('.main .categoryTitle').text(categoryName);
        breadcrumb.find('.main input[name="categoryId"]').val(categoryId);
    }
    itemDetail.init(id);
}
var review = {
    div : $('#div-item-review'),
    itemId : '',
    currPage : 1,
    firstPage : 1,
    lastPage : 5,
    init : function(itemId){
        const _this = this;
        _this.itemId = itemId;
        _this.load(_this.currPage);
        _this.div.find('.reviewDetail').addClass('d-none');

        _this.div.on('click', '#pagination-review .page-link', function() {
            var link = $(this).text();
            if (link === 'prev') {
                _this.firstPage = _this.firstPage - 5;
                _this.lastPage = _this.lastPage - 5;
                _this.list(_this.firstPage);
            } else if (link === 'next') {
                _this.firstPage = _this.firstPage + 5;
                _this.lastPage = _this.lastPage + 5;
                _this.list(_this.firstPage);
            } else {
                _this.list(link);
            }
        });

        _this.div.off('click').on('click', '.reviewTitle', function(){
           const reviewDetailRow = $(this).parents('tr').next();
           //console.log(reviewDetailRow.html());
           if(reviewDetailRow.hasClass('d-none')){
               reviewDetailRow.removeClass('d-none');
           } else {
               reviewDetailRow.addClass('d-none');
           }
        });
    },
    clear : function (){
        this.div.find('#table-review-list').addClass('d-none');
        this.div.find('#no-review').removeClass('d-none');
        this.div.find('#item-reviews').empty();
    },
    load : function (page){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/item/' + _this.itemId + '/review/list?page=' + page + '&size=5&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            _this.clear();
            _this.currPage = page;
            const resultData = response.response;
            if(resultData.totalElements > 0) {
                _this.div.find('#table-review-list').removeClass('d-none');
                _this.div.find('#no-review').addClass('d-none');
                $('#pagination-review').setPagination(
                    page,
                    _this.firstPage,
                    Math.min(resultData.totalPages, _this.lastPage),
                    5,
                    resultData.totalPages
                );
                $.each(resultData.content, function () {
                    //console.log(this);
                    const review = this;
                    const listRow = $('<tr class="text-left reviewTitle"/>');
                    const reviewTitle = $('<a class="reviewTitle" href="" onclick="return false;" />')
                        .append(review.content.split('\n')[0]);
                    listRow.append($('<input type="hidden" name="id" />').val(review.id))
                        .append($('<td class="score"/>').append(review.score))
                        .append($('<td class="abbr"/>').append(reviewTitle))
                        .append($('<td class="writer"/>').append(review.writerUserId));

                    const detailRow = $('<tr class="text-left reviewDetail d-none"/>');
                    const detailTd = $('<td class="text-left" colspan="3"/>');
                    const optionDiv = $('<div class="reviewOption text-info"/>').append('옵션 : ').append(review.optionInfo);
                    const commentListDiv = $('<div class="commentList" />');
                    $.each(review.comments, function(){
                       const comment = this;
                       const commentDiv = $('<div class="comment row" />')
                           .append($('<input type="hidden" name="id"/>').val(comment.id))
                           .append($('<div class="col-2" />').append($('<span class="writer" />').append(comment.writer.adminId)))
                           .append($('<div class="col" />').append($('<div class="content" />').append(comment.content)));
                        commentListDiv.append(commentDiv);
                    });
                    const fullContentDiv = $('<div class="reviewContent"/>')
                        .append(review.content.replaceAll('\n', '<br/>'))
                        .append($('<br/>'))
                        .append(($('<a class="reviewImg"/>')
                            .append($('<img style="width: 100%;"/>').prop('src', review.reviewImgUrl))
                        ))
                        .append(commentListDiv);
                    detailTd
                        .append(optionDiv)
                        .append(fullContentDiv);
                    detailRow
                        .append(detailTd);
                    _this.div.find('#item-reviews')
                        .append(listRow)
                        .append(detailRow);
                });
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
var itemDetail = {
    displayId : '',
    div : $("#div-sales-item-detail"),
    init : function(id){
        var _this = this;
        _this.displayId = id;
        $('.contentDiv').not(_this.div).addClass('d-none');
        _this.div.removeClass('d-none');
        _this.load();
        review.init(_this.displayId);

        _this.div.find('.cateArea').unbind().bind('click', function(){
            main.cateId = $(this).find('input[name="categoryId"]').val();
            main.page = 1;
            $("#div-thumb").empty();
            main.loadCateItems(1);
            main.div.removeClass('d-none');
            _this.div.addClass('d-none');
        });
        _this.div.find('#btn-add-cart').unbind().bind('click', function(){
            var addItemList = [];
            _this.div.find('#div-selected-result .selectedItem').each(function(){
                var id = $(this).find("input[name='optionId']").val();
                var count = $(this).find("input[name='count']").val();
                addItemList.push({"itemId" : id, "count" : count});
            })
            loadCart(addItemList);
        });
        _this.div.find('#div-item-detail-options #select-option').unbind().bind('change', function(){
            var optionId = $(this).val();
            var selected = $(this).find('option[value="'+ optionId +'"]');
            var info = selected.text();
            var isSoldOut = selected.find(".soldOut");
            if($('#div-selected-result').find('input[value="'+ optionId+'"]').length > 0)
                return false;
            if(optionId === undefined){
                alert('옵션을 선택해 주세요.');
                return false;
            }
            if(isSoldOut.length > 0){
                alert('해당 옵션은 품절되었습니다.');
                return false;
            }
            _this.addSelectedResult(optionId, info);
        });
        _this.div.find('#div-selected-result').off().on('click', '.btn', function(){
            $(this).adjustCount();
        });
    },
    clear : function(){
        this.div.find('#div-item-detail-thumbnail #img-thumbnail').prop("src", '');
        this.div.find('#div-item-detail-notice #p-notice').text('');
        this.div.find('#div-item-detail-description #text-description').val('');
        this.div.find('#div-item-detail-image #img-detail').prop("src", '');
        this.div.find('#div-item-summary #div-item-detail-name #p-name').text('');
        this.div.find('#div-item-summary #div-item-detail-price #p-price').text('');
        this.div.find('#div-item-summary #div-item-detail-size #p-size').text('');
        this.div.find('#div-item-summary #div-item-detail-material #p-material').text('');
        this.div.find('#div-item-summary #div-item-detail-options #select-option').empty().append("<option value='' />");
        this.div.find('#div-item-summary #div-selected-result').empty();
    },
    addSelectedResult : function(optionId, info){
        var el = $("<p class='selectedItem'/>");
        var idEl = $("<input type='hidden' name='optionId' value='"+ optionId +"'/>");
        var countEl = $("<span class='count float-right ml-2'/>")
            .append($("<input class='mr-2' type='text' name='count' value='1' style='width: 20px' readonly/>"))
            .append($("<button class='btn plus p-0 mr-2''>+</button>"))
            .append($("<button class='btn minus p-0 ''>-</button>"));
        el.append(info).append(countEl).append(idEl);
        $('#div-selected-result').append(el);
    },
    load : function(){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/item/' + _this.displayId,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var data = response.response;
            //console.log(data);
            _this.clear();
            _this.div.find('#div-item-detail-thumbnail #img-thumbnail').prop("src", data.thumbnail);
            _this.div.find('#div-item-detail-notice #p-notice').text(data.notice);
            _this.div.find('#div-item-detail-description #text-description').val(data.description);
            const temp = _this.div.find('#div-item-detail-description #text-description').prop("scrollHeight");
            _this.div.find('#div-item-detail-description #text-description').height(temp);
            _this.div.find('#div-item-detail-image  #img-detail').prop("src", data.detailImage);
            _this.div.find('#div-item-summary #div-item-detail-name #p-name').text(data.itemName);
            _this.div.find('#div-item-summary #div-item-detail-price #p-ori-price').text(data.originalPrice);
            _this.div.find('#div-item-summary #div-item-detail-price #p-price').text(data.itemPrice);
            var sizes = data.sizeInfo.split('\n');
            $.each(sizes, function(){
                _this.div.find('#div-item-summary #div-item-detail-size #p-size')
                    .append($('<div/>').append(this));
            });
            //_this.div.find('#div-item-summary #div-item-detail-size #p-size').text(sizeInfo);
            _this.div.find('#div-item-summary #div-item-detail-material #p-material').text(data.material);
            var optionsEl = _this.div.find('#div-item-summary #div-item-detail-options #select-option');
            $.each(data.options, function(){
                var optionName = this.color + "/" + this.size;
                if(this.soldOut)
                    optionName += '<span class="soldOut"> [품절]</span>';
                optionsEl.append("<option value='" + this.optionId + "'>" + optionName +"</option>");
            });
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
}
