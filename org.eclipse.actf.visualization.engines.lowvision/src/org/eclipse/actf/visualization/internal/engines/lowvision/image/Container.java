/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.lowvision.image;

import java.util.Vector;

import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CandidateCharacter;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CandidateUnderlinedCharacter;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CharacterMS;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CharacterSM;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CharacterSS;

public class Container extends PageComponent {
	private int color;

	int numSSCharacters = 0; // ssCharacters.length

	CharacterSS[] ssCharacters = null;

	int numMSCharacters = 0; // msCharacters.length

	CharacterMS[] msCharacters = null;

	int numSMCharacters = 0; // smCharacters.length

	CharacterSM[] smCharacters = null;

	Vector<CandidateCharacter> candidateCharacterVector = new Vector<CandidateCharacter>();

	Vector<CandidateUnderlinedCharacter> candidateUnderlinedCharacterVector = new Vector<CandidateUnderlinedCharacter>();

	Vector<CharacterSS> ssCharacterVector = new Vector<CharacterSS>();

	Vector<CharacterMS> msCharacterVector = new Vector<CharacterMS>();

	public Container(IPageImage _pi, int _ID, ConnectedComponent _cc, int _color) {
		super(CONTAINER_TYPE, _pi);
		cc = _cc;
		color = _color;
		componentID = _ID;
	}

	void ssVector2Array() {
		numSSCharacters = ssCharacterVector.size();
		ssCharacters = new CharacterSS[numSSCharacters];
		for (int k = 0; k < numSSCharacters; k++) {
			ssCharacters[k] = ssCharacterVector.elementAt(k);
		}
		ssCharacterVector.removeAllElements();
	}

	void msVector2Array() {
		numMSCharacters = msCharacterVector.size();
		msCharacters = new CharacterMS[numMSCharacters];
		for (int k = 0; k < numMSCharacters; k++) {
			msCharacters[k] = msCharacterVector.elementAt(k);
		}
		msCharacterVector.removeAllElements();
	}

	public int getColor() {
		return color;
	}

	public int getNumMSCharacters() {
		return numMSCharacters;
	}

	public int getNumSMCharacters() {
		return numSMCharacters;
	}

	public int getNumSSCharacters() {
		return numSSCharacters;
	}

	public CharacterMS[] getMsCharacters() {
		return msCharacters;
	}

	public CharacterSM[] getSmCharacters() {
		return smCharacters;
	}

	public CharacterSS[] getSsCharacters() {
		return ssCharacters;
	}
}
