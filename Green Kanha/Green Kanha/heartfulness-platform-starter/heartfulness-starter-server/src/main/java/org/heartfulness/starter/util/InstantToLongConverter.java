package org.heartfulness.starter.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;

@Converter(autoApply = true)
public class InstantToLongConverter implements AttributeConverter<Instant, Long> {

    @Override
    public Long convertToDatabaseColumn(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.getEpochSecond();
    }

    @Override
    public Instant convertToEntityAttribute(Long dbData) {
        if (dbData == null) {
            return null;
        }

        return Instant.ofEpochSecond(dbData);
    }
}
