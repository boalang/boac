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
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang.SerializationUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.LargeObjectException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.internal.storage.file.ByteArrayFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import boa.datagen.forges.github.RepoMetadata;
import boa.datagen.scm.AbstractConnector;
import boa.datagen.scm.GitConnector;
import boa.datagen.util.FileIO;
import boa.datagen.util.Properties;
import boa.types.Code.CodeRepository;
import boa.types.Code.Revision;
import boa.types.Toplevel.Project;

public class SeqRepoImporter {
	private final static boolean debug = Properties.getBoolean("debug", DefaultProperties.DEBUG);
	private final static boolean cache = Properties.getBoolean("cache", DefaultProperties.CACHE);

	private final static String base = Properties.getProperty("output.path", DefaultProperties.OUTPUT);
	private final static String jsonPath = Properties.getProperty("gh.json.path", DefaultProperties.GH_JSON_PATH);
	private final static int poolSize = Integer.parseInt(Properties.getProperty("num.threads", DefaultProperties.NUM_THREADS));

	private static Configuration conf = null;
	private static FileSystem fileSystem = null;
	private static boolean done = false;
	
	
//	private static final int MAX_SIZE_FOR_PROJECT_WITH_COMMITS = Integer.valueOf(DefaultProperties.MAX_SIZE_FOR_PROJECT_WITH_COMMITS);
//	private final static double MAX_REPO_SIZE = MAX_SIZE_FOR_PROJECT_WITH_COMMITS * DefaultProperties.MAX_SIZE_FACTOR;
//	private final static boolean STORE_COMMITS = DefaultProperties.STORE_COMMITS;
//	private final static boolean STORE_ASTS = DefaultProperties.STORE_ASTS;

	public static void main(String[] args) throws IOException, InterruptedException {

		conf = new Configuration();
		fileSystem = FileSystem.get(conf);
//		base = Properties.getProperty("output.path", DefaultProperties.OUTPUT);
		
		System.out.println(jsonPath);
		System.out.println(base);
		System.out.println(poolSize);
		System.out.println(debug);
		

//		getProcessedProjects();

		// assign each thread with a worker
//		ImportTask[] workers = new ImportTask[poolSize];
//		Thread[] threads = new Thread[poolSize];
//		for (int i = 0; i < poolSize; i++) {
//			workers[i] = new ImportTask(i);
//			threads[i] = new Thread(workers[i]);
//			threads[i].start();
//			Thread.sleep(10);
//		}


		int counter = 0;
		long repoKey = 1;
		File dir = new File(jsonPath);
		Queue<File> queue = new LinkedList<File>();
		queue.offer(dir);
		while (!queue.isEmpty()) {
			File cur = queue.poll();
			for (File file : cur.listFiles()) {
				if (file.isDirectory())
					queue.offer(file);
				if (file.isFile() && file.getName().endsWith(".json")) {
					System.out.println(file.getName());
					Gson parser = new Gson();
					String content = FileIO.readFileContents(file);
					break;
				}
			}
			break;
		}
		
	
		
//				String content = FileIO.readFileContents(file);
//				Gson parser = new Gson();
//
//				JsonArray repoArray = null;
//				try {
//					repoArray = parser.fromJson(content, JsonElement.class).getAsJsonArray();
//				} catch (Exception e) {
//					System.err.println("Error proccessing page: " + file.getPath());
//					e.printStackTrace();
//					continue;
//				}
//				for (int i = 0; i < repoArray.size(); i++) {
//					try {
//						JsonObject rp = repoArray.get(i).getAsJsonObject();
//						RepoMetadata repo = new RepoMetadata(rp);
//
//						if (!DefaultProperties.projects.contains(repo.name))
//							continue;
//
//						if (repo.id != null && repo.name != null && !processedProjectIds.contains(repo.id)) {
//							Project protobufRepo = repo.toBoaMetaDataProtobuf();
//
//							boolean assigned = false;
//							while (!assigned) {
//								for (int j = 0; j < poolSize; j++) {
//									if (workers[j].isReady()) {
//										workers[j].assignTask(protobufRepo, repoKey++);
//										workers[j].setReady(false);
//										assigned = true;
//										break;
//									}
//								}
////								Thread.sleep(100);
//							}
//							
//							System.err.println("Assigned the " + (++counter) + "th project: " + repo.name 
//									+ " with id: " + repo.id  
//									+ " from the " + i + "th object of the json file: " + file.getPath());
//						}
//					} catch (Exception e) {
//						System.err.println("Error proccessing item " + i + " of page " + file.getPath());
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		for (int j = 0; j < poolSize; j++) {
//			while (!workers[j].isReady())
//				Thread.sleep(100);
//		}
//		setDone(true);
//
//		// wait for workers to close writers and finish
//		for (Thread thread : threads)
//			while (thread.isAlive())
//				Thread.sleep(1000);
	}
	
	synchronized static boolean getDone() {
		return SeqRepoImporter.done;
	}
	
	synchronized static void setDone(boolean done) {
		SeqRepoImporter.done = done;
	}
	
	synchronized static void updateExceptions(String key, String value) {
		DefaultProperties.exceptions.put(key, value);
	}
	
//	synchronized static void updateProcessedProjects(String projectName) {
//		DefaultProperties.processedProjects.add(projectName);
//	}

//	private static void getProcessedProjects() throws IOException {
//		FileStatus[] files = fileSystem.listStatus(new Path(base + "/project"));
//		for (int i = 0; i < files.length; i++) {
//			FileStatus file = files[i];
//			String name = file.getPath().getName();
//			if (name.endsWith(".seq")) {
//				SequenceFile.Reader r = null;
//				try {
//					r = new SequenceFile.Reader(fileSystem, file.getPath(), conf);
//					final Text key = new Text();
//					while (r.next(key)) {
//						processedProjectIds.add(key.toString());
//					}
//					r.close();
//				} catch (Exception e) {
//					if (r != null)
//						r.close();
//					for (String dir : new String[] { "project", "ast", "commit", "source", "repo" }) {
//						fileSystem.delete(new Path(base + "/" + dir + "/" + name), false);
//						System.out.println("remove " + base + "/" + dir + "/" + name);
//					}
//				}
//			}
//		}
//		// processedProjects = processedProjectIds.size();
//		System.err.println("Got processed projects: " + processedProjectIds.size());
//	}

	public static class ImportTask implements Runnable {
		private int id;
		volatile private long repoKey;
		private int counter = 0, allCounter = 0;
		private String suffix;
		private SequenceFile.Writer projectWriter, astWriter, commitWriter, contentWriter, repoWriter;
		private long astWriterLen, commitWriterLen, contentWriterLen;
		private volatile boolean ready = true;
		volatile Project project;

		public ImportTask(int id) {
			setId(id);
		}

		public synchronized void assignTask(Project protobufRepo, long repoKey) {
			setProject(protobufRepo);
			setRepoKey(repoKey);
		};

		synchronized void openWriters() {
			long time = System.currentTimeMillis();
			suffix = getId() + "-" + time + ".seq";
			while (true) {
				try {
					System.out.println(Thread.currentThread().getName() + " " + getId() + " " + suffix + " starts!");
					projectWriter = SequenceFile.createWriter(fileSystem, conf, new Path(base + "/project/" + suffix),
							Text.class, BytesWritable.class, CompressionType.BLOCK);
					astWriter = SequenceFile.createWriter(fileSystem, conf, new Path(base + "/ast/" + suffix),
							LongWritable.class, BytesWritable.class, CompressionType.BLOCK);
					commitWriter = SequenceFile.createWriter(fileSystem, conf, new Path(base + "/commit/" + suffix),
							LongWritable.class, BytesWritable.class, CompressionType.BLOCK);
					contentWriter = SequenceFile.createWriter(fileSystem, conf, new Path(base + "/source/" + suffix),
							LongWritable.class, BytesWritable.class, CompressionType.BLOCK);
					repoWriter = SequenceFile.createWriter(fileSystem, conf, new Path(base + "/repo/" + suffix),
							LongWritable.class, BytesWritable.class, CompressionType.BLOCK);
					astWriterLen = 1;
					commitWriterLen = 1;
					contentWriterLen = 1;
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
					projectWriter.close();
					astWriter.close();
					commitWriter.close();
					contentWriter.close();
					repoWriter.close();
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
					final String name = project.getName();

					if (debug)
						System.err.println(Thread.currentThread().getName() + " id: " + Thread.currentThread().getId()
								+ " is processing the " + (allCounter + 1)
								+ "th project: " + name + " with id: "+ project.getId());
					
					project = storeRepository(project, 0);
					
					if (project != null) {
						
						if (debug)
							System.err.println(Thread.currentThread().getName() + " id: " + Thread.currentThread().getId() 
									+ " is putting project " + project.getName() 
									+ " in sequence file");
	
						BytesWritable bw = new BytesWritable(project.toByteArray());
//						if (bw.getLength() <= MAX_SIZE_FOR_PROJECT_WITH_COMMITS || (project.getCodeRepositoriesCount() > 0
//								&& project.getCodeRepositories(0).getRevisionKeysCount() > 0)) {
//							try {
//								projectWriter.append(new Text(project.getId()), bw);
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						} else {
//							Project.Builder pb = Project.newBuilder(project);
//							for (CodeRepository.Builder cb : pb.getCodeRepositoriesBuilderList()) {
//								for (Revision.Builder rb : cb.getRevisionsBuilderList()) {
//									cb.addRevisionKeys(commitWriterLen);
//									bw = new BytesWritable(rb.build().toByteArray());
//									commitWriter.append(new LongWritable(commitWriterLen), bw);
//									commitWriterLen += bw.getLength();
//								}
//								cb.clearRevisions();
//							}
//							try {
//								projectWriter.append(new Text(pb.getId()), new BytesWritable(pb.build().toByteArray()));
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
						counter++;
						allCounter++;
						if (counter >= Integer.parseInt(DefaultProperties.MAX_PROJECTS)) {
							closeWriters();
							openWriters();
							counter = 0;
						}
						
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
				setReady(true);
			}
			closeWriters();
		}
		

		private synchronized Project storeRepository(Project project, final int i)
				throws LargeObjectException, MissingObjectException, IncorrectObjectTypeException {
			final CodeRepository repo = project.getCodeRepositories(i);
			final Project.Builder projBuilder = Project.newBuilder(project);

			final String name = project.getName();
			File gitDir = new File("/" + name + "/.git");

			if (isFiltered(project))
				return null;

			// If repository is already cloned delete then re-clone, this should only happen during recover
//			FileIO.DirectoryRemover filecheck = new FileIO.DirectoryRemover(gitRootPath + "/" + project.getName());
//			filecheck.run();
			
			// clone repository
			if (!gitDir.exists()) {
				Git result = null;
				try {
					String url = repo.getUrl();
					File localGitDir = new File(gitDir.getAbsolutePath());
					result = Git.cloneRepository().setURI(url).setBare(true).setDirectory(localGitDir).call();
				} catch (Throwable t) {
					System.err.println("Error cloning " + repo.getUrl());
					updateExceptions(name, "err cloning");
					return null;
				} finally {
					if (result != null && result.getRepository() != null) {
						result.getRepository().close();
					}
				}
				System.err.println(gitDir.getName() + " not exist");
//				return null;
			} else {
//				updateProcessedProjects(name);
				System.err.println(gitDir.getName() + " already exists");
			}
			
			if (debug)
				System.err.println(Thread.currentThread().getName() + " id: " + Thread.currentThread().getId() + " cloned repository: " + name);
			
			AbstractConnector conn = null;
			int packSize = 0;
			
//			if (!STORE_ASTS) {
//				ByteArrayFile f = new ByteArrayFile(gitDir.getAbsolutePath());
//				BytesWritable bw = new BytesWritable(SerializationUtils.serialize(f));
//				if (!f.isBuilt() || bw.getLength() > MAX_REPO_SIZE) {
//					updateExceptions(name, "pack file size: " + bw.getLength() + " exceeding the max size: " + MAX_REPO_SIZE);
//					if (debug)
//						System.err.println(Thread.currentThread().getName() + " id: " + Thread.currentThread().getId() + " the pack file size of project: " + name + " exceeds the " + MAX_REPO_SIZE + " byte");
//					return null;
//				} else {
//					try {
//						repoWriter.append(new LongWritable(getRepoKey()), bw);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					if (debug)
//						System.err.println(Thread.currentThread().getName() + " id: " + Thread.currentThread().getId() + " the pack file size of project " + name + " is " + bw.getLength() + " byte");
//				}
//				packSize = bw.getLength();
//			}

			try {
				conn = new GitConnector(gitDir.getAbsolutePath(), project.getName(), astWriter, astWriterLen,
						commitWriter, commitWriterLen, contentWriter, contentWriterLen, getRepoKey());
				final CodeRepository.Builder repoBuilder = CodeRepository.newBuilder(repo);
//				if (STORE_COMMITS) {
//					List<Object> revisions = conn.getRevisions(project.getName());
//					if (!revisions.isEmpty()) {
//						if (revisions.get(0) instanceof Revision) {
//							for (final Object rev : revisions) {
//								final Revision.Builder revBuilder = Revision.newBuilder((Revision) rev);
//								repoBuilder.addRevisions(revBuilder);
//							}
//						} else {
//							for (final Object rev : revisions)
//								repoBuilder.addRevisionKeys((Long) rev);
//						}
//					}
//				}
//
//				if (debug)
//					System.err.println(Thread.currentThread().getName() + " id: " + Thread.currentThread().getId() + " is building head snapshot for " + name);
//
//				repoBuilder.setHead(conn.getHeadCommitOffset());
//				repoBuilder.addAllHeadSnapshot(conn.buildHeadSnapshot());
//
//				repoBuilder.addAllBranches(conn.getBranchIndices());
//				repoBuilder.addAllBranchNames(conn.getBranchNames());
//				repoBuilder.addAllTags(conn.getTagIndices());
//				repoBuilder.addAllTagNames(conn.getTagNames());
//
//				projBuilder.setCodeRepositories(i, repoBuilder);
//
//				if (!STORE_ASTS)
//					projBuilder.setSize(packSize);

				return projBuilder.build();
			} catch (final Throwable e) {
				printError(e, "unknown error", project.getName());
			} finally {
				if (conn != null) {
					this.astWriterLen = conn.getAstWriterLen();
					this.commitWriterLen = conn.getCommitWriterLen();
					this.contentWriterLen = conn.getContentWriterLen();
					try {
						conn.close();
					} catch (Exception e) {
						printError(e, "Cannot close Git connector to " + gitDir.getAbsolutePath(), project.getName());
					}
				}
//				if (!cache) {
//					new Thread(new FileIO.DirectoryRemover(gitRootPath + "/" + name)).start();
//				}
			}
			
			updateExceptions(name, "unknow");
			return null;
		}

		private synchronized boolean isFiltered(Project project) {
			if (project.getForked())
				return true;
//			if (project.getStars() < 2 && project.getSize() < 100)
//				return true;
			
			// exceptions
			if (DefaultProperties.exceptions.containsKey(project.getName()))
				return true;
			// new model
//			if (!STORE_ASTS) {
//				long curSize = project.getSize() * 1000L;
//				if (curSize > MAX_REPO_SIZE) {
//					updateExceptions(project.getName(), "repo size: " + curSize + " exceeding the max size: " + MAX_REPO_SIZE);
//					return true;
//				}
//			}
			if (project.getProgrammingLanguagesList().contains("Java")
					|| project.getProgrammingLanguagesList().contains("JavaScript")
					|| project.getProgrammingLanguagesList().contains("PHP"))
				return false;
			String lang = project.getMainLanguage();
			if (lang != null && (lang.equals("Java") || lang.equals("JavaScript") || lang.equals("PHP")))
				return false;
			return true;
		}

		synchronized long getRepoKey() {
			return this.repoKey;
		}

		synchronized void setRepoKey(long repoKey) {
			this.repoKey = repoKey;
		}

		synchronized Project getProject() {
			return this.project;
		}

		synchronized void setProject(Project project) {
			this.project = project;
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
