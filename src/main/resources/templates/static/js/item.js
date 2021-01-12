var main = {
    firstPage: 1,
    lastPage: 5,
    init : function() {
        var _this = this;
        _this.loadAllItems(1);

        $(document).on('click', '#btn-load-more-items', function(){
            var link = $(this).text();
            if(link === 'load'){
                _this.firstPage = _this.firstPage + 5;
                _this.lastPage = _this.lastPage + 5;
                _this.loadAllItems(_this.firstPage);
            } else {
                _this.loadAllItems(link);
            }
        });
    },
    loadAllItems : function (page){
        $.ajax({
            type: 'GET',
            url: '/api/mall/item/list?page=' + page +'&size=' + 5 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var data = response.response;
            var div = $("#div-thumb");
            $.each(data.content, function(){
                //console.log(this);
                var thumb =
                    '<div class="itemThumb">' +
                    '    <div>' +
                    '        <a href="#">' +
                    '             <div>' +
                    '                 <img src='+ this.thumbnail +' id="thumb-img">' +
                    '             </div>' +
                    '             <div>' +
                    '                 <input type="hidden" class="displayId" value="'+ this.displayId +'"/>' +
                    '                 <span class="itemText itemName" id="thumb-name">' + this.itemName + '</span>' +
                    '                 <span class="itemText itemPrice discount">' + this.originalPrice + '</span>' +
                    '                 <span class="itemText itemPrice" id="thumb-price">' + this.itemPrice + '</span>' +
                    '             </div>' +
                    '        </a>' +
                    '    </div>' +
                    '</div>';
                div.append(thumb);
            });
            if(data.totalPages > 1){
                var button = '<span id = "btn-load-more-items" class="btn-outline-info">Load More</span>';
                $("#div-load-more-items").append(button);
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }

};
main.init();