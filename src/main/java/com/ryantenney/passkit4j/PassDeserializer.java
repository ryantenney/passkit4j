package com.ryantenney.passkit4j;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.ryantenney.passkit4j.io.NamedInputStreamSupplier;
import com.ryantenney.passkit4j.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
        public Field deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
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
        objectMapper.setDateFormat(new ISO8601DateFormat());
		objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY));

        objectMapper.registerModule( new PassDeserializerModule() );
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);

        //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.addHandler(new DeserializationProblemHandler() {
            @Override
            public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser jp, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException {
                System.out.println("Unknown property " + propertyName);
                return true;
            }
        });
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

    public static Pass readPkPassArchive(InputStream input) throws PassDeserializationException {
        ZipInputStream zip = new ZipInputStream(input);

        try {
            Pass pass = null;
            Map<String, String> manifest = null;
            List<NamedInputStreamSupplier> files = new ArrayList<NamedInputStreamSupplier>();
            String signature = null;

            ZipEntry entry = zip.getNextEntry();
            while (entry != null) {
                String name = entry.getName();

                if (name.equals("pass.json")) {
                    System.out.println("pass.json");

                    ObjectNode tree = objectMapper.readValue(zip, ObjectNode.class);
                    pass = generatePass(tree);

                } else if (name.equals("manifest.json")) {
                    System.out.println("manifest.json");

                    TypeReference<TreeMap<String,String>> typeRef = new TypeReference<TreeMap<String,String>>() {};
                    manifest = objectMapper.readValue(zip, typeRef);


                } else if (name.equals("signature")) {
                    System.out.println("signature");
                    signature = "ok";
                } else {
                    // Anything else
                    System.out.println(entry.getName());
                    files.add( new BufferedPassResource( entry.getName(), zip ) );
                }

                entry = zip.getNextEntry();
            }

            if (pass == null)
                throw new PassDeserializationException("Pass is missing pass.json");

            if (manifest == null)
                throw new PassDeserializationException("Pass is missing manifest.json");

            if (signature == null)
                throw new PassDeserializationException("Pass is missing signature");

            System.out.println(manifest.toString());

            // TODO VALIDATE SIGNATURE
            // TODO Check hashes in manifest agaisnt files
            // TODO Check there aren't any missing files
            // TODO Check there aren't extra files

            return pass.files(files);

        } catch (IOException e) {
            throw new PassDeserializationException(e);
        }
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
