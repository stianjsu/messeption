module messeption.core {
    requires com.google.gson;
    exports messeption.core;
    exports messeption.json;
    
    opens messeption.core to com.google.gson;
}
