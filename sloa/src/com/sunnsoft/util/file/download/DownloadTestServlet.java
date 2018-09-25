package com.sunnsoft.util.file.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadTestServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doPost(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)

	throws ServletException, IOException {
		// resp.reset();
		long pos = 0;

		String fileName = req.getParameter("file");

		OutputStream os = null;

		FileInputStream is = null;

		try {

			File f = new File("D:\\xx\\" + fileName);

			is = new FileInputStream(f);
			long fSize = f.length();
			byte xx[] = new byte[4096];
			resp.setHeader("Accept-Ranges", "bytes");
			resp.setHeader("Content-Length", fSize + "");
			resp.setHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			if (req.getHeader("Range") != null) {

				// 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)

				resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

				pos = Long.parseLong(req.getHeader("Range")

				.replaceAll("bytes=", "").replaceAll("-", ""));

			}

			if (pos != 0) {

				String contentRange = new StringBuffer("bytes ").append(
						new Long(pos).toString()).append("-").append(
						new Long(fSize - 1).toString()).append("/").append(
						new Long(fSize).toString()).toString();

				resp.setHeader("Content-Range", contentRange);

				System.out.println("Content-Range=" + contentRange);

				// 略过已经传输过的字节

				is.skip(pos);

			}

			os = resp.getOutputStream();

			boolean all = false;

			while (!all) {

				int n = is.read(xx);

				if (n != -1) {

					os.write(xx, 0, n);

				} else {
					all = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {

			if (is != null)
				is.close();
			if (os != null)
				os.close();
		}

	}

}
