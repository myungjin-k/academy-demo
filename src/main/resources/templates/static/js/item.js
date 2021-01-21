var main = {
    page: 1,
    init : function() {
        var _this = this;
        _this.loadAllItems(1);

        $('.mainCategory').click(function(){
            var id = $(this).find('input[name="id"]').val();
            _this.loadCateItems(id, 1);
        });

        $(document).on('click', '#btn-load-more-items', function(){
            _this.page = _this.page + 1;
            _this.loadAllItems(_this.page);
        });
    },
    makeItemHtml : function(data){
        var onClick = "loadDetail('"+ data.displayId +"', '"+ data.categoryId +"', '"+ data.categoryName +"');";
        var thumb =
            '<div class="itemThumb" onclick="'+ onClick +'">' +
            '    <div>' +
            '        <a href="#">' +
            '             <div>' +
            '                 <img src='+ data.thumbnail +' id="thumb-img">' +
            '             </div>' +
            '             <div>' +
            '                 <input type="hidden" class="displayId" value="'+ data.displayId +'"/>' +
            '                 <span class="itemText itemName" id="thumb-name">' + data.itemName + '</span>' +
            '                 <span class="itemText itemPrice">' +
            '                     <span class="discount">' + data.originalPrice + '</span>' +
            '                     <span id="thumb-price">' + data.itemPrice + '</span>' +
            '                 </span>' +
            '             </div>' +
            '        </a>' +
            '    </div>' +
            '</div>';
        return thumb;
    },
    loadAllItems : function (page){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/item/list?page=' + page +'&size=' + 3 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var data = response.response;
            var div = $("#div-thumb");
            div.empty();
            console.log(data);
            $.each(data.content, function(){
                div.append(_this.makeItemHtml(this));
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

    },
    loadCateItems : function(cateId, page){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/category/'+ cateId +'/item/list?page=' + page +'&size=' + 3 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var data = response.response;
            var div = $("#div-thumb");
            console.log(data);
            div.empty();
            $.each(data.content, function(){
                div.append(_this.makeItemHtml(this));
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