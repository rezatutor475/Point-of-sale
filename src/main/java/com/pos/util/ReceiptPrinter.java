package com.pos.util;

import java.awt.*;
import java.awt.print.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for printing receipts in a POS system.
 */
public class ReceiptPrinter {

    private static final int LINE_HEIGHT = 15;
    private static final int MARGIN_LEFT = 10;
    private static final Font RECEIPT_FONT = new Font("Monospaced", Font.PLAIN, 10);
    private static final String DIVIDER = "------------------------------";
    private static final Logger LOGGER = Logger.getLogger(ReceiptPrinter.class.getName());

    /**
     * Prints a formatted receipt.
     *
     * @param storeName   the name of the store
     * @param items       list of item descriptions
     * @param totalAmount the total price
     * @param cashierName the name of the cashier
     */
    public static void printReceipt(String storeName, List<String> items, double totalAmount, String cashierName) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.setFont(RECEIPT_FONT);

            int y = LINE_HEIGHT;

            drawCenteredText(g2d, storeName.toUpperCase(), pageFormat, y);
            y += LINE_HEIGHT;

            g2d.drawString("Date: " + getCurrentDateTime(), MARGIN_LEFT, y);
            y += LINE_HEIGHT;
            g2d.drawString("Cashier: " + cashierName, MARGIN_LEFT, y);
            y += LINE_HEIGHT * 2;

            g2d.drawString("Items:", MARGIN_LEFT, y);
            y += LINE_HEIGHT;
            for (String item : items) {
                g2d.drawString("- " + item, MARGIN_LEFT, y);
                y += LINE_HEIGHT;
            }

            y += LINE_HEIGHT;
            drawDivider(g2d, y);
            y += LINE_HEIGHT;
            g2d.drawString("Total: " + formatCurrency(totalAmount) + " IRR", MARGIN_LEFT, y);
            y += LINE_HEIGHT;
            drawDivider(g2d, y);
            y += LINE_HEIGHT * 2;

            drawCenteredText(g2d, "Thank you for your purchase!", pageFormat, y);
            y += LINE_HEIGHT;
            drawCenteredText(g2d, "POS Powered by Java", pageFormat, y);

            return Printable.PAGE_EXISTS;
        });

        try {
            if (printerJob.printDialog()) {
                printerJob.print();
            }
        } catch (PrinterException e) {
            logError("Receipt printing failed", e);
        }
    }

    /**
     * Draws centered text within the printable area.
     *
     * @param g2d        the graphics object
     * @param text       the text to draw
     * @param pageFormat the page format
     * @param y          the y-coordinate for the text
     */
    private static void drawCenteredText(Graphics2D g2d, String text, PageFormat pageFormat, int y) {
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int x = (int) ((pageFormat.getImageableWidth() - textWidth) / 2);
        g2d.drawString(text, x, y);
    }

    /**
     * Draws a simple divider line.
     *
     * @param g2d the graphics object
     * @param y   the vertical position
     */
    private static void drawDivider(Graphics2D g2d, int y) {
        g2d.drawString(DIVIDER, MARGIN_LEFT, y);
    }

    /**
     * Formats a currency value to two decimal places.
     *
     * @param amount the amount
     * @return formatted string
     */
    private static String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("fa", "IR"));
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        return formatter.format(amount);
    }

    /**
     * Returns the current date and time formatted.
     *
     * @return current timestamp string
     */
    private static String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * Logs errors using a standard logger.
     *
     * @param message the error message
     * @param e       the exception
     */
    private static void logError(String message, Exception e) {
        LOGGER.log(Level.SEVERE, message, e);
    }
}
