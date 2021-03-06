/*
 * Copyright 2013 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.gwt.site.uploader;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class Uploader {
	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("Usage Uploader <filesDir> <UploadUrl>");
			throw new IllegalArgumentException("Usage Uploader <filesDir> <UploadUrl>");
		}

		FileTraverser traverser = new FileTraverser(args[0]);
		List<MarkdownFile> markdownFiles = traverser.traverse(args[0]);

		for (MarkdownFile markdownFile : markdownFiles) {
			System.out.println(markdownFile.getPath());
			putFile(args[1], markdownFile.getPath(), markdownFile.getContent());
		}
	}

	private static void putFile(String uploadUrl, String relativePath, String content) {
		URL url;
		try {
			url = new URL(uploadUrl + relativePath);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("PUT");
			OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
			out.write(content);
			out.close();
			System.out.println(httpCon.getResponseCode());
			if (httpCon.getResponseCode() != 201) {
				// TODO Errorhandling
				System.out.println("Response code: " + httpCon.getResponseCode());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
