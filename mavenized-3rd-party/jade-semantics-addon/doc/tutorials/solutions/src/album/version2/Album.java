package album.version2;

import jade.core.behaviours.TickerBehaviour;
import jade.semantics.interpreter.SemanticAgentBase;
import jade.semantics.lang.sl.grammar.ByteConstantNode;
import jade.semantics.lang.sl.grammar.Formula;
import jade.semantics.lang.sl.tools.SL;
import album.tools.JPEGUtilities;

public class Album extends SemanticAgentBase {
    Formula IMAGE_CONTENT_PATTERN =
    	SL.fromFormula("(image-content img ??content)");

    // To avoid the retract operation (see below), one can use an equals formula between an iota IRE expression and a value to instantiate
    // Formula IMAGE_CONTENT_PATTERN =
    //	SL.fromFormula("(= (iota ?value (image-content img ?value)) ??content)");

    public void setup() {
	super.setup();

	// Add a TickerBehaviour, which periodically updates the picture in the belief base
	addBehaviour(new TickerBehaviour(this, 500) {
		int index = 0;
		protected void onTick() {
		    String imageId = (String)getArguments()[index];
		    byte[] imageCt = JPEGUtilities.toBytesArray(imageId);

		    Formula imageFormula = IMAGE_CONTENT_PATTERN.instantiate("content", new ByteConstantNode(imageCt));
		    // Retract the current picture from the belief base
		    getSemanticCapabilities().getMyKBase().retractFormula(IMAGE_CONTENT_PATTERN);
		    // If one uses the equals formula, the retract operation is useless,
		    // because the semantics of the iota part of the formula entails this retraction

		    // Assert the new picture (as in the previous exercise)
		    getSemanticCapabilities().interpret(imageFormula);

		    index = (index + 1) % getArguments().length;
		}
	});
    }
}
