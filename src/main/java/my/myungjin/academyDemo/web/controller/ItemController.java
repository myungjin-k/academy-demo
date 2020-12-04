package my.myungjin.academyDemo.web.controller;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.service.item.ItemMasterService;
import my.myungjin.academyDemo.web.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static my.myungjin.academyDemo.web.Response.OK;

@RequiredArgsConstructor
@RequestMapping("/item")
@RestController
public class ItemController {

    private final ItemMasterService itemMasterService;

    @GetMapping("/all")
    public Response<List<ItemMaster>> allItems(){
        return OK(
                itemMasterService.findAllItems()
        );
    }
}
