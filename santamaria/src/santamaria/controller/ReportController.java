package santamaria.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import santamaria.sessions.BusinessSBSLLocal;

public class ReportController {

	@EJB(mappedName = "ejb/BusinessLocal")
	protected BusinessSBSLLocal businessLocal;

	@ManagedProperty("#{msg}")
	private ResourceBundle bundle;

	@ManagedProperty("#{msgApps}")
	private ResourceBundle bundleApps;

	public static final String REPORT_TYPE_EXCEL = "X";
	public static final String REPORT_TYPE_PDF = "P";
	public static final long REPORT_PDF_DESTINATION_ATTACHMENT = 1L;
	public static final long REPORT_PDF_DESTINATION_PREVIEW = 2L;

	private String sessionKey;
	private boolean preview = false;

	/****** ATRIBUTOS PARA REPORTES **********************/
	private Map<String, Object> rptParam;
	private String reportId;
	private String rptURL;
	private String frmtRepo = REPORT_TYPE_PDF;
	private String fileName;
	private String jdbcName;
	private StreamedContent fileGenerated;
	private long destination = REPORT_PDF_DESTINATION_PREVIEW;
	private boolean sendLogo = false;
	private int pageWidth = 0;
	private int pageHeight = 0;
	/***************************************************/

	/****** ATRIBUTOS PARA REPORTES EXPORTADOS DATATABLE **********************/
	private String reportInstitucion;
	private String reportSistema;
	private String reportName;

	/***************************************************/

	public ReportController() {
		super();
	}

	public void onReport() {
		Connection conexion = null;
		try {
			if (beforeReport()) {
				if (getRptParam() == null || getRptParam().isEmpty()) {
					setRptParam(new HashMap<String, Object>());
				}
				if (reportId != null && !reportId.isEmpty()) {
					FacesContext context = FacesContext.getCurrentInstance();
					if (isSendLogo()) {
						getRptParam().put(
								"IMG_LOGO_LEFT",
								context.getExternalContext().getRealPath(
										getBundle().getString("imgLogoLeft")));
						getRptParam().put(
								"IMG_LOGO_RIGHT",
								context.getExternalContext().getRealPath(
										getBundle().getString("imgLogoRight")));
					}
					Context ctx = new InitialContext();
					DataSource ds = (DataSource) ctx.lookup(jdbcName);
					conexion = ds.getConnection();
					conexion.setAutoCommit(true);

					File file = new File(context.getExternalContext()
							.getRealPath(
									this.rptURL + this.reportId + ".jasper"));

					getRptParam().put(JRParameter.REPORT_LOCALE,
							context.getViewRoot().getLocale());

					if (getFrmtRepo().equals(REPORT_TYPE_EXCEL)) {
						getRptParam().put("IS_IGNORE_PAGINATION", true);
						File f;

						JasperPrint jasperPrint = JasperFillManager.fillReport(
								file.toString(), getRptParam(), conexion);
						if (this.fileName == null || this.fileName.equals("")) {
							this.fileName = this.reportId;
						}
						String xlsFileName = this.fileName + ".xls";

						// Creacion del XLS
						JRXlsExporter exporter = new JRXlsExporter();
						exporter.setParameter(JRExporterParameter.JASPER_PRINT,
								jasperPrint);
						exporter.setParameter(
								JRExporterParameter.OUTPUT_FILE_NAME,
								xlsFileName);
						exporter.setParameter(
								JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
								false);
						exporter.setParameter(
								JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
								true);
						exporter.exportReport();

						/*
						 * exporter.setExporterInput(new SimpleExporterInput(
						 * jasperPrint)); exporter.setExporterOutput(new
						 * SimpleOutputStreamExporterOutput( xlsFileName));
						 * SimpleXlsReportConfiguration configuration = new
						 * SimpleXlsReportConfiguration();
						 * configuration.setOnePagePerSheet(false);
						 * configuration.setDetectCellType(true);
						 * exporter.setConfiguration(configuration);
						 * exporter.exportReport();
						 */

						f = new File(xlsFileName);

						HttpServletResponse response = (HttpServletResponse) context
								.getExternalContext().getResponse();
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition",
								"attachment;filename=\"" + xlsFileName + "\"");
						ServletOutputStream output = null;
						InputStream input = null;
						input = new FileInputStream(f);
						output = response.getOutputStream();

						byte[] buf = new byte[1024];
						int len;
						while ((len = input.read(buf)) > 0) {
							output.write(buf, 0, len);
						}
						output.flush();
						output.close();
						input.close();
					} else {
						JasperPrint jasperPrint = JasperFillManager.fillReport(
								file.getPath(), getRptParam(), conexion);
						HttpServletResponse response = (HttpServletResponse) context
								.getExternalContext().getResponse();
						if (getDestination() == REPORT_PDF_DESTINATION_PREVIEW) {
							response.setContentType("application/pdf");
						} else {
							response.addHeader("Content-disposition",
									"attachment;filename=" + this.fileName
											+ ".pdf");
						}
						JasperExportManager.exportReportToPdfStream(
								jasperPrint, response.getOutputStream());

					}
					afterReport();
					context.responseComplete();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conexion != null) {
				try {
					conexion.close();
					conexion = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected boolean beforeReport() {
		return true;
	}

	protected void afterReport() {
	}

	public void onGenerateReport() {

		InputStream inputStream = null;
		Connection conn = null;
		String contentType = null;
		setFileGenerated(null);
		try {
			if (beforeReport()) {
				if (getRptParam() == null || getRptParam().isEmpty()) {
					setRptParam(new HashMap<String, Object>());
				}
				if (getReportId() != null && !getReportId().isEmpty()) {
					FacesContext context = FacesContext.getCurrentInstance();
					if (isSendLogo()) {
						getRptParam().put(
								"IMG_LOGO_LEFT",
								context.getExternalContext().getRealPath(
										getBundle().getString("imgLogoLeft")));
						getRptParam().put(
								"IMG_LOGO_RIGHT",
								context.getExternalContext().getRealPath(
										getBundle().getString("imgLogoRight")));
					}
					if (getFileName() == null || getFileName().equals("")) {
						setFileName(getReportId());
					}
					Context ctx = new InitialContext();
					DataSource ds = (DataSource) ctx.lookup(getJdbcName());
					conn = ds.getConnection();
					conn.setAutoCommit(true);
					ByteArrayOutputStream teste = new ByteArrayOutputStream();
					File file = new File(context.getExternalContext()
							.getRealPath(
									getRptURL() + getReportId() + ".jasper"));

					getRptParam().put(JRParameter.REPORT_LOCALE,
							context.getViewRoot().getLocale());

					if (getFrmtRepo().equals(REPORT_TYPE_EXCEL)) {
						getRptParam().put("IS_IGNORE_PAGINATION", true);
						JasperPrint jasperPrint = JasperFillManager.fillReport(
								file.toString(), getRptParam(), conn);
						setFileName(getFileName() + ".xls");

						JRXlsExporter exporter = new JRXlsExporter();

						exporter.setParameter(JRExporterParameter.JASPER_PRINT,
								jasperPrint);
						exporter.setParameter(
								JRExporterParameter.OUTPUT_STREAM, teste);
						exporter.setParameter(
								JRExporterParameter.OUTPUT_FILE_NAME,
								getFileName());
						exporter.setParameter(
								JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
								false);
						exporter.setParameter(
								JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
								true);
						exporter.exportReport();
						/*
						 * exporter.setExporterInput(new SimpleExporterInput(
						 * jasperPrint)); exporter.setExporterOutput(new
						 * SimpleOutputStreamExporterOutput( getFileName()));
						 * SimpleXlsReportConfiguration configuration = new
						 * SimpleXlsReportConfiguration();
						 * configuration.setOnePagePerSheet(false);
						 * configuration.setDetectCellType(true);
						 * exporter.setConfiguration(configuration);
						 * 
						 * exporter.exportReport();
						 */

						inputStream = new ByteArrayInputStream(
								teste.toByteArray());
						contentType = "application/vnd.ms-excel";
					} else {
						setFileName(getFileName() + ".pdf");

						JasperPrint print = JasperFillManager.fillReport(
								file.getPath(), getRptParam(), conn);
						if (pageHeight > 0) {
							print.setPageHeight(pageHeight);
						}
						if (pageWidth > 0) {
							print.setPageWidth(pageWidth);
						}
						 JRExporter exporter = new JRPdfExporter();
						//JRPdfExporter exporter = new JRPdfExporter();
						
						  exporter.setParameter(JRExporterParameter.
						  OUTPUT_STREAM, teste);
						  exporter.setParameter(JRExporterParameter.
						  JASPER_PRINT, print);
						 
						/*exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
								teste));
						exporter.setExporterInput(new SimpleExporterInput(print));*/

						exporter.exportReport();
						inputStream = new ByteArrayInputStream(
								teste.toByteArray());
						contentType = "application/pdf";
					}
					setFileGenerated(new DefaultStreamedContent(inputStream,
							contentType, getFileName()));

					if (isPreview()) {
						setSessionKey(UUID.randomUUID().toString());
						context.getExternalContext().getSessionMap()
								.put(getSessionKey(), getFileGenerated());
					}
				}
			}
			afterReport();
		} catch (JRException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void onList(Object document) throws IOException,
			BadElementException, DocumentException {

		if (beforeList(document)) {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			String pathImg = servletContext.getRealPath("") + File.separator
					+ "resources" + File.separator + "images" + File.separator
					+ "reportes" + File.separator;

			Paragraph parrafoHoja = new Paragraph();
			float[] colsWidth = { 1f, 3f, 1f };
			PdfPTable tabla = new PdfPTable(colsWidth);
			tabla.setWidthPercentage(100);
			tabla.setHorizontalAlignment(Element.ALIGN_CENTER);

			Image im = Image.getInstance(pathImg + "logo.jpg");
			PdfPCell cell = new PdfPCell(im);
			cell.setRowspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(0);
			tabla.addCell(cell);

			PdfPCell cell1 = new PdfPCell(new Paragraph(reportInstitucion));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell1.setBorder(0);
			cell1.setNoWrap(false);
			tabla.addCell(cell1);

			Image im1 = Image.getInstance(pathImg + "escudo.jpg");
			PdfPCell cell2 = new PdfPCell(im1);
			cell2.setRowspan(3);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell2.setBorder(0);
			tabla.addCell(cell2);

			PdfPCell cell3 = new PdfPCell(new Paragraph(reportSistema));
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell3.setBorder(0);
			cell3.setNoWrap(false);
			tabla.addCell(cell3);

			PdfPCell cell4 = new PdfPCell(new Paragraph(reportName));
			cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell4.setBorder(0);
			cell4.setNoWrap(false);
			tabla.addCell(cell4);

			parrafoHoja.add(tabla);

			HeaderFooter header = new HeaderFooter(parrafoHoja, false);
			HeaderFooter footer = new HeaderFooter(new Phrase("Pagina "), true);
			Document pdf = (Document) document;
			pdf.setHeader(header);
			pdf.setFooter(footer);
			pdf.setPageSize(PageSize.LETTER);
			pdf.setMargins(36, 36, 36, 36);
			pdf.setMarginMirroring(true);
			afterList(pdf);
		}
	}

	public boolean beforeList(Object document) {
		return true;
	}

	public void afterList(Object document) {

	}

	public void onCloseRep() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.remove(sessionKey);
		setFileGenerated(null);
		this.preview = false;
		this.fileName = null;
		this.reportId = null;
		afterCloseRep();
	}

	public void afterCloseRep() {

	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public boolean isPreview() {
		return preview;
	}

	public void setPreview(boolean preview) {
		this.preview = preview;
	}

	public Map<String, Object> getRptParam() {
		return rptParam;
	}

	public void setRptParam(Map<String, Object> rptParam) {
		this.rptParam = rptParam;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getRptURL() {
		return rptURL;
	}

	public void setRptURL(String rptURL) {
		this.rptURL = rptURL;
	}

	public String getFrmtRepo() {
		return frmtRepo;
	}

	public void setFrmtRepo(String frmtRepo) {
		this.frmtRepo = frmtRepo;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getJdbcName() {
		return jdbcName;
	}

	public void setJdbcName(String jdbcName) {
		this.jdbcName = jdbcName;
	}

	public StreamedContent getFileGenerated() {
		return fileGenerated;
	}

	public void setFileGenerated(StreamedContent fileGenerated) {
		this.fileGenerated = fileGenerated;
	}

	public long getDestination() {
		return destination;
	}

	public void setDestination(long destination) {
		this.destination = destination;
	}

	public boolean isSendLogo() {
		return sendLogo;
	}

	public void setSendLogo(boolean sendLogo) {
		this.sendLogo = sendLogo;
	}

	public int getPageWidth() {
		return pageWidth;
	}

	public void setPageWidth(int pageWidth) {
		this.pageWidth = pageWidth;
	}

	public int getPageHeight() {
		return pageHeight;
	}

	public void setPageHeight(int pageHeight) {
		this.pageHeight = pageHeight;
	}

	public String getReportInstitucion() {
		return reportInstitucion;
	}

	public void setReportInstitucion(String reportInstitucion) {
		this.reportInstitucion = reportInstitucion;
	}

	public String getReportSistema() {
		return reportSistema;
	}

	public void setReportSistema(String reportSistema) {
		this.reportSistema = reportSistema;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	public ResourceBundle getBundleApps() {
		return bundleApps;
	}

	public void setBundleApps(ResourceBundle bundleApps) {
		this.bundleApps = bundleApps;
	}
}
