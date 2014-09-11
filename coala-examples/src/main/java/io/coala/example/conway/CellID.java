/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/conway/CellID.java $
 * 
 * Part of the EU project Adapt4EE, see http://www.adapt4ee.eu/
 * 
 * @license
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Copyright (c) 2010-2013 Almende B.V. 
 */
package io.coala.example.conway;

import io.coala.agent.AgentID;
import io.coala.model.ModelID;
import io.coala.name.AbstractIdentifier;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * {@link CellID}
 * 
 * @date $Date: 2014-06-03 13:55:16 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 295 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class CellID extends AgentID
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static final String CELL_ID_FORMAT = "cell_%d_%d";

	/** */
	private int row;

	/** */
	private int col;

	/**
	 * {@link CellID} constructor
	 * 
	 * @param modelID
	 * @param row the {@link Cell}'s row in the lattice
	 * @param col the {@link Cell}'s column in the lattice
	 */
	@AssistedInject
	public CellID(final ModelID modelID, @Assisted final int row,
			@Assisted final int col)
	{
		super(modelID, String.format(CELL_ID_FORMAT, row, col));
		this.row = row;
		this.col = col;
	}

	public int getRow()
	{
		return this.row;
	}

	public int getCol()
	{
		return this.col;
	}

	/** @see AbstractIdentifier#compareTo(AbstractIdentifier) */
	@Override
	public int compareTo(final AbstractIdentifier<String> other)
	{
		if (other instanceof CellID)
		{
			final CellID that = (CellID) other;
			final int rowCompare = Integer.compare(getRow(), that.getRow());
			if (rowCompare != 0)
				return rowCompare;
			return Integer.compare(getCol(), that.getCol());
		}
		return super.compareTo(other);
	}

	@Override
	public String toString()
	{
		return String.format("%s[%02d,%02d]", getModelID(), getRow(), getCol());
	}

}