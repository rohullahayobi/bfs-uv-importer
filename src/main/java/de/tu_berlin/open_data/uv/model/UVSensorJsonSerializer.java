package de.tu_berlin.open_data.uv.model;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.tu_berlin.open_data.uv.model.UVSensor;

/**
 * Created by Oliver Bruski on 04.07.2017.
 * Class to create the JSON schema from a UVSensor Object.
 */
public class UVSensorJsonSerializer {

    /**
     * Creates the JSON schema for the given UVSensor.
     * @param sensor The Sensor Object that should be parsed to JSON.
     * @return A JSON String with the sensor data.
     */
    public static String create(UVSensor sensor) {

        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

        ObjectNode mainObject = nodeFactory.objectNode();

        mainObject.put("source_id", "bsf_uvindex");
        mainObject.put("device", sensor.getSensorId() != null ? sensor.getSensorId() : "");
        mainObject.put("timestamp", sensor.getDate().toString());


        ObjectNode firstLevelChild = nodeFactory.objectNode();

        firstLevelChild.put("lat", sensor.getCoordinates().latitude);
        firstLevelChild.put("lon", sensor.getCoordinates().longitude);

        mainObject.set("location", firstLevelChild);

        mainObject.put("license", "no information");

        firstLevelChild = nodeFactory.objectNode();

        ObjectNode secondLevelChild = nodeFactory.objectNode();
        secondLevelChild.put("sensor", sensor.getSensorType());
        secondLevelChild.put("observation_value", stringToDouble(sensor.getMeasurement()));
        secondLevelChild.put("unit", sensor.getUnit());
        firstLevelChild.set("uv-radiation", secondLevelChild);


        mainObject.set("sensors", firstLevelChild);
        firstLevelChild = nodeFactory.objectNode();

        firstLevelChild.put("location_name", sensor.getLocation());
        mainObject.set("extra", firstLevelChild);

        return mainObject.toString();
    }

    private static Double stringToDouble(String value){
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e){
            return 0.0;
        }

    }

}
