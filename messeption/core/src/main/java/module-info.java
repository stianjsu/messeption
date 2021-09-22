module messeption.core {
    requires com.google.gson;
    exports messeption.core;
    
    opens messeption.core to com.google.gson;
}
