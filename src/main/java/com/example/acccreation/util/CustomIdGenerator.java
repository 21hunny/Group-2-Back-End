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

    /**
     * Generates the next Event ID based on the current max ID in the event table.
     * If maxId is null/empty, returns "E001".
     * e.g. maxId="E007" -> next="E008"
     */
    public static String getNextEventId(String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return "E001";
        }
        String numericPart = maxId.substring(1); // Remove 'E'
        int numeric = Integer.parseInt(numericPart);
        numeric++;
        DecimalFormat df = new DecimalFormat("000");
        return "E" + df.format(numeric);
    }

    /**
     * Generates the next Workshop ID based on the current max ID in the workshop table.
     * If maxId is null/empty, returns "W001".
     * e.g. maxId="W007" -> next="W008"
     */
    public static String getNextWorkshopId(String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return "W001";
        }
        String numericPart = maxId.substring(1); // Remove 'W'
        int numeric = Integer.parseInt(numericPart);
        numeric++;
        DecimalFormat df = new DecimalFormat("000");
        return "W" + df.format(numeric);
    }

    /**
     * Generates the next Announcement ID based on the current max ID in the announcement table.
     * If maxId is null/empty, returns "AN001".
     * e.g. maxId="AN007" -> next="AN008"
     */
    public static String getNextAnnouncementId(String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return "AN001";
        }
        String numericPart = maxId.substring(2); // Remove 'AN'
        int numeric = Integer.parseInt(numericPart);
        numeric++;
        DecimalFormat df = new DecimalFormat("000");
        return "AN" + df.format(numeric);
    }

    /**
     * Generates the next Interview ID based on the current max ID in the interview table.
     * If maxId is null/empty, returns "I001".
     * e.g. maxId="I007" -> next="I008"
     */
    public static String getNextInterviewId(String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return "I001";
        }
        String numericPart = maxId.substring(1); // Remove 'I'
        int numeric = Integer.parseInt(numericPart);
        numeric++;
        DecimalFormat df = new DecimalFormat("000");
        return "I" + df.format(numeric);
    }

    /**
     * Generates the next Feedback ID based on the current max ID in the feedback table.
     * If maxId is null/empty, returns "F001".
     * e.g. maxId="F007" -> next="F008"
     */
    public static String getNextFeedbackId(String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return "F001";
        }
        String numericPart = maxId.substring(1); // Remove 'F'
        int numeric = Integer.parseInt(numericPart);
        numeric++;
        DecimalFormat df = new DecimalFormat("000");
        return "F" + df.format(numeric);
    }

    public static String getNextPortfolioId(String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return "P001";
        }
        String numericPart = maxId.substring(1); // Remove 'P'
        int numeric = Integer.parseInt(numericPart);
        numeric++;
        DecimalFormat df = new DecimalFormat("000");
        return "P" + df.format(numeric);
    }


    /**
     * Generates the next Team ID based on the current max ID in the team table.
     * If maxId is null/empty, returns "T001".
     * e.g. maxId="T007" -> next="T008"
     */
    public static String getNextTeamId(String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return "T001";
        }
        // "T007" -> strip 'T' -> "007"
        String numericPart = maxId.substring(1);
        int numeric = Integer.parseInt(numericPart);
        numeric++;
        DecimalFormat df = new DecimalFormat("000");
        return "T" + df.format(numeric);
    }

    /**
     * Generates the next ID for messages table.
     * If maxId is null/empty, returns "MSG001".
     * e.g. maxId="MSG007" -> next="MSG008"
     */
    public static String getNextMessageId(String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return "MSG001";
        }
        String numericPart = maxId.substring(3); // Remove "MSG"
        int numeric = Integer.parseInt(numericPart);
        numeric++;
        DecimalFormat df = new DecimalFormat("000");
        return "MSG" + df.format(numeric);
    }

    /**
     * Generates the next ID for progress_updates table.
     * If maxId is null/empty, returns "PU001".
     * e.g. maxId="PU007" -> next="PU008"
     */
    public static String getNextProgressUpdateId(String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return "PU001";
        }
        String numericPart = maxId.substring(2); // Remove "PU"
        int numeric = Integer.parseInt(numericPart);
        numeric++;
        DecimalFormat df = new DecimalFormat("000");
        return "PU" + df.format(numeric);
    }

    /**
     * Generates the next ID for document_uploads table.
     * If maxId is null/empty, returns "DOC001".
     * e.g. maxId="DOC007" -> next="DOC008"
     */
    public static String getNextDocumentUploadId(String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return "DOC001";
        }
        String numericPart = maxId.substring(3); // Remove "DOC"
        int numeric = Integer.parseInt(numericPart);
        numeric++;
        DecimalFormat df = new DecimalFormat("000");
        return "DOC" + df.format(numeric);
    }



}
