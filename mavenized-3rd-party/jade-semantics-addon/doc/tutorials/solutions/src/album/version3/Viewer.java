package album.version3;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.semantics.interpreter.SemanticAgent;
import jade.semantics.interpreter.SemanticCapabilities;
import jade.semantics.interpreter.SemanticInterpretationPrincipleTable;
import jade.semantics.interpreter.SemanticRepresentation;
import jade.semantics.interpreter.Tools;
import jade.semantics.interpreter.SemanticInterpretationPrinciple;
import jade.semantics.interpreter.sips.adapters.NotificationSIPAdapter;
import jade.semantics.kbase.FilterKBase;
import jade.semantics.kbase.KBase;
import jade.semantics.kbase.filter.KBAssertFilter;
import jade.semantics.kbase.filter.KBAssertFilterAdapter;
import jade.semantics.lang.sl.grammar.ByteConstantNode;
import jade.semantics.lang.sl.grammar.Formula;
import jade.semantics.lang.sl.grammar.IdentifyingExpression;
import jade.semantics.lang.sl.grammar.Term;
import jade.semantics.lang.sl.grammar.TrueNode;
import jade.semantics.lang.sl.grammar.WordConstantNode;
import jade.semantics.lang.sl.grammar.Constant;
import jade.semantics.lang.sl.tools.MatchResult;
import jade.semantics.lang.sl.tools.SL;
import jade.semantics.lang.sl.tools.SL.WrongTypeException;
import jade.util.leap.ArrayList;
import album.tools.DefaultViewerGUI;
import album.tools.ViewerGUI;

public class Viewer extends SemanticAgent {

    ViewerGUI myGUI = new DefaultViewerGUI("Photo Viewer");

    IdentifyingExpression ANY_IMAGE_CONTENT_IRE_PATTERN =
	(IdentifyingExpression)SL.fromTerm("(any ?a (image-content ??id ?a))");

    Formula B_IMAGE_CONTENT_PATTERN =
	SL.fromFormula("(B ??myself (image-content ??id ??content))");

    public class ViewerSemanticCapabilities extends SemanticCapabilities {

		protected SemanticInterpretationPrincipleTable setupSemanticInterpretationPrinciples() {
			SemanticInterpretationPrincipleTable table = super.setupSemanticInterpretationPrinciples();
			SemanticInterpretationPrinciple sip =
			new NotificationSIPAdapter(this, B_IMAGE_CONTENT_PATTERN) {
			    protected void notify(final MatchResult applyResult, SemanticRepresentation sr) {
				potentiallyAddBehaviour(new OneShotBehaviour() {
					public void action() {
					    myGUI.displayPhoto(((Constant)applyResult.term("content")).byteValue());
					}
				    });
			    }
			};
		    table.addSemanticInterpretationPrinciple(sip);
		    return table;
		}

		// Override the setupKbase method
		protected KBase setupKbase() {
			// Create a default KBase
			FilterKBase res_kbase = (FilterKBase)super.setupKbase();
	        // Create an assertion filter using the proper adapter
		    KBAssertFilter filter = new KBAssertFilterAdapter(B_IMAGE_CONTENT_PATTERN) {
	   		    // The doApply method consists in absorbing the asserted formula
			    public Formula doApply(Formula formula) {
			    	return new TrueNode();
			    }
			};
			// Add the created filter to the KBase
			res_kbase.addKBAssertFilter(filter);
			return res_kbase;
		}
    }

    public Viewer() {
    	setSemanticCapabilities(new ViewerSemanticCapabilities());
    }

    public void setup() {
		super.setup();
		Term album = Tools.AID2Term(new AID((String)getArguments()[0], AID.ISLOCALNAME));
		String imageId = (String)getArguments()[1];

		getSemanticCapabilities().queryRef((IdentifyingExpression)ANY_IMAGE_CONTENT_IRE_PATTERN
						   .instantiate("id", new WordConstantNode(imageId)),
						   album);
    }
}
