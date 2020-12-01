var main = {
    init : function() {
        var _this = this;

        $("#btn-save").click(function () {
            $("#tel").val($("#tel1").val() + "-" + $("#tel2").val() + "-" + $("#tel3").val());
            $("#addr").val($("#addr1").val() + " " + $("#addr2").val());
            _this.save();
        });
    },
    load : function (){
        $.ajax({
            type: 'GET',
            url: '/member/me',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            console.log(response);

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    save : function() {
        var data = $('#form-info').serializeObject();
        var _this = this;
        $.ajax({
            type: 'PUT',
            url: '/member/me',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (response) {
            console.log(response);
            alert("저장 되었습니다.")
            _this.load();
        }).fail(function (error) {
            alert(JSON.stringify(error));
            _this.load();
        })
    }

};
main.init();