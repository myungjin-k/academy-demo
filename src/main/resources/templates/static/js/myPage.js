var main = {
    init : function() {
        var _this = this;
        _this.load();
        $("#btn-save").click(function () {
            $("#tel").val($("#tel1").val() + "-" + $("#tel2").val() + "-" + $("#tel3").val());
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
           // console.log(response);
            var form = $("#form-info");
            var data = response.response;
            form.find("input[name='userId']").val(data.userId);
            form.find("input[name='name']").val(data.name);
            var splitTel = data.tel.split("-");
            form.find("#tel1").val(splitTel[0]);
            form.find("#tel2").val(splitTel[1]);
            form.find("#tel3").val(splitTel[2]);
            form.find("input[name='addr1']").val(data.addr1);
            form.find("input[name='addr2']").val(data.addr2);
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
            //console.log(response);
            alert("저장 되었습니다.")
            _this.load();
        }).fail(function (error) {
            alert(JSON.stringify(error));
            _this.load();
        })
    }

};
main.init();