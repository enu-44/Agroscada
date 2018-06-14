package com.a3jfernando.serialport.Adapters.listactuadores;

import com.a3jfernando.serialport.models.Device;

/**
 * Created by EnuarMunoz on 17/04/18.
 */

public interface OnItemClickListenerActuadores {
    void onItemClick(Device device);

    void onItemSerialOn(Device device);

    void onItemSerialOff(Device device);

    void onItemEditControl(Device device);

    void onItemConfiguracionControl(Device device);
}
