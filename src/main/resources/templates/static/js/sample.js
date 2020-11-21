const sample = {
    init : function () {
        const _this = this;
        $('#btn-sample').on('click', function () {
            _this.list();
        });
    },
    list : function (){
        $.ajax({
            type: 'GET',
            url: '/sample/codeGroups/list',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            $.each(response, function(){
                const item = this;
                const row = '<tr>'
                    + '<td>' + item.code +'</td>'
                    + '<td>' + item.nameEng +'</td>'
                    + '<td>' + item.nameKor +'</td>'
                    + '<td>' + item.createAt +'</td>'
                    + '</tr>';
                console.log(row);
                $('#samples').append(row);
            });
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }

};

sample.init();