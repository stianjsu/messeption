module messeption.restapi {
    requires transitive jakarta.ws.rs;

    requires org.glassfish.hk2.api;
    requires org.slf4j;

    requires com.google.gson;

    requires messeption.core;
    requires messeption.json;

    exports messeption.restapi;

    opens messeption.restapi;

}
