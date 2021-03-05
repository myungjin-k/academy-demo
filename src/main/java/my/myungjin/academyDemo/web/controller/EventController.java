package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.event.Event;
import my.myungjin.academyDemo.service.admin.EventService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.EventRequest;
import my.myungjin.academyDemo.web.request.PageRequest;
import my.myungjin.academyDemo.web.response.EventDetailResponse;
import my.myungjin.academyDemo.web.response.EventListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/api/admin")
@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/event/search")
    @ApiOperation("이벤트 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "ASC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<EventListResponse>> search (@RequestParam  String eventStatus, PageRequest request){
        List<EventListResponse> results = eventService.search(eventStatus)
                .stream()
                .map(EventListResponse::new)
                .collect(Collectors.toList());
        return OK(new PageImpl<>(results, request.of(), request.getSize()));
    }

    @GetMapping("/event/{seq}")
    @ApiOperation("이벤트 단건 조회")
    public Response<EventDetailResponse> findEventById (
            @PathVariable(name = "seq") @ApiParam(value = "대상 이벤트 PK", defaultValue = "1") long eventSeq){
        return OK(new EventDetailResponse(eventService.findBySeqWithDetail(Id.of(Event.class, eventSeq))));
    }

    @PostMapping("/event")
    @ApiOperation("이벤트 등록")
    public Response<Event> createEvent (@RequestBody EventRequest request){
        return OK(eventService.save(request.newEvent(), request.toItemIds()));
    }

    @PutMapping("/event/{seq}")
    @ApiOperation("이벤트 수정")
    public Response<Event> updateEvent (
            @PathVariable(name = "seq") @ApiParam(value = "대상 이벤트 PK", defaultValue = "1") long eventSeq,
            @RequestBody EventRequest request){
        return OK(eventService.modify(Id.of(Event.class, eventSeq), request.newEvent(), request.toItemIds()));
    }
}
