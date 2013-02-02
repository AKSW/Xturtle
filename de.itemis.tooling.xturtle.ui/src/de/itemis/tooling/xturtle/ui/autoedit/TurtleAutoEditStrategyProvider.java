package de.itemis.tooling.xturtle.ui.autoedit;

import org.eclipse.jface.text.IDocument;
import org.eclipse.xtext.ui.editor.autoedit.DefaultAutoEditStrategyProvider;
import org.eclipse.xtext.ui.editor.model.TerminalsTokenTypeToPartitionMapper;

public class TurtleAutoEditStrategyProvider extends
		DefaultAutoEditStrategyProvider {

	//removed single quote string literal, multi line comments and all kinds of parentheses
	@Override
	protected void configure(IEditStrategyAcceptor acceptor) {
		configureIndentationEditStrategy(acceptor);
		configureStringLiteral(acceptor);
		acceptor.accept(partitionInsert.newInstance("<", ">"),IDocument.DEFAULT_CONTENT_TYPE);
		acceptor.accept(partitionDeletion.newInstance("<",">"),IDocument.DEFAULT_CONTENT_TYPE);
		
		configureParenthesis(acceptor);
		configureSquareBrackets(acceptor);
		acceptor.accept(compoundMultiLineTerminals.newInstanceFor("[", "]"), IDocument.DEFAULT_CONTENT_TYPE);

		//		super.configure(acceptor);
	}
	
	protected void configureStringLiteral(IEditStrategyAcceptor acceptor) {
		acceptor.accept(partitionInsert.newInstance("\"","\""),IDocument.DEFAULT_CONTENT_TYPE);
		// The following two are registered for the default content type, because on deletion 
		// the command.offset is cursor-1, which is outside the partition of terminals.length = 1.
		// How crude is that?
		// Note that in case you have two string literals following each other directly, the deletion strategy wouldn't apply.
		// One could add the same strategy for the STRING partition in addition to solve this
		acceptor.accept(partitionDeletion.newInstance("\"","\""),IDocument.DEFAULT_CONTENT_TYPE);
		acceptor.accept(partitionEndSkippingEditStrategy.get(),TerminalsTokenTypeToPartitionMapper.STRING_LITERAL_PARTITION);
	}

}
