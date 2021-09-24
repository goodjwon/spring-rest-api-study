package me.goodjwon.springrestapistudy.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

    @Test
    public void 빌더가있는지_확인(){
        Event event = Event.builder()
                .name("")
                .description("")
                .build();
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
    @Parameters(method = "유료인지_무료인지_테스트_예제")
    public void 유료인지_무료인지_테스트(int basePirce, int maxPrice, boolean isFree){
        //Given
        Event event = Event.builder()
                .basePrice(basePirce)
                .maxPrice(maxPrice)
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isFree()).isEqualTo(isFree);

    }

    private Object[] 유료인지_무료인지_테스트_예제() {
        return new Object[]{
                new Object[]{0, 0, true},
                new Object[]{100, 0,  false},
                new Object[]{100, 200,  false}
        };
    }

    @Test
    @Parameters(method = "온라인인지_오프라인인지_테스트_예제")
    public void 온라인인지_오프라인인지_테스트(String location, boolean isOnline){
        //Given
        Event event = Event.builder()
                .location(location)
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isEqualTo(isOnline);

    }

    private Object[] 온라인인지_오프라인인지_테스트_예제(){
        return new Object[]{
                new Object[]{"강남역 네이버", true},
                new Object[]{null, false},
                new Object[]{"", false},
                new Object[]{"여의도 사학연금", true},

        };
    }



}