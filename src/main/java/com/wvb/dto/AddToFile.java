/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package com.wvb.dto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;

import com.wvb.model.DataModel;

/**
 *
 * @author AL-ME3RAJ
 */
public class AddToFile {

    public static void saveCompanyFile(ArrayList<com.wvb.model.DataModel> list) {

        BufferedWriter writer = null;
        try {

            File logFile = new File("D:\\pr\\PR.txt");

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("Company Code" + "\t" + "ParentUrl" + "\t" + "News Url \t"
                    + "Title" + "\t" + "Publish Date");

            writer.newLine();
            for (DataModel companyInfo : list) {
                writer.write(companyInfo.getCompanyId() + "\t" + companyInfo.getParentUrl() + "\t"
                        + companyInfo.getDataSheetUrl() + "\t"
                        + companyInfo.getNewsTitle() + "\t" + companyInfo.getDate());
                writer.newLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }

    }

}
