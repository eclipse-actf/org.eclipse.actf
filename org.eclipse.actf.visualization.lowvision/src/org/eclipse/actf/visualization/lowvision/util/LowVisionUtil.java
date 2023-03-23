/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.lowvision.util;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.actf.model.dom.html.HTMLParserFactory;
import org.eclipse.actf.model.dom.html.IHTMLParser;
import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.visualization.IVisualizationConst;
import org.eclipse.actf.visualization.lowvision.LowVisionVizPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;




public class LowVisionUtil implements IVisualizationConst{
    
    static File runtimeHtmlFile = null;
        
    public static String[] frameAnalyze(IWebBrowserACTF webBrowser) {
        try {
            if(runtimeHtmlFile==null){
                runtimeHtmlFile = LowVisionVizPlugin.createTempFile("runtime", SUFFIX_HTML); //$NON-NLS-1$
            }
            URL urlBase = new URL(webBrowser.getURL());

            webBrowser.saveDocumentAsHTMLFile(runtimeHtmlFile.getAbsolutePath());

            IHTMLParser htmlParser = HTMLParserFactory.createHTMLParser();
            htmlParser.parse(new FileInputStream(runtimeHtmlFile));

            //TODO from Mediator
            Document document = htmlParser.getDocument();
            Element documentElement = document.getDocumentElement();

            NodeList frameList = documentElement.getElementsByTagName("frame"); //$NON-NLS-1$
            try {
                NodeList baseNL = documentElement.getElementsByTagName("base"); //$NON-NLS-1$
                if (baseNL.getLength() > 0) {
                    Element baseE = (Element) baseNL.item(baseNL.getLength() - 1);
                    String baseUrlS = baseE.getAttribute("href"); //$NON-NLS-1$
                    if (baseUrlS.length() > 0) {
                        URL tmpUrl = new URL(urlBase, baseUrlS);
                        urlBase = tmpUrl;
                    }
                }
            } catch (Exception e) {
            }

            int frameNum = frameList.getLength();
            String[] result = new String[frameNum];

            for (int i = 0; i < frameNum; i++) {
                Element frameEl = (Element) frameList.item(i);
                String srcUrl = frameEl.getAttribute("src"); //$NON-NLS-1$
                try {
                    URL url = new URL(urlBase, srcUrl);
                    result[i] = url.toString();
                } catch (MalformedURLException e2) {
                    result[i] = ""; //$NON-NLS-1$
                    e2.printStackTrace();
                }
            }
            htmlParser = null;
            return (result);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return (new String[0]);
    }

    public static ImagePositionInfo[] trimInfoImageInHtml(ImagePositionInfo[] target, int height) {
        Vector<ImagePositionInfo> tmpV = new Vector<ImagePositionInfo>();
        for (int i = 0; i < target.length; i++) {
            if (target[i].getY() <= height) {
                tmpV.add(target[i]);
            }
        }
        ImagePositionInfo[] result = new ImagePositionInfo[tmpV.size()];
        tmpV.toArray(result);
        return (result);
    }

    public static Map<String, ICurrentStyles> trimStyleInfoArray(Map<String, ICurrentStyles> target, int height) {
        Map<String, ICurrentStyles> result = new HashMap<String, ICurrentStyles>();
        for (String tmpKey :  target.keySet()) {
            try {
                ICurrentStyles tmpS = target.get(tmpKey);
                if (tmpS.getRectangle().y <= height) {
                    result.put(tmpKey, tmpS);
                }
            } catch (Exception e) {
            }
        }
        // System.out.println("size: "+target.size()+" : "+result.size());
        return (result);
    }

}
