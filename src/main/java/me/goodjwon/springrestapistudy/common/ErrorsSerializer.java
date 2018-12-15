package me.goodjwon.springrestapistudy.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {

        generator.writeStartArray();

        errors.getFieldErrors().stream().forEach(e->{
            try {
                generator.writeStringField("field", e.getField());
                generator.writeStringField("objectName", e.getObjectName());
                generator.writeStringField("code", e.getCode());
                generator.writeStringField("defaultMessage", e.getDefaultMessage());
                Object rejectValue = e.getRejectedValue();
                if(rejectValue!=null){
                    generator.writeStringField("rejectValue",rejectValue.toString());
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });


        errors.getGlobalErrors().stream().forEach(e->{
            try {
                generator.writeStringField("objectName", e.getObjectName());
                generator.writeStringField("code", e.getCode());
                generator.writeStringField("defaultMessage", e.getDefaultMessage());

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        generator.writeEndArray();
    }
}
