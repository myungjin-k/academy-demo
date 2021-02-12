var main = {
    init : function() {
        orderList.init();
        //delivery.init();

        $('#div-order-list').off('click').on('click', '.orderLink', function(){
            var orderId = $(this).text();
            $('#div-order-list').removeClass('active');
            $('#div-order-detail').addClass('active').addClass('show');

            $('#tab-order-list').removeClass('active').prop('aria-selected', false);
            $('#tab-order-detail').show().addClass('active').prop('aria-selected', true);
            orderDetail.init(orderId);
        });

        $('#div-order-list #btn-manual-order').unbind('click').bind('click', function(){
            $('#div-order-list').removeClass('active');
            $('#div-manual-order').addClass('active').addClass('show');

            $('#tab-order-list').removeClass('active').prop('aria-selected', false);
            $('#tab-manual-order').show().addClass('active').prop('aria-selected', true);
            manualOrder.init();
        });

        $('#div-order-detail').off('click').on('click', '.deliveryLink', function(){
            var deliveryId = $(this).text();
            $('#div-order-detail').removeClass('active');
            $('#div-delivery-detail').addClass('active').addClass('show');

            $('#tab-order-detail').removeClass('active').prop('aria-selected', false);
            $('#tab-delivery-detail').show().addClass('active').prop('aria-selected', true);
            deliveryDetail.init(deliveryId);
        });
    }
}
// TODO 주문정보 끌어오기
var manualOrder = {
    div : $('#div-manual-order'),
    init : function(){
        const _this = this;
        _this.div.find('#btn-append-manual-order-item').unbind('click').bind('click', function(){
            _this.appendOrderItem();
        });
        _this.div.find('#btn-save-manual-order').unbind('click').bind('click', function(){
            _this.save();
        });
        _this.div.off('click').on('click', '#manual-order-items .deleteBtn', function(){
            const row = $(this).parents('tr');
            row.remove();
        });
    },
    appendOrderItem : function(){
        const _this = this;
        const addDiv = _this.div.find('#div-add-manual-order-item');
        const itemId = addDiv.find('input[name="itemId"]').val();
        const itemInfo = addDiv.find('#itemInfo').val();
        const count = addDiv.find('input[name="count"]').val();
        const itemRow = $('<tr />');

        itemRow
            .append($('<input type="hidden" name="itemId" />').val(itemId))
            .append($('<td />').text(itemInfo))
            .append($('<td class="count"/>').text(count))
            .append($('<td class="deleteBtn"/>')
                        .append($('<a class="deleteBtn" href="" onclick="return false;"/>').text('삭제'))
            );

        _this.div.find('#manual-order-items').append(itemRow);
    },
    collectItems : function(){
        var items = [];
        var _this = this;
        $(_this.div.find('#manual-order-items tr')).each(function(){
            items.push({
                "itemId" : $(this).find('input[name="itemId"]').val(),
                "count" : $(this).find('.count').text()
            });
        });
        return items;
    },
    save : function(){
        var _this = this;
        var form = _this.div.find('#form-save-manual-order');
        var tel = form.find('#orderTel1').val() + '-' + form.find('#orderTel2').val() + '-' + form.find('#orderTel3').val();
        form.find('input[name="tel"]').val(tel);
        var receiverTel = form.find('#receiverTel1').val() + '-' + form.find('#receiverTel2').val() + '-' + form.find('#receiverTel3').val();
        form.find('input[name="receiverTel"]').val(receiverTel);
        var data = $('#form-save-manual-order').serializeObject();
        data['items'] = _this.collectItems();
        console.log(data);

        $.ajax({
            type: 'POST',
            url: '/api/admin/order',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            var data = response.response;
            console.log(data);
            alert('주문이 완료되었습니다.');
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
var orderList = {
    firstPage: 1,
    lastPage: 5,
    div : $('#div-order-list'),
    init : function () {
        var _this = this;
        _this.div.on('click', '#pagination-order .page-link', function(){
            var link = $(this).text();
            if(link === 'prev'){
                _this.firstPage = _this.firstPage - 5;
                _this.lastPage = _this.lastPage - 5;
                _this.list(_this.firstPage);
            } else if(link === 'next'){
                _this.firstPage = _this.firstPage + 5;
                _this.lastPage = _this.lastPage + 5;
                _this.list(_this.firstPage);
            } else {
                _this.list(link);
            }
        });
        _this.div.find('#btn-search-order').click(function (){
            _this.firstPage = 1;
            _this.lastPage = 5;
            _this.list(_this.firstPage);
        });

    },
    clearTable : function(){
        $('#orders').empty();
    },
    list : function (page){
        var _this = this;
        this.clearTable();
        var param = $('#input-search-order').val();
        $.ajax({
            type: 'GET',
            url: '/api/admin/order/search?orderId='+ param +'&page=' + page +'&size=' + 5 + '&direction=DESC',
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var resultData = response.response;
            //console.log(resultData);
            if(resultData.totalElements > 0){
                $('#pagination-order').setPagination(
                    page,
                    _this.firstPage,
                    Math.min(resultData.totalPages, _this.lastPage),
                    5,
                    resultData.totalPages
                );
                $.each(resultData.content, function(){
                    //console.log(this);
                    const item = this;
                    const row = '<tr>'
                        + '<input type="hidden" name="id" value="' + item.orderItemId + '"/>'
                        + '<td class="orderId">' + '<a class="orderLink" href="" onclick="return false;">' + item.orderId + '</a>' +'</td>'
                        + '<td class="itemName">' + item.itemName +'</td>'
                        + '<td class="option">' + item.color + '/' + item.size +'</td>'
                        + '<td class="count">' + item.count +'</td>'
                        + '<td class="deliveryStatus">' + item.deliveryStatus +'</td>'
                        + '</tr>';
                    $('#orders').append(row);
                });
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }
};
var orderDetail = {
    div : $('#div-order-detail'),
    id : '',
    init : function(id){
        const _this = this;
        _this.id = id;
        _this.load();

        _this.div.find('#btn-save-delivery').unbind('click').bind('click', function () {
           _this.addDelivery();
        });

        _this.div.find('#deliveries').off('click').on('click', '.delete-delivery', function () {
            const deliveryId = $(this).parents('tr').find('input[name="id"]').val();
            const status = $(this).parents('tr').find('.status').text();
            if(status !== 'PROCESSING')
                alert('이미 발송된 배송정보는 취소할 수 없습니다.');
            else
                _this.deleteDelivery(deliveryId);
        });
    },
    deleteDelivery : function(id){
        const _this = this;
        $.ajax({
            type: 'DELETE',
            url: '/api/admin/delivery/' + id ,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var resultData = response.response;
            console.log(resultData);
            alert('삭제되었습니다.');
            _this.load();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    addDelivery : function(){
        const _this = this;
        const data = $('#form-save-delivery').serializeObject();
        let itemIds = [];
        _this.div.find('#order-items tr').each(function(){
           const orderItemId = $(this).find('input[name="id"]').val();
           itemIds.push(orderItemId);
        });
        data['orderItemIds'] = itemIds;
        console.log(data);
        $.ajax({
            type: 'POST',
            url: '/api/admin/order/' + _this.id + '/delivery',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            var resultData = response.response;
            //console.log(resultData);
            alert('생성되었습니다.');
            _this.load();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    load : function () {
      const _this = this;
      //this.clearTable();
      $.ajax({
          type: 'GET',
          url: '/api/admin/order/' + _this.id,
          dataType: 'json',
          contentType:'application/json; charset=utf-8'
      }).done(function(response) {
          var resultData = response.response;
          console.log(resultData);
          const orderInfo = _this.div.find('.orderInfo');
          orderInfo.find('#orderId').text(resultData.id);
          orderInfo.find('#orderName').text(resultData.orderName);
          orderInfo.find('#orderDate').text(resultData.createAt);
          const orderItem = _this.div.find('#order-items');
          orderItem.empty();
          $.each(resultData.items, function(){
              const item = this;
              const itemOption = item.itemOption;
              const row = '<tr>'
                  + '<input type="hidden" name="id" value="' + item.id + '"/>'
                  + '<td class="itemName">' + itemOption.itemDisplay.itemDisplayName +'</td>'
                  + '<td class="option">' + itemOption.color + '/' + itemOption.size +'</td>'
                  + '<td class="count">' + item.count +'</td>'
                  + '<td class="price">' + itemOption.itemDisplay.itemMaster.price +'</td>'
                  + '</tr>';
              orderItem.append(row);
          });
          const deliveries = _this.div.find('#deliveries');
          deliveries.empty();
          $.each(resultData.deliveries, function(){
              const delivery = this;
              const items = delivery.items;
              let abbrItem = items[0].itemOption.itemDisplay.itemDisplayName;
              if(items.length > 1)
                  abbrItem += '외 ' + String(items.length - 1) + '건';
              const deleteBtn = (delivery.status !== "PROCESSING") ?
                "" : '<a class="btn btn-sm delete-delivery" href="" onclick="return false;">취소</a>'
              ;
              const row = '<tr>'
                  + '<input type="hidden" name="id" value="' + delivery.id + '"/>'
                  + '<td class="deliveryId">' + '<a class="deliveryLink" href="" onclick="return false;">' + delivery.id + '</a>' +'</td>'
                  + '<td class="itemName">' + abbrItem +'</td>'
                  + '<td class="status">' + delivery.status +'</td>'
                  + '<td class="createAt">' + delivery.createAt +'</td>'
                  + '<td class="delete">' + deleteBtn +'</td>'
                  + '</tr>';
              deliveries.append(row);
          });

          const lastDelivery = resultData.deliveries[0];
          if(lastDelivery !== undefined){
              const newDeliveryForm = _this.div.find('#form-save-delivery');
              newDeliveryForm.find('input[name="receiverName"]').val(lastDelivery.receiverName);
              newDeliveryForm.find('input[name="receiverTel"]').val(lastDelivery.receiverTel);
              newDeliveryForm.find('input[name="receiverAddr1"]').val(lastDelivery.receiverAddr1);
              newDeliveryForm.find('input[name="receiverAddr2"]').val(lastDelivery.receiverAddr2);
              newDeliveryForm.find('input[name="message"]').val(lastDelivery.message);
          }

      }).fail(function (error) {
          alert(JSON.stringify(error));
      });

  }
};

var deliveryDetail = {
    div : $('#div-delivery-detail'),
    deliveryId : '',
    deliveryStatus : '',
    init : function(id){
        const _this = this;
        _this.id = id;
        _this.load(_this.id);
        _this.div.find('#btn-edit-delivery-status').unbind('click').bind('click', function(){
            if (_this.deliveryStatus === 'DELETED'){
                alert('삭제된 배송정보입니다.');
                return false;
            } else {
                $(this).parents('.deliveryStatus').find('.edit').removeClass('d-none');
                $(this).parents('.deliveryStatus').find('.text').addClass('d-none');
            }
        });

        _this.div.find('#btn-save-delivery-status').unbind('click').bind('click', function(){
            if(_this.deliveryStatus !== 'PROCESSING'
                && $(this).parents('.deliveryStatus select[name="status"]').val() === 'DELETED'){
                alert('상품 발송 이후에는 배송 취소가 불가합니다.');
                return false;
            } else {
                _this.updateStatus();
            }
        });

        _this.div.find('#btn-edit-invoice-num').unbind('click').bind('click', function(){
            if (_this.deliveryStatus === 'DELETED'){
                alert('삭제된 배송정보입니다.');
                return false;
            } else {
                $(this).parents('.invoiceNum').find('.edit').removeClass('d-none');
                $(this).parents('.invoiceNum').find('.text').addClass('d-none');
            }
        });

        _this.div.find('#btn-save-invoice-num').unbind('click').bind('click', function(){
            _this.updateInvoiceNum();
        });

        _this.div.find('#btn-edit-delivery-address').unbind('click').bind('click', function() {
            if (_this.deliveryStatus === 'DELETED'){
                alert('삭제된 배송정보입니다.');
                return false;
            } else if(_this.deliveryStatus !== 'PROCESSING'){
                alert('상품 발송 이후에는 주소 변경이 불가합니다.');
                return false;
            } else {
                $(this).parents('.deliveryAddress').find('.edit').removeClass('d-none');
                $(this).parents('.deliveryAddress').find('.text').addClass('d-none');
            }
        });

        _this.div.find('#btn-save-delivery-address').unbind('click').bind('click', function(){
            _this.updateAddress();
        });

        _this.div.find('#btn-save-delivery-item').unbind('click').bind('click', function(){
            if(_this.deliveryStatus !== 'PROCESSING'){
                alert('상품 발송 이후에는 배송상품 변경이 불가합니다.');
                return false;
            } else {
                _this.addDeliveryItem();
            }
        });

        _this.div.off().on('click', '#delivery-items .delete-delivery-item', function(){
            if(confirm('해당 배송상품을 삭제하시겠습니까?')){
                const itemId = $(this).parents('tr').find('input[name="id"]').val();
                _this.deleteDeliveryItem(itemId);
            }
        });

        _this.div.find('#delivery-items').off().on('click', '.modify-delivery-item-count', function(){
            const itemId = $(this).parents('tr').find('input[name="id"]').val();
            const count = $(this).parents('.count').find('input[name="count"]').val();
            _this.modifyItemCount(itemId, count);
        });
    },
    modifyItemCount : function (itemId, count){
        var _this = this;
        //console.log(data);
        $.ajax({
            type: 'PATCH',
            url: '/api/admin/delivery/' + _this.id + '/item/' + itemId + '?count=' + count,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var resultData = response.response;
            //console.log(resultData);
            alert('변경되었습니다.');
            _this.load();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    deleteDeliveryItem : function(itemId){
        var _this = this;
        //console.log(data);
        $.ajax({
            type: 'DELETE',
            url: '/api/admin/delivery/' + _this.id + '/item/' + itemId,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var resultData = response.response;
            //console.log(resultData);
            _this.load();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    addDeliveryItem : function(){
        var _this = this;
        const addDiv = _this.div.find('#div-add-delivery-item');
        const itemId = addDiv.find('input[name="itemId"]').val();
        const count = addDiv.find('input[name="count"]').val();
        const data = {deliveryId: _this.id, itemId : itemId, count : count};
        //console.log(data);
        $.ajax({
            type: 'POST',
            url: '/api/admin/delivery/' + _this.id + '/item',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            var resultData = response.response;
            //console.log(resultData);
            if(resultData === null)
                alert('해당 상품이 이미 존재합니다. 기존 상품 삭제 후 추가 가능합니다.');
            else {
                addDiv.find('input[name="itemId"]').val('');
                addDiv.find('input[name="itemInfo"]').val('');
                addDiv.find('input[name="count"]').val(1);
                _this.load();
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    updateAddress : function() {
        var _this = this;
        //this.clearTable();
        const param = {
            "addr1" : _this.div.find('.deliveryAddress input[name="addr1"]').val(),
            "addr2" : _this.div.find('.deliveryAddress input[name="addr2"]').val()
        }
        $.ajax({
            type: 'PATCH',
            url: '/api/admin/delivery/' + _this.id + '/address',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(param)
        }).done(function(response) {
            var resultData = response.response;
            //console.log(resultData);
            const deliveryInfo = _this.div.find('.deliveryInfo');
            deliveryInfo.find('input[name="addr1"]').val(resultData.receiverAddr1);
            deliveryInfo.find('input[name="addr2"]').val(resultData.receiverAddr2);
            deliveryInfo.find('#receiverAddr1').text(resultData.receiverAddr1);
            deliveryInfo.find('#receiverAddr2').text(resultData.receiverAddr2);
            deliveryInfo.find('.text').removeClass('d-none');
            deliveryInfo.find('.edit').addClass('d-none');
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    updateStatus : function() {
        var _this = this;
        //this.clearTable();
        const param = _this.div.find('.deliveryStatus select[name="status"]').val();
        $.ajax({
            type: 'PATCH',
            url: '/api/admin/delivery/' + _this.id + '/status/' + param,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
        }).done(function(response) {
            var resultData = response.response;
            //console.log(resultData);
            const updated = resultData.status;
            const deliveryInfo = _this.div.find('.deliveryInfo');
            deliveryInfo.find('select[name="status"]').val(updated);
            deliveryInfo.find('#status').text(updated);
            deliveryInfo.find('.text').removeClass('d-none');
            deliveryInfo.find('.edit').addClass('d-none');
            _this.deliveryStatus = updated;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    updateInvoiceNum : function() {
        var _this = this;
        //this.clearTable();
        const param = _this.div.find('.invoiceNum input[name="invoiceNum"]').val();
        $.ajax({
            type: 'PATCH',
            url: '/api/admin/delivery/' + _this.id + '/invoiceNum/' + param,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
        }).done(function(response) {
            var resultData = response.response;
            //console.log(resultData);
            const deliveryInfo = _this.div.find('.invoiceNum');
            deliveryInfo.find('input[name="invoiceNum"]').val(resultData.invoiceNum);
            deliveryInfo.find('#invoiceNum').text(resultData.invoiceNum);
            deliveryInfo.find('.text').removeClass('d-none');
            deliveryInfo.find('.edit').addClass('d-none');
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    load : function () {
        var _this = this;
        //this.clearTable();
        $.ajax({
            type: 'GET',
            url: '/api/admin/delivery/' + _this.id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var resultData = response.response;
            //console.log(resultData);
            _this.deliveryStatus = resultData.status;
            const deliveryInfo = _this.div.find('.deliveryInfo');
            deliveryInfo.find('#deliveryId').text(resultData.id);
            deliveryInfo.find('#receiverName').text(resultData.receiverName);
            deliveryInfo.find('#receiverTel').text(resultData.receiverTel);
            deliveryInfo.find('#receiverAddr1').text(resultData.receiverAddr1);
            deliveryInfo.find('input[name="addr1"]').val(resultData.receiverAddr1);
            deliveryInfo.find('#receiverAddr2').text(resultData.receiverAddr2);
            deliveryInfo.find('input[name="addr2"').val(resultData.receiverAddr2);
            deliveryInfo.find('#message').text(resultData.message);
            deliveryInfo.find('select[name="status"]').val(resultData.status);
            deliveryInfo.find('#status').text(resultData.status);
            deliveryInfo.find('#invoiceNum').text(resultData.invoiceNum);
            deliveryInfo.find('input[name="invoiceNum"]').val(resultData.invoiceNum);
            const deliveryItem = _this.div.find('#delivery-items');
            deliveryItem.empty();
            $.each(resultData.items, function(){
                const item = this;
                const itemOption = item.itemOption;
                const deleteBtn = (resultData.status !== "PROCESSING") ?
                    "" : '<a class="btn btn-sm delete-delivery-item" href="" onclick="return false;">삭제</a>';
                const modifyCnt = (resultData.status !== "PROCESSING") ?
                    item.count :
                    '<input name="count" value="' + item.count + '" style="width: 33%;">'
                    + '<a class="btn btn-sm modify-delivery-item-count" href="" onclick="return false;">변경</a>';
                const row = '<tr>'
                    + '<input type="hidden" name="id" value="' + item.id + '"/>'
                    + '<td class="itemName">' + itemOption.itemDisplay.itemDisplayName +'</td>'
                    + '<td class="option">' + itemOption.color + '/' + itemOption.size +'</td>'
                    + '<td class="count">' + modifyCnt +'</td>'
                    + '<td class="price">' + itemOption.itemDisplay.itemMaster.price +'</td>'
                    + '<td class="deleteBtn">' + deleteBtn +'</td>'
                    + '</tr>';
                deliveryItem.append(row);
            });
            if(resultData.status === "PROCESSING")
                _this.div.find('#div-add-delivery-item').removeClass('d-none');
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }
};
main.init();

function orderer_execDaumPostcode() {
    new daum.Postcode({
        popupName: 'ordererPostcodePopup',
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            // 주소 정보를 해당 필드에 넣는다.
            var input = document.querySelector('#div-manual-order #orderAddr1');
            input.value = roadAddr;
        }
    }).open();
}

function receiver_execDaumPostcode() {
    new daum.Postcode({
        popupName: 'receiverPostcodePopup',
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            // 주소 정보를 해당 필드에 넣는다.

            var input = document.querySelector('#div-manual-order #receiverAddr1');
            input.value = roadAddr;
        }
    }).open();
}