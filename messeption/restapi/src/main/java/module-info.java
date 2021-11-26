module messeption.restapi {
    requires transitive jakarta.ws.rs;

    requires org.glassfish.hk2.api;
    requires org.slf4j;

    requires com.google.gson;

    requires transitive messeption.core;
    requires transitive messeption.json;

    exports messeption.restapi;

    opens messeption.restapi;

}
