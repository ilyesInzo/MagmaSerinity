/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.bibliotheque;

import java.io.FileOutputStream;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author inzo
 */
public class GenerationPdf {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
            Font.BOLD);

    private static Font small = new Font(Font.FontFamily.TIMES_ROMAN, 11);

    private static BaseColor borderColor = new BaseColor(185, 188, 191);

    private static BaseColor enteteBackgroundColor = new BaseColor(229, 229, 229);

    public static void generationPdf(String image, String path, String docType, String numero, String date, ArrayList<String> entrepriseInfos, ArrayList<String> clientInfos, ArrayList<String> docEntete, ArrayList<ArrayList<String>> docMatrice, ArrayList<String> docSummerize) {

        try {
            Document document = new Document(PageSize.A4_LANDSCAPE);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);
            document.setMargins(50, 45, 50, 80);
            document.setMarginMirroring(false);
            document.open();

            addHeaderInfo(document, image, docType, numero, date);

            Paragraph paragraph = new Paragraph();
            addEmptyLine(paragraph, 1);
            document.add(paragraph);

            addCompanyCustomerInfo(document, entrepriseInfos, clientInfos);

            paragraph = new Paragraph();
            addEmptyLine(paragraph, 1);
            document.add(paragraph);

            addInvoiceInfo(document, docEntete, docMatrice);

            paragraph = new Paragraph();
            addEmptyLine(paragraph, 1);
            document.add(paragraph);

            addInvoiceSummarize(document, docSummerize);

            // must be replaced by go end page
            paragraph = new Paragraph();
            addEmptyLine(paragraph, 1);
            document.add(paragraph);

            addSignTable(writer, document);

            /*
            Font footerFont = new Font(Font.FontFamily.UNDEFINED, 8);
            PdfContentByte contentByte = writer.getDirectContent();

            Phrase footer = new Phrase(" Magma - copyright © www.aa.com", footerFont);

            ColumnText.showTextAligned(contentByte, Element.ALIGN_CENTER,
                    footer,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 10, 0);*/
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void addHeaderInfo(Document document, String image, String docType, String numero, String date) throws DocumentException, BadElementException, IOException {

        //String imgUrl = LireParametrage.returnValeurParametrage("urlImageNonPersistant") + "image.jpg";
        Image img = Image.getInstance(image);
        img.scaleAbsolute(200f, 100f);

        PdfPTable table = new PdfPTable(new float[]{2, 3});
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell c1 = new PdfPCell(img);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell();
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph paragraph = new Paragraph(docType, catFont);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        c1.addElement(paragraph);

        paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        c1.addElement(paragraph);

        PdfPTable table2 = new PdfPTable(new float[]{1, 1});
        table2.setWidthPercentage(80);
        table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell c2 = new PdfPCell(new Phrase("\u00a0\u00a0\u00a0 Numero", small));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        //c2.setPaddingBottom(10);
        c2.setBorderColor(borderColor);
        table2.addCell(c2);

        c2 = new PdfPCell(new Phrase("\u00a0\u00a0\u00a0 " + numero, small));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        //c2.setPaddingBottom(10);
        c2.setBorderColor(borderColor);
        table2.addCell(c2);

        c2 = new PdfPCell(new Phrase("\u00a0\u00a0\u00a0 Date", small));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        //c2.setPaddingBottom(10);
        c2.setBorderColor(borderColor);
        table2.addCell(c2);

        c2 = new PdfPCell(new Phrase("\u00a0\u00a0\u00a0 " + date, small));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        //c2.setPaddingBottom(10);
        c2.setBorderColor(borderColor);
        table2.addCell(c2);

        c1.addElement(table2);

        table.addCell(c1);
        document.add(table);

    }

    private static void addCompanyCustomerInfo(Document document, ArrayList<String> entrepriseInfos, ArrayList<String> clientInfos) throws DocumentException {

        PdfPTable tableContainer = new PdfPTable(new float[]{4, 1, 4});
        tableContainer.setWidthPercentage(100);
        tableContainer.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPTable tableElement = new PdfPTable(1);
        addCompanyCustomerEntete(tableElement, "Entreprise");
        PdfPCell c2;
        for (String entrepriseInfo : entrepriseInfos) {

            if (entrepriseInfo != null && !entrepriseInfo.isEmpty()) {
                c2 = new PdfPCell(new Phrase("\u00a0\u00a0\u00a0 " + entrepriseInfo, small));
                c2.setBorderColor(borderColor);
                tableElement.addCell(c2);
            }
        }

        tableContainer.addCell(tableElement);

        tableElement = new PdfPTable(1);
        tableElement.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        c2 = new PdfPCell(new Phrase());
        c2.setBorder(Rectangle.NO_BORDER);
        tableElement.addCell(c2);
        tableContainer.addCell(tableElement);

        tableElement = new PdfPTable(1);
        addCompanyCustomerEntete(tableElement, "Client");

        for (String clientInfo : clientInfos) {
            if (clientInfo != null && !clientInfo.isEmpty()) {
                c2 = new PdfPCell(new Phrase("\u00a0\u00a0\u00a0 " + clientInfo, small));
                c2.setBorderColor(borderColor);
                tableElement.addCell(c2);
            }
        }

        tableContainer.addCell(tableElement);
        document.add(tableContainer);

    }

    private static void addCompanyCustomerEntete(PdfPTable tableElement, String entete) {

        tableElement.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell c2 = new PdfPCell(new Phrase("\u00a0\u00a0\u00a0 " + entete, smallBold));
        c2.setBorderColor(borderColor);
        c2.setBackgroundColor(enteteBackgroundColor);
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        c2.setPaddingBottom(10);
        tableElement.addCell(c2);

    }

    private static void addInvoiceInfo(Document document, ArrayList<String> docEntetes, ArrayList<ArrayList<String>> docMatrice) throws DocumentException {

        
        
        
        PdfPTable tableInvoice = null;
        
        
        switch (docEntetes.size()) {
            case 7:
                tableInvoice = new PdfPTable(new float[]{3, 6, 2, 2, 2, 2,2});
                break;
            case 6:
                tableInvoice = new PdfPTable(new float[]{3, 6, 2, 2, 2, 2});
                break;
            case 5:
                tableInvoice = new PdfPTable(new float[]{3, 6, 2, 2, 2});
                break;
            default:
                tableInvoice = new PdfPTable(new float[]{3, 6, 2, 2});
                break;
        }
        
        
        tableInvoice.setWidthPercentage(100);

        PdfPCell invoiceCell = null;

        for (String docEntete : docEntetes) {
            invoiceCell = new PdfPCell(new Phrase(docEntete, smallBold));
            invoiceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            invoiceCell.setBorderColor(borderColor);
            invoiceCell.setPaddingBottom(10);
            tableInvoice.addCell(invoiceCell);

        }

        //ArrayList<ArrayList<String>> Matrice = new ArrayList<ArrayList<String>>();
        ArrayList Table = new ArrayList();

        /* for (int i = 0; i < 27; i++) {
            Table = new ArrayList();
            for (int j = 0; j < 6; j++) {

                Table.add("" + j);
            }
            Matrice.add(i, Table);
        }*/
        boolean b = false;
        for (ArrayList<String> arrayList : docMatrice) {
            for (String colone : arrayList) {

                //\u00a0\u00a0\u00a0
                String code = colone.split(":")[0];
                String value = colone.split(":")[1];
                int alignment = -1;
                if (code.equals("1")) {
                    value = "\u00a0\u00a0\u00a0 " + value;
                    alignment = Element.ALIGN_LEFT;
                } else if (code.equals("3")) {
                    value = value + " \u00a0\u00a0\u00a0";
                    alignment = Element.ALIGN_RIGHT;
                }

                Phrase phase = new Phrase(value, small);
                invoiceCell = new PdfPCell(phase);

                if (code.equals("0")) {
                    alignment = Element.ALIGN_LEFT;
                } else if (code.equals("2")) {
                    alignment = Element.ALIGN_CENTER;
                } else if (code.equals("4")) {
                    alignment = Element.ALIGN_RIGHT;
                }
                if (alignment != -1) {
                    invoiceCell.setHorizontalAlignment(alignment);
                }
                invoiceCell.setBorderColor(borderColor);
                invoiceCell.setBackgroundColor(b ? enteteBackgroundColor : BaseColor.WHITE);
                tableInvoice.addCell(invoiceCell);
            }
            b = !b;
        }

        document.add(tableInvoice);

    }

    private static void addInvoiceSummarize(Document document, ArrayList<String> docSummerize) throws DocumentException {

        PdfPTable tableSummary = new PdfPTable(new float[]{1, 1});
        tableSummary.setWidthPercentage(50);
        tableSummary.setHorizontalAlignment(Element.ALIGN_RIGHT);
        // don't dissociate
        tableSummary.setKeepTogether(true);
        PdfPCell summaryCell = null;
        for (int i = 0; i < docSummerize.size(); i++) {

            summaryCell = new PdfPCell(new Phrase(docSummerize.get(i).split(":")[0] + " \u00a0\u00a0\u00a0", i == docSummerize.size() - 1 ? smallBold : small));
            summaryCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            summaryCell.setBorderColor(borderColor);
            if (i == docSummerize.size() - 1) {
                summaryCell.setBackgroundColor(enteteBackgroundColor);
            }

            tableSummary.addCell(summaryCell);

            summaryCell = new PdfPCell(new Phrase(docSummerize.get(i).split(":")[1] + " \u00a0\u00a0\u00a0", i == docSummerize.size() - 1 ? smallBold : small));
            summaryCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            summaryCell.setBorderColor(borderColor);
            if (i == docSummerize.size() - 1) {
                summaryCell.setBackgroundColor(enteteBackgroundColor);
            }
            tableSummary.addCell(summaryCell);

        }

        document.add(tableSummary);

    }

    private static void addSignTable(PdfWriter writer, Document document) {

        PdfPTable tableSignContainer = new PdfPTable(new float[]{5, 7, 5});
        tableSignContainer.setWidthPercentage(100);
        tableSignContainer.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        tableSignContainer.setKeepTogether(true);
        PdfPTable tableSign = new PdfPTable(1);
        tableSign.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell c2 = new PdfPCell(new Phrase("\u00a0\u00a0\u00a0 Signature Entreprise", smallBold));
        c2.setBackgroundColor(enteteBackgroundColor);
        c2.setBorderColor(borderColor);
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        c2.setPaddingBottom(10);
        tableSign.addCell(c2);
        c2 = new PdfPCell();
        c2.setFixedHeight(30f);
        c2.setBorderColor(borderColor);
        tableSign.addCell(c2);
        tableSignContainer.addCell(tableSign);

        tableSign = new PdfPTable(1);
        tableSign.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        c2 = new PdfPCell(new Phrase());
        c2.setBorder(Rectangle.NO_BORDER);
        tableSign.addCell(c2);
        tableSignContainer.addCell(tableSign);

        tableSign = new PdfPTable(1);
        tableSign.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        c2 = new PdfPCell(new Phrase("\u00a0\u00a0\u00a0 Signature Client", smallBold));
        c2.setBackgroundColor(enteteBackgroundColor);
        c2.setBorderColor(borderColor);
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        c2.setPaddingBottom(10);
        tableSign.addCell(c2);
        c2 = new PdfPCell();
        c2.setFixedHeight(30f);
        c2.setBorderColor(borderColor);
        tableSign.addCell(c2);
        tableSignContainer.addCell(tableSign);

        // pour ajoute la table directement
        //document.add(tableSignContainer);
        // pour ajouter a la fin de la derniere page
        final int FIRST_ROW = 0;
        final int LAST_ROW = -1;
        //Table must have absolute width set.
        if (tableSignContainer.getTotalWidth() == 0) {
            tableSignContainer.setTotalWidth((document.right() - document.left()) * tableSignContainer.getWidthPercentage() / 100f);
        }
        tableSignContainer.writeSelectedRows(FIRST_ROW, LAST_ROW, document.left(), document.bottom() - 50 + tableSignContainer.getTotalHeight(), writer.getDirectContent());

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}
