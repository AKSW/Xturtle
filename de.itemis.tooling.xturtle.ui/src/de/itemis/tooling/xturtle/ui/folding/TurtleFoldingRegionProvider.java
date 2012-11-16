package de.itemis.tooling.xturtle.ui.folding;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.IRegion;
import org.eclipse.xtext.ui.editor.folding.DefaultFoldedPosition;
import org.eclipse.xtext.ui.editor.folding.DefaultFoldingRegionAcceptor;
import org.eclipse.xtext.ui.editor.folding.DefaultFoldingRegionProvider;
import org.eclipse.xtext.ui.editor.folding.FoldedPosition;
import org.eclipse.xtext.ui.editor.folding.IFoldingRegionAcceptor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.ITextRegion;

import de.itemis.tooling.xturtle.xturtle.StringLiteral;
import de.itemis.tooling.xturtle.xturtle.Triples;

public class TurtleFoldingRegionProvider extends DefaultFoldingRegionProvider {

	//marker class indicating that the region corresponds to a multi line string
	public class TextFoldedRegion extends DefaultFoldedPosition{
		public TextFoldedRegion(int offset, int length, int contentStart,
				int contentLength) {
			super(offset, length, contentStart, contentLength);
		}
	}

	//hack for marking text regions, the acceptor is informed beforehand whether it would
	//be a string region the corresponding marker region is created in case the
	//offset (start of line) does not correspond with that of the previous region
	//this could happen e.g. for a triple (string in first line of the triple)
	//in which case there would be two foldable regions starting on the same line
	//this leads to unexpected behaviour as you cannot choose which region to expand/collapse
	private class TurtleFoldingRegionAcceptor extends DefaultFoldingRegionAcceptor{
		private boolean isStringRegion;
		private int lastOffset=-1;
		public TurtleFoldingRegionAcceptor(IXtextDocument document,
				Collection<FoldedPosition> result) {
			super(document, result);
		}

		@Override
		protected FoldedPosition newFoldedPosition(IRegion region,
				ITextRegion significantRegion) {
			FoldedPosition result;
			if(isStringRegion && significantRegion!=null &&region!=null && lastOffset!=region.getOffset()){
				result= new TextFoldedRegion(region.getOffset(), region.getLength(), significantRegion.getOffset() - region.getOffset(), significantRegion.getLength());
			}else{
				result= super.newFoldedPosition(region, significantRegion);
			}
			if(result!=null){
				lastOffset=region.getOffset();
			}
			return result;
		}
		
		public void setStringRegion(boolean isStringRegion) {
			this.isStringRegion = isStringRegion;
		}
	}

	@Override
	protected IFoldingRegionAcceptor<ITextRegion> createAcceptor(
			IXtextDocument xtextDocument,
			Collection<FoldedPosition> foldedPositions) {
		return new TurtleFoldingRegionAcceptor(xtextDocument, foldedPositions);
	}

	@Override
	protected void computeObjectFolding(EObject eObject,
			IFoldingRegionAcceptor<ITextRegion> foldingRegionAcceptor) {
		((TurtleFoldingRegionAcceptor)foldingRegionAcceptor).setStringRegion(eObject instanceof StringLiteral);
		super.computeObjectFolding(eObject, foldingRegionAcceptor);
	}
	
	//for now we allow folding only for triples and String literals
	//TODO check whether to extend that to BlankObjects/BlankCollection
	@Override
	protected boolean isHandled(EObject eObject) {
		return (eObject instanceof Triples) || (eObject instanceof StringLiteral); 
	}
}
