var main = {
    init : function() {
        codeGroup.init();
        commonCode.init();

        $(document).on('click', '.btn-search-codes', function(){
            var id = $(this).parents('tr').find('input[name="id"]').val();
            $('#div-code-group').removeClass('active');
            $('#div-code').addClass('active').addClass('show');

            $('#tab-code-group').removeClass('active').prop('aria-selected', false);
            $('#tab-code').show().addClass('active').prop('aria-selected', true);
            commonCode.list(id, 1);
        });


        /*$('#tab-code-group').click(function(){
            $('#div-code-group').show();
            $('#div-code').hide();
            $('#tab-code').hide();
            $('#tab-code a').removeClass('active');
            $('#tab-code-group a').addClass('active');
        });*/

        /*$('#tab-code').click(function(){
            $('#div-code-group').hide();
            $('#div-code').show();
            $('#tab-code-group a').removeClass('active');
            $('#tab-code').show();
            $('#tab-code a').addClass('active');
        });*/

    }
}
var codeGroup = {
    firstPage: 1,
    lastPage: 5,
    init : function () {
        var _this = this;
        $('#btn-load-groups').click(function () {
            _this.list(1);
        });
        $('#btn-add-group').click(function () {
            _this.clearForm();
        });
        $('#btn-save-group').click(function (){
            var id = $('#form-save-group').find('input[name="id"]').val();
            if(id !== ''){
                _this.update();
            } else {
                _this.save();
            }
        });
        $(document).on('click', '.btn-modify', function(){
            var tr = $(this).parents('tr');
            var data = {
                "id" : tr.find('input[name="id"]').val(),
                "code" : tr.find('.code').text(),
                "nameEng" : tr.find('.nameEng').text(),
                "nameKor" : tr.find('.nameKor').text()
            };
            _this.setData(data);
        });
        $(document).on('click', '.btn-delete', function(){
            var id = $(this).parents('tr').find('input[name="id"]').val();
            _this.delete(id);
        });
        $(document).on('click', '#pagination-group .page-link', function(){
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
        $('#groups').empty();
    },
    clearForm : function(){
        var form = $('#form-save-group');
        form.find('input[name="id"]').val('');
        form.find('input[name="code"]').val('');
        form.find('input[name="nameEng"]').val('');
        form.find('input[name="nameKor"]').val('');
    },
    list : function (page){
        var _this = this;
        this.clearTable();
        $.ajax({
            type: 'GET',
            url: '/admin/codeGroup/list?page=' + page +'&size=' + 5 + '&direction=ASC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var resultData = response.response;
            //console.log(resultData);
            if(resultData.totalElements > 0){
                $('#pagination-group').setPagination(
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
                        + '<td class="code">' + item.code +'</td>'
                        + '<td class="nameEng">' + item.nameEng +'</td>'
                        + '<td class="nameKor">' + item.nameKor +'</td>'
                        + '<td>' + item.createAt +'</td>'
                        + '<td><a class="btn btn-sm btn-outline-dark btn-modify">수정</a>'
                        + '<a class="btn btn-sm btn-outline-dark btn-delete">삭제</a>'
                        + '<a class="btn btn-sm btn-outline-dark btn-search-codes">공통코드</a></td>'
                        + '</tr>';
                    $('#groups').append(row);
                });
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    save : function (){
        var _this = this;
        var data = $('#form-save-group').serializeObject();
        $.ajax({
            type: 'POST',
            url: '/admin/codeGroup',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            //console.log(response);
            _this.list(1);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    setData : function(data){
        var form = $('#form-save-group');
        form.find('input[name="id"]').val(data.id);
        form.find('input[name="code"]').val(data.code);
        form.find('input[name="nameEng"]').val(data.nameEng);
        form.find('input[name="nameKor"]').val(data.nameKor);
    },
    update : function (){
        var _this = this;
        var data = $('#form-save-group').serializeObject();
        $.ajax({
            type: 'PUT',
            url: '/admin/codeGroup/' + data.id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            //console.log(response);
            _this.list(1);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    delete : function (id){
        var _this = this;
        $.ajax({
            type: 'DELETE',
            url: '/admin/codeGroup/' + id
        }).done(function(response) {
            //console.log(response);
            _this.list(1);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }
};
var commonCode = {
    groupId: null,
    firstPage: 1,
    lastPage: 5,
    init : function () {
        var _this = this;
        $('#btn-add-code').click(function () {
            _this.clearForm();
        });
        $('#btn-save-code').click(function (){
            var id = $('#form-save-code').find('input[name="id"]').val();
            if(id !== ''){
                _this.update();
            } else {
                _this.save();
            }
        });
        $(document).on('click', '.btn-modify-code', function(){
            var tr = $(this).parents('tr');
            var data = {
                "id" : tr.find('input[name="id"]').val(),
                "code" : tr.find('.code').text(),
                "nameEng" : tr.find('.nameEng').text(),
                "nameKor" : tr.find('.nameKor').text(),
                "groupId" : tr.find('input[name="groupId"]').val()
            };
            _this.setData(data);
        });
        $(document).on('click', '.btn-delete-code', function(){
            var id = $(this).parents('tr').find('input[name="id"]').val();
            _this.delete(_this.groupId, id);
        });

        $(document).on('click', '#pagination-code .page-link', function(){
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
        $('#codes').empty();
    },
    clearForm : function(){
        var form = $('#form-save-code');
        form.find('input[name="id"]').val('');
        form.find('input[name="code"]').val('');
        form.find('input[name="nameEng"]').val('');
        form.find('input[name="nameKor"]').val('');
    },
    list : function (id, page){
        var _this = this;
        this.groupId = id;
        this.clearTable();
        this.clearForm();
        $.ajax({
            type: 'GET',
            url: '/admin/codeGroup/' + id + '/commonCode/list?page=' + page +'&size=5&direction=ASC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
           // console.log(response);
            var resultData = response.response;
            $('#form-save-code input[name="groupId"]').val(_this.groupId);
            if(resultData.totalElements > 0){
                $('#pagination-code').setPagination(
                    page,
                    _this.firstPage,
                    Math.min(resultData.totalPages, _this.lastPage),
                    5,
                    resultData.totalPages
                );
                $.each(resultData.content, function(){
                    var item = this;
                    //console.log(item);
                    var row = '<tr>'
                        + '<input type="hidden" name="id" value="' + item.id+ '"/>'
                        + '<td class="code">' + item.code +'</td>'
                        + '<td class="nameEng">' + item.nameEng +'</td>'
                        + '<td class="nameKor">' + item.nameKor +'</td>'
                        + '<td>' + item.createAt +'</td>'
                        + '<td><a class="btn btn-sm btn-outline-dark btn-modify-code">수정</a>'
                        + '<a class="btn btn-sm btn-outline-dark btn-delete-code">삭제</a></td>'
                        + '</tr>';
                    $('#codes').append(row);
                });

            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    save : function (){
        var _this = this;
        var data = $('#form-save-code').serializeObject();
        //console.log(data.groupId);
        $.ajax({
            type: 'POST',
            url: '/admin/codeGroup/' + data.groupId + '/commonCode',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            _this.list(data.groupId, 1);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    setData : function(data){
        var form = $('#form-save-code');
        form.find('input[name="id"]').val(data.id);
        form.find('input[name="code"]').val(data.code);
        form.find('input[name="nameEng"]').val(data.nameEng);
        form.find('input[name="nameKor"]').val(data.nameKor);
    },
    update : function (){
        var _this = this;
        var data = $('#form-save-code').serializeObject();
        $.ajax({
            type: 'PUT',
            url: '/admin/codeGroup/' + data.groupId + '/commonCode/' + data.id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            //console.log(response);
            _this.list(data.groupId, 1);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    delete : function (groupId, codeId){
        var _this = this;
        $.ajax({
            type: 'DELETE',
            url: '/admin/codeGroup/' + groupId + '/commonCode/' + codeId,
        }).done(function(response) {
            //console.log(response);
            _this.list(groupId, 1);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }
};
main.init();