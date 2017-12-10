package com.oilfieldapps.allspark.strokescalculator.calculators;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.oilfieldapps.allspark.strokescalculator.R;
import com.oilfieldapps.allspark.strokescalculator.converters.Converter;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.Annulus_Data;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.Annulus_DataBase;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.Annulus_Results;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.Annulus_Results_DataBase;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.HoleData;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.HoleData_DataBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allspark on 17/07/2017.
 */

public class SNV_Calculator {

    private static List<Annulus_Data> annulusDataList;
    private static List<HoleData> holeDataList;
    private static String volumeUnits;

    public static void CalculateAnnularData(Context context, Annulus_DataBase annulus_dataBase, HoleData_DataBase holeData_dataBase, double pumpOutput,
                                            Annulus_Results_DataBase annulus_results_dataBase) {

        double startingData = 0;
        double tempCalculatedLength = 0;
        double volumePerStage = 0;
        double strokesPerStage = 0;

        double totalStrokes = 0;
        double totalVolume = 0;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        volumeUnits = sharedPreferences.getString("SNV_VOLUME_UNITS", context.getResources().getString(R.string.bbl));

        annulusDataList = annulus_dataBase.getAllItems();

        List<Double> sumOfAllDSLengths = new ArrayList<>();
        double tempSum = 0;
        for (int ds = 0; ds < annulusDataList.size(); ds++) {
            tempSum = tempSum + Double.parseDouble(annulusDataList.get(ds).getString_length());
            sumOfAllDSLengths.add(tempSum);
        }

        // testing 2

        holeDataList = holeData_dataBase.getAllItem();

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
                                double calculatedVolume = CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                                double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                                double calculatedStrokes = tempVolForStrokes / pumpOutput;
                                volumePerStage = volumePerStage + calculatedVolume;
                                strokesPerStage = strokesPerStage + calculatedStrokes;
                                Log.d("1.01 -->", String.valueOf(CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
                            } else {
                                if (j < sumOfAllDSLengths.size() - 1) {
                                    tempCalculatedLength = sumOfAllDSLengths.get(j) - sumOfAllDSLengths.get(j - 1);
                                    double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                                    double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                                    double calculatedVolume = CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                                    double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                                    double calculatedStrokes = tempVolForStrokes / pumpOutput;
                                    volumePerStage = volumePerStage + calculatedVolume;
                                    strokesPerStage = strokesPerStage + calculatedStrokes;
                                    Log.d("N.N1 -->", String.valueOf(CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
                                }
                            }
                        } else {
                            tempCalculatedLength = sumOfAllDSLengths.get(j) - startingData;
                            double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                            double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                            double calculatedVolume = CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                            double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                            double calculatedStrokes = tempVolForStrokes / pumpOutput;
                            volumePerStage = volumePerStage + calculatedVolume;
                            strokesPerStage = strokesPerStage + calculatedStrokes;
                            Log.d("1.02 -->", String.valueOf(CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
                        }
                    } else if (i == 0) {
                        if (j == i) {
                            tempCalculatedLength = sumOfAllDSLengths.get(j) - startingData;
                            double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                            double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                            double calculatedVolume = CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                            double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                            double calculatedStrokes = tempVolForStrokes / pumpOutput;
                            volumePerStage = volumePerStage + calculatedVolume;
                            strokesPerStage = strokesPerStage + calculatedStrokes;
                            Log.d("1.03 -->", String.valueOf(CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)) + " " + String.valueOf(startingData));
                        } else if (j > i) {
                            if (j < (sumOfAllDSLengths.size() - 1)) {
                                tempCalculatedLength = sumOfAllDSLengths.get(j) - sumOfAllDSLengths.get(j - 1);
                                double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                                double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                                double calculatedVolume = CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                                double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                                double calculatedStrokes = tempVolForStrokes / pumpOutput;
                                volumePerStage = volumePerStage + calculatedVolume;
                                strokesPerStage = strokesPerStage + calculatedStrokes;
                                Log.d("N.N2 -->", String.valueOf(CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
                            }
                        }
                    }
                    if (j - 1 >= 0) {
                        if (j < sumOfAllDSLengths.size() - 1) {
                            if (sumOfAllDSLengths.get(j + 1) > closingData) {
                                tempCalculatedLength = closingData - sumOfAllDSLengths.get(j);
                                double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                                double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                                double calculatedVolume = CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                                double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                                double calculatedStrokes = tempVolForStrokes / pumpOutput;
                                volumePerStage = volumePerStage + calculatedVolume;
                                strokesPerStage = strokesPerStage + calculatedStrokes;
                                Log.d("L.L1 -->", String.valueOf(CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
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
                                double calculatedVolume = CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                                double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                                double calculatedStrokes = tempVolForStrokes / pumpOutput;
                                volumePerStage = volumePerStage + calculatedVolume;
                                strokesPerStage = strokesPerStage + calculatedStrokes;
                                Log.d("L.L2 -->", String.valueOf(CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
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
                        double calculatedVolume = CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                        double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                        double calculatedStrokes = tempVolForStrokes / pumpOutput;
                        volumePerStage = volumePerStage + calculatedVolume;
                        strokesPerStage = strokesPerStage + calculatedStrokes;
                        Log.d("1.1 -->", String.valueOf(CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
                        break;
                    } else {
                        if (j == 0) {
                            tempCalculatedLength = closingData - startingData;
                        } else {
                            tempCalculatedLength = closingData - sumOfAllDSLengths.get(j - 1);
                        }
                        double od = Double.parseDouble(annulusDataList.get(j).getString_od());
                        double id = Double.parseDouble(holeDataList.get(i).getInput_id());
                        double calculatedVolume = CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit);
                        double tempVolForStrokes = Converter.VolumeConverter(volumeUnits, context.getResources().getString(R.string.bbl), calculatedVolume);
                        double calculatedStrokes = tempVolForStrokes / pumpOutput;
                        volumePerStage = volumePerStage + calculatedVolume;
                        strokesPerStage = strokesPerStage + calculatedStrokes;
                        Log.d("N.N3 -->", String.valueOf(CalculateVolume(context, tempCalculatedLength, id, od, diameter_unit, length_unit)));
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

    private static double CalculateVolume(Context context, double length, double id, double od, String diameterUnit, String lengthUnit) {
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

}
