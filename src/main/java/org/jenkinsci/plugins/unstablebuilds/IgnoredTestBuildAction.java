package org.jenkinsci.plugins.unstablebuilds;

import hudson.model.BuildBadgeAction;

public class IgnoredTestBuildAction implements BuildBadgeAction {

	private int count;

	public IgnoredTestBuildAction(int count) {
		this.count = count;
	}

	public String getDisplayName() {
		return Messages.IgnoredTestBuildAction_Ignored();
	}

	public String getIconFileName() {
		return "/plugin/unstable-builds/images/ignored.png";
	}

	public String getUrlName() {
		return null;
	}
	
	public int getCount() {
		return count;
	}

}
