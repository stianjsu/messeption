module messeption.json {
    requires com.google.gson;
    requires messeption.core;
    exports messeption.json;
    
    opens messeption.json to com.google.gson;
}
