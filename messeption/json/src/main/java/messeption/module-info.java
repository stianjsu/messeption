module messeption.json {
    requires com.google.gson;
    requires transitive messeption.core;
    exports messeption.json;
    
    opens messeption.json to com.google.gson;
}
