var main = {
    init : function() {
        var _this = this;
        _this.loadAllItems();
    },
    loadAllItems : function (){
        $.ajax({
            type: 'GET',
            url: '/item/all',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var data = response.response;
            var div = $("#div-thumb");
            $.each(data, function(){
                console.log(this);
                var thumb =
                    '<div class="itemThumb">' +
                    '    <div>' +
                    '        <a href="#">' +
                    '             <div>' +
                    '                 <img src='+ this.thumbnail +' id="thumb-img">' +
                    '             </div>' +
                    '             <div>' +
                    '                 <span class="itemText itemName" id="thumb-name">' + this.itemName + '</span>' +
                    '                 <span class="itemText itemPrice" id="thumb-price">' + this.price + '</span>' +
                    '             </div>' +
                    '        </a>' +
                    '    </div>' +
                    '</div>';
                div.append(thumb);
            });
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