package com.oilfieldapps.allspark.snvcalculator.data_and_databases;


public class Annulus_Results {

    private String ds_part_name;
    private String ds_part_volume;
    private String ds_part_strokes;
    private String ds_part_volume_units;

    Annulus_Results() {

    }

    public Annulus_Results(String _ds_part_name, String _ds_part_volume, String _ds_part_strokes, String _ds_part_volume_units) {
        this.ds_part_name = _ds_part_name;
        this.ds_part_volume = _ds_part_volume;
        this.ds_part_strokes = _ds_part_strokes;
        this.ds_part_volume_units = _ds_part_volume_units;
    }

    public String getDs_part_name() {
        return ds_part_name;
    }

    public String getDs_part_volume() {
        return ds_part_volume;
    }

    public String getDs_part_strokes() {
        return ds_part_strokes;
    }

    public String getDs_part_volume_units() {
        return ds_part_volume_units;
    }

    void setDs_part_name(String ds_part_name) {
        this.ds_part_name = ds_part_name;
    }

    void setDs_part_volume(String ds_part_volume) {
        this.ds_part_volume = ds_part_volume;
    }

    void setDs_part_strokes(String ds_part_strokes) {
        this.ds_part_strokes = ds_part_strokes;
    }

    void setDs_part_volume_units(String ds_part_volume_units) {
        this.ds_part_volume_units = ds_part_volume_units;
    }
}
