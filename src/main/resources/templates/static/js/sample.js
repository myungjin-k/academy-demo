var sample = {
    init : function () {
        var _this = this;
        $('#btn-load-samples').click(function () {
            _this.list();
        });
        $('#btn-add-sample').click(function () {
            _this.clearForm();
        });
        $('#btn-save').click(function (){
            var id = $('#form-save-sample').find('input[name="id"]').val();
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
        $('#samples').empty();
    },
    clearForm : function(){
        var form = $('#form-save-sample');
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
                    + '</tr>';
                $('#samples').append(row);
            });
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    save : function (){
        var _this = this;
        var data = $('#form-save-sample').serializeObject();
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
        var form = $('#form-save-sample');
        form.find('input[name="id"]').val(data.id);
        form.find('input[name="code"]').val(data.code);
        form.find('input[name="nameEng"]').val(data.nameEng);
        form.find('input[name="nameKor"]').val(data.nameKor);
    },
    update : function (){
        var _this = this;
        var data = $('#form-save-sample').serializeObject();
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

sample.init();