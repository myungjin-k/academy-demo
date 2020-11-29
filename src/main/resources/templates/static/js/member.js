var main = {
    init : function() {
        var _this = this;
        $("#btn-login").click(function () {
            this.auth();
        });
        $("#btn-go-join").click(function () {
            $("#div-login").hide();
            $("#div-join").show();
        });

        $("#btn-join").click(function () {
            $("#tel").val($("#tel1").val() + "-" + $("#tel2").val() + "-" + $("#tel3").val());
            $("#addr").val($("#addr1").val() + " " + $("#addr2").val());
            this.join();
        });

        $("#btn-forgot").click(function () {

        });
    },
    join : function() {
        var data = $('#form-join').serializeObject();

        $.ajax({
            type: 'POST',
            url: '/member/join',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (response) {
            console.log(response);
            alert("가입 되었습니다.")
            location.href = "/login";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        })
    },
    auth : function () {
        var data = $('#form-login').serializeObject();
        //console.log(data.groupId);
        $.ajax({
            type: 'POST',
            url: '/auth',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            console.log(response);
            location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

};
main.init();