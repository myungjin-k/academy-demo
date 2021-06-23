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
        $("#btn-forgot-password").click(function (){
            _this.forgotPassword();
        });
        $(".btn-back-login").click(function () {

            $("#div-login").removeClass("d-none");
            $("#div-join").addClass("d-none");
            $("#div-find").addClass("d-none");
        });
    },
    join : function() {
        var data = $('#form-join').serializeObject();

        $.ajax({
            type: 'POST',
            url: '/api/mall/join',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (response) {
            console.log(response);
            alert("가입 되었습니다.")
            location.href = "/mall/login";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        })
    },
    auth : function () {
        var data = $('#form-login').serializeObject();
        //console.log(data);
        $.ajax({
            type: 'POST',
            url: '/auth',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            //console.log(response);
            const message = response.response.message;
            if(message !== null && message.length > 0)
                alert(message);
            location.href = data.redirectUri === undefined ? "/" : redirectUri;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    forgotId : function () {
        var data = {"tel" : $('#id-find-key').val()};
        $.ajax({
            type: 'GET',
            url: '/api/mall/find/id',
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
    },
    forgotPassword : function() {
        var _this = this;
        var data = {"email" : $('#password-find-key').val()};
        $("#mail-spinner").removeClass("d-none");
        $("#btn-forgot-password").attr("disabled", true);
        $.ajax({
            type: 'GET',
            url: '/api/mall/find/password',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: data
        }).done(function(response) {
            if(response.response === "")
                alert("일치하는 회원정보가 없습니다.");
            else
                alert("입력하신 이메일 주소로 비밀번호 변경 메일이 발송되었습니다.");
            $("#mail-spinner").addClass("d-none");
            $("#btn-forgot-password").attr("disabled", false);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

};
main.init();