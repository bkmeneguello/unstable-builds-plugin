package org.jenkinsci.plugins.unstablebuilds;

import hudson.Extension;
import hudson.console.ConsoleNote;
import hudson.matrix.MatrixRun;
import hudson.matrix.MatrixBuild;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;
import hudson.model.listeners.RunListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Extension
public class UnstableBuildScanner extends RunListener<AbstractBuild<?, ?>> {

	private static Pattern ignoredTestPattern = Pattern.compile(".*Ignoring intermittent test.*");
	
	private static Pattern intermitentTestPattern = Pattern.compile(".*Found a new intermittent test.*");

	@Override
	public void onCompleted(AbstractBuild<?, ?> build, TaskListener listener) {
		UnstableBuildScanner.scan(build);
	}

	public static void scan(AbstractBuild<?, ?> build) {
		if (build instanceof MatrixBuild) {
			try {
				final List<MatrixRun> runs = new ArrayList<MatrixRun>();
				for (MatrixRun run : ((MatrixBuild)build).getRuns()) {
					try {
						int ignored = 0;
						int unstable = 0;
						final BufferedReader reader = new BufferedReader(run.getLogReader());
						String line;
						while ((line = reader.readLine()) != null) {
							line = ConsoleNote.removeNotes(line);
							if (ignoredTestPattern.matcher(line).matches()) {
								ignored++;
							}
							if (intermitentTestPattern.matcher(line).matches()) {
								unstable++;
							}
						}
						if (ignored > 0) {
							run.getActions().removeAll(run.getActions(IgnoredTestBuildAction.class));
							run.addAction(new IgnoredTestBuildAction(ignored));
						}
						if (unstable > 0) {
							run.getActions().removeAll(run.getActions(UnstableTestBuildAction.class));
							run.addAction(new UnstableTestBuildAction(unstable));
						}
						run.save();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (!run.getActions(IgnoredTestBuildAction.class).isEmpty() ||
							!run.getActions(UnstableTestBuildAction.class).isEmpty()) {
						runs.add(run);
					}
				}
				if (!runs.isEmpty()) {
					build.getActions().removeAll(build.getActions(UnstableMatrixTestBuildAction.class));
					build.addAction(new UnstableMatrixTestBuildAction((MatrixBuild)build));
				}
				build.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
