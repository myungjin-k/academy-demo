package my.myungjin.academyDemo.web.controller;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.service.item.ItemMasterService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.ItemMasterRequest;
import my.myungjin.academyDemo.web.request.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static my.myungjin.academyDemo.commons.AttachedFile.toAttachedFile;
import static my.myungjin.academyDemo.web.Response.OK;

@RequiredArgsConstructor
@RequestMapping("/admin/item")
@RestController
public class ItemAdminController {

    private final ItemMasterService itemMasterService;

    @GetMapping("/all")
    public Response<Page<ItemMaster>> allItems(PageRequest pageRequest){
        return OK(
                itemMasterService.findAllItems(pageRequest.of())
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<ItemMaster> saveMaster(
            @ModelAttribute ItemMasterRequest request,
            @RequestPart MultipartFile thumbnail) throws IOException {
        return OK(
                itemMasterService.saveItemMaster(request.newItemMaster(), toAttachedFile(thumbnail))
        );
    }
}
