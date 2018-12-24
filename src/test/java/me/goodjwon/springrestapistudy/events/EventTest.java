package me.goodjwon.springrestapistudy.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void 빌더가있는지_확인(){
        Event event = Event.builder().build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean스팩을준수하는지확인(){

        //given
        String description = "Spring Rest API";
        String name = "Spring";

        //when
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        //then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }

    @Test
    public void 유료인지_무료인지_테스트(){
        //Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isFree()).isFalse();

        //Given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
        //Wen
        event.update();

        //Then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void 온라인인지_오프라인인지_테스트(){
        //Given
        Event event = Event.builder()
                .location("강남역 네이버 D2")
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isTrue();


        //Given
        event = Event.builder()
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isFalse();

    }



}