package plugins.faubin.cytomine.oldgui.mvc.template;

import be.cytomine.client.Cytomine;

public abstract class Model {

	protected Cytomine cytomine;

	public Model(Cytomine cytomine) {

		this.cytomine = cytomine;

	}

	public Cytomine getCytomine() {
		return cytomine;
	}

}
