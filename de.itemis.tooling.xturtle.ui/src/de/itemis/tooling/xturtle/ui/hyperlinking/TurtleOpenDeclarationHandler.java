package de.itemis.tooling.xturtle.ui.hyperlinking;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.URLHyperlink;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.hyperlinking.OpenDeclarationHandler;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;

public class TurtleOpenDeclarationHandler extends OpenDeclarationHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		XtextEditor xtextEditor = EditorUtils.getActiveXtextEditor(event);
		if (xtextEditor != null) {
			ITextSelection selection = (ITextSelection) xtextEditor.getSelectionProvider().getSelection();
	
			IRegion region = new Region(selection.getOffset(), selection.getLength());
	
			ISourceViewer internalSourceViewer = xtextEditor.getInternalSourceViewer();
	
			IHyperlink[] hyperlinks = getDetector().detectHyperlinks(internalSourceViewer, region, false);
			if (hyperlinks != null && hyperlinks.length > 0) {
				for (IHyperlink hyperlink : hyperlinks) {
					//Open declaration should not open a browser
					//this can by done using ctrl-click
					//of course this behaviour could be made configurable
					if(!(hyperlink instanceof URLHyperlink)){
						hyperlink.open();
						break;
					}
				}
			}
		}
		return null;
	}
}
