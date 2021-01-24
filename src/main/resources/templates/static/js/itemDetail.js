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
var itemDetail = {
    displayId : '',
    div : $("#div-sales-item-detail"),
    init : function(id){
        var _this = this;
        _this.displayId = id;
        $('#div-sales-item-list').addClass('d-none');
        $('#div-sales-item-detail').removeClass('d-none');
        _this.load();

        _this.div.find('.cateArea').unbind().bind('click', function(){
            main.cateId = $(this).find('input[name="categoryId"]').val();
            main.page = 1;
            $("#div-thumb").empty();
            main.loadCateItems(1);
            main.div.removeClass('d-none');
            _this.div.addClass('d-none');
        });
        _this.div.find('#btn-add-cart').click(function(){
            var optionId =_this.div.find('#div-item-detail-options #select-option').val();
            var isSoldOut = _this.div.find('#div-item-detail-options #select-option option[value="'+ optionId +'"]').find(".soldOut");
            if(isSoldOut.length > 0){
                alert('해당 옵션은 품절되었습니다.');
                return false;
            }
            loadCart(optionId);
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
        this.div.find('#div-item-summary #div-item-detail-options #select-option').empty();
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
            _this.div.find('#div-item-detail-image  #img-detail').prop("src", data.detailImage);
            _this.div.find('#div-item-summary #div-item-detail-name #p-name').text(data.itemName);
            _this.div.find('#div-item-summary #div-item-detail-price #p-price').text(data.itemPrice);
            var sizeInfo = data.sizeInfo.replaceAll('\n' , '<br>');
            _this.div.find('#div-item-summary #div-item-detail-size #p-size').text(sizeInfo);
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
