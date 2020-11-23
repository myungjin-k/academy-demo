var main = {
    init : function() {
        codeGroup.init();
        commonCode.init();

        $(document).on('click', '.btn-search-codes', function(){
            var id = $(this).parents('tr').find('input[name="id"]').val();
            $('#div-code-group').hide();
            $('#div-code').show();
            $('#tab-code-group a').removeClass('active');
            $('#tab-code').show();
            $('#tab-code a').addClass('active');
            commonCode.list(id);
        });


        $('#tab-code-group').click(function(){
            $('#div-code-group').show();
            $('#div-code').hide();
            $('#tab-code').hide();
            $('#tab-code a').removeClass('active');
            $('#tab-code-group a').addClass('active');
        });

        $('#tab-code').click(function(){
            $('#div-code-group').hide();
            $('#div-code').show();
            $('#tab-code-group a').removeClass('active');
            $('#tab-code').show();
            $('#tab-code a').addClass('active');
        });

    }
}
var codeGroup = {
    init : function () {
        var _this = this;
        $('#btn-load-groups').click(function () {
            _this.list();
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
    list : function (){
        this.clearTable();
        $.ajax({
            type: 'GET',
            url: '/codeGroup/list',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            $.each(response, function(){
                const item = this;
                const row = '<tr>'
                    + '<input type="hidden" name="id" value="' + item.id + '"/>'
                    + '<td class="code">' + item.code +'</td>'
                    + '<td class="nameEng">' + item.nameEng +'</td>'
                    + '<td class="nameKor">' + item.nameKor +'</td>'
                    + '<td>' + item.createAt +'</td>'
                    + '<td><a class="btn btn-primary btn-modify">수정</a></td>'
                    + '<td><a class="btn btn-primary btn-delete">삭제</a></td>'
                    + '<td><a class="btn btn-primary btn-search-codes">공통코드 보기</a></td>'
                    + '</tr>';
                $('#groups').append(row);
            });
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    save : function (){
        var _this = this;
        var data = $('#form-save-group').serializeObject();
        $.ajax({
            type: 'POST',
            url: '/codeGroup',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            //console.log(response);
            _this.list();
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
            url: '/codeGroup/' + data.id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            //console.log(response);
            _this.list();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    delete : function (id){
        var _this = this;
        $.ajax({
            type: 'DELETE',
            url: '/codeGroup/' + id
        }).done(function(response) {
            //console.log(response);
            _this.list();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }
};
var commonCode = {
    init : function () {
        var _this = this;
        $('#btn-add-code').click(function () {
            _this.clearForm();
        });
        $('#btn-save-code').click(function (){
            var id = $('#form-save-group').find('input[name="id"]').val();
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
            var groupId = $(this).parents('tr').find('input[name="groupId"]').val();
            var id = $(this).parents('tr').find('input[name="id"]').val();
            _this.delete(groupId, id);
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
    list : function (id){
        this.clearTable();
        $.ajax({
            type: 'GET',
            url: '/codeGroup/' + id + '/commonCode/list',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var codeGroup = response.code;
            $.each(response.commonCodes, function(){
                var item = this;
                //console.log(item);
                var row = '<tr>'
                    + '<input type="hidden" name="id" value="' + item.id + '"/>'
                    + '<td class="codeGroup">'
                    + '<input type="hidden" name="groupId" value="'+ item.groupId +'" />'
                    + codeGroup +'</td>'
                    + '<td class="code">' + item.code +'</td>'
                    + '<td class="nameEng">' + item.nameEng +'</td>'
                    + '<td class="nameKor">' + item.nameKor +'</td>'
                    + '<td>' + item.createAt +'</td>'
                    + '<td><a class="btn btn-primary btn-modify-code">수정</a></td>'
                    + '<td><a class="btn btn-primary btn-delete-code">삭제</a></td>'
                    + '</tr>';
                $('#codes').append(row);
                $('#form-save-code input[name="groupId"]').val(item.groupId);
            });
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
            url: '/codeGroup/' + data.groupId + '/commonCode',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            _this.list(response.groupId);
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
            url: '/codeGroup/' + data.groupId + '/commonCode/' + data.id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            //console.log(response);
            _this.list(response.groupId);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    delete : function (groupId, codeId){
        var _this = this;
        $.ajax({
            type: 'DELETE',
            url: '/codeGroup/' + groupId + '/commonCode/' + codeId,
        }).done(function(response) {
            //console.log(response);
            _this.list(groupId);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }
};
main.init();