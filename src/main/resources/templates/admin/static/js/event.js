const main = {
    init : function() {
        eventList.init();
    }
}
let eventList = {
    firstPage: 1,
    lastPage: 5,
    currPage: 1,
    div : $('#div-admin-event-list'),
    init : function() {
        const _this = this;
        _this.search(_this.firstPage);
        _this.div.on('click', '#pagination-events .page-link', function(){
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

        _this.div.off('click').on('click', '.eventLink', function(){
            const seq = $(this).parents('tr').find('.eventSeq').text();
            goEventDetail(seq);
        });

        _this.div.find('#btn-new-event').unbind('click').bind('click', function(){
           goNewEvent();
        });
    },
    clearTable : function(){
        $('#events').empty();
    },
    setData: function (resultData){
        const _this = this;
        $('#pagination-events').setPagination(
            _this.currPage,
            _this.firstPage,
            Math.min(resultData.totalPages, _this.lastPage),
            5,
            resultData.totalPages
        );
        $.each(resultData.content, function(){
            //console.log(this);
            const event = this;
            const row = '<tr>'
                + '<td class="eventSeq">' + event.seq +'</td>'
                + '<td class="name">' + '<a class="eventLink" href="" onclick="return false;">' + event.name + '</a>' +'</td>'
                + '<td class="startAt">' + event.startAt +'</td>'
                + '<td class="endAt">' + event.endAt +'</td>'
                + '<td class="status">' + event.status +'</td>'
                + '</tr>';
            $('#events').append(row);
        });
    },
    search : function (page){
        const _this = this;
        _this.clearTable();
        $.ajax({
            type: 'GET',
            url: '/api/admin/event/search?eventStatus=&page='+ page + '&size=' + 5 + '&direction=DESC',
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            const resultData = response.response;
            //console.log(resultData);
            _this.currPage = page;
            _this.clearTable();
            if(resultData.totalElements > 0){
                _this.setData(resultData);
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
}

function goNewEvent(){
    $('.contentDiv').not('#div-admin-event-new').removeClass('active');
    $('#div-admin-event-new').addClass('active').addClass('show');

    $('.contentTab').not('#tab-admin-event-new').removeClass('active').prop('aria-selected', false);
    $('#tab-admin-event-new').show().addClass('active').prop('aria-selected', true);

    newEvent.init();
}

let newEvent = {
    div : $('#div-admin-event-new'),
    init : function() {
        const _this = this;
        _this.clear();
        _this.div.find('#btn-save-event').unbind('click').bind('click', function(){
           _this.save();
        });
        _this.div.find('.eventInfo .eventItems').off('click').on('click', '.deleteBtn', function(){
            $(this).parents('.eventItem').remove();
        });

        _this.div.find('.eventInfo select[name="type"]').unbind('change').bind('change', function(){
            const type = $(this).val();
            if(type === 'P'){
                _this.div.find('.eventInfo .dr').removeClass('d-none');
                _this.div.find('.eventInfo .da').addClass('d-none');
            } else if(type === 'C') {
                _this.div.find('.eventInfo .dr').removeClass('d-none');
                _this.div.find('.eventInfo .da').removeClass('d-none');
            } else {
                _this.div.find('.eventInfo .dr').addClass('d-none');
                _this.div.find('.eventInfo .da').addClass('d-none');
            }
            _this.div.find('.eventInfo .dr input').val(0);
            _this.div.find('.eventInfo .da input').val(0);
        });
    },
    clear : function (){
        const _this = this;
        const eventInfo = _this.div.find('.eventInfo');
        eventInfo.find('input.field, select.field').val('');
        _this.div.find('.eventInfo .eventItems').empty();
    },
    collectItemIds : function(){
        const _this = this;
        let items = [];
        $.each(_this.div.find('.eventInfo .eventItems .eventItem'), function(){
            items.push($(this).find('input[name="itemId"]').val());
        });
        return items;
    },
    save : function(){
        const _this = this;
        let data = $('#form-new-event').serializeObject();
        data['itemStringIds'] = _this.collectItemIds();
        //console.log(data);
        $.ajax({
            type: 'POST',
            url: '/api/admin/event',
            contentType:'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(data)
        }).done(function(response) {
            var event = response.response;
            //console.log(event);
            alert('저장되었습니다.');
            main.init();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
}
function goEventDetail(eventSeq){
    $('.contentDiv').not('#div-admin-event-detail').removeClass('active');
    $('#div-admin-event-detail').addClass('active').addClass('show');

    $('.contentTab').not('#tab-admin-event-detail').removeClass('active').prop('aria-selected', false);
    $('#tab-admin-event-detail').show().addClass('active').prop('aria-selected', true);

    eventDetail.init(eventSeq);
}

let eventDetail = {
    eventSeq : '',
    div : $('#div-admin-event-detail'),
    init : function(seq) {
        const _this = this;
        _this.eventSeq = seq;
        _this.load();

        _this.div.find('#btn-update-event').unbind('click').bind('click', function(){
            _this.update();
        });
        _this.div.find('.eventInfo .eventItems').off('click').on('click', '.deleteBtn', function(){
            $(this).parents('.eventItem').remove();
        });
        _this.div.find('.eventInfo select[name="type"]').unbind('change').bind('change', function(){
            const type = $(this).val();
            if(type === 'P'){
                _this.div.find('.eventInfo .dr').removeClass('d-none');
                _this.div.find('.eventInfo .da').addClass('d-none');
            } else if(type === 'C') {
                _this.div.find('.eventInfo .dr').removeClass('d-none');
                _this.div.find('.eventInfo .da').removeClass('d-none');
            } else {
                _this.div.find('.eventInfo .dr').addClass('d-none');
                _this.div.find('.eventInfo .da').addClass('d-none');
            }

            _this.div.find('.eventInfo .dr input').val(0);
            _this.div.find('.eventInfo .da input').val(0);
        });
    },
    clear : function (){
        const _this = this;
        const eventInfo = _this.div.find('.eventInfo');
        eventInfo.find('input.field').val('');
        _this.div.find('.eventInfo .eventItems').empty();
    },
    load : function(){
        const _this = this;
        $.ajax({
            type: 'GET',
            url: '/api/admin/event/' + _this.eventSeq,
            contentType:'application/json; charset=utf-8'
        }).done(function(response) {
            var event = response.response;
            //console.log(event);
            _this.clear();
            const eventInfo = _this.div.find('.eventInfo');
            eventInfo.find('input[name="seq"]').val(event.seq);
            eventInfo.find('input[name="name"]').val(event.name);
            eventInfo.find('input[name="type"]').val(event.type);
            if(event.type === 'P'){
                _this.div.find('.eventInfo .dr').removeClass('d-none');
                _this.div.find('.eventInfo .da').addClass('d-none');
            } else if(event.type === 'C') {
                _this.div.find('.eventInfo .dr').removeClass('d-none');
                _this.div.find('.eventInfo .da').removeClass('d-none');
            } else {
                _this.div.find('.eventInfo .dr').addClass('d-none');
                _this.div.find('.eventInfo .da').addClass('d-none');
            }
            eventInfo.find('input[name="amount"]').val(event.amount);
            eventInfo.find('input[name="ratio"]').val(event.ratio);
            eventInfo.find('input[name="startAt"]').val(event.startAt);
            eventInfo.find('input[name="endAt"]').val(event.endAt);
            eventInfo.find('input[name="createAt"]').val(event.createAt);

            const eventItems = _this.div.find('.eventInfo .eventItems');
            $.each(event.items, function(){
               const item = this;
               let eventItemDiv = $('<div class="mb-1 eventItem"/>');
               eventItemDiv.append($('<input type="hidden" name="eventItemId"/>').val(item.eventItemId))
                   .append($('<input type="hidden" name="itemId"/>').val(item.itemId))
                   .append($('<button class="btn btn-sm btn-secondary"/>').text(item.itemName))
                   .append($('<button class="btn btn-sm deleteBtn"/>').text('X'));
                eventItems.append(eventItemDiv);
            });

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    collectItemIds : function(){
        const _this = this;
        let items = [];
        $.each(_this.div.find('.eventInfo .eventItems .eventItem'), function(){
            items.push($(this).find('input[name="itemId"]').val());
        });
        return items;
    },
    update: function () {
        const _this = this;
        let data = $('#form-save-event').serializeObject();
        data['itemStringIds'] = _this.collectItemIds();
        data['status'] = '0';
        //console.log(data);
        $.ajax({
            type: 'PUT',
            url: '/api/admin/event/' + _this.eventSeq,
            contentType:'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(data)
        }).done(function(response) {
            var event = response.response;
            console.log(event);
            alert('저장되었습니다.');
            main.init();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
}
main.init();