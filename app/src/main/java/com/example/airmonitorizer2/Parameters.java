package com.example.airmonitorizer2;

public class Parameters {
    public String temperature, humidity, gas, dust, smoke;
    public Parameters(){}
    public Parameters(String temp, String hum, String gas, String dust, String smk){
        this.temperature = temp;
        this.humidity = hum;
        this.gas = gas;
        this.dust = dust;
        this.smoke = smk;
    }
}
