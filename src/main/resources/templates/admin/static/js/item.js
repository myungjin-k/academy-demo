var main = {
    init : function() {
        itemMaster.init();
        //itemOption.init();

        $(document).on('click', '.btn-search-item-options', function(){
            var id = $(this).parents('tr').find('input[name="id"]').val();
            $('#div-item-master').removeClass('active');
            $('#div-item-option').addClass('active').addClass('show');

            $('#tab-item-master').removeClass('active').prop('aria-selected', false);
            $('#tab-item-option').show().addClass('active').prop('aria-selected', true);
            itemOption.list(id, 1);
        });
    }
}

var itemMaster = {
    firstPage: 1,
    lastPage: 5,
    init : function() {
        var _this = this;
        $('#btn-load-item-masters').click(function () {
            _this.list(1);
        });
        $('#btn-add-item-master').click(function () {
            _this.clearForm();
        });
        $('#btn-save-item-master').click(function (){
            var id = $('#form-save-item-master').find('input[name="id"]').val();
            if(id !== ''){
                _this.update(_this.firstPage);
            } else {
                _this.save();
            }
        });
        $('#btn-delete-thumbnail').click(function(){
           _this.deleteThumbnail();
        });
        $(document).on('click', '.btn-modify', function(){
            var tr = $(this).parents('tr');
            var data = {
                "id" : tr.find('input[name="id"]').val(),
                "itemName" : tr.find('.itemName').text(),
                "categoryId" : tr.find('input[name="categoryId"]').val(),
                "categoryName" : tr.find('.categoryId').text(),
                "price" : tr.find('.price').text(),
                "thumbnail" : tr.find('.thumbnail').val()
            };
            _this.setData(data);
        });
        $(document).on('click', '.btn-delete', function(){
            var id = $(this).parents('tr').find('input[name="id"]').val();
            _this.delete(id);
        });
        $(document).on('click', '.page-link', function(){
            var link = $(this).text();
            if(link === 'prev'){
                _this.firstPage = _this.firstPage - 5;
                _this.lastPage = _this.lastPage - 5;
                _this.list(_this.groupId, _this.firstPage);
            } else if(link === 'next'){
                _this.firstPage = _this.firstPage + 5;
                _this.lastPage = _this.lastPage + 5;
                _this.list(_this.groupId, _this.firstPage);
            } else {
                _this.list(_this.groupId, link);
            }
        });

    },
    clearTable : function(){
        $('#item-masters').empty();
    },
    clearForm : function(){
        var form = $('#form-save-item-master');
        form.find('input[name="id"]').val('');
        form.find('input[name="categoryName"]').val('');
        form.find('input[name="category"]').val('');
        form.find('input[name="itemName"]').val('');
        form.find('input[name="price"]').val('');
        form.find('.thumbnailInfo').addClass("d-none");
        form.find('.oriThumbnail').prop("src", '');
    },
    deleteThumbnail : function(){
        if(confirm("이미지를 삭제하시겠습니까?")){
            var form = $('#form-save-item-master');
            form.find('.thumbnailInfo').addClass("d-none");
            form.find('.oriThumbnail').prop("src", '');
        }
    },
    list : function (page){
        var _this = this;
        this.clearTable();
        $.ajax({
            type: 'GET',
            url: '/api/admin/itemMaster/list?page=' + page +'&size=' + 5 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var resultData = response.response;
           //console.log(resultData);
            if(resultData.totalElements > 0){
                $('#pagination-item-master').setPagination(
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
                        + '<input type="hidden" name="id" value="' + item.id + '"/>'
                        + '<td class="categoryId">' + item.category.nameKor
                        + '<input type="hidden" name="categoryId" value="' + item.category.id + '"/>'
                        +'</td>'
                        + '<td class="itemName">' + item.itemName
                        + '<input class="thumbnail" type="hidden" name="thumbnail" value="' + item.thumbnail + '"/>'
                        +'</td>'
                        + '<td class="price">' + item.price +'</td>'
                        + '<td>' + item.createAt +'</td>'
                        + '<td><a class="btn btn-sm btn-outline-dark btn-modify">수정</a>'
                        + '<a class="btn btn-sm btn-outline-dark btn-delete">삭제</a>'
                        + '<a class="btn btn-sm btn-outline-dark btn-search-codes">옵션</a></td>'
                        + '</tr>';
                    $('#item-masters').append(row);
                });
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    makeFormData : function(){
        var data = new FormData();
        var form = $('#form-save-item-master');
        data.append('id', form.find("input[name='id']").val());
        data.append('categoryId', form.find("input[name='categoryId']").val());
        data.append('itemName', form.find("input[name='itemName']").val());
        data.append('price', form.find("input[name='price']").val());
        data.append('thumbnail', form.find("input[name='thumbnail']")[0].files[0]);
        return data;
    },
    save : function (){
        var _this = this;
        var data = _this.makeFormData();
        /*console.log(data.getAll("id"));
        console.log(data.getAll("categoryId"));
        console.log(data.getAll("price"));
        console.log(data.getAll("itemName"));
        console.log(data.getAll("thumbnail"));*/
        $.ajax({
            type: 'POST',
            url: '/api/admin/itemMaster',
            processData: false,
            contentType: false,
            data: data
        }).done(function(response) {
            //console.log(response);
            _this.list(1);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    setData : function(data){
        //console.log(data);
        var form = $('#form-save-item-master');
        form.find('input[name="id"]').val(data.id);
        form.find('input[name="categoryId"]').val(data.categoryId);
        form.find('input[name="categoryName"]').val(data.categoryName);
        form.find('input[name="itemName"]').val(data.itemName);
        form.find('input[name="price"]').val(data.price);
        form.find('.thumbnailInfo').removeClass("d-none");
        form.find('.oriThumbnail').prop("src", data.thumbnail);
    },
    update : function (page){
        var _this = this;
        var data = _this.makeFormData();
        $.ajax({
            type: 'PUT',
            url: '/api/admin/itemMaster/' + data.getAll("id"),
            processData: false,
            contentType: false,
            data: data
        }).done(function(response) {
            //console.log(response);
            _this.list(page);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    delete : function (id){
        var _this = this;
        $.ajax({
            type: 'DELETE',
            url: '/api/admin/itemMaster/' + id
        }).done(function(response) {
            //console.log(response);
            _this.list(1);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }
};
var itemOption = {

};
main.init();