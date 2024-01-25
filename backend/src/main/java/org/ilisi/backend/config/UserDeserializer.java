package org.ilisi.backend.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ilisi.backend.model.Manager;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.model.User;

import java.io.IOException;

public class UserDeserializer extends JsonDeserializer<User> {

    @Override
    public User deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectNode node = jp.getCodec().readTree(jp);
        if (node.has("cne")) {
            return jp.getCodec().treeToValue(node, Student.class);
        } else {
            return jp.getCodec().treeToValue(node, Manager.class);
        }
    }
}