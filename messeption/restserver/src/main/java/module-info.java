module messeption.restserver {
    requires jersey.common;
    requires jersey.server;
    requires jersey.media.json.jackson;

    requires org.glassfish.hk2.api;

    requires com.google.gson;

    requires transitive messeption.core;
    requires transitive messeption.json;
    requires messeption.restapi;

    exports messeption.restserver;

    opens messeption.restserver to jersey.server, com.google.gson;

}
