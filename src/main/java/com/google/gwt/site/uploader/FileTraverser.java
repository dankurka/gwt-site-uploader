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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class FileTraverser {
	List<MarkdownFile> markdownFiles = new LinkedList<MarkdownFile>();
	private final String filesDir;

	public FileTraverser(String filesDir) {
		this.filesDir = filesDir;

	}

	public List<MarkdownFile> traverse(String path) {

		File file = new File(path);
		if (file.isDirectory()) {

			System.out.println("dir found: " + path);
			File[] listFiles = file.listFiles();
			for (File f : listFiles) {
				traverse(f.getPath());
			}
		} else if (file.isFile() && file.getName().endsWith(".html")) {

			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(file);

				markdownFiles.add(new MarkdownFile(path.replace(filesDir, ""), IOUtils.toString(fileInputStream, "UTF-8")));
				System.out.println("file found: " + path);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileInputStream != null) {
					try {
						fileInputStream.close();
					} catch (IOException ignored) {

					}
				}
			}
		}
		return markdownFiles;
	}
}
