jQuery.fn.serializeObject = function() {
    var obj = null;
    try {
        if (this[0].tagName && this[0].tagName.toUpperCase() === "FORM") {
            var arr = this.serializeArray();
            if (arr) {
                obj = {};
                jQuery.each(arr, function() {
                    obj[this.name] = this.value;
                });
            }//if ( arr )
        }
    } catch (e) {
        alert(e.message);
    } finally {
    }

    return obj;
};

jQuery.fn.setFormData = function(form, data) {

    try {

    } catch (e) {
        alert(e.message);
    } finally {
    }
};

jQuery.fn.setPagination = function( page, start, end, size, totalPages) {
    try {
        this[0].innerText = '';
        var innerHtml = '';
        if(start > size){
            innerHtml += '<li class="page-item"><a class="page-link previous" href="#">prev</a></li>';
        }
        for(var i = start; i <= end; i++){
            if(i == page)
                innerHtml += '<li class="page-item active"><a class="page-link" href="#">'+i+'</a></li>';
            else
                innerHtml += '<li class="page-item"><a class="page-link" href="#">'+i+'</a></li>';
        }
        if(end < totalPages){
            innerHtml += '<li class="page-item"><a class="page-link next" href="#">next</a></li>';
        }
        this[0].innerHTML = innerHtml;
    } catch (e) {
        alert(e.message);
    } finally {
    }
};

jQuery.fn.adjustCount = function(){
    var btn = $(this);
    var count = btn.parents('.count').find('input[name="count"]');
    if(btn.hasClass('plus')){
        count.val(Number(count.val()) + 1);
    } else if(btn.hasClass('minus')){
        if(Number(count.val()) === 1)
            return false;
        count.val(Number(count.val()) - 1);
    }
}
function sample4_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            // 주소 정보를 해당 필드에 넣는다.
            document.getElementById("addr1").value = roadAddr;
        }
    }).open();
}