var main = {
    init : function() {
        var _this = this;
        $("#btn-login").click(function () {
            _this.auth();
        });
        $("#btn-go-join").click(function () {
            $("#div-login").addClass("d-none");
            $("#div-join").removeClass("d-none");
        });

        $("#btn-join").click(function () {
            $("#tel").val($("#tel1").val() + "-" + $("#tel2").val() + "-" + $("#tel3").val());
            $("#addr").val($("#addr1").val() + " " + $("#addr2").val());
            _this.join();
        });

        $("#btn-forgot").click(function () {
            $("#div-login").addClass("d-none");
            $("#div-join").addClass("d-none");
            $("#div-find").removeClass("d-none");
        });

        $("#btn-forgot-id").click(function (){
            _this.forgotId();
        });
        $("#btn-back-login").click(function () {

            $("#div-login").removeClass("d-none");
            $("#div-find").addClass("d-none");
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
    forgotId : function () {
        var data = {"tel" : $('#id-find-key').val()};
        $.ajax({
            type: 'GET',
            url: 'member/id',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: data
        }).done(function(response) {
            if(response.response === "")
                alert("일치하는 회원정보가 없습니다.")
            else {
                var userId = response.response;
                $("#id-present-area").removeClass("d-none");
                $("#found-id").text(userId);
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
main.init();