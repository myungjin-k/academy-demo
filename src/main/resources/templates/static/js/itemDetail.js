function loadDetail(id){
    $('#div-sales-item-list').addClass('d-none');
    itemDetail.init(id);
}
var itemDetail = {
    displayId : '',
    div : $("#div-sales-item-detail"),
    init : function(id){
        var _this = this;
        _this.displayId = id;
        $('#div-sales-item-detail').removeClass('d-none');
        _this.load();
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
            console.log(data);
            _this.div.find('#div-item-detail-thumbnail #img-thumbnail').prop("src", data.thumbnail);
            _this.div.find('#div-item-detail-notice #p-notice').text(data.notice);
            _this.div.find('#div-item-detail-description #text-description').val(data.description);
            _this.div.find('#div-item-detail-image #img-detail').prop("src", data.detailImage);
            _this.div.find('#div-item-summary #div-item-detail-name #p-name').text(data.itemName);
            _this.div.find('#div-item-summary #div-item-detail-price #p-price').text(data.itemPrice);
            _this.div.find('#div-item-summary #div-item-detail-size #p-size').text(data.sizeInfo);
            _this.div.find('#div-item-summary #div-item-detail-material #p-material').text(data.material);
            var optionsEl = _this.div.find('#div-item-summary #div-item-detail-options #select-option');
            $.each(data.options, function(){
                optionsEl.append("<option value='" + this.optionId + "'>" + this.color + "/" + this.size +"</option>");
            });
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
}
