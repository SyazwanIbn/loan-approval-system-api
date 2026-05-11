package com.loan.project.service;

import com.loan.project.dto.LoanApplicationResponse;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;

@Service
public class PdfGeneratorService {

    public byte[] generateApprovalLetter(LoanApplicationResponse loanResponse) {

        if (!"APPROVED".equals(loanResponse.getStatus())) {
            throw new RuntimeException("Cannot generate letter for non-approved loans");
        }

        //guna ByteArrayOutputStream supaya boleh hantar fail terus ke API tanpa simpan kat hard disk
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        //setup document dengan margin(set whitespace 50)
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            PdfWriter.getInstance(document, outputStream); //getInstance untuk hubungkan document dengan output
            document.open(); //document.open untuk open document

            //Font minimalist and corporate
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, new Color(44, 62, 80));
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.DARK_GRAY);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);

            //Tajuk Surat
            Paragraph title = new Paragraph("OFFICIAL LOAN APPROVAL LETTER", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(30f);
            document.add(title);

            //Introduction text
            Paragraph greeting = new Paragraph("Dear " + loanResponse.getCustomerName() + ",", bodyFont);
            greeting.setSpacingAfter(15f);
            document.add(greeting);

            Paragraph intro = new Paragraph(
                    "Congratulations ! We are pleased to inform you that your loan application has been " +
                            loanResponse.getStatus() + ". Below are the details of your loan agreement.", bodyFont);
            intro.setSpacingAfter(15f);
            document.add(intro);

            //details section
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            addRow(table, "Application ID", loanResponse.getApplicationId().toString(), headerFont, bodyFont);
            addRow(table, "Approved Amount", "RM" + loanResponse.getLoanAmount().toString(), headerFont, bodyFont);
            addRow(table, "Tenure", loanResponse.getTenureMonths() + " months", headerFont, bodyFont);
            addRow(table, "Date of Approval", loanResponse.getCreatedAt(), headerFont, bodyFont);

            document.add(table);

            // Closing text
            Paragraph closing = new Paragraph(
                    "Please review the terms and conditions of your loan agreement. If you have any questions, " +
                            "feel free to contact our customer service team.", bodyFont);
            closing.setSpacingBefore(15f);
            document.add(closing);

            //signature
            Paragraph sign = new Paragraph(
                    "Authorized Signature\n\n\n________________________\nLoan Officer", bodyFont);
            sign.setSpacingBefore(30f);
            document.add(sign);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error occur while generating PDF: " + e.getMessage());
        } finally {
            if (document.isOpen()) document.close();
        }
        return outputStream.toByteArray(); //return byte array untuk hantar terus ke API
    }

    //helper method addRow
    private int rowCount = 0; // field kat atas

    private void addRow(PdfPTable table, String key, String value, Font keyFont, Font valueFont) {

        Color bg = (rowCount % 2 == 0) ? new Color(245, 245, 245) : Color.WHITE;
        rowCount++;

        PdfPCell keyCell = new PdfPCell(new Phrase(key, keyFont));
        keyCell.setBorder(Rectangle.BOTTOM);
        keyCell.setBorderColor(Color.LIGHT_GRAY);
        keyCell.setPadding(10f);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBackgroundColor(bg);
        valueCell.setBorder(Rectangle.BOTTOM);
        valueCell.setBorderColor(Color.LIGHT_GRAY);
        valueCell.setPadding(10f);

        table.addCell(keyCell);
        table.addCell(valueCell);


    }
}
