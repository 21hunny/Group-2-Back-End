package com.example.acccreation.util;

import java.text.DecimalFormat;

public class CustomIdGenerator {

    /**
     * Generates the next Admin ID based on the current max ID in the admin table.
     * If maxId is null/empty, returns "A001".
     * e.g. maxId="A007" -> next="A008"
     */
    public static String getNextAdminId(String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return "A001";
        }
        // "A007" -> strip 'A' -> "007"
        String numericPart = maxId.substring(1);
        int numeric = Integer.parseInt(numericPart);
        numeric++;
        DecimalFormat df = new DecimalFormat("000");
        return "A" + df.format(numeric);
    }

    /**
     * Generates the next Lecturer ID based on the current max ID in the lecturer table.
     * If maxId is null/empty, returns "L001".
     * e.g. maxId="L010" -> next="L011"
     */
    public static String getNextLecturerId(String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return "L001";
        }
        // "L010" -> strip 'L' -> "010"
        String numericPart = maxId.substring(1);
        int numeric = Integer.parseInt(numericPart);
        numeric++;
        DecimalFormat df = new DecimalFormat("000");
        return "L" + df.format(numeric);
    }

    /**
     * Generates the next Student ID for a per-batch table.
     * If maxId is null/empty, returns "<batchId>-001".
     * e.g. maxId="gadse232f-043" -> next="gadse232f-044"
     */
    public static String getNextStudentId(String batchId, String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return batchId + "-001";
        }
        // e.g. "gadse232f-043"
        int dashIndex = maxId.lastIndexOf("-");
        String numericPart = maxId.substring(dashIndex + 1); // "043"
        int numeric = Integer.parseInt(numericPart);
        numeric++;
        DecimalFormat df = new DecimalFormat("000");
        return batchId + "-" + df.format(numeric);
    }
}
