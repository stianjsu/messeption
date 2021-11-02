module messeption.rest {
    requires jakarta.ws.rs;

    requires jersey.common;
    requires jersey.server;
    requires jersey.media.json.jackson;

    requires org.glassfish.hk2.api;
    requires org.slf4j;

    requires com.google.gson;

    requires messeption.core;
    requires messeption.json;



    opens messeption.restapi to jersey.server, com.google.gson;
}
