package ndm.krvidict;

public class Word {

	public String objectName;

	public int id;

	public String mean;

	public String pron;

	public int type;

	// constructor for adding sample data
	public Word(String objectName, int id, String mean, int type) {

		this.objectName = objectName;
		this.id = id;
		this.mean = mean;
		this.type = type;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.objectName;
	}
}