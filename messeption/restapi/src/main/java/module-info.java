module messeption.restapi {
    requires transitive jakarta.ws.rs;

    requires jersey.common;
    requires jersey.server;
    requires jersey.media.json.jackson;

    requires org.glassfish.hk2.api;
    requires org.slf4j;

    requires transitive com.google.gson;

    requires transitive messeption.core;
    requires messeption.json;

    exports messeption.restapi;

    opens messeption.restapi;

}
