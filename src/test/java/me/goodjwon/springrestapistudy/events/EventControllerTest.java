package me.goodjwon.springrestapistudy.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.goodjwon.springrestapistudy.common.RestDocsConfiguration;
import me.goodjwon.springrestapistudy.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.regex.Matcher;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * 2018.12.13
 * 슬라이싱 테스트에서 스프링부터 테스트로 변경
 * 더 현실에 가까운 테스트가 가능함
 * 테스트 코드를 만드는데 좀더 수월함.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("이벤트를 생성한다.")
    public void 이벤트를생성한다() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated()) // 201
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))     //100이 되면 테스트 실패
                .andExpect(jsonPath("free").value(false))  //true이 되면 테스트 실패
                .andExpect(jsonPath("offline").value(true))  //true이 되면 테스트 실패
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document(
                        "create-event",
                        links(halLinks(),
                                linkWithRel("self").description("Link to self"),
                                linkWithRel("query-events").description("Link to query events"),
                                linkWithRel("update-event").description("Link to update event")
                                ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("contnetsType header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("이벤트명"),
                                fieldWithPath("description").description("이벤트 설명"),
                                fieldWithPath("beginEnrollmentDateTime").description("이벤트 접수시작 시간"),
                                fieldWithPath("closeEnrollmentDateTime").description("이벤트 접수완료 시간"),
                                fieldWithPath("beginEventDateTime").description("이벤트 시작 시간"),
                                fieldWithPath("endEventDateTime").description("이벤트 종료 시간"),
                                fieldWithPath("location").description("장소"),
                                fieldWithPath("basePrice").description("최소 참가비용"),
                                fieldWithPath("maxPrice").description("최대 참가비용"),
                                fieldWithPath("limitOfEnrollment").description("마감여부")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Contents Type")
                        ),
                        responseFields(  // relaxedResponseFields 문서의 일부분만 체크, 정확한 문서를 만들지 못할 가능성이 있다. 주의 해서 사용.
                                fieldWithPath("id").description("문서id"),
                                fieldWithPath("name").description("이벤트명"),
                                fieldWithPath("description").description("이벤트 설명"),
                                fieldWithPath("beginEnrollmentDateTime").description("이벤트 접수시작 시간"),
                                fieldWithPath("closeEnrollmentDateTime").description("이벤트 접수완료 시간"),
                                fieldWithPath("beginEventDateTime").description("이벤트 시작 시간"),
                                fieldWithPath("endEventDateTime").description("이벤트 종료 시간"),
                                fieldWithPath("location").description("장소"),
                                fieldWithPath("basePrice").description("최소 참가비용"),
                                fieldWithPath("maxPrice").description("최대 참가비용"),
                                fieldWithPath("limitOfEnrollment").description("마감여부"),
                                fieldWithPath("free").description("무료인지여부"),
                                fieldWithPath("offline").description("오프파인인지 여부"),
                                fieldWithPath("eventStatus").description("이벤트 상태 표시"),
                                fieldWithPath("_links.self.href").description("이벤트 상태 표시"),
                                fieldWithPath("_links.query-events.href").description("이벤트 상태 표시"),
                                fieldWithPath("_links.update-event.href").description("이벤트 상태 표시")

                        )

                ));

    }


    @Test
    @TestDescription("이벤트를생성한다. 요청할수_없는 항목으로인한 잘못된요청. BadRequest 반환")
    public void 이벤트를생성한다_요청할수_없는_항목으로인한_잘못된요청_BadRequest_반환() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .free(true)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest()) // 400

        ;

    }

    @Test
    @TestDescription("이벤트를 생성한다. 빈값요청 isBadRequest 반환 400")
    public void 이벤트를_생성한다_빈값요청_isBadRequest_반환() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @TestDescription("이벤트를 생성한다 잘봇된 값으로 요청 isBadRequest 400 반환")
    public void 이벤트를_생성한다_잘봇된_값으로_요청_isBadRequest_반환() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 20, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].field").exists())
                //.andExpect(jsonPath("$[0].rejectValue").exists())
        ;

    }
}
