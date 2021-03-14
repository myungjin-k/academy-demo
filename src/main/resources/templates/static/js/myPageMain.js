function goMyPageMain(){
    //console.log($('#loginUserId').val());
    myPageMain.init();
}
var myPageMain = {
    div : $('#div-my-page-main'),
    init : function() {
        var _this = this;
        $('.contentDiv').not(_this.div).addClass('d-none');
        _this.div.removeClass('d-none');
    }
};