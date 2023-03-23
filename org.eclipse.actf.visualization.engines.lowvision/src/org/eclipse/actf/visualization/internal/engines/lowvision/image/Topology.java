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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Vector;

import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;

/*
 * Show topology of connected component (thinning, 8 connected)
 */
public class Topology{
    // type of node
    static final short TYPE_INTERIOR = 0; 
    static final short TYPE_EDGE = 1; 
    static final short TYPE_CONNECTED = 2; 
    static final short TYPE_BRANCHING = 3; 
    static final short TYPE_CROSSING = 4; 

    // Left side of 'A' might have 2/3 connected component
    //  #      #
    //  #      #
    //  2##     3##
    // #       #
    // #      #
    //
    // So, use crossing number to detect shape change.
    
    ConnectedComponent component;
    int width;
    int height;
    int numNodes = 0;     // number of nodes (connection 0,1,3,4 ( +2(edge)))
    Node[] nodes = null;  // node array (connection 0,1,3,4)
    int numInterior = 0;  
    int numEdges = 0;     
    int numBranches = 0;  
    int numCrosses = 0;   
    int[][] distance = null;     
    int count = 0; // number of fg pixel

    public Topology( ConnectedComponent _cc ) throws ImageException{
    	this( _cc, false );
    }

    public Topology( ConnectedComponent _cc, boolean _calcDistance ) throws ImageException{
		component = _cc;
		width = component.shape.width;
		height = component.shape.height;
		BinaryImage bi = component.shape;
	
		Vector<Node> tmpNodes = new Vector<Node>();
		int topPosition = -1;    
		int bottomPosition = -1; 
		for( int j=0; j<height; j++ ){
		    for( int i=0; i<width; i++ ){
				if( bi.data[j][i] != 0 ){
					count++; 
				    if( topPosition == -1 ) 
						topPosition = j;
				    bottomPosition = j;
				    Neighbor nei = new Neighbor( bi, i, j );
				    // int cn = nei.connectivityNumber8(); 
					int cn = nei.crossingNumber(); 
				    if( cn == 1 ){
						numNodes++;
						numEdges++;
						Node n = new Node( i, j, TYPE_EDGE );
						tmpNodes.addElement( n );
				    }
				    else if( cn == 3 ){
						numNodes++;
						numBranches++;
						Node n = new Node( i, j, TYPE_BRANCHING );
						tmpNodes.addElement( n );
				    }
				    else if( cn == 4 ){
						numNodes++;
						numCrosses++;
						Node n = new Node( i, j, TYPE_CROSSING );
						tmpNodes.addElement( n );
				    }
				    else if( cn == 0 ){ 
						numNodes++;
						numInterior++;
						Node n = new Node( i, j, TYPE_INTERIOR );
						tmpNodes.addElement( n );
				    }
				}
		    }
		}

		if( topPosition == -1 ){
			return;
		}
	
		// add top/bottom node (to support chars like 'O')
		boolean existTop = false;
		boolean existBottom = false;
		for( int i=0; i<numNodes; i++ ){
		    Node n = (tmpNodes.elementAt(i));
		    if( n.y == topPosition )
				existTop = true;
		    if( n.y == bottomPosition )
				existBottom = true;
		}
		Node topNode = null;
		Node bottomNode = null;
		if( !existTop ){
		    for( int i=0; i<width; i++ ){
				if( bi.data[topPosition][i] != 0 ){
				    Neighbor nei = new Neighbor( bi, i, topPosition );
				    // int cn = nei.connectivityNumber8();
					int cn = nei.crossingNumber();
				    if( cn == 2 ){
						numNodes++;
						topNode = new Node( i, topPosition, TYPE_CONNECTED );
						break;
				    }
				    else{
						throw new ImageException( "Unknown type" ); //$NON-NLS-1$
				    }
				}
		    }
		}
		if( !existBottom ){
		    for( int i=width-1; i>=0; i-- ){
				if( bi.data[bottomPosition][i] != 0 ){
				    Neighbor nei = new Neighbor( bi, i, bottomPosition );
				    // int cn = nei.connectivityNumber8();
					int cn = nei.crossingNumber();
				    if( cn == 2 ){
						numNodes++;
						bottomNode = new Node( i, bottomPosition, TYPE_CONNECTED );
						break;
				    }
				    else{
						throw new ImageException( "Unknown type" ); //$NON-NLS-1$
				    }
				}
		    }
		}
	
		nodes = new Node[numNodes];
		int offset = 0;
		if( topNode != null ){
		    nodes[0] = topNode;
		    offset = 1;
		}
		for( int i=0; i<tmpNodes.size(); i++ )
		    nodes[i+offset] = (tmpNodes.elementAt(i));
		if( bottomNode != null )
		    nodes[numNodes-1] = bottomNode;
	
		if( _calcDistance ){
			distance = new int[numNodes][numNodes];
			for( int i=0; i<numNodes-1; i++ ){
				int[][] map = getDistanceMap( new Coord( nodes[i].x, nodes[i].y ) );
				for( int j=i+1; j<numNodes; j++ ){
					distance[i][j] = map[nodes[j].y][nodes[j].x];
					distance[j][i] = distance[i][j];
				}
			}
		}
    }

	public static boolean match( Topology _t1, Topology _t2 ){
		if( _t1.numInterior != _t2.numInterior ){
			return( false );
		}
		if( _t1.numEdges != _t2.numEdges ){
			return( false );
		}
		if( _t1.numBranches != _t2.numBranches ){
			return( false );
		}
		if( _t1.numCrosses != _t2.numCrosses ){
			return( false );
		}
		return( true );
	}
	
	public boolean match( Topology _t ){
		return( match( this, _t ) );
	}

    private int[][] getDistanceMap( Coord _co ){
    	//TODO check component != null
		int[][] map = new int[height][width];
		BinaryImage bi = component.shape;
		Vector<Coord> currentNodes = null;
		Vector<Coord> nextNodes = new Vector<Coord>();
		nextNodes.addElement( _co );
	
		int currentDistance = 1;
		int size = nextNodes.size();
		while( size > 0 ){
		    currentNodes = nextNodes;
		    nextNodes = new Vector<Coord>();
	
		    for( int i=0; i<size; i++ ){
				Coord co = (currentNodes.elementAt(i));
				int x = co.x;
				int y = co.y;
				if( x>0 && bi.data[y][x-1] != 0 ){ 
				    checkAndSetDistance( x-1, y, map, currentDistance, nextNodes );
				}
				if( y>0 && bi.data[y-1][x] != 0 ){ 
				    checkAndSetDistance( x, y-1, map, currentDistance, nextNodes );
				}
				if( x<width-1 && bi.data[y][x+1] != 0 ){ 
				    checkAndSetDistance( x+1, y, map, currentDistance, nextNodes );
				}
				if( y<height-1 && bi.data[y+1][x] != 0 ){ 
				    checkAndSetDistance( x, y+1, map, currentDistance, nextNodes );
				}
				if( x>0 && y>0 && bi.data[y-1][x-1] != 0 ){ 
				    checkAndSetDistance( x-1, y-1, map, currentDistance, nextNodes );
				}
				if( x<width-1 && y>0 && bi.data[y-1][x+1] != 0 ){ 
				    checkAndSetDistance( x+1, y-1, map, currentDistance, nextNodes );
				}
				if( x>0 && y<height-1 && bi.data[y+1][x-1] != 0 ){ 
				    checkAndSetDistance( x-1, y+1, map, currentDistance, nextNodes );
				}
				if( x<width-1 && y<height-1 && bi.data[y+1][x+1] != 0 ){ 
				    checkAndSetDistance( x+1, y+1, map, currentDistance, nextNodes );
				}
		    } 
	
		    size = nextNodes.size();
		    currentDistance++;
		} 
		return( map );
    } 


    private void checkAndSetDistance( int _x, int _y, int[][] _map, int _distance, Vector<Coord> _next ){
		if( _map[_y][_x] == 0 ){
		    _map[_y][_x] = _distance;
		    _next.addElement( new Coord( _x, _y ) );
		}
    }

	public int getNumNodes() {
		return (numNodes);
	}

	public int getNumInterior() {
		return (numInterior);
	}

	public int getNumEdges() {
		return (numEdges);
	}

	public int getNumBranches() {
		return (numBranches);
	}

	public int getNumCrosses() {
		return (numCrosses);
	}

	public int getCount() {
		return (count);
	}


    class Node {
    	//TBD use Coord class?
		int x; 
		int y;
		short type;

		public Node(int _x, int _y, short _type) {
			x = _x;
			y = _y;
			type = _type;
		}
	}

	public void showNodes(PrintStream _ps) {
		PrintWriter pw = new PrintWriter(_ps, true);
		showNodes(pw);
	}

	@SuppressWarnings("nls")
	public void showNodes(PrintWriter _pw) {
		_pw.println("-------------------------------");
		_pw.println("Dumping nodes");
		char[][] cimage = new char[height][width];
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (component.shape.data[j][i] != 0)
					cimage[j][i] = '#';
				else
					cimage[j][i] = '.';
			}
		}
		for (int i = 0; i < numNodes; i++) {
			Node n = nodes[i];
			if (n.type == TYPE_EDGE)
				cimage[n.y][n.x] = '1';
			else if (n.type == TYPE_CONNECTED)
				cimage[n.y][n.x] = '2';
			else if (n.type == TYPE_BRANCHING)
				cimage[n.y][n.x] = '3';
			else if (n.type == TYPE_CROSSING)
				cimage[n.y][n.x] = '4';
			else if (n.type == TYPE_INTERIOR)
				cimage[n.y][n.x] = '0';
		}
		for (int j = 0; j < height; j++) {
			_pw.println(new String(cimage[j]));
		}
		_pw.println("-------------------------------");
	}

	public void showDistances(PrintStream _ps) {
		if (distance == null) {
			return;
		}
		PrintWriter pw = new PrintWriter(_ps, true);
		showDistances(pw);
	}

	@SuppressWarnings("nls")
	public void showDistances(PrintWriter _pw) {
		if (distance == null) {
			return;
		}
		_pw.println("-------------------------------");
		_pw.println("Showing distances");

		StringBuffer top = new StringBuffer();
		top.append("    ");
		StringBuffer bar = new StringBuffer();
		bar.append("----");
		for (int i = 0; i < numNodes; i++) {
			top.append("|" + numberFormat4(i));
			bar.append("+----");
		}
		_pw.println(top.toString());
		_pw.println(bar.toString());
		for (int i = 0; i < numNodes; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append(numberFormat4(i) + "|");
			for (int j = 0; j < numNodes - 1; j++) {
				sb.append(numberFormat4(distance[i][j]) + "|");
			}
			sb.append(numberFormat4(distance[i][numNodes - 1]));
			_pw.println(sb.toString());
		}
		_pw.println("-------------------------------");
	}

	@SuppressWarnings("nls")
	private String numberFormat4(int _i) {
		if (0 <= _i && _i <= 9)
			return ("   " + _i);
		else if (10 <= _i && _i <= 99)
			return ("  " + _i);
		else if (100 <= _i && _i <= 999)
			return (" " + _i);
		else
			return ("" + _i);
	}
}
