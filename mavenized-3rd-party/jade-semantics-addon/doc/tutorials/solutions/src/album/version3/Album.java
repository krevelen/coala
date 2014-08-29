package album.version3;

import jade.semantics.interpreter.SemanticAgent;
import jade.semantics.interpreter.SemanticCapabilities;
import jade.semantics.kbase.FilterKBase;
import jade.semantics.kbase.KBase;
import jade.semantics.kbase.filter.KBQueryFilter;
import jade.semantics.kbase.filter.KBQueryFilterAdapter;
import jade.semantics.lang.sl.grammar.ByteConstantNode;
import jade.semantics.lang.sl.grammar.Constant;
import jade.semantics.lang.sl.grammar.Formula;
import jade.semantics.lang.sl.tools.MatchResult;
import jade.semantics.lang.sl.tools.SL;
import album.tools.JPEGUtilities;

public class Album extends SemanticAgent {
    Formula B_IMAGE_CONTENT_PATTERN =
	SL.fromFormula("(B ??myself (image-content ??id ??content))");

    // Initialize the agent's capabilities with the AlbumSemanticCapabilities class
    public Album() {
	setSemanticCapabilities(new AlbumSemanticCapabilities());
    }

    // Define the AlbumSemanticCapabilities class
    public class AlbumSemanticCapabilities extends SemanticCapabilities {

	// Override the setupKbase method
	protected KBase setupKbase() {
		// Create a default KBase
		FilterKBase res_kbase = (FilterKBase)super.setupKbase();

	    // Create a KBQueryFilter filter using the proper adapter
	    KBQueryFilter filter = new KBQueryFilterAdapter(B_IMAGE_CONTENT_PATTERN){
			// The doApply method retrieves the binary content of a picture (2nd parameter of the image-content predicate)
			// from its given URI (1st parameter of the predicate)
	    	public MatchResult doApply(Formula formula, MatchResult matchResult) {
			    String imageId = ((Constant)matchResult.term("id")).stringValue();
			    byte[] imageCt = JPEGUtilities.toBytesArray(imageId);
			    if (matchResult.set("content", new ByteConstantNode(imageCt))) {
			    	return matchResult;
			    }
			    else {
			    	return null;
			    }
			}
	    };
	    // Add the created filter to the KBase
	    res_kbase.addKBQueryFilter(filter);
	    return res_kbase;
	}
    }
}
