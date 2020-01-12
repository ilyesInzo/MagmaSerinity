/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.bibliotheque;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *
 * @author inzo
 */
public class HeaderFooterPageEvent extends PdfPageEventHelper {

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        //super.onEndPage(writer, document); 
        Font footerFont = new Font(Font.FontFamily.UNDEFINED, 8);
        PdfContentByte contentByte = writer.getDirectContent();

        Phrase footer = new Phrase(" copyright © MagmaCompanies@gmail.com", footerFont);
        ColumnText.showTextAligned(contentByte, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 60, 0);

        Phrase header = new Phrase(" copyright © MagmaCompanies@gmail.com", footerFont);
        ColumnText.showTextAligned(contentByte, Element.ALIGN_CENTER,
                header,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.top() + 30, 0);

        
        Phrase page = new Phrase(writer.getPageNumber()+"", footerFont);
        ColumnText.showTextAligned(contentByte, Element.ALIGN_RIGHT,
                page,
                (document.right()) - document.rightMargin() + 10,
                document.bottom() - 60, 0);

    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        //super.onStartPage(writer, document); 
    }

}
