var main = {
    init : function() {
        itemMaster.init();

        $(document).on('click', '.btn-search-item-options', function(){
            var id = $(this).parents('tr').find('input[name="id"]').val();
            var name = $(this).parents('tr').find('.itemName').text();
            $('#div-item-master').removeClass('active');
            $('#div-item-option').addClass('active').addClass('show');

            $('#tab-item-master').removeClass('active').prop('aria-selected', false);
            $('#tab-item-option').show().addClass('active').prop('aria-selected', true);
            itemOption.init(id, name);
            //itemOption.list(id, 1);
        });

        $(document).on('click', '.btn-search-item-display', function(){
            var id = $(this).parents('tr').find('input[name="id"]').val();
            var name = $(this).parents('tr').find('.itemName').text();
            $('#div-item-master').removeClass('active');
            $('#div-item-display').addClass('active').addClass('show');

            $('#tab-item-master').removeClass('active').prop('aria-selected', false);
            $('#tab-item-display').show().addClass('active').prop('aria-selected', true);
            itemDisplay.init(id, name);
        });

        $(document).on('click', '.btn-search-item-display-option', function(){
            var id = $('#form-save-item-display').find('input[name="id"]').val();
            var name = $('#form-save-item-display').find('input[name="itemDisplayName"]').val();
            $('#div-item-master').removeClass('active');
            $('#div-item-display-option').addClass('active').addClass('show');

            $('#tab-item-master').removeClass('active').prop('aria-selected', false);
            $('#tab-item-display-option').show().addClass('active').prop('aria-selected', true);
            //itemDisplay.init(id, name);
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
        $('#div-item-master #btn-save-item-master').click(function (){
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
        $(document).on('click', '#div-item-master .btn-modify', function(){
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
        $(document).on('click', '#div-item-master .btn-delete', function(){
            var id = $(this).parents('tr').find('input[name="id"]').val();
            _this.delete(id);
        });
        $(document).on('click', '#div-item-master page-link', function(){
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
                        + '<a class="btn btn-sm btn-outline-dark btn-search-item-options">옵션</a>'
                        + '<a class="btn btn-sm btn-outline-dark btn-search-item-display">전시</a></td>'
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
    masterId: '',
    firstPage: 1,
    lastPage: 5,
    currPage: 1,
    init : function(masterId, masterName) {
        var _this = this;
        _this.masterId = masterId;
        _this.list(_this.firstPage);
        $('#form-save-item-option input[name="itemMasterName"]').val(masterName);
        $(document).on('click', '#div-item-option .page-link', function(){
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

        $('#btn-add-item-option').click(function () {
            _this.clearForm();
        });
        $('#div-item-option #btn-save-item-option').click(function (){
            var id = $('#form-save-item-option').find('input[name="id"]').val();
            if(id !== ''){
                _this.update();
            } else {
                _this.save();
            }
        });
        $(document).on('click', '#div-item-option .btn-delete', function(){
            var id = $(this).parents('tr').find('input[name="id"]').val();
            _this.delete(id);
        });
        $(document).on('click', '#div-item-option .btn-modify', function(){
            var tr = $(this).parents('tr');
            var data = {
                "id" : tr.find('input[name="id"]').val(),
                "color" : tr.find('.color').text(),
                "size" : tr.find('.size').text()
            };
            _this.setData(data);
        });
        $('#btn-save-option-list').click(function(){
            var newOptions = [];
            $('.optionAddPreview .newOption').each(function () {
                var optionStr = $(this).text().split('/');
                newOptions.push({"color" : optionStr[0], "size" : optionStr[1]});
            });
           _this.saveList(newOptions);
        });
    },
    clearTable : function(){
        $('#item-options').empty();
    },
    clearForm : function(){
        var form = $('#form-save-item-option');
        form.find('input[name="id"]').val('');
        form.find('input[name="color"]').val('');
        form.find('input[name="size"]').val('');
    },
    setData : function(data){
        var form = $('#form-save-item-option');
        form.find('input[name="id"]').val(data.id);
        form.find('input[name="color"]').val(data.color);
        form.find('input[name="size"]').val(data.size);
    },
    list : function (page){
        var _this = this;
        _this.clearTable();
        _this.currPage = page;
        $.ajax({
            type: 'GET',
            url: '/api/admin/itemMaster/' + _this.masterId +'/itemOption/list?page=' + page +'&size=' + 5 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var resultData = response.response;
            //console.log(resultData);
            $('#pagination-item-option').setPagination(
                page,
                _this.firstPage,
                Math.min(resultData.totalPages, _this.lastPage),
                5,
                resultData.totalPages
            );
            if(resultData.totalElements > 0){
                $.each(resultData.content, function(){
                    //console.log(this);
                    const option = this;
                    const row = '<tr>'
                        + '<input type="hidden" name="id" value="' + option.id + '"/>'
                        + '<td class="masterId">' + option.itemMaster.itemName
                        + '<input type="hidden" name="masterId" value="' + option.itemMaster.id + '"/>'
                        +'</td>'
                        + '<td class="optionInfo"><span class="color">' + option.color +'</span>/<span class="size">'+ option.size +'</span></td>'
                        + '<td>' + option.createAt +'</td>'
                        + '<td><a class="btn btn-sm btn-outline-dark btn-modify">수정</a>'
                        + '<a class="btn btn-sm btn-outline-dark btn-delete">삭제</a>'
                        + '</tr>';
                    $('#item-options').append(row);
                });
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    save : function(){
        var _this = this;
        var data = $('#form-save-item-option').serializeObject();
        $.ajax({
            type: 'POST',
            url: '/api/admin/itemMaster/' + _this.masterId + '/itemOption',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            console.log(response);
            _this.list(1);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    update : function (){
        var _this = this;
        var data = $("#form-save-item-option").serializeObject();
        $.ajax({
            type: 'PUT',
            url: '/api/admin/itemOption/' + data.id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            //console.log(response);
            _this.list(_this.currPage);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    delete : function (id){
        var _this = this;
        $.ajax({
            type: 'DELETE',
            url: '/api/admin/itemOption/' + id
        }).done(function(response) {
            //console.log(response);
            _this.list(1);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    saveList : function (newOptions) {
        var _this = this;
        $.ajax({
            type: 'POST',
            url: '/api/admin/itemMaster/' + _this.masterId + '/itemOption/list',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(newOptions)
        }).done(function(response) {
            _this.list(1);
            $('#div-item-option .optionAddPreview').empty();
            $('#btn-save-option-list').addClass('d-none');
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
var itemDisplay = {
    masterId : '',
    init : function (masterId, masterName){
        this.masterId = masterId;
        $('#form-save-item-display').find('input[name="itemMasterName"]').val(masterName);
        var _this = this;
        _this.load();
        $('#div-item-display #btn-save-item-display').click(function (){
            var id = $('#form-save-item-display').find('input[name="id"]').val();
            if(id !== ''){
                _this.update();
            } else {
                _this.save();
            }
        });
        $('#btn-delete-item-display').click(function(){
            var id = $('#form-save-item-display').find('input[name="id"]').val();
            _this.delete(id);
        });
        $('#btn-delete-detail-image').click(function(){
            _this.deleteDetailImage();
        });
    },
    clearForm : function(){
        var form = $('#form-save-item-display');
        form.find('input[name="id"]').val('');
        form.find('input[name="itemDisplayName"]').val('');
        form.find('input[name="salePrice"]').val('');
        form.find('textarea').val('');
        form.find('select[name="status"').val('0');
        form.find('.detailImageInfo').addClass("d-none");
        form.find('.oriDetailImage').prop("src", '');
    },
    deleteDetailImage : function(){
        if(confirm("이미지를 삭제하시겠습니까?")){
            var form = $('#form-save-item-display');
            form.find('.detailImageInfo').addClass("d-none");
            form.find('.oriDetailImage').prop("src", '');
        }
    },
    load : function (){
        var _this = this;
        _this.clearForm();
        $.ajax({
            type: 'GET',
            url: '/api/admin/itemMaster/' + _this.masterId +'/itemDisplay',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var resultData = response.response;
            if(resultData !== null){
                var form = $('#form-save-item-display');
                form.find('input[name="id"]').val(resultData.id);
                form.find('input[name="salePrice"]').val(resultData.salePrice);
                form.find('input[name="itemDisplayName"]').val(resultData.itemDisplayName);
                form.find('textarea[name="size"]').val(resultData.size);
                form.find('textarea[name="material"]').val(resultData.material);
                form.find('textarea[name="description"]').val(resultData.description);
                form.find('textarea[name="notice"]').val(resultData.notice);
                form.find('select[name="status"').val(resultData.status);
                form.find('.detailImageInfo').removeClass("d-none");
                form.find('.oriDetailImage').prop("src", resultData.detailImage);
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    makeFormData : function(){
        var data = new FormData();
        var form = $('#form-save-item-display');
        data.append('id', form.find("input[name='id']").val());
        data.append('itemDisplayName', form.find("input[name='itemDisplayName']").val());
        data.append('salePrice', form.find("input[name='salePrice']").val());
        data.append('detailImageFile', form.find("input[name='detailImage']")[0].files[0]);
        data.append('size', form.find('textarea[name="size"]').val());
        data.append('material', form.find('textarea[name="material"]').val());
        data.append('description', form.find('textarea[name="description"]').val());
        data.append('notice', form.find('textarea[name="notice"]').val());
        data.append('status', form.find('select[name="status"]').val());
        //console.log(data.getAll("size"));
        return data;
    },
    save : function(){
        var _this = this;
        var data = _this.makeFormData();
        $.ajax({
            type: 'POST',
            url: '/api/admin/itemMaster/' + _this.masterId + '/itemDisplay',
            processData: false,
            contentType: false,
            data: data
        }).done(function(response) {
            //console.log(response);
            _this.load();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    update : function (){
        var _this = this;
        var data = _this.makeFormData();
        $.ajax({
            type: 'PUT',
            url: '/api/admin/itemMaster/'+ _this.masterId +'/itemDisplay/' + data.getAll('id'),
            processData: false,
            contentType: false,
            data: data
        }).done(function(response) {
            //console.log(response);
            alert('저장되었습니다.');
            _this.load();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    delete : function (id){
        var _this = this;
        $.ajax({
            type: 'DELETE',
            url: '/api/admin/itemDisplay/' + id
        }).done(function(response) {
            //console.log(response);
            _this.load();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
main.init();