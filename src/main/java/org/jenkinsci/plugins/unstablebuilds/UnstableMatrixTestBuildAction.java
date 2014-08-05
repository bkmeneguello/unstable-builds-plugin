package org.jenkinsci.plugins.unstablebuilds;

import hudson.matrix.MatrixRun;
import hudson.matrix.MatrixBuild;
import hudson.model.BuildBadgeAction;

import java.util.LinkedList;
import java.util.List;

public class UnstableMatrixTestBuildAction implements BuildBadgeAction {

	private MatrixBuild build;

	public UnstableMatrixTestBuildAction(MatrixBuild build) {
		this.build = build;
	}

	public String getDisplayName() {
		return Messages.UnstableMatrixTestBuildAction_Unstable();
	}

	public String getIconFileName() {
		return "/plugin/unstable-builds/images/unstable.png";
	}

	public String getUrlName() {
		return null;
	}
	
	public int getCount() {
		return getRuns().size();
	}
	
	public List<MatrixRun> getRuns() {
		return getRuns(build);
	}
	
	public String worstIcon(MatrixRun run) {
		if (!run.getActions(IgnoredTestBuildAction.class).isEmpty()) {
			return "/plugin/unstable-builds/images/ignored.png";
		} else if (!run.getActions(UnstableTestBuildAction.class).isEmpty()) {
			return "/plugin/unstable-builds/images/unstable.png";
		}
		return null;
	}

	private List<MatrixRun> getRuns(MatrixBuild matrixBuild) {
		final List<MatrixRun> runs = matrixBuild.getRuns();
		final List<MatrixRun> runsWithWarnings = new LinkedList<MatrixRun>();
		for (MatrixRun run : runs) {
			if (!run.getActions(IgnoredTestBuildAction.class).isEmpty() ||
					!run.getActions(UnstableTestBuildAction.class).isEmpty()) {
				runsWithWarnings.add(run);
			}
		}
		return runsWithWarnings;
	}

}
