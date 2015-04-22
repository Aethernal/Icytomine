package plugins.faubin.cytomine.utils.cytomine;

public class AnnotationTerm {
	private String name;
	private long id;

	public AnnotationTerm(String name, long id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}

	public String toString() {
		return name + " - " + id;
	}

}
