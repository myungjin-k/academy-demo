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
            orderDetail.load(orderId);
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

//TODO 배송상태변경
//TODO 배송상품 추가, 삭제
//TODO 배송 추가, 삭제
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
  load : function (id) {
      var _this = this;
      //this.clearTable();
      $.ajax({
          type: 'GET',
          url: '/api/admin/order/' + id,
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
              const row = '<tr>'
                  + '<input type="hidden" name="id" value="' + delivery.id + '"/>'
                  + '<td class="deliveryId">' + '<a class="deliveryLink" href="" onclick="return false;">' + delivery.id + '</a>' +'</td>'
                  + '<td class="itemName">' + abbrItem +'</td>'
                  + '<td class="createAt">' + delivery.createAt +'</td>'
                  + '</tr>';
              deliveries.append(row);
          });
      }).fail(function (error) {
          alert(JSON.stringify(error));
      });

  }
};

var deliveryDetail = {
    div : $('#div-delivery-detail'),
    deliveryId : '',
    init : function(id){
      const _this = this;
      _this.id = id;
      _this.load(_this.id);

      _this.div.find('#btn-edit-delivery-status').unbind('click').bind('click', function(){
         $(this).parents('.deliveryStatus').find('.edit').removeClass('d-none');
         $(this).parents('.deliveryStatus').find('.text').addClass('d-none');
      });

        _this.div.find('#btn-save-delivery-status').unbind('click').bind('click', function(){
            _this.updateStatus();
        });



    },
    updateStatus : function() {
        var _this = this;
        //this.clearTable();
        const param = _this.div.find('.deliveryStatus select[name="status"]').val();
        $.ajax({
            type: 'PATCH',
            url: '/api/admin/delivery/' + _this.id + '?status=' + param,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
        }).done(function(response) {
            var resultData = response.response;
            console.log(resultData);
            const deliveryInfo = _this.div.find('.deliveryInfo');
            deliveryInfo.find('select[name="status"]').val(resultData.status);
            deliveryInfo.find('#status').text(resultData.status);
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
            console.log(resultData);
            const deliveryInfo = _this.div.find('.deliveryInfo');
            deliveryInfo.find('#deliveryId').text(resultData.id);
            deliveryInfo.find('#receiverName').text(resultData.receiverName);
            deliveryInfo.find('#receiverTel').text(resultData.receiverTel);
            deliveryInfo.find('#receiverAddr1').text(resultData.receiverAddr1);
            deliveryInfo.find('#receiverAddr2').text(resultData.receiverAddr2);
            deliveryInfo.find('#message').text(resultData.message);
            deliveryInfo.find('select[name="status"]').val(resultData.status);
            deliveryInfo.find('#status').text(resultData.status);
            deliveryInfo.find('#invoiceNum').text(resultData.invoiceNum);
            const deliveryItem = _this.div.find('#delivery-items');
            deliveryItem.empty();
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
                deliveryItem.append(row);
            });
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }
};
main.init();