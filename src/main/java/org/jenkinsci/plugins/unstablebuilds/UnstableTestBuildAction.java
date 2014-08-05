package org.jenkinsci.plugins.unstablebuilds;

import hudson.model.BuildBadgeAction;

public class UnstableTestBuildAction implements BuildBadgeAction {

	private int count;

	public UnstableTestBuildAction(int count) {
		this.count = count;
	}

	public String getDisplayName() {
		return Messages.UnstableTestBuildAction_Unstable();
	}

	public String getIconFileName() {
		return "/plugin/unstable-builds/images/unstable.png";
	}

	public String getUrlName() {
		return null;
	}
	
	public int getCount() {
		return count;
	}

}
