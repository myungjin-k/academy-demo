function newOrder(itemIds) {
    var userId = $('.loginInfo').val();
    order.init(userId, itemIds);
}
var order = {
  userId : '',
  itemIds : [],
  div : $('#div-order'),
  init : function(userId, itemIds){
      var _this = this;
      _this.userId = userId;
      _this.itemIds = itemIds;
      //_this.load();
      $('.contentDiv').not(_this.div).addClass('d-none');
      _this.div.removeClass('d-none');
  }
};