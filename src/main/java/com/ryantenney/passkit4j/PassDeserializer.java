package com.ryantenney.passkit4j;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.ryantenney.passkit4j.model.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PassDeserializer {

    public static class PassDeserializerModule extends SimpleModule {
        public PassDeserializerModule() {
            addDeserializer(Field.class, new FieldDeserializer());
        }
    }

    static final class FieldDeserializer extends StdScalarDeserializer<Field> {

        private static final Map<String, Class<? extends Field>> fields;
        static {
            Map<String, Class<? extends Field>> _fields = new HashMap<String, Class<? extends Field>>();

            // DateField
            _fields.put("dateStyle", DateField.class);
            _fields.put("ignoresTimeZone", DateField.class);
            _fields.put("isRelative", DateField.class);
            _fields.put("timeStyle", DateField.class);

            // NumberField
            _fields.put("currencyCode", NumberField.class);
            _fields.put("numberStyle",  NumberField.class);

            // TextField
            // nothing

            fields = Collections.unmodifiableMap(_fields);
        }

        FieldDeserializer() {
            super(Field.class);
        }

        @Override
        public Field deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectCodec codec = jp.getCodec();
            JsonNode node = codec.readTree(jp);
            for (String field : fields.keySet()) {
                if (node.has(field)) {
                    return codec.treeToValue(node, fields.get(field));
                }
            }
            // Here we might actually be a a NumberField, but we assume Text
            return codec.treeToValue(node, TextField.class);
        }
    }


    private static final ObjectMapper objectMapper;
	static {
		objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setDateFormat(new ISO8601DateFormat());
		objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY));
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
        objectMapper.registerModule( new PassDeserializerModule() );
	}

    private static final Map<String, Class<? extends PassInformation>> styles;
    static {
        final Map<String, Class<? extends PassInformation>> _styles = new HashMap<String, Class<? extends PassInformation>>();
        _styles.put("boardingPass", BoardingPass.class);
        _styles.put("coupon", Coupon.class);
        _styles.put("eventTicket", EventTicket.class);
        _styles.put("generic", Generic.class);
        _styles.put("storeCard", StoreCard.class);
        styles = Collections.unmodifiableMap(_styles);
    }

    /*
    public static ObjectNode generatePass(Pass pass) {
        ObjectNode tree = objectMapper.valueToTree(pass);

        PassInformation info = pass.passInformation();
        tree.put(info.typeName(), objectMapper.valueToTree(info));

        return tree;
    }
    */

	public static Pass generatePass(ObjectNode tree) throws PassDeserializationException {

        String styleType = null;
        for (String style : styles.keySet()) {
            if (tree.has(style)) {
                styleType = style;
                break;
            }
        }

        if (styleType == null) {
            throw new PassDeserializationException("Missing style type");
        }

        try {
            PassInformation info = objectMapper.treeToValue(tree.get(styleType), styles.get(styleType));
            tree.remove(styleType);

            Pass pass = objectMapper.treeToValue(tree, Pass.class);
            pass.passInformation(info);

            return pass;

        } catch (JsonProcessingException e) {
            throw new PassDeserializationException(e);
        }

	}

}
