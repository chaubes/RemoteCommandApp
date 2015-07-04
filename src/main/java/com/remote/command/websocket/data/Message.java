package com.remote.command.websocket.data;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringWriter;

/**
 * Created by saurabhchaubey on 15/06/15.
 */


    public class Message {
        private JsonObject json;

        public Message(JsonObject json) {
            this.json = json;
        }

        public JsonObject getJson() {
            return json;
        }

        public void setJson(JsonObject json) {
            this.json = json;
        }

        @Override
        public String toString(){
            StringWriter writer = new StringWriter();

            Json.createWriter(writer).write(json);

            return writer.toString();
        }

    }

