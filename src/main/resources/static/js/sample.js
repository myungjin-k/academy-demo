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
            console.log(response);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }

};

sample.init();