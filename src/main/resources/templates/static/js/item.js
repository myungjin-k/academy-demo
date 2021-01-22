var main = {
    div : $('#div-sales-item-list'),
    page: 1,
    cateId: '',
    cateName: 'BEST',
    init : function() {
        var _this = this;
        _this.loadAllItems(1);

        $('.itemMenu').click(function(){
            $('#div-sales-item-detail').addClass('d-none');
            _this.div.removeClass('d-none');
            $("#div-thumb").empty();
            if($(this).hasClass('best')){
                _this.cateId = '';
                _this.cateName = 'BEST';
                _this.loadAllItems(1);
            } else {
                _this.cateId = $(this).find('input[name="id"]').val();
                _this.cateName = $(this).text();
                _this.setSubCateList();
                _this.loadCateItems(1);
            }
        });

        _this.div.on('click', '.sub', function(){
            _this.cateId = $(this).find('input[name="id"]').val();
            $("#div-thumb").empty();
            _this.loadCateItems(1);
        });

        _this.div.on('click', '#btn-load-more-items', function(){
            _this.page = _this.page + 1;
            if(_this.cateName === 'BEST'){
                _this.loadAllItems(_this.page);
            } else {
                _this.loadCateItems(_this.page);
            }
        });
    },
    setSubCateList : function(){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/category/' + _this.cateId +'/sub/list',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var data = response.response;
            $('#div-sub-cates').empty();
            $.each(data, function(){
                var cate = this;
                var el = '<a class="sub mr-3" href="" onclick="return false;">' + cate.nameKor +
                    '<input type="hidden" name="id" value="' + cate.id +'"' +
                    '</a>';
                $('#div-sub-cates').append(el);
            });
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    makeItemHtml : function(data, page){
        var div = $("#div-thumb");
        console.log(data.content);

        $.each(data.content, function(){
            var item = this;
            var onClick = "loadDetail('"+ item.displayId +"', '"+ item.categoryId +"', '"+ item.categoryName +"'" +
                ", '"+ item.parentCategoryId +"', '"+ item.parentCategoryName +"');";
            var thumb =
                '<div class="itemThumb" onclick="'+ onClick +'">' +
                '    <div>' +
                '        <a href="#">' +
                '             <div>' +
                '                 <img src='+ item.thumbnail +' id="thumb-img">' +
                '             </div>' +
                '             <div>' +
                '                 <input type="hidden" class="displayId" value="'+ item.displayId +'"/>' +
                '                 <span class="itemText itemName" id="thumb-name">' + item.itemName + '</span>' +
                '                 <span class="itemText itemPrice">' +
                '                     <span class="discount">' + item.originalPrice + '</span>' +
                '                     <span id="thumb-price">' + item.itemPrice + '</span>' +
                '                 </span>' +
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
    },
    loadAllItems : function (page){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/item/list?page=' + page +'&size=' + 3 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            _this.makeItemHtml(response.response, page);
            $('#title .main').text('BEST');
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    loadCateItems : function(page){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/category/'+ _this.cateId +'/item/list?page=' + page +'&size=' + 3 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            //console.log(response.response);
            _this.makeItemHtml(response.response, page);
            $('#title .main').text(_this.cateName);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

};
main.init();