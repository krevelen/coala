package album.version1;

import jade.core.AID;
import jade.semantics.interpreter.SemanticAgent;
import jade.semantics.interpreter.Tools;
import jade.semantics.lang.sl.grammar.IdentifyingExpression;
import jade.semantics.lang.sl.grammar.Term;
import jade.semantics.lang.sl.tools.SL;

public class Viewer extends SemanticAgent {

	static final long serialVersionUID = 3L;

    // The following IRE represents a sequence of parameters satisfying the image-content predicate
    IdentifyingExpression ANY_IMAGE_CONTENT_IRE =
	(IdentifyingExpression)SL.fromTerm("(any (sequence ?x ?y) (image-content ?x ?y))");

    public void setup() {
	super.setup();

	// Read the name of the album agent from the agent's parameters
	Term album = Tools.AID2Term(new AID((String)getArguments()[0], AID.ISLOCALNAME));

	// Use the queryRef method to query a picture from the album agent
	getSemanticCapabilities().queryRef(ANY_IMAGE_CONTENT_IRE, album);
    }
}
