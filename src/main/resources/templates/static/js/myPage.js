function goMyPage(){
    //console.log($('#loginUserId').val());
    myPage.init();
}
var myPage = {
    div : $('#div-member-info'),
    init : function() {
        var _this = this;
        $('.contentDiv').not(_this.div).addClass('d-none');
        _this.div.removeClass('d-none');
        _this.load();
        _this.div.find("#btn-save").unbind().bind('click', function () {
            _this.save();
        });
    },
    clear : function (){
        var form = this.div.find("#form-info");
        form.find("input[name='userId']").val('');
        form.find("input[name='name']").val('');
        form.find("#tel1").val('');
        form.find("#tel2").val('');
        form.find("#tel3").val('');
        form.find("input[name='addr1']").val('');
        form.find("input[name='addr2']").val('');
    },
    load : function (){
        var _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/mall/member/me',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            //console.log(response);
            _this.clear();
            var form = _this.div.find("#form-info");
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
        var _this = this;
        _this.div.find("#tel").val(
            _this.div.find("#tel1").val() + "-"
            + _this.div.find("#tel2").val() + "-"
            + _this.div.find("#tel3").val()
        );
        var data = $('#form-info').serializeObject();
        $.ajax({
            type: 'PUT',
            url: '/api/mall/member/me',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (response) {
            console.log(response);
            alert("저장 되었습니다.")
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
        _this.load();
    }

};

function mypage_execDaumPostcode() {
    new daum.Postcode({
        popupName: 'mypagePostcodePopup',
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            // 주소 정보를 해당 필드에 넣는다.

            var input = document.querySelector('#div-member-info #addr1');
            input.value = roadAddr;
        }
    }).open();
}