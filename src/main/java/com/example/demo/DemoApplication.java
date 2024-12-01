package com.example.demo;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Image;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class DemoApplication {

	public static void main(String[] args) {
		// Define the output file
		String outputFile = "ColorfulReport.pdf";

		try {
			// Create a Document instance
			Document document = new Document();

			// Create a PdfWriter instance to write to the file
			PdfWriter writer=PdfWriter.getInstance(document, new FileOutputStream(outputFile));

			// Open the document to start writing
			document.open();

			//Giving the document see green color
			Rectangle background = new Rectangle(0, 0, 595, 842); // A4 width with custom height
			background.setBackgroundColor(new Color(225, 238, 221)); // Sea Green color
			document.add(background);


			// Add a title with a background color
			Font titleFont = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL, Color.WHITE);
			PdfContentByte canvas = writer.getDirectContent();

			// Step 5: Draw a rectangle for the title background
			canvas.setColorFill(new Color(255, 0, 0)); // Sea Green color
			canvas.rectangle(0, 792 - 50, 595, 40); // Top of A4 page, full width, height 50
			canvas.fill();

			ColumnText.showTextAligned(
					canvas,
					Element.ALIGN_CENTER,         // Align the text to the center
					new Phrase("WHAT A CREDIT CARD SHOULD BE", titleFont), // Create a Phrase with the title and font
					100,                      // X-coordinate (center of the page: width / 2 = 595 / 2)
					792 - 35,                    // Y-coordinate (positioned in the middle of the rectangle)
					0                            // Rotation angle
			);



			// Add a colorful section header
			Paragraph sectionHeader = new Paragraph();
			sectionHeader.setSpacingBefore(30);
			sectionHeader.setSpacingAfter(10);
			document.add(sectionHeader);

			// Add text content with a different font color
			Font textFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK);
			Paragraph text = new Paragraph(" GOKUL TEXTPRINT PRIVATE lIMITED", textFont);
			text.setSpacingAfter(20);
			document.add(text);

			String svgPath="Kotak Mahindra Bank Logo.svg";
			byte[] pngBytes = convertSvgToPng(svgPath);

			// Load the PNG into OpenPDF
			Image logo = Image.getInstance(pngBytes);
			logo.scaleToFit(100, 50); // Scale the logo size
			logo.setAbsolutePosition(500, 792-20); // Position at top-left
			canvas.addImage(logo);

			// Add a table with alternating row colors
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100);

			Font headerFont = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL, Color.WHITE);

			String[] headers = { "Previous Amount (Due Rs.)", "Purchase & Other Charges(Rs.)", "Payments(Rs.) ", "Total Amount Due(Rs.)" };

			// Define header background color
			Color headerBackgroundColor = new Color(204, 153, 0); // Custom color

			// Loop through headers and add them to the table
			for (String headerTitle : headers) {
				PdfPCell headerCell = new PdfPCell(new Phrase(headerTitle, headerFont));
				headerCell.setBackgroundColor(headerBackgroundColor);
				headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				headerCell.setFixedHeight(30f); // Set fixed height for header
				table.addCell(headerCell);
			}

			// Add table rows with alternating colors
			for (int i = 1; i <= 6; i++) {
				PdfPCell cell1 = new PdfPCell(new Phrase("633.63"));
				PdfPCell cell2 = new PdfPCell(new Phrase("Row " + i + ", Col 2", textFont));
				PdfPCell cell3 = new PdfPCell(new Phrase("Row " + i + ", Col 3", textFont));

				if (i % 2 == 0) {
					cell1.setBackgroundColor(new Color(204, 153, 0));
					cell2.setBackgroundColor(Color.LIGHT_GRAY);
					cell3.setBackgroundColor(Color.LIGHT_GRAY);
				}

				table.addCell(cell1);
				table.addCell(cell2);
				table.addCell(cell3);
			}

			document.add(table);

			// Add a footer
			Font footerFont = new Font(Font.HELVETICA, 10, Font.ITALIC, Color.GRAY);
			Paragraph footer = new Paragraph("This is a footer. Page 1 of 1.", footerFont);
			footer.setAlignment(Element.ALIGN_CENTER);
			footer.setSpacingBefore(30);
			document.add(footer);

			// Close the document
			document.close();

			System.out.println("PDF created successfully at " + outputFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static byte[] convertSvgToPng(String svgFilePath) throws Exception {
		File svgFile = new File(svgFilePath);
		FileInputStream inputStream = new FileInputStream(svgFile);

		// Prepare the Batik Transcoder
		PNGTranscoder transcoder = new PNGTranscoder();
		TranscoderInput input = new TranscoderInput(inputStream);

		// Output Stream for PNG
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		TranscoderOutput output = new TranscoderOutput(outputStream);

		// Perform the conversion
		transcoder.transcode(input, output);
		inputStream.close();
		outputStream.flush();

		return outputStream.toByteArray(); // Return PNG bytes
	}
}

