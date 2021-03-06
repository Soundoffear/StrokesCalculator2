package com.oilfieldapps.allspark.snvcalculator.calculators;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.oilfieldapps.allspark.snvcalculator.R;
import com.oilfieldapps.allspark.snvcalculator.converters.Converter;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.Annulus_Data;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.Annulus_DataBase;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.Annulus_Results;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.Annulus_Results_DataBase;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.DrillString_Results;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.DrillString_Results_DataBase;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.HoleData;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.HoleData_DataBase;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.HoleResultsData;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.HoleResults_DataBase;
import com.oilfieldapps.allspark.snvcalculator.data_in.DSDataDisplay;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SNV_Calculator {

    private static String volumeUnits;

    public static void calculateAnnularData(Context context, double pumpOutput) {

        double startingData = 0;
        double tempCalculatedLength = 0;
        double volumePerStage = 0;
        double strokesPerStage = 0;

        double totalStrokes = 0;
        double totalVolume = 0;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        volumeUnits = sharedPreferences.getString("SNV_VOLUME_UNITS", context.getResources().getString(R.string.bbl));
        String pumpOutputUnits = sharedPreferences.getString("SNV_PUMP_OUTPUT", context.getResources().getString(R.string.bbl_stk));

        pumpOutput = Converter.outputConverter(pumpOutputUnits, context.getResources().getString(R.string.bbl_stk), pumpOutput);

        // get all database
        Annulus_DataBase annulus_dataBase = new Annulus_DataBase(context);
        HoleData_DataBase holeData_dataBase = new HoleData_DataBase(context);
        Annulus_Results_DataBase annulus_results_dataBase = new Annulus_Results_DataBase(context);
        annulus_results_dataBase.delateDatabase(); //delete database to avoid duplication of elements

        List<Annulus_Data> annulusDataList = annulus_dataBase.getAllItems();
        List<HoleData> holeDataList = holeData_dataBase.getAllItem();

        // invert annulusData if checkbox is true;
        boolean isCheckBoxChecked = sharedPreferences.getBoolean(DSDataDisplay.BIT_CHECK_BOX_STATE, false);

        if(isCheckBoxChecked) {
            List<Annulus_Data> invertedAnnulusData = new ArrayList<>();
            for(int i = (annulusDataList.size() - 1); i >= 0; i--) {
                invertedAnnulusData.add(annulusDataList.get(i));
            }
            annulusDataList = invertedAnnulusData;
            for(int i = 0; i < annulusDataList.size();i++) {
                Log.d("TEST ANN DATA 1", annulusDataList.get(i).getString_name());
            }
        }

        for(int i = 0; i < annulusDataList.size();i++) {
            Log.d("TEST ANN DATA 2", annulusDataList.get(i).getString_name());
        }

        // sum up all values by increment and create list for calculation
        List<Double> sumOfAllDSLengths = new ArrayList<>();
        double tempSum = 0;
        for (int ds = 0; ds < annulusDataList.size(); ds++) {
            tempSum = tempSum + Double.parseDouble(annulusDataList.get(ds).getString_length());
            sumOfAllDSLengths.add(tempSum);
        }

        // Annular volume calculator

        Annulus_Results annulus_results;
        for (int i = 0; i < holeDataList.size(); i++) {
            double closingData = Double.parseDouble(holeDataList.get(i).getInput_end_md());
            String length_unit = holeDataList.get(i).getInput_length_unit();
            String diameter_unit = holeDataList.get(i).getInput_diameter_unit();
            Log.d("<Start, End>", "<" + startingData + "," + closingData + ">");
            for (int j = 0; j < sumOfAllDSLengths.size(); j++) {
                if (sumOfAllDSLengths.get(j) > startingData && sumOfAllDSLengths.get(j) <= closingData) {
                    if (i > 0) {
                        if (j > 0) {
                            if (sumOfAllDSLengths.get(j) > startingData && sumOfAllDSLengths.get(j - 1) < startingData) {
                                tempCalculatedLength = sumOfAllDSLengths.get(j) - startingData;
                                double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                                double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                                double calculatedVolume = calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                                double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                                double calculatedStrokes = tempVolForStrokes / pumpOutput;
                                volumePerStage = volumePerStage + calculatedVolume;
                                strokesPerStage = strokesPerStage + calculatedStrokes;
                                Log.d("1.01 -->", String.valueOf(calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
                            } else {
                                if (j < sumOfAllDSLengths.size() - 1) {
                                    tempCalculatedLength = sumOfAllDSLengths.get(j) - sumOfAllDSLengths.get(j - 1);
                                    double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                                    double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                                    double calculatedVolume = calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                                    double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                                    double calculatedStrokes = tempVolForStrokes / pumpOutput;
                                    volumePerStage = volumePerStage + calculatedVolume;
                                    strokesPerStage = strokesPerStage + calculatedStrokes;
                                    Log.d("N.N1 -->", String.valueOf(calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
                                }
                            }
                        } else {
                            tempCalculatedLength = sumOfAllDSLengths.get(j) - startingData;
                            double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                            double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                            double calculatedVolume = calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                            double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                            double calculatedStrokes = tempVolForStrokes / pumpOutput;
                            volumePerStage = volumePerStage + calculatedVolume;
                            strokesPerStage = strokesPerStage + calculatedStrokes;
                            Log.d("1.02 -->", String.valueOf(calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
                        }
                    } else if (i == 0) {
                        if (j == i) {
                            tempCalculatedLength = sumOfAllDSLengths.get(j) - startingData;
                            double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                            double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                            double calculatedVolume = calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                            double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                            double calculatedStrokes = tempVolForStrokes / pumpOutput;
                            volumePerStage = volumePerStage + calculatedVolume;
                            strokesPerStage = strokesPerStage + calculatedStrokes;
                            Log.d("1.03 -->", String.valueOf(calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)) + " " + String.valueOf(startingData));
                        } else if (j > i) {
                            if (j < (sumOfAllDSLengths.size() - 1)) {
                                tempCalculatedLength = sumOfAllDSLengths.get(j) - sumOfAllDSLengths.get(j - 1);
                                double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                                double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                                double calculatedVolume = calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                                double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                                double calculatedStrokes = tempVolForStrokes / pumpOutput;
                                volumePerStage = volumePerStage + calculatedVolume;
                                strokesPerStage = strokesPerStage + calculatedStrokes;
                                Log.d("N.N2 -->", String.valueOf(calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
                            }
                        }
                    }
                    if (j - 1 >= 0) {
                        if (j < sumOfAllDSLengths.size() - 1) {
                            if (sumOfAllDSLengths.get(j + 1) > closingData) {
                                tempCalculatedLength = closingData - sumOfAllDSLengths.get(j);
                                double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                                double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                                double calculatedVolume = calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                                double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                                double calculatedStrokes = tempVolForStrokes / pumpOutput;
                                volumePerStage = volumePerStage + calculatedVolume;
                                strokesPerStage = strokesPerStage + calculatedStrokes;
                                Log.d("L.L1 -->", String.valueOf(calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
                                break;
                            }
                        } else if (j == (sumOfAllDSLengths.size() - 1)) {
                            if (sumOfAllDSLengths.get(j) <= closingData) {
                                if (sumOfAllDSLengths.get(j - 1) < startingData) {
                                    break;
                                }
                                tempCalculatedLength = sumOfAllDSLengths.get(j) - sumOfAllDSLengths.get(j - 1);
                                double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                                double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                                double calculatedVolume = calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                                double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                                double calculatedStrokes = tempVolForStrokes / pumpOutput;
                                volumePerStage = volumePerStage + calculatedVolume;
                                strokesPerStage = strokesPerStage + calculatedStrokes;
                                Log.d("L.L2 -->", String.valueOf(calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
                            }
                        }
                    }
                } else if (sumOfAllDSLengths.get(j) > startingData && sumOfAllDSLengths.get(j) > closingData) {
                    if (startingData == 0) {
                        if (j > 0) {
                            tempCalculatedLength = sumOfAllDSLengths.get(j) - (sumOfAllDSLengths.get(j) - closingData) - sumOfAllDSLengths.get(j - 1);
                        } else if (j == 0) {
                            tempCalculatedLength = sumOfAllDSLengths.get(j) - (sumOfAllDSLengths.get(j) - closingData);
                        }

                        double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                        double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                        double calculatedVolume = calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                        double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                        double calculatedStrokes = tempVolForStrokes / pumpOutput;
                        volumePerStage = volumePerStage + calculatedVolume;
                        strokesPerStage = strokesPerStage + calculatedStrokes;
                        Log.d("1.1 -->", String.valueOf(calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
                        break;
                    } else {
                        if (j == 0) {
                            tempCalculatedLength = closingData - startingData;
                        } else {
                            tempCalculatedLength = closingData - sumOfAllDSLengths.get(j - 1);
                        }
                        double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                        double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                        double calculatedVolume = calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                        double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                        double calculatedStrokes = tempVolForStrokes / pumpOutput;
                        volumePerStage = volumePerStage + calculatedVolume;
                        strokesPerStage = strokesPerStage + calculatedStrokes;
                        Log.d("N.N3 -->", String.valueOf(calculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
                        break;
                    }
                }
            }
            String results_string_ds = holeDataList.get(i).getName();
            volumePerStage = roundUpToTwoDec(volumePerStage);
            strokesPerStage = roundUpToTwoDec(strokesPerStage);
            annulus_results = new Annulus_Results(results_string_ds, String.valueOf(volumePerStage), String.valueOf(strokesPerStage), volumeUnits);
            annulus_results_dataBase.inputData(annulus_results);
            startingData = closingData;
            totalStrokes = totalStrokes + strokesPerStage;
            totalVolume = totalVolume + volumePerStage;
            volumePerStage = 0;
            strokesPerStage = 0;
        }
        String results_total_string = "Total Annular Values";
        totalVolume = roundUpToTwoDec(totalVolume);
        totalStrokes = roundUpToTwoDec(totalStrokes);
        annulus_results = new Annulus_Results(results_total_string, String.valueOf(totalVolume), String.valueOf(totalStrokes), volumeUnits);
        annulus_results_dataBase.inputData(annulus_results);

    }

    private static double calculateVolume(Context context, double length, double id, double od, String diameterUnit, String lengthUnit) {
        id = Converter.diameterConverter(diameterUnit, context.getResources().getString(R.string.in), id);
        od = Converter.diameterConverter(diameterUnit, context.getResources().getString(R.string.in), od);
        length = Converter.lengthConverter(lengthUnit, context.getResources().getString(R.string.feet), length);
        double volume = ((Math.pow(id, 2) - Math.pow(od, 2)) / 1029.4) * length;
        volume = Converter.VolumeConverter("bbl", volumeUnits, volume);
        return volume;
    }

    private static double roundUpToTwoDec(double dataIn) {
        double dataOut = dataIn * 100;
        dataOut = Math.round(dataOut);
        dataOut = dataOut / 100;
        return dataOut;
    }

    //calculating Drill String volumes and Strokes

    public static void calculateDrillStringData(Context context, double pump_output) {

        DrillString_Results drillString_results;

        Annulus_DataBase annulus_dataBase = new Annulus_DataBase(context);
        List<Annulus_Data> annulus_dataList = annulus_dataBase.getAllItems();

        DrillString_Results_DataBase drillString_results_dataBase = new DrillString_Results_DataBase(context);
        drillString_results_dataBase.deleteDB();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String volumeUnits = sharedPreferences.getString("SNV_VOLUME_UNITS", context.getResources().getString(R.string.bbl));
        String pumpOutputUnits = sharedPreferences.getString("SNV_PUMP_OUTPUT", context.getResources().getString(R.string.bbl_stk));

        double totalVolume = 0;
        double totalStrokes = 0;
        for(int i = 0; i < annulus_dataList.size(); i++) {
            String name = annulus_dataList.get(i).getString_name();
            String diameter_units = annulus_dataList.get(i).getDiameter_units();
            String length_units = annulus_dataList.get(i).getLength_units();
            double id = Double.parseDouble(annulus_dataList.get(i).getString_id());
            id = Converter.diameterConverter(diameter_units, context.getResources().getString(R.string.in), id);
            double length = Double.parseDouble(annulus_dataList.get(i).getString_length());
            length = Converter.lengthConverter(length_units, context.getResources().getString(R.string.feet), length);

            double volume = Math.pow(id, 2) / 1029.4 * length;
            double volumeConverted = Converter.VolumeConverter(context.getResources().getString(R.string.bbl), volumeUnits, volume);
            pump_output = Converter.outputConverter(pumpOutputUnits, context.getResources().getString(R.string.bbl_stk), pump_output);
            double strokes = volume / pump_output;
            strokes = Math.round(strokes);
            volumeConverted = roundUpToTwoDec(volumeConverted);
            String volumeString = String.valueOf(volumeConverted);
            drillString_results = new DrillString_Results(name, volumeString, String.valueOf(strokes), volumeUnits);
            drillString_results_dataBase.inputItem(drillString_results);
            totalVolume = totalVolume + volumeConverted;
            totalStrokes = totalStrokes + strokes;

        }
        String totalName = "Total Drill String Values";
        drillString_results = new DrillString_Results(totalName, new DecimalFormat("0.00").format(totalVolume), String.valueOf(totalStrokes), volumeUnits);
        drillString_results_dataBase.inputItem(drillString_results);
    }

    public static void calculateEmptyHoleVolume(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        volumeUnits = sharedPreferences.getString("SNV_VOLUME_UNITS", context.getResources().getString(R.string.bbl));

        HoleData_DataBase holeData_dataBase = new HoleData_DataBase(context);
        HoleResultsData holeResultsData;
        HoleResults_DataBase holeResults_dataBase = new HoleResults_DataBase(context);
        holeResults_dataBase.deleteDatabase();

        List<HoleData> holeDataList;
        holeDataList = holeData_dataBase.getAllItem();

        for(int i = 0; i < holeDataList.size(); i++) {
            double resultVolume = calculateVolume(context, holeDataList.get(i));
            resultVolume = roundUpToTwoDec(resultVolume);
            String name = holeDataList.get(i).getName();
            holeResultsData = new HoleResultsData(name, String.valueOf(resultVolume), volumeUnits);
            holeResults_dataBase.insertData(holeResultsData);
        }

    }

    private static double calculateVolume(Context context, HoleData data) {

        String length_units = data.getInput_length_unit();
        String diameter_units = data.getInput_diameter_unit();
        double endMD = Double.parseDouble(data.getInput_end_md());
        endMD = Converter.lengthConverter(length_units, context.getResources().getString(R.string.feet), endMD);
        String topMDs = data.getInput_top_md();
        if(TextUtils.isEmpty(topMDs)) {
            topMDs = "0";
        }
        double topMD = Double.parseDouble(topMDs);
        topMD = Converter.lengthConverter(length_units, context.getResources().getString(R.string.feet), topMD);
        double id = Double.parseDouble(data.getInput_id());
        id = Converter.diameterConverter(diameter_units, context.getResources().getString(R.string.in), id);

        double volume = Math.pow(id, 2) / 1029.4 * (endMD - topMD);

        volume = Converter.VolumeConverter(context.getResources().getString(R.string.bbl), volumeUnits, volume);

        return volume;
    }

}
