package santamaria.servlets;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.primefaces.model.StreamedContent;

/**
 * Servlet implementation class GeneraReporte
 */
@WebServlet("/generaReporte")
public class GeneraReporte extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static final int DEFAULT_BUFFER_SIZE = 10240;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GeneraReporte() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionKey = request.getParameter("sessionKey");
		StreamedContent fileGenerated = (StreamedContent) request.getSession().getAttribute(sessionKey);
        byte[] image = null;
        try {
        	if (fileGenerated != null) {
        		image = IOUtils.toByteArray(fileGenerated.getStream());
        		response.reset();
	            response.setBufferSize(DEFAULT_BUFFER_SIZE);
	            response.setContentType(fileGenerated.getContentType());
	            response.setHeader("Content-Disposition", "inline; filename=\""
	                    + fileGenerated.getName()+ "\"");
	            response.setContentLength(image.length);
        	}
        	BufferedOutputStream output = null;
        	try {
        		output = new BufferedOutputStream(response.getOutputStream(),
                        DEFAULT_BUFFER_SIZE);
                output.write(image);
        	} catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
