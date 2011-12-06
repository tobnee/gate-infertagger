package gate.creole.semtagger.rulemodel;

public class Token {
	public final String string, root, category;
	
	public Token(String string, String root, String category) {
		this.string = string;
		this.root = root;
		this.category = category;
	}

}
