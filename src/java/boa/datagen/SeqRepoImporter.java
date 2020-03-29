/*
 * Copyright 2015, Hridesh Rajan, Robert Dyer, Hoan Nguyen
 *                 and Iowa State University of Science and Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boa.datagen;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.SequenceFile.CompressionType;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import boa.datagen.paper.PaperJson;
import boa.datagen.util.FileIO;
import boa.datagen.util.Properties;
import boa.types.Toplevel.Paper;

public class SeqRepoImporter {
	private final static boolean debug = Properties.getBoolean("debug", DefaultProperties.DEBUG);
	private final static String base = Properties.getProperty("output.path", DefaultProperties.OUTPUT);
	private final static String jsonPath = Properties.getProperty("gh.json.path", DefaultProperties.GH_JSON_PATH);
	private final static int poolSize = Integer
			.parseInt(Properties.getProperty("num.threads", DefaultProperties.NUM_THREADS));

	private static Configuration conf = null;
	private static FileSystem fileSystem = null;
	private static boolean done = false;
	private static HashMap<String, CSVRecord> metaDataMap = null;
	
	
//	private static int count = 0;

	public static void main(String[] args) throws IOException, InterruptedException {

		conf = new Configuration();
		fileSystem = FileSystem.get(conf);

		System.out.println(jsonPath);
		System.out.println(base);
		System.out.println(poolSize);
		System.out.println(debug);

		String metaPath = jsonPath + "/metadata.csv";
		metaDataMap = getMetadataMap(metaPath);

		// assign each thread with a worker
		ImportTask[] workers = new ImportTask[poolSize];
		Thread[] threads = new Thread[poolSize];
		for (int i = 0; i < poolSize; i++) {
			workers[i] = new ImportTask(i);
			threads[i] = new Thread(workers[i]);
			threads[i].start();
			Thread.sleep(10);
		}

		int jsonCounter = 0;
		File dir = new File(jsonPath);
		Queue<File> queue = new LinkedList<File>();
		queue.offer(dir);
		while (!queue.isEmpty()) {
			File cur = queue.poll();
			for (File file : cur.listFiles()) {
				if (file.isDirectory())
					queue.offer(file);
				if (file.isFile() && file.getName().endsWith(".json")) {
					jsonCounter++;
					String content = FileIO.readFileContents(file);
					// assign each file content to each worker
					try {
						boolean assigned = false;
						while (!assigned) {
							for (int j = 0; j < poolSize; j++) {
								if (workers[j].isReady()) {
									workers[j].assignTask(content);
									workers[j].setReady(false);
									assigned = true;
									break;
								}
							}
						}
						if (debug)
							System.err.println("Assigned the " + jsonCounter + "th json file");
					} catch (Exception e) {
						System.err.println("Error proccessing " + jsonCounter + "th json file");
						e.printStackTrace();
					}
				}
			}
		}
		
		

		for (int j = 0; j < poolSize; j++)
			while (!workers[j].isReady())
				Thread.sleep(100);
		setDone(true);
		// wait for workers to close writers and finish
		for (Thread thread : threads)
			while (thread.isAlive())
				Thread.sleep(1000);
		
//		System.out.println(count);
	}
	
//	synchronized static void incrementCount() {
//		count++;
//	}

	synchronized static CSVRecord removeMetadata(String sha) {
		return metaDataMap.remove(sha);
	}

	synchronized static HashMap<String, CSVRecord> getMetadataMap(String path) throws IOException {
		HashMap<String, CSVRecord> map = new HashMap<String, CSVRecord>();
		Reader in = new FileReader(path);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
		for (CSVRecord record : records) {
			String sha = record.get("sha");			
			if (sha.contains("; "))
				sha = sha.split("; ")[0];
			if (!map.containsKey(sha))
				map.put(sha, record);
		}
		return map;
	}

	synchronized static boolean getDone() {
		return SeqRepoImporter.done;
	}

	synchronized static void setDone(boolean done) {
		SeqRepoImporter.done = done;
	}

	public static class ImportTask implements Runnable {
		private int id;
		private int counter = 0;
		private String suffix;
		private SequenceFile.Writer paperWriter;
		private volatile boolean ready = true;
		volatile String content;

		public ImportTask(int id) {
			setId(id);
		}

		public synchronized void assignTask(String content) {
			setContent(content);
		};

		synchronized void openWriters() {
			long time = System.currentTimeMillis();
			suffix = getId() + "-" + time + ".seq";
			while (true) {
				try {
					System.out.println(Thread.currentThread().getName() + " " + getId() + " " + suffix + " starts!");
					paperWriter = SequenceFile.createWriter(fileSystem, conf, new Path(base + "/paper/" + suffix),
							Text.class, BytesWritable.class, CompressionType.BLOCK);
					break;
				} catch (Throwable t) {
					t.printStackTrace();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}

		synchronized void closeWriters() {
			while (true) {
				try {
					paperWriter.close();
					System.out.println(Thread.currentThread().getName() + " " + getId() + " " + suffix + " done!!!");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					break;
				} catch (Throwable t) {
					t.printStackTrace();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}

		@Override
		public void run() {
			openWriters();
			while (true) {
				while (isReady()) {
					if (getDone())
						break;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (getDone())
					break;
				try {
					if (content != null) {
						Gson parser = new Gson();

						// parse json file
						JsonObject jo = null;
						try {
							jo = parser.fromJson(content, JsonElement.class).getAsJsonObject();
						} catch (Exception e) {
							e.printStackTrace();
						}

						// check duplication
						String id = null;
						if (jo.has("paper_id"))
							id = jo.get("paper_id").getAsString();
						
						CSVRecord metadataRecord = removeMetadata(id);
						if (metadataRecord != null) {
							// update protocbuf
							Paper paper = PaperJson.getPaper(jo, metadataRecord);
							
							if (debug)
								System.err.println(
										Thread.currentThread().getName() + " id: " + Thread.currentThread().getId()
												+ " is putting paper " + id + " in sequence file");

							// write to a sequence file
							try {
								paperWriter.append(new Text(paper.getId()), new BytesWritable(paper.toByteArray()));
								counter++;
							} catch (IOException e) {
								e.printStackTrace();
							}

							// write to a new sequence file if the previous one has written 1000 items
							if (counter >= Integer.parseInt(DefaultProperties.MAX_PROJECTS)) {
								closeWriters();
								openWriters();
								counter = 0;
							}
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
				setReady(true);
			}
			closeWriters();
		}

		synchronized public String getContent() {
			return content;
		}

		synchronized public void setContent(String content) {
			this.content = content;
		}

		synchronized boolean isReady() {
			return this.ready;
		}

		synchronized void setReady(boolean ready) {
			this.ready = ready;
		}

		synchronized int getId() {
			return this.id;
		}

		synchronized void setId(int id) {
			this.id = id;
		}
	}

	public synchronized static void printError(final Throwable e, final String message, String name) {
		System.err.println("ERR: " + message + " proccessing: " + name);
		if (debug) {
			e.printStackTrace();
			// System.exit(-1);
		} else
			System.err.println(e.getMessage());
	}
}
