var main = {
    page: 1,
    init : function() {
        var _this = this;
        _this.loadAllItems(1);

        $(document).on('click', '#btn-load-more-items', function(){
            _this.page = _this.page + 1;
            _this.loadAllItems(_this.page);
        });
    },
    loadAllItems : function (page){
        $.ajax({
            type: 'GET',
            url: '/api/mall/item/list?page=' + page +'&size=' + 3 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var data = response.response;
            var div = $("#div-thumb");
            console.log(data);
            $.each(data.content, function(){
                var onClick = "loadDetail('"+ this.displayId +"');";
                var thumb =
                    '<div class="itemThumb" onclick="'+ onClick +'">' +
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
            var loadMoreDiv = $("#div-load-more-items");
            loadMoreDiv.empty();
            if(data.totalPages > page){
                var button = '<span id = "btn-load-more-items" class="btn btn-lg btn-outline-secondary">Load More</span>';
                loadMoreDiv.append(button);
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }

};
main.init();