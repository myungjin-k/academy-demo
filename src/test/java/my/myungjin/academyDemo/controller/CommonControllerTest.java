package my.myungjin.academyDemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.security.EntryPointUnauthorizedHandler;
import my.myungjin.academyDemo.security.MyAccessDeniedHandler;
import my.myungjin.academyDemo.service.admin.AdminService;
import my.myungjin.academyDemo.service.admin.CommonCodeService;
import my.myungjin.academyDemo.service.member.MemberService;
import my.myungjin.academyDemo.web.controller.CommonController;
import my.myungjin.academyDemo.web.request.CodeGroupRequest;
import my.myungjin.academyDemo.web.request.PageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@WebMvcTest(controllers = {CommonController.class},
            includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
public class CommonControllerTest {

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;

    @MockBean
    private MyAccessDeniedHandler accessDeniedHandler;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AdminService adminService;

    @MockBean
    private CommonCodeService codeService;

    private CodeGroup codeGroup;

    private CommonCode commonCode;

    @BeforeEach
    void setup() {
        codeGroup =  CodeGroup.builder()
                .id("TEMP")
                .code("T")
                .nameEng("TEST_GROUP")
                .nameKor("테스트그룹")
                .build();
        commonCode = CommonCode.builder()
                .id("TEMP_CODE")
                .code("T01")
                .codeGroup(codeGroup)
                .nameEng("TEST_CODE")
                .nameKor("테스트코드")
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void 코드그룹_목록을_조회한다() throws Exception {

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(0);;
        pageRequest.setSize(5);
        pageRequest.setDirection(Sort.Direction.DESC);

        when(codeService.findAllGroups(pageRequest.of())).thenReturn(new PageImpl<>(List.of(codeGroup), pageRequest.of(),1));

        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap();
        map.add("page","0");
        map.add("size","5");
        map.add("direction","DESC");

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/admin/codeGroup/list")
                .queryParams(map)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void 코드그룹을_저장한다() throws Exception {

        CodeGroupRequest request = new CodeGroupRequest(codeGroup.getCode(), codeGroup.getNameEng(), codeGroup.getNameKor());
        when(codeService.registGroup(request.newCodeGroup())).thenReturn(codeGroup);

        String json = new ObjectMapper().writeValueAsString(request);
        log.info("Group Request: {}", json);

      mockMvc.perform(
                MockMvcRequestBuilders.post("/api/admin/codeGroup")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        )
        .andExpect(
                MockMvcResultMatchers.jsonPath("$.response.code").value(codeGroup.getCode())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.response.nameEng").value(codeGroup.getNameEng())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.response.nameKor").value(codeGroup.getNameKor())
        ).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void 코드그룹을_수정한다() throws Exception {

        String code = "T_UPDATED", nameEng = "UPDATED", nameKor = "테스트그룹_수정";
        when(codeService.modifyGroup(
                Id.of(CodeGroup.class, codeGroup.getId()), code, nameEng, nameKor)
        ).thenReturn(codeGroup);

        CodeGroupRequest request = new CodeGroupRequest(code, nameEng, nameKor);
        String json = new ObjectMapper().writeValueAsString(request);
        log.info("Group Request: {}", json);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/admin/codeGroup")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.response.code").value(code)
                ).andExpect(
                MockMvcResultMatchers.jsonPath("$.response.nameEng").value(nameEng)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.response.nameKor").value(nameKor)
        ).andDo(MockMvcResultHandlers.print());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void 코드그룹을_삭제한다() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/admin/codeGroup/" + codeGroup.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void 코드그룹을_검색한다() throws Exception {

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(0);;
        pageRequest.setSize(5);
        pageRequest.setDirection(Sort.Direction.DESC);

        when(codeService.search(codeGroup.getCode(), null, null, pageRequest.of()))
                .thenReturn(new PageImpl<>(List.of(codeGroup), pageRequest.of(),1));

        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap();
        map.add("page","0");
        map.add("size","5");
        map.add("direction","DESC");
        map.add("code", codeGroup.getCode());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/admin/codeGroup/search")
                        .queryParams(map)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andDo(MockMvcResultHandlers.print());
    }
}
