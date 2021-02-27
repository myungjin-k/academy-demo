var main = {
    init : function() {
        memberList.init();
        /*$('#div-admin-review-list').off('click').on('click', '.reviewLink', function(){
            var reviewId = $(this).text();
            $('#div-admin-review-list').removeClass('active');
            $('#div-admin-review-detail').addClass('active').addClass('show');

            $('#tab-admin-review-list').removeClass('active').prop('aria-selected', false);
            $('#tab-admin-review-detail').show().addClass('active').prop('aria-selected', true);
            reviewDetail.init(reviewId);
        });*/
    }
}
let memberList = {
    firstPage: 1,
    lastPage: 5,
    currPage: 1,
    div : $('#div-admin-member-list'),
    init : function () {
        var _this = this;

        _this.search(1);
        _this.div.on('click', '#pagination-reviews .page-link', function(){
            var link = $(this).text();
            if(link === 'prev'){
                _this.firstPage = _this.firstPage - 5;
                _this.lastPage = _this.lastPage - 5;
                _this.search(_this.firstPage);
            } else if(link === 'next'){
                _this.firstPage = _this.firstPage + 5;
                _this.lastPage = _this.lastPage + 5;
                _this.search(_this.firstPage);
            } else {
                _this.search(link);
            }
        });

        _this.div.off('click').on('click', '.memberLink', function(){
            const reviewId = $(this).parents('tr').find('.memberId').text();
            goMemberDetail(reviewId);
        });

        _this.div.find('#div-member-search #btn-search-member').unbind('click').bind('click', function(){
            _this.search( 1);
        })

    },
    clearTable : function(){
        $('#members').empty();
    },
    setData: function (resultData){
        const _this = this;
        $('#pagination-members').setPagination(
            _this.currPage,
            _this.firstPage,
            Math.min(resultData.totalPages, _this.lastPage),
            5,
            resultData.totalPages
        );
        $.each(resultData.content, function(){
            //console.log(this);
            const member = this;
            const row = '<tr>'
                + '<td class="memberId">' + '<a class="memberLink" href="" onclick="return false;">' + member.id + '</a>' +'</td>'
                + '<td class="userId">' + member.userId +'</td>'
                + '<td class="name">' + member.name +'</td>'
                + '<td class="createAt">' + member.createAt +'</td>'
                + '</tr>';
            $('#members').append(row);
        });
    },
    search : function (page){
        const _this = this;
        _this.clearTable();
        const param = {
            'memberId' : _this.div.find('#div-member-search input[name="memberId"]').val(),
            'userId' : _this.div.find('#div-member-search input[name="userId"]').val()
        }
        $.ajax({
            type: 'GET',
            url: '/api/admin/member/search?memberId=' + param.memberId + '&userId=' + param.userId
                + '&page='+ page + '&size=' + 5 + '&direction=DESC',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            const resultData = response.response;
            //console.log(resultData);
            _this.currPage = page;
            if(resultData.totalElements > 0){
                _this.setData(resultData);
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
function goMemberDetail(reviewId){
    $('.contentDiv').not('#div-admin-member-detail').removeClass('active');
    $('#div-admin-member-detail').addClass('active').addClass('show');

    $('.contentTab').not('#tab-admin-member-detail').removeClass('active').prop('aria-selected', false);
    $('#tab-admin-member-detail').show().addClass('active').prop('aria-selected', true);

    memberDetail.init(reviewId);
}
let memberDetail = {
    div : $('#div-admin-member-detail'),
    memberId : '',
    init : function(memberId){
        const _this = this;
        _this.memberId = memberId;
        _this.load();

        _this.div.find('.reservesInfo #btn-admin-pay-reserves').click(function(){
            const plus = _this.div.find('.reservesInfo #input-pay-reserves-plus').val();
            const minus = _this.div.find('.reservesInfo #input-pay-reserves-minus').val();
           _this.payReserves(minus, plus);
        });
    },
    clear : function (){
        const _this = this;
        _this.div.find('.reservesInfo #member-reserves-history').empty();
        _this.div.find('.reservesInfo input').val(0);
        const memberInfo = _this.div.find('.memberInfo');
        memberInfo.find('.field').text('');
        memberInfo.find('input.field').val('');

    },
    load : function(){
        const _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/admin/member/' + _this.memberId,
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var member = response.response;
            //console.log(member);
            _this.clear();
            const memberInfo = _this.div.find('.memberInfo');
            memberInfo.find('#memberId').text(member.id);
            memberInfo.find('#userId').text(member.userId);
            memberInfo.find('#name').text(member.name);
            memberInfo.find('#createAt').text(member.createAt);
            memberInfo.find('#email').text(member.email);
            memberInfo.find('#tel').text(member.tel);
            memberInfo.find('#addr1').text(member.addr1);
            memberInfo.find('#addr2').text(member.addr2);
            memberInfo.find('#rating').text(member.rating);
            memberInfo.find('#orderAmount').text(member.orderAmount);
            memberInfo.find('#reserves').text(member.reserves);
            if(member.reservesHistories.length > 0){
                _this.appendReservesHistories(member.reservesHistories);
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    appendReservesHistories : function(histories){
        const _this = this;
        $(histories).each(function(){
            const history = this;
            const ref = history.refId !== null ? ': ' + history.refId : '';
            let row = $('<tr />')
                .append($('<input type="hidden" name="reservesId"/>').val(history.id))
                .append($('<td class="creatAt"/>').text(history.createAt))
                .append($('<td class="amount text-right"/>').text(history.amount))
                .append($('<td class="ref"/>').text(history.type + ref));

            _this.div.find('.reservesInfo #member-reserves-history').append(row);
        });
    },
    payReserves : function(minus, plus){
        const _this = this;
        const chk = Number(_this.div.find('.memberInfo #reserves').text()) - minus < 0;
        if(chk){
            alert('차감 후 적립금이 0원 미만일 수 없습니다.');
            return false;
        }
        $.ajax({
            type: 'PATCH',
            url: '/api/admin/member/' + _this.memberId + '/payReserves?minus=' + minus + '&plus=' + plus,
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            _this.load();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
main.init();